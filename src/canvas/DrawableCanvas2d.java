package canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import stroke.StrokeProperties;
import util.Vector2;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandPixel;
import client.ClientController;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 */
public class DrawableCanvas2d extends DrawableBase {
    private static final long serialVersionUID = -7112257891818505133L;

    private final ClientController clientController;
    private final StrokeProperties strokeProperties;

    private final Canvas2d canvas;
    
    public DrawableCanvas2d(StrokeProperties strokeProperties, ClientController clientController, Canvas2d canvas) {
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
    	canvas.paintComponent(g);
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
            
            DrawCommand[] commands = strokeProperties.paintLine(canvas, lastX, lastY, x, y, 
                                                                new Vector2(x-lastX,y-lastY));
            for (DrawCommand command : commands) {
                clientController.sendDrawCommand(command);
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
    public void repaint() {
    	if (canvas != null) {
    		canvas.repaint();
    	}
    	super.repaint();
    }
    
    @Override
    public void drawPixel(Pixel pixel) {
        canvas.drawPixel(pixel);
        repaint();
    }
    
    @Override
    public void drawLine(Pixel pixelStart, Pixel pixelEnd, Stroke stroke) {
        canvas.drawLine(pixelStart, pixelEnd, stroke);
        repaint();
    }
    
    @Override
    public Color getPixelColor(Pixel pixel) {
        return canvas.getPixelColor(pixel);
    }
}
