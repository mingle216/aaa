package com.wisedu.minos.casp.portal.i18n;

import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Lang
 * <p/>
 * 多语言类型
 * @author hyluan
 * @date 2020/9/10 13:11
 * Copyright (c) 2018 wisedu
 */
@NoArgsConstructor
public class Lang implements Serializable {

    String langName;

    String langCname;

    String langCode;

    boolean isDefault;

    public Lang(String langName, String langCname,String langCode, boolean isDefault) {
        this.langName = langName;
        this.langCname = langCname;
        this.langCode = langCode;
        this.isDefault = isDefault;
    }

    public String getLangCname () {
        return langCname;
    }

    public void setLangCname (String langCname) {
        this.langCname = langCname;
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName.trim();
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode.trim();
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
