package com.danosoftware.spring_boot_logging.masking;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* org.slf4j.Logger.*(String, Object...)) && args(message, args)")
    public void beforeLogging(JoinPoint joinPoint, String message, Object[] args) {
        log.info("Here!");
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                args[i] = maskSensitiveFields(args[i]);
            }
        }
    }

    @Before("execution(* org.slf4j.Logger.*(String, Object...))")
    public void beforeLogging2(JoinPoint joinPoint, String message, Object[] args) {
        log.info("Here!");
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                args[i] = maskSensitiveFields(args[i]);
            }
        }
    }

    private Object maskSensitiveFields(Object obj) {
        // Skip primitive types and Strings
        if (obj == null || obj.getClass().isPrimitive() || obj instanceof String
                || obj instanceof Number || obj instanceof Boolean) {
            return obj;
        }

        try {
            // Process fields of complex objects
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Sensitive.class)) {
                    field.setAccessible(true);
                    field.set(obj, "********");
                }
            }
        } catch (Exception e) {
            // Just return the original object if we can't process it
        }

        return obj;
    }
}