package com.cgvsu.Affinetransform;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector4f;

import java.util.ArrayList;
import java.util.List;

public interface AffineTransformation {
    Matrix4f getMatrix();

    default Vector3f transform(Vector3f v) {
        Vector4f resVertex = getMatrix().mulVector(Vector4f.toVec4(v));
        return new Vector3f(resVertex.x, resVertex.y, resVertex.z);
    }

    default List<Vector3f> transform(List<Vector3f> v) {
        Matrix4f m = getMatrix();
        List<Vector3f> resV = new ArrayList<>();

        for (int i = 0; i < v.size(); i++) {
            Vector4f resVertex = m.mulVector(Vector4f.toVec4(v.get(i)));
            resV.add(new Vector3f(resVertex.x, resVertex.y, resVertex.z));
        }

        return resV;
    }

    /*default Iterable<Vector3f> tra(Iterable<Vector3f> d) {

    }*/
}
