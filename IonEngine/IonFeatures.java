package IonEngine;

import java.awt.Color;
import java.awt.image.Raster;

/**
 * Utility class providing common functionality for the Ion Engine.
 * Contains methods for timing, random generation, color manipulation, and
 * console output.
 */
public class IonFeatures {
    /**
     * Executes a callback function after a specified delay.
     * Similar to JavaScript's setTimeout function.
     *
     * @param time     The delay in milliseconds before executing the callback
     * @param callback The callback function to execute after the delay
     */
    public static void setTimeout(long time, IonCallback callback) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        } finally {
            callback.call();
        }
    }

    /**
     * Repeatedly executes a callback function at specified intervals.
     * Similar to JavaScript's setInterval function.
     * The execution continues until the callback's activated flag is set to false.
     *
     * @param time     The interval in milliseconds between callback executions
     * @param callback The callback function to execute repeatedly
     */
    public static void setInterval(long time, IonCallback callback) {
        while (callback.activated) {
            try {
                Thread.sleep(time);
            } catch (Exception e) {
            } finally {
                callback.call();
            }
        }
    }

    /**
     * Generates a random integer between the specified minimum and maximum values
     * (inclusive).
     *
     * @param min The minimum value of the range
     * @param max The maximum value of the range
     * @return A random integer between min and max (inclusive)
     */
    public static int random(int min, int max) {
        return (int) (Math.random() * (max + 1) + min);
    }

    /**
     * Generates a random Color object with random RGB values.
     * Each color component (red, green, blue) will be between 0 and 255.
     *
     * @return A new Color object with random RGB values
     */
    public static Color randomColor() {
        return new Color(random(0, 255), random(0, 255), random(0, 255));
    }

    /**
     * Retrieves the color of a pixel at the specified coordinates from a Raster.
     * Returns null if the coordinates are outside the Raster's bounds.
     *
     * @param raster The Raster object to get the pixel from
     * @param x      The x-coordinate of the pixel
     * @param y      The y-coordinate of the pixel
     * @return The Color at the specified coordinates, or null if out of bounds
     */
    public static Color getRasterPixel(Raster raster, int x, int y) {
        if (x >= raster.getWidth() || y >= raster.getHeight() || x < 0 || y < 0)
            return null;
        double[] array = raster.getPixel(x, y, new double[3]);
        return new Color((int) array[0], (int) array[1], (int) array[2]);
    }

    /**
     * Prints the arguments to the console.
     * 
     * @param args The arguments to print.
     * @return The arguments as a string.
     */
    public static String print(Object... args) {
        if (args.length == 0)
            return "";

        StringBuilder result = new StringBuilder(args[0].toString());
        for (int i = 1; i < args.length; i++) {
            result.append(" ").append(args[i]);
        }
        System.out.println(result);
        return result.toString();
    }
}
