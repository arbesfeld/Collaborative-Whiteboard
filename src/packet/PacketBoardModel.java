package packet;

import models.BoardModel;
/**
 * Class that represents a packet of the current BoardModel
 *
 */
public final class PacketBoardModel extends Packet {
    private static final long serialVersionUID = 8105009453719679025L;
    private final BoardModel boardModel;
    
    /**
     * Constructor that makes a Packet from a BoardModel
     * @param boardModel
     */
    public PacketBoardModel(BoardModel boardModel) {
        this.boardModel = boardModel;
    }
    /**
     * Getter method to get the BoardModel
     * @return BoardModel
     */
    public BoardModel boardModel() {
        return boardModel;
    }
    
    /**
     * Handler receiving a BoardModelPacket
     * 
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedBoardModelPacket(this);
	}
    
}
