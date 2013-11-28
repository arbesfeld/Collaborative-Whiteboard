package packet;

import name.BoardIdentifier;
import name.ClientIdentifier;
import canvas.command.DrawCommand;

public final class PacketDrawCommand extends Packet {
    /**
     * 
     */
    private static final long serialVersionUID = 8957926132249921422L;
    private final DrawCommand drawCommand;
    
    public PacketDrawCommand(BoardIdentifier boardName, ClientIdentifier senderName, DrawCommand drawCommand) {
        super(PacketType.DRAW_COMMAND, boardName, senderName);
        this.drawCommand = drawCommand;
    }
    
    public DrawCommand drawCommand() {
        return drawCommand;
    }
}
    