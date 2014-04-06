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
public class ChatMessage extends Packet
{

	private String message;

	@Override
	public void write(ByteBuf buf)
	{
		writeString(buf, message);
	}

	@Override
	public void read(ByteBuf buf)
	{
		this.message = readString(buf);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception
	{
		handler.handle(this);
	}

}
