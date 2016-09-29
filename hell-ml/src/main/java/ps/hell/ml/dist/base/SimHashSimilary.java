package ps.hell.ml.dist.base;

import java.util.Arrays;

import ps.hell.ml.dist.base.inter.SimilaryFactory;

/**
 * 最小hash 相似度 http://blog.csdn.net/pi9nc/article/details/12250361
 * 局部敏感哈希(Locality Sensitive Hashing)
 * 
 * @author Administrator Simhash主要做用是使复杂度o(nml)中，使m<<n，即大幅减小搜索空间的作用。例如计算item
 *         a的近临(top M)时，只搜索一个特定的近临空间m，而非整个庞大的n空间。
 *         Simhash是通过设计一个hash方法，使要内容相近item生的hash签名也相近
 *         ，hash签名的相近程度，也能反映出item间的相似程度。
 *
 */
public class SimHashSimilary implements SimilaryFactory {

	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		// userNode1表示用户1对应的物品
		// weight无效
		// 按位置计算对应的汉明码
		int user1=getDist(userNode1);
		int user2=getDist(userNode2);
		return user1-user2;
	}
	/**
	 * 获取具体的simhash值
	 * 
	 * @param userNode1
	 * @return
	 */
	public int getDist(int[] userNode1){
		int[] re=new int[32];
		int max=0;
		for (int i = 0; i < userNode1.length; i++) {
			StringBuffer temp2 = new StringBuffer(Integer.toBinaryString(userNode1[i]));
			char[] va2 = temp2.reverse().toString().toCharArray();
//			System.out.println(Arrays.toString(va2.toCharArray()));
			if(va2.length>max){
				max=va2.length;
			}
			for(int j=0;j<va2.length;j++)
			{
				if(va2[j]=='0'){
					re[j]-=1;
				}else{
					re[j]+=1;
				}
			}
		}
		if(max==0)
		{
			return 0;
		}else{
			//将re转化为具体的hash code
			int v=0;
			for(int i=0;i<max;i++){
				if(re[i]>0){
					int j=i;
					int v2=1;
					while(--j>=0){
						v2*=2;
					}
					v+=v2;
				}
			}
			return v;
		}
		
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub

		return 0;
	}

	public static void main(String[] args) {
		// Integer zn=new Integer(4);
		// char[] digits = {
		// '0' , '1' , '2' , '3' , '4' , '5' ,
		// '6' , '7' , '8' , '9' , 'a' , 'b' ,
		// 'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
		// 'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
		// 'o' , 'p' , 'q' , 'r' , 's' , 't' ,
		// 'u' , 'v' , 'w' , 'x' , 'y' , 'z'
		// };
		// int shift =1;
		// int i=4;
		// char[] buf = new char[32];
		// int charPos = 32;
		// int radix = 1 << shift;
		// int mask = radix - 1;
		// do {
		// buf[--charPos] = digits[i & mask];
		// i >>>= shift;
		// } while (i != 0);
		// System.out.println(buf+"\t"+buf.length);
		// System.out.println(new String(buf, charPos, (32 - charPos)));
		// System.out.println(zn.toBinaryString(5));

		// System.out.println(Arrays.toString(SimHashSimilary.intToIntegerList(4)));
		// System.out.println(Arrays.toString(array));
		int val = 4;
		StringBuffer temp = new StringBuffer(Integer.toBinaryString(val));
		String va = temp.reverse().toString();
		System.out.println(Arrays.toString(va.toCharArray()));
		int val2 = 5;
		StringBuffer temp2 = new StringBuffer(Integer.toBinaryString(val2));
		String va2 = temp2.reverse().toString();
		System.out.println(Arrays.toString(va2.toCharArray()));
		for (int i = 0; i < va.length(); i++) {
			System.out.println((int) va.toCharArray()[i]);
			// System.out.println(va2.toCharArray()[i]);
			System.out.println((char) (va.toCharArray()[i] + va2
					.toCharArray()[i]) - 96);
		}
	}
}
