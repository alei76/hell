package ps.hell.util.annotation.method;
import java.lang.annotation.Annotation;  
import java.lang.reflect.Method;  
  
public class Main {  
      
    public void test1(@Test(id = 0, age = 0) String a){  
    	System.out.println(a) ;
    }  
      
      
    public void test2(@Test(id = 0, age = 0,name="yyl") String b){  
       System.out.println(b) ;  
    }  
      
      
    public void test3(@Test(id = 10, age = 10,name="yyl") int c){  
    	System.out.println(c) ;
    }  
      
    public static void main(String[] args) {  
    	long start=System.currentTimeMillis();
        Method[] m = Main.class.getDeclaredMethods();  
        Annotation[][] an = null;  
        for(Method method:m){  
                 an =  method.getParameterAnnotations();  
                 System.out.println(method.getParameterTypes()  );  
                if(an.length>0){  
                    for(int i=0;i<an.length;i++){  
                        for(int j=0;j<an[i].length;j++){  
                            Test t = (Test) an[i][j];  
                            System.out.println("i:"+i+":j:"+j+"\t"+method.getName()+","+t.age()+","+t.id()+","+t.name()+","+t.test2());  
                        }  
                    }  
                }  
                  
        }  
        System.out.println((System.currentTimeMillis()-start)+"ms");
        
        Main main=new Main();
        main.test1("222");
        main.test2("333");
        main.test3(444);
    }  
} 