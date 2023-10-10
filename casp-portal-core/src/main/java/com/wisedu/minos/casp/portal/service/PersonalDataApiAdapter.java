package com.wisedu.minos.casp.portal.service;

import com.wisedu.minos.casp.portal.model.CommonResponseResult;
import com.wisedu.minos.casp.portal.model.personal.*;
import com.wisedu.minos.casp.portal.spi.model.MailPluginInfo;
import com.wisedu.minos.casp.portal.spi.model.PluginInfo;

import java.util.List;

/**
 * @ClassName PersonalDataApiAdapter
 * @Description //TODO
 * @Date 2021/7/13 16:14
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
public interface PersonalDataApiAdapter {
    /**
     * 添加个人数据配置
     * @param dto
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/14 14:48
     */
    CommonResponseResult addPersonDataConfig(PersonalDataDto dto);
    /**
     * 获取个人数据配置
     * @param dto
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult<java.util.List<com.wisedu.minos.casp.portal.model.personal.PersonalDataVo>>
     * @author jszhang
     * @date: 2021/7/14 17:05
     */
    CommonResponseResult<List<PersonalDataVo>> getPersonalDataConfig(PersonalDataQueryConfigDto dto);
    /***
     * 编辑
     * @param dto
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/15 15:24
     */
    CommonResponseResult editPersonDataConfig(PersonalDataDto dto);
    /**
     * 获取邮箱插件
     * @param
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult<java.util.List<com.wisedu.minos.casp.portal.spi.model.MailPluginInfo>>
     * @author jszhang
     * @date: 2021/7/15 16:57
     */
    CommonResponseResult<List<MailPluginInfo>> getMailPlugins();
    /**
     * 更新启停用状态
     * @param dto
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/19 9:35
     */
    CommonResponseResult updateEnableState(UpdateStateDto dto);
    /**
     * 排序
     * @param dto
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/19 9:42
     */
    CommonResponseResult updateSorting(List<UpdateSortingDto> dto);
    /**
     * 删除
     * @param wid
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/19 10:09
     */
    CommonResponseResult delete(String wid);
    /**
     * 授权
     * @param dto
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/19 15:37
     */
    CommonResponseResult grant(List<PersonalDataGrantDto> dto);

    CommonResponseResult<List<PluginInfo>> getPlugins();

    public void loadPersonalDataToRedis();
}
