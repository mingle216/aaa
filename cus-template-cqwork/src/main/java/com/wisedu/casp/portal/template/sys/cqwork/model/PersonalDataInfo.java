package com.wisedu.casp.portal.template.sys.cqwork.model;

import lombok.Data;

import java.util.Date;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title PersonalDataInfo
 * @Author: 01319098
 * @Date: 2021/2/22
 */
@Data
public class PersonalDataInfo {
    private String wid;
    private String title;
    private String iconUrl;
    private String mainInfo;
    private String subInfo;
    private String linkUrl;
    //是否需要再次获取数据 0：不需要再次获取数据,1：需要再次获取数据
    private Integer needRetrieve;
    // 是否邮箱数据 0：否，1：是
    private int isEmail;
    // 数据获取时间，判断缓存中的数据是否有效
    private Date accessTime;

    private String extraInfo;
    /**
     * 是否隐藏个人隐私数据
     */
    private Integer isHiddenPrivacy;

    /**
     * 邮件未读数量
     */
    private Integer emailUnReadCount;
}
