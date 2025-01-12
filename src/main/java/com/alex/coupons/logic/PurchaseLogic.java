package com.alex.coupons.logic;

import com.alex.coupons.dal.IPurchaseDal;
import com.alex.coupons.dto.Coupon;
import com.alex.coupons.dto.Purchase;
import com.alex.coupons.dto.PurchaseDetails;
import com.alex.coupons.entities.PurchaseEntity;
import com.alex.coupons.enums.ErrorType;
import com.alex.coupons.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class PurchaseLogic {

    private IPurchaseDal purchaseDal;
    private CouponLogic couponLogic;
    private CustomerLogic customerLogic;


    @Autowired
    public PurchaseLogic(IPurchaseDal purchaseDal, CouponLogic couponLogic, CustomerLogic customerLogic) {
        this.purchaseDal = purchaseDal;
        this.couponLogic = couponLogic;
        this.customerLogic = customerLogic;
    }

    /*:ToDo: I think that u suppose to still have an option to get to purchases even after deleting customer or company (as a part of UI)
     *how to get it done, is both of them are FK --> they can't be nullable
     */
    //Todo: make boolean active/not active, so when I'm  "deleting" company/user i still can have access to coupons/purchases (FK working cause it's the same)

    // @Transactional
    public void createPurchase(Purchase purchase, String userType) throws ServerException {// RuntimeException
        if (!userType.equals("customer")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        validatePurchase(purchase);
        PurchaseEntity purchaseEntity = convertPurchaseToPurchaseEntity(purchase);
        purchaseDal.save(purchaseEntity);
        this.couponLogic.decreaseCouponAmount(purchase.getAmount(), purchase.getCouponId());


    }

    //:ToDo How to show function
//:ToDo read about optional  and or else throw
    public PurchaseDetails getPurchaseDetailsByPurchaseId(int purchaseId, int userId) throws ServerException {
/*
        Optional<PurchaseEntity> purchaseDetails = purchaseDal.findById(purchaseId);
        PurchaseEntity purchaseEntity = purchaseDetails.orElseThrow(() -> new ServerException(ErrorType.USER_DO_NOT_EXIST));
        if (purchaseEntity.getCustomer().getId() != userId) {
            throw new ServerException(ErrorType.UNAUTHORIZED);
        }
        return purchaseDal.getPurchaseDetailsById(purchaseId);
*/

/*
        Optional<PurchaseEntity> byId = purchaseDal.findById(purchaseId);
        if (!byId.isPresent() || byId.get().getCustomer().getId() != userId){
            throw new ServerException(ErrorType.UNAUTHORIZED);
        }
*/
// the right one
        PurchaseEntity purchaseEntity = purchaseDal.findById(purchaseId).get();
        if (purchaseEntity.getCustomer().getId() != userId) {
            throw new ServerException(ErrorType.UNAUTHORIZED);
        }
        return purchaseDal.getPurchaseDetailsById(purchaseId);
    }

    public Page<PurchaseDetails> getPurchaseDetailsByCustomer(int customerId, int page, int size) throws ServerException {
        Pageable pageable = PageRequest.of(page, size);
        return this.purchaseDal.getPurchasesByCustomerId(pageable, customerId);
    }

    /*public PurchaseDetails getPurchaseDetailsByCouponId(int purchaseId, int userId) throws ServerException {
        PurchaseEntity purchaseEntity = purchaseDal.findById(cou).get();
        if(purchaseEntity.getCustomer().getId() != userId){
            throw new ServerException(ErrorType.UNAUTHORIZED);
        }
        return purchaseDal.getPurchaseDetailsById(purchaseId);
    }*/

    public Page<PurchaseDetails> getPurchasesListByAdmin(String userType, int page, int size) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        Pageable pageable = PageRequest.of(page, size);
        return this.purchaseDal.getPurchasesListByAdmin(pageable);
    }

    private Purchase getPurchaseByIdForDelete(int purchaseId) {

        PurchaseEntity purchasesEntity = this.purchaseDal.findById(purchaseId).get();
        Purchase purchase = convertPurchaseEntityToPurchase(purchasesEntity);
        return purchase;
    }

    private Purchase convertPurchaseEntityToPurchase(PurchaseEntity purchasesEntity) {
        Purchase purchase = new Purchase(purchasesEntity.getId(),
                purchasesEntity.getCustomer().getId(),
                purchasesEntity.getCoupon().getId(),
                purchasesEntity.getAmount(),
                purchasesEntity.getTimestamp());
        return purchase;
    }

    @Transactional
    public void deletePurchase(int purchaseId, String userType) throws ServerException, RuntimeException {
        Purchase purchase = getPurchaseByIdForDelete(purchaseId);
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        this.couponLogic.increaseCouponAmount(purchase.getAmount(), purchase.getCouponId());
        this.purchaseDal.deleteById(purchaseId);
    }

    private PurchaseEntity convertPurchaseToPurchaseEntity(Purchase purchase) {
        PurchaseEntity purchaseEntity = new PurchaseEntity(
                purchase.getCustomerId(),
                purchase.getCouponId(),
                purchase.getAmount(),
                purchase.getTimestamp());
        return purchaseEntity;
    }

    private void validatePurchase(Purchase purchase) throws ServerException {
        // Using the internal public method (no user type needed)
        Coupon coupon = couponLogic.getCouponByIdForInternal(purchase.getCouponId());

        if (purchase.getAmount() < 1) {
            throw new ServerException(ErrorType.INVALID_PURCHASE_AMOUNT);
        }

        if (purchase.getAmount() > coupon.getAmount()) {
            throw new ServerException(ErrorType.INVALID_COUPON_AMOUNT);
        }

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        // Convert Date to Timestamp if needed
        Timestamp endDate = new Timestamp(coupon.getEndDate().getTime());
        if (currentTime.after(endDate)) {
            throw new ServerException(ErrorType.GENERAL_ERROR, "COUPON_EXPIRED");
        }
    }
    // amount <1
    //amount cant be more than a coupons amount
    //coupon do not exist
    //timestamp after end date of coupon
    //

}
