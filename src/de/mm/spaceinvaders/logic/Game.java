package de.mm.spaceinvaders.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.mm.spaceinvaders.SpaceInvadersServer;
import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.protocol.packets.Respawn;
import de.mm.spaceinvaders.server.UserConnection;
import lombok.Getter;

public class Game
{
	@Getter
	private static Game currentGame;

	@Getter
	private List<Entity> entities = new ArrayList<>();
	@Getter
	private List<Entity> outstandingSpawns = new ArrayList<>();
	private GameTicker ticker;

	public Game()
	{
		if (currentGame != null) currentGame.ticker.stop();
		currentGame = this;

		System.out.println("! Lade Spiel...");
		ticker = new GameTicker();
		for (UserConnection uc : SpaceInvadersServer.getInstance().getConnectedPlayers())
		{
			Player p = new Player(uc, this);
			System.out.println("! F�ge " + p.getName() + " zum Spiel hinzu.");
			entities.add(p);
		}
		System.out.println("! " + entities.size() + " sind in dem Spiel.");

		ticker.start();

		System.out.println("Neues Spiel gestartet.");
	}

	public Packet getRespawnPacket()
	{
		Respawn r = new Respawn();
		Random rand = new Random();
		r.setX(rand.nextDouble());
		r.setY(rand.nextDouble());
		r.setRotation(rand.nextInt(360));
		return r;
	}

	public Player getPlayer(String name)
	{
		for (Entity e : entities)
		{
			if (e instanceof Player)
			{
				Player p = (Player) e;
				if (p.getName().equalsIgnoreCase(name)) return p;
			}
		}
		return null;
	}

	public List<Player> getPlayers()
	{
		List<Player> ret = new ArrayList<>();
		for (Entity e : entities)
			if (e instanceof Player) ret.add((Player) e);
		return ret;
	}

	public void prepareSpawn(Entity e)
	{
		if (!outstandingSpawns.contains(e)) outstandingSpawns.add(e);
	}

	public void prepareDespawn(Entity e)
	{
		if (entities.contains(e)) outstandingSpawns.add(e);
	}
}