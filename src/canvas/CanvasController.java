package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;

import javax.swing.JPanel;

import stroke.StrokeProperties;
import canvas.controller.DefaultDrawingController;
import canvas.controller.DrawingController;
import client.ClientController;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 */
public class CanvasController extends DrawableBase {
    private static final long serialVersionUID = -7112257891818505133L;

    private final DrawingController defaultDrawingController;
    private final Canvas canvas;
    
    public CanvasController(StrokeProperties strokeProperties, ClientController clientController, Canvas canvas) {
        super(canvas.width, canvas.height);
        this.canvas = canvas;
        this.defaultDrawingController = new DefaultDrawingController(clientController, strokeProperties, canvas);
    }

    // An empty canvas controller, no drawing support
    public CanvasController(int width, int height) {
    	super(width, height);
    	this.canvas = new Canvas(width, height);
    	this.defaultDrawingController = null;
    }
    
    /*
     * Add the mouse listener that supports the user's freehand drawing.
     */
    public void setDefaultDrawingController(JPanel obj) {
    	assert this.defaultDrawingController != null;
        obj.addMouseListener(defaultDrawingController);
        obj.addMouseMotionListener(defaultDrawingController);
    }
    
    public Canvas canvas() {
    	return canvas;
    }
    
    @Override
    public void paintOnGraphics(Graphics g) {
    	canvas.paintOnGraphics(g);
    }
    
    @Override
    public void drawPixel(Pixel pixel) {
        canvas.drawPixel(pixel);
    }
    
    @Override
    public void drawLine(Pixel pixelStart, Pixel pixelEnd, Stroke stroke, int symetry) {
        canvas.drawLine(pixelStart, pixelEnd, stroke, symetry);
    }
    
    @Override
    public void drawFill(Pixel pixel) {
        canvas.drawFill(pixel);
    }
    
    @Override
    public Color getPixelColor(Pixel pixel) {
        return canvas.getPixelColor(pixel);
    }
}
