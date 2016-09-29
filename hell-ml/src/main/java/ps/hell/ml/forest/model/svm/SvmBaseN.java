package ps.hell.ml.forest.model.svm;


import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;

import ps.landerbuluse.ml.math.matrix.Matrix;
import ps.landerbuluse.ml.statistics.base.Normal;
import ps.landerbuluse.plot.graph.base.BubbleGraph;

import com.googlecode.javacv.cpp.opencv_core;

/**
 * 处理多分类svm算法
 * @author Administrator
 *
 */
public class SvmBaseN{
	   
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
	     * 维度输入层维度
	     */
	    private int dimensionality;//
	    /**
	     * 输出层维度
	     */
	    private int outputNum;
	    /**
	     * 截距
	     */
	    private double[] b;//存储截距
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
	    private double[][] alpha;//alpha的largrange值
	    /**
	     * 核矩阵
	     */
	    private double[][][] kernal;//存储核相关矩阵
	    
	    
	    /**
	     * 预测核
	     */
	    private double[][] kernal_predict;
	    /**
	     * wx值
	     */
	    private double[][] sum_val;//存储对kernal变换后n个wx值
	    
	    /**
	     * 类类别
	     */
	    private String poly=null;
	    
		/**
		 * 标准的分类形式 如只有 n个分类 则存储n个
		 */
		public String[] outputStandardString;
		/**
		 * 存储对应标准分类的n个分类的的编译01二进制编码
		 */
		public LinkedList<int[]> outputStandardValue=new LinkedList<int[]>();
	    
	    
		/**
		 * 输出变量对应的string值
		 */
		public LinkedList<String> outputNormalString=new LinkedList<String>();
		/**
		 * 存储对应的编译01数据
		 */
		public LinkedList<int[]> outputNormalValue=new LinkedList<int[]>();

	/**
	 * 
	 * @param data 输入数据包括最后一列为output值
	 * @param sd 精度
	 * @param std 误差
	 * @param c 损失权重
	 * @param alpha alpha值
	 */
	public SvmBaseN(LinkedList<double[]> datainput,LinkedList<String[]> dataoutput,double sd,double std,double c,double alpha)
	{
		//将信息转化到 Standard信息中标准中
		changeToBinal(dataoutput);
		//将转化后的01二进制输入到norma信息中
		changeOutputToBinal(dataoutput);
		
	    this.flag=0;
	    if(alpha>c)
	    {
	        System.out.println("输入错误alpha值应比损失C小");
	        this.flag=1;
	        return ;
	    }
	    this.filelen=datainput.size();
	    this.dimensionality=datainput.get(0).length;
	    this.outputNum=this.outputStandardValue.get(0).length;
	    this.std_val=std;
	    this.alpha =new double[outputNum][filelen];
	    this.svm_data=new double[filelen][dimensionality];
	    this.b=new double[outputNum];
	    for(int k=0;k<outputNum;k++)
	    {
	        this.b[k]=0;
	    for(int i=0;i<filelen;i++)
	    {//存储数据
	        this.alpha[k][i]=alpha;
	        for(int j=0;j<dimensionality;j++)
	        {
	            svm_data[i][j]=datainput.get(i)[j];
	        }
	    }
	    }

	    this.sd_error=sd;
	    this.c_wigth=c;

	}

	/**
	 * 高斯核
	 */
	public void kernalGaussianCompute()
	{
			poly="Gaussian";
			kernal=new double[outputNum][filelen][filelen];
			 for(int k=0;k<outputNum;k++)
			    {
			    for(int i=0;i<filelen;i++)
			    {
			        for(int j=0;j<filelen;j++)
			        {
			        	kernal[k][i][j]=0.0;//初始化
			        }
			        for(int j=0;j<filelen;j++)
			        {
			            for(int m=0;m<dimensionality;m++)
			            {
			                kernal[k][i][j]+=(svm_data[i][m]-svm_data[j][m])*(svm_data[i][m]-svm_data[j][m]);
			            }
			            kernal[k][i][j]=Math.exp(-kernal[k][i][j]/2*std_val);//0.1-4之间方差值
			        }
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
		kernal_predict=new double[outputNum][filelen];
		 for(int k=0;k<outputNum;k++)
		    {
		  for(int j=0;j<filelen;j++)
	        {
	            for(int m=0;m<dimensionality;m++)
	            {
	            	kernal_predict[k][j]+=(inputData[m]-svm_data[j][m])*(inputData[m]-svm_data[j][m]);
	            }
	            kernal_predict[k][j]=Math.exp(-kernal_predict[k][j]/2*std_val);//0.1-4之间方差值
	        }
		    }
	}
	
/**
 * 多项式核
 * @param len
 */
	public void kernalPolynomialCompute(double len)
	{
		poly="Polynomial";
		kernal=new double[outputNum][filelen][filelen];
		for(int k=0;k<outputNum;k++)
		{
		 for(int i=0;i<filelen;i++)
		 {
		    for(int j=0;j<filelen;j++)
	        {
	          //  temp.push_back(0.0);
	        	kernal[k][i][j]=0.0;//初始化
	        }
		    for(int j=0;j<filelen;j++)
	        {
		    	for(int m=0;m<dimensionality;m++)
	            {
	                kernal[k][i][j]+=(svm_data[i][m]*svm_data[j][m]+1);
	                //if(i==1 && j==83)
	                //cout<<m<<"\t"<<svm_data[i][m]<<"\t"<<svm_data[j][m]<<"\tker "<<kernal[i][j]<<endl;
	            }
	            kernal[k][i][j]=Math.pow(kernal[k][i][j],len);//0.1-4之间方差值
	         //   if(i==1 && j==83)
	          //      cout<<len<<"\t"<<kernal[i][j]<<endl;
	        }
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
		kernal_predict=new double[outputNum][filelen];
		 for(int k=0;k<outputNum;k++)
		    {
		for(int j=0;j<filelen;j++)
        {
	    	for(int m=0;m<dimensionality;m++)
            {
	    		kernal_predict[k][j]+=(inputData[m]*svm_data[j][m]+1);
            }
	    	kernal_predict[k][j]=Math.pow(kernal_predict[k][j],len);//0.1-4之间方差值
        }
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
			kernal=new double[outputNum][filelen][filelen];
			 for(int k=0;k<outputNum;k++)
			    {
			for(int i=0;i<filelen;i++)
			{
				for(int j=0;j<filelen;j++)
				{
					for(int m=0;m<dimensionality;m++)
					{
						kernal[k][i][j]+=svm_data[i][m]*svm_data[j][m];
					}
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
			kernal_predict=new double[outputNum][filelen];
			 for(int k=0;k<outputNum;k++)
			    {
			for(int j=0;j<filelen;j++)
			{
				for(int m=0;m<dimensionality;m++)
				{
					kernal_predict[k][j]+=inputData[m]*svm_data[j][m];
				}
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
		kernal=new double[outputNum][filelen][filelen];
		 for(int k=0;k<outputNum;k++)
		    {
		for(int i=0;i<filelen;i++)
		{
			for(int j=0;j<filelen;j++)
			{
				for(int m=0;m<dimensionality;m++)
				{
					kernal[k][i][j]+=svm_data[i][m]*svm_data[j][m];
				}
				kernal[k][i][j]=Math.pow(kernal[k][i][j]+coef,len);
			}
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
		kernal_predict=new double[outputNum][filelen];
		 for(int k=0;k<outputNum;k++)
		    {
		for(int j=0;j<filelen;j++)
		{
			for(int m=0;m<dimensionality;m++)
			{
				kernal_predict[k][j]+=inputData[m]*svm_data[j][m];
			}
			kernal_predict[k][j]=Math.pow(kernal_predict[k][j]+coef,len);
		}
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
		kernal=new double[outputNum][filelen][filelen];
		double[] kernal1=new double[filelen];
		for(int i=0;i<filelen;i++)
		{
			for(int m=0;m<dimensionality;m++)
			{
				kernal1[i]+=svm_data[i][m]*svm_data[i][m];
			}
		}
		 for(int k=0;k<outputNum;k++)
		    {
		for(int i=0;i<filelen;i++)
		{
			for(int j=0;j<filelen;j++)
			{
				for(int m=0;m<dimensionality;m++)
				{
					kernal[k][i][j]+=svm_data[i][m]*svm_data[j][m];
				}
				kernal[k][i][j]=-(kernal1[i]+kernal1[j]-2*kernal[k][i][j]);
			}
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
		kernal_predict=new double[outputNum][filelen];
		double[] predict=new double[filelen];
		double temp=0.0;
		for(int m=0;m<dimensionality;m++)
		{
			temp+=inputData[m]*inputData[m];
		}
		for(int i=0;i<filelen;i++)
		{
			for(int m=0;m<dimensionality;m++)
			{
				predict[i]+=svm_data[i][m]*svm_data[i][m];
			}
		}
		 for(int k=0;k<outputNum;k++)
		    {
		for(int j=0;j<filelen;j++)
		{
			for(int m=0;m<dimensionality;m++)
			{
				kernal_predict[k][j]+=inputData[m]*svm_data[j][m];
			}
			kernal_predict[k][j]=-(temp+predict[j]-2*kernal_predict[k][j]);
		}
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
		kernal=new double[outputNum][filelen][filelen];
		 for(int k=0;k<outputNum;k++)
		    {
				for(int i=0;i<filelen;i++)
				{
					for(int j=0;j<filelen;j++)
					{
						for(int m=0;m<dimensionality;m++)
						{
							kernal[k][i][j]+=svm_data[i][m]*svm_data[j][m];
						}
						kernal[k][i][j]=Math.tanh(kernal[k][i][j]+coef);
					}
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
		kernal_predict=new double[outputNum][filelen];
		 for(int k=0;k<outputNum;k++)
		    {
		for(int j=0;j<filelen;j++)
		{
			for(int m=0;m<dimensionality;m++)
			{
				kernal_predict[k][j]+=inputData[m]*svm_data[j][m];
			}
			kernal_predict[k][j]=Math.tanh(kernal_predict[k][j]+coef);
		}
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
		kernal=new double[outputNum][filelen][filelen];
		 for(int k=0;k<outputNum;k++)
		    {
		for(int i=0;i<filelen;i++)
		{
			for(int j=0;j<filelen;j++)
			{
				kernal[k][i][j]=svm_data[i][(int)svm_data[j][0]];
			}
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
		kernal_predict=new double[outputNum][filelen];
		 for(int k=0;k<outputNum;k++)
		    {
		for(int j=0;j<filelen;j++)
		{
			kernal_predict[k][j]=inputData[(int)svm_data[j][0]];
		}
		    }
	}
	
	//计算添加核的wx值
	/**
	 * 计算添加核的wx值
	 */
	public void computeWxValue()
	{
		sum_val=new double[outputNum][filelen];
		 for(int k=0;k<outputNum;k++)
		    {
	    for(int i=0;i<filelen;i++)
	    {
	        sum_val[k][i]=0.0;
	    }
	    for(int i=0;i<filelen;i++)
	    {
	        for(int j=0;j<filelen;j++)
	        {
	            sum_val[k][i]+=alpha[k][j]*this.outputNormalValue.get(j)[k]*kernal[k][j][i];
	        }
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
		        if(mm>500)
		        {
		        	break;
		        }
		        for(int k=0;k<outputNum;k++)
			    {
			        //初始选点
			        int n1=0,n2=1;
			        //kkt条件
			        //***********查看是否正确这个条件
			        while(n1<filelen-1)
			        {
			            //cout<<"sum:"<<sum_val[n1]<<endl;
			            //cout<<sum_val[n1]+b<<endl;
			            //cout<<"vale:"<<svm_data[n1][length-1]*(sum_val[n1]+b)<<endl;
			            //如果在下属条件中则为需要修改的alpha值
			            if((Double.compare(outputNormalValue.get(n1)[k]*(sum_val[k][n1]+b[k]),1.0)==0) && (alpha[k][n1]>=c_wigth|| alpha[k][n1]<0.0))
			                break;
			            if(outputNormalValue.get(n1)[k]*(sum_val[k][n1]+b[k])>1.0 && Double.compare(alpha[k][n1],0.0)!=0)
			                break;
			            if(outputNormalValue.get(n1)[k]*(sum_val[k][n1]+b[k])<=1.0 && Double.compare(alpha[k][n1],c_wigth)!=0)
			                break;
			            //否则递n1
			            n1++;
			        }
			        System.out.println("值:"+outputNormalValue.get(n1)[k]+"\t"+(sum_val[k][n1]+b[k]));
			        if(n1==filelen)
			        {
			            System.out.println("全部不满足kkt条件");
			            break;
			        }
			        //n2通过计算与n1具有最大误差的位置
			        double E1=0;
			        double E2=0;
			        double max=0;//更改误差
			        E1=sum_val[k][n1]+b[k]-outputNormalValue.get(n1)[k];//计算值与实际值之差
			        
			        //计算n1的误差
			        for(int i=n1;i<filelen;i++)
			        {
			            double tempsum=sum_val[k][i]+b[k]-outputNormalValue.get(i)[k];
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
			        double a1old=alpha[k][n1];
			        double a2old=alpha[k][n2];
			        System.out.println("n1:"+n1+"\tn2:"+n2);
			        double kk=kernal[k][n1][n1]+kernal[k][n2][n2]-2*kernal[k][n1][n2];
			        double a2new=a2old+outputNormalValue.get(n2)[k]*(E1-E2)/kk;
			        //从旧的值替换成新值。添加上detle差异值变量
			        double s=outputNormalValue.get(n1)[k]*outputNormalValue.get(n2)[k];
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
			        alpha[k][n1]=a1new;
			        alpha[k][n2]=a2new;//替换
			        //重新计算wt值
			        for(int i=0;i<filelen;i++)
			        {
			            
			            if(i==n1 || i==n2)
			            {
			            	sum_val[k][i]=0.0;
				            for(int j=0;j<filelen;j++)
				            {
				                //sum_val[i]+=alpha[j]*svm_data[j][length-1]*kernal[i][j];
				            	sum_val[k][i]+=alpha[k][j]*outputNormalValue.get(j)[k]*kernal[k][j][i];
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
			                tempsn+=outputNormalValue.get(i)[k]*outputNormalValue.get(j)[k]*alpha[k][i]*alpha[k][j]*kernal[k][i][j];
			            }
			            wnew+=alpha[k][i];
			        }
			        wnew=wnew-0.5*tempsn;//max(q(x))值
			        //更新b
			        int support =1;//支持向量坐标初始化
			        while(Math.abs(alpha[k][support])<1e-4 && support<filelen-1)
			        {
			            support++;//找一个支持向量
			        }
			        //b=1.0/svm_data[support][length-1]-sum_val[support];//1/y-wx计算b值y=wx+b->是否有错
			        double b1=b[k]-E1-outputNormalValue.get(n1)[k]*(a1new-a1old)*kernal[k][n1][n1]-
			        outputNormalValue.get(n2)[k]*(a2new-a2old)*kernal[k][n2][n1];
			        double b2=b[k]-E2-outputNormalValue.get(n1)[k]*(a1new-a1old)*kernal[k][n1][n2]-
			        outputNormalValue.get(n2)[k]*(a2new-a2old)*kernal[k][n2][n2];
			        if(0<a1new&& a1new<c_wigth)
			        {
			            b[k]=b1;
			        }
			        else
			        {
			            if(0<a2new && a2new<c_wigth)
			            {
			                b[k]=b2;
			            }
			            else
			            {
			                b[k]=(b1+b2)/2.0;
			            }
			        }
			       // System.out.println("new:"+wnew+":"+wold);
			       // System.out.println("误差:"+Math.abs(wnew/wold-1)+"\tb:"+b[k]+"\t"+sd_error);
			        if(Double.compare(wold,0.0)==0)
			        {
			        	continue;
			        }
			        if(Math.abs(wnew/wold-1)<=sd_error && !Double.isInfinite(Math.abs(wnew/wold-1)))//如果新变量和就变量之间的差值足够小则跳出
			        {  
			            System.out.println("结束并收敛");
			            continue;
			        }
			    }
		    }
	    }
	    else
	    {
	        //cout<<"计算错误"<<endl;
	    }
	}
	
	
	
	/**
	 * 
	 * @param input
	 * @param count 如果为多项式数
	 * @return
	 */
	public String predict(double[] input,int count)
	{
		String ll="";
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
			System.exit(1);
		}

		 double[] o_comp=new double[outputNum];
		 for(int k=0;k<outputNum;k++)
		 {
			 double sum=0.0;
			 for(int i=0;i<filelen;i++)
			 {
				 sum+=alpha[k][i]*this.outputNormalValue.get(i)[k]*kernal_predict[k][i];
			 }
			 sum+=b[k];
			 //判断状态
			 o_comp[k]=sum;
		 }
		 boolean flag=false;
		    for(int i=0;i<outputStandardString.length;i++)
		    {

	    		int lk=0;
		    	for(int l=0;l<this.outputStandardValue.get(i).length;l++)
		    	{
		    		//http://www.ujmp.org/
		
			    	if(o_comp[l]>c_wigth && outputStandardValue.get(i)[l]==1)
			    	{
			    		lk++;
			    	}
			    	else if(o_comp[l]<-c_wigth && outputStandardValue.get(i)[l]==-1)
			    	{
			    		lk++;
			    	}
			    	else
			    	{
			    		continue;
			    	}
		    	}
			    if(lk==this.outputStandardValue.get(i).length)
			    {
			    	flag=true;
			    	ll=this.outputStandardString[i];
			    	break;
			    }
		    }
	    	if(flag==false)
	    	{
	    		ll="-2";
	    	
	    	}
	    	return ll;
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
	 * 将输出数据转化到Standard的list表中的二进制数
	 * @param data
	 */
	public void changeToBinal(LinkedList<String[]> data)
	{
		//只定在最后一列为分类
		for(int j=0;j<data.get(0).length;j++)
		{
			System.out.println("标准化列:"+j);
			TreeSet<String> str=new TreeSet<String>();
			for(int i=0;i<data.size();i++)
			{
			//	System.out.println(i);
				//遍历每一列将列编码并存储对应的数量
				//取出唯一的string
			//	System.out.println(data.get(i)[j]);
				str.add(data.get(i)[j]);
			}
			System.out.println("读取统计完成");
			//将对应值转化为标准结构
			String[] lnmm=new String[str.size()];
			int pp=0;
			for(String sn:str)
			{
				lnmm[pp]=sn;
				pp++;
			}
			pp=0;
			outputStandardString=lnmm.clone();
			lnmm=null;
			//将对应信息转化为01编码
			if(str.size()==2)
			{
				int[] ll3=new int[1];
				LinkedList<int[]> lnm=new LinkedList<int[]>();
				ll3[0]=-1;
				outputStandardValue.add(ll3);
				ll3=new int[1];
				ll3[0]=1;
				outputStandardValue.add(ll3);
				//System.out.println("值");
				//System.out.println(lnmm[0]+"\t"+lnmm[1]);
				//System.out.println(0+"\t"+1);
			}
			else if(str.size()>2)
			{
				//将信息对应编译
				//计算需要用到的长度
				//System.out.println("长度:"+str.size());
				int count=0;
				double vale=str.size()*1.0;
				while(true)
				{
					vale=vale/2;
					count++;
					System.out.println("二进制编码长度："+count+"\tll:"+vale);
					if(vale<=1.0)
					{
						break;
					}
				}
				for(int ii=0;ii<str.size();ii++)
				{
					//System.out.println("执行内:"+ii+"\tcount:"+count);
					int[] te=new int[count];
					//用二进制替换
					//count=0;
					//BigInteger src2 = new BigInteger(Integer.toString(ii));
					//String st=src2.toString();
					String st=Integer.toString(ii,2);
					//System.out.println("二进制:"+st);
					int len=st.length();
					int jjj=0;
					for(int jj=0;jj<count-len;jj++)
					{
						te[jjj]=-1;
						System.out.print(te[jjj]+"\t");
						jjj++;
					}
					for(int jj=0;jj<len;jj++)
					{
						int temp=Integer.parseInt(Character.toString(st.charAt(jj)));
						if(temp==0)
						{
							te[jjj]=-1;
						}
						else
						{
							te[jjj]=1;
						}
						System.out.print(te[jjj]+"\t");
						jjj++;
					}
					System.out.println();
					outputStandardValue.add(te.clone());
				}
			}
			else
			{
				System.out.println("输出文件中有某一列为唯一值，不符合要求");
				System.exit(1);
			}
		}
	}
	/**
	 * 将输出信息转化到二进制数中
	 */
	public void changeOutputToBinal(LinkedList<String[]> data)
	{
		//只适合一个输出列形式
		for(int i=0;i<data.size();i++)
		{
			String str=data.get(i)[0];
			for(int j=0;j<this.outputStandardString.length;j++)
			{
				if(str.equals(outputStandardString[j]))
				{
					System.out.print(i+"转二进制:"+str+"\t");
					for(int m=0;m<this.outputStandardValue.get(j).length;m++)
					{
						System.out.print(outputStandardValue.get(j)[m]);
					}
					System.out.println();
					this.outputNormalString.add(str);
					this.outputNormalValue.add(this.outputStandardValue.get(j));
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param data 为01编码
	 * @return
	 */
	public String getPredictString(int[] data)
	{
		for(int l=0;l<this.outputStandardValue.size();l++)
		{
			boolean flag=true;
			for(int i=0;i<outputStandardValue.get(l).length;i++)
			{
				//System.out.println(outputStandardValue.get(l)[i]+"\t"+data[i]);
				if(outputStandardValue.get(l)[i]==data[i])
				{
					
				}else
				{
					flag=false;
					break;
				}
			}
			if(flag==false)
			{
				//System.out.println("跳出");
				continue;
			}
			else
			{
				return this.outputNormalString.get(l);
			}
		}
		
		return "-1";//错误
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
		
		Normal n3=new Normal(3,1);
		Matrix m3=n3.random(50,2,2);
		
		m1.addMatrix(m3);
		
		 Normal n4=new Normal(-3,0.3);
		 Matrix m4=n4.random(100,1,3);
		 Normal n42=new Normal(3,0.4);
		 Matrix m42=n42.random(100,1);
		 m42.addColMatrix(m4);
		 m1.addMatrix(m42);
		 
		 
		 Normal n5=new Normal(7,0.3);
		 Matrix m5=n5.random(100,1,4);
		 Normal n52=new Normal(-7,0.4);
		 Matrix m52=n52.random(100,1);
		 m42.addColMatrix(m5);
		 m1.addMatrix(m52);
		
		double[][] data=m1.getData();
	
	//	System.out.println("m1:"+m1.getRowNum());
		
		//MatrixWriteTable.WriteToTxt("e:\\ml_test_work\\rtest1_bp_test.txt", matrix);
	   // double[][] data=MatrixReadSource.ReadTodouble("e:\\ml_test_work\\rtest1_bp_test.txt");
		LinkedList<double[]> datainput=new LinkedList<double[]>();//输入
		LinkedList<String[]> dataoutput=new LinkedList<String[]>();//输出
		for(int i=0;i<data.length;i++)
		{
			double[] da=new double[data[i].length-1];
			for(int j=0;j<data[i].length-1;j++)
			{
				da[j]=data[i][j];
			}
			String[] st=new String[1];
			st[0]=Integer.toString((int)(data[i][data[i].length-1]));
		//	System.out.println(st[0]);
			datainput.add(da);
			dataoutput.add(st);
		}

		
		
	    ////标准化
	    int filelen=data.length;
	    int length=data[0].length;
	    double sd=0.00001;//设定分类精度
	    //修改不同的C和alp初始值 可以获取不同的效果
	    double C=0.1;//对损失权重的c赋值
	    double wold=0,wnew=0;//更新前w(a)，更新后w（a）
	    double alp=0.1;//初始化apha
	    double std=1;//std=1//kernal方差值//与整体方差有关
	    //将输出更改为1,-1；当前输入为10
	        std=0.3;
		    SvmBaseN svm=new SvmBaseN(datainput,dataoutput,sd,std,C,alp);//初始化svm类
		    //svm.kernalPolynomialCompute(2.0);//多项式核
		    svm.train("Gaussian");
		    
		    
		    
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
				String predict=svm.predict(svm.svm_data[i], 1);
				cluster.add(Integer.parseInt(predict));
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
						String predict=svm.predict(ll, 1);
						cluster1.add(Integer.parseInt(predict));
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
						String predict=svm.outputNormalString.get(i);
							cluster3.add(Integer.parseInt(predict));

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

