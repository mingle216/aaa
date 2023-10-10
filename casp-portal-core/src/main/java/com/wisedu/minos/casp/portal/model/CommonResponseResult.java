package com.wisedu.minos.casp.portal.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 通用response
 * @date 2021/7/14 14:04
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Getter
@Setter
public class CommonResponseResult<T> extends ModelApiResponse {
    /**
     * 数据
     */
    private T data;

}
