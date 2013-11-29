package packet;

import name.BoardIdentifier;
import name.ClientIdentifier;

public final class PacketExitBoard extends Packet {
	private static final long serialVersionUID = -1888945253238858839L;

	@Override
	public void process(PacketHandler handler) {
		handler.receivedExitBoardPacket(this);
	}
}