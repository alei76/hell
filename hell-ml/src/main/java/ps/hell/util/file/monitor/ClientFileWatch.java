package ps.hell.util.file.monitor;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import name.pachler.nio.file.FileSystems;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.Paths;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchKey;
import name.pachler.nio.file.WatchService;
import name.pachler.nio.file.ext.ExtendedWatchEventModifier;

/**
 * 文件状态变更监控容器
 * 
 * @author Administrator
 *
 */
public class ClientFileWatch implements Runnable {
	/**
	 * 基础文件时间容器
	 */
	private BlockingQueue<FileStatusBean> queue = new ArrayBlockingQueue<FileStatusBean>(
			200);
	private WatchService watcher;
	
	private long timeOut=3000;
	/**
	 * 使用的目录
	 */
	private String path;
	/**
	 * 过滤掉包含的数据
	 */
	public HashSet<String> filterContain = new HashSet<String>();
	/**
	 * 过滤掉开始的字符
	 */
	public HashSet<String> filterStartsWith = new HashSet<String>();
	/**
	 * 过滤掉结束的字符
	 */
	public HashSet<String> filterEndsWith = new HashSet<String>();

	/**
	 * 初始化
	 */
	public void init() {
		filterContain.add("~");
		filterContain.add("$");
		filterContain.add(".log");
		filterEndsWith.add(".java");
	}

	public FileStatusBean getEvent() {
		while (true) {
			FileStatusBean bean=null;
			try {
				bean = queue.poll(timeOut,TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bean != null) {
				return bean;
			}
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param path
	 *            {@link String}
	 */
	public ClientFileWatch(String path) {
		this.path = path;
		if (this.standardEvents == null) {
			this.standardEvents = new WatchEvent.Kind[4];
			this.standardEvents[0] = StandardWatchEventKind.ENTRY_CREATE;
			this.standardEvents[1] = StandardWatchEventKind.ENTRY_DELETE;
			this.standardEvents[2] = StandardWatchEventKind.ENTRY_MODIFY;
			this.standardEvents[3] = StandardWatchEventKind.OVERFLOW;
		}
		init();
	}

	/**
	 * 使用的事件
	 */
	private WatchEvent.Kind[] standardEvents = null;

	/**
	 * 
	 * @param path
	 *            监控的目录
	 * @param standardEvents
	 *            使用的监控项目 standardEvents = {
	 *            StandardWatchEventKind.ENTRY_CREATE,
	 *            StandardWatchEventKind.ENTRY_DELETE,
	 *            StandardWatchEventKind.ENTRY_MODIFY,
	 *            StandardWatchEventKind.OVERFLOW };
	 */
	public ClientFileWatch(String path, WatchEvent.Kind[] standardEvents) {
		this.path = path;
		this.standardEvents = standardEvents;
		if (this.standardEvents == null) {
			this.standardEvents = new WatchEvent.Kind[4];
			this.standardEvents[0] = StandardWatchEventKind.ENTRY_CREATE;
			this.standardEvents[1] = StandardWatchEventKind.ENTRY_DELETE;
			this.standardEvents[2] = StandardWatchEventKind.ENTRY_MODIFY;
			this.standardEvents[3] = StandardWatchEventKind.OVERFLOW;
		}
		init();
	}

	@Override
	public void run() {

		try {
			this.filewatch(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private void filewatch(String paths) throws Exception {
		watcher = FileSystems.getDefault().newWatchService();
		Path path = Paths.get(paths);
		path.register(watcher, standardEvents,
				ExtendedWatchEventModifier.FILE_TREE);
		while (true) {
			WatchKey key = watcher.take();
			List<WatchEvent<?>> list = key.pollEvents();
			key.reset();
			/**
			 * 获取事件列表
			 */
			for (WatchEvent event : list) {
				// 文件名过滤条件
				FileStatusBean bean = new FileStatusBean(event, filterContain,
						filterStartsWith, filterEndsWith);
				this.queue.add(bean);
			}
			if (!key.reset()) {
				break;
			}
		}
	}

	public static void main(String[] args) {
		ClientFileWatch clientFileWater = new ClientFileWatch("f:/jsonTest");
		Thread thread = new Thread(clientFileWater);
		thread.start();
	}
}