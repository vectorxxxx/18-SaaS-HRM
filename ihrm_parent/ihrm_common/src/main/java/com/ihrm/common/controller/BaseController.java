package com.ihrm.common.controller;

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
        /**
         * 目前使用 companyId = 1
         *         companyName = "传智播客"
         */
        this.companyId = "1";
        this.companyName = "传智播客";
    }

    //企业id，（暂时使用1,以后会动态获取）
    public String parseCompanyId() {
        return "1";
    }

    public String parseCompanyName() {
        return "江苏传智播客教育股份有限公司";
    }
}
