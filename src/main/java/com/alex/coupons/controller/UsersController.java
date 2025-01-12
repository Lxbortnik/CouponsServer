package com.alex.coupons.controller;


import com.alex.coupons.dto.User;
import com.alex.coupons.dto.UserLogin;
import com.alex.coupons.dto.UserLoginData;
import com.alex.coupons.entities.UserEntity;
import com.alex.coupons.exceptions.ServerException;
import com.alex.coupons.logic.UserLogic;
import com.alex.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
    private UserLogic userLogic;
    private UserLoginData userLoginData;


    @Autowired
    public UsersController(UserLogic userLogic) {
        this.userLogic = userLogic;
    }


    @PostMapping // checked
    public UserEntity createUserOfTypeCustomer(@RequestBody User user) throws ServerException {
        return this.userLogic.createCustomer(user);
    }

    @PostMapping("/createbyadmin") // checked
    public UserEntity createUserOfAnyType(@RequestHeader("Authorization") String token, @RequestBody User user) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.userLogic.createUserByAdmin(user, userLogin.getUserType());
    }

    @PutMapping // checked
    public void updateUser(@RequestBody User user, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        User userFromDB = userLogic.getUserById(userLogin.getId());// check if it's relevant
        user.setUsername(userFromDB.getUsername());
        user.setId(userLogin.getId());
        user.setUserType(userLogin.getUserType());
        user.setCompanyId((userLogin.getCompanyId()));
        this.userLogic.updateUser(user);

    }

    @DeleteMapping("/deleteMyUser")//checked
    public void deleteMyUser(@RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        userLogic.deleteMyUser(userLogin.getId(), userLogin.getUserType());
    }

    @DeleteMapping("deleteuserbyadmin/{id}")// checked
    public void deleteUserByAdmin(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        userLogic.deleteUserByAdmin(id, userLogin.getUserType());
    }

//find user by user name - only admin can search - do i need it?
   /* :ToDo @GetMapping("/myUser")

    public User getUser(@RequestHeader("Authorization") String token) throws Exception {
        userLogin = JWTUtils.decodeJWT(token);
        return this.userLogic.getUserById(userLogin.getId());
    }*/

    @GetMapping //checked
    public Page<User> getListOfUsers(@RequestHeader("Authorization") String token,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return userLogic.getListOfUsers(userLogin.getUserType(), page, size);
    }

    @GetMapping("/{id}") // checked
    public User getUserById(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return userLogic.getUserById(id, userLogin.getUserType());
    }


    @GetMapping("/bycompanyid") // checked
    public Page<User> getListOfUsersForAdminByCompanyId(@RequestHeader("Authorization") String token,
                                                        @RequestParam("companyId") int companyId,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return userLogic.getListOfUsersForAdminByCompanyId(companyId, userLogin.getUserType(), page, size);

    }

   /* @GetMapping("/bycompanyidforcompany")
    public Page<User> getListOfUsersForCompanyByCompanyId(@RequestHeader("Authorization") String token,
                                                          @RequestParam("companyId") int companyId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return userLogic.getListOfUsersForCompanyByCompanyId(companyId, userLogin.getUserType(), page, size);
    }*/
   @PostMapping("/login")
   public String login(@RequestBody UserLoginData loginDetailsData) throws Exception {
       return userLogic.login(loginDetailsData);


   }
}
