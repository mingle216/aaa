package com.wisedu.minos.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
@QueueReceiver(exchangeName = MinosConstants.RabbitMqExchangeQueue.EXCHANGE_STATA_PORTAL_USER_LOGIN, queueName = Global.USER_LOGIN_QUEUE_NAME)
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
            userLoginEntity.setUserAccount(userAccount);
            userLoginEntity.setModelPlatform(platformType);
            userLoginEntity.setLoginIp(loginIp);
            userLoginEntity.setCreateTime(DateUtil.getDateFromStr(loginTime,DateUtil.DATE_FORMATE_STRING_A));
//            userLoginEntity portalUserLoginBackupsEntity = ObjectUtil.copyProperties(portalUserLoginEntity, new PortalUserLoginBackupsEntity());
//            portalUserLoginBackupsMapper.insert(portalUserLoginBackupsEntity);
            userLoginMapper.insert(userLoginEntity);

            //将登录用户最新的两条数据写入redis
            LambdaQueryWrapper<UserLoginEntity> userLoginEntityWrapper = new LambdaQueryWrapper<UserLoginEntity>()
                    .eq(UserLoginEntity::getUserAccount,userAccount);
            List<UserLoginEntity> userLoginEntityList = userLoginMapper.selectList(userLoginEntityWrapper);
            List<UserLoginEntity> latestList = new ArrayList<>();
            if(userLoginEntityList.size()>1){
                latestList = userLoginEntityList.stream().sorted(Comparator.comparing(UserLoginEntity::getCreateTime).reversed()).collect(Collectors.toList()).subList(0,2);
            }else{
                latestList = userLoginEntityList;
            }
            String userLoginKey = Global.USER_LOGIN_INFO_PREFIX + userAccount;
            ApplicationContextUtil.get(RedisUtil.class).save(userLoginKey,latestList);
        }
    }
}
