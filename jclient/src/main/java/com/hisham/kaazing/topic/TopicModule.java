package com.hisham.kaazing.topic;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(library = true, complete = false)
public class TopicModule {
    @Provides @Singleton TopicService provideTopicService() {
        return new JsonTopicService();
    }
}
