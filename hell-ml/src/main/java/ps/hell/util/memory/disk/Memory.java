package ps.hell.util.memory.disk;


import ps.hell.util.JsonUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class Memory {
//	name：Cache的名称，必须是唯一的(ehcache会把这个cache放到HashMap里)。
//	maxElementsInMemory：内存中保持的对象数量。
//	maxElementsOnDisk：DiskStore中保持的对象数量，默认值为0，表示不限制。
//	eternal：是否是永恒数据，如果是，则它的超时设置会被忽略。
//	overflowToDisk：如果内存中数据数量超过maxElementsInMemory限制，是否要缓存到磁盘上。
//	timeToIdleSeconds：对象空闲时间，指对象在多长时间没有被访问就会失效。只对eternal为false的有效。默认值0，表示一直可以访问。
//	timeToLiveSeconds：对象存活时间，指对象从创建到失效所需要的时间。只对eternal为false的有效。默认值0，表示一直可以访问。
//	diskPersistent：是否在磁盘上持久化。指重启jvm后，数据是否有效。默认为false。
//	diskExpiryThreadIntervalSeconds：对象检测线程运行时间间隔。标识对象状态的线程多长时间运行一次。
//	diskSpoolBufferSizeMB：DiskStore使用的磁盘大小，默认值30MB。每个cache使用各自的DiskStore。
//	memoryStoreEvictionPolicy：如果内存中数据超过内存限制，向磁盘缓存时的策略。默认值LRU，可选FIFO、LFU。 
	
	//authCache1是可以存储在硬盘的
	public static void test(){
		CacheManager manager = CacheManager.create(Memory.class.getResource("ehcache.xml"));//System.getProperty("user.dir")+"/src/com/ml/memory/ehcache.xml");
		Cache cache = manager.getCache("authCache1");  
		/*强行输出内存数据到硬盘*/
		 for(int i = 0;i < 36;i++){  
	            Element elements =new Element(i,"aa");
	            cache.put(elements);
	            System.out.print(elements.getKey() + " ----- ");  
	            System.out.println(elements.getValue());  
	        }  
		
        cache.flush();  
          
        /** 
         * 关闭ehcache 
         */  
        manager.shutdown();  
          
        /** 
         * 取出数据 
         */  
        manager = CacheManager.create(Memory.class.getResource("ehcache.xml"));  
         try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        cache = manager.getCache("authCache1");  
          
        for(int i = 0;i < 36;i++){  
            Element elements = cache.get(i);  
            System.out.println(JsonUtil.getJsonStr(elements)); 
        }  
        manager.shutdown();  
	}
	//eternal： 设定缓存的elements是否永远不过期。如果为true，则缓存的数据始终有效，如果为false那么还要根据timeToIdleSeconds，timeToLiveSeconds判断。
	public static void main(String[] args) {
		Memory.test();
		System.exit(1);
		// 使用默认配置文件创建CacheManager
		CacheManager manager = CacheManager.create(Memory.class.getResource("ehcache.xml"));//System.getProperty("user.dir")+"/src/com/ml/memory/ehcache.xml");
		// 通过manager可以生成指定名称的Cache对象
		Cache cache = manager.getCache("demoCache");
		// 使用manager移除指定名称的Cache对象
//		manager.removeCache("demoCache");
		//可以通过调用manager.removalAll()来移除所有的Cache。通过调用manager的shutdown()方法可以关闭CacheManager。
		//有了Cache对象之后就可以进行一些基本的Cache操作，例如：
		//往cache中添加元素
		Element element = new Element("key", "value");
		cache.put(element);
		//从cache中取回元素
		Element element2 = cache.get("key");
		System.out.println(JsonUtil.getJsonStr(element2));
		//从Cache中移除一个元素
		cache.remove("key");
		
		manager.removeCache("demoCache");
		
		Cache test =manager.getCache("TEST_ID");
		System.out.println(JsonUtil.getJsonStr(test.get("key"+3)));
		System.exit(1);
		//manager.addCache("TEST_ID");  
        Cache cid = manager.getCache("TEST_ID");
        for(int i=0;i<100000;i++)
        {
        	cid.put(new Element("key"+i, "aaaaaaaaaaaaaaaaaaaaa"));
        }
        //System.out.println(JsonUtil.getJsonStr(cid.get("key")));
        cid.flush();
        manager.shutdown();
	}
}
