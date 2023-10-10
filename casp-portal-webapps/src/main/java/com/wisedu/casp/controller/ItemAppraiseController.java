package com.wisedu.casp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.casp.controller.bean.AppraisePicInfo;
import com.wisedu.casp.controller.bean.UploadAppraiseRequest;
import com.wisedu.minos.api.constant.SensitiveWordStrategyEnum;
import com.wisedu.minos.casp.portal.common.annotations.Login.MustLogin;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.DateUtil;
import com.wisedu.minos.casp.portal.common.utils.FileUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.AmpBaseResponse;
import com.wisedu.minos.casp.portal.model.AppraiseRequest;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.service.impl.AppAppraiseService;
import com.wisedu.minos.casp.portal.service.impl.SensitiveWordService;
import com.wisedu.minos.casp.portal.service.impl.ServiceApiService;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.gateway.client.util.XSSKeyUtil;
import com.wisedu.minos.oss.client.util.OSSClientUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/itemAppraise")
public class ItemAppraiseController {
    private static final Logger logger = LogManager.getLogger(ItemAppraiseController.class);
    @Autowired
    AppAppraiseService appAppraiseService;

    @Autowired
    ServiceApiService serviceApiService;

    @Autowired
    SensitiveWordService sensitiveWordService;

    @Autowired
    UserUtil userUtil;

    @Autowired
    private OSSClientUtil ossClientUtil;

    @Value("${file.uploadPath}")
    private String uploadPath;

    @PostMapping("saveAppraiseWithPic")
    @MustLogin
    public ResultData saveAppraiseWithPic(@RequestPart(value = "uploadFile", required = false) List<MultipartFile> files,
                                          @RequestPart("data") String appraiseStr) {
        if (StringUtils.isEmpty(appraiseStr)) {
            throw new MinosException("-1", "入参不合法");
        }
        AppraiseRequest appraiseRequest = JSONObject.parseObject(appraiseStr, AppraiseRequest.class);

        UserInfo currentUser = userUtil.getCurrentUser();
        appraiseRequest.setUserId(currentUser.getWid());
        appraiseRequest.setUserName(currentUser.getUserName());

        try {
            // 对评论内容进行XSS校验
            XSSKeyUtil.isXssIllegalWord(appraiseRequest.getSuggest());

            // 对评价内容进行敏感词处理
            // 获取敏感词策略配置
            String config = sensitiveWordService.getConfig(SensitiveWordStrategyEnum.ModelWid.getCode(), SensitiveWordStrategyEnum.SIM_STRATEGY.getCode());
            // "0" 不处理
            if (SensitiveWordStrategyEnum.TWO.getCode().equals(config)) {
                // 禁发
                if (sensitiveWordService.isContainsSensitiveWord(appraiseRequest.getSuggest())) {
                    throw new MinosException("-1", "填写内容包含敏感词");
                }
            }

            if (!CollectionUtils.isEmpty(files)) {
                List<AppraisePicInfo> appraisePicInfos = new ArrayList<>();
                AppraisePicInfo info;
                if (files.size() > 3) {
                    throw new MinosException("-1", "图片不能超过3张");
                }
                for (MultipartFile file : files) {
                    if (!checkFileType(file)) {
                        throw new MinosException("-1", "请传入格式为JPG、JPEG、PNG的图片");
                    }
                    if (file.getSize() <= 0L) {
                        throw new MinosException("-1", "获取图片错误");
                    }
                    /**
                     * 限制在500k以内
                     */
                    if (file.getSize() > (1024 * 1024)) {
                        throw new MinosException("-1", "图片过大，限制在1M以内");
                    }
                    info = new AppraisePicInfo();
                    info.setFilePath(uploadPath + DateUtil.getCurrentYearMonthDay());
                    info.setFileName(getFileName(currentUser.getWid(), file));
                    ossClientUtil.upload(Global.OSS_FILE_BACKET_USERICON, info.getFilePath(), info.getFileName(), file);
                    appraisePicInfos.add(info);
                }
                appraiseRequest.setAppraisePics(JSONArray.toJSONString(appraisePicInfos));
            }

            HttpEntity entity = new HttpEntity(appraiseRequest);

            AmpBaseResponse response = RestTemplateUtils.post("/coosk/itemAppraise/dimension/save", entity, AmpBaseResponse.class).getBody();
            if(null != response){
                logger.debug("调用评价服务事项接口返回结果..." + JSON.toJSONString(response));
            }
        } catch (Exception e) {
            logger.warn("保存评价异常", e);
            return ResultData.error(e.getMessage());
        }
        return ResultData.success();
    }

    @PostMapping("saveAppraiseWithPicBase64")
    @MustLogin
    public ResultData saveAppraiseWithPicBase64(@RequestBody UploadAppraiseRequest request) {
        if (null == request || StringUtils.isEmpty(request.getData())) {
            throw new MinosException("-1", "入参不合法");
        }

        AppraiseRequest appraiseRequest = JSONObject.parseObject(request.getData(), AppraiseRequest.class);

        UserInfo currentUser = userUtil.getCurrentUser();
        appraiseRequest.setUserId(currentUser.getWid());
        appraiseRequest.setUserName(currentUser.getUserName());

        try {
            // 对评论内容进行XSS校验
            XSSKeyUtil.isXssIllegalWord(appraiseRequest.getSuggest());

            // 对评价内容进行敏感词处理
            // 获取敏感词策略配置
            String config = sensitiveWordService.getConfig(SensitiveWordStrategyEnum.ModelWid.getCode(), SensitiveWordStrategyEnum.SIM_STRATEGY.getCode());
            // "0" 不处理
            if (SensitiveWordStrategyEnum.TWO.getCode().equals(config)) {
                // 禁发
                if (sensitiveWordService.isContainsSensitiveWord(appraiseRequest.getSuggest())) {
                    throw new MinosException("-1", "填写内容包含敏感词");
                }
            }

            List<String> files = request.getUploadFile();
            if (!CollectionUtils.isEmpty(files)) {
                List<AppraisePicInfo> appraisePicInfos = new ArrayList<>();
                AppraisePicInfo info;
                if (files.size() > 3) {
                    throw new MinosException("-1", "图片不能超过3张");
                }
                for (String file : files) {
                    if(StringUtils.isEmpty(file)){
                        continue;
                    }
                    String fileType = getFileType(file);
                    byte[] content = Base64.getDecoder().decode(file.split(",")[1]);
                    if (!checkFileType(fileType)) {
                        throw new MinosException("-1", "请传入格式为JPG、JPEG、PNG的图片");
                    }
                    if (content.length <= 0L) {
                        throw new MinosException("-1", "获取图片错误");
                    }
                    /**
                     * 限制在500k以内
                     */
                    if (content.length > (1024 * 1024)) {
                        throw new MinosException("-1", "图片过大，限制在1M以内");
                    }
                    info = new AppraisePicInfo();
                    info.setFilePath(uploadPath + DateUtil.getCurrentYearMonthDay());
                    info.setFileName(getFileName(currentUser.getWid(), fileType));
                    ossClientUtil.upload(Global.OSS_FILE_BACKET_USERICON, info.getFilePath(), info.getFileName(), content);
                    appraisePicInfos.add(info);
                }
                appraiseRequest.setAppraisePics(JSONArray.toJSONString(appraisePicInfos));
            }

            HttpEntity entity = new HttpEntity(appraiseRequest);

            AmpBaseResponse response = RestTemplateUtils.post("/coosk/itemAppraise/dimension/save", entity, AmpBaseResponse.class).getBody();
            if(null != response){
                logger.debug("调用评价服务事项接口返回结果..." + JSON.toJSONString(response));
            }
        } catch (Exception e) {
            logger.warn("保存评价异常", e);
            return ResultData.error(e.getMessage());
        }
        return ResultData.success();
    }

    private String getFileType(String file){
        String fileType = "unknown";
        if(file.toLowerCase().startsWith("data:image/png;")){
            fileType = "png";
        }else if(file.toLowerCase().startsWith("data:image/jpeg;")){
            fileType = "jpeg";
        }else if(file.toLowerCase().startsWith("data:image/jpg;")){
            fileType = "jpg";
        }

        return fileType;
    }

    private boolean checkFileType(String fileType){
        List<String> fileTypeList = new ArrayList<>(Arrays.asList("jpg", "jpeg", "png"));
        if(fileTypeList.contains(fileType)){
            return true;
        }
        return false;
    }

    private String getFileName(String wid, String fileType) {
        return wid + System.currentTimeMillis() + "." + fileType;
    }



    private boolean checkFileType(MultipartFile file){
        List<String> imgType = new ArrayList<>(Arrays.asList("image/jpg", "image/jpeg", "image/png"));
        List<String> fileType = new ArrayList<>(Arrays.asList("jpg", "jpeg", "png"));
        String fileExtension = FileUtil.getFileExtension(StringUtil.isNotEmpty(file.getOriginalFilename()) ? file.getOriginalFilename() : file.getName());
        if(null != fileExtension){
            fileExtension = fileExtension.toLowerCase();
        }
        boolean flag = false;
        if(imgType.contains(file.getContentType()) && fileType.contains(fileExtension)){
            flag = true;
        }
        return flag;
    }

    private String getFileName(String wid, MultipartFile file) {
        String fileExtension = FileUtil.getFileExtension(StringUtil.isNotEmpty(file.getOriginalFilename()) ? file.getOriginalFilename() : file.getName());

        return wid + System.currentTimeMillis() + "." + fileExtension;
    }
}
