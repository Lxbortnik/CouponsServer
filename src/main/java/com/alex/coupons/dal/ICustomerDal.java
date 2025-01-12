package com.alex.coupons.dal;

import com.alex.coupons.dto.CustomerDetails;
import com.alex.coupons.entities.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICustomerDal extends JpaRepository<CustomerEntity,Integer> {
    @Query("SELECT new com.alex.coupons.dto.CustomerDetails(c.name , c.user.username ,c.address ,c.phone) FROM CustomerEntity c " +
            "WHERE c.user.id = :id")
    CustomerDetails getCustomerDetailsById(@Param("id") int id);

    @Query("SELECT new com.alex.coupons.dto.CustomerDetails(c.name , c.user.username ,c.address ,c.phone) FROM CustomerEntity c ")
    Page<CustomerDetails> getCustomersDetails(Pageable pageable);
}
