package de.mm.spaceinvaders;

import de.mm.spaceinvaders.server.Server;
import lombok.Getter;

@Getter
public class SpaceInvaders {

	@Getter
	private static SpaceInvaders instance;

	public static void main(String[] args) {
		instance = new SpaceInvaders();
		instance.start();
	}

	private void start() {
		try {
			new Server().run();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
