package clienthandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    ChannelHandlerContext ctx;

    public void sendMessage(String msgToSend) {
        if (ctx != null) {
            ChannelFuture cf = ctx.write(Unpooled.copiedBuffer(msgToSend, CharsetUtil.UTF_8));
            ctx.flush();
            if (!cf.isSuccess()) {
                System.out.println("Send failed: " + cf.cause());
            }
        } else {
            //ctx not initialized yet. you were too fast. do something here
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext arg0, String msg) throws Exception {
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }
}