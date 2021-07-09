package com.example.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;
import java.util.Queue;

public class Test2 {
    public static void main(String[] args) {
        // 获取Hazelcast实例
        HazelcastInstance ins = Hazelcast.newHazelcastInstance();

        // 从集群中读取Map实例
        Map<Integer, String> map = ins.getMap("default map");

        // 输出map中数据
        map.forEach((k,v)->{
            System.out.println("Pos:" + k + ". name:" + v);
        });
    }
}
