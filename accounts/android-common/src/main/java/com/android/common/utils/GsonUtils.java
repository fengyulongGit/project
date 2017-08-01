package com.android.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by fengyulong on 2016/7/22.
 */
public class GsonUtils {

    public static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(Integer.class, new IntegerDeserializer());
        gsonBuilder.registerTypeHierarchyAdapter(Double.class, new DoubleDeserializer());
        gsonBuilder.registerTypeHierarchyAdapter(Float.class, new FloatDeserializer());
        return gsonBuilder.create();
    }

    public static <T> T fromJson(final String result, Class<T> clazz) {
        Gson gson = createGson();
        T t = null;
        try {
            t = gson.fromJson(result, clazz);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static String toJson(Object object) {
        Gson json = createGson();
        return json.toJson(object);
    }

    private static class IntegerDeserializer implements JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            String string = arg0.getAsString();
            if (string.length() > 0) {
                return Integer.parseInt(string);
            } else {
                return 0;
            }
        }
    }

    private static class DoubleDeserializer implements JsonDeserializer<Double> {

        @Override
        public Double deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            String string = arg0.getAsString();
            if (string.length() > 0) {
                return Double.parseDouble(string);
            } else {
                return 0.0;
            }
        }
    }

    private static class FloatDeserializer implements JsonDeserializer<Float> {
        @Override
        public Float deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            String string = arg0.getAsString();
            if (string.length() > 0) {
                return Float.parseFloat(string);
            } else {
                return 0f;
            }
        }
    }
}
