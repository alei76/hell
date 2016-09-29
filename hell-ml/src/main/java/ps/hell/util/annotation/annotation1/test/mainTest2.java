package ps.hell.util.annotation.annotation1.test;


import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/9/23.
 */
public class mainTest2 {


    public static void main(String[] args) {
        Class<Test> testClass = Test.class;
        try {
            //因为是注解到method上的，所以首先要获取这个方法
            Method method = testClass.getDeclaredMethod("getUserName");

            //判断这个方法上是否有这个注解
            if (method.isAnnotationPresent(MethodType.class)) {
                System.out.println("this is a method Annotation");

                //如果有这个注解，则获取注解类
                MethodType methodType = (MethodType) method.getAnnotation(MethodType.class);
                if (methodType != null) {
                    if (MethodType.MethodTypeEnum.TYPE1.equals(methodType.methodType())) {
                        System.out.println("this is TYPE1");
                    } else {
                        System.out.println("this is TYPE2");
                    }
                }
            } else {
                System.out.println("this is not  a method Annotation");
            }

        } catch (Exception e) {
        }

    }
}
