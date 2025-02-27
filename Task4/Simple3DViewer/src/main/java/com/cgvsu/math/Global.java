package com.cgvsu.math;

public class Global {
    public static final float EPS = 1e-6f;


    public static double sqrt(double number) {
        if (number < 0) {
            throw new IllegalArgumentException("Искомое число не может быть меньше 0");
        }
        double low = 0;
        double high = number;
        double mid = 0;

        while (high - low > EPS) {
            mid = (low + high) / 2;
            if (mid * mid < number) {
                low = mid;
            } else {
                high = mid;
            }
        }

        return mid;
    }

}