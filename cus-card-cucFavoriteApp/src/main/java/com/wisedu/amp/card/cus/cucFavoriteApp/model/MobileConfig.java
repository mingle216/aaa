package com.wisedu.amp.card.cus.cucFavoriteApp.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MobileConfig implements Serializable {

    /**
     * 行数
     */
    private Integer rows;

    /**
     *  一行展示数量
     */
    private Integer columns;

}
