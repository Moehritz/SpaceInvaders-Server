package de.mm.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.protocol.Protocol;
import de.mm.spaceinvaders.protocol.packets.ChatMessage;
import de.mm.spaceinvaders.protocol.packets.UpdatePlayerName;
import de.mm.spaceinvaders.protocol.packets.UserJoin;
import de.mm.spaceinvaders.protocol.packets.UserLeave;

import de.mm.spaceinvaders.server.ServerConnectionHandler;
import de.mm.spaceinvaders.server.UserConnection;

import lombok.Getter;

@Getter
public class SpaceInvaders
{

	@Getter
	private static SpaceInvaders instance;

	public static void main(String[] args)
	{
		System.out.println("Starting SpaceInvaders Server");

		new Protocol();
		System.out.println("Protocol Version: " + Protocol.PROTOCOL_VERSION);

		instance = new SpaceInvaders();
		instance.start();
	}

	private List<UserConnection> connectedPlayers;

	private void start()
	{
		connectedPlayers = new ArrayList<>();

		System.out.println("Starting server...");
		new Thread(new ServerConnectionHandler(8888)).start();
	}

	public void login(UserConnection serverPacketHandler)
	{
		Packet login = new UserJoin(serverPacketHandler.getName(),
				serverPacketHandler.getUuid());
		Packet chat = new ChatMessage("> " + serverPacketHandler.getName()
				+ " hat das Spiel betreten.");
		System.out.println("> " + serverPacketHandler.getName()
				+ " hat das Spiel betreten.");
		for (UserConnection u : connectedPlayers)
		{
			u.send(login, chat);
			serverPacketHandler.send(new UserJoin(u.getName(), u.getUuid()));
		}
		serverPacketHandler.send(new UserJoin(serverPacketHandler.getName(),
				serverPacketHandler.getUuid()));
		connectedPlayers.add(serverPacketHandler);
	}

	public void logout(UserConnection con)
	{
		if (!connectedPlayers.contains(con)) return;
		connectedPlayers.remove(con);
		Packet logout = new UserLeave(con.getName());
		Packet chat = new ChatMessage("> " + con.getName()
				+ " hat die Verbindung getrennt.");
		System.out.println("> " + con.getName() + " hat die Verbindung getrennt.");
		for (UserConnection u : connectedPlayers)
		{
			u.send(logout, chat);
		}
	}

	public void changeName(UserConnection con, String newName)
	{
		for (UserConnection uc : connectedPlayers)
		{
			if (uc.getName().equalsIgnoreCase(newName))
			{
				System.out.println(con.getName()
						+ " tried to give himself a already taken username: \"" + newName
						+ "\" He has been disconnected.");
				con.getConnection().closeConnection();
			}
		}

		ChatMessage chat = new ChatMessage("> " + con.getName() + " hat sich in "
				+ newName + " umbenannt.");
		Packet rename = new UpdatePlayerName(con.getUuid(), newName);

		System.out.println(chat.getMessage());

		for (UserConnection c : connectedPlayers)
		{
			c.send(chat, rename);
		}
		con.setName(newName);
	}
}