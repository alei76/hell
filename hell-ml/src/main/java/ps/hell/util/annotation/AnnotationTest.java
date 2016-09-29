package ps.hell.util.annotation;

import java.lang.reflect.Method;

//1.CONSTRUCTOR:用于描述构造器
//2.FIELD:用于描述域
//3.LOCAL_VARIABLE:用于描述局部变量
//4.METHOD:用于描述方法
//5.PACKAGE:用于描述包
//6.PARAMETER:用于描述参数
//7.TYPE:用于描述类、接口(包括注解类型) 或enum声明
public class AnnotationTest {
	@Todo(priority = Todo.Priority.MEDIUM, author = "Yashwant", status = Todo.Status.STARTED)
	public void incompleteMethod1() {
	//Some business logic is written
	//But it’s not complete yet
		System.out.println();
	}
	public static void main(String[] args) {
		Class businessLogicClass = AnnotationTest.class;
		for(Method method : businessLogicClass.getMethods()) {
		Todo todoAnnotation = (Todo)method.getAnnotation(Todo.class);
		if(todoAnnotation != null) {
		System.out.println(" Method Name : " + method.getName());
		System.out.println(" Author : " + todoAnnotation.author());
		System.out.println(" Priority : " + todoAnnotation.priority());
		System.out.println(" Status : " + todoAnnotation.status());
		}
		}
	}
}
