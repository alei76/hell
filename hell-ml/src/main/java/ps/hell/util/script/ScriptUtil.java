package ps.hell.util.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * name.sdf.df 为对应传入的 obj的 字内容 都以 get方法为实现对象
 * 从脚本文件中获取 ${name.sdf.df default=1} 拼接后的实际脚本
 * @author Administrator
 *
 */
public class ScriptUtil {

	/**
	 * 日志
	 */
	public static Logger logger = LoggerFactory.getLogger(ScriptUtil.class);

	public static HashMap<String, ScriptCommon> scriptMap = new HashMap<String, ScriptCommon>();


	/**
	 * 期望获取的返回数据
	 */
	public static String fieldsString = null;

	public static void init(String dir) {
		init2(dir);
		
		for (Entry<String, ScriptCommon> map : scriptMap.entrySet()) {
			System.out.println("可用脚本名:" + map.getKey());
		}
	}
	/**
	 * 加载 基础script 脚本
	 */
	public static void init2(String dir) {
		String path = dir;
		try {
			
			System.out.println("读取脚本目录:" + path);
			File[] files = new File(path).listFiles();
			for (File file : files) {
				logger.info("获取脚本:" + file.getName());
				String script = readAllAndClose(file, "", "#");
				if (fieldsString != null) {
					script = script.substring(0, script.length() - 1);
					script += fieldsString + "}";
				}
				scriptMap.put(file.getName(),new ScriptCommon(file.getName(),script));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	/**
	 * 获取所有数据 回车分隔符替换为 split
	 * 
	 * @param deleteString
	 *            为 开头为这个的则删除该行
	 * @return
	 */
	public static String readAllAndClose(File file, String split,
			String deleteString) {
		StringBuffer result = new StringBuffer();
		BufferedReader reader = null;
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), "utf-8");
			reader = new BufferedReader(read);
			String tempString = null;
			// System.out.println("开始读取文件");
			// 一次读入一行，直到读入null为文件结束
			// 是否为注释
			while ((tempString = reader.readLine()) != null) {
				if (tempString.length() == 0) {
					continue;
				}
				tempString = tempString.trim();
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

	/**
	 * 获取脚本内容 不输入字段
	 * 
	 * @param scriptName
	 */
	public static String getScript(String scriptName) {
		// Thread.currentThread().getStackTrace()[1].getMethodName();
		ScriptCommon comm = ScriptUtil.scriptMap.get(scriptName);
		if (comm == null) {
			logger.error("不存在名为:" + scriptName + ".xxx  的脚本");
			return null;
		}
		return comm.get();
	}

	/**
	 * 获取脚本内容 并修改一个字段
	 * @param scriptName
	 * @param obj
	 * @return
	 */
	public static String getScript(String scriptName, int obj) {
		// Thread.currentThread().getStackTrace()[1].getMethodName();
		ScriptCommon comm = ScriptUtil.scriptMap.get(scriptName);
		if(comm==null){
			logger.error("不存在名为:" + scriptName + ".xxx  的脚本");
			return null;
		}
		return comm.get(obj);
	}
	/**
	 * 获取脚本内容 输入一个实体
	 * @param scriptPathName 使用的脚本名字
	 * @param obj 获取的实例化对象 
	 */
	public static String getScript(String scriptPathName,Object obj){
		ScriptCommon comm = ScriptUtil.scriptMap.get(scriptPathName);
		return comm.get(obj);
	}
	
	
	
	public static class BeanTest{
		public String name="asdf";
		public BeanTest2 bean=new BeanTest2();
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public BeanTest2 getBean() {
			return bean;
		}
		public void setBean(BeanTest2 bean) {
			this.bean = bean;
		}
		
		
	}
	public static class BeanTest2{
		public String name="asdfwerw";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}


	public static void main(String[] args) {
		String directory=null;
		try {
			directory = ScriptUtil.class.getClassLoader().getResource("").toURI()
					.getPath()
					+ "script";
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//初始化需要加载的脚本目录
		ScriptUtil.init(directory);
		BeanTest test=new BeanTest();
		System.out.println(ScriptUtil.getScript("scriptTest", test));
	}

}
