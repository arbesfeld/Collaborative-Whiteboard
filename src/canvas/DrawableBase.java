package canvas;

import java.awt.Graphics;
import java.io.Serializable;

public abstract class DrawableBase implements Drawable, Serializable {
    private static final long serialVersionUID = -6788405670250928889L;
    
    protected final int width;
    protected final int height;

    public DrawableBase(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public abstract void paintOnGraphics(Graphics g);
    
    public int width() {
        return width;
    }
    
    public int height() {
        return height;
    }
    
    protected boolean isValidPixel(Pixel pixel) {
        return pixel.x() >= 0 && pixel.x() < width &&
               pixel.y() >= 0 && pixel.y() < height;
    }
}
