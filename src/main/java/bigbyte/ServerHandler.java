package bigbyte;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		String str = "ʱ�����ۣ������������µ�����ո����������¾ʹ���ʫ��֮��������������������ҵ��������£�\n"
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
		byte[] temp = str.getBytes();
		//���ַ����ֽڳ���1280������nettyĬ��1024����������У�Ĭ�ϻ�ְ����ͣ�����˳���β�˵�ͷ��
		//Ϊ��һ���Խ��������ַ������ͻ���Ҳ��ҪLengthFieldBasedFrameDecoder�Զ���Э����
		System.out.println("ԭʼ�ַ����ֽ��ܳ���(δ���͵��ͻ���ǰ)��"+temp.length);
		ctx.writeAndFlush(temp);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		byte[] mg = (byte[])msg;
		System.out.println("���յ����ֽڳ��ȣ�"+mg.length);
		String temp = new String(mg);
		System.out.println("client say:"+temp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
