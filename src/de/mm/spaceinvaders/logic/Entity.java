package de.mm.spaceinvaders.logic;

import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import de.mm.spaceinvaders.util.Vector;

@Getter
@Setter
public class Entity
{

	@NonNull
	private final String uuid;

	private float x = 200, y = 200, health, width, height;
	private double rotation = 0;
	private Vector speed = new Vector();
	private boolean visible = true;

	private Game game;

	public Entity(Game game)
	{
		this(UUID.randomUUID().toString(), game);
	}

	public Entity(String uuid, Game game)
	{
		this.uuid = uuid;
		this.game = game;
	}

	/**
	 * UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3
	 */
	public boolean outOfBounds(int side)
	{
		switch (side)
		{
		case 0:
			return y + height / 2 > 10;// Display.getHeight();
		case 1:
			return x + width / 2 > 10;// Display.getWidth();
		case 2:
			return y - height / 2 < 0;
		case 3:
			return x - width / 2 < 0;
		}
		return false;
	}

	public boolean update(long delta)
	{
		boolean ret = true;
		if ((!outOfBounds(1) && speed.getX() > 0)
				|| (!outOfBounds(3) && speed.getX() < 0))
		{
			x += speed.getX() * (double) delta;
		}
		else
		{
			ret = false;
		}
		if ((!outOfBounds(0) && speed.getY() > 0)
				|| (!outOfBounds(2) && speed.getY() < 0))
		{
			y += speed.getY() * (double) delta;
		}
		else
		{
			ret = false;
		}

		// SpaceInvaders.getInstance().getClient()
		// .write(new UpdatePosition(uuid, x, y, rotation));

		if (speed.getX() < 0.01 && speed.getX() > -0.01) speed.setX(0);
		if (speed.getY() < 0.01 && speed.getY() > -0.01) speed.setY(0);
		return ret;
	}

	public void despawn()
	{
		game.prepareSpawn(this);
	}
}
