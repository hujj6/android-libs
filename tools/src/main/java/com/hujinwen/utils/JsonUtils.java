package com.hujinwen.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * Created by hu-jinwen on 2020/4/8
 * <p>
 * json相关的工具类
 */
public class JsonUtils {

    private static final ObjectMapper OM = new ObjectMapper();

    public static ObjectNode newObjectNode() {
        return OM.createObjectNode();
    }

    public static ArrayNode newArrayNode() {
        return OM.createArrayNode();
    }

    public static String toString(Object obj) throws JsonProcessingException {
        return OM.writeValueAsString(obj);
    }

    public static JsonNode toJsonNode(String str) throws IOException {
        return OM.readTree(str);
    }

    public static <T> T toObj(String str, Class<T> collectionClass, Class<?>... elementClasses) throws IOException {
        JavaType javaType = OM.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        return OM.readValue(str, javaType);
    }

    public static <T> T toObj(String content, TypeReference<T> valueTypeRef) throws IOException {
        return OM.readValue(content, valueTypeRef);
    }

    public static <T> T toObj(JsonNode node, Class<T> collectionClass, Class<?>... elementClasses) throws IOException {
        return toObj(toString(node), collectionClass, elementClasses);
    }

    public static <T> T toObj(String str, Class<T> clazz) throws IOException {
        return OM.readValue(str, clazz);
    }

    public static <T> T toObj(JsonNode node, Class<T> clazz) throws IOException {
        return OM.readValue(node.traverse(), clazz);
    }

}
