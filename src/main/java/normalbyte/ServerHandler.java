package normalbyte;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String msg = "i've got it,i will do it as soon as possible!s";
		String target = ByteBufUtil.hexDump(msg.getBytes());
		ctx.writeAndFlush(target);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String hexStr = msg.toString();
		System.err.println("16进制字节："+hexStr);
		System.err.println("其对应的执行命令："+new String(ByteBufUtil.decodeHexDump(hexStr)));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
