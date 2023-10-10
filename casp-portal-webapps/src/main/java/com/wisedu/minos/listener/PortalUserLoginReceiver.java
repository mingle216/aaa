package com.wisedu.minos.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.utils.DateUtil;
import com.wisedu.minos.casp.portal.common.utils.ObjectUtil;
import com.wisedu.minos.casp.portal.dao.entity.UserLoginEntity;
import com.wisedu.minos.casp.portal.dao.mapper.UserLoginMapper;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.queue.annotation.QueueReceiver;
import com.wisedu.minos.queue.common.MinosConstants;
import com.wisedu.minos.queue.listener.MinosQueueMessageListener;
import com.wisedu.minos.util.MinosCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述：门户上报用户登录信息到使用分析接收者
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title PortalUserLoginReceiver
 * @Author: 01120034
 * @Date: 2021/11/22
 */
@Component
@QueueReceiver(exchangeName = MinosConstants.RabbitMqExchangeQueue.EXCHANGE_PORTAL_USER_LOGIN, queueName = Global.USER_LOGIN_QUEUE_NAME)
public class PortalUserLoginReceiver implements MinosQueueMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(PortalUserLoginReceiver.class);

    @Autowired
    private UserLoginMapper userLoginMapper;
//    @Autowired
//    private PortalUserLoginBackupsMapper portalUserLoginBackupsMapper;
    @Override
    public void onMessage (String s) {
        logger.debug("=========>>>已接收到门户上报用户登录信息：{}",s);
        if( StringUtils.isNotBlank(s) ){
            JSONObject jsonObject = JSON.parseObject(s);
            UserLoginEntity userLoginEntity = new UserLoginEntity();
            String userAccount = ( String ) jsonObject.get("userAccount");
            String loginTime = ( String ) jsonObject.get("loginTime");
            int platformType = ( int ) jsonObject.get("platformType");
            String loginIp = ( String ) jsonObject.get("loginIp");
            userLoginEntity.setWid(MinosCommonUtil.getWid());
            userLoginEntity.setUserAccount(userAccount);
            userLoginEntity.setModelPlatform(platformType);
            userLoginEntity.setLoginIp(loginIp);
            userLoginEntity.setCreateTime(DateUtil.getDateFromStr(loginTime,DateUtil.DATE_FORMATE_STRING_A));
//            userLoginEntity portalUserLoginBackupsEntity = ObjectUtil.copyProperties(portalUserLoginEntity, new PortalUserLoginBackupsEntity());
//            portalUserLoginBackupsMapper.insert(portalUserLoginBackupsEntity);
            userLoginMapper.insert(userLoginEntity);
            //将登录用户最新的两条数据写入redis
            String userLoginKey = Global.USER_LOGIN_INFO_PREFIX + userAccount;
            String redisLoginData = (String) ApplicationContextUtil.get(RedisUtil.class).get(userLoginKey);
            List<UserLoginEntity> result = Lists.newArrayList();
            if(StringUtils.isNotBlank(redisLoginData)){
                //有登录记录
                List<UserLoginEntity> userLoginEntityList = JSON.parseObject(redisLoginData, new TypeReference<List<UserLoginEntity>>() {});
                if(CollectionUtils.isNotEmpty(userLoginEntityList)){
                    userLoginEntityList.add(userLoginEntity);
                    List<UserLoginEntity> userLoginEntities = userLoginEntityList.stream().sorted(Comparator.comparing(UserLoginEntity::getCreateTime).reversed()).collect(Collectors.toList());
                    if(userLoginEntities.size()>2){
                        result.addAll(userLoginEntities.subList(0, 2));
                    }else{
                        result.addAll(userLoginEntities);
                    }
                }
            }else{
                //从未登录过
                result.add(userLoginEntity);
            }
            ApplicationContextUtil.get(RedisUtil.class).save(userLoginKey,result);
        }
    }
}
