package com.wisedu.minos.casp.portal.model.v311beta2;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 *
 * 多站点
 * @date 2021/11/29 18:58
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Data
@Accessors(chain = true)
public class MultiSitesDto {
    private String wid;
    private String siteName;
    private String siteRoute;
    private String languageKey;
    private Integer isMaster;
    private Integer orderIndex;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiSitesDto that = (MultiSitesDto) o;
        return getWid().equals(that.getWid()) &&
                getSiteName().equals(that.getSiteName()) &&
                getSiteRoute().equals(that.getSiteRoute());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWid(), getSiteName(), getSiteRoute());
    }
}
