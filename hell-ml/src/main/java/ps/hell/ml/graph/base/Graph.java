package ps.hell.ml.graph.base;


public class Graph {

  public static void main(String[] args)
  {
    // TODO Auto-generated method stub
    GraphMethod M = new GraphMethod(12);
    GraphBean a = new GraphBean("a");
    GraphBean b = new GraphBean("b");
    GraphBean c = new GraphBean("c");
    GraphBean d = new GraphBean("d");
    GraphBean e = new GraphBean("e");
    GraphBean f = new GraphBean("f");
    GraphBean g = new GraphBean("g");
    GraphBean h = new GraphBean("h");
    GraphBean i = new GraphBean("i");
    GraphBean j = new GraphBean("j");
    GraphBean k = new GraphBean("k");
    GraphBean l = new GraphBean("l");
    
    M.createEdge(a,b,3);
    M.createEdge(a,c,5);
    M.createEdge(a,d,4);
    
    M.createEdge(b,f,6);
    
    M.createEdge(c,d,2);
    M.createEdge(c,g,4);
    
    M.createEdge(d,e,1);
    M.createEdge(d,h,5);
    
    M.createEdge(e,f,2);
    M.createEdge(e,i,4);
    
    M.createEdge(f,j,5);
    
    M.createEdge(g,h,3);
    M.createEdge(g,k,6);
    
    M.createEdge(h,i,6);
    M.createEdge(h,k,7);
    
    M.createEdge(i,j,3);
    M.createEdge(i,l,5);
    
    M.createEdge(j,l,9);
    
    M.createEdge(k,l,8);
    
    System.out.println("the graph is:\n");
    System.out.println(M);
    
  
    System.out.println();
    System.out.println("findPathByDFS:a to k");
    M.findPathByDFS(a,k);
    
    System.out.println();
    System.out.println("findPathByBFS:a to k");
    M.findPathByBFS(a,k);
    
    System.out.println();
    System.out.println("bellmanFord from a:");
    M.bellmanFord(a);
    
    System.out.println();
    System.out.println("dijkstra from a:");
    M.dijkstra(a);
    
    System.out.println();
    System.out.println("bellmanFord,print example from a:");
    M.floydWarshall();
    M.printFloydWarshallForOneCity(a);
  }

}