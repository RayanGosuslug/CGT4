package com.cgvsu.objreader;

import com.cgvsu.model.Polygon;

import java.util.ArrayList;

public class Triangulation {
    public static ArrayList<Polygon> triangulatePolygon(Polygon poly) {
        int vertexNum = poly.getVertexIndices().size();
        ArrayList<Polygon> polygons = new ArrayList<>();

        if (vertexNum == 3) {
            polygons.add(poly);
            return polygons;
        }

        for (int i = 2; i < vertexNum - 1; i++) {
            Polygon currPoly = new Polygon();

            currPoly.getVertexIndices().add(poly.getVertexIndices().get(0));
            currPoly.getVertexIndices().add(poly.getVertexIndices().get(i - 1));
            currPoly.getVertexIndices().add(poly.getVertexIndices().get(i));

            if (!poly.getTextureVertexIndices().isEmpty()) {
                currPoly.getTextureVertexIndices().add(poly.getTextureVertexIndices().get(0));
                currPoly.getTextureVertexIndices().add(poly.getTextureVertexIndices().get(i - 1));
                currPoly.getTextureVertexIndices().add(poly.getTextureVertexIndices().get(i));
            }

            polygons.add(currPoly);
        }

        if (vertexNum > 3) {
            Polygon currPoly = new Polygon();

            // Триангулируем вершины
            currPoly.getVertexIndices().add(poly.getVertexIndices().get(0));
            currPoly.getVertexIndices().add(poly.getVertexIndices().get(vertexNum - 2));
            currPoly.getVertexIndices().add(poly.getVertexIndices().get(vertexNum - 1));

            // Триангулируем текстурные координаты
            if (!poly.getTextureVertexIndices().isEmpty()) {
                currPoly.getTextureVertexIndices().add(poly.getTextureVertexIndices().get(0));
                currPoly.getTextureVertexIndices().add(poly.getTextureVertexIndices().get(vertexNum - 2));
                currPoly.getTextureVertexIndices().add(poly.getTextureVertexIndices().get(vertexNum - 1));
            }

            polygons.add(currPoly);
        }

        return polygons;
    }

    public static ArrayList<Polygon> triangulateModel(ArrayList<Polygon> polygons) {
        ArrayList<Polygon> newModelPoly = new ArrayList<>();

        for (Polygon poly : polygons) {
            newModelPoly.addAll(triangulatePolygon(poly));
        }

        return newModelPoly;
    }
}
