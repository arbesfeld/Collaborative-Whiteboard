package models;

import java.awt.Color;

import packet.BoardName;
import pixel.Pixel;

public class ServerBoardModel extends BoardModel {
    private final Color[][] pixels;
    
    public ServerBoardModel(BoardName boardName, int width, int height) {
        super(boardName);
        pixels = new Color[width][height];
    }

    @Override
    public synchronized void putPixel(Pixel pixel) {
        pixels[pixel.x()][pixel.y()] = pixel.color();
    }
    
    public synchronized Pixel[] getAllPixels() {
        Pixel[] resPixels = new Pixel[pixels.length*pixels[0].length];
        
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                int count = i*pixels[0].length + j;
                resPixels[count] = new Pixel(i, j, pixels[i][j]);
            }
        }
        return resPixels;
    }
}
