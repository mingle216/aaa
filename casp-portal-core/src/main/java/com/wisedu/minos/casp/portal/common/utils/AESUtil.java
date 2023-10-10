package com.wisedu.minos.casp.portal.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 功能描述：AES加密解密
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title AESUtil
 * @Author: jcx
 * @Date: 2020/9/21
 */
public class AESUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtil.class);

    /**
     * AES加密的秘钥  仅后端加密使用，该密钥不可以在前端使用
     */
    private static final String AES_KEY = "lr8qqwrkymr5uixa3fid7ore";
    /**
     * AES加密的iv值  仅后端加密使用，该值不可以在前端使用
     */
    private static final String AES_IV = "2001671872406214";


    /**
     * AES+Base64的加密算法
     * @param src
     * @return
     */
    public static String encryptAes(String src){
        try{
            byte[] raw = AES_KEY.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(AES_IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(src.getBytes());

            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return encryptBase64(encrypted);
        }catch (Exception e){
            LOGGER.error("异常",e);
            return "";
        }
    }

    /**
     * Base64加密算法
     * @param bytes
     * @return
     */
    public static String encryptBase64(byte[] bytes){
        return new Base64(true).encodeToString(bytes);
    }

    /**
     * AES解密算法
     * @param src
     * @return
     */
    public static String decryptAes(String src){
        if ( StringUtils.isEmpty(src)){
            return "";
        }
        try {
            byte[] raw = AES_KEY.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(AES_IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            //先用base64解密
            byte[] encrypted1 = new Base64(true).decode(src);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                // 如果解密失败，返回原文
                LOGGER.error("异常",e);
                return src;
            }
        } catch (Exception ex) {
            // 如果解密失败，返回原文
            LOGGER.error("异常",ex);
            return src;
        }
    }

}
