package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class MinosSearchForPortalRequest {
    @JsonProperty("userId")
    private String userId = null;

    @JsonProperty("userWid")
    private String userWid = null;

    @JsonProperty("searchKey")
    private String searchKey = null;

    @JsonProperty("lang")
    private String lang = null;

    @JsonProperty("serviceStation")
    private Integer serviceStation = null;

    @JsonProperty("pageSize")
    private Integer pageSize = null;

    @JsonProperty("groupWid")
    private String groupWid = null;

    @JsonProperty("sortType")
    private String sortType = null;

    @JsonProperty("authorityEnabled")
    private Integer authorityEnabled = null;

    public MinosSearchForPortalRequest userId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 用户id(账号)
     * @return userId
     **/
    @ApiModelProperty(value = "用户id(账号)")

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MinosSearchForPortalRequest userWid(String userWid) {
        this.userWid = userWid;
        return this;
    }

    /**
     * 用户wid
     * @return userWid
     **/
    @ApiModelProperty(value = "用户wid")

    public String getUserWid() {
        return userWid;
    }

    public void setUserWid(String userWid) {
        this.userWid = userWid;
    }

    public MinosSearchForPortalRequest searchKey(String searchKey) {
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

    public MinosSearchForPortalRequest lang(String lang) {
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

    public MinosSearchForPortalRequest serviceStation(Integer serviceStation) {
        this.serviceStation = serviceStation;
        return this;
    }

    /**
     * 平台类型 0PC 1移动 2PC和移动
     * @return serviceStation
     **/
    @ApiModelProperty(value = "平台类型 0PC 1移动 2PC和移动")

    public Integer getServiceStation() {
        return serviceStation;
    }

    public void setServiceStation(Integer serviceStation) {
        this.serviceStation = serviceStation;
    }

    public MinosSearchForPortalRequest pageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 是否开启权限判断
     * @return isAuthorized
     **/
    @ApiModelProperty(value = "是否开启权限判断")
    public Integer getAuthorityEnabled() {
        return authorityEnabled;
    }

    public void setAuthorityEnabled(Integer authorityEnabled) {
        this.authorityEnabled = authorityEnabled;
    }

    /**
     * 分页数
     * @return pageSize
     **/
    @ApiModelProperty(value = "分页数")

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getGroupWid() {
        return groupWid;
    }

    public void setGroupWid(String groupWid) {
        this.groupWid = groupWid;
    }


    /**
     * 新闻排序方式（1-按匹配度 2-按时间）
     * @return sortType
     **/
    @ApiModelProperty(value = "新闻排序方式")
    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MinosSearchForPortalRequest minosSearchForPortalRequest = (MinosSearchForPortalRequest) o;
        return Objects.equals(this.userId, minosSearchForPortalRequest.userId) &&
                Objects.equals(this.userWid, minosSearchForPortalRequest.userWid) &&
                Objects.equals(this.searchKey, minosSearchForPortalRequest.searchKey) &&
                Objects.equals(this.lang, minosSearchForPortalRequest.lang) &&
                Objects.equals(this.serviceStation, minosSearchForPortalRequest.serviceStation) &&
                Objects.equals(this.pageSize, minosSearchForPortalRequest.pageSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userWid, searchKey, lang, serviceStation, pageSize);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MinosSearchForPortalRequest {\n");

        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    userWid: ").append(toIndentedString(userWid)).append("\n");
        sb.append("    searchKey: ").append(toIndentedString(searchKey)).append("\n");
        sb.append("    lang: ").append(toIndentedString(lang)).append("\n");
        sb.append("    serviceStation: ").append(toIndentedString(serviceStation)).append("\n");
        sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
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
