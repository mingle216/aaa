package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 服务事项搜索历史表
 * </p>
 *
 * @author jcx
 * @since 2020-09-11
 */
@TableName("t_amp_service_search_his")
public class ServiceSearchHisEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜素关键字
     */
    @TableField("search_key")
    private String searchKey;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
