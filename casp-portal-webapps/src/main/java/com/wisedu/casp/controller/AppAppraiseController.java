package com.wisedu.casp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisedu.casp.controller.bean.DeletedAppraiseReq;
import com.wisedu.minos.api.constant.SensitiveWordStrategyEnum;
import com.wisedu.minos.casp.portal.service.impl.SensitiveWordService;
import com.wisedu.minos.casp.portal.utils.comparatorUtil.AppAppraiseComparator;
import com.wisedu.minos.casp.portal.utils.comparatorUtil.AppLetterComparator;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.dao.entity.AppAppraiseEntity;
import com.wisedu.minos.casp.portal.model.AllServiceRequest;
import com.wisedu.minos.casp.portal.model.AllServiceResponse;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.service.impl.AppAppraiseService;
import com.wisedu.minos.casp.portal.service.impl.ServiceApiService;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.casp.portal.vo.AppAppraiseSummaryVo;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import com.wisedu.minos.gateway.client.util.XSSKeyUtil;
import com.wisedu.minos.util.MenuEditTypeEnum;
import com.wisedu.minos.util.MinosCommonUtil;
import com.wisedu.minos.util.MinosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appAppraise")
public class AppAppraiseController {
    @Autowired
    AppAppraiseService appAppraiseService;

    @Autowired
    ServiceApiService serviceApiService;

    @Autowired
    SensitiveWordService sensitiveWordService;

    @Autowired
    UserUtil userUtil;

    @GetMapping("/myAppraiseExists")
    public ResultData<Boolean> exists(String appId) {
        UserInfo currentUser = userUtil.getCurrentUser();
//        currentUser = new UserInfo();
//        currentUser.setUserAccount("test");
//        currentUser.setUserName("张三2");
        boolean exists = false;
        if (currentUser != null) {
            exists = appAppraiseService.exists(appId, currentUser.getUserAccount());
        }
        return ResultData.success(exists);
    }

    @GetMapping("appraiseSummary")
    public ResultData<Object> appraiseSummary(String appId, @RequestParam(defaultValue = "2", required = false) int type) {
        UserInfo currentUser = userUtil.getCurrentUser();
//        currentUser = new UserInfo();
//        currentUser.setUserAccount("test");
//        currentUser.setUserName("张三2");

        Map<String, Object> resp = new HashMap<>();
        resp.put("isAppraised", appAppraiseService.exists(appId, currentUser.getUserAccount()));
        resp.put("appraiseSummary", appAppraiseService.queryAppAppraiseSummary(appId));
        return ResultData.success(resp);
    }

    @GetMapping("queryAppraiseByPage")
    public ResultData<IPage<AppAppraiseEntity>> queryAppraiseByPage(String appId, @RequestParam(defaultValue = "1", required = false) int pageNum, @RequestParam(defaultValue = "10", required = false) int pageSize) {
        IPage<AppAppraiseEntity> page = appAppraiseService.queryAppAppraises(appId, pageNum, pageSize);

        // 敏感词处理
        String config = sensitiveWordService.getConfig(SensitiveWordStrategyEnum.ModelWid.getCode(), SensitiveWordStrategyEnum.SERVICE_STRATEGY.getCode());
        // 前台展示默认隐藏展示
        if (!SensitiveWordStrategyEnum.ZERO.getCode().equals(config)){
            sensitiveWordService.batchesReplaceSensitiveWords(page.getRecords());
        }

        page.getRecords().stream().forEach(e -> {
            e.setUserName(nameDesensitization(e.getUserName()));
        });
        return ResultData.success(page);
    }

    @ManagerGatewayApi(name="查询评价详情",realPath = "/appAppraise/adminQueryAppraiseByPage")
    @GetMapping("adminQueryAppraiseByPage")
    public ResultData<IPage<AppAppraiseEntity>> adminQueryAppraiseByPage(String appId, @RequestParam(defaultValue = "0", required = false) int scoreLevel, @RequestParam(defaultValue = "1", required = false) int pageNum, @RequestParam(defaultValue = "10", required = false) int pageSize) {
        IPage<AppAppraiseEntity> page = appAppraiseService.queryAppAppraises(appId, scoreLevel,true, pageNum, pageSize);

        // 管控台查询评价不处理敏感词
//        // 敏感词处理
//        String config = sensitiveWordService.getConfig("sensitiveWord", "casp.service.sensitiveWord.strategy");
//        // 前台展示默认隐藏展示
//        config = "0".equals(config) ? "0" : "1";
//        List<String> allSensitiveWord = sensitiveWordService.getAllSensitiveWord();

//        String finalConfig = config;
        page.getRecords().stream().forEach(e -> {
            e.setUserName(nameDesensitization(e.getUserName()));
//            e.setContent(sensitiveWordService.dealSensitiveWord(e.getContent(), finalConfig, allSensitiveWord));
        });
        return ResultData.success(page);
    }

    @ManagerGatewayApi(name="隐藏评价详情",realPath = "/appAppraise/deleteAppraise")
    @PostMapping("deleteAppraise")
    public ResultData deleteAppraise(@RequestBody DeletedAppraiseReq deletedAppraiseReq){
        //添加校验用户是否有操作权限
        if(!MenuEditTypeEnum.isEditRole(MenuEditTypeEnum.MENU_EDIT_TYPE_SET,MenuEditTypeEnum.MENU_ID_SERVICE_COMMENT)){
            return ResultData.error("当前用户无操作权限");
        }
        if(appAppraiseService.update(appAppraiseService.lambdaUpdate().set(AppAppraiseEntity::getIsDeleted,deletedAppraiseReq.getIsDeleted()).eq(AppAppraiseEntity::getWid,deletedAppraiseReq.getWid()).getWrapper())){
            return ResultData.success();
        }else
        {
            return ResultData.error("删除评论失败");
        }

    }

    @PostMapping("saveAppraise")
    public ResultData saveAppraise(@RequestBody AppAppraiseEntity appAppraiseEntity) {
        UserInfo currentUser = userUtil.getCurrentUser();
//        currentUser = new UserInfo();
//        currentUser.setUserAccount("test");
//        currentUser.setUserName("张三2");
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
            if(SensitiveWordStrategyEnum.TWO.getCode().equals(config)){
                // 禁发
                if(sensitiveWordService.isContainsSensitiveWord(appAppraiseEntity.getContent())){
                    throw new MinosException("-1", "填写内容包含敏感词");
                }
            }
        }catch (Exception e){
            return ResultData.error(e.getMessage());
        }
        if (appAppraiseService.saveOrUpdate(appAppraiseEntity)) {
            return ResultData.success();
        } else {
            return ResultData.error("保存评价失败");
        }
    }


    @ManagerGatewayApi(name="查询服务评价统计列表",realPath = "/appAppraise/queryAppraiseSummaryByPage")
    @GetMapping("queryAppraiseSummaryByPage")
    public ResultData<List<AppAppraiseSummaryVo>> queryAppraiseSummaryByPage(String keyword, @RequestParam(required = false,defaultValue = "1") int pageNum, @RequestParam(required = false,defaultValue = "1000000") int pageSize) {
        AllServiceRequest allServiceRequest = new AllServiceRequest();
        allServiceRequest.setSearchKey(keyword);
        allServiceRequest.setPageNumber(pageNum);
        allServiceRequest.setPageSize(pageSize);
        //添加xss漏洞检查
        try {
            XSSKeyUtil.isXssIllegalWord(keyword);
        }catch (Exception e){
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
            String picIconLink = e.getServiceStation() == 1? e.getMobileIconLink() : e.getIconLink();
            appAppraiseSummaryVo.setServiceIcon(picIconLink);
            return appAppraiseSummaryVo;
        }).collect(Collectors.toList());

        Collections.sort(appAppraiseSummaryVoList,new AppAppraiseComparator().thenComparing(new AppLetterComparator()));
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
