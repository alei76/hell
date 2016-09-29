package ps.hell.math.base.persons.fft.zx;

import java.util.Arrays;

public class alg {
	public boolean[] add(boolean[] x, boolean[] y) {
		int size = x.length;
		boolean[] sol = new boolean[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = (((x[i] == false) && (y[i] == false)) ? false : true);
		}
		return sol;
	}

	public boolean[][] add(boolean[][] x, boolean[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		boolean[][] sol = new boolean[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = (((x[i][j] == false) && (y[i][j] == false)) ? false : true);
			}
		}
		return sol;
	}

	public boolean[] mul(boolean[] x, boolean[] y) {
		int size = x.length;
		boolean[] sol = new boolean[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = (((x[i] != false) && (y[i] != false)) ? true : false);
		}
		return sol;
	}

	public boolean[][] mul(boolean[][] x, boolean[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		boolean[][] sol = new boolean[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = (((x[i][j] != false) && (y[i][j] != false)) ? true : false);
			}
		}
		return sol;
	}

	public boolean[] not(boolean[] x) {
		int size = x.length;
		boolean[] sol = new boolean[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = ((x[i] != false) ? false : true);
		}
		return sol;
	}

	public boolean[][] not(boolean[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		boolean[][] sol = new boolean[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = ((x[i][j] !=false) ? false : true);
			}
		}
		return sol;
	}

	public boolean mul(boolean[] x) {
		int size = x.length;
		boolean sol = true;
		for (int i = 0; i < size; ++i) {
			sol = (sol) && (x[i] != false);
		}
		return sol;
	}

	public boolean mul(boolean[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		boolean sol = true;
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol = (sol) && (x[i][j] != false);
			}
		}
		return sol;
	}

	public boolean add(boolean[] x) {
		int size = x.length;
		boolean sol = true;
		for (int i = 0; i < size; ++i) {
			sol = (sol) && (x[i] != false);
		}
		return sol;
	}

	public boolean add(boolean[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		boolean sol = true;
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol = (sol) && (x[i][j] != false);
			}
		}
		return sol;
	}

	public double total(double[] x) {
		double sol = 0.0D;
		int size = x.length;
		for (int i = 0; i < size; ++i) {
			sol += x[i];
		}
		return sol;
	}

	public double prod(double[] x) {
		double sol = 1.0D;
		int size = x.length;
		for (int i = 0; i < size; ++i) {
			sol *= x[i];
		}
		return sol;
	}

	public complex[] add(complex[] x, complex[] y) {
		int size = x.length;
		complex[] sol = new complex[size];
		complex c = new complex();
		for (int i = 0; i < size; ++i) {
			c = c.add(x[i], y[i]);
			sol[i] = c;
		}
		return sol;
	}

	public complex[][] add(complex[][] x, complex[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		complex[][] sol = new complex[size1][size2];
		complex c = new complex();
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				c = c.add(x[i][j], y[i][j]);
				sol[i][j] = c;
			}
		}
		return sol;
	}

	public complex[] sub(complex[] x, complex[] y) {
		int size = x.length;
		complex[] sol = new complex[size];
		complex c = new complex();
		for (int i = 0; i < size; ++i) {
			c = c.sub(x[i], y[i]);
			sol[i] = c;
		}
		return sol;
	}

	public complex[][] sub(complex[][] x, complex[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		complex[][] sol = new complex[size1][size2];
		complex c = new complex();
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				c = c.sub(x[i][j], y[i][j]);
				sol[i][j] = c;
			}
		}
		return sol;
	}

	public complex[] mul(complex[] x, complex[] y) {
		int size = x.length;
		complex[] sol = new complex[size];
		complex c = new complex();
		for (int i = 0; i < size; ++i) {
			sol[i] = c.mul(x[i], y[i]);
		}
		return sol;
	}

	public complex[][] mul(complex[][] x, complex[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		complex[][] sol = new complex[size1][size2];
		complex c = new complex();
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = c.mul(x[i][j], y[i][j]);
			}
		}
		return sol;
	}

	public complex[] div(complex[] x, complex[] y) {
		int size = x.length;
		complex[] sol = new complex[size];
		complex c = new complex();
		for (int i = 0; i < size; ++i) {
			sol[i] = c.div(x[i], y[i]);
		}
		return sol;
	}

	public complex[][] div(complex[][] x, complex[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		complex[][] sol = new complex[size1][size2];
		complex c = new complex();
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = c.div(x[i][j], y[i][j]);
			}
		}
		return sol;
	}

	public complex[] random(int x) {
		complex[] sol = new complex[x];
		complex c = new complex();
		for (int i = 0; i < x; ++i) {
			sol[i] = c.random();
		}
		return sol;
	}

	public complex[][] random(int x, int y) {
		complex[][] sol = new complex[x][y];
		complex c = new complex();
		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; ++j) {
				sol[i][j] = c.random();
			}
		}
		return sol;
	}

	public complex[] complex(int x) {
		int size = x;
		complex[] sol = new complex[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = new complex();
		}
		return sol;
	}

	public complex[][] complex(int x, int y) {
		int size1 = x;
		int size2 = y;
		complex[][] sol = new complex[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = new complex();
			}
		}
		return sol;
	}

	public complex[] complex(double[] x, double[] y) {
		int size = x.length;
		complex[] sol = new complex[size];

		for (int i = 0; i < size; ++i) {
			complex c = new complex();
			c.real = x[i];
			c.imag = y[i];
			sol[i] = c;
		}
		return sol;
	}

	public complex[][] complex(double[][] x, double[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		complex[][] sol = new complex[size1][size2];

		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				complex c = new complex();
				c.real = x[i][j];
				c.imag = y[i][j];
				sol[i][j] = c;
			}
		}
		return sol;
	}

	public complex[][] complex(complex[] x) {
		int size = x.length;
		complex[][] sol = new complex[1][size];
		for (int i = 0; i < size; ++i) {
			complex c = new complex();
			c = x[i];
			sol[0][i] = c;
		}
		return sol;
	}

	public complex[] complex(double[] x) {
		int size = x.length;
		complex[] sol = new complex[size];

		for (int i = 0; i < size; ++i) {
			complex c = new complex();
			c.real = x[i];
			c.imag = 0.0D;
			sol[i] = c;
		}
		return sol;
	}

	public complex[][] complex(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		complex[][] sol = new complex[size1][size2];

		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				complex c = new complex();
				c.real = x[i][j];
				c.imag = 0.0D;
				sol[i][j] = c;
			}
		}
		return sol;
	}

	public double[] getreal(complex[] x) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = x[i].real;
		}
		return sol;
	}

	public double[][] getreal(complex[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = x[i][j].real;
			}
		}
		return sol;
	}

	public double[] getimag(complex[] x) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = x[i].imag;
		}
		return sol;
	}

	public double[][] getimag(complex[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = x[i][j].imag;
			}
		}
		return sol;
	}

	public double[] scale_add(double x, double[] y) {
		int size = y.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = (x + y[i]);
		}
		return sol;
	}

	public double[][] scale_add(double x, double[][] y) {
		int size1 = y.length;
		int size2 = y[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = (x + y[i][j]);
			}
		}
		return sol;
	}

	public double[] scale_sub(double x, double[] y) {
		int size = y.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = (x - y[i]);
		}
		return sol;
	}

	public double[][] scale_sub(double x, double[][] y) {
		int size1 = y.length;
		int size2 = y[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = (x - y[i][j]);
			}
		}
		return sol;
	}

	public double[] scale_mul(double x, double[] y) {
		int size = y.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = (x * y[i]);
		}
		return sol;
	}

	public double[][] scale_mul(double x, double[][] y) {
		int size1 = y.length;
		int size2 = y[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = (x * y[i][j]);
			}
		}
		return sol;
	}

	public double[] scale_div(double x, double[] y) {
		int size = y.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = (x / y[i]);
		}
		return sol;
	}

	public double[][] scale_div(double x, double[][] y) {
		int size1 = y.length;
		int size2 = y[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = (x / y[i][j]);
			}
		}
		return sol;
	}

	public double[] add(double[] x, double[] y) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = (x[i] + y[i]);
		}
		return sol;
	}

	public double[][] add(double[][] x, double[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = (x[i][j] + y[i][j]);
			}
		}
		return sol;
	}

	public double[] sub(double[] x, double[] y) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = (x[i] - y[i]);
		}
		return sol;
	}

	public double[][] sub(double[][] x, double[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = (x[i][j] - y[i][j]);
			}
		}
		return sol;
	}

	public double[] mul(double[] x, double[] y) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = (x[i] * y[i]);
		}
		return sol;
	}

	public double[][] mul(double[][] x, double[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = (x[i][j] * y[i][j]);
			}
		}
		return sol;
	}

	public double[] div(double[] x, double[] y) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = (x[i] / y[i]);
		}
		return sol;
	}

	public double[][] div(double[][] x, double[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = (x[i][j] / y[i][j]);
			}
		}
		return sol;
	}

	public double random(double x, double y) {
		double sol = x + Math.random() * (y - x);
		return sol;
	}

	public double chaos() {
		double tmp = Math.random();
		double sol = 4.0D * tmp * (1.0D - tmp);
		return sol;
	}

	public double chaos(double x, double y) {
		double tmp1 = Math.random();
		double tmp2 = 4.0D * tmp1 * (1.0D - tmp1);
		double sol = x + tmp2 * (y - x);
		return sol;
	}

	public double[] getrow(double[][] x, int y) {
		int size2 = x[0].length;
		double[] sol = new double[size2];
		for (int i = 0; i < size2; ++i) {
			sol[i] = x[y][i];
		}
		return sol;
	}

	public complex[] getrow(complex[][] x, int y) {
		int size2 = x[0].length;
		complex[] sol = new complex[size2];
		for (int i = 0; i < size2; ++i) {
			sol[i] = x[y][i];
		}
		return sol;
	}

	public double[] getcol(double[][] x, int y) {
		int size1 = x.length;
		double[] sol = new double[size1];
		for (int i = 0; i < size1; ++i) {
			sol[i] = x[i][y];
		}
		return sol;
	}

	public complex[] getcol(complex[][] x, int y) {
		int size1 = x.length;
		complex[] sol = new complex[size1];
		for (int i = 0; i < size1; ++i) {
			sol[i] = x[i][y];
		}
		return sol;
	}

	public void set(double[] x, int y, double z) {
		x[y] = z;
	}

	public void set(double[][] x, int y, int z, double w) {
		x[y][z] = w;
	}

	public void set(complex[] x, int y, complex z) {
		x[y] = z;
	}

	public void set(complex[][] x, int y, int z, complex w) {
		x[y][z] = w;
	}

	public void setrow(double[][] x, int y, double[] z) {
		x[y] = z;
	}

	public void setrow(complex[][] x, int y, complex[] z) {
		x[y] = z;
	}

	public void setcol(double[][] x, int y, double[] z) {
		int size1 = x.length;
		int size2 = x[0].length;
		for (int i = 0; i < size1; ++i)
			for (int j = 0; j < size2; ++j)
				x[i][y] = z[i];
	}

	public void setcol(complex[][] x, int y, complex[] z) {
		int size1 = x.length;
		int size2 = x[0].length;
		for (int i = 0; i < size1; ++i)
			for (int j = 0; j < size2; ++j)
				x[i][y] = z[i];
	}

	public double[][] random(int x, double[] low, double[] up) {
		int size = low.length;
		double[][] sol = new double[size][x];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < x; ++j) {
				sol[i][j] = random(low[i], up[i]);
			}
		}
		return sol;
	}

	public double[][] chaos(int x, double[] low, double[] up) {
		int size = low.length;
		double[][] sol = new double[size][x];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < x; ++j) {
				sol[i][j] = chaos(low[i], up[i]);
			}
		}
		return sol;
	}

	public double[] chaos(int x, double low, double up) {
		double[] sol = new double[x];
		for (int j = 0; j < x; ++j) {
			sol[j] = chaos(low, up);
		}
		return sol;
	}

	public double[] random(int x, double low, double up) {
		double[] sol = new double[x];
		for (int j = 0; j < x; ++j) {
			sol[j] = random(low, up);
		}
		return sol;
	}

	public double[][] trans(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size2][size1];
		for (int i = 0; i < size2; ++i) {
			for (int j = 0; j < size1; ++j) {
				sol[i][j] = x[j][i];
			}
		}
		return sol;
	}

	public complex[][] trans(complex[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		complex[][] sol = new complex[size2][size1];
		complex c = new complex();
		for (int i = 0; i < size2; ++i) {
			for (int j = 0; j < size1; ++j) {
				c = x[j][i];
				sol[i][j] = c;
			}
		}
		return sol;
	}

	public complex[][] ctrans(complex[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		complex[][] sol = new complex[size2][size1];
		complex c = new complex();
		for (int i = 0; i < size2; ++i) {
			for (int j = 0; j < size1; ++j) {
				c = c.conj(x[j][i]);
				sol[i][j] = c;
			}
		}
		return sol;
	}

	public double[][] _mul(double[][] x, double[][] y) {
		int size1 = x.length;
		int size2 = y[0].length;
		int size3 = x[0].length;
		double[][] sol = new double[size1][size2];

		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				double tmp = 0.0D;
				for (int k = 0; k < size3; ++k) {
					tmp += x[i][k] * y[k][j];
				}
				sol[i][j] = tmp;
			}
		}
		return sol;
	}

	public complex[][] _mul(complex[][] x, complex[][] y) {
		int size1 = x.length;
		int size2 = y[0].length;
		int size3 = x[0].length;
		complex[][] sol = new complex[size1][size2];

		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				complex c = new complex();
				for (int k = 0; k < size3; ++k) {
					c = c.add(c, c.mul(x[i][k], y[k][j]));
				}
				sol[i][j] = c;
			}
		}
		return sol;
	}

	public double[][] _div_r(double[][] x, double[][] y) {
		double[][] sol = _mul(x, pinv(y));
		return sol;
	}

	public complex[][] _div_r(complex[][] x, complex[][] y) {
		complex[][] sol = _mul(x, pinv(y));
		return sol;
	}

	public double[][] _div_l(double[][] x, double[][] y) {
		double[][] sol = _mul(pinv(x), y);
		return sol;
	}

	public complex[][] _div_l(complex[][] x, complex[][] y) {
		complex[][] sol = _mul(pinv(x), y);
		return sol;
	}

	public double[] linspace(double x, double y, int z) {
		double[] sol = new double[z];
		for (int i = 0; i < z; ++i) {
			sol[i] = (x + (y - x) * i / (z - 1));
		}
		return sol;
	}

	public double[][] eye(int x, int y) {
		double[][] sol = new double[x][y];
		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; ++j) {
				sol[i][j] = ((i == j) ? 1.0D : 0.0D);
			}
		}
		return sol;
	}

	public complex[][] ceye(int x, int y) {
		complex[][] sol = new complex[x][y];
		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; ++j) {
				complex c = new complex();
				c.real = 1.0D;
				c.imag = 0.0D;
				sol[i][j] = c;
			}
		}
		return sol;
	}

	public solution lpmin(double[][] x, double[] y, int[] z) {
		int size1 = x.length;
		int size2 = y.length;
		lpsolve lp = new lpsolve(-1.0D, size1, size2, z[0], z[1], z[2], x, y);
		lp.solve();
		solution sol = new solution();
		sol.fval = lp.fval;
		sol.point = lp.point;
		return sol;
	}

	public solution lpmax(double[][] x, double[] y, int[] z) {
		int size1 = x.length;
		int size2 = y.length;
		lpsolve lp = new lpsolve(1.0D, size1, size2, z[0], z[1], z[2], x, y);
		lp.solve();
		solution sol = new solution();
		sol.fval = lp.fval;
		sol.point = lp.point;
		return sol;
	}

	public double[] sort(double[] x) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = x[i];
		}
		Arrays.sort(sol);
		return sol;
	}

	public double[] quartile1(double[] x) {
		double[] xx = sort(x);
		int size = xx.length;
		double[] sol = new double[3];
		double[] index = new double[3];
		index[0] = ((size + 3.0D) / 4.0D);
		index[1] = ((2.0D * size + 2.0D) / 4.0D);
		index[2] = ((3.0D * size + 1.0D) / 4.0D);

		for (int i = 0; i < 3; ++i) {
			int tmp = (int) index[i];
			if (tmp == index[i]) {
				sol[i] = xx[(tmp - 1)];
			} else {
				double val1 = xx[(tmp - 1)];
				double val2 = xx[tmp];
				sol[i] = (val1 + (val2 - val1) * (index[i] - tmp));
			}
		}
		return sol;
	}

	public double[] quartile2(double[] x) {
		double[] xx = sort(x);
		int size = xx.length;
		double[] sol = new double[3];
		double[] index = new double[3];
		index[0] = ((size + 1.0D) / 4.0D);
		index[1] = ((size + 1.0D) / 2.0D);
		index[2] = (3.0D * (size + 1.0D) / 4.0D);

		for (int i = 0; i < 3; ++i) {
			int tmp = (int) index[i];
			if (tmp == index[i]) {
				sol[i] = xx[(tmp - 1)];
			} else {
				double val1 = xx[(tmp - 1)];
				double val2 = xx[tmp];
				sol[i] = (val1 + (val2 - val1) * (index[i] - tmp));
			}
		}
		return sol;
	}

	public double min(double[] x) {
		double sol = sort(x)[0];
		return sol;
	}

	public int minindex(double[] x) {
		double sol = min(x);
		int size = x.length;
		int index = 0;
		for (int i = 0; i < size; ++i) {
			if (x[i] == sol)
				index = i;
			else {
				index = 0;
			}
		}
		return index;
	}

	public double max(double[] x) {
		double sol = sort(x)[(x.length - 1)];
		return sol;
	}

	public int maxindex(double[] x) {
		double sol = max(x);
		int size = x.length;
		int index = 0;
		for (int i = 0; i < size; ++i) {
			if (x[i] == sol)
				index = i;
			else {
				index = 0;
			}
		}
		return index;
	}

	public int index(double[] x, double y) {
		int size = x.length;
		int index = -1;
		for (int i = 0; i < size; ++i) {
			if (x[i] == y) {
				index = i;
			}
		}
		return index;
	}

	public int index(boolean[] x, boolean y) {
		int size = x.length;
		int index = -1;
		for (int i = 0; i < size; ++i) {
			if (x[i] == y) {
				index = i;
			}
		}
		return index;
	}

	public double[] reverse(double[] x) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = x[(size - 1 - i)];
		}
		return sol;
	}

	public boolean[] find(double[] x, booleanfunction f) {
		int size = x.length;
		boolean[] sol = new boolean[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = f.function(x[i]);
		}
		return sol;
	}

	public boolean[][] find(double[][] x, booleanfunction f) {
		int size1 = x.length;
		int size2 = x[0].length;
		boolean[][] sol = new boolean[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = f.function(x[i][j]);
			}
		}
		return sol;
	}

	private double integration(int n, int[] js, function[] ss, function f) {
		double[] y = new double[2];
		double s = 0.0D;
		double[] t = { -0.9061798459D, -0.5384693101D, 0.0D, 0.5384693101D,
				0.9061798459D };
		double[] c = { 0.2369268851D, 0.4786286705D, 0.5688888889D,
				0.4786286705D, 0.2369268851D };
		int[] is = new int[2 * (n + 1)];
		double[] x = new double[n];
		double[] a = new double[2 * (n + 1)];
		double[] b = new double[n + 1];
		int m = 1;
		int l = 1;
		a[n] = 1.0D;
		a[(2 * n + 1)] = 1.0D;
		while (l == 1) {
			for (int j = m; j <= n; ++j) {
				y = ss[(j - 1)].function(x);
				a[(j - 1)] = (0.5D * (y[1] - y[0]) / js[(j - 1)]);
				b[(j - 1)] = (a[(j - 1)] + y[0]);
				x[(j - 1)] = (a[(j - 1)] * t[0] + b[(j - 1)]);
				a[(n + j)] = 0.0D;
				is[(j - 1)] = 1;
				is[(n + j)] = 1;
			}
			int j = n;
			int q = 1;
			while (q == 1) {
				int k = is[(j - 1)];
				double p;
				if (j == n)
					p = f.function(x)[0];
				else {
					p = 1.0D;
				}
				a[(n + j)] = (a[(n + j + 1)] * a[j] * p * c[(k - 1)] + a[(n + j)]);
				is[(j - 1)] += 1;
				if (is[(j - 1)] > 5) {
					if (is[(n + j)] >= js[(j - 1)]) {
						--j;
						q = 1;
						if (j == 0) {
							s = a[(n + 1)] * a[0];
							return s;
						}
					} else {
						is[(n + j)] += 1;
						b[(j - 1)] += a[(j - 1)] * 2.0D;
						is[(j - 1)] = 1;
						k = is[(j - 1)];
						x[(j - 1)] = (a[(j - 1)] * t[(k - 1)] + b[(j - 1)]);
						if (j == n)
							q = 1;
						else
							q = 0;
					}
				} else {
					k = is[(j - 1)];
					x[(j - 1)] = (a[(j - 1)] * t[(k - 1)] + b[(j - 1)]);
					if (j == n)
						q = 1;
					else {
						q = 0;
					}
				}
			}
			m = j + 1;
		}
		return s;
	}

	public double integration(function f, function[] ss) {
		int n = ss.length;
		int[] js = new int[n];
		for (int i = 0; i < n; ++i) {
			js[i] = 5;
		}
		double sol = integration(n, js, ss, f);
		return sol;
	}

	public double integration(function f, function[] ss, int[] js) {
		int n = ss.length;
		double sol = integration(n, js, ss, f);
		return sol;
	}

	private complex[][] inv(complex[][] xx) {
		int size1 = xx.length;
		int size2 = xx[0].length;
		complex[][] x = new complex[size1][size2];

		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				complex tmp = new complex();
				tmp = xx[i][j];
				x[i][j] = tmp;
			}
		}

		int[] pnrow = new int[size2];
		int[] pncol = new int[size2];
		for (int k = 0; k < size2; ++k) {
			double d = 0.0D;
			for (int i = k; i < size2; ++i) {
				for (int j = k; j < size2; ++j) {
					double p = x[i][j].real * x[i][j].real + x[i][j].imag
							* x[i][j].imag;
					if (p > d) {
						d = p;
						pnrow[k] = i;
						pncol[k] = j;
					}
				}
			}
			if (pnrow[k] != k) {
				for (int j = 0; j < size2; ++j) {
					double t = x[k][j].real;
					x[k][j].real = x[pnrow[k]][j].real;
					x[pnrow[k]][j].real = t;
					t = x[k][j].imag;
					x[k][j].imag = x[pnrow[k]][j].imag;
					x[pnrow[k]][j].imag = t;
				}
			}
			if (pncol[k] != k) {
				for (int i = 0; i < size2; ++i) {
					double t = x[i][k].real;
					x[i][k].real = x[i][pncol[k]].real;
					x[i][pncol[k]].real = t;
					t = x[i][k].imag;
					x[i][k].imag = x[i][pncol[k]].imag;
					x[i][pncol[k]].imag = t;
				}
			}
			x[k][k].real /= d;
			x[k][k].imag = (-x[k][k].imag / d);
			for (int j = 0; j < size2; ++j) {
				if (j != k) {
					double p = x[k][j].real * x[k][k].real;
					double q = x[k][j].imag * x[k][k].imag;
					double s = (x[k][j].real + x[k][j].imag)
							* (x[k][k].real + x[k][k].imag);
					x[k][j].real = (p - q);
					x[k][j].imag = (s - p - q);
				}
			}
			for (int i = 0; i < size2; ++i) {
				if (i != k) {
					for (int j = 0; j < size2; ++j) {
						if (j != k) {
							double p = x[k][j].real * x[i][k].real;
							double q = x[k][j].imag * x[i][k].imag;
							double s = (x[k][j].real + x[k][j].imag)
									* (x[i][k].real + x[i][k].imag);
							double t = p - q;
							double b = s - p - q;
							x[i][j].real -= t;
							x[i][j].imag -= b;
						}
					}
				}
			}
			for (int i = 0; i < size2; ++i) {
				if (i != k) {
					double p = x[i][k].real * x[k][k].real;
					double q = x[i][k].imag * x[k][k].imag;
					double s = (x[i][k].real + x[i][k].imag)
							* (x[k][k].real + x[k][k].imag);
					x[i][k].real = (q - p);
					x[i][k].imag = (p + q - s);
				}
			}
		}
		for (int k = size2 - 1; k >= 0; --k) {
			if (pncol[k] != k) {
				for (int j = 0; j < size2; ++j) {
					double t = x[k][j].real;
					x[k][j].real = x[pncol[k]][j].real;
					x[pncol[k]][j].real = t;
					t = x[k][j].imag;
					x[k][j].imag = x[pncol[k]][j].imag;
					x[pncol[k]][j].imag = t;
				}
			}
			if (pnrow[k] != k) {
				for (int i = 0; i < size2; ++i) {
					double t = x[i][k].real;
					x[i][k].real = x[i][pnrow[k]].real;
					x[i][pnrow[k]].real = t;
					t = x[i][k].imag;
					x[i][k].imag = x[i][pnrow[k]].imag;
					x[i][pnrow[k]].imag = t;
				}
			}
		}
		return x;
	}

	public complex[][] solve(complex[][] x, complex[][] y) {
		int size1 = x.length;
		int size2 = x[0].length;
		complex[][] sol = new complex[size1][size2];
		sol = _mul(inv(x), y);
		return sol;
	}

	public complex[] solve(complex[][] x, complex[] y) {
		int size = y.length;
		complex[][] yy = trans(complex(y));
		return getcol(solve(x, yy), 0);
	}

	public complex[][] pinv(complex[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		complex[][] sol = new complex[size1][size2];
		if (size2 >= size1) {
			complex[][] b = _mul(x, ctrans(x));
			complex[][] tmp = _mul(b, b);
			complex[][] xh = solve(tmp, b);
			complex[][] pinvb = _mul(_mul(ctrans(xh), b), xh);
			sol = _mul(ctrans(x), pinvb);
		} else {
			complex[][] b = _mul(ctrans(x), x);
			complex[][] tmp = _mul(b, b);
			complex[][] xh = solve(tmp, b);
			complex[][] pinvb = _mul(_mul(ctrans(xh), b), xh);
			sol = _mul(pinvb, ctrans(x));
		}
		if ((Double.isNaN(sol[0][0].real)) || (Double.isNaN(sol[0][0].imag))) {
			sol = trans(pinv(trans(x)));
		}
		return sol;
	}

	public double[][] pinv(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] y = new double[size1][size2];
		complex[][] sol = pinv(complex(x, y));
		return getreal(sol);
	}

	public complex[][] pinvsolve(complex[][] x, complex[][] y) {
		return _mul(pinv(x), y);
	}

	public complex[] pinvsolve(complex[][] x, complex[] y) {
		int size = y.length;
		complex[][] yy = trans(complex(y));
		return getcol(pinvsolve(x, yy), 0);
	}

	public double norm(double[] x) {
		double sol = 0.0D;
		int size = x.length;
		for (int i = 0; i < size; ++i) {
			sol += x[i] * x[i];
		}
		return Math.sqrt(sol);
	}

	public double[][] solve(double[][] x, double[][] y) {
		double[][] sol = getreal(solve(complex(x), complex(y)));
		return sol;
	}

	public double[] solve(double[][] x, double[] y) {
		double[] sol = getreal(solve(complex(x), complex(y)));
		return sol;
	}

	public double[][] pinvsolve(double[][] x, double[][] y) {
		double[][] sol = getreal(pinvsolve(complex(x), complex(y)));
		return sol;
	}

	public double[] pinvsolve(double[][] x, double[] y) {
		double[] sol = getreal(pinvsolve(complex(x), complex(y)));
		return sol;
	}

	public double[] copy(double[] x) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = x[i];
		}
		return sol;
	}

	public double[][] copy(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = x[i][j];
			}
		}
		return sol;
	}

	public complex[] copy(complex[] x) {
		int size = x.length;
		complex[] sol = new complex[size];
		for (int i = 0; i < size; ++i) {
			complex c = new complex(x[i].real, x[i].imag);
			sol[i] = c;
		}
		return sol;
	}

	public complex[][] copy(complex[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		complex[][] sol = new complex[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				complex c = new complex(x[i][j].real, x[i][j].imag);
				sol[i][j] = c;
			}
		}
		return sol;
	}

	public complex[][] flinv(complex[][] x) {
		flinv flinv = new flinv(x);
		flinv.eval();
		return flinv.d;
	}

	public double[][] flinv(double[][] x) {
		flinv flinv = new flinv(complex(x));
		flinv.eval();
		return getreal(flinv.d);
	}

	private class flinv {
		private int size;
		private complex[] c;
		private complex[][] d;
		private complex[][][] s;

		private flinv(complex[][] x) {
			this.size = x.length;
			this.c = new complex[this.size];
			this.d = new complex[this.size][this.size];
			for (int i = 0; i < this.size; ++i) {
				this.c[i] = new complex();
				for (int j = 0; j < this.size; ++j) {
					this.d[i][j] = new complex();
				}
			}
			this.s = fl(x, this.c);
		}

		private complex[][][] fl(complex[][] a, complex[] c) {
			int n = this.size;
			complex cc = new complex(0.0D, 0.0D);
			complex[][][] ss = new complex[n][n][n];
			for (int i = 0; i < n; ++i) {
				for (int j = 0; j < n; ++j) {
					for (int k = 0; k < this.size; ++k) {
						ss[i][j][k] = new complex();
					}
				}
			}
			for (int i = 0; i < n; ++i) {
				ss[0][i][i] = new complex(1.0D, 0.0D);
			}
			for (int k = 1; k < n; ++k) {
				ss[k] = mm(a, ss[(k - 1)]);
				c[(n - k)] = cc.div(cc.mul(new complex(-1.0D, 0.0D),
						tr(mm(a, ss[(k - 1)]))), new complex(k, 0.0D));
				for (int i = 0; i < n; ++i) {
					ss[k][i][i] = cc.add(ss[k][i][i], c[(n - k)]);
				}
			}
			c[0] = cc.div(
					cc.mul(new complex(-1.0D, 0.0D), tr(mm(a, ss[(n - 1)]))),
					new complex(n, 0.0D));

			return ss;
		}

		private complex tr(complex[][] a) {
			int n = this.size;
			complex sum = new complex(0.0D, 0.0D);
			for (int i = 0; i < n; ++i) {
				sum = sum.add(sum, a[i][i]);
			}
			return sum;
		}

		private complex[][] mm(complex[][] a, complex[][] b) {
			return alg.this._mul(a, b);
		}

		private void eval() {
			complex cc = new complex();
			for (int i = 0; i < this.size; ++i)
				for (int j = 0; j < this.size; ++j)
					this.d[i][j] = cc.div(cc.mul(new complex(-1.0D, 0.0D),
							this.s[(this.size - 1)][i][j]), this.c[0]);
		}
	}

	private class lpsolve {
		private int m;
		private int n;
		private int m1;
		private int m2;
		private int m3;
		private int error;
		private int[] basic;
		private int[] nonbasic;
		private double[][] a;
		private double minmax;
		private double fval;
		private double[] point;

		private lpsolve(double minmax, int m, int n, int m1, int m2, int m3,
				double[][] a, double[] x) {
			this.error = 0;
			this.minmax = minmax;
			this.m = m;
			this.n = n;
			this.m1 = m1;
			this.m2 = m2;
			this.m3 = m3;
			if (m != m1 + m2 + m3) {
				this.error = 1;
			}
			this.a = new double[m + 2][];
			this.point = new double[n];
			this.fval = 0.0D;
			for (int i = 0; i < m + 2; ++i) {
				this.a[i] = new double[n + m + m3 + 1];
			}
			this.basic = new int[m + 2];
			this.nonbasic = new int[n + m3 + 1];
			for (int i = 0; i <= m + 1; ++i) {
				for (int j = 0; j <= n + m + m3; ++j) {
					this.a[i][j] = 0.0D;
				}
			}
			for (int j = 0; j <= n + m3; ++j) {
				this.nonbasic[j] = j;
			}
			int i = 1;
			for (int j = n + m3 + 1; i <= m; ++j) {
				this.basic[i] = j;

				++i;
			}

			i = m - m3 + 1;
			for (int j = n + 1; i <= m; ++j) {
				this.a[i][j] = -1.0D;
				this.a[(m + 1)][j] = -1.0D;

				++i;
			}

			for (i = 1; i <= m; ++i) {
				double value;
				for (int j = 1; j <= n; ++j) {
					value = a[(i - 1)][(j - 1)];
					this.a[i][j] = value;
				}
				value = a[(i - 1)][n];
				if (value < 0.0D) {
					this.error = 1;
				}
				this.a[i][0] = value;
			}
			for (int j = 1; j <= n; ++j) {
				double value = x[(j - 1)];
				this.a[0][j] = (value * minmax);
			}
			for (int j = 1; j <= n; ++j) {
				double value = 0.0D;
				for (i = m1 + 1; i <= m; ++i) {
					value += this.a[i][j];
				}
				this.a[(m + 1)][j] = value;
			}
		}

		private int enter(int objrow) {
			int col = 0;
			for (int j = 1; j <= this.n + this.m3; ++j) {
				if ((this.nonbasic[j] > this.n + this.m1 + this.m3)
						|| (this.a[objrow][j] <= 1.0E-007D))
					continue;
				col = j;
				break;
			}

			return col;
		}

		private int leave(int col) {
			double temp = -1.0D;
			int row = 0;
			for (int i = 1; i <= this.m; ++i) {
				double val = this.a[i][col];
				if (val > 1.0E-007D) {
					val = this.a[i][0] / val;
					if ((val < temp) || (temp == -1.0D)) {
						row = i;
						temp = val;
					}
				}
			}
			return row;
		}

		private void swapbasic(int row, int col) {
			int temp = this.basic[row];
			this.basic[row] = this.nonbasic[col];
			this.nonbasic[col] = temp;
		}

		private void pivot(int row, int col) {
			for (int j = 0; j <= this.n + this.m3; ++j) {
				if (j != col) {
					this.a[row][j] /= this.a[row][col];
				}
			}
			this.a[row][col] = (1.0D / this.a[row][col]);
			for (int i = 0; i <= this.m + 1; ++i) {
				if (i != row) {
					for (int j = 0; j <= this.n + this.m3; ++j) {
						if (j != col) {
							this.a[i][j] -= this.a[i][col] * this.a[row][j];
							if (Math.abs(this.a[i][j]) < 1.0E-007D) {
								this.a[i][j] = 0.0D;
							}
						}
					}
					this.a[i][col] = (-this.a[i][col] * this.a[row][col]);
				}
			}
			swapbasic(row, col);
		}

		private int simplex(int objrow) {
			int row = 0;
			while (true) {
				int col = enter(objrow);
				if (col > 0)
					row = leave(col);
				else {
					return 0;
				}
				if (row <= 0)
					break;
				pivot(row, col);
			}
			return 2;
		}

		private int phase1() {
			this.error = simplex(this.m + 1);
			if (this.error > 0) {
				return this.error;
			}
			for (int i = 1; i <= this.m; ++i) {
				if (this.basic[i] > this.n + this.m1 + this.m3) {
					if (this.a[i][0] > 1.0E-007D) {
						return 3;
					}
					for (int j = 1; j <= this.n; ++j) {
						if (Math.abs(this.a[i][j]) >= 1.0E-007D) {
							pivot(i, j);
							break;
						}
					}
				}
			}
			return 0;
		}

		private int phase2() {
			return simplex(0);
		}

		private int compute() {
			if (this.error > 0) {
				return this.error;
			}
			if (this.m != this.m1) {
				this.error = phase1();
				if (this.error > 0) {
					return this.error;
				}
			}
			return phase2();
		}

		private void solve() {
			this.error = compute();
			switch (this.error) {
			case 0:
				output();
				break;
			case 1:
				System.out.println("输入数据错误!");
				break;
			case 2:
				System.out.println("无界解!");
				break;
			case 3:
				System.out.println("无可行解!");
			}
		}

		private void output() {
			int[] basicp = new int[this.n + 1];
			for (int i = 0; i <= this.n; ++i) {
				basicp[i] = 0;
			}
			for (int i = 1; i <= this.m; ++i) {
				if ((this.basic[i] >= 1) && (this.basic[i] <= this.n)) {
					basicp[this.basic[i]] = i;
				}
			}
			for (int i = 1; i <= this.n; ++i) {
				if (basicp[i] != 0)
					this.point[(i - 1)] = this.a[basicp[i]][0];
				else {
					this.point[(i - 1)] = 0.0D;
				}
			}
			this.fval = (-this.minmax * this.a[0][0]);
		}
	}
}