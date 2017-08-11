package bigbyte;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		String str = "时光荏苒，岁月如梭。七月的热情刚刚消隐，八月就带着诗意之美，翩翩而来，正好与我的美丽心事，\n"
				+ "撞了一个满怀。淡然的风，氤氲心头，萦绕绿窗，醉了一池心湖荡漾。端坐在八月的渡口，一些花依旧吐露着\n"
				+ "不惊不扰的芬芳。花开，花落，都是不尽的温柔。花开既是短暂，也要把最后一抹嫣然，开到绝艳，直至荼蘼。\n"
				+ "人生，其实就像一朵花，不管怎么样？我们都要尽量把自己开到绝艳。这种精神，需要的就是一种坚持。有些事情，我们必须坚持，\n"
				+ "如坚强的活着。正如一些爱情，明知不会开花结果，却依然不悔初衷的坚持着。爱，不需要轰轰烈烈，也不要因为惧怕结果是否遂愿？\n"
				+ "而选择放弃。倘若你连给自己一次真诚的付出的机会，都不给，就算会有花开蒂落的一天，也被你的不坚持而深深错过。\n"
				+ "有时候，坚持并不一定会有结果，但是，真爱，一定需要时间来证明。时间，逝而不返，感情，也是如此，一次的错过，\n"
				+ "或许会是一辈子的遗憾。爱错一个人，是一时的后悔，错过一个相爱的人，则是一生的遗憾。爱，并没有罪，相爱的人，\n"
				+ "就勇敢去追。爱到底有多长？就让时间来丈量。所有的心事，坦然而淡定。静静的依着门楣，任由窗外的绿意，铺天盖地。\n"
				+ "时至黄昏，最后一抹斜阳淹没在天边，是不舍，是留恋。纤月黄昏，庭院深深。清晰的是你印在我心底微笑的容颜。你说，\n"
				+ "我是你今生惊鸿一瞥的美，是一朵开在你心中的水莲，不胜娇羞；我说，你是我身处孤寂中的一杯美酒，饮不尽相思。思念，\n"
				+ "是一种植入骨髓的心痛。你看！这盛夏的藤蔓已经爬满了绿窗，你可知？那弥笃的相思，早已经葳蕤成无边的切切深情。\n";
		byte[] temp = str.getBytes();
		//该字符串字节长度1280，超过netty默认1024，传输过程中，默认会分包发送，传输顺序从尾端到头部
		//为了一次性接收完整字符串，客户端也需要LengthFieldBasedFrameDecoder自定义协议体
		System.out.println("原始字符串字节总长度(未发送到客户端前)："+temp.length);
		ctx.writeAndFlush(temp);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		byte[] mg = (byte[])msg;
		System.out.println("接收到的字节长度："+mg.length);
		String temp = new String(mg);
		System.out.println("client say:"+temp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
