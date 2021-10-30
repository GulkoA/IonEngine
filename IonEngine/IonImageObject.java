package IonEngine;

import javax.imageio.ImageIO;
import javax.swing.WindowConstants;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class IonImageObject extends IonObject{
    private BufferedImage image;
    public IonImageObject(String path, int width, int height, double x, double y, int z_index) {
        super(width, height, x, y, z_index);
    }
    public IonImageObject(int width, int height, double x, double y) {
        super(width, height, x, y);
    }
    public IonImageObject(int x, int y) {
        super(x, y);
    }
    public IonImageObject() {
        super();
    }

    public IonImageObject loadImage(String path) {
        try {
            this.image = ImageIO.read(new File(path));
            setWidth(this.image.getWidth());
            setHeight(this.image.getHeight());
        }
        catch (Exception e) {
            System.out.println("File load error! \n" + e + "\n at " + path);
        }
        return this;
    } //!fix optimise by setting width and height automatically

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, (int)(super.getContainer().getX() + super.getX()), (int)(super.getContainer().getY() + super.getY()), null);
        }
    }
}
