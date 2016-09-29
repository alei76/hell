package ps.hell.math.base.persons.fft.test;

import edu.emory.mathcs.jtransforms.dct.AccuracyCheckDoubleDCT;
import edu.emory.mathcs.jtransforms.dct.DoubleDCT_1D;
import edu.emory.mathcs.jtransforms.dct.DoubleDCT_2D;
import edu.emory.mathcs.jtransforms.dht.DoubleDHT_1D;
import edu.emory.mathcs.jtransforms.dht.DoubleDHT_2D;
import edu.emory.mathcs.jtransforms.dst.DoubleDST_1D;
import edu.emory.mathcs.jtransforms.dst.DoubleDST_2D;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_2D;
import zx.alg;
import zx.complex;

public class jtrans {
	private complex[] complex(double[] x) {
		int size = x.length / 2;
		double[] real = new double[size];
		double[] imag = new double[size];
		for (int i = 0; i < size; ++i) {
			real[i] = x[(2 * i)];
			imag[i] = x[(2 * i + 1)];
		}
		alg alg = new alg();
		return alg.complex(real, imag);
	}

	public complex[] fft(complex[] x) {
		int size = x.length;
		double s = Math.sqrt(size);
		double[] sol = new double[2 * size];
		for (int i = 0; i < size; ++i) {
			sol[(2 * i)] = (x[i].real / s);
			sol[(2 * i + 1)] = (x[i].imag / s);
		}
		DoubleFFT_1D FFT = new DoubleFFT_1D(size);
		FFT.complexForward(sol);
		return complex(sol);
	}

	public complex[] fft(double[] x) {
		int size = x.length;
		double s = Math.sqrt(size);
		double[] sol = new double[2 * size];
		for (int i = 0; i < size; ++i) {
			sol[(2 * i)] = (x[i] / s);
			sol[(2 * i + 1)] = 0.0D;
		}
		DoubleFFT_1D FFT = new DoubleFFT_1D(size);
		FFT.complexForward(sol);
		return complex(sol);
	}

	public complex[] ifft(complex[] x) {
		int size = x.length;
		double s = Math.sqrt(size);
		double[] sol = new double[2 * size];
		for (int i = 0; i < size; ++i) {
			sol[(2 * i)] = (x[i].real * s);
			sol[(2 * i + 1)] = (x[i].imag * s);
		}
		DoubleFFT_1D FFT = new DoubleFFT_1D(size);
		FFT.complexInverse(sol, true);
		return complex(sol);
	}

	public complex[] ifft(double[] x) {
		int size = x.length;
		double s = Math.sqrt(size);
		double[] sol = new double[2 * size];
		for (int i = 0; i < size; ++i) {
			sol[(2 * i)] = (x[i] * s);
			sol[(2 * i + 1)] = 0.0D;
		}
		DoubleFFT_1D FFT = new DoubleFFT_1D(size);
		FFT.complexInverse(sol, true);
		return complex(sol);
	}

	private complex[][] complex(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length / 2;
		double[][] real = new double[size1][size2];
		double[][] imag = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				real[i][j] = x[i][(2 * j)];
				imag[i][j] = x[i][(2 * j + 1)];
			}
		}
		alg alg = new alg();
		return alg.complex(real, imag);
	}

	public complex[][] fft(complex[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double s = Math.sqrt(size1 * size2);
		double[][] sol = new double[size1][2 * size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][(2 * j)] = (x[i][j].real / s);
				sol[i][(2 * j + 1)] = (x[i][j].imag / s);
			}
		}
		DoubleFFT_2D FFT = new DoubleFFT_2D(size1, size2);
		FFT.complexForward(sol);
		return complex(sol);
	}

	public complex[][] fft(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double s = Math.sqrt(size1 * size2);
		double[][] sol = new double[size1][2 * size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][(2 * j)] = (x[i][j] / s);
				sol[i][(2 * j + 1)] = 0.0D;
			}
		}
		DoubleFFT_2D FFT = new DoubleFFT_2D(size1, size2);
		FFT.complexForward(sol);
		alg c = new alg();
		return complex(sol);
	}

	public complex[][] ifft(complex[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double s = Math.sqrt(size1 * size2);
		double[][] sol = new double[size1][2 * size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][(2 * j)] = (x[i][j].real * s);
				sol[i][(2 * j + 1)] = (x[i][j].imag * s);
			}
		}
		DoubleFFT_2D FFT = new DoubleFFT_2D(size1, size2);
		FFT.complexInverse(sol, true);
		return complex(sol);
	}

	public complex[][] ifft(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double s = Math.sqrt(size1 * size2);
		double[][] sol = new double[size1][2 * size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][(2 * j)] = (x[i][j] * s);
				sol[i][(2 * j + 1)] = 0.0D;
			}
		}
		DoubleFFT_2D FFT = new DoubleFFT_2D(size1, size2);
		FFT.complexInverse(sol, true);
		return complex(sol);
	}

	public double[] dct(double[] x) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = x[i];
		}
		DoubleDCT_1D DCT = new DoubleDCT_1D(size);
		DCT.forward(sol, true);
		return sol;
	}

	public double[][] dct(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = x[i][j];
			}
		}
		DoubleDCT_2D DCT = new DoubleDCT_2D(size1, size2);
		DCT.forward(sol, true);
		return sol;
	}

	public double[] idct(double[] x) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = x[i];
		}
		DoubleDCT_1D DCT = new DoubleDCT_1D(size);
		DCT.inverse(sol, true);
		return sol;
	}

	public double[][] idct(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = x[i][j];
			}
		}
		DoubleDCT_2D DCT = new DoubleDCT_2D(size1, size2);
		DCT.inverse(sol, true);
		return sol;
	}

	public double[] dht(double[] x) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = x[i];
		}
		DoubleDHT_1D DHT = new DoubleDHT_1D(size);
		DHT.forward(sol);
		return sol;
	}

	public double[][] dht(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = x[i][j];
			}
		}
		DoubleDHT_2D DHT = new DoubleDHT_2D(size1, size2);
		DHT.forward(sol);
		return sol;
	}

	public double[] idht(double[] x) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = x[i];
		}
		DoubleDHT_1D DHT = new DoubleDHT_1D(size);
		DHT.inverse(sol, true);
		return sol;
	}

	public double[][] idht(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = x[i][j];
			}
		}
		DoubleDHT_2D DHT = new DoubleDHT_2D(size1, size2);
		DHT.inverse(sol, true);
		return sol;
	}

	public double[] dst(double[] x) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = x[i];
		}
		DoubleDST_1D DST = new DoubleDST_1D(size);
		DST.forward(sol, true);
		return sol;
	}

	public double[][] dst(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = x[i][j];
			}
		}
		DoubleDST_2D DST = new DoubleDST_2D(size1, size2);
		DST.forward(sol, true);
		return sol;
	}

	public double[] idst(double[] x) {
		int size = x.length;
		double[] sol = new double[size];
		for (int i = 0; i < size; ++i) {
			sol[i] = x[i];
		}
		DoubleDST_1D DST = new DoubleDST_1D(size);
		DST.inverse(sol, true);
		return sol;
	}

	public double[][] idst(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		double[][] sol = new double[size1][size2];
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				sol[i][j] = x[i][j];
			}
		}
		DoubleDST_2D DST = new DoubleDST_2D(size1, size2);
		DST.inverse(sol, true);
		return sol;
	}
	public static void main(String[] args) {
		AccuracyCheckDoubleDCT.checkAccuracyDCT_1D();
		AccuracyCheckDoubleDCT.checkAccuracyDCT_2D();
		AccuracyCheckDoubleDCT.checkAccuracyDCT_3D();
		System.exit(0);
	}
}