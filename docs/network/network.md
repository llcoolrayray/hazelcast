## Overview 
Hazelcast 网络配置。要使 Hazelcast 集群正常运行，所有节点都必须能够联系到其它节点，Hazelcast 是一个点对点集群。

## Port
Hazelcast 默认端口号为 5701。
设置 Hazelcast 端口范围：
```
NetworkConfig networkConfig = config.getNetworkConfig();
// ports between 35000 and 35100
networkConfig.addOutboundPortDefinition("35000-35100");
// comma separated ports
networkConfig.addOutboundPortDefinition("36001, 36002, 36003");
networkConfig.addOutboundPort(37000);
networkConfig.addOutboundPort(37001);
```

## 加入集群机制
Hazelcast 加入集群有如下三种机制：TCP/IP-cluster，Multicast，Amazon EC2。使用编程方式配置 Hazelcast 默认使用 Multicast 加入集群。

### Multicast
使用多播，节点会向同一个 group 内的所有节点发送消息。
使用以下参数配置 Multicast
* multicast-group：设置 group，加入到集群中的特定 group
* multicast-port：多播端口号，默认为 54327
* multicast-time-to-live：多播数据包生存时间。默认为 32，最大值为 255
* multicast-timeout-seconds：节点多播后等待其它节点响应的时间，如果超时则节点组建集群并将自己标记为 master

Hazelcast 支持仅允许受信任的节点（ip）加入集群

### TCP/IP Cluster
在生产环境下多播通常是禁止的，并且在云环境下不支持多播。因此 Hazelcast 提供了另外一种发现机制 TCP/IP。
当一个节点连接到一个众所周知的节点，那么所有节点都会更新所有节点的地址。

## 分区设置
通常，Hazelcast 会阻止主分区和备份分区存储在同一个 JVM 上以保证高可用性，但同一集群的多个 Hazelcast 成员可以运行在
同一台机器，因此当机器出现故障时，主备都可能失败。 Hazelcast 提供了分区设置来解决此问题。

## 集群分组
有时需要在同一个网络上有多个隔离的集群而不是一个，则可以使用集群分组

## SSL
Hazelcast 提供`BasicSSLContextFactory`作为 SSL 加密解决方案

## 对称加密
Hazelcast 支持对称加密

## Firewall
Hazelcast 可配置客户端端口防火墙
