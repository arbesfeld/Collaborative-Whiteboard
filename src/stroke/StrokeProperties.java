package stroke;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import pixel.Pixel;


public class StrokeProperties {

    private Color strokeColor;
    private int strokeWidth;
    private boolean eraserOn;
    
    public StrokeProperties(Color strokeColor, int strokeWidth) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.eraserOn = false;
    }
    
    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }
    
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setEraserOn(boolean eraserOn) {
        this.eraserOn = eraserOn;
    }

    public Pixel[] drawPoint(int x, int y) {
        List<Pixel> result = new ArrayList<Pixel>(strokeWidth*strokeWidth);
        
        Color color = eraserOn ? Color.WHITE : strokeColor;
        for (int i = x-strokeWidth/2; i <= x+strokeWidth/2; i++) {
            for (int j = y-strokeWidth/2; j <= y+strokeWidth/2; j++) {
                result.add(new Pixel(i, j, color));
            }
        }
        return result.toArray(new Pixel[result.size()]);
    }
}
