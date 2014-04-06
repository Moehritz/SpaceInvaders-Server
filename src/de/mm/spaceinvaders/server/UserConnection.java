package de.mm.spaceinvaders.server;

import io.netty.channel.Channel;
import de.mm.spaceinvaders.SpaceInvaders;
import de.mm.spaceinvaders.logic.Player;
import de.mm.spaceinvaders.protocol.packets.ChatMessage;
import de.mm.spaceinvaders.protocol.packets.Login;
import de.mm.spaceinvaders.server.netty.PacketHandler;

public class UserConnection extends PacketHandler
{

	private Player player;
	private Channel ch;

	@Override
	public void connected(Channel ch) throws Exception
	{
		player = new Player();
		this.ch = ch;
	}

	@Override
	public void handle(Login login) throws Exception
	{
		if (login.getVersion() != SpaceInvaders.getProtocolVersion())
		{
			ch.close();
		}
		player.setName(login.getName());
		player.login();
		System.out.println(player.getName() + " logged in.");
	}

	@Override
	public void handle(ChatMessage chatMessage) throws Exception
	{
		System.out.println(player.getName() + ": " + chatMessage.getMessage());
	}

	@Override
	public String toString()
	{
		return player == null ? "mäh" : player.getName();
	}

}
