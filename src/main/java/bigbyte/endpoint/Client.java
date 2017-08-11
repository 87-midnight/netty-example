package bigbyte.endpoint;

import java.nio.ByteBuffer;

import bigbyte.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * �������1024���ȵ��ֽڣ��ͻ��� ��װ���ݰ����������ݳ����Լ�������
 * 
 * @author Annie
 *
 */
//http://blog.csdn.net/suifeng3051/article/details/34444585
public final class Client {

	private static final int port = 4396;

	private static final String host = "localhost";

	 // ����һ��������ϢЭ���ʽ��|--header:4 byte--|--content:10MB--|
	/**Ĭ����Ϣͷ4λ�洢�ֽ��ܳ���**/
	private final int header = 4;
	/**������**/
	private byte[] content;

	public Client() {
		
	}
    
	/**
	 * msg������Ҫ��дtoString����������Ҫ��������ݷ�װ��
	 * @param msg
	 * @return
	 */
	public Client beforeStart(Object msg) {
		//��ȡһ��4�ֽڳ��ȵ�Э����ͷ(�ֽ����飬msg���󳤶�����ֵд���ֽ�������)
		byte[] rebuildArray = byteArrayBuild(header, msg.toString().getBytes().length);
		System.out.print("msg���ȣ�"+msg.toString().getBytes().length+",Э��ͷ��"+rebuildArray.length);
		//��msg����ת�����ֽ�����洢
		byte[] targetBytes = msg.toString().getBytes();
		//��װ�ô���Э����ͷ�Լ����ݵ��ֽ�����
		this.content = combineByteArray(rebuildArray, targetBytes);
		System.out.println("����ǰ���ַ�����"+new String(this.content));
		return this;
	}

	public void start() {
		try{
			if(this.content == null){
				throw new Exception("������Ϊ�գ���ȷ�����ȵ���beforeStart");
			}
			Bootstrap client = new Bootstrap();
			EventLoopGroup group = new NioEventLoopGroup();
			client.group(group)
			.channel(NioSocketChannel.class)
			.remoteAddress(host, port)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ByteArrayEncoder());
					ch.pipeline().addLast(new ByteArrayDecoder());
					ch.pipeline().addLast(new ClientHandler());
				}
			});
			ChannelFuture f =  client.connect().sync();
			if(f.isSuccess()){
				System.out.println("---�ɹ����ӵ�������---");
			}
			f.channel().writeAndFlush(content);
			f.channel().closeFuture().sync();
		}catch (Exception e){
			
		}
	}
    /**
     * ����һ��������ܳ��ȵ��ֽ�����(����header��content)
     * ��Ŀ�����ĳ��Ȱ������͵���ֵд��4�ֽڵ�byte�������档
     * @param byteLength header��λ�������ڴ洢�ֽ��ܳ���,����ʹ��4�ֽ�
     * @param targetLength ��������ܳ���
     * @return
     */
	private static byte[] byteArrayBuild(final int byteLength, int targetLength) {

		return ByteBuffer.allocate(byteLength).putInt(targetLength).array();
	}
    /**
     * ����һ��������ֽڵ�����
     * @param array1 ����ó��ȵĿ��ֽ�����
     * @param array2 ������������1024�ֽڵ��ֽ�����
     * @return
     */
	private static byte[] combineByteArray(byte[] array1, byte[] array2) {
		byte[] combined = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, combined, 0, array1.length);
		System.arraycopy(array2, 0, combined, array1.length, array2.length);
		System.out.println("Э�����ܳ��ȣ�"+combined.length);
		return combined;
	}
	public static void main(String[] args) {
		String str = "hello server!"
				+"һֱ����Ϊ�Լ����������󣬾�������֪���������ĳ����ˡ�һ��Ҳ������ĩ����欣�������һ����ɫ�ķ�����\n"
				+"�Թ����ֲ��Զ����ĸо���ϲ������������������Ȱ����ľ��������ѣ���Ľ���������뷴�����������������Ը��һ���⡣\n��Ϊָ��˭���಻Ϊ����˭��ֻԸΪ�Լ���һ�����У�ȥ���ݸ�����������"
				+"�Ҳ�֪��һ����ľ����ж�Զ���಻֪һ������ֻ��ܹ���Խ�Ŷ��ٵ������������۾����������⣬��ůů��̫���ֻ������أ�\nֻ��ʱ�⣬�������Ӵ���ԯ���ޣ������н�����"
				+"��ʱ���¶������Ǻ�ˮ������ǵؾ���������һ�У��Լ����Ƕϴ�һ�������������͵���ײ㣬����������Ӱ��һ˲��ʧ�������С�";
		Client c = new Client();
		System.err.println("�ַ������ȣ�"+str.length());
		c.beforeStart(str).start();
	}
}
