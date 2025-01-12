package com.alex.coupons.logic;

import com.alex.coupons.dal.ICompanyDal;
import com.alex.coupons.dto.Company;
import com.alex.coupons.entities.CompanyEntity;
import com.alex.coupons.enums.ErrorType;
import com.alex.coupons.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;


@Service
public class CompanyLogic {
    Logger logger = Logger.getLogger(CompanyLogic.class.getSimpleName());
    private ICompanyDal companyDal;

    @Autowired
    public CompanyLogic(ICompanyDal companyDal) {
        this.companyDal = companyDal;
    }

    public void createCompany(Company company, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        validateCompany(company);
        CompanyEntity companyEntity = convertCompanyToCompanyEntity(company);
        this.companyDal.save(companyEntity);
    }

    public void updateCompany(Company company, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        CompanyEntity companiesEntity = this.companyDal.findById(company.getId()).get();
        if (this.companyDal == null) {
            throw new ServerException(ErrorType.INVALID_COMPANY_ID);
        }

        validateCompany(company);
        companiesEntity.setName(company.getName());
        companiesEntity.setAddress(company.getAddress());
        companiesEntity.setPhone(company.getPhone());
        this.companyDal.save(companiesEntity);
    }

    public void deleteCompanyById(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.UNAUTHORIZED, "No Authorization");
        }
        companyDal.deleteById(id);
    }
    //ToDo update all thg exceptions validated by admin to ErrorType.UNAUTHORIZED, "No Authorization"

    public Company getCompanyById(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        if (!this.companyDal.existsById(id)) {
            throw new ServerException(ErrorType.INVALID_COMPANY_ID);
        }
        CompanyEntity companiesEntity = this.companyDal.findById(id).get();
        Company company = convertCompanyEntityToCompany(companiesEntity);
        return company;
    }

    public Company findCompanyByName(String name) throws ServerException {

        CompanyEntity companiesEntity = this.companyDal.findCompanyByName(name);
        if (companiesEntity == null) {
            logger.info(" this are all your companies: " + companyDal.allCompany(Pageable.unpaged()).getContent());
            throw new ServerException(ErrorType.GENERAL_ERROR, "no such company " + name); //ToDo add string with a problem
        }

        Company company = convertCompanyEntityToCompany(companiesEntity);
        return company;
    }

    private Company convertCompanyEntityToCompany(CompanyEntity companiesEntity) {
        Company company = new Company(companiesEntity.getId(),
                companiesEntity.getName(), companiesEntity.getAddress(),
                companiesEntity.getPhone());
        return company;
    }

    public Page<Company> getCompanies(int page, int size) throws ServerException {
        Pageable pageable = PageRequest.of(page, size);
        return this.companyDal.allCompany(pageable);
    }

    private CompanyEntity convertCompanyToCompanyEntity(Company company) {
        CompanyEntity companyEntity = new CompanyEntity(company.getId(),
                company.getName(),
                company.getAddress(),
                company.getPhone());
        return companyEntity;
    }

    private void validateCompany(Company company) throws ServerException {
        if (company.getName() == null || company.getName().length() > 45) {
            throw new ServerException(ErrorType.INVALID_NAME, company.toString());
        }
        if (company.getPhone() != null && (company.getPhone().length() < 9 || company.getPhone().length() > 18)) {
            throw new ServerException(ErrorType.INVALID_PHONE, company.toString());
        }

        if (company.getAddress() != null && company.getAddress().length() > 45) {
            throw new ServerException(ErrorType.INVALID_ADDRESS, company.toString());
        }

    }
}
