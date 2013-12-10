package packet;

import name.LayerIdentifier;
import canvas.layer.LayerAdjustment;
import canvas.layer.LayerProperties;

public class PacketLayerAdjustment extends Packet {
	private static final long serialVersionUID = 5126362530793078834L;
	private final LayerAdjustment adjustment;
	private final LayerProperties properties;
	
	public PacketLayerAdjustment(LayerProperties properties, LayerAdjustment adjustment){
		this.properties = properties;
		this.adjustment = adjustment;
	}

	public LayerProperties layerProperties() {
		return properties;
	}
	
	public LayerIdentifier layer() {
		return properties.layerIdentifier();
	}
	
	public LayerAdjustment adjustment() {
		return adjustment;
	}

	@Override
	public void process(PacketHandler handler) {
		handler.receivedLayerAdjustmentPacket(this);
	}
	
}
