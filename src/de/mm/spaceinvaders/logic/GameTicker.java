package de.mm.spaceinvaders.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.protocol.packets.DespawnEntity;
import de.mm.spaceinvaders.protocol.packets.SpawnEntity;

public class GameTicker
{

	public static int tps = 20;

	private Timer timer;
	private long last = System.currentTimeMillis();

	public void start()
	{
		timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				runTick();
			}
		}, 0, 1000 / tps);
	}

	public void stop()
	{
		if (timer != null) timer.cancel();
	}

	public void runTick()
	{
		long thisFrame = System.currentTimeMillis();
		long delta = thisFrame - last;
		last = thisFrame;
		Game g = Game.getCurrentGame();
		List<Packet> sendThem = new ArrayList<>();
		synchronized (g.getOutstandingSpawns())
		{
			for (Entity e : g.getOutstandingSpawns())
			{
				if (g.getEntities().contains(e))
				{
					g.getEntities().remove(e);
					sendThem.add(new DespawnEntity(e.getUuid()));
				}
				else
				{
					g.getEntities().add(e);
					sendThem.add(new SpawnEntity(e.getUuid(), e.getX(), e.getY(), e
							.getRotation(), e.getType()));
				}
			}
			g.getOutstandingSpawns().clear();
		}
		for (Player p : g.getPlayers())
			p.getConnnection().send(sendThem.toArray(new Packet[sendThem.size()]));
		List<Entity> allEntities = new ArrayList<>(g.getEntities());
		for (Entity e : allEntities)
		{
			boolean out = e.update(delta);
			if (!out && e instanceof Bullet)
			{
				e.despawn();
			}
		}
	}
}