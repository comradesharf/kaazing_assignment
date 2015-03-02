package com.hisham.kaazing.topic;

import java.util.List;

public class Subscription {

    private List<News> news;

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public static class News {
        private String topic;
        private List<String> headlines;

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public List<String> getHeadlines() {
            return headlines;
        }

        public void setHeadlines(List<String> headlines) {
            this.headlines = headlines;
        }
    }
}
