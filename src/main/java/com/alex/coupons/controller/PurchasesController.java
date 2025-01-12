package com.alex.coupons.controller;


import com.alex.coupons.dto.Purchase;
import com.alex.coupons.dto.PurchaseDetails;
import com.alex.coupons.dto.UserLogin;
import com.alex.coupons.logic.PurchaseLogic;
import com.alex.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchasesController {

    private PurchaseLogic purchaseLogic;


    @Autowired
    public PurchasesController(PurchaseLogic purchaseLogic) {
        this.purchaseLogic = purchaseLogic;
    }


    @PostMapping // checked

    //: ToDo how to receive in a body in API the message with a problem(like not enough coupons available)?
    public void createPurchase(@RequestBody Purchase purchase, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.purchaseLogic.createPurchase(purchase, userLogin.getUserType());
    }


    @GetMapping("/bycustomer") // checked
    public Page<PurchaseDetails> getPurchaseDetailsByCustomer(@RequestHeader("Authorization") String token,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.purchaseLogic.getPurchaseDetailsByCustomer(userLogin.getId(), page, size);
    }

    @GetMapping("/{id}") // checked
    public PurchaseDetails getPurchaseDetailsByPurchaseId(@PathVariable("id") int purchaseId, @RequestHeader("Authorization") String token) throws Exception {
       UserLogin userLogin = JWTUtils.decodeJWT(token);
        return purchaseLogic.getPurchaseDetailsByPurchaseId(purchaseId, userLogin.getId());
    }

    @GetMapping // checked
    public Page<PurchaseDetails> getPurchasesListByAdmin(@RequestHeader("Authorization") String token,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.purchaseLogic.getPurchasesListByAdmin(userLogin.getUserType(), page, size);
    }

    @DeleteMapping("/{id}")
    public void deletePurchaseByPurchaseId(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
     UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.purchaseLogic.deletePurchase(id, userLogin.getUserType());
    }

}
