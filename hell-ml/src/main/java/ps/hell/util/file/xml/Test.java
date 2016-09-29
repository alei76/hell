package ps.hell.util.file.xml;

import java.util.Date;

import ps.hell.util.JsonUtil;

import com.thoughtworks.xstream.XStream;



public class Test {
	public static void main(String[] args) {
	    XStream xstream = new XStream(); 
	    xstream.autodetectAnnotations(true);  
	    Person person = new Person();  
	    person.setName("pli");  
	    person.setAge(18);  
	    person.setBirthday(new Date());
	    System.out.println( xstream.toXML(person));  
	    System.out.println(JsonUtil.getJsonStr(xstream.fromXML(xstream.toXML(person))));
	}
}
