package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @ClassName SchoolCalendarModel
 * @Description
 * @Date 2021/4/7 19:03
 * @Author zkpu
 * @Version 1.0
 **/
@Data
public class SchoolCalendarModel implements Serializable {

    private String xn;

    private String xq;

    private int weekNumber;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate begDate;
}
