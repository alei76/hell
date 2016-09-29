package ps.hell.util.annotation.annotation1.test;

import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
/** 
 * 定义一个作用到方法的注解 
 * @author zhuli.zhul 
 * @date 2014-7-5 
 */  
@Documented//文档  
@Retention(RetentionPolicy.RUNTIME)//在运行时可以获取  
@Target({ ElementType.TYPE, ElementType.METHOD })//作用到类，方法，接口上等  
public @interface MethodType {  
  
    //枚举类型  
    public enum MethodTypeEnum {  
        TYPE1, TYPE2  
    }  
  
    //实际的值  
    public MethodTypeEnum methodType() default MethodTypeEnum.TYPE1;  
}  