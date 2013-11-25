package pixel;

import java.awt.Color;

import util.Utils;

public class Pixel {
    private int x, y;
    private Color color;
    private int rgb; // used for serialization
    
    private Pixel(int x, int y, Color color) {
        if (this.x < 0 || this.y < 0) {
            throw new IllegalArgumentException("Pixels must have positive x and y coordinates. x = "
                                              + x + ", y = " + y + ".");
        }
        this.x = x;
        this.y = y;
        this.color = color;
        this.rgb = color.getRGB();
    }
    
    public Pixel(int x, int y, String colorString) {
        this(x, y, Utils.colorFromString(colorString));
    }
    
    public Pixel(int x, int y, int rgb) {
        this(x, y, new Color(rgb));
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + rgb;
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
        if (rgb != other.rgb)
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
