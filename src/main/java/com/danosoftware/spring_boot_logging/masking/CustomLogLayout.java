package com.danosoftware.spring_boot_logging.masking;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

public class CustomLogLayout extends LayoutBase<ILoggingEvent> {

    @Override
    public String doLayout(ILoggingEvent event) {

        String timestamp = String.valueOf(event.getTimeStamp());
        String level = event.getLevel().toString();
        String threadName = event.getThreadName();
        String loggerName = event.getLoggerName();
        String message = event.getFormattedMessage();



        String originalMessage = event.getFormattedMessage();

        // Modify the message: add a prefix
        String modifiedMessage = "[Modified] " + originalMessage;

        // Build the final log line
//        return modifiedMessage + System.lineSeparator();

        return String.format("[%s] [%s] [%s] %s - %s%n",
                timestamp, level, threadName, loggerName, message);
    }
}
