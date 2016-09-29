package ps.hell.ml.statistics.base;

import ps.hell.math.base.MathBase;


/**
 * 统计分析基本量
 * sst=sse+ssr
 * @author root
 *
 */
public class StatisticsAnalysis {

	/**
	 * 离差平方和
	 * @param yi 输入样本
	 * @return
	 */
	public double  sst(double[] yi){
		double  ymean=MathBase.mean(yi);
		return MathBase.sqrtMinus(yi, ymean);
	}
	/**
	 * 回归平方和
	 * @param yf 预测值
	 * @param ymean 样本均值
	 * @return 
	 */
	public double ssr(double[] yf,double ymean){
		return MathBase.sqrtMinus(yf, ymean);
	}
	/**
	 * 残差平方和
	 * @param yi
	 * @param yf
	 * @return
	 */
	public double sse(double[] yi,double[] yf){
		return MathBase.sqrtMinus(yi,yf);
	}
	/**
	 * (the model's R-squared value)
	 * 获取 r^2值
	 * @param sse
	 * @param sst
	 * @return
	 */
	public double r2(double sse,double sst){
		return 1-(sse/sst);
	}
	
	
	  /**
	   * Returns the adjusted R-squared value for a linear regression model. This
	   * works for either a simple or a multiple linear regression model.
	 	* 自适应 r^2
	   * @param rsq (the model's R-squared value)
	   * @param n (the number of instances in the data)样本数量
	   * @param k (the number of coefficients in the model: k>=2) 纬度
	   * @return the adjusted R squared value
	   */
	  public static double adjR2(double rsq, int n, int k) {
	    if (n < 1 || k < 2 || n == k) {
	      System.err.println("Cannot calculate Adjusted R^2.");
	      return Double.NaN;
	    }
	    return 1 - ((1 - rsq) * (n - 1) / (n - k));
	  }
	  
	  
	  /**
	   * Returns the F-statistic for a linear regression model.
	   * 计算f统计量
	   * @param rsq (the model's R-squared value)
	   * @param n (the number of instances in the data)
	   * @param k (the number of coefficients in the model: k>=2)
	   * @return F-statistic
	   */
	  public static double fStat(double rsq, int n, int k) {
	    if (n < 1 || k < 2 || n == k) {
	      System.err.println("Cannot calculate F-stat.");
	      return Double.NaN;
	    }

	    double numerator = rsq / (k - 1);
	    double denominator = (1 - rsq) / (n - k);
	    return numerator / denominator;
	  }
	  
	  
	  /**
	   * Returns an array of the t-statistic of each coefficient in a multiple
	   * linear regression model.
	    * 计算t统计量
	   * @param coef (array holding the value of each coefficient)
	   * @param stderror (array holding each coefficient's standard error)标准差
	   * @param k (number of coefficients, includes constant)纬度
	   * @return array of t-statistics of coefficients
	   */
	  public static double[] tStats(double[] coef, double[] stderror, int k) {
	    double[] result = new double[k];
	    for (int i = 0; i < k; i++) {
	      result[i] = coef[i] / stderror[i];
	    }
	    return result;
	  }
	  

}
