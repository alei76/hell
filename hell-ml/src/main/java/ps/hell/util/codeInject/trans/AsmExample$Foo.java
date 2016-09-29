package ps.hell.util.codeInject.trans;
  
public class AsmExample$Foo  
{  
  public static void execute1()  
  {  
    System.out.println("test changed method name");  
  }  
  public static void changeMethodContent() {  
    System.out.println("test change method");  
    System.out.println("this is a modify method!");  
  }  
  
  public static void add(String[] paramArrayOfString)  
  {  
    System.out.println("this is add method print!");  
  }  
} 