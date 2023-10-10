package com.wisedu.minos.casp.portal.common.utils;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 功能描述：主要用于忽略SSL校验发起请求
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title SslMitm
 * @Author: jcx
 * @Date: 2020/7/29
 */
public class SslMitm implements TrustManager, X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
        return true;
    }

    public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
        return true;
    }

    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }

    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }
}
