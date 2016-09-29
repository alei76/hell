package ps.hell.math.base.persons.fft.transform.util;

import java.util.concurrent.*;

public class ConcurrencyUtils {
	private static final ExecutorService THREAD_POOL = Executors
			.newCachedThreadPool(new CustomThreadFactory(
					new CustomExceptionHandler()));
	private static int THREADS_BEGIN_N_1D_FFT_2THREADS = 8192;
	private static int THREADS_BEGIN_N_1D_FFT_4THREADS = 65536;
	private static int THREADS_BEGIN_N_2D = 65536;
	private static int THREADS_BEGIN_N_3D = 65536;
	private static int NTHREADS = prevPow2(getNumberOfProcessors());

	public static int getNumberOfProcessors() {
		return Runtime.getRuntime().availableProcessors();
	}

	public static int getNumberOfThreads() {
		return NTHREADS;
	}

	public static void setNumberOfThreads(int paramInt) {
		NTHREADS = prevPow2(paramInt);
	}

	public static int getThreadsBeginN_1D_FFT_2Threads() {
		return THREADS_BEGIN_N_1D_FFT_2THREADS;
	}

	public static int getThreadsBeginN_1D_FFT_4Threads() {
		return THREADS_BEGIN_N_1D_FFT_4THREADS;
	}

	public static int getThreadsBeginN_2D() {
		return THREADS_BEGIN_N_2D;
	}

	public static int getThreadsBeginN_3D() {
		return THREADS_BEGIN_N_3D;
	}

	public static void setThreadsBeginN_1D_FFT_2Threads(int paramInt) {
		if (paramInt < 512)
			THREADS_BEGIN_N_1D_FFT_2THREADS = 512;
		else
			THREADS_BEGIN_N_1D_FFT_2THREADS = paramInt;
	}

	public static void setThreadsBeginN_1D_FFT_4Threads(int paramInt) {
		if (paramInt < 512)
			THREADS_BEGIN_N_1D_FFT_4THREADS = 512;
		else
			THREADS_BEGIN_N_1D_FFT_4THREADS = paramInt;
	}

	public static void setThreadsBeginN_2D(int paramInt) {
		THREADS_BEGIN_N_2D = paramInt;
	}

	public static void setThreadsBeginN_3D(int paramInt) {
		THREADS_BEGIN_N_3D = paramInt;
	}

	public static void resetThreadsBeginN_FFT() {
		THREADS_BEGIN_N_1D_FFT_2THREADS = 8192;
		THREADS_BEGIN_N_1D_FFT_4THREADS = 65536;
	}

	public static void resetThreadsBeginN() {
		THREADS_BEGIN_N_2D = 65536;
		THREADS_BEGIN_N_3D = 65536;
	}

	public static int nextPow2(int paramInt) {
		if (paramInt < 1)
			throw new IllegalArgumentException("x must be greater or equal 1");
		if ((paramInt & paramInt - 1) == 0)
			return paramInt;
		paramInt |= paramInt >>> 1;
		paramInt |= paramInt >>> 2;
		paramInt |= paramInt >>> 4;
		paramInt |= paramInt >>> 8;
		paramInt |= paramInt >>> 16;
		paramInt |= paramInt >>> 32;
		return (paramInt + 1);
	}

	public static int prevPow2(int paramInt) {
		if (paramInt < 1)
			throw new IllegalArgumentException("x must be greater or equal 1");
		return (int) Math.pow(2.0D,
				Math.floor(Math.log(paramInt) / Math.log(2.0D)));
	}

	public static boolean isPowerOf2(int paramInt) {
		if (paramInt <= 0)
			return false;
		return ((paramInt & paramInt - 1) == 0);
	}

	public static void sleep(long paramLong) {
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException localInterruptedException) {
			localInterruptedException.printStackTrace();
		}
	}

	public static Future<?> submit(Runnable paramRunnable) {
		return THREAD_POOL.submit(paramRunnable);
	}

	public static void waitForCompletion(Future<?>[] paramArrayOfFuture) {
		int i = paramArrayOfFuture.length;
		try {
			for (int j = 0; j < i; ++j)
				paramArrayOfFuture[j].get();
		} catch (ExecutionException localExecutionException) {
			localExecutionException.printStackTrace();
		} catch (InterruptedException localInterruptedException) {
			localInterruptedException.printStackTrace();
		}
	}

	private static class CustomThreadFactory implements ThreadFactory {
		private static final ThreadFactory defaultFactory = Executors
				.defaultThreadFactory();
		private final Thread.UncaughtExceptionHandler handler;

		CustomThreadFactory(
				Thread.UncaughtExceptionHandler paramUncaughtExceptionHandler) {
			this.handler = paramUncaughtExceptionHandler;
		}

		public Thread newThread(Runnable paramRunnable) {
			Thread localThread = defaultFactory.newThread(paramRunnable);
			localThread.setUncaughtExceptionHandler(this.handler);
			return localThread;
		}
	}

	private static class CustomExceptionHandler implements
			Thread.UncaughtExceptionHandler {
		public void uncaughtException(Thread paramThread,
				Throwable paramThrowable) {
			paramThrowable.printStackTrace();
		}
	}
}