package ps.hell.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.HttpURLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JavaNetURLRESTFullClient {
	/**
	 * 日志
	 */
	public static Logger logger = LoggerFactory
			.getLogger(JavaNetURLRESTFullClient.class);

	/**
	 * 连接超时时间
	 */
	public static int connectionTimeOut=500;
	/**
	 * 读取超时时间
	 */
	public static int readTimeOut=3000;
	
	public static String code="utf-8";

	public static String DELETE="DELETE";
	public static String GET="GET";
	public static String POST="POST";
	public static String PUT="PUT";
	
	static {
//		InputStream inputStream = null;
//		String path = null;
//		try {
//			path = JavaNetURLRESTFullClient.class.getClassLoader()
//					.getResource("").toURI().getPath();
//		} catch (URISyntaxException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
//		try {
//			//需要的配置文件
//			inputStream = new FileInputStream(path + "/storage.properties");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Properties p = new Properties();
//
//		try {
//
//			p.load(inputStream);
//
//		} catch (IOException e1) {
//
//			e1.printStackTrace();
//
//		}

	}

	/**
	 * 发送rest并 获取状态
	 * 
	 * @param url
	 * @param method
	 * @param json
	 * @return
	 */
	public static boolean runAndGetStatus(String url, String method, String json) {

		String result = post2(url, method, json,code);

		return suggestStatus(result);
	}

	/**
	 * 放松rest并获取文本
	 * 
	 * @param url
	 * @param method
	 * @param json
	 * @return
	 */
	public static String runAndGetText(String url, String method, String json) {
		String result = post2(url, method, json,code);
		boolean flag = suggestStatus(result);
		if (flag) {
			return result;
		} else {
			return "{}";
		}
	}

	/**
	 * http 请求方法
	 * 
	 * @param url
	 * @param method
	 * @param json
	 * @return
	 */
	public static String post(String url, String method, String json) {
		logger.info("post to es:" + url + "  -X" + method + " -d " + json);
		StringBuffer sb = new StringBuffer();
		HttpURLConnection httpConnection = null;
		OutputStream outputStream = null;
		BufferedReader responseBuffer = null;
		try {
			URL targetUrl = new URL(url);
			httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setConnectTimeout(connectionTimeOut);
			httpConnection.setReadTimeout(readTimeOut);
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod(method);
			httpConnection.setRequestProperty("Content-Type",
					"application/json");
			if (json != null) {
			outputStream = httpConnection.getOutputStream();
			
				outputStream.write(json.getBytes());
			outputStream.flush();
			}

			if (httpConnection.getResponseCode() != 200
					&& httpConnection.getResponseCode() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ httpConnection.getResponseCode());
			}

			responseBuffer = new BufferedReader(new InputStreamReader(
					(httpConnection.getInputStream())));

			String output;
			// System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				sb.append(output);
				// System.out.println(output);
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (responseBuffer != null) {
				try {
					responseBuffer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (httpConnection != null) {
				try {
					httpConnection.getInputStream().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
		}
		return sb.toString();
	}
	

	public static String post2(String url, String method, String json,String code){
		try {
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(connectionTimeOut);
			client.getHttpConnectionManager().getParams()
					.setSoTimeout(readTimeOut);
			HttpMethodBase methodBase=null;
			RequestEntity entiry=null;
			if(json!=null){
				entiry=new StringRequestEntity(new String(json.getBytes(),code));
			}
			if(method.equals(JavaNetURLRESTFullClient.DELETE)){
				DeleteMethod delete=new DeleteMethod(url);
				methodBase=delete;
			}else if(method.equals(JavaNetURLRESTFullClient.GET)){
				if (entiry == null) {
					GetMethod get = new GetMethod(url);
					// logger.info("json:"+new String(json.getBytes(),code));
					// get.setQueryString(new String(json.getBytes(),code));
					methodBase = get;
				} else {
					return post(url, method, json);
				}
			}else if(method.equals(JavaNetURLRESTFullClient.PUT)){
				PutMethod put=new PutMethod(url);
				methodBase=put;
				put.setRequestEntity(entiry);
			}else if(method.equals(JavaNetURLRESTFullClient.POST)){
				PostMethod post=new PostMethod(url);
				methodBase=post;
				post.setRequestEntity(entiry);
			}else{
				return null;
			}
			methodBase.addRequestHeader("Content-Type",
					"application/json;charset=UTF-8");
	
//			postMethod
//					.addRequestHeader("User-Agent",
//							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//			postMethod.removeRequestHeader("Proxy-Connection");
//			postMethod.addRequestHeader("Connection", "Keep-Alive");
			// System.out.println(Arrays.toString(postMethod.getParameters()));
			// 使用POST方法
			// HttpMethod method = new PostMethod("http://java.sun.com");
			client.executeMethod(client.getHostConfiguration(), methodBase);
			InputStream is = methodBase.getResponseBodyAsStream();
			Header header = methodBase.getResponseHeader("Content-Encoding");
			InputStreamReader isr = null;
			GZIPInputStream gzin = null;
			boolean useGip = false;
			if (header == null) {
				isr = new InputStreamReader(is);
			} else {
				if (header.getValue().contains("gzip")) {
					useGip = true;
					gzin = new GZIPInputStream(is);
					isr = new InputStreamReader(gzin, code);
				}
			}
			BufferedReader br = new BufferedReader(isr);
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
			methodBase.releaseConnection();
			return sb.toString();
		} catch (ConnectTimeoutException e1) {
			e1.printStackTrace();
			return null;
		} catch (ConnectException e2) {
			e2.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断rest的文档状态
	 * 
	 * @param str
	 * @return
	 */
	public static boolean suggestStatus(String str) {
		if (str.equals("")) {
			return false;
		}
		if (str.startsWith("\\{\\{\"error\"")) {
			return false;
		} else {
			return true;
		}
	}

	public static void main(String[] args) {
		// System.out.println(new Date());
		// long startTime=System.currentTimeMillis();
		// int start=1;
		// int count=300;
		// for (int i = start; i < count; i++) {
		// String url=JavaNetURLRESTFulClient.targetURL+"mall/"+i;
		// PostBean bean=new PostBean(86007050,10000,0,10);
		// String code=JavaNetURLRESTFulClient.post(url,
		// JsonUtil.getJsonStr(bean));
		// //System.out.println(code);
		// }
		// start=10001;
		// count=30000;
		// for (int i = start; i < count; i++) {
		// String url=JavaNetURLRESTFulClient.targetURL+"brand/"+i;
		// PostBean bean=new PostBean(86007050,10000,0,10);
		// String code=JavaNetURLRESTFulClient.post(url,
		// JsonUtil.getJsonStr(bean));
		// //System.out.println(code);
		// }
		// long endTime=System.currentTimeMillis();
		// System.out.println(new Date());
		// System.out.println("总用时间:"+(endTime-startTime)*1d/1000+"秒");
		// System.out.println("平均一个时间:"+(endTime-startTime)*1d/(count-start)+"毫秒");
		// System.out.println("平均美秒处理:"+(count-start)*1d/(endTime-startTime)*1000+"个");
		
		
		String add="http:\\127.0.0.1:8080/lablesystem/addData";
		String update="http:\\127.0.0.1:8080/lablesystem/updateDate";
		QuazerMoniterStatus v1=new QuazerMoniterStatus();
		v1.setId(1962L);
		v1.setRootId(26);
		v1.setRefId(1961l);
		v1.setDeep(1);
		v1.setStatus(-1);
		JavaNetURLRESTFullClient.post(add,"POST", JsonUtil.getJsonStr(v1));
		
		v1=new QuazerMoniterStatus();
		v1.setId(1963L);
		v1.setRootId(26);
		v1.setDeep(0);
		v1.setStatus(-1);
		JavaNetURLRESTFullClient.post(add,"POST", JsonUtil.getJsonStr(v1));
		
		v1=new QuazerMoniterStatus();
		v1.setId(1964L);
		v1.setRootId(26);
		v1.setRefId(1963l);
		v1.setDeep(1);
		v1.setStatus(-1);
		JavaNetURLRESTFullClient.post(add,"POST", JsonUtil.getJsonStr(v1));
		
		v1=new QuazerMoniterStatus();
		v1.setId(1964L);
		v1.setRootId(26);
		v1.setRefId(1963l);
		v1.setDeep(1);
		v1.setStatus(1);
		JavaNetURLRESTFullClient.post(update,"POST", JsonUtil.getJsonStr(v1));
		
		v1=new QuazerMoniterStatus();
		v1.setId(1965L);
		v1.setRootId(26);
		v1.setDeep(0);
		v1.setStatus(1);
		JavaNetURLRESTFullClient.post(update,"POST", JsonUtil.getJsonStr(v1));

	}
}