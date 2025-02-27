package com.cgvsu.math;

import java.util.Arrays;

import static com.cgvsu.math.Global.EPS;

public class Matrix4f {

    static final private int SIZE = 4;
    public float[][] mat;

    public Matrix4f(float[][] mat) {
        if (mat.length != SIZE || mat[0].length != SIZE) {
            throw new IllegalArgumentException("Matrix must be 4x4");
        }
        this.mat = mat;
    }

    public Matrix4f() {
        this.mat = new float[SIZE][SIZE];
    }

    public Matrix4f(Matrix4f var) {
        this.mat = var.mat;
    }


    public Matrix4f(float num) {
        this.mat = new float[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            this.mat[i][i] = num;
        }
    }

    public static Matrix4f one() {
        float[][] oneData = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
        return new Matrix4f(oneData);
    }

    public static Matrix4f add(final Matrix4f m1, final Matrix4f m2) {
        Matrix4f res = new Matrix4f(new float[SIZE][SIZE]);
        for (int col = 0; col < SIZE; col++) {
            for (int row = 0; row < SIZE; row++) {
                res.mat[col][row] = m1.mat[col][row] + m2.mat[col][row];
            }
        }
        return res;
    }

    public static Matrix4f sub(final Matrix4f m1, final Matrix4f m2) {
        Matrix4f res = new Matrix4f(new float[SIZE][SIZE]);
        for (int col = 0; col < SIZE; col++) {
            for (int row = 0; row < SIZE; row++) {
                res.mat[col][row] = m1.mat[col][row] - m2.mat[col][row];
            }
        }
        return res;
    }


    public static Matrix4f multiply(final Matrix4f m1, final Matrix4f m2) {
        Matrix4f res = new Matrix4f(new float[SIZE][SIZE]);
        for (int m2col = 0; m2col < SIZE; m2col++) {
            for (int ma1row = 0; ma1row < SIZE; ma1row++) {
                float a = 0;
                for (int i = 0; i < SIZE; i++) {
                    a += m1.mat[m2col][i] * m2.mat[i][ma1row];
                }
                res.mat[m2col][ma1row] = a;
            }
        }
        return res;
    }

    public Vector4f mulVector(final Vector4f v) {
        float[] arr = new float[]{v.x, v.y, v.z, v.w};
        float[] res = new float[SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                res[row] += this.mat[row][col] * arr[col];
            }
        }
        return new Vector4f(res[0], res[1], res[2], res[3]);
    }

    public float determinant(float[][] mat) {
        float det = 0;
        for (int i = 0; i < SIZE; i++) {
            float[][] minor = getMinor(0, i);
            Matrix3f minorMatrix = new Matrix3f(minor);
            det += (float) (Math.pow(-1, i) * mat[0][i] * minorMatrix.determinant());
        }
        return det;
    }

    public Matrix4f invert(float[][] matrix) {
        if (matrix.length != 4 || matrix[0].length != 4) {
            throw new IllegalArgumentException("Матрица должна быть размером 4x4.");
        }

        // Копируем исходную матрицу, чтобы не изменять ее
        float[][] result = Arrays.stream(matrix).map(float[]::clone).toArray(float[][]::new);

        // Определитель матрицы
        double det = determinant(matrix);

        if (det == 0) {
            throw new ArithmeticException("Определитель матрицы равен нулю, невозможно найти обратную матрицу.");
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int subi = i > 0 ? i : 1;
                int subj = j > 0 ? j : 1;
                double minorDet = determinant(getMinor(subi, subi));
                result[j][i] = (float) (((i + j) % 2 == 0 ? 1 : -1) * minorDet / det);
            }
        }

        return new Matrix4f(result);
    }

    private float[][] getMinor(int row, int col) {
        float[][] minor = new float[3][3];
        int minorRow = 0;
        for (int i = 0; i < SIZE; i++) {
            if (i == row) continue;
            int minorCol = 0;
            for (int j = 0; j < SIZE; j++) {
                if (j == col) continue;
                minor[minorRow][minorCol] = mat[i][j];
                minorCol++;
            }
            minorRow++;
        }
        return minor;
    }

    public Matrix4f cloneMatrix() {
        Matrix4f clonedMatrix = new Matrix4f();
        for (int i = 0; i < Matrix4f.SIZE; i++) {
            System.arraycopy(this.mat[i], 0, clonedMatrix.mat[i], 0, Matrix4f.SIZE);
        }
        return clonedMatrix;
    }

    public boolean equals(final Matrix4f other) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (Math.abs(this.mat[row][col] - other.mat[row][col]) >= EPS) {
                    return false;
                }
            }
        }
        return true;
    }
}
