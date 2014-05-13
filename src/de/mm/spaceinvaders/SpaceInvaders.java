package de.mm.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.protocol.Protocol;
import de.mm.spaceinvaders.protocol.packets.ChatMessage;
import de.mm.spaceinvaders.protocol.packets.GameStart;
import de.mm.spaceinvaders.protocol.packets.UpdatePlayerName;
import de.mm.spaceinvaders.protocol.packets.UserJoin;
import de.mm.spaceinvaders.protocol.packets.UserLeave;
import de.mm.spaceinvaders.server.Server;
import de.mm.spaceinvaders.server.ServerPacketHandler;
import lombok.Getter;

@Getter
public class SpaceInvaders
{

	@Getter
	private static SpaceInvaders instance;

	public static void main(String[] args)
	{
		new Protocol();

		instance = new SpaceInvaders();
		instance.start();
	}

	private List<ServerPacketHandler> connectedPlayers;

	private void start()
	{
		connectedPlayers = new ArrayList<>();

		Server.startServer(8888);
	}

	public void login(ServerPacketHandler serverPacketHandler)
	{
		Packet login = new UserJoin(serverPacketHandler.getName(),
				serverPacketHandler.getUuid());
		Packet chat = new ChatMessage("> " + serverPacketHandler.getName()
				+ " hat das Spiel betreten.");
		System.out.println("> " + serverPacketHandler.getName()
				+ " hat das Spiel betreten.");
		connectedPlayers.add(serverPacketHandler);
		for (ServerPacketHandler u : connectedPlayers)
		{
			u.send(login, chat);
			serverPacketHandler.send(new UserJoin(u.getName(), u.getUuid()));
		}
		System.out.println(serverPacketHandler.getUuid());
	}

	public void logout(ServerPacketHandler con)
	{
		connectedPlayers.remove(con);
		Packet logout = new UserLeave(con.getName());
		Packet chat = new ChatMessage("> " + con.getName()
				+ " hat die Verbindung getrennt.");
		System.out.println("> " + con.getName() + " hat die Verbindung getrennt.");
		for (ServerPacketHandler u : connectedPlayers)
		{
			u.send(logout, chat);
		}
	}

	public void chat(ServerPacketHandler con, String message)
	{
		Packet packet = new ChatMessage(con.getName() + ": " + message);
		for (ServerPacketHandler u : connectedPlayers)
		{
			u.send(packet);
		}
	}

	public void changeName(ServerPacketHandler con, String newName)
	{
		Packet chat = new ChatMessage("> " + con.getName() + " hat sich in " + newName
				+ " umbenannt.");
		Packet rename = new UpdatePlayerName(con.getUuid(), newName);

		for (ServerPacketHandler c : connectedPlayers)
		{
			c.send(chat, rename);
		}
		con.setName(newName);

		con.send(new GameStart());
	}

}
