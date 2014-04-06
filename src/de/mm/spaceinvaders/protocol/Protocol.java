package de.mm.spaceinvaders.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.mm.spaceinvaders.protocol.packets.ChatMessage;
import de.mm.spaceinvaders.protocol.packets.Login;

public class Protocol
{

	public static Protocol prot;

	private Map<Integer, Class<? extends Packet>> packets = new HashMap<>();

	public Protocol()
	{
		prot = this;

		packets.put(0, ChatMessage.class);
		packets.put(1, Login.class);
	}

	public Packet createPacket(int id)
	{
		try
		{
			return packets.get(id).newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public int getPacketId(Class<? extends Packet> c)
	{
		for (Entry<Integer, Class<? extends Packet>> e : packets.entrySet())
		{
			if (e.getValue().equals(c)) return e.getKey();
		}
		return 0;
	}

}
