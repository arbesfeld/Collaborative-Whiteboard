package packet;

import name.LayerIdentifier;
public class PacketLayerOrderList extends Packet {
	
	private static final long serialVersionUID = 5126362530793078834L;
	private final LayerIdentifier[] layers;
	
	public PacketLayerOrderList(LayerIdentifier[] layers){
		this.layers = layers;
	}
	
	public LayerIdentifier[] layers() {
		return layers.clone();
	}

	@Override
	public void process(PacketHandler handler) {
		handler.receivedLayerOrderListPacket(this);
		
	}
	
}
