package object;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import object.handler.server.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
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

public class EchoServer {

	private int port; 
	
	public final static Map<Integer,Channel> map = new ConcurrentHashMap<>();
	public EchoServer(int port){
		this.port = port;
		bind();
	}
	public void bind(){
		//创建一个服务端实例
		ServerBootstrap sb = new ServerBootstrap();
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();
		try{
			//服务端需要一边处理待连接请求一边处理已经连接的客户端，多线程可以并发处理
			sb.group(boss, work)
			  //通道的实例化将有参数的类型所决定
			  .channel(NioServerSocketChannel.class)
			  //服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，
			  //多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，
			  //backlog参数指定了队列的大小
			  .option(ChannelOption.SO_BACKLOG,128)
			  //此选项只在5.0版本上才有应用，用在4.0只会warning不会报错
			  .option(ChannelOption.TCP_NODELAY,true)
			  //保持与客户端的长连接
			  .childOption(ChannelOption.SO_KEEPALIVE, true)
			  //为客户端绑定所需要的handler
			  .childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					/**一个pipeline对应一个客户端，由于管道的流向可以是双向性
					 * 比如说，服务端向客户端发送对象流，服务端则需要添加ObjectEncoder来编码，
					 * 而客户端则需要添加ObjectDecoder来解码才能解析对象流；同理，服务端接收客户端
					 * 发送过来的对象流，服务端要ObjectDecoder，客户端要ObjectEncoder。
					 */
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
					ch.pipeline().addLast(new MessageHandler());
				}
				  
			});
			//启动服务器
			ChannelFuture f = sb.bind(port).sync();
			if(f.isSuccess()){
				map.put(1, f.channel());
				System.out.println("---服务器启动成功---");
			}
			//通道一直等待直到服务端socket关闭
			f.channel().closeFuture().sync();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new EchoServer(7770);
	}
}
