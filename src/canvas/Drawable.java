package canvas;

public interface Drawable {
    public int width();
    public int height();
    
    public void drawPixel(Pixel pixel);
}
