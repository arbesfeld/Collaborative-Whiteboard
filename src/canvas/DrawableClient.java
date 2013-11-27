package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import stroke.StrokeProperties;
import util.Vector2;
import client.BoardClientController;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 */
public class DrawableClient extends DrawableBase implements Drawable {
    private static final long serialVersionUID = 1L;
    
    // image where the user's drawing is stored
    private BufferedImage drawingBuffer;

    private final StrokeProperties strokeProperties;
    
    /**
     * Make a canvas.
     * @param width width in pixels
     * @param height height in pixels
     */
    public DrawableClient(BoardClientController controller, StrokeProperties strokeProperties, int width, int height, Pixel[] initPixels) {
        super(width, height, initPixels);
        addDrawingController(controller);
        this.strokeProperties = strokeProperties;
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
    
    @Override
    public void drawPixel(Pixel pixel) {
        if (drawingBuffer == null) {
            makeDrawingBuffer();
        }
        
        if (outOfRange(pixel.x(), pixel.y()))
            return;
        
        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

        g.setColor(pixel.color());
        g.fillRect(pixel.x(), pixel.y(), 1, 1);
        
        this.repaint();
    }
    
    public int getPixelRGB(int x, int y) {
        if (drawingBuffer == null) {
            makeDrawingBuffer();
        }
        
        if (outOfRange(x, y))
            return 0;
        
        return drawingBuffer.getRGB(x, y);
    }
    
    private boolean outOfRange(int x, int y) {
        return x < 0 || x >= width || y < 0 || y >= height;
    }

    /*
     * Add the mouse listener that supports the user's freehand drawing.
     */
    private void addDrawingController(BoardClientController clientController) {
        DrawingController controller = new DrawingController(clientController);
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
        
        private final BoardClientController clientController;
        
        public DrawingController(BoardClientController clientController) {
            this.clientController = clientController;
        }
        
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
            Vector2 velocity = new Vector2(lastX - x, lastY - y);
            double dist = velocity.abs();
            
            for (int i = 0; i <= (int)dist; i++) {
                int curX = (int) (lastX*i/dist + x*(dist-i)/dist);
                int curY = (int) (lastY*i/dist + y*(dist-i)/dist);
                
                Pixel[] pixels = strokeProperties.paintPoint(curX, curY, velocity);
                
                for (Pixel pixel : pixels) {
                    if (outOfRange(pixel.x(), pixel.y()))
                        continue;
                    
                    if (pixel.color().getRGB() == getPixelRGB(pixel.x(), pixel.y())) {
                        continue;
                    }
                    drawPixel(pixel);
                    clientController.sendPixel(pixel);
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

    @Override
    public Pixel[] getAllPixels() {
        throw new UnsupportedOperationException();
    }
}
