package packet;

public class PacketMessage extends Packet {
	private static final long serialVersionUID = 678813893543304091L;
	
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
