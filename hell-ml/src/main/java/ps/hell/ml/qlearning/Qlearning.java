package ps.hell.ml.qlearning;

import java.util.ArrayList;
import java.util.Random;

/**
 * http://blog.csdn.net/pi9nc/article/details/27649323
 * q-learning 通过 无监督学习，获取 从任意位置到目标位置的路径方式 r矩阵对应状态-行动，如果状态1-状态2
 * 无行动则为-1，否则为0，如果1-2状态为可联通则为100 q矩阵代表累计转移矩阵，用于描述学习过后的值
 * 
 * @author Administrator
 *
 */
public class Qlearning {

	public int[][] rMatrix = null;

	public float[][] qMatrix = null;
	/**
	 * 迭代次数
	 */
	public int repeatCount=1000;
	/**
	 * 学习率
	 */
	float alpha = 0.8f;
	/**
	 * 行数 m*m
	 */
	public int xLength = 0;
	
	Random random = new Random();
/**
 * 最终的路线
 */
	public ArrayList<Integer> path=null;
	/**
	 * @param rMatrix
	 *            r状态活动矩阵
	 * @param repeatCount
	 *            重复训练的次数
	 */
	public Qlearning(int[][] rMatrix, int repeatCount) {
		this.rMatrix = rMatrix;
		this.xLength = rMatrix.length;
		this.qMatrix = new float[xLength][];
		for (int i = 0; i < rMatrix.length; i++) {
			qMatrix[i] = new float[xLength];
		}
	}
	/**
	 * 执行程序
	 */
	public void exec() {
		for(int i=0;i<repeatCount;i++){
			execOne();
		}
		//更新为标准值
		updateToStander();
	}
	/**
	 * 测试一个点
	 * val 对应 x,x点 的位置
	 */
	public void forest(Object[] val){
		path=new ArrayList<Integer>(xLength);
		int index=(int)val[0];
		path.add(index);
		while(!isStop(index)){
			index=selectOne(index);
			if(index<0){
				break;
			}
			path.add(index);
		}
	}
	/**
	 * 获取下一个 action的位置
	 * @param index
	 * @return
	 */
	public int selectOne(int index){
		float max=-1f;
		int val=-1;
		for(int i=0;i<xLength;i++){
			if(i==index){
				continue;
			}
			if(rMatrix[index][i]>max){
				max=rMatrix[index][i];
				val=i;
			}
		}
		return val;
	}

	/**
	 * 一次调度
	 * 
	 * @return 是否有效
	 */
	public void execOne() {
		for(int status1=0;status1<xLength;status1++)
		{
			for(int status2=0;status2<xLength;status2++)
			{
				if(status1==status2)
				{
					continue;
				}else if(rMatrix[status1][status2]<0){
					continue;
				}
			
				// 如果没有到最后则只需迭代下去
				int status3=status1;
				int status4=status2;
				boolean flag=false;
				while (!isStop(status3)) {
					if(rMatrix[status3][status4]<0){
						flag=true;
						break;
					}
					// 更新Q矩阵
					qMatrix[status3][status4] = rMatrix[status3][status4] + alpha
							* maxRow(status4);
					status3=status4;
					status4=randomTwo(status3);
					if(status4<0){
						break;
					}
				}
				if(flag){
					continue;
				}
				//MathMethod.print(qMatrix);
			}
		}
//		int status1 = randomOne();
//		if (status1 < 0) {
//			return false;
//		}
//		// 如果没有到最后则只需迭代下去
//		while (!isStop(status1)) {
//			int status2 = randomTwo(status1);
//			// 更新Q矩阵
//			qMatrix[status1][status2] = rMatrix[status1][status2] + alpha
//					* maxRow(status2);
//			status1=status2;
//		//	System.out.println(status1+":"+status2+"="+qMatrix[status1][status2]);
//		}
//		MathMethod.print(qMatrix);
//		return true;
	}
	

	/**
	 * 随机获取一个点 作活动1
	 * 
	 * @return 如果返回为-1则表示不存在任何点
	 */
	public int randomOne() {
		int index = -1;
		int zn = 0;
		while (true) {
			zn++;
			if (zn > xLength * 100) {
				break;
			}else if(zn%100==0){
				System.out.println("迭代次数:"+zn);
			}
			index = Math.abs(random.nextInt() % xLength);
			// 判断该点是否为结束点
			//System.out.println(index+"\t"+isStop(index)+"\t"+isConnect(index));
			if (isStop(index)) {
				continue;
				// 判断是否为有效联通点>=0
			} else if (isConnect(index)) {
				break;
			}
		}
		return index;
	}

	/**
	 * 随机获取一个点作为活动2
	 * 
	 * @param status1
	 * @return
	 */
	public int randomTwo(int status1) {
		// 获取有效的数量
		// 如果为stop点则直接返回对应的index
		ArrayList<Integer> list = new ArrayList<Integer>(xLength);
		for (int i = 0; i < xLength; i++) {
			if (rMatrix[status1][i] == 0) {
				list.add(i);
			} else if (rMatrix[status1][i] == 100) {
				list = null;
				return i;
			}
		}
		int index =-1;
		while(true)
		{
			index=list.remove(Math.abs(random.nextInt() % list.size()));
			if(index!=status1){
				break;
			}
		}
		list = null;
		return index;
	}

	/**
	 * R(x,y)+alpha*matrix(R(rows)) 获取一行中的最大值
	 * 
	 * @param rowNum
	 * @return
	 */
	public float maxRow(int rowNum) {
		float matrix = -1f;
		for (int i = 0; i < xLength; i++) {
			if (qMatrix[rowNum][i] > matrix) {
				matrix = qMatrix[rowNum][i];
			}
		}
		return matrix;
	}

	/**
	 * 判断最终是否为终止点
	 */
	public boolean isStop(int index) {
		for (int i = 0; i < xLength; i++) {
			if (rMatrix[i][index] == 100) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否有有效联通店
	 * 
	 * @param index
	 * @return
	 */
	public boolean isConnect(int index) {
		for (int i = 0; i < xLength; i++) {
			if (rMatrix[index][i] >= 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 将q矩阵更新为标准矩阵格式
	 * 最大值为1f
	 */
	public void updateToStander(){
		//获取最大值
		float maxVal=0;
		for(int i=0;i<xLength;i++){
			for(int j=0;j<xLength;j++){
				if(qMatrix[i][j]<maxVal){
					maxVal=qMatrix[i][j];
				}
			}
		}
		//标准化
		for(int i=0;i<xLength;i++){
			for(int j=0;j<xLength;j++){
				qMatrix[i][j]/=maxVal;
			}
		}
	}
	/**
	 * 打印路径
	 */
	public void printPath(){
		if(path==null)
		{
			System.out.println(null+"");
		}else{
			System.out.print("start:");
			for(int i=0;i<path.size();i++){
				if(i==0){}else{
					System.out.print(" --> "); 
				}
				System.out.print(path.get(i));
				
			}
		}
	}
	
	public static void main(String[] args) {
		int[][] rMatrix=new int[][]{
				{-1,-1,-1,-1,0,-1},
				{-1,-1,-1,0,-1,100},
				{-1,-1,-1,0,-1,-1},
				{-1,0,0,-1,0,-1},
				{0,-1,-1,0,-1,100},
				{-1,0,-1,-1,0,100}
		};
		Qlearning qlearning=new Qlearning(rMatrix,10);
		qlearning.exec();
		qlearning.forest(new Integer[]{2});
		qlearning.printPath();
	}
}
