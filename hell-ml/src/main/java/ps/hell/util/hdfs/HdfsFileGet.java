package ps.hell.util.hdfs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
//
//import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
//
//public class HdfsFileGet {
//	static {
//		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
//	}
//	public HdfsFileGet(){
//
//	}
//
//	/**
//	 * line byte length 40960 start with # continue line
//	 *
//	 * @param configPath
//	 * @return
//	 */
//	public static ArrayList<String> getFile(String configPath) {
//		ArrayList<String> list = new ArrayList<String>();
//		if (configPath == null) {
//			return list;
//		}
//
//		while (true) {
//			InputStream in = null;
//			try {
//				// String hdfsPathUrl="hdfs://master:9000/storm_plugin_conf/";
//				URL url = new URL(configPath);
//				in = url.openStream();
//				byte[] b = new byte[40960];
//				BufferedReader in2 = new BufferedReader(new InputStreamReader(
//						in));
//				String param = null;
//				while ((param = in2.readLine()) != null) {
//
//					param = param.trim();
//					if (param.startsWith("#") || param.length() <= 0) {
//						continue;
//					}
//					list.add(param);
//				}
//
//			} catch (FileNotFoundException fe) {
//				try {
//					if (in != null) {
//						in.close();
//					}
//				} catch (Exception e) {
//
//				}
//				return list;
//			} catch (Exception e) {
//				try {
//					if (in != null) {
//						in.close();
//					}
//				} catch (Exception e2) {
//
//				}
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				e.printStackTrace();
//				continue;
//			}
//			return list;
//		}
//	}
//
//	public static void main(String[] args) {
//		ArrayList<String> list = HdfsFileGet
//				.getFile("hdfs://fcredis:9000/storm_plugin_conf/infoQueue.properties");
//		for (String str : list) {
//			System.out.println(str);
//		}
//	}
//}
