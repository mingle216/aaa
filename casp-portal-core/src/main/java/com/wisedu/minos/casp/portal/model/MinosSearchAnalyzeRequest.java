package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class MinosSearchAnalyzeRequest {
    @JsonProperty("searchKey")
    private String searchKey = null;

    @JsonProperty("lang")
    private String lang = null;

    public MinosSearchAnalyzeRequest searchKey(String searchKey) {
        this.searchKey = searchKey;
        return this;
    }

    /**
     * 搜索关键词
     * @return searchKey
     **/
    @ApiModelProperty(value = "搜索关键词")

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public MinosSearchAnalyzeRequest lang(String lang) {
        this.lang = lang;
        return this;
    }

    /**
     * 多语言
     * @return lang
     **/
    @ApiModelProperty(value = "多语言")

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MinosSearchAnalyzeRequest minosSearchAnalyzeRequest = (MinosSearchAnalyzeRequest) o;
        return Objects.equals(this.searchKey, minosSearchAnalyzeRequest.searchKey) &&
                Objects.equals(this.lang, minosSearchAnalyzeRequest.lang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchKey, lang);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MinosSearchAnalyzeRequest {\n");

        sb.append("    searchKey: ").append(toIndentedString(searchKey)).append("\n");
        sb.append("    lang: ").append(toIndentedString(lang)).append("\n");
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
