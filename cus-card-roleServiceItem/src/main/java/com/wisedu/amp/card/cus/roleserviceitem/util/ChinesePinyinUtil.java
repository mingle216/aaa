package com.wisedu.amp.card.cus.roleserviceitem.util;

import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import net.sourceforge.pinyin4j.PinyinHelper;

public class ChinesePinyinUtil {


    /**
     * 得到中文首字母
     *
     * @param str 中文字符串
     * @return
     */
    public static String headChar(String str) {
        if (StringUtil.isEmpty(str)) {
            return "#";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char word = str.charAt(i);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                sb.append(pinyinArray[0].charAt(0));
            } else {
                sb.append(word);
            }
        }

        return sb.toString().toUpperCase().substring(0, 1);
    }


}
