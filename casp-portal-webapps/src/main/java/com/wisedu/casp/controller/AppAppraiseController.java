package com.wisedu.casp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisedu.casp.controller.bean.*;
import com.wisedu.casp.mc.api.message.MessageSendService;
import com.wisedu.casp.mc.api.model.DubboMessageSendDTO;
import com.wisedu.casp.mc.api.model.DubboReceiverDTO;
import com.wisedu.minos.annotation.ConsoleRightCheck;
import com.wisedu.minos.api.constant.SensitiveWordStrategyEnum;
import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.api.model.DubboServiceInfoApiResp;
import com.wisedu.minos.casp.portal.common.annotations.Login.MustLogin;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.DateUtil;
import com.wisedu.minos.casp.portal.common.utils.FileUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.AppAppraiseEntity;
import com.wisedu.minos.casp.portal.dao.entity.SystemConfigEntity;
import com.wisedu.minos.casp.portal.model.AllServiceRequest;
import com.wisedu.minos.casp.portal.model.AllServiceResponse;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.service.impl.*;
import com.wisedu.minos.casp.portal.utils.MinosConsoleUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.casp.portal.utils.comparatorUtil.AppAppraiseComparator;
import com.wisedu.minos.casp.portal.utils.comparatorUtil.AppLetterComparator;
import com.wisedu.minos.casp.portal.vo.AppAppraiseSummaryVo;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import com.wisedu.minos.gateway.client.util.XSSKeyUtil;
import com.wisedu.minos.oss.client.bean.OSSFileBean;
import com.wisedu.minos.oss.client.util.OSSClientUtil;
import com.wisedu.minos.util.MenuEditTypeEnum;
import com.wisedu.minos.util.MinosCommonUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appAppraise")
public class AppAppraiseController {
    private static final Logger logger = LogManager.getLogger(HomeController.class);
    @Autowired
    AppAppraiseService appAppraiseService;

    @Autowired
    ServiceApiService serviceApiService;

    @Autowired
    SensitiveWordService sensitiveWordService;

    @Autowired
    private HomeService homeService;

    @Autowired
    UserUtil userUtil;

    @Autowired
    private OSSClientUtil ossClientUtil;

    @Value("${file.uploadPath}")
    private String uploadPath;

    @DubboReference
    UserService userService;

    @DubboReference
    private MessageSendService messageSendService;

    @Autowired
    private SystemConfigService systemConfigService;

    @GetMapping("/myAppraiseExists")
    public ResultData<Boolean> exists(String appId) {
        UserInfo currentUser = userUtil.getCurrentUser();
        boolean exists = false;
        if (currentUser != null) {
            exists = appAppraiseService.exists(appId, currentUser.getUserAccount());
        }
        return ResultData.success(exists);
    }

    @GetMapping("appraiseSummary")
    @MustLogin
    public ResultData<Object> appraiseSummary(String appId, @RequestParam(defaultValue = "2", required = false) int type) {
        UserInfo currentUser = userUtil.getCurrentUser();
        Map<String, Object> resp = new HashMap<>();
        String displayType = systemConfigService.getSystemConfigValue(Global.SYS_CONFIG_KEY_APP_APPRAISE_DISPLAY_TYPE);
        if (StringUtils.isEmpty(displayType)) {
            displayType = "0";
        }
        resp.put("displayType", Integer.valueOf(displayType));
        resp.put("isAppraised", appAppraiseService.exists(appId, currentUser.getUserAccount()));
        resp.put("appraiseSummary", appAppraiseService.queryAppAppraiseSummary(appId));
        return ResultData.success(resp);
    }

    @GetMapping("queryAppraiseByPage")
    public ResultData<IPage<AppAppraiseEntity>> queryAppraiseByPage(String appId,
                                                                    @RequestParam(defaultValue = "1", required = false) int pageNum,
                                                                    @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                                    @RequestParam(defaultValue = "0", required = false) int scoreLevel) {
        String displayType = systemConfigService.getSystemConfigValue(Global.SYS_CONFIG_KEY_APP_APPRAISE_DISPLAY_TYPE);
        if (StringUtils.isEmpty(displayType)) {
            displayType = "0";
        }
        UserInfo userInfo = userUtil.getCurrentUser();
        if (null == userInfo) {
            throw new NoLoginException("当前用户未登录");
        }
        IPage<AppAppraiseEntity> page = appAppraiseService.queryAppAppraises(appId, scoreLevel, false,
                pageNum, pageSize, Integer.valueOf(displayType) == 1, userInfo.getUserAccount());

        // 敏感词处理
        String config = sensitiveWordService.getConfig(SensitiveWordStrategyEnum.ModelWid.getCode(), SensitiveWordStrategyEnum.SERVICE_STRATEGY.getCode());
        // 前台展示默认隐藏展示
        if (!SensitiveWordStrategyEnum.ZERO.getCode().equals(config)) {
            sensitiveWordService.batchesReplaceSensitiveWords(page.getRecords());
        }

        page.getRecords().stream().forEach(e -> {
            e.setUserName(nameDesensitization(e.getUserName()));
            List<AppraisePicInfo> pics = JSONArray.parseArray(e.getAppraisePics(), AppraisePicInfo.class);
            if (!CollectionUtils.isEmpty(pics)) {
                List<OSSFileBean> ossFileBeans = pics.stream().map(item -> {
                    OSSFileBean bean = new OSSFileBean();
                    bean.setDir(item.getFilePath());
                    bean.setObjectName(item.getFileName());
                    return bean;
                }).collect(Collectors.toList());
                ossFileBeans = ossClientUtil.getFileUrlsWithLocal(Global.OSS_FILE_BACKET_USERICON, ossFileBeans);
                if (!CollectionUtils.isEmpty(ossFileBeans)) {
                    List<String> urls = ossFileBeans.stream().map(OSSFileBean::getIconUrl).collect(Collectors.toList());
                    e.setAppraisePics(JSONArray.toJSONString(urls));
                }
            }
        });
        return ResultData.success(page);
    }

    @GetMapping("queryAppraiseByPageNew")
    public ResultData<Map<String, Object>> queryAppraiseByPageNew(String appId,
                                                                  @RequestParam(defaultValue = "1", required = false) int pageNum,
                                                                  @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                                  @RequestParam(defaultValue = "0", required = false) int scoreLevel) {
        String displayType = systemConfigService.getSystemConfigValue(Global.SYS_CONFIG_KEY_APP_APPRAISE_DISPLAY_TYPE);
        if (StringUtils.isEmpty(displayType)) {
            displayType = "0";
        }
        UserInfo userInfo = userUtil.getCurrentUser();
        if (null == userInfo) {
            throw new NoLoginException("当前用户未登录");
        }
        IPage<AppAppraiseEntity> page = appAppraiseService.queryAppAppraises(appId, scoreLevel, true,
                pageNum, pageSize, Integer.valueOf(displayType) == 1, userInfo.getUserAccount());

        // 敏感词处理
        String config = sensitiveWordService.getConfig(SensitiveWordStrategyEnum.ModelWid.getCode(), SensitiveWordStrategyEnum.SERVICE_STRATEGY.getCode());
        // 前台展示默认隐藏展示
        if (!SensitiveWordStrategyEnum.ZERO.getCode().equals(config)) {
            sensitiveWordService.batchesReplaceSensitiveWords(page.getRecords());
        }

        page.getRecords().stream().forEach(e -> {
            e.setUserName(nameDesensitization(e.getUserName()));
            if ("0".equals(e.getIsDeleted())) {
                List<AppraisePicInfo> pics = JSONArray.parseArray(e.getAppraisePics(), AppraisePicInfo.class);
                if (!CollectionUtils.isEmpty(pics)) {
                    List<OSSFileBean> ossFileBeans = pics.stream().map(item -> {
                        OSSFileBean bean = new OSSFileBean();
                        bean.setDir(item.getFilePath());
                        bean.setObjectName(item.getFileName());
                        return bean;
                    }).collect(Collectors.toList());
                    ossFileBeans = ossClientUtil.getFileUrlsWithLocal(Global.OSS_FILE_BACKET_USERICON, ossFileBeans);
                    if (!CollectionUtils.isEmpty(ossFileBeans)) {
                        List<String> urls = ossFileBeans.stream().map(OSSFileBean::getIconUrl).collect(Collectors.toList());
                        e.setAppraisePics(JSONArray.toJSONString(urls));
                    }
                }
            }
            // 被隐藏的评价，不展示内容与图片
            else {
                e.setContent("");
                e.setAppraisePics(null);
            }
        });
        int allCount = appAppraiseService.countAppAppraises(appId, 0, true, Integer.valueOf(displayType) == 1, userInfo.getWid());
        int goodCount = appAppraiseService.countAppAppraises(appId, 1, true, Integer.valueOf(displayType) == 1, userInfo.getWid());
        int middleCount = appAppraiseService.countAppAppraises(appId, 2, true, Integer.valueOf(displayType) == 1, userInfo.getWid());
        int badCount = appAppraiseService.countAppAppraises(appId, 3, true, Integer.valueOf(displayType) == 1, userInfo.getWid());

        Map<String, Object> result = new HashMap<>();
        result.put("data", page);
        result.put("goodCount", goodCount);
        result.put("middleCount", middleCount);
        result.put("badCount", badCount);
        result.put("allCount", allCount);
        return ResultData.success(result);
    }

    private void sendMessage(String userId, String serviceWid, String content) {
        try {
            String msgContent = "管理员给您的评价进行了回复：" + content;
            DubboServiceInfoApiResp dubboServiceInfoApiResp = homeService.queryServiceByWid(serviceWid, null);
            if (null != dubboServiceInfoApiResp && !CollectionUtils.isEmpty(dubboServiceInfoApiResp.getData())) {
                msgContent = "管理员给您对\"" + dubboServiceInfoApiResp.getData().get(0).getServiceName() + "\"的评价进行了回复：" + content;
            }
            List<DubboReceiverDTO> receivers = new ArrayList<>();
            DubboReceiverDTO receiverDTO = new DubboReceiverDTO();
            receiverDTO.setUserId(userId);
            receivers.add(receiverDTO);
            DubboMessageSendDTO messageSendDTO = new DubboMessageSendDTO();
            messageSendDTO.setAppId("casp.casp-portal");
            messageSendDTO.setAppName("门户引擎");
            messageSendDTO.setMsgType("0");
            messageSendDTO.setSubject("评价反馈");
            messageSendDTO.setContent(msgContent);
            messageSendDTO.setSendType("1");
            messageSendDTO.setReceivers(receivers);
            messageSendService.sendMessage(messageSendDTO);

        } catch (Exception e) {
            logger.warn("消息发送失败", e);
        }
    }

    @ManagerGatewayApi(name = "修改评价可见范围", realPath = "/appAppraise/updateDisplayType")
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_SERVICE_COMMENT)
    @PostMapping("updateDisplayType")
    public ResultData<?> updateDisplayType(HttpServletRequest request, @RequestBody AppAppraiseDisplayType appAppraiseDisplayType) {
        if(!MinosConsoleUtil.getIsSupperManager(request)){
            return ResultData.error("当前用户无操作权限");
        }
        if (null != appAppraiseDisplayType && null != appAppraiseDisplayType.getDisplayType()) {
            LambdaQueryWrapper<SystemConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemConfigEntity::getConfigKey, Global.SYS_CONFIG_KEY_APP_APPRAISE_DISPLAY_TYPE);
            SystemConfigEntity systemConfigEntity = systemConfigService.getOne(queryWrapper);
            if (null != systemConfigEntity) {
                systemConfigEntity.setConfigValue(String.valueOf(appAppraiseDisplayType.getDisplayType()));
                systemConfigService.update(systemConfigEntity, queryWrapper);
            } else {
                systemConfigEntity = new SystemConfigEntity();
                systemConfigEntity.setConfigKey(Global.SYS_CONFIG_KEY_APP_APPRAISE_DISPLAY_TYPE);
                systemConfigEntity.setConfigValue(String.valueOf(appAppraiseDisplayType.getDisplayType()));
                systemConfigEntity.setDefaultValue("0");
                systemConfigEntity.setIsDeleted(0);
                systemConfigEntity.setConfigDesc("评价可见范围 0:全部评价 1:与我相关");
                systemConfigEntity.setConfigModel(0);
                systemConfigService.save(systemConfigEntity);
            }
        }
        return ResultData.success();
    }

    @ManagerGatewayApi(name = "评价可见范围", realPath = "/appAppraise/getDisplayType")
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_SERVICE_COMMENT)
    @GetMapping("getDisplayType")
    public ResultData<?> getDisplayType(HttpServletRequest request) {
        if(!MinosConsoleUtil.getIsSupperManager(request)){
            return ResultData.error("当前用户无操作权限");
        }
        String displayType = systemConfigService.getSystemConfigValue(Global.SYS_CONFIG_KEY_APP_APPRAISE_DISPLAY_TYPE);
        if (StringUtils.isEmpty(displayType)) {
            displayType = "0";
        }
        return ResultData.success(Integer.valueOf(displayType));
    }

    @ManagerGatewayApi(name = "管理员回复评价", realPath = "/appAppraise/adminReply")
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_SERVICE_COMMENT)
    @PostMapping("adminReply")
    public ResultData<?> adminReply(HttpServletRequest request,@RequestBody AdminReplyInfo adminReplyInfo) {
        AppAppraiseEntity appAppraiseEntity = new AppAppraiseEntity();
        appAppraiseEntity.setWid(adminReplyInfo.getAppraiseWid());
        Map<String, String> adminComment = new HashMap<>();
        adminComment.put("userId", MinosConsoleUtil.getLoginUserId(request));
        adminComment.put("userName", MinosConsoleUtil.getLoginUserName(request));
        adminComment.put("replyTime", DateUtil.getStrFromDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        adminComment.put("replyDetail", adminReplyInfo.getReplyDetail());
        appAppraiseEntity.setAdminComment(JSONObject.toJSONString(adminComment));
        appAppraiseService.updateById(appAppraiseEntity);
        appAppraiseEntity = appAppraiseService.getById(adminReplyInfo.getAppraiseWid());
        sendMessage(appAppraiseEntity.getUserId(), appAppraiseEntity.getAppId(), adminReplyInfo.getReplyDetail());
        return ResultData.success();
    }

    @ManagerGatewayApi(name = "查询评价详情", realPath = "/appAppraise/adminQueryAppraiseByPage")
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_LOOK, menu = MenuEditTypeEnum.MENU_ID_SERVICE_COMMENT)
    @GetMapping("adminQueryAppraiseByPage")
    public ResultData<Map<String, Object>> adminQueryAppraiseByPage(String appId, @RequestParam(defaultValue = "0", required = false) int scoreLevel, @RequestParam(defaultValue = "1", required = false) int pageNum, @RequestParam(defaultValue = "10", required = false) int pageSize) {
        IPage<AppAppraiseEntity> page = appAppraiseService.queryAppAppraises(appId, scoreLevel, true, pageNum, pageSize, false, "");

        page.getRecords().stream().forEach(e -> {
            e.setUserName(nameDesensitization(e.getUserName()));
            List<AppraisePicInfo> pics = JSONArray.parseArray(e.getAppraisePics(), AppraisePicInfo.class);
            if (!CollectionUtils.isEmpty(pics)) {
                List<OSSFileBean> ossFileBeans = pics.stream().map(item -> {
                    OSSFileBean bean = new OSSFileBean();
                    bean.setDir(item.getFilePath());
                    bean.setObjectName(item.getFileName());
                    return bean;
                }).collect(Collectors.toList());
                ossFileBeans = ossClientUtil.getFileUrlsWithLocal(Global.OSS_FILE_BACKET_USERICON, ossFileBeans);
                if (!CollectionUtils.isEmpty(ossFileBeans)) {
                    List<String> urls = ossFileBeans.stream().map(OSSFileBean::getIconUrl).collect(Collectors.toList());
                    e.setAppraisePics(JSONArray.toJSONString(urls));
                }
            }
        });
        int allCount = appAppraiseService.countAppAppraises(appId, 0, true, false, "");
        int goodCount = appAppraiseService.countAppAppraises(appId, 1, true, false, "");
        int middleCount = appAppraiseService.countAppAppraises(appId, 2, true, false, "");
        int badCount = appAppraiseService.countAppAppraises(appId, 3, true, false, "");
        Map<String, Object> result = new HashMap<>();
        result.put("data", page);
        result.put("allCount", allCount);
        result.put("goodCount", goodCount);
        result.put("middleCount", middleCount);
        result.put("badCount", badCount);
        return ResultData.success(result);
    }

    @ManagerGatewayApi(name = "隐藏评价详情", realPath = "/appAppraise/deleteAppraise")
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_SERVICE_COMMENT)
    @PostMapping("deleteAppraise")
    public ResultData deleteAppraise(@RequestBody DeletedAppraiseReq deletedAppraiseReq) {
        //添加校验用户是否有操作权限
        if (!MenuEditTypeEnum.isEditRole(MenuEditTypeEnum.MENU_EDIT_TYPE_SET, MenuEditTypeEnum.MENU_ID_SERVICE_COMMENT)) {
            return ResultData.error("当前用户无操作权限");
        }
        if (appAppraiseService.update(appAppraiseService.lambdaUpdate().set(AppAppraiseEntity::getIsDeleted, deletedAppraiseReq.getIsDeleted()).eq(AppAppraiseEntity::getWid, deletedAppraiseReq.getWid()).getWrapper())) {
            return ResultData.success();
        } else {
            return ResultData.error("删除评论失败");
        }

    }

    @PostMapping("saveAppraise")
    @MustLogin
    public ResultData saveAppraise(@RequestBody AppAppraiseEntity appAppraiseEntity) {
        UserInfo currentUser = userUtil.getCurrentUser();

        if (appAppraiseEntity.getWid() == null) {
            appAppraiseEntity.setWid(MinosCommonUtil.getWid());
            appAppraiseEntity.setUserId(currentUser.getUserAccount());
            appAppraiseEntity.setUserName(currentUser.getUserName());
            appAppraiseEntity.setIsDeleted("0");
        }
        try {
            // 对评论内容进行XSS校验
            XSSKeyUtil.isXssIllegalWord(appAppraiseEntity.getContent());

            // 对评价内容进行敏感词处理
            // 获取敏感词策略配置
            String config = sensitiveWordService.getConfig(SensitiveWordStrategyEnum.ModelWid.getCode(), SensitiveWordStrategyEnum.SERVICE_STRATEGY.getCode());
            // "0" 不处理
            if (SensitiveWordStrategyEnum.TWO.getCode().equals(config)) {
                // 禁发
                if (sensitiveWordService.isContainsSensitiveWord(appAppraiseEntity.getContent())) {
                    throw new MinosException("-1", "填写内容包含敏感词");
                }
            }
        } catch (Exception e) {
            logger.warn("保存评价异常", e);
            return ResultData.error(e.getMessage());
        }
        if (appAppraiseService.saveOrUpdate(appAppraiseEntity)) {
            return ResultData.success();
        } else {
            return ResultData.error("保存评价失败");
        }
    }

    @PostMapping("saveAppraiseWithPic")
    @MustLogin
    public ResultData saveAppraiseWithPic(@RequestPart(value = "uploadFile", required = false) List<MultipartFile> files,
                                          @RequestPart("data") String appAppraiseStr) {
        if (StringUtils.isEmpty(appAppraiseStr)) {
            throw new MinosException("-1", "入参不合法");
        }
        AppAppraiseEntity appAppraiseEntity = JSONObject.parseObject(appAppraiseStr, AppAppraiseEntity.class);

        UserInfo currentUser = userUtil.getCurrentUser();

        if (appAppraiseEntity.getWid() == null) {
            appAppraiseEntity.setWid(MinosCommonUtil.getWid());
            appAppraiseEntity.setUserId(currentUser.getUserAccount());
            appAppraiseEntity.setUserName(currentUser.getUserName());
            appAppraiseEntity.setIsDeleted("0");
        }
        try {
            // 对评论内容进行XSS校验
            XSSKeyUtil.isXssIllegalWord(appAppraiseEntity.getContent());

            // 对评价内容进行敏感词处理
            // 获取敏感词策略配置
            String config = sensitiveWordService.getConfig(SensitiveWordStrategyEnum.ModelWid.getCode(), SensitiveWordStrategyEnum.SERVICE_STRATEGY.getCode());
            // "0" 不处理
            if (SensitiveWordStrategyEnum.TWO.getCode().equals(config)) {
                // 禁发
                if (sensitiveWordService.isContainsSensitiveWord(appAppraiseEntity.getContent())) {
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
                    info.setFileName(getFileName(appAppraiseEntity.getWid(), file));
                    ossClientUtil.upload(Global.OSS_FILE_BACKET_USERICON, info.getFilePath(), info.getFileName(), file);
                    appraisePicInfos.add(info);
                }
                appAppraiseEntity.setAppraisePics(JSONArray.toJSONString(appraisePicInfos));
            }
        } catch (Exception e) {
            logger.warn("保存评价异常", e);
            return ResultData.error(e.getMessage());
        }
        if (appAppraiseService.saveOrUpdate(appAppraiseEntity)) {
            return ResultData.success();
        } else {
            return ResultData.error("保存评价失败");
        }
    }
    @PostMapping("saveAppraiseWithPicBase64")
    @MustLogin
    public ResultData saveAppraiseWithPicBase64(@RequestBody UploadAppraiseRequest request) {
        if (null == request || StringUtils.isEmpty(request.getData())) {
            throw new MinosException("-1", "入参不合法");
        }
        AppAppraiseEntity appAppraiseEntity = JSONObject.parseObject(request.getData(), AppAppraiseEntity.class);

        UserInfo currentUser = userUtil.getCurrentUser();

        if (appAppraiseEntity.getWid() == null) {
            appAppraiseEntity.setWid(MinosCommonUtil.getWid());
            appAppraiseEntity.setUserId(currentUser.getUserAccount());
            appAppraiseEntity.setUserName(currentUser.getUserName());
            appAppraiseEntity.setIsDeleted("0");
        }
        try {
            // 对评论内容进行XSS校验
            XSSKeyUtil.isXssIllegalWord(appAppraiseEntity.getContent());

            // 对评价内容进行敏感词处理
            // 获取敏感词策略配置
            String config = sensitiveWordService.getConfig(SensitiveWordStrategyEnum.ModelWid.getCode(), SensitiveWordStrategyEnum.SERVICE_STRATEGY.getCode());
            // "0" 不处理
            if (SensitiveWordStrategyEnum.TWO.getCode().equals(config)) {
                // 禁发
                if (sensitiveWordService.isContainsSensitiveWord(appAppraiseEntity.getContent())) {
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
                    info.setFileName(getFileName(appAppraiseEntity.getWid(), fileType));
                    ossClientUtil.upload(Global.OSS_FILE_BACKET_USERICON, info.getFilePath(), info.getFileName(), content);
                    appraisePicInfos.add(info);
                }
                appAppraiseEntity.setAppraisePics(JSONArray.toJSONString(appraisePicInfos));
            }
        } catch (Exception e) {
            logger.warn("保存评价异常", e);
            return ResultData.error(e.getMessage());
        }
        if (appAppraiseService.saveOrUpdate(appAppraiseEntity)) {
            return ResultData.success();
        } else {
            return ResultData.error("保存评价失败");
        }
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


    @ManagerGatewayApi(name = "查询服务评价统计列表", realPath = "/appAppraise/queryAppraiseSummaryByPage")
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_LOOK, menu = MenuEditTypeEnum.MENU_ID_SERVICE_COMMENT)
    @PostMapping("queryAppraiseSummaryByPage")
    public ResultData<List<AppAppraiseSummaryVo>> queryAppraiseSummaryByPage(@RequestBody QueryAppraiseSummaryRequest request) {
        AllServiceRequest allServiceRequest = new AllServiceRequest();
        allServiceRequest.setSearchKey(request.getKeyword());
        allServiceRequest.setPageNumber(request.getPageNum());
        allServiceRequest.setPageSize(request.getPageSize());
        allServiceRequest.setDomainWidList(request.getDomainWidList());
        allServiceRequest.setConsole(true);
        //添加xss漏洞检查
        try {
            XSSKeyUtil.isXssIllegalWord(request.getKeyword());
        } catch (Exception e) {
            logger.warn("查询服务评价统计列表异常", e);
            return ResultData.error(e.getMessage());
        }

        List<AllServiceResponse.ServiceModel> serviceModels = serviceApiService.getAllService(allServiceRequest);
        List<String> serviceWids = serviceModels.stream().map(e -> e.getServiceWid()).collect(Collectors.toList());

        Map<String, AppAppraiseSummaryVo> appAppraiseSummaryVoMap = appAppraiseService.queryAppAppraiseSummary(serviceWids).stream().collect(Collectors.toMap(e -> e.getServiceWid(), e -> e));

        List<AppAppraiseSummaryVo> appAppraiseSummaryVoList = serviceModels.stream().map(e -> {
            appAppraiseSummaryVoMap.get(e.getServiceWid());
            AppAppraiseSummaryVo appAppraiseSummaryVo = appAppraiseSummaryVoMap.get(e.getServiceWid());
            if (appAppraiseSummaryVo == null) {
                appAppraiseSummaryVo = new AppAppraiseSummaryVo();
                appAppraiseSummaryVo.setServiceWid(e.getServiceWid());
                appAppraiseSummaryVo.setAvgScore(0);
                appAppraiseSummaryVo.setTotal(0);
            }
            appAppraiseSummaryVo.setServiceName(e.getServiceName());
            String picIconLink = e.getServiceStation() == 1 ? e.getMobileIconLink() : e.getIconLink();
            appAppraiseSummaryVo.setServiceIcon(picIconLink);
            return appAppraiseSummaryVo;
        }).collect(Collectors.toList());

        Collections.sort(appAppraiseSummaryVoList, new AppAppraiseComparator().thenComparing(new AppLetterComparator()));
        return ResultData.success(appAppraiseSummaryVoList);
    }


    /**
     * 名字脱敏
     * 规则，张三丰，脱敏为：张*丰
     *
     * @param name
     * @return
     */
    String nameDesensitization(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        String myName = null;
        char[] chars = name.toCharArray();
        if (chars.length == 1) {
            myName = name;
        }
        if (chars.length == 2) {
            myName = name.replaceFirst(name.substring(1), "*");
        }
        if (chars.length > 2) {
            myName = name.replaceAll(name.substring(1, chars.length - 1), "*");
        }
        return myName;
    }

}
