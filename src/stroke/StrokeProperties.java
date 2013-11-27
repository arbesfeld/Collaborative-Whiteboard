package stroke;

import java.awt.Color;

import canvas.Canvas;


import pixel.Pixel;
import util.Vector2;

public class StrokeProperties {
    public final static Color DEFAULT_STROKE_COLOR = Color.BLACK;
    public final static int DEFAULT_STROKE_WIDTH = 3;
    
    private Color strokeColor;
    private int strokeWidth;
    private StrokeType strokeType;
    
    public StrokeProperties(Color strokeColor, int strokeWidth) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.strokeType = new StrokeTypeBasic();
    }    
    
    public StrokeProperties() {
        this(DEFAULT_STROKE_COLOR, DEFAULT_STROKE_WIDTH);
    }
    
    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }
    
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setStrokeType(StrokeType strokeType) {
        this.strokeType = strokeType;
    }

    public Pixel[] paintPoint(int x, int y, Vector2 velocity) {
        return strokeType.paintPoint(strokeColor, strokeWidth, x, y, velocity);
    }
}
