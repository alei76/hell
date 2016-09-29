package ps.hell.util.annotation.annotation1.demo;

//老师 类
@GenerateInterface(suffix="IntSuffix")
public class Teacher {
 
  //教书
  private void teach(){
    System.out.println("teach...");
  }
 
  //行走
  public void walk(){
    System.out.println("walking");
  }
}