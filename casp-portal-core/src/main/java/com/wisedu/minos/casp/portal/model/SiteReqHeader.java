package com.wisedu.minos.casp.portal.model;

import lombok.Data;

import java.util.List;

/**
 * @author 01319098
 */
@Data
public class SiteReqHeader {

  private boolean isSysAdmin;

  private String userWid;

  private List<String> roleWids;

}
