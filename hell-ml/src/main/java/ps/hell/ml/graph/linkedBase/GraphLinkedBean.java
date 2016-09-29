package ps.hell.ml.graph.linkedBase;

import java.util.HashMap;

public class GraphLinkedBean implements Comparable<GraphLinkedBean>{
	public String name;
	public int id;
	public float value;
	public static HashMap<String, Integer> userMap = new HashMap<String, Integer>();
	public static HashMap<Integer, String> userMapRel = new HashMap<Integer, String>();
	public static int userCount = -1;
	public static HashMap<String, Integer> itemMap = new HashMap<String, Integer>();
	public static HashMap<Integer, String> itemMapRel = new HashMap<Integer, String>();
	public static int itemCount = -1;

	public GraphLinkedBean(String name, boolean isUser) {
		this.name = name;
		if (isUser) {
			Integer count = userMap.get(name);
			if (count == null) {
				userCount++;
				userMapRel.put(userCount, name);
				userMap.put(name, userCount);
				id = userCount;
			} else {
				id = count;
			}
		} else {
			Integer count = itemMap.get(name);
			if (count == null) {
				itemCount++;
				itemMapRel.put(itemCount, name);
				itemMap.put(name, itemCount);
				id = itemCount;
			} else {
				id = count;
			}
		}
	}
	
	public GraphLinkedBean(int nameInt, boolean isUser) {
		String name= Integer.toString(nameInt);
		this.name =name;
		if (isUser) {
			Integer count = userMap.get(name);
			if (count == null) {
				userCount++;
				userMapRel.put(userCount, name);
				userMap.put(name, userCount);
				id = userCount;
			} else {
				id = count;
			}
		} else {
			Integer count = itemMap.get(name);
			if (count == null) {
				itemCount++;
				itemMapRel.put(itemCount, name);
				itemMap.put(name, itemCount);
				id = itemCount;
			} else {
				id = count;
			}
		}
	}
	
	@Override
	public String toString()
	{
		return "["+name+":"+id+"][value:"+value+"]";
	}

	@Override
	public int compareTo(GraphLinkedBean o) {
		// TODO Auto-generated method stub
		return Float.compare(o.value,value);
	}
}