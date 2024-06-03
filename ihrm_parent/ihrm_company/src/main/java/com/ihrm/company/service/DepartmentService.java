package com.ihrm.company.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description 部门操作业务逻辑层
 * @date 2024-05-31 13:42:24
 */
@Service
public class DepartmentService extends BaseService<Department>
{
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 添加部门
     *
     * @param department 部门
     */
    public void save(Department department) {
        department.setId(idWorker.nextId() + "");
        department.setCreateTime(new Date());
        departmentDao.save(department);
    }

    /**
     * 删除部门
     *
     * @param id id
     */
    public void delete(String id) {
        departmentDao.deleteById(id);
    }

    /**
     * 更新部门
     *
     * @param department 部门
     */
    public void update(Department department) {
        final Department sourceDepartment = departmentDao
                .findById(department.getId())
                .get();
        sourceDepartment.setName(department.getName());
        sourceDepartment.setPid(department.getPid());
        sourceDepartment.setManager(department.getManager());
        sourceDepartment.setManagerId(department.getManagerId());
        sourceDepartment.setIntroduce(department.getIntroduce());
        departmentDao.save(sourceDepartment);
    }

    /**
     * 按 ID 获取部门信息
     *
     * @param id id
     * @return {@link Department }
     */
    public Department findById(String id) {
        return departmentDao
                .findById(id)
                .get();
    }

    /**
     * 获取部门列表
     *
     * @param companyId 公司编号
     * @return {@link List }<{@link Department }>
     */
    public List<Department> findAll(String companyId) {
        return departmentDao.findAll(getSpecification(companyId));
    }
}
