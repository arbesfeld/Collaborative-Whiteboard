package stroke;

import java.awt.Color;

import canvas.Vector2;

import pixel.Pixel;

public class StrokeProperties {

    private Color strokeColor;
    private int strokeWidth;
    private StrokeType strokeType;
    
    public StrokeProperties(Color strokeColor, int strokeWidth) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.strokeType = new StrokeTypeBasic();
    }
    
    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }
    
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public Pixel[] paintPoint(int x, int y, Vector2 velocity) {
        return strokeType.paintPoint(strokeColor, strokeWidth, x, y, velocity);
    }

    public void setStrokeType(StrokeType strokeType) {
        this.strokeType = strokeType;
    }
}
