package ps.hell.ml.nlp.own.landerbuluse.participle.newWorDiscovery;

import ps.hell.ml.nlp.own.landerbuluse.participle.newWorDiscovery.NewWordDiscovery.OutBean;

public class SortBean implements Comparable<SortBean>{
	public String name=null;
	public OutBean val=null;
	public SortBean(String name,OutBean val){
		this.name=name;
		this.val=val;
	}
	
	public int compareTo(SortBean o){
		return -Double.compare(val.weight,o.val.weight);
	}
	@Override
	public String toString(){
		return "["+name+"\t,\t"+val+"]";
	}
}
