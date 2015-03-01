package com.hisham.kaazing;

import com.hisham.kaazing.jms.JmsService;
import com.hisham.kaazing.jms.JmsServiceException;
import dagger.ObjectGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class App implements Runnable {

    public static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Inject JmsService jmsService;

    public static void main(String[] args) {
        ObjectGraph objectGraph = ObjectGraph.create(new AppModule());
        App app = objectGraph.get(App.class);
        app.run();
    }

    @Override public void run() {
        try {
            jmsService.launchService();
            LOGGER.info("Service started");
            jmsService.publishTopics();
            LOGGER.info("Topics published");
        } catch (JmsServiceException exception) {
            LOGGER.error("Service runtime error: ", exception);
        }
    }
}
