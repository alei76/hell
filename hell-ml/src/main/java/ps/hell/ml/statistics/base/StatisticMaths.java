/*
 * This software is a cooperative product of The MathWorks and the National
 * Institute of Standards and Technology (NIST) which has been released to the
 * public domain. Neither The MathWorks nor NIST assumes any responsibility
 * whatsoever for its use by other parties, and makes no guarantees, expressed
 * or implied, about its quality, reliability, or any other characteristic.
 */

/*
 * Maths.java
 * Copyright (C) 1999 The Mathworks and NIST
 *
 */

package ps.hell.ml.statistics.base;


import ps.hell.math.base.MathBase;

import java.util.Random;

/**
 * 使用 参考 Statistic
 * 统计基础量
 * Utility class.
 * <p/>
 * Adapted from the <a href="http://math.nist.gov/javanumerics/jama/" target="_blank">JAMA</a> package.
 *
 * @author The Mathworks and NIST 
 * @author Fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 5953 $
 */
public class StatisticMaths {
  
  /** The constant 1 / sqrt(2 pi) */
  public static final double PSI = 0.3989422804014327028632;

  /** The constant - log( sqrt(2 pi) ) */
  public static final double logPSI = -0.9189385332046726695410;

  /** Distribution type: undefined */
  public static final int undefinedDistribution = 0;

  /** Distribution type: noraml */
  public static final int normalDistribution = 1;

  /** Distribution type: chi-squared */
  public static final int chisqDistribution = 2;

  /** 
   * sqrt(a^2 + b^2) without under/overflow. 
   */
  public static double hypot(double a, double b) {
    double r;
    if (Math.abs(a) > Math.abs(b)) {
      r = b/a;
      r = Math.abs(a)*Math.sqrt(1+r*r);
    } else if (b != 0) {
      r = a/b;
      r = Math.abs(b)*Math.sqrt(1+r*r);
    } else {
      r = 0.0;
    }
    return r;
  }

  /**
   *  Returns the square of a value
   *  @param x 
   *  @return the square
   */
  public static double  square( double x ) 
  {
    return x * x;
  }

  /* methods for normal distribution */

  /**
   *  Returns the cumulative probability of the standard normal.
   *  @param x the quantile
   */
  public static double  pnorm( double x ) 
  {
    return Statistics.normalProbability( x );
  }
    
  /** 
   *  Returns the cumulative probability of a normal distribution.
   *  @param x the quantile
   *  @param mean the mean of the normal distribution
   *  @param sd the standard deviation of the normal distribution.
   */
  public static double  pnorm( double x, double mean, double sd ) 
  {
    if( sd <= 0.0 )
      throw new IllegalArgumentException("standard deviation <= 0.0");
    return pnorm( (x - mean) / sd );
  }
    
  /** 
   *  Returns the cumulative probability of a set of normal distributions
   *  with different means.
   *  @param x the vector of quantiles
   *  @param mean the means of the normal distributions
   *  @param sd the standard deviation of the normal distribution.
   *  @return the cumulative probability */
  public static double[] pnorm( double x, double mean[],double sd ) 
  {
   double[] val=new double[mean.length];
   for(int i=0;i<mean.length;i++){
	   val[i]=pnorm(x,mean[i],sd);
   }
    return val;
  }
    
  /** Returns the density of the standard normal.
   *  @param x the quantile
   *  @return the density
   */
  public static double  dnorm( double x ) 
  {
    return Math.exp( - x * x / 2. ) * PSI;
  }
    
  /** Returns the density value of a standard normal.
   *  @param x the quantile
   *  @param mean the mean of the normal distribution
   *  @param sd the standard deviation of the normal distribution.
   *  @return the density */
  public static double  dnorm( double x, double mean, double sd ) 
  {
    if( sd <= 0.0 )
      throw new IllegalArgumentException("standard deviation <= 0.0");
    return dnorm( (x - mean) / sd );
  }
    
  /** Returns the density values of a set of normal distributions with
   *  different means.
   *  @param x the quantile
   *  @param mean the means of the normal distributions
   *  @param sd the standard deviation of the normal distribution.
   * @return the density */
  public static double[]  dnorm( double x, double[] mean, 
                                     double sd ) 
  {
    double[] den = new double[mean.length];
        
    for( int i = 0; i < mean.length; i++ ) {
      den[i]=dnorm(x, mean[i], sd);
    }
    return den;
  }
    
  /** Returns the log-density of the standard normal.
   *  @param x the quantile
   *  @return the density
   */
  public static double  dnormLog( double x ) 
  {
    return logPSI - x * x / 2.;
  }
    
  /** Returns the log-density value of a standard normal.
   *  @param x the quantile
   *  @param mean the mean of the normal distribution
   *  @param sd the standard deviation of the normal distribution.
   *  @return the density */
  public static double  dnormLog( double x, double mean, double sd ) {
    if( sd <= 0.0 )
      throw new IllegalArgumentException("standard deviation <= 0.0");
    return - Math.log(sd) + dnormLog( (x - mean) / sd );
  }
    
  /** Returns the log-density values of a set of normal distributions with
   *  different means.
   *  @param x the quantile
   *  @param mean the means of the normal distributions
   *  @param sd the standard deviation of the normal distribution.
   * @return the density */
  public static double[]  dnormLog( double x, double[] mean, 
                                        double sd ) 
  {
    double[] denLog = new double[mean.length];
        
    for( int i = 0; i < mean.length; i++ ) {
      denLog[i]=dnormLog(x, mean[i], sd);
    }
    return denLog;
  }
    
  /** 
   *  Generates a sample of a normal distribution.
   *  @param n the size of the sample
   *  @param mean the mean of the normal distribution
   *  @param sd the standard deviation of the normal distribution.
   *  @param random the random stream
   *  @return the sample
   */
  public static double[] rnorm( int n, double mean, double sd, 
                                    Random random ) 
  {
    if( sd < 0.0)
      throw new IllegalArgumentException("standard deviation < 0.0");
        
    if( sd == 0.0 )
    	{
    		return MathBase.number(n,mean);
    	}
    double[] v = new double[n];
    for( int i = 0; i < n; i++ ) {
      v[i]=(random.nextGaussian() + mean) / sd ;}
    return v;
  }
    
  /* methods for Chi-square distribution */

  /** Returns the cumulative probability of the Chi-squared distribution
   *  @param x the quantile
   */
  public static double  pchisq( double x ) 
  {
    double xh = Math.sqrt( x );
    return pnorm( xh ) - pnorm( -xh );
  }
    
  /** Returns the cumulative probability of the noncentral Chi-squared
   *  distribution.
   *  @param x the quantile
   *  @param ncp the noncentral parameter */
  public static double  pchisq( double x, double ncp ) 
  {
    double mean = Math.sqrt( ncp );
    double xh = Math.sqrt( x );
    return pnorm( xh - mean ) - pnorm( -xh - mean );
  }
    
  /** Returns the cumulative probability of a set of noncentral Chi-squared
   *  distributions.
   *  @param x the quantile
   *  @param ncp the noncentral parameters */
  public static double[]  pchisq( double x, double[] ncp )
  {
    int n = ncp.length;
    double[] p = new double[ n];
    double mean;
    double xh = Math.sqrt( x );
        
    for( int i = 0; i < n; i++ ) {
      mean = Math.sqrt( ncp[i] );
      p[i]= pnorm( xh - mean ) - pnorm( -xh - mean ) ;
    }
    return p;
  }
    
  /** Returns the density of the Chi-squared distribution.
   *  @param x the quantile
   *  @return the density
   */
  public static double  dchisq( double x ) 
  {
    if( x == 0.0 ) return Double.POSITIVE_INFINITY;
    double xh = Math.sqrt( x );
    return dnorm( xh ) / xh;
  }
    
  /** Returns the density of the noncentral Chi-squared distribution.
   *  @param x the quantile
   *  @param ncp the noncentral parameter
   */
  public static double  dchisq( double x, double ncp ) 
  {
    if( ncp == 0.0 ) return dchisq( x );
    double xh = Math.sqrt( x );
    double mean = Math.sqrt( ncp );
    return (dnorm( xh - mean ) + dnorm( -xh - mean)) / (2 * xh);
  }
    
  /** Returns the density of the noncentral Chi-squared distribution.
   *  @param x the quantile
   *  @param ncp the noncentral parameters 
   */
  public static double[]  dchisq( double x, double[] ncp )
  {
    int n = ncp.length;
    double[] d = new double[n];
    double xh = Math.sqrt( x );
    double mean;
    for( int i = 0; i < n; i++ ) {
      mean = Math.sqrt( ncp[i]);
      if( ncp[i] == 0.0 ) d[ i]= dchisq( x ) ;
      else d[i]= (dnorm( xh - mean ) + dnorm( -xh - mean)) / 
                  (2 * xh) ;
    }
    return d;
  }
    
  /** Returns the log-density of the noncentral Chi-square distribution.
   *  @param x the quantile
   *  @return the density
   */
  public static double  dchisqLog( double x ) 
  {
    if( x == 0.0) return Double.POSITIVE_INFINITY;
    double xh = Math.sqrt( x );
    return dnormLog( xh ) - Math.log( xh );
  }
    
  /** Returns the log-density value of a noncentral Chi-square distribution.
   *  @param x the quantile
   *  @param ncp the noncentral parameter
   *  @return the density */
  public static double  dchisqLog( double x, double ncp ) {
    if( ncp == 0.0 ) return dchisqLog( x );
    double xh = Math.sqrt( x );
    double mean = Math.sqrt( ncp );
    return Math.log( dnorm( xh - mean ) + dnorm( -xh - mean) ) - 
    Math.log(2 * xh);
  }
    
  /** Returns the log-density of a set of noncentral Chi-squared
   *  distributions.
   *  @param x the quantile
   *  @param ncp the noncentral parameters */
  public static double[]  dchisqLog( double x, double[] ncp )
  {
    double[] dLog = new double[ ncp.length];
    double xh = Math.sqrt( x );
    double mean;
        
    for( int i = 0; i < ncp.length; i++ ) {
      mean = Math.sqrt( ncp[i] );
      if( ncp[i] == 0.0 ) dLog[ i]= dchisqLog( x ) ;
      else dLog[i]= Math.log( dnorm( xh - mean ) + dnorm( -xh - mean) ) - 
                     Math.log(2 * xh);
    }
    return dLog;
  }
    
  /** 
   *  Generates a sample of a Chi-square distribution.
   *  @param n the size of the sample
   *  @param ncp the noncentral parameter
   *  @param random the random stream
   *  @return the sample
   */
  public static double[]  rchisq( int n, double ncp, Random random ) 
  {
    double[] v = new double[n];
    double mean = Math.sqrt( ncp );
    double x;
    for( int i = 0; i < n; i++ ) {
      x = random.nextGaussian() + mean;
      v[i]=x * x;
    }
    return v;
  }
  
  /**
   * kl散度
   * Returns the Kullback-Leibler divergence of the second
   * specified multinomial relative to the first.
   *
   * <p>The K-L divergence of a multinomial <code>q</code> relative
   * to a multinomial <code>p</code>, both with <code>n</code>
   * outcomes, is:
   *
   * <blockquote><pre>
   * D<sub><sub>KL</sub></sub>(p||q)
   * = <big><big><big>&Sigma;</big></big></big><sub><sub>i &lt; n</sub></sub>  p(i) log<sub>2</sub> (p(i) / q(i))</pre></blockquote>
   *
   * The value is guaranteed to be non-negative, and will be 0.0
   * only if the two distributions are identicial.  If any outcome
   * has zero probability in <code>q</code> and non-zero probability
   * in <code>p</code>, the result is infinite.
   *
   * <p>KL divergence is not symmetric.  That is, there are
   * <code>p</code> and <code>q</code> such that
   * <code>D<sub><sub>KL</sub></sub>(p||q) !=
   * D<sub><sub>KL</sub></sub>(q||p)</code>.  See {@link
   * symmetrizedKlDivergence(double[],double[])} and {@link
   * jsDivergence(double[],double[])} for symmetric variants.
   *
   * <p>KL divergence is equivalent to conditional entropy, although
   * it is written in the opposite order.  If <code>H(p,q)</code> is
   * the joint entropy of the distributions <code>p</code> and
   * <code>q</code>, and <code>H(p)</code> is the entropy of
   * <code>p</code>, then:
   *
   * <blockquote><pre>
   * D<sub><sub>KL</sub></sub>(p||q) = H(p,q) - H(p)</pre></blockquote>
   *
   * @param p First multinomial distribution.
   * @param q Second multinomial distribution.
   * @throws IllegalArgumentException If the distributions are not
   * the same length or have entries less than zero or greater than
   * 1.
   */
  public static double klDivergence(double[] p, double[] q) {
      verifyDivergenceArgs(p,q);
      double divergence = 0.0;
      int len = p.length;
      for (int i = 0; i < len; ++i) {
          if (p[i] > 0.0 && p[i] != q[i])
              divergence += p[i] * MathBase.log2(p[i] / q[i]);
      }
      return divergence;
  }

  static void verifyDivergenceArgs(double[] p, double[] q) {
      if (p.length != q.length) {
          String msg = "Input distributions must have same length."
              + " Found p.length=" + p.length
              + " q.length=" + q.length;
          throw new IllegalArgumentException(msg);
      }
      int len = p.length;
      for (int i = 0; i < len; ++i) {
          if (p[i] < 0.0
              || p[i] > 1.0
              || Double.isNaN(p[i])
              || Double.isInfinite(p[i])) {
              String msg = "p[i] must be between 0.0 and 1.0 inclusive."
                  + " found p[" + i + "]=" + p[i];
              throw new IllegalArgumentException(msg);
          }
          if (q[i] < 0.0
              || q[i] > 1.0
              || Double.isNaN(q[i])
              || Double.isInfinite(q[i])) {
              String msg = "q[i] must be between 0.0 and 1.0 inclusive."
                  + " found q[" + i + "] =" + q[i];
              throw new IllegalArgumentException(msg);
          }
      }
  }
}
