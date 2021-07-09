package com.example.hazelcast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;

public class Test {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode specificProps = mapper.createObjectNode();
        specificProps.putObject("a");
        specificProps.putObject("b");
        Iterator<String> stringIterator = specificProps.fieldNames();
        while ( stringIterator.hasNext() )
        {

            System.out.println(stringIterator.next());
        }
        System.out.println(stringIterator);
    }

    private static void test(int num) {
        if (num == 3) {
            return;
        }
        System.out.println(num);
    }
}
