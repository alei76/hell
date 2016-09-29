package ps.hell.util.notMemoryModle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ps.hell.util.FileUtil;
import ps.hell.util.serializable.SerializableMethod;

/**
 * memory的list方法
 * 
 * @author Administrator 先不支持remove的属性
 */
public class ArrayListMemory<E extends Comparable<? super E>>{

	/**
	 * 文件的总大小
	 */
	public long size = 0;
	/**
	 * 初始大小
	 */
	public int initSize = 0;
	/**
	 * 当前块的数据
	 */
	public Memory data = null;
	/**
	 * bluck文件存储地址
	 */
	public String dir = null;
	/**
	 * 已经存在的0开始
	 */
	public int existBluckIndex = -1;
	/**
	 * 是否写入执行
	 */
	public boolean isWrite = false;

	public static class Memory<E extends Comparable<? super E>> {
		/**
		 * 每个桶最大使用的数量
		 */
		public int initSize = 0;

		/**
		 * 文件的总大小
		 */
		public int size = 0;

		ArrayList<E> list = null;
		/**
		 * 桶的数量 从0开始
		 */
		public int bluckId = 0;

		/**
		 * 
		 * @param initSize
		 *            初始化大小
		 * @param bluckId
		 *            bluck id
		 */
		public Memory(int initSize, int bluckId) {
			this.list = new ArrayList(initSize);
			this.initSize = initSize;
			this.bluckId = bluckId;
		}

		/**
		 * 写磁盘
		 * 
		 * @return
		 */
		public boolean write(String dir) {
			String file = dir + "/" + bluckId + ".bin";
			// 写磁盘需要写入
			try {
				SerializableMethod.serializeObjKryo(file, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}

		/**
		 * 读取磁盘数据
		 * 
		 * @param dir
		 * @return
		 */
		public static Memory read(String dir, int buckId) {
			String file = dir + "/" + buckId + ".bin";
			try {
				// System.out.println("读取:" + file);
				Memory mo = (Memory) SerializableMethod.revSerializeObjKryo(
						file, Memory.class);
				return mo;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		public void add(E obj) {
			this.size++;
			this.list.add(obj);
		}

		public void addAll(Collection<? extends E> list) {
			this.size += list.size();
			this.list.addAll(list);
		}

		public E get(int index) {
			return this.list.get(index);
		}

		public int getInitSize() {
			return initSize;
		}

		public void setInitSize(int initSize) {
			this.initSize = initSize;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public ArrayList<E> getList() {
			return list;
		}

		public void setList(ArrayList<E> list) {
			this.list = list;
		}

		public int getBluckId() {
			return bluckId;
		}

		public void setBluckId(int bluckId) {
			this.bluckId = bluckId;
		}

	}

	/**
	 * 
	 * @param initSize
	 *            每一个桶中最大存储数量
	 * @param dir
	 *            全部桶的存储地址
	 */
	public ArrayListMemory(int initSize, String dir) {
		this.initSize = initSize;
		this.data = new Memory(initSize, ++existBluckIndex);
		this.dir = dir;
	}

	/**
	 * 新增数据
	 * 
	 * @param obj
	 */
	public void add(E obj) {
		if (data.getSize() == initSize) {
			// 如果满了则需要把数据写入磁盘并新建MEMORY
			data.write(dir);
			this.data = new Memory<E>(initSize, ++existBluckIndex);
		}
		data.add(obj);
		isWrite = false;
		this.size++;
	}

	/**
	 * 主要适用于迭代程序
	 * 
	 * @param index
	 * @throws Exception
	 */
	public E get(long index) {
		if (index > size - 1) {
			try {
				throw new Exception("越界:current_all:" + (size - 1)
						+ ":current_used:" + index);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!isWrite && data.bluckId != 0) {
			// 如果没发生过写入 并且只存在一个bluckID块 那么需要写入当前块
			this.data.write(dir);
			this.isWrite = true;
		}
		// 判断是不是在该桶中
		int use_bluck_id = getBluckId(index);
		if (data.bluckId != use_bluck_id) {
			changeBluck(use_bluck_id);
		}
		// 获取其他的桶
		return (E) this.data.get((int) (index % initSize));
	}

	/**
	 * 写入硬盘
	 */
	public void write() {
		// 需要写入备库
		// 需要写入基本信息
		FileUtil file = new FileUtil(dir + "/main.txt", "utf-8", true, "delete");
		file.write(size + "\t" + initSize + "\t" + existBluckIndex);
		file.close();
		if (!isWrite) {
			// 如果没发生过写入 并且只存在一个bluckID块 那么需要写入当前块
			this.data.write(dir);
			this.isWrite = true;
		}
	}

	/**
	 * 从硬盘读取
	 * 
	 * @throws Exception
	 */
	public void read() {
		// 读取基本信息
		FileUtil file = new FileUtil(dir + "/main.txt", "utf-8", false, null);
		ArrayList<String> ll = file.readAndClose();
		if (ll.size() == 1) {
			String[] strs = ll.get(0).split("\t");
			this.size = Long.parseLong(strs[0]);
			this.initSize = Integer.parseInt(strs[1]);
			this.existBluckIndex = Integer.parseInt(strs[2]);
		} else {
			try {
				throw new Exception("main.txt 格式异常");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.data = null;
		if (this.existBluckIndex >= 0) {
			changeBluck(0);
		}
	}

	/**
	 * 获取桶的id
	 * 
	 * @param index
	 * @return
	 */
	public int getBluckId(long index) {
		return (int) (index / initSize);
	}

	/**
	 * sort排序算法 默认是从小到大 需要花费2倍内存
	 * 
	 * @sortDir 存储排序的地址
	 * @dir2 临时小文件地址 排序后 存储到 指定目录 ，并且该索引会重新放回原始数据集上
	 */
	public void sort(String sortDir, String dir2) {
		// 删除临时文件目录
		File file = new File(dir2);
		if (!file.exists()) {
			file.mkdir();
		}
		file = new File(sortDir);
		if (!file.exists()) {
			file.mkdir();
		}

		// 首先对整个桶做排序
		// 记录每一个组位置的使用本次的起始位置
		ArrayList<Integer> indexUse = new ArrayList<Integer>();
		ArrayList<Integer> indexUseNew = new ArrayList<Integer>();
		// 还存在数据的index
		ArrayList<Integer> existBluckIndexList = new ArrayList<Integer>();
		for (int i = 0; i <= this.existBluckIndex; i++) {
			changeBluck(i);
			Collections.sort(this.data.list);
			this.data.write(dir);
			// for(int j=0;j<this.data.list.size();j++){
			// System.out.println("sort:"+this.data.list.get(j));
			// }
			indexUse.add(0);
			indexUseNew.add(0);
			existBluckIndexList.add(i);
		}
		int length = this.initSize / (existBluckIndex + 1);

		// 获取每一个块的最右侧最小数，合并其他类的
		// 然后以该最小数为下一个桶的最小值
		// 排序组中的bluckid
		int useBluckIndexOne = -1;

		while (true) {
//			System.out.println(useBluckIndexOne);
			ArrayList<ArrayList<E>> tmpList = new ArrayList<ArrayList<E>>(
					existBluckIndexList.size());
			E obj = null;
			int index = -1;
			// 下一轮需要提出的bluck
			ArrayList<Integer> removeIndex = new ArrayList<Integer>();
			// 获取最小值
			for (int i = 0; i < existBluckIndexList.size(); i++) {
				int ii = existBluckIndexList.get(i);
				changeBluck(ii);
				if (indexUse.get(ii) + length > data.size) {
					indexUseNew.set(ii, data.size - 1);
					removeIndex.add(i);
				} else {
					indexUseNew.set(ii, indexUse.get(ii) + length - 1);
				}
				ArrayList<E> tm = new ArrayList<E>();
				for (int j = indexUse.get(ii); j <= indexUseNew.get(ii); j++) {
					tm.add((E) this.data.get(j));
				}
				tmpList.add(tm);
				if (tmpList.size() < this.initSize) {
					E obj2 = (E) this.data.get(indexUseNew.get(ii));
					if (obj == null) {
						obj = obj2;
//						System.out.println(ii + "\t" + indexUseNew.get(ii)
//								+ "\t" + indexUse.get(ii) + "\t" + obj + "\t"
//								+ obj2 + "\t" + obj.compareTo(obj2));
					} else {
						// 获取最大值 的最小值
//						System.out.println(ii + "\t" + indexUseNew.get(ii)
//								+ "\t" + indexUse.get(ii) + "\t" + obj + "\t"
//								+ obj2 + "\t" + obj.compareTo(obj2));
						if (obj.compareTo(obj2) < 0) {
							obj = obj2;
							index = i;
						}
					}
				} else {
					E obj2 = (E) this.data.get(indexUseNew.get(ii));
					if (obj == null) {
						obj = obj2;
//						System.out.println(ii + "\t" + indexUseNew.get(ii)
//								+ "\t" + indexUse.get(ii) + "\t" + obj + "\t"
//								+ obj2 + "\t" + obj.compareTo(obj2));
					} else {
						// 获取最大值 的最小值
//						System.out.println(ii + "\t" + indexUseNew.get(ii)
//								+ "\t" + indexUse.get(ii) + "\t" + obj + "\t"
//								+ obj2 + "\t" + obj.compareTo(obj2));
						if (obj.compareTo(obj2) > 0) {
							obj = obj2;
							index = i;
						}
					}
				}
			}
//			System.out.println("obj:" + obj);
			// 根据最小值获取提取边界
			Memory<E> end = new Memory<E>(this.initSize, ++useBluckIndexOne);
			for (int i = 0; i < existBluckIndexList.size(); i++) {
				int ii = existBluckIndexList.get(i);
				if (i == index) {
					indexUse.set(i, indexUseNew.get(ii) + 1);
					end.addAll(tmpList.get(i));
//					System.out.println(i + " : " + indexUseNew.get(ii) + "\t"
//							+ indexUse.get(ii));
				} else {
					// 获取有效边界
					ArrayList<E> ll = tmpList.get(i);
					boolean flag = false;
					for (int j = 0; j < ll.size(); j++) {
						E tz = ll.get(j);
						// System.out.println(obj+"\t"+ tz
						// +"\t"+obj.compareTo(tz));
						if (obj.compareTo(tz) >= 0) {
							end.add(tz);
						} else {
							flag = true;
							indexUse.set(i, indexUse.get(ii) + j + 1);
							break;
						}
					}
					if (!flag) {
						indexUse.set(i, indexUseNew.get(ii) + 1);
					}
//					if (ll.size() == 0) {
//						System.out.println(i + " : " + indexUseNew.get(ii)
//								+ "\t" + indexUse.get(ii) + "\tmax:" + null
//								+ "\tmin:" + null);
//					} else {
//						System.out.println(i + " : " + indexUseNew.get(ii)
//								+ "\t" + indexUse.get(ii) + "\tmax:"
//								+ ll.get(ll.size() - 1) + "\tmin:" + ll.get(0));
//					}
				}
			}
			// 最终数据写入备库
			Collections.sort(end.list);
			end.write(dir2);
			end = null;
			// 提出下一轮不用的
			for (int i = 0; i < removeIndex.size(); i++) {
				// System.out.println("移除：" + removeIndex.get(i));
				existBluckIndexList.remove(removeIndex.get(i) - i);
			}
			if (existBluckIndexList.size() <= 0) {
				break;
			}
		}
		// 合并小文件集合到标准文件集合
		String dirBak = this.dir;
		this.dir = dir2;
		this.data = null;
		System.gc();
		int tempCount = 0;
		Memory<E> me = null;
		ArrayList<E> list = new ArrayList<E>(this.initSize);
		int ii = -1;
		for (int i = 0; i <= useBluckIndexOne; i++) {
			changeBluck(i);
			// 获取文件大小
			tempCount += this.data.size;
			if (tempCount >= this.initSize) {
				int index_v = this.initSize - (tempCount - this.data.size);
				// 划分存储
//				System.out.println((tempCount - this.data.size) + "\t"
//						+ this.initSize + "\t" + this.data.size + "\tindex_v:"
//						+ index_v);

				for (int j = 0; j < index_v; j++) {
					list.add((E) this.data.get(j));
				}
				me = new Memory<E>(this.initSize, ++ii);
				me.addAll(list);
				me.write(sortDir);
//				System.out.println(sortDir + " bluck:" + ii + "\tsize:"
//						+ me.list.size());
				me = null;
				list = new ArrayList<E>(this.initSize);
				for (int j = index_v; j < this.data.size; j++) {
					list.add((E) this.data.get(j));
				}
				tempCount -= this.initSize;
			} else {
				list.addAll(this.data.list);
			}
		}
		if (tempCount > 0) {
			me = new Memory<E>(this.initSize, ++ii);
			me.addAll(list);
			me.write(sortDir);
//			System.out.println(sortDir + " bluck:" + ii + "\tsize:"
//					+ me.list.size());
		}
		this.isWrite = true;
		this.dir = sortDir;
		write();
		this.dir = dirBak;
		// 删除临时文件目录
		 File file2=new File(dir2);
		 if(file2.isDirectory()){
		 file2.deleteOnExit();
		 }
	}

	/**
	 * 桶的切换
	 * 
	 * @param bluckId
	 */
	public void changeBluck(int bluckId) {
		// System.out.println("切换桶："+bluckId);
		if (this.data != null && this.data.bluckId == bluckId) {
			return;
		}
		// System.out.println("读取地址："+dir);
		this.data = Memory.read(dir, bluckId);
	}

	public static void main(String[] args) {
		int size = 100000;
		ArrayListMemory<Integer> list = new ArrayListMemory<Integer>(size,
				"H:\\test\\memory");
		long all_size = size * 200;
		long start_time = System.currentTimeMillis();
		Random random = new Random();
//		for (long l = 0l; l < all_size; l++) {
//			list.add(random.nextInt());
//		}
		long end_time = System.currentTimeMillis();
		System.out.println("写入时间:" + (end_time - start_time) + "ms");
		System.out.println("每万条写入:" + (end_time - start_time) * 1d / size
				+ "ms");
		// 获取数据
		start_time = System.currentTimeMillis();
//		for (long l = 0; l < list.size; l++) {
//			if (l % 3000 == 0) {
//				System.out.println(list.get(l));
//			}
//		}
		end_time = System.currentTimeMillis();
		System.out.println((end_time - start_time) + "ms");
		System.out.println("每万条读取入:" + (end_time - start_time) * 1d / size
				+ "ms");

		// 对该数据做排序
		//大规模数据集的排序
		start_time = System.currentTimeMillis();
		list.sort("H:\\test\\memory\\sort", "H:\\test\\memory\\sort_tmp");
		end_time = System.currentTimeMillis();
		System.out.println((end_time - start_time) + "ms");
		System.out.println("每万条读取入:" + (end_time - start_time) * 1d / size
				+ "ms");
		
		//读取数据
		list = new ArrayListMemory<Integer>(size, "H:\\test\\memory\\sort");
		list.read();
//		for (long l = 0; l < list.size; l++) {
//			 if (l % 3000 == 0)
////			 {
////			 System.out.println();
////			
////			 }
//			 System.out.println(l+"\t"+list.get(l) );
//		}
		list = null;
		
	}
}
