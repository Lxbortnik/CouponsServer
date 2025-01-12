package com.alex.coupons.controller;

import com.alex.coupons.dto.Customer;
import com.alex.coupons.dto.CustomerDetails;
import com.alex.coupons.dto.UserLogin;
import com.alex.coupons.exceptions.ServerException;
import com.alex.coupons.logic.CustomerLogic;
import com.alex.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomersController {
    private CustomerLogic customerLogic;
    private UserLogin userLogin;

    @Autowired
// ToDo - to ask: why if i'm putting userLogin into constructor , project failing to start?
    public CustomersController(CustomerLogic customerLogic) {
        this.customerLogic = customerLogic;
    }

    @PostMapping  //checked
    public void createCustomer(@RequestBody Customer customer) throws Exception {
        customer.getUser().setUserType("customer");
        customerLogic.createCustomer(customer);
    }

    @PutMapping // checked
    public void updateCustomer(@RequestBody Customer customer, @RequestHeader("Authorization") String token) throws Exception {
        userLogin = JWTUtils.decodeJWT(token);
        customer.setId(userLogin.getId());
        customerLogic.updateCustomer(customer);
    }

    @DeleteMapping // checked
    public void deleteMyCustomer(@RequestHeader("Authorization") String token) throws Exception {
        this.userLogin = JWTUtils.decodeJWT(token);
        this.customerLogic.deleteMyCustomer(this.userLogin);
    }

    @DeleteMapping("/byAdmin/{id}") // checked
    public void deleteCustomerByAdmin(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        this.userLogin = JWTUtils.decodeJWT(token);
        this.customerLogic.deleteCustomerByAdmin(userLogin.getUserType(), id);
    }

    @GetMapping("/myCustomer/{id}") // checked
    public CustomerDetails getCustomerDetailsById(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        userLogin = JWTUtils.decodeJWT(token);
        return this.customerLogic.getCustomerDetailsById(userLogin.getId());
    }

    @GetMapping("/byAdmin/{id}") // checked
    public CustomerDetails getCustomerByAdmin(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        userLogin = JWTUtils.decodeJWT(token);
        return this.customerLogic.getCustomerDetailsByAdmin(id, userLogin.getUserType());
    }

    @GetMapping  // checked
    public Page<CustomerDetails> getCustomersDetails(@RequestHeader("Authorization") String token,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.customerLogic.getCustomersDetails(userLogin.getUserType(), page, size);
    }


}
