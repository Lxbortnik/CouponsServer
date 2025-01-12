package com.alex.coupons.enums;
//:ToDo - update all http errors
public enum ErrorType {
    GENERAL_ERROR(400,1000, "A general error has occurred, please try again later"),
    UNAUTHORIZED(401,1004, "Invalid sing in information"),

    INVALID_USER_NAME(400,2001, "UserName must be contains between 1 - 45 characters "),
    INVALID_PASSWORD(400,2002, "Password is not correct"),
    INVALID_USER_TYPE(400,2003, "Type must be Customer/Company/Admin"),
    USER_NAME_ALREADY_EXIST(400,2004, "Username already exist, please use another one"),
    USER_DO_NOT_EXIST(400,2005, "User id is not exist"),

    INVALID_COMPANY_ID(400,3001, "Invalid company ID"), //  suppose to be in Users Logic??

    INVALID_PURCHASE_AMOUNT(400,4001, "Invalid purchase ID"),


    INVALID_NAME(400,5001, "Name must contain between 1 - 45 characters"),
    INVALID_PHONE(400,5002, "Invalid phone number, must contain between 9 and 45 characters"),
    INVALID_ADDRESS(400,5003, "The  address  must contain up to 45 characters"),

    INVALID_COUPON_TITLE(400,6001, " Invalid coupon title "),
    INVALID_COUPON_DESCRIPTION(400,6002, "Invalid coupon description"),
    INVALID_COUPON_TYPE(400,6003, "Invalid coupon type"),// why i didn't use it?
    INVALID_COUPON_DATE(400,6004, "Invalid coupon date"),
    INVALID_COUPON_END_DATE(400,6005, "Start date must be earlier than the end date"),
    INVALID_COUPON_AMOUNT(400,6006, "Coupon amount is not valid"),
    INVALID_COUPON_PRICE(400,6007, "Price have to be grater than 0"),
    INVALID_COUPON_URL_IMAGE(400,6008, "ImageURL must contain less than 100 characters"),

    INVALID_CATEGORY_NAME(400,7001, "Please enter valid name"),
    INVALID_CATEGORY_ID(400,7002, "Wrong category id");

    private int internalError;
    private String clientErrorMessage;
    private int httpStatus;

    ErrorType(int httpStatus, int internalError, String clientErrorMessage) {
        this.internalError = internalError;
        this.httpStatus = httpStatus;
        this.clientErrorMessage = clientErrorMessage;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public int getInternalError() {
        return internalError;
    }

    public String getClientErrorMessage() {
        return clientErrorMessage;
    }
}

/*
UPDATE_ERROR(1001, "Updating failed, no rows affected"),
DELETE_ERROR(1003, "Deleting failed, no rows affected"),
INVALID_PURCHASE_TIMESTAMP(4002, "Invalid purchase date??"), // not sure needed??
NOT_AUTHORIZED_UPDATE(401,2006, "Not authorized update"),
*/
