package com.greeapp.Assistant;

import com.google.gson.Gson;


public class JsonUtil {

    private static Gson gson = buildGson();

    private static Gson buildGson() {
        Gson gson = new Gson();
        return gson;
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <T>T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

}
