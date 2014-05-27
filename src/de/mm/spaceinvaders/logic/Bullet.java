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
		setWidth(8 * 0.003125);
		setHeight(4 * 0.003125);
	}

	@Override
	public boolean update(long delta)
	{
		double xB = getX();
		double yB = getY();
		double rB = getRotation();
		boolean ret = super.update(delta);
		if (xB == getX() && yB == getY() && rB == getRotation()) return ret;
		Packet pos = new UpdatePosition(getUuid(), getX(), getY(), getRotation(),
				getSpeed());
		for (Player p : Game.getCurrentGame().getPlayers())
			p.getConnnection().send(pos);
		return ret;
	}

}
