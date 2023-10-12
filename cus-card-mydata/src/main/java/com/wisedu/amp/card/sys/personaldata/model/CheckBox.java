package com.wisedu.amp.card.sys.personaldata.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * 复选框
 * @date 2021/7/20 17:07
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Getter
@Setter
@Accessors(chain = true)
public class CheckBox {
    private String label;
    private String value;
}
