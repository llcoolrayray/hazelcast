package com.example.hazelcast.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

/**
 * @author w97766
 * @date 2021/7/7
 */
public class Group {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();

        ClientNetworkConfig networkConfig = new ClientNetworkConfig();
        networkConfig.addAddress("127.0.0.1");

        GroupConfig groupConfig = new GroupConfig();
        groupConfig.setName("group1");

        clientConfig.setNetworkConfig(networkConfig).setGroupConfig(groupConfig);

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
    }
}
