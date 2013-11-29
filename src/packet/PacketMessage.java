package packet;

public class PacketMessage extends Packet {
	private final String text;
	
	public PacketMessage(String text) {
		this.text = text;
	}
	
	public String text() {
		return text;
	}
	
	@Override
	public void process(PacketHandler handler) {
		handler.receivedMessagePacket(this);
	}
}
