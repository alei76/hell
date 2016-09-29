package ps.hell.math.base.persons.fft.transform.dct;//package ps.landerbuluse.ml.math.fft.transform.dct;
//
//import edu.emory.mathcs.utils.IOUtils;
//import java.io.PrintStream;
//
//public class AccuracyCheckDoubleDCT {
//	private static int[] sizes1D = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 120, 128, 256, 310, 512, 1024, 1056, 2048, 8192,
//			10158, 16384, 32768, 65536, 131072 };
//	private static int[] sizes2D = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 120, 128, 256, 310, 511, 512, 1024 };
//	private static int[] sizes3D = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 128 };
//	private static double eps = Math.pow(2.0D, -52.0D);
//
//	public static void checkAccuracyDCT_1D() {
//		System.out.println("Checking accuracy of 1D DCT...");
//		for (int i = 0; i < sizes1D.length; ++i) {
//			DoubleDCT_1D localDoubleDCT_1D = new DoubleDCT_1D(sizes1D[i]);
//			double d = 0.0D;
//			double[] arrayOfDouble1 = new double[sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfDouble1);
//			double[] arrayOfDouble2 = new double[sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfDouble2);
//			localDoubleDCT_1D.forward(arrayOfDouble1, true);
//			localDoubleDCT_1D.inverse(arrayOfDouble1, true);
//			d = computeRMSE(arrayOfDouble1, arrayOfDouble2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			arrayOfDouble1 = null;
//			arrayOfDouble2 = null;
//			localDoubleDCT_1D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyDCT_2D() {
//		System.out.println("Checking accuracy of 2D DCT (double[] input)...");
//		DoubleDCT_2D localDoubleDCT_2D;
//		double d;
//		Object localObject1;
//		Object localObject2;
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localDoubleDCT_2D = new DoubleDCT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			localObject1 = new double[sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject1);
//			localObject2 = new double[sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject2);
//			localDoubleDCT_2D.forward(localObject1, true);
//			localDoubleDCT_2D.inverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localDoubleDCT_2D = null;
//			System.gc();
//		}
//		System.out.println("Checking accuracy of 2D DCT (double[][] input)...");
//		for (i = 0; i < sizes2D.length; ++i) {
//			localDoubleDCT_2D = new DoubleDCT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			localObject1 = new double[sizes2D[i]][sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject1);
//			localObject2 = new double[sizes2D[i]][sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], localObject2);
//			localDoubleDCT_2D.forward(localObject1, true);
//			localDoubleDCT_2D.inverse(localObject1, true);
//			d = computeRMSE(localObject1, localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = (double[][]) null;
//			localObject2 = (double[][]) null;
//			localDoubleDCT_2D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyDCT_3D() {
//		System.out.println("Checking accuracy of 3D DCT (double[] input)...");
//		DoubleDCT_3D localDoubleDCT_3D;
//		double d;
//		Object localObject1;
//		Object localObject2;
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localDoubleDCT_3D = new DoubleDCT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			localObject1 = new double[sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject1);
//			localObject2 = new double[sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject2);
//			localDoubleDCT_3D.forward(localObject1, true);
//			localDoubleDCT_3D.inverse(localObject1, true);
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
//			localDoubleDCT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D DCT (double[][][] input)...");
//		for (i = 0; i < sizes3D.length; ++i) {
//			localDoubleDCT_3D = new DoubleDCT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			localObject1 = new double[sizes3D[i]][sizes3D[i]][sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject1);
//			localObject2 = new double[sizes3D[i]][sizes3D[i]][sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					localObject2);
//			localDoubleDCT_3D.forward(localObject1, true);
//			localDoubleDCT_3D.inverse(localObject1, true);
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
//			localDoubleDCT_3D = null;
//			System.gc();
//		}
//	}
//
//	private static double computeRMSE(double[] paramArrayOfDouble1,
//			double[] paramArrayOfDouble2) {
//		if (paramArrayOfDouble1.length != paramArrayOfDouble2.length)
//			throw new IllegalArgumentException("Arrays are not the same size");
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
//			throw new IllegalArgumentException("Arrays are not the same size");
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
//			throw new IllegalArgumentException("Arrays are not the same size");
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
//		checkAccuracyDCT_1D();
//		checkAccuracyDCT_2D();
//		checkAccuracyDCT_3D();
//		System.exit(0);
//	}
//}