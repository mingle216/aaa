package com.wisedu.amp.card.cus.allserviceitem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceItemCategoryResponse extends AmpBaseResponse {

    List<ServiceItemCategory> data;

}
