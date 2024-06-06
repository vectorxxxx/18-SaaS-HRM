package com.ihrm.common.controller;

import com.ihrm.domain.system.response.ProfileResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-05-31 11:03:58
 */
public class BaseController
{
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;

    @ModelAttribute
    public void setReqAndResp(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        // this.companyId = "1";
        // this.companyName = "传智播客";
        // 使用 shiro 从 Redis 获取数据
        final Subject subject = SecurityUtils.getSubject();
        final PrincipalCollection principals = subject.getPrincipals();
        if (principals != null && !principals.isEmpty()) {
            final ProfileResult profileResult = (ProfileResult) principals.getPrimaryPrincipal();
            this.companyId = profileResult.getCompanyId();
            this.companyName = profileResult.getCompany();
        }
    }

    // //企业id，（暂时使用1,以后会动态获取）
    // public String parseCompanyId() {
    //     return "1";
    // }
    //
    // public String parseCompanyName() {
    //     return "江苏传智播客教育股份有限公司";
    // }
}
