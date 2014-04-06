package de.mm.spaceinvaders.protocol.packets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.netty.buffer.ByteBuf;
import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.server.netty.PacketHandler;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Login extends Packet
{

	private String name;
	private int version;

	@Override
	public void read(ByteBuf buf)
	{
		this.name = readString(buf);
		this.version = buf.readInt();
	}

	@Override
	public void write(ByteBuf buf)
	{
		writeString(buf, name);
		buf.writeInt(version);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception
	{
		handler.handle(this);
	}

}
