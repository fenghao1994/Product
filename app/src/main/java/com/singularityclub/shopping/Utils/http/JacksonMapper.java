package com.singularityclub.shopping.Utils.http;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.Map;

/**
 * json转换
 * Created by fenghao on 2015/6/27.
 */
public class JacksonMapper {
    private static ObjectMapper mapper = null;
    static{
        mapper = new ObjectMapper();
    }

    /**
     * 获取直接对象
     * @return
     */
    public static ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * 解析成Map
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parse(String jsonStr) {
        Map<String, Object> result = null;
        try {
            result = (Map<String, Object>)mapper.readValue(jsonStr, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析成指定对象
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(String jsonStr, Class<?> T) {
        T result = null;
        try {
            result = (T)mapper.readValue(jsonStr, T);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析成指定对象，带泛型
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseToList(String jsonStr, TypeReference<T> reference) {
        T result = null;
        try {
            result = (T)mapper.readValue(jsonStr, reference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
