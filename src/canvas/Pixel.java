package canvas;

import java.awt.Color;

import util.Utils;

public class Pixel {
    private int x, y;
    private Color color;
    private int r; // used for serialization
    private int g; // used for serialization
    private int b; // used for serialization
    
    public Pixel(int x, int y, Color color) {
        if (this.x < 0 || this.y < 0) {
            throw new IllegalArgumentException("Pixels must have positive x and y coordinates. x = "
                                              + x + ", y = " + y + ".");
        }
        this.x = x;
        this.y = y;
        this.color = color;
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
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

    @Override
    public String toString() {
        return "Pixel [x=" + x + ", y=" + y + ", color=" + color + ", r=" + r
                + ", g=" + g + ", b=" + b + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + b;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + g;
        result = prime * result + r;
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
        if (b != other.b)
            return false;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (g != other.g)
            return false;
        if (r != other.r)
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    public Pixel clone() {
        return new Pixel(x, y, color);
    }

}
