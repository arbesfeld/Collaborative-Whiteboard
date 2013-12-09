package packet;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import javax.swing.ImageIcon;

import name.BoardIdentifier;
import name.Identifiable;
import canvas.Canvas2d;
import canvas.DrawableBase;
import models.BoardModel;
/**
 * Class that represents a packet of the current BoardModel
 *
 */
public final class PacketBoardModel extends Packet {
    private static final long serialVersionUID = 8105009453719679025L;

    private final BoardIdentifier boardName;

    private transient BufferedImage image;
    private final Identifiable[] users;
    
    /**
     * Constructor that makes a Packet from a BoardModel
     * @param boardModel
     */
    public PacketBoardModel(BoardModel boardModel) {
        this.boardName = boardModel.identifier();
        this.image = ((Canvas2d) boardModel.canvas()).image();
        this.users = boardModel.users();
 
    }
    /**
     * Getter method to get the BoardModel
     * @return BoardModel
     */
    public BoardIdentifier boardName() {
        return boardName;
    }
    
    public Canvas2d canvas() {
        return new Canvas2d(image);
    }
    
    public Identifiable[] users() {
        return users;
    }
    
    /**
     * Handler receiving a BoardModelPacket
     * 
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedBoardModelPacket(this);
	}
	
	private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        
        int w = image.getWidth();
        int h = image.getHeight();
        int[] pixels = image != null? new int[w * h] : null;

        if (image != null) {
            try {
                PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
                pg.grabPixels();
                if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
                    throw new IOException("failed to load image contents");
                }
            }
            catch (InterruptedException e) {
                throw new IOException("image load interrupted");
            }
        }
        s.writeInt(w);
        s.writeInt(h);
        s.writeObject(pixels);
    }

    private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
        s.defaultReadObject();

        int w = s.readInt();
        int h = s.readInt();
        int[] pixels = (int[])(s.readObject());

        if (pixels != null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            ColorModel cm = ColorModel.getRGBdefault();
            Image temp = tk.createImage(new MemoryImageSource(w, h, cm, pixels, 0, w));
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            image.getGraphics().drawImage(temp, 0, 0, null);
        }
    }
}
