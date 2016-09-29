package ps.hell.ml.forest.model.svm;

import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.util.Vector;

import ps.landerbuluse.ml.math.matrix.Matrix;
import ps.landerbuluse.ml.statistics.base.Normal;
import ps.landerbuluse.plot.graph.base.BubbleGraph;

import com.googlecode.javacv.cpp.opencv_core;


/**
 * 处理二分类svm算法
 * @author Administrator
 *
 */
public class SvmBase{
	   
	/**
	 * 精度
	 */
		private double coef=1E-10;
	
		/**
		 * 标记是否正常
		 */
		private int flag;//标记是否正常
		/**
		 * 文件长度
		 */
	    private int filelen;//文件长度
	    /**
	     * 维度
	     */
	    private int length;//包括输出列的总维度
	    /**
	     * 截距
	     */
	    private double b;//存储截距
	    /**
	     * 数据源
	     */
	    private double[][]    svm_data;//存储源数据
	    /**
	     * 误差精度
	     */
	    private double sd_error;//存储精度
	    /**
	     * 损失权重
	     */
	    private double c_wigth;//存储损失函数权重
	    /**
	     * kernal 方差值
	     */
	    private double std_val;//存储kernal方差//可以控制在2
	    /**
	     * alpha的 拉格朗日 值
	     */
	    private double[] alpha;//alpha的largrange值
	    /**
	     * 核矩阵
	     */
	    private double[][] kernal;//存储核相关矩阵
	    
	    
	    /**
	     * 预测核
	     */
	    private double[] kernal_predict;
	    /**
	     * wx值
	     */
	    private double[] sum_val;//存储对kernal变换后的wx值
	    
	    /**
	     * 类类别
	     */
	    private String poly=null;
	    
	    

	/**
	 * 
	 * @param data 输入数据包括最后一列为output值
	 * @param sd 精度
	 * @param std 误差
	 * @param c 损失权重
	 * @param alpha alpha值
	 */
	public SvmBase(double[][] data,double sd,double std,double c,double alpha)
	{
	    this.flag=0;
	    if(alpha>c)
	    {
	        System.out.println("输入错误alpha值应比损失C小");
	        this.flag=1;
	        return ;
	    }
	    this.filelen=data.length;
	    this.length=data[0].length;
	    this.std_val=std;
	    this.alpha =new double[filelen];
	    this.svm_data=new double[filelen][length];
	    for(int i=0;i<filelen;i++)
	    {//存储数据
	        this.alpha[i]=alpha;
	        for(int j=0;j<length;j++)
	        {
	            svm_data[i][j]=data[i][j];
	        }
	    }
	    
	    this.sd_error=sd;
	    this.c_wigth=c;
	    this.b=0;
	    
	    //转换输出数据
	    transforOutput();
	}

	/**
	 * 高斯核
	 */
	public void kernalGaussianCompute()
	{
			poly="Gaussian";
			kernal=new double[filelen][filelen];
		    for(int i=0;i<filelen;i++)
		    {
		        for(int j=0;j<filelen;j++)
		        {
		        	kernal[i][j]=0.0;//初始化
		        }
		        for(int j=0;j<filelen;j++)
		        {
		            for(int m=0;m<length-1;m++)
		            {
		                kernal[i][j]+=(svm_data[i][m]-svm_data[j][m])*(svm_data[i][m]-svm_data[j][m]);
		            }
		            kernal[i][j]=Math.exp(-kernal[i][j]/2*std_val);//0.1-4之间方差值
		        }
		    }
	}
	
	/**
	 * 计算预测核
	 * 存储为kernal中的 1-filelen 个 输入与filelen个支持向量的值
	 * @param inputData
	 */
	public void kernalGaussianPredict(double[] inputData)
	{
		kernal_predict=new double[filelen];
		  for(int j=0;j<filelen;j++)
	        {
	            for(int m=0;m<length-1;m++)
	            {
	            	kernal_predict[j]+=(inputData[m]-svm_data[j][m])*(inputData[m]-svm_data[j][m]);
	            }
	            kernal_predict[j]=Math.exp(-kernal_predict[j]/2*std_val);//0.1-4之间方差值
	        }
	}
	
/**
 * 多项式核
 * @param len
 */
	public void kernalPolynomialCompute(double len)
	{
		poly="Polynomial";
		kernal=new double[filelen][filelen];
		 for(int i=0;i<filelen;i++)
		 {
		    for(int j=0;j<filelen;j++)
	        {
	          //  temp.push_back(0.0);
	        	kernal[i][j]=0.0;//初始化
	        }
		    for(int j=0;j<filelen;j++)
	        {
		    	for(int m=0;m<length-1;m++)
	            {
	                kernal[i][j]+=(svm_data[i][m]*svm_data[j][m]+1);
	                //if(i==1 && j==83)
	                //cout<<m<<"\t"<<svm_data[i][m]<<"\t"<<svm_data[j][m]<<"\tker "<<kernal[i][j]<<endl;
	            }
	            kernal[i][j]=Math.pow(kernal[i][j],len);//0.1-4之间方差值
	         //   if(i==1 && j==83)
	          //      cout<<len<<"\t"<<kernal[i][j]<<endl;
	        }
	    }
	}
	
	/**
	 * 计算预测核
	 * 
	 * @param inputData
	 */
	public void kernalPolynomialPredict(double len,double[] inputData)
	{
		kernal_predict=new double[filelen];
		for(int j=0;j<filelen;j++)
        {
	    	for(int m=0;m<length-1;m++)
            {
	    		kernal_predict[j]+=(inputData[m]*svm_data[j][m]+1);
            }
	    	kernal_predict[j]=Math.pow(kernal_predict[j],len);//0.1-4之间方差值
        }
	}
	
	/**
	 * 来自libsvm
	 * dot(x[i],x[j])
	 *线性核
	 * @param len
	 */
		public void kernalLinnerCompute()
		{
			poly="Linner";
			kernal=new double[filelen][filelen];
			for(int i=0;i<filelen;i++)
			{
				for(int j=0;j<filelen;j++)
				{
					for(int m=0;m<length-1;m++)
					{
						kernal[i][j]+=svm_data[i][m]*svm_data[j][m];
					}
				}
			}
		}
		
		/**
		 * 计算预测核
		 * 
		 * @param inputData
		 */
		public void kernalLinnerPredict(double[] inputData)
		{
			kernal_predict=new double[filelen];
			for(int j=0;j<filelen;j++)
			{
				for(int m=0;m<length-1;m++)
				{
					kernal_predict[j]+=inputData[m]*svm_data[j][m];
				}
			}
		}
	/**
	* 来自libsvm
	* 多项式核2
	* 线性核之后 做一次 len power
	* 平方和 之后 gamma*dot(x[i],x[j])+coef0
	* @param len
	*/
	public void kernalPolynomial2Compute(double len)	
	{
		poly="Polynomial2";
		double sum = 0;
		kernal=new double[filelen][filelen];
		for(int i=0;i<filelen;i++)
		{
			for(int j=0;j<filelen;j++)
			{
				for(int m=0;m<length-1;m++)
				{
					kernal[i][j]+=svm_data[i][m]*svm_data[j][m];
				}
				kernal[i][j]=Math.pow(kernal[i][j]+coef,len);
			}
		}
	}
	/**
	 * 计算预测核
	 * 
	 * @param inputData
	 */
	public void kernalPolynomial2Predict(double len,double[] inputData)
	{
		kernal_predict=new double[filelen];
		for(int j=0;j<filelen;j++)
		{
			for(int m=0;m<length-1;m++)
			{
				kernal_predict[j]+=inputData[m]*svm_data[j][m];
			}
			kernal_predict[j]=Math.pow(kernal_predict[j]+coef,len);
		}
	}
	
	/**
	 * 来自libsvm
	 * -gamma*(x_square[i]+x_square[j]-2*dot(x[i],x[j]))
	 * x_square[i] = dot(x[i],x[i]);
	 *RBF核
	 * @param len
	 */
	public void kernalRBFCompute()
	{
		poly="RBF";
		double sum = 0;
		kernal=new double[filelen][filelen];
		double[] kernal1=new double[filelen];
		for(int i=0;i<filelen;i++)
		{
			for(int m=0;m<length-1;m++)
			{
				kernal1[i]+=svm_data[i][m]*svm_data[i][m];
			}
		}
		for(int i=0;i<filelen;i++)
		{
			for(int j=0;j<filelen;j++)
			{
				for(int m=0;m<length-1;m++)
				{
					kernal[i][j]+=svm_data[i][m]*svm_data[j][m];
				}
				kernal[i][j]=-(kernal1[i]+kernal1[j]-2*kernal[i][j]);
			}
		}
	}
	
	/**
	 * 计算预测核
	 * 
	 * @param inputData
	 */
	public void kernalRBFPredict(double[] inputData)
	{
		kernal_predict=new double[filelen];
		double[] predict=new double[filelen];
		double temp=0.0;
		for(int m=0;m<length-1;m++)
		{
			temp+=inputData[m]*inputData[m];
		}
		for(int i=0;i<filelen;i++)
		{
			for(int m=0;m<length-1;m++)
			{
				predict[i]+=svm_data[i][m]*svm_data[i][m];
			}
		}
		for(int j=0;j<filelen;j++)
		{
			for(int m=0;m<length-1;m++)
			{
				kernal_predict[j]+=inputData[m]*svm_data[j][m];
			}
			kernal_predict[j]=-(temp+predict[j]-2*kernal_predict[j]);
		}
	}
	/**
	 * 来自libsvm
	 * tanh(gamma*dot(x[i],x[j])+coef0);
	 *	sigmoid核
	 * @param len
	 */
	public void kernalSigmoidCompute()
	{
		poly="Sigmoid";
		kernal=new double[filelen][filelen];
		for(int i=0;i<filelen;i++)
		{
			for(int j=0;j<filelen;j++)
			{
				for(int m=0;m<length-1;m++)
				{
					kernal[i][j]+=svm_data[i][m]*svm_data[j][m];
				}
				kernal[i][j]=Math.tanh(kernal[i][j]+coef);
			}
		}
	}
	/**
	 * 计算预测核
	 * 
	 * @param inputData
	 */
	public void kernalSigmoidPredict(double[] inputData)
	{
		kernal_predict=new double[filelen];
		for(int j=0;j<filelen;j++)
		{
			for(int m=0;m<length-1;m++)
			{
				kernal_predict[j]+=inputData[m]*svm_data[j][m];
			}
			kernal_predict[j]=Math.tanh(kernal_predict[j]+coef);
		}
	}
	
	/**
	 * 必须是规定的输入数据才有效
	 * 来自libsvm
	 * x[i][(int)(x[j][0].value)].value;  
	 * //x: test (validation), y: SV
	 *	precomputed核
	 * @param len
	 */
	public void kernalPrecomputedCompute()
	{
		poly="Precomputed";
		kernal=new double[filelen][filelen];
		for(int i=0;i<filelen;i++)
		{
			for(int j=0;j<filelen;j++)
			{
				kernal[i][j]=svm_data[i][(int)svm_data[j][0]];
			}
		}
	}
	/**
	 * 计算预测核
	 * 
	 * @param inputData
	 */
	public void kernalPrecomputedPredict(double[] inputData)
	{
		kernal_predict=new double[filelen];
		for(int j=0;j<filelen;j++)
		{
			kernal_predict[j]=inputData[(int)svm_data[j][0]];
		}
	}
	
	//计算添加核的wx值
	/**
	 * 计算添加核的wx值
	 */
	public void computeWxValue()
	{
		sum_val=new double[filelen];
	    for(int i=0;i<filelen;i++)
	    {
	        sum_val[i]=0.0;
	    }
	    for(int i=0;i<filelen;i++)
	    {
	        for(int j=0;j<filelen;j++)
	        {
	            sum_val[i]+=alpha[j]*svm_data[j][length-1]*kernal[j][i];
	        }
	    }
	}
	
	
	/**
	 * 执行整个svm计算过程
	 */
	public void computeAll()
	{
	    if(flag==0)
	    {
		    double wold=0;//未更新a的w(a)
		    double wnew=0;//更新a的w(a)
		    int mm=0;
		    while(true)
		    {
		        mm++;
		        //cout<<mm<<endl;
		        //初始选点
		        int n1=0,n2=1;
		        //kkt条件
		        //***********查看是否正确这个条件
		        while(n1<filelen)
		        {
		            //cout<<"sum:"<<sum_val[n1]<<endl;
		            //cout<<sum_val[n1]+b<<endl;
		            //cout<<"vale:"<<svm_data[n1][length-1]*(sum_val[n1]+b)<<endl;
		            //如果在下属条件中则为需要修改的alpha值
		            if((Double.compare(svm_data[n1][length-1]*(sum_val[n1]+b),1.0)==0) && (alpha[n1]>=c_wigth|| alpha[n1]<0.0))
		                break;
		            if(svm_data[n1][length-1]*(sum_val[n1]+b)>1.0 && Double.compare(alpha[n1],0.0)!=0)
		                break;
		            if(svm_data[n1][length-1]*(sum_val[n1]+b)<=1.0 && Double.compare(alpha[n1],c_wigth)!=0)
		                break;
		            //否则递n1
		            n1++;
		        }
		        System.out.println("值:"+svm_data[n1][length-1]+"\t"+(sum_val[n1]+b));
		        if(n1==filelen)
		        {
		            System.out.println("全部不满足kkt条件");
		            break;
		        }
		        //n2通过计算与n1具有最大误差的位置
		        double E1=0;
		        double E2=0;
		        double max=0;//更改误差
		        E1=sum_val[n1]+b-svm_data[n1][length-1];//计算值与实际值之差
		        
		        //计算n1的误差
		        for(int i=n1;i<filelen;i++)
		        {
		            double tempsum=sum_val[i]+b-svm_data[i][length-1];
		            //cout<<i<<":"<<tempsum<<endl;
		            if(Math.abs(E1-tempsum)>max)
		            {
		                max=Math.abs(E1-tempsum);//更改误差
		                n2=i;//选取n2;
		                E2=tempsum;
		            }
		        }
		      //  System.out.println("E1:"+E1+"\tE2:"+E2+"\tMax:"+max);
		        //cout<<"E1:"<<E1<<"\t"<<"E2:"<<E2<<endl;
		        //更新操作
		        double a1old=alpha[n1];
		        double a2old=alpha[n2];
		        System.out.println("n1:"+n1+"\tn2:"+n2);
		        double kk=kernal[n1][n1]+kernal[n2][n2]-2*kernal[n1][n2];
		        double a2new=a2old+svm_data[n2][length-1]*(E1-E2)/kk;
		        //从旧的值替换成新值。添加上detle差异值变量
		        double s=svm_data[n1][length-1]*svm_data[n2][length-1];
		        double u,v;
		        if(s==-1)//如果两个输出时不同的UV条件
		        {
		        	u=Math.max(0.0, a2old-a1old);
		        	v=Math.min(c_wigth,c_wigth-a1old+a2old);
		        }
		        else//如果相同
		        {
		        	u=Math.max(0.0,a2old+a1old-c_wigth);
		        	v=Math.min(c_wigth,a1old+a2old);
		           
		        }
		        if(a2new<u)
		            a2new=u;
		        else if(a2new>v)
		            a2new=v;
		        
		        double a1new=a1old+s*(a2old-a2new);//按照sum（ai）==0的约束条件条件
		        alpha[n1]=a1new;
		        alpha[n2]=a2new;//替换
		        //重新计算wt值
		        for(int i=0;i<filelen;i++)
		        {
		            
		            if(i==n1 || i==n2)
		            {
		            	sum_val[i]=0.0;
			            for(int j=0;j<filelen;j++)
			            {
			                //sum_val[i]+=alpha[j]*svm_data[j][length-1]*kernal[i][j];
			            	sum_val[i]+=alpha[j]*svm_data[j][length-1]*kernal[j][i];
			            }
		            }
		        }
		        wold=wnew;
		        wnew=0.0;
		        double tempsn=0.0;
		        //计算新的wnew
		        for(int i=0;i<filelen;i++)
		        {
		            for(int j=0;j<filelen;j++)
		            {
		                tempsn+=svm_data[i][length-1]*svm_data[j][length-1]*alpha[i]*alpha[j]*kernal[i][j];
		            }
		            wnew+=alpha[i];
		        }
		        wnew=wnew-0.5*tempsn;//max(q(x))值
		        //更新b
		        int support =1;//支持向量坐标初始化
		        while(Math.abs(alpha[support])<1e-4 && support<filelen-1)
		        {
		            support++;//找一个支持向量
		        }
		        //b=1.0/svm_data[support][length-1]-sum_val[support];//1/y-wx计算b值y=wx+b->是否有错
		        double b1=b-E1-svm_data[n1][length-1]*(a1new-a1old)*kernal[n1][n1]-
		            svm_data[n2][length-1]*(a2new-a2old)*kernal[n2][n1];
		        double b2=b-E2-svm_data[n1][length-1]*(a1new-a1old)*kernal[n1][n2]-
		            svm_data[n2][length-1]*(a2new-a2old)*kernal[n2][n2];
		        if(0<a1new&& a1new<c_wigth)
		        {
		            b=b1;
		        }
		        else
		        {
		            if(0<a2new && a2new<c_wigth)
		            {
		                b=b2;
		            }
		            else
		            {
		                b=(b1+b2)/2.0;
		            }
		        }
		        System.out.println("new:"+wnew+":"+wold);
		        System.out.println("误差:"+Math.abs(wnew/wold-1)+"\tb:"+b+"\t"+sd_error);
		        if(Double.compare(wold,0.0)==0)
		        {
		        	continue;
		        }
		        if(Math.abs(wnew/wold-1)<=sd_error && !Double.isInfinite(Math.abs(wnew/wold-1)))//如果新变量和就变量之间的差值足够小则跳出
		        {  
		            System.out.println("结束并收敛");
		            break;
		        }
		    }
	    }
	    else
	    {
	        //cout<<"计算错误"<<endl;
	    }
	}
	
	
	/**
	 * @param 当左后的output输入为10时需要将之修改为1-1对应
	 * @param 或者为-10时
	 * @param 将0修改为-1
	 */
	public void transforOutput()
	{
	System.out.println("执行转换");	
		double ll1=100.0;
		double ll2=100.0;
	//	System.out.println(filelen+"\t:::\t"+length);
		for(int i=0;i<filelen;i++)
		{
			//System.out.println(this.svm_data[i][length-1]+"\t"+ll1);
			
			if(Double.compare(this.svm_data[i][length-1],ll1)!=0)
			{
				if(ll1==100.0)
				{
					ll1=this.svm_data[i][length-1];
					continue;
				}
			
				if(Double.compare(this.svm_data[i][length-1],ll2)!=0)
				{
					ll2=this.svm_data[i][length-1];
					System.out.println("跳出");
					break;
				}
				else
				{
					break;
				}
			}
		}
		System.out.println(ll1+":::::"+ll2);
		if((Double.compare(ll1,0.0)==0 && Double.compare(ll2,1.0)==0 )||(Double.compare(ll2,0.0)==0 
				&& Double.compare(ll1,1.0)==0 ))
		{
			for(int i=0;i<filelen;i++)
			{
				if(Double.compare(this.svm_data[i][length-1],0.0)==0)
		        {
					this.svm_data[i][length-1]=-1.0;
		        }
			}
		}
		if((Double.compare(ll1,0.0)==0 && Double.compare(ll2,-1.0)==0 )||(Double.compare(ll2,0.0)==0 
				&& Double.compare(ll1,-1.0)==0 ))
		{
			for(int i=0;i<filelen;i++)
			{
				if(Double.compare(this.svm_data[i][length-1],0.0)==0)
		        {
					this.svm_data[i][length-1]=1.0;
		        }
			}
		}
		
	}
	/**
	 * 
	 * @param input
	 * @param count 如果为多项式数
	 * @return
	 */
	public double predict(double[] input,int count)
	{

		if(poly.equals("Gaussian"))
		{
		 kernalGaussianPredict(input);//计算kernal核矩阵	
		}
		else if(poly.equals("RBF"))
		{
			kernalRBFPredict(input);//计算kernal核矩阵
		}
		else if(poly.equals("Linner"))
		{
		  kernalLinnerPredict(input);
		}
		else if(poly.equals("Sigmoid"))
		{
			kernalSigmoidPredict(input);
		}
		else if(poly.equals("Polynomial2"))
		{
			this.kernalPolynomial2Predict(count, input);
		}
		else if(poly.equals("Precomputed"))
		{
			this.kernalPrecomputedPredict(input);
		}
		else
		{
			System.out.println("输入kernal 错误 请检查");
			return 0.0;
		}
		 double sum=0.0;
		 for(int i=0;i<filelen;i++)
		 {
			 sum+=alpha[i]*svm_data[i][length-1]*kernal_predict[i];
		 }
		 return sum+b;
	}	
	
	
	/**
	 * svm训练集
	 */
	public  void train(String poly)
	{
		if(poly.equals("Gaussian"))
		{
		 kernalGaussianCompute();//计算kernal核矩阵
		}
		else if(poly.equals("RBF"))
		{
			kernalRBFCompute();//计算kernal核矩阵
		}
		else if(poly.equals("Linner"))
		{
		  kernalLinnerCompute();
		}
		else
		{
			System.out.println("输入kernal 错误 请检查");
			return;
		}
		this.poly=poly;
		 computeWxValue();//就算对kernal变换后的wx值
		 computeAll();//执行所有计算
	}
	/**
	 * 测试程序
	 * @param argc
	 * @return
	 */
	public static void main(String[] args)
	{
		Normal n1=new Normal(-3,1);
		Matrix m1=n1.random(50,2,0);
		
		Normal n2=new Normal(1,1);
		Matrix m2=n2.random(50,2,1);
		
		m1.addMatrix(m2);
		double[][] data=m1.getData();
	
	//	System.out.println("m1:"+m1.getRowNum());
		
		//MatrixWriteTable.WriteToTxt("e:\\ml_test_work\\rtest1_bp_test.txt", matrix);
	   // double[][] data=MatrixReadSource.ReadTodouble("e:\\ml_test_work\\rtest1_bp_test.txt");
	    //mat data=matinput_data("d:\\test\\rtest_bp_13.txt");
	    //mat data=matinput_data("d:\\data.txt");
	    ////标准化
	    int filelen=data.length;
	    int length=data[0].length;
	    double sd=0.0001;//设定分类精度
	    //修改不同的C和alp初始值 可以获取不同的效果
	    double C=0.1;//对损失权重的c赋值
	    double wold=0,wnew=0;//更新前w(a)，更新后w（a）
	    double alp=0.1;//初始化apha
	    double std=1;//std=1//kernal方差值//与整体方差有关
	    //将输出更改为1,-1；当前输入为10
	        std=0.3;
		    SvmBase svm=new SvmBase(data,sd,std,C,alp);//初始化svm类
		    //svm.kernalPolynomialCompute(2.0);//多项式核
		    svm.train("Gaussian");
		    
		    int count=0;
		    for(int i=0;i<filelen;i++)
		    {
		       System.out.printf("第%d次：判别函数%f点：元标为%f:",i,svm.sum_val[i]+svm.b,svm.svm_data[i][length-1]);
		    	if(svm.sum_val[i]+svm.b>0.3)
		        {    if(svm.svm_data[i][length-1]>0)
		            {
		        		System.out.println("分类为：1");
		            }
		            else
		            {
		                count++;
		                System.out.println("分类错误:"+svm.svm_data[i][length-1]);
		            }
		        }
		        else
		        {
		            if(svm.sum_val[i]+svm.b<-0.3)
		            {
		                if(svm.svm_data[i][length-1]<0)
		                {
		                	System.out.println("分类为：-1");
		                }
		                else
		                {
		                    count++;
		                    System.out.println("分类错误:"+svm.svm_data[i][length-1]);
		                }
		            }
		            else
		            {
		                count++;
		                System.out.println("分类错误:"+svm.svm_data[i][length-1]);
		            }
		        }
		    }
		    System.out.println(count+":"+filelen+"\t"+std+"\t错误率为:"+(count*1.0)/filelen+"%");
		    
		    
		    //执行预测状态
		  
		    //画出来

			Vector<Double> x=new Vector<Double>();
			Vector<Double> y=new Vector<Double>();
			Vector<Integer> cluster=new Vector<Integer>();
			for(int i=0;i<svm.svm_data.length;i++)
			{
			//	System.out.println(svm.svm_data[i][0]+"\t"+svm.svm_data[i][1]+"\t"+svm.sum_val[i]+svm.b);
				x.add(svm.svm_data[i][0]);
				y.add(svm.svm_data[i][1]);
				double predict=svm.sum_val[i]+svm.b;
				if(predict>0)
					cluster.add(1);
				else
					cluster.add(0);
			}
			
			
			//绘图模块
			
			BubbleGraph graph=new BubbleGraph();
			graph.width=600;
		    graph.height=400;
			
			graph.parentent_config();//计算改变的所有全局参量
			  //初始化图片 
				BubbleGraph.img_trans img1=new BubbleGraph.img_trans();
			    //main_type="scatter";
			    img1.img1=opencv_core.cvCreateImage(opencv_core.cvSize(graph.width,graph.height),8,3);
			    graph.main_type="qipao2";
			    
			    if(graph.main_type=="scatter" ||graph.main_type=="qipao" ||graph.main_type=="qipao2" ||graph.main_type=="qipao3" )
			    {
			        System.out.println("初始化图片");
			        graph.plotScatter(img1,x,y,graph.main_type,cluster,0);
			    }
			    
			    cvNamedWindow("train",1);
			    cvShowImage("train",img1.img1);
			   // cvWaitKey(0);
				Vector<Double> x1=new Vector<Double>();
				Vector<Double> y1=new Vector<Double>();
				Vector<Integer> cluster1=new Vector<Integer>();
				for(int i=-20;i<20;i++)
				{
					for(int j=-20;j<20;j++)
					{
						x1.add(i*1.0/3);
						y1.add(j*1.0/3);
						double[] ll=new double[2];
						ll[0]=i*1.0/3;
						ll[1]=j*1.0/3;
						 double predict= svm.predict(ll,1);
						// System.out.println(ll[0]+"\t"+ll[1]+"\t"+predict);
						if(predict>0.5)
						cluster1.add(1);
						else if(predict<-0.5)
							cluster1.add(0);
						else
							cluster1.add(2);
					}
				}
				
				BubbleGraph graph2=new BubbleGraph();
				graph2.width=600;
			    graph2.height=400;
				
				graph2.parentent_config();//计算改变的所有全局参量
				  //初始化图片 
					BubbleGraph.img_trans img2=new BubbleGraph.img_trans();
				    //main_type="scatter";
				    img2.img1=opencv_core.cvCreateImage(opencv_core.cvSize(graph2.width,graph2.height),8,3);
				    graph2.main_type="qipao2";
				    
				    if(graph2.main_type=="scatter" ||graph2.main_type=="qipao" ||graph2.main_type=="qipao2" ||graph2.main_type=="qipao3" )
				    {
				        System.out.println("初始化图片");
				        graph2.plotScatter(img2,x1,y1,graph2.main_type,cluster1,0);
				    }
				    
				    cvNamedWindow("test",1);
				    cvShowImage("test",img2.img1);
				//    cvWaitKey(0);
			
					Vector<Double> x3=new Vector<Double>();
					Vector<Double> y3=new Vector<Double>();
					Vector<Integer> cluster3=new Vector<Integer>();
					for(int i=0;i<svm.svm_data.length;i++)
					{
						x3.add(svm.svm_data[i][0]);
						y3.add(svm.svm_data[i][1]);
						double predict=svm.svm_data[i][2];
						if(predict>0)
							cluster3.add(1);
						else
							cluster3.add(0);
					}
					
					BubbleGraph graph3=new BubbleGraph();
					graph3.width=600;
				    graph3.height=400;
					
					graph3.parentent_config();//计算改变的所有全局参量
					  //初始化图片 
						BubbleGraph.img_trans img3=new BubbleGraph.img_trans();
					    //main_type="scatter";
					    img3.img1=opencv_core.cvCreateImage(opencv_core.cvSize(graph3.width,graph3.height),8,3);
					    graph3.main_type="qipao2";
					    
					    if(graph3.main_type=="scatter" ||graph3.main_type=="qipao" ||graph3.main_type=="qipao2" ||graph3.main_type=="qipao3" )
					    {
					        System.out.println("初始化图片");
					        graph3.plotScatter(img3,x3,y3,graph3.main_type,cluster3,0);
					    }
					    
					    cvNamedWindow("source",1);
					    cvShowImage("source",img3.img1);
					    cvWaitKey(0);
				    
	    }
	
}
