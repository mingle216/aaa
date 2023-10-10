package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 用户邮箱关系表
 * </p>
 *
 * @author jcx
 * @since 2021-09-08
 */
@TableName("t_amp_user_email")
public class UserEmailEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务主键
     */
    @TableId("wid")
    private String wid;

    /**
     * 邮箱账号
     */
    @TableField("email")
    private String email;

    /**
     * 用户id
     */
    @TableField("user_wid")
    private String userWid;

    /**
     * 是否已删除 1是0否
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 字段排序
     */
    @TableField("order_number")
    private Integer orderNumber;

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getUserWid() {
        return userWid;
    }

    public void setUserWid(String userWid) {
        this.userWid = userWid;
    }
    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "UserEmailEntity{" +
        "wid=" + wid +
        ", email=" + email +
        ", userWid=" + userWid +
        ", isDelete=" + isDelete +
        ", orderNumber=" + orderNumber +
        "}";
    }
}
