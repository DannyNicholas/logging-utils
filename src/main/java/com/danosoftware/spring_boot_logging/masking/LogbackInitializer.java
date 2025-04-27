package com.danosoftware.spring_boot_logging.masking;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.slf4j.LoggerFactory;

public class LogbackInitializer {
//    public static void init() {
//        try {
//            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//            JoranConfigurator configurator = new JoranConfigurator();
//            configurator.setContext(context);
//            context.reset();
//
//            configurator.doConfigure(LogbackInitializer.class.getResource("/logback-spring.xml"));
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to configure Logback", e);
//        }
//    }


    public static void init() {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // 1. Create the custom layout
        MaskingLayout maskingLayout = new MaskingLayout();
        maskingLayout.setContext(context);
        maskingLayout.start(); // Important!

        // 2. Create an encoder that wraps the layout
        LayoutWrappingEncoder<ch.qos.logback.classic.spi.ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(context);
        encoder.setLayout(maskingLayout);
        encoder.start();

        // 3. Create a console appender that uses the encoder
        ConsoleAppender<ch.qos.logback.classic.spi.ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(context);
        appender.setName("MASKING_CONSOLE");
        appender.setEncoder(encoder);
        appender.start();

        // 4. Attach the appender to the root logger
        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.detachAndStopAllAppenders(); // optional: clear default ones
        rootLogger.addAppender(appender);

        System.out.println("[LogbackInitializer] Masking console appender initialized.");


//        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//
////        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
////        consoleAppender.setContext(context);
//
//        CustomLogAppender customAppender = new CustomLogAppender();
//        customAppender.setContext(context);
//
//        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
//        encoder.setContext(context);
//
//        CustomLogLayout layout = new CustomLogLayout();
//        layout.setContext(context);
//        layout.start();
//
//        encoder.setLayout(layout);
//        encoder.start();
//
//        customAppender.setEncoder(encoder);
//        customAppender.start();
//
//
//
//
////        CustomLogAppender customAppender = new CustomLogAppender();
////        customAppender.setContext(context);
////        customAppender.setName("MY_CUSTOM_APPENDER");
////        customAppender.start();
//
////        Logger rootLogger = context.getLogger("ROOT");
//        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//        rootLogger.addAppender(consoleAppender);
//


//        // 1. Create your custom appender
//        ConsoleAppender customAppender = new ConsoleAppender();
//        customAppender.setContext(context);
//        customAppender.setName("CUSTOM_CONSOLE");
//
//        // 2. Set layout or encoder
//        LayoutWrappingEncoder encoder = new LayoutWrappingEncoder();
//        encoder.setContext(context);
//
//        TTLLLayout layout = new TTLLLayout();
//        layout.setContext(context);
//        layout.start(); // always start layouts
//
//        encoder.setLayout(layout);
//        encoder.start(); // start encoder
//
//        customAppender.setEncoder(encoder);
//        customAppender.start(); // very important
//
//        // 3. Attach it to the root logger
//        Logger rootLogger = context.getLogger("ROOT");
//        rootLogger.addAppender(customAppender);
//
//        System.out.println("[LogbackInitializer] Custom appender added.");
    }
}