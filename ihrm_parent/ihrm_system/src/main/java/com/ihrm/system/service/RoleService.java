package com.ihrm.system.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService extends BaseService<Role>
{

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 分配权限
     */
    public void assignPerms(String roleId, List<String> permIds) {
        //1.获取分配的角色对象
        Role role = roleDao
                .findById(roleId)
                .get();
        //2.构造角色的权限集合
        Set<Permission> perms = new HashSet<>();
        for (String permId : permIds) {
            Permission permission = permissionDao
                    .findById(permId)
                    .get();
            //需要根据父id和类型查询API权限列表
            List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PERMISSION_API, permission.getId());
            perms.addAll(apiList);//自定赋予API权限
            perms.add(permission);//当前菜单或按钮的权限
        }
        System.out.println(perms.size());
        //3.设置角色和权限的关系
        role.setPermissions(perms);
        //4.更新角色
        roleDao.save(role);
    }

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
