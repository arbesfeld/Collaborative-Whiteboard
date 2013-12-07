package stroke;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandLine;

public class StrokeProperties {
    public final static Color DEFAULT_STROKE_COLOR = Color.BLACK;
    public final static int DEFAULT_STROKE_WIDTH = 5;
    
    private Color strokeColor;
    private int strokeWidth;
    private StrokeType strokeType;
    private StrokeType eraserStroke;
    private Boolean eraserOn;
    private StrokeType fillStroke;
    private Boolean fillOn;
    
    public StrokeProperties(Color strokeColor, int strokeWidth) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.strokeType = new StrokeTypeBasic();
        this.eraserOn = false;
        this.eraserStroke = new StrokeTypeEraser();
        this.fillOn = false;
        this.fillStroke = new StrokeTypeFill();
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
    
    public void setEraserOn(Boolean eraserOn) {
        this.eraserOn = eraserOn;
    }
    
    public void setFillOn(Boolean fillOn) {
        this.fillOn = fillOn;
    }

    public DrawCommand[] paintLine(Drawable canvas, int x1, int y1, int x2, int y2, Vector2 velocity) {
        if (eraserOn) {
            return eraserStroke.paintLine(canvas, Color.WHITE, strokeWidth, x1, y1, x2, y2, velocity);
        }
        else if (fillOn) {
            return fillStroke.paintLine(canvas, strokeColor, strokeWidth, x1, y1, x2, y2, velocity);
        }
        else {
            return strokeType.paintLine(canvas, strokeColor, strokeWidth, x1, y1, x2, y2, velocity);
        }
    }
}
