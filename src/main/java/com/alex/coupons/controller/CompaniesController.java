package com.alex.coupons.controller;

import com.alex.coupons.dto.Company;
import com.alex.coupons.dto.UserLogin;
import com.alex.coupons.exceptions.ServerException;
import com.alex.coupons.logic.CompanyLogic;
import com.alex.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompaniesController {
    private CompanyLogic companyLogic;


    @Autowired
    public CompaniesController(CompanyLogic companyLogic) {
        this.companyLogic = companyLogic;
    }


    @PostMapping // checked
    public void createCompany(@RequestBody Company company, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.companyLogic.createCompany(company, userLogin.getUserType());
    }

    @PutMapping // checked
    public void updateCompany(@RequestBody Company company, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.companyLogic.updateCompany(company, userLogin.getUserType());
    }

    @DeleteMapping("/{id}") // checked
    public void deleteCompanyById(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        companyLogic.deleteCompanyById(id, userLogin.getUserType());
    }
    //:ToDo check if coupons are deactivated after deleting company

    @GetMapping("/{id}") // checked
    public Company getCompanyById(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.companyLogic.getCompanyById(id, userLogin.getUserType());
    }

    @GetMapping("/findByName") // checked
    public Company findCompanyByName(@RequestParam("name") String name) throws ServerException {
        return this.companyLogic.findCompanyByName(name);
    }

    @GetMapping // checked
    //:ToDo not working without token somehow....
    public Page<Company> getCompanies(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) throws ServerException {
        return this.companyLogic.getCompanies(page, size);

    }
}
