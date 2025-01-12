package com.alex.coupons.logic;

import com.alex.coupons.dal.ICategoryDal;
import com.alex.coupons.dto.Category;
import com.alex.coupons.entities.CategoryEntity;
import com.alex.coupons.enums.ErrorType;
import com.alex.coupons.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryLogic {

    private ICategoryDal categoryDal;

    @Autowired
    public CategoryLogic(ICategoryDal categoryDal) {
        this.categoryDal = categoryDal;
    }


    public void createCategory(Category category, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        validateCategory(category);
        CategoryEntity categoriesEntity = convertCategoryToCategoryEntity(category);
        this.categoryDal.save(categoriesEntity);
    }

    public void updateCategory(Category category, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }

        CategoryEntity categoriesEntity = this.categoryDal.findById(category.getId()).get();
        if (categoriesEntity == null) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }

        validateCategory(category);
        categoriesEntity.setName(category.getCategory());
        this.categoryDal.save(categoriesEntity);
    }

    public Category findCategoryByName(String name) throws ServerException {
        CategoryEntity categoriesEntity = this.categoryDal.findCategoryByName(name);
        if (categoriesEntity == null) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        Category category = convertCategoryEntityToCategory(categoriesEntity);
        return category;
    }

    public Category getCategoryByIdForUser(int id) {
        CategoryEntity categoryEntity = categoryDal.findById(id).get();
        Category category = convertCategoryEntityToCategory(categoryEntity);
        return category;
    }

    public Category getCategoryByIdForAdmin(Integer id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        if (!categoryDal.existsById(id)) {
            throw new ServerException(ErrorType.USER_DO_NOT_EXIST);
        }
        CategoryEntity categoryEntity = this.categoryDal.findById(id).get();
        Category category = convertCategoryEntityToCategory(categoryEntity);
        return category;

    }

/*
    public List<Category> getListOfCategories() {
        List<CategoryEntity> categoryEntities = this.categoryDal.findAll();
        // Set<Object> objects = new HashSet<>();
        List<Category> categories = convertCategoryEntityToCategoryList(categoryEntities);
        return categories;
    }*/

    public Page<Category> getCategories(int page, int size) throws ServerException {
        Pageable pageable = PageRequest.of(page , size);

        return this.categoryDal.allCategory(pageable);
    }

    public void deleteCategoryById(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException((ErrorType.UNAUTHORIZED), "No Authorization");
        }
        categoryDal.deleteById(id);
    }

    private List<Category> convertCategoryEntityToCategoryList(List<CategoryEntity> categoryEntities) {
        List<Category> categories = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryEntities) {
            Category category = new Category(
                    categoryEntity.getId(),
                    categoryEntity.getCategory());
            categories.add(category);
        }
        return categories;

    }

    private Category convertCategoryEntityToCategory(CategoryEntity categoryEntity) {
        Category category = new Category(categoryEntity.getId(),
                categoryEntity.getCategory());
        return category;
    }

    private CategoryEntity convertCategoryToCategoryEntity(Category category) {
        CategoryEntity categoriesEntity = new CategoryEntity(category.getId(),
                category.getCategory());
        return categoriesEntity;
    }

    private void validateCategory(Category category) throws ServerException {
        // Validate length and null
        if (category.getCategory() == null || category.getCategory().length() > 45) {
            throw new ServerException(ErrorType.INVALID_CATEGORY_NAME, category.toString());
        }
       /* :ToDo if (!category.matches("^[a-zA-Z0-9\\s-_]+$")) {
            throw new ServerException(ErrorType.INVALID_CATEGORY_NAME,
                    "Category name contains invalid characters");
        }
       */
        if (this.categoryDal.isCategoryNameNotUnique(category.getCategory())) {
            throw new ServerException(ErrorType.INVALID_CATEGORY_NAME);
        }
    }

/*    public boolean isCategoryNameIsNotUnique(String categoryName) {
        return categoryDal.existsByCategory(categoryName);
        // OR if you want to use findBy
        // return categoryDal.findByCategory(categoryName) != null;
    }*/

    boolean isCategoryIdExists(int id) throws ServerException {
        return categoryDal.existsById(id);
    }

}
