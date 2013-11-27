package models;

import java.awt.Color;

import name.BoardName;
import name.User;
import client.BoardClientController;
import canvas.Canvas;

import pixel.Pixel;
import stroke.StrokeProperties;

public class ClientBoardModel extends BoardModel {
    private final Canvas canvas;
    
    public ClientBoardModel(BoardClientController controller, StrokeProperties strokeProperties,
                            int width, int height, BoardName boardName, 
                            User[] initUsers, Pixel[] initPixels) {
        super(boardName, initUsers);
        this.canvas = new Canvas(controller, strokeProperties, width, height);
        
        for (Pixel initPixel : initPixels) {
        	assert !initPixel.color().equals(Color.WHITE);
        	putPixel(initPixel);
        }
    }
    
    @Override
    public void putPixel(Pixel pixel) {
        canvas.drawPixel(pixel);
    }
    
    public Canvas canvas() {
        return canvas;
    }
}
