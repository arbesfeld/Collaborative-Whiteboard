package packet;

public class PacketClientReady extends Packet {
	private static final long serialVersionUID = 5713340681218216132L;

	@Override
	public void process(PacketHandler handler) {
		handler.receivedClientReadyPacket(this);
	}
}
