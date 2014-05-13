package de.mm.spaceinvaders.logic;

import de.mm.spaceinvaders.server.UserConnection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player extends Entity
{
	@Getter
	private static final int shotCooldown = 100, maxAmmo = 50;

	private int ammo = maxAmmo;
	private long lastShot;

	private final UserConnection connnection;

	private int score;

	public Player(UserConnection con, Game game)
	{
		super(con.getUuid(), game);
		this.connnection = con;
	}

	public void setAmmo(int ammo)
	{
		this.ammo = ammo;
	}
	
	public String getName() {
		return getConnnection().getName();
	}

	public void shoot()
	{
		setAmmo(ammo - 1);
		// TODO
	}

	@Override
	public boolean update(long delta)
	{
		return super.update(delta);
	}
}
