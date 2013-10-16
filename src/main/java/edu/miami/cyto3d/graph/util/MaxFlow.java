package edu.miami.cyto3d.graph.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MaxFlow {
  public static void main(String[] args) {
    MaxFlow mf = new MaxFlow(5);
    mf.addEdge(0, 2, 2);
    mf.addEdge(0, 3, 5);
    mf.addEdge(2, 3, 3);
    mf.addEdge(3, 1, 4);
    mf.addEdge(2, 1, 3);
    mf.addEdge(0, 4, 10);

    double maxflow = mf.getMaxFlow(0, 4);
    System.out.println("max flow " + maxflow);
  }

  public MaxFlow(int numVerts) {
    for (int i = 0; i < numVerts; i++)
      new Vert();
  }

  /**
   * Add an edge with given capacity in either direction.
   */
  public void addEdge(int iVert, int jVert, double capacity) {
    Vert a = verts.get(iVert);
    Vert b = verts.get(jVert);
    Edge e = new Edge(a, b, capacity);
  }

  /**
   * Calculate the maximum flow possible between two vertices.
   */
  public double getMaxFlow(int iVert, int jVert) {
    for (Edge edge : edges)
      edge.flow = 0;

    Vert s = verts.get(iVert);
    Vert t = verts.get(jVert);

    double add = 0, flow = 0;
    while ((add = addFlow(s, t)) > 0)
      flow += add;

    return flow;
  }

  Queue<Vert> queue = new ArrayDeque<Vert>();

  double addFlow(Vert s, Vert t) {
    for (Vert v : verts)
      v.back = null;
    queue.offer(s);
    while (!queue.isEmpty()) {
      Vert v = queue.poll();
      for (Edge e : v.edges)
        if (e.twin.vert != s && e.twin.vert.back == null && e.getCapacity() > 0) {
          e.twin.vert.back = e;
          queue.offer(e.twin.vert);
        }
    }
    if (t.back == null) return 0;

    double minCap = t.back.getCapacity();
    for (Vert v = t; v.back != null; v = v.back.vert)
      if (v.back.getCapacity() < minCap) minCap = v.back.getCapacity();
    for (Vert v = t; v.back != null; v = v.back.vert)
      v.back.addFlow(minCap);

    System.out.println("addFlow " + minCap);
    return minCap;
  }

  class Vert {
    List<Edge> edges = new ArrayList<Edge>();
    Edge       back;

    Vert() {
      verts.add(this);
    }
  }

  List<Vert> verts = new ArrayList<Vert>();

  class Edge {
    Vert   vert;
    Edge   twin;
    double capacity;
    double flow;

    Edge(Vert a, Vert b, double capacity) {
      this.capacity = capacity;
      vert = a;
      a.edges.add(this);
      twin = new Edge(capacity, this, b);
      edges.add(this);
    }

    Edge(double capacity, Edge edge, Vert b) {
      this.capacity = capacity;
      vert = b;
      b.edges.add(this);
      twin = edge;
      edges.add(this);
    }

    double getCapacity() {
      return capacity - flow + twin.flow;
    }

    void addFlow(double add) {
      if (add <= twin.flow) {
        twin.flow -= add;
        return;
      }
      add -= twin.flow;
      twin.flow = 0;
      flow += add;
    }
  }

  List<Edge> edges = new ArrayList<Edge>();
}
