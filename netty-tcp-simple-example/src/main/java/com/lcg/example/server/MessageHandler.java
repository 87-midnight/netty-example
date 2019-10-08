package com.lcg.example.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.lcg.example.server.TcpServer.clients;

@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<byte[]> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        System.out.println("收到客户端["+channelHandlerContext.channel().id()+"]发给我的消息："+new String(bytes));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.putIfAbsent(ctx.channel().id().asLongText(),ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        clients.remove(ctx.channel().id().asLongText());
    }
}
