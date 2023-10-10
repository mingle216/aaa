package com.wisedu.minos.casp.portal.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.beetl.core.Format;
import org.springframework.stereotype.Component;

@Component
public class ScriptDefenseFormat implements Format {


    @Override
    public Object format(Object data, String pattern) {
        if (null == data) {
            return null;
        } else {
            return ((String)data).replaceAll("<script>","").replaceAll("</script>","");
        }
    }

}
