package ps.hell.util.notMemoryModle;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import ps.hell.util.serializable.SerializableMethod;

/**
 * memory的HashMap方法
 * 对于hash数据倾斜很严重的非常有效
 * @author Administrator 先不支持remove的属性
 * 
 * 倾斜化 map 一般不适用 在 linked方面做了局部硬盘处理,map table不会做扩容 
 * 默认为标准化map 对于扩容的点会分割到不同文件中
 * 
 * 该方法适合于批量化的put和get方法优势明显
 */
public class HashMapMemory<K extends Comparable<? super K>, V> {

	Entry<K, V>[] table = null;
	long size = 0;
	/**
	 * hash table 的大小
	 */
	int maxLength = 65535;
	
	long allLength=maxLength;
	/**
	 * 如果是标准方法会当前所使用的块
	 * 从0开始
	 */
	int useBluckKeyId=0;
	/**
	 * 扩容的量  n^2的数量
	 */
	int bluckKeySize=1;
	
	/**
	 * 内存及的最大链表大小
	 */
	int maxSonSize = 10;
	/**
	 * 临时数据存储地址
	 */
	String dir = null;
	
	public boolean useIncline=false;
	
	public HashMapMemory(String dir, int maxLength, int maxSonSize,boolean useIncline) {
		File file=new File(dir);
		if(!file.exists()||!file.isDirectory()){
			file.mkdir();
		}
		this.dir = dir;
		this.table = new Entry[maxLength];
		this.maxLength = maxLength;
		this.maxSonSize = maxSonSize;
		this.useIncline=useIncline;
		allLength=maxLength;
	}
	
/**
 * 
 * @param dir
 * @param maxLength
 * @param maxSonSize
 */
	public HashMapMemory(String dir, int maxLength, int maxSonSize) {
		File file=new File(dir);
		if(!file.exists()||!file.isDirectory()){
			file.mkdir();
		}
		this.dir = dir;
		this.table = new Entry[maxLength];
		this.maxLength = maxLength;
		this.maxSonSize = maxSonSize;
		this.allLength=maxLength;
	}

	public static class Entry<K, V> {

		K k = null;

		V v = null;

		Entry<K, V> next = null;
		/**
		 * 文件属性状态
		 */
		boolean isFile = false;
		
		long size=0L;

		public Entry(K k, V v, Entry<K, V> next) {
			size++;
			this.k = k;
			this.v = v;
			if (this.next == null) {
				this.next = next;
			} else {
				Entry<K, V> nex = this.next;
				while (true) {
					nex = nex.next;
					if (nex == null) {
						nex = next;
						break;
					}
				}
			}
		}

		public int hash() {
			return k.hashCode();
		}
	}

	/**
	 * A randomizing value associated with this instance that is applied to hash
	 * code of keys to make hash collisions harder to find. If 0 then
	 * alternative hashing is disabled.
	 */
	transient int hashSeed = 0;

	/**
	 * Returns the entry associated with the specified key in the HashMap.
	 * Returns null if the HashMap contains no mapping for the key.
	 */
	public final V getEntry(K key) {
		if (size == 0) {
			return null;
		}
		int hash = hash(key);
		int i = indexFor(hash);
		Entry<K, V> e0 = table[i];
		if (e0 != null) {
			if (e0.isFile) {
				// 读硬盘
				readDisk(e0, dir, i);
				e0.isFile=false;
			}
			Entry<K, V> e = e0;
			while (true) {
				K k = e.k;
				if (e.hash() == hash && (k.compareTo(key) == 0)) {
					V value = e.v;
					return value;
				}
				if (e.next == null) {
					break;
				}
				e = e.next;
			}
		}
		return null;
	}

	public int hash(K k) {
		int h = hashSeed;
		if (0 != h && k instanceof String) {
			return sun.misc.Hashing.stringHash32((String) k);
		}
		h ^= k.hashCode();
		// This function ensures that hashCodes that differ only by
		// constant multiples at each bit position have a bounded
		// number of collisions (approximately 8 at default load factor).
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
//		return k.hashCode();
	}

	public int indexFor(int h) {
		// assert Integer.bitCount(length) == 1 :
		// "length must be a non-zero power of 2";
		return (int) (h & (allLength - 1));
	}

	/**
	 * K不支持 null值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public final V put(K key, V value) {
		if (key == null)
			return null;
		int hash = hash(key);
		int i = indexFor(hash);
		if(this.useIncline){
			return incLinePut(key,value,i,hash);
		}else{
			return standerPut(key,value,i,hash);
		}		
	}
	/**
	 * 标准方法
	 * @param key
	 * @param value
	 * @param bluckId
	 * @param hash
	 * @return
	 */
	public V standerPut(K key,V value,int bluckId,int hash){
		//hashmap的扩容方法直接获取2倍空间，不考虑tour值
		//首先直接put进去 然后调整数据空间
		//先调整bluck块
		int bluckKey=bluckId/maxLength;
//		System.out.println(bluckId+":bluckKey:"+bluckKey+"\t"+maxLength);
		changeBluck(bluckKey);
		int useIndex=bluckId%maxLength;
		Entry<K, V> e0 = table[useIndex];
		if (e0 == null) {
			table[useIndex] = new Entry(key, value, null);
			size++;
			//调整空间
			changeBluckFile();
			return null;
		}
		Entry<K, V> e = e0;
		while (true) {
			K k = e.k;
			if (e.hash() == hash && (k.compareTo(key) == 0)) {
				V oldValue = e.v;
				e.v = value;
				return oldValue;
			}
			if (e.next == null) {
				e.next = new Entry(key, value, null);
				size++;
				// 如果数量过大 则写入硬盘
				changeBluckFile();
				return null;
			}
			e = e.next;
		}
	}
	
	/**
	 * 标准方法 不考虑其他情况全部数据塞入table 中
	 * @param key
	 * @param value
	 * @param bluckId
	 * @param hash
	 * @return
	 */
	public V standerPut(Entry<K,V>[] table,K key,V value,int bluckId,int hash){
		//hashmap的扩容方法直接获取2倍空间，不考虑tour值
		//首先直接put进去 然后调整数据空间
		//先调整bluck块
		int bluckKey=bluckId/maxLength;
//		System.out.println("bluckKey:"+bluckKey);
		changeBluck(bluckKey);
		int useIndex=bluckId%maxLength;
		Entry<K, V> e0 = table[useIndex];
		if (e0 == null) {
			table[useIndex] = new Entry(key, value, null);
			size++;
			changeBluckFile();
			return null;
		}
		Entry<K, V> e = e0;
		while (true) {
			K k = e.k;
			if (e.hash() == hash && (k.compareTo(key) == 0)) {
				V oldValue = e.v;
				e.v = value;
				return oldValue;
			}
			if (e.next == null) {
				e.next = new Entry(key, value, null);
				size++;
				// 如果数量过大 则写入硬盘
				changeBluckFile();
				return null;
			}
			e = e.next;
		}
	}
	/**
	 * 调整空间
	 */
	public void changeBluckFile(){
//		System.out.println(this.size+"\t"+maxLength*maxSonSize*bluckKeySize);
		//不需要调整
		if(this.size<maxLength*maxSonSize*bluckKeySize){
			return;
		}
		//调整空间
		bluckKeySize*=2;
		//调整总空间大小
		allLength=this.maxLength*bluckKeySize;
		//调整大小
		//读取每一块空间，然后修改对应的数据到文件中
		for(int i=0;i<bluckKeySize/2;i++){
			changeBluck(i);
			//重新处理空间构成
			//修正的size应变为2倍
			for(int j=0;j<bluckKeySize;j++){
				Entry<K,V>[] tableTmp=new Entry[this.maxLength];
				for(Entry<K,V> m:getKeySet(this.table)){
					int hash=hash(m.k);
					int index=indexFor(hash);
					int bluck=index/maxSonSize;
					if(bluck==j){
						standerPut(tableTmp,m.k,m.v,index%maxSonSize,hash);
					}
				}
				//写文件
				writeBluckByKey(tableTmp,dir,j,Integer.toString(i));	
			}
		}
		//读取相同的bluck的文件的不同分类下合并文件
		for(int i=0;i<bluckKeySize;i++){
			Entry<K,V>[] tableTmp=new Entry[this.maxLength];
			for(int j=0;j<bluckKeySize/2;j++){
				this.useBluckKeyId=i;
				readBluckByKey(dir,i,Integer.toString(j),true);
				for(Entry<K,V> m:getKeySet(this.table)){
					int hash=hash(m.k);
					int index=indexFor(hash);
					int bluck=index/maxSonSize;
					if(bluck==j){
						standerPut(tableTmp,m.k,m.v,index%maxSonSize,hash);
					}
				}
			}
			//写文件
			writeBluckByKey(tableTmp,dir,i,"");	
		}
	}
	/**
	 * 获取单个的keyset
	 * @return
	 */
	public Set<Entry<K,V>> getKeySet(Entry<K,V>[] list){
		Set<Entry<K,V>> set=new HashSet<Entry<K,V>>();
		for(int i=0;i<list.length;i++){
			Entry<K,V> val=list[i];
			if(val==null){
				continue;
			}
			set.add(val);
			while(val.next!=null){
				val=val.next;
				set.add(val);
			}
		}
		return set;
	}
	/**
	 * 调整使用bluckkey的文件
	 */
	public void changeBluck(int bluckKey){
		if(this.useBluckKeyId==bluckKey){
			return;
		}
		readBluckByKey(this.dir,bluckKey);
	}
	/**
	 * 读块
	 * @param dir
	 * @param bluckKey
	 */
	public void readBluckByKey(String dir,int bluckKey){
		String file = dir + "/bluck_" + bluckKey + ".bin";
//		System.out.println(file);
		try {
			table = (Entry<K, V>[]) SerializableMethod.revSerializeObjKryo(
					file, Entry[].class);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 读块
	 * @param dir
	 * @param bluckKey
	 * @param index 主类bluckKey的分类
	 */
	public void readBluckByKey(String dir,int bluckKey,String index,boolean isDelete){
		String file = dir + "/bluck_" + bluckKey + ".bin."+index;
		try {
			table = (Entry<K, V>[]) SerializableMethod.revSerializeObjKryo(
					file, Entry[].class);
			if(isDelete){
				File file2=new File(file);
				if(file2.exists()){
					file2.delete();
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 写块
	 * @param dir
	 * @param bluckKey
	 */
	public void writeBluckByKey(String dir,int bluckKey){
		String file = dir + "/bluck_" + bluckKey + ".bin";
		// 写磁盘需要写入
		try {
			SerializableMethod.serializeObjKryo(file, table);
		} catch (Exception e) {

		}
	}
	/**
	 * 写块
	 * @param dir
	 * @param bluckKey
	 * @param index 主类bluckKey的分类
	 */
	public void writeBluckByKey(Entry<K,V>[] entry,String dir,int bluckKey,String index){
		String file = dir + "/bluck_" + bluckKey + ".bin."+index;
		// 写磁盘需要写入
		try {
			SerializableMethod.serializeObjKryo(file, entry);
		} catch (Exception e) {

		}
	}
	/**
	 * 倾斜化 数据put方法
	 * @param key
	 * @param value
	 * @param bluckId
	 * @param hash
	 * @return
	 */
	public V incLinePut(K key,V value,int bluckId,int hash){
		
		Entry<K, V> e0 = table[bluckId];
		if (e0 == null) {
			table[bluckId] = new Entry(key, value, null);
			size++;
			return null;
		}
		if (e0.isFile) {
			// 读取数据并装载
			readDisk(e0, dir, bluckId);
		}
		int count = 0;
		Entry<K, V> e = e0;
		while (true) {
			count++;
			K k = e.k;
			if (e.hash() == hash && (k.compareTo(key) == 0)) {
				V oldValue = e.v;
				e.v = value;
				if (e0.isFile) {
					// 写入硬盘
					writeDisk(e0, dir, bluckId);
				}
				return oldValue;
			}
			if (e.next == null) {
				e.next = new Entry(key, value, null);
				e0.size++;
				// 如果数量过大 则写入硬盘
				if (count >= this.maxSonSize||e0.size>=this.maxSonSize) {
					// 写文件
					e0.isFile = true;
				}
				if (e0.isFile) {
					// 写入硬盘
					writeDisk(e0, dir, bluckId);
				}
				return null;
			}
			e = e.next;
		}
	}

	/**
	 * 
	 * @param entry
	 *            值
	 * @param dir
	 *            存储的目录
	 * @param bluckId
	 *            bluckId从0 开始
	 * @return
	 */
	public boolean writeDisk(Entry<K, V> entry, String dir, int bluckId) {

		String file = dir + "/" + bluckId + ".bin";
		// 写磁盘需要写入
		try {
			SerializableMethod.serializeObjKryo(file, entry.next);
		} catch (Exception e) {

		}
		entry.isFile = true;
		entry.next = null;
		return true;
	}

	/**
	 * 
	 * @param entry
	 * @param dir
	 * @param bluckId
	 */
	public void readDisk(Entry<K, V> entry, String dir, int bluckId) {
		String file = dir + "/" + bluckId + ".bin";
		try {
			entry.next = (Entry<K, V>) SerializableMethod.revSerializeObjKryo(
					file, Entry.class);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int size = 1000000;
		long allSize=size*100;
		HashMapMemory memory=new HashMapMemory("H:\\test\\memory\\hashMap",1000,10,false);
		long start_time = System.currentTimeMillis();
		long end_time = System.currentTimeMillis();
		for(long i=0l;i<allSize;i++){
			memory.put(i, i);
			if(i%100==0){
				end_time=System.currentTimeMillis();
				System.out.println(i+"\t"+(end_time-start_time)+"ms/"+10000);
			}
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
