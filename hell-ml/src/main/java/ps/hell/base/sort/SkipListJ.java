package ps.hell.base.sort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 跳表的使用
 * @author Administrator
 *
 */
public class SkipListJ {
  public static List<Thread> threads = new ArrayList<Thread>();
  
  public static void doIt(final Map<String, String> map) {
    threads.clear();
    map.put("fjs", "fjs");
    
    for (int i = 0; i < 2; i++) {
      threads.add(new Thread(new Runnable(){

        @Override
        public void run() {
          // TODO Auto-generated method stub
          for (int i = 0; i < 1000000; i++) {
            String str = UUID.randomUUID().toString();
            map.put(str, str);
          }
        }
        
      }));
      threads.get(i).start();
    }
    for (Thread t : threads) {
      try {
        t.join();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
  }
  
  
  public static void main(String args[]) {
    long s1 = System.currentTimeMillis();
    Map<String, String> hmap = new ConcurrentHashMap<String, String>();
    SkipListJ.doIt(hmap);
    System.out.println(hmap.get("fjs"));
    long e1 = System.currentTimeMillis();
    System.out.println((e1 - s1) / 1000);
    
    long s2 = System.currentTimeMillis();
    Map<String, String> smap = new ConcurrentSkipListMap<String, String>();
    SkipListJ.doIt(smap);
    System.out.println(smap.get("fjs"));
    long e2 = System.currentTimeMillis();
    System.out.println((e2 - s2) / 1000);
  }
}