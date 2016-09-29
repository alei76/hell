package ps.hell.util.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import ps.hell.util.JsonUtil;
import ps.hell.util.serializable.kryo.test.Simple;

public class FstTest {

	static FSTConfiguration conf = FSTConfiguration
			.createDefaultConfiguration();

	public static Simple myreadMethod(InputStream stream) throws Exception {
		FSTObjectInput in = conf.getObjectInput(stream);
		Simple result = (Simple) in.readObject(Simple.class);
		// DON'T: in.close(); here prevents reuse and will result in an
		// exception
		stream.close();
		return result;
	}

	public static void mywriteMethod(OutputStream stream, Simple toWrite)
			throws IOException {
		FSTObjectOutput out = conf.getObjectOutput(stream);
		out.writeObject(toWrite, Simple.class);
		// DON'T out.close() when using factory method;
		out.flush();
		stream.close();
	}

	public static void main(String[] args) throws Exception {
		FileOutputStream output = new FileOutputStream("f://file.bin");
		for (int i = 0; i < 1; i++) {
			Map<String, Integer> map = new HashMap<String, Integer>(2);
			map.put("zhang0", i);
			map.put("zhang1", i);
			FstTest.mywriteMethod(output, new Simple("zhang" + i, (i + 1), map));
		}
		FileInputStream input = new FileInputStream("f://file.bin");

		Simple samp = FstTest.myreadMethod(input);
		System.out.println(JsonUtil.getJsonStr(samp));
	}
}
