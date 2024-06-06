package com.ihrm.system.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.client.DepartmentFeignClient;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class UserService extends BaseService<User>
{

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 根据mobile查询用户
     */
    public User findByMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }

    /**
     * 1.保存用户
     */
    public void save(User user) {
        //设置主键的值
        user.setId(idWorker.nextId() + "");
        user.setCreateTime(new Date());
        user.setPassword("123456");//设置初始密码
        user.setEnableState(1);
        //调用dao保存部门
        userDao.save(user);
    }

    /**
     * 2.更新用户
     */
    public void update(User user) {
        //1.根据id查询部门
        User target = userDao
                .findById(user.getId())
                .get();
        //2.设置部门属性
        BeanUtils.copyProperties(user, target);
        //3.更新部门
        userDao.save(target);
    }

    /**
     * 3.根据id查询用户
     */
    public User findById(String id) {
        return userDao
                .findById(id)
                .get();
    }

    /**
     * 4.查询全部用户列表 参数：map集合的形式 hasDept departmentId companyId
     */
    public Page<User> findAll(Map<String, Object> map, int page, int size) {
        //1.需要查询条件
        Specification<User> spec = new Specification<User>()
        {
            /**
             * 动态拼接查询条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyId是否为空构造查询条件
                if (!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(criteriaBuilder.equal(root
                            .get("companyId")
                            .as(String.class), map.get("companyId")));
                }
                //根据请求的部门id构造查询条件
                if (!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(criteriaBuilder.equal(root
                            .get("departmentId")
                            .as(String.class), map.get("departmentId")));
                }
                if (!StringUtils.isEmpty(map.get("hasDept"))) {
                    //根据请求的hasDept判断  是否分配部门 0未分配（departmentId = null），1 已分配 （departmentId ！= null）
                    if ("0".equals(map.get("hasDept"))) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    }
                    else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        //2.分页
        return userDao.findAll(spec, new PageRequest(page - 1, size));
    }

    /**
     * 5.根据id删除用户
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    public Page<User> findSearch(Map<String, Object> map, int page, int size) {
        return userDao.findAll(createSpecification(map), PageRequest.of(page - 1, size));
    }

    /**
     * 调整部门
     */
    public void changeDept(String deptId, String deptName, List<String> ids) {
        for (String id : ids) {
            User user = userDao.getOne(id);
            user.setDepartmentName(deptName);
            user.setDepartmentId(deptId);
            userDao.save(user);
        }
    }

    @Autowired
    private RoleDao roleDao;

    /**
     * 分配角色
     */
    public void assignRoles(String userId, List<String> roleIds) {
        User user = userDao
                .findById(userId)
                .get();
        Set<Role> roles = new HashSet<>();
        for (String id : roleIds) {
            Role role = roleDao
                    .findById(id)
                    .get();
            roles.add(role);
        }
        //设置用户和角色之间的关系
        user.setRoles(roles);
        userDao.save(user);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {
        return new Specification<User>()
        {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.equal(root
                            .get("id")
                            .as(String.class), searchMap.get("id")));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.equal(root
                            .get("mobile")
                            .as(String.class), searchMap.get("mobile")));
                }
                // 用户ID
                if (searchMap.get("departmentId") != null && !"".equals(searchMap.get("departmentId"))) {
                    predicateList.add(cb.like(root
                            .get("departmentId")
                            .as(String.class), (String) searchMap.get("departmentId")));
                }
                // 标题
                if (searchMap.get("formOfEmployment") != null && !"".equals(searchMap.get("formOfEmployment"))) {
                    predicateList.add(cb.like(root
                            .get("formOfEmployment")
                            .as(String.class), (String) searchMap.get("formOfEmployment")));
                }
                if (searchMap.get("companyId") != null && !"".equals(searchMap.get("companyId"))) {
                    predicateList.add(cb.like(root
                            .get("companyId")
                            .as(String.class), (String) searchMap.get("companyId")));
                }
                if (searchMap.get("hasDept") != null && !"".equals(searchMap.get("hasDept"))) {
                    if ("0".equals(searchMap.get("hasDept"))) {
                        predicateList.add(cb.isNull(root.get("departmentId")));
                    }
                    else {
                        predicateList.add(cb.isNotNull(root.get("departmentId")));
                    }
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

    @Autowired
    private DepartmentFeignClient departmentFeignClient;

    public void saveAll(List<User> users, String companyId, String companyName) {
        users.forEach(user -> {
            //默认密码
            user.setPassword(new Md5Hash("123456", user.getMobile(), 3).toString());
            //id
            user.setId(idWorker.nextId() + "");
            //基本属性
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            user.setInServiceStatus(1);
            user.setEnableState(1);
            user.setLevel("user");

            //填充部门的属性
            Department department = departmentFeignClient.findByCode(user.getDepartmentId(), companyId);
            if (department != null) {
                user.setDepartmentId(department.getId());
                user.setDepartmentName(department.getName());
            }
        });
        userDao.saveAll(users);
    }
}
