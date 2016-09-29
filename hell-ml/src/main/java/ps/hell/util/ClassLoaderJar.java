package ps.hell.util;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderJar {

	public static MyClassLoader  classLoader=null;
	
	static class MyClassLoader extends URLClassLoader {

		public MyClassLoader(URL[] urls) {
			super(urls);
		}

		public MyClassLoader(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}

		public void addJar(URL url) {
			this.addURL(url);
		}

	}

	/**
	 * 加载 jar文件
	 * 
	 * @param filePath
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws MalformedURLException
	 */
	public static void reloadJar(String filePath) throws NoSuchMethodException,
			SecurityException, MalformedURLException {
		// 系统类库路径
		if (filePath == null || filePath.equals("")) {
			return;
		}
		File libPath = new File(filePath);
		if (!libPath.exists()) {
			System.out.println("不存在");
			return;
		}
		// 获取所有的.jar和.zip文件
		File[] jarFiles = null;
		if (libPath.isDirectory()) {
			jarFiles = libPath.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".jar") || name.endsWith(".zip");
				}
			});
		} else {
			jarFiles = new File[1];
			jarFiles[0] = libPath;
		}

		if (jarFiles != null) {
			// 从URLClassLoader类中获取类所在文件夹的方法
			// 对于jar文件，可以理解为一个存放class文件的文件夹
			URL[] urls = new URL[] {};
			classLoader = new MyClassLoader(urls, null);
			try {
				for (File file : jarFiles) {
					System.out.println(file.getAbsolutePath());
					classLoader.addJar(file.toURI().toURL());
					// Class<?> clazz =
					// classLoader.loadClass("com.flyingzl.Util");
					// Method method = clazz.getDeclaredMethod("getVersion");
					// method.invoke(null);
					// classLoader.close();
				}
				Class<?> clazz = classLoader.loadClass("com.mongodb.Mongo");
				// Class<?> clazz =
					//	 classLoader.loadClass("com.mongodb.Mongodb");
//						 Method method = clazz.getDeclaredMethod("getVersion");
//						 method.invoke(null);
			//			 classLoader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
	}
	
	public static Class forName(String classPath)
	{
		try {
			return classLoader.loadClass(classPath);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		try{
			//C:\Users\Administrator\Desktop
		ClassLoaderJar.reloadJar("H:/eclipse/workspaceML/stormKafka/lib/mongo-2.10.1.jar");
		
		Class main = ClassLoaderJar.forName("com.mongodb.Mongo");
		@SuppressWarnings("unchecked")
		Constructor constructor = main.getConstructor(String.class,int.class);
		 System.out.println();
		Object entity =  constructor.newInstance("192.168.1.11",27017);
		System.out.println(Object.class.getName());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
