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
 * �ڳ�ʼ������bootstrap������������Ϣ������ʵ�ֿͻ�������������Ϣ�������
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
					//ֻ�б��룬���ӽ��룬MessageHandler������ȷ�����������������ᱨ�쳣
					/**
					 * pipeline���handler��˳��һ��io.netty.handler.codec���µ���
					 * ����ǰ�棬�ڴ˰��µ���֮���˳���ƺ�����(�ײ⵫����ȷ��)��ʵ����socketChannel
					 * ��ʱ�򣬻�Ѽ̳�channelInboundHandler�����ำֵ��handler���ԣ��������λ����
					 * ���档���̳�channelOutboundHandler�������������λ�ö���Ӱ�����(��ȷ��������)
					 * ����Ҫдһ���ͻ��˷��Ͷ���������������������pipeline��handlerλ�õ�Ӱ��
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
				System.out.println("---�ɹ����ӵ�������---");
			}
			//Ŀǰֻ�뵽��channelFuture�ȴ�֮ǰдһ�������ܳɹ����͵�������������������ʱû�뵽
			f.channel().writeAndFlush(msg);
			//�ڴ�֮ǰ����ͨ����ȡchannel������������˷����ֽ���
			//ͨ������ȴ�״ֱ̬���رգ����������δ��룬�ͻ���ʵ�����겻�ᴦ���̵߳ȴ�״̬
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
