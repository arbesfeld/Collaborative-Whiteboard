package models;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import name.BoardIdentifier;
import name.Identifiable;
import name.Identifier;
import canvas.Drawable;
import canvas.DrawableBase;
import canvas.Pixel;

public class BoardModel implements Drawable, Identifiable {
    private final BoardIdentifier boardName;

    private final DrawableBase canvas;
    private final Set<Identifiable> users;
    
    public BoardModel(BoardIdentifier boardName, DrawableBase canvas) {
        this.boardName = boardName;
        this.canvas = canvas;
        this.users = Collections.synchronizedSet(new HashSet<Identifiable>());
    }
    
    public BoardModel(BoardIdentifier boardName, DrawableBase canvas, Identifiable[] initUsers) {
        this.boardName = boardName;
        this.canvas = canvas;
        this.users = Collections.synchronizedSet(new HashSet<Identifiable>(Arrays.asList(initUsers)));
    }
    
    public void addUser(Identifiable user) {
        assert !users.contains(user);
        users.add(user);
    }
    
    public void removeUser(Identifiable user) {
        users.remove(user);
    }
    
    public Identifiable[] users() {
        return users.toArray(new Identifiable[users.size()]).clone();
    }

    public Identifier[] userIdentifiers() {
        Identifier[] result = new Identifier[users.size()];
        int i = 0;
        for (Identifiable user : users) {
            result[i] = user.identifier();
        }
        return result;
    }
    
    @Override
    public void drawPixel(Pixel pixel) {
        canvas.drawPixel(pixel);
    }
    
    @Override
    public Pixel[] getAllPixels() {
        return canvas.getAllPixels();
    }

    @Override
    public int width() {
        return canvas.width();
    }
    
    @Override
    public int height() {
        return canvas.height();
    }
    
    public JPanel panel() {
        return canvas;
    }
    
    @Override
    public BoardIdentifier identifier() {
        return boardName;
    }
}
