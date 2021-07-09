## Overview 
Hazelcast 为了支持事务，提供了 `TransactionalMap`，`TransactionalMultiMap`，`TransactionalQueue`，`TransactionalSet`，
`TransactionalList`

## Demo
```java
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
```
## TransactionOptions  
Hazelcast 提供一下配置参数配置事务
* timeoutMillis：事务持有锁的超时时间
* TransactionType：LOCAL/TWO_PHASE（见 TransactionType）
* durability：事务日志备份数量，默认为 1
* durability：事务日志备份数量，默认为 1

```
TransactionOptions txOptions = new TransactionOptions()
    .setTimeout(1, TimeUnit.MINUTES)
    .setTransactionType(TransactionOptions.TransactionType.TWO_PHASE) .setDurability(1);
TransactionContext txCxt = hz.newTransactionContext(txOptions);
```

注意：`TransactionOptions` 不是线程安全的

## TransactionType
当事务提交时 Hazelcast 提供了两种 TransactionType
* LOCAL：当节点在提交事务时，如果有成员失败或崩溃，那么系统可能会处于不一致的状态
* TWO_PHASE：节点在提交之前将事务日志复制到其它节点中，如果该节点提交事务失败，则其它节点可以提交事务

## TransactionalTask
在前面的示例中提交事务，失败回滚都是手动的，这样会有大量的重复代码。Hazelcast 提供了 TransactionalTask 来处理这种问题
```java
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
```














