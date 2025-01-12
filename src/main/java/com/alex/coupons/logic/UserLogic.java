package com.alex.coupons.logic;

import com.alex.coupons.dal.IUserDal;
import com.alex.coupons.dto.User;
import com.alex.coupons.dto.UserLogin;
import com.alex.coupons.dto.UserLoginData;
import com.alex.coupons.entities.UserEntity;
import com.alex.coupons.enums.ErrorType;
import com.alex.coupons.exceptions.ServerException;
import com.alex.coupons.utils.JWTUtils;
import com.alex.coupons.utils.StatisticsUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserLogic {
    private IUserDal userDal;
    private UserLogin userLogin;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,}|co\\.il)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    public UserLogic(IUserDal userDal) {
        this.userDal = userDal;
    }

    public String login(UserLoginData userLoginData) throws ServerException, JsonProcessingException {
        UserEntity usersEntity = userDal.login(userLoginData.getUsername(), userLoginData.getPassword());
        if (usersEntity == null) {
            throw new ServerException(ErrorType.UNAUTHORIZED);
        }
//What happened if I have a wrong password or username

        StatisticsUtils.sendStatistics(userLoginData.getUsername(), "login");//if (get.company = null - put null)check that it's Integer
        Integer companyOfTheUser = (usersEntity.getCompany() != null) ? usersEntity.getCompany().getId() : null;
        UserLogin userLogin = new UserLogin(usersEntity.getId(), usersEntity.getUserType(), companyOfTheUser); // write with if

        /* UserLogin userLoginDetails = new UserLogin(usersEntity.getId(), usersEntity.getUserType(), usersEntity.getCompany().getId());*/ // company id will be null for customer
        String token = JWTUtils.createJWT(userLogin);
        return token;
    }

    public void updateUser(User user) throws ServerException {
        validateUserForUpdate(user);
        UserEntity userNameBeforeUpdate = this.userDal.findById(user.getId()).get();

        if (userNameBeforeUpdate == null) {
            throw new ServerException(ErrorType.USER_DO_NOT_EXIST);
        }
        //If the username is different from what it was before, it still must remain unique.
        if (!userNameBeforeUpdate.getUsername().equals(user.getUsername())) {
            if (userDal.isUserNameNotUnique(user.getUsername())) {
                throw new ServerException(ErrorType.INVALID_USER_NAME, user.toString());
            }
        }
        UserEntity userEntity = convertUserToUserEntity(user);
        userDal.save(userEntity);
    }

    private void validateUserForUpdate(User user) throws ServerException {
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new ServerException(ErrorType.INVALID_PASSWORD);
        }
    }

    //:ToDo read about transactional & Runtime exception

    //:ToDo  check from David's code logic and controller for creating user and customer
    //:ToDo - double coding in Create Customer + create User By Admin - new f "Create User"  with validation for customer and admin
    
    @Transactional
    public UserEntity createCustomer(User user) throws ServerException, RuntimeException {
        validateUser(user);
        if (!user.getUserType().equals("customer")){
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        if (!isUserNameNotUnique(user)) {
            throw new ServerException(ErrorType.USER_NAME_ALREADY_EXIST);
        }
        //user.setUserType("customer");
        UserEntity usersEntity = convertUserToUserEntity(user);
        return this.userDal.save(usersEntity);
    }

    // f creating any type of users
    public UserEntity createUserByAdmin (User user, String userType) throws ServerException {
        if (!userType.equals("admin")){
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        validateUser(user);
        if (!isUserNameNotUnique(user)) {
            throw new ServerException(ErrorType.USER_NAME_ALREADY_EXIST);
        }
        UserEntity usersEntity = convertUserToUserEntity(user);
        return this.userDal.save(usersEntity);
    }


    // add validation, that only admin can create admin
    public void deleteMyUser(int id, String userType) throws ServerException {

        if (userType.equals("company")) {
            throw new ServerException(ErrorType.GENERAL_ERROR, "invalid user type");
        }
        userDal.deleteById(id);
    }

    public void deleteUserByAdmin(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.GENERAL_ERROR, "No Authorization");
        }
        userDal.deleteById(id);
    }
    // no option for deleting user by company - only admin


    /*:Todo:
     *   how can i add another validation if user.equals "company" can see the list of employees only in the same company
     *   is it logical to give any employee in a company to see the list of employees in a same company
     * */


    //:ToDo List of users by company ID
    public Page<User> getListOfUsersForAdminByCompanyId(int companyId, String userType, int page, int size) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        Pageable pageable = PageRequest.of(page,size);
        return this.userDal.findUsersByCompanyId(pageable,companyId);
    }

    //Todo does it need to be user or user entities list?

    //:ToDo check if it's work
    public Page<User> getListOfUsersForCompanyByCompanyId(int companyId, String userType, int page, int size) throws ServerException {
        if (!userType.equals("company")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        Pageable pageable = PageRequest.of(page,size);
        return this.userDal.findUsersByCompanyId(pageable,companyId);
    }

    public Page<User> getListOfUsers(String userType, int page, int size) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        Pageable pageable = PageRequest.of(page, size);
        return this.userDal.getAllUsers(pageable);
    }

    private boolean isUserNameNotUnique(User user) {
        UserEntity userToCheck = userDal.findByUserName(user.getUsername());
        if (userToCheck == null) {
            return true;
        }
        return false;

    }

    private UserEntity convertUserToUserEntity(User user) {
        UserEntity userEntity = new UserEntity(user.getId(),
                user.getUsername(),
                 user.getPassword(),
                user.getUserType(),
                user.getCompanyId());
        return userEntity;
    }

    private void validateUser(User user) throws ServerException {
        if (user.getPassword() == null || user.getPassword().length() > 45) {
            throw new ServerException(ErrorType.UNAUTHORIZED, user.toString());
        }

        if (user.getUsername() == null || user.getUsername().length() < 5 || user.getUsername().length() > 45) {
            throw new ServerException(ErrorType.UNAUTHORIZED, user.toString());
        }

        if (user.getUserType() == null ||
                !(user.getUserType().equals("customer") ||
                        user.getUserType().equals("company") ||
                        user.getUserType().equals("admin"))) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE, "Invalid user type: " + user.getUserType());
        }

        if (user.getUserType().equals("company") && (user.getCompanyId() == null || user.getCompanyId() < 1)) {
            throw new ServerException(ErrorType.INVALID_COMPANY_ID, user.toString());
        }

        //:Todo add validation that any type that is not a company company id is 100% null

        if (!isValidateEmail(user.getUsername())) {
            throw new ServerException(ErrorType.UNAUTHORIZED, user.toString());
        }

    }

    private boolean isValidateEmail(String userName) {
        Matcher matcher = UserLogic.EMAIL_PATTERN.matcher(userName);
        return matcher.matches();
    }

    public User getUserById(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        if (!userDal.existsById(id)) {
            throw new ServerException(ErrorType.USER_DO_NOT_EXIST);
        }
        UserEntity userEntity = this.userDal.findById(id).get();
        User user = convertUserEntityToUser(userEntity);

        return user;
    }

    private User convertUserEntityToUser(UserEntity userEntity) {

        Integer companyId = null;
        if (userEntity.getCompany() != null) {
            companyId = userEntity.getCompany().getId();
        }
        User user = new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getUserType(),
                companyId);
        return user;
    }

    public User getUserById(Integer id) throws ServerException {
        if (!userDal.existsById(id)) {
            throw new ServerException(ErrorType.USER_DO_NOT_EXIST);
        }
        UserEntity userEntity = this.userDal.findById(id).get();
        User user = convertUserEntityToUser(userEntity);

        return user;


    }

  /*  private List<User> convertUserEntityToUsersList(List<UserEntity> usersEntities) {
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : usersEntities) {
            Integer companyId = null;
            if (userEntity.getCompany() != null) {
                companyId = userEntity.getCompany().getId();  // Assign companyId if company exists
            }
            User user = new User(
                    userEntity.getId(),
                    userEntity.getUsername(),
                    userEntity.getUserType(),
                    companyId              );
            users.add(user);
        }
        return users;
    }*/
}
