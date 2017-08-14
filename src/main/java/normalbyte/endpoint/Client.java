package normalbyte.endpoint;
/**
 * 普通字节传输（小于8192）例如发送16进制的数据
 * @author Annie
 *
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufUtil;
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
import normalbyte.ClientHandler;

/**
 * 普通字节传输（小于1024）,例如发送16进制的字符串
 * 
 * @author Annie
 *
 */
public class Client {
	private static final int port = 2333;
	private static final String host = "localhost";
	// 待发送的16位进制的对象数据
	private String hexDump;

	public Client beforeStart(Object msg) {
		byte[] binaryByte = msg.toString().getBytes();
		// 将二进制转换成16进制字符串
		this.hexDump = ByteBufUtil.hexDump(binaryByte);
		System.err.println("初始化16进制：" + this.hexDump);
		return this;
	}

	public void run() {
		Bootstrap client = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			client.group(group).channel(NioSocketChannel.class).remoteAddress(host, port)
					.option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new ObjectEncoder());
							ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
							ch.pipeline().addLast(new ClientHandler());
						}
					});
			ChannelFuture f = client.connect().sync();
			if (f.isSuccess()) {
				System.out.println("---成功连接到服务器---");
			}
			f.channel().writeAndFlush(this.hexDump);
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		new Client().beforeStart(new String("turn on the light")).run();
	}
}
