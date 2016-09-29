package ps.hell.util.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;






import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ps.hell.util.serializable.SerializableMethod;

public class ClientSocket {
	
	public static Logger log = LoggerFactory.getLogger(ClientSocket.class);
	public Socket s;
	private String ip;
	private int port;
	int sotimeout = 100000;
	Service server = null;

	public ClientSocket(String ip, int port, int sotimeout) throws Exception {
		this.ip = ip;
		this.port = port;
		this.sotimeout = sotimeout;
		//需要判断端口是否重复
		try{
			Socket socket = new Socket(ip,port); 
			//socket.isConnected();
		}catch(Exception e)
		{
			log.error("端口被占用或连接异常,退出:"+ip+":"+port);
			//e.printStackTrace();
			//System.exit(1);
			throw e;
		}
	}

	public String Post(String str) {
		String str2 = "";
		try {
			this.s = new Socket(ip, port);
			// this.s.setKeepAlive(true);
			this.s.setSoTimeout(sotimeout);
			OutputStream os = s.getOutputStream();
			InputStream is = s.getInputStream();
			os.write(str.getBytes("UTF-8"));
			byte[] buf = new byte[100000];
			int len = is.read(buf);
			os.close();
			is.close();
			if(len>0)
			{
				str2 = new String(buf, 0, len);
			}
			s.close();
			log.info("接受服务端信息:" + str2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str2;
	}

	/**
	 * 传入 参数并 获取参数
	 * 
	 * @param obj
	 * @return
	 */
	public Object post(Object obj) {
		Object str2 = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			//传入object
			this.s = new Socket(ip, port);
			// this.s.setKeepAlive(true);
			 this.s.setSoTimeout(sotimeout);
			os = s.getOutputStream();
			is = s.getInputStream();
			byte[] bytes = null;
			//System.out.println("写入服务器:" + obj.toString());
			if (obj instanceof String) {
				bytes = ((String) obj).getBytes();
			} else {
				bytes = SerializableMethod.serializeObjByte(obj);
			}
			os.write(bytes);
			byte[] buf = new byte[100000];
			int len = is.read(buf);
			is.close();
			os.close();
			//System.out.println(new String(buf, 0, len));
			try {
				byte[] input = new byte[len];
				System.arraycopy(buf, 0, input, 0, len);
				str2 = SerializableMethod.revSerializeObjByte(input);
			} catch (Exception e) {
				str2 = new String(buf, 0, len);
			}
			// str2 += new String(buf, 0, len);
			//System.out.println("接受服务端信息:" + str2.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str2;
	}

	public static void main(String[] args) {
		// System.out.println("执行服务端");
		// //服务端
		// try {
		// ServerSocket ss=new ServerSocket(6001);
		// while(true)
		// {
		// Socket s=ss.accept();
		//
		// new ServiceSocket(s).start();
		// }
		//
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//System.out.println("执行客户端");
		// 客户端
		try {
			Socket s = new Socket(InetAddress.getByName(null), 6001);// "localhost"
																		// "127.0.0.1s"
			OutputStream os = s.getOutputStream();
			InputStream is = s.getInputStream();
			byte[] buf = new byte[100];
			int len = is.read(buf);
			//System.out.println(new String(buf, 0, len));
			os.write("Hello,this is zhangsan 萨芬".getBytes());
			Thread.sleep(100);
			os.close();
			is.close();
			s.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}