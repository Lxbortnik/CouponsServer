package com.alex.coupons.logic;

import com.alex.coupons.dal.ICustomerDal;
import com.alex.coupons.dto.Customer;
import com.alex.coupons.dto.CustomerDetails;
import com.alex.coupons.dto.UserLogin;
import com.alex.coupons.entities.CustomerEntity;
import com.alex.coupons.entities.UserEntity;
import com.alex.coupons.enums.ErrorType;
import com.alex.coupons.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.isEmpty;

@Service
public class CustomerLogic {

    private ICustomerDal customerDal;
    private UserLogic userLogic;

    @Autowired

    public CustomerLogic(ICustomerDal customerDal, UserLogic userLogic) {
        this.customerDal = customerDal;
        this.userLogic = userLogic;
    }

    //:ToDo Why program isn't working when I'm adding userLogin to controller const - explain
    public void createCustomer(Customer customer) throws ServerException {
        validateCustomer(customer);
        customer.getUser().setUserType("customer");
        customer.getUser().setCompanyId(null);

        UserEntity user = userLogic.createCustomer(customer.getUser());
        CustomerEntity customerEntity = convertCustomerToCustomerEntity(customer);
        customerEntity.setId(user.getId());
        this.customerDal.save(customerEntity);
    }

    // :Todo add admin update
    public void updateCustomer(Customer customer) throws ServerException {
        validateCustomer(customer);
        CustomerEntity customerEntity = convertCustomerToCustomerEntity(customer);
        this.customerDal.save(customerEntity);
    }

    public void deleteMyCustomer(UserLogin userLogin) throws ServerException {
        this.customerDal.deleteById(userLogin.getId());
    }

    public void deleteCustomerByAdmin(String userType, int customerId) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        customerDal.deleteById(customerId);
    }

    public CustomerDetails getCustomerDetailsById(Integer id) throws ServerException {
        if (!customerDal.existsById(id)) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        return customerDal.getCustomerDetailsById(id);
    }

    public CustomerDetails getCustomerDetailsByAdmin(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE, "you new to do admin from this ");
        }
        return this.customerDal.getCustomerDetailsById(id);
    }

     public Page<CustomerDetails> getCustomersDetails(String userType, int page, int size) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        Pageable pageable = PageRequest.of(page, size);
        return this.customerDal.getCustomersDetails(pageable);
    }


    //:ToDo check if I need 2 diff validations for create and update
    //ToDo and  if validation for "customer" is enough inside the function
   /* private void validateCustomerForUpdate(Customer customer) throws ServerException {
        if (isEmpty(customer.getName())) {
            throw new ServerException(ErrorType.INVALID_USER_NAME);
        }
        if (customer.getName().length() > 45) {
            throw new ServerException(ErrorType.INVALID_USER_NAME);
        }
        if (nonNull(customer.getPhone()) && (customer.getPhone().length() < 9 && customer.getPhone().length() > 45)) {
            throw new ServerException(ErrorType.INVALID_PHONE);
        }
        if (nonNull(customer.getAddress()) && customer.getAddress().length() > 45) {
            throw new ServerException(ErrorType.INVALID_ADDRESS);
        }
    }
*/

    private CustomerEntity convertCustomerToCustomerEntity(Customer customer) {
        CustomerEntity customerEntity = new CustomerEntity(customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone());
        return customerEntity;
    }// wrong function for create or update???

    private void validateCustomer(Customer customer) throws ServerException {
        if (isEmpty(customer.getName())) {
            throw new ServerException(ErrorType.INVALID_USER_NAME);
        }
        if (customer.getName().length() > 45) {
            throw new ServerException(ErrorType.INVALID_USER_NAME);
        }

        //ToDo null in address or phone throw 500
        if (nonNull(customer.getPhone()) && (customer.getPhone().length() < 9 && customer.getPhone().length() > 45)) {
            throw new ServerException(ErrorType.INVALID_PHONE);
        }
        if (nonNull(customer.getAddress()) && customer.getAddress().length() > 45) {
            throw new ServerException(ErrorType.INVALID_ADDRESS);
        }
    /*    if (!customer.getUser().getUserType().equals("customer")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }*/
    }

}
