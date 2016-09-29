package ps.hell.util.tcp;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ps.hell.util.FileUtil;


public class Service extends Thread implements Serializable {

	public static Logger log = LoggerFactory.getLogger(Service.class);

	/**
	 * 缓存 key 存储 client 的ip:port value 为 是否有效 String 192.168.1.11:10008 value
	 * 对应描述 + &true|false;
	 */
	public static HashMap<String, TcpBean> map = new HashMap<String, TcpBean>();
	public int socketPort = 6002;
	ServerSocket ss = null;
	public int port = 6001;
	public boolean isStop = false;

	public int count = 0;

	public static String logFile = System.getProperty("user.dir") + "/Config/configLog.txt";
	/**
	 * 是否使用配置文件
	 */
	public boolean isPurchuse = false;

	public Service(int port, boolean isPurchuse) {
		this.port = port;
		this.isPurchuse = isPurchuse;
		// 如果实用配置文件则需要从Config/configLog.txt 中获取有所的历史信息
		if (this.isPurchuse) {
			FileUtil fileUtil = new FileUtil(logFile, "utf-8", false,null);
			ArrayList<String> list = fileUtil.readAndClose();
			for(String str:list){
				String[] temp = str.split("\t");
				//log.info("加载log ip地址:" + temp);
				map.put(temp[0], new TcpBean(temp[1], true));
			}
		}
	}

	/**
	 * 像指定客户端传递信息 开启或停止相关etl程序
	 */
	public boolean post(String ipInfoOther, boolean isRun, boolean isStop,boolean isPrint) {
		String[] lll = ipInfoOther.split("@")[1].split(":");
		// 获取map值是否关闭
		Boolean socketIsRun =null;
		TcpBean bean=map.get(lll[0] + ":" + lll[1]);
		if(bean!=null)
		{
			socketIsRun=bean.isConnection;
		}
		// for (Entry<String, TcpBean> m : map.entrySet()) {
		// System.out.println("key:" + m.getKey());
		// }
		// System.out.println("请求客户端信息:" + lll[0] + ":" + lll[1] + "\tisRun:"
		// + isRun + "\tisStop:" + isStop);
		if (socketIsRun == null) {
			log.error("不存在socket:" + map.get(lll[0] + ":" + lll[1]));
			log.error("请启动相关socket服务");
			return false;
		} else if (socketIsRun == false) {
			log.error("socket已停止:" + map.get(lll[0] + ":" + lll[1]));
			log.error("请启动相关socket服务");
			return false;
		} else {
			// System.out.println("发送客户端信息:" + lll[0] + ":" + lll[1] +
			// "\tisRun:"
			// + isRun + "\tisStop:" + isStop);
			Boolean result=false;
			try{
			ClientSocket client = new ClientSocket(lll[0],
					Integer.parseInt(lll[1]), 3000);
			// 向socket发送信息
			result= Boolean.parseBoolean((String) client
					.post("server=modify&ipInfo=" + ipInfoOther + "&isSonRun="
							+ isRun + "&isSonStop=" + isStop+"&isPrint="+isPrint));
			// System.out.println("获取修改客户端返回信息:" + result);
			}catch(Exception e)
			{
				
			}
			return result;
		}
	}

	public long index = -1;

	@Override
	public void run() {
		log.info("服务端启动");
		try{
		ss = new ServerSocket(port);
		}catch(Exception e)
		{
			log.error("异常");
			e.printStackTrace();
			System.exit(1);
		}
		try {
		
			while (true) {
				Socket s = ss.accept();
				new ServiceSocket(s, new String()).start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error("service 结束");
		}
	}

	/**
	 * 移除其中一个历史ip信息
	 * @param ipInfo
	 * @return
	 */
	public synchronized boolean remove(String ipInfo) {
		System.out.println("移除:"+ipInfo);
		for(Entry<String,TcpBean> m:map.entrySet())
		{
			System.out.println(m.getKey());
		}
		TcpBean bean = map.remove(ipInfo);
		if (bean == null) {
			return false;
		} else {
			flushFile();
			return true;
		}
	}
	/**
	 * 获取日志信息
	 * @param ipInfoOther
	 * @param isMain
	 * @param lastLineCount
	 * @return
	 */
	public synchronized String getLog(String ipInfoOther,boolean isMain,int lastLineCount)
	{
		System.out.println(ipInfoOther);
		String[] lll = ipInfoOther.split("@")[1].split(":");
		// 获取map值是否关闭
		Boolean socketIsRun =null;
		TcpBean bean=map.get(lll[0] + ":" + lll[1]);
		if(bean!=null)
		{
			socketIsRun=bean.isConnection;
		}
		if (socketIsRun == null) {
			log.error("不存在socket:" + map.get(lll[0] + ":" + lll[1]));
			log.error("请启动相关socket服务");
			return ipInfoOther+ " connection error";
		} else if (socketIsRun == false) {
			log.error("socket已停止:" + map.get(lll[0] + ":" + lll[1]));
			log.error("请启动相关socket服务");
			return ipInfoOther + " connection error";
		} else {
			// System.out.println("发送客户端信息:" + lll[0] + ":" + lll[1] +
			// "\tisRun:"
			// + isRun + "\tisStop:" + isStop);
			String result ="";
			try{
			ClientSocket client = new ClientSocket(lll[0],
					Integer.parseInt(lll[1]), 30000);
			// 向socket发送信息
			result= (String) client
					.post("server=getLog&ipInfo=" + ipInfoOther + "&isMain="+isMain
							+"&lastLineCount="+lastLineCount);
			}catch(Exception e)
			{
				log.error(e.getMessage());
				e.printStackTrace();
			}
			// System.out.println("获取修改客户端返回信息:" + result);
			return result;
		}
	}
	

	/**
	 * 刷新配置文件
	 */
	public static synchronized void flushFile() {
		FileUtil fileUtil = new FileUtil(logFile, "utf-8",true,"delete");
		LinkedList<String> list = new LinkedList<String>();
		for (Entry<String, TcpBean> m : map.entrySet()) {
			list.add(m.getKey() + "\t" + m.getValue().desc);
		}
		fileUtil.write(list);
		fileUtil.close();
	}

	/**
	 * 如果str为 getInfo 则表示 获取所有的数据
	 * 
	 * @param str
	 */
	public ArrayList<Object> post(String str) {
		count++;
		int val = count % 5;
		ArrayList<Object> list = new ArrayList<Object>();
		LinkedList<String> notConnection = new LinkedList<String>();
		for (Entry<String, TcpBean> m : map.entrySet()) {
			String key = m.getKey();
			Boolean temp = m.getValue().isConnection;
			if (val != 4) {
				if (temp == false) {
					continue;
				}
			}
			// 获取每一个str的socket
			String[] stTemp = key.split(":");
			try {
				 System.out.println("发送客户端请求数据:" + stTemp[0] + ":" +
				 stTemp[1]);
				ClientSocket client = new ClientSocket(stTemp[0],
						Integer.parseInt(stTemp[1]), 3000);
				// System.out.println(str);
				Object etlInfo =  client.post(str);
				// System.out.println("获取数据:" + etlInfo.toString());
				list.add(etlInfo);
				System.out.println("成功");
			} catch (Exception e) {
				// 表示 连接失败不存在
				notConnection.add(key);
			}
		}
		while (notConnection.size() > 0) {
			// 修改所有socket连接失败的并修改状态
			String temp = notConnection.pollFirst();
			map.put(temp, new TcpBean(map.get(temp).desc, false));
		}
		return list;
	}

	public void stopService() {
		if (ss != null) {
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// int port = 6001;
		// Service server = new Service(port,false);
		// server.start();
		// OutputStream os = null;
		// InputStream is = null;
		// ClientSocket client = new ClientSocket("127.0.0.1", 6001, 3000);
		// // client.startServer(6002);
		// SerialEtlInfo etl = new SerialEtlInfo(1);
		// // for (int i = 0; i < 100; i++) {
		// // client.Post("i:" + i);
		// // }
		// // System.out.println(client.post(etl));
		//
		// System.out.println(client.post(etl));
		// server.post("新");
		// server.stopService();

		// byte[] bytes=SerializableMethod.serializeObjByte(etl);
		// System.out.println(bytes.length);
		// Object objRev = SerializableMethod.revSerializeObjByte(bytes);
		// System.out.println("结束");
		// System.out.println("服务端:" + objRev.toString());
		// System.out.println(":"+((SerialEtlInfo)objRev).name);

		Service server = new Service(60001, true);
		server.start();
		// 并触发向web端发送新的ip:port
		ClientSocket client = new ClientSocket("127.0.0.1", 60001, 30000);
		// 发送请求
		String temp = client.Post("127.0.0.1:60003");
		System.out.println(temp);
		server.stopService();
		for (Entry<String, TcpBean> m : Service.map.entrySet()) {
			System.out.println(m.getKey() + "----" + m.getValue().isConnection);
		}
	}
}
