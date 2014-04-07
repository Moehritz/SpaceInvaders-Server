package de.mm.spaceinvaders.server;

import lombok.Getter;
import lombok.Setter;
import io.netty.channel.Channel;
import de.mm.spaceinvaders.SpaceInvaders;
import de.mm.spaceinvaders.io.PacketHandler;
import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.protocol.Protocol;
import de.mm.spaceinvaders.protocol.packets.ChangeName;
import de.mm.spaceinvaders.protocol.packets.ChatMessage;
import de.mm.spaceinvaders.protocol.packets.Login;

@Getter
@Setter
public class UserConnection extends PacketHandler
{

	private String name = "_unknown";
	private Channel ch;

	@Override
	public void connected(Channel ch) throws Exception
	{
		this.ch = ch;
	}

	@Override
	public void handle(Login login) throws Exception
	{
		setName(login.getName());
		if (login.getVersion() != Protocol.PROTOCOL_VERSION)
		{
			System.out.println(getName() + " was disconnected. Wrong Protocol version");
			ch.close();
			return;
		}
		SpaceInvaders.getInstance().login(this);
	}

	@Override
	public void handle(ChatMessage chatMessage) throws Exception
	{
		SpaceInvaders.getInstance().chat(this, chatMessage.getMessage());
	}

	@Override
	public void handle(ChangeName changeName) throws Exception
	{
		SpaceInvaders.getInstance().changeName(this, changeName.getName());
	}

	@Override
	public String toString()
	{
		return name;
	}

	public void send(Packet... packet)
	{
		ch.writeAndFlush(packet);
	}

}
