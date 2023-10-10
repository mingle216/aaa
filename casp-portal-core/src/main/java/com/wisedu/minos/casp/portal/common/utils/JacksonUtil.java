package com.wisedu.minos.casp.portal.common.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.StringWriter;

/**
 * 功能描述：基于Jackson的Json工具
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title JacksonUtil
 * @Author: jcx
 * @Date: 2020/7/29
 */
public class JacksonUtil {
    private static Logger logger = LogManager.getLogger(JacksonUtil.class);

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        // 将null值不序列化
        // objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        /* 将null值转换为空串 */
        mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException, JsonProcessingException {
                gen.writeString("");
            }
        });
    }

    /**
     * 对象转换成JSON
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        StringWriter sw = new StringWriter();
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createGenerator(sw);
            mapper.writeValue(gen, obj);
        } catch (IOException e) {
            logger.error("对象转换成JSON执行错误", e);
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException e) {
                    logger.error("对象转换成JSON执行错误", e);
                }
            }
        }

        return sw.toString();
    }

    /**
     * 将 JSON 字符串转化为 Java 对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        T object = null;
        try {
            object = mapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("将 JSON 字符串转化为 Java 对象执行错误", e);
        }
        return object;
    }
}
