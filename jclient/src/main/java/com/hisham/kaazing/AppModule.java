package com.hisham.kaazing;

import com.hisham.kaazing.jms.JmsService;
import com.hisham.kaazing.jms.JmsServiceImpl;
import com.hisham.kaazing.topic.JsonTopicService;
import com.hisham.kaazing.topic.TopicService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(injects = App.class)
public class AppModule {
    @Provides @Singleton public JmsService provideJmsService(JmsServiceImpl service) {
        return service;
    }

    @Provides @Singleton public TopicService provideTopicService() {
        return new JsonTopicService();
    }
}
