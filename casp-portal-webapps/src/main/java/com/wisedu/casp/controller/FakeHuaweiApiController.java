package com.wisedu.casp.controller;

import com.alibaba.fastjson.JSONObject;

import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FakeHuaweiApiController extends AbstractController {
    private static final Logger logger = LogManager.getLogger(FakeHuaweiApiController.class);
    @RequestMapping(value = {"/getLicense"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Object getLicense(HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        obj.put("resultCode", "000000");
        obj.put("resultMsg", "success");
        obj.put("license", UUID.randomUUID().toString());
        String body = obj.toJSONString();
        try {
            String key = "ebf72724-bacc-414b-a86b-fda5bcc8153c";
            String sign = generateResponseBodySignature(key, body);
            response.setHeader("Body-Sign", "sign_type=\"HMAC-SHA256\", signature= \"" + sign + "\"");
        } catch (Exception e) {
            logger.warn("编码异常", e);
        }
        return body;
    }

    @RequestMapping(value = {"/saasAppOperate"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Object saasAppOperate(HttpServletResponse response, HttpServletRequest request) {
        String activity = request.getParameter("activity");
        String key = "ebf72724-bacc-414b-a86b-fda5bcc8153c";
        JSONObject obj = new JSONObject();
        obj.put("resultCode", "000000");
        obj.put("resultMsg", "success");
        if ("newInstance".equals(activity)) {
            obj.put("encryptType", "1");
            obj.put("instanceId", UUID.randomUUID().toString());
            JSONObject appInfo = new JSONObject();
            appInfo.put("frontEndUrl", "http://caspportal.wisedu.com");
            appInfo.put("adminUrl", "http://caspconsole.wisedu.com");
            String username = "sysadmin";
            String crUname = generateSaaSUsernameOrPwd(key, username, 32);
            appInfo.put("userName", crUname);
            String passwd = "123456";
            String crpwd = generateSaaSUsernameOrPwd(key, passwd, 32);
            appInfo.put("password", crpwd);
        }
        String body = obj.toJSONString();
        try {
            String sign = generateResponseBodySignature(key, body);
            response.setHeader("Body-Sign", "sign_type=\"HMAC-SHA256\", signature= \"" + sign + "\"");
        } catch (Exception e) {
            logger.warn("编码异常", e);
        }
        return body;
    }

    public static String getRandomChars(int length) {
        String randomChars = "";
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            if (random.nextInt(2) % 2 == 0) {
                int letterIndex = random.nextInt(2) % 2 == 0 ? 65 : 97;
                randomChars = randomChars + (char) (random.nextInt(26) + letterIndex);
            } else {
                randomChars = randomChars + String.valueOf(random.nextInt(10));
            }
        }
        return randomChars;
    }

    public static String generateSaaSUsernameOrPwd(String key, String str, int encryptLength) {
        String iv = getRandomChars(16);
        String afterEncryptStr = "";
        try {
            afterEncryptStr = encryptAESCBCEncode(str, key, iv, encryptLength);
        } catch (Exception e) {
            logger.warn("AES加密异常", e);
        }
        return iv + afterEncryptStr;
    }

    public static String encryptAESCBCEncode(String content, String key, String iv, int encryptLength) throws Exception {
        if ((StringUtils.isEmpty(content)) || (StringUtils.isEmpty(key)) || (StringUtils.isEmpty(iv))) {
            return null;
        }
        return base64(encryptAESCBC(content.getBytes(), key.getBytes(), iv.getBytes(), encryptLength));
    }

    public static byte[] encryptAESCBC(byte[] content, byte[] keyBytes, byte[] iv, int encryptLength) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(keyBytes);
        keyGenerator.init(encryptLength, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, key, new IvParameterSpec(iv));
        byte[] result = cipher.doFinal(content);
        return result;
    }

    public static String generateResponseBodySignature(String key, String body) throws Exception {
        return base64(hmacSHA256(key, body));
    }

    public static String base64(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    public static byte[] hmacSHA256(String macKey, String macData) throws Exception {
        SecretKeySpec secret = new SecretKeySpec(macKey.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secret);
        byte[] doFinal = mac.doFinal(macData.getBytes("UTF-8"));
        return doFinal;
    }
}