package com.wisedu.casp.controller.minos;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.api.model.*;
import com.wisedu.minos.casp.portal.model.BaseResponse;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.caluser.QueryUserForCalDto;
import com.wisedu.minos.casp.portal.model.caluser.QueryUserForCalPageDto;
import com.wisedu.minos.casp.portal.model.caluser.UserInfoForCalVo;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 日程用户相关操作
 * @date 2022/8/11 13:43
 * @author jszhang@wisedu
 * @version 1.0
 **/
@RestController
public class CalUserController {
    @DubboReference
   private UserService userService;
    @Autowired
    private  UserUtil userUtil;

    @RequestMapping("/cal/searchUserByKeywordAndOrgwid")
    public BaseResponse<List<UserInfoForCalVo>> getUser(@RequestBody QueryUserForCalDto dto) {
        BaseResponse<List<UserInfoForCalVo>> baseResponse = new BaseResponse<>();
        baseResponse.setErrcode("0");
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (currentUser == null) {
            return baseResponse;
        }
        UserInfosByOrgGroupAndUsersRequest re = new UserInfosByOrgGroupAndUsersRequest();
        re.setSearchKey(dto.getKeyword());
        re.setOrgWidList(dto.getOrgIds());
        DubboUserInfoResp userInfoResp = userService.queryUserInfosByOrgGroupAndUsersForCalendar(re);
        if (userInfoResp != null && "0".equals(userInfoResp.getErrcode())) {
            List<DubboUserInfo> userInfos = userInfoResp.getData().stream().filter(e -> !userUtil.getCurrentUser().getWid().equals(e.getWid())).collect(Collectors.toList());
            List<UserInfoForCalVo> collect = userInfos.stream().map(e ->
                    new UserInfoForCalVo().setUserAccount(e.getUserAccount())
                            .setUserName(e.getUserName()).setWid(e.getWid()).setDeptName(e.getDeptName())
            ).collect(Collectors.toList());
            baseResponse.setData(collect);
        }
        return baseResponse;
    }

    @RequestMapping("/cal/queryOrgUsers")
    public BaseResponse queryUserList(@RequestBody QueryUserForCalPageDto dto) {
        BaseResponse<List<UserInfoForCalVo>> baseResponse = new BaseResponse<>();
        baseResponse.setErrcode("0");
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (currentUser == null) {
            return baseResponse;
        }
        if (dto.getIsAll() == 0) {
            DubboUserSearchRequest request2 = getDubboUserSearchRequest(dto.getPageNumber(), dto.getPageSize(), dto.getOrgId());
            PageResponse<DubboUserInfo> resp2 = userService.searchUser(request2);
            Page page = new Page<>();
            page.setSize(resp2.getPageSize());
            page.setTotal(resp2.getTotal());
            page.setCurrent(dto.getPageNumber());
            page.setRecords(getList(resp2));
            baseResponse.setPage(page);
        } else {
            baseResponse.setPage( queryForAll(dto.getOrgId()));
        }
        return baseResponse;

    }

    Page<UserInfoForCalVo> queryForAll(String orgWid) {
        Page<UserInfoForCalVo> page = new Page<>();
        int pageNumber = 1;
        int pageSize = 1000;
//        int pageSize = 10;
        DubboUserSearchRequest request2 = getDubboUserSearchRequest(pageNumber, pageSize, orgWid);
        PageResponse<DubboUserInfo> resp = userService.searchUser(request2);
        List<UserInfoForCalVo> list = new ArrayList<>(getList(resp));
        //超过1k条只返回1k条
        long total = resp.getTotal();
        long tempTotal;
        if (resp.getTotal() >= 1_000) {
            tempTotal = 1_000;
        }
//        if (resp.getTotal() >= 20) {
//            tempTotal = 20;
//        }
        else {
            tempTotal = resp.getTotal();
        }

        while (tempTotal > pageNumber * pageSize) {
            ++pageNumber;
            request2 = getDubboUserSearchRequest(pageNumber, pageSize, orgWid);
            resp = userService.searchUser(request2);
            list.addAll(getList(resp));
        }
        page.setRecords(list);
        page.setTotal(total);
        return page;
    }

    @NotNull
    private DubboUserSearchRequest getDubboUserSearchRequest(int pageNumber, int pageSize, String orgWid) {
        DubboUserSearchRequest request2 = new DubboUserSearchRequest();
        request2.setPageNumber(pageNumber);
        request2.setPageSize(pageSize);
        DubboModelInfo modelInfo2 = new DubboModelInfo();
        modelInfo2.setModelType("2");
        modelInfo2.setModelWid(orgWid);
        modelInfo2.setCurrentOrg(1);
        request2.setModelInfo(modelInfo2);
        List<DubboSearchObject> searchObjects =new ArrayList<>();
        DubboSearchObject dubboSearchObject = new DubboSearchObject();
        dubboSearchObject.setComparator(DubboSearchObject.ComparatorEnum.NOT);
        dubboSearchObject.setField("wid");
        dubboSearchObject.setValue(userUtil.getCurrentUser().getWid());
        searchObjects.add(dubboSearchObject);
        request2.setSearchCriteria(searchObjects);
        return request2;
    }

    List<UserInfoForCalVo> getList(PageResponse<DubboUserInfo> resp2) {
        if (resp2 != null && CollectionUtil.isNotEmpty(resp2.getDatas())) {
            return resp2.getDatas().stream().map(e -> new UserInfoForCalVo().setUserAccount(e.getUserAccount())
                    .setUserName(e.getUserName()).setWid(e.getWid())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}