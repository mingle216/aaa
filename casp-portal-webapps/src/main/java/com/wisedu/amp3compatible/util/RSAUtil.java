/**
 * 
 */
package com.wisedu.amp3compatible.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 类名称：RSA工具类
 * <p>应用管理平台</p>
 * <p>江苏金智教育股份有限公司</p>
 * <p>类说明：用于门户与Cloud功能调用中的加密处理（针对页面级别的应用，对url相关参数进行加解密处理）</p>
 * 
 * @author 丁窍
 * @version 1.0	创建时间：2015年12月2日下午8:06:16	丁窍	发布
 */
abstract class RSAUtil {
	
	private static final String ALGORITHM_RSA = "RSA";

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
            mi.append(bcd2Str(cipher.doFinal(s.getBytes())));
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
			mi.append(bcd2Str(cipher.doFinal(s.getBytes())));
		}
		//return mi;

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
        byte[] bytes = data.getBytes();  
        byte[] bcd = asciiToBcd(bytes, bytes.length);
//        System.err.println(bcd.length);  
        //如果密文长度大于模长则要分组解密  
        StringBuilder ming = new StringBuilder();
        byte[][] arrays = splitArray(bcd, keyLen);
        for(byte[] arr : arrays){  
            ming.append(new String(cipher.doFinal(arr)));
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
    	
        byte[] bytes = rsaEncryptedData.getBytes();  
        byte[] bcd = asciiToBcd(bytes, bytes.length);
//        System.err.println(bcd.length);  
        //如果密文长度大于模长则要分组解密  
        StringBuilder ming = new StringBuilder();
        byte[][] arrays = splitArray(bcd, keyLen);
        for(byte[] arr : arrays){  
            ming.append(new String(cipher.doFinal(arr)));
        }  
        return ming.toString();
    }

    /** 
     * ASCII码转BCD码 
     *  
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
        for (int i=0; i<x+z; i++) {  
            if (i==x+z-1 && y!=0) {  
                str = string.substring(i*len, i*len+y);  
            }else{  
                str = string.substring(i*len, i*len+len);  
            }  
            strings[i] = str;  
        }  
        return strings;  
    }
    
    /** 
     *拆分数组  
     */  
    private static byte[][] splitArray(byte[] data,int len){  
        int x = data.length / len;  
        int y = data.length % len;  
        int z = 0;  
        if(y!=0){  
            z = 1;  
        }  
        byte[][] arrays = new byte[x+z][];  
        byte[] arr;  
        for(int i=0; i<x+z; i++){  
            arr = new byte[len];  
            if(i==x+z-1 && y!=0){  
                System.arraycopy(data, i*len, arr, 0, y);  
            }else{  
                System.arraycopy(data, i*len, arr, 0, len);  
            }  
            arrays[i] = arr;  
        }  
        return arrays;  
    } 

    /**
     * Return public RSA key modulus
     *
     * @param keyPair
     *            RSA keys
     * @return modulus value as hex string
     */
    public static String getPublicKeyModulus(KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey.getModulus().toString();
    }

    /**
     * Return public RSA key exponent
     *
     * @param keyPair
     *            RSA keys
     * @return public exponent value as hex string
     */
    public static String getPublicKeyExponent(KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey.getPublicExponent().toString();
    }

    /**
     * Max block size with given key length
     *
     * @param keyLength
     *            length of key
     * @return numeber of digits
     */
    public static int getMaxDigits(int keyLength) {
        return ((keyLength * 2) / 16) + 3;
    }

    /**
     * Convert byte array to hex string
     *
     * @param bytes
     *            input byte array
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
     * @param data
     *            input string data
     * @return bytes
     */
    public static byte[] hexStringToByteArray(String data) {
        int k = 0;
        byte[] results = new byte[data.length() / 2];
        for (int i = 0; i < data.length();) {
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

}
