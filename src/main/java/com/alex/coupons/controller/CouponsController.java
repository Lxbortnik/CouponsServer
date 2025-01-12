package com.alex.coupons.controller;

import com.alex.coupons.dto.Coupon;
import com.alex.coupons.dto.CouponDetails;
import com.alex.coupons.dto.UserLogin;
import com.alex.coupons.exceptions.ServerException;
import com.alex.coupons.logic.CouponLogic;
import com.alex.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponsController {
    private CouponLogic couponLogic;
    private UserLogin userLogin;
    // dif var in each function

//:ToDo remove from all const in controllers userLogin

    @Autowired
    public CouponsController(CouponLogic couponLogic) {
        this.couponLogic = couponLogic;
    }

    @PostMapping // checked
    public void createCouponByCompany(@RequestBody Coupon coupon, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        coupon.setCompanyId(userLogin.getCompanyId());
        this.couponLogic.createCouponByCompany(coupon, userLogin.getUserType());

        //:Todo add validation to not add coupon after end date
    }

    @PostMapping("/byadmin") // checked
    public void createCouponByAdmin(@RequestBody Coupon coupon, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.couponLogic.createCouponByAdmin(coupon, userLogin.getUserType());
    }

    @PutMapping // checked
    public void updateCouponByCompany(@RequestBody Coupon coupon, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.couponLogic.updateCouponByCompany(coupon, userLogin.getUserType());
    }

    @PutMapping("/byadmin") // checked
    public void updateCouponByAdmin(@RequestBody Coupon coupon, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.couponLogic.updateCouponByAdmin(coupon, userLogin.getUserType());
    }

    @DeleteMapping("/Admin/{id}") // checked
    public void deleteCouponByAdmin(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        userLogin = JWTUtils.decodeJWT(token);
        this.couponLogic.deleteCouponByAdmin(id, userLogin.getUserType());
    }

    @DeleteMapping("/{id}") // checked
    public void deleteCouponByUserCompany(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        userLogin = JWTUtils.decodeJWT(token);
        this.couponLogic.deleteCouponByCompany(id, userLogin);
    }

   /* @GetMapping("/{id}") // checked
    public Coupon getCouponById(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        this.userLogin = JWTUtils.decodeJWT(token);
        return this.couponLogic.getCouponById(id, userLogin.getUserType());
    }
*/
/*
    @GetMapping ("/coupondetails")
    public CouponDetails getCouponById(@RequestParam("id") int id) throws ServerException {
        return this.couponLogic.getCouponDetailsByCouponId(id);
    }
*/

    @GetMapping("{id}")
    public CouponDetails getCoupon(@PathVariable("id") int id) throws ServerException {
        return this.couponLogic.getCouponDetailsByCouponId(id);
    }

    @GetMapping // checked
    public Page<CouponDetails> getListOfCoupons(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return this.couponLogic.getListOfCoupons(page, size);
    }

    @GetMapping("/bycompanyname") // checked
    public Page<CouponDetails> getListOfCouponsByCompanyName(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam("companyName") String companyName) throws ServerException {
        return this.couponLogic.getCouponsByCompanyName(page, size, companyName);
    }

    @GetMapping("/bycategoryname") // checked
    public Page<CouponDetails> findCouponsByCategoryName(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam("categoryName") String categoryName) throws ServerException {
        return this.couponLogic.getCouponsByCategoryName(page, size, categoryName);
    }

    @GetMapping("/getlistbytitle")
    public Page<CouponDetails> getListOfCouponsByTitle(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam("title") String title) throws ServerException {
        return this.couponLogic.getListOfCouponsByTitle(page, size, title);
    }


    @GetMapping("/bycompanyid")
    public Page<Coupon> getListOfCouponsByCompanyId(@RequestHeader("Authorization") String token,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.couponLogic.getCouponsByCompanyId(page, size, userLogin.getCompanyId());
}
}
