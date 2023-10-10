package com.wisedu.minos.casp.portal.i18n;

import java.util.List;
import java.util.Locale;

public interface I18nService {

    /**
     * getSupportLanguages
     * <p/>
     * 获取当前支持的语言种类
     *
     * @param
     * @return List<Lang>
     * @throws
     * @date 2020/9/10 12:39
     */
    List<Lang> getSupportLanguages();

    /**
     * getCurrentLanguage
     * <p/>
     * 获取用户当前选择的语言
     *
     * @param
     * @return java.util.Locale
     * @throws
     * @date 2020/9/25 13:49
     */
    Locale getCurrentLanguage();



    /**
     * getSupportDisplayLanguages
     * <p/>
     * 获取当前支持的语言种类 用于前台展示 每种语言的展示名称使用该语言描述  例如:
     *  zh_CN  中文
     *  en_US  English
     *  ru_RU  русский язык 
     *  fr_FR  Français
     *  de_DE  Deutsch
     * @param
     * @return List<Lang>
     * @throws
     * @date 2020/9/10 12:39
     */
    List<Lang> getSupportDisplayLanguages();
}
