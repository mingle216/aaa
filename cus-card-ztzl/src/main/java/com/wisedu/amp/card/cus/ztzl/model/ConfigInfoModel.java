package com.wisedu.amp.card.cus.ztzl.model;

import java.util.List;
import lombok.Data;

@Data
public class ConfigInfoModel {

    private HeightModel linkHeight = new HeightModel();

    private Integer linkDisplayRadio = 1;

    private Integer linkImgShowRadio = 1;

    private String linkDisplayNumSelect = "2";

    private List<Label> linkList;


}
