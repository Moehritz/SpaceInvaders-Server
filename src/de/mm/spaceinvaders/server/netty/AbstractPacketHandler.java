package de.mm.spaceinvaders.server.netty;

import de.mm.spaceinvaders.protocol.packets.ChatMessage;
import de.mm.spaceinvaders.protocol.packets.Login;

public abstract class AbstractPacketHandler
{

	public void handle(Login login) throws Exception
	{
	}

	public void handle(ChatMessage chatMessage) throws Exception
	{
	}

}
