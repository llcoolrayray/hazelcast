package com.example.hazelcast.config;

import com.example.hazelcast.pojo.IMapInterceptor;
import com.example.hazelcast.pojo.IMapListener;
import com.example.hazelcast.pojo.TopicListener;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {
    @Bean
    public Config config() {
        Config config = new Config();
        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config config) {
        HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance(config);
        //分布式map监听
        IMap<Object, Object> imap = hzInstance.getMap("my-map");
        imap.addLocalEntryListener(new IMapListener());
        //拦截器（没写内容）
        imap.addInterceptor(new IMapInterceptor());
        //发布/订阅模式
        ITopic<String> topic = hzInstance.getTopic("my-topic");
        topic.addMessageListener(new TopicListener());

        return hzInstance;
    }
}
