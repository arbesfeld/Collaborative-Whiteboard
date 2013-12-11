package canvas;

import java.awt.Color;
import java.io.Serializable;

import util.Utils;

public class Pixel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int x, y;
    private Color color;
    
    public Pixel(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    public Pixel(int x, int y, String colorString) {
        this(x, y, Utils.colorFromString(colorString));
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public Color color() {
        return color;
    }

    public Pixel clone() {
        return new Pixel(x, y, color);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pixel other = (Pixel) obj;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
}
