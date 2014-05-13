package de.mm.spaceinvaders.server;

import lombok.Getter;
import lombok.Setter;
import de.mm.spaceinvaders.SpaceInvaders;
import de.mm.spaceinvaders.io.PacketHandler;
import de.mm.spaceinvaders.logic.Game;
import de.mm.spaceinvaders.logic.Player;
import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.protocol.Protocol;
import de.mm.spaceinvaders.protocol.packets.ChangeName;
import de.mm.spaceinvaders.protocol.packets.ChatMessage;
import de.mm.spaceinvaders.protocol.packets.GameStart;
import de.mm.spaceinvaders.protocol.packets.JoinGame;
import de.mm.spaceinvaders.protocol.packets.Login;
import de.mm.spaceinvaders.protocol.packets.ResetGame;
import de.mm.spaceinvaders.protocol.packets.UpdatePosition;

@Getter
@Setter
public class UserConnection extends PacketHandler
{
	private String name = "_unknown";
	private String uuid = "";

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
	public void handle(ChangeName changeName) throws Exception
	{
		SpaceInvaders.getInstance().changeName(this, changeName.getName());
	}

	@Override
	public void handle(ResetGame reset) throws Exception
	{
		new Game();
		Packet chat = new ChatMessage(getName() + " hat das Spiel neugestartet!");
		for (UserConnection uc : SpaceInvaders.getInstance().getConnectedPlayers())
		{
			uc.send(chat);
		}
	}

	@Override
	public void handle(UpdatePosition pos) throws Exception
	{
		for (Player p : Game.getCurrentGame().getPlayers())
		{
			if (p.getName().equalsIgnoreCase(getName()))
			{
				p.setX(pos.getX());
				p.setY(pos.getY());
				p.setRotation(pos.getRotation());
				p.setSpeed(pos.getSpeed());
			}
			else
			{
				p.getConnnection().send(pos);
			}
		}
	}

	@Override
	public void handle(GameStart start) throws Exception
	{
		Packet join = new JoinGame(getUuid());
		Packet chat = new ChatMessage(getName() + " tritt dem Kampf bei!");
		for (Player p : Game.getCurrentGame().getPlayers())
		{
			p.getConnnection().send(join, chat);
			send(new JoinGame(p.getUuid()));
		}
		Game.getCurrentGame().prepareSpawn(new Player(this, Game.getCurrentGame()));
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
