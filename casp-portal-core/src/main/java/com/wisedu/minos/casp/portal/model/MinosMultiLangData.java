package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 *  Minos多语言对象
 */
@Getter
@Setter
public class MinosMultiLangData {

  private String wid;

  private String sourceType;

  private String langKey;

  private String langValue;

  private String langCountry;
  
}
