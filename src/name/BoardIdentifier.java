package name;

public final class BoardIdentifier extends Identifier {
    public static final BoardIdentifier NULL_BOARD = new BoardIdentifier(0, "");
    public BoardIdentifier(int id, String name) {
        super(id, name);
    }

}
