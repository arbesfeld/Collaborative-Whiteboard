package game;

import java.util.HashMap;

import packet.Packet;
import pixel.Pixel;

import util.Pair;

import canvas.Canvas;

public class Board {
    // A HashMap from id to name of the current clients.
    private final HashMap<String, String> clientNames;
    private final Canvas canvas;
    private final int boardID;
    
    /**
     * Initialize a board with width and height and empty clientNames
     * should only be done on the server, the clients should use the other constructor.
     * @param width
     * @param height
     */
    public Board(int boardID, int width, int height) {
        this.boardID = boardID;
        this.canvas = new Canvas(width, height);
        this.clientNames = new HashMap<String, String>();
    }
    
    // Initialize a board with names, and a list of Pixels
    public Board(int boardID, int width, int height, 
                      Pair<String, String>[] initClients, Pixel[] initPixels) {
        this(boardID, width, height);
        
        for (Pair<String, String> client : initClients) {
            clientNames.put(client.first, client.second);
        }
        
        for (Pixel pixel : initPixels) {
            drawPixel(pixel);
        }
    }
    
    public void updateWithPacket(Packet packet) {
        // If the packet is for a different white board, then ignore it.
        if (packet.boardID() != boardID) {
            return;
        }
        
        
    }
    
    private void drawPixel(Pixel pixel) {
        // TODO
    }
}
