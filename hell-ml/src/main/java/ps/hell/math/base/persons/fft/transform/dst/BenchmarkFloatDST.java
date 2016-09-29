package ps.hell.math.base.persons.fft.transform.dst;//package ps.landerbuluse.ml.math.fft.transform.dst;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import edu.emory.mathcs.utils.IOUtils;
//import java.io.PrintStream;
//import java.util.Arrays;
//
//public class BenchmarkFloatDST {
//	private static int nthread = 8;
//	private static int niter = 200;
//	private static int nsize = 16;
//	private static int threadsBegin2D = 65636;
//	private static int threadsBegin3D = 65636;
//	private static boolean doWarmup = true;
//	private static int[] sizes1D = { 65536, 131072, 262144, 524288, 1048576,
//			2097152, 4194304, 8388608, 10368, 27000, 75600, 165375, 362880,
//			1562500, 3211264, 6250000 };
//	private static int[] sizes2D = { 128, 256, 512, 1024, 2048, 4096, 8192,
//			16384, 260, 520, 1050, 1458, 1960, 2916, 4116, 5832 };
//	private static int[] sizes3D = { 8, 16, 32, 64, 128, 256, 512, 1024, 5, 17,
//			30, 95, 180, 270, 324, 420 };
//	private static boolean doScaling = false;
//
//	public static void parseArguments(String[] paramArrayOfString) {
//		if (paramArrayOfString.length > 0) {
//			nthread = Integer.parseInt(paramArrayOfString[0]);
//			threadsBegin2D = Integer.parseInt(paramArrayOfString[1]);
//			threadsBegin3D = Integer.parseInt(paramArrayOfString[2]);
//			niter = Integer.parseInt(paramArrayOfString[3]);
//			doWarmup = Boolean.parseBoolean(paramArrayOfString[4]);
//			doScaling = Boolean.parseBoolean(paramArrayOfString[5]);
//			nsize = Integer.parseInt(paramArrayOfString[6]);
//			sizes1D = new int[nsize];
//			sizes2D = new int[nsize];
//			sizes3D = new int[nsize];
//			for (int i = 0; i < nsize; ++i)
//				sizes1D[i] = Integer.parseInt(paramArrayOfString[(7 + i)]);
//			for (i = 0; i < nsize; ++i)
//				sizes2D[i] = Integer
//						.parseInt(paramArrayOfString[(7 + nsize + i)]);
//			for (i = 0; i < nsize; ++i)
//				sizes3D[i] = Integer.parseInt(paramArrayOfString[(7 + nsize
//						+ nsize + i)]);
//		} else {
//			System.out.println("Default settings are used.");
//		}
//		ConcurrencyUtils.setNumberOfThreads(nthread);
//		ConcurrencyUtils.setThreadsBeginN_2D(threadsBegin2D);
//		ConcurrencyUtils.setThreadsBeginN_3D(threadsBegin3D);
//		System.out.println("nthred = " + nthread);
//		System.out.println("threadsBegin2D = " + threadsBegin2D);
//		System.out.println("threadsBegin3D = " + threadsBegin3D);
//		System.out.println("niter = " + niter);
//		System.out.println("doWarmup = " + doWarmup);
//		System.out.println("doScaling = " + doScaling);
//		System.out.println("nsize = " + nsize);
//		System.out.println("sizes1D[] = " + Arrays.toString(sizes1D));
//		System.out.println("sizes2D[] = " + Arrays.toString(sizes2D));
//		System.out.println("sizes3D[] = " + Arrays.toString(sizes3D));
//	}
//
//	public static void benchmarkForward_1D() {
//		double[] arrayOfDouble = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Forward DST 1D of size " + sizes1D[i]);
//			FloatDST_1D localFloatDST_1D = new FloatDST_1D(sizes1D[i]);
//			float[] arrayOfFloat = new float[sizes1D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_1D(sizes1D[i], arrayOfFloat);
//				localFloatDST_1D.forward(arrayOfFloat, doScaling);
//				IOUtils.fillMatrix_1D(sizes1D[i], arrayOfFloat);
//				localFloatDST_1D.forward(arrayOfFloat, doScaling);
//			}
//			float f = 0.0F;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_1D(sizes1D[i], arrayOfFloat);
//				l = System.nanoTime();
//				localFloatDST_1D.forward(arrayOfFloat, doScaling);
//				l = System.nanoTime() - l;
//				f += (float) l;
//			}
//			arrayOfDouble[i] = (f / 1000000.0D / niter);
//			System.out.println("Average execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(f / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfFloat = null;
//			localFloatDST_1D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkFloatForwardDST_1D.txt", nthread, niter, doWarmup,
//				doScaling, sizes1D, arrayOfDouble);
//	}
//
//	public static void benchmarkForward_2D_input_1D() {
//		double[] arrayOfDouble = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Forward DST 2D (input 1D) of size "
//					+ sizes2D[i] + " x " + sizes2D[i]);
//			FloatDST_2D localFloatDST_2D = new FloatDST_2D(sizes2D[i],
//					sizes2D[i]);
//			float[] arrayOfFloat = new float[sizes2D[i] * sizes2D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfFloat);
//				localFloatDST_2D.forward(arrayOfFloat, doScaling);
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfFloat);
//				localFloatDST_2D.forward(arrayOfFloat, doScaling);
//			}
//			float f = 0.0F;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfFloat);
//				l = System.nanoTime();
//				localFloatDST_2D.forward(arrayOfFloat, doScaling);
//				l = System.nanoTime() - l;
//				f += (float) l;
//			}
//			arrayOfDouble[i] = (f / 1000000.0D / niter);
//			System.out.println("Average execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(f / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfFloat = null;
//			localFloatDST_2D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkFloatForwardDST_2D_input_1D.txt", nthread, niter,
//				doWarmup, doScaling, sizes2D, arrayOfDouble);
//	}
//
//	public static void benchmarkForward_2D_input_2D() {
//		double[] arrayOfDouble = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Forward DST 2D (input 2D) of size "
//					+ sizes2D[i] + " x " + sizes2D[i]);
//			FloatDST_2D localFloatDST_2D = new FloatDST_2D(sizes2D[i],
//					sizes2D[i]);
//			float[][] arrayOfFloat = new float[sizes2D[i]][sizes2D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfFloat);
//				localFloatDST_2D.forward(arrayOfFloat, doScaling);
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfFloat);
//				localFloatDST_2D.forward(arrayOfFloat, doScaling);
//			}
//			float f = 0.0F;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfFloat);
//				l = System.nanoTime();
//				localFloatDST_2D.forward(arrayOfFloat, doScaling);
//				l = System.nanoTime() - l;
//				f += (float) l;
//			}
//			arrayOfDouble[i] = (f / 1000000.0D / niter);
//			System.out.println("Average execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(f / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfFloat = (float[][]) null;
//			localFloatDST_2D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkFloatForwardDST_2D_input_2D.txt", nthread, niter,
//				doWarmup, doScaling, sizes2D, arrayOfDouble);
//	}
//
//	public static void benchmarkForward_3D_input_1D() {
//		double[] arrayOfDouble = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Forward DST 3D (input 1D) of size "
//					+ sizes3D[i] + " x " + sizes3D[i] + " x " + sizes3D[i]);
//			FloatDST_3D localFloatDST_3D = new FloatDST_3D(sizes3D[i],
//					sizes3D[i], sizes3D[i]);
//			float[] arrayOfFloat = new float[sizes3D[i] * sizes3D[i]
//					* sizes3D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfFloat);
//				localFloatDST_3D.forward(arrayOfFloat, doScaling);
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfFloat);
//				localFloatDST_3D.forward(arrayOfFloat, doScaling);
//			}
//			float f = 0.0F;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfFloat);
//				l = System.nanoTime();
//				localFloatDST_3D.forward(arrayOfFloat, doScaling);
//				l = System.nanoTime() - l;
//				f += (float) l;
//			}
//			arrayOfDouble[i] = (f / 1000000.0D / niter);
//			System.out.println("Average execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(f / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfFloat = null;
//			localFloatDST_3D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkFloatForwardDST_3D_input_1D.txt", nthread, niter,
//				doWarmup, doScaling, sizes3D, arrayOfDouble);
//	}
//
//	public static void benchmarkForward_3D_input_3D() {
//		double[] arrayOfDouble = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Forward DST 3D (input 3D) of size "
//					+ sizes3D[i] + " x " + sizes3D[i] + " x " + sizes3D[i]);
//			FloatDST_3D localFloatDST_3D = new FloatDST_3D(sizes3D[i],
//					sizes3D[i], sizes3D[i]);
//			float[][][] arrayOfFloat = new float[sizes3D[i]][sizes3D[i]][sizes3D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfFloat);
//				localFloatDST_3D.forward(arrayOfFloat, doScaling);
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfFloat);
//				localFloatDST_3D.forward(arrayOfFloat, doScaling);
//			}
//			float f = 0.0F;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfFloat);
//				l = System.nanoTime();
//				localFloatDST_3D.forward(arrayOfFloat, doScaling);
//				l = System.nanoTime() - l;
//				f += (float) l;
//			}
//			arrayOfDouble[i] = (f / 1000000.0D / niter);
//			System.out.println("Average execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(f / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfFloat = (float[][][]) null;
//			localFloatDST_3D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkFloatForwardDST_3D_input_3D.txt", nthread, niter,
//				doWarmup, doScaling, sizes3D, arrayOfDouble);
//	}
//
//	public static void main(String[] paramArrayOfString) {
//		parseArguments(paramArrayOfString);
//		benchmarkForward_1D();
//		benchmarkForward_2D_input_1D();
//		benchmarkForward_2D_input_2D();
//		benchmarkForward_3D_input_1D();
//		benchmarkForward_3D_input_3D();
//		System.exit(0);
//	}
//}