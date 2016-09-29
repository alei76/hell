package ps.hell.ml.forest.model.anns;
/**
 * 广义rbf
 * 用梯度下降法训练RBF网络，
 * 设η=0.001，M=10，初始权值为[-0.1,0.1]内的随机数，
 * 初始数据中心为[-4,4]内的随机数，
 * 初始扩展常数取[0.1,0.3]内的随机数，目标误差为0.9，最大训练次数为5000。
 * @author Administrator
 *
 */
public class Rbf2Ann {
	 
	private int P=100;        //输入样本的数量
	double[] X=new double[this.P];  //输入样本
	double[] Y=new double[this.P];      //输入样本对应的期望输出
	private int M=10;         //隐藏层节点数目
	double[] center=new double[M];       //M个Green函数的数据中心
	double[] delta=new double[M];        //M个Green函数的扩展常数
	double[][] Green=new double[P][M];         //Green矩阵
	double[] Weight=new double[M];       //权值矩阵
	private  double eta=0.001;     //学习率
	private double ERR=0.9;       //目标误差
	private int ITERATION_CEIL=5000;      //最大训练次数
	double[] error=new double[this.P];  //单个样本引起的误差
	 
	/*Hermit多项式函数
	 * 伪测试 这个方法
	 */
	public double Hermit(double x){
	    return 1.1*(1-x+2*x*x)*Math.exp(-1*x*x/2);
	}
	 
	/*产生指定区间上均匀分布的随机数*/
	public double uniform(double floor,double ceil){
	    return floor+1.0*Math.random()/1*(ceil-floor);
	}
	 
	/*产生区间[floor,ceil]上服从正态分布N[mu,sigma]的随机数*/
	public double RandomNorm(double mu,double sigma,double floor,double ceil){
	    double x,prob,y;
	    do{
	        x=uniform(floor,ceil);
	        prob=1/Math.sqrt(2*Math.PI*sigma)*Math.exp(-1*(x-mu)*(x-mu)/(2*sigma*sigma));
	        y=1.0*Math.random()/1;
	    }while(y>prob);
	    return x;
	}
	 
	/*产生输入样本*/
	void generateSample(){
	    for(int i=0;i<P;++i){
	        double in=uniform(-4,4);
	        X[i]=in;
	        Y[i]=Hermit(in)+RandomNorm(0,0.1,-0.3,0.3);
	    }
	}
	 
	/*给向量赋予[floor,ceil]上的随机值*/
	void initVector(double[] vec,double floor,double ceil){
	    for(int i=0;i<vec.length;++i)
	        vec[i]=uniform(floor,ceil);
	}
	 
	/*根据网络，由输入得到输出*/
	double getOutput(double x){
	    double y=0.0;
	    for(int i=0;i<M;++i)
	        y+=Weight[i]*Math.exp(-1.0*(x-center[i])*(x-center[i])/(2*delta[i]*delta[i]));
	    return y;
	}
	 
	/*计算单个样本引起的误差*/
	double calSingleError(int index){
	    double output=getOutput(X[index]);
	    return Y[index]-output;
	}
	 
	/*计算所有训练样本引起的总误差*/
	double calTotalError(){
	    double rect=0.0;
	    for(int i=0;i<P;++i){
	        error[i]=calSingleError(i);
	        rect+=error[i]*error[i];
	    }
	    return rect/2;
	}
	 
	/*更新网络参数*/
	void updateParam(){
	    for(int j=0;j<M;++j){
	        double delta_center=0.0,delta_delta=0.0,delta_weight=0.0;
	        double sum1=0.0,sum2=0.0,sum3=0.0;
	        for(int i=0;i<P;++i){
	        	//中心变化 yita*wj/deltaj^2*sum(error*green*(xi-center(i)))
	            sum1+=error[i]*Math.exp(-1.0*(X[i]-center[j])*(X[i]-center[j])/(2*delta[j]*delta[j]))*(X[i]-center[j]);
	          //delta变化 yita*wj/deltaj^3*sum(error*green*(||xi-center(i)||))
	            sum2+=error[i]*Math.exp(-1.0*(X[i]-center[j])*(X[i]-center[j])/(2*delta[j]*delta[j]))*(X[i]-center[j])*(X[i]-center[j]);
	          //权值变化 yita*sum(error*green)
	            sum3+=error[i]*Math.exp(-1.0*(X[i]-center[j])*(X[i]-center[j])/(2*delta[j]*delta[j]));
	        }
	        delta_center=eta*Weight[j]/(delta[j]*delta[j])*sum1;
	        delta_delta=eta*Weight[j]/Math.pow(delta[j],3)*sum2;
	        delta_weight=eta*sum3;
	        center[j]+=delta_center;
	        delta[j]+=delta_delta;
	        Weight[j]+=delta_weight;
	    }
	}
	 
	public static void main(String[] args){
	    /*初始化网络参数*/
		Rbf2Ann rbf=new Rbf2Ann();
		rbf.initVector(rbf.Weight,-0.1,0.1);
	    rbf.initVector(rbf.center,-4.0,4.0);
	    rbf.initVector(rbf.delta,0.1,0.3);
	    /*产生输入样本*/
	    rbf.generateSample();
	    /*开始迭代*/
	    int iteration=rbf.ITERATION_CEIL;
	    while(iteration-->0){
	        if(rbf.calTotalError()<rbf.ERR)      //误差已达到要求，可以退出迭代
	            break;
	        rbf.updateParam();      //更新网络参数
	    }
	    System.out.println("迭代次数："+(rbf.ITERATION_CEIL-iteration-1));
	   // cout<<"迭代次数:"<<ITERATION_CEIL-iteration-1<<endl;
	     
	    //根据已训练好的神经网络作几组测试
	    for(int x=-4;x<5;++x){
	    	System.out.print(x+"\t");
	        //cout<<x<<"\t";
	        //cout<<setprecision(8)<<setiosflags(ios::left)<<setw(15);
	        //cout<<getOutput(x)<<Hermit(x)<<endl;      //先输出我们预测的值，再输出真实值
	    	System.out.println(rbf.getOutput(x)+"\t"+rbf.Hermit(x));
	    }
	}
}
