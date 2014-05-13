package de.mm.spaceinvaders.server;

public class Server
{

	public static void startServer(int port)
	{
		new Thread(new ServerConnectionHandler(port)).start();
	}

}
