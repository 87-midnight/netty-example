package clienthandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
/**
 * stackoverflow�Ͽ����Ĵ��룬��û��ʵ��
 * �ͻ��������˽������Ӻ󣬿ͻ��˿�����������Ϣ��
 * ����˵Ĺ��ܣ����������˼����ʽ��ȷ��һ��
 * @author Annie
 *
 */
public final class ChatServer {

	public ChatServer(int PORT) throws Exception {

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast(new StringEncoder());
							ch.pipeline().addLast(new ChatServerHandler());
						}

					});

			b.bind(PORT).sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args) throws Exception {
		ChatServer ct = new ChatServer(7201);
	}
}