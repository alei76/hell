package ps.hell.util.codeInject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 对于一个自动注入方法使用的任意工具类
 * 
 * @author Administrator
 *
 */
public class AdapterUtil {
	/**
	 * 日志
	 */
	public static Logger logger = LoggerFactory.getLogger(AdapterUtil.class);
	/**
	 * 需要编译包含的目录
	 * @param method key对应为"com/util/codeInject/C"  value对应为方法名字
	 *            key对应的类，value对应的额方法，同名方法会出问题
	 * @param writeDir 
	 *            写入的目录
	 * @throws IOException
	 */
	public AdapterUtil(HashMap<String, ArrayList<String>> method,
			String writeDir) throws IOException {
		// 获取当前目录
		for (Entry<String, ArrayList<String>> path : method.entrySet()) {
			String name=path.getKey();
			ClassReader cr = new ClassReader(name);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			HashSet<String> methods = new HashSet<String>();
			for (String m : path.getValue()) {
				methods.add(m);
			}
			ClassAdapter classAdapter = new AddCounterClassAdapter(cw, methods);
			// 使给定的访问者访问Java类的ClassReader
			cr.accept(classAdapter, ClassReader.SKIP_DEBUG);
			byte[] data = cw.toByteArray();
			File file = new File(writeDir+"/"+path+".class");
			FileOutputStream fout = new FileOutputStream(file);
			fout.write(data);
			fout.close();
			logger.info("success:"+name);
		}
	}

	public static void main(String[] args) {
		try {
			ClassReader cr = new ClassReader("com/util/codeInject/C");
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			// ClassAdapter classAdapter = new AddTimeClassAdapter(cw);
			HashSet<String> method = new HashSet<String>();
			method.add("m2");
			ClassAdapter classAdapter = new AddCounterClassAdapter(cw, method);
			// 使给定的访问者访问Java类的ClassReader
			cr.accept(classAdapter, ClassReader.SKIP_DEBUG);
			byte[] data = cw.toByteArray();
			File file = new File(System.getProperty("user.dir")
					+ "/src/com/util/codeInject/trans/C.class");
			FileOutputStream fout = new FileOutputStream(file);
			fout.write(data);
			fout.close();
			System.out.println("success!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
