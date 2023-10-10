package com.wisedu.minos.casp.portal.model.v311beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SearchObject
 */
@Validated

public class SearchObject   {
  /**
   * 与前一个条件的连接关系
   */
  public enum ConditionEnum {
    AND("and"),
    
    OR("or");

    private String value;

    ConditionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ConditionEnum fromValue(String text) {
      for (ConditionEnum b : ConditionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("condition")
  private ConditionEnum condition = null;

  /**
   * Gets or Sets comparator
   */
  public enum ComparatorEnum {
    NOT("not"),
    
    ALIKE("alike"),
    
    ILIKE("ilike"),
    
    LIKE("like"),
    
    EQ("eq"),
    
    GE("ge"),
    
    LE("le"),
    
    GT("gt"),
    
    LT("lt"),

    NOT_IN("not_in"),
    
    IN("in"),
    
    NOTIN("notIn"),
    
    BETWEEN("between");

    private String value;

    ComparatorEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ComparatorEnum fromValue(String text) {
      for (ComparatorEnum b : ComparatorEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("comparator")
  private ComparatorEnum comparator = null;

  @JsonProperty("field")
  private String field = null;

  @JsonProperty("value")
  private Object value = null;

  @JsonProperty("mark")
  private String mark = null;

  public SearchObject condition(ConditionEnum condition) {
    this.condition = condition;
    return this;
  }

  /**
   * 与前一个条件的连接关系
   * @return condition
  **/
  @ApiModelProperty(value = "与前一个条件的连接关系")
  
    public ConditionEnum getCondition() {
    return condition;
  }

  public void setCondition(ConditionEnum condition) {
    this.condition = condition;
  }

  public SearchObject comparator(ComparatorEnum comparator) {
    this.comparator = comparator;
    return this;
  }

  /**
   * Get comparator
   * @return comparator
  **/
  @ApiModelProperty(value = "")
  
    public ComparatorEnum getComparator() {
    return comparator;
  }

  public void setComparator(ComparatorEnum comparator) {
    this.comparator = comparator;
  }

  public SearchObject field(String field) {
    this.field = field;
    return this;
  }

  /**
   * Get field
   * @return field
  **/
  @ApiModelProperty(value = "")
  
    public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public SearchObject value(Object value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
  **/
  @ApiModelProperty(value = "")
  
    public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public SearchObject mark(String mark) {
    this.mark = mark;
    return this;
  }

  /**
   * Get mark
   * @return mark
  **/
  @ApiModelProperty(value = "")
  
    public String getMark() {
    return mark;
  }

  public void setMark(String mark) {
    this.mark = mark;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchObject searchObject = (SearchObject) o;
    return Objects.equals(this.condition, searchObject.condition) &&
        Objects.equals(this.comparator, searchObject.comparator) &&
        Objects.equals(this.field, searchObject.field) &&
        Objects.equals(this.value, searchObject.value) &&
        Objects.equals(this.mark, searchObject.mark);
  }

  @Override
  public int hashCode() {
    return Objects.hash(condition, comparator, field, value, mark);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchObject {\n");
    
    sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
    sb.append("    comparator: ").append(toIndentedString(comparator)).append("\n");
    sb.append("    field: ").append(toIndentedString(field)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    mark: ").append(toIndentedString(mark)).append("\n");
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
