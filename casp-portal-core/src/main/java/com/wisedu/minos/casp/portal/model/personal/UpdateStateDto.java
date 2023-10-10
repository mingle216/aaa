package com.wisedu.minos.casp.portal.model.personal;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 更新状态dto
 * @date 2021/7/14 14:26
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Getter
@Setter
public class UpdateStateDto {
    private String wid;
    /**f
     * 0：停用，1：启用
     */
    private Integer enabled;

}
