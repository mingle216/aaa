/**
 *
 */
package com.wisedu.minos.casp.portal.common.utils.rsa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;

import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.common.utils.WebUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/**
 * 类名称：RSA工具类
 * <p>应用管理平台</p>
 * <p>江苏金智教育股份有限公司</p>
 * <p>类说明：用于门户与Cloud功能调用中的加密处理（针对页面级别的应用，对url相关参数进行加解密处理）</p>
 *
 * @author 丁窍
 * @version 1.0    创建时间：2015年12月2日下午8:06:16	丁窍	发布
 */
public abstract class RSAUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtil.class);

    private static final String ALGORITHM_RSA = "RSA";

    /**
     * RSA秘钥相关的token，客户端传递该token，用于服务端获取对应的私钥进行解密
     */
    public static final String RSA_PUBLIC_KEY_TOKEN = "asa_token";

    /**
     * RSA加密后的信息在request中对应的参数
     * 加密后的信息应该存在于这个参数对应的值中
     */
    public static final String REQUEST_PARAM_RSA_ENCRTPT = "req_ept_info";

    /**
     * 保存生成的密钥对的文件名称。
     */
    private static final String RSA_PUBLIC_KEY_FILENAME = "com/wisedu/minos/casp/portal/common/utils/rsa/PublicKey";

    private static final String RSA_PRIVATE_KEY_FILENAME = "com/wisedu/minos/casp/portal/common/utils/rsa/PrivateKey";

    /**
     * 缓存的密钥对。
     */
    private static RSAPrivateKey privateKey = null;
    private static RSAPublicKey publicKey = null;

    /**
     * 生成RSA密钥对
     *
     * @param keyLength length of key
     * @return KeyPair object
     * @throws RuntimeException if the RSA algorithm not supported
     */
    public static KeyPair generateKeypair(int keyLength) throws Exception {
        try {
            KeyPairGenerator kpg;
            try {
                kpg = KeyPairGenerator.getInstance(ALGORITHM_RSA);
            } catch (Exception e) {
                throw new RuntimeException("RSA algorithm not supported", e);
            }
            kpg.initialize(keyLength);
            KeyPair keyPair = kpg.generateKeyPair();
            return keyPair;
        } catch (Exception e) {
            throw new Exception("other exceptions", e);
        }
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长  
        int keyLen = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11  
        String[] datas = splitString(data, keyLen - 11);
        StringBuilder mi = new StringBuilder();
        //如果明文长度大于模长-11则要分组加密  
        for (String s : datas) {
            mi.append(bcd2Str(cipher.doFinal(s.getBytes("utf-8"))));
        }
        return mi.toString();
    }


    /**
     * 私钥加密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        // 模长
        int keyLen = privateKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, keyLen - 11);
        StringBuilder mi = new StringBuilder();
        //如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi.append(bcd2Str(cipher.doFinal(s.getBytes("utf-8"))));
        }

        //返回Base64转码后的串。
        byte[] bytes = hexStringToByteArray(mi.toString());
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //模长  
        int keyLen = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes("utf-8");
        byte[] bcd = asciiToBcd(bytes, bytes.length);
        //如果密文长度大于模长则要分组解密
        StringBuilder ming = new StringBuilder();
        byte[][] arrays = splitArray(bcd, keyLen);
        for (byte[] arr : arrays) {
            ming.append(new String(cipher.doFinal(arr),"utf-8"));
        }
        return ming.toString();
    }

    /**
     * 公钥钥解密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        //模长  
        int keyLen = publicKey.getModulus().bitLength() / 8;

        //Base64解码。
        byte[] base64DecodedByte = Base64.decodeBase64(data);
        String rsaEncryptedData = byteArrayToHexString(base64DecodedByte);

        byte[] bytes = rsaEncryptedData.getBytes("utf-8");
        byte[] bcd = asciiToBcd(bytes, bytes.length);
        //如果密文长度大于模长则要分组解密  
        StringBuilder ming = new StringBuilder();
        byte[][] arrays = splitArray(bcd, keyLen);
        for (byte[] arr : arrays) {
            ming.append(new String(cipher.doFinal(arr),"utf-8"));
        }
        return ming.toString();
    }


    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA/None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        if ( StringUtil.isEmpty(modulus) || StringUtil.isEmpty(exponent)) {
            return null;
        }

        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            LOGGER.error("系统异常", e);
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA/None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPublicKey getPublicKey(BigInteger modulus, BigInteger exponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            LOGGER.error("系统异常", e);
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA/None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPrivateKey getPrivateKey(BigInteger modulus, BigInteger exponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            LOGGER.error("系统异常", e);
            return null;
        }
    }

    /**
     * 方法名：生成RSA-JSON格式的公钥数据
     * <p>功能说明：</p>
     *
     * @param keyPair 密钥对
     * @return    <json>{"modulus": "****", "exponent": "**********"}</json>
     */
    public static JSONObject generatePublicKeyJsonResult(KeyPair keyPair) {
        JSONObject jsonResult = new JSONObject();
        if (keyPair == null) {
            return jsonResult;
        }

        jsonResult.put("modulus", getPublicKeyModulus(keyPair));
        jsonResult.put("exponent", getPublicKeyExponent(keyPair));
        return jsonResult;
    }

    /**
     * 方法名：从远程机器获取公钥对象
     * <p>功能说明：</p>
     *
     * @param url      公钥的获取地址
     * @param keyToken token信息
     * @return
     */
    public static PublicKeyInfo getRemotePublicKey(String url, String keyToken) {
        String tempUrl=url;
        if (StringUtil.isEmpty(tempUrl))
            return null;

        try {
            RestTemplate restTemplate = new RestTemplate();
            if (StringUtil.isNotEmpty(keyToken)) {
                tempUrl = WebUtil.appendUrlParameters(tempUrl,
                        Collections.singletonMap(RSA_PUBLIC_KEY_TOKEN, keyToken));
            }
            String result = restTemplate.getForObject(tempUrl, String.class);
            JSONObject jsonResult = JSONObject.parseObject(result);
            String modulus = jsonResult.getString("modulus");
            String exponent = jsonResult.getString("exponent");
            String token = jsonResult.getString("token");
            if (StringUtil.isEmpty(modulus)) {
                String data = jsonResult.getString("data");
                if (StringUtil.isNotEmpty(data)) {
                    JSONObject jsonData = JSONObject.parseObject(data);
                    modulus = jsonData.getString("modulus");
                    exponent = jsonData.getString("exponent");
                    token = jsonData.getString("token");
                }
            }
            RSAPublicKey publicKey = getPublicKey(modulus, exponent);
            if (publicKey == null)
                return null;

            return new PublicKeyInfo(publicKey, token);
        } catch (Exception e) {
            LOGGER.error("从远程地址【url={}】获取公钥数据异常", tempUrl, e);
            return null;
        }
    }

    /**
     * 方法名：获取Request中的token信息
     * <p>功能说明：</p>
     *
     * @param request 请求对象
     * @return 没有则返回 null或""
     */
    public static String getTokenFormRequest(HttpServletRequest request) {
        if (request == null)
            return "";

        String token = request.getParameter(RSA_PUBLIC_KEY_TOKEN);
        if (StringUtil.isEmpty(token) && request.getSession() != null)
            token = request.getSession().getId();
        return token;
    }

    /**
     * 方法名：对URL包装rsa加密信息，返回包装后的url
     * <p>功能说明：</p>
     *
     * @param originalUrl    原始url
     * @param rsaEncryptInfo 加密后的字符串
     * @param token          token信息，用户服务端私钥的对应
     * @return 包装后的url
     */
    public static String wrapUrlWithRSAEncryptInfo(String originalUrl, String rsaEncryptInfo, String token) {
        if (StringUtil.isEmpty(originalUrl) || StringUtil.isEmpty(rsaEncryptInfo))
            return null;

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(REQUEST_PARAM_RSA_ENCRTPT, rsaEncryptInfo);
        if (StringUtil.isNotEmpty(token)) {
            parameters.put(RSA_PUBLIC_KEY_TOKEN, token);
        }
        return WebUtil.appendUrlParameters(originalUrl, parameters);
    }

    /**
     * 方法名：从请求对象中获取RSA机密后的字符串
     * <p>功能说明：</p>
     *
     * @param request 请求对象
     * @return
     */
    public static String getRSAEncryptInfo(HttpServletRequest request) {
        if (request == null)
            return null;

        return request.getParameter(REQUEST_PARAM_RSA_ENCRTPT);
    }

    public static class PublicKeyInfo implements Serializable {

        private static final long serialVersionUID = -8603081222860122989L;

        private RSAPublicKey rsaPublicKey;    //	RSA
        private String token;

        public PublicKeyInfo(RSAPublicKey publicKey, String token) {
            super();
            this.rsaPublicKey = publicKey;
            this.token = token;
        }

        /**
         * @return the publicKey
         */
        public RSAPublicKey getRsaPublicKey() {
            return rsaPublicKey;
        }

        /**
         * @return the token
         */
        public String getToken() {
            return token;
        }
    }

    /**
     * ASCII码转BCD码
     */
    private static byte[] asciiToBcd(byte[] ascii, int ascLen) {
        byte[] bcd = new byte[ascLen / 2];
        int j = 0;
        for (int i = 0; i < (ascLen + 1) / 2; i++) {
            bcd[i] = ascToBcd(ascii[j++]);
            bcd[i] = (byte) (((j >= ascLen) ? 0x00 : ascToBcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    private static byte ascToBcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    /**
     * BCD转字符串
     */
    private static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    private static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    private static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    /**
     * Return public RSA key modulus
     *
     * @param keyPair RSA keys
     * @return modulus value as hex string
     */
    public static String getPublicKeyModulus(KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey.getModulus().toString();
    }

    /**
     * Return public RSA key exponent
     *
     * @param keyPair RSA keys
     * @return public exponent value as hex string
     */
    public static String getPublicKeyExponent(KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey.getPublicExponent().toString();
    }

    /**
     * Max block size with given key length
     *
     * @param keyLength length of key
     * @return numeber of digits
     */
    public static int getMaxDigits(int keyLength) {
        return ((keyLength * 2) / 16) + 3;
    }

    /**
     * Convert byte array to hex string
     *
     * @param bytes input byte array
     * @return Hex string representation
     */
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            result.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return result.toString();
    }

    /**
     * Convert hex string to byte array
     *
     * @param data input string data
     * @return bytes
     */
    public static byte[] hexStringToByteArray(String data) {
        int k = 0;
        byte[] results = new byte[data.length() / 2];
        for (int i = 0; i < data.length(); ) {
            results[k] = (byte) (Character.digit(data.charAt(i++), 16) << 4);
            results[k] += (byte) (Character.digit(data.charAt(i++), 16));
            k++;
        }
        return results;
    }

    /**
     * @return
     */
    public String toPublicKeyString() throws Exception {
        KeyPair keys = generateKeypair(512);
        StringBuilder out = new StringBuilder();

        String e = getPublicKeyExponent(keys);
        String n = getPublicKeyModulus(keys);
        String md = String.valueOf(getMaxDigits(512));

        out.append("{\"e\":\"");
        out.append(e);
        out.append("\",\"n\":\"");
        out.append(n);
        out.append("\",\"maxdigits\":\"");
        out.append(md);
        out.append("\"}");

        return out.toString();
    }

    /**
     * 获取RSA私钥。
     */
    public static RSAPrivateKey getPrivateKey() {
        if (privateKey == null) {
            try {
                ClassLoader classLoader = RSAUtil.class.getClassLoader();
                InputStream fis = classLoader.getResourceAsStream(RSA_PRIVATE_KEY_FILENAME);

                ObjectInputStream oos = new ObjectInputStream(fis);
                privateKey = (RSAPrivateKey) oos.readObject();

                oos.close();
                fis.close();
            } catch (Exception e) {
                LOGGER.error("加载私钥文件出错", e);
            }
        }
        return privateKey;
    }

    /**
     * 获取RSA公钥。
     */
    public static RSAPublicKey getPublicKey() {

        if (publicKey == null) {
            try {
                ClassLoader classLoader = CerDecrypt.class.getClassLoader();
                InputStream fis = classLoader.getResourceAsStream(RSA_PUBLIC_KEY_FILENAME);

                ObjectInputStream oos = new ObjectInputStream(fis);
                publicKey = (RSAPublicKey) oos.readObject();

                oos.close();
                fis.close();
            } catch (Exception e) {
                LOGGER.error("系统异常", e);
            }
        }
        return publicKey;
    }

    /**
     * 方法名：生成RSA秘钥文件对
     * <p>功能说明：在指定路径下生成PublicKey和PrivateKey文件</p>
     *
     * @param keyPair
     * @param savePath
     */
    public static void generateRSAKeyPairFile(KeyPair keyPair, String savePath) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File pathFile = new File(savePath);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            try {
                File rsaPubKeyFile = new File(pathFile, "PublicKey");
                fos = new FileOutputStream(rsaPubKeyFile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(keyPair.getPublic());
            }catch (Exception e){
                LOGGER.error("系统异常", e);
            }finally {
                try {
                    if (oos != null) {
                        oos.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("系统异常", e);
                }
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("系统异常", e);
                }
            }
            try {
                File rsaPrivKeyFile = new File(pathFile, "PrivateKey");
                fos = new FileOutputStream(rsaPrivKeyFile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(keyPair.getPrivate());
            }catch (Exception e){
                LOGGER.error("系统异常", e);
            }finally {
                try {
                    if (oos != null) {
                        oos.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("系统异常", e);
                }
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("系统异常", e);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("系统异常", ex);
        }
    }
}
