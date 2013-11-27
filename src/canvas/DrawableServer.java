package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

public final class DrawableServer extends DrawableBase {    
    private final Color[][] pixels;
    
    public DrawableServer(int width, int height) {
        super(width, height);
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
    
    @Override
    public synchronized Pixel[] getAllPixels() {
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

    @Override
    public void paintComponent(Graphics g) {
        throw new UnsupportedOperationException();
    }
}
