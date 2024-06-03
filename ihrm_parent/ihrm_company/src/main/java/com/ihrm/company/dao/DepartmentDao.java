package com.ihrm.company.dao;

import com.ihrm.domain.company.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author VectorX
 * @version V1.0
 * @description 部门操作持久层
 * @date 2024-05-31 11:22:57
 */
// 通过继承这两个接口，DepartmentDao接口具备了对数据库表中数据的增删改查功能以及动态查询的能力。开发人员可以在该接口中定义自定义的查询方法，以便实现特定的数据查询需求。
public interface DepartmentDao extends
        // Spring Data JPA提供的Repository接口之一，用于操作数据库表中的数据。其中，泛型<Department, String>分别代表实体类Department和主键类型String。
        JpaRepository<Department, String>,
        // Spring Data JPA提供的用于支持JPA Criteria查询的接口，可以实现根据条件动态查询数据库表中的数据。
        JpaSpecificationExecutor<Department>
{

}
