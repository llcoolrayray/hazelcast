package com.example.hazelcast.pojo;

import com.hazelcast.config.Config;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.ConcurrentMap;

public class HazelcastGetStartServerSlave {
    public static void main(String[] args) {
        Config config = new Config();
        config.setLiteMember(true);
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        Cluster cluster = hazelcastInstance.getCluster();
        cluster.promoteLocalLiteMember();
    }
}
