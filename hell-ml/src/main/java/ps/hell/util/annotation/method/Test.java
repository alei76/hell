package ps.hell.util.annotation.method;
  
import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Inherited;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
  
@Target(ElementType.PARAMETER)  
@Retention(RetentionPolicy.RUNTIME)     
@Documented    
@Inherited    
  
public @interface Test {  
    public int id();  
    public String name() default "lidong";  
    public int age();  
    public Test2 test2() default Test2.FILE;  
}  