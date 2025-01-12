package com.alex.coupons.dal;

import com.alex.coupons.dto.User;
import com.alex.coupons.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDal extends JpaRepository<UserEntity, Integer> {

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username and u.password = :password")
        // didn't have select
    UserEntity login(@Param("username") String username, @Param("password") String password);

    @Query("SELECT count (u.id) > 0 FROM UserEntity u WHERE u.username = :username")
    Boolean isUserNameNotUnique(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    UserEntity findByUserName(@Param("username") String username);

    @Query("SELECT new com.alex.coupons.dto.User(u.id,u.username,u.userType,u.company.id) " +
            "FROM UserEntity u WHERE u.company.id = :companyId")
    Page<User> findUsersByCompanyId(Pageable pageable,@Param("companyId") int companyId);

    @Query("select new com.alex.coupons.dto.User(u.id,u.username,u.userType,u.company.id) from UserEntity u")
    Page<User> getAllUsers(Pageable pageable);
}
