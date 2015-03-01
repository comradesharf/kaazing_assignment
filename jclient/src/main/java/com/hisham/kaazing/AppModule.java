package com.hisham.kaazing;

import com.hisham.kaazing.jms.JmsService;
import com.hisham.kaazing.jms.JmsServiceImpl;
import com.hisham.kaazing.topic.TopicModule;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(injects = App.class, includes = TopicModule.class)
public class AppModule {
    @Provides @Singleton JmsService provideJmsService() {
        return new JmsServiceImpl();
    }
}
