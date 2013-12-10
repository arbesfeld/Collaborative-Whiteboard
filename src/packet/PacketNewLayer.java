package packet;

import name.LayerIdentifier;

public class PacketNewLayer extends Packet {
	
	private static final long serialVersionUID = 2258037891058384987L;
	private final LayerIdentifier layerName;
	
	public PacketNewLayer(LayerIdentifier layerName){
		this.layerName = layerName;
	}
	
	public LayerIdentifier layerName() {
		return layerName;
	}
	
	@Override
	public void process(PacketHandler handler) {
		handler.receivedNewLayerPacket(this);	
	}
	

}
