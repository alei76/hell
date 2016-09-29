package ps.hell.ml.nlp.own.landerbuluse.hmm.test;


import java.util.Arrays;

/**
 * 维特比算法
 * 
 * @author hankcs
 */
public class Viterbi {
	/**
	 * 求解HMM模型
	 * 
	 * @param obs
	 *            观测序列 对应的状态
	 * @param states
	 *            隐状态 对应的目标
	 * @param start_p
	 *            初始概率（隐状态）
	 * @param trans_p
	 *            转移概率（隐状态） 行列都为状态
	 * @param emit_p
	 *            发射概率 （隐状态表现为显状态的概率）混淆概率 对应的为列为目标 行为状态 每一列为每次的
	 * @return 最可能的序列
	 */
	public static int[] compute(int[] obs, int[] states, double[] start_p,
			double[][] trans_p, double[][] emit_p) {
		double[][] V = new double[obs.length][states.length];
		int[][] path = new int[states.length][obs.length];
		/**
		 * 初始化第一次的状态值 为p(初始*p转移)
		 */
		for (int y : states) {
			V[0][y] = start_p[y] * emit_p[y][obs[0]];
			path[y][0] = y;
		}
		for (int t = 1; t < obs.length; ++t) {
			int[][] newpath = new int[states.length][obs.length];

			for (int y : states) {
				double prob = -1;
				int state;
				for (int y0 : states) {
					double nprob = V[t - 1][y0] * trans_p[y0][y]
							* emit_p[y][obs[t]];
					if (nprob > prob) {
						prob = nprob;
						state = y0;
						// 记录最大概率
						V[t][y] = prob;
						// 记录路径
						System.arraycopy(path[state], 0, newpath[y], 0, t);
						newpath[y][t] = y;
					}
				}
			}
			path = newpath;
		}

		double prob = -1;
		int state = 0;
		for (int y : states) {
			if (V[obs.length - 1][y] > prob) {
				prob = V[obs.length - 1][y];
				state = y;
			}
		}
		return path[state];
	}

	/**
	 * 计算转移概率 每次只执行1次
	 *
	 *            目标
	 * @param start_p
	 *            初始状态
	 * @param trans_p
	 *            转移矩阵
	 * @param dayCount
	 *            转移次数
	 * @return 对应obs的转移概率
	 */
	public static double[][] computeP(double[] start_p, double[][] trans_p,
			int dayCount) {
		double[][] result = new double[start_p.length][];
		for (int i = 0; i < start_p.length; i++) {
			result[i] = new double[start_p.length];
		}
		double[][] start_p2 = new double[start_p.length][];
		for (int i = 0; i < start_p.length; i++) {
			start_p2[i] = new double[start_p.length];
		}
		/**
		 * 初始化第一次的状态值 为p(初始*p转移)
		 */
		if (dayCount >= 1) {
			for (int i = 0; i < start_p.length; i++) {
				// result[i][i] = start_p[i] * trans_p[i][i];
				result[i][i] = start_p[i];
			}
		}
		if (dayCount > 1) {
			for (int i = 0; i < start_p.length; i++) {
				for (int j = 0; j < start_p.length; j++) {
					result[j][i] = result[i][i];
				}
			}
		}
		for (int day = 1; day < dayCount; day++) {
			for (int i = 0; i < start_p.length; i++) {
				start_p2[i] = result[i].clone();
			}
			// 计算转移
			for (int i = 0; i < start_p.length; i++) {
				for (int j = 0; j < start_p.length; j++) {
					result[i][j] = 0;
					result[i][j] += start_p2[i][j] * trans_p[j][i];
				}
			}
		}
		return result;
	}

	/**
	 * 求解HMM模型
	 *
	 *            隐状态 对应的目标
	 * @param start_p
	 *            初始概率（隐状态）
	 * @param trans_p
	 *            转移概率（隐状态） 行列都为状态
	 * @param emit_p
	 *            发射概率 （隐状态表现为显状态的概率）混淆概率 对应的为列为目标 行为状态 emit_p每一行为每一次的
	 * @param from
	 *
	 * @param end
	 * @return 最可能的序列 from from从0开始 to end
	 */
	public static int[] computeP(double[] start_p,
			double[][] trans_p, double[][] emit_p,int from,int end) {
		int[] endValue=new int[end-from+1];
		double[][] result = new double[start_p.length][];
		for (int i = 0; i < start_p.length; i++) {
			result[i] = new double[start_p.length];
		}
		double[][] start_p2 = new double[start_p.length][];
		for (int i = 0; i < start_p.length; i++) {
			start_p2[i] = new double[start_p.length];
		}
		int index=-1;
//		System.out.println("1:");
//		println(result);
		/**
		 * 初始化第一次的状态值 为p(初始*p转移)
		 */
		if (end >= 1) {
			for (int i = 0; i < start_p.length; i++) {
				 result[i][i] = start_p[i] * emit_p[0][i];
			}
			if(from==0)
			{
				
				int indexL = 0;
				double max = -Double.MAX_VALUE;
				for (int j = 0; j < start_p.length; j++) {
					if (result[j][j] > max) {
						indexL = j;
						max = result[j][j];
					}
				}
				index++;
				endValue[index]=indexL;
			}
		}
//		System.out.println("2:");
//		println(result);
		if (end > 1) {
			for (int i = 0; i < start_p.length; i++) {
				for (int j = 0; j < start_p.length; j++) {
					result[j][i] = result[i][i];
				}
			}
		}
		for (int day = 1; day < end; day++) {
			for (int i = 0; i < start_p.length; i++) {
				start_p2[i] = result[i].clone();
			}
			// 计算转移
			for (int i = 0; i < start_p.length; i++) {
				for (int j = 0; j < start_p.length; j++) {
					result[i][j] = 0;
					result[i][j] += start_p2[i][j] * trans_p[j][i];
				}
			}
			//矩阵调整
			index=transMatrix(result,emit_p[day],endValue,index,day,from);
		}
		return endValue;
	}

	/**
	 * 通过状态转移替换
	 * @param result 替换的矩阵
	 * @param emit_p 添加混淆矩阵
	 * @param indexValue 返回的索引排序值
	 * @param index
	 * @param day
	 * @param from
	 * @param result
	 */
	public static int transMatrix(double[][] result,double[] emit_p,int[] indexValue,int index,int day,int from)
	{
		//获取最大序列值
		int[] maxIndex=getMaxIndex(result);
//		System.out.println("max:"+Arrays.toString(maxIndex));
//		//并替换整个result
//		System.out.println("1:");
//		println(result);
//		System.out.println("em:"+Arrays.toString(emit_p));
		for(int i=0;i<maxIndex.length;i++)
		{
			result[i][i]=result[i][maxIndex[i]]*emit_p[i];
		}
		for(int i=0;i<maxIndex.length;i++)
		{
			for(int j=0;j<maxIndex.length;j++)
			{
				if(i==j)
				{
					continue;
				}
				result[j][i]=result[i][i];
			}
		}
//		System.out.println("2:");
//		println(result);
		if(day>=from)
		{
			//添加对应的状态
			index++;
			indexValue[index]=getMaxIndexOne(result);
			System.out.println("maxId:"+indexValue[index]);
		}
		return index;
	}
	/**
	 * 获取最大序列
	 * 
	 * @param val
	 * @return
	 */
	public static int[] getMaxIndex(double[][] val) {
		int[] result = new int[val.length];
		for (int i = 0; i < val.length; i++) {
			int index = 0;
			double max = -Double.MAX_VALUE;
			for (int j = 0; j < val[i].length; j++) {
				if (val[i][j] > max) {
					index = j;
					max = val[i][j];
				}
			}
			result[i] = index;
		}
		return result;
	}
	
	/**
	 * 获取当天最大的序号值
	 * @param val
	 * @param result
	 * @return
	 */
	public static int getMaxIndexOne(double[][] result)
	{
		int index = 0;
		double max = -Double.MAX_VALUE;
		for(int i=0;i<result.length;i++)
		{
			if (result[i][i] > max) {
				index = i;
				max = result[i][i] ;
			}
		}
		return index;
	}

	public static void println(double[][] val) {
		for (int i = 0; i < val.length; i++) {
			System.out.println(Arrays.toString(val[i]));
		}
	}

	public static void main(String[] args) {
		double[] start_p = new double[] { 0.63, 0.17, 0.20 };
		double[][] trans_p = new double[][] { { 0.5, 0.375, 0.125 },
				{ 0.25, 0.125, 0.625 }, { 0.25, 0.375, 0.375 } };
		double[][] emit_p=new double[][]{{0.6,0.25,0.05},{0.15,0.25,0.35},{0.05,0.25,0.50}};//,{0.2,0.25,0.1}
//		System.out.println(1);
//		Viterbi.println(Viterbi.computeP(start_p, trans_p, 1));
//		System.out.println(2);
//		Viterbi.println(Viterbi.computeP(start_p, trans_p, 2));
//		System.out.println(3);
//		Viterbi.println(Viterbi.computeP(start_p, trans_p, 3));
		
		
		System.out.println(Arrays.toString(Viterbi.computeP(start_p, trans_p, emit_p,0,3)));
		
		
		
		    double[] start_probability = new double[]{0.6, 0.4};
		     double[][] transititon_probability = new double[][]{
		            {0.7, 0.3},
		            {0.4, 0.6},
		    };
		     double[][] emission_probability = new double[][]{
		            {0.1, 0.4, 0.5},
		            {0.6, 0.3, 0.1},
		    };
		     double[][] emission_probability2 = new double[][]{
			            {0.1,0.6,},
			            {0.4, 0.3},
			            {0.5,0.1}
			    };
		     System.out.println(Arrays.toString(Viterbi.computeP(start_probability, transititon_probability, emission_probability2,0,2))); 
	}
}
