package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.common.annotations.excel.ExcelField;
import com.wisedu.minos.casp.portal.common.annotations.excel.ExcelTitle;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：Excel工具类所需参数
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ExcelParam
 * @Author: jcx
 * @Date: 2020/7/23
 */
public class ExcelParam {
    private List<Class> classList;
    private ExcelTitle excelTitle;
    private ExcelField excelField;
    private HSSFRow hssfRow;
    private HSSFSheet sheet;
    private int indexRow;
    private HSSFWorkbook wb;
    private HSSFCell headCell;
    private Map cglab;
    private List list;
    private String excelTitleName;
    private List<Integer> classListHeadIndex;
    private int index;
    private int temp;
    private Field[] fieldDatas;
    private Object object;

    public List<Class> getClassList () {
        return classList;
    }

    public void setClassList (List<Class> classList) {
        this.classList = classList;
    }

    public ExcelTitle getExcelTitle () {
        return excelTitle;
    }

    public void setExcelTitle (ExcelTitle excelTitle) {
        this.excelTitle = excelTitle;
    }

    public ExcelField getExcelField () {
        return excelField;
    }

    public void setExcelField (ExcelField excelField) {
        this.excelField = excelField;
    }

    public HSSFRow getHssfRow () {
        return hssfRow;
    }

    public void setHssfRow (HSSFRow hssfRow) {
        this.hssfRow = hssfRow;
    }

    public HSSFSheet getSheet () {
        return sheet;
    }

    public void setSheet (HSSFSheet sheet) {
        this.sheet = sheet;
    }

    public int getIndexRow () {
        return indexRow;
    }

    public void setIndexRow (int indexRow) {
        this.indexRow = indexRow;
    }

    public HSSFWorkbook getWb () {
        return wb;
    }

    public void setWb (HSSFWorkbook wb) {
        this.wb = wb;
    }

    public HSSFCell getHeadCell () {
        return headCell;
    }

    public void setHeadCell (HSSFCell headCell) {
        this.headCell = headCell;
    }

    public Map getCglab () {
        return cglab;
    }

    public void setCglab (Map cglab) {
        this.cglab = cglab;
    }

    public List getList () {
        return list;
    }

    public void setList (List list) {
        this.list = list;
    }

    public String getExcelTitleName () {
        return excelTitleName;
    }

    public void setExcelTitleName (String excelTitleName) {
        this.excelTitleName = excelTitleName;
    }

    public List<Integer> getClassListHeadIndex () {
        return classListHeadIndex;
    }

    public void setClassListHeadIndex (List<Integer> classListHeadIndex) {
        this.classListHeadIndex = classListHeadIndex;
    }

    public int getIndex () {
        return index;
    }

    public void setIndex (int index) {
        this.index = index;
    }

    public int getTemp () {
        return temp;
    }

    public void setTemp (int temp) {
        this.temp = temp;
    }

    public Field[] getFieldDatas () {
        return fieldDatas;
    }

    public void setFieldDatas (Field[] fieldDatas) {
        this.fieldDatas = fieldDatas;
    }

    public Object getObject () {
        return object;
    }

    public void setObject (Object object) {
        this.object = object;
    }
}
