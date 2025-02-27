package com.cgvsu.model;
import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

import java.awt.image.BufferedImage;
import java.util.*;

public class Model {

    public ArrayList<Point2f> texCoords;
    public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<Vector2f>();
    public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();
    private BufferedImage texture;
    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public BufferedImage getTexture() {
        return texture;
    }
}
