package com.lcg.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TcpServer {

    private Integer port;

    private Channel server;

    private EventLoopGroup boss;
    private EventLoopGroup group;

    public static ConcurrentMap<String,Channel> clients = new ConcurrentHashMap<>();

    public TcpServer(Integer port) {
        this.port = port;
    }

    public void startServer()throws Exception{
        ServerBootstrap sb = new ServerBootstrap();
        boss = new NioEventLoopGroup(5);
        group = new NioEventLoopGroup(5);
        sb.group(boss,group)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.SO_RCVBUF,1024*64)
                .option(ChannelOption.SO_SNDBUF,1024*64)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ByteArrayEncoder());
                        ch.pipeline().addLast(new ByteArrayDecoder());
                        ch.pipeline().addLast(new MessageHandler());
                    }
                });
        ChannelFuture f = sb.bind(new InetSocketAddress(port)).await();
        if (f.isSuccess()){
            server = f.channel();
            System.out.println("Tcp服务器启动成功");
        }
    }

    public void sendToAll(String msg){
        for (Map.Entry<String,Channel>entry : clients.entrySet()){
            String id = entry.getKey();
            entry.getValue().writeAndFlush((msg+id).getBytes());
        }
    }

    public void close(){
        server.close();
        boss.shutdownGracefully();
        group.shutdownGracefully();
    }
}
