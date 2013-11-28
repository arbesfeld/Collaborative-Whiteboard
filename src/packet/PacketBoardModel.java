package packet;

import models.BoardModel;
import name.ClientIdentifier;

public final class PacketBoardModel extends Packet {
    private static final long serialVersionUID = 8105009453719679025L;
    private final BoardModel boardModel;
    
    public PacketBoardModel(BoardModel boardModel, ClientIdentifier senderName) {
        super(PacketType.PacketTypeBoardModel, boardModel.identifier(), senderName);
        this.boardModel = boardModel;
    }
    
    public BoardModel boardModel() {
        return boardModel;
    }
}
