package de.mm.spaceinvaders.logic;

import java.util.ArrayList;
import java.util.List;

import de.mm.spaceinvaders.SpaceInvaders;
import de.mm.spaceinvaders.server.UserConnection;
import lombok.Getter;

public class Game
{

	public static int WIDTH = 800;
	public static int HEIGHT = 600;

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

		ticker = new GameTicker();
		for (UserConnection uc : SpaceInvaders.getInstance().getConnectedPlayers())
		{
			Player p = new Player(uc, this);
			entities.add(p);
		}
		System.out.println(entities.size() + " players are in the game.");

		ticker.run();

		System.out.println("New Game started.");
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
}