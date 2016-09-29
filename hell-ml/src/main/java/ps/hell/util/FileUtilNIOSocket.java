package ps.hell.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
/**
 * nio的socket使用
 * @author Administrator
 *
 */
public class FileUtilNIOSocket {

	public class SimpleServer {
		public SimpleServer(int port) throws IOException {
			final AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel
					.open().bind(new InetSocketAddress(port));
			// 监听消息，收到后启动 Handle 处理模块
			listener.accept(null,
					new CompletionHandler<AsynchronousSocketChannel, Void>() {
						@Override
						public void completed(AsynchronousSocketChannel ch,
								Void att) {
							listener.accept(null, this);// 接受下一个连接
							handle(ch);// 处理当前连接
						}

						@Override
						public void failed(Throwable exc, Void attachment) {
							// TODO Auto-generated method stub

						}

					});
		}

		public void handle(AsynchronousSocketChannel ch) {
			ByteBuffer byteBuffer = ByteBuffer.allocate(32);// 开一个 Buffer
			try {
				ch.read(byteBuffer).get();// 读取输入
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byteBuffer.flip();
			System.out.println(byteBuffer.get());
			// Do something
		}

	}

	public class SimpleClientClass {
		private AsynchronousSocketChannel client;

		public SimpleClientClass(String host, int port) throws IOException,
				InterruptedException, ExecutionException {
			this.client = AsynchronousSocketChannel.open();
			Future<?> future = client
					.connect(new InetSocketAddress(host, port));
			future.get();
		}

		public void write(byte b) {
			ByteBuffer byteBuffer = ByteBuffer.allocate(32);
			System.out.println("byteBuffer=" + byteBuffer);
			byteBuffer.put(b);// 向 buffer 写入读取到的字符
			byteBuffer.flip();
			System.out.println("byteBuffer=" + byteBuffer);
			client.write(byteBuffer);
		}

	}

	public class AIODemoTest {

		@Test
		public void testServer() throws IOException, InterruptedException {
			SimpleServer server = new SimpleServer(9021);
			Thread.sleep(10000);// 由于是异步操作，所以睡眠一定时间，以免程序很快结束
		}

		@Test
		public void testClient() throws IOException, InterruptedException,
				ExecutionException {
			SimpleClientClass client = new SimpleClientClass("localhost", 9021);
			client.write((byte) 11);
		}

	}

	public static void main(String[] args) {
		FileUtilNIOSocket ft=new FileUtilNIOSocket();
		AIODemoTest demoTest = ft.new AIODemoTest();
		try {
			demoTest.testServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			demoTest.testClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
