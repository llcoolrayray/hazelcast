## Overview 
Hazelcast 提供了分布式的高密度内存存储机制

## 高密度内存存储机制
### 常见场景
Hazelcast 通常部署在大型商用硬件集群上可进行横向扩展。 这种部署模式可以使用几十个甚至几百个带有 Java 虚拟机 (JVM) 的节点。

### 垃圾收集的挑战
Hazelcast 存储在堆上，这会带来一些问题。如受 GC 影响长时间 GC 使程序未响应，数据存满堆中会造成内存错误。

## 高密度内存解决方案
Hazelcast High-Density Memory Store 是 Hazelcast Elastic Memory 的继任者，是 Hazelcast 最新的企业级内存存储解决方案。
它解决了垃圾收集的限制，能够更有效的利用硬件内存而无需水平扩展。它被设计为一个可插拔的内存管理器，不同的内存存储不同的数据结构

## 配置高密度内存
要使用高密度内存，必须先进行配置。有如下配置项：
* size：本机内存可分配最大大小，默认值为 512 MB
* allocator type：内存分配模式
    * STANDARD：使用操作系统默认内存管理机制
    * POOLED：使用 Hazelcast 内部内存池管理内存（默认）
* minimum block size：内存分页最小大小，默认为 16
* page size：内存页大小，默认为 4MB
* metadata space percentage:使用已分配内存的百分比
* size：

配置代码如下：
```
MemorySize memorySize = new MemorySize(512, MemoryUnit.MEGABYTES);
NativeMemoryConfig nativeMemoryConfig = new NativeMemoryConfig().setAllocatorType(MemoryAllocatorType.POOLED) 
    .setSize(memorySize) .setEnabled(true)
    .setMinBlockSize(16) .setPageSize(1 << 20);
```

## IMap 配置高密度内存
```
MapConfig mapConfig = new MapConfig("myMap");
mapConfig.setInMemoryFormat(InMemoryFormat.NATIVE);
```

```
EvictionConfig evictionConfig = new EvictionConfig();
evictionConfig.setSize(90);
evictionConfig.setMaximumSizePolicy(MaxSizePolicy.USED_NATIVE_MEMORY_PERCENTAGE);
cacheConfig.setEvictionConfig(evictionConfig);
```

## 配置 Near Cache
```
NearCacheConfig nearCacheConfig = new NearCacheConfig("myNearCache");
nearCacheConfig.setInMemoryFormat(InMemoryFormat.NATIVE);
```

## 监控高密度内存
使用`hazelcast.health.monitoring.level` 配置监控等级