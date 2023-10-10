package com.wisedu.amp3compatible.service;

import com.wisedu.amp3compatible.model.*;
import com.wisedu.minos.casp.portal.model.UserInfo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2022/8/1
 */

public interface GetInfoService {

    Object getPersonalRemindDetail (PersonalRemindDetailRequest request);

    UserInfo getUserWithGroupInfoById(String userId);


    PersonalRemindResponse getPersonalRemindList (UserIdEntity userIdEntity);

    Object getMailAccountInfo (UserIdEntity userIdEntity);

    Object getServiceItemInfo (YanBianServiceReq yanBianServiceReq);

    Object getLinkUrl (GetLinkUrlRequest request);
}
