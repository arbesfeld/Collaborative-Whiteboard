package packet;

import java.io.Serializable;

public abstract class Packet implements Serializable {
	private static final long serialVersionUID = -2598835099616000879L;
	
	public abstract void process(PacketHandler handler);
}
