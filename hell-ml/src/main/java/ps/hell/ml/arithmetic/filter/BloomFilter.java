package ps.hell.ml.arithmetic.filter;

import java.io.*;
import java.util.BitSet;

/**
 * 布隆过滤器 从一个文件中读取每一行，然后判断一句话是否在这个文件中出现过
 * 
 * @author peng.dong
 *
 */
public class BloomFilter {
	// DEFAULT_SIZE为2的22次方，即此处的左移28位
	private static final int DEFAULT_SIZE = 2 << 21;
	/*
	 * 不同哈希函数的种子，一般取质数 seeds数组共有8个值，则代表采用8种不同的哈希函数
	 */
	private int[] seeds = new int[] { 3, 5, 7, 11, 13, 31, 37, 61 };
	/*
	 * 初始化一个给定大小的位集 BitSet实际是由“二进制位”构成的一个Vector。 假如希望高效率地保存大量“开－关”信息，就应使用BitSet.
	 */
	private BitSet bitSets = new BitSet(DEFAULT_SIZE);
	// 构建hash函数对象
	private SimpleHash[] hashFuns = new SimpleHash[seeds.length];
	// 布隆过滤器配置文件存放路径
	private String path = "";

	public BloomFilter(String path) {
		/**
		 * 给出所有的hash值，共计seeds.length个hash值。共8位。
		 * 通过调用SimpleHash.hash(),可以得到根据8种hash函数计算得出hash值。
		 * 传入DEFAULT_SIZE(最终字符串的长度），seeds[i](一个指定的质数)即可得到需要的那个hash值的位置。
		 */
		for (int i = 0; i < seeds.length; i++) {
			hashFuns[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
		}
		// 配置文件路径地址
		this.path = path;
	}

	/**
	 * 
	 * 方法名：add 描述：将给定的字符串标记到bitSets中，即设置字符串的8个函数值的位置为1
	 * 
	 * @param value
	 */
	public synchronized void add(String value) {
		for (SimpleHash hashFun : hashFuns) {
			bitSets.set(hashFun.hash(value), true);
		}
	}

	/**
	 * 
	 * 方法名：isExit 描述：判断给定的字符串是否已经存在在bloofilter中，如果存在返回true，不存在返回false
	 * 
	 * @param value
	 * @return
	 */
	public synchronized boolean isExit(String value) {
		// 判断传入的值是否为null
		if (null == value) {
			return false;
		}

		for (SimpleHash hashFun : hashFuns) {
			if (!bitSets.get(hashFun.hash(value))) {
				// 如果判断8个hash函数值中有一个位置不存在即可判断为不存在Bloofilter中
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * 方法名：init 描述：读取配置文件
	 */
	public void init() {
		File file = new File(path);
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			read(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 方法名：read 描述：根据传入的流，初始化bloomfilter
	 * 
	 * @param in
	 */
	private void read(InputStream in) {
		if (null == in) { // 如果in为null，则返回
			return;
		}

		InputStreamReader reader = null;

		try {
			// 创建输入流
			reader = new InputStreamReader(in, "UTF-8");
			BufferedReader buffReader = new BufferedReader(reader, 512);
			String theWord = null;
			do {
				theWord = buffReader.readLine();
				// 如果theWord不为null和空，则加入Bloomfilter中
				if (theWord != null && !theWord.trim().equals("")) {
					add(theWord);
				}

			} while (theWord != null);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {
		BloomFilter bloomFilterTest = new BloomFilter("e:/test.txt");
		bloomFilterTest.init();

		System.out.println(bloomFilterTest.isExit("spark"));
		System.out.println(bloomFilterTest.isExit("storm"));
	}

	public static class SimpleHash {
		/*
		 * cap为DEFAULT_SIZE，即用于结果的最大字符串的值 seed为计算hash值的一个key值，具体对应上文中的seeds数组
		 */
		private int cap;
		private int seed;

		public SimpleHash(int cap, int seed) {
			this.cap = cap;
			this.seed = seed;
		}

		/**
		 * 
		 * 方法名：hash 描述：计算hash的函数，用户可以选择其他更好的hash函数
		 * 
		 * @param value
		 * @return
		 */
		public int hash(String value) {
			int result = 0;
			int length = value.length();
			for (int i = 0; i < length; i++) {
				result = seed * result + value.charAt(i);
			}

			return (cap - 1) & result;
		}
	}

}