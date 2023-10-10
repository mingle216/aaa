package com.wisedu.minos.casp.portal.vo;

import com.wisedu.minos.casp.portal.dao.entity.CardEntity;
import com.wisedu.minos.casp.portal.dao.entity.TemplateEntity;

import java.io.Serializable;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CardTemUpVo
 * @Author: d
 * @Date: 2020/9/23
 */
public class CardTemUpVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean uploadResult = false;
    private String copyFilePath = "";
    private String uploadType="";
    private String upLoadFilePath="";
    private String uploadTypeName="";
    private CardEntity cardEntityfromPro;
    private CardEntity dbCardEntityfromPro;
    private TemplateEntity templateEntityfromPro;
    private TemplateEntity dbTemplateEntityfromPro;
    private String  isRestart;
    private String  isCancel;
    //是否是新包，而不是覆盖
    private Boolean  isNewCardTem=true;

    public Boolean getNewCardTem() {
        return isNewCardTem;
    }

    public void setNewCardTem(Boolean newCardTem) {
        isNewCardTem = newCardTem;
    }

    public String getIsRestart () {
        return isRestart;
    }

    public void setIsRestart (String isRestart) {
        this.isRestart = isRestart;
    }

    public String getIsCancel () {
        return isCancel;
    }

    public void setIsCancel (String isCancel) {
        this.isCancel = isCancel;
    }

    public boolean isUploadResult () {
        return uploadResult;
    }

    public void setUploadResult (boolean uploadResult) {
        this.uploadResult = uploadResult;
    }

    public String getCopyFilePath () {
        return copyFilePath;
    }

    public void setCopyFilePath (String copyFilePath) {
        this.copyFilePath = copyFilePath;
    }

    public String getUploadType () {
        return uploadType;
    }

    public void setUploadType (String uploadType) {
        this.uploadType = uploadType;
    }

    public String getUpLoadFilePath () {
        return upLoadFilePath;
    }

    public void setUpLoadFilePath (String upLoadFilePath) {
        this.upLoadFilePath = upLoadFilePath;
    }

    public String getUploadTypeName () {
        return uploadTypeName;
    }

    public void setUploadTypeName (String uploadTypeName) {
        this.uploadTypeName = uploadTypeName;
    }

    public CardEntity getCardEntityfromPro () {
        return cardEntityfromPro;
    }

    public void setCardEntityfromPro (CardEntity cardEntityfromPro) {
        this.cardEntityfromPro = cardEntityfromPro;
    }

    public CardEntity getDbCardEntityfromPro () {
        return dbCardEntityfromPro;
    }

    public void setDbCardEntityfromPro (CardEntity dbCardEntityfromPro) {
        this.dbCardEntityfromPro = dbCardEntityfromPro;
    }

    public TemplateEntity getTemplateEntityfromPro () {
        return templateEntityfromPro;
    }

    public void setTemplateEntityfromPro (TemplateEntity templateEntityfromPro) {
        this.templateEntityfromPro = templateEntityfromPro;
    }

    public TemplateEntity getDbTemplateEntityfromPro () {
        return dbTemplateEntityfromPro;
    }

    public void setDbTemplateEntityfromPro (TemplateEntity dbTemplateEntityfromPro) {
        this.dbTemplateEntityfromPro = dbTemplateEntityfromPro;
    }
}
