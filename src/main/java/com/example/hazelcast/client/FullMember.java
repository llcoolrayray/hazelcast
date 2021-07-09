package com.example.hazelcast.client;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.BlockingQueue;

/**
 * @author w97766
 * @date 2021/7/6
 */
public class FullMember {
    public static void main(String[] args) throws Exception{
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        System.out.print("Full member up");
        BlockingQueue<String> queue = hz.getQueue("queue");
        while (true) {
            System.out.println(queue.take());
        }
    }
}
