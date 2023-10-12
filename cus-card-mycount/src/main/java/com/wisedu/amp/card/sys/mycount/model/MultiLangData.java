package com.wisedu.amp.card.sys.mycount.model;

/**
 * @ClassName MultiLangData
 * @Description
 * @Date 2021/1/27 0027 16:25
 * @Author zkpu
 * @Version 1.0
 **/
public class MultiLangData {

    private String langKey;

    private String langValue;

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getLangValue() {
        return langValue;
    }

    public void setLangValue(String langValue) {
        this.langValue = langValue;
    }

    public MultiLangData(String langKey, String langValue) {
        this.langKey = langKey;
        this.langValue = langValue;
    }

    public MultiLangData() {
    }
}
