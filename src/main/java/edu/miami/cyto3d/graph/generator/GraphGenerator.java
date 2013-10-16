package edu.miami.cyto3d.graph.generator;

import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;

/**
 * Interface for an object that creates a graph of a special type. The generated graph will be added
 * to an existing graph model and view.
 * 
 * @author justin
 */
public interface GraphGenerator {

   public void generate(Graph graph, GraphView view);
}