package ps.hell.math.base.persons.aTest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		int number = 1000000;
		// 我们构造一个列表
		List<String> list = new ArrayList<String>(number);
		long begin1=System.nanoTime();
		for (int i = 0; i < number; i++) {
			list.add(Integer.toString(i));
		}
		System.out.println();
		System.out.printf("%10s=%10d/n", "new", System.nanoTime() - begin1);
		// 我们用foreach方式测试
		long begin = System.nanoTime();
		for (String i : list) {
			testMethod(i);
		}
		System.out.printf("%10s=%10d/n", "foreach", System.nanoTime() - begin);
		begin = System.nanoTime();
		// 下面我们用Iterator测试
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			testMethod(it.next());
		}
		System.out.printf("%10s=%10d/n", "iterator",
				(System.nanoTime() - begin));

		// 第三种，我们用index进行
		begin = System.nanoTime();
		for (int i = 0; i < number; i++) {
			testMethod(list.get(i));
		}
		System.out.printf("%10s=%10d/n", "for", (System.nanoTime() - begin));
		System.out.println();
		System.out.println("测试数组的性能...");
		// 我们再来测试数组
		String[] strArr = new String[number];
		for (int i = 0; i < number; i++) {
			strArr[i] = Integer.toString(i);
		}
		// 我们用foreach方式测试
		begin = System.nanoTime();
		for (String i : strArr) {
			testMethod(i);
		}
		System.out.printf("%10s=%10d/n", "foreach", System.nanoTime() - begin);
		// 第二种，我们用index进行
		begin = System.nanoTime();
		for (int i = 0; i < number; i++) {
			testMethod(strArr[i]);
		}
		System.out.printf("%10s=%10d/n", "for", System.nanoTime() - begin);

		
		LinkedList<String> list2 = new LinkedList<String>();
		begin1=System.nanoTime();
		for (int i = 0; i < number; i++) {
			list2.add(Integer.toString(i));
		}
		System.out.println();
		System.out.printf("%10s=%10d/n", "new", System.nanoTime() - begin1);
		System.out.println();
		System.out.println("测试链表的性能...");
		// 我们用foreach方式测试
		begin = System.nanoTime();
		for (String i : list2) {
			testMethod(i);
		}
		System.out.printf("%10s=%10d/n", "foreach", System.nanoTime() - begin);
		begin = System.nanoTime();
		// 下面我们用Iterator测试
		it = list2.iterator();
		while (it.hasNext()) {
			testMethod(it.next());
		}
		System.out.printf("%10s=%10d/n", "iterator",
				(System.nanoTime() - begin));

		// 第三种，我们用index进行
//		begin = System.nanoTime();
//		for (int i = 0; i < number; i++) {
//			testMethod(list2.get(i));
//		}
//		System.out.printf("%10s=%10d/n", "for", (System.nanoTime() - begin));
		
		begin = System.nanoTime();
		while(list2.size()>0){
			testMethod(list2.pollFirst());
		}
		System.out.printf("%10s=%10d/n", "for", (System.nanoTime() - begin));
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		System.out.println();
		System.out.println("测试int数组的性能...");
		// 我们再来测试数组
		int[] intArr = new int[number];
		for (int i = 0; i < number; i++) {
			intArr[i] = i;
		}
		// 我们用foreach方式测试
		begin = System.nanoTime();
		for (int i : intArr) {
			testMethod(i);
		}
		System.out.printf("%10s=%10d/n", "foreach", System.nanoTime() - begin);
		// 第二种，我们用index进行
		begin = System.nanoTime();
		for (int i = 0; i < number; i++) {
			testMethod(intArr[i]);
		}
		System.out.printf("%10s=%10d/n", "for", System.nanoTime() - begin);
		
		System.out.println();
		System.out.println("测试Integer数组的性能...");
		// 我们再来测试数组
		Integer[] integerArr = new Integer[number];
		for (int i = 0; i < number; i++) {
			integerArr[i] = i;
		}
		// 我们用foreach方式测试
		begin = System.nanoTime();
		for (Integer i : integerArr) {
			testMethod(i);
		}
		System.out.printf("%10s=%10d/n", "foreach", System.nanoTime() - begin);
		// 第二种，我们用index进行
		begin = System.nanoTime();
		for (int i = 0; i < number; i++) {
			testMethod(integerArr[i]);
		}
		System.out.printf("%10s=%10d/n", "for", System.nanoTime() - begin);
		
	}

	public static void testMethod(String str) {
	}
	
	public static void testMethod(int str) {
	}
	
	public static void testMethod(Integer str) {
	}
}
