package com.wisedu.minos.casp.portal.common.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述：错误跳转
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title MyExceptionAdvice
 * @Author: jcx
 * @Date: 2020/10/22
 */
@Controller
public class MyExceptionAdvice implements ErrorController {

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = {"/error"})
    public ModelAndView getErrorPath2 () {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        switch (statusCode) {
            case 404:
                return GlobalExceptionProcessor.getModelAndView("404","页面找不到了。。。",request.getRequestURI());
            case 403:
                return GlobalExceptionProcessor.getModelAndView("403","无权限。。。",request.getRequestURI());
            default:
                return GlobalExceptionProcessor.getModelAndView("500","服务器错误。。。",request.getRequestURI());
        }
    }

    @Override
    public String getErrorPath () {
        return null;
    }
}
