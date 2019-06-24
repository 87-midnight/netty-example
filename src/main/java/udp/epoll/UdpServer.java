package udp.epoll;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class UdpServer  {

    private void startServer() throws InterruptedException {
        EventLoopGroup acceptGroup = new EpollEventLoopGroup(12);
        Bootstrap serverBootstrap = new Bootstrap();
        serverBootstrap.group(acceptGroup);
        serverBootstrap.channel(EpollDatagramChannel.class);
        serverBootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 64*1024);
        serverBootstrap.option(ChannelOption.SO_SNDBUF, 1024 * 64*1024);
        serverBootstrap.option(EpollChannelOption.SO_REUSEPORT,true);
        serverBootstrap.handler(new ChannelInitializer<EpollDatagramChannel>() {

            @Override
            protected void initChannel(EpollDatagramChannel ch) throws Exception {
                ch.pipeline().addLast(new ServerHandler());
            }

        });
        InetSocketAddress inetSocketAddress = new InetSocketAddress(5588);
        for(int i=0;i<Runtime.getRuntime().availableProcessors();i++){
            ChannelFuture f =serverBootstrap.bind(inetSocketAddress).await();
            if (!f.isSuccess()){
                log.error("start udp server failed,{}",f.cause());
            }
        }
    }
}
