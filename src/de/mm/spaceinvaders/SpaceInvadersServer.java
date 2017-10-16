package de.mm.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import de.mm.spaceinvaders.logic.Game;
import de.mm.spaceinvaders.logic.Player;
import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.protocol.Protocol;
import de.mm.spaceinvaders.protocol.packets.*;

import de.mm.spaceinvaders.server.ServerHandler;
import de.mm.spaceinvaders.server.UserConnection;

import lombok.Getter;

@Getter
public class SpaceInvadersServer
{

	@Getter
	private static SpaceInvadersServer instance;

	public static void main(String[] args)
	{
		System.out.println("Starte SpaceInvaders Server");

		new Protocol();
		System.out.println("Protocol Version: " + Protocol.PROTOCOL_VERSION);

		instance = new SpaceInvadersServer();
		instance.start();
	}

	private List<UserConnection> connectedPlayers;

	private void start()
	{
		connectedPlayers = new ArrayList<>();

		System.out.println("Starte Netty-Server...");
		new Thread(new ServerHandler(8889)).start();
	}

	public void login(UserConnection serverPacketHandler)
	{
		Packet login = new UserJoin(serverPacketHandler.getName(),
				serverPacketHandler.getUuid());
		Packet chat = new ChatMessage("> " + serverPacketHandler.getName()
				+ " hat das Spiel betreten.");
		System.out.println("> " + serverPacketHandler.getName()
				+ " hat das Spiel betreten.");
        serverPacketHandler.send(new Respawn());
        serverPacketHandler.send(new UserJoin(serverPacketHandler.getName(),
                serverPacketHandler.getUuid()));
		for (UserConnection u : connectedPlayers)
		{
			u.send(login, chat);
            u.send(new SpawnEntity(serverPacketHandler.getUuid(), 0, 0, 0, (byte) 1));
            serverPacketHandler.send(new UserJoin(u.getName(), u.getUuid()));
            serverPacketHandler.send(new SpawnEntity(u.getUuid(), 0, 0, 0, (byte) 1));
		}
		connectedPlayers.add(serverPacketHandler);

        Game.getCurrentGame().prepareSpawn(new Player(serverPacketHandler, Game.getCurrentGame()));
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
						+ " hat sich versucht falsch umzubenennen: \"" + newName
						+ "\" Er wurde gekickt.");
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