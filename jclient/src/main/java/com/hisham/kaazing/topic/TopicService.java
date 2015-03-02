package com.hisham.kaazing.topic;

import java.util.Optional;

public interface TopicService {
    Optional<Subscription> getSubscription();
}
