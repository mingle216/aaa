package com.wisedu.minos.casp.portal.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.wisedu.minos.casp.portal.common.baseservice.CaspSafetyService;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.*;
import com.wisedu.minos.casp.portal.dao.entity.*;
import com.wisedu.minos.casp.portal.dao.mapper.*;
import com.wisedu.minos.casp.portal.model.AttachInfoCustomEntity;
import com.wisedu.minos.casp.portal.model.CardTemplateInstallDto;
import com.wisedu.minos.casp.portal.vo.CardTemUpVo;
import com.wisedu.minos.casp.portal.vo.SwitchVersionVo;
import com.wisedu.minos.gateway.client.bean.LicenseInfo;
import com.wisedu.minos.gateway.client.license.MinosLicenseManager;
import com.wisedu.minos.oss.client.bean.OSSInvokeRequest;
import com.wisedu.minos.oss.client.util.OSSClientUtil;
import com.wisedu.minos.util.MenuEditTypeEnum;
import com.wisedu.minos.util.MinosException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 功能描述：文件相关实现类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title FileService
 * @Author: jcx
 * @Date: 2020/8/14
 */
@Service("FileService")
public class FileService {

    private static final Logger logger = LogManager.getLogger(FileService.class);

    //这里之所以不用@Value注解直接获取配置信息，目的是作为开发底座，如果二次开发人员未配置该信息，@Value注解直接会启动报错
    @Autowired
    private Environment environment;
    //卡片上传路径
    private String cardUploadPath;
    //模板上传路径
    private String templateUploadPath;

    @Autowired
    private OSSClientUtil ossClientUtil;
    @Autowired
    private AttachInfoMapper attachInfoMapper;
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private VersionManagementMapper versionManagementMapper;
    @Autowired
    private AttachEntityRelMapper attachEntityRelMapper;
    @Autowired
    private PluginService pluginService;
    @Autowired
    private PluginInstallService pluginInstallService;
    @Autowired
    private PluginInstallMapper pluginInstallMapper;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private UserConsoleAuthService userConsoleAuthService;


    @Value("${file.uploadPath}")
    private String uploadPath;
    @Value("${file.cardTemplateSize:200M}")
    private String cardTemplateSize;
    @Value("${file.uploadLocalIncoPath}")
    private String uploadLocalIncoPath;
    @Value("${file.cardUploadPath}")
    private String cardLocalUploadPath;
    @Value("${file.templateUploadPath}")
    private String templateLocalUploadPath;
    @Value("${file.systemCardUploadPath}")
    private String systemCardUploadPath;
    @Value("${file.systemTemplateUploadPath}")
    private String systemTemplateUploadPath;
    @Value("${minos.oss.server.addresses}")
    private String minosOssAdress;
    @Autowired
    private ExecutorService executorService;

    @Autowired
    private CaspSafetyService caspSafetyService;

    /**
     * @return Result
     * @Author jcx
     * @Description 单个附件上传服务器指定目录
     * @Date 17:25 2020/8/14
     * @Param file:
     **/
    @Transactional
    public ResultData uploadFile (MultipartFile file, String type) {
        if ( file.isEmpty() ) {
            throw new BusinessException("文件为空，请重新上传！");
        }
        //检测文件后缀是否是合法文件,简单校验
        if ( ! FileUtil.checkSuffix(file.getOriginalFilename(), new String[] { ".zip" }) )
            throw new BusinessException(GlobalEnum.FILE_ILLEGAL.getMsg());
        int alowSize = Integer.valueOf(cardTemplateSize.substring(0, cardTemplateSize.length() - 1));
        String alowUnit = cardTemplateSize.substring(cardTemplateSize.length() - 1, cardTemplateSize.length());
        if ( FileUtil.checkFileSize(file.getSize(), alowSize, alowUnit) ) {
            throw new BusinessException("文件太大，已超过" + cardTemplateSize + "阈值，请重新上传！");
        }
        CardTemUpVo cardTemUpVo = new CardTemUpVo();
        cardTemUpVo.setUploadType(type);
        cardTemUpVo.setUploadTypeName(type.equals(Global.CARD_UPLOAD_TYPE) ? "卡片" : "模板");
        //检查上传类型
        String filePath = checkUploadPath(type);
        //解压的模板文件夹路径
        String jarPathName=filePath+"/"+DateUtil.getCurrentYearMonthDayTime();
        File fileFir = null;
        String uploadTypeName = cardTemUpVo.getUploadTypeName();
        String msg = "非标准" + uploadTypeName + "包，请上传标准的" + uploadTypeName + "包!";
        try {
            if ( StringUtil.isNotEmpty(filePath) ) {
                cardTemUpVo.setUpLoadFilePath(filePath);
                //MultipartFile 转 File，便于操作
                fileFir = FileUtil.multipartFileToFile(file, filePath);
                //上传本地
                String fileName = file.getOriginalFilename();
                // 任意文件写入风险，需要控制写入的文件目录只能是允许的目录
                // add by zhangjian 2021-12-01
                caspSafetyService.legalPath(filePath);
                cardTemUpVo.setUploadResult(FileUtil.uploadFile(file, fileName, filePath));
                //检查卡片/模板是否正确压缩，是否符合规范
                Map<String, Object> checkResult = checkZip(fileFir, jarPathName, cardTemUpVo);
                FileInputStream inputStream = new FileInputStream(fileFir);
                MockMultipartFile localMultipartFile = new MockMultipartFile(fileFir.getName(), inputStream);
                //上传oss作为版本包备份
                AttachInfoCustomEntity attachInfoEntity = uploadFileToOss(localMultipartFile);
                cardTemUpVo.setCopyFilePath(attachInfoEntity.getReUrl().replace(minosOssAdress, "ossAddress"));
                //如果上传的是模板，则需要校验定制模板licnese
                if ( Global.TEMPLATE_UPLOAD_TYPE.equals(cardTemUpVo.getUploadType()) ) {
                    deepCheckTemplate(checkResult,cardTemUpVo);
                }
                if ( ! "normal".equals(checkResult.get("msg")) ) {
                    //存在相同卡片或者模板，返回给前台确认是否覆盖，如果覆盖，则提前创建副本，便于后续操作，因为finally会最终把源文件删除
                    cardTemUpVo.setNewCardTem(false);
                    return ResultData.success(ResultData.DEFAULT_SUCCESS_CODE, checkResult.get("msg").toString(), cardTemUpVo);
                }
                return ResultData.success(ResultData.DEFAULT_SUCCESS_CODE, "当前" + cardTemUpVo.getUploadTypeName() + "包为新" + cardTemUpVo.getUploadTypeName() + "，请确认是否安装!", cardTemUpVo);
            }
        } catch ( BusinessException e ) {
            logger.error(msg + e);
            throw new BusinessException(e.getMessage());
        } catch ( Exception e ) {
            logger.error(msg + e);
            throw new BusinessException(msg);
        } finally {
            //删除临时zip
            FileUtil.deleteFile(fileFir);
            //删除上传到本地的zip
            FileUtil.deletePathFile(Arrays.asList(cardTemUpVo.getUpLoadFilePath() + "/" + file.getOriginalFilename()));
            //删除解压的模板文件夹
            FileUtil.deleteDir(new File(jarPathName));
        }
        throw new MinosException(GlobalEnum.UPLOAD_FILE_ERROR.getMsg());
    }

    /***
     * @Author jcx
     * @Description 检查是否有定制PC模板和定制移动H5模板的license
     * @Date 14:02 2022/9/27
     * @return Map<String,Boolean>
     **/
    private Map<String,Boolean> checkCustTemplateLicense(){
        Map<String,Boolean> result = Maps.newHashMap();
        result.put(Global.PC_CUSTOMIZED,false);
        result.put(Global.MOB_CUSTOMIZED,false);
        LicenseInfo licenseInfo = MinosLicenseManager.getLicenseInfo();
        List<LicenseInfo.App> apps = licenseInfo.getApps();
        if(CollectionUtils.isNotEmpty(apps)){
            apps.forEach(app-> {
                List<LicenseInfo.Module> modules = app.getModules();
                if ("casp".equalsIgnoreCase(app.getId())) {
                    modules.forEach(module -> {
                        if (Global.TEMPLATE_PC_CUSTOMIZED.equals(module.getName())) {
                            result.put(Global.PC_CUSTOMIZED,true);
                        }
                        if (Global.TEMPLATE_H5_CUSTOMIZED.equals(module.getName())) {
                            result.put(Global.MOB_CUSTOMIZED,true);
                        }
                    });
                }
            });
        }else{
            //开发环境
            result.put(Global.PC_CUSTOMIZED,true);
            result.put(Global.MOB_CUSTOMIZED,true);
        }
        return result;
    }

    /***
     * @Author jcx
     * @Description 校验模板lincense合法性
     * @Date 13:54 2022/9/22
     * @Param file:
     * @return void
     **/
    private void deepCheckTemplate(Map<String, Object> checkResult,CardTemUpVo cardTemUpVo){
        //检查是否有定制PC模板和定制移动H5模板的license
        Map<String, Boolean> custTemplateLicense = checkCustTemplateLicense();
        if(!custTemplateLicense.get(Global.PC_CUSTOMIZED)&&!custTemplateLicense.get(Global.MOB_CUSTOMIZED)){
            throw new BusinessException("缺少定制模板license,请更新license之后再进行操作");
        }
        TemplateEntity templateEntity=null;
        if(checkResult.containsKey("properties")){
            if(Global.TEMPLATE_UPLOAD_TYPE.equals(cardTemUpVo.getUploadType())){
                templateEntity= ( TemplateEntity ) checkResult.get("properties");
            }
        }
        if(String.valueOf(Global.PlatformType.PC.getType()).equals(templateEntity.getPlatformType())&&!custTemplateLicense.get(Global.PC_CUSTOMIZED)){
            throw new BusinessException("缺少定制PC模板license,请更新license之后再进行操作");
        }else if(String.valueOf(Global.PlatformType.MOBILE.getType()).equals(templateEntity.getPlatformType())&&!custTemplateLicense.get(Global.MOB_CUSTOMIZED)){
            throw new BusinessException("缺少定制移动H5模板license,请更新license之后再进行操作");
        }else if("0;1".equals(templateEntity.getPlatformType())&&(!custTemplateLicense.get(Global.MOB_CUSTOMIZED)||!custTemplateLicense.get(Global.PC_CUSTOMIZED))){
            throw new BusinessException("缺少定制PC或者移动H5模板license,请更新license之后再进行操作");
        }
        //校验文件合法
//        if(checkResult.containsKey(Global.ZIP_UNCOMPRESSPATH)&&StringUtils.isNotBlank(String.valueOf(checkResult.get(Global.ZIP_UNCOMPRESSPATH)))){
//            List<String> unCompressPath = ( List<String> ) checkResult.get("unCompressPath");
//            AtomicReference<String> unCompressJarPath= new AtomicReference<>("");
//            unCompressPath.forEach(k->{
//                if(k.contains(".jar")){
//                    unCompressJarPath.set(k);
//                }
//            });
//            if(StringUtils.isNotBlank(unCompressJarPath.get())){
//                JarFile jarFile = new JarFile(new File(unCompressJarPath.get()));
//                Enumeration<JarEntry> entries = jarFile.entries();
//                while ( entries.hasMoreElements() ){
//                    JarEntry jarEntry = entries.nextElement();
//                    String name = jarEntry.getName();
//                }
//            }
//        }
    }

    /**
     * @return void
     * @Author jcx
     * @Description 上传卡片、模板到oss后续操作，兼容确认覆盖后的操作
     * @Date 14:38 2020/9/21
     **/
    @Transactional
    public void uploadCardTempToOss (CardTemUpVo cardTemUpVo) {
        InputStream inputStream = null;
        String newFileNamePath = "";
        try {
            if ( null != cardTemUpVo.getIsCancel() && Global.CONSTANT_NO.equals(cardTemUpVo.getIsCancel()) ) {
                File copyFile = null;
                if ( StringUtil.isNotEmpty(cardTemUpVo.getCopyFilePath()) ) {
                    String newFileName = CommonUtil.getPathFileName("zip");
                    newFileNamePath = cardTemUpVo.getUpLoadFilePath() + "/" + newFileName;

                    // 任意文件写入风险，需要控制写入的文件目录只能是允许的目录
                    // add by zhangjian 2021-12-01
                    caspSafetyService.legalPath(newFileNamePath);
                    String ossAddress = cardTemUpVo.getCopyFilePath().replace("ossAddress", minosOssAdress);
                    FileUtil.downloadFileFromURL(ossAddress, cardTemUpVo.getUpLoadFilePath() + "/", newFileName);
                    copyFile = new File(newFileNamePath);
                    inputStream = new FileInputStream(copyFile);
                }

                if ( cardTemUpVo.isUploadResult() ) {
                    //上传本地成功
                    //校验卡片/模板包   及入库操作
                    cardTemplateHandler(copyFile, cardTemUpVo);

                    //异步重启系统
                    if ( null != cardTemUpVo.getIsRestart() && Global.CONSTANT_YES.equals(cardTemUpVo.getIsRestart()) ) {
                        executorService.restartSystem();
                    }
                }
            }
        } catch ( IOException e ) {
            logger.error(e);
        } finally {
            try {
                if ( inputStream != null ) {
                    inputStream.close();
                }
                //删除copy副本
                if ( ! "".equals(newFileNamePath) ) {
                    FileUtil.deletePathFile(Arrays.asList(newFileNamePath));
                }
            } catch ( IOException e ) {
                logger.error(e);
            }

        }
    }

    /**
     * @return void
     * @Author jcx
     * @Description 检查卡片/模板是否正确压缩
     * @Date 9:47 2020/9/19
     * @Param source:
     **/
    private Map<String, Object> checkZip (File source, String jarPath, CardTemUpVo cardTemUpVo) {
        Map<String, Object> result = Maps.newHashMap();
        ZipEntry zipEntry = null;
        String versionNumber = "";
        String dbVersionNumber = "";
        String uploadTypeName = cardTemUpVo.getUploadTypeName();
        String uploadType = cardTemUpVo.getUploadType();
        String msg = "非标准" + uploadTypeName + "包，请上传标准的" + uploadTypeName + "包!";

        ZipFile zipFile=null;
        ZipInputStream zipInputStream=null;
        try {
            if ( Global.TEMPLATE_UPLOAD_TYPE.equals(uploadType) ) {
                List<String> strings = ZipUtil.unCompress(source, jarPath + "/", false);
                result.put(Global.ZIP_UNCOMPRESSPATH,strings);
            }
            zipFile = new ZipFile(source) ;
            zipInputStream = new ZipInputStream(new FileInputStream(source));
            boolean isJar = true;
            boolean isProperties = true;
            //校验原目录中jar文件,查看是否覆盖以前版本
            if ( zipFile.size() != 2 ) {
                throw new BusinessException(msg);
            }
            while ( (zipEntry = zipInputStream.getNextEntry()) != null ) {
                String fileName = zipEntry.getName();
                if ( zipEntry.isDirectory() ) {
                    throw new BusinessException(msg);
                }
                if ( "jar".equals(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase()) ) {
                    isJar = false;
                }
                if ( "properties".equals(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase()) ) {
                    //读取描述文件内容
                    isProperties = false;
                    if ( Global.CARD_UPLOAD_TYPE.equals(uploadType) ) {
                        CardEntity cardEntity = ( CardEntity ) getProPerByStream(zipFile.getInputStream(zipEntry), uploadType);
                        //安装的包一律都为自定义的第三方包
                        cardEntity.setCardSystemType(Global.CARD_SYS_TYPE_CUSTOM);
                        cardTemUpVo.setCardEntityfromPro(cardEntity);
                        String cardId = cardEntity.getCardId();
                        QueryWrapper<CardEntity> wrapper = new QueryWrapper<>();
                        wrapper.eq(DbFieldConstant.CARD_ID, cardId).ne(DbFieldConstant.CARD_STATUS, Global.CARD_STATUS_NOT_AVAILABLE);
                        CardEntity dbCardEntity = cardMapper.selectOne(wrapper);
                        versionNumber = cardEntity.getVersionNumber();
                        dbVersionNumber = getDbVersionByCard(cardTemUpVo, dbVersionNumber, msg, dbCardEntity);
                        result.put(Global.ZIP_PROPERTIES,cardEntity);
                    } else {
                        TemplateEntity templateEntity = ( TemplateEntity ) getProPerByStream(zipFile.getInputStream(zipEntry), uploadType);
                        //安装的包一律都为自定义的第三方包
                        templateEntity.setTemplateSystemType(Global.CARD_SYS_TYPE_CUSTOM);
                        cardTemUpVo.setTemplateEntityfromPro(templateEntity);
                        String templateId = templateEntity.getTemplateId();
                        QueryWrapper<TemplateEntity> wrapper = new QueryWrapper<>();
                        wrapper.eq(DbFieldConstant.TEMPLATE_ID, templateId).ne(DbFieldConstant.TEMPLATE_STATUS, Global.CARD_STATUS_NOT_AVAILABLE);
                        TemplateEntity dbTemplateEntity = templateMapper.selectOne(wrapper);
                        versionNumber = templateEntity.getVersionNumber();
                        dbVersionNumber = getDbVersionByTemplate(cardTemUpVo, dbVersionNumber, msg, dbTemplateEntity);
                        result.put(Global.ZIP_PROPERTIES,templateEntity);
                    }
                }
            }
            if ( isJar || isProperties ) {
                //zip包中没有jar，或者没有描述文件
                throw new BusinessException(msg);
            }
            if ( StringUtil.isNotEmpty(dbVersionNumber) ) {
                //覆盖情况 cardid 一样才算做覆盖
                //当前卡片/模板已存在
                dbVersionNumber = dbVersionNumber.contains("_") ? dbVersionNumber.substring(0, dbVersionNumber.lastIndexOf("_")) : dbVersionNumber;
                versionNumber = versionNumber.contains("_") ? versionNumber.substring(0, versionNumber.lastIndexOf("_")) : versionNumber;
                result.put("msg","当前" + uploadTypeName + "包中的" + uploadTypeName + "已安装&" + dbVersionNumber + "," + versionNumber + "");
                return result;
            }
        } catch ( Exception e ) {
            logger.error("检查卡片/模板是否正确压缩发生异常", e);
            throw new BusinessException(e.getMessage());
        }finally {
            try {
                if(null!=zipFile){
                    zipFile.close();
                }
                if(null!=zipInputStream){
                    zipInputStream.close();
                }
            } catch ( IOException e ) {
                logger.error("zipFile或zipInputStream关闭流异常：{}",e);
            }
        }
        result.put("msg","normal");
        return result;
    }

    private String getDbVersionByTemplate (CardTemUpVo cardTemUpVo, String dbVersionNumber, String msg, TemplateEntity dbTemplateEntity) {
        if ( null != dbTemplateEntity ) {
            // templateId相等 dbsystemType 是系统包  报错
//            if (dbTemplateEntity.getTemplateSystemType().equals(Global.CARD_SYS_TYPE_SYS)) {
//                throw new BusinessException(msg);
//            }
            cardTemUpVo.setDbTemplateEntityfromPro(dbTemplateEntity);
            dbVersionNumber = dbTemplateEntity.getVersionNumber();
        }
        return dbVersionNumber;
    }

    private String getDbVersionByCard (CardTemUpVo cardTemUpVo, String dbVersionNumber, String msg, CardEntity dbCardEntity) {
        if ( null != dbCardEntity ) {
            // cardid相等 dbsystemType 是系统包  报错
//            if (dbCardEntity.getCardSystemType().equals(Global.CARD_SYS_TYPE_SYS)) {
//                throw new BusinessException(msg);
//            }
            cardTemUpVo.setDbCardEntityfromPro(dbCardEntity);
            dbVersionNumber = dbCardEntity.getVersionNumber();
        }
        return dbVersionNumber;
    }

    /**
     * @return String
     * @Author jcx
     * @Description 检查上传类型
     * @Date 11:04 2020/9/18
     * @Param type:
     **/
    private String checkUploadPath (String type) {
        String path = "";
        switch ( type ) {
            case Global.CARD_UPLOAD_TYPE:
                this.cardUploadPath = environment.getProperty("file.cardUploadPath") != null ? environment.getProperty("file.cardUploadPath") : "";
                if ( StringUtil.isEmpty(cardUploadPath) ) {
                    throw new MinosException("卡片包上传失败，因为未配置卡片包上传路径，请按【file.cardUploadPath=XXX/XXX】的格式配置卡片包上传路径");
                }
                path = this.cardUploadPath;
                break;
            case Global.TEMPLATE_UPLOAD_TYPE:
                this.templateUploadPath = environment.getProperty("file.templateUploadPath") != null ? environment.getProperty("file.templateUploadPath") : "";
                if ( StringUtil.isEmpty(templateUploadPath) ) {
                    throw new MinosException("模板包上传失败，因为未配置模板包上传路径，请按【file.templateUploadPath=XXX/XXX】的格式配置模板包上传路径");
                }
                path = this.templateUploadPath;
                break;
            default:
                throw new MinosException("上传参数错误，请检查");
        }
        return path;
    }

    /**
     * @return Result
     * @Author jcx
     * @Description 多个附件上传服务器指定目录
     * @Date 17:36 2020/8/14
     * @Param files:
     **/
    public List<String> uploadFiles (MultipartFile[] files, String type) {
        List<String> result = new ArrayList<>();
        if ( files != null && files.length > 0 ) {
            for ( int i = 0 ; i < files.length ; i++ ) {
                MultipartFile file = files[ i ];
                String fileUrl = null != this.uploadFile(file, type).getData() ? String.valueOf(this.uploadFile(file, type).getData()) : "";
                result.add(fileUrl);
            }
        }
        return result;
    }

    /**
     * @return void
     * @Author jcx
     * @Description 下载服务器指定目录下文件
     * @Date 9:34 2020/8/17
     * @Param response:
     * @Param filePathName: 文件相对路径 例：/cardAccess/20200817/15973970786160441.doc
     * @Param fileTrueName: 文件真实名称 例：XXXX报告.doc
     **/
    public void downloadFile (HttpServletResponse response, String filePathName, String fileTrueName, String type) {
        String fileUploadPath = checkUploadPath(type);
        String path = "";
        if ( filePathName.contains(Global.accessFilePrefix.CARD_ACCESS_PREFIX.getType()) && StringUtil.isNotEmpty(fileUploadPath) ) {
            path = fileUploadPath + "/" + filePathName.substring(filePathName.indexOf(Global.accessFilePrefix.CARD_ACCESS_PREFIX.getType()) + 5, filePathName.lastIndexOf("/"));
        }
        if ( filePathName.contains(Global.accessFilePrefix.TEMPLATE_ACCESS_PREFIX.getType()) && StringUtil.isNotEmpty(fileUploadPath) ) {
            path = fileUploadPath + "/" + filePathName.substring(filePathName.indexOf(Global.accessFilePrefix.TEMPLATE_ACCESS_PREFIX.getType()) + 5, filePathName.lastIndexOf("/"));
        }
        FileUtil.downLocalFile(response, path, filePathName, fileTrueName);
        throw new MinosException(GlobalEnum.DOWNLOAD_FILE_ERROR.getMsg());
    }

    /**
     * @return void
     * @Author jcx
     * @Description 从OSS下载文件
     * @Date 10:23 2020/9/18
     * @Param request:
     * @Param response:
     * @Param attachWid:
     **/
    public void downLoadFileFromOss (HttpServletResponse response, String attachWid) {
        if ( StringUtils.isBlank(attachWid) ) {
            throw new MinosException(GlobalEnum.PARAM_FAIL.getMsg());
        }
        try {
            finalDown(response, attachWid);
        } catch ( Exception e ) {
            logger.error("从OSS下载文件出现异常：" + e);
            throw new MinosException(GlobalEnum.FILE_DOWNLOAD_ERROR.getMsg());
        }
    }

    /**
     * @return void
     * @Author jcx
     * @Description 从OSS下载卡片或者模板包
     * @Date 14:40 2020/10/10
     * @Param response:
     * @Param wid:
     * @Param type: 0:模板  1:卡片
     **/
    public void downCardOrTemFromOss (HttpServletResponse response, String wid, String type) {
        String jarPath = "";
        String propertiesPath = "";
        String zipTempFilePath = "";
        try {
            Map<String, Object> handleResult = handleVer(type, wid);
            CardEntity cardEntity = ( CardEntity ) handleResult.get("cardEntity");
            TemplateEntity templateEntity = ( TemplateEntity ) handleResult.get("templateEntity");
            String versionNum = String.valueOf(handleResult.get("versionNum"));
            String carTemId = String.valueOf(handleResult.get("carTemId"));
            if ( StringUtil.isNotEmpty(versionNum) && StringUtil.isNotEmpty(carTemId) ) {
                //查询版本管理表
                QueryWrapper<VersionManagementEntity> wrapper = new QueryWrapper<>();
                wrapper.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId())
                        .eq(DbFieldConstant.VERSION_STATUS, Global.CONSTANT_YES)
                        .eq(DbFieldConstant.VERSION_NUMBER, versionNum)
                        .eq(DbFieldConstant.VERSION_TYPE, type)
                        .eq(DbFieldConstant.FOREIGN_KEY, carTemId);
                VersionManagementEntity versionManagement = versionManagementMapper.selectOne(wrapper);
                if ( null != versionManagement ) {
                    List<AttachEntityRelEntity> list = queryAttachRel(versionManagement.getWid());
                    if ( CollectionUtils.isNotEmpty(list) ) {
                        //在oss上有备份包的情况
                        finalDown(response, list.get(0).getAttachWid());
                    } else {
                        //在oss上没有备份包的情况，将本地jar及生成的描述文件压缩成zip包并上传oss做备份
                        if ( type.equals(Global.CARD_UPLOAD_TYPE) ) {
                            //卡片
                            cardTemZipHandler(response, jarPath, propertiesPath, zipTempFilePath, cardEntity, versionManagement.getWid(), true);
                        } else {
                            //模板
                            cardTemZipHandler(response, jarPath, propertiesPath, zipTempFilePath, templateEntity, versionManagement.getWid(), true);
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            logger.info("从OSS下载卡片或者模板包异常:", e);
            throw new BusinessException("下载异常，请联系管理员！");
        }
    }

    private Map<String, Object> handleVer (String type, String wid) {
        CardEntity cardEntity = null;
        TemplateEntity templateEntity = null;
        Map<String, Object> result = new HashMap<>();
        String versionNum = "";
        String carTemId = "";
        if ( type.equals(Global.CARD_UPLOAD_TYPE) ) {
            cardEntity = ( CardEntity ) getEntityByType(type, wid);
            if ( null != cardEntity ) {
                versionNum = cardEntity.getVersionNumber();
                carTemId = cardEntity.getCardId();
            }
        } else {
            templateEntity = ( TemplateEntity ) getEntityByType(type, wid);
            if ( null != templateEntity ) {
                versionNum = templateEntity.getVersionNumber();
                carTemId = templateEntity.getTemplateId();
            }
        }
        result.put("versionNum", versionNum);
        result.put("carTemId", carTemId);
        result.put("cardEntity", cardEntity);
        result.put("templateEntity", templateEntity);
        return result;
    }

    private Object getEntityByType (String type, String wid) {
        CardEntity cardEntity = null;
        TemplateEntity templateEntity = null;
        if ( type.equals(Global.CARD_UPLOAD_TYPE) ) {
            //卡片
            QueryWrapper<CardEntity> wrapper = new QueryWrapper<>();
            wrapper.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId()).eq(DbFieldConstant.WID, wid);
            cardEntity = cardMapper.selectOne(wrapper);
            if ( null != cardEntity ) {
                return cardEntity;
            }
        } else if ( type.equals(Global.TEMPLATE_UPLOAD_TYPE) ) {
            //模板
            QueryWrapper<TemplateEntity> wrapper = new QueryWrapper<>();
            wrapper.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId()).eq(DbFieldConstant.WID, wid);
            templateEntity = templateMapper.selectOne(wrapper);
            if ( null != templateEntity ) {
                return templateEntity;
            }
        }
        return null;
    }

    //卡片、模板zip处理
    private <T> void cardTemZipHandler (HttpServletResponse response, String jarPath,
                                        String propertiesPath, String zipTempFilePath, T t, String versionManageWid, boolean isDownLoad) {
        CardEntity cardEntity = null;
        TemplateEntity templateEntity = null;
        if ( t instanceof CardEntity ) {
            cardEntity = ( CardEntity ) t;
        } else if ( t instanceof TemplateEntity ) {
            templateEntity = ( TemplateEntity ) t;
        }
        if ( null != cardEntity ) {
            //卡片
            String jarCardName = cardEntity.getCardId().toLowerCase().replaceAll("_", "-") + "-";
            //下载zip
            exportZip(response, cardEntity.getCardSystemType().equals(Global.CARD_SYS_TYPE_SYS) ? systemCardUploadPath : cardLocalUploadPath, jarCardName, jarPath,
                    propertiesPath, zipTempFilePath, cardEntity, versionManageWid, isDownLoad);

        } else {
            //模板
            String jarTemplateName = templateEntity.getTemplateId().toLowerCase().replaceAll("_", "-") + "-";
            //下载zip
            exportZip(response, templateEntity.getTemplateSystemType().equals(Global.CARD_SYS_TYPE_SYS) ? systemTemplateUploadPath : templateLocalUploadPath, jarTemplateName, jarPath,
                    propertiesPath, zipTempFilePath, templateEntity, versionManageWid, isDownLoad);
        }
    }

    //生成zip并下载
    private <T> void exportZip (HttpServletResponse response, String localUploadPath, String jarName, String jarPath,
                                String propertiesPath, String zipTempFilePath, T t, String versionManageWid, boolean isDownLoad) {
        try {
            File file = new File(localUploadPath);
            File[] fs = file.listFiles();
            if ( fs.length > 0 ) {
                for ( File f : fs ) {    //遍历File[]数组
                    if ( f.getName().toLowerCase().contains(jarName) ) {
                        //匹配jar
                        jarPath = localUploadPath + "/" + f.getName();
                        break;
                    }
                }
                if ( StringUtil.isNotEmpty(jarPath) ) {
                    //得到描述文件Properties实例
                    Properties properties = getProperties(t);
                    String id = properties.getProperty("id");
                    //描述文件路径
                    propertiesPath = localUploadPath + "/" + id + ".properties";
                    //utf-8编码，防止中文乱码，生成描述文件
                    OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(propertiesPath), "utf-8");
                    properties.store(oStreamWriter, "Init properties");
                    oStreamWriter.flush();
                    oStreamWriter.close();
                    List<String> sourceFilePaths = new ArrayList<String>();
                    sourceFilePaths.add(jarPath);
                    sourceFilePaths.add(propertiesPath);
                    //指定打包到哪个zip（绝对路径）
                    zipTempFilePath = localUploadPath + "/" + id + ".zip";

                    // 任意文件写入风险，需要控制写入的文件目录只能是允许的目录
                    // add by zhangjian 2021-12-01
                    caspSafetyService.legalPath(zipTempFilePath);

                    //压缩成zip
                    int sLength = ZipUtil.compress(sourceFilePaths, zipTempFilePath, false);
                    if ( sLength == 2 ) {
                        //成功压缩,上传oss作备份
                        File zipFile = null;
                        InputStream inputStream = null;
                        MultipartFile localMultipartFile = null;
                        zipFile = new File(zipTempFilePath);
                        inputStream = new FileInputStream(zipFile);
                        localMultipartFile = new MockMultipartFile(zipFile.getName(), inputStream);
                        //上传oss作为版本包备份
                        AttachInfoEntity attachInfoEntity = uploadFileToOss(localMultipartFile);
                        saveAttachRel(versionManageWid, attachInfoEntity.getWid());

                        if ( null != attachInfoEntity && isDownLoad ) {
                            //下载
                            finalDown(response, attachInfoEntity.getWid());
                        }
                    }
                }
            } else {
                throw new BusinessException("下载异常，请联系管理员！");
            }
        } catch ( Exception e ) {
            logger.info("从OSS下载卡片或者模板包异常：{}", e);
            throw new BusinessException("下载异常，请联系管理员！");
        } finally {
            //删除生成的描述文件
            handleExportZipFinally(propertiesPath, zipTempFilePath);
        }
    }

    private void handleExportZipFinally (String propertiesPath, String zipTempFilePath) {
        if ( StringUtil.isNotEmpty(propertiesPath) ) {
            FileUtil.deletePathFile(Arrays.asList(propertiesPath));
        }
        //删除生成的zip文件
        if ( StringUtil.isNotEmpty(zipTempFilePath) ) {
            FileUtil.deletePathFile(Arrays.asList(zipTempFilePath));
        }
    }

    //得到描述文件Properties实例
    private <T> Properties getProperties (T t) {
        // 生成实例
        CardEntity cardEntity = null;
        TemplateEntity templateEntity = null;
        if ( t instanceof CardEntity ) {
            cardEntity = ( CardEntity ) t;
        } else if ( t instanceof TemplateEntity ) {
            templateEntity = ( TemplateEntity ) t;
        }
        Properties pro = getProperties(cardEntity, templateEntity);
        return pro;
    }

    private Properties getProperties (CardEntity cardEntity, TemplateEntity templateEntity) {
        Properties pro = new Properties();
        pro.setProperty("id", null != cardEntity ? cardEntity.getCardId() : templateEntity.getTemplateId());
        pro.setProperty("name", null != cardEntity ? cardEntity.getCardName() : templateEntity.getTemplateName());
        pro.setProperty("imageUrl", null != cardEntity ? cardEntity.getImageUrl() : templateEntity.getTemplateImgUrl());
        pro.setProperty("imageUrlMobile", null != cardEntity ? cardEntity.getImageUrlMobile() : templateEntity.getTemplateImgMobileUrl());
        pro.setProperty("platformType", null != cardEntity ? cardEntity.getPlatformType() : templateEntity.getPlatformType());
        pro.setProperty("configurableFlag", null != cardEntity ? String.valueOf(cardEntity.getConfigurableFlag()) : String.valueOf(templateEntity.getConfigurableFlag()));
        pro.setProperty("configurableRuntimeFlag", null != cardEntity ? String.valueOf(cardEntity.getConfigurableRuntimeFlag()) : String.valueOf(templateEntity.getConfigurableRuntimeFlag()));
        pro.setProperty("systemType", null != cardEntity ? String.valueOf(cardEntity.getCardSystemType()) : String.valueOf(templateEntity.getTemplateSystemType()));
        pro.setProperty("desc", null != cardEntity ? (null != cardEntity.getCardDesc() ? cardEntity.getCardDesc() : "") : (null != templateEntity.getTemplateDesc() ? templateEntity.getTemplateDesc() : ""));
        pro.setProperty("versionNumber", null != cardEntity ?
                (cardEntity.getVersionNumber().contains("_") ? cardEntity.getVersionNumber().substring(0, cardEntity.getVersionNumber().lastIndexOf("_")) : cardEntity.getVersionNumber())
                : (templateEntity.getVersionNumber().contains("_") ? templateEntity.getVersionNumber().substring(0, templateEntity.getVersionNumber().lastIndexOf("_")) : templateEntity.getVersionNumber()));
        return pro;
    }

    //根据实体id查询附件
    private List<AttachEntityRelEntity> queryAttachRel (String entityWid) {
        QueryWrapper<AttachEntityRelEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbFieldConstant.ENTITY_WID, entityWid);
        List<AttachEntityRelEntity> attachEntityRelEntities = attachEntityRelMapper.selectList(queryWrapper);
        return attachEntityRelEntities;
    }

    //获取上传后改变后的文件名
    private String getFileNameByLocation (String fileUrl) {
        String indexStr = Global.OSS_FILE_BACKET_USERICON + uploadPath;
        String fileName = fileUrl.substring(indexStr.length() + 9, fileUrl.indexOf("?"));
        return fileName;
    }

    //下载
    private void finalDown (HttpServletResponse response, String attachWid) {
        AttachInfoEntity attachInfoEntity = getAtt(attachWid);
        //根据文件流下载文件
        FileUtil.downLoadFileByByte(response, getFileByte(attachInfoEntity), attachInfoEntity.getFileName());
    }

    private AttachInfoEntity getAtt (String attachWid) {
        return attachInfoMapper.selectById(attachWid);
    }

    //得到远程oss上的文件流
    private byte[] getFileByte (AttachInfoEntity attachInfoEntity) {
        String fileUrl = attachInfoEntity.getFileUrl();
        String fileName = getFileNameByLocation(fileUrl);
        return ossClientUtil.download(Global.OSS_FILE_BACKET_USERICON, attachInfoEntity.getFileLocation(), fileName);
    }

    /**
     * @return Result
     * @Author jcx
     * @Description 上传oss附件至oss
     * @Date 10:31 2020/9/16
     * @Param file:
     **/
    public AttachInfoCustomEntity uploadFileToOss (MultipartFile file) {
        boolean upload = false;
        String fileUrl = "";
        try {
            StringBuilder fileLocation = new StringBuilder("");
            fileLocation.append(uploadPath).append(DateUtil.getCurrentYearMonthDay());
            String fileExtension = FileUtil.getFileExtension(StringUtil.isNotEmpty(file.getOriginalFilename()) ? file.getOriginalFilename() : file.getName());
            String newFileName = CommonUtil.getPathFileName(fileExtension);
            if ( ! file.isEmpty() ) {
                upload = ossClientUtil.upload(Global.OSS_FILE_BACKET_USERICON, fileLocation.toString(), newFileName, file);

                if ( upload ) {
                    OSSInvokeRequest ossInvokeRequest = new OSSInvokeRequest();
                    ossInvokeRequest.setBucket(Global.OSS_FILE_BACKET_USERICON);
                    ossInvokeRequest.setDir(fileLocation.toString());
                    ossInvokeRequest.setObjectName(newFileName);
                    ossInvokeRequest.setExpiration(DateUtil.year(999));
                    fileUrl = ossClientUtil.getUrl(ossInvokeRequest);
                    int userIconIndex = fileUrl.indexOf(Global.OSS_FILE_BACKET_USERICON);
                    //fileUrl保存相对路径
                    return saveAttachInfo(file.getOriginalFilename(), fileUrl, fileUrl.substring(userIconIndex, fileUrl.length()), fileLocation.toString(),newFileName);
                }
            }
        } catch ( Exception e ) {
            logger.error("上传文件发生异常：{}", e);
            throw new MinosException(GlobalEnum.FILE_ERROR.getMsg());
        }
        throw new MinosException(GlobalEnum.FILE_ERROR.getMsg());
    }

    /**
     * @return List<AttachInfoEntity>
     * @Author jcx
     * @Description 上传多个附件至oss
     * @Date 10:18 2020/9/18
     * @Param files:
     **/
    public List<AttachInfoEntity> uploadFilesToOss (MultipartFile[] files) {
        List<AttachInfoEntity> result = new ArrayList<>();
        if ( files != null && files.length > 0 ) {
            for ( int i = 0 ; i < files.length ; i++ ) {
                MultipartFile file = files[ i ];
                result.add(this.uploadFileToOss(file));
            }
        }
        return result;
    }

    /**
     * @return AttachInfoEntity
     * @Author jcx
     * @Description 保存附件
     * @Date 13:46 2020/9/16
     * @Param fileName:附件名称
     * @Param fileUrl: 保存相对路径
     * @Param location:
     **/
    public AttachInfoCustomEntity saveAttachInfo (String fileName, String fileRelUrl, String fileUrl, String location,String newFileName) {
        AttachInfoCustomEntity attach = new AttachInfoCustomEntity();
        attach.setFileName(fileName);
        attach.setFileUrl(fileUrl);
        attach.setFileSuffix(FileUtil.getFileExtension(fileName));
        attach.setFileLocation(location);
        attach.setIsDeleted(Global.CONSTANT_NO);
        attachInfoMapper.insert(attach);
        attach.setReUrl(fileRelUrl);
        attach.setOssName(newFileName);
        return attach;
    }

    /**
     * @return VersionManagementEntity
     * @Author jcx
     * @Description 保存版本管理表
     * @Date 16:19 2020/9/21
     * @Param versionNum:
     * @Param name:
     * @Param id:
     **/
    public VersionManagementEntity saveVersionManagement (String versionNum, String dbVersionNum, String name, String id, String uploadType,
                                                          Boolean isNewInstall, Object cardTemple, String path) {
        if ( ! isNewInstall ) {
            //覆盖情况
            List<VersionManagementEntity> versionManagements = versionManagementMapper.selectList(new QueryWrapper<VersionManagementEntity>().lambda()
                    .eq(VersionManagementEntity::getForeignKey, id).eq(VersionManagementEntity::getVersionNumber, dbVersionNum).eq(VersionManagementEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));
            if ( CollectionUtils.isEmpty(versionManagements) ) {
                //当前启用即将被覆盖的卡片、模板没建立版本号，覆盖前备份此版本
                if ( cardTemple instanceof CardEntity ) {
                    //卡片
                    CardEntity dbCardEntity = ( CardEntity ) cardTemple;
                    CardTemplateInstallDto cardTemplateInstallDto = new CardTemplateInstallDto(dbCardEntity.getCardId(), dbCardEntity.getCardName(),
                            dbCardEntity.getImageUrl(), dbCardEntity.getPlatformType(), String.valueOf(dbCardEntity.getConfigurableFlag()),
                            String.valueOf(dbCardEntity.getConfigurableRuntimeFlag()), dbCardEntity.getCardDesc(), dbCardEntity.getVersionNumber());
                    //备份卡片版本
                    backUpVersion(cardTemplateInstallDto, id, path, dbCardEntity.getVersionNumber(), dbCardEntity.getCardName(), Integer.valueOf(uploadType));
                } else if ( cardTemple instanceof TemplateEntity ) {
                    //模板
                    TemplateEntity dbTemplateEntity = ( TemplateEntity ) cardTemple;
                    CardTemplateInstallDto cardTemplateInstallDto = new CardTemplateInstallDto(dbTemplateEntity.getTemplateId(), dbTemplateEntity.getTemplateName(),
                            dbTemplateEntity.getTemplateImgUrl(), dbTemplateEntity.getPlatformType(), String.valueOf(dbTemplateEntity.getConfigurableFlag()),
                            String.valueOf(dbTemplateEntity.getConfigurableRuntimeFlag()), dbTemplateEntity.getTemplateDesc(), dbTemplateEntity.getVersionNumber());
                    //备份模板版本
                    backUpVersion(cardTemplateInstallDto, id, path, dbTemplateEntity.getVersionNumber(), dbTemplateEntity.getTemplateName(), Integer.valueOf(uploadType));
                }
            }
        }
        VersionManagementEntity versionManage = addNewVersionManage(id, versionNum, name, Integer.valueOf(uploadType), Integer.valueOf(Global.CONSTANT_YES), Global.DeleteStatus.NO.getId());
        versionManagementMapper.insert(versionManage);
        return versionManage;
    }

    /**
     * @return void
     * @Author jcx
     * @Description 备份版本
     * @Date 17:21 2021/8/27
     * @Param cardTemplateInstallDto:
     * @Param id: 卡片、模板id
     * @Param path: 卡片、模板所在路径
     * @Param dbVersionNum: 未备份的包的版本号
     * @Param name: 卡片、模板名称
     * @Param type: 版本类型 0：模板1：卡片
     **/
    private void backUpVersion (CardTemplateInstallDto cardTemplateInstallDto, String id, String path, String dbVersionNum, String name, Integer type) {
        String zipPath = "";
        String newPropertiesPath = "";
        String jarPath = "";
        FileWriter fileWriter = null;
        FileInputStream inputStream = null;
        try {
            //生成一个安装所需的properties文件
            Map map = JSON.parseObject(JSON.toJSONString(cardTemplateInstallDto), Map.class);
            Properties properties = new Properties();
            properties.putAll(map);
            String newPropertiesName = CommonUtil.getPathFileName("properties");
            String jarName = id.toLowerCase().replaceAll("_", "-");
            zipPath = path + "/" + CommonUtil.getPathFileName("zip");
            newPropertiesPath = path + "/" + newPropertiesName;
            jarPath = path + "/" + jarName + ".jar";
            fileWriter = new FileWriter(path + "/" + newPropertiesName);
            properties.store(fileWriter, "Install Package Properties ");
            List<String> filePaths = Arrays.asList(jarPath, newPropertiesPath);
            //检查文件路径是否存在
            boolean isExist = checkFilePath(filePaths);
            if ( isExist ) {
                // 任意文件写入风险，需要控制写入的文件目录只能是允许的目录
                // add by zhangjian 2021-12-01
                caspSafetyService.legalPath(zipPath);

                int compress = ZipUtil.compress(filePaths, zipPath, false);
                MultipartFile localMultipartFile = null;
                String attWid = "";
                if ( compress == 2 ) {
                    VersionManagementEntity versionManage = addNewVersionManage(id, dbVersionNum,
                            name, type, Integer.valueOf(Global.CONSTANT_NO), Global.DeleteStatus.NO.getId());
                    versionManagementMapper.insert(versionManage);
                    File ossZipFile = new File(zipPath);
                    inputStream = new FileInputStream(ossZipFile);
                    localMultipartFile = new MockMultipartFile(ossZipFile.getName(), inputStream);
                    //上传oss作为版本包备份
                    AttachInfoEntity attachInfoEntity = uploadFileToOss(localMultipartFile);
                    attWid = attachInfoEntity.getWid();
                    //保存附件关系表
                    saveAttachRel(versionManage.getWid(), attWid);
                }
            }
        } catch ( Exception e ) {
            logger.info("备份版本发生异常：" + e);
            throw new BusinessException("备份版本发生异常：" + e);
        } finally {
            //清理文件
            if ( null != fileWriter ) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
            if ( null != inputStream ) {
                try {
                    inputStream.close();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
            if ( StringUtil.isNotEmpty(zipPath) ) {
                FileUtil.deletePathFile(Arrays.asList(zipPath));
            }
            if ( StringUtil.isNotEmpty(newPropertiesPath) ) {
                FileUtil.deletePathFile(Arrays.asList(newPropertiesPath));
            }
        }
    }

    /***
     * @Author jcx
     * @Description 检查文件或者文件夹路径是否存在
     * @Date 16:05 2021/8/27
     * @Param paths:
     * @return boolean
     **/
    private boolean checkFilePath (List<String> paths) {
        if ( CollectionUtils.isEmpty(paths) ) {
            return false;
        }
        boolean flag = true;
        for ( String str : paths ) {
            File file = new File(str);
            if ( ! file.exists() ) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    /***
     * @Author jcx
     * @Description 新增版本表实体
     * @Date 15:37 2021/8/27
     * @Param id:
     * @Param versionNum:
     * @Param name:
     * @Param versionType:
     * @Param status:
     * @Param deletdStatus:
     * @Param versionStatus:
     * @return VersionManagementEntity
     **/
    private VersionManagementEntity addNewVersionManage (String id, String versionNum, String name,
                                                         Integer versionType, Integer status, Integer deletdStatus) {
        VersionManagementEntity versionManage = new VersionManagementEntity();
        versionManage.setVersionNumber(versionNum);
        versionManage.setVersionType(versionType);
        versionManage.setVersionName(name);
        versionManage.setVersionStatus(status);
        versionManage.setForeignKey(id);
        versionManage.setIsDeleted(deletdStatus);
        return versionManage;
    }

    /**
     * @return void
     * @Author jcx
     * @Description 卡片/模板入库操作
     * @Date 15:50 2020/9/19
     * @Param file:
     * @Param path:
     **/
    public void cardTemplateHandler (File file, CardTemUpVo cardTemUpVo) {
        String zipUnCompressPath = "";
        boolean propertiesCheckResult = false;
        String jarPath = "";
        String properFilePath = "";
        String id = "";

        String path = cardTemUpVo.getUpLoadFilePath();

        String uploadTypeName = cardTemUpVo.getUploadTypeName();
        CardEntity cardEntity = null;
        TemplateEntity templateEntity = null;
        CardEntity dbCardEntityfromPro = cardTemUpVo.getDbCardEntityfromPro();
        CardEntity cardEntityfromPro = cardTemUpVo.getCardEntityfromPro();
        TemplateEntity dbTemplateEntityfromPro = cardTemUpVo.getDbTemplateEntityfromPro();
        TemplateEntity templateEntityfromPro = cardTemUpVo.getTemplateEntityfromPro();
        String uploadType = cardTemUpVo.getUploadType();
        try {
            // 任意文件写入风险，需要控制写入的文件目录只能是允许的目录
            // add by zhangjian 2021-12-01
            caspSafetyService.legalPath(path);

            List<String> fileNamePathArray = ZipUtil.unCompress(file, path, true);
            if ( CollectionUtils.isNotEmpty(fileNamePathArray) ) {
                Map<String, Object> checkResult = check(fileNamePathArray, zipUnCompressPath, id, cardTemUpVo, propertiesCheckResult, jarPath, properFilePath);
                zipUnCompressPath = String.valueOf(checkResult.get("zipUnCompressPath"));
                jarPath = String.valueOf(checkResult.get("jarPath"));
                properFilePath = String.valueOf(checkResult.get("properFilePath"));
                id = String.valueOf(checkResult.get("id"));
                propertiesCheckResult = ( boolean ) checkResult.get("propertiesCheckResult");
                cardEntity = null != checkResult.get("cardEntity") ? ( CardEntity ) checkResult.get("cardEntity") : null;
                templateEntity = null != checkResult.get("templateEntity") ? ( TemplateEntity ) checkResult.get("templateEntity") : null;
                String jarNewPath = "";
                //重命名jar，并复制jar至工作目录
                if ( propertiesCheckResult && ! "".equals(jarPath) && ! "".equals(id) ) {
                    File jarOldFile = new File(jarPath);
                    //新命名后的jar路径
                    String jarNewPathName = jarPath.substring(0, jarPath.lastIndexOf("/")) + "/" + id + ".jar";
                    File jarNewFile = new File(jarNewPathName);
                    if ( jarOldFile.exists() ) {
                        jarOldFile.renameTo(jarNewFile);
                        jarNewPath = cardTemUpVo.getUpLoadFilePath() + "/" + id + ".jar";
                        FileUtil.copy(jarNewPathName, jarNewPath);
                    }
                    VersionManagementEntity versionManag = null;
                    String carTemId = "";
                    Boolean isNewInstall = false;
                    if ( null != cardEntity ) {
                        if ( dbCardEntityfromPro != null ) {
                            //覆盖的情况，把原来记录逻辑删除，版本管理记录直接停用
                            cardEntity.setWid(dbCardEntityfromPro.getWid());
                            cardEntity.setCardSystemType(cardEntityfromPro.getCardSystemType());
                            cardMapper.updateById(cardEntity);
                            updateVersionMan(dbCardEntityfromPro.getCardId(), dbCardEntityfromPro.getVersionNumber());
                        } else {
                            isNewInstall = true;
                            cardMapper.insert(cardEntity);
                        }
                        carTemId = cardEntity.getCardId();
                        //保存版本管理表
                        versionManag = saveVersionManagement(cardEntity.getVersionNumber(), dbCardEntityfromPro != null ? dbCardEntityfromPro.getVersionNumber() : "",
                                cardEntity.getCardName(), carTemId, uploadType, isNewInstall, dbCardEntityfromPro, cardLocalUploadPath);
                    } else if ( null != templateEntity ) {
                        if ( dbTemplateEntityfromPro != null ) {
                            //覆盖的情况，把原来记录逻辑删除，版本管理记录直接停用
                            templateEntity.setWid(dbTemplateEntityfromPro.getWid());
                            templateEntity.setTemplateSystemType(templateEntityfromPro.getTemplateSystemType());
                            templateMapper.updateById(templateEntity);
                            updateVersionMan(dbTemplateEntityfromPro.getTemplateId(), dbTemplateEntityfromPro.getVersionNumber());
                        } else {
                            isNewInstall = true;
                            templateMapper.insert(templateEntity);
                        }
                        carTemId = templateEntity.getTemplateId();
                        //保存版本管理表
                        versionManag = saveVersionManagement(templateEntity.getVersionNumber(), dbTemplateEntityfromPro != null ? dbTemplateEntityfromPro.getVersionNumber() : "",
                                templateEntity.getTemplateName(),
                                carTemId, uploadType, isNewInstall, dbTemplateEntityfromPro, templateLocalUploadPath);
                    }
                    MultipartFile localMultipartFile = null;
                    String attWid = "";
                    if ( StringUtil.isNotEmpty(zipUnCompressPath) ) {
                        String zipNewName = CommonUtil.getPathFileName("zip");
                        String ossZipFilePath = zipUnCompressPath + "/" + zipNewName;

                        // 任意文件写入风险，需要控制写入的文件目录只能是允许的目录
                        // add by zhangjian 2021-12-01
                        caspSafetyService.legalPath(ossZipFilePath);

                        int compress = ZipUtil.compress(Arrays.asList(jarNewPath, properFilePath), ossZipFilePath, false);
                        if ( 0 != compress ) {
                            File ossZipFile = new File(ossZipFilePath);
                            FileInputStream inputStream = new FileInputStream(ossZipFile);
                            localMultipartFile = new MockMultipartFile(ossZipFile.getName(), inputStream);
                            //上传oss作为版本包备份
                            AttachInfoEntity attachInfoEntity = uploadFileToOss(localMultipartFile);
                            attWid = attachInfoEntity.getWid();
                            //保存附件关系表
                            saveAttachRel(versionManag.getWid(), attWid);
                        }
                    }
                    pluginsHandler(carTemId, Integer.valueOf(uploadType), attWid);
                }
            }
        } catch ( Exception e ) {
            logger.error("" + uploadTypeName + "包入库失败，请联系开发者！", e);
            throw new BusinessException("" + uploadTypeName + "包入库失败，请联系开发者！");
        } finally {
            //删除解压的目录
            FileUtil.deleteDir(new File(zipUnCompressPath));
        }
    }


    private void pluginsHandler (String carTemId, Integer carTemType, String attWid) {
        PluginsEntity pluginsEn = pluginService.getOne(new QueryWrapper<PluginsEntity>().eq(DbFieldConstant.PLUGIN_ID, carTemId).eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId()));
        if ( null == pluginsEn ) {
            //新增
            PluginsEntity plugins = new PluginsEntity();
            plugins.setPluginId(carTemId);
            plugins.setPluginType(carTemType);
            plugins.setDownloadAttWid(attWid);
            plugins.setIsDeleted(Global.DeleteStatus.NO.getId());
            pluginService.save(plugins);
        } else {
            //编辑
            pluginsEn.setDownloadAttWid(attWid);
            pluginService.updateById(pluginsEn);
            List<PluginInstallEntity> list = pluginInstallService.list(new QueryWrapper<PluginInstallEntity>().eq(DbFieldConstant.PLUGIN_WID, pluginsEn.getWid()));
            if ( CollectionUtils.isNotEmpty(list) ) {
                pluginInstallMapper.delete(new QueryWrapper<PluginInstallEntity>().eq(DbFieldConstant.PLUGIN_WID, pluginsEn.getWid()));
            }
        }
    }

    private Map<String, Object> check (List<String> fileNamePathArray, String zipUnCompressPath, String id, CardTemUpVo cardTemUpVo,
                                       boolean propertiesCheckResult, String jarPath, String properFilePath) {
        CardEntity cardEntityfromPro = cardTemUpVo.getCardEntityfromPro();
        TemplateEntity templateEntityfromPro = cardTemUpVo.getTemplateEntityfromPro();
        String uploadType = cardTemUpVo.getUploadType();
        Map<String, Object> result = new HashMap<String, Object>();
        CardEntity cardEntity = null;
        TemplateEntity templateEntity = null;
        for ( int i = 0 ; i < fileNamePathArray.size() ; i++ ) {
            String filePath = fileNamePathArray.get(i);
            zipUnCompressPath = filePath.substring(0, filePath.lastIndexOf("/"));
            //描述文件校验
            if ( "properties".equals(filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase()) ) {
                if ( Global.CARD_UPLOAD_TYPE.equals(uploadType) && cardEntityfromPro != null ) {
                    cardEntity = cardHandler(cardEntityfromPro);
                    id = cardEntity.getCardId();
                    propertiesCheckResult = true;
                } else if ( Global.TEMPLATE_UPLOAD_TYPE.equals(uploadType) && templateEntityfromPro != null ) {
                    templateEntity = templateHandler(templateEntityfromPro);
                    id = templateEntity.getTemplateId();
                    propertiesCheckResult = true;
                }
                properFilePath = filePath;
            }
            if ( "jar".equals(filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase()) ) {
                jarPath = filePath;
            }
        }
        result.put("zipUnCompressPath", zipUnCompressPath);
        result.put("id", id.toLowerCase().replaceAll("_", "-"));
        result.put("propertiesCheckResult", propertiesCheckResult);
        result.put("jarPath", jarPath);
        result.put("properFilePath", properFilePath);
        result.put("cardEntity", cardEntity);
        result.put("templateEntity", templateEntity);
        return result;
    }

    //卡片处理
    private CardEntity cardHandler (CardEntity cardEntityfromPro) {
        cardEntityfromPro.setCardSystemType(Global.CARD_SYS_TYPE_CUSTOM);
        cardEntityfromPro.setIsDeleted(Integer.valueOf(Global.CONSTANT_NO));
        cardEntityfromPro.setCardStatus(Global.CARD_STATUS_TO_UPDATE);

        return cardEntityfromPro;
    }

    //模板处理
    private TemplateEntity templateHandler (TemplateEntity templateEntityfromPro) {
        templateEntityfromPro.setTemplateSystemType(Global.CARD_SYS_TYPE_CUSTOM);
        templateEntityfromPro.setIsDeleted(Integer.valueOf(Global.CONSTANT_NO));
        templateEntityfromPro.setTemplateStatus(Global.CARD_STATUS_TO_UPDATE);
        return templateEntityfromPro;
    }

    //更新版本管理表
    private void updateVersionMan (String foreignKey, String versionNum) {
        QueryWrapper<VersionManagementEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(DbFieldConstant.FOREIGN_KEY, foreignKey)
                .eq(DbFieldConstant.VERSION_NUMBER, versionNum)
                .eq(DbFieldConstant.VERSION_STATUS, Integer.valueOf(Global.CONSTANT_YES));
        VersionManagementEntity versionManagementEntity = versionManagementMapper.selectOne(wrapper);
        if ( null != versionManagementEntity ) {
            versionManagementEntity.setVersionStatus(Integer.valueOf(Global.CONSTANT_NO));
            versionManagementMapper.updateById(versionManagementEntity);
        }
    }

    //读取描述文件，返回对象
    private Object getProPerByStream (InputStream proInputStream, String uploadType) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(proInputStream));
        Properties prop = new Properties();
        prop.load(bf);
        //卡片或模板id
        String id = prop.getProperty("id");
        //卡片或模板名称
        String name = prop.getProperty("name");
        //截图地址
        String imageUrl = prop.getProperty("imageUrl");
        //移动截图地址
        String imageUrlMobile = prop.getProperty("imageUrlMobile");
        //运行平台 0:PC 1:Mobile   0;1 pc&Mobile
        String platformType = prop.getProperty("platformType");
        //是否支持可配置 0:不支持 1:支持
        String configurableFlag = "0";
        //是否支持运行时可配置 0:不支持 1:支持
        String configurableRuntimeFlag = prop.getProperty("configurableRuntimeFlag");
        //描述
        String desc = prop.getProperty("desc");
        //版本号
        String currentYearMonthDayTime = DateUtil.getCurrentYearMonthDayTime();
        String versionNumber = prop.getProperty("versionNumber");
//        String versionNumber = !"".equals(prop.getProperty("versionNumber"))||null==prop.getProperty("versionNumber")?prop.getProperty("versionNumber")+"_"+currentYearMonthDayTime:"1.0.0_"+currentYearMonthDayTime;
        boolean proper = false;
        if ( StringUtil.isBlank(id) || StringUtil.isBlank(name) || StringUtil.isBlank(platformType) || StringUtil.isBlank(configurableRuntimeFlag) || StringUtil.isBlank(versionNumber) ) {
            proper = true;
        }
        //校验Properties字段
        checkProperties( proper, platformType, imageUrl, imageUrlMobile);
        versionNumber = versionNumber + "_" + currentYearMonthDayTime;
        String schoolId = prop.getProperty("schoolId");
        //校验描述文件
        if ( uploadType.equals(Global.CARD_UPLOAD_TYPE) ) {
            //上传的是卡片包
            CardEntity cardEntity = new CardEntity();
            cardEntity.setCardId(id);
            cardEntity.setCardName(name);
            cardEntity.setImageUrl(imageUrl);
            cardEntity.setPlatformType(platformType);
            cardEntity.setConfigurableFlag(Integer.valueOf(configurableFlag));
            cardEntity.setConfigurableRuntimeFlag(Integer.valueOf(configurableRuntimeFlag));
            cardEntity.setCardDesc(desc);
            cardEntity.setVersionNumber(versionNumber);
            // cardEntity.setSchoolId(schoolId); jcx说可以删
            cardEntity.setImageUrlMobile(imageUrlMobile);
            return cardEntity;
        } else {
            //上传的是模板包
            TemplateEntity templateEntity = new TemplateEntity();
            templateEntity.setTemplateId(id);
            templateEntity.setTemplateName(name);
            templateEntity.setTemplateImgUrl(imageUrl);
            templateEntity.setTemplateImgMobileUrl(imageUrlMobile);
            templateEntity.setPlatformType(platformType);
            templateEntity.setConfigurableFlag(Integer.valueOf(configurableFlag));
            templateEntity.setConfigurableRuntimeFlag(Integer.valueOf(configurableRuntimeFlag));
            templateEntity.setTemplateDesc(desc);
            templateEntity.setVersionNumber(versionNumber);
            // templateEntity.setSchoolId(schoolId); jcx说可以删
            return templateEntity;
        }
    }

    private void checkProperties(boolean proper,String platformType,String imageUrl,String imageUrlMobile){
        if ( proper ) {
            throw new BusinessException("描述文件需要包含模板ID、模板名称、适配终端、是否支持实例级配置、版本号!");
        }
        if(!"0".equals(platformType)&&!"1".equals(platformType)&&!"0;1".equals(platformType)){
            throw new BusinessException("适配终端【platformType】必须为0或者1或者0;1");
        }
        if ( StringUtils.equals(platformType, "0") && StringUtils.isBlank(imageUrl) ) {
            throw new BusinessException("pc平台必须包含pc截图地址");
        }else if ( StringUtils.equals(platformType, "1") && StringUtils.isBlank(imageUrlMobile) ) {
            throw new BusinessException("移动平台必须包含移动截图地址");
        }else if ( StringUtils.equals(platformType, "0;1") && StringUtils.isBlank(imageUrl) && StringUtils.isBlank(imageUrlMobile) ) {
            throw new BusinessException("pc、移动平台必须包含pc、移动截图地址");
        }
    }

    /**
     * @return void
     * @Author jcx
     * @Description 保存附件关系表
     * @Date 18:15 2020/9/21
     * @Param wid:
     * @Param attachwid:
     **/
    private void saveAttachRel (String wid, String attachwid) {
        AttachEntityRelEntity attachEntityRel = new AttachEntityRelEntity();
        attachEntityRel.setAttachWid(attachwid);
        attachEntityRel.setEntityWid(wid);
        attachEntityRelMapper.insert(attachEntityRel);
    }

    /**
     * @return void
     * @Author jcx
     * @Description 上传其他文件至服务器本地目录
     * @Date 10:50 2020/9/23
     * @Param file: 附件
     * @Param type: 0:图标
     **/
    public String uploadLocalServer (MultipartFile file, String type) {
        if ( StringUtil.isNotEmpty(type) && type.equals(Global.uploadLocalType.ICON.getType()) ) {
            //检测文件后缀是否是合法文件
            if ( ! FileUtil.checkSuffix(file.getOriginalFilename(), new String[] { ".jpg", ".jpeg", ".png", ".bmp", ".gif" }) ||
                    ! FileUtil.checkSuffix(file.getResource().getFilename(), new String[] { ".jpg", ".jpeg", ".png", ".bmp", ".gif" }) ) {
                throw new BusinessException(GlobalEnum.FILE_ILLEGAL.getMsg());
            }
            //上传图标
            AttachInfoCustomEntity attachInfoEntity = uploadFileToOss(file);
            //上传oss成功
            return attachInfoEntity.getReUrl();
        }
        throw new MinosException(GlobalEnum.PARAM_FAIL.getMsg());
    }


    /**
     * @return ResultData
     * @Author jcx
     * @Description 卡片、模板切换版本
     * @Date 10:43 2020/10/13
     * @Param vo:
     **/
    @Transactional
    public ResultData switchVersion (HttpServletResponse response, SwitchVersionVo vo) {
        Assert.notNull(vo, GlobalEnum.PARAM_FAIL.getCode(), GlobalEnum.PARAM_FAIL.getMsg());
        VersionManagementEntity versionManagement = getVersionManagementEntity(vo);
        List<AttachEntityRelEntity> list = getAttachEntityRelEntities(versionManagement);
        //固定根目录
        String parentRoutePath = "";
        //需解压的本地目录
        String path = "";
        //描述文件路径
        String propertiesPath = "";
        //zip包路径
        String zipPath = "";
        //jar路径
        String jarPath = "";
        //jar包名称
        String jarName = "";
        InputStream fileInputStream = null;
        try {
            AttachInfoEntity attachInfoEntity = getAtt(list.get(0).getAttachWid());
            //获取到的文件流
            byte[] fileByte = getFileByte(attachInfoEntity);
            //生成文件新文件名
            String fileName = DateUtil.getCurrentYearMonthDayTime();
            //得到固定根目录
            parentRoutePath = checkUploadPath(vo.getSwitchType()) + "/";
            //得到指定上传目录
            path = parentRoutePath + fileName;
            String fullFileName = fileName + ".zip";

            // 任意文件写入风险，需要控制写入的文件目录只能是允许的目录
            // add by zhangjian 2021-12-01
            caspSafetyService.legalPath(path);

            //生成文件到服务器指定目录
            FileUtil.uploadFile(path, fullFileName, true, fileByte);
            zipPath = path + "/" + fullFileName;
            File file = new File(zipPath);
            //解压zip包
            List<String> fileNamePathArray = ZipUtil.unCompress(file, path, false);
            if ( CollectionUtils.isNotEmpty(fileNamePathArray) ) {
                for ( int i = 0 ; i < fileNamePathArray.size() ; i++ ) {
                    String filePath = fileNamePathArray.get(i);
                    if ( "properties".equals(filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase()) ) {
                        propertiesPath = filePath;
                        fileInputStream = new FileInputStream(new File(propertiesPath));
                        upLoadByType(vo, fileInputStream, versionManagement);
                        //禁用版本管理表当前使用的版本记录
                        setVersionManagementDisable(vo);
                        //启用即将生效的切换后的版本记录
                        versionManagement.setVersionStatus(Global.EnableStatus.ENABLE.getId());
                        versionManagementMapper.updateById(versionManagement);

                        // 是否异步重启门户引擎
                        restartSystem(vo);
                    } else if ( "jar".equals(filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase()) ) {
                        jarPath = filePath;
                        jarName = jarPath.substring(jarPath.lastIndexOf("/") + 1, filePath.lastIndexOf(".") + 1);
                    }
                }
            }
        } catch ( Exception e ) {
            logger.error("切换版本失败！----" + e);
            jarPath = "";
        } finally {
            handleFinally(parentRoutePath, path, jarPath, jarName, fileInputStream);
        }
        return ResultData.success();
    }

    private void restartSystem (SwitchVersionVo vo) {
        if ( null != vo.getIsRestart() && Global.CONSTANT_YES.equals(vo.getIsRestart()) ) {
            executorService.restartSystem();
        }
    }

    private List<AttachEntityRelEntity> getAttachEntityRelEntities (VersionManagementEntity versionManagement) {
        List<AttachEntityRelEntity> list = queryAttachRel(versionManagement.getWid());
        if ( CollectionUtils.isEmpty(list) ) {
            throw new BusinessException("切换版本失败");
        }
        return list;
    }

    private VersionManagementEntity getVersionManagementEntity (SwitchVersionVo vo) {
        VersionManagementEntity versionManagement = versionManagementMapper.selectById(vo.getVersionNumWid());
        if ( versionManagement == null ) {
            throw new BusinessException("切换版本失败");
        }
        return versionManagement;
    }

    private void setVersionManagementDisable (SwitchVersionVo vo) {
        QueryWrapper<VersionManagementEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(DbFieldConstant.FOREIGN_KEY, vo.getForeignKey()).eq(DbFieldConstant.VERSION_NUMBER, vo.getVersionNumber());
        VersionManagementEntity versionManagementEntity = versionManagementMapper.selectOne(wrapper);
        if ( null != versionManagementEntity ) {
            versionManagementEntity.setVersionStatus(Global.EnableStatus.DISABLE.getId());
            versionManagementMapper.updateById(versionManagementEntity);
        }
    }

    private void upLoadByType (SwitchVersionVo vo, InputStream fileInputStream, VersionManagementEntity versionManagement) throws IOException {
        if ( Global.CARD_UPLOAD_TYPE.equals(vo.getSwitchType()) ) {
            upLoadCard(vo, fileInputStream, versionManagement);
        } else {
            upLoadTemplate(vo, fileInputStream, versionManagement);
        }
    }

    private void handleFinally (String parentRoutePath, String path, String jarPath, String jarName, InputStream fileInputStream) {
        if ( null != fileInputStream ) {
            try {
                fileInputStream.close();
            } catch ( IOException e ) {
                logger.error(e);
            }
        }
        if ( StringUtil.isNotEmpty(jarPath) ) {
            //复制文件
            FileUtil.copy(jarPath, parentRoutePath + "/" + jarName + "jar");
        }
        //删除解压目录
        if ( StringUtil.isNotEmpty(path) ) {
            FileUtil.deleteDir(new File(path));
        }
    }

    private void upLoadTemplate (SwitchVersionVo vo, InputStream fileInputStream, VersionManagementEntity versionManagement) throws IOException {
        //禁用当前使用的模板
        TemplateEntity localTemplateEntity = templateMapper.selectById(vo.getWid());
        if ( null != localTemplateEntity ) {
            //得到oss上需要切换版本的描述文件  转换成实体
            TemplateEntity templateEntity = ( TemplateEntity ) getProPerByStream(fileInputStream, vo.getSwitchType());
            templateEntity.setWid(localTemplateEntity.getWid());
            templateEntity.setTemplateStatus(Global.CARD_STATUS_TO_UPDATE);
            templateMapper.updateById(templateEntity);
        }
    }

    private void upLoadCard (SwitchVersionVo vo, InputStream fileInputStream, VersionManagementEntity versionManagement) throws IOException {
        //禁用当前使用的卡片
        CardEntity localCardEntity = cardMapper.selectById(vo.getWid());
        if ( null != localCardEntity ) {
            //得到oss上需要切换版本的描述文件  转换成实体
            CardEntity cardEntity = ( CardEntity ) getProPerByStream(fileInputStream, vo.getSwitchType());
            cardEntity.setWid(localCardEntity.getWid());
            cardEntity.setCardStatus(Global.CARD_STATUS_TO_UPDATE);
            cardMapper.updateById(cardEntity);
        }
    }

    //删除Jar包
    public static void deleteJar (String localUploadPath, List<String> jarNames) {
        File file = new File(localUploadPath);
        File[] fs = file.listFiles();
        List<String> jarpPathNames = new ArrayList<>();
        if ( fs.length > 0 ) {
            for ( File f : fs ) {    //遍历File[]数组
                for ( String str : jarNames ) {
                    if ( f.getName().contains(str) ) {
                        //匹配jar
                        jarpPathNames.add(localUploadPath + "/" + f.getName());
                    }
                }
            }
            if ( CollectionUtils.isNotEmpty(jarpPathNames) ) {
                for ( String str : jarpPathNames ) {
                    FileUtil.deletePathFile(Arrays.asList(str));
                }
            }
        }
    }

    public void downloadApplet (HttpServletResponse response, String type, String portalDomain) {
        //校验登录用户是否有操作权限
        List<MenuEditTypeEnum> typeEnums = Collections.singletonList(MenuEditTypeEnum.MENU_EDIT_TYPE_SET);
        if ( !userConsoleAuthService.isUserEditAuthority(typeEnums, MenuEditTypeEnum.MENU_ID_PROGRAMME_MANAGE) ) {
            throw new MinosException("非法权限操作，请联系管理员");
        }

        // type: 0 微信，1 钉钉
        String appletPath = Global.CONSTANT_NO.equals(type) ? "applet/mp-weixin.zip" : "applet/mp-alipay.zip";
        String fileName = Global.CONSTANT_NO.equals(type) ? "mp-weixin.zip" : "mp-alipay.zip";
        try ( InputStream fis = FileService.class.getClassLoader().getResourceAsStream(appletPath) ) {
            if ( null == fis ) {
                throw new MinosException("文件不存在！");
            }
            // 解压文件
            String unCompressPath = checkUploadPath(Global.TEMPLATE_UPLOAD_TYPE) + "/applet/";
            FileUtil.copyByInputStream(fis, unCompressPath + fileName);
            List<String> filePaths = ZipUtil.unCompress(new File(unCompressPath + fileName), unCompressPath, false);
            if ( CollectionUtils.isNotEmpty(filePaths) ) {
                filePaths.forEach(path -> {
                    if ( path.endsWith("static/config.js") ) {
                        // 修改文件
                        FileUtil.modifyFileContent(path, "https://wechattest.wisedu.com", portalDomain);
                    }
                });
                // 压缩文件
                String compressFile = unCompressPath + "temp.zip";
                File zipUnCompressFile = new File(filePaths.get(0));
                ZipUtil.deCompress(zipUnCompressFile, compressFile);
                // 下载压缩文件
                FileUtil.downLocalFile(response, unCompressPath, compressFile, fileName);
                // 删除临时文件
                FileUtil.deleteDir(new File(unCompressPath));
            }
            // 将门户域名保存至数据库中
            SystemConfigEntity systemConfig = systemConfigService.getSystemConfig("applet_portal_domain");
            if ( null != systemConfig ) {
                systemConfig.setConfigValue(portalDomain);
                systemConfigService.updateById(systemConfig);
            }
        } catch ( Exception e ) {
            logger.error("下载小程序模板异常: ", e);
            throw new BusinessException("下载小程序模板异常");
        }
    }

    public void downloadFileByUrl(HttpServletRequest request, HttpServletResponse response, String fileUrl, String fileName){
        BufferedOutputStream outputStream = null;
        fileName = StringEscapeUtils.unescapeHtml4(fileName);
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection httpURLConnection = ( HttpURLConnection ) new URL(url.toURI().toString())
                    .openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
            httpURLConnection
                    .setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(20000);
            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
            byte[] bytes = HttpUtil.downloadBytes(url.toURI().toString());
            String newUrl = httpURLConnection.getURL().getFile();
            httpURLConnection.getContentType();
            if (newUrl != null || newUrl.length() <= 0) {
                newUrl = java.net.URLDecoder.decode(newUrl, "UTF-8");
                int pos = newUrl.indexOf('?');
                if (pos > 0) {
                    newUrl = newUrl.substring(0, pos);
                }
                pos = newUrl.lastIndexOf('/');
                String fileName1 = newUrl.substring(pos + 1);
                if(!StringUtils.isEmpty(fileName)){
                    String suffix = fileName1.substring(fileName1.lastIndexOf('.'));
                    fileName += suffix;
                }else{
                    fileName = fileName1;
                }
            }
            String userAgent = request.getHeader("User-Agent").toLowerCase(Locale.ROOT);
            logger.debug("User-Agent: {}", userAgent);
            if(userAgent.contains("safari") && !userAgent.contains("chrome")){
                response.setHeader("Content-Disposition", "attachment;fileName="
                        + new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
            }else{
                response.setHeader("Content-Disposition", "attachment;fileName="
                        + URLEncoder.encode(fileName, "UTF-8"));
            }
            response.setCharacterEncoding("UTF-8");
            // 设置response的Header
            outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception e ) {
            logger.error("下载图片异常", e);
        } finally {
            if ( null != outputStream ) {
                try {
                    outputStream.close();
                } catch ( IOException e ) {
                    logger.error("关闭outputStream异常", e);
                }
            }
        }
    }

    /**
     * 依据浏览器判断编码规则，转换下载文件名，防止出现乱码
     *
     * @param request
     * @param fileName
     * @return
     */
    public static String encodeFileName(HttpServletRequest request, String fileName) {
        String userAgent = request.getHeader("User-Agent");
        try {
            String encodedFileName = URLEncoder.encode(fileName, "UTF8");
            // 如果没有UA，则默认使用IE的方式进行编码
            if (userAgent == null) {
                return "filename=\"" + encodedFileName + "\"";
            }

            userAgent = userAgent.toLowerCase();
            // IE浏览器，只能采用URLEncoder编码
            if (userAgent.contains("msie")) {
                return "filename=\"" + encodedFileName + "\"";
            }

            // Opera浏览器只能采用filename*
            if (userAgent.contains("opera")) {
                return "filename*=UTF-8''" + encodedFileName;
            }

            // Safari浏览器，只能采用ISO编码的中文输出,Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
            if (userAgent.contains("mac os")||userAgent.contains("iphone")) {
//                String newName =  StringEscapeUtils.unescapeHtml4(fileName);
                return "filename=\"" + new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1") + "\"";
            }

            // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
            if (userAgent.contains("mozilla")) {
                return "filename*=UTF-8''" + encodedFileName;
            }

            return "filename=\"" + encodedFileName + "\"";
        } catch (UnsupportedEncodingException e) {
            logger.error("encodeFileName转换异常", e);
            return fileName;
        }
    }
}
