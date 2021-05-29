package com.publicis.sapient.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.publicis.sapient.model.Error;
import com.publicis.sapient.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class ObjectMapperUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> ApiResponse<T> parseResponse(String response, Class<?> clazz) {
        ApiResponse res = new ApiResponse<>();
        List<T> objects;
        try {
            TypeFactory typeFactory = TypeFactory.defaultInstance();
            objects = OBJECT_MAPPER.readValue(response, typeFactory.constructCollectionType(ArrayList.class, clazz));
            res.setData(objects);
        } catch (JsonProcessingException e) {
            log.error("Got the error response :  {}", e);
            try {
                Error error = OBJECT_MAPPER.readValue(response, Error.class);
                res.setErrorResponse(true);
                res.setError(error);
            } catch (JsonProcessingException jsonProcessingException) {
                res.setErrorResponse(true);
                log.error("Error while parsing the json response");
            }
        }
        return res;
    }

}
