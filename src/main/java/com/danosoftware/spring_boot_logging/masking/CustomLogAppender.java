package com.danosoftware.spring_boot_logging.masking;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.lang.reflect.Field;

public class CustomLogAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent eventObject) {
        Object[] args = eventObject.getArgumentArray();
        Object[] maskedArgs = null;

        if (args != null) {
            maskedArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg != null) {
                    maskedArgs[i] = maskSensitiveFieldsToString(arg);
                }
            }
        }

        // Reformat the message using masked arguments
        String maskedMessage = eventObject.getMessage();
        if (maskedArgs != null && maskedArgs.length > 0) {
            maskedMessage = formatMessage(maskedMessage, maskedArgs);
        }

        System.out.println("[MaskedLog] " + maskedMessage);
    }

    private String maskSensitiveFieldsToString(Object obj) {
        try {
            Class<?> clazz = obj.getClass();
            StringBuilder sb = new StringBuilder();
            sb.append(clazz.getSimpleName()).append("{");

            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                sb.append(field.getName()).append("=");

                Object value = field.get(obj);
                if (field.isAnnotationPresent(Sensitive.class)) {
                    sb.append("***MASKED***");
                } else {
                    sb.append(value);
                }

                if (i < fields.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            return obj.toString(); // fallback if anything fails
        }
    }

    private String formatMessage(String pattern, Object[] args) {
        if (args == null || args.length == 0) {
            return pattern;
        }

        for (Object arg : args) {
            pattern = pattern.replaceFirst("\\{\\}", arg == null ? "null" : arg.toString());
        }
        return pattern;
    }

}
