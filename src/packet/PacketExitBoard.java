package packet;

import name.BoardIdentifier;
import name.ClientIdentifier;

public final class PacketExitBoard extends Packet {
    /**
     * 
     */
    private static final long serialVersionUID = -5398259621648005467L;

    public PacketExitBoard(BoardIdentifier boardName, ClientIdentifier senderName) {
        super(PacketType.EXIT_BOARD, boardName, senderName);
    }
}