package canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import stroke.StrokeProperties;
import client.ClientController;

public class Canvas2d extends DrawableBase {
    private static final long serialVersionUID = -6329493755553689791L;

    // image where the user's drawing is stored
    private transient BufferedImage image;
    
    /**
     * Make a canvas.
     * @param width width in pixels
     * @param height height in pixels
     */
    public Canvas2d(int width, int height) {
        super(width, height);

        this.setPreferredSize(new Dimension(width, height));
        // note: we can't call makeImage here, because it only
        // works *after* this canvas has been added to a window.  Have to
        // wait until paintComponent() is first called.
    }
    
    /*
     * Make the drawing buffer and draw some starting content for it.
     */
    private synchronized void makeImage() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        fillWithWhite();
    }
    
    /*
     * Make the drawing buffer entirely white.
     */
    private synchronized void fillWithWhite() {
        final Graphics2D g = (Graphics2D) image.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        repaint();
    }
    
    @Override
    public synchronized void drawPixel(Pixel pixel) {
        if (image == null) {
        	makeImage();
        }
        
        if (!isValidPixel(pixel)) {
            return;
        }
        
        image.setRGB(pixel.x(), pixel.y(), pixel.color().getRGB());
        repaint();
    }

	public synchronized Color getPixelColor(Pixel pixel) {
		if (image == null) {
			makeImage();
		}
		
		if (!isValidPixel(pixel)) {
			return Color.WHITE;
		}
		
		return new Color(image.getRGB(pixel.x(), pixel.y()));
	}

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public synchronized void paintComponent(Graphics g) {
    	if (image == null) {
    		makeImage();
    	}
    	
        g.drawImage(image, 0, 0, null);
    }
    
    private synchronized void writeObject(ObjectOutputStream out) throws IOException {
    	if (image == null) {
    		makeImage();
    	}
    	
        out.defaultWriteObject();
        ImageIO.write(image, "png", out); 
    }

    private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = ImageIO.read(in);
        
        assert image != null;
    }
    
    public synchronized DrawableCanvas2d makeDrawable(StrokeProperties strokeProperties, ClientController clientController) {
    	return new DrawableCanvas2d(strokeProperties, clientController, this);
    }
}
