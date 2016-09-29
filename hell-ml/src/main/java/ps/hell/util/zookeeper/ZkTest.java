package ps.hell.util.zookeeper;

import ps.hell.util.zookeeper.ConcurrentTest.ConcurrentTask;

public class ZkTest {
	public static void main(String[] args) {
		Runnable task1 = new Runnable() {
			public void run() {
				DistributedLock lock = null;
				try {
					lock = new DistributedLock("127.0.0.1:2182", "test1");
					// lock = new DistributedLock("127.0.0.1:2182","test2");
					lock.lock();
					Thread.sleep(3000);
					System.out.println("===Thread "
							+ Thread.currentThread().getId() + " running");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (lock != null)
						lock.unlock();
				}

			}

		};
		new Thread(task1).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		ConcurrentTask[] tasks = new ConcurrentTask[60];
		for (int i = 0; i < tasks.length; i++) {
			ConcurrentTask task3 = new ConcurrentTask() {
				public void run() {
					DistributedLock lock = null;
					try {
						lock = new DistributedLock("127.0.0.1:2183", "test2");
						lock.lock();
						System.out.println("Thread "
								+ Thread.currentThread().getId() + " running");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						lock.unlock();
					}

				}
			};
			tasks[i] = task3;
		}
		new ConcurrentTest(tasks);
	}
}