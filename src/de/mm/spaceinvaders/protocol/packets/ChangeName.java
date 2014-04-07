package de.mm.spaceinvaders.protocol.packets;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.server.netty.PacketHandler;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ChangeName extends Packet
{

	private String name;

	@Override
	public void read(ByteBuf buf)
	{
		this.name = readString(buf);
	}

	@Override
	public void write(ByteBuf buf)
	{
		writeString(buf, name);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception
	{
		handler.handle(this);
	}
}
