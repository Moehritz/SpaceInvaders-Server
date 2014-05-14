package de.mm.spaceinvaders.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
		synchronized (g.getOutstandingSpawns())
		{
			for (Entity e : g.getOutstandingSpawns())
			{
				if (!g.getEntities().contains(e)) g.getEntities().add(e);
			}
			if (g.getOutstandingSpawns().size() != 0)
				System.out.println(g.getOutstandingSpawns().size()
						+ " Entities wurden gespawnt.");
			g.getOutstandingSpawns().clear();
		}
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