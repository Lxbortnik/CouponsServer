package com.alex.coupons.dal;

import com.alex.coupons.dto.PurchaseDetails;
import com.alex.coupons.entities.PurchaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPurchaseDal extends JpaRepository<PurchaseEntity,Integer> {

    @Query("SELECT new com.alex.coupons.dto.PurchaseDetails(p.id, p.coupon.id, p.amount, p.customer.id, p.timestamp, (p.amount * p.coupon.price) AS totalPrice, " +
            "p.coupon.title, p.coupon.description, p.coupon.endDate) " +
            "FROM PurchaseEntity p " +
            "WHERE p.id = :id")
    PurchaseDetails getPurchaseDetailsById(@Param("id") int id);

    @Query("SELECT new com.alex.coupons.dto.PurchaseDetails(p.id, p.coupon.id, p.amount,  p.customer.id, p.timestamp, (p.amount * p.coupon.price) AS totalPrice, " +
            "p.coupon.title, p.coupon.description, p.coupon.endDate) " +
            "FROM PurchaseEntity p " +
            "WHERE p.customer.id = :customerId")
    Page<PurchaseDetails> getPurchasesByCustomerId(Pageable pageable, @Param("customerId") int customerId);

    @Query("SELECT new com.alex.coupons.dto.PurchaseDetails(p.id, p.coupon.id, p.amount, p.customer.id, p.timestamp, (p.amount * p.coupon.price) AS totalPrice, " +
            "p.coupon.title, p.coupon.description, p.coupon.endDate) " +
            "FROM PurchaseEntity p ")
    Page<PurchaseDetails> getPurchasesListByAdmin(Pageable pageable);
}
