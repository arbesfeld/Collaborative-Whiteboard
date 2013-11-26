package models;

import BoardClient.BoardClientController;
import canvas.Canvas;

import packet.BoardName;
import packet.User;
import pixel.Pixel;

public class ClientBoardModel extends BoardModel {
    private final Canvas canvas;
    
    public ClientBoardModel(BoardClientController controller, int width, int height, BoardName boardName, User[] initUsers, Pixel[] initPixels) {
        super(boardName, initUsers);
        canvas = new Canvas(controller, width, height);
        
        for (Pixel initPixel : initPixels) {
            canvas.drawPixel(initPixel);
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
