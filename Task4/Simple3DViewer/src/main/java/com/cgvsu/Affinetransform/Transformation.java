package com.cgvsu.Affinetransform;


import com.cgvsu.math.Matrix4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Transformation implements AffineTransformation, DataList<AffineTransformation> {
    private final List<AffineTransformation> affineTransformations = new ArrayList<>();
    private boolean isCalculated = false;
    private Matrix4f trsMatrix;

    public Transformation(AffineTransformation... ats) {
        Collections.addAll(affineTransformations, ats);
    }

    public Transformation() {
    }

    public boolean isCalculated() {
        return isCalculated;
    }


    @Override
    public Matrix4f getMatrix() {
        if (isCalculated()) {
            return trsMatrix;
        }

        trsMatrix = new Matrix4f(new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });

        for (AffineTransformation at : affineTransformations) {
            trsMatrix = Matrix4f.multiply(trsMatrix, at.getMatrix());
        }
        isCalculated = true;
        return trsMatrix;
    }

    @Override
    public void add(AffineTransformation at) {
        affineTransformations.add(at);
        isCalculated = false;
    }

    @Override
    public void remove(int index) {
        affineTransformations.remove(index);
        isCalculated = false;
    }

    @Override
    public void remove(AffineTransformation at) {
        affineTransformations.remove(at);
        isCalculated = false;
    }

    @Override
    public void set(int index, AffineTransformation at) {
        affineTransformations.set(index, at);
        isCalculated = false;
    }

    @Override
    public AffineTransformation get(int index) {
        return affineTransformations.get(index);
    }
}
