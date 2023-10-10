package com.wisedu.casp.portal.template.cus.official.model;

import com.wisedu.minos.casp.portal.model.LangResoure;

import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title TemplateBasicPage
 * @Author: u
 * @Date: 2020/9/28
 */
public class TemplateBasicPage {
    private List<ColorBiz> primaryColor;
    private List<ColorBiz> fontColor;
    private Map<String,String> logoImgSrc;
    private List<LangResoure> navbarTitle;
    private String footer;

    public List<ColorBiz> getPrimaryColor () {
        return primaryColor;
    }

    public void setPrimaryColor (List<ColorBiz> primaryColor) {
        this.primaryColor = primaryColor;
    }

    public List<ColorBiz> getFontColor () {
        return fontColor;
    }

    public void setFontColor (List<ColorBiz> fontColor) {
        this.fontColor = fontColor;
    }

    public Map<String, String> getLogoImgSrc() {
        return logoImgSrc;
    }

    public void setLogoImgSrc(Map<String, String> logoImgSrc) {
        this.logoImgSrc = logoImgSrc;
    }

    public List<LangResoure> getNavbarTitle() {
        return navbarTitle;
    }

    public void setNavbarTitle(List<LangResoure> navbarTitle) {
        this.navbarTitle = navbarTitle;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }
}
