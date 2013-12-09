package models;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import util.Vector2;
import name.BoardIdentifier;
import name.Identifiable;
import canvas.Drawable;
import canvas.DrawableBase;
import canvas.Pixel;
import canvas.command.DrawCommand;

public class BoardModel implements Drawable, Identifiable, Serializable {
	private static final long serialVersionUID = -7812022750931126889L;

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
    
    public boolean containsUser(Identifiable user) {
    	return users.contains(user);
    }
    
    public void removeUser(Identifiable user) {
    	assert users.contains(user);
        users.remove(user);
    }
    
    public Identifiable[] users() {
        return users.toArray(new Identifiable[users.size()]).clone();
    }
    
    public BoardIdentifier getBoardName() {
        return boardName;
    }
    
    @Override
    public void drawPixel(Pixel pixel) {
        canvas.drawPixel(pixel);
    }

    @Override
    public void drawLine(Pixel pixelStart, Pixel pixelEnd, Stroke stroke) {
        canvas.drawLine(pixelStart, pixelEnd, stroke);
    }
    
    @Override
    public void drawFill(Pixel pixel) {
        canvas.drawFill(pixel);
    }
    
    @Override
    public Color getPixelColor(Pixel pixel) {
        return canvas.getPixelColor(pixel);
    }

    @Override
    public int width() {
        return canvas.width();
    }
    
    @Override
    public int height() {
        return canvas.height();
    }
    
    public DrawableBase canvas() {
        return canvas;
    }
    
    @Override
    public BoardIdentifier identifier() {
        return boardName;
    }
    
    public void paintComponent(Graphics g) {
        canvas.paintComponent(g);
    }
    
}
