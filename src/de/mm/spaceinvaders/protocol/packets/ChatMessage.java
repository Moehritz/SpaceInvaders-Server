package de.mm.spaceinvaders.protocol.packets;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import de.mm.spaceinvaders.protocol.Packet;
import de.mm.spaceinvaders.server.PacketHandler;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ChatMessage extends Packet {

	private String msg;

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, msg);
	}

	@Override
	public void read(ByteBuf buf) {
		this.msg = readString(buf);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}

}
