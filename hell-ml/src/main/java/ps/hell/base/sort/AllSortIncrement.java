package ps.hell.base.sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * 增量式 排序方法
 * 
 * @author Administrator
 *
 */
public class AllSortIncrement {

	/**
	 * 增量式插入 默认的插入模式为 list已经被排序好了
	 * 
	 * @param list
	 *            初始化topN的大小
	 *            索引值 从0 开始对应的填充数量-1
	 * @param insertValue
	 *            新增值
	 * @param length
	 *            长度
	 * @param flag
	 */
	public static <T extends Comparable<? super T>> void insertSort(
			LinkedList<T> list, T insertValue, int length, boolean flag) {
		if (flag) {
			// 升序
			Iterator<T> iterator = list.iterator();
			int index = 0;
			while (iterator.hasNext()) {
				index++;
				T val = iterator.next();
				if (insertValue.compareTo(val) > 0) {
				} else {
					index--;
					break;
				}
			}
			if (index >= 0) {
				list.add(index, insertValue);
			}
			if (list.size() > length) {
				list.pollFirst();
			}
		} else {
			// 降序
			Iterator<T> iterator = list.iterator();
			int index = -1;
			while (iterator.hasNext()) {
				index++;
				T val = iterator.next();
				if (insertValue.compareTo(val) < 0) {

				} else {
					index--;
					break;
				}
			}
			if (index >= 0) {
				list.add(index, insertValue);
			} else if (list.size() < length) {
				list.addFirst(insertValue);
			}
			if (list.size() > length) {
				list.pollLast();
			}
		}
	}

	public static void main(String[] args) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		ArrayList<Integer> valList = new ArrayList<Integer>();
		Random random = new Random();
		int index = 0;
		int count = 102400;
		int length = 1000;
		int[] valList2 = new int[count];
		long start = System.currentTimeMillis();
		System.out.println("初始化");
		while (index < count) {
			index++;
			if (index % 10000 == 0) {
				System.out.println(index);
			}
			int val = Math.abs(random.nextInt() % 100);
			valList.add(val);
			valList2[index - 1] = val;
		}
		System.out.println((System.currentTimeMillis() - start) / 1000 + "ms");
		start = System.currentTimeMillis();
		for (Integer val : valList) {
			AllSortIncrement.insertSort(list, val, length, true);
		}

		System.out.println("第一个:" + (System.currentTimeMillis() - start) / 1000
				+ "ms");
		System.out.println(list.toString());
		start = System.currentTimeMillis();
		AllSort.heapSort(valList2, length, true);
		System.out.println("第二个:" + (System.currentTimeMillis() - start) / 1000
				+ "ms");
		for (int i = 0; i < length; i++) {
			System.out.print(valList2[i] + ",");
		}
	}
}
