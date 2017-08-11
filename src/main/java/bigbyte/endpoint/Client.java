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
 * 传输大于1024长度的字节，客户端 封装数据包，定义数据长度以及内容体
 * 
 * @author Annie
 *
 */
//http://blog.csdn.net/suifeng3051/article/details/34444585
public final class Client {

	private static final int port = 4396;

	private static final String host = "localhost";

	 // 定义一个发送消息协议格式：|--header:4 byte--|--content:10MB--|
	/**默认消息头4位存储字节总长度**/
	private final int header = 4;
	/**内容体**/
	private byte[] content;

	public Client() {
		
	}
    
	/**
	 * msg对象需要重写toString方法，把需要传输的数据封装好
	 * @param msg
	 * @return
	 */
	public Client beforeStart(Object msg) {
		//获取一个4字节长度的协议体头(字节数组，msg对象长度以数值写入字节数组里)
		byte[] rebuildArray = byteArrayBuild(header, msg.toString().getBytes().length);
		System.out.print("msg长度："+msg.toString().getBytes().length+",协议头："+rebuildArray.length);
		//把msg对象转换成字节数组存储
		byte[] targetBytes = msg.toString().getBytes();
		//封装好带有协议体头以及内容的字节数组
		this.content = combineByteArray(rebuildArray, targetBytes);
		System.out.println("发送前的字符串："+new String(this.content));
		return this;
	}

	public void start() {
		try{
			if(this.content == null){
				throw new Exception("对象不能为空，请确保优先调用beforeStart");
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
				System.out.println("---成功连接到服务器---");
			}
			f.channel().writeAndFlush(content);
			f.channel().closeFuture().sync();
		}catch (Exception e){
			
		}
	}
    /**
     * 返回一个计算好总长度的字节数组(包含header、content)
     * 把目标对象的长度按照整型的数值写到4字节的byte数组里面。
     * @param byteLength header的位数，用于存储字节总长度,这里使用4字节
     * @param targetLength 内容体的总长度
     * @return
     */
	private static byte[] byteArrayBuild(final int byteLength, int targetLength) {

		return ByteBuffer.allocate(byteLength).putInt(targetLength).array();
	}
    /**
     * 返回一个重组好字节的数组
     * @param array1 计算好长度的空字节数组
     * @param array2 带有数据量超1024字节的字节数组
     * @return
     */
	private static byte[] combineByteArray(byte[] array1, byte[] array2) {
		byte[] combined = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, combined, 0, array1.length);
		System.arraycopy(array2, 0, combined, array1.length, array2.length);
		System.out.println("协议体总长度："+combined.length);
		return combined;
	}
	public static void main(String[] args) {
		String str = "hello server!"
				+"一直都认为自己会慢慢长大，就这样不知不觉里就真的长大了。一切也都像日末般妖娆，卷乱另一场黑色的纷争。\n"
				+"对光有种不言而喻的感觉，喜欢它的张扬与活力，热爱它的绝望与破裂，羡慕它的挣扎与反抗。如果有来生，我愿做一道光。\n不为指引谁，亦不为光明谁，只愿为自己做一场旅行，去放纵浮华的脉动。"
				+"我不知道一光年的距离有多远，亦不知一光年的轮回能够穿越着多少的漫长。闭上眼睛，亲吻阳光，呃暖暖的太阳又回来了呢！\n只是时光，已是逆差。从此南辕北辙，不再有交集。"
				+"有时，孤独会像是洪水般扑天盖地卷来，湮灭一切；自己像是断代一样，被埋在世纪的最底层，看不到光与影，一瞬间失掉了所有。";
		Client c = new Client();
		System.err.println("字符串长度："+str.length());
		c.beforeStart(str).start();
	}
}
