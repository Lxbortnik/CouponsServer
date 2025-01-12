package com.alex.coupons.dal;

import com.alex.coupons.dto.Category;
import com.alex.coupons.entities.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryDal extends JpaRepository<CategoryEntity, Integer> {

    @Query("SELECT count(c.id)>0 FROM CategoryEntity c WHERE category = :category")
    boolean isCategoryNameNotUnique(@Param("category")String categoryName);

    @Query("SELECT c FROM CategoryEntity c WHERE c.category = :category")
    CategoryEntity findCategoryByName(@Param("category") String category);

    @Query("SELECT new com.alex.coupons.dto.Category(c.id,c.category) FROM CategoryEntity c")
    Page<Category> allCategory(Pageable pageable);
}
