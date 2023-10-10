package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.dao.entity.AttachInfoEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title AttachInfoCustomEntity
 * @Author: 01116267
 * @Date: 2021/11/16
 */
@Data
public class AttachInfoCustomEntity extends AttachInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String reUrl;
    private String ossName;
}
