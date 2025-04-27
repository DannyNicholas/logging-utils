package com.danosoftware.spring_boot_logging.masking;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SensitiveDataMaskerOld {

    public static String mask(Object object) {
        if (object == null) {
            return "null";
        }

        if (isPrimitiveOrWrapper(object.getClass()) || object instanceof String) {
            return object.toString();
        }

        try {
            Map<String, Object> maskedFields = new HashMap<>();
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(object);

                if (field.isAnnotationPresent(Sensitive.class)) {
                    maskedFields.put(field.getName(), "***");
                } else {
                    maskedFields.put(field.getName(), value);
                }
            }
            return maskedFields.toString();
        } catch (Exception e) {
            return object.toString(); // fallback if anything fails
        }
    }

    private static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive()
                || type == Boolean.class
                || type == Byte.class
                || type == Character.class
                || type == Short.class
                || type == Integer.class
                || type == Long.class
                || type == Float.class
                || type == Double.class;
    }
}
