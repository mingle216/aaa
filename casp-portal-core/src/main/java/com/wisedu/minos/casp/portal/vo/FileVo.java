package com.wisedu.minos.casp.portal.vo;

import com.wisedu.minos.casp.portal.common.constant.ValidationConstant;
import com.wisedu.minos.casp.portal.inter.CommonMethod;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：文件相关参数类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title FileVo
 * @Author: jcx
 * @Date: 2020/8/17
 */
public class FileVo extends CardAjaxRequest {

    //文件相对路径 例:/files/20200817/15973970786160441.doc
    @NotBlank(message = ValidationConstant.FILE_PATH_NAME_NOT_NULL,groups = { CommonMethod.class})
    private String filePathName;
    //文件真实名字 例：XXXX报告.doc
    @NotBlank(message = ValidationConstant.FILE_TRUE_NAME_NOT_NULL,groups = { CommonMethod.class})
    private String fileTrueName;


    //sheet页名称
    private String sheetName;
    //标题名称(可为空)
    private String title;
    //二级标题
    private String[] secondTitles;
    //字段名称
    private String[] beanProperty;
    //导出Excel的名称
    private String fileName;
    //导出数据
    private List result=new ArrayList();

    public String getSheetName () {
        return sheetName;
    }

    public void setSheetName (String sheetName) {
        this.sheetName = sheetName;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String[] getSecondTitles () {
        return secondTitles;
    }

    public void setSecondTitles (String[] secondTitles) {
        this.secondTitles = secondTitles;
    }

    public String[] getBeanProperty () {
        return beanProperty;
    }

    public void setBeanProperty (String[] beanProperty) {
        this.beanProperty = beanProperty;
    }

    public String getFileName () {
        return fileName;
    }

    public void setFileName (String fileName) {
        this.fileName = fileName;
    }

    public List getResult () {
        return result;
    }

    public void setResult (List result) {
        this.result = result;
    }

    public String getFilePathName () {
        return filePathName;
    }

    public void setFilePathName (String filePathName) {
        this.filePathName = filePathName;
    }

    public String getFileTrueName () {
        return fileTrueName;
    }

    public void setFileTrueName (String fileTrueName) {
        this.fileTrueName = fileTrueName;
    }
}
