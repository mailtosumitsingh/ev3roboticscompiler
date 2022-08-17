package org.menlorobotics.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {
public static String toString(Object obj) throws JsonProcessingException {
	ObjectMapper mapper = new ObjectMapper();
	String jsonInString = mapper.writeValueAsString(obj);
	return jsonInString;
}
public static <T>T toObject(String json, Class<T> cls) throws JsonParseException, JsonMappingException, IOException {
	ObjectMapper mapper = new ObjectMapper();
	T t= mapper.readValue(json, cls);
	return t;
}
}
