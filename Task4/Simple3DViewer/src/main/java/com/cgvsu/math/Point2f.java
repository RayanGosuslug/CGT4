package com.cgvsu.math;


import static com.cgvsu.math.Global.sqrt;

public class Point2f {
    public float x;
    public float y;

    public Point2f(float var1, float var2) {
        this.x = var1;
        this.y = var2;
    }

    public Point2f(float[] var1) {
        this.x = var1[0];
        this.y = var1[1];
    }

    public Point2f(Point2f var1) {
        this.x = var1.x;
        this.y = var1.y;
    }

    public Point2f() {
    }

    public final float distanceSquared(Point2f var1) {
        float var2 = this.x - var1.x;
        float var3 = this.y - var1.y;
        return var2 * var2 + var3 * var3;
    }

    public final float distance(Point2f var1) {
        float var2 = this.x - var1.x;
        float var3 = this.y - var1.y;
        return (float) sqrt((var2 * var2 + var3 * var3));
    }

    public final float distanceL1(Point2f var1) {
        return Math.abs(this.x - var1.x) + Math.abs(this.y - var1.y);
    }

    public final float distanceLinf(Point2f var1) {
        return Math.max(Math.abs(this.x - var1.x), Math.abs(this.y - var1.y));
    }
}
