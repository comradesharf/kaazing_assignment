package com.hisham.kaazing.topic;

public class TopicServiceException extends RuntimeException {
    public TopicServiceException(String message, Exception exception) {
        super(message, exception);
    }
}
