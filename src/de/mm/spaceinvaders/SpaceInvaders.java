package de.mm.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.protocol.packets.ChatMessage;
import de.mm.spaceinvaders.protocol.packets.UserJoin;
import de.mm.spaceinvaders.protocol.packets.UserLeave;
import de.mm.spaceinvaders.server.Server;
import de.mm.spaceinvaders.server.UserConnection;
import lombok.Getter;

@Getter
public class SpaceInvaders
{

	@Getter
	private static SpaceInvaders instance;

	public static void main(String[] args)
	{
		instance = new SpaceInvaders();
		instance.start();
	}

	private List<UserConnection> connectedPlayers;

	private void start()
	{
		connectedPlayers = new ArrayList<>();

		try
		{
			new Server().run();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void login(UserConnection con)
	{
		Packet login = new UserJoin(con.getName());
		Packet chat = new ChatMessage("> " + con.getName()
				+ " hat die Verbindung getrennt.");
		for (UserConnection u : connectedPlayers)
		{
			u.send(login);
			u.send(chat);
		}
		connectedPlayers.add(con);
	}

	public void logout(UserConnection con)
	{
		connectedPlayers.remove(con);
		Packet logout = new UserLeave(con.getName());
		Packet chat = new ChatMessage("> " + con.getName()
				+ " hat die Verbindung getrennt.");
		for (UserConnection u : connectedPlayers)
		{
			u.send(logout);
			u.send(chat);
		}
	}

	public void chat(UserConnection con, String message)
	{
		Packet packet = new ChatMessage(con.getName() + ": " + message);
		for (UserConnection u : connectedPlayers)
		{
			u.send(packet);
		}
	}

	public void changeName(UserConnection con, String newName)
	{
		logout(con);

		con.setName(newName);

		login(con);
	}

}
