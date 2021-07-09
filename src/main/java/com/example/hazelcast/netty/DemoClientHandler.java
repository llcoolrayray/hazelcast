/*
package com.example.hazelcast.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.StandardCharsets;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.awt.*;
import java.util.List;

*/
/**
 * @author w97766
 * @date 2021/7/2
 *//*

public class DemoClientHandler extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 消息头
        byteBuf.readByte();
        byteBuf.readInt();
        int clientType = byteBuf.readByte();
        int messageType = TrayIcon.MessageType.valueOf(byteBuf.readUnsignedShort());
        int sysDeviceId = byteBuf.readShort();

        byteBuf.readByte();

        int dataIndex = byteBuf.readByte();

        int deviceTypeVersion = byteBuf.readInt();
        int projectVersion = byteBuf.readShort();
        int deviceId = byteBuf.readShort();
        int deviceTypeId = byteBuf.readShort();

        int deviceVersion = byteBuf.readShort();

        // 消息体
        int result = byteBuf.readInt();
        int signalCount = byteBuf.readInt();
        byte[] raw = new byte[byteBuf.readableBytes()];
        buffer.readBytes(raw);

        int clientType = this.header.getClientType();
        String body = clientType < 9 ? new String(raw, CHARSET_GB2312) : new String(raw, StandardCharsets.UTF_8);
        body = body.trim();
        String[] split = body.split("\\|");
        for (int i = 0; i < split.length; i++) {
            ConfigSignal signal = new ConfigSignal();
            if (!split[i].isEmpty()) {
                signal.deserialize(split[i]);
                signals.add(signal);
            }
        }

    }

    enum RegisterResult {
        Success(0),
        Busy(1),
        NoAccess(2),
        NetworkException(255);

        private int index;

        private RegisterResult(int index) {
            this.index = index;
        }

        public static RegisterResult valueOf(int index) {
            RegisterResult[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                RegisterResult t = var1[var3];
                if (t.getIndex() == index) {
                    return t;
                }
            }

            return NetworkException;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public String toString() {
            return this.name();
        }
    }
}

*/
