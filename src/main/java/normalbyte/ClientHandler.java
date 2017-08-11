package normalbyte;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String>{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		
	}

}
