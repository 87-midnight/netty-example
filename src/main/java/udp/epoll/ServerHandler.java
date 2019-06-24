package udp.epoll;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;
import java.nio.charset.Charset;

@Slf4j
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        log.info("receive message:{}",datagramPacket.content().toString(Charset.forName("utf-8")));
        String msg = "hello client:"+channelHandlerContext.channel().id().asLongText()+",time:"+System.currentTimeMillis();
        channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.buffer().writeBytes(msg.getBytes()),datagramPacket.sender()));
    }
}
