package edu.miami.cyto3d.graph.view.shapes;

import edu.miami.math.geom.Geosphere;
import edu.miami.math.vector.Vec3;

public class Sphere extends Geosphere implements Polyhedron {

   private float[][] normals;

   public Sphere(float radius, int level) {
      super(radius, level);

      float[][] v = getVertices();
      normals = new float[v.length][3];
      for (int i = 0; i < normals.length; i++)
         normals[i] = new Vec3(v[i]).normalize().values();
   }

   @Override
   public float[][] getVertices() {
      return getVerts();
   }

   @Override
   public float[][] getNormals() {
      return normals;
   }
}
