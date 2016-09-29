package ps.hell.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 通过反射复制对象
 * @author Administrator
 *
 */
public class ObjectCopyAsReflect {
	/**
	 * 从read中读取内容到 write中
	 * @param obj write
	 * @param object read
	 * @throws Exception
	 */
	public void copy(Object write,Object read) throws Exception{  
        Class classType = read.getClass();//获得对象的类型  
        System.out.println("Class:" + classType.getName());  
          
        //通过默认的构造函数创建一个新的对象            
        //获得对象的所有属性  
        Field fields[] = classType.getDeclaredFields();  
          
        for(int i = 0; i < fields.length; i++) {  
            Field field = fields[i];  
              
            String fieldName = field.getName();  
            String firstLetter = fieldName.substring(0,1).toUpperCase();  
            //获得和属性对应的getXXX（）方法的名字  
            String getMethodName = "get" + firstLetter + fieldName.substring(1);  
            //获得和属性对应的setXXX()方法的名字  
            String setMethodName = "set" + firstLetter + fieldName.substring(1);  
              
            //获得和属性对应的getXXX()方法  
            Method getMethod = classType.getMethod(getMethodName, new Class[]{});  
            //获得和属性对应的setXXX()方法  
            Method setMethod = classType.getMethod(setMethodName, new Class[]{field.getType()});  
              
            //调用原对象的getXXX()方法  
            Object value = getMethod.invoke(read, new Object[]{});  
            System.out.println(fieldName + ":" + value);  
            //调用复制对象的setXXX()方法  
            setMethod.invoke(write, new Object[]{value});  
        }
    }  
}
