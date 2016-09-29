package ps.hell.util.tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceSocket extends Thread {

	public static Logger log = LoggerFactory.getLogger(ServiceSocket.class);

	public Socket sock = null;

	public Object obj = null;

	public ServiceSocket(Socket sock) {
		this.sock = sock;

	}

	/**
	 * 对应的实体类
	 * 
	 * @param sock
	 * @param obj
	 */
	public ServiceSocket(Socket sock, Object obj) {
		this.sock = sock;
		this.obj = obj;
	}

	@Override
	public void run() {
		// System.out.println("服务端获取新内容");
		OutputStream os = null;
		InputStream is = null;
		try {
			os = sock.getOutputStream();
			is = sock.getInputStream();
			if (obj == null) {
				os.write("Hello Welcome you".getBytes());
				byte[] buf = new byte[100000];
				int len = is.read(buf);

			} else {
				byte[] buf = new byte[100000];
				int len = is.read(buf);
				String str = "";
				if (len < 0) {
					os.write("false".getBytes());
				} else {
					str = new String(buf, 0, len);
					os.write(str.getBytes());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
				sock.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// try {
		// // System.out.println("启动");
		// ServerSocket ss = new ServerSocket(6001);
		// while (true) {
		// Socket s = ss.accept();
		// new ServiceSocket(s).start();
		// System.out.println(1);
		// }
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// System.out.println("end");
		String temp = "192.168.1.17:10008&搜房信息入库";
		System.out.println(Arrays.toString(temp.split("\\&")));
	}

}
