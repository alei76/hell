package ps.hell.ml.graph.linkedBase;
import java.util.ArrayList;
import java.util.Collections;


public class Graph {

  public static void main(String[] args)
  {
    // TODO Auto-generated method stub
	GraphLinkedMethod M = new GraphLinkedMethod();
    GraphLinkedBean a = new GraphLinkedBean("a",true);
    GraphLinkedBean b = new GraphLinkedBean("b",true);
    GraphLinkedBean c = new GraphLinkedBean("c",true);
    GraphLinkedBean d = new GraphLinkedBean("d",true);
    GraphLinkedBean e = new GraphLinkedBean("e",true);
    GraphLinkedBean f = new GraphLinkedBean("f",true);
    GraphLinkedBean g = new GraphLinkedBean("g",false);
    GraphLinkedBean h = new GraphLinkedBean("h",false);
    GraphLinkedBean i = new GraphLinkedBean("i",false);
    GraphLinkedBean j = new GraphLinkedBean("j",false);
    GraphLinkedBean k = new GraphLinkedBean("k",false);
    GraphLinkedBean l = new GraphLinkedBean("l",false);
    
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
    
    M.createEdge(a,g,3);
    M.createEdge(a,k,6);
    
    M.createEdge(b,i,6);
    M.createEdge(c,k,7);
    
    M.createEdge(e,j,3);
    M.createEdge(d,l,5);
    
    M.createEdge(a,l,9);
    
    M.createEdge(d,l,8);
    M.addEdge(a, l, 4);
    M.addEdge(a,l,-1);
    
    System.out.println("the graph is:\n");
    System.out.println(M);
    
  
//    System.out.println();
//    System.out.println("findPathByDFS:a to k");
//    M.findPathByDFS(a,k);
//    
//    System.out.println();
//    System.out.println("findPathByBFS:a to k");
//    M.findPathByBFS(a,k);
//    
//    System.out.println();
//    System.out.println("bellmanFord from a:");
//    M.bellmanFord(a);
//    
//    System.out.println();
//    System.out.println("dijkstra from a:");
//    M.dijkstra(a);
//    
//    System.out.println();
//    System.out.println("bellmanFord,print example from a:");
//    M.floydWarshall();
//    M.printFloydWarshallForOneCity(a);
    
    ArrayList<GraphLinkedBean> itemBean=M.getUserToItem("a",0.9f);
    Collections.sort(itemBean);
    for(GraphLinkedBean bean:itemBean)
    System.out.println(bean.toString());
  }

}