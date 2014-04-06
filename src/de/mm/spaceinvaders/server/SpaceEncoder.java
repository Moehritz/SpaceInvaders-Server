package de.mm.spaceinvaders.server;

import de.mm.spaceinvaders.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class SpaceEncoder extends MessageToByteEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet p, ByteBuf out)
			throws Exception {
		p.write(out);
	}

}
