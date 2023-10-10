package com.wisedu.minos.casp.portal.service;

import com.wisedu.minos.casp.portal.model.CommonResponseResult;
import com.wisedu.minos.casp.portal.model.personal.*;
import com.wisedu.minos.casp.portal.spi.model.MailPluginInfo;
import com.wisedu.minos.casp.portal.spi.model.PluginInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @className: PersonalDataApi
 * @description: 个人数据api
 * @author jszhang
 * @date 2021/7/14 13:39
 * @version 1.0
 */
public interface PersonalDataApi {

    /**
     * 获取个人数据的配置信息
     * @param dto
     * @return java.util.List<com.wisedu.minos.casp.portal.model.personal.PersonalDataVo>
     * @author jszhang
     * @date: 2021/7/14 13:47
     */
    @RequestMapping("/personalData/queryConfigList")
    @ResponseBody
    CommonResponseResult<List<PersonalDataVo>> getPersonalDataConfig(@RequestBody PersonalDataQueryConfigDto dto);

    /**
     * 添加个人数据配置信息
     * @param dto
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/14 14:17
     */
    @PostMapping(value = "/personalData/addPersonDataConfig")
    @ResponseBody
    CommonResponseResult addPersonDataConfig(@RequestBody PersonalDataDto dto);

    /**
     * 编辑个人数据配置信息
     * @param dto
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/14 14:17
     */
    @PostMapping(value = "/personalData/editPersonDataConfig")
    @ResponseBody
    CommonResponseResult editPersonDataConfig(@RequestBody PersonalDataDto dto);

    /**
     * 更新启停状态
     * @param dto
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/14 14:17
     */
    @PostMapping(value = "/personalData/updateEnableState")
    @ResponseBody
    CommonResponseResult updateEnableState(@RequestBody UpdateStateDto dto);
    /**
     * 更新排序
     * @param dto
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/14 14:44
     */
    @PostMapping(value = "/personalData/updateSorting")
    @ResponseBody
    CommonResponseResult updateSorting(@RequestBody List<UpdateSortingDto> dto);

    /**
     * 删除
     * @param wid
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/19 10:08
     */
    @PostMapping(value = "/personalData/delete/{wid}")
    @ResponseBody
    CommonResponseResult delete(@PathVariable String wid);
   /**
    * 获取已经安装的邮箱插件
    * @param
    * @return com.wisedu.minos.casp.portal.model.CommonResponseResult<java.util.List<com.wisedu.minos.casp.portal.spi.model.MailPluginInfo>>
    * @author jszhang
    * @date: 2021/7/15 16:53
    */
    @GetMapping(value = "/personalData/getMailPlugins")
    @ResponseBody
    CommonResponseResult<List<MailPluginInfo>> getMailPlugins();

    @GetMapping(value = "/personalData/getPlugins")
    @ResponseBody
    CommonResponseResult<List<PluginInfo>> getPlugins();

   /**
    * 授权
    * @param dto
    * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
    * @author jszhang
    * @date: 2021/7/19 11:07
    */
    @PostMapping(value = "/personalData/grant")
    @ResponseBody
    CommonResponseResult grant(@RequestBody PersonalDataGrantDto dto);

    /**
     * 个人数据授权查询
     * @param wid
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/19 11:07
     */
    @GetMapping(value = "/personalData/grantQuery")
    @ResponseBody
    CommonResponseResult<List<PersonalDataGrantVo>> grantQuery(String wid);
}
