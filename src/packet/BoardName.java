package packet;

public class BoardName extends Name {
    BoardName(int id, String name) {
        super(id, name);
    }
    
    public BoardName clone() {
        return new BoardName(id(), name());
    }
}
