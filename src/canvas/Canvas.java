package canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import client.BoardClientController;

import pixel.Pixel;
import stroke.StrokeProperties;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 */
public class Canvas extends JPanel {
	private static final long serialVersionUID = 1L;

	public static int STROKE_DEFAULT = 3;
	
	// image where the user's drawing is stored
    private BufferedImage drawingBuffer;
    
    private final int width;
    private final int height;
    
    private final BoardClientController controller;

    private final StrokeProperties strokeProperties;
    
    /**
     * Make a canvas.
     * @param width width in pixels
     * @param height height in pixels
     */
    public Canvas(BoardClientController controller, StrokeProperties strokeProperties, int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        addDrawingController();
        this.controller = controller;
        this.strokeProperties = strokeProperties;
        this.width = width;
        this.height = height;
        // note: we can't call makeDrawingBuffer here, because it only
        // works *after* this canvas has been added to a window.  Have to
        // wait until paintComponent() is first called.
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        // If this is the first time paintComponent() is being called,
        // make our drawing buffer.
        if (drawingBuffer == null) {
            makeDrawingBuffer();
        }
        
        // Copy the drawing buffer to the screen.
        g.drawImage(drawingBuffer, 0, 0, null);
    }
    
    /*
     * Make the drawing buffer and draw some starting content for it.
     */
    private void makeDrawingBuffer() {
        drawingBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        fillWithWhite();
    }
    
    /*
     * Make the drawing buffer entirely white.
     */
    private void fillWithWhite() {
        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0,  0,  width, height);
        
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
    }
    
    public void drawPixel(Pixel pixel) {
        if (drawingBuffer == null) {
            makeDrawingBuffer();
        }
        
        if (outOfRange(pixel))
            return;
        
        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

        g.setColor(pixel.color());
        g.fillRect(pixel.x(), pixel.y(), 1, 1);
        
        this.repaint();
    }
    
    private boolean outOfRange(Pixel pixel) {
        return pixel.x() < 0 || pixel.x() >= width || pixel.y() < 0 || pixel.y() > height;
    }

    /*
     * Add the mouse listener that supports the user's freehand drawing.
     */
    private void addDrawingController() {
        DrawingController controller = new DrawingController();
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }
    
    /*
     * DrawingController handles the user's freehand drawing.
     */
    private class DrawingController implements MouseListener, MouseMotionListener {
        // store the coordinates of the last mouse event, so we can
        // draw a line segment from that last point to the point of the next mouse event.
        private int lastX, lastY; 

        /*
         * When mouse button is pressed down, start drawing.
         */
        public void mousePressed(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
        }

        /*
         * When mouse moves while a button is pressed down,
         * draw a line segment.
         */
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            
            // draw line (x,y) -> (lastx, lasty)
            int dist = (x - lastX) * (x-lastX) + (y-lastY)+(y+lastY);
            for (int i = 0; i <= dist; i += 50) {
                int curX = (int) ((float) x*i/dist + (float)lastX*(dist-i)/dist);
                int curY = (int) ((float) y*i/dist + (float)lastY*(dist-i)/dist);
                
                Pixel[] pixels = strokeProperties.drawPoint(curX, curY);
                
                for (Pixel pixel : pixels) {
                    controller.drawPixel(pixel);
                    drawPixel(pixel);
                }
            }
            lastX = x;
            lastY = y;
        }

        // Ignore all these other mouse events.
        public void mouseMoved(MouseEvent e) { }
        public void mouseClicked(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
    }
}
