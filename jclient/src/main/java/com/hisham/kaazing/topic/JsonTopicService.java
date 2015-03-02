package com.hisham.kaazing.topic;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Paths.get;

public class JsonTopicService implements TopicService {

    @Override public Optional<Subscription> getSubscription() {
        try (BufferedReader reader = newBufferedReader(get("topics.json"), UTF_8)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Subscription subscription = objectMapper.readValue(reader, Subscription.class);
            return Optional.ofNullable(subscription);
        } catch (IOException e) {
            throw new TopicServiceException("Failed to read external file: ", e);
        }
    }
}
