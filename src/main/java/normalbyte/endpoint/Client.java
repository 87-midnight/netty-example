package normalbyte.endpoint;
/**
 * ��ͨ�ֽڴ��䣨С��8192�����緢��16���Ƶ�����
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
 * ��ͨ�ֽڴ��䣨С��1024��,���緢��16���Ƶ��ַ���
 * 
 * @author Annie
 *
 */
public class Client {
	private static final int port = 2333;
	private static final String host = "localhost";
	// �����͵�16λ���ƵĶ�������
	private String hexDump;

	public Client beforeStart(Object msg) {
		byte[] binaryByte = msg.toString().getBytes();
		// ��������ת����16�����ַ���
		this.hexDump = ByteBufUtil.hexDump(binaryByte);
		System.err.println("��ʼ��16���ƣ�" + this.hexDump);
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
				System.out.println("---�ɹ����ӵ�������---");
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
