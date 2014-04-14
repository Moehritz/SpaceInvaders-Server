package de.mm.spaceinvaders.server;

public class Server
{

	public static void startServer(int port)
	{
		ServerConnectionHandler s = new ServerConnectionHandler(port);
		try
		{
			s.start();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
