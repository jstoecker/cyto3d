package edu.miami.cyto3d.graph.view.shapes;

public class Tetrahedron implements Polyhedron {

   private final float[][] vertices;
   private final float[][] normals;
   private final int[][]   triangles;

   public Tetrahedron(float size) {

      float s = size / 2;
      vertices = new float[][] { { 0, s, 0 }, { s, -s, -s }, { 0, -s, s }, { -s, -s, -s }, };

      triangles = new int[][] { { 0, 3, 2 }, { 0, 2, 1 }, { 0, 1, 3 }, { 2, 3, 1 } };

      normals = new float[vertices.length][3];
      for (int i = 0; i < normals.length; i++)
         for (int j = 0; j < 3; j++)
            normals[i][j] = vertices[i][j] / s;
   }

   @Override
   public float[][] getVertices() {
      return vertices;
   }

   @Override
   public int[][] getTriangles() {
      return triangles;
   }

   @Override
   public float[][] getNormals() {
      return normals;
   }
}
