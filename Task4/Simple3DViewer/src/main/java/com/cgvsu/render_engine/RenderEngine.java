package com.cgvsu.render_engine;

import java.util.ArrayList;
import java.util.Arrays;

import com.cgvsu.model.Polygon;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javax.vecmath.*;
import com.cgvsu.model.Model;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void renderConstruct(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height) {
        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.polygons.get(polygonInd).getVertexIndices().size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                com.cgvsu.math.Vector3f vertex = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                javax.vecmath.Vector3f vertexVecmath = new javax.vecmath.Vector3f(vertex.x, vertex.y, vertex.z);

                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath), width, height);
                resultPoints.add(resultPoint);
            }

            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).x,
                        resultPoints.get(vertexInPolygonInd - 1).y,
                        resultPoints.get(vertexInPolygonInd).x,
                        resultPoints.get(vertexInPolygonInd).y);
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).x,
                        resultPoints.get(nVerticesInPolygon - 1).y,
                        resultPoints.get(0).x,
                        resultPoints.get(0).y);
        }
    }

    public static void renderColor(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height) {
        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        float[][] zBuffer = new float[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                zBuffer[i][j] = Float.POSITIVE_INFINITY;
            }
        }

        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            if (mesh.polygons.get(polygonInd).getVertexIndices().size() != 3) {
                continue;
            }

            ArrayList<Point3f> resultPoints = new ArrayList<>();
            for (int vertexInd : mesh.polygons.get(polygonInd).getVertexIndices()) {
                com.cgvsu.math.Vector3f vertex = mesh.vertices.get(vertexInd);
                Vector3f vertextemp = convertToVecmath(vertex);
                Vector3f transformedVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertextemp);

                Point2f screenPoint = vertexToPoint(transformedVertex, width, height);
                resultPoints.add(new Point3f(screenPoint.x, screenPoint.y, transformedVertex.z));
            }

            fillTriangleWithZBuffer(graphicsContext, zBuffer, resultPoints.get(0), Color.RED, resultPoints.get(1), Color.GREEN, resultPoints.get(2), Color.BLUE);
        }
    }

    public static void renderWithTexture(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
            final Image texture,
            final int light) {

        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        float[][] zBuffer = new float[width][height];
        for (int i = 0; i < width; i++) {
            Arrays.fill(zBuffer[i], Float.POSITIVE_INFINITY);
        }

        final int nPolygons = mesh.polygons.size();
        PixelReader pixelReader = texture.getPixelReader();
        int texWidth = (int) texture.getWidth();
        int texHeight = (int) texture.getHeight();

        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            if (mesh.polygons.get(polygonInd).getVertexIndices().size() != 3) {
                continue;
            }

            ArrayList<Point3f> resultPoints = new ArrayList<>();
            ArrayList<Point2f> texturePoints = new ArrayList<>();
            ArrayList<Vector3f> normalVectors = new ArrayList<>();

            for (int vertexInd = 0; vertexInd < 3; vertexInd++) {
                int modelVertexIndex = mesh.polygons.get(polygonInd).getVertexIndices().get(vertexInd);
                com.cgvsu.math.Vector3f vertex = mesh.vertices.get(modelVertexIndex);
                Vector3f vertexVecmath = convertToVecmath(vertex);
                Vector3f transformedVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath);

                Point2f screenPoint = vertexToPoint(transformedVertex, width, height);
                resultPoints.add(new Point3f(screenPoint.x, screenPoint.y, transformedVertex.z));

                com.cgvsu.math.Vector3f normal = mesh.normals.get(modelVertexIndex);
                normalVectors.add(new Vector3f(normal.x, normal.y, normal.z));

                int textureIndex = mesh.polygons.get(polygonInd).getTextureVertexIndices().get(vertexInd);
                com.cgvsu.math.Vector2f texCoord = mesh.textureVertices.get(textureIndex);
                texturePoints.add(new Point2f(texCoord.x, texCoord.y));
            }

            switch (light) {
                case 0:
                    fillTriangleWithTexture(graphicsContext, zBuffer,
                            resultPoints.get(0), texturePoints.get(0),
                            resultPoints.get(1), texturePoints.get(1),
                            resultPoints.get(2), texturePoints.get(2),
                            pixelReader, texWidth, texHeight);
                    break;
                case 1:
                    fillTriangleWithTextureLight(graphicsContext, zBuffer,
                            resultPoints.get(0), texturePoints.get(0), normalVectors.get(0),
                            resultPoints.get(1), texturePoints.get(1), normalVectors.get(1),
                            resultPoints.get(2), texturePoints.get(2), normalVectors.get(2),
                            pixelReader, texWidth, texHeight,
                            camera.getViewDirection(),
                            Color.WHITE,
                            0.2f
                    );
                    break;
            }
        }
    }


    private static void fillTriangleWithTextureLight(
            GraphicsContext graphicsContext,
            float[][] zBuffer,
            Point3f p1, Point2f uv1, Vector3f n1,
            Point3f p2, Point2f uv2, Vector3f n2,
            Point3f p3, Point2f uv3, Vector3f n3,
            PixelReader texture,
            int texWidth, int texHeight,
            Vector3f lightDir,
            Color lightColor,
            float ambient
    ) {
        int minX = (int) Math.max(0, Math.min(p1.x, Math.min(p2.x, p3.x)));
        int maxX = (int) Math.min(zBuffer.length - 1, Math.max(p1.x, Math.max(p2.x, p3.x)));
        int minY = (int) Math.max(0, Math.min(p1.y, Math.min(p2.y, p3.y)));
        int maxY = (int) Math.min(zBuffer[0].length - 1, Math.max(p1.y, Math.max(p2.y, p3.y)));

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                float[] barycentric = barycentricCoords(p1, p2, p3, x, y);
                float alpha = barycentric[0], beta = barycentric[1], gamma = barycentric[2];

                if (alpha >= 0 && beta >= 0 && gamma >= 0) {
                    float z = alpha * p1.z + beta * p2.z + gamma * p3.z;

                    if (z < zBuffer[x][y]) {
                        zBuffer[x][y] = z;

                        // Интерполяция нормали
                        Vector3f normal = new Vector3f(
                                alpha * n1.x + beta * n2.x + gamma * n3.x,
                                alpha * n1.y + beta * n2.y + gamma * n3.y,
                                alpha * n1.z + beta * n2.z + gamma * n3.z
                        );
                        normal.normalize();

                        float diffuse = Math.max(0, normal.dot(lightDir));
                        float lightIntensity = ambient + diffuse;

                        float u = alpha * uv1.x + beta * uv2.x + gamma * uv3.x;
                        float v = alpha * uv1.y + beta * uv2.y + gamma * uv3.y;

                        int texX = Math.min(texWidth - 1, Math.max(0, (int) (u * texWidth)));
                        int texY = Math.min(texHeight - 1, Math.max(0, (int) ((1 - v) * texHeight)));

                        javafx.scene.paint.Color texColor = texture.getColor(texX, texY);

                        Color shadedColor = Color.color(
                                Math.min(texColor.getRed() * lightColor.getRed() * lightIntensity, 1.0),
                                Math.min(texColor.getGreen() * lightColor.getGreen() * lightIntensity, 1.0),
                                Math.min(texColor.getBlue() * lightColor.getBlue() * lightIntensity, 1.0)
                        );

                        graphicsContext.getPixelWriter().setColor(x, y, shadedColor);
                    }
                }
            }
        }
    }


    public static javax.vecmath.Vector3f convertToVecmath(com.cgvsu.math.Vector3f v) {
        return new javax.vecmath.Vector3f(v.x, v.y, v.z);
    }

    public static com.cgvsu.math.Vector3f convertFromVecmath(Vector3f v) {
        return new com.cgvsu.math.Vector3f(v.x, v.y, v.z);
    }

    private static void fillTriangleWithTexture(
            GraphicsContext graphicsContext,
            float[][] zBuffer,
            Point3f p1, Point2f uv1,
            Point3f p2, Point2f uv2,
            Point3f p3, Point2f uv3,
            PixelReader texture,
            int texWidth, int texHeight) {

        int minX = (int) Math.max(0, Math.min(p1.x, Math.min(p2.x, p3.x)));
        int maxX = (int) Math.min(zBuffer.length - 1, Math.max(p1.x, Math.max(p2.x, p3.x)));
        int minY = (int) Math.max(0, Math.min(p1.y, Math.min(p2.y, p3.y)));
        int maxY = (int) Math.min(zBuffer[0].length - 1, Math.max(p1.y, Math.max(p2.y, p3.y)));

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                float[] barycentric = barycentricCoords(p1, p2, p3, x, y);
                float alpha = barycentric[0], beta = barycentric[1], gamma = barycentric[2];

                if (alpha >= 0 && beta >= 0 && gamma >= 0) {
                    float z = alpha * p1.z + beta * p2.z + gamma * p3.z;

                    if (z < zBuffer[x][y]) {
                        zBuffer[x][y] = z;

                        float u = alpha * uv1.x + beta * uv2.x + gamma * uv3.x;
                        float v = alpha * uv1.y + beta * uv2.y + gamma * uv3.y;

                        int texX = Math.min(texWidth - 1, Math.max(0, (int) (u * texWidth)));
                        int texY = Math.min(texHeight - 1, Math.max(0, (int) ((1 - v) * texHeight)));

                        javafx.scene.paint.Color texColor = texture.getColor(texX, texY);
                        graphicsContext.getPixelWriter().setColor(x, y, texColor);
                    }
                }
            }
        }
    }



    private static void fillTriangleWithZBuffer(
            GraphicsContext graphicsContext,
            float[][] zBuffer,
            Point3f p1, javafx.scene.paint.Color c1,
            Point3f p2, javafx.scene.paint.Color c2,
            Point3f p3, javafx.scene.paint.Color c3) {

        int minX = (int) Math.max(0, Math.min(p1.x, Math.min(p2.x, p3.x)));
        int maxX = (int) Math.min(zBuffer.length - 1, Math.max(p1.x, Math.max(p2.x, p3.x)));
        int minY = (int) Math.max(0, Math.min(p1.y, Math.min(p2.y, p3.y)));
        int maxY = (int) Math.min(zBuffer[0].length - 1, Math.max(p1.y, Math.max(p2.y, p3.y)));

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                float[] barycentric = barycentricCoords(p1, p2, p3, x, y);
                float alpha = barycentric[0], beta = barycentric[1], gamma = barycentric[2];

                if (alpha >= 0 && beta >= 0 && gamma >= 0) {
                    float z = alpha * p1.z + beta * p2.z + gamma * p3.z;

                    if (z < zBuffer[x][y]) {
                        zBuffer[x][y] = z;

                        double r = alpha * c1.getRed() + beta * c2.getRed() + gamma * c3.getRed();
                        double g = alpha * c1.getGreen() + beta * c2.getGreen() + gamma * c3.getGreen();
                        double b = alpha * c1.getBlue() + beta * c2.getBlue() + gamma * c3.getBlue();

                        javafx.scene.paint.Color interpolatedColor = new javafx.scene.paint.Color(r, g, b, 1.0);
                        graphicsContext.getPixelWriter().setColor(x, y, interpolatedColor);
                    }
                }
            }
        }
    }


    private static float[] barycentricCoords(Point3f p1, Point3f p2, Point3f p3, int x, int y) {
        float detT = (p2.y - p3.y) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.y - p3.y);
        float alpha = ((p2.y - p3.y) * (x - p3.x) + (p3.x - p2.x) * (y - p3.y)) / detT;
        float beta = ((p3.y - p1.y) * (x - p3.x) + (p1.x - p3.x) * (y - p3.y)) / detT;
        float gamma = 1.0f - alpha - beta;
        return new float[]{alpha, beta, gamma};
    }
}