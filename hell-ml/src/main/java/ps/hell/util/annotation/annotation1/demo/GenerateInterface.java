package ps.hell.util.annotation.annotation1.demo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
@Target(ElementType.TYPE)//注解使用目标为类
@Retention(RetentionPolicy.SOURCE)//注解保留范围为源代码
public @interface GenerateInterface {
  String suffix() default "Interface";//生成对应接口的后缀名
}