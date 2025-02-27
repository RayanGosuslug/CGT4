package com.cgvsu.Affinetransform;


import com.cgvsu.math.Matrix4f;

import java.util.Objects;

public class Rotator implements AffineTransformation {
    private final float angle;
    private final Axis axis;

    public Rotator(int dangle, Axis axis) {
        this.angle = (float) Math.toRadians(dangle);
        this.axis = axis;
    }

    public Rotator(float rangle, Axis axis) {
        this.angle = rangle;
        this.axis = axis;
    }

    @Override
    public Matrix4f getMatrix() {
        float cosA = (float) Math.cos(angle);
        float sinA = (float) Math.sin(angle);

        switch (axis) {
            case X -> {
                return new Matrix4f(new float[][]{
                        {1, 0, 0, 0},
                        {0, cosA, sinA, 0},
                        {0, -sinA, cosA, 0},
                        {0, 0, 0, 1}
                });
            }
            case Y -> {
                return new Matrix4f(new float[][]{
                        {cosA, 0, -sinA, 0},
                        {0, 1, 0, 0},
                        {sinA, 0, cosA, 0},
                        {0, 0, 0, 1}
                });
//
            }
            case Z -> {
                return new Matrix4f(new float[][]{
                        {cosA, sinA, 0, 0},
                        {-sinA, cosA, 0, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 1}
                });
            }
            default -> {
                return Matrix4f.one();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rotator rotator = (Rotator) o;
        return Float.compare(angle, rotator.angle) == 0 && axis == rotator.axis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(angle, axis);
    }

    public enum Axis {
        X, Y, Z
    }
}

