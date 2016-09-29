package ps.hell.util.memory.logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MemoryUtil {
	private static final Logger log = LoggerFactory.getLogger(MemoryUtil.class);
	private static volatile ScheduledExecutorService scheduler;

	public static void logMemoryStatistics() {
		Runtime runtime = Runtime.getRuntime();
		long freeBytes = runtime.freeMemory();
		long maxBytes = runtime.maxMemory();
		long totalBytes = runtime.totalMemory();
		long usedBytes = totalBytes - freeBytes;
		log.info(
				"Memory (bytes): {} used, {} heap, {} max",
				new Object[] { Long.valueOf(usedBytes),
						Long.valueOf(totalBytes), Long.valueOf(maxBytes) });
	}

	public static void startMemoryLogger(long rateInMillis) {
		stopMemoryLogger();
		scheduler = Executors.newScheduledThreadPool(1, new ThreadFactory() {
//			private final ThreadFactory delegate;
			@Override
			public Thread newThread(Runnable r) {
//				Thread t = this.delegate.newThread(r);
				Thread t=new Thread(r);
				t.setDaemon(true);
				return t;
			}
		});
		Runnable memoryLoogerRunnable = new Runnable() {
			public void run() {
				MemoryUtil.logMemoryStatistics();
			}
		};
		scheduler.scheduleAtFixedRate(memoryLoogerRunnable, rateInMillis,
				rateInMillis, TimeUnit.MILLISECONDS);
	}

	public static void startMemoryLogger() {
		startMemoryLogger(1000L);
	}

	public static void stopMemoryLogger() {
		if (scheduler != null) {
			scheduler.shutdownNow();
			scheduler = null;
		}
	}
	
}