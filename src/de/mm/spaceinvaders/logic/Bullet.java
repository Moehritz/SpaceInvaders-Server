package de.mm.spaceinvaders.logic;

import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.protocol.packets.UpdatePosition;
import lombok.Getter;
import lombok.Setter;

public class Bullet extends Entity
{
	@Getter
	@Setter
	private Entity sender;

	public Bullet(Game game)
	{
		super(game);
		setType((byte) 2);
	}

	@Override
	public boolean update(long delta)
	{
		boolean ret = super.update(delta);
		Packet pos = new UpdatePosition(getUuid(), getX(), getY(), getRotation(),
				getSpeed());
		for (Player p : Game.getCurrentGame().getPlayers())
			p.getConnnection().send(pos);
		return ret;
	}

}
