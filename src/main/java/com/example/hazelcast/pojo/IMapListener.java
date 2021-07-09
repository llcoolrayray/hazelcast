package com.example.hazelcast.pojo;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;

public class IMapListener implements EntryAddedListener<String, String> {

    @Override
    public void entryAdded(EntryEvent<String, String> event) {
        // TODO Auto-generated method stub
        //干你监听的操作
        System.out.println("MAP分布式监听："+event.getValue());
    }

}
