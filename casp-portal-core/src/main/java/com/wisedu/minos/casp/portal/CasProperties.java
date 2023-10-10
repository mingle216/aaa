package com.wisedu.minos.casp.portal;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * CasProperties
 * <p/>
 * cas认证配置属性
 *
 * @author hyluan
 * @date 2020/10/10 19:29
 * Copyright (c) 2018 wisedu
 */
@Configuration
@ConfigurationProperties
@Data
@Slf4j
public class CasProperties {

    Cas cas = new Cas();

    Module module = new Module();

    @ToString
    public static class Cas {

        /**
         * 认证服务器地址
         */
        @Setter
        String serverUrl = "https://idsdx2.wisedu.com/authserver/";

        /**
         * cas集成方式 proxy：反向代理   cas：cas原生集成
         */
        @Getter
        @Setter
        String integrated = "cas";

        /**
         * nginx 头中的用户
         */
        @Getter
        @Setter
        String nginxHeaderUser = "CAS_USER";

        /**
         * cas client默认的session key
         */
        @Getter
        @Setter
        String casSessionKey = "_const_cas_assertion_";

        /**
         * 过滤的静态资源文件后缀
         */
        @Getter
        @Setter
        String staticSuffix = ".js,.css,.ico,.jpg,.png,.jpeg,.svg,.gif,.off2,.woff,.woff2,.ttf,.otf)";

        public String getServerUrl() {
            if (!this.serverUrl.endsWith("/")) {
                return this.serverUrl + "/";
            }
            return this.serverUrl;
        }
    }

    @Data
    public static class Module {
        /**
         * 系统域名
         */
        String domain = "http://127.0.0.1:8116";
    }

    @PostConstruct
    public void init() {
        LOGGER.info("cas配置属性：" + this.toString());
    }


}
