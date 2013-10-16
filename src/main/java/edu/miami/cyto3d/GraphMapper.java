package edu.miami.cyto3d;

import java.util.HashMap;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

import edu.miami.cyto3d.graph.model.BasicGraph;
import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.BasicGraphView;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.cyto3d.graph.view.VertexView;
import edu.miami.math.util.GMath;
import edu.miami.math.vector.Vec3;

/**
 * Converts a 2D cytoscape network to a 3D graph.
 * 
 * @author justin
 */
public class GraphMapper {

    CyNetwork    network;
    String       edgeLengthName;
    C3DApp plugin;

    /**
     * @param network
     * @param edgeLengthName
     *            - name of the attribute that is used for the edge length
     * @param plugin
     */
    public GraphMapper(CyNetwork network, String edgeLengthName, C3DApp plugin) {
        this.network = network;
        this.edgeLengthName = edgeLengthName;
        this.plugin = plugin;
    }

    /**
     * @param minEL
     *            - minimum edge length
     * @param maxEL
     *            - maximum edge length
     */
    public void map(float minEL, float maxEL) {

        Graph graph = new BasicGraph();
        GraphView graphView = new BasicGraphView(graph);
        graph.addListener(graphView);

        HashMap<CyNode, Vertex> nodeMap = new HashMap<CyNode, Vertex>();

        for (CyNode node : network.getNodeList()) {

            String name = network.getRow(node).get(CyNetwork.NAME, String.class);

            Vec3 p = GMath.rndVec3f(-30, 30);
            Vertex vertex = graph.createVertex();
            vertex.getAttributes().set("name", name);
            VertexView vertexView = graphView.get(vertex);

            vertexView.setPosition(p);
            nodeMap.put(node, vertex);
        }

        for (CyEdge edge : network.getEdgeList()) {
            Vertex src = nodeMap.get(edge.getSource());
            Vertex tgt = nodeMap.get(edge.getTarget());

            Edge edge3D = graph.createEdge(src, tgt, false);

            if (edgeLengthName == null) {
                edge3D.getAttributes().set("length", maxEL);
            } else {
                try {
                    double d = network.getRow(edge).get(edgeLengthName, Double.class);
                    float el = interactivityToLength((float)d, minEL, maxEL);
                    edge3D.getAttributes().set("length", el);
                } catch (Exception e) {
                    edge3D.getAttributes().set("length", maxEL);
                }
            }

        }

        graph.getAttributes().set("min_edge_length", minEL);
        graph.getAttributes().set("max_edge_length", maxEL);
        graph.getAttributes().set("name", network.getRow(network).get(CyNetwork.NAME, String.class));

        plugin.setNewGraph(graph, graphView, true);
    }

    private float interactivityToLength(float interactivity, float minEL, float maxEL) {
        float range = maxEL - minEL;
        return (1 - interactivity) * range + minEL;
    }
}
