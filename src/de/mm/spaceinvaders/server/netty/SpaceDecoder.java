package de.mm.spaceinvaders.server.netty;

import java.util.List;

import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.protocol.PacketWrapper;
import de.mm.spaceinvaders.protocol.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SpaceDecoder extends ByteToMessageDecoder
{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out)
			throws Exception
	{
		if (buf.readableBytes() < 8)
		{
			return;
		}
		int packetID = buf.readInt();

		Packet packet = Protocol.prot.createPacket(packetID);

		packet.read(buf);

		out.add(new PacketWrapper(packet, buf));
	}
}
