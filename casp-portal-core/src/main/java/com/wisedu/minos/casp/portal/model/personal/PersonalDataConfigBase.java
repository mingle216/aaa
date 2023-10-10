package com.wisedu.minos.casp.portal.model.personal;

import com.wisedu.minos.casp.portal.model.MultiLangData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName PersonalDataVo
 * @Description 个人数据
 * @Date 2021/7/13 17:59
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Getter
@Setter
public class PersonalDataConfigBase {

    private String wid;
    /**
     * 标题
     */
    private List<MultiLangData> title;

    private List<MultiLangData> mainInfo;

    private List<MultiLangData> subInfo;

    private String iconUrl;
    /**
     * 数据来源方式（0：静态文字，1：JSON接口，2:MINOS数据源，3：邮箱 4：插件）
     */
    private Integer sourceType;

    private Integer enabled;

    private Long cacheTimeout;

    private int orderNumber;

    private StaticDataSource staticDataSource;

    private JSONDataSource jsonDataSource;

    private MinosDataSource minosDataSource;

    private MailDataSource mailDataSource;

    private PluginDataSource pluginDataSource;

    /**
     * 静态数据源
     */
    @Getter
    @Setter
    public static class StaticDataSource {
        private String linkUrl;
    }

    /**
     * json数据源
     */
    @Getter
    @Setter
    public static class JSONDataSource {
        /**f
         * 接口地址
         */
        private String sourceKey;
        /**f
         * 接口请求方式
         */
        private Integer httpMethod;
        /**f
         * 用户参数名字
         */
        private String userKey;
        /**f
         * 跳转地址
         */
        private String linkUrl;

    }

    /**
     * minos数据源
     */
    @Getter
    @Setter
    public static class MinosDataSource {
        /**
         * minos数据源id
         */
        private String sourceKey;
        /**f
         * sql
         */
        private String dataSql;
        /**f
         * 跳转地址
         */
        private String linkUrl;
    }

    @Setter
    @Getter
    public static class MailDataSource {
        /**
         * 邮箱类型
         */
        private String sourceKey;
    }

    @Setter
    @Getter
    public static class PluginDataSource {
        /**
         * 邮箱类型
         */
        private String sourceKey;

        /**f
         * 跳转地址
         */
        private String linkUrl;
        /**
         * 插件配置
         */
        private String authConfig;
    }
}
