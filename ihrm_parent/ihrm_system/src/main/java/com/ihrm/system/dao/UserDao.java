package com.ihrm.system.dao;

import com.ihrm.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author VectorX
 * @version V1.0
 * @description 企业数据访问接口
 * @date 2024-05-31 15:48:52
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User>
{}
