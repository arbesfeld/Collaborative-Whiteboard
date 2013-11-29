package packet;

import models.BoardModel;

public final class PacketBoardModel extends Packet {
    private static final long serialVersionUID = 8105009453719679025L;
    private final BoardModel boardModel;
    
    public PacketBoardModel(BoardModel boardModel) {
        this.boardModel = boardModel;
    }
    
    public BoardModel boardModel() {
        return boardModel;
    }

	@Override
	public void process(PacketHandler handler) {
		handler.receivedBoardModelPacket(this);
	}
    
}
