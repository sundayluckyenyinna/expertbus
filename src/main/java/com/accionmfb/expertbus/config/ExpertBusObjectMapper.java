package com.accionmfb.expertbus.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExpertBusObjectMapper {

    private static final ObjectMapper OBJECT_MAPPER = getJacksonObjectMapper();

    public String stringify(Object object){
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.info("Exception occurred while trying to stringify object: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String prettyStringify(Object object){
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.info("Exception occurred while trying to stringify object: {}", e.getMessage());
            return "";
        }
    }

    public <T> T objectify(String json, Class<T> tClass){
        try {
            return OBJECT_MAPPER.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            log.info("Exception occurred while trying to parse json: {}", e.getMessage());
            return null;
        }
    }

    public Object objectify(String json){
        try {
            return OBJECT_MAPPER.readValue(json, Object.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private static ObjectMapper getJacksonObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper;
    }
}
