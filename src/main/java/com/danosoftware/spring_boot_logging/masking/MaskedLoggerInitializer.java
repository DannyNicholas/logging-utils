package com.danosoftware.spring_boot_logging.masking;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import io.micrometer.common.util.StringUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * Configures logback for a logger that will mask sensitive fields.
 * <p>
 * This is a utility class, so must be called manually if log masking is required.
 */
@Slf4j
@UtilityClass
public class MaskedLoggerInitializer {

    private final static String DEFAULT_LOGGING_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{36} - %msg%n";

    /**
     * Initialise the customised masking logger.
     * <p>
     * Typically, this is called during the application's @PostConstruct phase.
     */
    public static void init() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);

        // 1. Create the masking layout
        final String defaultLoggingPattern = loggingPattern(rootLogger);
        MaskingLayout maskingLayout = new MaskingLayout(defaultLoggingPattern);
        maskingLayout.setContext(context);
        maskingLayout.start();

        // 2. Create an encoder that wraps the layout
        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(context);
        encoder.setLayout(maskingLayout);
        encoder.start();

        // 3. Create a console appender that uses the encoder
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(context);
        appender.setName("MASKING_CONSOLE");
        appender.setEncoder(encoder);
        appender.start();

        // 4. Attach the appender to the root logger
        rootLogger.detachAndStopAllAppenders(); // clears default appenders
        rootLogger.addAppender(appender);
    }

    /**
     * We want our customised logger to re-use the application's existing logging pattern.
     * This is provided to the application at run-time.
     * Typically, this comes from the application's "logback-spring.xml" file (in resources folder).
     * <p>
     * We will iterate through all existing appenders and return the logging pattern.
     * <p>
     * If not found, fall-back to a default pattern.
     *
     * @param rootLogger - existing root logger holding our logging appenders
     * @return the found or default logging pattern
     */
    private static String loggingPattern(Logger rootLogger) {
        for (Iterator<Appender<ILoggingEvent>> it = rootLogger.iteratorForAppenders(); it.hasNext(); ) {
            Appender<ILoggingEvent> appender = it.next();
            if (appender instanceof ConsoleAppender<?> consoleAppender) {
                if (consoleAppender.getEncoder() instanceof LayoutWrappingEncoder<?> encoder) {
                    Layout<?> layout = encoder.getLayout();
                    if (layout instanceof PatternLayout) {
                        final String loggingPattern = ((PatternLayout) layout).getPattern();
                        if (StringUtils.isNotEmpty(loggingPattern)) {
                            log.info("Found logging pattern: {}", loggingPattern);
                            return loggingPattern;
                        }
                    }
                }
            }
        }
        log.info("Falling back to default logging pattern: {}", DEFAULT_LOGGING_PATTERN);
        return DEFAULT_LOGGING_PATTERN;
    }
}
