package packet;

import name.BoardIdentifier;
import name.ClientIdentifier;

public final class PacketJoinBoard extends Packet {
    private static final long serialVersionUID = 3502327190158127229L;

    public PacketJoinBoard(BoardIdentifier boardName, ClientIdentifier senderName) {
        super(PacketType.PacketTypeJoinBoard, boardName, senderName);
    }
}
