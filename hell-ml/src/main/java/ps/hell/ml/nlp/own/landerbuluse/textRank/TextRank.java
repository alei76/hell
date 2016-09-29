package ps.hell.ml.nlp.own.landerbuluse.textRank;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import ps.hell.ml.nlp.own.landerbuluse.participle.mmseg2.MmsegFast;
import ps.hell.ml.nlp.own.landerbuluse.struct.WordFather;
/**
 * 文本的主题词 排序
 * (1-d)+d* sum(wji/sum(wjk)*WS(Vj))
 * {
 * a:[b,c,e,d],
 * b:[a,c],
 * c:[d,a]
 * }
 * @author Administrator
 *
 */
public class TextRank {
	/**
	 * 阻尼系数
	 */
	public static float d=0.85f;
	/**
	 * 最大迭代次数
	 */
	public static int max_iter=50;
	/**
	 * 变化值
	 */
	public static float min_diff=0.03f;
	/**
	 * 使用词的前后length个词
	 */
	public static int length=5;
	
	public static MmsegFast mm=null;
	static{
		mm=new MmsegFast(null,null);
	}
	/**
	 * 输入的字符串
	 * @param str
	 * @return
	 */
	public static HashMap<String,Float> getScores(String str)
	{
		
		LinkedList<LinkedList<WordFather>>  word=mm.analyze(str);
		HashMap<String,HashSet<String>> words=new HashMap<String,HashSet<String>>();
		HashMap<String,Float> score=new HashMap<String,Float>();
		for(LinkedList<WordFather> wo:word)
		{
			for(int i=0;i<wo.size();i++)
			{				
				String st=wo.get(i).word;
				HashSet<String> set=words.get(st);
				if(set==null)
				{
					set=new HashSet<String>();
					words.put(st,set);
				}
				for(int j=-5;j<=5;j++)
				{
					if(j==0 || i+j<0 || i+j>=wo.size())
					{
						continue;
					}
					set.add(wo.get(i+j).word);
				}
			}
		}
		for (int i = 0; i < max_iter; ++i)
        {
			//每一轮的得分
            HashMap<String, Float> m = new HashMap<String, Float>();
            float max_diff = 0;
            for (Entry<String, HashSet<String>> entry : words.entrySet())
            {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                //初始化
                m.put(key, 1 - d);
                //每一个词对应的周围词
                for (String other : value)
                {//获取词对应的对应数组数量
                    int size = words.get(other).size();
                    //如果为空或者重复则continue;
                    if (key.equals(other) || size == 0) continue;
                    //累加
                    m.put(key, m.get(key) + d / size * (score.get(other) == null ? 0 : score.get(other)));
                }
                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }
            score = m;
            System.out.println(max_diff);
            if (max_diff <= min_diff)
            	break;
        }
		return score;
	}

	public static void main(String[] args) {
		String str="程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。";
		HashMap<String,Float> score=TextRank.getScores(str);
		for(Entry<String,Float> ma:score.entrySet())
		{
			System.out.println(ma.getKey()+"\t"+ma.getValue());
		}
	}
}
