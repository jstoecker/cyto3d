package edu.miami.cyto3d.graph.view.shapes;

/**
 * A cube.
 * 
 * @author justin
 */
public class Box implements Polyhedron {

   private final float[][] vertices;
   private final float[][] normals;
   private final int[][]   triangles;

   public Box(float width, float height, float depth) {

      // 5--------------6
      // /| /|
      // | 4--------------7 |
      // | | | | |
      // height | 1------------|-2 /
      // | |/ |/ depth
      // | 0--------------3 /
      //
      // ------width-----

      float w = width / 2;
      float h = height / 2;
      float d = depth / 2;
      vertices = new float[][] { { -w, -h, d }, { -w, -h, -d }, { w, -h, -d }, { w, -h, d },
            { -w, h, d }, { -w, h, -d }, { w, h, -d }, { w, h, d }, };

      triangles = new int[][] { { 4, 0, 3 }, { 4, 3, 7 }, // front
            { 2, 1, 5 }, { 2, 5, 6 }, // back
            { 7, 3, 2 }, { 7, 2, 6 }, // right
            { 1, 0, 4 }, { 1, 4, 5 }, // left
            { 5, 4, 7 }, { 5, 7, 6 }, // top
            { 3, 0, 1 }, { 3, 1, 2 } // bottom
      };

      float n = (float) Math.sqrt(3.0);
      normals = new float[][] { { -n, -n, n }, { -n, -n, -n }, { n, -n, -n }, { n, -n, n },
            { -n, n, n }, { -n, n, -n }, { n, n, -n }, { n, n, n }, };
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
