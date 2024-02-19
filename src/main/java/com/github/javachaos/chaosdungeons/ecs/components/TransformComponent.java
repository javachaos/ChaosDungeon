package com.github.javachaos.chaosdungeons.ecs.components;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TransformComponent extends Component {
    private final Matrix4f transform;
    private final Vector3f position;
    private final Quaternionf rotation;
    private final Vector3f scale;

    public TransformComponent() {
        transform = new Matrix4f();
        transform.identity();
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = new Vector3f(1, 1, 1);
    }

    public void setTransform(Matrix4f transform) {
        this.transform.set(transform);
    }

    public Matrix4f getTransform() {
        return transform;
    }

    @Override
    public void update(double dt) {
        transform.translate(position)
                 .rotation(rotation)
                 .scale(scale);
    }

    @Override
    public void destroy() {
        transform.identity();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void setRotation(Quaternionf rot) {
        this.rotation.set(rot);
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public Vector3f getRotation3f() {
        return getRotation().getEulerAnglesXYZ(new Vector3f());
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale.set(scale);
    }
}
