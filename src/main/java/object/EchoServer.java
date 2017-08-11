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
		//����һ�������ʵ��
		ServerBootstrap sb = new ServerBootstrap();
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();
		try{
			//�������Ҫһ�ߴ������������һ�ߴ����Ѿ����ӵĿͻ��ˣ����߳̿��Բ�������
			sb.group(boss, work)
			  //ͨ����ʵ�������в���������������
			  .channel(NioServerSocketChannel.class)
			  //����˴���ͻ�������������˳����ģ�����ͬһʱ��ֻ�ܴ���һ���ͻ������ӣ�
			  //����ͻ�������ʱ�򣬷���˽����ܴ���Ŀͻ�������������ڶ����еȴ�����
			  //backlog����ָ���˶��еĴ�С
			  .option(ChannelOption.SO_BACKLOG,128)
			  //��ѡ��ֻ��5.0�汾�ϲ���Ӧ�ã�����4.0ֻ��warning���ᱨ��
			  .option(ChannelOption.TCP_NODELAY,true)
			  //������ͻ��˵ĳ�����
			  .childOption(ChannelOption.SO_KEEPALIVE, true)
			  //Ϊ�ͻ��˰�����Ҫ��handler
			  .childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					/**һ��pipeline��Ӧһ���ͻ��ˣ����ڹܵ������������˫����
					 * ����˵���������ͻ��˷��Ͷ����������������Ҫ���ObjectEncoder�����룬
					 * ���ͻ�������Ҫ���ObjectDecoder��������ܽ�����������ͬ������˽��տͻ���
					 * ���͹����Ķ������������ҪObjectDecoder���ͻ���ҪObjectEncoder��
					 */
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
					ch.pipeline().addLast(new MessageHandler());
				}
				  
			});
			//����������
			ChannelFuture f = sb.bind(port).sync();
			if(f.isSuccess()){
				map.put(1, f.channel());
				System.out.println("---�����������ɹ�---");
			}
			//ͨ��һֱ�ȴ�ֱ�������socket�ر�
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
