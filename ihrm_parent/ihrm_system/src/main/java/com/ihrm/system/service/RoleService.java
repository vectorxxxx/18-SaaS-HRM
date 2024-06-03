package com.ihrm.system.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService extends BaseService<Role>
{

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存
     */
    public void save(Role role) {
        //基本属性的设置
        String id = idWorker.nextId() + "";
        role.setId(id);
        roleDao.save(role);
    }

    /**
     * 更新
     */
    public void update(Role role) {
        roleDao.save(role);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        roleDao.deleteById(id);
    }

    /**
     * 根据id查询
     */
    public Role findById(String id) {
        return roleDao
                .findById(id)
                .get();
    }

    /**
     * 查询列表
     */
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    public Page<Role> findSearch(String companyId, int page, int size) {
        return roleDao.findAll(getSpecification(companyId), PageRequest.of(page - 1, size));
    }
}
