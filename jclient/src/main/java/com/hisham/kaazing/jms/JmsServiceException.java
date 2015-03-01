package com.hisham.kaazing.jms;

public class JmsServiceException extends RuntimeException {
    public JmsServiceException(String message, Exception exception) {
        super(message, exception);
    }
}
