package ps.hell.ml.graph.linkedBase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import ps.hell.util.db.MysqlConnection;

/**
 * 图的连接方法 user - user 关系
 * 
 * @author Administrator
 *
 */
public class GraphLinkedMethod {

	/**
	 * 连接矩阵
	 */
	HashMap<Integer, HashMap<Integer, Float>> matrix = new HashMap<Integer, HashMap<Integer, Float>>();

	/**
	 * 
	 * @param length
	 */
	public GraphLinkedMethod() {
	}

	/**
	 * 创建边
	 * 
	 * @param a
	 * @param b
	 * @param count
	 */
	public void createEdge(GraphLinkedBean user, GraphLinkedBean item,
			float count) {
		HashMap<Integer, Float> list = matrix.get(user.id);
		if (list == null) {
			list = new HashMap<Integer, Float>();
			this.matrix.put(user.id, list);
			list.put(item.id, count);
		} else {
			list.put(item.id, count);
		}
	}

	// /**
	// * 添加边 累加
	// *
	// * @param a
	// * @param b
	// * @param count
	// */
	// public void addEdge(String userName, String itemName, float count) {
	// HashMap<Integer, Float> list = matrix.get(user.id);
	// if (list == null) {
	// list = new HashMap<Integer, Float>();
	// this.matrix.put(user.id, list);
	// list.put(item.id, count);
	// } else {
	// Float fval = list.get(item.id);
	// if (fval == null) {
	// list.put(item.id, count);
	// } else {
	// list.put(item.id, count + fval);
	// }
	// }
	// }

	/**
	 * 添加边 累加
	 * 
	 * @param a
	 * @param b
	 * @param count
	 */
	public void addEdge(GraphLinkedBean user, GraphLinkedBean item, float count) {
		HashMap<Integer, Float> list = matrix.get(user.id);
		if (list == null) {
			list = new HashMap<Integer, Float>();
			this.matrix.put(user.id, list);
			list.put(item.id, count);
		} else {
			Float fval = list.get(item.id);
			if (fval == null) {
				list.put(item.id, count);
			} else {
				list.put(item.id, count + fval);
			}
		}
	}

	/**
	 * 获取用户下>value的数用户
	 * 
	 * @param userId
	 * @return
	 */
	public ArrayList<GraphLinkedBean> getUserToItem(String userName, float val) {
		ArrayList<GraphLinkedBean> result = new ArrayList<GraphLinkedBean>();
		Integer userId = GraphLinkedBean.userMap.get(userName);
		if (userId == null) {
			return result;
		}
		HashMap<Integer, Float> list = matrix.get(userId);
		if (list == null) {
			return result;
		} else {
			for (Entry<Integer, Float> m : list.entrySet()) {
				if (m.getValue() > val) {
					GraphLinkedBean item = new GraphLinkedBean(
							GraphLinkedBean.itemMapRel.get(m.getKey()), false);
					item.value = m.getValue();
					result.add(item);
				}
			}
		}
		return result;
	}

	/**
	 * 获取物品对应的用户>value值
	 * 
	 * @param itemId
	 * @param value
	 * @return
	 */
	public ArrayList<GraphLinkedBean> getItemToUser(String itemName, float value) {

		ArrayList<GraphLinkedBean> result = new ArrayList<GraphLinkedBean>();
		Integer itemId = GraphLinkedBean.itemMap.get(itemName);
		if (itemId == null) {
			return result;
		}
		for (Entry<Integer, HashMap<Integer, Float>> userS : this.matrix
				.entrySet()) {
			Float val = userS.getValue().get(itemId);
			if (val == null) {
				continue;
			} else {
				if (val > value) {
					GraphLinkedBean user = new GraphLinkedBean(
							GraphLinkedBean.userMapRel.get(userS.getKey()),
							true);
					user.value = val;
					result.add(user);
				}
			}
		}
		return result;
	}

	/**
	 * 转换为string
	 */
	@Override
	public String toString() {
		StringBuffer r =new StringBuffer("the graph model size: userSize:" + matrix.size()+"\n");
		for(Entry<Integer,HashMap<Integer, Float>> entry:this.matrix.entrySet())
		{
			//r.append("user:"+GraphLinkedBean.)
		}
		return r.toString();
	}

	public static void main(String[] args) {
		// 连接方法的测试使用
		GraphLinkedMethod method= new GraphLinkedMethod();
		String temp = "select a.mall_id,a.dp_id,b.userId,b.tags from fangcheng_global.fc_brand_shop as a,"
				+ "BussinessInfoComment as b "
				+ "where a.dp_id=b.shopId and b.shopCount>1 "
				+ "and a.mall_id is not null and a.dp_id is not null and a.mall_id<126";
		MysqlConnection mysql = new MysqlConnection("192.168.1.4", 3306,
				"fcMysql", "root", "zjroot");
		ResultSet result = mysql.sqlSelect(temp).resultSet;
		try {
			while (result.next()) {
				int mallId=result.getInt(1);
				int userId=result.getInt(3);
				GraphLinkedBean bean=new GraphLinkedBean(mallId,true);
				GraphLinkedBean bean2=new GraphLinkedBean(userId,false);
				method.addEdge(bean, bean2, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//	    ArrayList<GraphLinkedBean> itemBean=method.getUserToItem("1",0.01f);
//	    Collections.sort(itemBean);
//	    for(GraphLinkedBean bean:itemBean)
//	    System.out.println(bean.toString());
	    
	    ArrayList<GraphLinkedBean> itemBean=method.getItemToUser("167456112",0.01f);
	    Collections.sort(itemBean);
	    for(GraphLinkedBean bean:itemBean)
	    System.out.println(bean.toString());
	}
}
