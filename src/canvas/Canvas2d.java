package canvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.ImageIcon;

import stroke.StrokeProperties;
import util.Vector2;

public class Canvas2d extends DrawableBase implements Drawable, Serializable {
    private static final long serialVersionUID = -6329493755553689791L;

    // image where the user's drawing is stored
    private ImageIcon drawingBuffer;
    
    /**
     * Make a canvas.
     * @param width width in pixels
     * @param height height in pixels
     */
    public Canvas2d(int width, int height) {
        super(width, height);
        
        // note: we can't call makeDrawingBuffer here, because it only
        // works *after* this canvas has been added to a window.  Have to
        // wait until paintComponent() is first called.
    }
    
    /*
     * Make the drawing buffer and draw some starting content for it.
     */
    private void makeDrawingBuffer() {
        drawingBuffer = new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
        fillWithWhite();
    }
    
    /*
     * Make the drawing buffer entirely white.
     */
    private void fillWithWhite() {
        final Graphics2D g = (Graphics2D) drawingBuffer.getImage().getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0,  0,  width, height);
    }
    
    @Override
    public void drawPixel(Pixel pixel) {
        if (drawingBuffer == null) {
            makeDrawingBuffer();
        }
        
        if (!isValidPixel(pixel)) {
            return;
        }
        
        final Graphics2D g = (Graphics2D) drawingBuffer.getImage().getGraphics();

        g.setColor(pixel.color());
        g.fillRect(pixel.x(), pixel.y(), 1, 1);
    }

    public Image drawingBuffer() {
        if (drawingBuffer == null) {
            makeDrawingBuffer();
        }
        return drawingBuffer.getImage();
    }

}
