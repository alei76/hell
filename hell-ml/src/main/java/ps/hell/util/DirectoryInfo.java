package ps.hell.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 获取文件信息
 * 
 * @author Administrator
 *
 */
public class DirectoryInfo {

	public enum FILE_PARAM {
		BYTE, KB, MB, GB, TB
	}

	/**
	 * 获取满足文件的内容
	 * 
	 * @param dir 目录
	 * @param endsWith 文件结尾
	 * @param param 大小量纲
	 * @param from 大小范围
	 * @param to 大小范围
	 * @return
	 */
	public HashMap<String, Long> getFileFromSize(File dir, String endsWith,
			FILE_PARAM param, long from, long to) {
		HashMap<String, Long> map = new HashMap<String, Long>();
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files == null) {
				System.out.println(dir.getAbsolutePath());
			} else {
				for (File file : files) {
					HashMap<String, Long> m = getFileFromSize(file, endsWith,
							param, from, to);
					for (Entry<String, Long> m2 : m.entrySet()) {
						map.put(m2.getKey(), m2.getValue());
					}
				}
			}
		} else if (dir.getAbsolutePath().endsWith(endsWith)) {
			long size = getSize(dir, endsWith, param);
//			System.out.println(size);
			if (size >= from && size <= to) {
				map.put(dir.getAbsolutePath(), size);
			}
		}
		return map;
	}

	/**
	 * 获取文件大小 传入的为byte
	 * 
	 * @param dir
	 *            目录或文件
	 * @param endWith
	 *            获取结尾为这个的文件
	 * @param param
	 *            文件量纲 默认为mb
	 * @return
	 */
	public long getSize(File dir, String endsWith, FILE_PARAM param) {
		long size = getSonSize(dir, endsWith);
		if (param == null) {
			return size / 1024 / 1024;
		} else if (param == FILE_PARAM.BYTE) {
			return size;
		} else if (param == FILE_PARAM.KB) {
			return size / 1024;
		} else if (param == FILE_PARAM.MB) {
			return size / 1024 / 1024;
		} else if (param == FILE_PARAM.GB) {
			return size / 1024 / 1024 / 1024;
		} else if (param == FILE_PARAM.TB) {
			return size / 1024 / 1024 / 1024 / 1024;
		} else {
			return size / 1024 / 1024;
		}
	}

	private long getSonSize(File dir, String endsWith) {
		long size = 0;
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files == null) {
				System.out.println(dir.getAbsolutePath());
			} else {
				for (File file : files) {
					size += getSonSize(file, endsWith);
				}
			}
		} else if (dir.getAbsolutePath().endsWith(endsWith)) {
			size = dir.length();
		}
		return size;
	}

	public static void main(String[] args) {
		File dir = new File("H:\\eclipse\\workspaceML\\dm_ml_landerbuluse\\src");
		DirectoryInfo tool = new DirectoryInfo();
		long size = tool.getSize(dir, "java", null);
		System.out.println(size + " mb");
		dir = new File("G:\\");
		HashMap<String, Long> fileGet = tool.getFileFromSize(dir, "", FILE_PARAM.MB,
				100, 100000);
		for (Entry<String, Long> m : fileGet.entrySet()) {
			System.out.println(m.getKey() + "\t" + m.getValue() + " mb");
		}
	}
}
