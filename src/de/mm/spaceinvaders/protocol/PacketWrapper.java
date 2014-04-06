package de.mm.spaceinvaders.protocol;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PacketWrapper
{

	private Packet packet;
	private ByteBuf buf;

	public void release()
	{
		//buf.release();
	}

}
