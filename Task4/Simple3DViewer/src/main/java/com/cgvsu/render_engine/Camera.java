package com.cgvsu.render_engine;
import com.cgvsu.Affinetransform.AffineTransformation;
import com.cgvsu.Affinetransform.Rotator;
import com.cgvsu.Affinetransform.Transformation;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;

import static com.cgvsu.render_engine.RenderEngine.convertFromVecmath;
import static com.cgvsu.render_engine.RenderEngine.convertToVecmath;

public class Camera {

    public Camera(
            final Vector3f position,
            final Vector3f target,
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public void setPosition(final Vector3f position) {
        this.position = position;
    }

    public void setTarget(final Vector3f target) {
        this.target = target;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getTarget() {
        return target;
    }

    public void movePosition(final Vector3f translation) {
        this.position.add(translation);
        this.target.add(translation);
    }

    Matrix4f getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }

    Matrix4f getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    public Vector3f getViewDirection() {
        Vector3f direction = new Vector3f(position.x - target.x, position.y - target.y, position.z - target.z);
        direction.normalize();
        return direction;
    }

    public void Rotate(float x, float y, float z) {
        AffineTransformation affineTransformation = new Transformation(
                new Rotator(x, Rotator.Axis.X),
                new Rotator(y, Rotator.Axis.Y),
                new Rotator(z, Rotator.Axis.Z));
        this.position = convertToVecmath(affineTransformation.transform(convertFromVecmath(this.position)));
    }

    private Vector3f position;
    private Vector3f target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;
}