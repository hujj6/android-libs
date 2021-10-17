package com.hujinwen.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Created by hu-jinwen on 2021/10/15
 */
public class News {

    public String[] toStringArray(String jsonArrayStr) throws IOException {
        JsonNode jsonNode = JsonUtils.toJsonNode(jsonArrayStr);
        String[] resultArray = new String[jsonNode.size()];
        for (int i = 0; i < jsonNode.size(); i++) {
            resultArray[i] = jsonNode.path(i).asText();
        }
        return resultArray;
    }

}
