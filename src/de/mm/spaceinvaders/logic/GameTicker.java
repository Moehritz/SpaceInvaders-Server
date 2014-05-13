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
				run();
			}
		}, 0, 1000 / tps);
	}

	public void stop()
	{
		if (timer != null) timer.cancel();
	}

	public void run()
	{
		long thisFrame = System.currentTimeMillis();
		long delta = thisFrame - last;
		last = thisFrame;
		List<Entity> allEntities = new ArrayList<>(Game.getCurrentGame().getEntities());
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