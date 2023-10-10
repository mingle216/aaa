package com.wisedu.amp.card.cus.cucRecommendapp.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title QueryAllAppResponse
 * @Author: hlchen02
 * @Date: 2020/9/8
 */
@Getter
@Setter
public class QueryAllAppResponse extends AmpBaseResponse implements Serializable {
    private List<AppInfoBiz> data;

    private Integer pageNum;

    private Integer pageSize;

    private Integer total;

    private String searchValue;

}
