package edu.miami.jgloo.content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.jgloo.Texture2D;
import edu.miami.jgloo.mesh.Mesh;
import edu.miami.jgloo.mesh.MeshPart;
import edu.miami.jgloo.mesh.ObjMaterial;
import edu.miami.jgloo.mesh.ObjMeshImporter;

public class CMModel extends CMContent {

  Mesh mesh = null;

  public CMModel(String name, ContentManager manager) {
    super(name, manager);
  }

  public Mesh get() {
    return mesh;
  }

  public void readMeshData() {
    ObjMeshImporter importer = new ObjMeshImporter(manager.modelDir, manager.materialDir,
        manager.textureDir);

    try {
      InputStream is;
      if (manager.loadFromFile) {
        is = new FileInputStream(new File(manager.modelDir, name));
      } else {
        String resource = new File(manager.modelDir, name).getPath();
        is = getClass().getClassLoader().getResourceAsStream(resource);
      }
      mesh = importer.loadMesh(new BufferedReader(new InputStreamReader(is)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Texture2D blankTex = manager.blankTexture.get();
    for (MeshPart p : mesh.getParts()) {
      if (p.getMaterial() instanceof ObjMaterial) {
        ObjMaterial mat = (ObjMaterial) p.getMaterial();
        if (mat.getTexture() == null && mat.getTextureSource() == null) {
          mat.setTexture(blankTex, false);
        } else {
          mat.setTexParams(manager.defaultTexParams);
        }
      }
    }
  }

  /**
   * Copies material information from src material to target material
   */
  public void replaceMaterial(String target, ObjMaterial src) {
    for (MeshPart part : mesh.getParts()) {
      ObjMaterial mat = (ObjMaterial) part.getMaterial();
      if (mat.getName().equals(target)) {
        if (src.getTexture() != null) mat.setTexture(src.getTexture(), false);
        mat.setAmbient(src.getAmbient());
        mat.setDiffuse(src.getDiffuse());
        mat.setSpecular(src.getSpecular());
        mat.setShininess(src.getShininess());
        return;
      }
    }
  }

  protected void initialize(GL2 gl) {
    if (initialized) return;
    mesh.init(gl, manager.meshRenderMode);
    initialized = true;
  }

  @Override
  public void dispose(GL gl) {
    if (disposed) return;
    if (mesh != null) mesh.dispose(gl);
    disposed = true;
  }
}
