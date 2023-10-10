/**
 *
 */
package com.wisedu.minos.casp.portal.common.utils.rsa;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名称：解密SDK
 * <p>应用管理平台</p>
 * <p>江苏金智教育股份有限公司</p>
 * <p>类说明：用于解密应用管理平台生成的证书。</p>
 *
 * @author AMP
 * @version 1.0
 */
public class CerDecrypt {
    private CerDecrypt() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CerDecrypt.class);

    private static final String ALGORITHM_RSA = "RSA";

    /**
     * 保存生成的密钥对的文件名称。
     */
    private static final String RSA_PUBLIC_KEY_FILENAME = "com/wisedu/minos/casp/portal/common/utils/rsa/PublicKey";

    /**
     * 缓存的密钥对。
     */
    private static RSAPublicKey publicKey = null;


    /**
     * 公钥钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, Key key)
            throws Exception {
        if (key instanceof RSAPublicKey) {
            RSAPublicKey publicKey = (RSAPublicKey) key;
            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            //模长
            int keyLen = publicKey.getModulus().bitLength() / 8;

            String rsaEncryptedData = decodeBase64String(data);
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

        return null;
    }

    /*
     * 获取解密公钥。
     */
    public static Key getPublicKey() {

        if (publicKey == null) {
            try {
                ClassLoader classLoader = CerDecrypt.class.getClassLoader();
                InputStream fis = classLoader.getResourceAsStream(RSA_PUBLIC_KEY_FILENAME);

                ObjectInputStream oos = new ObjectInputStream(fis);
                publicKey = (RSAPublicKey) oos.readObject();

                oos.close();
                fis.close();
            } catch (Exception e) {
                LOGGER.error("getPublicKey failed.", e);
            }
        }

        return publicKey;

    }

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

    public static String decodeBase64String(String data) {

        byte[] bytes = Base64.decodeBase64(data);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            result.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }

        return result.toString();
    }


}
