package normalbyte;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String>{

//	@Override
//	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		System.out.println("���շ��������͵���Ϣ��channelRead:"+new String(ByteBufUtil.decodeHexDump(msg.toString())));
//	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println("���շ��������͵���Ϣ��channelRead0:"+new String(ByteBufUtil.decodeHexDump(msg.toString())));
		//
	}

}
