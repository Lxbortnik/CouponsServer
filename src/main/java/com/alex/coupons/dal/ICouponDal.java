package com.alex.coupons.dal;

import com.alex.coupons.dto.Coupon;
import com.alex.coupons.dto.CouponDetails;
import com.alex.coupons.entities.CompanyEntity;
import com.alex.coupons.entities.CouponEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ICouponDal extends JpaRepository<CouponEntity, Integer> {

    @Query("SELECT new com.alex.coupons.dto.CouponDetails(c.id, c.title, c.description," +
            "c.price, c.startDate, c.endDate, c.amount, c.imageUrl, c.company.name," +
            "c.category.category) from CouponEntity c")
    Page<CouponDetails> getListOfCoupons(Pageable pageable);

    @Query("SELECT new com.alex.coupons.dto.CouponDetails(c.id, c.title, c.description," +
            "c.price, c.startDate, c.endDate, c.amount, c.imageUrl, c.company.name," +
            "c.category.category) FROM CouponEntity c WHERE c.company.name = :companyName")
    Page<CouponDetails> findCouponsByCompanyName(Pageable pageable, @Param("companyName") String companyName);

    @Query("SELECT new com.alex.coupons.dto.CouponDetails(c.id, c.title, c.description," +
            "c.price, c.startDate, c.endDate, c.amount, c.imageUrl, c.company.name," +
            "c.category.category) FROM CouponEntity c WHERE c.category.category = :categoryName")
    Page<CouponDetails> findCouponsByCategoryName(Pageable pageable,@Param("categoryName") String categoryName);

    @Query("SELECT new com.alex.coupons.dto.CouponDetails(c.id, c.title, c.description," +
            "c.price, c.startDate, c.endDate, c.amount, c.imageUrl, c.company.name," +
            "c.category.category)  FROM CouponEntity c WHERE c.title = :title")
    Page<CouponDetails> findCouponsByTitle(Pageable pageable,@Param("title") String title);

    @Query("SELECT new com.alex.coupons.dto.CouponDetails(c.id, c.title, c.description," +
            "c.price, c.startDate, c.endDate, c.amount, c.imageUrl, c.company.name," +
            "c.category.category) FROM CouponEntity c WHERE c.id= :id")
    CouponDetails getCouponDetailsByCouponId(@Param("id") int id);


    //:ToDo check about what @ I need for delete
    @Modifying
    @Transactional
    @Query("DELETE FROM CouponEntity c WHERE endDate < :currentDate")
    void deleteExpiredCoupons(@Param("currentDate") Date currentDate);



    @Query("SELECT new com.alex.coupons.dto.Coupon(c.id, c.title, c.description," +
            "c.price, c.company.id, c.category.id, c.startDate, c.endDate, c.amount," +
            "c.imageUrl) FROM CouponEntity c WHERE c.company.id = :companyId")
    Page<Coupon> findCouponsByCompanyId(Pageable pageable, @Param("companyId")int companyId);


}
