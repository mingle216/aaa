package com.wisedu.minos.casp.portal.common.constant;


import com.google.common.collect.Lists;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 功能描述： 全局参数类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title Global
 * @Author: jcx
 * @Date: 2020/7/20
 */
public class Global {

    public static final String CONSTANT_YES = "1";//是
    public static final String CONSTANT_NO = "0";//否
    public static final int YES = 1;//是
    public static final int NO = 0;//否

    public static final Integer NUMBER_ZERO = 0;
    public static final String EMPTY = "";//否
    public static final String WID = "WID";//主键名称
    public static final String FB = "副本";//副本名称
    public static final String CREATE_TIME="create_time";
    public static final String POP_WINDOW_PREFIX="casp:portal:popWindow";
    /**
     * 日期时间类型格式
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期类型格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 时间类型的格式
     */
    public static final String TIME_FORMAT = "HH:mm:ss";
    /**
     * 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块
     */
    public static final String US_ASCII = "US-ASCII";

    /**
     * ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1
     */
    public static final String ISO_8859_1 = "ISO-8859-1";

    /**
     * 8 位 UCS 转换格式
     */
    public static final String UTF_8 = "UTF-8";

    /**
     * 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序
     */
    public static final String UTF_16BE = "UTF-16BE";

    /**
     * 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序
     */
    public static final String UTF_16LE = "UTF-16LE";

    /**
     * 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识
     */
    public static final String UTF_16 = "UTF-16";

    /**
     * 中文超大字符集
     */
    public static final String GBK = "GBK";

    /**
     * 最常见的中文字符集
     */
    public static final String GB2312 = "GB2312";
    /**
     * 空字符串
     */
    public static final String STR_EMPTY = "";
    /**
     * minos人员状态 (0 未激活 1 正常 2 冻结 3 锁定 4 禁用)
     */
    public static final String MINOS_USER_STATES = "0,1";
    /**
     * minos账号状态 (1 正常 2 冻结 4 禁用 5归档)
     */
    public static final String MINOS_ACCOUNT_STATUS = "1";


    /**
     * 默认主页编码
     */
    public static final String HOME = "home";

    /**
     * 根菜单 为0表示没有父菜单
     */
    public static final String ROOT_MENU_ID = "0";
    /**
     * 根页面 为0表示没有父页面
     */
    public static final String ROOT_PAGE_ID = "0";

    /**
     * 默认站点wid
     */
    public static final String MASTER_SITE_WID = "-100";

    public static final String PORTAL_URI = "/portal";
    public static final String LOGIN_URI = "/login";
    public static final String LOGOUT_URI = "/logout";
    public static final String GET_PAGEVIEW_URI = "/getPageView";
    public static final String INDEX_URI = "/index.html";
    public static final String UNMATCHED_STATE = "未匹配的状态";
    /**
     * 会话中保存多站点的key
     */
    public static final String SITE_WID = "COM.WISEDU.CASP.SITEWID";

    /**f
     * 是否随机站点
     */
    public static final String IS_RANDOM = "COM.WISEDU.CASP.IS_RANDOM";
    /**f
     * 组织机构是全局，
     * 其下面的所有组织机构的人都可以访问
     */
    public static final String ORG_SCHOOL = "school";



    /**
     * spi插件类型类型
     */
    public enum SpiPluginType {
        /**
         * 卡片
         */
        CARD("卡片"),
        /**
         * 模板
         */
        TEMPLATE("模板");

        String name;

        SpiPluginType(String name) {
            this.name = name;
        }
    }

    /**
     * 删除状态
     * author jdwan
     */
    @AllArgsConstructor
    @Getter
    public enum DeleteStatus {
        /**
         *  删除 1
         */
        YES("删除", 1),

        /**
         *  不删除 0
         */
        NO("不删除", 0);
        private String name;
        private int id;


        public static DeleteStatus getById(int id) {
            return Arrays.stream(DeleteStatus.values()).filter(state -> state.getId() == id)
                    .findFirst().orElseThrow(() -> new BusinessException(UNMATCHED_STATE + id));
        }
    }


    /**
     * Status
     * <p/>
     * 禁用启用状态
     *
     * @author hyluan
     * @date 2020/9/22 18:22
     * Copyright (c) 2018 wisedu
     */
    @AllArgsConstructor
    @Getter
    public enum Status {

        DISABLE("禁用",1),
        ENABLE("启用",0);
        private String name;
        private int id;

        public static Status getById(int id) {
            return Arrays.stream(Status.values()).filter(state -> state.getId() == id)
                    .findFirst().orElseThrow(() -> new BusinessException(UNMATCHED_STATE + id));
        }
    }


    /**
     * 方案启用停用状态
     */
    @AllArgsConstructor
    @Getter
    public enum PageStatus {

        ENABLE("启用",0),
        DISABLE("停用",1);
        private String name;
        private int id;

        public static PageStatus getById(int id) {
            return Arrays.stream(PageStatus.values()).filter(state -> state.getId() == id)
                    .findFirst().orElseThrow(() -> new BusinessException(UNMATCHED_STATE + id));
        }
    }

    @AllArgsConstructor
    @Getter
    public enum PageTypes {

        DEFAULT_PAGE("功能页",0),
        FREE_PAGE("自由页",1);
        private String name;
        private int type;

        public static PageTypes getByType(int type) {
            return Arrays.stream(PageTypes.values()).filter(state -> state.getType() == type)
                    .findFirst().orElseThrow(() -> new BusinessException(UNMATCHED_STATE + type));
        }
    }

    /**
     * 侧边栏启用停用状态
     */
    @AllArgsConstructor
    @Getter
    public enum EnableStatus {

        ENABLE("启用",1),
        DISABLE("停用",0);
        private String name;
        private int id;

        public static EnableStatus getById(int id) {
            return Arrays.stream(EnableStatus.values()).filter(state -> state.getId() == id)
                    .findFirst().orElseThrow(() -> new BusinessException(UNMATCHED_STATE + id));
        }
    }

    /**
     * PlatformType
     * <p/>
     * 平台类型
     *
     * @author hyluan
     * @date 2020/9/22 18:22
     * Copyright (c) 2018 wisedu
     */
    @AllArgsConstructor
    @Getter
    public enum PlatformType {

        PC(0),
        MOBILE(1);
        int type;

        public static PlatformType getByType(int type) {
            return Arrays.stream(PlatformType.values()).filter(state -> state.getType() == type)
                    .findFirst().orElseThrow(() -> new BusinessException(UNMATCHED_STATE + type));
        }
    }

    /**
     * SidebarPositionType
     * <p/>
     * 侧边栏位置类型
     *
     * @author hyluan
     * @date 2020/9/22 18:23
     * Copyright (c) 2018 wisedu
     */
    @AllArgsConstructor
    @Getter
    public enum SidebarPositionType {

        LEFT(0),
        RIGHT(1);
        int id;

    }

    /**
     * uploadLocalType
     * <p/>
     * 上传到服务器本地文件类型，以指向不同目录
     *
     * @author hyluan
     * @date 2020/9/22 18:23
     * Copyright (c) 2018 wisedu
     */
    @AllArgsConstructor
    @Getter
    public enum uploadLocalType {
        ICON("0");
        String type;
    }

    /**
     * uploadLocalType
     * <p/>
     * 相对路径文件访问前缀
     *
     * @author hyluan
     * @date 2020/9/22 18:23
     * Copyright (c) 2018 wisedu
     */
    @AllArgsConstructor
    @Getter
    public enum accessFilePrefix {
        /**
         * 卡片jar相对路径文件访问前缀
         */
        CARD_ACCESS_PREFIX("cardAccess"),
        /**
         * 模板jar相对路径文件访问前缀
         */
        TEMPLATE_ACCESS_PREFIX("templateAccess"),
        /**
         * 模板jar相对路径文件访问前缀
         */
        ICON_ACCESS_PREFIX("icon");
        String type;
    }
    /**
     * uploadLocalType
     * <p/>
     *
     *
     * @author hyluan
     * @date 2020/9/22 18:23
     * Copyright (c) 2018 wisedu
     */
    @AllArgsConstructor
    @Getter
    public enum openType {
        /**
         * 当前页打开
         */
        OPEN_LOCAL_PAGE(0),
        /**
         * 新开窗口
         */
        OPEN_OTHER_WINDOW_PAGE(1);
        int type;
    }

    /**
     * MenuType
     * <p/>
     * 菜单类型
     *
     * @author hyluan
     * @date 2020/9/22 18:22
     * Copyright (c) 2018 wisedu
     */
    @AllArgsConstructor
    @Getter
    public enum MenuType {

        NULL_LINK("无连接",0),
        INNER_LINK("内部链接",1),
        OUTER_LINK("外部链接",2)
        ;
        private String name;
        private int id;

        public static MenuType getById(int id) {
            return Arrays.stream(MenuType.values()).filter(state -> state.getId() == id)
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的菜单连接" + id));
        }
    }

    /**
     * IconType
     * <p/>
     * 图标类型
     *
     * @author hyluan
     * @date 2020/9/22 18:22
     * Copyright (c) 2018 wisedu
     */
    @AllArgsConstructor
    @Getter
    public enum IconType {

        PNG("png图标",0),
        FONT("字体图标",1),
        ;
        private String name;
        private int id;

        public static IconType getById(int id) {
            return Arrays.stream(IconType.values()).filter(state -> state.getId() == id)
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的图标类型" + id));
        }
    }

    @AllArgsConstructor
    @Getter
    public enum SelectMenuIconType {

        NO_PNG("不使用图标",2),
        PNG("png图标",1),
        FONT("字体图标",0),
        ;
        private String name;
        private int id;

        public static SelectMenuIconType getById(int id) {
            return Arrays.stream(SelectMenuIconType.values()).filter(state -> state.getId() == id)
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的图标类型" + id));
        }
    }

    /**
     * MenuAuthType
     * <p/>
     * 菜单授权类型
     *
     * @author hyluan
     * @date 2020/9/22 18:22
     * Copyright (c) 2018 wisedu
     */
    @AllArgsConstructor
    @Getter
    public enum MenuAuthType {

        ORG("组织机构",0),
        USER("用户",1),
        DOMAIN_AND_GROUP("域及用户组",2)
        ;
        private String name;
        private int id;

        public static MenuAuthType getById(int id) {
            return Arrays.stream(MenuAuthType.values()).filter(state -> state.getId() == id)
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的菜单授权类型" + id));
        }
    }

    /**
     * MenuMenuAuthType
     * <p/>
     * 菜单授权种类
     * @author hyluan
     * @date 2020/10/10 10:35
     * Copyright (c) 2018 wisedu
     */
    @AllArgsConstructor
    @Getter
    public enum MenuMenuAuthType {
        TOP_MENU("顶部菜单栏", "0"),
        SIDEBAR("侧边栏", "1"),
        SELECT_MENU("下拉菜单","2")
        ;
        private String name;
        private String id;

        public static MenuMenuAuthType getById(String id) {
            return Arrays.stream(MenuMenuAuthType.values()).filter(state -> state.getId().equals(id))
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的菜单授权种类" + id));
        }
    }

    /**
     * 模板状态
     */
    @AllArgsConstructor
    @Getter
    public enum TemplateStatus {
        NORMAL("正常", "0"),
        HAVE_UPDATES("有更新", "1"),
        NOT_AVAILABLE("不可用", "2"),
        ;
        private String name;
        private String status;

        public static TemplateStatus getByStatus(String status) {
            return Arrays.stream(TemplateStatus.values()).filter(state -> state.getStatus().equals(status))
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的模板状态" + status));
        }
    }


    /**
     * 插件发布表 插件类型
     */
    @AllArgsConstructor
    @Getter
    public enum PluginType {
        TEMPLATE("模板", 0),
        CARD("卡片", 1),
        MAIL_PLUGIN("邮箱插件", 2);
        private String name;
        private Integer type;

        public static PluginType getByType(String type) {
            return Arrays.stream(PluginType.values()).filter(state -> state.getType().equals(type))
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的插件类型" + type));
        }
    }



    /**
     * AuthType
     * <p/>
     * 授权类型
     *
     * @author hyluan
     * @date 2020/9/22 18:22
     * Copyright (c) 2018 wisedu
     */
    @AllArgsConstructor
    @Getter
    public enum AuthType {
        /**
         * 0:游客可见 1:登录后可见  2:游客及登录后可见 3:授权后可见
         */
        VISITOR_VISIBLE("游客可见",0),
        LOGIN_VISIBLE("登录后可见",1),
        VISITOR_AND_LOGIN_VISIBLE("游客及登录后可见",2),
        AUTH_VISIBLE("授权后可见",3),
        ;
        private String name;
        private int id;

        public static AuthType getById(int id) {
            return Arrays.stream(AuthType.values()).filter(state -> state.getId() == id)
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的授权类型" + id));
        }
    }


    /**
     * @Author jcx
     * @Description 管控台 模型对象：-1:全部 0:用户 1:用户组 2:组织机构 3:用户分类 4:业务域
     * @Date 19:52 2020/12/23
     **/
    @AllArgsConstructor
    @Getter
    public enum ModelType {
        /**
         * 全部
         */
        ALL_MODEL("-1"),
        /**
         * 用户
         */
        USER_MODEL("0"),
        /**
         * 用户组
         */
        USER_GRANT_MODEL("1"),
        /**
         * 组织机构
         */
        ORG_MODEL("2"),
        /**
         * 用户分类
         */
        USER_CLASSIFICATION_MODEL("3"),
        /**
         * 业务域
         */
        BUSINESS_DOMAIN_MODEL("4");
        String type;
    }

    /**
     * 模板初始化Wid
     */
    @AllArgsConstructor
    @Getter
    public enum TemplateInitWid {

        OFFICIAL_WID("1");
        String id;

    }

    /**
     * 展示方案初始化Wid
     */
    @AllArgsConstructor
    @Getter
    public enum ShowProgrammeInitWid {
        //初始化official展示方案wid
        OFFICIAL_PC_INTI_PROGRAMME_WID("2d8296b6d3bc49ec977af23029b3d718"),
        //默认official展示方案wid
        OFFICIAL_PC_DEFAULT_PROGRAMME_WID("2d8296b6d3bc49ec977af23029b3d717"),
        OFFICIAL_MOBILE_DEFAULT_PROGRAMME_WID("2d8296b6d3bc49ec977af23029b3d719"),
        //初始化移动端展示方案wid
        OFFICIAL_MOBILE_INTI_PROGRAMME_WID("c2a803f5786d414ea50fa780398c7f74");
        String id;

    }

    @AllArgsConstructor
    @Getter
    public enum TemPlateGlobalMethod {

        GET_TEMPLATE_CONTENT("getTemplateContent");
        String methodName;

    }

    /***
     * @Author jcx
     * @Description 组件公共设置参数
     * @Date 9:39 2021/1/19
     **/
    @AllArgsConstructor
    @Getter
    public enum ComponentParam {
        DATAS("datas"),
        DEFAULT_VALUE("defaultValue"),
        VALUE("value"),
        COMPONENT("component");
        String param;

        public static ComponentParam getByParam(String param) {
            return Arrays.stream(ComponentParam.values()).filter(state -> state.getParam().equals(param))
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的组件公共设置参数" + param));
        }
    }

    /***
     * @Author jcx
     * @Description 组件名称集合
     * @Date 11:06 2021/2/9
     **/
    @AllArgsConstructor
    @Getter
    public enum Components {
        ADD_INFO("addInfo"),
        ADD_LINKLIST("addLinkList"),
        ADD_LINKLIST_PC("addLinkListPC"),
        CARD_HEIGHT("cardHeight"),
        CASCADER("cascader"),
        CHECKBOX("checkbox"),
        INPUT("input"),
        INPUT_NUMBER("inputNumber"),
        CONFIG_LOGO("configLogo"),
        NEWS_DISPLAY("newsDisplay"),
        ANNOUNCEMENT_DISPLAY("announcementDisplay"),
        RADIO("radio"),
        SELECT("select"),
        DISPLAY_AREA_SELECT("displayAreaSelect"),
        SLIDE_SETTING("slideSetting"),
        THEME_COLOR_SETTING("themeColorSetting"),
        LINK("link"),
        NEWS_CONTENT("newsContent"),
        RADIO_AND_BOX("radioAndBox"),
        HEADERNAVBAR("headerNavBar"),
        ALL_SERVICE_ITEMS_DISPLAY("allServiceItemsDisplay"),
        ADD_CALENDAR("addCalendar"),
        ADD_CALENDAR_DETAIL("addCalendarDetail"),
        CAN_CREATE_SCHEDULE("canCreateSchedule"),
        SCHEDULE_REMIND_CHANNEL("scheduleRemindChannel"),
        SERVICE_ITEM_HOT_DISPLAY("serviceItemHotDisplay"),
        WEEK_REMIND_CALENDAR("WeekRemindCalendar"),
        PERSONAL_DATA_SOURCE("personalDataSource"),
        TODO_TASK_LIST_CONFIG("todotaskListConfig"),
        MYAPP_SELECT("myAppSelect"),
        ONE_THING_DISPLAY_MODEL("oneThingDisplayModel"),
        FEEDBACK_SETTING("feedBackSetting"),
        HOT_SEARCH_DISPLAY("hotSearchDisplay"),
        RECOMMEND_ONE_THING("recommendOneThing"),
        QUICK_FEEDBACK_SETTING("quickFeedbackSetting"),
        DETAIL_SERVICE_ITEM_BASE_INFO("detailServiceItemBaseInfo"),
        NEWS_SUBSCRIBE("newsSubscribe")
        ;
        String key;
        public static ComponentParam getByParam(String key) {
            return Arrays.stream(ComponentParam.values()).filter(state -> state.getParam().equals(key))
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的组件" + key));
        }
    }


    // 批量提交数据
    public static final int BATCH_PRE_COMMIT = 1000;

    public static final String OSS_FILE_BACKET_USERICON = "userIcon";//OSS上传文件用户分组
    public static final String OSS_FILE_BACKET_APPICON = "appIcon";//OSS上传文件应用分组

    public static final String TEMPLATE_UPLOAD_TYPE = "0";//上传模板包类型
    public static final String CARD_UPLOAD_TYPE = "1";//上传卡片包类型

    public static final int CARD_SYS_TYPE_SYS = 0;//卡片系统类型0：系统
    public static final int CARD_SYS_TYPE_CUSTOM = 1;//卡片系统类型1：自定义

    //卡片状态 0正常1有更新2不可用
    public static final int CARD_STATUS_NORMAL = 0;
    //卡片状态 0正常1有更新2不可用
    public static final int CARD_STATUS_TO_UPDATE = 1;
    //卡片状态 0正常1有更新2不可用
    public static final int CARD_STATUS_NOT_AVAILABLE = 2;


    public static final String DEFAULT_MENU_ROOT_PARRNT_ID="0"; //菜单表默认一级节点PARRNT_ID为0
    public static final String ORG_ROOT_PWID="-1";//机构根节点parent_id是-1
    public static final String SYS_TEMPLATE_OFFICIAL="SYS_TEMPLATE_OFFICIAL"; //默认official模板id
    public static final String TEMPLATE_ID="templateId";//模板id

    public static final String DEFAULT_LANGUAGE="zh_CN";//默认语言

    /**
     * 提供给运营中心接口salt----------开始
     */
    public static final String AMP_SALT = "CSW46YWWX9YZ5RGP2ZBZ5Z6EW363MU7C";
    public static final String FLAG = "flag";
    public static final String FAILED = "failed";
    public static final String MESSAGE = "message";
    public static final String SUCCESS = "success";
    public static final String AMP_SERVICE_ITEM = "ampServiceItem";
    public static final String AMP_APP_INFO = "ampAppInfo";
    public static final String MSG_ID = "msgId";
    public static final String PAGE_SIZE = "pageSize";
    public static final String TIMESTAMP = "timestamp";
    public static final String MODEL_TYPE = "modelType";
    //--------------------------结束--------------------------
    @AllArgsConstructor
    @Getter
    public enum LiuquibaseCompensate {
        UPDATE_20201219_DATA("update_20201219_data"),
        UPDATE_20210119_DATA("update_20210119_data"),
        UPDATE_20210122_PAGE("update_20210122_page"),
        UPDATE_20210401_PAGE_INFO("update_20210401_page_info"),
        UPDATE_MOBILE_PAGE_INFO("update_mobile_page_info"),
        UPDATE_MOBILE_PAGE_INFO_LINK("update_mobile_page_info_link"),
        UPDATE_PAGE_INFO0602_MOBILE("update_page_info0602-mobile"),
        UPDATE_INTI_PAGE_INFO_IMG("update_inti_page_info_img"),
        UPDATE_20210121_DATA("update_20210121_data"),
        UPDATE_20210207_DATA("update_20210207_data"),
        UPDATE_20210222_TEMPLATECONFIG("update_20210222_templateConfig"),
        UPDATE_20210302_TEMPLATECONFIG("update_20210302_templateConfig"),
        UPDATE_20210121_SYSTEM_CONFIG("update_20210121_system_config"),
        UPDATE_20210219_CARD("update_20210219_card"),
        UPDATE_20210302_SYS_ICON("update_20210302_sys_icon"),
        UPDATE_20210303_PAGE("update_20210303_page"),
        UPDATE_20210302_SYSTEM_CONFIG("update_20210302_system_config"),
        UPDATE_T_AMP_VIEW_TEMPLATE("update_t_amp_view_template-3.1.3"),
        UPDATE_T_AMP_VIEW_CARD("update_t_amp_view_card-3.1.3"),
        UPDATE_20210329_CARD("update_20210329_card"),
        UPDATE_20210410_VIEW_CARD("update_20210410_view_card"),
        UPDATE_20210410_UPDATE_TABLE("update_20210410_update_table"),
        UPDATE_OFFICIAL_MOBILE_IMG("update_official_mobile_img-3.1.3"),
        UPDATE_20210510_TEMPLATECONFIG("update_20210510_templateConfig_3.1.3"),
        UPDATE_PAGE_INFO0602("update_page_info0602-mobile"),

        COMPONENT("component");
        String fileName;
    }


    /**
     * 定时任务常量
     */
    @AllArgsConstructor
    @Getter
    public enum JobType {
        GET_PORTAL_SUPPORT_LANGUAGES_TASK("获取门户支持的多语言定时任务","GET_PORTAL_SUPPORT_LANGUAGES_TASK"),
        CAL_WEEKLY_REMIND_TASK("日程周提醒定时任务与","CAL_WEEKLY_REMIND_TASK"),
        PORTAL_MANAGER_PROPERTIES_TASK("查询公共属性","PORTAL_MANAGER_PROPERTIES_TASK");

        private String name;
        private String id;

        public static JobType getById(String id) {
            return Arrays.stream(JobType.values()).filter(state -> state.getId().equals(id))
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的定时任务类型" + id));
        }
    }

    // 公告弹窗次数缓存
    public static final String REDIS_POPUP_WINDOW_VISIT = "popupWindow:visit";

    //数据中需要替换的oss域名数据
    public static final String OLD_OSS_URL_DATA = "casp:portal:oldOssUrlData";

    //用户登录门户 mq name
    public static final String USER_LOGIN_QUEUE_NAME="user.login.queue.info";

    //用户登录信息redis的key前缀
    public static final String USER_LOGIN_INFO_PREFIX="user.login.info.prefix";

    // 用户邮箱绑定 邮件发送前缀
    public static final String USER_SEND_MAIL_SUCCESS="user:send:mail:success";

    public static final String SYS_CONFIG_KEY_APP_APPRAISE_DISPLAY_TYPE = "app_appraise_display_type";

    //旧事项 license code
    public static final String CASP_SIM="casp-sim";

    public static final String COOSK="coosk";

    public static final String CASP="casp";

    // 事项相关license
    public static final String COOSK_SIM_SIM="coosk-sim.sim";
    public static final String COOSK_SIM_EVALUATE="coosk-sim.evaluate";
    public static final String COOSK_SIM_SORT="coosk-sim.sort";
    public static final String COOSK_SIM_RESPONSIBILITIES="coosk-sim.responsibilities";
    public static final String COOSK_SIM_DEPTSET="coosk-sim.deptset";
    public static final String COOSK_SIM_CONFIG="coosk-sim.config";
    public static final String COOSK_SIM_OBJECT="coosk-sim.object";
    public static final String COOSK_SIL_QUERY="coosk-sil.query";
    public static final String COOSK_SIF_CONFIG="coosk-sif.config";
    public static final String COOSK_SIF_FLOW="coosk-sif.flow";
    public static final String COOSK_HOK_FEC="coosk-hok.fec";
    public static final String COOSK_HOK_HOK="coosk-hok.hok";
    public static final String COOSK_SIM_ONETHING="coosk-sim.onething";


    //定制pc模板 lincese module
    public static final String TEMPLATE_PC_CUSTOMIZED="casp-customized-pc";
    //定制h5模板 lincese module
    public static final String TEMPLATE_H5_CUSTOMIZED="casp-customized-h5";
    //定制PC模板 lincese functionCode
    public static final String PC_CUSTOMIZED="pc_customized";
    //定制移动H5模板 lincese functionCode
    public static final String MOB_CUSTOMIZED="mob_customized";

    /***
     * @Author jcx
     * @Description 服务、事项、一件事访问类型
     * @Date 21:29 2022/9/1
     **/
    @AllArgsConstructor
    @Getter
    public enum VisitType {
        SERVICE("service","服务"),
        ITEM("serviceItem","事项"),
        ONETHING("OneThing","一件事");
        private String type;
        private String des;

        public static VisitType getByParam(String type) {
            return Arrays.stream(VisitType.values()).filter(state -> state.getType().equals(type))
                    .findFirst().orElseThrow(() -> new BusinessException("未匹配的访问类型" + type));
        }
    }

    /**
     * 默认最大的数量
     */
    public static final Integer MAXSIZE = 9999;

    /**
     * 搜索关键词最大长度
     */
    public static final Integer SEARCH_KEY_MAX_LENGTH = 100;



    public static final String ZIP_UNCOMPRESSPATH="unCompressPath";
    public static final String ZIP_PROPERTIES="properties";

    //岗位中心
    public static final String CASP_POSITION="casp-position";

    public static final String POSITION_URL="positionUrl";
    public static final String COOSK_CONSOLE_URL="cooskConsoleUrl";
    public static final String COOSK_PORTAL_URL="cooskPortalUrl";


    //全局设置
    public static final String LOCAL_BLACKANDWHITE_MODE="local_blackAndWhite_Mode";
    public static final String LOCAL_MOBILE_BLACKANDWHITE_MODE="local_mobile_blackAndWhite_Mode";
    public static final String SITE_RULE="SITE_RULE";
    public static final String IS_BROWSER_COMPATIBLE="is_browser_compatible";
    public static final String COMPATIBLE_BROWSER="compatible_browser";
    public static final String COMPATIBLE_BROWSER_INFO="compatible_browser_info";
    public static final String BROWSER_INFO_IS_CHANGED="compatible_browser_info_is_changed";
    public static final String IS_CUSTOM_JUMP="is_custom_jump";
    public static final String CUSTOM_JUMP_URL="custom_jump_url";


    //getLoginUser接口获取登录人头像的oss地址 0:关闭 1:开启(默认)
    public static final String GET_LOGIN_USER_IMAGE_OSS="get_login_user_image_oss";

    //登录后动态配置的href
    public static final String GET_LOGIN_HREF_KEY="get_login_href_key";


}
