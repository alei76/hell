package ps.hell.ml.dist.base;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 匹配率
 * 
 * @author Administrator
 *
 */
public class MatchGoods{

	/**
	 * 计算 goods/all 切比雪夫
	 * 
	 * @param userNode1
	 * @param userNode2
	 * @return
	 */
	public double getSimilaryMax(HashMap<Integer, Integer> userNode1,
			HashMap<Integer, Integer> userNode2) {
		double up = 0f;
		double down1 = 0f;
		// 计算其他非结构化属性
		for (Entry<Integer, Integer> m : userNode1.entrySet()) {
			Integer index2 = userNode2.get(m.getKey());
			// System.out.println("id:"+m.getKey()+"\tuser1:"+m.getValue()+"\tuser2:"+index2);
			if (index2 == null) {
				down1 += m.getValue();
				continue;
			}
			up += Math.abs(m.getValue() - index2);
			down1 += Math.max(m.getValue(), index2);
			// System.out.println("up:"+up);
		}
		if (Double.compare(up, 0d) == 0) {
			return 0f;
		}
		for (Entry<Integer, Integer> m : userNode2.entrySet()) {
			Integer index2 = userNode1.get(m.getKey());
			if (index2 == null) {
				down1 += m.getValue();
				continue;
			}
		}
		// System.out.println("up:"+up+"\tdown1:"+down1+"\tdown2:"+down2);
		double sim1 = up / down1;
		return 1 - sim1;
	}

	/**
	 * 计算 user feather to item feather mathches 为物品的属性是否存在于用户中
	 * 
	 * @param userNode1
	 * @param userNode2
	 * @return
	 */
	public double getSimilaryMatch(HashMap<Integer, Integer> userNode1,
			HashMap<Integer, Integer> itemNode1,double lossValue) {
		int matchCount = 0;
		// 计算其他非结构化属性
		if (userNode1 == null || userNode1.size() == 0 || itemNode1 == null
				|| itemNode1.size() == 0) {
			return 0f;
		}
		for (Entry<Integer, Integer> m : itemNode1.entrySet()) {
			Integer index2 = userNode1.get(m.getKey());
			if (index2 == null) {
				continue;
			}
			matchCount++;
		}
		if (matchCount == 0) {
			return -1f;
		}
		if(matchCount!=itemNode1.size())
		{
			//做负增益
			matchCount-=(itemNode1.size()-matchCount)*lossValue;
		}
		return matchCount*1f/ itemNode1.size();
	}

}
