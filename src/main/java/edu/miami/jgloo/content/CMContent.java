package edu.miami.jgloo.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.media.opengl.GL2;

import edu.miami.jgloo.GLDisposable;

public abstract class CMContent implements GLDisposable {

  String               name;
  boolean              initialized = false;
  boolean              disposed    = false;
  boolean              released    = false;
  final ContentManager manager;

  public CMContent(String name, ContentManager manager) {
    this.name = name;
    this.manager = manager;
  }

  public String getName() {
    return name;
  }

  public boolean isInitialized() {
    return initialized;
  }

  public boolean isDisposed() {
    return disposed;
  }

  public void release() {
    manager.addTrash(this);
    released = true;
  }

  public boolean isReleased() {
    return released;
  }

  protected abstract void initialize(GL2 gl);

  /** Retrieves this content as an input stream using either a class loader or file system path. */
  protected InputStream getContentInputStream(String directory) {
    InputStream in = null;
    if (manager.loadFromFile) {
      File file = new File(directory, name);
      if (file.exists()) {
        try {
          in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
        	System.out.println("cannot find file: " + name);
        }
      }
    } else {
      String resource = directory + name;
      System.out.println("load resource : " + resource);
      in = getClass().getClassLoader().getResourceAsStream(resource);
    }
    return in;
  }
}
