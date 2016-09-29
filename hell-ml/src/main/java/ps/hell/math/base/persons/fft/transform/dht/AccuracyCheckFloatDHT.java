package ps.hell.math.base.persons.fft.transform.dht;//package ps.landerbuluse.ml.math.fft.transform.dht;
//
//import edu.emory.mathcs.utils.IOUtils;
//import java.io.PrintStream;
//
//public class AccuracyCheckFloatDHT {
//	private static int[] sizes1D = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 120, 128, 256, 310, 512, 1024, 1056, 2048, 8192,
//			10158, 16384, 32768, 65536, 131072 };
//	private static int[] sizes2D = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 120, 128, 256, 310, 511, 512, 1024 };
//	private static int[] sizes3D = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//			16, 32, 64, 100, 128 };
//	private static double eps = Math.pow(2.0D, -23.0D);
//
//	public static void checkAccuracyDHT_1D() {
//		System.out.println("Checking accuracy of 1D DHT...");
//		for (int i = 0; i < sizes1D.length; ++i) {
//			FloatDHT_1D localFloatDHT_1D = new FloatDHT_1D(sizes1D[i]);
//			double d = 0.0D;
//			float[] arrayOfFloat1 = new float[sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfFloat1);
//			float[] arrayOfFloat2 = new float[sizes1D[i]];
//			IOUtils.fillMatrix_1D(sizes1D[i], arrayOfFloat2);
//			localFloatDHT_1D.forward(arrayOfFloat1);
//			localFloatDHT_1D.inverse(arrayOfFloat1, true);
//			d = computeRMSE(arrayOfFloat1, arrayOfFloat2);
//			if (d > eps)
//				System.err
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			else
//				System.out
//						.println("\tsize = " + sizes1D[i] + ";\terror = " + d);
//			arrayOfFloat1 = null;
//			arrayOfFloat2 = null;
//			localFloatDHT_1D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyDHT_2D() {
//		System.out.println("Checking accuracy of 2D DHT (float[] input)...");
//		FloatDHT_2D localFloatDHT_2D;
//		double d;
//		Object localObject1;
//		Object localObject2;
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localFloatDHT_2D = new FloatDHT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			localObject1 = new float[sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], (float[])localObject1);
//			localObject2 = new float[sizes2D[i] * sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], (float[])localObject2);
//			localFloatDHT_2D.forward((float[])localObject1);
//			localFloatDHT_2D.inverse((float[])localObject1, true);
//			d = computeRMSE((float[])localObject1, (float[])localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = null;
//			localObject2 = null;
//			localFloatDHT_2D = null;
//			System.gc();
//		}
//		System.out.println("Checking accuracy of 2D DHT (float[][] input)...");
//		for (int i = 0; i < sizes2D.length; ++i) {
//			localFloatDHT_2D = new FloatDHT_2D(sizes2D[i], sizes2D[i]);
//			d = 0.0D;
//			localObject1 = new float[sizes2D[i]][sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], (float[][])localObject1);
//			localObject2 = new float[sizes2D[i]][sizes2D[i]];
//			IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], (float[][])localObject2);
//			localFloatDHT_2D.forward((float[][])localObject1);
//			localFloatDHT_2D.inverse((float[][])localObject1, true);
//			d = computeRMSE((float[][])localObject1,(float[][]) localObject2);
//			if (d > eps)
//				System.err.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			else
//				System.out.println("\tsize = " + sizes2D[i] + " x "
//						+ sizes2D[i] + ";\terror = " + d);
//			localObject1 = (float[][]) null;
//			localObject2 = (float[][]) null;
//			localFloatDHT_2D = null;
//			System.gc();
//		}
//	}
//
//	public static void checkAccuracyDHT_3D() {
//		System.out.println("Checking accuracy of 3D DHT (float[] input)...");
//		FloatDHT_3D localFloatDHT_3D;
//		double d;
//		Object localObject1;
//		Object localObject2;
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localFloatDHT_3D = new FloatDHT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			localObject1 = new float[sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					(float[][][])localObject1);
//			localObject2 = new float[sizes3D[i] * sizes3D[i] * sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					(float[][][])localObject2);
//			localFloatDHT_3D.forward((float[][][])localObject1);
//			localFloatDHT_3D.inverse((float[][][])localObject1, true);
//			d = computeRMSE((float[][][])localObject1, (float[][][])localObject2);
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
//			localFloatDHT_3D = null;
//			System.gc();
//		}
//		System.out
//				.println("Checking accuracy of 3D DHT (float[][][] input)...");
//		for (int i = 0; i < sizes3D.length; ++i) {
//			localFloatDHT_3D = new FloatDHT_3D(sizes3D[i], sizes3D[i],
//					sizes3D[i]);
//			d = 0.0D;
//			localObject1 = new float[sizes3D[i]][sizes3D[i]][sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					(float[][][])localObject1);
//			localObject2 = new float[sizes3D[i]][sizes3D[i]][sizes3D[i]];
//			IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//					(float[][][])localObject2);
//			localFloatDHT_3D.forward((float[][][])localObject1);
//			localFloatDHT_3D.inverse((float[][][])localObject1, true);
//			d = computeRMSE((float[][][])localObject1,(float[][][]) localObject2);
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
//			localFloatDHT_3D = null;
//			System.gc();
//		}
//	}
//
//	private static double computeRMSE(float[] paramArrayOfFloat1,
//			float[] paramArrayOfFloat2) {
//		if (paramArrayOfFloat1.length != paramArrayOfFloat2.length)
//			throw new IllegalArgumentException("Arrays are not the same size");
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
//			throw new IllegalArgumentException("Arrays are not the same size");
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
//			throw new IllegalArgumentException("Arrays are not the same size");
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
//		checkAccuracyDHT_1D();
//		checkAccuracyDHT_2D();
//		checkAccuracyDHT_3D();
//		System.exit(0);
//	}
//}