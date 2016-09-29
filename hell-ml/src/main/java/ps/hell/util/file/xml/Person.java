package ps.hell.util.file.xml;

import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("person")  
public class Person {  
    private String name;  
    @XStreamAsAttribute  
    private int age;  
    @XStreamImplicit(itemFieldName="girl")  
    @XStreamOmitField  
    List<String> girlFriends;  
    @XStreamConverter(value=DateConverter.class)  
    Date birthday;  
      
    public Date getBirthday() {  
        return birthday;  
    }  

    public void setBirthday(Date birthday) {  
        this.birthday = birthday;  
    }  

    public List<String> getGirlFriends() {  
        return girlFriends;  
    }  

    public void setGirlFriends(List<String> girlFriends) {  
        this.girlFriends = girlFriends;  
    }  

    public int getAge() {  
        return age;  
    }  

    public void setAge(int age) {  
        this.age = age;  
    }  

    public void setName(String name) {  
        this.name = name;  
    }  

    public String getName() {  
        return this.name;  
    }  
}  