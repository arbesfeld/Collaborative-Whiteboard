package models;

import java.util.Arrays;
import java.util.HashSet;

import name.BoardName;
import name.User;

import pixel.Pixel;

public abstract class BoardModel {
    private final BoardName boardName;
    private final HashSet<User> users;
    protected final int width, height;
    
    protected BoardModel(BoardName boardName, int width, int height) {
        this(boardName, new User[0], width, height);
    }
    
    protected BoardModel(BoardName boardName, User[] initUsers, int width, int height) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        
        // A server should always have not initial users.
        
        if (isServerBoard())
            assert initUsers.length == 0;
        
        this.users = new HashSet<User>(Arrays.asList(initUsers));
    }
    
    public synchronized void addUser(User user) {
        this.users.add(user);
    }
    
    public synchronized void removeUser(User user) {
        if (!this.users.contains(user))
            throw new IllegalArgumentException("User " + user.toString() + " does not exist.");
        this.users.remove(user);
    }
    
    public BoardName boardName() {
        return boardName;
    }
    
    public synchronized User[] users() {
        return users.toArray(new User[users.size()]);
    }

    protected boolean isValidPixel(Pixel pixel) {
        return pixel.x() >= 0 && pixel.x() < width || pixel.y() >= 0 || pixel.y() < height;
    }

    public abstract void drawPixel(Pixel pixel);
    protected abstract boolean isServerBoard();
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((boardName == null) ? 0 : boardName.hashCode());
        result = prime * result + height;
        result = prime * result + ((users == null) ? 0 : users.hashCode());
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoardModel other = (BoardModel) obj;
        if (boardName == null) {
            if (other.boardName != null)
                return false;
        } else if (!boardName.equals(other.boardName))
            return false;
        if (height != other.height)
            return false;
        if (users == null) {
            if (other.users != null)
                return false;
        } else if (!users.equals(other.users))
            return false;
        if (width != other.width)
            return false;
        return true;
    }

}
