package ps.hell.util.serializable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * 序列化反序列化方法
 * @author Administrator
 *
 */
public class test {
	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		long start1=System.currentTimeMillis();
		String modelFile = "f:\\data\\test";
		ArrayList<SonBean> list = new ArrayList<SonBean>();
		for (int i = 0; i < 100000; i++) {
			SonBean sonbean=new SonBean(null,null,i);
			list.add(sonbean);
		}
		Son son = new Son();
		son.list = list;
		son.name = "test";
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(
				modelFile));
		os.writeObject(son);
		os.close();
		long end1=System.currentTimeMillis();
		System.out.println("序列化时间:"+(end1-start1)+"ms");
		long start=System.currentTimeMillis();
		Son list2 = null;
		ObjectInputStream oi = new ObjectInputStream(new FileInputStream(
				modelFile));
		list2 = (Son) oi.readObject();
		oi.close();
		long end=System.currentTimeMillis();
		System.out.println("反序列化时间:"+(end-start)+"ms");
		System.out.println(list2.list.get(1000));
		System.out.println(list2.name);

	}
}
