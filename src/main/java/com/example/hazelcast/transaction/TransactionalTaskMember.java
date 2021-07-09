package com.example.hazelcast.transaction;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.TransactionalMap;
import com.hazelcast.transaction.TransactionException;
import com.hazelcast.transaction.TransactionalTask;
import com.hazelcast.transaction.TransactionalTaskContext;

/**
 * @author w97766
 * @date 2021/7/7
 */
public class TransactionalTaskMember {
    public static void main(String[] args) throws Throwable {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        hz.executeTransaction((TransactionalTask) context -> {
            TransactionalMap<String,String> map = context.getMap("map");
            map.put("1", "1");
            map.put("2", "2");
            return null; });
        System.out.println("Finished");
        System.exit(0);
    }
}
