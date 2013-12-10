package packet;

import name.LayerIdentifier;
import canvas.layer.LayerAdjustment;

public class PacketLayerAdjustment extends Packet {
	private static final long serialVersionUID = 5126362530793078834L;
	private final LayerIdentifier layer;
	private final LayerAdjustment adjustment;
	
	public PacketLayerAdjustment(LayerIdentifier layer, LayerAdjustment adjustment){
		this.layer = layer;
		this.adjustment = adjustment;
	}
	
	public LayerIdentifier layer() {
		return layer;
	}
	
	public LayerAdjustment adjustment() {
		return adjustment;
	}

	@Override
	public void process(PacketHandler handler) {
		handler.receivedLayerAdjustmentPacket(this);
	}
	
}
