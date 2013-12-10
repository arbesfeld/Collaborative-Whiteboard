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
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import stroke.StrokeProperties;
import canvas.command.DrawCommandPixel;
import client.ClientController;

public class Canvas2d extends DrawableBase implements Drawable, Serializable {
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
    }
    
    public Canvas2d(BufferedImage image) {
        super(image.getWidth(), image.getHeight());
        this.image = image;
    }
    /*
     * Make the drawing buffer and draw some starting content for it.
     */
    private synchronized void makeImage() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fillWithWhite();
    }
    
    /*
     * Make the drawing buffer entirely white.
     */
    private synchronized void fillWithWhite() {
        final Graphics2D g = (Graphics2D) image.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
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
    }
    
    @Override
    public synchronized void drawLine(Pixel pixelStart, Pixel pixelEnd, Stroke stroke, int symetry) {
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
        
//        int rots = 3;
//        int y1 = -pixelStart.y() + height/2;
//        int x1 = pixelStart.x() - width/2;
//        double distanceStart = Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));
//        double thetaStart = Math.atan(y1/x1);
//        
//        int y2 = -pixelEnd.y() + height/2;
//        int x2 = pixelEnd.x() - width/2;
//        double distanceEnd = Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2));
//        double thetaEnd = Math.atan(y2/x2);
//        
//        double rotAmmount = 2 * Math.PI / rots;
//        
//        
//        for (int i = 0; i < rots; i++) {
//            g.drawLine(width/2 + (int)(Math.sin(thetaStart) * distanceStart), height/2 + (int)(Math.cos(thetaStart) * distanceStart), 
//                    width/2 + (int)(Math.sin(thetaEnd) * distanceEnd), height/2 + (int)(Math.cos(thetaEnd) * distanceEnd));
//            thetaStart += rotAmmount;
//            thetaEnd += rotAmmount;
//        }
        g.drawLine(pixelStart.x(), pixelStart.y(), pixelEnd.x(), pixelEnd.y());
        if (symetry >= 2) {
            g.drawLine(width - pixelStart.x(), height - pixelStart.y(), width - pixelEnd.x(), height - pixelEnd.y());
        }
        if (symetry >= 3) {
            g.drawLine(pixelStart.y(), pixelStart.x(), pixelEnd.y(), pixelEnd.x());
            g.drawLine(width - pixelStart.y(), height - pixelStart.x(), width - pixelEnd.y(), height - pixelEnd.x());
        }
        if (symetry >= 4) {
            g.drawLine(pixelStart.x(), height - pixelStart.y(), pixelEnd.x(), height - pixelEnd.y());
            g.drawLine(pixelStart.y(), height - pixelStart.x(), pixelEnd.y(), height - pixelEnd.x());
            g.drawLine(width - pixelStart.x(),pixelStart.y(), width - pixelEnd.x(),pixelEnd.y());
            g.drawLine(width - pixelStart.y(),pixelStart.x(), width - pixelEnd.y(),pixelEnd.x());
        }
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

    public synchronized void paintComponent(Graphics g) {
    	if (image == null) {
    		makeImage();
    	}

        g.drawImage(image, 0, 0, null);
    }
    
    public synchronized DrawableCanvas2d makeDrawable(StrokeProperties strokeProperties, ClientController clientController) {
    	return new DrawableCanvas2d(strokeProperties, clientController, this);
    }

    public synchronized BufferedImage image() {
        if (image == null) {
            makeImage();
        }
        
        return image;
    }
        
}
