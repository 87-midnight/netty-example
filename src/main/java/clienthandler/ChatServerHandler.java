package clienthandler;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

	private static List<Channel> channels = new ArrayList<>();
	  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
	    Channel incoming = ctx.channel();
	    channels.add(ctx.channel());
	  }

	  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
	    Channel incoming = ctx.channel();
	    channels.remove(ctx.channel());
	  } 

	  @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	    cause.printStackTrace();
	    ctx.close();
	  }

	  @Override
	    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
	    System.out.println(msg);
	  }
	}