package edu.kis.powp.jobs2d.drivers;

import edu.kis.powp.jobs2d.Job2dDriver;

public class TransformerDriver implements Job2dDriver {
    private Job2dDriver driver;
    private double scaleX;
    private double scaleY;

    public TransformerDriver(Job2dDriver driver, double scaleX, double scaleY) {
        this.driver = driver;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public void setPosition(int x, int y) {
        int[] coords = calculateCoordinates(x, y);
        this.driver.setPosition(coords[0], coords[1]);
    }

    @Override
    public void operateTo(int x, int y) {
        int[] coords = calculateCoordinates(x, y);
        this.driver.operateTo(coords[0], coords[1]);
    }

    private int[] calculateCoordinates(int x, int y) {
        int newX = (int) (x * scaleX);
        int newY = (int) (int) (y * scaleY);
        return new int[]{newX, newY};
    }

    @Override
    public String toString() {
        return "Transform: " + driver.toString();
    }
}