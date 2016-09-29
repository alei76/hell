package ps.hell.math.base.persons.aTest;

import java.util.*;

/**
 * 空间占用大小测试
 * @author Administrator
 *
 */
public class ObjectSize {
	
	public static long getKB(long size){
		return size/1024;
	}
	
	public static long println(String name,long size){
		long total1=Runtime.getRuntime().totalMemory();
        long m2 = Runtime.getRuntime().freeMemory();
        long re=total1-m2-size;
        System.out.println(name+":"+getKB(re)+" kb");
        return re;
	}
	
	public static long getCur(){
		long total1 = Runtime.getRuntime().totalMemory();
        long m2 = Runtime.getRuntime().freeMemory();
        return total1-m2;
	}

	public static void main(String[] args) {
		int num=1000000;
		System.gc();
        long size=getCur();
        
        int[]  array=new int[num];
        for(int i=0;i<num;i++){
        	array[i]=i;
        }
        println("int[]",size);
        array=null;
        System.gc();
        
        size=getCur();
        Integer[] arrayI=new Integer[num];
        for(int i=0;i<num;i++){
        	arrayI[i]=i;
        }
        size= println("Integer[]",size);
        arrayI=null;
        System.gc();
        
        
        size=getCur();
        ArrayList<Integer> arrayA=new ArrayList<Integer>(num);
        for(int i=0;i<num;i++){
        	arrayA.add(i);
        }
        size= println("Array[]",size);
        arrayA=null;
        System.gc();
        
        
        size=getCur();
        Set<Integer> set=new HashSet<Integer>();
        for(int i=0;i<num;i++){
        	set.add(i);
        }
        size= println("set",size);
        arrayA=null;
        System.gc();
        
        size=getCur();
        Map<Integer,Integer> map=new HashMap<Integer,Integer>();
        for(int i=0;i<num;i++){
        	map.put(i,i);
        }
        size= println("map",size);
        arrayA=null;
        System.gc();
	}
}
