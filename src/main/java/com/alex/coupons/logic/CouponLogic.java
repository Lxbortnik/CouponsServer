package com.alex.coupons.logic;

import com.alex.coupons.dal.ICouponDal;
import com.alex.coupons.dto.Coupon;
import com.alex.coupons.dto.CouponDetails;
import com.alex.coupons.dto.UserLogin;
import com.alex.coupons.entities.CouponEntity;
import com.alex.coupons.enums.ErrorType;
import com.alex.coupons.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CouponLogic {

    private ICouponDal couponDal;
    private CategoryLogic categoryLogic;

    @Autowired
    public CouponLogic(ICouponDal couponDal, CategoryLogic categoryLogic) {
        this.couponDal = couponDal;
        this.categoryLogic = categoryLogic;
    }


    public void createCouponByCompany(Coupon coupon, String userType) throws ServerException {
        if (!userType.equals("company")) {
            throw new ServerException(ErrorType.UNAUTHORIZED);
        }
        validateCoupon(coupon);
        CouponEntity couponEntity = convertCouponToCouponEntity(coupon);
        couponDal.save(couponEntity);
    }

    public void createCouponByAdmin(Coupon coupon, String userType) throws Exception {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        validateCoupon(coupon);
        CouponEntity couponsEntity = convertCouponToCouponEntity(coupon);
        this.couponDal.save(couponsEntity);
    }

    public void updateCouponByCompany(Coupon coupon, String userType) throws ServerException {
        if (!userType.equals("company")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        CouponEntity couponsEntity = this.couponDal.findById(coupon.getId()).get();
        if (couponsEntity == null) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        couponsEntity.setDescription(coupon.getDescription());
        couponsEntity.setTitle(coupon.getTitle());
        couponsEntity.setAmount(coupon.getAmount());
        couponsEntity.setPrice(coupon.getPrice());
        couponsEntity.setEndDate(coupon.getEndDate());
        couponsEntity.setStartDate(coupon.getStartDate());
        //couponEntity.setCategory(coupon.getCategoryId());
        couponsEntity.setImageUrl(coupon.getImageUrl());
        validateCoupon(coupon);

        this.couponDal.save(couponsEntity);
//:ToDo add error types
//:ToDo how to add category to edit
//:ToDo block option to update company ID
    }

    public void updateCouponByAdmin(Coupon coupon, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        CouponEntity couponsEntity = this.couponDal.findById(coupon.getId()).get();
        if (couponsEntity == null) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        couponsEntity.setDescription(coupon.getDescription());
        couponsEntity.setTitle(coupon.getTitle());
        couponsEntity.setAmount(coupon.getAmount());
        couponsEntity.setPrice(coupon.getPrice());
        couponsEntity.setEndDate(coupon.getEndDate());
        couponsEntity.setStartDate(coupon.getStartDate());
        //couponEntity.setCategory(coupon.getCategoryId());
        couponsEntity.setImageUrl(coupon.getImageUrl());
        validateCoupon(coupon);

        this.couponDal.save(couponsEntity);

    }

    public void deleteCouponByAdmin(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        this.couponDal.deleteById(id);
    }

    public void deleteCouponByCompany(int id, UserLogin userLogin) throws ServerException {
        if (!userLogin.getUserType().equals("company")) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        CouponEntity couponEntity = couponDal.findById(id).get();

        //checking that the user belongs to the same company whose coupon is being deleted
        if (couponEntity.getCompany().getId() != userLogin.getCompanyId()) {
            throw new ServerException(ErrorType.GENERAL_ERROR,
                    "You are trying to delete a coupon that does not belong to your company");
        }

        this.couponDal.deleteById(id);
    }

    public Coupon getCouponById(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.UNAUTHORIZED);
        }
        if (!this.couponDal.existsById(id)) {
            throw new ServerException(ErrorType.INVALID_COMPANY_ID);
        }
        CouponEntity couponEntity = couponDal.findById(id).get();
        Coupon coupon = convertCouponEntityToCoupon(couponEntity);
        return coupon;
    }

    public Page<CouponDetails> getListOfCoupons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.couponDal.getListOfCoupons(pageable);
    }

    public CouponDetails getCouponDetailsByCouponId (int id) throws ServerException {
        return this.couponDal.getCouponDetailsByCouponId(id);
    }

    public Page<CouponDetails> getCouponsByCompanyName(int page, int size, String companyName) throws ServerException {
        Pageable pageable = PageRequest.of(page, size);
        return this.couponDal.findCouponsByCompanyName(pageable, companyName);
    }

    public Page<CouponDetails> getListOfCouponsByTitle(int page, int size, String title) throws ServerException {
        Pageable pageable = PageRequest.of(page, size);
        return this.couponDal.findCouponsByTitle(pageable, title);
    }

    public Page<CouponDetails> getCouponsByCategoryName(int page, int size, String categoryName) throws ServerException {
        Pageable pageable = PageRequest.of(page, size);
        return couponDal.findCouponsByCategoryName(pageable, categoryName);
    }

    private Coupon convertCouponEntityToCoupon(CouponEntity couponEntity) {
        Coupon coupon = new Coupon(
                couponEntity.getId(),
                couponEntity.getTitle(),
                couponEntity.getDescription(),
                couponEntity.getPrice(),
                couponEntity.getCompany().getId(),
                couponEntity.getCategory().getId(),
                couponEntity.getStartDate(),
                couponEntity.getEndDate(),
                couponEntity.getAmount(),
                couponEntity.getImageUrl());
        return coupon;
    }

    private List<Coupon> convertCouponsEntitiesToCouponsList(List<CouponEntity> couponsEntities) {
        List<Coupon> coupons = new ArrayList<>();
        for (CouponEntity couponsEntity : couponsEntities) {
            Coupon coupon = new Coupon(couponsEntity.getId(), couponsEntity.getTitle(), couponsEntity.getDescription(), couponsEntity.getPrice(), couponsEntity.getCompany().getId(), couponsEntity.getCategory().getId(), couponsEntity.getStartDate(), couponsEntity.getEndDate(), couponsEntity.getAmount(), couponsEntity.getImageUrl());
            coupons.add(coupon);
        }
        return coupons;
    }

    private CouponEntity convertCouponToCouponEntity(Coupon coupon) {
        CouponEntity couponEntity = new CouponEntity(coupon.getId(), coupon.getTitle(), coupon.getDescription(), coupon.getPrice(), coupon.getCategoryId(), coupon.getCompanyId(), coupon.getStartDate(), coupon.getEndDate(), coupon.getAmount(), coupon.getImageUrl());
        return couponEntity;
    }


    private void validateCoupon(Coupon coupon) throws ServerException {
//:ToDo add toString to all validations - check if exseption is enougth


        if (coupon.getAmount() < 1) {
            throw new ServerException(ErrorType.INVALID_COUPON_AMOUNT, coupon.toString());
        }

        if (coupon.getDescription() == null) {
            throw new ServerException(ErrorType.INVALID_COUPON_DESCRIPTION, coupon.toString());
        }

        if (coupon.getDescription().length() > 200) {
            throw new ServerException(ErrorType.INVALID_COUPON_DESCRIPTION, coupon.toString());
        }

        if (coupon.getDescription().length() < 5) {
            throw new ServerException(ErrorType.INVALID_COUPON_DESCRIPTION, coupon.toString());
        }

        if (coupon.getPrice() <= 0) {
            throw new ServerException(ErrorType.INVALID_COUPON_PRICE, coupon.toString());
        }

        if (coupon.getTitle() == null) {
            throw new ServerException(ErrorType.INVALID_COUPON_TITLE, coupon.toString());
        }

        if (coupon.getTitle().length() > 45) {
            throw new ServerException(ErrorType.INVALID_COUPON_TITLE, coupon.toString());
        }

        if (coupon.getTitle().length() < 2) {
            throw new ServerException(ErrorType.INVALID_COUPON_TITLE, coupon.toString());
        }

        if (coupon.getStartDate() == null || coupon.getEndDate() == null) {
            throw new ServerException(ErrorType.INVALID_COUPON_DATE, coupon.toString());
        }

        if (coupon.getEndDate().before(coupon.getStartDate())) {
            throw new ServerException(ErrorType.INVALID_COUPON_END_DATE, coupon.toString());
        }

        if (coupon.getImageUrl() != null && coupon.getImageUrl().length() > 100) {
            throw new ServerException(ErrorType.INVALID_COUPON_URL_IMAGE, coupon.toString());
        }

        if (!categoryLogic.isCategoryIdExists(coupon.getCategoryId())) {
            throw new ServerException(ErrorType.INVALID_CATEGORY_ID, coupon.toString());
        }
    /*    if (!userType.equals("company")) {
            throw new ServerException(ErrorType.UNAUTHORIZED);
        }*/

    }


    // Public method for internal services (no user type validation)
    public Coupon getCouponByIdForInternal(int id) throws ServerException {
        return getInternalCouponById(id);
    }

    //TODo to check
    // Protected method with core logic (for inheritance/same package)
    protected Coupon getInternalCouponById(int id) throws ServerException {
        if (!this.couponDal.existsById(id)) {
            throw new ServerException(ErrorType.GENERAL_ERROR, "Invalid coupon ID");
        }
        CouponEntity couponEntity = couponDal.findById(id).get();
        return convertCouponEntityToCoupon(couponEntity);
    }

    public void deleteExpiredCoupons() throws ServerException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Date currentDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        this.couponDal.deleteExpiredCoupons(currentDate);
    }

    //:ToDo explain those 2f

    void decreaseCouponAmount(int purchaseAmount, int couponId) throws ServerException {
        Coupon coupon = getCouponByIdForInternal(couponId);
        int newAmount = coupon.getAmount() - purchaseAmount;

        coupon.setAmount(newAmount);
        CouponEntity couponEntity = convertCouponToCouponEntity(coupon);
        this.couponDal.save(couponEntity);
    }

    void increaseCouponAmount(int purchaseAmount, int couponId) throws ServerException {
        Coupon coupon = getInternalCouponById(couponId);
        int newAmount = coupon.getAmount() + purchaseAmount;
        coupon.setAmount(newAmount);
        CouponEntity couponEntity = convertCouponToCouponEntity(coupon);
        this.couponDal.save(couponEntity);
    }


    public Page<Coupon> getCouponsByCompanyId(int page, int size, int companyId) {
        Pageable pageable = PageRequest.of(page , size);
        return this.couponDal.findCouponsByCompanyId(pageable,companyId);

}
}
