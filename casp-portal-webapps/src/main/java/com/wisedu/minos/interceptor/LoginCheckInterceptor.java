package com.wisedu.minos.interceptor;

import com.wisedu.minos.casp.portal.common.annotations.Login.MustLogin;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            MustLogin mustLogin = handlerMethod.getMethodAnnotation(MustLogin.class);
            if (null != mustLogin) {
                UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
                if (null == currentUser) {
                    throw new NoLoginException();
                }
                String restrictUser = mustLogin.restrictUser();
                if(StringUtils.isNotEmpty(restrictUser) && !restrictUser.contains(currentUser.getUserAccount())){
                    throw new BusinessException("此接口只有"+restrictUser+"才有权限操作");
                }
            }
        }
        return true;
    }
}
