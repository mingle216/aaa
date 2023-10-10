package com.wisedu.minos.casp.portal.common.utils.rsa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author 01212021
 * @time 2016年1月27日上午10:48:56
 */
public class RSATool {
    private final static Log LOGGER = LogFactory.getLog(RSATool.class);
    private final static char[] BCD_LOOKUP = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//    private static final Base64.Encoder BASE_64 = new Base64.Encoder();
    private String pri = null;
    private String pub = null;

    public String bytesToHexStr(byte[] bcd) {
        StringBuilder s = new StringBuilder(bcd.length * 2);
        for (int i = 0; i < bcd.length; ++i) {
            s.append(BCD_LOOKUP[bcd[i] >>> 4 & 0xF]);
            s.append(BCD_LOOKUP[bcd[i] & 0xF]);
        }
        return s.toString();
    }

    public byte[] hexStrToBytes(String s) {
        byte[] bytes = new byte[s.length() / 2];

        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
                    16);
        }

        return bytes;
    }

    public void genRSAKeyPair() {
        KeyPairGenerator rsaKeyGen = null;
        KeyPair rsaKeyPair = null;
        try {
            rsaKeyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = new SecureRandom();
            random.setSeed(System.currentTimeMillis());
            rsaKeyGen.initialize(1024, random);
            rsaKeyPair = rsaKeyGen.genKeyPair();
            PublicKey rsaPublic = rsaKeyPair.getPublic();
            PrivateKey rsaPrivate = rsaKeyPair.getPrivate();
            this.pub = bytesToHexStr(rsaPublic.getEncoded());
            this.pri = bytesToHexStr(rsaPrivate.getEncoded());
        } catch (Exception e) {
            LOGGER.error("Exception genRSAKeyPair:" + e);
        }
    }

    public String generateSHA1withRSASigature(String src, String priKey) {
        Signature sigEng;
        try {
            sigEng = Signature.getInstance("SHA1withRSA");

            byte[] pribyte = hexStrToBytes(priKey.trim());

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);

            KeyFactory fac = KeyFactory.getInstance("RSA");

            RSAPrivateKey privateKey = (RSAPrivateKey) fac
                    .generatePrivate(keySpec);
            sigEng.initSign(privateKey);
            sigEng.update(src.getBytes("UTF-8"));

            byte[] signature = sigEng.sign();
            return bytesToHexStr(signature);
        } catch (Exception e) {
            LOGGER.error("Exception generateSHA1withRSASigature:" + e);
        }
        return null;
    }

    public boolean verifySHA1withRSASigature(String sign, String src,
                                             String pubKeyStr) {
        Signature sigEng;
        try {
            sigEng = Signature.getInstance("SHA1withRSA");

            byte[] pubbyte = hexStrToBytes(pubKeyStr.trim());

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPublicKey pubKey = (RSAPublicKey) fac.generatePublic(keySpec);

            sigEng.initVerify(pubKey);
            sigEng.update(src.getBytes("UTF-8"));

            byte[] sign1 = hexStrToBytes(sign);
            return sigEng.verify(sign1);
        } catch (Exception e) {
            LOGGER.error("Exception verifySHA1withRSASigature:" + e);
        }
        return false;
    }

    public String encryptWithPriKey(String src, String priKey) {
        byte[] pribyte;
        try {
            pribyte = hexStrToBytes(priKey.trim());

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);

            KeyFactory fac = KeyFactory.getInstance("RSA");
            Key privateKey = fac.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, privateKey);

            byte[] signature = cipher.doFinal(src.getBytes("UTF-8"));

            return bytesToHexStr(signature);
        } catch (Exception e) {
            LOGGER.error("Exception encryptWithPriKey:" + e);
        }
        return null;
    }

    public String encryptLongTextWithPriKey(String src, String priKey) {
        if (src.length() <= 117) {
            return encryptWithPriKey(src, priKey);
        }
        StringBuilder sb = new StringBuilder();
        int idx = 0;
        while (idx < src.length()) {
            int end = (idx + 117 > src.length()) ? src.length() : idx + 117;
            String sub = src.substring(idx, end);
            String encSub = encryptWithPriKey(sub, priKey);
            sb.append(encSub);
            idx += 117;
        }
        return sb.toString();
    }

//    public String encryptWithPriKeyWithBase64(String src, String priKey) {
//        byte[] pribyte;
//        try {
//            pribyte = hexStrToBytes(priKey.trim());
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
//            KeyFactory fac = KeyFactory.getInstance("RSA");
//            Key privateKey = fac.generatePrivate(keySpec);
//            Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(1, privateKey);
//            byte[] signature = cipher.doFinal(src.getBytes("UTF-8"));
//
//            return base64.encode(signature).replaceAll("[^a-zA-Z0-9+/=]", "");
//        } catch (Exception e) {
//            LOGGER.error("Exception encryptWithPriKeyWithBase64:" + e);
//        }
//        return null;
//    }

    public String encryptWithPubKey(String src, String pubKey) {
        byte[] pubbyte;
        try {
            pubbyte = hexStrToBytes(pubKey.trim());

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);

            KeyFactory fac = KeyFactory.getInstance("RSA");
            Key publicKey = fac.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, publicKey);
            byte[] signature = cipher.doFinal(src.getBytes("UTF-8"));

            return bytesToHexStr(signature);
        } catch (Exception e) {
            LOGGER.error("Exception encryptWithPubKey:" + e);
        }
        return null;
    }

    public String decryptWithPriKey(String enc, String priKey) {
        byte[] pribyte;
        try {
            pribyte = hexStrToBytes(priKey.trim());

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) fac
                    .generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, privateKey);

            byte[] forumcookie = hexStrToBytes(enc);

            byte[] plainText = cipher.doFinal(forumcookie);

            return bytesToHexStr(plainText);
        } catch (Exception e) {
            LOGGER.error("Exception decryptWithPriKey:" + e);
        }
        return null;
    }

    public String decryptWithPubKey(String enc, String pubKey) {
        byte[] pubbyte;
        try {
            pubbyte = hexStrToBytes(pubKey.trim());

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            Key publicKey = fac.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, publicKey);

            byte[] forumcookie = hexStrToBytes(enc);

            byte[] plainText = cipher.doFinal(forumcookie);

            return new String(plainText, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("Exception decryptWithPubKey:" + e);
        }
        return null;
    }

    public RSAPrivateKey getPriKey(String priKey) {
        byte[] pribyte;
        try {
            pribyte = hexStrToBytes(priKey.trim());

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPrivateKey key = (RSAPrivateKey) fac.generatePrivate(keySpec);
            return key;
        } catch (Exception e) {
            LOGGER.error("Exception getPriKey:" + e);
        }
        return null;
    }

    public RSAPublicKey getPubKey(String pubKey) {
        byte[] pubbyte;
        try {
            pubbyte = hexStrToBytes(pubKey.trim());

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPublicKey key = (RSAPublicKey) fac.generatePublic(keySpec);
            return key;
        } catch (Exception e) {
            LOGGER.error("Exception getPubKey:" + e);
        }
        return null;
    }

    public String getPri() {
        return this.pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }

    public String getPub() {
        return this.pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }
}
