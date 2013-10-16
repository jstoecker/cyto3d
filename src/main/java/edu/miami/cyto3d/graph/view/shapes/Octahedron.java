package edu.miami.cyto3d.graph.view.shapes;

public class Octahedron implements Polyhedron {

   private final float[][] vertices;
   private final float[][] normals;
   private final int[][]   triangles;

   public Octahedron(float size) {

      float s = size / 2;
      vertices = new float[][] { { s, 0, 0 }, { 0, 0, s }, { -s, 0, 0 }, { 0, 0, -s }, { 0, s, 0 },
            { 0, -s, 0 } };

      triangles = new int[][] { { 4, 0, 3 }, { 4, 3, 2 }, { 4, 2, 1 }, { 4, 1, 0 }, { 5, 0, 1 },
            { 5, 1, 2 }, { 5, 2, 3 }, { 5, 3, 0 } };

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
