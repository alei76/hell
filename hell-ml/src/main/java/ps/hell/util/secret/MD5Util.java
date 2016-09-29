package ps.hell.util.secret;

import java.security.MessageDigest;

/**
 * 采用MD5加密解密
 * 
 * @author tfq
 * @datetime 2011-10-13
 */
public class MD5Util {

	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = (md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public static String convertMD5(String inStr) {

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };

		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 测试主函数
	public static void main(String args[]) {
		
		System.out.println(Math.pow(32,16));
		String s = new String("tangfu中国qianadsfsdfaswersdf:asdfsadfqwerqwsadfasdfqwerasdfasdfxvzxcvasdfsdfsg");
		String s2 = new String("tangfuqiang");
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + string2MD5(s));
		System.out.println("原始：" + s);
		System.out.println("加密的：" + convertMD5(s));

		System.out.println("解密的：" + convertMD5(convertMD5(s)));

		System.out.println("解密的：" + string2MD5(string2MD5(s)));
		System.out.println("解密的：" + convertMD5(convertMD5(string2MD5(s))));

		System.out.println(MD5Util.MD5( s));
		System.out.println(MD5Util.MD5( s2));
		System.out.println(MD5Util.MD5("加密"));
		int count=1000000;
		System.out.println("测试");
		long start=System.currentTimeMillis();
		for(int i=0;i<count;i++){
			string2MD5(s);
//			MD5Util.MD5( s);
		}
		long end=System.currentTimeMillis();
		System.out.println((end-start)+"ms");
	}
}