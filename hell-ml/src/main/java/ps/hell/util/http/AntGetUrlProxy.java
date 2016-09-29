package ps.hell.util.http;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntGetUrlProxy {
	private static Logger log = LoggerFactory.getLogger(AntGetUrlProxy.class);
	public static int limitTime = 10000;

	/**
	 * 使用具体的代理测试程序
	 * 
	 * @param url
	 * @param proxy
	 * @return
	 * @throws Exception
	 */
	public static String testDoGet(String url, IPProxy proxy) throws Exception {
		HttpClient client = new HttpClient();

		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(limitTime);
		client.getHttpConnectionManager().getParams().setSoTimeout(limitTime);
		client.getHostConfiguration().setProxy(proxy.getIp(), proxy.getPort());
		HttpMethod method = new GetMethod(new String(url.getBytes(), "utf-8"));
		method.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
		method.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
		method.removeRequestHeader("Proxy-Connection");
		method.addRequestHeader("Connection", "Keep-Alive");
		// 使用POST方法
		// HttpMethod method = new PostMethod("http://java.sun.com");
		client.executeMethod(client.getHostConfiguration(), method);
		InputStream is = method.getResponseBodyAsStream();
		Header header = method.getResponseHeader("Content-Encoding");
		InputStreamReader isr = null;
		GZIPInputStream gzin = null;
		boolean useGip = false;
		if (header == null) {
			isr = new InputStreamReader(is);
		} else {
			if (header.getValue().contains("gzip")) {
				useGip = true;
				gzin = new GZIPInputStream(is);
				isr = new InputStreamReader(gzin, "utf-8");
			}
		}
		java.io.BufferedReader br = new java.io.BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String tempbf;
		while ((tempbf = br.readLine()) != null) {
			sb.append(tempbf);
			sb.append("\r\n");
		}
		isr.close();
		if (useGip) {
			gzin.close();
		}
		// 释放连接
		method.releaseConnection();
		return sb.toString();
	}

	public static String doGet(String url, String encoding, boolean isProxy,
			HashMap<String, String> config) {
		IPProxy proxy = null;
		if(!url.startsWith("http://")&&!url.startsWith("https://")&&!url.startsWith("hdfs://"))
		{
			url="http://"+url;
		}
		while (true) {
			try {
				HttpClient client = new HttpClient();
				client.getHttpConnectionManager().getParams()
						.setConnectionTimeout(limitTime);
				client.getHttpConnectionManager().getParams()
						.setSoTimeout(limitTime);
				if (isProxy) {
					proxy = MainProxy.get(config);
					if (proxy != null) {
						// 设置代理服务器地址和端口
						client.getHostConfiguration().setProxy(proxy.getIp(),
								proxy.getPort());
					}
					// .setProxy(proxy.getIp(),proxy.getPort());
					// 设置用户和密码，代理控件，代理域
					// NTCredentials defaultcreds = new NTCredentials("liu.cf",
					// "Founder1234", "172.18.40.3", "hold");
					// 设置
					// client.getState().setProxyCredentials(AuthScope.ANY,
					// defaultcreds);
					// 使用GET方法，如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
				}
				HttpMethod method = new GetMethod(new String(url.getBytes(),
						encoding));
//				method.addRequestHeader("Content-Type",
//						"application/x-www-form-urlencoded");
//				method.addRequestHeader("User-Agent",
//						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//				method.removeRequestHeader("Proxy-Connection");
//				method.addRequestHeader("Connection", "Keep-Alive");
				method.addRequestHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				method.addRequestHeader("connection", "Keep-Alwive");
				method.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
				method.addRequestHeader("user-agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
				//connection.setRequestProperty("cookie","city=www; __utma=147393320.525473397.1434073773.1436322492.1438306404.9; __utmz=147393320.1434073773.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); global_cookie=p58ilyu6y0igpgzxypi2daidk38iasyhx8h; unique_cookie=U_6jsx7de60l9h1crx62l446tq11licqyhw4j*5; __utmb=147393320.15.10.1438306404; __utmc=147393320; __utmt_t0=1; __utmt_t1=1; __utmt_t2=1");
				method.addRequestHeader("Accept-Encoding", "gzip, deflate");
				// 使用POST方法
				// HttpMethod method = new PostMethod("http://java.sun.com");
				client.executeMethod(client.getHostConfiguration(), method);
				InputStream is = method.getResponseBodyAsStream();
				Header header = method.getResponseHeader("Content-Encoding");
				InputStreamReader isr = null;
				GZIPInputStream gzin = null;
				boolean useGip = false;
				if (header == null) {
					isr = new InputStreamReader(is);
				} else {
					if (header.getValue().contains("gzip")) {
						useGip = true;
						gzin = new GZIPInputStream(is);
						isr = new InputStreamReader(gzin, encoding);
					}
				}
				java.io.BufferedReader br = new java.io.BufferedReader(isr);
				StringBuffer sb = new StringBuffer();
				String tempbf;
				while ((tempbf = br.readLine()) != null) {
					sb.append(tempbf);
					sb.append("\r\n");
				}
				isr.close();
				if (useGip) {
					gzin.close();
				}
				// 返回proxy
				if (isProxy) {
					MainProxy.addProxy(proxy,config);
				}
				// 释放连接
				method.releaseConnection();
				return sb.toString();
			} catch (ConnectTimeoutException e1) {
				e1.printStackTrace();
			} catch (ConnectException e2) {
				e2.printStackTrace();
			} catch (Exception e) {
				// 返回proxy
				if (proxy!=null) {
					MainProxy.addProxy(proxy,config);
				}else{
				e.printStackTrace();
				return null;
				}
			}
			try {
				Thread.sleep(limitTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 得到指定URL的文本响应数据
	 * */
	public static String doGet(String url, String encoding, boolean isProxy) {
		IPProxy proxy = null;
		while (true) {
			try {
				HttpClient client = new HttpClient();
				client.getHttpConnectionManager().getParams()
						.setConnectionTimeout(limitTime);
				client.getHttpConnectionManager().getParams()
						.setSoTimeout(limitTime);
				if (isProxy) {
					proxy = MainProxy.get();
					if (proxy != null) {
						// 设置代理服务器地址和端口
						client.getHostConfiguration().setProxy(proxy.getIp(),
								proxy.getPort());
					}
					// .setProxy(proxy.getIp(),proxy.getPort());
					// 设置用户和密码，代理控件，代理域
					// NTCredentials defaultcreds = new NTCredentials("liu.cf",
					// "Founder1234", "172.18.40.3", "hold");
					// 设置
					// client.getState().setProxyCredentials(AuthScope.ANY,
					// defaultcreds);
					// 使用GET方法，如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
				}
				HttpMethod method = new GetMethod(new String(url.getBytes(),
						encoding));
				method.addRequestHeader("Content-Type",
						"application/x-www-form-urlencoded");
				method.addRequestHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
				method.removeRequestHeader("Proxy-Connection");
				method.addRequestHeader("Connection", "Keep-Alive");
				// 使用POST方法
				// HttpMethod method = new PostMethod("http://java.sun.com");
				client.executeMethod(client.getHostConfiguration(), method);
				InputStream is = method.getResponseBodyAsStream();
				Header header = method.getResponseHeader("Content-Encoding");
				InputStreamReader isr = null;
				GZIPInputStream gzin = null;
				boolean useGip = false;
				if (header == null) {
					isr = new InputStreamReader(is);
				} else {
					if (header.getValue().contains("gzip")) {
						useGip = true;
						gzin = new GZIPInputStream(is);
						isr = new InputStreamReader(gzin, encoding);
					}
				}
				java.io.BufferedReader br = new java.io.BufferedReader(isr);
				StringBuffer sb = new StringBuffer();
				String tempbf;
				while ((tempbf = br.readLine()) != null) {
					sb.append(tempbf);
					sb.append("\r\n");
				}
				isr.close();
				if (useGip) {
					gzin.close();
				}
				// 返回proxy
				if (isProxy) {
					MainProxy.addProxy(proxy,null);
				}
				// 释放连接
				method.releaseConnection();
				return sb.toString();
			} catch (ConnectTimeoutException e1) {
				// e1.printStackTrace();
			} catch (ConnectException e2) {
				// e2.printStackTrace();
			} catch (Exception e) {
				// 返回proxy如果使用代理
				if (proxy!=null) {
					MainProxy.addProxy(proxy,null);
				}else{
					//如果使用本地
				e.printStackTrace();
				return null;
				}
			}
			try {
				Thread.sleep(limitTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		String url = "http://kaidedasha.fang.com/";
		// url = "http://www.baidu.com";
		System.out.println(AntGetUrlProxy.doGet(url, "gbk", true));
		// System.out.println(getUrl(1));
	}

	/**
	 * 获取page信息
	 * 
	 * @param pageNum
	 *            page编号
	 * @return
	 */
	public static String getUrl(int pageNum) {
		Map<String, String> m = new HashMap();
		String url = "http://www.bjjtw.gov.cn/jtw_service/page/service/parking.jsp";
		String code = "UTF-8";
		// area=东城区&ptype=&pname=&pagenum=1&pagesize=12
		m.put("area", "西城");
		m.put("ptype", "公建配建停车场（位）");
		m.put("pname", "停车场");
		m.put("pagenum", "1");
		m.put("pagesize", "12");
		m.put("page", Integer.toString(pageNum));
		String rus = doPost(url, m, code, false);

		// System.out.println(rus);
		return rus;
	}

	public static String getUrl2(int pageNum) {
		Map m = new HashMap();
		String url = "http://www.bjjtw.gov.cn/jtw_service/page/service/parking.jsp";
		String code = "UTF-8";
		m.put("area", "");
		m.put("ptype", "");
		m.put("pname", "停车场");
		m.put("pagenum", 1);
		m.put("pagesize", 12);
		m.put("page", pageNum);
		String rus = doPost(url, m, code, false);
		// System.out.println("result:"+rus);
		return rus;
	}

	public static String doPost(String reqUrl, Map<String, String> parameters,
			String recvEncoding, boolean isProxy) {
		IPProxy proxy = null;
		try {
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(limitTime);
			client.getHttpConnectionManager().getParams()
					.setSoTimeout(limitTime);
			if (isProxy) {
				proxy = MainProxy.get();
				if (proxy != null) {
					// 设置代理服务器地址和端口
					client.getHostConfiguration().setProxy(proxy.getIp(),
							proxy.getPort());
				}
				// 设置代理服务器地址和端口
				// .setProxy(proxy.getIp(),proxy.getPort());
				// 设置用户和密码，代理控件，代理域
				// NTCredentials defaultcreds = new NTCredentials("liu.cf",
				// "Founder1234", "172.18.40.3", "hold");
				// 设置
				// client.getState().setProxyCredentials(AuthScope.ANY,
				// defaultcreds);
				// 使用GET方法，如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
			}
			PostMethod postMethod = new PostMethod(new String(
					reqUrl.getBytes(), recvEncoding));
			NameValuePair[] param = new NameValuePair[parameters.size()];
			int i = -1;
			for (Entry<String, String> pa : parameters.entrySet()) {
				i++;
				param[i] = new NameValuePair(URLEncoder.encode(pa.getKey(),
						recvEncoding), URLEncoder.encode(pa.getValue(),
						recvEncoding));
			}
			postMethod.setRequestBody(param);
			postMethod.addRequestHeader("Content-Type",
					"application/x-www-form-urlencoded");
			postMethod
					.addRequestHeader("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
			postMethod.removeRequestHeader("Proxy-Connection");
			postMethod.addRequestHeader("Connection", "Keep-Alive");
			// System.out.println(Arrays.toString(postMethod.getParameters()));
			// 使用POST方法
			// HttpMethod method = new PostMethod("http://java.sun.com");
			client.executeMethod(client.getHostConfiguration(), postMethod);
			InputStream is = postMethod.getResponseBodyAsStream();
			Header header = postMethod.getResponseHeader("Content-Encoding");
			InputStreamReader isr = null;
			GZIPInputStream gzin = null;
			boolean useGip = false;
			if (header == null) {
				isr = new InputStreamReader(is);
			} else {
				if (header.getValue().contains("gzip")) {
					useGip = true;
					gzin = new GZIPInputStream(is);
					isr = new InputStreamReader(gzin, recvEncoding);
				}
			}
			java.io.BufferedReader br = new java.io.BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String tempbf;
			while ((tempbf = br.readLine()) != null) {
				sb.append(tempbf);
				sb.append("\r\n");
			}
			isr.close();
			if (useGip) {
				gzin.close();
			}
			// 释放连接
			postMethod.releaseConnection();
			if (isProxy) {
				MainProxy.addProxy(proxy,null);
			}
			return sb.toString();
		} catch (ConnectTimeoutException e1) {
		} catch (ConnectException e2) {
		} catch (Exception e) {
			if (isProxy) {
				MainProxy.addProxy(proxy,null);
			}
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public static String doPost(String reqUrl, Map<String, String> parameters,
			String recvEncoding, boolean isProxy,HashMap<String,String> config) {
		IPProxy proxy = null;
		try {
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(limitTime);
			client.getHttpConnectionManager().getParams()
					.setSoTimeout(limitTime);
			if (isProxy) {
				proxy = MainProxy.get(config);
				if (proxy != null) {
					// 设置代理服务器地址和端口
					client.getHostConfiguration().setProxy(proxy.getIp(),
							proxy.getPort());
				}
				// 设置代理服务器地址和端口
				// .setProxy(proxy.getIp(),proxy.getPort());
				// 设置用户和密码，代理控件，代理域
				// NTCredentials defaultcreds = new NTCredentials("liu.cf",
				// "Founder1234", "172.18.40.3", "hold");
				// 设置
				// client.getState().setProxyCredentials(AuthScope.ANY,
				// defaultcreds);
				// 使用GET方法，如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
			}
			PostMethod postMethod = new PostMethod(new String(
					reqUrl.getBytes(), recvEncoding));
			NameValuePair[] param = new NameValuePair[parameters.size()];
			int i = -1;
			for (Entry<String, String> pa : parameters.entrySet()) {
				i++;
				param[i] = new NameValuePair(URLEncoder.encode(pa.getKey(),
						recvEncoding), URLEncoder.encode(pa.getValue(),
						recvEncoding));
			}
			postMethod.setRequestBody(param);
			postMethod.addRequestHeader("Content-Type",
					"application/x-www-form-urlencoded");
			postMethod
					.addRequestHeader("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
			postMethod.removeRequestHeader("Proxy-Connection");
			postMethod.addRequestHeader("Connection", "Keep-Alive");
			// System.out.println(Arrays.toString(postMethod.getParameters()));
			// 使用POST方法
			// HttpMethod method = new PostMethod("http://java.sun.com");
			client.executeMethod(client.getHostConfiguration(), postMethod);
			InputStream is = postMethod.getResponseBodyAsStream();
			Header header = postMethod.getResponseHeader("Content-Encoding");
			InputStreamReader isr = null;
			GZIPInputStream gzin = null;
			boolean useGip = false;
			if (header == null) {
				isr = new InputStreamReader(is);
			} else {
				if (header.getValue().contains("gzip")) {
					useGip = true;
					gzin = new GZIPInputStream(is);
					isr = new InputStreamReader(gzin, recvEncoding);
				}
			}
			java.io.BufferedReader br = new java.io.BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String tempbf;
			while ((tempbf = br.readLine()) != null) {
				sb.append(tempbf);
				sb.append("\r\n");
			}
			isr.close();
			if (useGip) {
				gzin.close();
			}
			// 释放连接
			postMethod.releaseConnection();
			if (isProxy) {
				MainProxy.addProxy(proxy,null);
			}
			return sb.toString();
		} catch (ConnectTimeoutException e1) {
		} catch (ConnectException e2) {
		} catch (Exception e) {
			if (isProxy) {
				MainProxy.addProxy(proxy,null);
			}
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
