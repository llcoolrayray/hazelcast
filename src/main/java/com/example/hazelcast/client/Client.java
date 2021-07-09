package com.example.hazelcast.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.BlockingQueue;

/**
 * @author w97766
 * @date 2021/7/6
 */
public class Client {
    public static void main(String[] args) throws Exception {
        ClientConfig clientConfig = new ClientConfig().addAddress("127.0.0.1");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        BlockingQueue<String> queue = client.getQueue("queue");
        queue.put("Hello");
        System.out.println("Message send by Client!");
    }
}
