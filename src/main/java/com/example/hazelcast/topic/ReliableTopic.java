package com.example.hazelcast.topic;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;

import java.util.Date;

/**
 * @author w97766
 * @date 2021/6/30
 */
public class ReliableTopic {
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        ITopic<Date> topic = hz.getReliableTopic("reliable-topic");
        topic.publish(new Date());
        System.out.println("Published");
    }
}
