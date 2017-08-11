package bigbyte;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<byte[]>{

	/**
	 * 当服务器传递的字节长度超过1024，不管使用哪个方法接收数据，都是不完整的分包数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("服务器传过来字节长度："+((byte[])msg).length);
		System.err.println("Server said:"+new String((byte[])msg));
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
//		System.out.println("服务器传过来字节长度："+msg.length);
//		System.err.println("Server said:"+new String(msg));
	}

}
