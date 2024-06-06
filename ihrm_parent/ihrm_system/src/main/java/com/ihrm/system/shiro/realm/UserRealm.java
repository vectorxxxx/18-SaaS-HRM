package com.ihrm.system.shiro.realm;

import com.ihrm.common.shiro.realm.IhrmRealm;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-05 17:05:46
 */
public class UserRealm extends IhrmRealm
{
    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public void setName(String name) {
        super.setName("userRealm");
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        final UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        final String mobile = upToken.getUsername();
        final String password = String.valueOf(upToken.getPassword());
        final User user = userService.findByMobile(mobile);
        if (user
                .getPassword()
                .equals(password)) {
            ProfileResult result;
            if ("user".equals(user.getLevel())) {
                result = new ProfileResult(user);
            }
            else {
                Map<String, Object> map = new HashMap<>();
                if ("coAdmin".equals(user.getLevel())) {
                    map.put("enVisible", "1");
                }
                List<Permission> list = permissionService.findAll(map);
                result = new ProfileResult(user, list);
            }

            // 安全数据，密码，realm域名
            return new SimpleAuthenticationInfo(result, user.getPassword(), this.getName());
        }
        return null;
    }
}
