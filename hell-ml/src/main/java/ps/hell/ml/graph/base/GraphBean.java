package ps.hell.ml.graph.base;
public class GraphBean 
{
  public String name; 
  public int id;
  static int idCounter = 0;
  public GraphBean(String name) 
  {
    this.name=name;
    id = idCounter++;
  }
}