package com.wisedu.minos.casp.portal.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;

/**
 * 功能描述：Md5工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title StructureTransitionUtil
 * @Author: jcx
 * @Date: 2020/7/24
 */
public class Md5Util {

    private static final Logger log = LogManager.getLogger(Md5Util.class);

    private static final String CHARSET = "UTF-8";
    private static final char[] HEX_DIGIT = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * @Author jcx
     * @Description MD5加密
     * @Date 16:10 2020/7/30
     * @Param str:
     * @return String
     **/
    public static String makeMD5(String str) {
        String resultStr = "";
        try {
            byte[] buf = str == null ? new byte[0] : str.getBytes(CHARSET);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buf);
            byte[] result = md.digest();
            int count = result.length;
            StringBuilder strBuf = new StringBuilder(count * 2);
            for (int i = 0; i < count; i++) {
                byte b = result[i];
                strBuf.append(HEX_DIGIT[(b >>> 4) & 0xf])
                        .append(HEX_DIGIT[b & 0xf]);
            }
            resultStr = strBuf.toString();
        } catch (Exception ex) {
            log.error("MD5加密失败："+ ex);
        }
        return resultStr;
    }

    private Md5Util (){
        //过sonar检查
    }

}
