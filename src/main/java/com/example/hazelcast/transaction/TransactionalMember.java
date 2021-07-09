package com.example.hazelcast.transaction;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.TransactionalMap;
import com.hazelcast.transaction.TransactionContext;

/**
 * @author w97766
 * @date 2021/7/7
 */
public class TransactionalMember {
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        TransactionContext txCxt = hz.newTransactionContext();
        txCxt.beginTransaction();
        TransactionalMap<String, String> map = txCxt.getMap("map");
        try {
            map.put("1", "1");
            map.put("2", "2");
            txCxt.commitTransaction();
        } catch (Throwable t) {
            txCxt.rollbackTransaction();
        }
        System.out.println("Finished");
        System.exit(0);
    }
}
