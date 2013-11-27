package canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class DrawableBase extends JPanel implements Drawable {
    private static final long serialVersionUID = 1L;
    
    protected final int width;
    protected final int height;

    public DrawableBase(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.width = width;
        this.height = height;
    }
    
    public DrawableBase(int width, int height, Pixel[] initPixels) {
        this(width, height);

        for (Pixel initPixel : initPixels) {
            assert !initPixel.color().equals(Color.WHITE);
            drawPixel(initPixel);
        }
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public abstract void paintComponent(Graphics g);
    
    @Override
    public abstract void drawPixel(Pixel pixel);

    @Override
    public abstract Pixel[] getAllPixels();
    
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
