package ps.hell.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * 
 * NIO 提供了处理结构化数据的方法，称之为散射 (Scattering) 和聚集 (Gathering)。散射是指将数据读入一组 Buffer
 * 中，而不仅仅是一个。聚集与之相反，指将数据写入一组 Buffer 中。散射和聚集的基本使用方法和对单个 Buffer
 * 操作时的使用方法相当类似。在散射读取中
 * ，通道依次填充每个缓冲区。填满一个缓冲区后，它就开始填充下一个，在某种意义上，缓冲区数组就像一个大缓冲区。在已知文件具体结构的情况下
 * ，可以构造若干个符合文件结构的 Buffer，使得各个 Buffer
 * 的大小恰好符合文件各段结构的大小。此时，通过散射读的方式可以一次将内容装配到各个对应的 Buffer
 * 中，从而简化操作。如果需要创建指定格式的文件，只要先构造好大小合适的 Buffer 对象，使用聚集写的方式，便可以很快地创建出文件
 * 
 * @author Administrator
 *
 */
public class FileUtilNIO extends FileUtil{
	
	public FileUtilNIO(String filename,String code,boolean isWrite,String fileStatus){
		super(filename, code, isWrite, fileStatus);
	}
	
	public void write(String data) {
		try {
			ByteBuffer bookBuf = ByteBuffer.wrap(data
					.getBytes("utf-8"));
			ByteBuffer valueBuf = ByteBuffer.wrap(new byte[]{((byte) 34)});
//			int booklen = bookBuf.limit();
			ByteBuffer[] bufs = new ByteBuffer[] { bookBuf };
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				FileOutputStream fos = new FileOutputStream(file);
				FileChannel fc = fos.getChannel();
				fc.write(bufs);
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public String read(){
//		ByteBuffer b1 = ByteBuffer.allocate(
//				(int) file.length());
		ByteBuffer b1 = ByteBuffer.allocate(
				(int) file.length());
		ByteBuffer[] bufs1 = new ByteBuffer[] { b1};
		try {
			FileInputStream fis = new FileInputStream(file);
			FileChannel fc = fis.getChannel();
			fc.read(bufs1);
			String bookname = new String(bufs1[0].array(), getCode());
			return bookname;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		FileUtilNIO nio = new FileUtilNIO("F:\\1.TXT","utf-8",true,"delete");
		nio.write("塑料袋附近阿斯兰的风景拉萨的街坊邻居");
		System.out.println(nio.read());
	}
}
