package cn.itcast.shiro.session;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 自定义会话管理器
 * @date 2024/06/05
 * @see DefaultWebSessionManager
 */
public class CustomSessionManager extends DefaultWebSessionManager
{
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        final String id = WebUtils
                // ServletRequest => HttpServletRequest
                .toHttp(request)
                // 获取授权ID
                .getHeader("Authorization");
        if (StringUtils.isEmpty(id)) {
            return super.getSessionId(request, response);
        }
        else {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }
    }
}
