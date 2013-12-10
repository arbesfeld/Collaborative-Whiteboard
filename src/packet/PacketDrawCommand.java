package packet;

import name.LayerIdentifier;
import canvas.command.DrawCommand;

/**
 * Class that represents a DrawCommand packet
 *
 */
public final class PacketDrawCommand extends Packet {
    private static final long serialVersionUID = 8957926132249921422L;
    private final DrawCommand drawCommand;
    private final LayerIdentifier layerIdentifier;
    
    public PacketDrawCommand(DrawCommand drawCommand, LayerIdentifier layerIdentifier) {
        this.drawCommand = drawCommand;
        this.layerIdentifier = layerIdentifier;
    }
    
    public DrawCommand drawCommand() {
        return drawCommand;
    }
    
    public LayerIdentifier layerIdentifier() {
    	return layerIdentifier;
    }
    
    /**
     * Handles receiving a DrawCommandPacket
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedDrawCommandPacket(this);
	}
}
    