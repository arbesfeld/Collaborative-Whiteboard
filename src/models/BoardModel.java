package models;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import packet.PacketGameState;
import server.BoardServer;
import canvas.Drawable;
import canvas.DrawableBase;
import canvas.Pixel;
import name.BoardName;
import name.Identifiable;
import name.Name;

public class BoardModel<U extends Identifiable, C extends DrawableBase> implements Drawable {
    private final BoardName boardName;

    private final C canvas;
    private final Set<U> users;
    
    public BoardModel(BoardName boardName, C canvas) {
        this.boardName = boardName;
        this.canvas = canvas;
        this.users = Collections.synchronizedSet(new HashSet<U>());
    }
    
    public BoardModel(BoardName boardName, C canvas, U[] initUsers) {
        this.boardName = boardName;
        this.canvas = canvas;
        this.users = Collections.synchronizedSet(new HashSet<U>(Arrays.asList(initUsers)));
    }
    
    public void addUser(U user) {
        users.add(user);
    }
    
    public void removeUser(U user) {
        users.remove(user);
    }
    
    public BoardName boardName() {
        return boardName;
    }
    
    public Set<U> users() {
        return users;
    }

    @Override
    public void drawPixel(Pixel pixel) {
        canvas.drawPixel(pixel);
    }
    
    @Override
    public Pixel[] getAllPixels() {
        return canvas.getAllPixels();
    }

    public PacketGameState constructGameStatePacket() {
        synchronized(users) {
            Name[] result = new Name[users.size()];
            int i = 0;
            for (U user : users) {
                result[i] = user.identifier();
                i++;
            }
            return new PacketGameState(boardName(), BoardServer.SERVER_NAME, 
                    canvas.width(), canvas.height(), result, getAllPixels());
        }
    }

    public JPanel panel() {
        return canvas;
    }
}
