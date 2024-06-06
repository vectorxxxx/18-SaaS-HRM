package com.ihrm.domain.system.response;

import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import lombok.Getter;
import lombok.Setter;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;
import java.util.*;

@Setter
@Getter
public class ProfileResult implements Serializable, AuthCachePrincipal
{
    private String mobile;
    private String username;
    private String company;
    private String companyId;
    private Map<String, Object> roles = new HashMap<>();

    /**
     * @param user
     */
    public ProfileResult(User user, List<Permission> list) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.company = user.getCompanyName();
        this.companyId = user.getCompanyId();

        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();

        for (Permission perm : list) {
            String code = perm.getCode();
            if (perm.getType() == 1) {
                menus.add(code);
            }
            else if (perm.getType() == 2) {
                points.add(code);
            }
            else {
                apis.add(code);
            }
        }
        this.roles.put("menus", menus);
        this.roles.put("points", points);
        this.roles.put("apis", apis);
    }

    public ProfileResult(User user) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.company = user.getCompanyName();
        this.companyId = user.getCompanyId();

        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();

        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> perms = role.getPermissions();
            for (Permission perm : perms) {
                String code = perm.getCode();
                if (perm.getType() == 1) {
                    menus.add(code);
                }
                else if (perm.getType() == 2) {
                    points.add(code);
                }
                else {
                    apis.add(code);
                }
            }
        }

        this.roles.put("menus", menus);
        this.roles.put("points", points);
        this.roles.put("apis", apis);
    }

    /**
     * 针对特定的主体对象生成唯一的缓存键，并在缓存中存储相关的身份验证信息，以提高系统性能和减少重复验证操作。
     * <p>
     * 在大多数的身份验证缓存框架中，该方法是一个重要的接口方法，用于在缓存层面实现对主体对象的有效管理和缓存处理。
     *
     * @return {@link String }
     */
    @Override
    public String getAuthCacheKey() {
        return null;
    }
}
