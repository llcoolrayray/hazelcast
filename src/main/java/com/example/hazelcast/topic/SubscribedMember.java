package com.example.hazelcast.topic;

import com.hazelcast.config.Config;
import com.hazelcast.config.ListenerConfig;
import com.hazelcast.config.TopicConfig;
import com.hazelcast.core.*;

import java.util.Date;

/**
 * @author w97766
 * @date 2021/6/29
 */
public class SubscribedMember {
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        ITopic<Date> topic = hz.getTopic("topic");
        topic.addMessageListener(new MessageListenerImpl1());
        topic.addMessageListener(new MessageListenerImpl2());
        System.out.println("Subscribed");
    }

    private static class MessageListenerImpl1 implements MessageListener<Date> {
        @Override
        public void onMessage(Message<Date> m) {
            System.out.println("Received1: " + m.getMessageObject());
        }
    }

    private static class MessageListenerImpl2 implements MessageListener<Date> {
        @Override
        public void onMessage(Message<Date> m) {
            System.out.println("Received2: " + m.getMessageObject());
        }
    }

    /**t
     * 配置 topic
     * setGlobalOrderingEnabled:
     */
    public void config() {
        Config config = new Config();
        TopicConfig topicConfig = new TopicConfig();
        topicConfig.setName("topic");
        topicConfig.setGlobalOrderingEnabled(true);
        topicConfig.setGlobalOrderingEnabled(true);
        MessageListener<String> listener = m -> {};
        topicConfig.addMessageListenerConfig(new ListenerConfig(listener));
        config.addTopicConfig(topicConfig);
    }
}
