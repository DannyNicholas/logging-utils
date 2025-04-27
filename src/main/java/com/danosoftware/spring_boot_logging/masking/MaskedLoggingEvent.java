package com.danosoftware.spring_boot_logging.masking;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;
import org.slf4j.Marker;
import org.slf4j.event.KeyValuePair;

import java.util.List;
import java.util.Map;

public class MaskedLoggingEvent implements ILoggingEvent {

    private final ILoggingEvent original;
    private final String newMessage;

    public MaskedLoggingEvent(ILoggingEvent original, String newMessage) {
        this.original = original;
        this.newMessage = newMessage;
    }

    @Override
    public String getThreadName() {
        return original.getThreadName();
    }

    @Override
    public Level getLevel() {
        return original.getLevel();
    }

    @Override
    public String getMessage() {
        return newMessage;
    }

    @Override
    public Object[] getArgumentArray() {
        return original.getArgumentArray();
    }

    @Override
    public String getFormattedMessage() {
        return newMessage;
    }

    @Override
    public String getLoggerName() {
        return original.getLoggerName();
    }

    @Override
    public LoggerContextVO getLoggerContextVO() {
        return original.getLoggerContextVO();
    }

    @Override
    public IThrowableProxy getThrowableProxy() {
        return original.getThrowableProxy();
    }

    @Override
    public StackTraceElement[] getCallerData() {
        return original.getCallerData();
    }

    @Override
    public boolean hasCallerData() {
        return original.hasCallerData();
    }

    @Override
    public Marker getMarker() {
        return original.getMarker();
    }

    @Override
    public List<Marker> getMarkerList() {
        return original.getMarkerList();
    }

    @Override
    public Map<String, String> getMDCPropertyMap() {
        return original.getMDCPropertyMap();
    }

    @Override
    public Map<String, String> getMdc() {
        return original.getMdc();
    }

    @Override
    public long getTimeStamp() {
        return original.getTimeStamp();
    }

    @Override
    public int getNanoseconds() {
        return original.getNanoseconds();
    }

    @Override
    public long getSequenceNumber() {
        return original.getSequenceNumber();
    }

    @Override
    public List<KeyValuePair> getKeyValuePairs() {
        return original.getKeyValuePairs();
    }

    @Override
    public void prepareForDeferredProcessing() {
        original.prepareForDeferredProcessing();
    }
}

