package IonEngine;

import java.awt.Color;
import java.awt.image.Raster;


public class IonFeatures {
    public static void setTimeout(long time, IonCallback callback) {
        try {
            Thread.sleep(time);
        }
        catch(Exception e) {}
        finally {
            callback.call();
        }
    }

    public static void setInterval(long time, IonCallback callback) {
        while (callback.activated)
        {
            try {
                Thread.sleep(time);
            }
            catch(Exception e) {}
            finally {
                callback.call();
            }
        }
    }

    public static int random(int min, int max) {
        return (int)(Math.random() * (max + 1) + min);
    }

    public static Color randomColor() {
        return new Color(random(0, 255), random(0, 255), random(0, 255));
    }

    public static Color getRasterPixel(Raster raster, int x, int y) {
        if (x >= raster.getWidth() || y >= raster.getHeight() || x < 0 || y < 0)
            return null;
        double[] array = raster.getPixel(x, y, new double[3]);
        return new Color((int)array[0], (int)array[1], (int)array[2]);
    }
}
