package ps.hell.ml.optimization.adadelta;

import ps.hell.math.base.MathBase;

/**
 * 
 * @author Administrator
 *adadelta优化方法
 */
public class Adadelta {
	//假设在时间t时刻求的的梯度是grad(t)。
   // g(t) = (1-p)*grad(t)^2 + p*g(t-1)  
   // delta = sqrt(s(t-1) + e)/sqrt(g(t) + e)*(-grad(t))  
  //  s(t) = (1-p)*delta^2 + p*s(t-1)  
   // W = W(t-1)+delta
	/**
	 * 具体数值
	 */
	public double[] data=null;
	/**
	 * 梯度
	 */
	public double[] grad=null;
	public double[] g=null;
	public double[] w=null;
	public double rho=0.95;
	public double epsilon=1E-6;
	
	public Adadelta(double[] timeData,double[] p){
		this.data=timeData;
		this.g=new double[p.length];
		this.w=new double[p.length];
	}
	public void exec(){
		train();
	}
	
	public void train(){
		getGrad();
		double delta=0;
		for(int i=1;i<data.length;i++){
			g[i]=(1-rho)*grad[i]*grad[i]+rho*g[i-1];
			delta=Math.sqrt(g[i-1]*g[i-1]+epsilon)/Math.sqrt(g[i]*g[i]+epsilon)*(-grad[i]);
			g[i]=(1-rho)*delta*delta+rho*g[i-1];
			w[i]=w[i-1]+delta;
		}
	}
	
	/**
	 * 获取梯度
	 */
	public void getGrad(){
		this.grad=MathBase.gradient(data,true);
	}
	
	public static void main(String[] args) {
		
	}
}

