## Overview 
Hazelcast 提高性能技巧

## 集群设计
Hazelcast 假设集群中每一个节点都是完全相同的，具有相同的带宽，内存，cpu 等。Hazelcast 会平均给每一个节点相同的备份。
如果生产环境中节点不是完全相同的，建议创建多个集群并通过客户端让他们通讯。

## 分布式数据引用
不建议每次使用分布式对象时都调用 `IMap map = hz.getMap("some-map");`，而是保持 `map` 引用

## 脑裂问题
Hazelcast 可以定义集群中最少节点数量。开启脑裂保护，支持以下数据机构：Map，Transactional，Cache，Lock，Queue

## 分区
使用 Hazelcast 尽量少使用分布式对象，因为可能会造成大量远程访问。如下示例两个计数器可能不在同一个分区中，需要远程处理并降低了系统性能
```
IAtomicLong counter1 = hz.getCounter("counter1");
IAtomicLong counter2 = hz.getCounter("counter2");
```

## Map 性能
* 设置备份数量，越少性能越好
* 异步备份代替同步备份
* 使用`IMap.set(k, v)`而不是`put(k,v)`没有返回值就不会序列化
* 避免锁和事务
* 如果真的需要`MapStore`，那就考虑使用 write-behind。直写会减慢你的速度。

## 分布式对象状态
一些分布式对象如 IMap, IQueue 和 ITopic,可以查看他们的状态
```
IMap map = hz.getMap();
LocalMapStats stats = map.getLocalMapStats()
```

## 日志
SlowOperationDetector 会监控并收集所有慢的操作。可通过 hazelcast.slow.operation.detector.threshold.millis，
hazelcast.slow.operation.detector.enabled 配置，默认值为 10000。

SystemLogServices 处理系统日志