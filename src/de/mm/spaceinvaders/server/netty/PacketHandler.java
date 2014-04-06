package de.mm.spaceinvaders.server.netty;

import io.netty.channel.Channel;
import de.mm.spaceinvaders.protocol.Packet;

public abstract class PacketHandler extends AbstractPacketHandler
{

	public void handle(Packet packet) throws Exception
	{
	}

	public void connected(Channel ch) throws Exception
	{
	}

	public void disconnected(Channel ch) throws Exception
	{
	}
}
