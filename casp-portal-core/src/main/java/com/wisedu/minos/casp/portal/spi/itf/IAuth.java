package com.wisedu.minos.casp.portal.spi.itf;

import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;

import java.util.List;
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
 * @title IAuth
 * @Author: 01319098
 * @Date: 2021/5/20
 */
@MinosSPI
public interface IAuth {
    /**
     * 返回认证插件的唯一ID
     *
     * @return
     */
    String getAuthId ();

    /**
     * The destroy store template
     */
    void destroy ();

    void initialize (Map<String, Object> param);

    /**
     * 获取插件需要的参数
     * @return
     */
    List<String> getParams ();

    /**
     * 获取数据
     * @return
     */
    String getAuthData (Map<String, Object> paramMap);
}
