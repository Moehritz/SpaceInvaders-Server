package de.mm.spaceinvaders.server.netty;

import de.mm.spaceinvaders.protocol.packets.ChangeName;
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

	public void handle(ChangeName changeName) throws Exception
	{
	}

}
