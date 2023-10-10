package com.wisedu.minos.casp.portal.common.utils;

import com.alibaba.fastjson.JSON;
import com.wisedu.minos.casp.portal.common.annotations.excel.ExcelField;
import com.wisedu.minos.casp.portal.common.annotations.excel.ExcelSheet;
import com.wisedu.minos.casp.portal.common.annotations.excel.ExcelTitle;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 功能描述：Excel工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ExcelUtil
 * @Author: jcx
 * @Date: 2020/7/24
 */
@Component
public class ExcelUtil {

    private static final Logger log = LogManager.getLogger(ExcelUtil.class);

    private final static String EXCEL2003L = ".xls";    //2003- 版本的excel
    private final static String EXCEL2007U = ".xlsx";   //2007+ 版本的excel

    private ExcelUtil () {
        //过sonar检查
    }

    /**
     * @param list      要导出的数据
     * @param c         要导出的数据对应实体类
     * @param excelName excel自定义名称
     * @Author jcx
     * @Description 导出excel公共方法（单Sheet）
     * @Date 9:45 2020/4/17
     **/
    public static byte[] exportInfo (HttpServletResponse response, List list, Class c, String excelName, boolean isCreateWbByte) {

        byte[] bytes = null;
        try {
            if ( null == list || null == c ) {
                return bytes;
            }
            boolean flag = true;
            HSSFWorkbook wb = getHSSFWorkbook(flag, new HSSFWorkbook(), list, c);
            if ( ! isCreateWbByte ) {
                createHSS(wb, response, excelName);
            } else {
                //HSSFWorkbook 对象转换成流 以适配生成的文件以流上传服务器方法
                return createHSSFWorkbookByte(wb);
            }
        } catch ( Exception e ) {
            log.debug(excelName + "导出Excel失败:" + e);
        }
        return bytes;
    }

    /**
     * @Author jcx
     * @Description HSSFWorkbook 对象转换成流 以适配生成的文件以流上传服务器方法
     * @Date 19:50 2020/6/17
     **/
    private static byte[] createHSSFWorkbookByte (HSSFWorkbook wb) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        wb.write(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * @param lists  各个sheet的数据和对象
     * @param cLists 各个sheet的对象
     * @Author jcx
     * @Description 导出excel公共方法（多sheet）
     * @Date 10:53 2020/4/17
     **/
    public static byte[] exportInfos (HttpServletResponse response, List<List> lists, List<Class> cLists, String excelName, boolean isCreateWbByte) {
        byte[] bytes = null;
        try {
            if ( null == lists || null == cLists ) {
                return bytes;
            }
            HSSFWorkbook wb = returnHssWb(lists, cLists);
            if ( ! isCreateWbByte ) {
                createHSS(wb, response, excelName);
            } else {
                //HSSFWorkbook 对象转换成流 以适配生成的文件以流上传服务器方法
                return createHSSFWorkbookByte(wb);
            }
        } catch ( Exception e ) {
            log.debug(excelName + "导出Excel失败:" + e);
        }
        return bytes;
    }

    /***
     * @Author jcx
     * @Description 创建HSSWorkbook对象
     * @Date 15:08 2020/7/30
     * @Param wb:
     * @Param response:
     * @Param excelName:
     * @return void
     **/
    private static void createHSS (HSSFWorkbook wb, HttpServletResponse response, String excelName) throws IOException {
        String currentDate = DateUtil.getStrFromDate(CommonUtil.getSystemDate(), "yyyyMMddHHmmss");
        createHSSFWorkbook(wb, response, excelName + currentDate + ".xls");
    }

    /**
     * @Author jcx
     * @Description 多个sheet建立
     * @Date 10:45 2020/4/17
     **/
    private static HSSFWorkbook returnHssWb (List<List> lists, List<Class> cLists) throws Exception {
        //创建Excel
        HSSFWorkbook wb = new HSSFWorkbook();
        boolean flag = false;
        if ( CollectionUtils.isNotEmpty(lists) && CollectionUtils.isNotEmpty(cLists) ) {
            for ( int i = 0 ; i < lists.size() ; i++ ) {
                getHSSFWorkbook(flag, wb, lists.get(i), cLists.get(i));
            }
        }
        return wb;
    }

    /**
     * @Author jcx
     * @Description Excel导出，支持动态显示列，复杂表头的构建
     * @Date 9:14 2020/3/12
     * @Param * list Class
     **/
    private static HSSFWorkbook getHSSFWorkbook (boolean flag, HSSFWorkbook wb, List list, Class c) throws Exception {
        /**
         * 记录需要动态增加列的列号和列标题
         */
        Map cglab = new HashMap();
        //创建一个Excel 兼容多sheet
        if ( flag == false ) {
            wb = new HSSFWorkbook();
        }
        //ExcelSheet里面存放了Sheet名
        ExcelSheet excelSheet = ( ExcelSheet ) c.getAnnotation(ExcelSheet.class);
        //excelSheet.value()获取sheet名然后设置sheet名
        HSSFSheet sheet = wb.createSheet(excelSheet.value());

        //ExcelTitle里面存放了Title名
        ExcelTitle excelTitle = ( ExcelTitle ) c.getAnnotation(ExcelTitle.class);
        String excelTitleName = "";
        if ( null != excelTitle ) {
            excelTitleName = excelTitle.value();
        }
        //设置列宽度大小
        sheet.setDefaultColumnWidth(( short ) 15 * 1);
        //设置行高
        sheet.setDefaultRowHeight(( short ) (15 * 20));
        //index标记表头列号
        int index = 0;
        //标记表的行号
        int indexRow = 0;
        HSSFRow hssfRow = null;
        List<Class> classList = new ArrayList();//定义一个存放类的栈
        List<Integer> classListHeadIndex = new ArrayList();//定义一个存放每个类开始位置
        classList.add(c);
        classListHeadIndex.add(0);
        //创建表头

        ExcelParam excelParam = new ExcelParam();
        excelParam.setClassList(classList);
        excelParam.setExcelTitle(excelTitle);
        excelParam.setHssfRow(hssfRow);
        excelParam.setSheet(sheet);
        excelParam.setIndexRow(indexRow);
        excelParam.setWb(wb);
        excelParam.setExcelTitleName(excelTitleName);
        excelParam.setClassListHeadIndex(classListHeadIndex);
        excelParam.setIndex(index);
        excelParam.setList(list);
        excelParam.setCglab(cglab);

        ExcelParam creatTabExcelParam = creatTableField(excelParam);

        //处理表头合并
        if ( null != excelParam.getExcelTitle() )
            downRange(creatTabExcelParam.getSheet(), creatTabExcelParam.getIndex(), creatTabExcelParam.getWb());

        //表内容
        ExcelParam creatTableExcelParam = creatTableContent(creatTabExcelParam);
        setAutoSizeColumn(creatTableExcelParam.getSheet(), creatTableExcelParam.getIndex());
        return creatTableExcelParam.getWb();
    }

    /**
     * @Author jcx
     * @Description 创建表头
     * @Date 16:43 2020/5/25
     **/
    private static ExcelParam creatTableField (ExcelParam excelParam) throws Exception {

        List<Class> classList = excelParam.getClassList();
        ExcelTitle excelTitle = excelParam.getExcelTitle();
        HSSFRow hssfRow = excelParam.getHssfRow();
        HSSFSheet sheet = excelParam.getSheet();
        int indexRow = excelParam.getIndexRow();
        HSSFWorkbook wb = excelParam.getWb();
        List<Integer> classListHeadIndex = excelParam.getClassListHeadIndex();
        int index = excelParam.getIndex();

        HSSFCell headCell = null;

        HSSFCellStyle hssfCellStyle = setHeadStyle(wb);
        while ( CollectionUtils.isNotEmpty(classList) ) {
            int indexTemp = classList.size();//有多少向下扩展的属性
            //标题行创建（这里判断NULL是否有标题行注解，决定是否需要添加标题）
            ExcelParam excelTitleSec = getExcelTitle(excelParam);
            hssfRow = excelTitleSec.getHssfRow();
            sheet = excelTitleSec.getSheet();
            indexRow = excelTitleSec.getIndexRow();
            wb = excelTitleSec.getWb();

            //计算标题行列数，方便下面合并
            int titleCellNum = 0;
            Field[] fieldList = classList.get(0).getDeclaredFields();
            titleCellNum = getTitleCellNum(fieldList, titleCellNum);
            if ( null != excelTitle ) {
                sheet.addMergedRegion(new CellRangeAddress(hssfRow.getRowNum(),
                        hssfRow.getRowNum(), hssfRow.getRowNum(), titleCellNum - 1));
            }
            hssfRow = sheet.createRow(indexRow++);

            //class c里面放有不需要动态增加的表头信息
            for ( int k = 0 ; k < indexTemp ; k++ ) {
                Field[] fields = classList.remove(0).getDeclaredFields();
                int indexCell = classListHeadIndex.remove(0);
                for ( int i = 0 ; i < fields.length ; i++ ) {
                    ExcelField excelField = fields[ i ].getAnnotation(ExcelField.class);
                    if ( excelField != null ) {
                        headCell = hssfRow.createCell(indexCell++);
                        //设置表头的样式
                        headCell.setCellStyle(hssfCellStyle);
                        //设置单元格内容
                        headCell.setCellValue(excelField.value());
                    } else {
                        continue;
                    }
                    if ( ! "".equals(excelField.className()) ) {
                        Class classTemp = Class.forName(excelField.className());
                        classList.add(classTemp);//向栈中添加一个类
                        classListHeadIndex.add(indexCell - 1);
                        int count = countLeaf(classTemp);
                        //创建单元格合并
                        CellRangeAddress cellRangeAddress = new CellRangeAddress(indexRow - 1, indexRow - 1, indexCell - 1, indexCell + count - 2);
                        sheet.addMergedRegion(cellRangeAddress);
                        //设置合并后单元格的样式
                        setRangeStyle(cellRangeAddress, sheet);
                        indexCell = indexCell + count - 1;
                    }
                }
                index = indexCell;
            }
        }

        excelParam.setSheet(sheet);
        excelParam.setIndex(index);
        excelParam.setWb(wb);
        excelParam.setHeadCell(headCell);
        excelParam.setIndexRow(indexRow);
        excelParam.setHssfRow(hssfRow);
        excelParam.setHeadCell(headCell);
        return excelParam;
    }

    /**
     * @Author jcx
     * @Description 获得标题行列数
     * @Date 15:09 2020/7/30
     * @Param fieldList:
     * @Param titleCellNum:
     * @return int
     **/
    private static int getTitleCellNum (Field[] fieldList, int titleCellNum) {
        for ( int q = 0 ; q < fieldList.length ; q++ ) {
            ExcelField excelField = fieldList[ q ].getAnnotation(ExcelField.class);
            if ( excelField != null ) {
                titleCellNum++;
            }
        }
        return titleCellNum;
    }

    /**
     * @Author jcx
     * @Description 获得标题行
     * @Date 15:09 2020/7/30
     * @Param excelParam:
     * @return ExcelParam
     **/
    private static ExcelParam getExcelTitle (ExcelParam excelParam) {
        ExcelTitle excelTitle = excelParam.getExcelTitle();
        HSSFRow hssfRow = excelParam.getHssfRow();
        HSSFSheet sheet = excelParam.getSheet();
        int indexRow = excelParam.getIndexRow();
        HSSFWorkbook wb = excelParam.getWb();
        String excelTitleName = excelParam.getExcelTitleName();
        //标题行创建（这里判断NULL是否有标题行注解，决定是否需要添加标题）
        if ( null != excelTitle ) {
            hssfRow = sheet.createRow(indexRow++);
            CellStyle titleStyle = wb.createCellStyle();//标题样式
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            HSSFFont ztFont = wb.createFont();
            ztFont.setItalic(false);                     // 设置字体为斜体字
            ztFont.setColor(Font.COLOR_NORMAL);            // 将字体设置为“红色”
            ztFont.setFontHeightInPoints(( short ) 16);    // 将字体大小设置为18px
            ztFont.setFontName("宋体");             // 将“宋体”字体应用到当前单元格上
            ztFont.setBold(true);//加粗
            titleStyle.setFont(ztFont);
            hssfRow.setHeightInPoints(30);
            Cell titleCell = hssfRow.createCell(0);
            titleCell.setCellStyle(titleStyle);
            titleCell.setCellValue(excelTitleName);

        }
        excelParam.setHssfRow(hssfRow);
        excelParam.setSheet(sheet);
        excelParam.setIndexRow(indexRow);
        excelParam.setWb(wb);
        return excelParam;
    }

    /**
     * @Author jcx
     * @Description 表内容
     * @Date 16:53 2020/5/25
     **/
    private static ExcelParam creatTableContent (ExcelParam creatTabExcelParam) throws Exception {
        List list = creatTabExcelParam.getList();
        HSSFSheet sheet = creatTabExcelParam.getSheet();
        int indexRow = creatTabExcelParam.getIndexRow();
        int index = creatTabExcelParam.getIndex();
        HSSFWorkbook wb = creatTabExcelParam.getWb();
        HSSFCell headCell = creatTabExcelParam.getHeadCell();
        HSSFRow hssfRowData = null;
        HSSFCellStyle hssfCellStyle = setDataStyle(wb);
        HSSFCellStyle style = setHeadStyle(wb);
        for ( int i = 0 ; i < list.size() ; i++ ) {
            hssfRowData = sheet.createRow(indexRow++);
            Object object = list.get(i);
            Field[] fieldDatas = object.getClass().getDeclaredFields();
            int temp = 0;//标记数据项列号
            creatTabExcelParam.setFieldDatas(fieldDatas);
            addContent(object, hssfRowData, temp, wb, hssfCellStyle, style, creatTabExcelParam);
        }
        creatTabExcelParam.setSheet(sheet);
        creatTabExcelParam.setIndex(index);
        creatTabExcelParam.setWb(wb);
        creatTabExcelParam.setHeadCell(headCell);
        return creatTabExcelParam;
    }

    /**
     * @Author jcx
     * @Description 添加表内容
     * @Date 15:10 2020/7/30
     * @Param object:
     * @Param hssfRowData:
     * @Param temp:
     * @Param wb:
     * @Param hssfCellStyle:
     * @Param style:
     * @Param creatTabExcelParam:
     * @return void
     **/
    private static void addContent (Object object, HSSFRow hssfRowData, int temp, HSSFWorkbook wb, HSSFCellStyle hssfCellStyle,
                                    HSSFCellStyle style, ExcelParam creatTabExcelParam) throws Exception {
        HSSFCell hssfCell = null;
        HSSFCell headCell = creatTabExcelParam.getHeadCell();
        Map cglab = creatTabExcelParam.getCglab();
        int index = creatTabExcelParam.getIndex();
        HSSFRow hssfRow = creatTabExcelParam.getHssfRow();
        Field[] fieldDatas = creatTabExcelParam.getFieldDatas();
        creatTabExcelParam.setObject(object);

        for ( int j = 0 ; j < fieldDatas.length ; j++ ) {
            Type fieldType = fieldDatas[ j ].getType();//获取属性类型
            String fieldName = fieldDatas[ j ].getName();//获取属性名称
            //获取动态增加列的信息
            if ( "java.util.List".equals(fieldType.getTypeName()) ) {
                Map tempMap = getMap(( List ) getFieldValueByFieldName(fieldName, object));
                List obExcelFieldList = ( List ) tempMap.get("obExcelField");
                List obFieldValueList = ( List ) tempMap.get("obFieldValue");
                for ( int k = 0 ; k < obExcelFieldList.size() ; k++ ) {
                    String obExcelField = ( String ) obExcelFieldList.get(k);
                    String obFieldValue = ( String ) obFieldValueList.get(k);
                    Integer clIndex = ( Integer ) cglab.get(obExcelField);
                    if ( null == clIndex ) {
                        clIndex = index++;
                        cglab.put(obExcelField, clIndex);
                        headCell = hssfRow.createCell(clIndex);
                        /**
                         * 设置外边框和设置外边框的值
                         */
                        headCell.setCellStyle(style);
                        headCell.setCellValue(obExcelField);
                    }
                    hssfCell = hssfRowData.createCell(clIndex);
                    hssfCell.setCellStyle(hssfCellStyle);
                    hssfCell.setCellValue(obFieldValue);
                }
            }
            /**
             * 添加数据项
             */
            ExcelField excelField = fieldDatas[ j ].getAnnotation(ExcelField.class);

            creatTabExcelParam.setExcelField(excelField);

            temp = setExcelFieldValue(fieldDatas[ j ].getName(), creatTabExcelParam, fieldDatas[ j ].getType(),
                    hssfRowData, temp, hssfCellStyle);
        }
    }

    private static int setExcelFieldValue (String fieldDatasName, ExcelParam creatTabExcelParam, Class<?> type,
                                           HSSFRow hssfRowData, int temp, HSSFCellStyle hssfCellStyle) throws ClassNotFoundException {
        HSSFCell hssfCell = null;
        ExcelField excelField = creatTabExcelParam.getExcelField();
        Object object = creatTabExcelParam.getObject();
        if ( excelField != null ) {
            if ( ! "".equals(excelField.className()) ) {
                List valueList = new ArrayList();
                Object o = getFieldValueByFieldName(fieldDatasName, object);
                if ( o == null ) {
                    for ( int k = 0 ; k < countLeaf(type.getClass()) ; k++ ) {
                        valueList.add("");
                    }
                } else {
                    valueLeaf(valueList, o);
                }
                for ( int k = 0 ; k < valueList.size() ; k++ ) {
                    hssfCell = hssfRowData.createCell(temp++);
                    Object value = valueList.get(k);
                    hssfCell.setCellStyle(hssfCellStyle);
                    setValue(hssfCell, value);//设置值
                }
            } else {
                hssfCell = hssfRowData.createCell(temp++);
                Object value = getFieldValueByFieldName(fieldDatasName, object);
                hssfCell.setCellStyle(hssfCellStyle);
                setValue(hssfCell, value);//设置值
            }
        }
        return temp;
    }

    /**
     * 设置合并后的单元格样式
     */
    private static void setRangeStyle (CellRangeAddress cellRangeAddress, HSSFSheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet); // 下边框
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet); // 左边框
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet); // 有边框
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet); // 上边框
    }

    /**
     * 单元格向下合并
     */
    private static void downRange (HSSFSheet sheet, int clounm, HSSFWorkbook workbook) {

        HSSFRow row = sheet.getRow(1);
        int rowNum = sheet.getLastRowNum();
        for ( int i = 0 ; i <= clounm ; i++ ) {
            if ( row == null ) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(0, rowNum, i, i);
                sheet.addMergedRegion(cellRangeAddress);
                setRangeStyle(cellRangeAddress, sheet);
            }
        }
    }

    /**
     * 为单元格样式添加外边框属性和居中属性
     * @param style 单元格样式对象
     */
    private static void setBord (HSSFCellStyle style) {
        //设置下边框
        style.setBorderBottom(BorderStyle.THIN);
        //设置左边框
        style.setBorderLeft(BorderStyle.THIN);
        //设置右边框
        style.setBorderRight(BorderStyle.THIN);
        //设置下边框
        style.setBorderTop(BorderStyle.THIN);
        //设置居中样式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    /**
     * 设置表头单元格样式
     */
    private static HSSFCellStyle setHeadStyle (HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        setBord(style);
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints(( short ) 12);
        font.setBold(true);//加粗
        style.setFont(font);//设置字体
        //设置背景色
        return style;
    }

    /**
     * 设置数据项单元格样式的样式
     *
     * @param wb Excel对象
     */
    private static HSSFCellStyle setDataStyle (HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        setBord(style);
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints(( short ) 11);
        style.setFont(font);
        return style;
    }

    /**
     * 设置表格内容的值
     *
     * @param hssfCell 单元格对象
     * @param value    单元格的值
     */
    private static void setValue (HSSFCell hssfCell, Object value) {
        if ( value instanceof String ) {
            hssfCell.setCellValue(value.toString());
        } else if ( value instanceof Integer ) {
            hssfCell.setCellValue(( Integer ) value);
        } else if ( value instanceof Double ) {
            hssfCell.setCellValue(( Double ) value);
        } else if ( value instanceof Boolean ) {
            hssfCell.setCellValue(( Boolean ) value);
        } else if ( value instanceof Float ) {
            hssfCell.setCellValue(( Float ) value);
        } else if ( value instanceof Short ) {
            hssfCell.setCellValue(( Short ) value);
        } else if ( value instanceof Long ) {
            hssfCell.setCellValue(( Long ) value);
        } else if ( value instanceof Character ) {
            hssfCell.setCellValue(( Character ) value);
        }
    }

    /**
     * 设置自动列宽
     *
     * @param hssfSheet
     * @param size
     */
    private static void setAutoSizeColumn (HSSFSheet hssfSheet, int size) {
        for ( int i = 0 ; i < size ; i++ ) {
            hssfSheet.autoSizeColumn(i, true);
        }
    }

    /**
     * 深搜
     * 获取这个类下带ExcelField注解的子节点个数
     *
     * @param c
     * @return
     * @throws ClassNotFoundException
     */
    private static int countLeaf (Class c) throws ClassNotFoundException {
        Field fields[] = c.getDeclaredFields();
        int ans = 0;
        for ( int i = 0 ; i < fields.length ; i++ ) {
            ExcelField excelField = fields[ i ].getAnnotation(ExcelField.class);
            if ( excelField != null ) {
                if ( ! "".equals(excelField.className()) ) {
                    ans += countLeaf(Class.forName(excelField.className()));
                } else {
                    ans++;
                }
            }
        }
        return ans;
    }

    /**
     * 深搜
     * 获取叶子节点的值
     *
     * @param list 传入的list
     * @param o    传入的object
     */
    private static void valueLeaf (List list, Object o) {
        Class c = o.getClass();
        Field fields[] = c.getDeclaredFields();
        for ( int i = 0 ; i < fields.length ; i++ ) {
            ExcelField excelField = fields[ i ].getAnnotation(ExcelField.class);
            if ( excelField != null ) {
                if ( ! "".equals(excelField.className()) ) {
                    valueLeaf(list, getFieldValueByFieldName(fields[ i ].getName(), o));
                } else {
                    list.add(getFieldValueByFieldName(fields[ i ].getName(), o));
                }

            }
        }
    }

    /**
     * 获取map
     *
     * @param list
     * @return
     */
    private static Map getMap (List list) {
        Map map = new LinkedHashMap();
        List obExcelFieldList = new ArrayList();
        List obobFieldValueList = new ArrayList();
        for ( int i = 0 ; i < list.size() ; i++ ) {
            Object ob = list.get(i);
            Class c = ob.getClass();
            Field[] fields = c.getDeclaredFields();

            for ( int j = 0 ; j < fields.length ; j++ ) {
                String tempName = fields[ j ].getName();
                if ( "obExcelField".equals(tempName) ) {
                    obExcelFieldList.add(( String ) getFieldValueByFieldName(fields[ j ].getName(), ob));
                }
                if ( "obFieldValue".equals(tempName) ) {
                    obobFieldValueList.add(( String ) getFieldValueByFieldName(fields[ j ].getName(), ob));
                }
            }
        }
        map.put("obExcelField", obExcelFieldList);
        map.put("obFieldValue", obobFieldValueList);
        return map;
    }

    /**
     * 通过属性名字，调用相应的Get方法获取属性值
     *
     * @param object
     * @param fieldName 属性名字
     * @return
     */
    private static Object getFieldValueByFieldName (String fieldName, Object object) {
        Class c = object.getClass();
        try {
            //设置对象的访问权限，保证对private的属性的访问
            String s = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method method = c.getMethod("get" + s);
            return method.invoke(object);
        } catch ( Exception e ) {
            log.debug("通过属性名字，调用相应的Get方法获取属性值失败：" + e);
        }
        return null;
    }

    /**
     * @return
     * @Author jcx
     * @Description 以流的形式创建excel文档
     * @Date 9:25 2020/3/12
     * @Param * @param
     **/
    public static void createHSSFWorkbook (HSSFWorkbook wb, HttpServletResponse response, String fileName) throws IOException {
        ServletOutputStream out = null;
        try {
            // 设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + ";filename*=utf-8''"
                    + URLEncoder.encode(fileName, "UTF-8"));
            out = response.getOutputStream();
            wb.write(out);
        } catch ( Exception e ) {
            log.debug("以流的形式创建excel文档失败：" + e);
        } finally {
            if ( out != null ) {
                out.close();
            }
        }
    }


    /**
     * @return List<List < Object>>
     * @Author jcx
     * @Description Excel导入 可指定sheetName  sheetName为空，则默认取第一个sheet页数据
     * @Date 16:58 2020/7/24
     * @Param request:
     * @Param sheetName:sheet页名称
     * @Param fileTypeName: 前端定义的上传文件控件的名称
     **/
    public static List<List<Object>> getDataFromExcel (HttpServletRequest request, String sheetName, String fileTypeName) {
        List<List<Object>> listObject = null;
        MultipartHttpServletRequest multipartRequest = ( MultipartHttpServletRequest ) request;
        MultipartFile file = multipartRequest.getFile(fileTypeName);
        if ( file != null ) {//不为空
            InputStream in = null;
            try {
                in = file.getInputStream();
                listObject = ExcelUtil.getObjectListFromExcel(in, file.getOriginalFilename(), sheetName);

            } catch ( Exception e ) {
                log.debug("Excel导入失败：" + e);
            } finally {
                try {
                    if ( in != null ) {
                        in.close();
                    }
                } catch ( Exception e ) {
                    log.error("关闭流发生异常：" + e);
                }
            }
        }
        return listObject;
    }


    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     *
     * @param in
     * @param fileName
     * @param sheetName 指定sheetName
     * @return
     * @throws IOException
     */
    private static List<List<Object>> getObjectListFromExcel (InputStream in, String fileName, String sheetName) throws IOException {

        Workbook work = getWorkbook(in, fileName);//创建Excel工作薄

        List<List<Object>> list = null;
        if ( work != null ) {
            list = getSheetData(work, sheetName);//获取所有数据
            for ( int i = list.size() - 1 ; i >= 0 ; i-- ) {
                List<Object> objects = list.get(i);
                boolean flag = true;
                for ( int j = 0 ; j < objects.size() ; j++ ) {
                    String str = String.valueOf(objects.get(j));
                    if ( ! "".equals(str) ) {
                        flag = false;
                    }
                }
                if ( flag ) {
                    list.remove(objects);
                }
            }
        }
        return list;
    }


    /**
     * 根据sheetName获取对应的数据，sheetName为空，则默认获取第一个sheet页
     *
     * @param work
     * @param sheetName
     */
    private static List<List<Object>> getSheetData (Workbook work, String sheetName) {

        Sheet sheet = null;
        List<List<Object>> list = new ArrayList<>();
        if ( StringUtils.isBlank(sheetName) ) {//sheetName为空，则默认取第一个

            sheet = work.getSheetAt(0);//获取Sheet页
        } else {
            for ( int i = 0 ; i < work.getNumberOfSheets() ; i++ ) {

                Sheet sheetFor = work.getSheetAt(i);//获取Sheet页
                if ( sheetFor != null && sheetName.equals(sheetFor.getSheetName()) ) {//获取对应的sheet页
                    sheet = sheetFor;
                    break;
                }
            }
        }
        if ( sheet != null ) {
            getLi(sheet, list);//遍历当前sheet中的所有行
        }
        return list;
    }

    /**
     * 遍历当前sheet中的所有行，将列存储到list里去
     *
     * @param sheet
     * @param list
     */
    private static void getLi (Sheet sheet, List<List<Object>> list) {

        int lastCellNum = 0;//每行的列数，以表头的列数为准，其他行列数超过的自动不获取
        for ( int j = sheet.getFirstRowNum() ; j <= sheet.getLastRowNum() ; j++ ) {

            Row row = sheet.getRow(j);//行数
            if ( row != null ) {
                List<Object> li = new ArrayList<>();

                if ( j == sheet.getFirstRowNum() ) {// 将第一行的列数设为最大
                    lastCellNum = row.getLastCellNum();
                }
                for ( int y = row.getFirstCellNum() ; y < lastCellNum ; y++ ) {//遍历所有的列
                    Cell cell = row.getCell(y);//列数
                    li.add(getValue(cell));
                }
                list.add(li);
            }
        }
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     *
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    private static Workbook getWorkbook (InputStream inStr, String fileName) throws IOException {

        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));

        if ( EXCEL2003L.equals(fileType) ) {
            wb = new HSSFWorkbook(inStr);  //2003-
        } else if ( EXCEL2007U.equals(fileType) ) {
            wb = new XSSFWorkbook(inStr);  //2007+
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     *
     * @param cell
     * @return
     */
    private static String getValue (Cell cell) {
        String value = "";

        if ( cell != null ) {
            value = getValueByType(cell);
        }
        if ( "null".endsWith(value.trim()) ) {
            value = "";
        }
        return value;
    }

    /**
     * 对值进行分类处理
     *
     * @param cell
     * @return
     */
    private static String getValueByType (Cell cell) {
        String value = "";

        switch ( cell.getCellType() ) {

            case NUMERIC://数值型
                value = getNumberRic(cell);
                break;

            case STRING://字符串类型
                value = cell.getStringCellValue().trim();
                break;

            case FORMULA:// 公式类型
                value = String.valueOf(cell.getNumericCellValue());//读公式计算值
                if ( "NaN".equals(value) ) {// 如果获取的数据值为非法值,则转换为获取字符串
                    value = cell.getStringCellValue().trim();
                }
                break;
            case BOOLEAN:// 布尔类型
                value = " " + cell.getBooleanCellValue();
                break;
            default:
                value = cell.getStringCellValue().trim();
                break;
        }
        return value;
    }

    /**
     * 处理number类型的数值
     *
     * @param cell
     * @return
     */
    private static String getNumberRic (Cell cell) {

        String value = "";
        if ( org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell) ) {
            //如果是date类型则 ，获取该cell的date值
            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue());
            // 根据自己的实际情况，excel表中的时间格式是yyyy-MM-dd HH:mm:ss还是yyyy-MM-dd，或者其他类型
            // 由于方法的返回值类型为String，这里将Date类型转为String，便于统一返回数据
            value = DateUtil.getStrFromDate(date, "yyyy-MM-dd HH:mm:ss");
        } else {// 纯数字

            DecimalFormat df = new DecimalFormat("0");
            value = df.format(cell.getNumericCellValue());
            //解决1234.0  去掉后面的.0
            if ( null != value && ! "".equals(value.trim()) ) {
                String[] item = value.split("[.]");
                if ( 1 < item.length && "0".equals(item[ 1 ]) ) {
                    value = item[ 0 ];
                }
            }
        }
        return value;
    }



    //------------------------------------------第二种方式导出Excel----------------------------------------------------------
    /**
     * 导出excel头部标题
     * @param title
     * @param cellRangeAddressLength
     * @return
     */
    public static HSSFWorkbook makeExcelHead(String sheetName,String title,int cellRangeAddressLength){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle styleTitle = createStyle(workbook, (short)16);
        HSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(25);
        if(StringUtil.isNotEmpty(title)){
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, cellRangeAddressLength);
            sheet.addMergedRegion(cellRangeAddress);
            HSSFRow rowTitle = sheet.createRow(0);
            HSSFCell cellTitle = rowTitle.createCell(0);
            // 为标题设置背景颜色
            styleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleTitle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
            cellTitle.setCellValue(title);
            cellTitle.setCellStyle(styleTitle);
        }
        return workbook;
    }
    /**
     * 设定二级标题
     * @param workbook
     * @param secondTitles
     * @return
     */
    public static HSSFWorkbook makeSecondHead(HSSFWorkbook workbook, String[] secondTitles){
        // 创建属性栏
        HSSFSheet sheet = workbook.getSheetAt(0);
        //添加边框
        HSSFRow rowField = sheet.getRow(0)!=null?sheet.createRow(1):sheet.createRow(0);
        HSSFCellStyle styleField = createStyle(workbook, (short)13);
        setBorderThin(styleField);
        for (int i = 0; i < secondTitles.length; i++) {
            HSSFCell cell = rowField.createCell(i);
            cell.setCellValue(secondTitles[i]);
            cell.setCellStyle(styleField);
        }
        return workbook;
    }

    /**
     * @Author jcx
     * @Description 设置边框
     * @Date 10:07 2020/8/18
     * @Param styleField:
     * @return void
     **/
    private static void setBorderThin(HSSFCellStyle styleField){
        styleField.setBorderBottom(BorderStyle.THIN); //下边框
        styleField.setBorderLeft(BorderStyle.THIN);//左边框
        styleField.setBorderTop(BorderStyle.THIN);//上边框
        styleField.setBorderRight(BorderStyle.THIN);//右边框
    }

    /**
     * 插入数据
     * @param workbook
     * @param dataList
     * @param beanPropertys
     * @return
     */
    public static <T> HSSFWorkbook exportExcelData(HSSFWorkbook workbook, List<T> dataList, String[] beanPropertys) {
        HSSFSheet sheet = workbook.getSheetAt(0);
        // 填充数据
        HSSFCellStyle styleData = workbook.createCellStyle();
        styleData.setAlignment(HorizontalAlignment.CENTER);
        styleData.setVerticalAlignment(VerticalAlignment.CENTER);
        //自动换行
        styleData.setWrapText(true);
        //设置边框
        setBorderThin(styleData);
        int rowDataIndex=sheet.getRow(1)!=null?2:1;
        for (int j = 0; j < dataList.size(); j++) {
            HSSFRow rowData = sheet.createRow(j+rowDataIndex);
            T t = dataList.get(j);
            for(int k=0; k<beanPropertys.length; k++){
                Map<String, Object> map = ( Map<String, Object> ) JSON.toJSON(t);
                String value = null!=map.get(beanPropertys[ k ])?String.valueOf(map.get(beanPropertys[ k ])):"";
                HSSFCell cellData = rowData.createCell(k);
                cellData.setCellValue(value);
                cellData.setCellStyle(styleData);
            }
        }
        return workbook;
    }
    /**
     * 使用批量导入方法时，请注意需要导入的Bean的字段和excel的列一一对应
     * @param clazz
     * @param file
     * @param beanPropertys
     * @return
     */
    public static <T> List<T> parserExcel(Class<T> clazz, File file, String[] beanPropertys) {
        // 得到workbook
        List<T> list = new ArrayList<T>();
        try {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            // 直接从第三行开始获取数据
            int rowSize = sheet.getPhysicalNumberOfRows();
            if(rowSize > 2){
                for (int i = 2; i < rowSize; i++) {
                    T t = clazz.newInstance();
                    Row row = sheet.getRow(i);
                    int cellSize = row.getPhysicalNumberOfCells();
                    for(int j=0; j<cellSize; j++){

                        Object cellValue = getValueByType(row.getCell(j));
                        org.apache.commons.beanutils.BeanUtils.copyProperty(t, beanPropertys[j], cellValue);
                    }

                    list.add(t);

                }
            }

        } catch (Exception e) {
            log.info("context",e);
        }
        return list;

    }


    /**
     * 提取公共的样式
     * @param workbook
     * @param fontSize
     * @return
     */
    private static HSSFCellStyle createStyle(HSSFWorkbook workbook, short fontSize){
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 创建一个字体样式
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setBold(true);//加粗
        style.setFont(font);
        return style;
    }
}

