package ps.hell.util.file.monitor;

/**
 * 文件变更监控
 * @author Administrator
 *
 */
public class FileMonitorTest {
	public static void main(String[] args) {
		ClientFileWatch clientFileWater=new ClientFileWatch("f:/jsonTest");
		Thread thread=new Thread(clientFileWater);
		thread.start();
		//队列监控器
		while(true){
			System.out.println(clientFileWater.getEvent());
		}
	}
}
