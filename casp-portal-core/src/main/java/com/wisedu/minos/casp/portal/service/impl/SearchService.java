package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.ServiceSearchHisEntity;
import com.wisedu.minos.casp.portal.dao.mapper.ServiceSearchHisMapper;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;

/**
 * 功能描述：搜索ServiceImpl
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title SearchServiceImpl
 * @Author: jcx
 * @Date: 2020/9/8
 */
@Service
public class SearchService {

    @Autowired
    private ServiceSearchHisMapper serviceSearchHisMapper;

    @Autowired
    UserUtil userUtil;


    /**
     * @return void
     * @Author jcx
     * @Description 搜索历史添加
     * @Date 16:47 2020/9/8
     * @Param userAccount: 用户帐号
     * @Param keywords: 关键字
     **/
    public void addSearchHis(String userAccount, String keywords) {
        if (StringUtil.isEmpty(userAccount)) {
            return;
        }
        if (StringUtil.isBlank(keywords)) {
            return;
        }
        String unescapeKeywords = StringUtil.xssDecode(keywords);
        deleteSearchHis(userAccount, unescapeKeywords);
        ServiceSearchHisEntity searchHis = new ServiceSearchHisEntity();
        searchHis.setSearchKey(unescapeKeywords);
        searchHis.setUserId(userAccount);
        searchHis.setWid(StringUtil.simpleUuid());
        serviceSearchHisMapper.insert(searchHis);
    }

    /**
     * getServiceSearchHis
     * <p/>
     * 搜索历史查询
     *
     * @param maxNum 最大记录数
     * @return java.util.List<com.wisedu.minos.casp.portal.dao.entity.ServiceSearchHisEntity>
     * @throws
     * @date 2020/10/15 18:32
     */
    public List<ServiceSearchHisEntity> getServiceSearchHis(int maxNum) {
        UserInfo currentUser = userUtil.getCurrentUser();
        if (currentUser == null) {
            return Collections.emptyList();
        }
        QueryWrapper<ServiceSearchHisEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(DbFieldConstant.USER_ID, currentUser.getUserAccount());
        wrapper.orderByDesc(DbFieldConstant.UPDATE_TIME);
        Page<ServiceSearchHisEntity> page = new Page<>();
        page.setPages(1).setSize(maxNum);
        return serviceSearchHisMapper.selectPage(page, wrapper).getRecords();
    }


    /**
     * @return void
     * @Author jcx
     * @Description 删除搜索历史
     * @Date 16:58 2020/9/8
     * @Param userAccount: 用户帐号
     * @Param keywords: 关键字
     **/
    public void deleteSearchHis(String userAccount, String keywords) {
        if (StringUtil.isEmpty(userAccount)) {
            return;
        }
        QueryWrapper<ServiceSearchHisEntity> wrapper = new QueryWrapper<>();
              wrapper.eq(DbFieldConstant.USER_ID, userAccount);
        if (StringUtil.isNotEmpty(keywords)) {
            wrapper.eq(DbFieldConstant.SEARCH_KEY, keywords);
        }
        serviceSearchHisMapper.delete(wrapper);
    }

}
