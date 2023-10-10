package com.wisedu.minos.casp.portal.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

public class JSONLibDataFormatSerializer implements ObjectSerializer {
    public JSONLibDataFormatSerializer() {
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object == null) {
            serializer.out.writeNull();
        } else {
            Date date = (Date)object;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            JSONObject json = new JSONObject();
            json.put("date", calendar.get(Calendar.DATE));
            json.put("day", calendar.get(Calendar.DAY_OF_MONTH));
            json.put("hours", calendar.get(Calendar.HOUR_OF_DAY));
            json.put("minutes", calendar.get(Calendar.MINUTE));
            json.put("month", calendar.get(Calendar.MONTH));
            json.put("seconds", calendar.get(Calendar.SECOND));
            json.put("time", calendar.getTimeInMillis());
            json.put("timezoneOffset", calendar.get(Calendar.ZONE_OFFSET));
            json.put("year", calendar.get(Calendar.YEAR));
            serializer.write(json);
        }
    }
}