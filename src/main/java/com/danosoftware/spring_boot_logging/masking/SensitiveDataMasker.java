package com.danosoftware.spring_boot_logging.masking;

import java.lang.reflect.Field;
import java.util.*;

public class SensitiveDataMasker {

    private static final String MASK = "***";

    public static String mask(Object object) {
        return mask(object, new IdentityHashMap<>());
    }

    private static String mask(Object object, Map<Object, Boolean> visited) {
        if (object == null) {
            return "null";
        }

        // Prevent infinite loops for cyclic references
        if (visited.containsKey(object)) {
            return "...(circular reference)...";
        }
        visited.put(object, true);

        Class<?> clazz = object.getClass();

        if (isPrimitiveOrWrapper(clazz) || object instanceof String || object instanceof Number || object instanceof Boolean) {
            return object.toString();
        }

        if (object instanceof Collection<?> collection) {
            return maskCollection(collection, visited).toString();
        }

        if (clazz.isArray()) {
            Object[] array = (Object[]) object;
            return maskArray(array, visited).toString();
        }

        if (object instanceof Map<?, ?> map) {
            return maskMap(map, visited).toString();
        }

        return maskObjectFields(object, visited).toString();
    }

    private static Map<String, Object> maskObjectFields(Object object, Map<Object, Boolean> visited) {
        Map<String, Object> maskedFields = new LinkedHashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(object);
                if (field.isAnnotationPresent(Sensitive.class)) {
                    maskedFields.put(field.getName(), MASK);
                } else {
                    maskedFields.put(field.getName(), mask(fieldValue, visited));
                }
            } catch (IllegalAccessException e) {
                maskedFields.put(field.getName(), "ERROR");
            }
        }

        return maskedFields;
    }

    private static Collection<Object> maskCollection(Collection<?> collection, Map<Object, Boolean> visited) {
        Collection<Object> masked = new ArrayList<>();
        for (Object item : collection) {
            masked.add(mask(item, visited));
        }
        return masked;
    }

    private static Collection<Object> maskArray(Object[] array, Map<Object, Boolean> visited) {
        Collection<Object> masked = new ArrayList<>();
        for (Object item : array) {
            masked.add(mask(item, visited));
        }
        return masked;
    }

    private static Map<Object, Object> maskMap(Map<?, ?> map, Map<Object, Boolean> visited) {
        Map<Object, Object> masked = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            masked.put(mask(entry.getKey(), visited), mask(entry.getValue(), visited));
        }
        return masked;
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
                || type == Double.class
                || type == String.class;
    }
}
