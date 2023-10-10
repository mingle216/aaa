package com.wisedu.minos.casp.portal.utils;

/**
 * 功能描述：卡片相关工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CardUtil
 * @Author: jcx
 * @Date: 2020/8/03
 */
public class CommunalUtil {

    // 雪花算法生成器
    private static SnowflakeIdWorker sf = new SnowflakeIdWorker();
    /**
     * 获取一个WID
     *
     * @return
     */
    public static String getWid() {
        return String.valueOf(sf.nextId());
    }
}
