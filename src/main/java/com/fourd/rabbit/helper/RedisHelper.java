package com.fourd.rabbit.helper;

public class RedisHelper {
    public static String generateKey(String teacher,String functionName){
        return teacher + "::" + functionName;
    }
}
