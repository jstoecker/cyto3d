package edu.miami.jgloo.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.jgloo.Texture2D;
import edu.miami.jgloo.mesh.ObjMaterial;
import edu.miami.jgloo.mesh.ObjMaterialLibrary;

public class CMMatLib extends CMContent {

  ObjMaterialLibrary lib = new ObjMaterialLibrary();

  public CMMatLib(String name, ContentManager manager) throws IOException {
    super(name, manager);

    InputStream is = getContentInputStream(manager.materialDir);
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    lib.load(br, manager.textureDir, manager.loadFromFile ? null : getClass().getClassLoader());
  }

  protected void initialize(GL2 gl) {
    if (initialized) return;
    for (ObjMaterial mat : lib.getMaterials())
      mat.init(gl);

    Texture2D blankTex = manager.blankTexture.get();
    for (ObjMaterial m : lib.getMaterials()) {
      if (m.getTexture() == null && m.getTextureSource() == null) {
        m.setTexture(blankTex, false);
      } else {
        m.setTexParams(manager.defaultTexParams);
      }
    }
    initialized = true;
  }

  public void dispose(GL gl) {
    if (disposed) return;
    for (ObjMaterial mat : lib.getMaterials())
      mat.dispose(gl);
  }

  public ObjMaterialLibrary get() {
    return lib;
  }
}
