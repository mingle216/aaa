package com.wisedu.minos.casp.portal.utils;

import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.spi.MinosExtensionLoader;
import com.wisedu.minos.casp.portal.spi.itf.IAuth;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title AuthUtil
 * @Author: 01319098
 * @Date: 2021/5/20
 */
@Service
public class AuthUtil {

    public Map<String, IAuth> getAuthMap () {
        return MinosExtensionLoader.getExtensionLoader(IAuth.class).getSupportedExtensionInstances();
    }

    public IAuth getAuthPluginById (String authId) {
        Map<String, IAuth> authMap = getAuthMap();
        IAuth auth = authMap.get(authId);
        if ( null == auth ) {
            throw new BusinessException(String.format("系统异常，该认证插件%s未安装", authId));
        }
        return auth;
    }


}
