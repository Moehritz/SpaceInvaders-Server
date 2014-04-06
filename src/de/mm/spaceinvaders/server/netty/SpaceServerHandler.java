package de.mm.spaceinvaders.server.netty;

import lombok.NonNull;
import de.mm.spaceinvaders.protocol.PacketWrapper;
import de.mm.spaceinvaders.server.UserConnection;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SpaceServerHandler extends ChannelHandlerAdapter
{

	@NonNull
	private PacketHandler handler;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		System.out.println("Connected");
		handler = new UserConnection();
		handler.connected(ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		handler.disconnected(ctx.channel());
		System.out.println(handler + " disconnected.");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		System.out.println("Packet received");
		PacketWrapper p = (PacketWrapper) msg;
		try
		{
			p.getPacket().handle(handler);
		}
		finally
		{
			p.release();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception
	{
		cause.printStackTrace();
		handler.disconnected(ctx.channel());
		ctx.close();
	}
}
