package ps.hell.util.annotation.annotation1.test;

public class mainTest {

    public static void main(String[] args) {
        Class<Test> testClass = Test.class;
        //因为注解是作用于类上面的，所以可以通过isAnnotationPresent来判断是否是一个  
        //有UserNameAnnotations注解的类  
        if (testClass.isAnnotationPresent(UserNameAnnotations.class)) {
            System.out.println("this is a Annotations class");
            //通过getAnnotation可以获取注解对象  
            UserNameAnnotations userNameAnnotations = (UserNameAnnotations) testClass.
                    getAnnotation(UserNameAnnotations.class);
            if (userNameAnnotations != null) {
                System.out.println("value:" + userNameAnnotations.value());
            } else {
                System.out.println("null");
            }
        } else {
            System.out.println("this is not Annotations class");
        }

    }
}  