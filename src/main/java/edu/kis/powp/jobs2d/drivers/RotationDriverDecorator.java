package edu.kis.powp.jobs2d.drivers;

import edu.kis.powp.jobs2d.Job2dDriver;

public class RotationDriverDecorator implements Job2dDriver {
    private Job2dDriver driver;
    private double angle;

    public RotationDriverDecorator(Job2dDriver driver, double angleDegree) {
        this.driver = driver;
        this.angle = Math.toRadians(angleDegree);
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
        int newX = (int) (x * Math.cos(angle) - y * Math.sin(angle));
        int newY = (int) (x * Math.sin(angle) + y * Math.cos(angle));
        return new int[]{newX, newY};
    }

    @Override
    public String toString() {
        return "Rotate: " + driver.toString();
    }
}