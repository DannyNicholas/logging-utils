package com.danosoftware.spring_boot_logging.masking;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.*;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;

public class MaskingLayout extends LayoutBase<ILoggingEvent> {

    private PatternLayout delegateLayout;

    @Override
    public void start() {
        delegateLayout = new PatternLayout();
        delegateLayout.setContext((Context) getContext());

        // Use the default Spring Boot Logback pattern
        String defaultPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{36} - %msg%n";
        delegateLayout.setPattern(defaultPattern);

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        for (Iterator<Appender<ILoggingEvent>> it = rootLogger.iteratorForAppenders(); it.hasNext(); ) {
            Appender<ILoggingEvent> appender = it.next();
            if (appender instanceof ConsoleAppender) {
                LayoutWrappingEncoder<ILoggingEvent> encoder = (LayoutWrappingEncoder<ILoggingEvent>) ((ConsoleAppender<?>) appender).getEncoder();
                Layout<?> layout = encoder.getLayout();
                if (layout instanceof PatternLayout) {
                    String pattern = ((PatternLayout) layout).getPattern();
                    delegateLayout.setPattern(pattern);
                }
            }
        }

        // You could make this configurable via a setter if you want
//        delegateLayout.setPattern(defaultPattern);

        delegateLayout.start();
        super.start();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        String message = event.getMessage();

        Object[] args = event.getArgumentArray();
        if (args != null && args.length > 0) {
            Object[] maskedArgs = Arrays.stream(args)
                    .map(SensitiveDataMasker::mask)
                    .toArray();

            // Substitute {} with masked arguments
            message = substitutePlaceholders(message, maskedArgs);

            return delegateLayout.doLayout(new MaskedLoggingEvent(event, message));
        }

//        return message + System.lineSeparator();

        // Step 2: Delegate normal log if no args
        return delegateLayout.doLayout(event);
    }

    private String substitutePlaceholders(String message, Object[] args) {
        for (Object arg : args) {
            message = message.replaceFirst("\\{\\}", arg != null ? arg.toString() : "null");
        }
        return message;
    }
}
