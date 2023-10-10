package com.wisedu.minos.casp.portal.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:给运营中心应用信息接口
 * @Author: 01120012
 * @Date: 2020/4/29
 */
@Getter
@Setter
public class AppInfoForCasp {
    private String appId;
    private String markId;
    private String appName;
    private String iconUrl;
    private int runPlatFormType;
    private String description;
    private String version;
    private String deployPrefix;
    private String entranceUrl;
    private String vendorName;
    private String domainId;
    private int status;
    private String onlineTime;
    private String offlineTime;
    private int isNew;
    private int isOpenInNewTab;
    private int isEnableDescription;
    private int descriptionAuth;
    private int viewAuth;
    private int accessAuth;
    private int isRecommend;
    private String recommendBeginTime;
    private String recommendEndTime;
    private String recommendOpTime;
    private int identityType;
    private String authUrl;
    private String appShortName;
    private int purchaseType;
    private String updateTime;
    private int isOpenAssessPage;
    private int isPcApp;
    private int isPcCard;
    private int isMobileCard;
    private int isTestRun;
    private int appProtocol;
    private String offlineDescription;
    private String appNameEnus;
    private String pcOfflineDes;
    private String mbStatus;
    private String mbOnlineTime;
    private String mbOfflineTime;
    private String mbOfflineDes;
    private String isServiceCard;
    private String isSimpleCard;
    private String categoryType;
    private String isThirdApp;
    private String thirdCallbackUrl;
    private String operateToken;
    private String cloudCategoryId;
    private String cloudCategoryName;
    private String pcEntranceUrl;
    private String h5EntranceUrl;
}
