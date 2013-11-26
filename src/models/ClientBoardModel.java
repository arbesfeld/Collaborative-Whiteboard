package models;

import canvas.Canvas;

import packet.BoardName;
import packet.User;
import pixel.Pixel;

public class ClientBoardModel extends BoardModel {
    private final Canvas canvas;
    
    public ClientBoardModel(int width, int height, BoardName boardName, User[] initUsers, Pixel[] initPixels) {
        super(boardName, initUsers);
        canvas = new Canvas(width, height);
        
        // TODO: draw the initial pixels
    }

    @Override
    public void putPixel(Pixel pixel) {
        // TODO
    }
}
