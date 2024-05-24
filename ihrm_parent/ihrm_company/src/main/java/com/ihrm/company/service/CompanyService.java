package com.ihrm.company.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CompanyService
{
    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存企业
     * <p>
     * 1. 配置idwork到工程
     * <p>
     * 2. 在service中注入idwork
     * <p>
     * 3. 通过idwork生成id
     * <p>
     * 4. 保存企业
     */
    public void add(Company company) {
        //基本属性的设置
        String id = idWorker.nextId() + "";
        company.setId(id);
        //默认的状态
        company.setAuditState("0");//0：未审核，1：已审核
        company.setState(1); //0.未激活，1：已激活
        company.setCreateTime(new Date());
        companyDao.save(company);
    }

    /**
     * 更新企业
     * <p>
     * 1. 参数：Company
     * <p>
     * 2. 根据id查询企业对象
     * <p>
     * 3. 设置修改的属性
     * <p>
     * 4. 调用dao完成更新
     */
    public void update(Company company) {
        Company temp = companyDao
                .findById(company.getId())
                .get();
        temp.setName(company.getName());
        temp.setCompanyPhone(company.getCompanyPhone());
        companyDao.save(temp);
    }

    /**
     * 删除企业
     */
    public void deleteById(String id) {
        companyDao.deleteById(id);
    }

    /**
     * 根据id查询企业
     */
    public Company findById(String id) {
        return companyDao
                .findById(id)
                .get();
    }

    /**
     * 查询企业列表
     */
    public List<Company> findAll() {
        return companyDao.findAll();
    }
}
