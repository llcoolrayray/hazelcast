## Overview 
Hazelcast 线程模型

## I/O Threading
Hazelcast 使用线程池进行 I/O 操作，线程分为以下三种类型：
* accept-I/O-threads：接收 I/O 请求
* read-I/O-threads：读取数据
* write-I/O-threads：写数据

可以使用`hazelcast.io.thread.count`配置 I/O 线程数量。如果配置为 3，则总共会有 7 个 I/O 线程。1 个 accept-I/O-threads，3 个
read-I/O-threads，3 个 write-I/O-threads。每一个 I/O 线程都有自己的 Selector。

## Event Threading
Hazelcast 使用共享的事件系统来处理事件，如 topic，collectionslisteners，near cache。

集群中每个节点都有一个数组存放着所有事件线程，每个线程都有自己的工作队列（实现 BlockingQueue）。当一个事件产生时，会选择一个事件线程并将事件放入
该线程的工作队列中。

事件线程有如下配置：
* hazelcast.event.thread.count：存储事件线程数组的大小，默认为 5
* hazelcast.event.queue.capacity：事件线程工作队列的大小，默认为  1000000
* hazelcast.event.queue.timeout.millis：将事件投递到工作队列的超时时间，默认为 250

Hazelcast 多个模块共享同一个事件队列。比如有 2 个 topic A 和 B。他们共享同一个事件队列和线程，因此如果 A 处理消息特别慢的话会使 B 阻塞，同理
B 也有可能阻塞 A。因此最佳实践是专门使用一个线程/线程池来消费消息。

## IExecutor Threading
它是完全隔离的

## Operation Threading
Hazelcast 有 2 种操作线程，它们有不同的线程模型
* 知道分区的操作，如 `IMap.get(key)`
* 不知道分区的操作，如 `IExecutorService.executeOnMember(command,member)`

### 分区感知操作
为了执行分区感知操作，节点会创建一个存放线程的数组，其大小默认为 cpu 的 2 倍，最小为 2。 可以通过 `hazelcast.operation.thread.count` 
修改该参数。每一个操作线程有自己的工作队列，它会从工作队列中消费数据。如果需要指定线程来执行操作，那么使用 
`threadIndex = partitionId % partition-thread-count`计算线程 threadIndex。确定好 threadIndex 后将操作放入指定线程的工作队列中。
这意味着 3 件事：
* 一个操作线程可以对多个分区执行操作，如果有 271 个分区和 10 操作线程那么 1 个操作线程会操作 27 个分区
* 操作线程和分区是一对多的关系，每个分区只能被同一个操作线程操作
* 分区感知操作不需要并发操作，因为一个分区只能被同一个操作线程操作

以上的线程策略可能会引发以下 2 种错误：
* 由于错误的分区，2 个数据结构完全相同的对象在同一个分区里面。如 employees map 和 orders map 在同一个分区里面，那么 `employees.get(peter)`
可能会阻塞 `orders.get(1234)`。因为他们共用同一个操作线程
* 一个操作线程负责多个分区，如果操作线程执行某个分区的任务时间较长，那么该操作线程负责的其它分区操作都会被阻塞

一般要尽快释放分区线程，避免执行耗时任务。

### 非分区感知操作
使用通用的线程执行非分区感知操作如 `IExecutorService.executeOnMember(command,member)`，当 Hazelcast 节点会创建一个存放线程的数组，
其大小默认为 cpu 的 1/2，最小值为 2。可以通过 `hazelcast.operation.generic.thread.count` 修改。

非分区感知操作线程不会为特定的分区去执行操作，所有的未知分区的操作线程共享同一个工作队列：`genericWorkQueue`

### 优先级操作线程
Hazelcast 允许在某些情况下让系统执行更高优先级的任务
* 对于分区感知操作，每个分区线程都有自己的工作列队，除此之外它还有一个优先级工作队列，每次执行操作之前都会先检查该队列
* 对于非分区感知操作，除了 genericWorkQueue 还有一个优先级工作队列 genericPriorityWorkQueue，每次执行操作之前都会先检查该队列

### 操作响应
执行一个操作后会返回一个 Future，提供更多能力
```
GetOperation operation = new GetOperation( mapName, key )
Future future = operationService.invoke( operation )
future.get)
```

### 查询
与 `IMap.get` 这类分区感知操作不同，还有一种直接在查询线程上进行的操作如：`Collection<V> IMap.values(Predicate predicate)`。
这意味着有一个单独的线程池执行查询操作，因此该操作可以和常规操作并行执行。通过 `ExecutorConfig` 可以设置查询线程池大小。










