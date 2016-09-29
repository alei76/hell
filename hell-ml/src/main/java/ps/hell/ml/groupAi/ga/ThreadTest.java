package ps.hell.ml.groupAi.ga;


public class ThreadTest implements Runnable{

	private int threadCount=0;
	public ThreadTest(int threadCount)
	{
		this.threadCount=threadCount;
	}
	private int count=0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			if(false)
			{
				break;
			}
			//System.out.println(Thread.currentThread().getName()+"执行");
			double dol=0;
			for(int i=0;i<19999;i++)
			{
				dol+=Math.sqrt(i);
			}
			if(set(dol)==threadCount)
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (this) {
					System.out.println("--------"+Thread.currentThread().getName()+"\t激活");
					count=0;
					this.notifyAll();
				}
			}else{
				synchronized (this) {
					try {
						//System.out.println(Thread.currentThread().getName()+"\t等待");
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	public synchronized int set(double dol)
	{
		count++;
		return count;
	}

	public static void main(String[] args) {
		int count=50;
		ThreadTest test=new ThreadTest(count);
		Thread[] thread=new Thread[count];
		for(int i=0;i<count;i++)
		{
			thread[i]=new Thread(test,Integer.toString(i));
			thread[i].start();
		}
		for(int i=0;i<count;i++)
		{
			try {
				thread[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
