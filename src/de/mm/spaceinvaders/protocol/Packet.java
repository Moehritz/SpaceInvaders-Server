package de.mm.spaceinvaders.protocol;

import de.mm.spaceinvaders.server.netty.PacketHandler;
import io.netty.buffer.ByteBuf;

public abstract class Packet
{

	public void read(ByteBuf buf)
	{
		throw new UnsupportedOperationException("Packet must implement read method");
	}

	public void write(ByteBuf buf)
	{
		throw new UnsupportedOperationException("Packet must implement write method");
	}

	public abstract void handle(PacketHandler handler) throws Exception;

	public static String readString(ByteBuf buf)
	{
		int l = buf.readInt();
		byte[] b = new byte[l];
		buf.readBytes(b);
		return new String(b);
	}

	public static void writeString(ByteBuf buf, String s)
	{
		byte[] b = s.getBytes();
		int l = b.length;
		buf.writeInt(l);
		buf.writeBytes(b);
	}

}
