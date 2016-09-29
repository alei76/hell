package ps.hell.math.base.matrix;

import java.util.LinkedList;
import java.util.Random;

import ps.hell.math.base.MathBase;
import ps.hell.math.base.matrix.decomposition.EigenValueDecomposition2;
import ps.hell.math.base.matrix.decomposition.LUDecomposition;
import ps.hell.math.base.matrix.decomposition.SingularValueDecomposition;
import ps.hell.math.base.matrix.inter.IMatrix;

/**
 * 矩阵函数基本类
 * 
 * @author Administrator
 *
 */
public class Matrix implements IMatrix {
	public int rowNum; // number of rows
	public int colNum; // number of columns

	public double[][] data; // M-by-N array

	public int getRowNum() {
		return rowNum;
	}

	public int getColNum() {
		return colNum;
	}

	public double[][] getData() {
		return data;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public void setData(double[][] data) {
		this.data = data;
	}

	/**
	 * create M-by-N matrix of 0's
	 * 
	 * @param rowNum
	 * @param colNum
	 */
	public Matrix(int rowNum, int colNum) {
		this.rowNum = rowNum;
		this.colNum = colNum;
		data = new double[rowNum][colNum];
	}

	public Matrix(int num) {
		this.rowNum = num;
		this.colNum = num;
		data = new double[num][num];
	}

	/**
	 * 获取魔术矩阵
	 * 
	 * @param paramInt
	 * @return
	 */
	public Matrix magic(int paramInt) {
		double[][] arrayOfdouble = new double[paramInt][paramInt];
		int i;
		int j;
		int i4;
		if (paramInt % 2 == 1) {
			i = (paramInt + 1) / 2;
			j = paramInt + 1;
			for (int k = 0; k < paramInt; ++k) {
				for (int l = 0; l < paramInt; ++l) {
					arrayOfdouble[l][k] = (paramInt * (l + k + i) % paramInt
							+ (l + 2 * k + j) % paramInt + 1);
				}

			}

		} else if (paramInt % 4 == 0) {
			for (i = 0; i < paramInt; ++i) {
				for (j = 0; j < paramInt; ++j) {
					if ((j + 1) / 2 % 2 == (i + 1) / 2 % 2)
						arrayOfdouble[j][i] = (paramInt * paramInt
								- (paramInt * j) - i);
					else {
						arrayOfdouble[j][i] = (paramInt * j + i + 1);
					}
				}
			}

		} else {
			i = paramInt / 2;
			j = (paramInt - 2) / 4;
			Matrix localMatrix = magic(i);
			double d2;
			for (int i1 = 0; i1 < i; ++i1) {
				for (int i3 = 0; i3 < i; ++i3) {
					d2 = localMatrix.data[i3][i1];
					arrayOfdouble[i3][i1] = d2;
					arrayOfdouble[i3][(i1 + i)] = (d2 + 2 * i * i);
					arrayOfdouble[(i3 + i)][i1] = (d2 + 3 * i * i);
					arrayOfdouble[(i3 + i)][(i1 + i)] = (d2 + i * i);
				}
			}
			for (int i2 = 0; i2 < i; ++i2) {
				for (i4 = 0; i4 < j; ++i4) {
					d2 = arrayOfdouble[i2][i4];
					arrayOfdouble[i2][i4] = arrayOfdouble[(i2 + i)][i4];
					arrayOfdouble[(i2 + i)][i4] = d2;
				}
				for (i4 = paramInt - j + 1; i4 < paramInt; ++i4) {
					d2 = arrayOfdouble[i2][i4];
					arrayOfdouble[i2][i4] = arrayOfdouble[(i2 + i)][i4];
					arrayOfdouble[(i2 + i)][i4] = d2;
				}
			}
			double d1 = arrayOfdouble[j][0];
			arrayOfdouble[j][0] = arrayOfdouble[(j + i)][0];
			arrayOfdouble[(j + i)][0] = d1;
			d1 = arrayOfdouble[j][j];
			arrayOfdouble[j][j] = arrayOfdouble[(j + i)][j];
			arrayOfdouble[(j + i)][j] = d1;
		}
		return new Matrix(arrayOfdouble);
	}

	/**
	 * create matrix based on 2d array
	 * 
	 * @param data
	 */
	public Matrix(double[][] data) {
		rowNum = data.length;
		colNum = data[0].length;
		this.data = new double[rowNum][colNum];
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++)
				this.data[i][j] = data[i][j];
	}

	/**
	 * create matrix based on 2d array
	 * 
	 * @param data
	 */
	public Matrix(float[][] data) {
		rowNum = data.length;
		colNum = data[0].length;
		this.data = new double[rowNum][colNum];
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++)
				this.data[i][j] = data[i][j];
	}

	/**
	 * 拷贝
	 * 
	 * @return
	 */
	public double[][] getDataCopy() {
		double[][] zv = new double[data.length][];
		for (int i = 0; i < zv.length; i++) {
			zv[i] = data[i].clone();
		}
		return zv;
	}

	/**
	 * 取局部数据
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public Matrix getMatrix(int x1, int y1, int x2, int y2) {
		double[][] zv = new double[x2 - x1 + 1][];
		for (int i = 0; i < zv.length; i++) {
			zv[i] = new double[y2 - y1 + 1];
		}
		for (int i = 0; i < x2 - x1 + 1; i++) {
			for (int j = 0; j < y2 - y1 + 1; j++) {
				zv[i][j] = this.data[i + x1][j + y1];
			}
		}
		return new Matrix(zv);
	}

	/**
	 * 获取伪逆
	 * 
	 * @return
	 */
	public static double[][] getWeiInverse(double[] val) {
		double[][] val2 = new double[1][val.length];
		for (int i = 0; i < val.length; i++) {
			val2[0][i] = val[i];
		}
		Matrix ma = new Matrix(val2);
		Matrix maT = Matrix.T(ma);
		Matrix ma2 = maT.times(maT);
		Matrix ma3 = ma2.inverse().times(maT);
		return ma3.data;

	}

	/**
	 * 获取伪逆
	 * 
	 * @return
	 */
	public static double[][] getWeiInverse(double[][] val) {
		double[][] val2 = new double[val.length][val[0].length];
		for (int i = 0; i < val.length; i++) {
			for (int j = 0; j < val[0].length; j++) {
				val2[i][j] = val[i][j];
			}
		}
		Matrix ma = new Matrix(val2);
		Matrix maT = Matrix.T(ma);
		Matrix ma2 = maT.times(ma);
		Matrix ma3 = ma2.inverse().times(maT);
		return ma3.data;

	}

	/**
	 * copy constructor
	 * 
	 * @param matrix
	 */
	private Matrix(Matrix matrix) {
		this(matrix.data);
	}

	/**
	 * create and return a random M-by-N matrix with values between 0 and 1
	 * 
	 * @param rowNum
	 * @param colNum
	 * @return
	 */
	public static Matrix random(int rowNum, int colNum) {
		Matrix A = new Matrix(rowNum, colNum);
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++)
				A.data[i][j] = (double) Math.random();
		return A;
	}

	/**
	 * create and return the N-by-N identity matrix
	 * 
	 * @param N
	 * @return
	 */
	public static Matrix identity(int N) {
		Matrix I = new Matrix(N, N);
		for (int i = 0; i < N; i++)
			I.data[i][i] = 1;
		return I;
	}

	/**
	 * swap rows i and j
	 * 
	 * @param i
	 * @param j
	 */
	private void swap(int i, int j) {
		double[] temp = data[i];
		data[i] = data[j];
		data[j] = temp;
	}

	/**
	 * create and return the transpose of the invoking matrix
	 * 
	 * @return
	 */
	public Matrix transpose() {
		Matrix A = new Matrix(colNum, rowNum);
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++)
				A.data[j][i] = this.data[i][j];
		return A;
	}

	/**
	 * return C = A + B
	 * 
	 * @param B
	 * @return
	 */
	public Matrix plus(Matrix B) {
		Matrix A = this;
		if (B.rowNum != A.rowNum || B.colNum != A.colNum)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(rowNum, colNum);
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++)
				C.data[i][j] = A.data[i][j] + B.data[i][j];
		return C;
	}

	/**
	 * return C = A - B
	 * 
	 * @param B
	 * @return
	 */
	public Matrix minus(Matrix B) {
		Matrix A = this;
		if (B.rowNum != A.rowNum || B.colNum != A.colNum)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(rowNum, colNum);
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++)
				C.data[i][j] = A.data[i][j] - B.data[i][j];
		return C;
	}

	/**
	 * return C = A / B
	 * 
	 * @param B
	 * @return
	 */
	public Matrix divide(Matrix B) {
		Matrix A = this;
		if (B.rowNum != A.rowNum || B.colNum != A.colNum)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(rowNum, colNum);
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++)
				C.data[i][j] = A.data[i][j] / B.data[i][j];
		return C;
	}

	/**
	 * return C = A / B
	 * 
	 * @param val
	 * @return
	 */
	public Matrix divide(double val) {
		Matrix A = this;
		Matrix C = new Matrix(rowNum, colNum);
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++)
				C.data[i][j] = A.data[i][j] / val;
		return C;
	}

	/**
	 * return C = A - B
	 * 
	 * @param B
	 * @return
	 */
	public Matrix Minus(Matrix B) {
		Matrix A = this;
		if (B.rowNum != A.rowNum || B.colNum != A.colNum)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(rowNum, colNum);
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++)
				C.data[i][j] = A.data[i][j] - B.data[i][j];
		return C;
	}

	/**
	 * does A = B exactly?
	 * 
	 * @param B
	 * @return
	 */
	public boolean eq(Matrix B) {
		Matrix A = this;
		if (B.rowNum != A.rowNum || B.colNum != A.colNum)
			throw new RuntimeException("Illegal matrix dimensions.");
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++)
				if (A.data[i][j] != B.data[i][j])
					return false;
		return true;
	}

	/**
	 * return C = A * B
	 * 
	 * @param B
	 * @return
	 */
	public Matrix times(Matrix B) {
		Matrix A = this;
		if (A.colNum != B.rowNum)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(A.rowNum, B.colNum);
		for (int i = 0; i < C.rowNum; i++)
			for (int j = 0; j < C.colNum; j++)
				for (int k = 0; k < A.colNum; k++)
					C.data[i][j] += (A.data[i][k] * B.data[k][j]);
		return C;
	}

	/**
	 * return C = A * B
	 * 
	 * @param B
	 * @return
	 */
	public Matrix times(double B) {
		Matrix A = this;
		Matrix C = new Matrix(A.rowNum, A.colNum);
		for (int i = 0; i < C.rowNum; i++)
			for (int j = 0; j < C.colNum; j++)
				for (int k = 0; k < A.colNum; k++)
					C.data[i][j] += (A.data[i][k] * B);
		return C;
	}

	/**
	 * 添加已个新行
	 * 
	 * @param val
	 * @return
	 */
	public Matrix addRow(double[] val) {
		Matrix A = this;
		Matrix C = new Matrix(A.rowNum + 1, A.colNum);
		for (int i = 0; i < A.rowNum; i++) {
			C.data[i] = A.data[i].clone();
		}
		for (int i = 0; i < val.length; i++) {
			C.data[A.rowNum][i] = val[i];
		}
		return C;
	}

	/**
	 * 添加一个新列
	 * 
	 * @param val
	 * @return
	 */
	public Matrix addCol(double[] val) {
		Matrix A = this;
		Matrix C = new Matrix(A.rowNum, A.colNum + 1);
		for (int i = 0; i < A.rowNum; i++) {
			for (int j = 0; j < C.colNum; j++) {
				if (j == A.colNum) {
					C.data[i][j] = val[i];
				} else {
					C.data[i][j] = A.data[i][j];
				}
			}
		}
		return C;
	}

	/**
	 * 更新列
	 * 
	 * @param index
	 * @param val
	 * @return
	 */
	public Matrix putCol(int index, double[] val) {
		Matrix A = this;
		Matrix C = new Matrix(A.rowNum, A.colNum);
		for (int i = 0; i < A.rowNum; i++) {
			for (int j = 0; j < C.colNum; j++) {
				if (j == index) {
					C.data[i][j] = val[i];
				} else {
					C.data[i][j] = A.data[i][j];
				}
			}
		}
		return C;
	}

	/**
	 * 获取列
	 * 
	 * @param index
	 * @return
	 */
	public Matrix getCol(int index) {
		Matrix A = this;
		Matrix C = new Matrix(A.rowNum, 1);
		for (int i = 0; i < A.rowNum; i++) {
			C.data[i][0] = A.data[i][index];
		}
		return C;
	}

	/**
	 * 获取列
	 * 
	 * @param index
	 * @return
	 */
	public double[] getColVal(int index) {
		Matrix A = this;
		double[] val = new double[A.rowNum];
		for (int i = 0; i < A.rowNum; i++) {
			val[i] = A.data[i][index];
		}
		return val;
	}

	public double norm2() {
		return new SingularValueDecomposition(this).norm2();
	}

	/**
	 * 获取svd数据
	 * 
	 * @return
	 */
	public SingularValueDecomposition svd() {
		return new SingularValueDecomposition(this);
	}

	public double normInf() {
		double d1 = 0.0f;
		for (int i = 0; i < this.rowNum; ++i) {
			double d2 = 0.0f;
			for (int j = 0; j < this.colNum; ++j) {
				d2 += Math.abs(this.data[i][j]);
			}
			d1 = Math.max(d1, d2);
		}
		return d1;
	}

	public double normF() {
		double d = 0.0f;
		for (int i = 0; i < this.rowNum; ++i) {
			for (int j = 0; j < this.colNum; ++j) {
				d = MathBase.hypot(d, this.data[i][j]);
			}
		}
		return d;
	}

	/**
	 * 矩阵所有值转成值*-1
	 * 
	 * @return
	 */
	public Matrix uminus() {
		Matrix localMatrix = new Matrix(this.rowNum, this.colNum);
		double[][] arrayOfdouble = localMatrix.data;
		for (int i = 0; i < this.rowNum; ++i) {
			for (int j = 0; j < this.colNum; ++j) {
				arrayOfdouble[i][j] = (-this.data[i][j]);
			}
		}
		return localMatrix;
	}

	/**
	 * 行列式值
	 * 
	 * @return
	 */
	public double det() {
		return new LUDecomposition(this).det();
	}

	/**
	 * 获取lu分解数据
	 * 
	 * @return
	 */
	public LUDecomposition lu() {
		return new LUDecomposition(this);
	}

	public Matrix getMatrix(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
		Matrix localMatrix = new Matrix(paramArrayOfInt.length, paramInt2
				- paramInt1 + 1);
		double[][] arrayOfdouble = localMatrix.data;
		try {
			for (int i = 0; i < paramArrayOfInt.length; ++i)
				for (int j = paramInt1; j <= paramInt2; ++j)
					arrayOfdouble[i][(j - paramInt1)] = this.data[paramArrayOfInt[i]][j];
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
		return localMatrix;
	}

	/**
	 * 获取秩
	 * 
	 * @return
	 */
	public int rank() {
		return new SingularValueDecomposition(this).rank();
	}

	/**
	 * 获取条件数
	 * 
	 * @return
	 */
	public double cond() {
		return new SingularValueDecomposition(this).cond();
	}

	public double trace() {
		double d = 0.0f;
		for (int i = 0; i < Math.min(this.rowNum, this.colNum); ++i) {
			d += this.data[i][i];
		}
		return d;
	}

	/**
	 * 将matrix一致化 对角线为1其他值为0
	 * 
	 * @param paramInt1
	 *            获取行数
	 * @param paramInt2
	 *            获取猎术
	 * @return
	 */
	public static Matrix identity(int paramInt1, int paramInt2) {
		Matrix localMatrix = new Matrix(paramInt1, paramInt2);
		double[][] arrayOfdouble = localMatrix.data;
		for (int i = 0; i < paramInt1; ++i) {
			for (int j = 0; j < paramInt2; ++j) {
				arrayOfdouble[i][j] = ((i == j) ? 1.0f : 0.0f);
			}
		}
		return localMatrix;
	}

	/**
	 * 获取列
	 * 
	 * @param index
	 * @return
	 */
	public Matrix getRow(int index) {
		Matrix A = this;
		Matrix C = new Matrix(1, A.colNum);
		for (int i = 0; i < A.colNum; i++) {
			C.data[0][i] = A.data[index][i];
		}
		return C;
	}

	/**
	 * 获取列
	 * 
	 * @param index
	 * @return
	 */
	public double[] getRowVal(int index) {
		Matrix A = this;
		double[] val = new double[A.colNum];
		for (int i = 0; i < A.colNum; i++) {
			val[i] = A.data[index][i];
		}
		return val;
	}

	/**
	 * 更新行
	 * 
	 * @param val
	 * @return
	 */
	public Matrix putRow(int index, double[] val) {
		Matrix A = this;
		Matrix C = new Matrix(A.rowNum, A.colNum);
		for (int i = 0; i < A.rowNum; i++) {
			if (i == index) {
				C.data[i] = val.clone();
			} else {
				C.data[i] = A.data[i].clone();
			}
		}
		return C;
	}

	/**
	 * 平方和
	 * 
	 * @return
	 */
	public double sumOfSquares() {
		double val = 0f;
		for (int i = 0; i < this.rowNum; i++) {
			for (int j = 0; j < this.colNum; j++) {
				val += this.data[i][j] * this.data[i][j];
			}
		}
		return val;
	}

	/**
	 * return x = A^-1 b, assuming A is square and has full rank
	 * 
	 * @param rhs
	 * @return
	 */
	public Matrix solve(Matrix rhs) {
		if (rowNum != rowNum || rhs.rowNum != rowNum || rhs.colNum != 1)
			throw new RuntimeException("Illegal matrix dimensions.");

		// create copies of the data
		Matrix A = new Matrix(this);
		Matrix b = new Matrix(rhs);

		// Gaussian elimination with partial pivoting
		for (int i = 0; i < colNum; i++) {

			// find pivot row and swap
			int max = i;
			for (int j = i + 1; j < colNum; j++)
				if (Math.abs(A.data[j][i]) > Math.abs(A.data[max][i]))
					max = j;
			A.swap(i, max);
			b.swap(i, max);

			// singular
			if (A.data[i][i] == 0.0)
				throw new RuntimeException("Matrix is singular.");

			// pivot within b
			for (int j = i + 1; j < colNum; j++)
				b.data[j][0] -= b.data[i][0] * A.data[j][i] / A.data[i][i];

			// pivot within A
			for (int j = i + 1; j < colNum; j++) {
				double m = A.data[j][i] / A.data[i][i];
				for (int k = i + 1; k < colNum; k++) {
					A.data[j][k] -= A.data[i][k] * m;
				}
				A.data[j][i] = 0.0f;
			}
		}

		// back substitution

		Matrix x = new Matrix(colNum, 1);
		for (int j = colNum - 1; j >= 0; j--) {
			double t = 0.0f;
			for (int k = j + 1; k < colNum; k++)
				t += A.data[j][k] * x.data[k][0];
			x.data[j][0] = (b.data[j][0] - t) / A.data[j][j];
		}
		return x;

	}

	/**
	 * print matrix to standard output
	 */

	public void show() {
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++)
				System.out.printf("%9.4f ", data[i][j]);
			System.out.println();
		}
	}

	/**
	 * add Matrix
	 * 
	 * @param ifExtend
	 *            是否扩充
	 * @param fillCol
	 *            从第几列开始 1表示第一列 0 为无效 -1 为负1列
	 * @param fillvalue
	 *            填补值
	 */
	public void addMatrix(Matrix m, boolean ifExtend, int fillCol,
			double fillvalue) {
		if (this.colNum == m.colNum) {
			this.rowNum = rowNum + m.rowNum;
			Matrix A = this;
			for (int i = 0; i < m.rowNum; i++)
				for (int j = 0; j < colNum; j++)
					A.data[i + A.rowNum - m.rowNum - 1][j] = m.data[i][j];
		} else {
			if (fillCol == 0) {
				System.out.println("补充列不能为0");
				return;

			}
			if (ifExtend == false) {
				System.out.println("列数不同");
				return;
			} else {
				if (fillCol >= 0) {
					this.rowNum = rowNum + m.rowNum;
					this.colNum = this.colNum >= (m.colNum - fillCol + 1) ? this.colNum
							: m.colNum;
					Matrix A = this;
					for (int i = 0; i < m.rowNum; i++) {
						for (int j = 0; j < A.colNum; j++) {
							if (j >= fillCol - 1) {
								if (j < m.colNum + fillCol - 1)
									A.data[i + A.rowNum - m.rowNum - 1][j] = m.data[i][j];
								else {
									A.data[i + A.rowNum - m.rowNum - 1][j] = fillvalue;
								}
							} else {
								A.data[i + A.rowNum - m.rowNum - 1][j] = fillvalue;
							}
						}
					}
				} else {
					Matrix B = new Matrix(rowNum + m.rowNum, this.colNum
							- fillCol >= (m.colNum) ? this.colNum : m.colNum);
					for (int i = 0; i < B.rowNum; i++) {
						for (int j = 0; j < B.colNum; j++) {
							if (i < rowNum) {

								if (j < -fillCol)
									B.data[i][j] = fillvalue;
								else {
									if (j >= -fillCol + colNum)
										B.data[i][j] = fillvalue;
									else {
										B.data[i][j] = this.data[i][j + fillCol];
									}
								}
							} else {
								if (j < m.colNum)
									B.data[i][j] = m.data[i + rowNum - 1][j];
								else {
									B.data[i][j] = fillvalue;
								}

							}
						}
					}
				}
			}
		}
	}

	/**
	 * 单纯添加 行添加
	 * 
	 * @param m
	 */
	public void addMatrix(Matrix m) {
		if (this.colNum == m.colNum) {
			this.rowNum = rowNum + m.rowNum;
			// System.out.println(colNum);
			double[][] data2 = new double[rowNum][colNum];
			// System.out.println("this.rowNum:"+rowNum);
			for (int i = 0; i < rowNum; i++)
				for (int j = 0; j < colNum; j++) {
					// System.out.println(i+rowNum-m.rowNum-1);
					if (i < rowNum - m.rowNum) {
						data2[i][j] = data[i][j];
					} else
						data2[i][j] = m.data[i - (rowNum - m.rowNum)][j];
				}
			data = data2;
			// System.out.println(data.length+"\t"+data[0].length);
		} else {
			System.out.println("列数不同");
			return;
		}
	}

	/**
	 * 单纯添加 列右侧添加
	 * 
	 * @param m
	 */
	public void addColMatrix(Matrix m) {
		if (this.rowNum == m.rowNum) {
			this.colNum = this.colNum + m.colNum;
			// System.out.println(colNum);
			double[][] data2 = new double[rowNum][colNum];
			// System.out.println("this.rowNum:"+rowNum);
			for (int i = 0; i < rowNum; i++)
				for (int j = 0; j < colNum; j++) {
					// System.out.println(i+rowNum-m.rowNum-1);
					if (j < colNum - m.colNum) {
						data2[i][j] = data[i][j];
					} else {
						data2[i][j] = m.data[i][j - (colNum - m.colNum)];
					}
				}
			data = data2;
			// System.out.println(data.length+"\t"+data[0].length);
		} else {
			System.out.println("列数不同");
			return;
		}
	}

	/**
	 * 添加数值并行
	 * 
	 * @param m
	 * @param tr
	 *            tr为true为行向 tr为false为纵向
	 */
	public void adddouble(double[] m, boolean tr) {
		if (tr == true) {// 行向
			for (int i = 0; i < m.length / colNum + 1; i++) {
				for (int j = 0; j < colNum; j++) {
					if ((i * colNum + j + 1) > m.length)
						break;
					else {
						data[i][j] = m[i * colNum + j];
					}
				}
			}
		} else {// 纵向
			for (int i = 0; i < colNum; i++) {
				for (int j = 0; j < rowNum; j++) {
					if ((i * rowNum + j + 1) > m.length)
						break;
					else {
						data[j][i] = m[i * rowNum + j];
					}
				}
			}
		}
	}

	/**
	 * 获取均值 flagRow 为true则为按行为单位
	 */
	public double[] getMeans(boolean flagRow) {
		double[] da;
		if (flagRow = true) {
			da = new double[rowNum];
			for (int i = 0; i < rowNum; i++) {
				for (int j = 0; j < colNum; j++) {
					da[i] += data[i][j];
				}
				da[i] = da[i] / colNum;
			}
		} else {
			da = new double[colNum];
			for (int i = 0; i < colNum; i++) {
				for (int j = 0; j < rowNum; j++) {
					da[i] += data[j][i];
				}
				da[i] = da[i] / rowNum;
			}
		}
		return da;
	}

	/**
	 * double[][] to matrix
	 * 
	 * @param da
	 * @return
	 */
	public static Matrix doubleToMatrix(double[][] da) {
		Matrix a = new Matrix(da.length, da[0].length);
		a.data = da;
		return a;
	}

	/**
	 * 获取协方差矩阵
	 */
	public Matrix getCovMatrix() {
		Matrix ll = new Matrix(colNum, colNum);
		double[] xavg = this.getMeans(false);
		for (int i = 0; i < rowNum; i++) {
			for (int k1 = 0; k1 < colNum; k1++) {
				for (int k2 = 0; k2 < colNum; k2++) {
					ll.data[k1][k2] += (data[i][k1] - xavg[k1])
							* (data[i][k2] - xavg[k2]) / (rowNum - 1);
				}
			}
		}

		return ll;
	}

	/**
	 * 获取协方差
	 */
	public double getCov(int first, int second) {
		double val = 0.0f;
		double[] xavg = this.getMeans(false);
		for (int i = 0; i < rowNum; i++) {
			val += (data[i][first] - xavg[first])
					* (data[i][second] - xavg[second]) / (rowNum - 1);
		}
		return val;
	}

	/**
	 * 取得转置矩阵
	 * 
	 * @param A
	 * @return
	 */
	public static Matrix T(Matrix A) {
		int h = A.rowNum;
		int v = A.colNum;
		// 创建和A行和列相反的转置矩阵
		Matrix A_T = new Matrix(v, h);
		// 根据A取得转置矩阵A_T
		for (int i = 0; i < v; i++) {
			for (int j = 0; j < h; j++) {
				A_T.data[i][j] = A.data[j][i];
			}
		}
		// System.out.println("取得转置矩阵  wanbi........");
		return A_T;
	}

	// /**
	// * 求解行列式的模----------->最终的总结归纳
	// *
	// * @param data
	// * @return
	// */
	// public double getDet() {
	// if (rowNum != colNum) {
	// System.out.println("行和列长度不同");
	// return 0f;
	// }
	//
	// // 终止条件
	// if (rowNum == 2) {
	// return data[0][0] * data[1][1] - data[0][1] * data[1][0];
	// }
	// double total = 0;
	// // 根据data 得到行列式的行数和列数
	//
	// // 创建一个大小为num 的数组存放对应的展开行中元素求的的值
	// double[] nums = new double[rowNum];
	//
	// for (int i = 0; i < rowNum; i++) {
	// if (i % 2 == 0) {
	// nums[i] = data[0][i] * getHL(getDY(data, 1, i + 1));
	// } else {
	// nums[i] = -data[0][i] * getHL(getDY(data, 1, i + 1));
	// }
	// }
	// for (int i = 0; i < rowNum; i++) {
	// total += nums[i];
	// }
	// System.out.println("total=" + total);
	// return total;
	// }

	/**
	 * 获取矩阵逆
	 * 
	 * @return
	 */
	public Matrix inverse() {
		return solve(identity(this.rowNum, this.rowNum));
	}

	// /**
	// * 求解逆矩阵------>z最后的总结和归纳
	// *
	// * @param data
	// * @return
	// */
	// public Matrix inversion() {
	// // 先是求出行列式的模|data|
	// Matrix mm = new Matrix(rowNum, rowNum);
	// double A = getHL(data);
	// // 创建一个等容量的逆矩阵
	// // double[][] newData = new double[data.length][data.length];
	//
	// for (int i = 0; i < rowNum; i++) {
	// for (int j = 0; j < rowNum; j++) {
	// double num;
	// if ((i + j) % 2 == 0) {
	// num = getHL(getDY(data, i + 1, j + 1));
	// } else {
	// num = -getHL(getDY(data, i + 1, j + 1));
	// }
	//
	// mm.data[i][j] = num / A;
	// }
	// }
	//
	// // 转置 代数余子式转制
	// mm = Matrix.T(mm);
	// // 打印
	// // for (int i = 0; i < data.length; i++) {
	// // for (int j = 0; j < data.length; j++) {
	// // System.out.print("newData[" + i + "][" + j + "]= "
	// // + mm.data[i][j] + "   ");
	// // }
	// //
	// // System.out.println();
	// // }
	//
	// return mm;
	// }

	// /**
	// * 求解行列式的模----------->最终的总结归纳
	// *
	// * @param data
	// * @return
	// */
	// public static double getHL(double[][] data) {
	//
	// // 终止条件
	// if (data.length == 2) {
	// return data[0][0] * data[1][1] - data[0][1] * data[1][0];
	// }
	//
	// double total = 0;
	// // 根据data 得到行列式的行数和列数
	// int num = data.length;
	// // 创建一个大小为num 的数组存放对应的展开行中元素求的的值
	// double[] nums = new double[num];
	//
	// for (int i = 0; i < num; i++) {
	// if (i % 2 == 0) {
	// nums[i] = data[0][i] * getHL(getDY(data, 1, i + 1));
	// } else {
	// nums[i] = -data[0][i] * getHL(getDY(data, 1, i + 1));
	// }
	// }
	// for (int i = 0; i < num; i++) {
	// total += nums[i];
	// }
	// // System.out.println("total=" + total);
	// return total;
	// }
	//
	// /**
	// * 2 求2阶行列式的数值
	// *
	// * @param data
	// * @return
	// */
	// public static double getHL2(double[][] data) {
	// // data 必须是2*2 的数组
	// double num1 = data[0][0] * data[1][1];
	// double num2 = -data[0][1] * data[1][0];
	// return num1 + num2;
	// }
	//
	// /**
	// * 求3阶行列式的数值
	// *
	// * @param data
	// * @return
	// */
	// public static double getHL3(double[][] data) {
	// double num1 = data[0][0] * getHL2(getDY(data, 1, 1));
	// double num2 = -data[0][1] * getHL2(getDY(data, 1, 2));
	// double num3 = data[0][2] * getHL2(getDY(data, 1, 3));
	// // System.out.println("---->"+num1);
	// // System.out.println("---->"+num2);
	// // System.out.println("---->"+num3);
	// // System.out.println("3阶行列式的数值是：----->" + (num1 + num2 + num3));
	// return num1 + num2 + num3;
	// }
	//
	// /**
	// * 求4阶行列式的数值
	// *
	// * @param data
	// * @return
	// */
	// public static double getHL4(double[][] data) {
	// double num1 = data[0][0] * getHL3(getDY(data, 1, 1));
	// double num2 = -data[0][1] * getHL3(getDY(data, 1, 2));
	// double num3 = data[0][2] * getHL3(getDY(data, 1, 3));
	// double num4 = -data[0][3] * getHL3(getDY(data, 1, 4));
	// // System.out.println("--------->"+num1);
	// // System.out.println("--------->"+num2);
	// // System.out.println("--------->"+num3);
	// // System.out.println("--------->"+num4);
	// // System.out.println("4阶行列式的数值------->"+(num1+num2+num3+num4));
	//
	// return num1 + num2 + num3 + num4;
	// }
	//
	// /**
	// * 求5阶行列式的数值
	// */
	// public static double getHL5(double[][] data) {
	//
	// double num1 = data[0][0] * getHL4(getDY(data, 1, 1));
	// double num2 = -data[0][1] * getHL4(getDY(data, 1, 2));
	// double num3 = data[0][2] * getHL4(getDY(data, 1, 3));
	// double num4 = -data[0][3] * getHL4(getDY(data, 1, 4));
	// double num5 = data[0][4] * getHL4(getDY(data, 1, 5));
	//
	// System.out.println("5 阶行列式的数值是：  ------->"
	// + (num1 + num2 + num3 + num4 + num5));
	// return num1 + num2 + num3 + num4 + num5;
	//
	// }
	//
	// /**
	// * 1 求解代数余子式 输入：原始矩阵+行+列 现实中真正的行和列数目
	// */
	// public static double[][] getDY(double[][] data, int h, int v) {
	// int H = data.length;
	// int V = data[0].length;
	// double[][] newData = new double[H - 1][V - 1];
	//
	// for (int i = 0; i < newData.length; i++) {
	//
	// if (i < h - 1) {
	// for (int j = 0; j < newData[i].length; j++) {
	// if (j < v - 1) {
	// newData[i][j] = data[i][j];
	// } else {
	// newData[i][j] = data[i][j + 1];
	// }
	// }
	// } else {
	// for (int j = 0; j < newData[i].length; j++) {
	// if (j < v - 1) {
	// newData[i][j] = data[i + 1][j];
	// } else {
	// newData[i][j] = data[i + 1][j + 1];
	// }
	// }
	//
	// }
	// }
	// //
	// System.out.println("---------------------代数余子式测试.---------------------------------");
	// // for(int i=0;i<newData.length;i++){
	// // for(int j=0;j<newData[i].length;j++){
	// // System.out.print("newData["+i+"]"+"["+j+"]="+newData[i][j]+"   ");
	// // }
	// //
	// // System.out.println();
	// // }
	//
	// return newData;
	// }

	/**
	 * 将matrix截断
	 * 
	 * @param start
	 *            从1开始也可以为end<start这样反项截断
	 * @param end
	 *            到length
	 * @param rowFlag
	 *            按行还是按列
	 * @return
	 */
	public Matrix truncMatrix(int start, int end, boolean rowFlag) {
		Matrix da;
		if (rowFlag = true) {
			da = new Matrix(Math.abs(end - start + 1), colNum);
			if (end >= start) {
				for (int i = start - 1; i < end; i++) {
					for (int j = 0; j < colNum; j++) {
						da.data[i - start + 1][j] = data[i][j];
					}
				}
			} else {// 反向
				for (int i = start - 1; i > end - 1; i--) {
					for (int j = 0; j < colNum; j++) {
						da.data[start - 1 - i][j] = data[i][j];
					}
				}
			}
		} else {
			da = new Matrix(rowNum, Math.abs(end - start + 1));
			if (end >= start) {
				for (int i = start - 1; i < end; i++) {
					for (int j = 0; j < colNum; j++) {
						da.data[i - start + 1][j] = data[i][j];
					}
				}
			} else {// 反向
				for (int i = start - 1; i > end - 1; i--) {
					for (int j = 0; j < colNum; j++) {
						da.data[start - 1 - i][j] = data[i][j];
					}
				}
			}
		}
		return da;
	}

	/**
	 * 将矩阵按照行随机化排列
	 * 
	 * @param ma
	 */
	public Matrix randomMatrix(Matrix ma) {
		double[][] ma2 = new double[ma.getRowNum()][ma.getColNum()];
		int i = 0;
		LinkedList<double[]> ma4 = new LinkedList<double[]>();
		for (double[] da : ma.getData()) {
			ma4.add(da.clone());
		}
		ma = null;
		Random random = new Random();
		while (true) {
			if (ma4.size() == 1) {
				ma2[i] = ma4.get(0);
				ma4 = null;
				break;
			}
			int ran = Math.abs(random.nextInt()) % (ma4.size());
			ma2[i] = ma4.get(ran);
			ma4.remove(ran);
			i++;
		}
		return new Matrix(ma2);
	}

	/**
	 * 转化为数值型
	 */
	public int[][] toTransInteger() {
		if (this.data.length == 0) {
			return null;
		}
		int[][] temp = new int[this.data.length][this.data[0].length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				temp[i][j] = (int) (data[i][j]);
			}
		}
		return temp;
	}

	/**
	 * 转化成int 再转化为String 转化为string
	 */
	public String[][] toTransString() {
		if (this.data.length == 0) {
			return null;
		}
		String[][] temp = new String[this.data.length][this.data[0].length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				temp[i][j] = Integer.toString((int) (data[i][j]));
			}
		}
		return temp;
	}

	/**
	 * Return aTa (a' * a)
	 */
	public static Matrix aTa(Matrix a) {
		int cols = a.colNum;
		double[][] A = a.data;
		Matrix x = new Matrix(cols, cols);
		double[][] X = x.data;
		double[] Acol = new double[a.rowNum];
		for (int col1 = 0; col1 < cols; col1++) {
			// cache the column for faster access later
			for (int row = 0; row < Acol.length; row++) {
				Acol[row] = A[row][col1];
			}
			// reference the row for faster lookup
			double[] Xrow = X[col1];
			for (int row = 0; row < Acol.length; row++) {
				double[] Arow = A[row];
				for (int col2 = col1; col2 < Xrow.length; col2++) {
					Xrow[col2] += Acol[row] * Arow[col2];
				}
			}
			// result is symmetric
			for (int col2 = col1 + 1; col2 < Xrow.length; col2++) {
				X[col2][col1] = Xrow[col2];
			}
		}
		return x;
	}

	/**
	 * Return aTy (a' * y)
	 */
	public static Matrix aTy(Matrix a, Matrix y) {
		double[][] A = a.data;
		double[][] Y = y.data;
		Matrix x = new Matrix(a.colNum, 1);
		double[][] X = x.data;
		for (int row = 0; row < A.length; row++) {
			// reference the rows for faster lookup
			double[] Arow = A[row];
			double[] Yrow = Y[row];
			for (int col = 0; col < Arow.length; col++) {
				X[col][0] += Arow[col] * Yrow[0];
			}
		}
		return x;
	}

	public Matrix copy() {
		double[][] val = new double[this.rowNum][];
		for (int i = 0; i < this.rowNum; i++) {
			val[i] = this.data[i].clone();
		}
		return new Matrix(val);
	}

	/**
	 * test client
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		double[][] d = { { 1, 60, 1830 }, { 1, 1830, 73810 }, { 1, 1, 1 } };
		Matrix D = new Matrix(d);
		D.show();
		System.out.println("矩阵D逆");
		D.inverse().show();
		System.out.println();

		// Matrix A = Matrix.random(6, 6);
		// A.show();
		// System.out.println();
		// A.inversion().show();
		// System.out.println("矩阵逆");
		// A.swap(1, 2);
		// A.show();
		// System.out.println();
		//
		// Matrix B = A.transpose();
		// B.show();
		// System.out.println();
		//
		// Matrix C = Matrix.identity(5);
		// C.show();
		// System.out.println();
		//
		// A.plus(B).show();
		// System.out.println();
		//
		// B.times(A).show();
		// System.out.println();
		//
		// // shouldn't be equal since AB != BA in general
		// System.out.println(A.times(B).eq(B.times(A)));
		// System.out.println();
		//
		// Matrix b = Matrix.random(5, 1);
		// b.show();
		// System.out.println();
		//
		// Matrix x = A.solve(b);
		// x.show();
		// System.out.println();
		//
		// A.times(x).show();

	}

	public boolean symmetric;
	public boolean positive;

	/**
	 * Constructor. Note that this is a shallow copy of A.
	 * 
	 * @param A
	 *            the array of matrix.
	 * @param symmetric
	 *            true if the matrix is symmetric.
	 */
	public Matrix(double[][] A, boolean symmetric) {
		if (symmetric && A.length != A[0].length) {
			throw new IllegalArgumentException("A is not square");
		}

		this.data = data;
		this.symmetric = symmetric;
	}

	/**
	 * Constructor. Note that this is a shallow copy of A.
	 * 
	 * @param A
	 *            the array of matrix.
	 * @param symmetric
	 *            true if the matrix is symmetric.
	 * @param positive
	 *            true if the matrix is positive definite.
	 */
	public Matrix(double[][] A, boolean symmetric, boolean positive) {
		if (symmetric && A.length != A[0].length) {
			throw new IllegalArgumentException("A is not square");
		}

		this.data = A;
		this.symmetric = symmetric;
		this.positive = positive;
	}

	@Override
	public int nrows() {
		return data.length;
	}

	@Override
	public int ncols() {
		return data[0].length;
	}

	@Override
	public double get(int i, int j) {
		return data[i][j];
	}

	@Override
	public void set(int i, int j, double x) {
		data[i][j] = x;
	}

	@Override
	public void ax(double[] x, double[] y) {
		MathBase.ax(data, x, y);
	}

	@Override
	public void axpy(double[] x, double[] y) {
		MathBase.axpy(data, x, y);
	}

	@Override
	public void axpy(double[] x, double[] y, double b) {
		MathBase.axpy(data, x, y, b);
	}

	@Override
	public void atx(double[] x, double[] y) {
		MathBase.atx(data, x, y);
	}

	@Override
	public void atxpy(double[] x, double[] y) {
		MathBase.atxpy(data, x, y);
	}

	@Override
	public void atxpy(double[] x, double[] y, double b) {
		MathBase.atxpy(data, x, y, b);
	}

	@Override
	public void asolve(double[] b, double[] x) {
		int m = data.length;
		int n = data[0].length;

		if (m != n) {
			throw new IllegalStateException("Matrix is not square.");
		}

		for (int i = 0; i < n; i++) {
			x[i] = data[i][i] != 0.0 ? b[i] / data[i][i] : b[i];
		}
	}

	/**
	 * Returns the largest eigen pair of matrix with the power iteration under
	 * the assumptions A has an eigenvalue that is strictly greater in magnitude
	 * than its other eigenvalues and the starting vector has a nonzero
	 * component in the direction of an eigenvector associated with the dominant
	 * eigenvalue.
	 * 
	 * @param v
	 *            on input, it is the non-zero initial guess of the eigen
	 *            vector. On output, it is the eigen vector corresponding
	 *            largest eigen value.
	 * @return the largest eigen value.
	 */
	public double eigen(double[] v) {
		if (nrows() != ncols()) {
			throw new UnsupportedOperationException("The matrix is not square.");
		}

		return EigenValueDecomposition2.eigen(this, v);
	}

	/**
	 * Returns the eigen value decomposition.
	 */
	public EigenValueDecomposition2 eigen() {
		if (nrows() != ncols()) {
			throw new UnsupportedOperationException("The matrix is not square.");
		}

		int n = data.length;
		double[][] V = new double[n][n];

		for (int i = 0; i < n; i++) {
			System.arraycopy(data[i], 0, V[i], 0, n);
		}

		EigenValueDecomposition2 eigen = EigenValueDecomposition2.decompose(V,
				symmetric);

		positive = true;
		for (int i = 0; i < n; i++) {
			if (eigen.getEigenValues()[i] <= 0) {
				positive = false;
				break;
			}
		}

		return eigen;
	}

	/**
	 * Returns the k largest eigen pairs. Only works for symmetric matrix.
	 */
	public EigenValueDecomposition2 eigen(int k) {
		if (nrows() != ncols()) {
			throw new UnsupportedOperationException("The matrix is not square.");
		}
		if (!symmetric) {
			throw new UnsupportedOperationException(
					"The current implementation of eigen value decomposition only works for symmetric matrices");
		}
		EigenValueDecomposition2 eigen = EigenValueDecomposition2.decompose(
				this, k);

		return eigen;
	}
	
	
	//
	//
	//
	// /**
	// * Solve A*x = b in place (exact solution if A is square, least squares
	// * solution otherwise), which means the results will be stored in b.
	// * @return the solution matrix, actually b.
	// */
	// public double[] solve(double[] b) {
	// if (data.length == data[0].length) {
	// if (symmetric && positive) {
	// cholesky().solve(b);
	// } else {
	// LU().solve(b);
	// }
	// } else {
	// QR().solve(b);
	// }
	// return b;
	// }
	//
	// /**
	// * Solve A*X = B in place (exact solution if A is square, least squares
	// * solution otherwise), which means the results will be stored in B.
	// * @return the solution matrix, actually B.
	// */
	// public double[][] solve(double[][] B) {
	// if (data.length == data[0].length) {
	// if (symmetric && positive) {
	// cholesky().solve(B);
	// } else {
	// LU().solve(B);
	// }
	// } else {
	// QR().solve(B);
	// }
	// return B;
	// }
	//
	// /**
	// * Iteratively improve a solution to linear equations.
	// *
	// * @param b right hand side of linear equations.
	// * @param x a solution to linear equations.
	// */
	// public void improve(double[] b, double[] x) {
	// int n = data.length;
	//
	// if (data.length != data[0].length) {
	// throw new IllegalStateException("A is not square.");
	// }
	//
	// if (x.length != n || b.length != n) {
	// throw new
	// IllegalArgumentException(String.format("Row dimensions do not agree: A is %d x %d, but b is %d x 1 and x is %d x 1",
	// A.length, A[0].length, b.length, x.length));
	// }
	//
	// if (symmetric && positive) {
	// // Calculate the right-hand side, accumulating the residual
	// // in higher precision.
	// double[] r = new double[n];
	// for (int i = 0; i < n; i++) {
	// double sdp = -b[i];
	// for (int j = 0; j < n; j++) {
	// sdp += A[i][j] * x[j];
	// }
	// r[i] = sdp;
	// }
	//
	// // Solve for the error term.
	// cholesky.solve(r);
	//
	// // Subtract the error from the old soluiton.
	// for (int i = 0; i < n; i++) {
	// x[i] -= r[i];
	// }
	// } else {
	// // Calculate the right-hand side, accumulating the residual
	// // in higher precision.
	// double[] r = new double[n];
	// for (int i = 0; i < n; i++) {
	// double sdp = -b[i];
	// for (int j = 0; j < n; j++) {
	// sdp += A[i][j] * x[j];
	// }
	// r[i] = sdp;
	// }
	//
	// // Solve for the error term.
	// lu.solve(r);
	//
	// // Subtract the error from the old soluiton.
	// for (int i = 0; i < n; i++) {
	// x[i] -= r[i];
	// }
	// }
	// }
}
