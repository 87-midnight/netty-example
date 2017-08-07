package endpoint;


import handler.client.MessageHandler;
import handler.client.StateHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import vo.Message;
/**
 * 在初始化配置bootstrap后立即发送消息，即可实现客户端主动发送消息至服务端
 * @author Annie
 *
 */
public class EchoClient {

	private int port;
	private String host;
	private SocketChannel socketChannel;
	
	public EchoClient(int port,String host){
		this.host = host;
		this.port = port;
	}
	
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public EchoClient connect(Object msg){
		Bootstrap b = new Bootstrap();
		EventLoopGroup work = new NioEventLoopGroup();
		try{			
			b.group(work)
			.channel(NioSocketChannel.class)
			.remoteAddress(host, port)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					//只有编码，不加解码，MessageHandler不能正确解析对象流，但不会报异常
					/**
					 * pipeline添加handler的顺序，一般io.netty.handler.codec包下的类
					 * 放在前面，在此包下的类之间的顺序似乎随意(亲测但不敢确定)，实例化socketChannel
					 * 的时候，会把继承channelInboundHandler的子类赋值给handler属性，该子类的位置于
					 * 后面。而继承channelOutboundHandler的子类放在任意位置都不影响程序(不确定，待测)
					 * 还需要写一个客户端发送对象流到服务端来测试这个pipeline的handler位置的影响
					 */
					ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new StateHandler());
					ch.pipeline().addLast(new MessageHandler());
				}
			});
			ChannelFuture f =  b.connect().sync();
			if(f.isSuccess()){
				this.socketChannel = (SocketChannel) f.channel();
				System.out.println("---成功连接到服务器---");
			}
			//目前只想到在channelFuture等待之前写一个对象，能成功发送到服务器，其他方法暂时没想到
			f.channel().writeAndFlush(msg);
			//在此之前可以通过获取channel对象来给服务端发送字节流
			//通道进入等待状态直到关闭，如果不加这段代码，客户端实例化完不会处于线程等待状态
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			work.shutdownGracefully();
		}
		return this;
	}
	
	public static void main(String[] args) {
		EchoClient ec = new EchoClient(7770,"localhost");
		Message msg = new Message();
		msg.setMsg("sssssssssssssssss");
		msg.setSn(222);
		ec.connect(msg);
	}
}
