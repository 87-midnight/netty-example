package normalbyte.endpoint;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import normalbyte.ServerHandler;

/**
 * 普通字节传输（小于8192）,例如发送16进制的字符串
 * @author Annie
 *
 */
public class Server {

private static final int port = 2333;
	
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
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
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