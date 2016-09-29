package ps.hell.math.base.matrix;

/**
 * matrix初始化类
 * @author Administrator
 *
 */
public class MatrixInit {
	
	/**
	 * 获取随机数
	 * @param x
	 * @param y
	 * @param rate 0-1值*比率
	 * @return
	 */
	public static double[][] random(int x,int y,double rate){
		double[][] val=new double[x][];
		if(rate==1f){
			for(int i=0;i<x;i++){
				double[] row=new double[y];
				for(int j=0;j<y;j++){
					row[j]=Math.random();
				}
				val[i]=row;
			}
		}else{
			for(int i=0;i<x;i++){
				double[] row=new double[y];
				for(int j=0;j<y;j++){
					row[j]=Math.random()*rate;
				}
				val[i]=row;
			}
		}
		return val;
	}
	/**
	 * @param x
	 * @param y
	 * @param rate
	 * @return
	 */
	public static double[][] eye(int x,int y,double rate){
		double[][] val=new double[x][];
			for(int i=0;i<x;i++){
				double[] row=new double[y];
				val[i]=row;
				for(int j=0;j<y;j++){
					val[i][j]=rate;
				}
			}
		return val;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param rate
	 * @return
	 */
	public static double[][] number(int x,int y,double rate){
		double[][] val=new double[x][];
		if(rate==0d){
			for(int i=0;i<x;i++){
				double[] row=new double[y];
				for(int j=0;j<y;j++){
					row[j]=rate;
				}
				val[i]=row;
			}
		}else{
			for(int i=0;i<x;i++){
				double[] row=new double[y];
				for(int j=0;j<y;j++){
					row[j]=rate;
				}
				val[i]=row;
			}
		}
		return val;
	}
	/**
	 * 获取固定值
	 * @param x
	 * @param y
	 * @param rate
	 * @return
	 */
	public static double[] number(int x,double rate){
		double[] val=new double[x];
		if(rate==0d){
		}else{
			for(int i=0;i<x;i++){
				val[i]=rate;
			}
		}
		return val;
	}
	
	//获取魔术矩阵
}
