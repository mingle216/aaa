package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.dao.entity.ServiceLatestUseEntity;
import com.wisedu.minos.casp.portal.dao.mapper.ServiceLatestUseMapper;
import com.wisedu.minos.casp.portal.model.AllServiceRequest;
import com.wisedu.minos.casp.portal.model.AllServiceResponse;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @title ServiceLatestUseService
 * @Author: gulong
 */
@Service
@Slf4j
public class ServiceLatestUseService extends ServiceImpl<ServiceLatestUseMapper, ServiceLatestUseEntity> {

    @Autowired
    ServiceLatestUseMapper serviceLatestUseMapper;

    @Autowired
    ServiceApiService serviceApiService;

    /**
     * 功能描述：根据条件查询最近使用服务列表
     *
     * @title getLatestServices
     * @Author: gulong
     */
    public List<AllServiceResponse.ServiceModel> getLatestServices(String userId) {
        List<AllServiceResponse.ServiceModel> serviceModelList = new ArrayList<>();
        List<ServiceLatestUseEntity> services =
                serviceLatestUseMapper.selectList(Wrappers.<ServiceLatestUseEntity>lambdaQuery().eq(ServiceLatestUseEntity::getUserId, userId).orderByDesc(ServiceLatestUseEntity::getUseTime));

        if (CollectionUtils.isNotEmpty(services)) {
            // 把查出来的数据转成map格式 key ->wid value ->entity
            Map<String, ServiceLatestUseEntity> latestUseEntityMap = services.stream().collect(Collectors.toMap(ServiceLatestUseEntity::getServiceWid, entity -> entity));

            // 查询全部服务接口
            AllServiceRequest allServiceRequest = getAllServiceRequest(userId);
            List<AllServiceResponse.ServiceModel> allService = serviceApiService.getAllService(allServiceRequest);
            if (CollectionUtils.isNotEmpty(allService)) {
                for (AllServiceResponse.ServiceModel serviceModel : allService) {
                    if (latestUseEntityMap.get(serviceModel.getServiceWid()) != null) {
                        serviceModel.setUseTime(latestUseEntityMap.get(serviceModel.getServiceWid()).getUseTime());
                        serviceModelList.add(serviceModel);
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(serviceModelList)){
            serviceModelList = serviceModelList.stream().sorted(Comparator.comparing(AllServiceResponse.ServiceModel::getUseTime).reversed()).collect(Collectors.toList());
        }
        return serviceModelList;
    }

    private AllServiceRequest getAllServiceRequest(String userId) {
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        AllServiceRequest allServiceRequest = new AllServiceRequest();
        allServiceRequest.setUserId(userId);
        allServiceRequest.setPageSize(9999);
        if(isMobileDevice){
            // 看移动端是否需要展示pc端服务
            boolean isShowPCService = ApplicationContextUtil.get(CardUtil.class).getIsShowPCService();
            if(!isShowPCService){
                allServiceRequest.setServiceStation("1");
            }
        } else{
            allServiceRequest.setServiceStation("0");
        }
        return allServiceRequest;
    }

}
