package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：使用HttpClient方式
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title HttpUtil
 * @Author: jcx
 * @Date: 2020/7/29
 */
public class HttpUtil {

    private static final Logger log = LogManager.getLogger(HttpUtil.class);

    /**
     * 发起http的get请求
     *
     * @param httpurl
     * @return
     */
    public static String sendGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            //初始化Http客户端
            connection=initHttpClient("GET", httpurl, 15000, 60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            Map<String, Object> readResultMap = readResult(connection);
            is= (InputStream) readResultMap.get("is");
            os= (OutputStream) readResultMap.get("os");
            result=String.valueOf(readResultMap.get("result"));
        } catch (Exception e) {
            log.error("发起http的get请求发生异常", e);
        } finally {
            // 关闭资源
            closeResources(br, is, connection, os, "发起http的get请求");
        }

        return result;
    }

    /**
     * 发起http的get请求支持忽略SSL校验
     *
     * @param httpurl
     * @param isIgnoreSSL 是否忽略SSL校验
     * @return
     */
    public static String sendGetSSL(String httpurl, boolean isIgnoreSSL) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        OutputStream os = null;
        String result = null;// 返回结果字符串
        try {
            if (isIgnoreSSL) {
                //初始化Http客户端(需忽略SSL校验时调用)
                connection =initHttpClientToSsl("GET", httpurl, 15000, 60000);
            } else {
                //初始化Http客户端
                connection=initHttpClient("GET", httpurl, 15000, 60000);
            }
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            Map<String, Object> readResultMap = readResult(connection);
            is= (InputStream) readResultMap.get("is");
            os= (OutputStream) readResultMap.get("os");
            result=String.valueOf(readResultMap.get("result"));
        } catch (Exception e) {
            log.error("发起http的get请求支持忽略SSL校验发生异常", e);
        } finally {
            // 关闭资源
            closeResources(br, is, connection, os, "发起http的get请求支持忽略SSL校验");
        }

        return result;
    }

    /**
     * 发起POST请求
     *
     * @param httpUrl
     * @param param
     * @return
     */
    public static String sendPost(String httpUrl, String param) {
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            //初始化Http客户端
            connection = initHttpClient("POST", httpUrl, 15000, 60000);
            //connection设置属性
            os=setConnection(connection, param);
            // 通过连接对象获取一个输入流，向远程读取
            Map<String, Object> readResultMap = readResult(connection);
            is= (InputStream) readResultMap.get("is");
            os= (OutputStream) readResultMap.get("os");
            result=String.valueOf(readResultMap.get("result"));
        } catch (Exception e) {
            log.error("发起POST请求发生异常", e);
        } finally {
            //关闭资源
            closeResources(br, is, connection, os, "发起POST请求");
        }
        return result;
    }

    /**
     * 发起POST请求 支持忽略SSL校验
     *
     * @param httpUrl
     * @param param
     * @param isIgnoreSSL
     * @return
     */
    public static String sendPostSSL(String httpUrl, String param, boolean isIgnoreSSL) {
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            if (isIgnoreSSL) {
                //初始化Http客户端(需忽略SSL校验时调用)
                connection =initHttpClientToSsl("GET", httpUrl, 15000, 60000);
            } else {
                //初始化Http客户端
                connection=initHttpClient("POST", httpUrl, 15000, 60000);
            }
            //connection设置属性
            os=setConnection(connection, param);
            // 通过连接对象获取一个输入流，向远程读取
            Map<String, Object> readResultMap = readResult(connection);
            is= (InputStream) readResultMap.get("is");
            os= (OutputStream) readResultMap.get("os");
            result=String.valueOf(readResultMap.get("result"));
        } catch (Exception e) {
            log.error("发起POST请求 支持忽略SSL校验发生异常", e);
        } finally {
            //关闭资源
            closeResources(br, is, connection, os, "发起POST请求 支持忽略SSL校验");
        }
        return result;
    }

    /**
     * @return void
     * @Author jcx
     * @Description connection设置属性
     * @Date 16:07 2020/7/30
     * @Param connection:
     * @Param os:
     * @Param param:
     **/
    private static OutputStream setConnection(HttpURLConnection connection, String param) throws IOException {
        // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
        connection.setDoOutput(true);
        // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
        connection.setDoInput(true);
        // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
        //connection.setRequestProperty("Authorization", "");
        // 通过连接对象获取一个输出流
        OutputStream os = connection.getOutputStream();
        // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
        os.write(param.getBytes());
        return os;
    }

    /**
     * @return HttpURLConnection
     * @Author jcx
     * @Description 初始化Http客户端
     * @Date 15:26 2020/7/30
     * @Param httpType:
     **/
    private static HttpURLConnection initHttpClient(String httpType, String httpurl, int connectTime, int readTime) throws IOException {
        // 创建远程url连接对象
        URL url = new URL(httpurl);
        // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置连接方式：get
        connection.setRequestMethod(httpType);
        // 设置连接主机服务器的超时时间
        connection.setConnectTimeout(connectTime);
        // 设置读取远程返回的数据时间
        connection.setReadTimeout(readTime);
        return connection;
    }

    /**
     * @return HttpURLConnection
     * @Author jcx
     * @Description 初始化Http客户端(需忽略SSL校验时调用)
     * @Date 15:40 2020/7/30
     * @Param connection:
     * @Param httpType:
     * @Param httpurl:
     * @Param connectTime:
     * @Param readTime:
     **/
    private static HttpURLConnection initHttpClientToSsl(String httpType, String httpurl, int connectTime, int readTime) throws Exception {
        //该部分必须在获取connection前调用
        trustAllHttpsCertificates();
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        HttpURLConnection connection = (HttpURLConnection) new URL(httpurl).openConnection();
        // 设置连接方式：get
        connection.setRequestMethod(httpType);
        // 设置连接主机服务器的超时时间
        connection.setConnectTimeout(connectTime);
        // 设置读取远程返回的数据时间
        connection.setReadTimeout(readTime);
        return connection;
    }

    /**
     * @return String
     * @Author jcx
     * @Description 通过connection连接，获取输入流
     * @Date 15:51 2020/7/30
     * @Param connection:
     * @Param is:
     * @Param br:
     * @Param result:
     **/
    private static Map<String, Object> readResult(HttpURLConnection connection) throws IOException {
        Map<String, Object> results = new HashMap<>();
        InputStream is=null;
        BufferedReader br=null;
        String result="";
        if (connection.getResponseCode() == 200) {
            is = connection.getInputStream();
            // 封装输入流is，并指定字符集
            br = new BufferedReader(new InputStreamReader(is, Global.UTF_8));
            // 存放数据
            StringBuilder sbf = new StringBuilder();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }
            result = sbf.toString();
        }
        results.put("is",is);
        results.put("br",br);
        results.put("result",result);
        return results;
    }

    /**
     * @return void
     * @Author jcx
     * @Description 统一关闭资源
     * @Date 15:57 2020/7/30
     * @Param br:
     * @Param is:
     * @Param connection:
     **/
    private static void closeResources(BufferedReader br, InputStream is, HttpURLConnection connection, OutputStream os, String functionInfo) {
        if (null != br) {
            try {
                br.close();
            } catch (IOException e) {
                log.error(functionInfo + "关闭资源发生异常", e);
            }
        }
        if (null != is) {
            try {
                is.close();
            } catch (IOException e) {
                log.error(functionInfo + "关闭资源发生异常", e);
            }
        }
        if (connection != null) {
            connection.disconnect();
        }
        if (null != os) {
            try {
                os.close();
            } catch (IOException e) {
                log.error(functionInfo + "关闭资源发生异常", e);
            }
        }
    }

    /**
     * 开启SSL忽略
     */
    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new SslMitm();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }


    public static <T> T getResponseDataByPost(String key, long timeout, String url, HttpEntity httpEntity,Class<T> dataType) {
        T responseData;
        responseData = (T) ApplicationContextUtil.get(RedisUtil.class).get(key);
        if (responseData == null) {
            responseData = RestTemplateUtils.post(url, httpEntity, dataType).getBody();
            ApplicationContextUtil.get(RedisUtil.class).set(key, responseData,timeout, TimeUnit.SECONDS);
        }
        return responseData;
    }

    public static <T> T getResponseDataByGet(String key, long timeout, String url, Class<T> dataType) {
        T responseData;
        responseData = (T) ApplicationContextUtil.get(RedisUtil.class).get(key);
        if (responseData == null) {
            responseData = RestTemplateUtils.get(url, dataType).getBody();
            ApplicationContextUtil.get(RedisUtil.class).set(key, responseData,timeout, TimeUnit.SECONDS);
        }
        return responseData;
    }

}
