package ps.hell.ml.nlp.own.landerbuluse.participle.mmseg;
import java.util.LinkedList;

public class struct {
	private LinkedList<String> name=new LinkedList<String>();
	private LinkedList<String> value=new LinkedList<String>();
	private int size=0;
	public struct()
	{
	}
	public void insert(String name,String value)
	{
		if(!this.name.isEmpty())
		{
			//System.out.println("???????");
			this.name.add(name);
			this.value.add(value);
			this.size++;
		}
		else
		{
			//System.out.println("??");
			this.name.add(name);
			this.value.add(value);
			this.size++;
		}
	}
	public LinkedList<String> name()
	{
		return this.name; 
	}
	public LinkedList<String> value()
	{
		return this.value;
	}
	public int size()
	{
		return this.size;
	}
}
