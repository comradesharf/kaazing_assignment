package com.hisham.kaazing.jms;

public interface JmsService {
    void launchService();

    void closeService();

    void publishTopics();
}
