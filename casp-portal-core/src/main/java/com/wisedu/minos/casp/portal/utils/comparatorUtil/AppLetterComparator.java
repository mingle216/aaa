package com.wisedu.minos.casp.portal.utils.comparatorUtil;

import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.vo.AppAppraiseSummaryVo;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2021/12/22
 */

public class AppLetterComparator implements Comparator<AppAppraiseSummaryVo> {
    /**
     * 26个英文字母
     */
    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    Pattern pattern = Pattern.compile("[0-9]*");

    @Override
    public int compare(AppAppraiseSummaryVo o1, AppAppraiseSummaryVo o2) {
        //按首字母英文字典序(首字母英文的最前面，然后数字，最后特殊字符)
        //字母-133  数字-58
        String headCharOne = headChar(o1.getServiceName()).toLowerCase();
        String headCharTwo = headChar(o2.getServiceName()).toLowerCase();
        int headOne=(int)headCharOne.toCharArray()[0];
        int headTwo=(int)headCharTwo.toCharArray()[0];
        if(LETTERS.contains(headCharOne)){
            headOne = headOne-133;
        }else if(pattern.matcher(headCharTwo).matches()){
            headOne = headOne - 58;
        }
        if(LETTERS.contains(headCharTwo)){
            headTwo = headTwo-133;
        }else if(pattern.matcher(headCharTwo).matches()){
            headTwo = headTwo - 58;
        }
        return headOne-headTwo;
    }

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
