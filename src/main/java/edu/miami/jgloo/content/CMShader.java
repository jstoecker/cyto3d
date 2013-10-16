package edu.miami.jgloo.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.jgloo.ShaderProgram;

public class CMShader extends CMContent {

  ShaderProgram program;
  String        v, f;
  InputStream   vIn, fIn;

  public CMShader(String name, ContentManager manager) throws FileNotFoundException {
    super(name, manager);

    if (manager.loadFromFile) {
      v = new File(manager.shaderDir, name + ".vs").getAbsolutePath();
      f = new File(manager.shaderDir, name + ".fs").getAbsolutePath();
      vIn = new FileInputStream(v);
      fIn = new FileInputStream(f);
    } else {
    	v = manager.shaderDir + name + ".vs";
    	f = manager.shaderDir + name + ".fs";
      vIn = getClass().getClassLoader().getResourceAsStream(v);
      fIn = getClass().getClassLoader().getResourceAsStream(f);
    }
  }

  public void dispose(GL gl) {
    if (disposed) return;
    program.dispose(gl);
  }

  public ShaderProgram get() {
    return program;
  }

  protected void initialize(GL2 gl) {
    if (initialized) return;
    program = ShaderProgram.create(gl, v, f, vIn, fIn);
    vIn = null;
    fIn = null;
    initialized = true;
  }
}
