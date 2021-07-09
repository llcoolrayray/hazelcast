## Overview
Hazelcast 是一个分布式内存数据存储和计算平台，具有容错性且易于扩展或缩减。

Hazelcast 通过将数据存储在内存中，使用户可以更快速的访问数据和低延迟的处理大量实时事件或静态数据集。

Hazelcast 带有以下内置数据结构：
* 基于 java 的 `Queue`，`Set`，`List`，`Map`，`Lock`，`ExecutorService`，`AtomicLong` 等分布式实现 
* 用于发布订阅的分布式 `Topic`
* 用于一对多关系的分布式 `MultiMap`
* 分布式事件驱动 `Distributed Events`
* 集群范围的唯一 ID 生成器`FlakeIdGenerator`
* 一个分布式的基于 CRDT 的计数器 `PNCounter`

Hazelcast 还提供了一个名为 Jet 的分布式批处理和流处理引擎。它提供了一个 Java API，通过使用数据流编程模型来构建流和批处理应用程序。
您可以使用它来处理大量实时事件或大量静态数据集。Hazelcast 的单个节点已被证明每秒聚合 1000 万个事件，延迟低于 10 毫秒。