package canvas.layer;

import java.awt.Color;
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

import canvas.DrawableBase;
import canvas.Pixel;

public class Layer extends DrawableBase {

    // image where the user's drawing is stored
    private transient BufferedImage image;
    
	/**
     * Make a canvas.
     * @param width width in pixels
     * @param height height in pixels
     */
    public Layer(int width, int height) {
        super(width, height);
    }
    
    public Layer(BufferedImage image) {
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
        
        if (symetry > 1) {
            double y1 = -pixelStart.y() + height/2;
            double x1 = pixelStart.x() - width/2;
            double distanceStart = Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));
            double thetaStart = Math.atan2(y1, x1);
            
            double y2 = -pixelEnd.y() + height/2;
            double x2 = pixelEnd.x() - width/2;
            double distanceEnd = Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2));
            double thetaEnd = Math.atan2(y2,x2);
            
            double rotAmmount = 2 * Math.PI / symetry;
            
            
            for (int i = 0; i < symetry; i++) {
                g.drawLine(width/2 + (int)(Math.cos(thetaStart) * distanceStart), height/2 - (int)(Math.sin(thetaStart) * distanceStart), 
                        width/2 + (int)(Math.cos(thetaEnd) * distanceEnd), height/2 - (int)(Math.sin(thetaEnd) * distanceEnd));
                g.drawLine(width/2 - (int)(Math.sin(thetaStart) * distanceStart), height/2 + (int)(Math.cos(thetaStart) * distanceStart), 
                        width/2 - (int)(Math.sin(thetaEnd) * distanceEnd), height/2 + (int)(Math.cos(thetaEnd) * distanceEnd));
                thetaStart += rotAmmount;
                thetaEnd += rotAmmount;
            }
        }
        else {
            g.drawLine(pixelStart.x(), pixelStart.y(), pixelEnd.x(), pixelEnd.y());
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

    public synchronized void paintOnGraphics(Graphics g) {
    	if (image == null) {
    		makeImage();
    	}

        g.drawImage(image, 0, 0, null);
    }

    public synchronized BufferedImage image() {
        if (image == null) {
            makeImage();
        }
        
        return image;
    }
	
	private void writeObject(ObjectOutputStream s) throws IOException {
		if (image == null) {
			makeImage();
		}
		
        s.defaultWriteObject();
        
        int w = image.getWidth();
        int h = image.getHeight();
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
        }
    }
}
