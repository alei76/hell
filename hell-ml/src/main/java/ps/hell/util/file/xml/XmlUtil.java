package ps.hell.util.file.xml;

import ps.hell.util.FileUtil;

import com.thoughtworks.xstream.XStream;

public class XmlUtil {

	private static XStream xstream = new XStream();
	static {
		xstream.autodetectAnnotations(true);
	}

	/**
	 * 将obj写入文件中
	 * 
	 * @param file
	 * @param obj
	 * @return
	 */
	public static boolean write(String file, Object obj) {
		try {
			FileUtil fileUtil = new FileUtil(file, "utf-8",true, "delete");
			System.out.println();
			fileUtil.write(xstream.toXML(obj));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 读取数据
	 * 
	 * @param file
	 * @return
	 */
	public static Object read(String file) {
		return xstream.fromXML(file);
	}
}
