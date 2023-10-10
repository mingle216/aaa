package com.wisedu.minos.casp.portal.utils;

import com.wisedu.minos.casp.portal.common.utils.FastJsonUtil;
import com.wisedu.minos.casp.portal.model.UserInfo;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 功能描述：封装UserInfo 修改记录:
 * 
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CasUtil
 * @Author: de
 * @Date: 2020/7/21
 */
public class CasUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CasUtil.class);

	private static String casSessionKey;

	/**
	 * 封装UserInfo
	 *
	 * @param request
	 * @return
	 */
	public static UserInfo getUserInfoFromCas(HttpServletRequest request) {
		Object object = request.getSession().getAttribute(casSessionKey);
		if (null == object){
			return null;
		}
		Assertion assertion = (Assertion) object;
		return buildUserInfoByCas(assertion);
	}

	/**
	 * 构建UserInfo
	 *
	 * @param assertion
	 * @return
	 */
	public static UserInfo buildUserInfoByCas(Assertion assertion) {
		if (null == assertion){
			LOGGER.error("cas对接buildUserInfoByCas没有获取到用户");
			return null;
		}
		UserInfo userInfo = new UserInfo();
		String userName = assertion.getPrincipal().getName();
		userInfo.setUserAccount(userName);
		// 获取属性值
		Map<String, Object> map = assertion.getPrincipal().getAttributes();
		Object name = map.get("cn");
		userInfo.setUserName(name == null ? userName : name.toString());
		userInfo.setWid(userName);
		return userInfo;
	}

	public static String getCasSessionKey () {
		return casSessionKey;
	}

	public static void setCasSessionKey (String casSessionKey) {
		CasUtil.casSessionKey = casSessionKey;
	}
}
