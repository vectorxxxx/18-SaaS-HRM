package cn.itcast.shiro.realm;

import cn.itcast.shiro.domain.Permission;
import cn.itcast.shiro.domain.Role;
import cn.itcast.shiro.domain.User;
import cn.itcast.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义的realm
 */
public class CustomRealm extends AuthorizingRealm
{

    @Override
    public void setName(String name) {
        super.setName("customRealm");
    }

    @Autowired
    private UserService userService;

    /**
     * 授权方法 操作的时候，判断用户是否具有响应的权限
     * <p>
     * 先认证 -- 安全数据
     * <p>
     * 再授权 -- 根据安全数据获取用户具有的所有操作权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        final User user = (User) principalCollection.getPrimaryPrincipal();
        final Set<Role> roles = user.getRoles();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        info.setStringPermissions(roles
                .stream()
                .flatMap(role -> role
                        .getPermissions()
                        .stream()
                        .map(Permission::getName))
                .collect(Collectors.toSet()));
        return info;
    }

    /**
     * 认证方法 参数：传递的用户名密码
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        final UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        final String username = token.getUsername();
        final String password = String.valueOf(token.getPassword());
        final User user = userService.findByName(username);
        if (user != null && user
                .getPassword()
                .equals(password)) {
            return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new Md5Hash("123456", "13800000002", 3));
    }
}
