package packet;

import java.util.Arrays;
import java.util.HashMap;

import name.BoardName;
import name.User;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class PacketBoardState extends Packet {
    private final BoardName[] boards;
    
    /**
     * 
     * @param data JSON representation of the data.
     * @return the constructed Packet
     */
    protected static Packet createPacketWithDataInternal(JsonObject data) {
        JsonArray jboards = data.get("boards").getAsJsonArray();
        
        BoardName[] boards = new BoardName[jboards.size()];
        
        for (int i = 0; i < jboards.size(); i++) {
            JsonObject jobject = jboards.get(i).getAsJsonObject();

            int boardId = jobject.get("id").getAsInt();
            String boardName = jobject.get("name").getAsString();
            boards[i] = new BoardName(boardId, boardName);
        }

        BoardName boardName = getBoardName(data);
        assert boardName.equals(BoardName.NULL_BOARD);
        
        User senderName = getSenderName(data);
        return new PacketBoardState(boards, senderName);
    }
    
    public PacketBoardState(BoardName[] boards, User senderName) {
        super(PacketType.PacketTypeBoardState, senderName);
        this.boards = boards;
    }
    
    /**
     * See specification in superclass Packet.
     */
    @Override
    protected void addPayloadToData(HashMap<Object, Object> data) {
        data.put("boards", boards);
    }
    
    public BoardName[] boards() {
        return boards.clone();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(boards);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PacketBoardState other = (PacketBoardState) obj;
		if (!Arrays.equals(boards, other.boards))
			return false;
		return true;
	}
    
    
}
