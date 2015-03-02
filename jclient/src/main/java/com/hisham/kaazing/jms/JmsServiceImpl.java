package com.hisham.kaazing.jms;

import com.hisham.kaazing.topic.JsonTopicService;
import com.hisham.kaazing.topic.TopicService;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

import static com.hisham.kaazing.topic.Subscription.News;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static org.slf4j.LoggerFactory.getLogger;

public class JmsServiceImpl implements JmsService {

    public static final Logger LOGGER = getLogger(JmsServiceImpl.class);

    TopicService topicService = new JsonTopicService();

    private Optional<InitialContext> sharedContext;
    private Optional<Connection> sharedConnection;
    private Optional<Session> sharedSession;

    @PostConstruct private void setUp() {
        launchService();
    }

    @Override public void launchService() {
        try {
            setContext();
            sharedContext.ifPresent(context -> {
                try {
                    ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
                    Connection connection = connectionFactory.createConnection(null, null);
                    connection.setExceptionListener(it -> LOGGER.error("JMS connection exception: ", it));
                    connection.start();
                    sharedConnection = Optional.of(connection);
                    sharedConnection.ifPresent(it ->
                            {
                                try {
                                    sharedSession = Optional.ofNullable(it.createSession(false, AUTO_ACKNOWLEDGE));
                                } catch (JMSException exception) {
                                    throw new JmsServiceException("Failed to create JMS session: ", exception);
                                }
                            }
                    );
                } catch (NamingException exception) {
                    throw new JmsServiceException("Failed to lookup JMS connection factory: ", exception);
                } catch (JMSException exception) {
                    throw new JmsServiceException("Failed to create JMS connection: ", exception);
                }
            });
        } catch (NamingException exception) {
            throw new JmsServiceException("Failed to create initial context: ", exception);
        }
    }

    private void setContext() throws NamingException {
        Properties properties = new Properties();
        properties.put(InitialContext.INITIAL_CONTEXT_FACTORY, "com.kaazing.gateway.jms.client.JmsInitialContextFactory");
        properties.put(Context.PROVIDER_URL, "ws://localhost:8001/jms");
        sharedContext = Optional.ofNullable(new InitialContext(properties));
    }

    @Override public void closeService() {
        sharedConnection.ifPresent((connection) -> {
            try {
                connection.close();
            } catch (JMSException exception) {
                throw new JmsServiceException("Failed to stop JMS service", exception);
            }
        });

    }

    @Override public void publishTopics() {
        topicService.getSubscription().ifPresent(subscription ->
                sharedSession.ifPresent(session ->
                        subscription.getNews().forEach(news ->
                                publishTopic(session, news))));
    }

    private void publishTopic(Session session, News news) {
        ScheduledExecutorService scheduler = newSingleThreadScheduledExecutor();
        String topicName = "/topic/" + news.getTopic();
        try {
            Topic topic = (Topic) sharedContext.get().lookup(topicName);
            MessageProducer producer = session.createProducer(topic);
            scheduler.scheduleAtFixedRate(() -> sendHeadline(session, producer, news), 0, 1, SECONDS);
        } catch (NamingException exception) {
            throw new JmsServiceException(
                    "Failed to lookup topic name " + topicName + ": ", exception);
        } catch (JMSException exception) {
            throw new JmsServiceException(
                    "Failed to create producer for topic " + topicName + ": ", exception);
        }

    }

    private void sendHeadline(Session session, MessageProducer producer, News news) {
        String headline = "";
        try {
            int index = new Random().nextInt(news.getHeadlines().size());
            headline = news.getHeadlines().get(index);
            Message message = session.createTextMessage(headline);
            producer.send(message);
        } catch (JMSException exception) {
            throw new JmsServiceException("Failed to create text message " + headline + ": ", exception);
        }
    }

    @PreDestroy private void tearDown() {
        closeService();
    }
}
