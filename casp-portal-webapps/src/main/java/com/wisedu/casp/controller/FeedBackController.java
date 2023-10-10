package com.wisedu.casp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.casp.controller.bean.AppraisePicInfo;
import com.wisedu.casp.controller.bean.SubmitFeedbackWithPicReq;
import com.wisedu.casp.controller.bean.UploadAppraiseRequest;
import com.wisedu.casp.expansion.dubbo.apps.feedback.service.vo.DubboRelatedContentInfo;
import com.wisedu.casp.expansion.dubbo.apps.feedback.service.vo.DubboSubmitFeedbackReq;
import com.wisedu.casp.expansion.dubbo.apps.feedback.service.vo.DubboSubmitResponse;
import com.wisedu.minos.casp.portal.common.annotations.Login.MustLogin;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.DateUtil;
import com.wisedu.minos.casp.portal.common.utils.FileUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.service.impl.AppAppraiseService;
import com.wisedu.minos.casp.portal.service.impl.SensitiveWordService;
import com.wisedu.minos.casp.portal.service.impl.ServiceApiService;
import com.wisedu.minos.casp.portal.utils.CardDubboUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.oss.client.util.OSSClientUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedBackController {
    private static final Logger logger = LogManager.getLogger(HomeController.class);
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

    @PostMapping("submitFeedbackWithPic")
    @MustLogin
    public ResultData submitFeedbackWithPic(@RequestPart(value = "uploadFile", required = false) List<MultipartFile> files,
                                          @RequestPart("data") String feedbackStr) {

        UserInfo currentUser = userUtil.getCurrentUser();
        if(null==currentUser){
            throw new NoLoginException("请登录！");
        }

        if (StringUtils.isEmpty(feedbackStr)) {
            throw new MinosException("-1", "入参不合法");
        }

        SubmitFeedbackWithPicReq submitFeedbackWithPicReq = JSONObject.parseObject(feedbackStr, SubmitFeedbackWithPicReq.class);

        // 校验关联内容，如果不为空，则对应的wid和type不能为空
        DubboRelatedContentInfo relatedContentInfo = submitFeedbackWithPicReq.getRelatedContentInfo();
        if(null != relatedContentInfo && (StringUtils.isEmpty(relatedContentInfo.getRelaWid()) || null == relatedContentInfo.getRelaType())){
            throw new MinosException("-1", "入参不合法");
        }

        submitFeedbackWithPicReq.setUserId(currentUser.getWid());
        submitFeedbackWithPicReq.setUserName(currentUser.getUserName());

        try {

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
                submitFeedbackWithPicReq.setAppraisePics(JSONArray.toJSONString(appraisePicInfos));
            }

            DubboSubmitFeedbackReq dubboSubmitFeedbackReq = new DubboSubmitFeedbackReq();
            BeanUtils.copyProperties(submitFeedbackWithPicReq, dubboSubmitFeedbackReq);

            CardDubboUtil cardUtil = ApplicationContextUtil.get(CardDubboUtil.class);
            DubboSubmitResponse response = cardUtil.getDubboFeedbackService().submitFeedbackCasp(dubboSubmitFeedbackReq);
            if(null != response){
                if("0".equals(response.getErrcode())){
                    logger.debug("调用提交意见反馈接口返回结果..." + JSON.toJSONString(response));
                }else{
                    throw new BusinessException("999", response.getErrmsg());
                }
            }
        } catch (Exception e) {
            logger.warn("提交意见反馈异常", e);
            return ResultData.error(e.getMessage());
        }
        return ResultData.success();
    }

    @PostMapping("submitFeedbackWithPicBase64")
    @MustLogin
    public ResultData submitFeedbackWithPicBase64(@RequestBody UploadAppraiseRequest request) {

        UserInfo currentUser = userUtil.getCurrentUser();
        if(null==currentUser){
            throw new NoLoginException("请登录！");
        }

        if (null == request || StringUtils.isEmpty(request.getData())) {
            throw new MinosException("-1", "入参不合法");
        }

        SubmitFeedbackWithPicReq submitFeedbackWithPicReq = JSONObject.parseObject(request.getData(), SubmitFeedbackWithPicReq.class);

        // 校验关联内容，如果不为空，则对应的wid和type不能为空
        DubboRelatedContentInfo relatedContentInfo = submitFeedbackWithPicReq.getRelatedContentInfo();
        if(null != relatedContentInfo && (StringUtils.isEmpty(relatedContentInfo.getRelaWid()) || null == relatedContentInfo.getRelaType())){
            throw new MinosException("-1", "入参不合法");
        }

        submitFeedbackWithPicReq.setUserId(currentUser.getWid());
        submitFeedbackWithPicReq.setUserName(currentUser.getUserName());

        try {
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
                submitFeedbackWithPicReq.setAppraisePics(JSONArray.toJSONString(appraisePicInfos));
            }

            DubboSubmitFeedbackReq dubboSubmitFeedbackReq = new DubboSubmitFeedbackReq();
            BeanUtils.copyProperties(submitFeedbackWithPicReq, dubboSubmitFeedbackReq);

            CardDubboUtil cardUtil = ApplicationContextUtil.get(CardDubboUtil.class);
            DubboSubmitResponse response = cardUtil.getDubboFeedbackService().submitFeedbackCasp(dubboSubmitFeedbackReq);
            if(null != response){
                if("0".equals(response.getErrcode())){
                    logger.debug("调用提交意见反馈接口返回结果..." + JSON.toJSONString(response));
                }else{
                    throw new BusinessException("999", response.getErrmsg());
                }
            }
        } catch (Exception e) {
            logger.warn("提交意见反馈异常", e);
            return ResultData.error(e.getMessage());
        }
        return ResultData.success();
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

}
