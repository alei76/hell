/*
 * This software is a cooperative product of The MathWorks and the National
 * Institute of Standards and Technology (NIST) which has been released to the
 * public domain. Neither The MathWorks nor NIST assumes any responsibility
 * whatsoever for its use by other parties, and makes no guarantees, expressed
 * or implied, about its quality, reliability, or any other characteristic.
 */

/*
 * LinearRegression.java
 * Copyright (C) 2005 University of Waikato, Hamilton, New Zealand
 *
 */

package ps.hell.ml.statistics.regression;

import ps.hell.math.base.matrix.Matrix;

import java.util.Arrays;


/**
 * 线性回归中包含 岭回归
 * Class for performing (ridged) linear regression using Tikhonov
 * regularization.
 * 
 * @author Fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 9768 $
 */
 
public class LinearRegression{

  /** the coefficients */
  protected double[] m_Coefficients = null;

  /**
   * Performs a (ridged) linear regression.
   *
   * @param a the matrix to perform the regression on
   * @param y the dependent variable vector
   * @param ridge the ridge parameter
   * @throws IllegalArgumentException if not successful
   */
  public LinearRegression(Matrix a, Matrix y, double ridge) {
    calculate(a, y, ridge);
  }

  /**
   * Performs a weighted (ridged) linear regression. 
   *
   * @param a the matrix to perform the regression on
   * @param y the dependent variable vector
   * @param w the array of data point weights 使用权重
   * @param ridge the ridge parameter 岭回归 参数
   * @throws IllegalArgumentException if the wrong number of weights were
   * provided.
   */
  public LinearRegression(Matrix a, Matrix y, double[] w, double ridge) {

    if (w.length != a.colNum)
      throw new IllegalArgumentException("Incorrect number of weights provided");
    Matrix weightedThis = new Matrix(
                              a.rowNum, a.colNum);
    Matrix weightedDep = new Matrix(a.rowNum, 1);
    for (int i = 0; i < w.length; i++) {
      double sqrt_weight = Math.sqrt(w[i]);
      for (int j = 0; j < a.colNum; j++)
        weightedThis.data[i][j]= a.data[i][ j] * sqrt_weight;
      weightedDep.data[i][0]= y.data[i][0] * sqrt_weight;
    }

    calculate(weightedThis, weightedDep, ridge);
  }

  /**
   * performs the actual regression.
   *
   * @param a the matrix to perform the regression on
   * @param y the dependent variable vector
   * @param ridge the ridge parameter
   * @throws IllegalArgumentException if not successful
   */
  protected void calculate(Matrix a, Matrix y, double ridge) {

    if (y.colNum > 1)
      throw new IllegalArgumentException("Only one dependent variable allowed");

    int nc = a.colNum;
    m_Coefficients = new double[nc];
    Matrix solution;

    Matrix ss = Matrix.aTa(a);
    Matrix bb = Matrix.aTy(a, y);

    boolean success = true;

    do {
      // Set ridge regression adjustment
      Matrix ssWithRidge = ss.copy();
      for (int i = 0; i < nc; i++)
        ssWithRidge.data[i][i] =ssWithRidge.data[i][i] + ridge;
      // Carry out the regression
      try {
        solution = ssWithRidge.solve(bb);
        for (int i = 0; i < nc; i++)
          m_Coefficients[i] = solution.data[i][0];
        success = true;
      } catch (Exception ex) {
        ridge *= 10;
        success = false;
      }
    } while (!success);
  }

  /**
   * returns the calculated coefficients
   *
   * @return the coefficients
   */
  public final double[] getCoefficients() {
    return m_Coefficients;
  }

  /**
   * returns the coefficients in a string representation
   */
  public String toString() {
    return Arrays.toString(getCoefficients());
  }

}
