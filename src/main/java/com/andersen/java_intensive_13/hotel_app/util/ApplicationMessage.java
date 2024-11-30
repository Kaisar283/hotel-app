package com.andersen.java_intensive_13.hotel_app.util;

public class ApplicationMessage {
    public static <T extends Number> String notFoundById(T entityId, String entityName){
        return String.format("%s with id %s is not found.",
                entityName,String.valueOf(entityId));
    }

}
