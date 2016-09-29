package ps.hell.ml.revaluate;

import ps.hell.math.base.MathBase;

/**
 *误差评估器皿
 */
public class EvaluateFactor<T extends Comparable<T>>{

	/**
	 * 卡方估计值
	 * @param tt
	 * @param tf
	 * @param ft
	 * @param ff
	 * @param n 自由度
	 * @return 值越大越好 x和y越相关
	 */
	public float chiSquare(int tt,int tf,int ft,int ff,int n){
		return n*MathBase.pow(tt*ff-tf*ft,2)/((tt+ft)*(tt+tf)*(ff+ft)*(ff+tf));
	}
	
	/**
	 * 卡方估计值 默认自由度为2
	 * @param tt
	 * @param tf
	 * @param ft
	 * @param ff
	 * @return 值越大越好 x和y越相关
	 */
	public float chiSquare(int tt,int tf,int ft,int ff){
		return 2f*MathBase.pow(tt*ff-tf*ft,2)/((tt+ft)*(tt+tf)*(ff+ft)*(ff+tf));
	}
	/**
	 *  卡方估计值 默认自由度为2
	 * @param x 0为false 1为true
	 * @param y 预测
	 * @return
	 */
	public float chiSquare(int[] x,int[] y){
		int tt=0;
		int tf=0;
		int ft=0;
		int ff=0;
		for(int i=0;i<x.length;i++){
			if(x[i]==0){
				if(y[i]==0){
					ff++;
				}else{
					ft++;
				}
			}else {
				if(y[i]==1){
					tt++;
				}else{
					tf++;
				}
			}
		}
		return chiSquare(tt,tf,ft,ff);
	}
	
	/**
	 * @param x 预测值
	 * @param y 评估值--通常评估值为0,1sigmod的解码值
	 * @return
	 *  交叉熵 
	 * 主要用在 sigmod函数 用来评估误差值
	 *	l(x,y)=-sum(xi*log(yi)+(1-xi)*log(1-yi))
	 */
	public double crossEntropy(double[] x,double[] y){
		EntropyUtil entropy=new EntropyUtil();
		return entropy.crossEntropy(x, y);
	}
	/**
	 * 平方误差
	 * @return
	 */
	public float squeredError(float[] x,float[] y){
		float re=0;
		for(int i=0;i<x.length;i++){
			re+=Math.abs(x[i]=y[i]);
		}
		return MathBase.sqrt(re);
	}
	/**
	 * 损失函数
	 * 需要重构损失方法，对y做重构计算
	 * @param x 该方法不需要有输出变量
	 * 是自编码和自解码的 无监督方法
	 * @param lossF
	 * @return
	 */
	public float lossFunction(float[] x,LossFunctionEvaluate lossF){
		float[] x2=new float[x.length];
		for(int i=0;i<x2.length;i++){
			x2[i]=lossF.evalue(x[i]);
		}
		return squeredError(x,x2);
	}
	
	public class LossFunctionEvaluate{
		public float evalue(float x){
			return x;
		}
	}
	/**
	 * 正确率
	 * @return
	 */
	public float  percentCorrect(T[] x,T[] forest){
		int count=0;
		for(int i=0;i<x.length;i++){
			if(x[i].compareTo(forest[i])==0){
				count++;
			}
		}
		return count*1f/x.length;
	}
	/**
	 * 正确率
	 * @return
	 */
	public float  percentCorrect(int[] x,int[] forest){
		int count=0;
		for(int i=0;i<x.length;i++){
			if(x[i]==forest[i]){
				count++;
			}
		}
		return count*1f/x.length;
	}
	/**
	 * 正确率
	 * @return
	 */
	public float  percentCorrect(long[] x,long[] forest){
		int count=0;
		for(int i=0;i<x.length;i++){
			if(x[i]==forest[i]){
				count++;
			}
		}
		return count*1f/x.length;
	}
	
	/**
	 * 召回率率
	 * @return
	 */
	public float  percentRecall(T[] x,T[] forest){
		return percentCorrect(x,forest);
	}
	/**
	 * 召回率
	 * @return
	 */
	public float  percentRecall(int[] x,int[] forest){
		return percentCorrect(x,forest);
	}
	/**
	 * 召回率
	 * @return
	 */
	public float  percentRecall(long[] x,long[] forest){
		return percentCorrect(x,forest);
	}
	
	
	/**
	 * f值
	 * @return
	 */
	public float fMeasure(float percentCorrect,float percentRecall){
		return percentCorrect * percentRecall * 2 / (percentCorrect + percentRecall);
	}
	
	/**
	 * f值
	 * @param alpha 权值
	 * @return
	 */
	public float fMeasure(float percentCorrect,float percentRecall,float alpha){
		return percentCorrect * percentRecall * (MathBase.pow(alpha,2)+1) / MathBase.pow(alpha,2)/(percentCorrect + percentRecall);
	}
	
	/**
	 * e值
	 * 查准率P和查全率R的加权平均值
	 * @return
	 */
	public float eMeasure(float percentCorrect,float percentRecall){
		return 1-2/(1/percentCorrect+1/percentRecall);
	}
	
	/**
	 * e值
	 * 查准率P和查全率R的加权平均值
	 * @return
	 */
	public float eMeasure(float percentCorrect,float percentRecall,float alpha){
		return 1-(1+alpha*alpha)/(alpha*alpha/percentCorrect+1/percentRecall);
	}
	/**
	 * 误差估计值
	 * @param x
	 * @param y
	 * @return
	 */
	public float mems(float[] x,float[] y){
		float sum=0f;
		for(int i=0;i<x.length;i++){
			sum+=MathBase.pow(x[i]-y[i],2);
		}
		return MathBase.sqrt(sum/x.length);
	}
}
