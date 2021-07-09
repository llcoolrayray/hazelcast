package com.example.hazelcast.pojo;

import com.hazelcast.core.*;

public class Member {
    public static void main(String[] args) throws Exception {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        IQueue<Integer> queue = hz.getQueue("queue");
        queue.put(1);
    }
}
