package de.mm.spaceinvaders;

import de.mm.spaceinvaders.server.Server;
import lombok.Getter;

@Getter
public class SpaceInvaders
{

	@Getter
	private static SpaceInvaders instance;
	@Getter
	private static int protocolVersion = 1;

	public static void main(String[] args)
	{
		System.out.println("Server");
		instance = new SpaceInvaders();
		instance.start();
	}

	private void start()
	{
		try
		{
			new Server().run();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
