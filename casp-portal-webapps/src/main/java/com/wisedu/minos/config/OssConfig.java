package com.wisedu.minos.config;

import com.wisedu.minos.oss.client.util.OSSClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title WebConfig
 * @Author: jcx
 * @Date: 2020/9/16
 */
@Configuration
public class OssConfig {

    @Value("${minos.oss.server.addresses}")
    private String minosOssAddResses;
    @Value("${minos.oss.server.accessToken}")
    private String minosOssAccessToken;
    @Bean
    public OSSClientUtil ossClientUtil (){
        OSSClientUtil ossClientUtil = new OSSClientUtil();
        ossClientUtil.setOssAddress(minosOssAddResses);
        ossClientUtil.setAccessToken(minosOssAccessToken);
        return ossClientUtil;
    }
}
