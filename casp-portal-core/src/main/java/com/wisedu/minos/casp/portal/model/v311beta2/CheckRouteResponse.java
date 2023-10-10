package com.wisedu.minos.casp.portal.model.v311beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.v311beta2.ModelApiResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CheckRouteResponse
 */
@Validated

public class CheckRouteResponse extends ModelApiResponse  {
  @JsonProperty("checkSuccess")
  private Boolean checkSuccess = null;

  @JsonProperty("checkErrorMsg")
  private String checkErrorMsg = null;

  public CheckRouteResponse checkSuccess(Boolean checkSuccess) {
    this.checkSuccess = checkSuccess;
    return this;
  }

  /**
   * 校验通过返回true，false表示路由非法
   * @return checkSuccess
  **/
  @ApiModelProperty(value = "校验通过返回true，false表示路由非法")
  
    public Boolean isCheckSuccess() {
    return checkSuccess;
  }

  public void setCheckSuccess(Boolean checkSuccess) {
    this.checkSuccess = checkSuccess;
  }

  public CheckRouteResponse checkErrorMsg(String checkErrorMsg) {
    this.checkErrorMsg = checkErrorMsg;
    return this;
  }

  /**
   * 错误信息
   * @return checkErrorMsg
  **/
  @ApiModelProperty(value = "错误信息")
  
    public String getCheckErrorMsg() {
    return checkErrorMsg;
  }

  public void setCheckErrorMsg(String checkErrorMsg) {
    this.checkErrorMsg = checkErrorMsg;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheckRouteResponse checkRouteResponse = (CheckRouteResponse) o;
    return Objects.equals(this.checkSuccess, checkRouteResponse.checkSuccess) &&
        Objects.equals(this.checkErrorMsg, checkRouteResponse.checkErrorMsg) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checkSuccess, checkErrorMsg, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheckRouteResponse {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    checkSuccess: ").append(toIndentedString(checkSuccess)).append("\n");
    sb.append("    checkErrorMsg: ").append(toIndentedString(checkErrorMsg)).append("\n");
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
