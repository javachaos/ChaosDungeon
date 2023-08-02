package com.github.javachaos.chaosdungeons.geometry.polygons;

/**
 * Half-edge implementation based on
 * <a href="https://www.graphics.rwth-aachen.de/media/openmesh_static/Documentations/OpenMesh-6.3-Documentation/a00010.html">OpenMesh</a>.
 */
@SuppressWarnings("unused")
public class HalfEdge {
  private Vertex vertex;
  private Face face;
  private HalfEdge next; //clockwise* (OpenMesh is counterclockwise)
  private HalfEdge prev; //counter-clockwise
  private HalfEdge otherHalfEdge;

}
