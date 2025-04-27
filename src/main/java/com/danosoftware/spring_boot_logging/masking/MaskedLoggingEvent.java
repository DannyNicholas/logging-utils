package com.danosoftware.spring_boot_logging.masking;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.experimental.Delegate;

/**
 * Masked logging event created from an original logging event.
 * <p>
 * The majority of method calls are delegated to the original logging event.
 * <p>
 * Calls to retrieve the logging message are handled by this class,
 * which allows it to return a masked version of the original message.
 */
public class MaskedLoggingEvent implements ILoggingEvent {

    @Delegate
    private final ILoggingEvent original;
    private final String newMessage;

    public MaskedLoggingEvent(ILoggingEvent original, String newMessage) {
        this.original = original;
        this.newMessage = newMessage;
    }

    @Override
    public String getMessage() {
        return newMessage;
    }

    @Override
    public String getFormattedMessage() {
        return newMessage;
    }
}

