package ps.hell.math.base.persons.fft.transform.fft;//package ps.landerbuluse.ml.math.fft.transform.fft;
//
//import ps.landerbuluse.ml.math.fft.transform.util.IOUtils;
//
//public class AccuracyCheckDoubleFFT {
//	private static int[] sizes1D = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 120, 128, 256, 310, 512, 1024, 1056, 2048, 8192,
//			10158, 16384, 32768, 65530, 65536, 131072 };
//	private static int[] sizes2D = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 120, 128, 256, 310, 511, 512, 1024 };
//	private static int[] sizes3D = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 128 };
//	private static int[] sizes2D2 = { 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024 };
//	private static int[] sizes3D2 = { 2, 4, 8, 16, 32, 64, 128 };
//	private static double eps = Math.pow(2.0D, -52.0D);
//
//	public static void checkAccuracyComplexFFT_1D() {
//		System.out.println("Checking accuracy of 1D complex FFT...");
//		for (int i = 0; i < sizes1D.length; ++i) {
//			DoubleFFT_1D localDoubleFFT_1D = new DoubleFFT_1D(sizes1D[i]);
//			double d = 0.0D;
//			double[] arrayOfDouble1 = new double[2 * sizes1D[i]];
//			IOUtils.fillMatrix_1D(2 * sizes1D[i], arrayOfDouble1);
//			double[] arrayOfDouble2 = new double[2 * sizes1D[i]];
//			IOUtils.fillMatrix_1D(2 * sizes1D[i], arrayOfDouble2);
//			localDoubleFFT_1D.complexForward(arrayOfDouble1);
//			localDoubleFFT_1D.complexInverse(arrayOfDouble1, true);
//			d = computeRMSE(arrayOfDouble1, arrayOfDouble2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			arrayOfDouble1 = null;
//			arrayOfDouble2 = null;
//			localDoubleFFT_1D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyComplexFFT_2D() {
//		System.out
//				.println("Checking accuracy of 2D complex FFT (double[] input)...");
//		DoubleFFT_2D localDoubleFFT_2D;
//		double d;
//		Object localObject1;
//		Object localObject2;
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localDoubleFFT_2D = new DoubleFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			localObject1 = new double[2 * sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i], (double[])localObject1);
//			localObject2 = new double[2 * sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i],  (double[])localObject2);
//			localDoubleFFT_2D.complexForward( (double[])localObject1);
//			localDoubleFFT_2D.complexInverse( (double[])localObject1, true);
//			d = computeRMSE( (double[])localObject1,  (double[])localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localDoubleFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D complex FFT (double[][] input)...");
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localDoubleFFT_2D = new DoubleFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			localObject1 = new double[sizes2D[i]][2 * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i],  (double[][])localObject1);
//			localObject2 = new double[sizes2D[i]][2 * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i],  (double[][])localObject2);
//			localDoubleFFT_2D.complexForward( (double[][])localObject1);
//			localDoubleFFT_2D.complexInverse( (double[][])localObject1, true);
//			d = computeRMSE( (double[][])localObject1,  (double[][])localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = (double[][]) null;
//			localObject2 = (double[][]) null;
//			localDoubleFFT_2D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyComplexFFT_3D() {
//		System.out
//				.println("Checking accuracy of 3D complex FFT (double[] input)...");
//		DoubleFFT_3D localDoubleFFT_3D;
//		double d;
//		Object localObject1;
//		Object localObject2;
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localDoubleFFT_3D = new DoubleFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			localObject1 = new double[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//					(double[])localObject1);
//			localObject2 = new double[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//					(double[])localObject2);
//			localDoubleFFT_3D.complexForward((double[])localObject1);
//			localDoubleFFT_3D.complexInverse((double[])localObject1, true);
//			d = computeRMSE((double[])localObject1, (double[])localObject2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localDoubleFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D complex FFT (double[][][] input)...");
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localDoubleFFT_3D = new DoubleFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			localObject1 = new double[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//					(double[][][])localObject1);
//			localObject2 = new double[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//					(double[][][])localObject2);
//			localDoubleFFT_3D.complexForward((double[][][])localObject1);
//			localDoubleFFT_3D.complexInverse((double[][][])localObject1, true);
//			d = computeRMSE((double[][][])localObject1, (double[][][])localObject2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			localObject1 = (double[][][]) null;
//			localObject2 = (double[][][]) null;
//			localDoubleFFT_3D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyRealFFT_1D() {
//		System.out.println("Checking accuracy of 1D real FFT...");
//		DoubleFFT_1D localDoubleFFT_1D;
//		double d;
//		double[] arrayOfDouble1;
//		double[] arrayOfDouble2;
//		for (int i = 0; i < sizes1D.length; ++i) {
//			localDoubleFFT_1D = new DoubleFFT_1D(sizes1D[i]);
//			d = 0.0D;
//			arrayOfDouble1 = new double[sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfDouble1);
//			arrayOfDouble2 = new double[sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfDouble2);
//			localDoubleFFT_1D.realForward(arrayOfDouble1);
//			localDoubleFFT_1D.realInverse(arrayOfDouble1, true);
//			d = computeRMSE(arrayOfDouble1, arrayOfDouble2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			arrayOfDouble1 = null;
//			arrayOfDouble2 = null;
//			localDoubleFFT_1D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of on 1D real forward full FFT...");
//		int j;
//		for (int i = 0; i < sizes1D.length; ++i) {
//			localDoubleFFT_1D = new DoubleFFT_1D(sizes1D[i]);
//			d = 0.0D;
//			arrayOfDouble1 = new double[2 * sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfDouble1);
//			arrayOfDouble2 = new double[2 * sizes1D[i]];
//			for (j = 0; j < sizes1D[i]; ++j)
//				arrayOfDouble2[(2 * j)] = arrayOfDouble1[j];
//			localDoubleFFT_1D.realForwardFull(arrayOfDouble1);
//			localDoubleFFT_1D.complexInverse(arrayOfDouble1, true);
//			d = computeRMSE(arrayOfDouble1, arrayOfDouble2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			arrayOfDouble1 = null;
//			arrayOfDouble2 = null;
//			localDoubleFFT_1D = null;
//			System.gc();
//		}
//		System.out.println("Checking accuracy of 1D real inverse full FFT...");
//		for (int i = 0; i < sizes1D.length; ++i) {
//			localDoubleFFT_1D = new DoubleFFT_1D(sizes1D[i]);
//			d = 0.0D;
//			arrayOfDouble1 = new double[2 * sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfDouble1);
//			arrayOfDouble2 = new double[2 * sizes1D[i]];
//			for (j = 0; j < sizes1D[i]; ++j)
//				arrayOfDouble2[(2 * j)] = arrayOfDouble1[j];
//			localDoubleFFT_1D.realInverseFull(arrayOfDouble1, true);
//			localDoubleFFT_1D.complexForward(arrayOfDouble1);
//			d = computeRMSE(arrayOfDouble1, arrayOfDouble2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			arrayOfDouble1 = null;
//			arrayOfDouble2 = null;
//			localDoubleFFT_1D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyRealFFT_2D() {
//		System.out
//				.println("Checking accuracy of 2D real FFT (double[] input)...");
//		DoubleFFT_2D localDoubleFFT_2D;
//		double d;
//		for (int i = 0; i < sizes2D2.length; ++i) {
//			localDoubleFFT_2D = new DoubleFFT_2D(sizes2D2[i], sizes2D2[i]);
//			d = 0.0D;
//			double[] localObject1 = new double[sizes2D2[i] * sizes2D2[i]];
//			IOUtils.fillMatrix_2D(sizes2D2[i], sizes2D2[i],localObject1);
//			double[] localObject2 = new double[sizes2D2[i] * sizes2D2[i]];
//			IOUtils.fillMatrix_2D(sizes2D2[i], sizes2D2[i], localObject2);
//			localDoubleFFT_2D.realForward(localObject1);
//			localDoubleFFT_2D.realInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D2[i] + " x "
//						+ sizes2D2[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D2[i] + " x "
//						+ sizes2D2[i] + ";\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localDoubleFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D real FFT (double[][] input)...");
//		for (int i = 0; i < sizes2D2.length; ++i) {
//			localDoubleFFT_2D = new DoubleFFT_2D(sizes2D2[i], sizes2D2[i]);
//			d = 0.0D;
//			double[][] localObject1 = new double[sizes2D2[i]][sizes2D2[i]];
//			IOUtils.fillMatrix_2D(sizes2D2[i], sizes2D2[i], localObject1);
//			double[][] localObject2 = new double[sizes2D2[i]][sizes2D2[i]];
//			IOUtils.fillMatrix_2D(sizes2D2[i], sizes2D2[i], localObject2);
//			localDoubleFFT_2D.realForward(localObject1);
//			localDoubleFFT_2D.realInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D2[i] + " x "
//						+ sizes2D2[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D2[i] + " x "
//						+ sizes2D2[i] + ";\terror = " + d);
//			localObject1 = (double[][]) null;
//			localObject2 = (double[][]) null;
//			localDoubleFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D real forward full FFT (double[] input)...");
//		int j;
//		int k;
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localDoubleFFT_2D = new DoubleFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			double[] localObject1 = new double[2 * sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject1);
//			double[] localObject2 = new double[2 * sizes2D[i] * sizes2D[i]];
//			for (j = 0; j < sizes2D[i]; ++j)
//				for (k = 0; k < sizes2D[i]; ++k)
//					localObject2[(j * 2 * sizes2D[i] + 2 * k)] = localObject1[(j
//							* sizes2D[i] + k)];
//			localDoubleFFT_2D.realForwardFull(localObject1);
//			localDoubleFFT_2D.complexInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localDoubleFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D real forward full FFT (double[][] input)...");
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localDoubleFFT_2D = new DoubleFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			double[][] localObject1 = new double[sizes2D[i]][2 * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject1);
//			double[][] localObject2 = new double[sizes2D[i]][2 * sizes2D[i]];
//			for (j = 0; j < sizes2D[i]; ++j)
//				for (k = 0; k < sizes2D[i]; ++k)
//					localObject2[j][(2 * k)] = localObject1[j][k];
//			localDoubleFFT_2D.realForwardFull(localObject1);
//			localDoubleFFT_2D.complexInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = (double[][]) null;
//			localObject2 = (double[][]) null;
//			localDoubleFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D real inverse full FFT (double[] input)...");
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localDoubleFFT_2D = new DoubleFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			double[] localObject1 = new double[2 * sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject1);
//			double[] localObject2 = new double[2 * sizes2D[i] * sizes2D[i]];
//			for (j = 0; j < sizes2D[i]; ++j)
//				for (k = 0; k < sizes2D[i]; ++k)
//					localObject2[(j * 2 * sizes2D[i] + 2 * k)] = localObject1[(j
//							* sizes2D[i] + k)];
//			localDoubleFFT_2D.realInverseFull(localObject1, true);
//			localDoubleFFT_2D.complexForward(localObject1);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localDoubleFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D real inverse full FFT (double[][] input)...");
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localDoubleFFT_2D = new DoubleFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			double[][] localObject1 = new double[sizes2D[i]][2 * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject1);
//			double[][] localObject2 = new double[sizes2D[i]][2 * sizes2D[i]];
//			for (j = 0; j < sizes2D[i]; ++j)
//				for (k = 0; k < sizes2D[i]; ++k)
//					localObject2[j][(2 * k)] = localObject1[j][k];
//			localDoubleFFT_2D.realInverseFull(localObject1, true);
//			localDoubleFFT_2D.complexForward(localObject1);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = (double[][]) null;
//			localObject2 = (double[][]) null;
//			localDoubleFFT_2D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyRealFFT_3D() {
//		System.out
//				.println("Checking accuracy of 3D real FFT (double[] input)...");
//		DoubleFFT_3D localDoubleFFT_3D;
//		double d;
//		for (int i = 0; i < sizes3D2.length; ++i) {
//			localDoubleFFT_3D = new DoubleFFT_3D(sizes3D2[i], sizes3D2[i],
//					sizes3D2[i]);
//			d = 0.0D;
//			double[] localObject1 = new double[sizes3D2[i] * sizes3D2[i] * sizes3D2[i]];
//			IOUtils.fillMatrix_3D(sizes3D2[i], sizes3D2[i], sizes3D2[i],
//					localObject1);
//			double[] localObject2 = new double[sizes3D2[i] * sizes3D2[i] * sizes3D2[i]];
//			IOUtils.fillMatrix_3D(sizes3D2[i], sizes3D2[i], sizes3D2[i],
//					localObject2);
//			localDoubleFFT_3D.realForward(localObject2);
//			localDoubleFFT_3D.realInverse(localObject2, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes3D2[i] + " x "
//						+ sizes3D2[i] + " x " + sizes3D2[i] + ";\t\terror = "
//						+ d);
//			else
//				System.out.println("\tsize = " + sizes3D2[i] + " x "
//						+ sizes3D2[i] + " x " + sizes3D2[i] + ";\t\terror = "
//						+ d);
//			localObject1 = null;
//			localObject2 = null;
//			localDoubleFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D real FFT (double[][][] input)...");
//		for (int i = 0; i < sizes3D2.length; ++i) {
//			localDoubleFFT_3D = new DoubleFFT_3D(sizes3D2[i], sizes3D2[i],
//					sizes3D2[i]);
//			d = 0.0D;
//			double[][][] localObject1 = new double[sizes3D2[i]][sizes3D2[i]][sizes3D2[i]];
//			IOUtils.fillMatrix_3D(sizes3D2[i], sizes3D2[i], sizes3D2[i],
//					localObject1);
//			double[][][] localObject2 = new double[sizes3D2[i]][sizes3D2[i]][sizes3D2[i]];
//			IOUtils.fillMatrix_3D(sizes3D2[i], sizes3D2[i], sizes3D2[i],
//					localObject2);
//			localDoubleFFT_3D.realForward(localObject2);
//			localDoubleFFT_3D.realInverse(localObject2, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes3D2[i] + " x "
//						+ sizes3D2[i] + " x " + sizes3D2[i] + ";\t\terror = "
//						+ d);
//			else
//				System.out.println("\tsize = " + sizes3D2[i] + " x "
//						+ sizes3D2[i] + " x " + sizes3D2[i] + ";\t\terror = "
//						+ d);
//			localObject1 = (double[][][]) null;
//			localObject2 = (double[][][]) null;
//			localDoubleFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D real forward full FFT (double[] input)...");
//		int j;
//		int k;
//		int l;
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localDoubleFFT_3D = new DoubleFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			double[] localObject1 = new double[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject1);
//			double[] localObject2 = new double[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			for (j = 0; j < sizes3D[i]; ++j)
//				for (k = 0; k < sizes3D[i]; ++k)
//					for (l = 0; l < sizes3D[i]; ++l)
//						localObject2[(j * 2 * sizes3D[i] * sizes3D[i] + k * 2
//								* sizes3D[i] + 2 * l)] = localObject1[(j
//								* sizes3D[i] * sizes3D[i] + k * sizes3D[i] + l)];
//			localDoubleFFT_3D.realForwardFull(localObject1);
//			localDoubleFFT_3D.complexInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localDoubleFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D real forward full FFT (double[][][] input)...");
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localDoubleFFT_3D = new DoubleFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			double[][][] localObject1 = new double[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject1);
//			double[][][] localObject2 = new double[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			for (j = 0; j < sizes3D[i]; ++j)
//				for (k = 0; k < sizes3D[i]; ++k)
//					for (l = 0; l < sizes3D[i]; ++l)
//						localObject2[j][k][(2 * l)] = localObject1[j][k][l];
//			localDoubleFFT_3D.realForwardFull(localObject1);
//			localDoubleFFT_3D.complexInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			localObject1 = (double[][][]) null;
//			localObject2 = (double[][][]) null;
//			localDoubleFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D real inverse full FFT (double[] input)...");
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localDoubleFFT_3D = new DoubleFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			double[] localObject1 = new double[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject1);
//			double[] localObject2 = new double[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			for (j = 0; j < sizes3D[i]; ++j)
//				for (k = 0; k < sizes3D[i]; ++k)
//					for (l = 0; l < sizes3D[i]; ++l)
//						localObject2[(j * 2 * sizes3D[i] * sizes3D[i] + k * 2
//								* sizes3D[i] + 2 * l)] = localObject1[(j
//								* sizes3D[i] * sizes3D[i] + k * sizes3D[i] + l)];
//			localDoubleFFT_3D.realInverseFull(localObject1, true);
//			localDoubleFFT_3D.complexForward(localObject1);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localDoubleFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D real inverse full FFT (double[][][] input)...");
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localDoubleFFT_3D = new DoubleFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			double[][][] localObject1 = new double[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject1);
//			double[][][] localObject2 = new double[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			for (j = 0; j < sizes3D[i]; ++j)
//				for (k = 0; k < sizes3D[i]; ++k)
//					for (l = 0; l < sizes3D[i]; ++l)
//						localObject2[j][k][(2 * l)] = localObject1[j][k][l];
//			localDoubleFFT_3D.realInverseFull(localObject1, true);
//			localDoubleFFT_3D.complexForward(localObject1);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			localObject1 = (double[][][]) null;
//			localObject2 = (double[][][]) null;
//			localDoubleFFT_3D = null;
//			System.gc();
//		}
//	}
//
//	private static double computeRMSE(double[] paramArrayOfDouble1,
//			double[] paramArrayOfDouble2) {
//		if (paramArrayOfDouble1.length != paramArrayOfDouble2.length)
//			throw new IllegalArgumentException("Arrays are not the same size.");
//		double d1 = 0.0D;
//		for (int i = 0; i < paramArrayOfDouble1.length; ++i) {
//			double d2 = paramArrayOfDouble1[i] - paramArrayOfDouble2[i];
//			d1 += d2 * d2;
//		}
//		return Math.sqrt(d1 / paramArrayOfDouble1.length);
//	}
//
//	private static double computeRMSE(double[][] paramArrayOfDouble1,
//			double[][] paramArrayOfDouble2) {
//		if ((paramArrayOfDouble1.length != paramArrayOfDouble2.length)
//				|| (paramArrayOfDouble1[0].length != paramArrayOfDouble2[0].length))
//			throw new IllegalArgumentException("Arrays are not the same size.");
//		double d1 = 0.0D;
//		for (int i = 0; i < paramArrayOfDouble1.length; ++i)
//			for (int j = 0; j < paramArrayOfDouble1[0].length; ++j) {
//				double d2 = paramArrayOfDouble1[i][j]
//						- paramArrayOfDouble2[i][j];
//				d1 += d2 * d2;
//			}
//		return Math.sqrt(d1 / paramArrayOfDouble1.length
//				* paramArrayOfDouble1[0].length);
//	}
//
//	private static double computeRMSE(double[][][] paramArrayOfDouble1,
//			double[][][] paramArrayOfDouble2) {
//		if ((paramArrayOfDouble1.length != paramArrayOfDouble2.length)
//				|| (paramArrayOfDouble1[0].length != paramArrayOfDouble2[0].length)
//				|| (paramArrayOfDouble1[0][0].length != paramArrayOfDouble2[0][0].length))
//			throw new IllegalArgumentException("Arrays are not the same size.");
//		double d1 = 0.0D;
//		for (int i = 0; i < paramArrayOfDouble1.length; ++i)
//			for (int j = 0; j < paramArrayOfDouble1[0].length; ++j)
//				for (int k = 0; k < paramArrayOfDouble1[0][0].length; ++k) {
//					double d2 = paramArrayOfDouble1[i][j][k]
//							- paramArrayOfDouble2[i][j][k];
//					d1 += d2 * d2;
//				}
//		return Math.sqrt(d1 / paramArrayOfDouble1.length
//				* paramArrayOfDouble1[0].length
//				* paramArrayOfDouble1[0][0].length);
//	}
//
//	public static void main(String[] paramArrayOfString) {
//		checkAccuracyComplexFFT_1D();
//		checkAccuracyRealFFT_1D();
//		checkAccuracyComplexFFT_2D();
//		checkAccuracyRealFFT_2D();
//		checkAccuracyComplexFFT_3D();
//		checkAccuracyRealFFT_3D();
//		System.exit(0);
//	}
//}