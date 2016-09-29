package ps.hell.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ps.hell.util.db.CursorInter;

/**
 * 文件读写操作类
 * 
 * @author Administrator
 *
 */
public class FileUtil<T> implements CursorInter, Serializable {
	Logger log = LoggerFactory.getLogger(FileUtil.class);
	/**
	 * 文件指针
	 */
	public File file = null;

	public File[] files = null;
	public int fileIndex = -1;
	/**
	 * 文件类型
	 */
	private String code = "utf-8";
	private OutputStreamWriter write;
	private BufferedWriter writer;
	private Object object = new Object();
	public boolean isWrite = false;

	public String getCode() {
		return code;
	}

	/**
	 * 写文件
	 * 
	 * @param filename
	 * @param code
	 *            字节码
	 * @param fileStatus 文件状态 主要是删除
	 *            delete
	 */
	public FileUtil(String filename, String code, boolean isWrite,
			String fileStatus) {
		// System.out.println(filename + "\t" + code);
		this.isWrite = isWrite;
		// 文件名需要重制定
		String fileName2 = filename;
		file = new File(fileName2);
		if (isWrite) {
			if (!file.exists()) {
				log.info("文件不存在:" + fileName2);
				// 创建文件
				try {
					log.info("创建文件:" + fileName2);
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

				int zn = 0;
				if (fileStatus.equals("delete")) {
					file.delete();
				} else {
					while (true) {
						zn++;
						File fileTemp = new File(fileName2.substring(0,
								fileName2.lastIndexOf("."))
								+ "_bak_"
								+ zn
								+ fileName2.substring(fileName2
										.lastIndexOf(".")));
						if (fileTemp.exists()) {
							continue;
						} else {
							log.info("转移文件：" + fileName2 + "\t"
									+ fileTemp.getAbsolutePath());
							file.renameTo(fileTemp);
							file = new File(fileName2);
							break;
						}
					}
				}
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.code = code;
			try {
				write = new OutputStreamWriter(
						new FileOutputStream(file, true), this.code);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer = new BufferedWriter(write);
		}else{
			this.code=code;
		}
		
	}

	BufferedReader readerTemp = null;
	InputStreamReader readTemp = null;

	/**
	 * 获取下一行 线程安全方法
	 * 
	 * @return 如果为null 则为文件获取结束
	 */
	public synchronized String getNextLineSYN() {
		String tempString = null;
		if (readTemp == null) {
			try {
				if (file.isDirectory()) {
					if (files == null) {
						files = file.listFiles();
					}
					if (files.length > fileIndex + 1) {
						fileIndex++;
						readTemp = new InputStreamReader(new FileInputStream(
								files[fileIndex]), code);
						readerTemp = new BufferedReader(readTemp);
					} else {
						// 目录便利完毕
						return null;
					}
				} else {
					if (isEnd) {// 如果一个文件的结束则结束
						return null;
					}
					readTemp = new InputStreamReader(new FileInputStream(file),
							code);
					readerTemp = new BufferedReader(readTemp);
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			while ((tempString = readerTemp.readLine()) != null) {
				if (tempString.length() == 0) {
					continue;
				}
				return tempString.trim();
			}
			readTemp = null;
			isEnd = true;
			return getNextLineSYN();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempString;
	}

	@Override
	public String nextString() {
		return getNextLine();
	}

	@Override
	public String getDB() {
		return "File";
	}

	@Override
	public String getPath() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 获得本机IP
		// 获得本机名称
		return addr.getHostAddress().toString() + ":"
				+ addr.getHostName().toString() + "/database=file/table="
				+ file.getAbsolutePath();
	}

	@Override
	public void insertString(String insertString) {
		// TODO Auto-generated method stub
		this.write(insertString);
	}

	public boolean isEnd = false;

	/**
	 * 获取下一行 线程不安全方法
	 * 
	 * @return 如果为null 则为文件获取结束
	 */
	public String getNextLine() {
		String tempString = null;
		if (readTemp == null) {
			try {
				if (file.isDirectory()) {
					if (files == null) {
						files = file.listFiles();
					}
					if (files.length > fileIndex + 1) {
						fileIndex++;
						readTemp = new InputStreamReader(new FileInputStream(
								files[fileIndex]), code);
						readerTemp = new BufferedReader(readTemp);
					} else {
						// 目录便利完毕
						return null;
					}
				} else {
					if (isEnd) {// 如果一个文件的结束则结束
						return null;
					}
					readTemp = new InputStreamReader(new FileInputStream(file),
							code);
					readerTemp = new BufferedReader(readTemp);
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			while ((tempString = readerTemp.readLine()) != null) {
				if (tempString.length() == 0) {
					continue;
				}
				return tempString.trim();
			}
			readTemp = null;
			isEnd = true;
			return getNextLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempString;
	}

	/**
	 * 获取所有数据
	 * 
	 * @return
	 */
	public ArrayList<String> readAndClose() {
		ArrayList<String> result = new ArrayList<String>();
		BufferedReader reader = null;
		try {

			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), this.code);
			reader = new BufferedReader(read);
			String tempString = null;
			//System.out.println("开始读取文件");
			// 一次读入一行，直到读入null为文件结束
			// 是否为注释
			while ((tempString = reader.readLine()) != null) {
				if (tempString.length() == 0) {
					continue;
				}
				result.add(tempString.trim());
			}
			try {
				reader.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return result;
	}

	/**
	 * 获取所有数据
	 * 
	 * @return
	 */
	public String readAllAndClose() {
		StringBuffer result = new StringBuffer();
		BufferedReader reader = null;
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), this.code);
			reader = new BufferedReader(read);
			String tempString = null;
			System.out.println("开始读取文件");
			// 一次读入一行，直到读入null为文件结束
			// 是否为注释
			while ((tempString = reader.readLine()) != null) {
				if (tempString.length() == 0) {
					continue;
				}
				result.append(tempString);
			}
			try {
				reader.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return result.toString();
	}

	/**
	 * 获取所有数据 回车分隔符替换为 split
	 * 
	 * @return
	 */
	public String readAllAndClose2(String split) {
		StringBuffer result = new StringBuffer();
		BufferedReader reader = null;
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), this.code);
			reader = new BufferedReader(read);
			String tempString = null;
			System.out.println("开始读取文件");
			// 一次读入一行，直到读入null为文件结束
			// 是否为注释
			while ((tempString = reader.readLine()) != null) {
				if (tempString.length() == 0) {
					continue;
				}
				result.append(tempString).append(split);
			}
			try {
				reader.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return result.toString();
	}

	/**
	 * 获取所有数据 回车分隔符替换为 split
	 * 
	 * @param deleteString
	 *            为 开头为这个的则删除该行
	 * @return
	 */
	public String readAllAndClose2(String split, String deleteString) {
		StringBuffer result = new StringBuffer();
		BufferedReader reader = null;
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file),this.code);
			reader = new BufferedReader(read);
			String tempString = null;
			System.out.println("开始读取文件");
			// 一次读入一行，直到读入null为文件结束
			// 是否为注释
			while ((tempString = reader.readLine()) != null) {
				if (tempString.length() == 0) {
					continue;
				}
				if (tempString.trim().startsWith(deleteString)) {
					continue;
				}
				result.append(tempString).append(split);
			}
			try {
				reader.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return result.toString();
	}

	public void write(ArrayList<String> list) {
		if (list == null) {
			return;
		}
		synchronized (object) {
			for(int i=0;i<list.size();i++) {
				String str = list.get(i);
				if (str == null) {
					continue;
				}
				try {
					write.write(str + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				writer.flush();
				write.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void write(LinkedList<String> list) {
		if (list == null) {
			return;
		}
		synchronized (object) {
			while(list.size()>0){
				String str = list.poll();
				if (str == null) {
					continue;
				}
				try {
					write.write(str + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				writer.flush();
				write.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 写入文件的内容
	 * 
	 * queue 队列 放入队列中的数量
	 * @param str
	 */
	public void write(String str) {
		synchronized (object) {
			// 如果异常则不管
			if (str == null || str.length() < 1) {
				return;
			}
			try {
				// log.info("写入文件内容：" + str);
				writer.write(str);
				writer.write("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				writer.flush();
				write.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 写入文件的内容
	 * 
	 * queue 放入队列中的数量
	 * @param map
	 */
	public void write(HashMap<T, T> map) {
		synchronized (object) {
			for (Entry<T, T> entry : map.entrySet()) {
				try {
					write.write(entry.getKey() + "=" + entry.getValue() + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				writer.flush();
				write.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 写入文件的内容
	 * 
	 *  queue 放入队列中的数量
	 *            队列
	 * @param map
	 * @param sort
	 */
	public void write(Hashtable<T, T> map, ArrayList<String> sort) {
		synchronized (object) {
			try {
				for (String str : sort) {
					Object obj = map.get(str);
					if (obj == null) {
						write.write(str + "\t= ");
					} else {
						write.write(str + "\t= " + obj);
					}
					write.write("\n");

				}
			} catch (Exception e) {

			}

			try {
				writer.flush();
				write.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 写入文件的内容
	 * 
	 * @param queue 放入队列中的数量
	 *
	 * @param size
	 */
	public void wirte(LinkedBlockingQueue<String> queue, int size) {
		synchronized (object) {
			for (int i = 0; i < size; i++) {
				// 如果异常则不管
				String str = queue.poll();
				if (str == null || str.length() < 1) {
					continue;
				}
				try {
					// log.info("写入文件内容：" + str);
					writer.write(str);
					writer.write("\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				writer.flush();
				write.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 写入文件的内容
	 * 
	 * @param queue 放入队列中的数量
	 *            队列
	 * @param size
	 */
	public void wirte(LinkedList<String> queue, int size) {
		synchronized (object) {
			for (int i = 0; i < size; i++) {
				// 如果异常则不管
				String str = queue.poll();
				if (str == null || str.length() < 1) {
					continue;
				}
				try {
					// log.info("写入文件内容：" + str);
					writer.write(str);
					writer.write("\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				writer.flush();
				write.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭当前文件io
	 */
	public void close() {
		try {
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// String filename="d:\\sdf\\sdf\\wrqwer.txt";
		// System.out.println(filename.substring(0,filename.indexOf("."))+"-"+DateFormat.dateTime+"."+
		// filename.substring(filename.indexOf(".")+1));
	}

}
