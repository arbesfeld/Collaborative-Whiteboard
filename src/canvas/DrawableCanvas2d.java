package canvas;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import stroke.StrokeProperties;
import util.Vector2;
import canvas.command.DrawCommandPixel;
import client.BoardClientController;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 */
public class DrawableCanvas2d extends DrawableBase implements Drawable {
    private static final long serialVersionUID = -7112257891818505133L;

    private final Canvas2d canvas;
    
    private final BoardClientController clientController;
    private final StrokeProperties strokeProperties;
    
    public DrawableCanvas2d(StrokeProperties strokeProperties, BoardClientController clientController, Canvas2d canvas) {
        super(canvas.width(), canvas.height());
        
        this.setPreferredSize(new Dimension(width(), height()));
        this.clientController = clientController;
        this.strokeProperties = strokeProperties;
        this.canvas = canvas;
        addDrawingController();
    }
    /*
     * Add the mouse listener that supports the user's freehand drawing.
     */
    private void addDrawingController() {
        DrawingController controller = new DrawingController();
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(canvas.drawingBuffer(), 0, 0, null);
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
            Vector2 velocity = new Vector2(lastX - x, lastY - y);
            double dist = velocity.abs();
            
            for (int i = 0; i <= (int)dist; i++) {
                int curX = (int) (lastX*i/dist + x*(dist-i)/dist);
                int curY = (int) (lastY*i/dist + y*(dist-i)/dist);

                Pixel[] pixels = strokeProperties.paintPoint(curX, curY, velocity);
                for (Pixel pixel : pixels) {
                    drawPixel(pixel);
                    clientController.sendDrawCommand(new DrawCommandPixel(pixel));
                }
            }
            lastX = x;
            lastY = y;
            repaint();
        }

        // Ignore all these other mouse events.
        public void mouseMoved(MouseEvent e) { }
        public void mouseClicked(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
    }

    @Override
    public int width() {
        return canvas.width();
    }
    
    @Override
    public int height() {
        return canvas.height();
    }
    
    @Override
    public void drawPixel(Pixel pixel) {
        canvas.drawPixel(pixel);
        repaint();
    }
}