## Overview 
com.hazelcast.core.ITopic：分布式消息队列

## Demo
先启动 SubscribedMember 再启动 PublisherMember 能够在 SubscribedMember 控制台看到  

```
Subscribed

Received1: Wed Jun 30 20:07:15 CST 2021
Received2: Wed Jun 30 20:07:15 CST 2021
```

```java
public class SubscribedMember {
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        ITopic<Date> topic = hz.getTopic("topic");
        topic.addMessageListener(new MessageListenerImpl1());
        topic.addMessageListener(new MessageListenerImpl2());
        System.out.println("Subscribed");
    }

    private static class MessageListenerImpl1 implements MessageListener<Date> {
        @Override
        public void onMessage(Message<Date> m) {
            System.out.println("Received1: " + m.getMessageObject());
        }
    }

    private static class MessageListenerImpl2 implements MessageListener<Date> {
        @Override
        public void onMessage(Message<Date> m) {
            System.out.println("Received2: " + m.getMessageObject());
        }
    }
}
```

```java
public class PublisherMember {
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        ITopic<Date> topic = hz.getTopic("topic");
        topic.publish(new Date());
        System.out.println("Published");
    }
}
```

## 配置 topic
```java
public class Config {
    public void config() {
        Config config = new Config();
        TopicConfig topicConfig = new TopicConfig();
        topicConfig.setName("topic");
        topicConfig.setGlobalOrderingEnabled(true);
        MessageListener<String> listener = m -> {};
        topicConfig.addMessageListenerConfig(new ListenerConfig(listener));
        config.addTopicConfig(topicConfig);
    }
}
```
## 消息顺序
Hazelcast 订阅者接收消息是有顺序的，如果集群中发送了一条消息，Hazelcast 保证每个 MessageListener 都会按照消息的发送顺序接收消息。
如果消息以 m1,m2,m3 的顺序发送，那么每个 MessageListener 都会以 m1,m2,m3 的顺序接收消息。  

尽管默认情况下 MessageListener 会以
顺序接收消息，如果消息发布者 M 发送 m1,m2,m3，消息发布者 N 发送 n1,n2,n3。MessageListener1 接收顺序为：m1, m2, m3, n1, n2, n3
而 MessageListener2 接收顺序为：m1, n1, n2, n3, m2, m3。这也是有效的，因为并未违反 Hazelcast 的消息接收规则。

在某些情况下，你希望所有 MessageListener 都以完全相同的顺序接收消息。MessageListener 提供了 globalOrderingEnabled 参数。
注意：此配置参数可能会对吞吐量和延迟有巨大影响

`topicConfig.setGlobalOrderingEnabled(true)`;

## 可靠的 Topic
在一些场景下当 topic 和一些高负载的 Imap，IQueue 一起使用时可能会出现一些意外情况
* 当 topic 和其他分布式组件使用相同的事件队列，可能会使 topic 性能急剧下降
* 当 MessageListener 消费消息较慢时可能会影响其他 topic，因为 topic 使用相同的队列发布消息
 
也可能会出现消息丢失的情况
* 为防止系统 OOM 事件队列容量被限制为默认 1，000，000。因此若队列已满则发布的消息会丢失
* 若 MessageListener 收到消息时崩溃了，则消息丢失
* 若消息发布者发送消息后，消息仍在队列时，消息发布者崩溃，则消息丢失。
---
    
## 为了解决以上问题 Hazelcast 提供了 ReliableTopic

```java
public class ReliableTopic {
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        ITopic<Date> topic = hz.getReliableTopic("reliable-topic");
        topic.publish(new Date());
        System.out.println("Published");
    }
}
```

ReliableTopic 使用 RingBuffer 构建，有如下优点
* 每个 ReliableTopic 实例都有自己的 RingBuffer，不存在资源共享
* ReliableTopic 还可以配置专有的执行器来处理消息，这使得 ReliableTopic 可以完全的和其他 Topic 隔离
* RingBuffer 在集群中每个节点都有副本，高可用
* 可以配置 RingBuffer 长度
* 处理消息较慢的消费者，不会影响其它消费者消费数据。查看 ReliableMessageListener 了解更多
* 如果消费者读取消息时发生错误，可以再次重复消费消息。查看 TopicOverloadPolicy 了解更多

ReliableTopic 中消息是有序的，不需要  `topicConfig.setGlobalOrderingEnabled(true)`

---
## MessageListener 的隐患
Hazelcast 内部使用事件机制来实现消息的发布订阅，即所有的消息生产和消费可能是由相同的线程处理的。
这意味着如果线程被阻塞，那么会影响到整个系统的 Topic。
可以通过 `hazelcast.event.thread.count` 为 Hazelcast 配置事件机制的线程数，默认为 5 个线程。
也可以讲消息的处理在另一个线程去做来解决次问题。

```java
public class SubscribedMember2 {
    private final static StripedExecutor executor = new StripedExecutor();

    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        ITopic<Date> topic = hz.getTopic("topic");
        topic.addMessageListener(new MessageListenerImpl("topic"));
        System.out.println("Subscribed");
    }

    private static class MessageListenerImpl
            implements MessageListener<Date> {
        private final String topicName;

        public MessageListenerImpl(String topicName) {
            this.topicName = topicName;
        }

        @Override
        public void onMessage(final Message<Date> m) {
            StripedRunnable task = new StripedRunnable() {
                @Override
                public int getKey() {
                    return topicName.hashCode();
                }

                @Override
                public void run() {
                    System.out.println("Received: " + m.getMessageObject());
                }
            };
            executor.execute(task);
        }
    }
}
```

在上面的代码中，消费者使用 `StripedRunnable` 处理消息，`StripedRunnable` 可能会造成 `OOM`，
最好使用有界的线程池处理消息，或者使用 `TimedRunnable` 来处理 `timeout`。一定要注意不要消费消息太久，因为它占用了
Hazelcast 事件系统中的一个线程，可能会影响到系统的其它模块。
如果不关心消息的顺序，可以使用`ordinary executor`替代`striped executor`。

## 备注
当消息发布者发布一条消息，每一个订阅者都会收到相同的消息