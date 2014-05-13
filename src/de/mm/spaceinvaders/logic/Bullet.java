package de.mm.spaceinvaders.logic;

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
	}

}
