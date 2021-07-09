## Overview
HazelCast Client 可以连接到 HazelCast 集群中执行读取，写入等操作，该操作会被转发给集群中的某个节点处理。
HazelCast Client 依赖 Hazelcast core JAR 和 Hazelcast client JAR。

## Demo
```java
public class FullMember {
    public static void main(String[] args) throws Exception{
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        System.out.print("Full member up");
        BlockingQueue<String> queue = hz.getQueue("queue");
        while (true) {
            System.out.println(queue.take());
        }
    }
}
```
```java
public class Client {
    public static void main(String[] args) throws Exception {
        ClientConfig clientConfig = new ClientConfig().addAddress("127.0.0.1");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        BlockingQueue<String> queue = client.getQueue("queue");
        queue.put("Hello");
        System.out.println("Message send by Client!");
    }
}
```

## 重用 client
尽可能重用 client 因为 `HazelcastClient` 是一个非常庞大的对象

## 配置 client
* addressList：集群中节点的地址，不需要包含全部地址，至少 1 个即可
* connectionTimeout：连接异常超时时间，默认 1 分钟
* connectionAttemptLimit：客户端连接集群最大尝试次数，默认为 2 次
* connectionAttemptPeriod：在集群中查找节点的时间间隔，默认 3000 毫秒
* listeners：启用监听集群状态，使用`LifecycleListener`
* loadBalancer：负载均衡配置，默认为`RoundRobinLB`
* smart：如果为真，客户端会将基于密钥的操作以最佳方式路由给密钥的所有者
* redoOperation：如果为 true 则当客户端执行操作时与集群断开连接，那么重连时客户端会重新执行操作。对于非幂等性操作，该功能
  可呢会引发不良影响。默认为 false，当断开连接时会抛出`RuntimeException`
* group: 见 Group Configuration.
* socketOptions：如`setKeepAlive(x)`，`setTcpNoDelay(x)`，`setReuseAddress(x)`，`setLingerSeconds(x)`，`setBufferSize(x)`
* serializationConfig：配置序列化与反序列化
* socketInterceptor：在 socket 连接之前执行回调函数
* classLoader：自定义 `classLoader`
* credentials：授权与鉴权，在企业版有效

## 负载均衡
客户端连接到集群时，它能够访问集群中的每个节点，本地同步存储集群的节点 list。如果一个操作需要集群中的特定节点，那么 client 会直接操作这个节点。否则
Hazelcast 会对集群中的每个节点负载均衡。

如果对负载均衡有特殊的需求如 CPU 负载，内存负载或队列大小，可以通过自定义`LoadBalancer`来满足这些需求

`MembershipListener`可以轻松的监控集群信息
```
Cluster cluster = hz.getCluster();
cluster.addMembershipListener(thelistener);
```

## 故障转移
客户端支持故障转移以提高可用性，分 2 部分实现
* 在 `ClientConfig` 中配置集群中的节点地址，只要有一个节点在线，客户端就可以获取集群中所有节点的信息
* `LoadBalancer` 实现 `MembershipListener`，`MembershipListener`会监听集群中节点的增删从而更新路由表

## client 连接策略
客户端连接集群可配置为同步或异步,也可以配置 client 断开连接后如何重新连接到集群，使用 `setReconnectMode`
有以下模式
* 禁用重新连接
* 阻塞方式重新连接，启动重新连接时所有的远程调用都会被阻塞，直到重新连接成功或失败
* 非阻塞方式重新连接

## 集群分组
可以为集群分组设置组名，设置 client 可以连接哪些组 
```java
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
```
## SSL
可以使用 SSL 加密客户端和集群中节点的通讯，这意味像 map.put 这类操作都是加密的

```
keytool -genkey -alias hazelcast -keyalg RSA -keypass password -keystore hazelcast.ks
-storepass password
keytool -export -alias hazelcast -file hazelcast.cer -keystore hazelcast.ks -storepass
password keytool
-import -v -trustcacerts -alias hazelcast -keypass password -file hazelcast.cer -keystore
hazelcast.ts
-storepass password
``` 

```java
public class Client {
    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.keyStore",
                new File("hazelcast.ks").getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStore",
                new File("hazelcast.ts").getAbsolutePath());
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        ClientConfig config = new ClientConfig();
        config.addAddress("127.0.0.1");
        config.setRedoOperation(true);
        config.getSocketOptions().setSocketFactory(new SSLSocketFactory());
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        BlockingQueue<String> queue = client.getQueue("queue");
        queue.put("Hello!");
        System.out.println("Message send by client!");
        System.exit(0);
    }
}
```

```java
public class Member {
    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.keyStore",
                new File("hazelcast.ks").getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStore",
                new File("hazelcast.ts").getAbsolutePath());
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        Config config = new Config();
        config.getNetworkConfig().setSSLConfig(new SSLConfig().setEnabled(true));
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        BlockingQueue<String> queue = hz.getQueue("queue");
        System.out.println("Full member up");
        for (; ; )
            System.out.println(queue.take());
    }
}
```

## 备注
client 使用完后一定要关闭。可用`client.getLifecycleService().shutdown();`或`LifeCycleService`关闭 client