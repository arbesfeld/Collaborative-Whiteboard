package packet;

import layer.LayerProperties;

public class PacketLayerOrderList extends Packet {
	
	private static final long serialVersionUID = 5126362530793078834L;
	private final LayerProperties[] layers;
	
	public PacketLayerOrderList(LayerProperties[] layers){
		this.layers = layers;
	}
	
	public LayerProperties[] layers() {
		return layers.clone();
	}

	@Override
	public void process(PacketHandler handler) {
		handler.receivedLayerOrderListPacket(this);
		
	}
	
}
