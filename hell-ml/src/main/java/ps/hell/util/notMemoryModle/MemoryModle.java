package ps.hell.util.notMemoryModle;

import ps.hell.util.FileUtil;

/**
 * 单机 处理超内存的数据模型框架
 * @author Administrator
 *
 */
public class MemoryModle {

	/**
	 * 文件读取统一使用流式处理模式
	 */
	public String readStreamNext(FileUtil fileTool){
		return fileTool.getNextLine();
	}
}
