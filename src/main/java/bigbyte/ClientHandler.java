package bigbyte;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<byte[]>{

	/**
	 * �����������ݵ��ֽڳ��ȳ���1024������ʹ���ĸ������������ݣ����ǲ������ķְ�����
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("�������������ֽڳ��ȣ�"+((byte[])msg).length);
		System.err.println("Server said:"+new String((byte[])msg));
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
//		System.out.println("�������������ֽڳ��ȣ�"+msg.length);
//		System.err.println("Server said:"+new String(msg));
	}

}
