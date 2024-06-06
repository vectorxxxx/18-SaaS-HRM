package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.domain.system.response.UserResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//1.解决跨域
@CrossOrigin
//2.声明restContoller
@RestController
//3.设置父路径
@RequestMapping(value = "/sys")
public class UserController extends BaseController
{

    @Autowired
    private UserService userService;

    // @Autowired
    // private JwtUtils jwtUtils;

    @Autowired
    private PermissionService permissionService;

    /**
     * 用户登录成功之后，获取用户信息 1.获取用户id 2.根据用户id查询用户 3.构建返回值对象 4.响应
     */
    @RequestMapping(value = "/profile",
                    method = RequestMethod.POST)
    public Result profile(HttpServletRequest request) throws Exception {
        final Subject subject = SecurityUtils.getSubject();
        final PrincipalCollection principals = subject.getPrincipals();
        final ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
        
        // Claims claims = (Claims) request.getAttribute("user_claims");
        // final String userId = claims.getId();
        //
        // // 测试临时使用
        // // String userId = "1063705989926227968";
        // User user = userService.findById(userId);
        // ProfileResult result;
        // if ("user".equals(user.getLevel())) {
        //     result = new ProfileResult(user);
        // }
        // else {
        //     Map<String, Object> map = new HashMap<>();
        //     if ("coAdmin".equals(user.getLevel())) {
        //         map.put("enVisible", "1");
        //     }
        //     List<Permission> list = permissionService.findAll(map);
        //     result = new ProfileResult(user, list);
        // }
        return new Result(ResultCode.SUCCESS, result);
    }

    /**
     * 用户登录 1.通过service根据mobile查询用户 2.比较password 3.生成jwt信息
     */
    @RequestMapping(value = "/login",
                    method = RequestMethod.POST)
    public Result login(
            @RequestBody
                    Map<String, String> loginMap) {
        String mobile = loginMap.get("mobile");
        String password = loginMap.get("password");
        try {
            password = new Md5Hash(password, mobile, 3).toString();// 密码、盐、加密次数
            UsernamePasswordToken token = new UsernamePasswordToken(mobile, password);
            final Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            final String sessionId = (String) subject
                    .getSession()
                    .getId();
            return new Result(ResultCode.SUCCESS, sessionId);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        }

        // User user = userService.findByMobile(mobile);
        // //登录失败
        // if (user == null || !user
        //         .getPassword()
        //         .equals(password)) {
        //     return new Result(ResultCode.MOBILEORPASSWORDERROR);
        // }
        // //登录成功
        // //api权限字符串
        // StringBuilder sb = new StringBuilder();
        // //获取到所有的可访问API权限
        // for (Role role : user.getRoles()) {
        //     for (Permission perm : role.getPermissions()) {
        //         if (perm.getType() == PermissionConstants.PERMISSION_API) {
        //             sb
        //                     .append(perm.getCode())
        //                     .append(",");
        //         }
        //     }
        // }
        // Map<String, Object> map = new HashMap<>();
        // map.put("apis", sb.toString());//可访问的api权限字符串
        // map.put("companyId", user.getCompanyId());
        // map.put("companyName", user.getCompanyName());
        // String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
        // return new Result(ResultCode.SUCCESS, token);
    }

    /**
     * 分配角色
     */
    @RequestMapping(value = "/user/assignRoles",
                    method = RequestMethod.PUT)
    public Result assignRoles(
            @RequestBody
                    Map<String, Object> map) {
        //1.获取被分配的用户id
        String userId = (String) map.get("id");
        //2.获取到角色的id列表
        List<String> roleIds = (List<String>) map.get("roleIds");
        //3.调用service完成角色分配
        userService.assignRoles(userId, roleIds);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/user",
                    method = RequestMethod.POST)
    public Result save(
            @RequestBody
                    User user) {
        //1.设置保存的企业id
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        //2.调用service完成保存企业
        userService.save(user);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的部门列表 指定企业id
     */
    @RequestMapping(value = "/user",
                    method = RequestMethod.GET)
    public Result findAll(int page, int size,
                          @RequestParam
                                  Map map) {
        //1.获取当前的企业id
        map.put("companyId", companyId);
        //2.完成查询
        Page<User> pageUser = userService.findAll(map, page, size);
        //3.构造返回结果
        PageResult<User> pageResult = new PageResult<>(pageUser.getTotalElements(), pageUser.getContent());
        return new Result(ResultCode.SUCCESS, pageResult);
    }

    /**
     * 根据ID查询user
     */
    @RequestMapping(value = "/user/{id}",
                    method = RequestMethod.GET)
    public Result findById(
            @PathVariable(value = "id")
                    String id) {
        final User user = userService.findById(id);
        final UserResult userResult = new UserResult(user);
        return new Result(ResultCode.SUCCESS, userResult);
    }

    /**
     * 修改User
     */
    @RequestMapping(value = "/user/{id}",
                    method = RequestMethod.PUT)
    public Result update(
            @PathVariable(value = "id")
                    String id,
            @RequestBody
                    User user) {
        //1.设置修改的部门id
        user.setId(id);
        //2.调用service更新
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequestMapping(value = "/user/{id}",
                    method = RequestMethod.DELETE)
    public Result delete(
            @PathVariable(value = "id")
                    String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/value",
                    method = RequestMethod.GET)
    public Result findByPage(int page, int pagesize,
                             @RequestParam
                                     Map<String, Object> map) throws Exception {
        map.put("companyId", companyId);
        final Page<User> searchPage = userService.findSearch(map, page, pagesize);
        final PageResult<User> pr = new PageResult<>(searchPage.getTotalElements(), searchPage.getContent());
        return new Result(ResultCode.SUCCESS, pr);
    }
}
