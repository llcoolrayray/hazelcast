package com.example.hazelcast.pojo;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;
import java.util.Queue;

import static com.example.hazelcast.pojo.Const.MAP_NAME;
import static com.example.hazelcast.pojo.Const.QUEUE_NAME;


//生产数据
public class HazelcastGetStartServerMaster {
    public static void main(String[] args) {
        // 创建一个 hazelcastInstance实例
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        // 创建集群Map
        Map<String, String> clusterMap = instance.getMap(MAP_NAME);
        clusterMap.put("a","data-a");
        clusterMap.put("b","data-b");

        // 创建集群Queue
        Queue<String> clusterQueue = instance.getQueue(QUEUE_NAME);
        clusterQueue.offer("Hello hazelcast!");
        clusterQueue.offer("Hello hazelcast queue!");
    }
}
