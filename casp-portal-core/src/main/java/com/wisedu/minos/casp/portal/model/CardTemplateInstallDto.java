package com.wisedu.minos.casp.portal.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述：卡片、模板安装基础属性
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CardTemplateInstallDto
 * @Author: 01120034
 * @Date: 2021/8/27
 */
@Getter
@Setter
public class CardTemplateInstallDto {
    private String id;
    private String name;
    private String imageUrl;
    private String platformType;
    private String configurableFlag;
    private String configurableRuntimeFlag;
    private String desc;
    private String versionNumber;

    public CardTemplateInstallDto(){
    }
    public CardTemplateInstallDto(String id,String name,String imageUrl,String platformType,
                                  String configurableFlag,String configurableRuntimeFlag,
                                  String desc,String versionNumber){
        this.id=id;
        this.name=name;
        this.imageUrl=imageUrl;
        this.platformType=platformType;
        this.configurableFlag=configurableFlag;
        this.configurableRuntimeFlag=configurableRuntimeFlag;
        this.desc=desc;
        this.versionNumber=versionNumber;
    }

}
