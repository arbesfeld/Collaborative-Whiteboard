package models;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import name.BoardName;
import packet.PacketGameState;
import pixel.Pixel;
import server.BoardServer;

public class ServerBoardModel extends BoardModel {
    private final Color[][] pixels;
    
    public ServerBoardModel(BoardName boardName, int width, int height) {
        super(boardName, width, height);
        pixels = new Color[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = Color.WHITE;
            }
        }
    }

    @Override
    public synchronized void drawPixel(Pixel pixel) {
        if (isValidPixel(pixel))
            pixels[pixel.x()][pixel.y()] = pixel.color();
    }
    
    private synchronized Pixel[] getAllPixels() {
    	List<Pixel> resPixelsList = new LinkedList<Pixel>();
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!pixels[i][j].equals(Color.WHITE)) {
                	resPixelsList.add(new Pixel(i, j, pixels[i][j]));
                }
            }
        }
        return resPixelsList.toArray(new Pixel[resPixelsList.size()]);
    }

    public synchronized PacketGameState constructGameStatePacket() {
        return new PacketGameState(boardName(), BoardServer.SERVER_NAME, width, height, users(), getAllPixels());
    }

    @Override
    protected boolean isServerBoard() {
        return true;
    }
    
}
