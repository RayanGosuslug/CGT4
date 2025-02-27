package com.cgvsu.math;


import static com.cgvsu.math.Global.EPS;
import static com.cgvsu.math.Global.sqrt;

public class Vector4f implements Vector<Vector4f> {
    public float x, y, z, w;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(float[] v) {
        this.x = v[0];
        this.y = v[1];
        this.z = v[2];
        this.w = v[3];
    }

    public static Vector4f addition(final Vector4f v1, final Vector4f v2) {
        return new Vector4f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z, v1.w + v2.w);
    }

    public static Vector4f subtraction(final Vector4f v1, final Vector4f v2) {
        return new Vector4f(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z, v1.w - v2.w);
    }

    public static float dotProduct(final Vector4f v1, final Vector4f v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z + v1.w * v2.w;
    }

    public static Vector4f toVec4(Vector3f v) {
        return new Vector4f(v.x, v.y, v.z, 1.0F);
    }

    @Override
    public void add(final Vector4f v) {
        x += v.x;
        y += v.y;
        z += v.z;
        w += v.w;
    }

    @Override
    public void sub(final Vector4f v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        w -= v.w;
    }

    @Override
    public void sub(Vector3f var1, Vector3f var2) {

    }

    @Override
    public Vector4f multiply(float c) {
        return new Vector4f(c * x, c * y, c * z, c * w);
    }

    @Override
    public void mult(float c) {
        x *= c;
        y *= c;
        z *= c;
        w *= c;
    }

    @Override
    public Vector4f divide(float c) {
        if (c < EPS) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return new Vector4f(x / c, y / c, z / c, w / c);
    }

    @Override
    public void div(float c) {
        if (c < EPS) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        x /= c;
        y /= c;
        z /= c;
        w /= c;
    }

    @Override
    public float length() {
        return (float) sqrt(x * x + y * y + z * z + w * w);
    }

    @Override
    public Vector4f normal() {
        final float length = this.length();
        if (length < EPS) {
            throw new ArithmeticException("Normalization of a zero vector is not allowed.");
        }
        return this.divide(length);
    }

    @Override
    public boolean equals(final Vector4f other) {
        return Math.abs(x - other.x) < EPS
                && Math.abs(y - other.y) < EPS
                && Math.abs(z - other.z) < EPS
                && Math.abs(w - other.w) < EPS;
    }
}
