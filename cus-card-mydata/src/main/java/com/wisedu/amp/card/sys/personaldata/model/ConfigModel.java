package com.wisedu.amp.card.sys.personaldata.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
/**
 * configJson对应的model
 *
 * @author jszhang
 * @date 2021/7/20 14:41
 * @version 1.0
 */
@Getter
@Setter
public class ConfigModel implements Serializable {
    private Object serviceCarHeight;
    private int showBindMail;
    private String columns;
    private List<String> personalDatas;

    //3.4.1.Beta2新增展示个人信息
    private PersonalConfigDto personalInfo;
}
