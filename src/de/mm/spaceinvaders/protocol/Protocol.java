package de.mm.spaceinvaders.protocol;

import java.util.HashMap;
import java.util.Map;

import de.mm.spaceinvaders.protocol.packets.ChatMessage;

public class Protocol {

	public static Protocol prot;

	private Map<Integer, Class<? extends Packet>> packets = new HashMap<>();

	public Protocol() {
		prot = this;

		packets.put(0, ChatMessage.class);
	}

	public Packet createPacket(int id) {
		try {
			return packets.get(id).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
