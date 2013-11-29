package packet;

import canvas.command.DrawCommand;

public final class PacketDrawCommand extends Packet {
    private static final long serialVersionUID = 8957926132249921422L;
    private final DrawCommand drawCommand;
    
    public PacketDrawCommand(DrawCommand drawCommand) {
        this.drawCommand = drawCommand;
    }
    
    public DrawCommand drawCommand() {
        return drawCommand;
    }

	@Override
	public void process(PacketHandler handler) {
		handler.receivedDrawCommandPacket(this);
	}
}
    