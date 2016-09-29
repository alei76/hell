package ps.hell.util.annotation.annotation1.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 定义一个用户名的自定义注解
 *
 * @author zhuli
 * @date 2014-7-5
 */
@Documented //文档  
@Retention(RetentionPolicy.RUNTIME) //在运行时可以获取  
@Target({ElementType.TYPE, ElementType.METHOD}) //作用到类，方法，接口上等
@Inherited //子类会继承  
public @interface UserNameAnnotations {

    public String value() default ""; //使用的时候 @UserNameAnnotations(value="xxx")  

} 