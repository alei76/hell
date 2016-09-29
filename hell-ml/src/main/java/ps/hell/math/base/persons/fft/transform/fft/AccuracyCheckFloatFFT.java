package ps.hell.math.base.persons.fft.transform.fft;//package ps.landerbuluse.ml.math.fft.transform.fft;
//
//import edu.emory.mathcs.utils.IOUtils;
//import java.io.PrintStream;
//
//public class AccuracyCheckFloatFFT {
//	private static int[] sizes1D = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 120, 128, 256, 310, 512, 1024, 1056, 2048, 8192,
//			10158, 16384, 32768, 65530, 65536, 131072 };
//	private static int[] sizes2D = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 120, 128, 256, 310, 511, 512, 1024 };
//	private static int[] sizes3D = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 128 };
//	private static int[] sizes2D2 = { 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024 };
//	private static int[] sizes3D2 = { 2, 4, 8, 16, 32, 64, 128 };
//	private static double eps = Math.pow(2.0D, -23.0D);
//
//	public static void checkAccuracyComplexFFT_1D() {
//		System.out.println("Checking accuracy of 1D complex FFT...");
//		for (int i = 0; i < sizes1D.length; ++i) {
//			FloatFFT_1D localFloatFFT_1D = new FloatFFT_1D(sizes1D[i]);
//			double d = 0.0D;
//			float[] arrayOfFloat1 = new float[2 * sizes1D[i]];
//			IOUtils.fillMatrix_1D(2 * sizes1D[i], arrayOfFloat1);
//			float[] arrayOfFloat2 = new float[2 * sizes1D[i]];
//			IOUtils.fillMatrix_1D(2 * sizes1D[i], arrayOfFloat2);
//			localFloatFFT_1D.complexForward(arrayOfFloat1);
//			localFloatFFT_1D.complexInverse(arrayOfFloat1, true);
//			d = computeRMSE(arrayOfFloat1, arrayOfFloat2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			arrayOfFloat1 = null;
//			arrayOfFloat2 = null;
//			localFloatFFT_1D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyComplexFFT_2D() {
//		System.out
//				.println("Checking accuracy of 2D complex FFT (float[] input)...");
//		FloatFFT_2D localFloatFFT_2D;
//		double d;
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localFloatFFT_2D = new FloatFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			float[] localObject1 = new float[2 * sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i], localObject1);
//			float[] localObject2 = new float[2 * sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i], localObject2);
//			localFloatFFT_2D.complexForward(localObject1);
//			localFloatFFT_2D.complexInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localFloatFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D complex FFT (float[][] input)...");
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localFloatFFT_2D = new FloatFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			float[][] localObject1 = new float[sizes2D[i]][2 * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i], localObject1);
//			float[][] localObject2 = new float[sizes2D[i]][2 * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i], localObject2);
//			localFloatFFT_2D.complexForward(localObject1);
//			localFloatFFT_2D.complexInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = (float[][]) null;
//			localObject2 = (float[][]) null;
//			localFloatFFT_2D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyComplexFFT_3D() {
//		System.out
//				.println("Checking accuracy of 3D complex FFT (float[] input)...");
//		FloatFFT_3D localFloatFFT_3D;
//		double d;
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localFloatFFT_3D = new FloatFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			float[] localObject1 = new float[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//					localObject1);
//			float[] localObject2 = new float[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//					localObject2);
//			localFloatFFT_3D.complexForward(localObject1);
//			localFloatFFT_3D.complexInverse(localObject1, true);
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
//			localFloatFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D complex FFT (float[][][] input)...");
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localFloatFFT_3D = new FloatFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			float[][][] localObject1 = new float[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//					localObject1);
//			float[][][] localObject2 = new float[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//					localObject2);
//			localFloatFFT_3D.complexForward(localObject1);
//			localFloatFFT_3D.complexInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			localObject1 = (float[][][]) null;
//			localObject2 = (float[][][]) null;
//			localFloatFFT_3D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyRealFFT_1D() {
//		System.out.println("Checking accuracy of 1D real FFT...");
//		FloatFFT_1D localFloatFFT_1D;
//		double d;
//		float[] arrayOfFloat1;
//		float[] arrayOfFloat2;
//		for (int i = 0; i < sizes1D.length; ++i) {
//			localFloatFFT_1D = new FloatFFT_1D(sizes1D[i]);
//			d = 0.0D;
//			arrayOfFloat1 = new float[sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfFloat1);
//			arrayOfFloat2 = new float[sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfFloat2);
//			localFloatFFT_1D.realForward(arrayOfFloat1);
//			localFloatFFT_1D.realInverse(arrayOfFloat1, true);
//			d = computeRMSE(arrayOfFloat1, arrayOfFloat2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			arrayOfFloat1 = null;
//			arrayOfFloat2 = null;
//			localFloatFFT_1D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of on 1D real forward full FFT...");
//		int j;
//		for (int i = 0; i < sizes1D.length; ++i) {
//			localFloatFFT_1D = new FloatFFT_1D(sizes1D[i]);
//			d = 0.0D;
//			arrayOfFloat1 = new float[2 * sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfFloat1);
//			arrayOfFloat2 = new float[2 * sizes1D[i]];
//			for (j = 0; j < sizes1D[i]; ++j)
//				arrayOfFloat2[(2 * j)] = arrayOfFloat1[j];
//			localFloatFFT_1D.realForwardFull(arrayOfFloat1);
//			localFloatFFT_1D.complexInverse(arrayOfFloat1, true);
//			d = computeRMSE(arrayOfFloat1, arrayOfFloat2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			arrayOfFloat1 = null;
//			arrayOfFloat2 = null;
//			localFloatFFT_1D = null;
//			System.gc();
//		}
//		System.out.println("Checking accuracy of 1D real inverse full FFT...");
//		for (int i = 0; i < sizes1D.length; ++i) {
//			localFloatFFT_1D = new FloatFFT_1D(sizes1D[i]);
//			d = 0.0D;
//			arrayOfFloat1 = new float[2 * sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfFloat1);
//			arrayOfFloat2 = new float[2 * sizes1D[i]];
//			for (j = 0; j < sizes1D[i]; ++j)
//				arrayOfFloat2[(2 * j)] = arrayOfFloat1[j];
//			localFloatFFT_1D.realInverseFull(arrayOfFloat1, true);
//			localFloatFFT_1D.complexForward(arrayOfFloat1);
//			d = computeRMSE(arrayOfFloat1, arrayOfFloat2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			arrayOfFloat1 = null;
//			arrayOfFloat2 = null;
//			localFloatFFT_1D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyRealFFT_2D() {
//		System.out
//				.println("Checking accuracy of 2D real FFT (float[] input)...");
//		FloatFFT_2D localFloatFFT_2D;
//		double d;
//		for (int i = 0; i < sizes2D2.length; ++i) {
//			localFloatFFT_2D = new FloatFFT_2D(sizes2D2[i], sizes2D2[i]);
//			d = 0.0D;
//			float[] localObject1 = new float[sizes2D2[i] * sizes2D2[i]];
//			IOUtils.fillMatrix_2D(sizes2D2[i], sizes2D2[i], localObject1);
//			float[] localObject2 = new float[sizes2D2[i] * sizes2D2[i]];
//			IOUtils.fillMatrix_2D(sizes2D2[i], sizes2D2[i], localObject2);
//			localFloatFFT_2D.realForward(localObject1);
//			localFloatFFT_2D.realInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D2[i] + " x "
//						+ sizes2D2[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D2[i] + " x "
//						+ sizes2D2[i] + ";\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localFloatFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D real FFT (float[][] input)...");
//		for (int i = 0; i < sizes2D2.length; ++i) {
//			localFloatFFT_2D = new FloatFFT_2D(sizes2D2[i], sizes2D2[i]);
//			d = 0.0D;
//			float[][] localObject1 = new float[sizes2D2[i]][sizes2D2[i]];
//			IOUtils.fillMatrix_2D(sizes2D2[i], sizes2D2[i], localObject1);
//			float[][] localObject2 = new float[sizes2D2[i]][sizes2D2[i]];
//			IOUtils.fillMatrix_2D(sizes2D2[i], sizes2D2[i], localObject2);
//			localFloatFFT_2D.realForward(localObject1);
//			localFloatFFT_2D.realInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D2[i] + " x "
//						+ sizes2D2[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D2[i] + " x "
//						+ sizes2D2[i] + ";\terror = " + d);
//			localObject1 = (float[][]) null;
//			localObject2 = (float[][]) null;
//			localFloatFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D real forward full FFT (float[] input)...");
//		int j;
//		int k;
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localFloatFFT_2D = new FloatFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			float[] localObject1 = new float[2 * sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject1);
//			float[] localObject2 = new float[2 * sizes2D[i] * sizes2D[i]];
//			for (j = 0; j < sizes2D[i]; ++j)
//				for (k = 0; k < sizes2D[i]; ++k)
//					localObject2[(j * 2 * sizes2D[i] + 2 * k)] = localObject1[(j
//							* sizes2D[i] + k)];
//			localFloatFFT_2D.realForwardFull(localObject1);
//			localFloatFFT_2D.complexInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localFloatFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D real forward full FFT (float[][] input)...");
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localFloatFFT_2D = new FloatFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			float[][] localObject1 = new float[sizes2D[i]][2 * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject1);
//			float[][] localObject2 = new float[sizes2D[i]][2 * sizes2D[i]];
//			for (j = 0; j < sizes2D[i]; ++j)
//				for (k = 0; k < sizes2D[i]; ++k)
//					localObject2[j][(2 * k)] = localObject1[j][k];
//			localFloatFFT_2D.realForwardFull(localObject1);
//			localFloatFFT_2D.complexInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = (float[][]) null;
//			localObject2 = (float[][]) null;
//			localFloatFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D real inverse full FFT (float[] input)...");
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localFloatFFT_2D = new FloatFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			float[] localObject1 = new float[2 * sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject1);
//			float[] localObject2 = new float[2 * sizes2D[i] * sizes2D[i]];
//			for (j = 0; j < sizes2D[i]; ++j)
//				for (k = 0; k < sizes2D[i]; ++k)
//					localObject2[(j * 2 * sizes2D[i] + 2 * k)] = localObject1[(j
//							* sizes2D[i] + k)];
//			localFloatFFT_2D.realInverseFull(localObject1, true);
//			localFloatFFT_2D.complexForward(localObject1);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localFloatFFT_2D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 2D real inverse full FFT (float[][] input)...");
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localFloatFFT_2D = new FloatFFT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			float[][] localObject1 = new float[sizes2D[i]][2 * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject1);
//			float[][] localObject2 = new float[sizes2D[i]][2 * sizes2D[i]];
//			for (j = 0; j < sizes2D[i]; ++j)
//				for (k = 0; k < sizes2D[i]; ++k)
//					localObject2[j][(2 * k)] = localObject1[j][k];
//			localFloatFFT_2D.realInverseFull(localObject1, true);
//			localFloatFFT_2D.complexForward(localObject1);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = (float[][]) null;
//			localObject2 = (float[][]) null;
//			localFloatFFT_2D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyRealFFT_3D() {
//		System.out
//				.println("Checking accuracy of 3D real FFT (float[] input)...");
//		FloatFFT_3D localFloatFFT_3D;
//		double d;
//		for (int i = 0; i < sizes3D2.length; ++i) {
//			localFloatFFT_3D = new FloatFFT_3D(sizes3D2[i], sizes3D2[i],
//					sizes3D2[i]);
//			d = 0.0D;
//			float[] localObject1 = new float[sizes3D2[i] * sizes3D2[i] * sizes3D2[i]];
//			IOUtils.fillMatrix_3D(sizes3D2[i], sizes3D2[i], sizes3D2[i],
//					localObject1);
//			float[] localObject2 = new float[sizes3D2[i] * sizes3D2[i] * sizes3D2[i]];
//			IOUtils.fillMatrix_3D(sizes3D2[i], sizes3D2[i], sizes3D2[i],
//					localObject2);
//			localFloatFFT_3D.realForward(localObject2);
//			localFloatFFT_3D.realInverse(localObject2, true);
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
//			localFloatFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D real FFT (float[][][] input)...");
//		for (int i = 0; i < sizes3D2.length; ++i) {
//			localFloatFFT_3D = new FloatFFT_3D(sizes3D2[i], sizes3D2[i],
//					sizes3D2[i]);
//			d = 0.0D;
//			float[][][] localObject1 = new float[sizes3D2[i]][sizes3D2[i]][sizes3D2[i]];
//			IOUtils.fillMatrix_3D(sizes3D2[i], sizes3D2[i], sizes3D2[i],
//					localObject1);
//			float[][][] localObject2 = new float[sizes3D2[i]][sizes3D2[i]][sizes3D2[i]];
//			IOUtils.fillMatrix_3D(sizes3D2[i], sizes3D2[i], sizes3D2[i],
//					localObject2);
//			localFloatFFT_3D.realForward(localObject2);
//			localFloatFFT_3D.realInverse(localObject2, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes3D2[i] + " x "
//						+ sizes3D2[i] + " x " + sizes3D2[i] + ";\t\terror = "
//						+ d);
//			else
//				System.out.println("\tsize = " + sizes3D2[i] + " x "
//						+ sizes3D2[i] + " x " + sizes3D2[i] + ";\t\terror = "
//						+ d);
//			localObject1 = (float[][][]) null;
//			localObject2 = (float[][][]) null;
//			localFloatFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D real forward full FFT (float[] input)...");
//		int j;
//		int k;
//		int l;
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localFloatFFT_3D = new FloatFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			float[] localObject1 = new float[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject1);
//			float[] localObject2 = new float[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			for (j = 0; j < sizes3D[i]; ++j)
//				for (k = 0; k < sizes3D[i]; ++k)
//					for (l = 0; l < sizes3D[i]; ++l)
//						localObject2[(j * 2 * sizes3D[i] * sizes3D[i] + k * 2
//								* sizes3D[i] + 2 * l)] = localObject1[(j
//								* sizes3D[i] * sizes3D[i] + k * sizes3D[i] + l)];
//			localFloatFFT_3D.realForwardFull(localObject1);
//			localFloatFFT_3D.complexInverse(localObject1, true);
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
//			localFloatFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D real forward full FFT (float[][][] input)...");
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localFloatFFT_3D = new FloatFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			float[][][] localObject1 = new float[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject1);
//			float[][][] localObject2 = new float[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			for (j = 0; j < sizes3D[i]; ++j)
//				for (k = 0; k < sizes3D[i]; ++k)
//					for (l = 0; l < sizes3D[i]; ++l)
//						localObject2[j][k][(2 * l)] = localObject1[j][k][l];
//			localFloatFFT_3D.realForwardFull(localObject1);
//			localFloatFFT_3D.complexInverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			localObject1 = (float[][][]) null;
//			localObject2 = (float[][][]) null;
//			localFloatFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D real inverse full FFT (float[] input)...");
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localFloatFFT_3D = new FloatFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			float[] localObject1 = new float[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject1);
//			float[] localObject2 = new float[2 * sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			for (j = 0; j < sizes3D[i]; ++j)
//				for (k = 0; k < sizes3D[i]; ++k)
//					for (l = 0; l < sizes3D[i]; ++l)
//						localObject2[(j * 2 * sizes3D[i] * sizes3D[i] + k * 2
//								* sizes3D[i] + 2 * l)] = localObject1[(j
//								* sizes3D[i] * sizes3D[i] + k * sizes3D[i] + l)];
//			localFloatFFT_3D.realInverseFull(localObject1, true);
//			localFloatFFT_3D.complexForward(localObject1);
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
//			localFloatFFT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D real inverse full FFT (float[][][] input)...");
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localFloatFFT_3D = new FloatFFT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			float[][][] localObject1 = new float[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject1);
//			float[][][] localObject2 = new float[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			for (j = 0; j < sizes3D[i]; ++j)
//				for (k = 0; k < sizes3D[i]; ++k)
//					for (l = 0; l < sizes3D[i]; ++l)
//						localObject2[j][k][(2 * l)] = localObject1[j][k][l];
//			localFloatFFT_3D.realInverseFull(localObject1, true);
//			localFloatFFT_3D.complexForward(localObject1);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes3D[i] + " x " + sizes3D[i]
//								+ " x " + sizes3D[i] + ";\t\terror = " + d);
//			localObject1 = (float[][][]) null;
//			localObject2 = (float[][][]) null;
//			localFloatFFT_3D = null;
//			System.gc();
//		}
//	}
//
//	private static double computeRMSE(float[] paramArrayOfFloat1,
//			float[] paramArrayOfFloat2) {
//		if (paramArrayOfFloat1.length != paramArrayOfFloat2.length)
//			throw new IllegalArgumentException("Arrays are not the same size.");
//		double d1 = 0.0D;
//		for (int i = 0; i < paramArrayOfFloat1.length; ++i) {
//			double d2 = paramArrayOfFloat1[i] - paramArrayOfFloat2[i];
//			d1 += d2 * d2;
//		}
//		return Math.sqrt(d1 / paramArrayOfFloat1.length);
//	}
//
//	private static double computeRMSE(float[][] paramArrayOfFloat1,
//			float[][] paramArrayOfFloat2) {
//		if ((paramArrayOfFloat1.length != paramArrayOfFloat2.length)
//				|| (paramArrayOfFloat1[0].length != paramArrayOfFloat2[0].length))
//			throw new IllegalArgumentException("Arrays are not the same size.");
//		double d1 = 0.0D;
//		for (int i = 0; i < paramArrayOfFloat1.length; ++i)
//			for (int j = 0; j < paramArrayOfFloat1[0].length; ++j) {
//				double d2 = paramArrayOfFloat1[i][j] - paramArrayOfFloat2[i][j];
//				d1 += d2 * d2;
//			}
//		return Math.sqrt(d1 / paramArrayOfFloat1.length
//				* paramArrayOfFloat1[0].length);
//	}
//
//	private static double computeRMSE(float[][][] paramArrayOfFloat1,
//			float[][][] paramArrayOfFloat2) {
//		if ((paramArrayOfFloat1.length != paramArrayOfFloat2.length)
//				|| (paramArrayOfFloat1[0].length != paramArrayOfFloat2[0].length)
//				|| (paramArrayOfFloat1[0][0].length != paramArrayOfFloat2[0][0].length))
//			throw new IllegalArgumentException("Arrays are not the same size.");
//		double d1 = 0.0D;
//		for (int i = 0; i < paramArrayOfFloat1.length; ++i)
//			for (int j = 0; j < paramArrayOfFloat1[0].length; ++j)
//				for (int k = 0; k < paramArrayOfFloat1[0][0].length; ++k) {
//					double d2 = paramArrayOfFloat1[i][j][k]
//							- paramArrayOfFloat2[i][j][k];
//					d1 += d2 * d2;
//				}
//		return Math.sqrt(d1 / paramArrayOfFloat1.length
//				* paramArrayOfFloat1[0].length
//				* paramArrayOfFloat1[0][0].length);
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