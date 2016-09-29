package ps.hell.util.serializable;

import java.io.Serializable;
import java.util.HashMap;

public class SonBean implements Serializable{

	
	public String name="";
	public String zn="";
	public int znp=4;
	public HashMap<String,Long> map=new HashMap<String,Long>();
	public SonBean(String name,String zn,int znp)
	{
		this.name=name;
		this.zn=zn;
		this.znp=znp;
		map.put(zn,(long)znp);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZn() {
		return zn;
	}
	public void setZn(String zn) {
		this.zn = zn;
	}
	public int getZnp() {
		return znp;
	}
	public void setZnp(int znp) {
		this.znp = znp;
	}
	
}
