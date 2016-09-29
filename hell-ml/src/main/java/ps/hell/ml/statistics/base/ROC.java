package ps.hell.ml.statistics.base;


import java.util.Arrays;

/**
 * 真正 TP true positive 被模型预测为正的正样本
 * 假负 FN false negative 被模型预测为负的正样本
 * 假正 FP false positive 被模型预测为正的负样本
 * 真负 TN true negative 被模型预测为负的负样本
 * 
 * 真正率
 * tpr=tp/（tp+fn）
 * y轴
 * 假正率
 * fpr=fp/(fp+tn)
 * x轴
 * @author root
 *
 */
public class ROC {
	public final int tp=1;
	public final int fn=2;
	public final int fp=3;
	public final int tn=4;
	/**
	 * 正例數
	 */
	public int posNum=0;
	/**
	 * 反例數
	 */
	public int negNum=0;
	/**
	 * 具体匹配值
	 * 1 tp
	 * 2 fn
	 * 3 fp
	 * 4 tn
	 */
	public int[] val=null;
	/**
	 * x轴
	 */
	public double[] x=null;
	/**
	 * y轴
	 */
	public double[] y=null;
	/**
	 * 計算的初始面積
	 */
	public double auc=0d;
	/**
	 * 面積 數
	 */
	public double[] aucList=null;
	
	/**
	 * 面積 數
	 */
	public double[] aucList2=null;
	
	/**
	 * 
	 * @param data 实际值
	 * @param preVal 预测值
	 */
	public ROC(boolean[] data,boolean[] preVal){
		this.val=new int[data.length];
		this.aucList=new double[data.length+1];
		this.aucList2=new double[data.length+1];
		//因該還有排序 纔對
		for(int i=0;i<data.length;i++){
			if(data[i]==true){
				posNum++;
				if(preVal[i]==true){
					val[i]=tp;
				}else{
					val[i]=fn;
				}
			}else{
				negNum++;
				if(preVal[i]==true){
					val[i]=fp;
				}else{
					val[i]=tn;
				}
			}
		}
		  this.x=new double[data.length+1];
		  this.y=new double[data.length+1];
		  this.x[0]=1;
		  this.y[0]=1;
		  this.x[data.length]=0;
		  this.y[data.length]=0;
	}
	/**
	 * 执行体
	 */
	public void exec(){
		train();
	}
	/**
	 * 训练
	 */
	public void train(){
		for(int i=1;i<this.val.length;i++){
			double tpVal=getCount(i,tp);
			double fpVal=getCount(i,fp);
			x[i]=fpVal/negNum;
			y[i]=tpVal/posNum;
			auc+=(y[i]-y[i-1])*(x[i-1]-x[i])/2;
			aucList2[i]=auc;
		}
//		aucList2[]
	}
	/**
	 * 打印
	 */
	public void print(){
		for(int i=0;i<this.val.length;i++){
			//auc+=y[i];
			System.out.println("["+x[i]+"，"+y[i]+"]");
		}
		System.out.println(Arrays.toString(this.aucList2));
	}
	
	/**
	 * 獲取某一個標籤de count
	 * @param index start index
	 * @param tag
	 * @return
	 */
	public int getCount(int index,int tag){
		int count=0;
		for(int i=index;i<this.val.length;i++){
			if(this.val[i]==tag){
				count++;
			}
		}
		return count;
	}
	
	public Object test(){
		return null;
	}
	
	public static void main(String[] args) {
		boolean[] data=new boolean[]{true,true,true,false,false,false};
		boolean[] pre=new boolean[]{true,true,false,true,true,false};
		ROC roc=new ROC(data,pre);
		roc.exec();
		roc.print();
	}

}
