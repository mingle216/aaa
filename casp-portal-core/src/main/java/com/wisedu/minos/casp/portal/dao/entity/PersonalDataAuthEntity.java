package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 个人数据
 *
 * @author jszhang
 * @date 2021/7/19 10:24
 * @version 1.0
 */
@Getter
@Setter
@TableName("T_AMP_PERSONAL_DATA_AUTH")
@Accessors(chain = true)
public class PersonalDataAuthEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableField("data_id")
    private String dataId;

    /**
     * 授权的其他类型id
     */
    @TableField("auth_rel_wid")
    private String authRelWid;

    /**
     * 授权类型  0:组织机构 1:用户 2:域及用户组
     */
    @TableField("auth_type")
    private String authType;

    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;



}
