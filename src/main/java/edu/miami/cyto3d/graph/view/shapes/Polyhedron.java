package edu.miami.cyto3d.graph.view.shapes;

/**
 * Description of a geometric shape that can be used as a renderable mesh.
 * 
 * @author justin
 */
public interface Polyhedron {

   /**
    * Returns an array of vertex positions in the shape. These positions must be relative to the
    * origin (0,0,0) and not some other local origin.
    */
   public float[][] getVertices();

   /**
    * Returns an array of vertex indices for each triangle in the shape. For example, triangles[i]
    * is an array of vertex indices for the i-th triangle, and triangles[i][j] corresponds to the
    * index of the j-th vertex in the i-th triangle. The total number of values must be
    * triangles.length * 3; no other face type other than triangles is permitted. Triangles should
    * be facing CCW.
    */
   public int[][] getTriangles();

   /**
    * Returns an array of vertex normals for each vertex in the shape. The number of normals must be
    * equal to the number of vertices.
    */
   public float[][] getNormals();
}