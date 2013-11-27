package models;

import java.awt.Color;

import javax.sound.midi.ControllerEventListener;
import javax.swing.JPanel;

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
        super(boardName, initUsers, width, height);
        this.canvas = new Canvas(controller, strokeProperties, width, height);
        
        for (Pixel initPixel : initPixels) {
        	assert !initPixel.color().equals(Color.WHITE);
        	drawPixel(initPixel);
        }
    }
    
    @Override
    public void drawPixel(Pixel pixel) {
        if (isValidPixel(pixel))
            canvas.drawPixel(pixel);
    }
    
    @Override
    protected boolean isServerBoard() {
        return false;
    }
    
    public JPanel canvas() {
        return canvas;
    }

}
