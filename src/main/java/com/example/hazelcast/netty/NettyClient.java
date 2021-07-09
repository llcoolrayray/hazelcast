/*
package com.example.hazelcast.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

*/
/**
 * @author w97766
 * @date 2021/7/2
 *//*


public class NettyClient {
    private final String host;
    private final int port;
    private Channel channel;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Channel start() {
        EventLoopGroup workerGroup = new NioEventLoopGroup(5);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new StringEncoder());
                    p.addLast(new DemoClientHandler());
                }
            });
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            return bootstrap.connect(host, port).sync().channel();
        } catch (InterruptedException e) {
            workerGroup.shutdownGracefully();
        }

        return null;

    }
    public static void main(String[] args) {
        NettyClient client = new NettyClient("10.146.102.10",13392);
        Channel channel = client.start();

        //简单设备信号：2
        ByteBuf body = Unpooled.buffer().writeInt(2);
        int length = 24 + body.writerIndex();
        ByteBuf dataOut = getDataOut(length);
        dataOut.writeBytes(body);
        dataOut.capacity(length);

        channel.writeAndFlush(dataOut);
    }

    private static ByteBuf getDataOut(int length) {
        ByteBuf dataOut = Unpooled.buffer(length);
        dataOut.writeByte(126);
        dataOut.writeInt(length);
        dataOut.writeByte(-1);
        dataOut.writeShort(1795);
        dataOut.writeShort(0);
        dataOut.writeByte(127);
        dataOut.writeByte(0);
        dataOut.writeInt(0);
        dataOut.writeShort(0);
        dataOut.writeShort(2);
        dataOut.writeShort(531);
        dataOut.writeShort(0);

        return dataOut;
    }

}

*/
