package handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import vo.Message;

public class MessageHandler extends SimpleChannelInboundHandler<Message>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		System.out.println(msg.toString());
	}

//	@Override
//	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		Message mes = new Message();
//		mes.setSn(2);
//		mes.setMsg("---客户端发送消息到服务端---");
//		ctx.writeAndFlush(mes);
//	}

}
