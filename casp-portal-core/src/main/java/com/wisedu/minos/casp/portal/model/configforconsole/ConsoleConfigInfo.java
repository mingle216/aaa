package com.wisedu.minos.casp.portal.model.configforconsole;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ConsoleConfigInfo
 */
@Validated

public class ConsoleConfigInfo   {
  @JsonProperty("configKey")
  private String configKey = null;

  @JsonProperty("configValue")
  private String configValue = null;

  @JsonProperty("configDesc")
  private String configDesc = null;

  @JsonProperty("tableName")
  private String tableName = null;

  public ConsoleConfigInfo configKey(String configKey) {
    this.configKey = configKey;
    return this;
  }

  /**
   * 配置项代码
   * @return configKey
  **/
  @ApiModelProperty(value = "配置项代码")
  
    public String getConfigKey() {
    return configKey;
  }

  public void setConfigKey(String configKey) {
    this.configKey = configKey;
  }

  public ConsoleConfigInfo configValue(String configValue) {
    this.configValue = configValue;
    return this;
  }

  /**
   * 配置项值
   * @return configValue
  **/
  @ApiModelProperty(value = "配置项值")
  
    public String getConfigValue() {
    return configValue;
  }

  public void setConfigValue(String configValue) {
    this.configValue = configValue;
  }

  public ConsoleConfigInfo configDesc(String configDesc) {
    this.configDesc = configDesc;
    return this;
  }

  /**
   * 配置项描述
   * @return configDesc
  **/
  @ApiModelProperty(value = "配置项描述")
  
    public String getConfigDesc() {
    return configDesc;
  }

  public void setConfigDesc(String configDesc) {
    this.configDesc = configDesc;
  }

  public ConsoleConfigInfo tableName(String tableName) {
    this.tableName = tableName;
    return this;
  }

  /**
   * 配置表名
   * @return tableName
  **/
  @ApiModelProperty(value = "配置表名")
  
    public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConsoleConfigInfo consoleConfigInfo = (ConsoleConfigInfo) o;
    return Objects.equals(this.configKey, consoleConfigInfo.configKey) &&
        Objects.equals(this.configValue, consoleConfigInfo.configValue) &&
        Objects.equals(this.configDesc, consoleConfigInfo.configDesc) &&
        Objects.equals(this.tableName, consoleConfigInfo.tableName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(configKey, configValue, configDesc, tableName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConsoleConfigInfo {\n");
    
    sb.append("    configKey: ").append(toIndentedString(configKey)).append("\n");
    sb.append("    configValue: ").append(toIndentedString(configValue)).append("\n");
    sb.append("    configDesc: ").append(toIndentedString(configDesc)).append("\n");
    sb.append("    tableName: ").append(toIndentedString(tableName)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
