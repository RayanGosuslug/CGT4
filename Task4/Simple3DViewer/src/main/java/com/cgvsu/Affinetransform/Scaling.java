package com.cgvsu.Affinetransform;


import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Vector3f;

import java.util.Objects;


public class Scaling implements AffineTransformation {
    private final Matrix4f scaleMatrix;

    public Scaling(float sx, float sy, float sz) {
        this.scaleMatrix = new Matrix4f(new float[][]{
                {sx, 0, 0, 0},
                {0, sy, 0, 0},
                {0, 0, sz, 0},
                {0, 0, 0, 1}
        });
    }

    public Scaling() {
        this.scaleMatrix = new Matrix4f(new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    @Override
    public Matrix4f getMatrix() {
        return scaleMatrix;
    }

    @Override
    public Vector3f transform(Vector3f v) {
        return new Vector3f(
                scaleMatrix.mat[0][0] * v.x,
                scaleMatrix.mat[1][1] * v.y,
                scaleMatrix.mat[2][2] * v.z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scaling scaling = (Scaling) o;
        return Objects.equals(scaleMatrix, scaling.scaleMatrix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scaleMatrix);
    }
}
