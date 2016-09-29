package ps.hell.util;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * http://www.tuicool.com/articles/MnYFRjz
 * 
 * 除上述描述及清单 3 所示代码以外，NIO 的 Buffer 还提供了一个可以直接访问系统物理内存的类 DirectBuffer。DirectBuffer
 * 继承自 ByteBuffer，但和普通的 ByteBuffer 不同。普通的 ByteBuffer 仍然在 JVM
 * 堆上分配空间，其最大内存受到最大堆的限制，而 DirectBuffer 直接分配在物理内存上，并不占用堆空间。在对普通的 ByteBuffer
 * 访问时，系统总是会使用一个“内核缓冲区”进行间接的操作。而 DirectrBuffer 所处的位置，相当于这个“内核缓冲区”。因此，使用
 * DirectBuffer 是一种更加接近系统底层的方法，所以，它的速度比普通的 ByteBuffer 更快。DirectBuffer 相对于
 * ByteBuffer 而言，读写访问速度快很多，但是创建和销毁 DirectrBuffer 的花费却比 ByteBuffer 高
 * 
 * 
 * 
 * DirectBuffer 的信息不会打印在 GC 里面，因为 GC 只记录了堆空间的内存回收。可以看到，由于 ByteBuffer 在堆上分配空间，因此其
 * GC 数组相对非常频繁，在需要频繁创建 Buffer 的场合，由于创建和销毁 DirectBuffer 的代码比较高昂，不宜使用
 * DirectBuffer。但是如果能将 DirectBuffer 进行复用，可以大幅改善系统性能。清单 8 是一段对 DirectBuffer
 * 进行监控代码
 * 
 * @author Administrator
 *
 */
public class DirectBuffervsByteBuffer {
	public void DirectBufferPerform() {
		long start = System.currentTimeMillis();
		ByteBuffer bb = ByteBuffer.allocateDirect(500);// 分配 DirectBuffer
		for (int i = 0; i < 100000; i++) {
			for (int j = 0; j < 99; j++) {
				bb.putInt(j);
			}
			bb.flip();
			for (int j = 0; j < 99; j++) {
				bb.getInt(j);
			}
		}
		bb.clear();
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		start = System.currentTimeMillis();
		for (int i = 0; i < 20000; i++) {
			ByteBuffer b = ByteBuffer.allocateDirect(10000);// 创建 DirectBuffer
		}
		end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	public void ByteBufferPerform() {
		long start = System.currentTimeMillis();
		ByteBuffer bb = ByteBuffer.allocate(500);// 分配 DirectBuffer
		for (int i = 0; i < 100000; i++) {
			for (int j = 0; j < 99; j++) {
				bb.putInt(j);
			}
			bb.flip();
			for (int j = 0; j < 99; j++) {
				bb.getInt(j);
			}
		}
		bb.clear();
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		start = System.currentTimeMillis();
		for (int i = 0; i < 20000; i++) {
			ByteBuffer b = ByteBuffer.allocate(10000);// 创建 ByteBuffer
		}
		end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	/**
	 * 对 DirectBuffer 监控代
	 * @author Administrator
	 *
	 */
	public class monDirectBuffer {

		public void main(String[] args) {
			try {
				Class c = Class.forName("java.nio.Bits");// 通过反射取得私有数据
				Field maxMemory = c.getDeclaredField("maxMemory");
				maxMemory.setAccessible(true);
				Field reservedMemory = c.getDeclaredField("reservedMemory");
				reservedMemory.setAccessible(true);
				synchronized (c) {
					Long maxMemoryValue = (Long) maxMemory.get(null);
					Long reservedMemoryValue = (Long) reservedMemory.get(null);
					System.out.println("maxMemoryValue=" + maxMemoryValue);
					System.out.println("reservedMemoryValue="
							+ reservedMemoryValue);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {
		DirectBuffervsByteBuffer db = new DirectBuffervsByteBuffer();
		db.ByteBufferPerform();
		db.DirectBufferPerform();
	}
}