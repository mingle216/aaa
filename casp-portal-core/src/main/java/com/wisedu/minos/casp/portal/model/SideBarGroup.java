package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.dao.entity.SidebarEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SideBarGroup {

    List<SidebarEntity> leftSidebarList;

    List<SidebarEntity> rightSidebarList;

}
