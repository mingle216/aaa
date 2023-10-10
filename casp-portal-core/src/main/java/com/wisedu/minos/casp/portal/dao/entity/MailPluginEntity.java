package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 邮箱后缀与邮箱插件关系表
 * </p>
 *
 * @author jcx
 * @since 2021-04-29
 */
@TableName("t_amp_mail_plugin")
public class MailPluginEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务主键
     */
    @TableId("wid")
    private String wid;

    /**
     * 邮箱插件名称
     */
    @TableField("mail_plugin_name")
    private String mailPluginName;

    /**
     * 邮箱后缀
     */
    @TableField("mail_suffix")
    private String mailSuffix;

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }
    public String getMailPluginName() {
        return mailPluginName;
    }

    public void setMailPluginName(String mailPluginName) {
        this.mailPluginName = mailPluginName;
    }
    public String getMailSuffix() {
        return mailSuffix;
    }

    public void setMailSuffix(String mailSuffix) {
        this.mailSuffix = mailSuffix;
    }

    @Override
    public String toString() {
        return "MailPluginEntity{" +
        "wid=" + wid +
        ", mailPluginName=" + mailPluginName +
        ", mailSuffix=" + mailSuffix +
        "}";
    }
}
