package com.alex.coupons.dal;

import com.alex.coupons.dto.Company;
import com.alex.coupons.entities.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyDal extends JpaRepository<CompanyEntity, Integer> {
    @Query("SELECT c FROM CompanyEntity c WHERE c.name = :name")
    CompanyEntity findCompanyByName(@Param("name") String name);



    @Query("SELECT new com.alex.coupons.dto.Company(c.id,c.name,c.address,c.phone) FROM CompanyEntity c")
    Page<Company> allCompany(Pageable pageable);
}
