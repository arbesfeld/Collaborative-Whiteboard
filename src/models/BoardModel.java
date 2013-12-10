package models;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Stroke;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import name.BoardIdentifier;
import name.Identifiable;
import name.LayerIdentifier;
import canvas.Canvas;
import canvas.CanvasController;
import canvas.Drawable;
import canvas.Pixel;
import canvas.layer.LayerAdjustment;
import canvas.layer.LayerProperties;

public class BoardModel extends JPanel implements Drawable, Identifiable, Serializable {
	private static final long serialVersionUID = -7812022750931126889L;

	private final BoardIdentifier boardName;
    private final CanvasController canvas;
    private final Set<Identifiable> users;
    
    // Server only
    public BoardModel(BoardIdentifier boardName, CanvasController canvas) {
    	this(boardName, canvas, new Identifiable[]{});
    }
    
    // Client only
    public BoardModel(BoardIdentifier boardName, CanvasController canvas, Identifiable[] initUsers) {
        this.boardName = boardName;
        this.canvas = canvas;
        this.users = Collections.synchronizedSet(new HashSet<Identifiable>(Arrays.asList(initUsers)));
        this.setPreferredSize(new Dimension(width(), height()));
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
    public void drawPixel(LayerIdentifier identifier, Pixel pixel) {
        canvas.drawPixel(identifier, pixel);
        repaint();
    }

    @Override
    public void drawLine(LayerIdentifier identifier, Pixel pixelStart, Pixel pixelEnd, Stroke stroke, int symetry) {
        canvas.drawLine(identifier, pixelStart, pixelEnd, stroke, symetry);
        repaint();
    }
    
    @Override
    public void drawFill(LayerIdentifier identifier, Pixel pixel) {
        canvas.drawFill(identifier, pixel);
        repaint();
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
    
    @Override
    public BoardIdentifier identifier() {
        return boardName;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        canvas.paintOnGraphics(g);
    }
    
    public Canvas canvas() {
    	return canvas.canvas();
    }

	public void setDrawingControllerDefault() {
		canvas.setDefaultDrawingController(this);
	}

	public void addLayer(LayerIdentifier layer) {
		canvas.canvas().addLayer(layer);
	}
	
	public void adjustLayer(LayerProperties properties, LayerAdjustment adjustment) {
		canvas.canvas().adjustLayer(properties, adjustment);
		repaint();
	}
	
}
