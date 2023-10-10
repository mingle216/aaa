package com.wisedu.minos.casp.portal.spi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.*;
import com.wisedu.minos.casp.portal.dao.mapper.UserEmailMapper;
import com.wisedu.minos.casp.portal.model.TemplateContent;
import com.wisedu.minos.casp.portal.service.impl.*;
import com.wisedu.minos.casp.portal.spi.itf.ITemplate;
import com.wisedu.minos.casp.portal.utils.TemplatePageUtil;
import com.wisedu.minos.casp.portal.utils.TemplateUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.smartcardio.Card;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/***
 * @Author jcx
 * @Description 门户启动完毕后执行此类逻辑
 * @Date 21:13 2021/6/1
 **/
@Component
@Order(2)
public class ApplicationRunnerPlugin implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationRunnerPlugin.class);
    @Autowired
    DatabasechangelogService databaseService;
    @Autowired
    PageInfoService pageInfoService;
    @Autowired
    ShowProgrammeService showProgrammeService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    CardService cardService;
    @Autowired
    SysIconService sysIconService;
    @Autowired
    PageCardConfigService pageCardConfigService;
    @Autowired
    TemplateService templateService;
    @Autowired
    TemplateUtil templateUtil;
//    @Autowired
//    private UserEmailMapper userEmailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ApplicationArguments args){
        logger.info("执行liquibase脚本补偿检查.......");

        //3.2.1.beta2版本页面配置中英文多语言为en_US改为en
        String editLangEnUsToEnFlag = systemConfigService.getSystemConfigValue("editLangEnUsToEnFlag");
        if(StringUtil.isEmpty(editLangEnUsToEnFlag)){
            List<PageInfoEntity> pageInfos = pageInfoService.list(new QueryWrapper<PageInfoEntity>().lambda().eq(PageInfoEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));
            if(CollectionUtils.isNotEmpty(pageInfos)){
                pageInfos.forEach(pageInfo->{
                    if(StringUtil.isNotEmpty(pageInfo.getPageConfig())&&pageInfo.getPageConfig().contains("header.title.en_US")){
                        String enConfig = pageInfo.getPageConfig().replaceAll("header.title.en_US", "header.title.en");
                        pageInfo.setPageConfig(enConfig);
                    }
                    if(StringUtil.isNotEmpty(pageInfo.getPageConfig())&&pageInfo.getPageConfig().contains("header.subTitle.en_US")){
                        String enConfig = pageInfo.getPageConfig().replaceAll("header.subTitle.en_US", "header.subTitle.en");
                        pageInfo.setPageConfig(enConfig);
                    }
                });
                pageInfoService.updateBatchById(pageInfos);
                systemConfigService.save(newSystemConfigEntity("", "editLangEnUsToEnFlag", "editLangEnUsToEnFlag", "", "3.2.1.beta2版本页面配置中英文多语言为en_US改为en", 2, 0));
            }
        }
        String official_default_pro_flag = systemConfigService.getSystemConfigValue("official_default_pro_flag");
        if(StringUtil.isEmpty(official_default_pro_flag)){
            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_INTI_PAGE_INFO_IMG.getFileName())){
                updateAllPageLayout();
            }
            systemConfigService.save(newSystemConfigEntity("", "official_default_pro_flag", "official_default_pro_flag", "", "3.2.1.beta2修改official默认展示方案布局卡片地址", 2, 0));
        }

        String compensationCheck = systemConfigService.getSystemConfigValue("compensationCheck");
        if(StringUtil.isEmpty(compensationCheck)){
            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_20201219_DATA.getFileName())){
                List<CardEntity> list = cardService.list(new QueryWrapper<CardEntity>().in(DbFieldConstant.WID, Arrays.asList("3")));
                if(CollectionUtils.isNotEmpty(list)){
                    list.forEach(pageInfo ->pageInfo.setIsDeleted(Global.DeleteStatus.YES.getId()));
                    cardService.updateBatchById(list);
                }
            }
            List<DatabasechangelogEntity> list = databaseService.list(new QueryWrapper<DatabasechangelogEntity>()
                    .apply(MinosCommonUtil.getLikeEscapeSql(DbFieldConstant.FILE_NAME), MinosCommonUtil.getLikeEscapeValue(Global.LiuquibaseCompensate.UPDATE_20210119_DATA.getFileName())).or()
                    .apply(MinosCommonUtil.getLikeEscapeSql(DbFieldConstant.FILE_NAME), MinosCommonUtil.getLikeEscapeValue(Global.LiuquibaseCompensate.UPDATE_20210122_PAGE.getFileName())).or()
                    .apply(MinosCommonUtil.getLikeEscapeSql(DbFieldConstant.FILE_NAME), MinosCommonUtil.getLikeEscapeValue(Global.LiuquibaseCompensate.UPDATE_20210401_PAGE_INFO.getFileName()))
            );
            if(CollectionUtils.isEmpty(list)){
                updateInitiProgrammePageConfig(Global.ShowProgrammeInitWid.OFFICIAL_PC_INTI_PROGRAMME_WID.getId(),String.valueOf(Global.PlatformType.PC.getType()));
                updateInitiProgrammePageConfig(Global.ShowProgrammeInitWid.OFFICIAL_PC_DEFAULT_PROGRAMME_WID.getId(),String.valueOf(Global.PlatformType.PC.getType()));
            }
            if(null==getDataBaseById(Global.LiuquibaseCompensate.UPDATE_MOBILE_PAGE_INFO.getFileName())){
                updateInitiProgrammePageConfig(Global.ShowProgrammeInitWid.OFFICIAL_MOBILE_DEFAULT_PROGRAMME_WID.getId(),String.valueOf(Global.PlatformType.MOBILE.getType()));
            }
            List<DatabasechangelogEntity> listSed = databaseService.list(new QueryWrapper<DatabasechangelogEntity>()
                    .apply(MinosCommonUtil.getLikeEscapeSql(DbFieldConstant.FILE_NAME), MinosCommonUtil.getLikeEscapeValue(Global.LiuquibaseCompensate.UPDATE_PAGE_INFO0602_MOBILE.getFileName())).or()
                    .apply(MinosCommonUtil.getLikeEscapeSql(DbFieldConstant.FILE_NAME), MinosCommonUtil.getLikeEscapeValue(Global.LiuquibaseCompensate.UPDATE_MOBILE_PAGE_INFO_LINK.getFileName()))
            );
            if(CollectionUtils.isEmpty(listSed)){
                updateMobileProgrammePageLayout(Global.ShowProgrammeInitWid.OFFICIAL_MOBILE_DEFAULT_PROGRAMME_WID.getId());
            }

            List<DatabasechangelogEntity> listSun = databaseService.list(new QueryWrapper<DatabasechangelogEntity>()
                    .apply(MinosCommonUtil.getLikeEscapeSql(DbFieldConstant.FILE_NAME), MinosCommonUtil.getLikeEscapeValue(Global.LiuquibaseCompensate.UPDATE_20210121_DATA.getFileName())).or()
                    .apply(MinosCommonUtil.getLikeEscapeSql(DbFieldConstant.FILE_NAME), MinosCommonUtil.getLikeEscapeValue(Global.LiuquibaseCompensate.UPDATE_20210207_DATA.getFileName())).or()
                    .apply(MinosCommonUtil.getLikeEscapeSql(DbFieldConstant.FILE_NAME), MinosCommonUtil.getLikeEscapeValue(Global.LiuquibaseCompensate.UPDATE_20210222_TEMPLATECONFIG.getFileName())).or()
                    .apply(MinosCommonUtil.getLikeEscapeSql(DbFieldConstant.FILE_NAME), MinosCommonUtil.getLikeEscapeValue(Global.LiuquibaseCompensate.UPDATE_20210302_TEMPLATECONFIG.getFileName()))
            );
            if(CollectionUtils.isEmpty(listSun)){
                updatePcOfficialProgrammeConfig(Global.ShowProgrammeInitWid.OFFICIAL_PC_INTI_PROGRAMME_WID.getId(),Global.ShowProgrammeInitWid.OFFICIAL_PC_DEFAULT_PROGRAMME_WID.getId(),String.valueOf(Global.PlatformType.PC.getType()));
            }
            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_20210121_SYSTEM_CONFIG.getFileName())){
                List<SystemConfigEntity> systemConfigs = systemConfigService.list(new QueryWrapper<SystemConfigEntity>().in(DbFieldConstant.WID, "47", "48", "49"));
                systemConfigs.forEach(systemConfig ->{
                    systemConfig.setIsDeleted(Global.DeleteStatus.NO.getId());
                    systemConfig.setConfigValue("");
                } );
                systemConfigService.updateBatchById(systemConfigs);
            }
//            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_20210219_CARD.getFileName())){
//                addCard();
//            }
            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_20210302_SYS_ICON.getFileName())){
                List<SysIconEntity> sysIcons = sysIconService.list(new QueryWrapper<SysIconEntity>().in(DbFieldConstant.WID, "1", "26"));
                if(CollectionUtils.isNotEmpty(sysIcons)){
                    sysIcons.forEach(sysIcon -> {
                        if("1".equals(sysIcon.getWid())){
                            sysIcon.setOrderNumber(26);
                        }
                        if("26".equals(sysIcon.getWid())){
                            sysIcon.setOrderNumber(1);
                        }
                    });
                    sysIconService.updateBatchById(sysIcons);
                }
            }
            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_20210303_PAGE.getFileName())){
                List<PageCardConfigEntity> pageCardConfigs = pageCardConfigService.list(new QueryWrapper<PageCardConfigEntity>());
                if(CollectionUtils.isNotEmpty(pageCardConfigs)){
                    pageCardConfigs.forEach(pageCardConfig -> pageCardConfig.setCardConfig(""));
                    pageCardConfigService.updateBatchById(pageCardConfigs);
                }
            }
            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_20210302_SYSTEM_CONFIG.getFileName())){
                addSysConfig();
            }
            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_T_AMP_VIEW_TEMPLATE.getFileName())){
                TemplateEntity officialTemplate= templateService.getOne(new QueryWrapper<TemplateEntity>().eq(DbFieldConstant.WID, "1"));
                officialTemplate.setPlatformType("0;1");
                templateService.updateById(officialTemplate);
            }
//            if(null==getDataBaseById(Global.LiuquibaseCompensate.UPDATE_T_AMP_VIEW_CARD.getFileName())){
//                List<CardEntity> cards = cardService.list(new QueryWrapper<CardEntity>().in(DbFieldConstant.WID, "21", "4", "13", "12", "18", "17", "5", "10", "30", "14", "25", "20", "2", "32", "23", "24", "27", "29", "26", "36",
//                        "40", "41", "1", "6", "7", "8", "9", "11", "31", "37", "3"));
//                if(CollectionUtils.isNotEmpty(cards)){
//                    updateCards(cards);
//                }
//            }
            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_20210329_CARD.getFileName())){
//                addNewCard();
            }
            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_20210410_VIEW_CARD.getFileName())){
                addNewCardFir();
            }
//            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_20210410_UPDATE_TABLE.getFileName())){
//                userEmailMapper.alterTable();
//                userEmailMapper.commentTable();
//            }
            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_20210410_UPDATE_TABLE.getFileName())){
                TemplateEntity officialTemplate = templateService.getOne(new QueryWrapper<TemplateEntity>().eq(DbFieldConstant.WID, "1"));
                officialTemplate.setTemplateImgMobileUrl("/sys_template_official/base/image/mobile_display.png");
                templateService.updateById(officialTemplate);
            }
            if(null==getDataBase(Global.LiuquibaseCompensate.UPDATE_20210510_TEMPLATECONFIG.getFileName())){
                updatePcOfficialProgrammeConfig(Global.ShowProgrammeInitWid.OFFICIAL_MOBILE_INTI_PROGRAMME_WID.getId(),Global.ShowProgrammeInitWid.OFFICIAL_MOBILE_DEFAULT_PROGRAMME_WID.getId(),String.valueOf(Global.PlatformType.MOBILE.getType()));
            }
            systemConfigService.save(newSystemConfigEntity("", "compensationCheck", "compensationCheck", "", "3.2.1.beta1去除之前版本所有sql脚本标志位", 2, 0));
        }
        updateTencentSystemConfig();
        logger.info("casp-portal项目启动完毕........");
    }

    private DatabasechangelogEntity getDataBase(String liquiBaseFileName){
        return databaseService.getOne(new QueryWrapper<DatabasechangelogEntity>().apply(MinosCommonUtil.getLikeEscapeSql(DbFieldConstant.FILE_NAME), MinosCommonUtil.getLikeEscapeValue(liquiBaseFileName)));
    }
    private DatabasechangelogEntity getDataBaseById(String liquiBaseFileName){
        return databaseService.getOne(new QueryWrapper<DatabasechangelogEntity>().eq(DbFieldConstant.ID,liquiBaseFileName));
    }

    private void addNewCard(){
        CardEntity cardFir = newCard("40","SYS_CARD_MYOFFICE","我的办件","/SYS_CARD_MYOFFICE/pc/img/display.png","0;1","我的办件","1.0.0","/SYS_CARD_STATISTICS/mobile/img/display.png");
        CardEntity cardSed = newCard("41","SYS_CARD_FEEDBACK","意见反馈","/SYS_CARD_FEEDBACK/pc/img/display.png","0","意见反馈","1.0.0","/SYS_CARD_STATISTICS/mobile/img/display.png");
        cardService.saveBatch(Arrays.asList(cardFir,cardSed));
    }

    /***
     * @Author jcx
     * @Description 初始化新卡片
     * @Date 13:20 2021/6/4
     * @return void
     **/
    private void addNewCardFir(){
//        CardEntity cardFir = newCard("37","SYS_CARD_PERSONALDATA","个人数据","/SYS_CARD_PERSONALDATA/pc/img/display.png","0","个人数据","1.0.0","/SYS_CARD_STATISTICS/mobile/img/display.png");
//        cardService.saveBatch(Arrays.asList(cardFir));
        SystemConfigEntity systemConfigFir = newSystemConfigEntity("61", "mailSendChannelId", "", "", "消息通道id", 1, 1);
        SystemConfigEntity systemConfigSed = newSystemConfigEntity("62", "mailSendInstanceWid", "", "", "消息插件id", 1, 1);
        SystemConfigEntity systemConfigWed = newSystemConfigEntity("58", "coreMail_host", "", "121.248.60.50", "coremail域名", 1, 1);
        SystemConfigEntity systemConfigFou = newSystemConfigEntity("59", "coreMail_port", "", "6195", "coremail端口", 1, 1);
        SystemConfigEntity systemConfigFri =newSystemConfigEntity("60","coreMail_loginUrl_prefix","","http://121.248.60.50/coremail/main.jsp?","coremail登录地址前缀",1,1);
        SystemConfigEntity systemConfigQed =newSystemConfigEntity("56","academic_report","","http://yyzx.seu.edu.cn/southeast-university/academic-report/list?_=1617698262182","学术报告地址",1, 0);
        SystemConfigEntity systemConfigRed =newSystemConfigEntity("57","library_lecture","","http://yyzx.seu.edu.cn/southeast-university/library-lecture/list/1?_=1617698310925","学生活动地址",1, 0);
        SystemConfigEntity systemConfigYed = newSystemConfigEntity("55", "tencent_newCount_clientSecret", "", "", "未读邮件密钥", 2, 1);
        systemConfigService.saveBatch(Arrays.asList(systemConfigFir,systemConfigSed,systemConfigWed,systemConfigFou,systemConfigFri,systemConfigQed,systemConfigRed,systemConfigYed));
    }

    private void updateTencentSystemConfig() {
        List<SystemConfigEntity> list = systemConfigService.list(new QueryWrapper<SystemConfigEntity>().in(DbFieldConstant.WID, "50", "51", "52", "53", "54"));
        if(CollectionUtils.isNotEmpty(list)){
            list.forEach(systemConfig -> {
                switch (systemConfig.getWid()){
                    case "50":
                        systemConfig.setConfigKey("tencent_accessTokenUrl");
                        break;
                    case "51":
                        systemConfig.setConfigKey("tencent_loginUrl");
                        break;
                    case "52":
                        systemConfig.setConfigKey("tencent_newCountUrl");
                        break;
                    case "53":
                        systemConfig.setConfigKey("tencent_clientId");
                        break;
                    default:
                        systemConfig.setConfigKey("tencent_login_clientSecret");
                        systemConfig.setConfigDesc("单点登录密钥");
                        break;
                }
            });
            systemConfigService.updateBatchById(list);
        }
    }

    /***
     * @Author jcx
     * @Description 修改卡片数据支持移动端
     * @Date 11:34 2021/6/4
     **/
    private void updateCards(List<CardEntity> cards){
        cards.forEach(card -> {
            switch (card.getWid()){
                case "21":
                    card=updateSetCard(card,"0;1","/SYS_CARD_TODOTASK/pc/img/display.png","/SYS_CARD_TODOTASK/mobile/img/display.png");
                    break;
                case "4":
                    card=updateSetCard(card,"0;1","/SYS_CARD_DONETASK/pc/img/display.png","/SYS_CARD_DONETASK/mobile/img/display.png");
                    break;
                case "13":
                    card=updateSetCard(card,"0;1","/SYS_CARD_MYTASK/pc/img/display.png","/SYS_CARD_MYTASK/mobile/img/display.png");
                    break;
                case "12":
                    card=updateSetCard(card,"0;1","/SYS_CARD_MYFAVORITESSERVICEITEM/pc/img/display.png","/SYS_CARD_MYFAVORITESSERVICEITEM/mobile/img/display.png");
                    break;
                case "18":
                    card=updateSetCard(card,"0;1","/SYS_CARD_RECOMMENDSERVICEITEMS/pc/img/display.png","/SYS_CARD_RECOMMENDSERVICEITEMS/mobile/img/display.png");
                    break;
                case "17":
                    card=updateSetCard(card,"0;1","/SYS_CARD_RECOMMENDAPP/pc/img/display.png","/SYS_CARD_RECOMMENDAPP/mobile/img/display.png");
                    break;
                case "5":
                    card=updateSetCard(card,"0;1","/SYS_CARD_FAVORITEAPP/pc/img/display.png","/SYS_CARD_FAVORITEAPP/mobile/img/display.png");
                    break;
                case "10":
                    card=updateSetCard(card,"0;1","/SYS_CARD_LINK/pc/img/display-1.png;/SYS_CARD_LINK/pc/img/display-2.png","/SYS_CARD_LINK/mobile/img/display.png");
                    break;
                case "30":
                    card=updateSetCard(card,"0;1","/SYS_CARD_STATISTICS/pc/img/display.png","/SYS_CARD_STATISTICS/mobile/img/display.png");
                    break;
                case "14":
                    card=updateSetCard(card,"0;1","/SYS_CARD_NEWSANNOUNCEMENT/pc/img/display-1.png;/SYS_CARD_NEWSANNOUNCEMENT/pc/img/display-4.png;/SYS_CARD_NEWSANNOUNCEMENT/pc/img/display-2.png;/SYS_CARD_NEWSANNOUNCEMENT/pc/img/display-3.png","/SYS_CARD_NEWSANNOUNCEMENT/mobile/img/display.png");
                    break;
                case "25":
                    card=updateSetCard(card,"0;1","/SYS_CARD_SERVICEITEMCOUNT/pc/img/display-1.png;/SYS_CARD_SERVICEITEMCOUNT/pc/img/display-2.png","/SYS_CARD_SERVICEITEMCOUNT/mobile/img/display.png");
                    break;
                case "20":
                    card=updateSetCard(card,"0;1","/SYS_CARD_SERVICEBUS/pc/img/display-1.png;/SYS_CARD_SERVICEBUS/pc/img/display-2.png","/SYS_CARD_SERVICEBUS/mobile/img/display.png");
                    break;
                case "2":
                    card=updateSetCard(card,"0;1","/SYS_CARD_CAROUSEL/pc/img/display.png","/SYS_CARD_CAROUSEL/mobile/img/display.png");
                    break;
                case "32":
                    card=updateSetCard(card,"0;1","/SYS_CARD_RECUSEAPP/pc/img/display.png","/SYS_CARD_RECUSEAPP/mobile/img/display.png");
                    break;
                case "23":
                    card=updateSetCard(card,"0;1","/SYS_CARD_ALLSERVICE/pc/img/display.png","/SYS_CARD_ALLSERVICE/mobile/img/display.png");
                    break;
                case "24":
                    card=updateSetCard(card,"0;1","/SYS_CARD_SERVICEITEMCATEGORY/pc/img/display-1.png;/SYS_CARD_SERVICEITEMCATEGORY/pc/img/display-2.png","/SYS_CARD_SERVICEITEMCATEGORY/mobile/img/display.png");
                    break;
                case "27":
                    card=updateSetCard(card,"0;1","/SYS_CARD_SEARCHRESULTS/pc/img/display.png","/SYS_CARD_SEARCHRESULTS/mobile/img/display.png");
                    break;
                case "29":
                    card=updateSetCard(card,"0;1","/SYS_CARD_SERVICEITEMCATEGORYDETAIL/pc/img/display.png","/SYS_CARD_SERVICEITEMCATEGORYDETAIL/mobile/img/display.png");
                    break;
                case "26":
                    card=updateSetCard(card,"0;1","/SYS_CARD_DETAILSOFSERVICEITEMS/pc/img/display.png","/SYS_CARD_DETAILSOFSERVICEITEMS/mobile/img/display.png");
                    break;
                case "36":
                    card=updateSetCard(card,card.getPlatformType(),"/SYS_CARD_UNDONE/pc/img/display.png","");
                    break;
                case "40":
                    card=updateSetCard(card,"0","/SYS_CARD_MYOFFICE/pc/img/display.png","");
                    break;
                case "41":
                    card=updateSetCard(card,card.getPlatformType(),"/SYS_CARD_FEEDBACK/pc/img/display.png","");
                    break;
                case "1":
                    card=updateSetCard(card,"0","/SYS_CARD_ALLSERVICEITEM/pc/img/display.png","");
                    break;
                case "6":
                    card=updateSetCard(card,card.getPlatformType(),"/SYS_CARD_HOTAPP/pc/img/display.png","");
                    break;
                case "7":
                    card=updateSetCard(card,"0","/SYS_CARD_HOTSERVICEITEMS/pc/img/display.png","");
                    break;
                case "8":
                    card=updateSetCard(card,"0","/SYS_CARD_LASTESTAPP/pc/img/display.png","");
                    break;
                case "9":
                    card=updateSetCard(card,"0","/SYS_CARD_LATESTSERVICEITEMS/pc/img/display.png","");
                    break;
                case "11":
                    card=updateSetCard(card,"0","/SYS_CARD_MESSAGECENTER/pc/img/display.png","");
                    break;
                case "31":
                    card=updateSetCard(card,"0","/SYS_CARD_RICHTEXT/pc/img/display.png","");
                    break;
                case "37":
                    card=updateSetCard(card,card.getPlatformType(),"/SYS_CARD_PERSONALDATA/pc/img/display.png","");
                    break;
                case "3":
                    card=updateSetCard(card,"0",card.getImageUrl(),"");
                    break;
                default:
                    card=card;
                    break;
            }
        });
        cardService.updateBatchById(cards);
    }

    private CardEntity updateSetCard(CardEntity card,String platformType,String imageUrl,String imageUrlMobile){
        card.setPlatformType(platformType);
        card.setImageUrl(imageUrl);
        card.setImageUrlMobile(imageUrlMobile);
        return card;
    }
    /***
     * @Author jcx
     * @Description 初始化腾讯邮箱配置
     * @Date 9:38 2021/6/4
     * @return void
     **/
    private void addSysConfig(){
        SystemConfigEntity systemConfigFir= newSystemConfigEntity("50", "tencent_new_accessTokenUrl", "https://api.exmail.qq.com/cgi-bin/gettoken", "https://api.exmail.qq.com/cgi-bin/gettoken", "获取access_token接口地址", 2, 1);
        SystemConfigEntity systemConfigSed= newSystemConfigEntity("51", "tencent_new_loginUrl", "https://api.exmail.qq.com/cgi-bin/service/get_login_url", "https://api.exmail.qq.com/cgi-bin/service/get_login_url", "获取登录企业邮箱的地址", 2, 1);
        SystemConfigEntity systemConfigWed= newSystemConfigEntity("52", "tencent_new_newCountUrl", "https://api.exmail.qq.com/cgi-bin/mail/newcount", "https://api.exmail.qq.com/cgi-bin/mail/newcount", "获取未读邮件数量", 2, 1);
        SystemConfigEntity systemConfigFou= newSystemConfigEntity("53", "tencent.new.clientId", "wma1cf2f625b012e46", "wma1cf2f625b012e46", "企业id", 2, 1);
        SystemConfigEntity systemConfigFri= newSystemConfigEntity("54", "tencent.new.clientSecret", "VT8e16nasnKDn0SRoDHN16m_wQcDpDcIFFOgm-OXkiw", "VT8e16nasnKDn0SRoDHN16m_wQcDpDcIFFOgm-OXkiw", "应用的凭证密钥", 2, 1);
        systemConfigService.saveBatch(Arrays.asList(systemConfigFir,systemConfigSed,systemConfigWed,systemConfigFou,systemConfigFri));
    }
    private SystemConfigEntity newSystemConfigEntity(String wid,String configKey,String configValue,
                                                     String defaultValue,String configDesc,Integer configModel, Integer pageShow){
        SystemConfigEntity systemConfig = new SystemConfigEntity();
        systemConfig.setWid(wid);
        systemConfig.setConfigKey(configKey);
        systemConfig.setConfigValue(configValue);
        systemConfig.setDefaultValue(defaultValue);
        systemConfig.setConfigDesc(configDesc);
        systemConfig.setConfigModel(configModel);
        systemConfig.setIsDeleted(Global.DeleteStatus.NO.getId());
        systemConfig.setPageShow(pageShow);
        return systemConfig;
    }

    /***
     * @Author jcx
     * @Description 新增卡片 办理统计 富文本 最近使用服务
     * @Date 19:47 2021/6/3
     * @return void
     **/
//    private void addCard(){
//        CardEntity cardFir = newCard("30","SYS_CARD_STATISTICS","办理统计","/SYS_CARD_STATISTICS/pc/img/display.png","0;1","办理统计","1.0.0","/SYS_CARD_STATISTICS/mobile/img/display.png");
//        CardEntity cardSed = newCard("31","SYS_CARD_RICHTEXT","富文本","/SYS_CARD_RICHTEXT/pc/img/display.png","0","富文本","1.0.0","");
//        CardEntity cardSun = newCard("32","SYS_CARD_RECUSEAPP","最近使用服务","/SYS_CARD_RECUSEAPP/pc/img/display.png","0;1","最近使用服务","1.0.0","/SYS_CARD_RECUSEAPP/mobile/img/display.png");
//        cardService.saveBatch(Arrays.asList(cardFir, cardSed, cardSun));
//    }

    private CardEntity newCard(String wid,String cardId,String cardName,String imageUrl,String platformType,String cardDesc,String versionNumber,String mageUrlMobile){
        CardEntity cardEntity = new CardEntity();
        cardEntity.setWid(wid);
        cardEntity.setCardId(cardId);
        cardEntity.setCardName(cardName);
        cardEntity.setImageUrl(imageUrl);
        cardEntity.setPlatformType(platformType);
        cardEntity.setConfigurableFlag(0);
        cardEntity.setConfigurableRuntimeFlag(1);
        cardEntity.setCardDesc(cardDesc);
        cardEntity.setCardSystemType(0);
        cardEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
        cardEntity.setCardStatus(0);
        cardEntity.setVersionNumber(versionNumber);
        cardEntity.setImageUrlMobile(mageUrlMobile);
        return cardEntity;
    }


    /***
     * @Author jcx
     * @Description 更新pc端、移动端official默认和初始化展示方案配置
     * @Date 19:17 2021/6/3
     **/
    private void updatePcOfficialProgrammeConfig(String intiShowProgrammeWid,String defaultShowShowProgrammeWid,String platformType){
        List<ShowProgrammeEntity> showProgrammes = showProgrammeService.list(new QueryWrapper<ShowProgrammeEntity>()
                .in(DbFieldConstant.WID,intiShowProgrammeWid,defaultShowShowProgrammeWid));
        String templateId=Global.SYS_TEMPLATE_OFFICIAL;
        ITemplate template = templateUtil.getTemplate(templateId);
        showProgrammes.forEach(showProgramme ->{
            showProgramme.setTemplateConfig(template.getTemplateConfig(platformType));
        } );
        showProgrammeService.updateBatchById(showProgrammes);
    }

    /***
     * @Author jcx
     * @Description 更新展示方案所有页面的的page_config
     * @Date 14:38 2021/6/3
     * @return void
     **/
    private void updateInitiProgrammePageConfig(String programmeWid,String platformType){
        //初始化展示方案的所有页面
        List<PageInfoEntity> intiProPages = pageInfoService.list(new QueryWrapper<PageInfoEntity>().eq(DbFieldConstant.PROGRAMME_ID, programmeWid));
        String templateId=Global.SYS_TEMPLATE_OFFICIAL;
        List<TemplateContent> pcOfficialTemplatePages = ApplicationContextUtil.get(TemplatePageUtil.class).getTemplateInfo(templateId,Integer.valueOf(platformType));
        intiProPages.forEach(intiProPage->{
            pcOfficialTemplatePages.forEach(pcOfficialTemplatePage->{
                if(intiProPage.getTemplateCode().equals(pcOfficialTemplatePage.getCode())){
                    intiProPage.setPageConfig(pcOfficialTemplatePage.getDefaultPageConfig());
                }
            });
        });
        pageInfoService.updateBatchById(intiProPages);
    }



    /***
     * @Author jcx
     * @Description 更新移动端展示方案页面CardLayout
     * @Date 18:47 2021/6/3
     * @return void
     **/
    private void updateMobileProgrammePageLayout(String programmeWid){
        //初始化展示方案的所有页面
        List<PageInfoEntity> intiProPages = pageInfoService.list(new QueryWrapper<PageInfoEntity>().eq(DbFieldConstant.PROGRAMME_ID, programmeWid));
        String homeLayout="[{\"columns\":[{\"width\":24,\"cards\":{\"cards\":[{\"wid\":\"21\",\"cardId\":\"SYS_CARD_TODOTASK\",\"cardName\":\"待办任务\",\"cardDesc\":\"待办任务\",\"imageUrl\":\"/SYS_CARD_TODOTASK/pc/img/display.png\",\"imageUrlMobile\":\"/SYS_CARD_TODOTASK/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"5050010743497069\",\"showTitle\":true,\"layoutCardTitle\":{\"cardTitle\":\"待办任务\"},\"enabled\":true,\"platformtype\":\"1\"},{\"wid\":\"4\",\"cardId\":\"SYS_CARD_DONETASK\",\"cardName\":\"已办任务\",\"cardDesc\":\"已办任务\",\"imageUrl\":\"/SYS_CARD_DONETASK/pc/img/display.png\",\"imageUrlMobile\":\"/SYS_CARD_DONETASK/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"4191570720358846\",\"showTitle\":true,\"layoutCardTitle\":{\"cardTitle\":\"已办任务\"},\"enabled\":true,\"platformtype\":\"1\"}],\"platformtype\":\"1\"}}]},{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"14\",\"cardId\":\"SYS_CARD_NEWSANNOUNCEMENT\",\"cardName\":\"新闻通知公告\",\"cardDesc\":\"新闻通知公告\",\"imageUrl\":\"/SYS_CARD_NEWSANNOUNCEMENT/pc/img/display-1.png;/SYS_CARD_NEWSANNOUNCEMENT/pc/img/display.png\",\"imageUrlMobile\":\"/SYS_CARD_NEWSANNOUNCEMENT/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"47490745393041855\",\"showTitle\":true,\"layoutCardTitle\":{\"cardTitle\":\"新闻通知公告\"},\"enabled\":true,\"platformtype\":\"1\"}}]},{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"20\",\"cardId\":\"SYS_CARD_SERVICEBUS\",\"cardName\":\"业务直通车\",\"cardDesc\":\"业务直通车\",\"imageUrl\":\"/SYS_CARD_SERVICEBUS/pc/img/display-1.png;/SYS_CARD_SERVICEBUS/pc/img/display-2.png\",\"imageUrlMobile\":\"/SYS_CARD_SERVICEBUS/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"2535989843290072\",\"showTitle\":true,\"layoutCardTitle\":{\"cardTitle\":\"业务直通车\"},\"enabled\":true,\"platformtype\":\"1\"}}]},{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"10\",\"cardId\":\"SYS_CARD_LINK\",\"cardName\":\"链接导航\",\"cardDesc\":\"链接导航\",\"imageUrl\":\"/SYS_CARD_LINK/pc/img/display-1.png;/SYS_CARD_LINK/pc/img/display-2.png\",\"imageUrlMobile\":\"/SYS_CARD_LINK/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"refPage\":true,\"cardWid\":\"4283591102373696\",\"showTitle\":true,\"layoutCardTitle\":{\"cardTitle\":\"链接导航\"},\"enabled\":true}}]}]";
        String hallLayout="[{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"30\",\"cardId\":\"SYS_CARD_STATISTICS\",\"cardName\":\"办理统计\",\"cardDesc\":\"办理统计\",\"imageUrl\":\"/SYS_CARD_STATISTICS/pc/img/display.png\",\"imageUrlMobile\":\"/SYS_CARD_STATISTICS/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:42:13\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"9537952268055865\",\"showTitle\":true,\"layoutCardTitle\":{\"cardTitle\":\"办理统计\"},\"enabled\":true}}]},{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"24\",\"cardId\":\"SYS_CARD_SERVICEITEMCATEGORY\",\"cardName\":\"事项分类\",\"cardDesc\":\"事项分类\",\"imageUrl\":\"/SYS_CARD_SERVICEITEMCATEGORY/pc/img/display-1.png;/SYS_CARD_SERVICEITEMCATEGORY/pc/img/display-2.png\",\"imageUrlMobile\":\"/SYS_CARD_SERVICEITEMCATEGORY/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"27001996781743576\",\"showTitle\":true,\"layoutCardTitle\":{\"cardTitle\":\"事项分类\"},\"enabled\":true}}]},{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"17\",\"cardId\":\"SYS_CARD_RECOMMENDAPP\",\"cardName\":\"推荐服务\",\"cardDesc\":\"推荐服务\",\"imageUrl\":\"/SYS_CARD_RECOMMENDAPP/pc/img/display.png\",\"imageUrlMobile\":\"/SYS_CARD_RECOMMENDAPP/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"25555659614712956\",\"showTitle\":true,\"layoutCardTitle\":{\"cardTitle\":\"推荐服务\"},\"enabled\":true}}]},{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"25\",\"cardId\":\"SYS_CARD_SERVICEITEMCOUNT\",\"cardName\":\"事项统计\",\"cardDesc\":\"事项统计\",\"imageUrl\":\"/SYS_CARD_SERVICEITEMCOUNT/pc/img/display-1.png;/SYS_CARD_SERVICEITEMCOUNT/pc/img/display-2.png\",\"imageUrlMobile\":\"/SYS_CARD_SERVICEITEMCOUNT/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"14316381851490267\",\"showTitle\":true,\"layoutCardTitle\":{\"cardTitle\":\"事项统计\"},\"enabled\":true}}]}]";
        String itemCategoryDetailLayout="[{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"29\",\"cardId\":\"SYS_CARD_SERVICEITEMCATEGORYDETAIL\",\"cardName\":\"事项分类详情\",\"cardDesc\":\"事项分类详情\",\"imageUrl\":\"/SYS_CARD_SERVICEITEMCATEGORYDETAIL/pc/img/display.png\",\"imageUrlMobile\":\"/SYS_CARD_SERVICEITEMCATEGORYDETAIL/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"7900736255870593\",\"showTitle\":false,\"layoutCardTitle\":{\"cardTitle\":\"事项分类详情\"},\"enabled\":true}}]}]";
        String itemDetailLayout="[{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"26\",\"cardId\":\"SYS_CARD_DETAILSOFSERVICEITEMS\",\"cardName\":\"事项详情\",\"cardDesc\":\"事项详情\",\"imageUrl\":\"/SYS_CARD_DETAILSOFSERVICEITEMS/pc/img/display.png\",\"imageUrlMobile\":\"/SYS_CARD_DETAILSOFSERVICEITEMS/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"17946041451275452\",\"showTitle\":false,\"layoutCardTitle\":{\"cardTitle\":\"事项详情\"},\"enabled\":true}}]}]";
        String appsLayout="[{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"23\",\"cardId\":\"SYS_CARD_ALLSERVICE\",\"cardName\":\"全部服务\",\"cardDesc\":\"全部服务\",\"imageUrl\":\"/SYS_CARD_ALLSERVICE/pc/img/display.png\",\"imageUrlMobile\":\"/SYS_CARD_ALLSERVICE/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"8885039549504614\",\"showTitle\":false,\"layoutCardTitle\":{\"cardTitle\":\"全部服务\"},\"enabled\":true}}]}]";
        String personalLayout="[{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"5\",\"cardId\":\"SYS_CARD_FAVORITEAPP\",\"cardName\":\"我收藏的服务\",\"cardDesc\":\"我收藏的服务\",\"imageUrl\":\"/SYS_CARD_FAVORITEAPP/pc/img/display.png\",\"imageUrlMobile\":\"/SYS_CARD_FAVORITEAPP/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"7501200553687359\",\"showTitle\":true,\"layoutCardTitle\":{\"cardTitle\":\"我收藏的服务\"},\"enabled\":true}}]},{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"12\",\"cardId\":\"SYS_CARD_MYFAVORITESSERVICEITEM\",\"cardName\":\"我收藏的事项\",\"cardDesc\":\"我收藏的事项\",\"imageUrl\":\"/SYS_CARD_MYFAVORITESSERVICEITEM/pc/img/display.png\",\"imageUrlMobile\":\"/SYS_CARD_MYFAVORITESSERVICEITEM/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"8518035338046501\",\"showTitle\":true,\"layoutCardTitle\":{\"cardTitle\":\"我收藏的事项\"},\"enabled\":true}}]}]";
        String resultLayout="[{\"columns\":[{\"width\":24,\"card\":{\"wid\":\"27\",\"cardId\":\"SYS_CARD_SEARCHRESULTS\",\"cardName\":\"搜索结果\",\"cardDesc\":\"搜索结果\",\"imageUrl\":\"/SYS_CARD_SEARCHRESULTS/pc/img/display.png\",\"imageUrlMobile\":\"/SYS_CARD_SEARCHRESULTS/mobile/img/display.png\",\"updateTime\":\"2021-02-22 10:41:50\",\"platformType\":\"0;1\",\"configurableFlag\":0,\"configurableRuntimeFlag\":1,\"cardSystemType\":0,\"configureContent\":null,\"versionNumber\":\"1.0.0\",\"cardStatus\":0,\"cardWid\":\"8531633258781537\",\"showTitle\":false,\"layoutCardTitle\":{\"cardTitle\":\"搜索结果\"},\"enabled\":true}}]}]";
        intiProPages.forEach(intiProPage->{
            switch (intiProPage.getPageCode()){
                case "home":
                    intiProPage.setCardLayout(homeLayout);
                    break;
                case "hall":
                    intiProPage.setCardLayout(hallLayout);
                    break;
                case "itemCategoryDetail":
                    intiProPage.setCardLayout(itemCategoryDetailLayout);
                    break;
                case "itemDetail":
                    intiProPage.setCardLayout(itemDetailLayout);
                    break;
                case "apps":
                    intiProPage.setCardLayout(appsLayout);
                    break;
                case "personal":
                    intiProPage.setCardLayout(personalLayout);
                    break;
                case "result":
                    intiProPage.setCardLayout(resultLayout);
                    break;
                default:
                    intiProPage.setCardLayout(homeLayout);
                    break;
            }
        });
        pageInfoService.updateBatchById(intiProPages);
    }

    /***
     * @Author jcx
     * @Description 更新所有旧版本展示方案页面卡片截图地址
     * @Date 18:43 2021/6/3
     * @return void
     **/
    private void updateAllPageLayout(){
        List<PageInfoEntity> pageInfos = pageInfoService.list(new QueryWrapper<PageInfoEntity>());
        if(CollectionUtils.isNotEmpty(pageInfos)){
            pageInfos.forEach(pageInfo->{
                String cardLayout=pageInfo.getCardLayout();
                String pcImg="/pc/img/display";
                String mobileImg="/mobile/img/display";
                if(StringUtil.isNotEmpty(cardLayout)){
                    if(cardLayout.contains("/pc/images/display")){
                        cardLayout=cardLayout.replaceAll("/pc/images/display",pcImg);
                    }
                    if(cardLayout.contains("/pc/image/display")){
                        cardLayout=cardLayout.replaceAll("/pc/image/display",pcImg);
                    }
                    if(cardLayout.contains("/mobile/image/display")){
                        cardLayout=cardLayout.replaceAll("/mobile/image/display",mobileImg);
                    }
                    if(cardLayout.contains("/mobile/images/display")){
                        cardLayout=cardLayout.replaceAll("/mobile/images/display",mobileImg);
                    }
                    if(cardLayout.contains("/images/display")){
                        cardLayout=cardLayout.replaceAll("/images/display",pcImg);
                    }
                    if(cardLayout.contains("/image/display")){
                        cardLayout=cardLayout.replaceAll("/image/display",pcImg);
                    }
                    if(cardLayout.contains("/SYS_CARD_SERVICEBUS/img/display")){
                        cardLayout=cardLayout.replaceAll("/SYS_CARD_SERVICEBUS/img/display","/SYS_CARD_SERVICEBUS/pc/img/display");
                    }
                    if(cardLayout.contains("/SYS_CARD_TODOTASK/img/display")){
                        cardLayout=cardLayout.replaceAll("/SYS_CARD_TODOTASK/img/display","/SYS_CARD_TODOTASK/pc/img/display");
                    }
                    if(cardLayout.contains("/SYS_CARD_RECOMMENDSERVICEITEMS/img/display")){
                        cardLayout=cardLayout.replaceAll("/SYS_CARD_RECOMMENDSERVICEITEMS/img/display","/SYS_CARD_RECOMMENDSERVICEITEMS/pc/img/display");
                    }
                    if(cardLayout.contains("/SYS_CARD_LINK/img/display")){
                        cardLayout=cardLayout.replaceAll("/SYS_CARD_LINK/img/display","/SYS_CARD_LINK/pc/img/display");
                    }
                    if(cardLayout.contains("/SYS_CARD_MESSAGECENTER/img/display")){
                        cardLayout=cardLayout.replaceAll("/SYS_CARD_MESSAGECENTER/img/display","/SYS_CARD_MESSAGECENTER/pc/img/display");
                    }
                    if(cardLayout.contains("/SYS_CARD_ALLSERVICEITEM/img/display")){
                        cardLayout=cardLayout.replaceAll("/SYS_CARD_ALLSERVICEITEM/img/display","/SYS_CARD_ALLSERVICEITEM/pc/img/display");
                    }
                    if(cardLayout.contains("/SYS_CARD_CAROUSEL/img/display")){
                        cardLayout=cardLayout.replaceAll("/SYS_CARD_CAROUSEL/img/display","/SYS_CARD_CAROUSEL/pc/img/display");
                    }
                    if(cardLayout.contains("/SYS_CARD_DONETASK/img/display")){
                        cardLayout=cardLayout.replaceAll("/SYS_CARD_DONETASK/img/display","/SYS_CARD_DONETASK/pc/img/display");
                    }
                    if(cardLayout.contains("/SYS_CARD_SEARCHRESULTS/img/display")){
                        cardLayout=cardLayout.replaceAll("/SYS_CARD_SEARCHRESULTS/img/display","/SYS_CARD_SEARCHRESULTS/pc/img/display");
                    }
                    pageInfo.setCardLayout(cardLayout);
                }
            });
            pageInfoService.updateBatchById(pageInfos);
        }
    }
}
