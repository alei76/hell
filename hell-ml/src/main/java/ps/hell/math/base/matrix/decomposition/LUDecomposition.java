package ps.hell.math.base.matrix.decomposition;

import java.io.Serializable;

import ps.hell.math.base.matrix.Matrix;

public class LUDecomposition implements Serializable {
	private double[][] LU;
	private int m;
	private int n;
	private int pivsign;
	private int[] piv;
	private static final long serialVersionUID = 1L;

	public LUDecomposition(Matrix paramMatrix) {
		this.LU = paramMatrix.copy().data;
		this.m = paramMatrix.rowNum;
		this.n = paramMatrix.colNum;
		this.piv = new int[this.m];
		for (int i = 0; i < this.m; ++i) {
			this.piv[i] = i;
		}
		this.pivsign = 1;

		double[] arrayOfdouble2 = new double[this.m];
		int k;
		int l;
		for (int j = 0; j < this.n; ++j) {
			for ( k = 0; k < this.m; ++k)
				arrayOfdouble2[k] = this.LU[k][j];
			double d;
			for (k = 0; k < this.m; ++k) {
				double[] arrayOfdouble1 = this.LU[k];

				l = Math.min(k, j);
				d = 0.0f;
				for (int i1 = 0; i1 < l; ++i1) {
					d += arrayOfdouble1[i1] * arrayOfdouble2[i1];
				}

				arrayOfdouble1[j] = (arrayOfdouble2[k] -= d);
			}

			k = j;
			for ( l = j + 1; l < this.m; ++l) {
				if (Math.abs(arrayOfdouble2[l]) > Math.abs(arrayOfdouble2[k])) {
					k = l;
				}
			}
			if (k != j) {
				for (l = 0; l < this.n; ++l) {
					d = this.LU[k][l];
					this.LU[k][l] = this.LU[j][l];
					this.LU[j][l] = d;
				}
				l = this.piv[k];
				this.piv[k] = this.piv[j];
				this.piv[j] = l;
				this.pivsign = (-this.pivsign);
			}

			if ((((j < this.m) ? 1 : 0) & ((this.LU[j][j] != 0.0D) ? 1 : 0)) != 0)
				for (l = j + 1; l < this.m; ++l)
					this.LU[l][j] /= this.LU[j][j];
		}
	}

	public boolean isNonsingular() {
		for (int i = 0; i < this.n; ++i) {
			if (this.LU[i][i] == 0.0D)
				return false;
		}
		return true;
	}

	public Matrix getL() {
		Matrix localMatrix = new Matrix(this.m, this.n);
		double[][] arrayOfdouble = localMatrix.data;
		for (int i = 0; i < this.m; ++i) {
			for (int j = 0; j < this.n; ++j) {
				if (i > j)
					arrayOfdouble[i][j] = this.LU[i][j];
				else if (i == j)
					arrayOfdouble[i][j] = 1.0f;
				else {
					arrayOfdouble[i][j] = 0.0f;
				}
			}
		}
		return localMatrix;
	}

	public Matrix getU() {
		Matrix localMatrix = new Matrix(this.n, this.n);
		double[][] arrayOfdouble = localMatrix.data;
		for (int i = 0; i < this.n; ++i) {
			for (int j = 0; j < this.n; ++j) {
				if (i <= j)
					arrayOfdouble[i][j] = this.LU[i][j];
				else {
					arrayOfdouble[i][j] = 0.0f;
				}
			}
		}
		return localMatrix;
	}

	public int[] getPivot() {
		int[] arrayOfInt = new int[this.m];
		for (int i = 0; i < this.m; ++i) {
			arrayOfInt[i] = this.piv[i];
		}
		return arrayOfInt;
	}

	public double[] getdoublePivot() {
		double[] arrayOfdouble = new double[this.m];
		for (int i = 0; i < this.m; ++i) {
			arrayOfdouble[i] = this.piv[i];
		}
		return arrayOfdouble;
	}

	public double det() {
		if (this.m != this.n) {
			throw new IllegalArgumentException("Matrix must be square.");
		}
		double d = this.pivsign;
		for (int i = 0; i < this.n; ++i) {
			d *= this.LU[i][i];
		}
		return d;
	}

	public Matrix solve(Matrix paramMatrix) {
		if (paramMatrix.rowNum != this.m) {
			throw new IllegalArgumentException(
					"Matrix row dimensions must agree.");
		}
		if (!(isNonsingular())) {
			throw new RuntimeException("Matrix is singular.");
		}

		int i = paramMatrix.colNum;
		Matrix localMatrix = paramMatrix.getMatrix(this.piv, 0, i - 1);
		double[][] arrayOfdouble = localMatrix.data;
		int k;
		int l;
		int j;
		for (j = 0; j < this.n; ++j) {
			for (k = j + 1; k < this.n; ++k) {
				for (l = 0; l < i; ++l) {
					arrayOfdouble[k][l] -= arrayOfdouble[j][l] * this.LU[k][j];
				}
			}
		}

		for (j = this.n - 1; j >= 0; --j) {
			for (k = 0; k < i; ++k) {
				arrayOfdouble[j][k] /= this.LU[j][j];
			}
			for (k = 0; k < j; ++k) {
				for (l = 0; l < i; ++l) {
					arrayOfdouble[k][l] -= arrayOfdouble[j][l] * this.LU[k][j];
				}
			}
		}
		return localMatrix;
	}
}