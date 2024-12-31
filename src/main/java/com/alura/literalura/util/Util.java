package com.alura.literalura.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util<T> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convertirDatos(String json, Class<T> clase) {
        try{
            return  objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
