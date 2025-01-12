package com.alex.coupons.controller;

import com.alex.coupons.dto.Category;
import com.alex.coupons.dto.UserLogin;
import com.alex.coupons.exceptions.ServerException;
import com.alex.coupons.logic.CategoryLogic;
import com.alex.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesController {
    private CategoryLogic categoryLogic;

    @Autowired
    public CategoriesController(CategoryLogic categoryLogic) {
        this.categoryLogic = categoryLogic;
    }

    @PostMapping
    public void createCategory(@RequestBody Category category, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.categoryLogic.createCategory(category, userLogin.getUserType());
    }

    @PutMapping
    public void updateCategory(@RequestBody Category category, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.categoryLogic.updateCategory(category, userLogin.getUserType());
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable("id") int id) throws ServerException {
        return categoryLogic.getCategoryByIdForUser(id);
    }

    @GetMapping("/findByName") // checked
    public Category findCategoryByName(@RequestParam("name") String name) throws ServerException {
        return this.categoryLogic.findCategoryByName(name);
    }
    //:ToDo to ask: Can i use try/catch in controller?

    //when category deleted make sure all coupons are deleted too?!?
    //:ToDo use f get category by id for admin for future functions (after adding another column to all tables for enabled/disabled use this f for seeing categories that are "deleted")

    @GetMapping("/Admin/{id}") //checked
    public Category getCategoryByIdForAdmin(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return categoryLogic.getCategoryByIdForAdmin(id, userLogin.getUserType());
    }

    @GetMapping // checked
    public Page<Category> getCategories(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) throws ServerException {
        return this.categoryLogic.getCategories(page, size);
    }

    @DeleteMapping("/{id}") // checked
    public void deleteCategory(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        categoryLogic.deleteCategoryById(id, userLogin.getUserType());
    }

}


   /* @GetMapping("/exists")
    public ResponseEntity<Boolean> checkIfCategoryExists(@RequestParam String category) throws Exception {
        try {
            // Call the logic layer to check if category exists
            boolean exists = categoryLogic.existByName(category);
            // Return true/false with HTTP 200 OK status
            return ResponseEntity.ok(exists);

        } catch (Exception e) {
            // If something goes wrong, return HTTP 500 with false
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }*/