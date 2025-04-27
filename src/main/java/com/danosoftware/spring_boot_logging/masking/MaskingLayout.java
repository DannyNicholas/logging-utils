package com.danosoftware.spring_boot_logging.masking;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import lombok.NonNull;

import java.util.Arrays;

public class MaskingLayout extends LayoutBase<ILoggingEvent> {

    // pattern layout delegate holding our logging pattern
    private final PatternLayout delegateLayout;

    public MaskingLayout(@NonNull String loggingPattern) {
        this.delegateLayout = new PatternLayout();
        this.delegateLayout.setPattern(loggingPattern);
    }

    @Override
    public void start() {
        delegateLayout.setContext(getContext());
        delegateLayout.start();
        super.start();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        String message = event.getMessage();

        // mask any sensitive fields found in arguments
        Object[] args = event.getArgumentArray();
        if (args != null && args.length > 0) {
            Object[] maskedArgs = Arrays.stream(args)
                    .map(SensitiveDataMasker::mask)
                    .toArray();

            // Substitute {} with masked arguments
            message = substitutePlaceholders(message, maskedArgs);
            return delegateLayout.doLayout(new MaskedLoggingEvent(event, message));
        }

        // Otherwise, delegate normal log if no args
        return delegateLayout.doLayout(event);
    }

    private String substitutePlaceholders(String message, Object[] args) {
        for (Object arg : args) {
            message = message.replaceFirst("\\{}", arg != null ? arg.toString() : "null");
        }
        return message;
    }
}
