package packet;

public class PacketNewLayer extends Packet {
	
	private final LayerIdentifier layerName;
	
	public PacketNewLayer(LayerIdentifier layerName){
		this.layerName=layerName;
	}
	
	public LayerIdentifier layerName() {
		return layerName;
	}
	@Override
	public void process(PacketHandler handler) {
		handler.receivedNewLayerPacket(this);
		
	}
	

}
