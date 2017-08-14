package normalbyte;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String msg = "ʱ�����ۣ������������µ�����ո����������¾ʹ���ʫ��֮��������������������ҵ��������£�\n"
				+ "ײ��һ����������Ȼ�ķ磬����ͷ�������̴�������һ���ĺ������������ڰ��µĶɿڣ�һЩ��������¶��\n"
				+ "�������ŵķҷ������������䣬���ǲ��������ᡣ�������Ƕ��ݣ�ҲҪ�����һĨ��Ȼ���������ޣ�ֱ��ݱ�¡�\n"
				+ "��������ʵ����һ�仨��������ô�������Ƕ�Ҫ�������Լ��������ޡ����־�����Ҫ�ľ���һ�ּ�֡���Щ���飬���Ǳ����֣�\n"
				+ "���ǿ�Ļ��š�����һЩ���飬��֪���Ὺ�������ȴ��Ȼ���ڳ��Եļ���š���������Ҫ������ң�Ҳ��Ҫ��Ϊ���½���Ƿ���Ը��\n"
				+ "��ѡ������������������Լ�һ����ϵĸ����Ļ��ᣬ��������������л��������һ�죬Ҳ����Ĳ���ֶ���������\n"
				+ "��ʱ�򣬼�ֲ���һ�����н�������ǣ��氮��һ����Ҫʱ����֤����ʱ�䣬�Ŷ����������飬Ҳ����ˣ�һ�εĴ����\n"
				+ "�������һ���ӵ��ź�������һ���ˣ���һʱ�ĺ�ڣ����һ���మ���ˣ�����һ�����ź���������û����మ���ˣ�\n"
				+ "���¸�ȥ׷���������ж೤������ʱ�������������е����£�̹Ȼ��������������������鹣����ɴ�������⣬����ǵء�\n"
				+ "ʱ���ƻ裬���һĨб����û����ߣ��ǲ��ᣬ�����������»ƻ裬ͥԺ�������������ӡ�����ĵ�΢Ц�����ա���˵��\n"
				+ "�������������һƳ��������һ�俪�������е�ˮ������ʤ���ߣ���˵�����������¼��е�һ�����ƣ���������˼��˼�\n"
				+ "��һ��ֲ��������ʹ���㿴����ʢ�ĵ������Ѿ��������̴������֪�������Ƶ���˼�����Ѿ���ި���ޱߵ��������顣\n";
		System.out.println("�����ַ����ĳ��ȣ�"+msg.toString().getBytes().length);
		String target = ByteBufUtil.hexDump(msg.getBytes());
		System.out.println("����16�����ַ����ĳ��ȣ�"+target.getBytes().length);
		ctx.writeAndFlush(target);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String hexStr = msg.toString();
		System.err.println("16�����ֽڣ�"+hexStr);
		System.err.println("���Ӧ��ִ�����"+new String(ByteBufUtil.decodeHexDump(hexStr)));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
