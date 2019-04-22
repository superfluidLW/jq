package com.chendu.jq.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class JsonUtils {

    private JsonUtils() {
    }

    private static ObjectMapper objectMapper = null;

    static {
        objectMapper = new ObjectMapper();
        objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                .getSerializerProvider();
        objectMapper.registerModule(new JavaTimeModule());
    }


    private static final Gson gson = new GsonBuilder().setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .serializeNulls()
            .create();


    public static String writeValueAsString(Object object) {
        try {
            if (object != null) {
                if (object instanceof String) {
                    return String.valueOf(object);
                } else {
                    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            JqLog.info("type {} to json message {}", object == null ? "" : object.getClass().getSubName(), writeValueAsString2(object));
                    return objectMapper.writeValueAsString(object);
                }
            }
            return null;
        } catch(JsonProcessingException e){
            JqLog.error("Object To Json error", e);
            return null;
        }
    }

    /**
     * 获取文件中的一个json串
     *
     * @param fileUrl
     * @return String
     */
    public static <T> T readFile(URL fileUrl, Class<T> tClass) {
        try {
            JsonNode jsonNode = objectMapper.readTree(fileUrl);
            return gson.fromJson(jsonNode.toString(), tClass);
        } catch (IOException e) {
            JqLog.error("file not found", e.getMessage());
            return null;
        }
    }

    public static String writeValueAsString2(Object object) {
        try {
            String message = gson.toJson(object);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            JqLog.error("Object To Json error", e);
            return null;
        }
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            JqLog.error("Json To Object error {}", e);
            return null;
        }
    }

    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (Exception e) {
            JqLog.error("Object To Json error {}", e);
            return null;
        }
    }

    public static Object mapToObject(Object object, final Map map) {
        //TODO why use json here?
        return gson.fromJson(writeValueAsString2(map), object.getClass());
//        return readValue(writeValueAsString(map),object.getClass());
    }

    public static Map<String, String> objectToMap(Object obj) throws Exception {
        return (Map<String, String>)obj;
    }


    public static <T> T readValue(String content, Class<?> collectionClass, Class<?>... elementClasses) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
            return objectMapper.readValue(content, javaType);
        } catch (Exception e) {
            JqLog.error("Object To Json error", e);
            return null;
        }
    }
}
