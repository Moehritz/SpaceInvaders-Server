package de.mm.spaceinvaders.server;

import lombok.Getter;
import lombok.Setter;
import de.mm.spaceinvaders.SpaceInvaders;
import de.mm.spaceinvaders.io.PacketHandler;
import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.protocol.Protocol;
import de.mm.spaceinvaders.protocol.packets.ChangeName;
import de.mm.spaceinvaders.protocol.packets.ChatMessage;
import de.mm.spaceinvaders.protocol.packets.Login;
import de.mm.spaceinvaders.protocol.packets.UpdatePosition;

@Getter
@Setter
public class ServerPacketHandler extends PacketHandler
{
	private String name = "_unknown";
	private String uuid = "";

	@Override
	public void connected() throws Exception
	{
	}

	@Override
	public void disconnected() throws Exception
	{
		SpaceInvaders.getInstance().logout(this);
	}

	@Override
	public void handle(Login login) throws Exception
	{
		setName(login.getName());
		setUuid(login.getUuid());
		if (login.getVersion() != Protocol.PROTOCOL_VERSION)
		{
			System.out.println(getName() + " was disconnected. Wrong Protocol version");
			getConnection().closeConnection();
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
	public void handle(UpdatePosition pos) throws Exception
	{
		for (ServerPacketHandler c : SpaceInvaders.getInstance().getConnectedPlayers())
		{
			if (c == this)
			{
				continue;
			}
			c.send(pos);
		}
	}

	@Override
	public String toString()
	{
		return name;
	}

	public void send(Packet... packet)
	{
		getConnection().sendPackets(packet);
	}
}
