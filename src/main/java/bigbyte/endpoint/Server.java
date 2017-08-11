package bigbyte.endpoint;

import bigbyte.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * 大于1024字节的数据会被netty默认分包传输，
 * 需要使用LengthFieldBasedFrameDecoder来
 * 制定协议，制定协议格式
 * @author Annie
 *
 */
public class Server {

	private static final int port = 4396;
	
	public Server(){
		run();
	}
	private void run(){
		ServerBootstrap sb = new ServerBootstrap();
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();
		try{
			sb.group(boss, work)
			  .channel(NioServerSocketChannel.class)
			  .option(ChannelOption.SO_BACKLOG,128)
			  .childOption(ChannelOption.SO_KEEPALIVE, true)
			  .childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					//最大数据量是1GB,长度属性位于数据包头部，占4个字节，协议体调节值为0，跳过头部协议长度四个字节
					ch.pipeline().addLast("framedecoder",new LengthFieldBasedFrameDecoder(1024*1024*1024, 0, 4,0,4));
					ch.pipeline().addLast(new ByteArrayEncoder());
					ch.pipeline().addLast(new ByteArrayDecoder());
					ch.pipeline().addLast(new ServerHandler());
				}
			  });
			ChannelFuture f = sb.bind(port).sync();
			if(f.isSuccess()){
				System.out.println("---服务器启动成功---");
			}
			f.channel().closeFuture().sync();
		  }catch(Exception e){
			  
		}finally{
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new Server();
	}
}
