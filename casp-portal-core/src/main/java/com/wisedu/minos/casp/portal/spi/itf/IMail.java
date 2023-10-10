package com.wisedu.minos.casp.portal.spi.itf;

import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.model.MailPluginInfo;

import java.util.Map;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title IMail
 * @Author: 01319098
 * @Date: 2021/3/2
 */
@MinosSPI
public interface IMail {
    /**
     * 返回邮箱的唯一ID
     * SYS_MAIL_TENCENTMAIL
     *
     * @return
     */
    String getMailId ();

    /**
     * The destroy store template
     */
    void destroy ();

    void initialize (Map<String, Object> param);

    /**
     * 获取未读邮件数量
     */
    int getUnreadCount (String userAccount);

    /**
     * 获取链接地址
     */
    String getLinkUrl (String userAccount);

    /**
     * 获取邮箱插件信息
     */
    MailPluginInfo getPluginInfo ();

}
