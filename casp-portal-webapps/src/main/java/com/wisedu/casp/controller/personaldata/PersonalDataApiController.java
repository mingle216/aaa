package com.wisedu.casp.controller.personaldata;

import com.wisedu.minos.annotation.ConsoleRightCheck;
import com.wisedu.minos.casp.portal.model.CommonResponseResult;
import com.wisedu.minos.casp.portal.model.personal.*;
import com.wisedu.minos.casp.portal.service.PersonalDataApi;
import com.wisedu.minos.casp.portal.service.PersonalDataApiAdapter;
import com.wisedu.minos.casp.portal.service.impl.personaldata.PersonalDataAuthService;
import com.wisedu.minos.casp.portal.spi.model.MailPluginInfo;
import com.wisedu.minos.casp.portal.spi.model.PluginInfo;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import com.wisedu.minos.gateway.client.util.ApiOperationTypeEnums;
import com.wisedu.minos.util.MenuEditTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 个人数据管控台接口
 *
 * @author jszhang
 * @date 2021/7/14 13:52
 * @version 1.0
 */
@Controller
public class PersonalDataApiController implements PersonalDataApi {

    @Autowired
    PersonalDataApiAdapter personalDataApiAdapter;

    @Autowired
    PersonalDataAuthService personalDataAuthService;

    @ManagerGatewayApi(name = "获取个人数据配置", realPath = "/personalData/queryConfigList")
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_LOOK, menu = MenuEditTypeEnum.MENU_ID_PERSONAL_DATA_MANAGE)
    @Override
    public CommonResponseResult<List<PersonalDataVo>> getPersonalDataConfig(PersonalDataQueryConfigDto dto) {
        return personalDataApiAdapter.getPersonalDataConfig(dto);
    }

    @ManagerGatewayApi(name = "添加个人数据配置信息", realPath = "/personalData/addPersonDataConfig",operationType = ApiOperationTypeEnums.API_OPERATION_ADD)
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_PERSONAL_DATA_MANAGE)
    @Override
    public CommonResponseResult addPersonDataConfig(PersonalDataDto dto) {
        return personalDataApiAdapter.addPersonDataConfig(dto);
    }

    @ManagerGatewayApi(name = "编辑个人数据配置信息", realPath = "/personalData/editPersonDataConfig",operationType = ApiOperationTypeEnums.API_OPERATION_MODIFY)
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_PERSONAL_DATA_MANAGE)
    @Override
    public CommonResponseResult editPersonDataConfig(PersonalDataDto dto) {
        return personalDataApiAdapter.editPersonDataConfig(dto);
    }

    @ManagerGatewayApi(name = "更新启停状态", realPath = "/personalData/updateEnableState",operationType = ApiOperationTypeEnums.API_OPERATION_MODIFY)
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_PERSONAL_DATA_MANAGE)
    @Override
    public CommonResponseResult updateEnableState(UpdateStateDto dto) {
        return personalDataApiAdapter.updateEnableState(dto);
    }

    @ManagerGatewayApi(name = "更新排序", realPath = "/personalData/updateSorting",operationType = ApiOperationTypeEnums.API_OPERATION_MODIFY)
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_PERSONAL_DATA_MANAGE)
    @Override
    public CommonResponseResult updateSorting( List<UpdateSortingDto> dto) {
        return personalDataApiAdapter.updateSorting(dto);
    }
    @ManagerGatewayApi(name = "删除个人数据", realPath = "/personalData/delete/{wid}",operationType = ApiOperationTypeEnums.API_OPERATION_DELETE)
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_PERSONAL_DATA_MANAGE)
    @Override
    public CommonResponseResult delete(String wid) {
        return personalDataApiAdapter.delete(wid);
    }

    @ManagerGatewayApi(name = "获取邮箱插件", realPath = "/personalData/getMailPlugins")
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_LOOK, menu = MenuEditTypeEnum.MENU_ID_PERSONAL_DATA_MANAGE)
    @Override
    public CommonResponseResult<List<MailPluginInfo>> getMailPlugins() {
        return personalDataApiAdapter.getMailPlugins();
    }
    @ManagerGatewayApi(name = "获取插件", realPath = "/personalData/getPlugins")
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_LOOK, menu = MenuEditTypeEnum.MENU_ID_PERSONAL_DATA_MANAGE)
    @Override
    public CommonResponseResult<List<PluginInfo>> getPlugins() {
        return personalDataApiAdapter.getPlugins();
    }

    @ManagerGatewayApi(name = "授权", realPath = "/personalData/grant",operationType = ApiOperationTypeEnums.API_OPERATION_MODIFY)
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_PERSONAL_DATA_MANAGE)
    @Override
    public CommonResponseResult grant(PersonalDataGrantDto dto) {
        personalDataAuthService.saveAuth(dto);
        return new CommonResponseResult();
    }

    @ManagerGatewayApi(name = "个人数据授权查询", realPath = "/personalData/grantQuery")
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_LOOK, menu = MenuEditTypeEnum.MENU_ID_PERSONAL_DATA_MANAGE)
    @Override
    public CommonResponseResult<List<PersonalDataGrantVo>> grantQuery(String wid) {
        return personalDataAuthService.grantQuery(wid);
    }
}
