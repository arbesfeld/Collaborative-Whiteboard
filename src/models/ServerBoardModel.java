package models;

import java.awt.Color;

import packet.BoardName;
import packet.PacketGameState;
import pixel.Pixel;

public class ServerBoardModel extends BoardModel {
    private final Color[][] pixels;
    private final int width, height;
    
    public ServerBoardModel(BoardName boardName, int width, int height) {
        super(boardName);
        this.width = width;
        this.height = height;
        pixels = new Color[width][height];
    }

    @Override
    public synchronized void putPixel(Pixel pixel) {
        pixels[pixel.x()][pixel.y()] = pixel.color();
    }
    
    private synchronized Pixel[] getAllPixels() {
        Pixel[] resPixels = new Pixel[width*height];
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int count = i*height + j;
                resPixels[count] = new Pixel(i, j, pixels[i][j]);
            }
        }
        return resPixels;
    }
    
    public synchronized PacketGameState constructGameStatePacket() {
        return new PacketGameState(boardName(), width, height, users(), getAllPixels());
    }
}
