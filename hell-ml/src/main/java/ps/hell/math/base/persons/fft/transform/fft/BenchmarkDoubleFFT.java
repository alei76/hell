package ps.hell.math.base.persons.fft.transform.fft;//package ps.landerbuluse.ml.math.fft.transform.fft;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import edu.emory.mathcs.utils.IOUtils;
//import java.io.PrintStream;
//import java.util.Arrays;
//
//public class BenchmarkDoubleFFT {
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
//			for (int i = 0; i < nsize; ++i)
//				sizes2D[i] = Integer
//						.parseInt(paramArrayOfString[(7 + nsize + i)]);
//			for (int i = 0; i < nsize; ++i)
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
//	public static void benchmarkComplexForward_1D() {
//		double[] arrayOfDouble2 = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Complex forward FFT 1D of size " + sizes1D[i]);
//			DoubleFFT_1D localDoubleFFT_1D = new DoubleFFT_1D(sizes1D[i]);
//			double[] arrayOfDouble1 = new double[2 * sizes1D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_1D(2 * sizes1D[i], arrayOfDouble1);
//				localDoubleFFT_1D.complexForward(arrayOfDouble1);
//				IOUtils.fillMatrix_1D(2 * sizes1D[i], arrayOfDouble1);
//				localDoubleFFT_1D.complexForward(arrayOfDouble1);
//			}
//			double d = 0.0D;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_1D(2 * sizes1D[i], arrayOfDouble1);
//				l = System.nanoTime();
//				localDoubleFFT_1D.complexForward(arrayOfDouble1);
//				l = System.nanoTime() - l;
//				d += l;
//			}
//			arrayOfDouble2[i] = (d / 1000000.0D / niter);
//			System.out.println("\tAverage execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(d / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfDouble1 = null;
//			localDoubleFFT_1D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkDoubleComplexForwardFFT_1D.txt", nthread, niter,
//				doWarmup, doScaling, sizes1D, arrayOfDouble2);
//	}
//
//	public static void benchmarkRealForward_1D() {
//		double[] arrayOfDouble1 = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Real forward FFT 1D of size " + sizes1D[i]);
//			DoubleFFT_1D localDoubleFFT_1D = new DoubleFFT_1D(sizes1D[i]);
//			double[] arrayOfDouble2 = new double[2 * sizes1D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_1D(sizes1D[i], arrayOfDouble2);
//				localDoubleFFT_1D.realForwardFull(arrayOfDouble2);
//				IOUtils.fillMatrix_1D(sizes1D[i], arrayOfDouble2);
//				localDoubleFFT_1D.realForwardFull(arrayOfDouble2);
//			}
//			double d = 0.0D;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_1D(sizes1D[i], arrayOfDouble2);
//				l = System.nanoTime();
//				localDoubleFFT_1D.realForwardFull(arrayOfDouble2);
//				l = System.nanoTime() - l;
//				d += l;
//			}
//			arrayOfDouble1[i] = (d / 1000000.0D / niter);
//			System.out.println("\tAverage execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(d / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfDouble2 = null;
//			localDoubleFFT_1D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkDoubleRealForwardFFT_1D.txt", nthread, niter,
//				doWarmup, doScaling, sizes1D, arrayOfDouble1);
//	}
//
//	public static void benchmarkComplexForward_2D_input_1D() {
//		double[] arrayOfDouble1 = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Complex forward FFT 2D (input 1D) of size "
//					+ sizes2D[i] + " x " + sizes2D[i]);
//			DoubleFFT_2D localDoubleFFT_2D = new DoubleFFT_2D(sizes2D[i],
//					sizes2D[i]);
//			double[] arrayOfDouble2 = new double[sizes2D[i] * 2 * sizes2D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i],
//						arrayOfDouble2);
//				localDoubleFFT_2D.complexForward(arrayOfDouble2);
//				IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i],
//						arrayOfDouble2);
//				localDoubleFFT_2D.complexForward(arrayOfDouble2);
//			}
//			double d = 0.0D;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i],
//						arrayOfDouble2);
//				l = System.nanoTime();
//				localDoubleFFT_2D.complexForward(arrayOfDouble2);
//				l = System.nanoTime() - l;
//				d += l;
//			}
//			arrayOfDouble1[i] = (d / 1000000.0D / niter);
//			System.out.println("\tAverage execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(d / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfDouble2 = null;
//			localDoubleFFT_2D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkDoubleComplexForwardFFT_2D_input_1D.txt", nthread,
//				niter, doWarmup, doScaling, sizes2D, arrayOfDouble1);
//	}
//
//	public static void benchmarkComplexForward_2D_input_2D() {
//		double[] arrayOfDouble = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Complex forward FFT 2D (input 2D) of size "
//					+ sizes2D[i] + " x " + sizes2D[i]);
//			DoubleFFT_2D localDoubleFFT_2D = new DoubleFFT_2D(sizes2D[i],
//					sizes2D[i]);
//			double[][] arrayOfDouble1 = new double[sizes2D[i]][2 * sizes2D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i],
//						arrayOfDouble1);
//				localDoubleFFT_2D.complexForward(arrayOfDouble1);
//				IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i],
//						arrayOfDouble1);
//				localDoubleFFT_2D.complexForward(arrayOfDouble1);
//			}
//			double d = 0.0D;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i],
//						arrayOfDouble1);
//				l = System.nanoTime();
//				localDoubleFFT_2D.complexForward(arrayOfDouble1);
//				l = System.nanoTime() - l;
//				d += l;
//			}
//			arrayOfDouble[i] = (d / 1000000.0D / niter);
//			System.out.println("\tAverage execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(d / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfDouble1 = (double[][]) null;
//			localDoubleFFT_2D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkDoubleComplexForwardFFT_2D_input_2D.txt", nthread,
//				niter, doWarmup, doScaling, sizes2D, arrayOfDouble);
//	}
//
//	public static void benchmarkRealForward_2D_input_1D() {
//		double[] arrayOfDouble1 = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Real forward FFT 2D (input 1D) of size "
//					+ sizes2D[i] + " x " + sizes2D[i]);
//			DoubleFFT_2D localDoubleFFT_2D = new DoubleFFT_2D(sizes2D[i],
//					sizes2D[i]);
//			double[] arrayOfDouble2 = new double[sizes2D[i] * 2 * sizes2D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfDouble2);
//				localDoubleFFT_2D.realForwardFull(arrayOfDouble2);
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfDouble2);
//				localDoubleFFT_2D.realForwardFull(arrayOfDouble2);
//			}
//			double d = 0.0D;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfDouble2);
//				l = System.nanoTime();
//				localDoubleFFT_2D.realForwardFull(arrayOfDouble2);
//				l = System.nanoTime() - l;
//				d += l;
//			}
//			arrayOfDouble1[i] = (d / 1000000.0D / niter);
//			System.out.println("\tAverage execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(d / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfDouble2 = null;
//			localDoubleFFT_2D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkDoubleRealForwardFFT_2D_input_1D.txt", nthread,
//				niter, doWarmup, doScaling, sizes2D, arrayOfDouble1);
//	}
//
//	public static void benchmarkRealForward_2D_input_2D() {
//		double[] arrayOfDouble = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Real forward FFT 2D (input 2D) of size "
//					+ sizes2D[i] + " x " + sizes2D[i]);
//			DoubleFFT_2D localDoubleFFT_2D = new DoubleFFT_2D(sizes2D[i],
//					sizes2D[i]);
//			double[][] arrayOfDouble1 = new double[sizes2D[i]][2 * sizes2D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfDouble1);
//				localDoubleFFT_2D.realForwardFull(arrayOfDouble1);
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfDouble1);
//				localDoubleFFT_2D.realForwardFull(arrayOfDouble1);
//			}
//			double d = 0.0D;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], arrayOfDouble1);
//				l = System.nanoTime();
//				localDoubleFFT_2D.realForwardFull(arrayOfDouble1);
//				l = System.nanoTime() - l;
//				d += l;
//			}
//			arrayOfDouble[i] = (d / 1000000.0D / niter);
//			System.out.println("\tAverage execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(d / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfDouble1 = (double[][]) null;
//			localDoubleFFT_2D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkDoubleRealForwardFFT_2D_input_2D.txt", nthread,
//				niter, doWarmup, doScaling, sizes2D, arrayOfDouble);
//	}
//
//	public static void benchmarkComplexForward_3D_input_1D() {
//		double[] arrayOfDouble1 = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			if (sizes3D[i] >= 1024)
//				continue;
//			System.out.println("Complex forward FFT 3D (input 1D) of size "
//					+ sizes3D[i] + " x " + sizes3D[i] + " x " + sizes3D[i]);
//			DoubleFFT_3D localDoubleFFT_3D = new DoubleFFT_3D(sizes3D[i],
//					sizes3D[i], sizes3D[i]);
//			double[] arrayOfDouble2 = new double[sizes3D[i] * sizes3D[i] * 2
//					* sizes3D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//						arrayOfDouble2);
//				localDoubleFFT_3D.complexForward(arrayOfDouble2);
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//						arrayOfDouble2);
//				localDoubleFFT_3D.complexForward(arrayOfDouble2);
//			}
//			double d = 0.0D;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//						arrayOfDouble2);
//				l = System.nanoTime();
//				localDoubleFFT_3D.complexForward(arrayOfDouble2);
//				l = System.nanoTime() - l;
//				d += l;
//			}
//			arrayOfDouble1[i] = (d / 1000000.0D / niter);
//			System.out.println("\tAverage execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(d / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfDouble2 = null;
//			localDoubleFFT_3D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkDoubleComplexForwardFFT_3D_input_1D.txt", nthread,
//				niter, doWarmup, doScaling, sizes3D, arrayOfDouble1);
//	}
//
//	public static void benchmarkComplexForward_3D_input_3D() {
//		double[] arrayOfDouble = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Complex forward FFT 3D (input 3D) of size "
//					+ sizes3D[i] + " x " + sizes3D[i] + " x " + sizes3D[i]);
//			DoubleFFT_3D localDoubleFFT_3D = new DoubleFFT_3D(sizes3D[i],
//					sizes3D[i], sizes3D[i]);
//			double[][][] arrayOfDouble1 = new double[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//						arrayOfDouble1);
//				localDoubleFFT_3D.complexForward(arrayOfDouble1);
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//						arrayOfDouble1);
//				localDoubleFFT_3D.complexForward(arrayOfDouble1);
//			}
//			double d = 0.0D;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i],
//						arrayOfDouble1);
//				l = System.nanoTime();
//				localDoubleFFT_3D.complexForward(arrayOfDouble1);
//				l = System.nanoTime() - l;
//				d += l;
//			}
//			arrayOfDouble[i] = (d / 1000000.0D / niter);
//			System.out.println("\tAverage execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(d / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfDouble1 = (double[][][]) null;
//			localDoubleFFT_3D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkDoubleComplexForwardFFT_3D_input_3D.txt", nthread,
//				niter, doWarmup, doScaling, sizes3D, arrayOfDouble);
//	}
//
//	public static void benchmarkRealForward_3D_input_1D() {
//		double[] arrayOfDouble1 = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			if (sizes3D[i] >= 1024)
//				continue;
//			System.out.println("Real forward FFT 3D (input 1D) of size "
//					+ sizes3D[i] + " x " + sizes3D[i] + " x " + sizes3D[i]);
//			DoubleFFT_3D localDoubleFFT_3D = new DoubleFFT_3D(sizes3D[i],
//					sizes3D[i], sizes3D[i]);
//			double[] arrayOfDouble2 = new double[sizes3D[i] * sizes3D[i] * 2
//					* sizes3D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfDouble2);
//				localDoubleFFT_3D.realForwardFull(arrayOfDouble2);
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfDouble2);
//				localDoubleFFT_3D.realForwardFull(arrayOfDouble2);
//			}
//			double d = 0.0D;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfDouble2);
//				l = System.nanoTime();
//				localDoubleFFT_3D.realForwardFull(arrayOfDouble2);
//				l = System.nanoTime() - l;
//				d += l;
//			}
//			arrayOfDouble1[i] = (d / 1000000.0D / niter);
//			System.out.println("\tAverage execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(d / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfDouble2 = null;
//			localDoubleFFT_3D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkDoubleRealForwardFFT_3D_input_1D.txt", nthread,
//				niter, doWarmup, doScaling, sizes3D, arrayOfDouble1);
//	}
//
//	public static void benchmarkRealForward_3D_input_3D() {
//		double[] arrayOfDouble = new double[nsize];
//		for (int i = 0; i < nsize; ++i) {
//			System.out.println("Real forward FFT 3D (input 3D) of size "
//					+ sizes3D[i] + " x " + sizes3D[i] + " x " + sizes3D[i]);
//			DoubleFFT_3D localDoubleFFT_3D = new DoubleFFT_3D(sizes3D[i],
//					sizes3D[i], sizes3D[i]);
//			double[][][] arrayOfDouble1 = new double[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
//			if (doWarmup) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfDouble1);
//				localDoubleFFT_3D.realForwardFull(arrayOfDouble1);
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfDouble1);
//				localDoubleFFT_3D.realForwardFull(arrayOfDouble1);
//			}
//			double d = 0.0D;
//			long l = 0L;
//			for (int j = 0; j < niter; ++j) {
//				IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i],
//						arrayOfDouble1);
//				l = System.nanoTime();
//				localDoubleFFT_3D.realForwardFull(arrayOfDouble1);
//				l = System.nanoTime() - l;
//				d += l;
//			}
//			arrayOfDouble[i] = (d / 1000000.0D / niter);
//			System.out.println("\tAverage execution time: "
//					+ String.format(
//							"%.2f",
//							new Object[] { Double.valueOf(d / 1000000.0D
//									/ niter) }) + " msec");
//			arrayOfDouble1 = (double[][][]) null;
//			localDoubleFFT_3D = null;
//			System.gc();
//			ConcurrencyUtils.sleep(5000L);
//		}
//		IOUtils.writeFFTBenchmarkResultsToFile(
//				"benchmarkDoubleRealForwardFFT_3D_input_3D.txt", nthread,
//				niter, doWarmup, doScaling, sizes3D, arrayOfDouble);
//	}
//
//	public static void main(String[] paramArrayOfString) {
//		parseArguments(paramArrayOfString);
//		benchmarkComplexForward_1D();
//		benchmarkRealForward_1D();
//		benchmarkComplexForward_2D_input_1D();
//		benchmarkComplexForward_2D_input_2D();
//		benchmarkRealForward_2D_input_1D();
//		benchmarkRealForward_2D_input_2D();
//		benchmarkComplexForward_3D_input_1D();
//		benchmarkComplexForward_3D_input_3D();
//		benchmarkRealForward_3D_input_1D();
//		benchmarkRealForward_3D_input_3D();
//		System.exit(0);
//	}
//}