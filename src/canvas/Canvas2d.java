package canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import stroke.StrokeProperties;
import canvas.command.DrawCommandPixel;
import client.ClientController;

public class Canvas2d extends DrawableBase {
    private static final long serialVersionUID = -6329493755553689791L;

    // image where the user's drawing is stored
    private transient ImageIcon imageIcon;
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
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        if (imageIcon == null) {
            imageIcon = new ImageIcon(image);
            fillWithWhite();
        } else {
            image.getGraphics().drawImage(imageIcon.getImage(), 0, 0 , null);
        }
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

        final Graphics2D g = (Graphics2D) image.getGraphics();

        g.setColor(Color.BLACK);
        
        image.setRGB(pixel.x(), pixel.y(), pixel.color().getRGB());
        repaint();
    }
    
    @Override
    public synchronized void drawLine(Pixel pixelStart, Pixel pixelEnd, Stroke stroke) {
        if (image == null) {
           makeImage();
        }

        if (!isValidPixel(pixelStart) || !isValidPixel(pixelEnd)) {
            return;
        }
        
        final Graphics2D g = (Graphics2D) image.getGraphics();
        if (pixelStart.color() != pixelEnd.color()) {
            g.setPaint(new GradientPaint(pixelStart.x(), pixelStart.y(), pixelStart.color(),
                                                       pixelEnd.x(), pixelEnd.y(), pixelEnd.color()));
        }
        else {
            g.setColor(pixelStart.color());
        }
        g.setStroke(stroke);
        g.drawLine(pixelStart.x(), pixelStart.y(), pixelEnd.x(), pixelEnd.y());

        repaint();
    }
    
    public synchronized void drawFill(Pixel pixel) {
        if (image == null) {
            makeImage();
        }
        
        if (!isValidPixel(pixel)) {
            return;
        }

        final Graphics2D g = (Graphics2D) image.getGraphics();

        g.setColor(Color.BLACK);

        HashSet<Pixel> pixels = new HashSet<Pixel>();
        Queue<Pixel> queue = new LinkedList<Pixel>();
        queue.add(pixel);
        Color initialColor = getPixelColor(pixel);
        while (!queue.isEmpty()) {
            Pixel newPixel = queue.remove();
            if (!pixels.contains(newPixel)) {
                if (getPixelColor(newPixel).equals(initialColor)) { 
                    drawPixel(newPixel);
                    pixels.add(newPixel);
                    for (int i = -1; i <= 2; i++) {
                        for (int j = -1; j <= 2; j++) {
                            if (!pixels.contains(new Pixel(newPixel.x() + i,  newPixel.y() + j, pixel.color()))) {
                                queue.add(new Pixel(newPixel.x() + i,  newPixel.y() + j, pixel.color()));
                            }
                        }
                    }   
                }
            }
        }
        repaint();
    }
    
    @Override
	public synchronized Color getPixelColor(Pixel pixel) {
		if (image == null) {
			makeImage();
		}
		
		if (!isValidPixel(pixel)) {
			return pixel.color();
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
    
    public synchronized DrawableCanvas2d makeDrawable(StrokeProperties strokeProperties, ClientController clientController) {
    	return new DrawableCanvas2d(strokeProperties, clientController, this);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        if (image == null) {
            makeImage();
        }
        int w = imageIcon.getIconWidth();
        int h = imageIcon.getIconHeight();
        int[] pixels = image != null? new int[w * h] : null;

        if (image != null) {
            try {
                PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
                pg.grabPixels();
                if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
                    throw new IOException("failed to load image contents");
                }
            }
            catch (InterruptedException e) {
                throw new IOException("image load interrupted");
            }
        }
        s.writeInt(w);
        s.writeInt(h);
        s.writeObject(pixels);
    }

    private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
        s.defaultReadObject();

        int w = s.readInt();
        int h = s.readInt();
        int[] pixels = (int[])(s.readObject());

        if (pixels != null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            ColorModel cm = ColorModel.getRGBdefault();
            Image temp = tk.createImage(new MemoryImageSource(w, h, cm, pixels, 0, w));
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            image.getGraphics().drawImage(temp, 0, 0, null);
            imageIcon = new ImageIcon(image);
        }
    }
        
}
