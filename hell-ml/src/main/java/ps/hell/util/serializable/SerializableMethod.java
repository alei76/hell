package ps.hell.util.serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.objenesis.strategy.StdInstantiatorStrategy;

import ps.hell.util.serializable.kryo.test.Simple;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * 实体的序列化及反序列化方法 对于实体必须集成Serializable方法
 * 
 * @author Administrator
 *
 */
public class SerializableMethod {

	public static int MAX_BUFFER_SIZE = 100;

	/**
	 * 序列化方法
	 * 
	 * @param filePath
	 *            输出文件
	 * @param obj
	 *            实体
	 * @throws IOException
	 */
	public static void serializeObj(String filePath, Object obj)
			throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(
				filePath));
		os.writeObject(obj);
		os.close();
	}

	public static byte[] serializeObjByte(Object obj)
			throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(outStream);
		oos.writeObject(obj);
		return outStream.toByteArray();
	}

	/**
	 * 反序列化方法
	 * 
	 * @param bytes
	 * @return 返回object 需要强制转换为 对应的实体
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Object revSerializeObjByte(byte[] bytes)
			throws ClassNotFoundException, IOException {
		ByteArrayInputStream inputSteam = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = new ObjectInputStream(inputSteam);
		Object object = oi.readObject();
		oi.close();
		return object;
	}
	/**
	 * kryo 反序列化
	 * 
	 * @param bytes
	 * @param objInput
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Object revSerializeObj2(byte[] bytes, Object objInput)
			throws ClassNotFoundException, IOException {
		Kryo kryo = new Kryo();
		//ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		//outStream.write(bytes);
		Registration registration = kryo.register(objInput.getClass());
		Object obj = null;
		Input input = null;
		try {
			input = new Input(bytes);
			obj = kryo.readObject(input, registration.getType());
		} catch(Exception e)
		{
			e.printStackTrace();
		}finally {
			if (input != null) {
				input.close();
			}
		}
		return obj;
	}

	/**
	 * kryo序列化
	 * 
	 * @param filePath
	 * @param obj
	 * @throws IOException
	 */
	public static void serializeObjKryo(String filePath, Object obj)
			throws IOException {
		Kryo kryo = new Kryo();
		Output output = null;
		kryo.setReferences(false);

		kryo.setRegistrationRequired(false);

		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

		kryo.register(obj.getClass());
		try {
			RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
			output = new Output(new FileOutputStream(raf.getFD()),
					MAX_BUFFER_SIZE);
			kryo.writeObject(output, obj);
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	public static void serializeObjKryo(OutputStream outputStream, Object obj) {
		Kryo kryo = new Kryo();

		Output output = null;
		kryo.setReferences(false);

		kryo.setRegistrationRequired(false);

		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

		kryo.register(obj.getClass());
		try {
			output = new Output(outputStream, MAX_BUFFER_SIZE);
			kryo.writeObject(output, obj);
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	/**
	 * 反序列化方法
	 * 
	 * @param filePath
	 * @return 返回object 需要强制转换为 对应的实体
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Object revSerializeObj(String filePath)
			throws ClassNotFoundException, IOException {
		ObjectInputStream oi = new ObjectInputStream(new FileInputStream(
				filePath));
		Object object = oi.readObject();
		oi.close();
		return object;
	}

	/**
	 * kryo 反序列化
	 * 
	 * @param filePath
	 * @param objInput
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Object revSerializeObjKryo(String filePath, Class<?> objInput)
			throws ClassNotFoundException, IOException {
		RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
		return revSerializeObjKryo(new FileInputStream(raf.getFD()), objInput);
	}

	/**
	 * kryo 反序列化
	 * 
	 * @param inputStream
	 * @param objInput
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Object revSerializeObjKryo(InputStream inputStream,
			Class<?> objInput) throws ClassNotFoundException, IOException {
		Kryo kryo = new Kryo();
		Registration registration = kryo.register(objInput);
		Object obj = null;
		Input input = null;
		kryo.setReferences(false);

		kryo.setRegistrationRequired(false);

		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		try {
			input = new Input(inputStream, MAX_BUFFER_SIZE);
			obj = kryo.readObject(input, registration.getType());
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return obj;
	}

	static FSTConfiguration conf = FSTConfiguration
			.createDefaultConfiguration();

	/**
	 * 序列化使用fst方法
	 * 
	 * @param stream
	 * @param classz
	 * @return
	 * @throws Exception
	 */
	public static Object revSerializeObjFst(InputStream stream, Class<?> classz)
			throws Exception {
		FSTObjectInput in = conf.getObjectInput(stream);
		Object result = in.readObject(classz);
		// DON'T: in.close(); here prevents reuse and will result in an
		// exception
		stream.close();
		return result;
	}

	/**
	 * 序列化使用fst方法
	 * 
	 * @param filePath
	 * @param classz
	 * @return
	 * @throws Exception
	 */
	public static Object revSerializeObjFst(String filePath, Class<?> classz)
			throws Exception {
		return revSerializeObjFst(new FileInputStream(new RandomAccessFile(filePath, "rw").getFD()),classz);
	}

	/**
	 * 反序列化使用fst
	 * 
	 * @param stream
	 * @param toWrite
	 * @throws IOException
	 */
	public static void serializeObjFst(OutputStream stream, Object toWrite)
			throws IOException {
		FSTObjectOutput out = conf.getObjectOutput(stream);
		out.writeObject(toWrite, toWrite.getClass());
		// DON'T out.close() when using factory method;
		out.flush();
		stream.close();
	}

	/**
	 * 反序列化使用fst
	 * 
	 * @param filePath
	 * @param toWrite
	 * @throws IOException
	 */
	public static void serializeObjFst(String filePath, Object toWrite)
			throws IOException {
		serializeObjFst(new FileOutputStream(new RandomAccessFile(filePath, "rw").getFD()), toWrite);
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		long start1 = System.currentTimeMillis();
		String modelFile = "f:\\data\\test";
		ArrayList<SonBean> list = new ArrayList<SonBean>();
		for (int i = 0; i < 500000; i++) {
			list.add(new SonBean(Integer.toBinaryString(i),
					Integer.toString(i), i));
		}
		Son son = new Son();
		son.list = list;
		son.name = "test";
		System.out.println((System.currentTimeMillis() - start1) + "ms");
		SerializableMethod.serializeObjKryo(modelFile, son);
		long end1 = System.currentTimeMillis();
		System.out.println("序列化时间:" + (end1 - start1) + "ms");
		long start = System.currentTimeMillis();
		Son list2 = (Son) SerializableMethod.revSerializeObjKryo(modelFile,
				Son.class);
		long end = System.currentTimeMillis();
		System.out.println("反序列化时间:" + (end - start) + "ms");
		System.out.println(list2.list.get(1000));
		System.out.println(list2.name);
		System.out
				.println(list2.list.get(4000).map.get(Integer.toString(4000)));

	}
}
