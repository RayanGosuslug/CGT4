package com.cgvsu.Affinetransform;

public interface DataList<T> {
    void add(AffineTransformation at);

    void remove(int index);

    void remove(AffineTransformation at);

    void set(int index, AffineTransformation at);

    T get(int index);
}
