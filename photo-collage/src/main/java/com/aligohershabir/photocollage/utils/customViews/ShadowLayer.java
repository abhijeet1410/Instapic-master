package com.aligohershabir.photocollage.utils.customViews;

public class ShadowLayer {

    private float radius;
    private float dx;
    private float dy;
    private int color;

    public ShadowLayer(float radius, float dx, float dy, int color){
        this.radius = radius;
        this.dx = dx;
        this.dy = dy;
        this.color = color;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
