package com.wisedu.minos.casp.portal.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.dao.entity.MailPluginEntity;
import com.wisedu.minos.casp.portal.dao.entity.UserEmailEntity;
import com.wisedu.minos.casp.portal.dao.mapper.MailPluginMapper;
import com.wisedu.minos.casp.portal.dao.mapper.UserEmailMapper;
import com.wisedu.minos.casp.portal.spi.MinosExtensionLoader;
import com.wisedu.minos.casp.portal.spi.itf.IMail;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title MailUtilImpl
 * @Author: 01319098
 * @Date: 2021/3/2
 */
@Service
public class MailUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);
    private final static String SEPARATOR_ATTR = "@";

    @Autowired
    private MailPluginMapper mailPluginMapper;

    @Autowired
    private UserEmailMapper userEmailMapper;

    public int getUnreadCount (String userAccount) {
        // 根据邮箱后缀判断是什么邮箱插件
        final IMail iMail = getMailPluginByAccount(userAccount);
        int obj = 0;
        if ( null != iMail ) {
            try {
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                FutureTask<Integer> unreadCountTask = new FutureTask<>(()->{
                    RequestContextHolder.setRequestAttributes(requestAttributes);
                    return iMail.getUnreadCount(userAccount);
                });
                Executor executor = (Executor) ApplicationContextUtil.get("asyncExecutor");
                executor.execute(unreadCountTask);
                obj = unreadCountTask.get();
                LOGGER.debug("调用接口返回成功");
            } catch ( Exception e ) {
                LOGGER.error("获取未读邮件数量失败", e);
                throw new MinosException("获取未读邮件数量失败");
            }
        } else {
            throw new MinosException("获取未读邮件数量失败：不支持的邮箱后缀");
        }
        return obj;
    }

    public String getLinkUrl (String userAccount) {
        // 根据邮箱后缀判断是什么邮箱插件
        final IMail iMail = getMailPluginByAccount(userAccount);
        String obj = "";
        if ( null != iMail ) {
            try {
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                FutureTask<String> linkUrlTask = new FutureTask<>(()->{
                    RequestContextHolder.setRequestAttributes(requestAttributes);
                    return iMail.getLinkUrl(userAccount);
                });
                Executor executor = (Executor) ApplicationContextUtil.get("asyncExecutor");
                executor.execute(linkUrlTask);
                obj = linkUrlTask.get();
                LOGGER.debug("调用接口返回成功");
            } catch ( Exception e ) {
                LOGGER.error("获取邮箱单点登录地址失败", e);
                throw new MinosException("获取邮箱单点登录地址失败");
            }
        } else {
            throw new MinosException("获取邮箱单点登录地址失败：不支持的邮箱后缀");
        }
        return obj;
    }

    /**
     * 绑定邮箱是校验校验邮箱地址
     */
    public boolean verifyMail (String mailAccount, String userId) {
        // 校验后缀是否支持
        if ( null == getMailPluginByAccount(mailAccount) ) {
            return false;
        }

        // 判断邮箱是否已经本人被绑定
        QueryWrapper<UserEmailEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEmailEntity::getEmail, mailAccount)
                .eq(UserEmailEntity::getUserWid, userId)
                .eq(UserEmailEntity::getIsDelete, Global.DeleteStatus.NO.getId())
                .isNull(UserEmailEntity::getSourceType);
        List<UserEmailEntity> userEmailList = userEmailMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(userEmailList);
    }

    /**
     * 绑定邮箱是校验校验邮箱地址
     */
    public boolean verifyMail (String mailAccount, String userId, String sourceType) {
        // 校验后缀是否支持
        if ( null == getMailPluginByAccount(mailAccount) ) {
            return false;
        }

        // 判断邮箱是否已经本人被绑定
        QueryWrapper<UserEmailEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEmailEntity::getEmail, mailAccount)
                .eq(UserEmailEntity::getUserWid, userId)
                .eq(UserEmailEntity::getIsDelete, Global.DeleteStatus.NO.getId());
        if(StringUtils.isEmpty(sourceType)){
            queryWrapper.lambda().isNull(UserEmailEntity::getSourceType);
        }else{
            queryWrapper.lambda().eq(UserEmailEntity::getSourceType, sourceType);
        }
        List<UserEmailEntity> userEmailList = userEmailMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(userEmailList);
    }

    /**
     * 根据邮箱后缀判断是什么邮箱插件
     *
     * @param userAccount
     * @return
     */
    public IMail getMailPluginByAccount (String userAccount) {
        String mailSuffix = null;
        if ( userAccount.contains(SEPARATOR_ATTR) ) {
            mailSuffix = userAccount.split(SEPARATOR_ATTR)[ 1 ];
        }
        List<MailPluginEntity> mailPluginList = mailPluginMapper.selectList(Wrappers.<MailPluginEntity>lambdaQuery().eq(MailPluginEntity::getMailSuffix, mailSuffix));
        if ( CollectionUtils.isNotEmpty(mailPluginList) ) {
            return getMailById(mailPluginList.get(0).getMailPluginName());
        }
        LOGGER.error("不支持的邮箱后缀:{}", mailSuffix);
        return null;
    }


    public Map<String, IMail> getMailMap () {
        return MinosExtensionLoader.getExtensionLoader(IMail.class).getSupportedExtensionInstances();
    }

    private IMail getMailById (String mailId) {
        Map<String, IMail> mailMap = getMailMap();
        IMail mail = mailMap.get(mailId);
        if ( null == mail ) {
            throw new BusinessException(String.format("系统异常，该邮箱%s未安装", mailId));
        }
        return mail;
    }

    /**
     * 生成六位验证码
     *
     * @return
     */
    public String getVerificationCode () {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        IntStream.range(0, 6).forEach(i -> code.append(random.nextInt(10)));
        return code.toString();
    }

}
