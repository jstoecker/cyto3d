package edu.miami.jgloo.content;

/**
 * Loads a managed model in a thread. Once the mesh data has been read, the thread adds the model to
 * the list of items to be initialized.
 * 
 * @author justin
 */
public class ModelLoader extends Thread {

  CMModel        model;
  ContentManager content;

  public ModelLoader(CMModel model, ContentManager content) {
    this.model = model;
    this.content = content;
  }

  @Override
  public void run() {
    model.readMeshData();
    synchronized (content) {
      content.uninitializedContent.add(model);
    }
  }
}
