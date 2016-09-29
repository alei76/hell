package ps.hell.ml.nlp.own.landerbuluse.participle.crf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 解析crf++模板
 * 
 * @author ansj
 * 
 */
public class Template implements Serializable {

	private static final long serialVersionUID = 8265350854930361325L;

	public int left = 2;

	public int right = 2;

	public int[][] ft = { { -2 }, { -1 }, { 0 }, { 1 }, { 2 }, { -2, -1 }, { -1, 0 }, { 0, 1 }, { 1, 2 }, { -1, 1 } };

	public int tagNum;

	public Map<String, Integer> statusMap;

	/**
	 * 解析配置文件
	 * 
	 * @param templateStr
	 * @return
	 * @throws IOException
	 */
	public static Template parse(String templateStr) throws IOException {
		// TODO Auto-generated method stub
		return parse(new BufferedReader(new StringReader(templateStr)));
	}

	public static Template parse(BufferedReader br) throws IOException {
		Template t = new Template();

		String temp = null;

		List<String> lists = new ArrayList<String>();
		while ((temp = br.readLine()) != null) {
			if (StringUtil.isBlank(temp) || temp.startsWith("#")) {
				continue;
			}
			lists.add(temp);
		}
		br.close();

		t.ft = new int[lists.size() - 1][0];
		for (int i = 0; i < lists.size() - 1; i++) {
			temp = lists.get(i);
			String[] split = temp.split(":");

			int index = Integer.parseInt(split[0].substring(1));

			split = split[1].split(" ");
			int[] ints = new int[split.length];

			for (int j = 0; j < ints.length; j++) {
				ints[j] = Integer.parseInt(split[j].substring(split[j].indexOf("[") + 1, split[j].indexOf(",")));
			}
			t.ft[index] = ints;
		}
		t.left = 0;
		t.right = 0;
		// find max and min
		for (int[] ints : t.ft) {
			for (int j : ints) {
				t.left = t.left > j ? j : t.left;
				t.right = t.right < j ? j : t.right;
			}
		}
		t.left = t.left;

		return t;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("left:" + left);
		sb.append("\t");
		sb.append("rightr:" + right);
		sb.append("\n");
		for (int[] ints : ft) {
			sb.append(Arrays.toString(ints));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String toString2() {
		StringBuilder sb = new StringBuilder();
		sb.append("<template>\n");
		sb.append("left:" + left);
		sb.append("\n");
		sb.append("right:" + right);
		sb.append("\nf:t\n");
		for (int[] ints : ft) {
			int zn=0;
			for(int in:ints)
			{
				zn++;
				if(zn!=1)
				{
					sb.append("\t");
				}
				sb.append(in);	
			}
			sb.append("\n");
		}
		sb.append("\n");
		sb.append("tagNum:"+tagNum);
		sb.append("\n");
		sb.append("statusMap:");
		sb.append("\n");
		for(Entry<String,Integer> map:statusMap.entrySet())
		{
			sb.append(map.getKey()+"\t"+map.getValue());
		}
		sb.append("\n");
		sb.append("</template>\n");
		return sb.toString();
	}
	
	public void readString(String str)
	{
		String[] list=str.replaceAll("<template>\n","").replaceAll("</template>","").split("\n");
		boolean isFt=false;
		boolean isStat=false;
		LinkedList<int[]> ftList=new LinkedList<int[]>();
		for(String st:list)
		{
			if(isFt)
			{
				String[] zn=st.split("\t");
				int[] znI=new int[zn.length];
				int i=-1;
				for(String zp:zn)
				{
					i++;
					znI[i]=Integer.parseInt(zp);
				}
				ftList.add(znI);
			}else if(isStat)
			{
				this.ft=new int[ftList.size()][];
				int i=-1;
				for(int[] zp:ftList)
				{
					i++;
					ft[i]=zp;
				}
				//获取内
				if(statusMap==null)
				{
					statusMap=new HashMap<String,Integer>();
				}
				String[] z=str.split("\t");
				statusMap.put(z[0],Integer.parseInt(z[1]));
			}
			if(st.startsWith("left:"))
			{
				this.left=Integer.parseInt(st.substring(5));
			}else if(st.startsWith("right:"))
			{
				this.right=Integer.parseInt(st.substring(6));
			}else if(st.startsWith("ft:"))
			{
				isFt=true;
			}else if(st.startsWith("tagNum:"))
			{
				this.tagNum=Integer.parseInt(st.substring(7));
				isFt=false;
			}else if(st.startsWith("statusMap:"))
			{
				isStat=true;
			}
		}
	}
}