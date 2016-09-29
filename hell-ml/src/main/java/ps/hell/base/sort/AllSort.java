package ps.hell.base.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 插入排序
 * 
 * @author Administrator
 * 
 */
public class AllSort<T> {

	/**
	 * 插入排序方法 T[] 泛型方法
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static <T extends Comparable<? super T>> void insertSort(T[] arr,
			int length, boolean flag) {
		int i, j;
		T target;
		if (flag) {
			for (i = 1; i < length; i++) {
				j = i;
				target = arr[i];
				while (j > 0 && target.compareTo(arr[j - 1]) < 0) {
					arr[j] = arr[j - 1];
					j--;
				}
				arr[j] = target;
			}
		} else {
			for (i = 1; i < length; i++) {
				j = i;
				target = arr[i];
				while (j > 0 && target.compareTo(arr[j - 1]) > 0) {
					arr[j] = arr[j - 1];
					j--;
				}
				arr[j] = target;
			}
		}
	}

	/**
	 * 插入排序方法 int[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void insertSort(int[] arr, int length, boolean flag) {
		int i, j;
		int target;
		if (flag) {
			for (i = 1; i < length; i++) {
				j = i;
				target = arr[i];
				while (j > 0 && target < arr[j - 1]) {
					arr[j] = arr[j - 1];
					j--;
				}
				arr[j] = target;
			}
		} else {
			for (i = 1; i < length; i++) {
				j = i;
				target = arr[i];
				while (j > 0 && target > arr[j - 1]) {
					arr[j] = arr[j - 1];
					j--;
				}
				arr[j] = target;
			}
		}
	}
	

	/**
	 * 插入排序方法 double[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void insertSort(double[] arr, int length, boolean flag) {
		int i, j;
		double target;
		if (flag) {
			for (i = 1; i < length; i++) {
				j = i;
				target = arr[i];
				while (j > 0 && target < arr[j - 1]) {
					arr[j] = arr[j - 1];
					j--;
				}
				arr[j] = target;
			}
		} else {
			for (i = 1; i < length; i++) {
				j = i;
				target = arr[i];
				while (j > 0 && target > arr[j - 1]) {
					arr[j] = arr[j - 1];
					j--;
				}
				arr[j] = target;
			}
		}
	}

	/**
	 * 插入排序方法 float[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void insertSort(float[] arr, int length, boolean flag) {
		int i, j;
		float target;
		if (flag) {
			for (i = 1; i < length; i++) {
				j = i;
				target = arr[i];
				while (j > 0 && target < arr[j - 1]) {
					arr[j] = arr[j - 1];
					j--;
				}
				arr[j] = target;
			}
		} else {
			for (i = 1; i < length; i++) {
				j = i;
				target = arr[i];
				while (j > 0 && target > arr[j - 1]) {
					arr[j] = arr[j - 1];
					j--;
				}
				arr[j] = target;
			}
		}
	}

	/**
	 * 插入排序方法 long[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void insertSort(long[] arr, int length, boolean flag) {
		int i, j;
		long target;
		if (flag) {
			for (i = 1; i < length; i++) {
				j = i;
				target = arr[i];
				while (j > 0 && target < arr[j - 1]) {
					arr[j] = arr[j - 1];
					j--;
				}
				arr[j] = target;
			}
		} else {
			for (i = 1; i < length; i++) {
				j = i;
				target = arr[i];
				while (j > 0 && target > arr[j - 1]) {
					arr[j] = arr[j - 1];
					j--;
				}
				arr[j] = target;
			}
		}
	}

	// 待排数组:[6 2 4 1 5 9]
	// 排后数组:[1 2 4 6 5 9]
	/**
	 *希尔排序方法 T[] 泛型方法
	 * 原理：又称增量缩小排序。先将序列按增量划分为元素个数相同的若干组，使用直接插入排序法进行排序，然后不断缩小增量直至为1
	 * ，以n的倍数分成一组，最后使用直接插入排序完成排序。
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param truncate为初始的块分割数量
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static <T extends Comparable<? super T>> void shellSort(T[] arr,
			int length, int truncate, boolean flag) {
		if (flag) {
			while (truncate >= 1) {
				int i, j;
				T target;
				for (i = truncate + 1; i < length; i++) {

					if (arr[i].compareTo(arr[i - truncate]) > 0) {
						target = arr[i];
						j = i;
						while (j > truncate
								&& target.compareTo(arr[j - truncate]) > 0) {
							arr[j] = arr[j - truncate];// 移动
							j = j - truncate;// 查找

						}
						arr[j] = target;

					}
				}
				truncate /= 2;
			}
		} else {
			while (truncate >= 1) {
				int i, j;
				T target;
				for (i = truncate + 1; i < length; i++) {

					if (arr[i].compareTo(arr[i - truncate]) < 0) {
						target = arr[i];
						j = i;
						while (j > truncate
								&& target.compareTo(arr[j - truncate]) < 0) {
							arr[j] = arr[j - truncate];// 移动
							j = j - truncate;// 查找
						}
						arr[j] = target;

					}
				}
				truncate /= 2;
			}
		}
	}

	/**
	 *希尔排序方法 int[] 原理：又称增量缩小排序。先将序列按增量划分为元素个数相同的若干组，使用直接插入排序法进行排序，然后不断缩小增量直至为1
	 * ，以n的倍数分成一组，最后使用直接插入排序完成排序。
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param truncate为初始的块分割数量
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void shellSort(int[] arr, int length, int truncate,
			boolean flag) {
		if (flag) {
			while (truncate >= 1) {
				int i, j, target;
				for (i = truncate + 1; i < length; i++) {

					if (arr[i] > arr[i - truncate]) {
						target = arr[i];
						j = i;
						while (j > truncate && target > arr[j - truncate]) {
							arr[j] = arr[j - truncate];// 移动
							j = j - truncate;// 查找

						}
						arr[j] = target;

					}
				}
				truncate /= 2;
			}
		} else {
			while (truncate >= 1) {
				int i, j, target;
				for (i = truncate + 1; i < length; i++) {

					if (arr[i] < arr[i - truncate]) {
						target = arr[i];
						j = i;
						while (j > truncate && target < arr[j - truncate]) {
							arr[j] = arr[j - truncate];// 移动
							j = j - truncate;// 查找
						}
						arr[j] = target;

					}
				}
				truncate /= 2;
			}
		}
	}

	/**
	 *希尔排序方法 double[]
	 * 原理：又称增量缩小排序。先将序列按增量划分为元素个数相同的若干组，使用直接插入排序法进行排序，然后不断缩小增量直至为1
	 * ，以n的倍数分成一组，最后使用直接插入排序完成排序。
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param truncate为初始的块分割数量
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void shellSort(double[] arr, int length, int truncate,
			boolean flag) {
		if (flag) {
			while (truncate >= 1) {
				int i, j;
				double target;
				for (i = truncate + 1; i < length; i++) {

					if (arr[i] > arr[i - truncate]) {
						target = arr[i];
						j = i;
						while (j > truncate && target > arr[j - truncate]) {
							arr[j] = arr[j - truncate];// 移动
							j = j - truncate;// 查找

						}
						arr[j] = target;

					}
				}
				truncate /= 2;
			}
		} else {
			while (truncate >= 1) {
				int i, j;
				double target;
				for (i = truncate + 1; i < length; i++) {

					if (arr[i] < arr[i - truncate]) {
						target = arr[i];
						j = i;
						while (j > truncate && target < arr[j - truncate]) {
							arr[j] = arr[j - truncate];// 移动
							j = j - truncate;// 查找
						}
						arr[j] = target;

					}
				}
				truncate /= 2;
			}
		}
	}

	/**
	 *希尔排序方法 float[]
	 * 原理：又称增量缩小排序。先将序列按增量划分为元素个数相同的若干组，使用直接插入排序法进行排序，然后不断缩小增量直至为1
	 * ，以n的倍数分成一组，最后使用直接插入排序完成排序。
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param truncate为初始的块分割数量
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void shellSort(float[] arr, int length, int truncate,
			boolean flag) {
		if (flag) {
			while (truncate >= 1) {
				int i, j;
				float target;
				for (i = truncate + 1; i < length; i++) {

					if (arr[i] > arr[i - truncate]) {
						target = arr[i];
						j = i;
						while (j > truncate && target > arr[j - truncate]) {
							arr[j] = arr[j - truncate];// 移动
							j = j - truncate;// 查找

						}
						arr[j] = target;

					}
				}
				truncate /= 2;
			}
		} else {
			while (truncate >= 1) {
				int i, j;
				float target;
				for (i = truncate + 1; i < length; i++) {

					if (arr[i] < arr[i - truncate]) {
						target = arr[i];
						j = i;
						while (j > truncate && target < arr[j - truncate]) {
							arr[j] = arr[j - truncate];// 移动
							j = j - truncate;// 查找
						}
						arr[j] = target;

					}
				}
				truncate /= 2;
			}
		}
	}

	/**
	 *希尔排序方法 long[] 原理：又称增量缩小排序。先将序列按增量划分为元素个数相同的若干组，使用直接插入排序法进行排序，然后不断缩小增量直至为1
	 * ，以n的倍数分成一组，最后使用直接插入排序完成排序。
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param truncate为初始的块分割数量
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void shellSort(long[] arr, int length, int truncate,
			boolean flag) {
		if (flag) {
			while (truncate >= 1) {
				int i, j;
				long target;
				for (i = truncate + 1; i < length; i++) {

					if (arr[i] > arr[i - truncate]) {
						target = arr[i];
						j = i;
						while (j > truncate && target > arr[j - truncate]) {
							arr[j] = arr[j - truncate];// 移动
							j = j - truncate;// 查找

						}
						arr[j] = target;

					}
				}
				truncate /= 2;
			}
		} else {
			while (truncate >= 1) {
				int i, j;
				long target;
				for (i = truncate + 1; i < length; i++) {

					if (arr[i] < arr[i - truncate]) {
						target = arr[i];
						j = i;
						while (j > truncate && target < arr[j - truncate]) {
							arr[j] = arr[j - truncate];// 移动
							j = j - truncate;// 查找
						}
						arr[j] = target;

					}
				}
				truncate /= 2;
			}
		}
	}

	/**
	 * 选择排序方法 T[] 泛型方法 原理:就是直接从待排序数组里选择一个最小(或最大)的数字,每次都拿一个最小数字出来, 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static <T extends Comparable<? super T>> void selectSort(T[] arr,
			int length, boolean flag) {
		if (flag) {
			int i, j, maxIndex;
			T max;
			for (i = 0; i < length - 1; i++) {
				max = arr[i];
				maxIndex = i;
				for (j = i + 1; j < length; j++) {
					if (max.compareTo(arr[j]) < 0) {
						max = arr[j];
						maxIndex = j;
					}
				}
				if (maxIndex == i) {
				} else {
					arr[maxIndex] = arr[i];
					arr[i] = max;
				}
			}
		} else {
			int i, j, minIndex;
			T min;
			for (i = 0; i < length - 1; i++) {
				min = arr[i];
				minIndex = i;
				for (j = i + 1; j < length; j++) {
					if (min.compareTo(arr[j]) > 0) {
						min = arr[j];
						minIndex = j;
					}
				}
				if (minIndex == i) {
				} else {
					arr[minIndex] = arr[i];
					arr[i] = min;
				}
			}
		}
	}

	/**
	 * 选择排序方法 int[] 原理:就是直接从待排序数组里选择一个最小(或最大)的数字,每次都拿一个最小数字出来, 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void selectSort(int[] arr, int length, boolean flag) {
		if (flag) {
			int i, j, maxIndex;
			int max;
			for (i = 0; i < length - 1; i++) {
				max = arr[i];
				maxIndex = i;
				for (j = i + 1; j < length; j++) {
					if (max < arr[j]) {
						max = arr[j];
						maxIndex = j;
					}
				}
				if (maxIndex == i) {
				} else {
					arr[maxIndex] = arr[i];
					arr[i] = max;
				}
			}
		} else {
			int i, j, minIndex;
			int min;
			for (i = 0; i < length - 1; i++) {
				min = arr[i];
				minIndex = i;
				for (j = i + 1; j < length; j++) {
					if (min > arr[j]) {
						min = arr[j];
						minIndex = j;
					}
				}
				if (minIndex == i) {
				} else {
					arr[minIndex] = arr[i];
					arr[i] = min;
				}
			}
		}
	}

	/**
	 * 选择排序方法 double[] 原理:就是直接从待排序数组里选择一个最小(或最大)的数字,每次都拿一个最小数字出来, 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void selectSort(double[] arr, int length, boolean flag) {
		if (flag) {
			int i, j, maxIndex;
			double max;
			for (i = 0; i < length - 1; i++) {
				max = arr[i];
				maxIndex = i;
				for (j = i + 1; j < length; j++) {
					if (max < arr[j]) {
						max = arr[j];
						maxIndex = j;
					}
				}
				if (maxIndex == i) {
				} else {
					arr[maxIndex] = arr[i];
					arr[i] = max;
				}
			}
		} else {
			int i, j, minIndex;
			double min;
			for (i = 0; i < length - 1; i++) {
				min = arr[i];
				minIndex = i;
				for (j = i + 1; j < length; j++) {
					if (min > arr[j]) {
						min = arr[j];
						minIndex = j;
					}
				}
				if (minIndex == i) {
				} else {
					arr[minIndex] = arr[i];
					arr[i] = min;
				}
			}
		}
	}

	/**
	 * 选择排序方法 float[] 原理:就是直接从待排序数组里选择一个最小(或最大)的数字,每次都拿一个最小数字出来, 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void selectSort(float[] arr, int length, boolean flag) {
		if (flag) {
			int i, j, maxIndex;
			float max;
			for (i = 0; i < length - 1; i++) {
				max = arr[i];
				maxIndex = i;
				for (j = i + 1; j < length; j++) {
					if (max < arr[j]) {
						max = arr[j];
						maxIndex = j;
					}
				}
				if (maxIndex == i) {
				} else {
					arr[maxIndex] = arr[i];
					arr[i] = max;
				}
			}
		} else {
			int i, j, minIndex;
			float min;
			for (i = 0; i < length - 1; i++) {
				min = arr[i];
				minIndex = i;
				for (j = i + 1; j < length; j++) {
					if (min > arr[j]) {
						min = arr[j];
						minIndex = j;
					}
				}
				if (minIndex == i) {
				} else {
					arr[minIndex] = arr[i];
					arr[i] = min;
				}
			}
		}
	}

	/**
	 * 选择排序方法 long[] 原理:就是直接从待排序数组里选择一个最小(或最大)的数字,每次都拿一个最小数字出来, 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void selectSort(long[] arr, int length, boolean flag) {
		if (flag) {
			int i, j, maxIndex;
			long max;
			for (i = 0; i < length - 1; i++) {
				max = arr[i];
				maxIndex = i;
				for (j = i + 1; j < length; j++) {
					if (max < arr[j]) {
						max = arr[j];
						maxIndex = j;
					}
				}
				if (maxIndex == i) {
				} else {
					arr[maxIndex] = arr[i];
					arr[i] = max;
				}
			}
		} else {
			int i, j, minIndex;
			long min;
			for (i = 0; i < length - 1; i++) {
				min = arr[i];
				minIndex = i;
				for (j = i + 1; j < length; j++) {
					if (min > arr[j]) {
						min = arr[j];
						minIndex = j;
					}
				}
				if (minIndex == i) {
				} else {
					arr[minIndex] = arr[i];
					arr[i] = min;
				}
			}
		}
	}

	/**
	 * 基数排序方法 T[] 泛型方法 不支持泛型方法 原理:从个位开始 先排序 从前往后 再10位 从前往后直到没有则排序结束
	 * 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static <T extends Comparable<? super T>> void baseSort(T[] arr,
			int length, boolean flag) {
		// if (flag) {
		// int i=0;//0-9
		// while(true)
		// {
		// i++;
		// int pow=(int)Math.pow(10.0,i);
		// for(int j=0;j<length;j++)
		// {
		// int index=arr[j]/pow%10;
		// }
		// }
		// } else {
		//			
		// }
	}

	/**
	 * 基数排序方法 int[] 原理:从个位开始 先排序 从前往后 再10位 从前往后直到没有则排序结束 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void baseSort(int[] arr, int length, int maxSize, boolean flag) {
		if (flag) {

			int i = 0;// 0-9
			int p = 0;
			int[][] bucket = new int[10][maxSize];
			while (true) {

				boolean flg = false;
				int pow = (int) Math.pow(10.0, i);
				for (int j = 0; j < length; j++) {
					int index = arr[j] / pow % 10;
					if (index == 0) {
						continue;
					}
					for (int m = 0; m < maxSize; m++) {
						if (bucket[index][m] == 0) {
							bucket[index][m] = arr[j];
							flg = true;
							break;
						}
					}
				}
				if (flg == false) {
					break;
				}
				p = length - 1;
				for (int m = 0; m < 10; m++) {
					for (int j = 0; j < maxSize; j++) {
						if (bucket[m][j] == 0) {
							break;
						} else {
							arr[p] = bucket[m][j];
							p--;
						}
					}
				}
				i++;
			}
		} else {
			int i = 0;// 0-9
			int p = 0;
			while (true) {

				int[][] bucket = new int[10][maxSize];
				int pow = (int) Math.pow(10.0, i);
				boolean flg = false;
				for (int j = 0; j < length; j++) {
					int index = arr[j] / pow % 10;
					if (index == 0) {
						continue;
					}
					for (int m = 0; m < maxSize; m++) {
						if (bucket[index][m] == 0) {
							bucket[index][m] = arr[j];
							flg = true;
							break;
						}
					}
				}
				if (flg == false) {
					break;
				}
				p = 0;
				for (int m = 0; m < 10; m++) {
					for (int j = 0; j < maxSize; j++) {
						if (bucket[m][j] == 0) {
							break;
						} else {
							arr[p] = bucket[m][j];
							p++;
						}
					}
				}
				i++;
			}
		}
	}

	/**
	 * 基数排序方法 long[] 原理:从个位开始 先排序 从前往后 再10位 从前往后直到没有则排序结束 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void baseSort(long[] arr, int length, int maxSize,
			boolean flag) {
		if (flag) {

			int i = 0;// 0-9
			int p = 0;
			long[][] bucket = new long[10][maxSize];
			while (true) {

				boolean flg = false;
				long pow = (long) Math.pow(10.0, i);
				for (int j = 0; j < length; j++) {
					int index = (int) (arr[j] / pow % 10);
					if (index == 0) {
						continue;
					}
					for (int m = 0; m < maxSize; m++) {
						if (bucket[index][m] == 0) {
							bucket[index][m] = arr[j];
							flg = true;
							break;
						}
					}
				}
				if (flg == false) {
					break;
				}
				p = length - 1;
				for (int m = 0; m < 10; m++) {
					for (int j = 0; j < maxSize; j++) {
						if (bucket[m][j] == 0) {
							break;
						} else {
							arr[p] = bucket[m][j];
							p--;
						}
					}
				}
				i++;
			}
		} else {
			int i = 0;// 0-9
			int p = 0;
			while (true) {

				long[][] bucket = new long[10][maxSize];
				long pow = (long) Math.pow(10.0, i);
				boolean flg = false;
				for (int j = 0; j < length; j++) {
					int index = (int) (arr[j] / pow % 10);
					if (index == 0) {
						continue;
					}
					for (int m = 0; m < maxSize; m++) {
						if (bucket[index][m] == 0) {
							bucket[index][m] = arr[j];
							flg = true;
							break;
						}
					}
				}
				if (flg == false) {
					break;
				}
				p = 0;
				for (int m = 0; m < 10; m++) {
					for (int j = 0; j < maxSize; j++) {
						if (bucket[m][j] == 0) {
							break;
						} else {
							arr[p] = bucket[m][j];
							p++;
						}
					}
				}
				i++;
			}
		}
	}

	/**
	 * 桶排序方法 long[] 原理:从个位开始 先排序 从前往后 再10位 从前往后直到没有则排序结束 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序 长度需要<Integer.MAX_VALUE
	 * @param 最小值
	 * @param 最大值
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void bucketSort(long[] arr, int length, long minValue,
			long maxValue, boolean flag) {
		if (flag) {
			// 拆成多组
			long[] bucket = new long[(int) (maxValue - minValue + 1)];
			HashMap map = new HashMap();
			for (int index = 0; index < length; index++) {
				if (bucket[(int) (arr[index] - minValue)] == 0L) {
					bucket[(int) (arr[index] - minValue)] = index;
				} else {
					if (map.containsKey((int) (arr[index] - minValue))) {
						map.put((int) (arr[index] - minValue),
								((StringBuffer) map
										.get((int) (arr[index] - minValue)))
										.append("|").append(
												Integer.toString(index)));
					} else {
						map.put((int) (arr[index] - minValue),
								new StringBuffer(Integer.toString(index)));

					}
				}
			}
			int i = 0;
			for (int index = (int) (maxValue - minValue); index >= 0; index--) {
				if (bucket[index] == 0L) {
				} else {
					arr[i] = bucket[index];
					i++;
					StringBuffer str = ((StringBuffer) map.get(index));
					if (str == null) {
					} else {
						System.out.println(str.toString());
						String[] strSplit = str.toString().split("\\|");
						for (String s : strSplit) {
							// 为获取编号
							// arr[i]=bucket[Integer.parseInt(s)];
							arr[i] = bucket[index];
							i++;
						}
					}

				}
			}
			map = null;

		} else {

			long[] bucket = new long[(int) (maxValue - minValue + 1)];
			HashMap map = new HashMap();
			for (int index = 0; index < length; index++) {
				if (bucket[(int) (arr[index] - minValue)] == 0L) {
					bucket[(int) (arr[index] - minValue)] = index;
				} else {
					if (map.containsKey((int) (arr[index] - minValue))) {
						map.put((int) (arr[index] - minValue),
								((StringBuffer) map
										.get((int) (arr[index] - minValue)))
										.append("|").append(
												Integer.toString(index)));
					} else {
						map.put((int) (arr[index] - minValue),
								new StringBuffer(Integer.toString(index)));

					}
				}
			}
			int i = 0;
			for (int index = 0; index <= (int) (maxValue - minValue); index++) {
				if (bucket[index] == 0L) {
				} else {
					arr[i] = bucket[index];
					i++;
					StringBuffer str = ((StringBuffer) map.get(index));
					if (str == null) {
					} else {
						System.out.println(str.toString());
						String[] strSplit = str.toString().split("\\|");
						for (String s : strSplit) {
							// 为获取编号
							// arr[i]=bucket[Integer.parseInt(s)];
							arr[i] = bucket[index];
							i++;
						}
					}

				}
			}
			map = null;
		}
	}

	/**
	 * 桶排序方法 int[] 原理:从个位开始 先排序 从前往后 再10位 从前往后直到没有则排序结束 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param 最小值
	 * @param 最大值
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void bucketSort(int[] arr, int length, int minValue,
			int maxValue, boolean flag) {
		if (flag) {
			// 拆成多组

			int[] bucket = new int[(maxValue - minValue + 1)];
			HashMap map = new HashMap();
			for (int index = 0; index < length; index++) {
				if (bucket[arr[index] - minValue] == 0L) {
					bucket[arr[index] - minValue] = index;
				} else {
					if (map.containsKey(arr[index] - minValue)) {
						map.put(arr[index] - minValue,
								((StringBuffer) map
										.get(arr[index] - minValue))
										.append("|").append(
												Integer.toString(index)));
					} else {
						map.put(arr[index] - minValue,
								new StringBuffer(Integer.toString(index)));

					}
				}
			}
			int i = 0;
			for (int index = maxValue - minValue; index >= 0; index--) {
				if (bucket[index] == 0L) {
				} else {
					arr[i] = bucket[index];
					i++;
					StringBuffer str = ((StringBuffer) map.get(index));
					if (str == null) {
					} else {
						System.out.println(str.toString());
						String[] strSplit = str.toString().split("\\|");
						for (String s : strSplit) {
							// 为获取编号
							// arr[i]=bucket[Integer.parseInt(s)];
							arr[i] = bucket[index];
							i++;
						}
					}

				}
			}
			map = null;

		} else {

			int[] bucket = new int[(maxValue - minValue + 1)];
			HashMap map = new HashMap();
			for (int index = 0; index < length; index++) {
				if (bucket[arr[index] - minValue] == 0L) {
					bucket[arr[index] - minValue] = index;
				} else {
					if (map.containsKey(arr[index] - minValue)) {
						map.put(arr[index] - minValue,
								((StringBuffer) map
										.get(arr[index] - minValue))
										.append("|").append(
												Integer.toString(index)));
					} else {
						map.put(arr[index] - minValue,
								new StringBuffer(Integer.toString(index)));

					}
				}
			}
			int i = 0;
			for (int index = 0; index <= maxValue - minValue; index++) {
				if (bucket[index] == 0L) {
				} else {
					arr[i] = bucket[index];
					i++;
					StringBuffer str = ((StringBuffer) map.get(index));
					if (str == null) {
					} else {
						System.out.println(str.toString());
						String[] strSplit = str.toString().split("\\|");
						for (String s : strSplit) {
							// 为获取编号
							// arr[i]=bucket[Integer.parseInt(s)];
							arr[i] = bucket[index];
							i++;
						}
					}

				}
			}
			map = null;
		}
	}

	/**
	 * 快速排序方法 int[] 原理:从个位开始 先排序 从前往后 再10位 从前往后直到没有则排序结束 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            选取左侧标记
	 * @param right
	 *            选取右侧标记
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void quickSort(int[] arr, int left, int right, boolean flag) {
		if (flag) {
			// 拆成多组
			int i = left, j = right;
			int middle, iTemp;
			middle = arr[(left + right) / 2];
			do {
				while ((arr[i] > middle) && (i < right))
					// 从左扫描小于中值的树
					i++;
				while ((arr[j] < middle) && (j > left))
					// 从右扫描大于中值的数
					j--;
				if (i <= j) {
					iTemp = arr[i];
					arr[i] = arr[j];
					arr[j] = iTemp;
					i++;
					j--;
				}
			} while (i <= j);
			// 当左边部分有值(left<j)，递归左半边
			if (left < j)
				quickSort(arr, left, j, flag);
			// 当右边部分有值(right>i)，递归右半边
			if (right > i)
				quickSort(arr, i, right, flag);
		} else {

			// 拆成多组
			int i = left, j = right;
			int middle, iTemp;
			middle = arr[(left + right) / 2];
			do {
				while ((arr[i] < middle) && (i < right))
					// 从左扫描大于中值的数
					i++;
				while ((arr[j] > middle) && (j > left))
					// 从右扫描小于中值的数
					j--;
				if (i <= j) {
					iTemp = arr[i];
					arr[i] = arr[j];
					arr[j] = iTemp;
					i++;
					j--;
				}
			} while (i <= j);
			// 当左边部分有值(left<j)，递归左半边
			if (left < j)
				quickSort(arr, left, j, flag);
			// 当右边部分有值(right>i)，递归右半边
			if (right > i)
				quickSort(arr, i, right, flag);
		}
	}

	/**
	 * 快速排序方法 double[] 原理:从个位开始 先排序 从前往后 再10位 从前往后直到没有则排序结束 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            选取左侧标记
	 * @param right
	 *            选取右侧标记
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void quickSort(double[] arr, int left, int right, boolean flag) {
		if (flag) {
			// 拆成多组
			int i = left, j = right;
			double middle, iTemp;
			middle = arr[(left + right) / 2];
			do {
				while ((arr[i] > middle) && (i < right))
					// 从左扫描小于中值的树
					i++;
				while ((arr[j] < middle) && (j > left))
					// 从右扫描大于中值的数
					j--;
				if (i <= j) {
					iTemp = arr[i];
					arr[i] = arr[j];
					arr[j] = iTemp;
					i++;
					j--;
				}
			} while (i <= j);
			// 当左边部分有值(left<j)，递归左半边
			if (left < j)
				quickSort(arr, left, j, flag);
			// 当右边部分有值(right>i)，递归右半边
			if (right > i)
				quickSort(arr, i, right, flag);
		} else {

			// 拆成多组
			int i = left, j = right;
			double middle, iTemp;
			middle = arr[(left + right) / 2];
			do {
				while ((arr[i] < middle) && (i < right))
					// 从左扫描大于中值的数
					i++;
				while ((arr[j] > middle) && (j > left))
					// 从右扫描小于中值的数
					j--;
				if (i <= j) {
					iTemp = arr[i];
					arr[i] = arr[j];
					arr[j] = iTemp;
					i++;
					j--;
				}
			} while (i <= j);
			// 当左边部分有值(left<j)，递归左半边
			if (left < j)
				quickSort(arr, left, j, flag);
			// 当右边部分有值(right>i)，递归右半边
			if (right > i)
				quickSort(arr, i, right, flag);
		}
	}

	/**
	 * 快速排序方法 int[] 原理:从个位开始 先排序 从前往后 再10位 从前往后直到没有则排序结束 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            选取左侧标记
	 * @param right
	 *            选取右侧标记
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void quickSort(float[] arr, int left, int right, boolean flag) {
		if (flag) {
			// 拆成多组
			int i = left, j = right;
			float middle, iTemp;
			middle = arr[(left + right) / 2];
			do {
				while ((arr[i] > middle) && (i < right))
					// 从左扫描小于中值的树
					i++;
				while ((arr[j] < middle) && (j > left))
					// 从右扫描大于中值的数
					j--;
				if (i <= j) {
					iTemp = arr[i];
					arr[i] = arr[j];
					arr[j] = iTemp;
					i++;
					j--;
				}
			} while (i <= j);
			// 当左边部分有值(left<j)，递归左半边
			if (left < j)
				quickSort(arr, left, j, flag);
			// 当右边部分有值(right>i)，递归右半边
			if (right > i)
				quickSort(arr, i, right, flag);
		} else {

			// 拆成多组
			int i = left, j = right;
			float middle, iTemp;
			middle = arr[(left + right) / 2];
			do {
				while ((arr[i] < middle) && (i < right))
					// 从左扫描大于中值的数
					i++;
				while ((arr[j] > middle) && (j > left))
					// 从右扫描小于中值的数
					j--;
				if (i <= j) {
					iTemp = arr[i];
					arr[i] = arr[j];
					arr[j] = iTemp;
					i++;
					j--;
				}
			} while (i <= j);
			// 当左边部分有值(left<j)，递归左半边
			if (left < j)
				quickSort(arr, left, j, flag);
			// 当右边部分有值(right>i)，递归右半边
			if (right > i)
				quickSort(arr, i, right, flag);
		}
	}

	/**
	 * 快速排序方法 int[] 原理:从个位开始 先排序 从前往后 再10位 从前往后直到没有则排序结束 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            选取左侧标记
	 * @param right
	 *            选取右侧标记
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static void quickSort(long[] arr, int left, int right, boolean flag) {
		if (flag) {
			// 拆成多组
			int i = left, j = right;
			long middle, iTemp;
			middle = arr[(left + right) / 2];
			do {
				while ((arr[i] > middle) && (i < right))
					// 从左扫描小于中值的树
					i++;
				while ((arr[j] < middle) && (j > left))
					// 从右扫描大于中值的数
					j--;
				if (i <= j) {
					iTemp = arr[i];
					arr[i] = arr[j];
					arr[j] = iTemp;
					i++;
					j--;
				}
			} while (i <= j);
			// 当左边部分有值(left<j)，递归左半边
			if (left < j)
				quickSort(arr, left, j, flag);
			// 当右边部分有值(right>i)，递归右半边
			if (right > i)
				quickSort(arr, i, right, flag);
		} else {

			// 拆成多组
			int i = left, j = right;
			long middle, iTemp;
			middle = arr[(left + right) / 2];
			do {
				while ((arr[i] < middle) && (i < right))
					// 从左扫描大于中值的数
					i++;
				while ((arr[j] > middle) && (j > left))
					// 从右扫描小于中值的数
					j--;
				if (i <= j) {
					iTemp = arr[i];
					arr[i] = arr[j];
					arr[j] = iTemp;
					i++;
					j--;
				}
			} while (i <= j);
			// 当左边部分有值(left<j)，递归左半边
			if (left < j)
				quickSort(arr, left, j, flag);
			// 当右边部分有值(right>i)，递归右半边
			if (right > i)
				quickSort(arr, i, right, flag);
		}
	}

	/**
	 * 快速排序方法 T[] 泛型方法 不支持泛型方法 原理:从个位开始 先排序 从前往后 再10位 从前往后直到没有则排序结束
	 * 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param length为对整个
	 *            [] 取前length个 排序
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static <T extends Comparable<? super T>> void quickSort(T[] arr,
			int left, int right, boolean flag) {
		if (flag) {
			// 拆成多组
			int i = left, j = right;
			T middle, iTemp;
			middle = arr[(left + right) / 2];
			do {
				while ((arr[i].compareTo(middle) > 0) && (i < right))
					// 从左扫描小于中值的树
					i++;
				while ((arr[j].compareTo(middle) < 0) && (j > left))
					// 从右扫描大于中值的数
					j--;
				if (i <= j) {
					iTemp = arr[i];
					arr[i] = arr[j];
					arr[j] = iTemp;
					i++;
					j--;
				}
			} while (i <= j);
			// 当左边部分有值(left<j)，递归左半边
			if (left < j)
				quickSort(arr, left, j, flag);
			// 当右边部分有值(right>i)，递归右半边
			if (right > i)
				quickSort(arr, i, right, flag);
		} else {

			// 拆成多组
			int i = left, j = right;
			T middle, iTemp;
			middle = arr[(left + right) / 2];
			do {
				while ((arr[i].compareTo(middle) < 0) && (i < right))
					// 从左扫描大于中值的数
					i++;
				while ((arr[j].compareTo(middle) > 0) && (j > left))
					// 从右扫描小于中值的数
					j--;
				if (i <= j) {
					iTemp = arr[i];
					arr[i] = arr[j];
					arr[j] = iTemp;
					i++;
					j--;
				}
			} while (i <= j);
			// 当左边部分有值(left<j)，递归左半边
			if (left < j)
				quickSort(arr, left, j, flag);
			// 当右边部分有值(right>i)，递归右半边
			if (right > i)
				quickSort(arr, i, right, flag);
		}
	}

	/**
	 * 归并方法 int[] 原理:{6，202，100，301，38，8，1}
	 * 第一次归并后：{6,202},{100,301},{8,38},{1}，比较次数：3；
	 * 第二次归并后：{6,100,202,301}，{1,8,38}，比较次数：4；
	 * 第三次归并后：{1,6,8,38,100,202,301},比较次数：4； 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧index
	 * @param right
	 *            右侧 index
	 * @param count
	 *            初始 为1
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void mergerSort(int[] arr, int left, int right, int count,
			boolean flag) {
		if (flag) {
			int temp = 0, i;
			count *= 2;
			if (count == 2) {
				for (i = left; i < right - 1; i += 2) {
					if (arr[i] < arr[i + 1]) {
						temp = arr[i + 1];
						arr[i + 1] = arr[i];
						arr[i] = temp;
					}
				}
				mergerSort(arr, left, right, count, flag);
			} else if (count / 2 <= right - left) {
				int x;
				int y;
				for (i = left; i <= (right - count < left ? left : right
						- count); i += count) {
					x = i;
					y = i + count / 2;
					int maxIndex = i + count < right - left + 1 ? i + count
							: right - left + 1;

					int[] tempArr = new int[maxIndex - i];
					int index = 0;
					while (true) {
						if (x >= i + count / 2 && y >= maxIndex) {
							break;
						} else if (x >= i + count / 2) {
							tempArr[index] = arr[y];
							y++;
						} else if (y >= maxIndex) {
							tempArr[index] = arr[x];
							x++;
						} else if (arr[x] < arr[y]) {
							tempArr[index] = arr[y];
							y++;
						} else {
							tempArr[index] = arr[x];
							x++;
						}
						index++;
					}
					for (int j = 0; j < maxIndex - i; j++) {
						arr[i + j] = tempArr[j];
					}
					tempArr = null;
				}
				mergerSort(arr, left, right, count, flag);
			}
		} else {

			int temp = 0, i;
			count *= 2;
			if (count == 2) {
				for (i = left; i < right - 1; i += 2) {
					if (arr[i] > arr[i + 1]) {
						temp = arr[i + 1];
						arr[i + 1] = arr[i];
						arr[i] = temp;
					}
				}
				mergerSort(arr, left, right, count, flag);
			} else if (count / 2 <= right - left) {
				int x;
				int y;
				for (i = left; i <= (right - count < left ? left : right
						- count); i += count) {
					x = i;
					y = i + count / 2;
					int maxIndex = i + count < right - left + 1 ? i + count
							: right - left + 1;

					int[] tempArr = new int[maxIndex - i];
					int index = 0;
					while (true) {
						if (x >= i + count / 2 && y >= maxIndex) {
							break;
						} else if (x >= i + count / 2) {
							tempArr[index] = arr[y];
							y++;
						} else if (y >= maxIndex) {
							tempArr[index] = arr[x];
							x++;
						} else if (arr[x] > arr[y]) {
							tempArr[index] = arr[y];
							y++;
						} else {
							tempArr[index] = arr[x];
							x++;
						}
						index++;
					}
					for (int j = 0; j < maxIndex - i; j++) {
						arr[i + j] = tempArr[j];
					}
					tempArr = null;
				}
				mergerSort(arr, left, right, count, flag);
			}
		}

	}

	/**
	 * 归并方法 float[] 原理:{6，202，100，301，38，8，1}
	 * 第一次归并后：{6,202},{100,301},{8,38},{1}，比较次数：3；
	 * 第二次归并后：{6,100,202,301}，{1,8,38}，比较次数：4；
	 * 第三次归并后：{1,6,8,38,100,202,301},比较次数：4； 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧index
	 * @param right
	 *            右侧 index
	 * @param count
	 *            初始 为1
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void mergerSort(float[] arr, int left, int right, int count,
			boolean flag) {
		if (flag) {
			float temp = 0;
			int i;
			count *= 2;
			if (count == 2) {
				for (i = left; i < right - 1; i += 2) {
					if (arr[i] < arr[i + 1]) {
						temp = arr[i + 1];
						arr[i + 1] = arr[i];
						arr[i] = temp;
					}
				}
				mergerSort(arr, left, right, count, flag);
			} else if (count / 2 <= right - left) {
				int x;
				int y;
				for (i = left; i <= (right - count < left ? left : right
						- count); i += count) {
					x = i;
					y = i + count / 2;
					int maxIndex = i + count < right - left + 1 ? i + count
							: right - left + 1;

					float[] tempArr = new float[maxIndex - i];
					int index = 0;
					while (true) {
						if (x >= i + count / 2 && y >= maxIndex) {
							break;
						} else if (x >= i + count / 2) {
							tempArr[index] = arr[y];
							y++;
						} else if (y >= maxIndex) {
							tempArr[index] = arr[x];
							x++;
						} else if (arr[x] < arr[y]) {
							tempArr[index] = arr[y];
							y++;
						} else {
							tempArr[index] = arr[x];
							x++;
						}
						index++;
					}
					for (int j = 0; j < maxIndex - i; j++) {
						arr[i + j] = tempArr[j];
					}
					tempArr = null;
				}
				mergerSort(arr, left, right, count, flag);
			}
		} else {

			float temp = 0;
			int i;
			count *= 2;
			if (count == 2) {
				for (i = left; i < right - 1; i += 2) {
					if (arr[i] > arr[i + 1]) {
						temp = arr[i + 1];
						arr[i + 1] = arr[i];
						arr[i] = temp;
					}
				}
				mergerSort(arr, left, right, count, flag);
			} else if (count / 2 <= right - left) {
				int x;
				int y;
				for (i = left; i <= (right - count < left ? left : right
						- count); i += count) {
					x = i;
					y = i + count / 2;
					int maxIndex = i + count < right - left + 1 ? i + count
							: right - left + 1;

					float[] tempArr = new float[maxIndex - i];
					int index = 0;
					while (true) {
						if (x >= i + count / 2 && y >= maxIndex) {
							break;
						} else if (x >= i + count / 2) {
							tempArr[index] = arr[y];
							y++;
						} else if (y >= maxIndex) {
							tempArr[index] = arr[x];
							x++;
						} else if (arr[x] > arr[y]) {
							tempArr[index] = arr[y];
							y++;
						} else {
							tempArr[index] = arr[x];
							x++;
						}
						index++;
					}
					for (int j = 0; j < maxIndex - i; j++) {
						arr[i + j] = tempArr[j];
					}
					tempArr = null;
				}
				mergerSort(arr, left, right, count, flag);
			}
		}

	}

	/**
	 * 归并方法 double[] 原理:{6，202，100，301，38，8，1}
	 * 第一次归并后：{6,202},{100,301},{8,38},{1}，比较次数：3；
	 * 第二次归并后：{6,100,202,301}，{1,8,38}，比较次数：4；
	 * 第三次归并后：{1,6,8,38,100,202,301},比较次数：4； 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧index
	 * @param right
	 *            右侧 index
	 * @param count
	 *            初始 为1
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void mergerSort(double[] arr, int left, int right, int count,
			boolean flag) {
		if (flag) {
			double temp = 0;
			int i;
			count *= 2;
			if (count == 2) {
				for (i = left; i < right - 1; i += 2) {
					if (arr[i] < arr[i + 1]) {
						temp = arr[i + 1];
						arr[i + 1] = arr[i];
						arr[i] = temp;
					}
				}
				mergerSort(arr, left, right, count, flag);
			} else if (count / 2 <= right - left) {
				int x;
				int y;
				for (i = left; i <= (right - count < left ? left : right
						- count); i += count) {
					x = i;
					y = i + count / 2;
					int maxIndex = i + count < right - left + 1 ? i + count
							: right - left + 1;

					double[] tempArr = new double[maxIndex - i];
					int index = 0;
					while (true) {
						if (x >= i + count / 2 && y >= maxIndex) {
							break;
						} else if (x >= i + count / 2) {
							tempArr[index] = arr[y];
							y++;
						} else if (y >= maxIndex) {
							tempArr[index] = arr[x];
							x++;
						} else if (arr[x] < arr[y]) {
							tempArr[index] = arr[y];
							y++;
						} else {
							tempArr[index] = arr[x];
							x++;
						}
						index++;
					}
					for (int j = 0; j < maxIndex - i; j++) {
						arr[i + j] = tempArr[j];
					}
					tempArr = null;
				}
				mergerSort(arr, left, right, count, flag);
			}
		} else {

			double temp = 0;
			int i;
			count *= 2;
			if (count == 2) {
				for (i = left; i < right - 1; i += 2) {
					if (arr[i] > arr[i + 1]) {
						temp = arr[i + 1];
						arr[i + 1] = arr[i];
						arr[i] = temp;
					}
				}
				mergerSort(arr, left, right, count, flag);
			} else if (count / 2 <= right - left) {
				int x;
				int y;
				for (i = left; i <= (right - count < left ? left : right
						- count); i += count) {
					x = i;
					y = i + count / 2;
					int maxIndex = i + count < right - left + 1 ? i + count
							: right - left + 1;

					double[] tempArr = new double[maxIndex - i];
					int index = 0;
					while (true) {
						if (x >= i + count / 2 && y >= maxIndex) {
							break;
						} else if (x >= i + count / 2) {
							tempArr[index] = arr[y];
							y++;
						} else if (y >= maxIndex) {
							tempArr[index] = arr[x];
							x++;
						} else if (arr[x] > arr[y]) {
							tempArr[index] = arr[y];
							y++;
						} else {
							tempArr[index] = arr[x];
							x++;
						}
						index++;
					}
					for (int j = 0; j < maxIndex - i; j++) {
						arr[i + j] = tempArr[j];
					}
					tempArr = null;
				}
				mergerSort(arr, left, right, count, flag);
			}
		}

	}

	/**
	 * 归并方法 long[] 原理:{6，202，100，301，38，8，1}
	 * 第一次归并后：{6,202},{100,301},{8,38},{1}，比较次数：3；
	 * 第二次归并后：{6,100,202,301}，{1,8,38}，比较次数：4；
	 * 第三次归并后：{1,6,8,38,100,202,301},比较次数：4； 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧index
	 * @param right
	 *            右侧 index
	 * @param count
	 *            初始 为1
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void mergerSort(long[] arr, int left, int right, int count,
			boolean flag) {
		if (flag) {
			long temp = 0L;
			int i;
			count *= 2;
			if (count == 2) {
				for (i = left; i < right - 1; i += 2) {
					if (arr[i] < arr[i + 1]) {
						temp = arr[i + 1];
						arr[i + 1] = arr[i];
						arr[i] = temp;
					}
				}
				mergerSort(arr, left, right, count, flag);
			} else if (count / 2 <= right - left) {
				int x;
				int y;
				for (i = left; i <= (right - count < left ? left : right
						- count); i += count) {
					x = i;
					y = i + count / 2;
					int maxIndex = i + count < right - left + 1 ? i + count
							: right - left + 1;

					long[] tempArr = new long[maxIndex - i];
					int index = 0;
					while (true) {
						if (x >= i + count / 2 && y >= maxIndex) {
							break;
						} else if (x >= i + count / 2) {
							tempArr[index] = arr[y];
							y++;
						} else if (y >= maxIndex) {
							tempArr[index] = arr[x];
							x++;
						} else if (arr[x] < arr[y]) {
							tempArr[index] = arr[y];
							y++;
						} else {
							tempArr[index] = arr[x];
							x++;
						}
						index++;
					}
					for (int j = 0; j < maxIndex - i; j++) {
						arr[i + j] = tempArr[j];
					}
					tempArr = null;
				}
				mergerSort(arr, left, right, count, flag);
			}
		} else {

			long temp = 0L;
			int i;
			count *= 2;
			if (count == 2) {
				for (i = left; i < right - 1; i += 2) {
					if (arr[i] > arr[i + 1]) {
						temp = arr[i + 1];
						arr[i + 1] = arr[i];
						arr[i] = temp;
					}
				}
				mergerSort(arr, left, right, count, flag);
			} else if (count / 2 <= right - left) {
				int x;
				int y;
				for (i = left; i <= (right - count < left ? left : right
						- count); i += count) {
					x = i;
					y = i + count / 2;
					int maxIndex = i + count < right - left + 1 ? i + count
							: right - left + 1;

					long[] tempArr = new long[maxIndex - i];
					int index = 0;
					while (true) {
						if (x >= i + count / 2 && y >= maxIndex) {
							break;
						} else if (x >= i + count / 2) {
							tempArr[index] = arr[y];
							y++;
						} else if (y >= maxIndex) {
							tempArr[index] = arr[x];
							x++;
						} else if (arr[x] > arr[y]) {
							tempArr[index] = arr[y];
							y++;
						} else {
							tempArr[index] = arr[x];
							x++;
						}
						index++;
					}
					for (int j = 0; j < maxIndex - i; j++) {
						arr[i + j] = tempArr[j];
					}
					tempArr = null;
				}
				mergerSort(arr, left, right, count, flag);
			}
		}

	}

	/**
	 * 归并方法 T[] 泛型方法 原理:{6，202，100，301，38，8，1}
	 * 第一次归并后：{6,202},{100,301},{8,38},{1}，比较次数：3；
	 * 第二次归并后：{6,100,202,301}，{1,8,38}，比较次数：4；
	 * 第三次归并后：{1,6,8,38,100,202,301},比较次数：4； 顺序放入新数组,直到全部拿完
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧index
	 * @param right
	 *            右侧 index
	 * @param count
	 *            初始 为1
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static <T extends Comparable<? super T>> void mergerSort(T[] arr,
			int left, int right, int count, boolean flag) {
		if (flag) {
			T temp;
			int i;
			count *= 2;
			if (count == 2) {
				for (i = left; i < right - 1; i += 2) {
					if (arr[i].compareTo(arr[i + 1]) < 0) {
						temp = arr[i + 1];
						arr[i + 1] = arr[i];
						arr[i] = temp;
					}
				}
				mergerSort(arr, left, right, count, flag);
			} else if (count / 2 <= right - left) {
				int x;
				int y;
				for (i = left; i <= (right - count < left ? left : right
						- count); i += count) {
					x = i;
					y = i + count / 2;
					int maxIndex = i + count < right - left + 1 ? i + count
							: right - left + 1;

					List<T> tempArr = new ArrayList<T>();
					int index = 0;
					while (true) {
						if (x >= i + count / 2 && y >= maxIndex) {
							break;
						} else if (x >= i + count / 2) {
							tempArr.add(arr[y]);
							y++;
						} else if (y >= maxIndex) {
							tempArr.add(arr[x]);
							x++;
						} else if (arr[x].compareTo(arr[y]) < 0) {
							tempArr.add(arr[y]);
							y++;
						} else {
							tempArr.add(arr[x]);
							x++;
						}
						index++;
					}
					for (int j = 0; j < tempArr.size(); j++) {
						arr[i + j] = tempArr.get(j);
					}
					tempArr = null;
				}
				mergerSort(arr, left, right, count, flag);
			}
		} else {

			T temp;
			int i;
			count *= 2;
			if (count == 2) {
				for (i = left; i < right - 1; i += 2) {
					if (arr[i].compareTo(arr[i + 1]) > 0) {
						temp = arr[i + 1];
						arr[i + 1] = arr[i];
						arr[i] = temp;
					}
				}
				mergerSort(arr, left, right, count, flag);
			} else if (count / 2 <= right - left) {
				int x;
				int y;
				for (i = left; i <= (right - count < left ? left : right
						- count); i += count) {
					x = i;
					y = i + count / 2;
					int maxIndex = i + count < right - left + 1 ? i + count
							: right - left + 1;

					List<T> tempArr = new ArrayList<T>();
					int index = 0;
					while (true) {
						if (x >= i + count / 2 && y >= maxIndex) {
							break;
						} else if (x >= i + count / 2) {
							tempArr.add(arr[y]);
							y++;
						} else if (y >= maxIndex) {
							tempArr.add(arr[x]);
							x++;
						} else if (arr[x].compareTo(arr[y]) > 0) {
							tempArr.add(arr[y]);
							y++;
						} else {
							tempArr.add(arr[x]);
							x++;
						}
						index++;
					}
					for (int j = 0; j < tempArr.size(); j++) {
						arr[i + j] = tempArr.get(j);
					}
					tempArr = null;
				}
				mergerSort(arr, left, right, count, flag);
			}
		}
	}

	/**
	 * 记数 排序int[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param k为
	 *            int中最大的数字+1
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void countSort(int[] input, int k) {
		// input为输入数组，output为输出数组，k表示有所输入数字都介于0到k之间
		int[] output = new int[input.length];
		int[] c = new int[k];// 临时存储区
		int len = c.length;
		// 初始化
		for (int i = 0; i < len; i++) {
			c[i] = 0;
		}
		// 检查每个输入元素，如果一个输入元素的值为input[i],那么c[input[i]]的值加1，此操作完成后，c[i]中存放了值为i的元素的个数
		for (int i = 0; i < input.length; i++) {
			c[input[i]]++;
		}
		// 通过在c中记录计数和，c[i]中存放的是小于等于i元素的数字个数
		for (int i = 1; i < len; i++) {
			c[i] = c[i] + c[i - 1];
		}
		// 把输入数组中的元素放在输出数组中对应的位置上
		for (int i = input.length - 1; i >= 0; i--) {// 从后往前遍历
			output[c[input[i]] - 1] = input[i];
			c[input[i]]--;// 该操作使得下一个值为input[i]的元素直接进入输出数组中input[i]的前一个位置
		}
		for (int i = 0; i < input.length; i++) {
			input[i] = output[i];
		}
	}

	/**
	 * 记数 排序Integer[]
	 * 
	 * @param input为
	 *            list的相关 extend类型
	 * @param k为
	 *            int中最大的数字+1
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void countSort(Integer[] input, int k, boolean flag) {

		// input为输入数组，output为输出数组，k表示有所输入数字都介于0到k之间
		int[] output = new int[input.length];
		int[] c = new int[k];// 临时存储区
		int len = c.length;
		// 初始化
		for (int i = 0; i < len; i++) {
			c[i] = 0;
		}
		// 检查每个输入元素，如果一个输入元素的值为input[i],那么c[input[i]]的值加1，此操作完成后，c[i]中存放了值为i的元素的个数
		for (int i = 0; i < input.length; i++) {
			c[input[i]]++;
		}
		// 通过在c中记录计数和，c[i]中存放的是小于等于i元素的数字个数
		for (int i = 1; i < len; i++) {
			c[i] = c[i] + c[i - 1];
		}
		// 把输入数组中的元素放在输出数组中对应的位置上
		for (int i = input.length - 1; i >= 0; i--) {// 从后往前遍历
			output[c[input[i]] - 1] = input[i];
			c[input[i]]--;// 该操作使得下一个值为input[i]的元素直接进入输出数组中input[i]的前一个位置
		}
		if (flag) {
			for (int i = 0; i < input.length; i++) {
				input[i] = output[i];
			}
		} else {
			for (int i = input.length - 1; i >= 0; i--) {
				input[input.length - 1 - i] = output[i];
			}
		}
	}

	/**
	 * 堆排序int[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param index
	 *            为 0-index的排序
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void heapSort(int[] list, int index, boolean flag) {
		if (flag) {
			// 大根堆
			if (index > 0) {
				// 初始化堆，找出最大的放在堆顶
				int m = (index + 1) / 2;
				int l_child;
				int r_child;
				boolean flag2;
				for (int i = 0; i < m; i++) {
					flag2 = false;
					// 建立子堆
					l_child = 2 * i + 1;// 左孩子
					r_child = 2 * i + 2;// 右孩子
					if (r_child > index) { // 判断是否有右孩子，没有的话直接比较，大于交换
						if (list[i] > list[l_child]) {
							list[i] = list[i] + list[l_child];
							list[l_child] = list[i] - list[l_child];
							list[i] = list[i] - list[l_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					} else {
						// 在根与两个孩子之间找出最小的那个值进行交换
						if (list[i] > list[l_child]) {

							if (list[l_child] < list[r_child]) {
								// 交换根与左孩子的值
								list[i] = list[i] + list[l_child];
								list[l_child] = list[i] - list[l_child];
								list[i] = list[i] - list[l_child];
								flag2 = true;
							} else {
								// 交换根与右孩子的值
								list[i] = list[i] + list[r_child];
								list[r_child] = list[i] - list[r_child];
								list[i] = list[i] - list[r_child];
								flag2 = true;
							}
						} else if (list[i] > list[r_child]) {
							// 交换根与右孩子的值
							list[i] = list[i] + list[r_child];
							list[r_child] = list[i] - list[r_child];
							list[i] = list[i] - list[r_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					}

					// 如果孩子之间有交换，就要重新开始
					if (flag2) {
						i = -1;
					}

				}
				list[0] = list[index] + list[0];
				list[index] = list[0] - list[index];
				list[0] = list[0] - list[index];
				heapSort(list, index - 1, flag);
			}
		} else {
			// 小根堆
			if (index > 0) {
				// 初始化堆，找出最大的放在堆顶
				int m = (index + 1) / 2;
				int l_child;
				int r_child;
				boolean flag2;
				for (int i = 0; i < m; i++) {
					flag2 = false;
					// 建立子堆
					l_child = 2 * i + 1;// 左孩子
					r_child = 2 * i + 2;// 右孩子
					if (r_child > index) { // 判断是否有右孩子，没有的话直接比较，小于交换
						if (list[i] < list[l_child]) {
							list[i] = list[i] + list[l_child];
							list[l_child] = list[i] - list[l_child];
							list[i] = list[i] - list[l_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					} else {
						// 在根与两个孩子之间找出最大的那个值进行交换
						if (list[i] < list[l_child]) {
							if (list[l_child] > list[r_child]) {
								// 交换根与左孩子的值
								list[i] = list[i] + list[l_child];
								list[l_child] = list[i] - list[l_child];
								list[i] = list[i] - list[l_child];
								flag2 = true;
							} else {
								// 交换根与右孩子的值
								list[i] = list[i] + list[r_child];
								list[r_child] = list[i] - list[r_child];
								list[i] = list[i] - list[r_child];
								flag2 = true;
							}
						} else if (list[i] < list[r_child]) {
							// 交换根与右孩子的值
							list[i] = list[i] + list[r_child];
							list[r_child] = list[i] - list[r_child];
							list[i] = list[i] - list[r_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					}

					// 如果孩子之间有交换，就要重新开始
					if (flag2) {
						i = -1;
					}

				}
				list[0] = list[index] + list[0];
				list[index] = list[0] - list[index];
				list[0] = list[0] - list[index];
				heapSort(list, index - 1, flag);
			}
		}
	}

	/**
	 * 堆排序double[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param index
	 *            为 0-index的排序
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void heapSort(double[] list, int index, boolean flag) {
		if (flag) {
			// 大根堆
			if (index > 0) {
				// 初始化堆，找出最大的放在堆顶
				int m = (index + 1) / 2;
				int l_child;
				int r_child;
				boolean flag2;
				for (int i = 0; i < m; i++) {
					flag2 = false;
					// 建立子堆
					l_child = 2 * i + 1;// 左孩子
					r_child = 2 * i + 2;// 右孩子
					if (r_child > index) { // 判断是否有右孩子，没有的话直接比较，大于交换
						if (list[i] > list[l_child]) {
							list[i] = list[i] + list[l_child];
							list[l_child] = list[i] - list[l_child];
							list[i] = list[i] - list[l_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					} else {
						// 在根与两个孩子之间找出最小的那个值进行交换
						if (list[i] > list[l_child]) {

							if (list[l_child] < list[r_child]) {
								// 交换根与左孩子的值
								list[i] = list[i] + list[l_child];
								list[l_child] = list[i] - list[l_child];
								list[i] = list[i] - list[l_child];
								flag2 = true;
							} else {
								// 交换根与右孩子的值
								list[i] = list[i] + list[r_child];
								list[r_child] = list[i] - list[r_child];
								list[i] = list[i] - list[r_child];
								flag2 = true;
							}
						} else if (list[i] > list[r_child]) {
							// 交换根与右孩子的值
							list[i] = list[i] + list[r_child];
							list[r_child] = list[i] - list[r_child];
							list[i] = list[i] - list[r_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					}

					// 如果孩子之间有交换，就要重新开始
					if (flag2) {
						i = -1;
					}

				}
				list[0] = list[index] + list[0];
				list[index] = list[0] - list[index];
				list[0] = list[0] - list[index];
				heapSort(list, index - 1, flag);
			}
		} else {
			// 小根堆
			if (index > 0) {
				// 初始化堆，找出最大的放在堆顶
				int m = (index + 1) / 2;
				int l_child;
				int r_child;
				boolean flag2;
				for (int i = 0; i < m; i++) {
					flag2 = false;
					// 建立子堆
					l_child = 2 * i + 1;// 左孩子
					r_child = 2 * i + 2;// 右孩子
					if (r_child > index) { // 判断是否有右孩子，没有的话直接比较，小于交换
						if (list[i] < list[l_child]) {
							list[i] = list[i] + list[l_child];
							list[l_child] = list[i] - list[l_child];
							list[i] = list[i] - list[l_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					} else {
						// 在根与两个孩子之间找出最大的那个值进行交换
						if (list[i] < list[l_child]) {
							if (list[l_child] > list[r_child]) {
								// 交换根与左孩子的值
								list[i] = list[i] + list[l_child];
								list[l_child] = list[i] - list[l_child];
								list[i] = list[i] - list[l_child];
								flag2 = true;
							} else {
								// 交换根与右孩子的值
								list[i] = list[i] + list[r_child];
								list[r_child] = list[i] - list[r_child];
								list[i] = list[i] - list[r_child];
								flag2 = true;
							}
						} else if (list[i] < list[r_child]) {
							// 交换根与右孩子的值
							list[i] = list[i] + list[r_child];
							list[r_child] = list[i] - list[r_child];
							list[i] = list[i] - list[r_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					}

					// 如果孩子之间有交换，就要重新开始
					if (flag2) {
						i = -1;
					}

				}
				list[0] = list[index] + list[0];
				list[index] = list[0] - list[index];
				list[0] = list[0] - list[index];
				heapSort(list, index - 1, flag);
			}
		}
	}

	/**
	 * 堆排序float[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param index
	 *            为 0-index的排序
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void heapSort(float[] list, int index, boolean flag) {
		if (flag) {
			// 大根堆
			if (index > 0) {
				// 初始化堆，找出最大的放在堆顶
				int m = (index + 1) / 2;
				int l_child;
				int r_child;
				boolean flag2;
				for (int i = 0; i < m; i++) {
					flag2 = false;
					// 建立子堆
					l_child = 2 * i + 1;// 左孩子
					r_child = 2 * i + 2;// 右孩子
					if (r_child > index) { // 判断是否有右孩子，没有的话直接比较，大于交换
						if (list[i] > list[l_child]) {
							list[i] = list[i] + list[l_child];
							list[l_child] = list[i] - list[l_child];
							list[i] = list[i] - list[l_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					} else {
						// 在根与两个孩子之间找出最小的那个值进行交换
						if (list[i] > list[l_child]) {

							if (list[l_child] < list[r_child]) {
								// 交换根与左孩子的值
								list[i] = list[i] + list[l_child];
								list[l_child] = list[i] - list[l_child];
								list[i] = list[i] - list[l_child];
								flag2 = true;
							} else {
								// 交换根与右孩子的值
								list[i] = list[i] + list[r_child];
								list[r_child] = list[i] - list[r_child];
								list[i] = list[i] - list[r_child];
								flag2 = true;
							}
						} else if (list[i] > list[r_child]) {
							// 交换根与右孩子的值
							list[i] = list[i] + list[r_child];
							list[r_child] = list[i] - list[r_child];
							list[i] = list[i] - list[r_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					}

					// 如果孩子之间有交换，就要重新开始
					if (flag2) {
						i = -1;
					}

				}
				list[0] = list[index] + list[0];
				list[index] = list[0] - list[index];
				list[0] = list[0] - list[index];
				heapSort(list, index - 1, flag);
			}
		} else {
			// 小根堆
			if (index > 0) {
				// 初始化堆，找出最大的放在堆顶
				int m = (index + 1) / 2;
				int l_child;
				int r_child;
				boolean flag2;
				for (int i = 0; i < m; i++) {
					flag2 = false;
					// 建立子堆
					l_child = 2 * i + 1;// 左孩子
					r_child = 2 * i + 2;// 右孩子
					if (r_child > index) { // 判断是否有右孩子，没有的话直接比较，小于交换
						if (list[i] < list[l_child]) {
							list[i] = list[i] + list[l_child];
							list[l_child] = list[i] - list[l_child];
							list[i] = list[i] - list[l_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					} else {
						// 在根与两个孩子之间找出最大的那个值进行交换
						if (list[i] < list[l_child]) {
							if (list[l_child] > list[r_child]) {
								// 交换根与左孩子的值
								list[i] = list[i] + list[l_child];
								list[l_child] = list[i] - list[l_child];
								list[i] = list[i] - list[l_child];
								flag2 = true;
							} else {
								// 交换根与右孩子的值
								list[i] = list[i] + list[r_child];
								list[r_child] = list[i] - list[r_child];
								list[i] = list[i] - list[r_child];
								flag2 = true;
							}
						} else if (list[i] < list[r_child]) {
							// 交换根与右孩子的值
							list[i] = list[i] + list[r_child];
							list[r_child] = list[i] - list[r_child];
							list[i] = list[i] - list[r_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					}

					// 如果孩子之间有交换，就要重新开始
					if (flag2) {
						i = -1;
					}

				}
				list[0] = list[index] + list[0];
				list[index] = list[0] - list[index];
				list[0] = list[0] - list[index];
				heapSort(list, index - 1, flag);
			}
		}
	}

	/**
	 * 堆排序Long[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param index
	 *            为 0-index的排序
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void heapSort(long[] list, int index, boolean flag) {
		if (flag) {
			// 大根堆
			if (index > 0) {
				// 初始化堆，找出最大的放在堆顶
				int m = (index + 1) / 2;
				int l_child;
				int r_child;
				boolean flag2;
				for (int i = 0; i < m; i++) {
					flag2 = false;
					// 建立子堆
					l_child = 2 * i + 1;// 左孩子
					r_child = 2 * i + 2;// 右孩子
					if (r_child > index) { // 判断是否有右孩子，没有的话直接比较，大于交换
						if (list[i] > list[l_child]) {
							list[i] = list[i] + list[l_child];
							list[l_child] = list[i] - list[l_child];
							list[i] = list[i] - list[l_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					} else {
						// 在根与两个孩子之间找出最小的那个值进行交换
						if (list[i] > list[l_child]) {

							if (list[l_child] < list[r_child]) {
								// 交换根与左孩子的值
								list[i] = list[i] + list[l_child];
								list[l_child] = list[i] - list[l_child];
								list[i] = list[i] - list[l_child];
								flag2 = true;
							} else {
								// 交换根与右孩子的值
								list[i] = list[i] + list[r_child];
								list[r_child] = list[i] - list[r_child];
								list[i] = list[i] - list[r_child];
								flag2 = true;
							}
						} else if (list[i] > list[r_child]) {
							// 交换根与右孩子的值
							list[i] = list[i] + list[r_child];
							list[r_child] = list[i] - list[r_child];
							list[i] = list[i] - list[r_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					}

					// 如果孩子之间有交换，就要重新开始
					if (flag2) {
						i = -1;
					}

				}
				list[0] = list[index] + list[0];
				list[index] = list[0] - list[index];
				list[0] = list[0] - list[index];
				heapSort(list, index - 1, flag);
			}
		} else {
			// 小根堆
			if (index > 0) {
				// 初始化堆，找出最大的放在堆顶
				int m = (index + 1) / 2;
				int l_child;
				int r_child;
				boolean flag2;
				for (int i = 0; i < m; i++) {
					flag2 = false;
					// 建立子堆
					l_child = 2 * i + 1;// 左孩子
					r_child = 2 * i + 2;// 右孩子
					if (r_child > index) { // 判断是否有右孩子，没有的话直接比较，小于交换
						if (list[i] < list[l_child]) {
							list[i] = list[i] + list[l_child];
							list[l_child] = list[i] - list[l_child];
							list[i] = list[i] - list[l_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					} else {
						// 在根与两个孩子之间找出最大的那个值进行交换
						if (list[i] < list[l_child]) {
							if (list[l_child] > list[r_child]) {
								// 交换根与左孩子的值
								list[i] = list[i] + list[l_child];
								list[l_child] = list[i] - list[l_child];
								list[i] = list[i] - list[l_child];
								flag2 = true;
							} else {
								// 交换根与右孩子的值
								list[i] = list[i] + list[r_child];
								list[r_child] = list[i] - list[r_child];
								list[i] = list[i] - list[r_child];
								flag2 = true;
							}
						} else if (list[i] < list[r_child]) {
							// 交换根与右孩子的值
							list[i] = list[i] + list[r_child];
							list[r_child] = list[i] - list[r_child];
							list[i] = list[i] - list[r_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					}

					// 如果孩子之间有交换，就要重新开始
					if (flag2) {
						i = -1;
					}

				}
				list[0] = list[index] + list[0];
				list[index] = list[0] - list[index];
				list[0] = list[0] - list[index];
				heapSort(list, index - 1, flag);
			}
		}
	}

	/**
	 * 堆排序T[] 泛型方法
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param index
	 *            为 0-index的排序
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static <T extends Comparable<? super T>> void heapSort(T[] list,
			int index, boolean flag) {
		if (flag) {
			// 大根堆
			if (index > 0) {
				// 初始化堆，找出最大的放在堆顶
				int m = (index + 1) / 2;
				int l_child;
				int r_child;
				T temp;
				boolean flag2;
				for (int i = 0; i < m; i++) {
					flag2 = false;
					// 建立子堆
					l_child = 2 * i + 1;// 左孩子
					r_child = 2 * i + 2;// 右孩子
					if (r_child > index) { // 判断是否有右孩子，没有的话直接比较，大于交换
						if (list[i].compareTo(list[l_child]) > 0) {
							temp = list[i];
							list[i] = list[l_child];
							list[l_child] = temp;
							// list[i] = list[i] + list[l_child];
							// list[l_child] = list[i] - list[l_child];
							// list[i] = list[i] - list[l_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					} else {
						// 在根与两个孩子之间找出最小的那个值进行交换
						if (list[i].compareTo(list[l_child]) > 0) {

							if (list[l_child].compareTo(list[r_child]) < 0) {
								// 交换根与左孩子的值
								temp = list[i];
								list[i] = list[l_child];
								list[l_child] = temp;
								// list[i] = list[i] + list[l_child];
								// list[l_child] = list[i] - list[l_child];
								// list[i] = list[i] - list[l_child];
								flag2 = true;
							} else {
								// 交换根与右孩子的值
								temp = list[i];
								list[i] = list[r_child];
								list[r_child] = temp;
								// list[i] = list[i] + list[r_child];
								// list[r_child] = list[i] - list[r_child];
								// list[i] = list[i] - list[r_child];
								flag2 = true;
							}
						} else if (list[i].compareTo(list[r_child]) > 0) {
							// 交换根与右孩子的值
							temp = list[i];
							list[i] = list[r_child];
							list[r_child] = temp;
							// list[i] = list[i] + list[r_child];
							// list[r_child] = list[i] - list[r_child];
							// list[i] = list[i] - list[r_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					}

					// 如果孩子之间有交换，就要重新开始
					if (flag2) {
						i = -1;
					}

				}
				temp = list[0];
				list[0] = list[index];
				list[index] = temp;
				heapSort(list, index - 1, flag);
			}
		} else {
			// 小根堆
			if (index > 0) {
				// 初始化堆，找出最大的放在堆顶
				int m = (index + 1) / 2;
				int l_child;
				int r_child;
				T temp;
				boolean flag2;
				for (int i = 0; i < m; i++) {
					flag2 = false;
					// 建立子堆
					l_child = 2 * i + 1;// 左孩子
					r_child = 2 * i + 2;// 右孩子
					if (r_child > index) { // 判断是否有右孩子，没有的话直接比较，小于交换
						if (list[i].compareTo(list[l_child]) < 0) {
							temp = list[i];
							list[i] = list[l_child];
							list[l_child] = temp;
							// list[i] = list[i] + list[l_child];
							// list[l_child] = list[i] - list[l_child];
							// list[i] = list[i] - list[l_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					} else {
						// 在根与两个孩子之间找出最大的那个值进行交换
						if (list[i].compareTo(list[l_child]) < 0) {
							if (list[l_child].compareTo(list[r_child]) > 0) {
								// 交换根与左孩子的值
								temp = list[i];
								list[i] = list[l_child];
								list[l_child] = temp;
								// list[i] = list[i] + list[l_child];
								// list[l_child] = list[i] - list[l_child];
								// list[i] = list[i] - list[l_child];
								flag2 = true;
							} else {
								// 交换根与右孩子的值
								temp = list[i];
								list[i] = list[r_child];
								list[r_child] = temp;
								// list[i] = list[i] + list[r_child];
								// list[r_child] = list[i] - list[r_child];
								// list[i] = list[i] - list[r_child];
								flag2 = true;
							}
						} else if (list[i].compareTo(list[r_child]) < 0) {
							// 交换根与右孩子的值
							temp = list[i];
							list[i] = list[r_child];
							list[r_child] = temp;
							// list[i] = list[i] + list[r_child];
							// list[r_child] = list[i] - list[r_child];
							// list[i] = list[i] - list[r_child];
							flag2 = true;
						} else {
							flag2 = false;
						}
					}

					// 如果孩子之间有交换，就要重新开始
					if (flag2) {
						i = -1;
					}

				}
				temp = list[0];
				list[0] = list[index];
				list[index] = temp;
				heapSort(list, index - 1, flag);
			}
		}
	}

	/**
	 * 冒泡排序排序int[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void bubbleSort(int[] list, int left, int right, boolean flag) {
		if (flag) {
			int temp;
			for (int i = left; i <= right; i++) {
				for (int j = i + 1; j <= right; j++) {
					if (list[i] < list[j]) {
						temp = list[i];
						list[i] = list[j];
						list[j] = temp;
					}
				}
			}
		} else {
			int temp;
			for (int i = left; i <= right; i++) {
				for (int j = i + 1; j <= right; j++) {
					if (list[i] > list[j]) {
						temp = list[i];
						list[i] = list[j];
						list[j] = temp;
					}
				}
			}
		}
	}

	/**
	 * 冒泡排序排序float[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void bubbleSort(float[] list, int left, int right,
			boolean flag) {
		if (flag) {
			float temp;
			for (int i = left; i <= right; i++) {
				for (int j = i + 1; j <= right; j++) {
					if (list[i] < list[j]) {
						temp = list[i];
						list[i] = list[j];
						list[j] = temp;
					}
				}
			}
		} else {
			float temp;
			for (int i = left; i <= right; i++) {
				for (int j = i + 1; j <= right; j++) {
					if (list[i] > list[j]) {
						temp = list[i];
						list[i] = list[j];
						list[j] = temp;
					}
				}
			}
		}
	}

	/**
	 * 冒泡排序排序double[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void bubbleSort(double[] list, int left, int right,
			boolean flag) {
		if (flag) {
			double temp;
			for (int i = left; i <= right; i++) {
				for (int j = i + 1; j <= right; j++) {
					if (list[i] < list[j]) {
						temp = list[i];
						list[i] = list[j];
						list[j] = temp;
					}
				}
			}
		} else {
			double temp;
			for (int i = left; i <= right; i++) {
				for (int j = i + 1; j <= right; j++) {
					if (list[i] > list[j]) {
						temp = list[i];
						list[i] = list[j];
						list[j] = temp;
					}
				}
			}
		}
	}

	/**
	 * 冒泡排序排序long[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void bubbleSort(long[] list, int left, int right, boolean flag) {
		if (flag) {
			long temp;
			for (int i = left; i <= right; i++) {
				for (int j = i + 1; j <= right; j++) {
					if (list[i] < list[j]) {
						temp = list[i];
						list[i] = list[j];
						list[j] = temp;
					}
				}
			}
		} else {
			long temp;
			for (int i = left; i <= right; i++) {
				for (int j = i + 1; j <= right; j++) {
					if (list[i] > list[j]) {
						temp = list[i];
						list[i] = list[j];
						list[j] = temp;
					}
				}
			}
		}
	}

	/**
	 * 冒泡排序排序T[] 泛型
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static <T extends Comparable<? super T>> void bubbleSort(T[] list,
			int left, int right, boolean flag) {
		if (flag) {
			T temp;
			for (int i = left; i <= right; i++) {
				for (int j = i + 1; j <= right; j++) {
					if (list[i].compareTo(list[j]) < 0) {
						temp = list[i];
						list[i] = list[j];
						list[j] = temp;
					}
				}
			}
		} else {
			T temp;
			for (int i = left; i <= right; i++) {
				for (int j = i + 1; j <= right; j++) {
					if (list[i].compareTo(list[j]) > 0) {
						temp = list[i];
						list[i] = list[j];
						list[j] = temp;
					}
				}
			}
		}
	}
	
	//直接跳用冒泡排序获取前n个最大值
	/**
	 * 
	 * @param list
	 * @param limit
	 * @param flag true 降序 false升序
	 */
	public static <T extends Comparable<? super T>> void bubbleSort(ArrayList<T> list,int limit,boolean flag)
	{
		Object[] fl=list.toArray();
		for(int j=0;j<limit;j++)
		{
			if(flag)
			{
				for(int i=fl.length-1;i>0;i--)
				{
					if(((Comparable)fl[i]).compareTo(fl[i-1])>0)
					{
						fl[i-1]=fl[i];
					}
				}
				list.set(j,(T)fl[j]);
			}else{
				for(int i=fl.length-1;i>0;i--)
				{
					if(((Comparable)fl[i]).compareTo(fl[i-1])<0)
					{
						fl[i-1]=fl[i];
					}
				}
				list.set(j,(T)fl[j]);
			}
		}
	}

	/**
	 * 地精排序排序int[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void tkSort(int[] list, int left, int right, boolean flag) {
		if (flag) {
			int temp;
			for (int i = left; i <= right - 1; i++) {
				if (list[i] < list[i + 1]) {
					temp = list[i];
					list[i] = list[i + 1];
					list[i + 1] = temp;
					i = left;
				}
			}
		} else {
			int temp;
			for (int i = left; i <= right - 1; i++) {
				if (list[i] > list[i + 1]) {
					temp = list[i];
					list[i] = list[i + 1];
					list[i + 1] = temp;
					i--;
				}
			}
		}
	}

	/**
	 * 地精排序排序float[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void tkSort(float[] list, int left, int right, boolean flag) {
		if (flag) {
			float temp;
			for (int i = left; i <= right - 1; i++) {
				if (list[i] < list[i + 1]) {
					temp = list[i];
					list[i] = list[i + 1];
					list[i + 1] = temp;
					i--;
				}
			}
		} else {
			float temp;
			for (int i = left; i <= right - 1; i++) {
				if (list[i] > list[i + 1]) {
					temp = list[i];
					list[i] = list[i + 1];
					list[i + 1] = temp;
					i--;
				}
			}
		}
	}

	/**
	 * 地精排序排序double[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void tkSort(double[] list, int left, int right, boolean flag) {
		if (flag) {
			double temp;
			for (int i = left; i <= right - 1; i++) {
				if (list[i] < list[i + 1]) {
					temp = list[i];
					list[i] = list[i + 1];
					list[i + 1] = temp;
					i--;
				}
			}
		} else {
			double temp;
			for (int i = left; i <= right - 1; i++) {
				if (list[i] > list[i + 1]) {
					temp = list[i];
					list[i] = list[i + 1];
					list[i + 1] = temp;
					i--;
				}
			}
		}
	}

	/**
	 * 地精排序排序long[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static void tkSort(long[] list, int left, int right, boolean flag) {
		if (flag) {
			long temp;
			for (int i = left; i <= right - 1; i++) {
				if (list[i] < list[i + 1]) {
					temp = list[i];
					list[i] = list[i + 1];
					list[i + 1] = temp;
					i--;
				}
			}
		} else {
			long temp;
			for (int i = left; i <= right - 1; i++) {
				if (list[i] > list[i + 1]) {
					temp = list[i];
					list[i] = list[i + 1];
					list[i + 1] = temp;
					i--;
				}
			}
		}
	}

	/**
	 * 地精排序排序T[] 泛型
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc
	 */
	public static <T extends Comparable<? super T>> void tkSort(T[] list,
			int left, int right, boolean flag) {
		if (flag) {
			T temp;
			for (int i = left; i <= right - 1; i++) {
				if (list[i].compareTo(list[i + 1]) < 0) {
					temp = list[i];
					list[i] = list[i + 1];
					list[i + 1] = temp;
					i--;
				}
			}
		} else {
			T temp;
			for (int i = left; i <= right - 1; i++) {
				if (list[i].compareTo(list[i + 1]) > 0) {
					temp = list[i];
					list[i] = list[i + 1];
					list[i + 1] = temp;
					i--;
				}
			}
		}
	}

	/**
	 * 奇偶排序int[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void oddEvenSort(int[] list, int left, int right, boolean flag) {
		if (flag) {
			int temp;
			boolean fl = false;
			boolean fl1 = false;
			boolean fl2 = false;
			while (true) {
				if (fl == false) {
					fl = true;
				} else {
					fl = false;
				}
				if (fl == true) {
					fl1 = true;
					
					for (int i = left; i < right; i += 2) {
						if (list[i] < list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl1 = false;
						}
					}
					if (fl2 == true) {
						if (fl1 == true) {
							break;
						}
					}
				} else {
					fl2 = true;
					for (int i = left + 1; i < right; i += 2) {
						if (list[i] < list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl2 = false;
						}
					}
					if (fl1 == true) {
						if (fl2 == true) {
							break;
						}
					}
				}
			}
		} else {
			int temp;
			boolean fl = false;
			boolean fl1 = false;
			boolean fl2 = false;
			while (true) {
				if (fl == false) {
					fl = true;
				} else {
					fl = false;
				}
				if (fl == true) {
					fl1 = true;
					
					for (int i = left; i < right; i += 2) {
						if (list[i] > list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl1 = false;
						}
					}
					if (fl2 == true) {
						if (fl1 == true) {
							break;
						}
					}
				} else {
					fl2 = true;
					for (int i = left + 1; i < right; i += 2) {
						if (list[i] > list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl2 = false;
						}
					}
					if (fl1 == true) {
						if (fl2 == true) {
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * 奇偶排序float[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void oddEvenSort(float[] list, int left, int right, boolean flag) {
		if (flag) {
			float temp;
			boolean fl = false;
			boolean fl1 = false;
			boolean fl2 = false;
			while (true) {
				if (fl == false) {
					fl = true;
				} else {
					fl = false;
				}
				if (fl == true) {
					fl1 = true;
					
					for (int i = left; i < right; i += 2) {
						if (list[i] < list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl1 = false;
						}
					}
					if (fl2 == true) {
						if (fl1 == true) {
							break;
						}
					}
				} else {
					fl2 = true;
					for (int i = left + 1; i < right; i += 2) {
						if (list[i] < list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl2 = false;
						}
					}
					if (fl1 == true) {
						if (fl2 == true) {
							break;
						}
					}
				}
			}
		} else {
			float temp;
			boolean fl = false;
			boolean fl1 = false;
			boolean fl2 = false;
			while (true) {
				if (fl == false) {
					fl = true;
				} else {
					fl = false;
				}
				if (fl == true) {
					fl1 = true;
					
					for (int i = left; i < right; i += 2) {
						if (list[i] > list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl1 = false;
						}
					}
					if (fl2 == true) {
						if (fl1 == true) {
							break;
						}
					}
				} else {
					fl2 = true;
					for (int i = left + 1; i < right; i += 2) {
						if (list[i] > list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl2 = false;
						}
					}
					if (fl1 == true) {
						if (fl2 == true) {
							break;
						}
					}
				}
			}
		}
	}
	/**
	 * 奇偶排序double[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void oddEvenSort(double[] list, int left, int right, boolean flag) {
		if (flag) {
			double temp;
			boolean fl = false;
			boolean fl1 = false;
			boolean fl2 = false;
			while (true) {
				if (fl == false) {
					fl = true;
				} else {
					fl = false;
				}
				if (fl == true) {
					fl1 = true;
					
					for (int i = left; i < right; i += 2) {
						if (list[i] < list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl1 = false;
						}
					}
					if (fl2 == true) {
						if (fl1 == true) {
							break;
						}
					}
				} else {
					fl2 = true;
					for (int i = left + 1; i < right; i += 2) {
						if (list[i] < list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl2 = false;
						}
					}
					if (fl1 == true) {
						if (fl2 == true) {
							break;
						}
					}
				}
			}
		} else {
			double temp;
			boolean fl = false;
			boolean fl1 = false;
			boolean fl2 = false;
			while (true) {
				if (fl == false) {
					fl = true;
				} else {
					fl = false;
				}
				if (fl == true) {
					fl1 = true;
					
					for (int i = left; i < right; i += 2) {
						if (list[i] > list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl1 = false;
						}
					}
					if (fl2 == true) {
						if (fl1 == true) {
							break;
						}
					}
				} else {
					fl2 = true;
					for (int i = left + 1; i < right; i += 2) {
						if (list[i] > list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl2 = false;
						}
					}
					if (fl1 == true) {
						if (fl2 == true) {
							break;
						}
					}
				}
			}
		}
	}
	/**
	 * 奇偶排序long[]
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void oddEvenSort(long[] list, int left, int right, boolean flag) {
		if (flag) {
			long temp;
			boolean fl = false;
			boolean fl1 = false;
			boolean fl2 = false;
			while (true) {
				if (fl == false) {
					fl = true;
				} else {
					fl = false;
				}
				if (fl == true) {
					fl1 = true;
					
					for (int i = left; i < right; i += 2) {
						if (list[i] < list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl1 = false;
						}
					}
					if (fl2 == true) {
						if (fl1 == true) {
							break;
						}
					}
				} else {
					fl2 = true;
					for (int i = left + 1; i < right; i += 2) {
						if (list[i] < list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl2 = false;
						}
					}
					if (fl1 == true) {
						if (fl2 == true) {
							break;
						}
					}
				}
			}
		} else {
			long temp;
			boolean fl = false;
			boolean fl1 = false;
			boolean fl2 = false;
			while (true) {
				if (fl == false) {
					fl = true;
				} else {
					fl = false;
				}
				if (fl == true) {
					fl1 = true;
					
					for (int i = left; i < right; i += 2) {
						if (list[i] > list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl1 = false;
						}
					}
					if (fl2 == true) {
						if (fl1 == true) {
							break;
						}
					}
				} else {
					fl2 = true;
					for (int i = left + 1; i < right; i += 2) {
						if (list[i] > list[i + 1]) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl2 = false;
						}
					}
					if (fl1 == true) {
						if (fl2 == true) {
							break;
						}
					}
				}
			}
		}
	}
	/**
	 * 奇偶排序T[] 泛型
	 * 
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static <T extends Comparable<? super T>> void oddEvenSort(T[] list, int left, int right, boolean flag) {
		if (flag) {
			T temp;
			boolean fl = false;
			boolean fl1 = false;
			boolean fl2 = false;
			while (true) {
				if (fl == false) {
					fl = true;
				} else {
					fl = false;
				}
				if (fl == true) {
					fl1 = true;
					
					for (int i = left; i < right; i += 2) {
						if (list[i].compareTo(list[i + 1])<0) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl1 = false;
						}
					}
					if (fl2 == true) {
						if (fl1 == true) {
							break;
						}
					}
				} else {
					fl2 = true;
					for (int i = left + 1; i < right; i += 2) {
						if (list[i].compareTo(list[i + 1])<0) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl2 = false;
						}
					}
					if (fl1 == true) {
						if (fl2 == true) {
							break;
						}
					}
				}
			}
		} else {
			T temp;
			boolean fl = false;
			boolean fl1 = false;
			boolean fl2 = false;
			while (true) {
				if (fl == false) {
					fl = true;
				} else {
					fl = false;
				}
				if (fl == true) {
					fl1 = true;
					
					for (int i = left; i < right; i += 2) {
						if (list[i].compareTo(list[i + 1])>0) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl1 = false;
						}
					}
					if (fl2 == true) {
						if (fl1 == true) {
							break;
						}
					}
				} else {
					fl2 = true;
					for (int i = left + 1; i < right; i += 2) {
						if (list[i].compareTo(list[i + 1])>0) {
							temp = list[i];
							list[i] = list[i + 1];
							list[i + 1] = temp;
							fl2 = false;
						}
					}
					if (fl1 == true) {
						if (fl2 == true) {
							break;
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 鸡尾酒排序int[]
	 *双向冒泡排序
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void cocktailSort(int[] list, int left, int right, boolean flag) {
		if (flag) {
			boolean fl2=true;
			boolean fl=false;
			int temp;
			int l=left;
			int r=right;
			while(true)
			{
				fl2=true;
				if(fl==false)
				{
					fl=true;
					for(int i=l;i<r;i++)
					{
						if(list[i]<list[i+1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i+1];
							list[i+1]=temp;
						}
					}
					r--;
				}
				else
				{
					fl=false;
					for(int i=r;i>l+1;i--)
					{
						if(list[i]>list[i-1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i-1];
							list[i-1]=temp;
						}
					}
					l++;
				}
				if(fl2==true)
				{
					break;
				}
			}
		} else {
			boolean fl2=true;
			boolean fl=false;
			int temp;
			int l=left;
			int r=right;
			while(true)
			{
				fl2=true;
				if(fl==false)
				{
					fl=true;
					for(int i=left;i<right;i++)
					{
						if(list[i]>list[i+1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i+1];
							list[i+1]=temp;
						}
					}
					r--;
				}
				else
				{
					fl=false;
					for(int i=right;i>left+1;i--)
					{
						if(list[i]<list[i-1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i-1];
							list[i-1]=temp;
						}
					}
					l++;
				}
				if(fl2==true)
				{
					break;
				}
			}
		}
	}
	/**
	 * 鸡尾酒排序double[]
	 *双向冒泡排序
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void cocktailSort(double[] list, int left, int right, boolean flag) {
		if (flag) {
			boolean fl2=true;
			boolean fl=false;
			double temp;
			int l=left;
			int r=right;
			while(true)
			{
				fl2=true;
				if(fl==false)
				{
					fl=true;
					for(int i=l;i<r;i++)
					{
						if(list[i]<list[i+1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i+1];
							list[i+1]=temp;
						}
					}
					r--;
				}
				else
				{
					fl=false;
					for(int i=r;i>l+1;i--)
					{
						if(list[i]>list[i-1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i-1];
							list[i-1]=temp;
						}
					}
					l++;
				}
				if(fl2==true)
				{
					break;
				}
			}
		} else {
			boolean fl2=true;
			boolean fl=false;
			double temp;
			int l=left;
			int r=right;
			while(true)
			{
				fl2=true;
				if(fl==false)
				{
					fl=true;
					for(int i=left;i<right;i++)
					{
						if(list[i]>list[i+1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i+1];
							list[i+1]=temp;
						}
					}
					r--;
				}
				else
				{
					fl=false;
					for(int i=right;i>left+1;i--)
					{
						if(list[i]<list[i-1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i-1];
							list[i-1]=temp;
						}
					}
					l++;
				}
				if(fl2==true)
				{
					break;
				}
			}
		}
	}
	/**
	 * 鸡尾酒排序float[]
	 *双向冒泡排序
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void cocktailSort(float[] list, int left, int right, boolean flag) {
		if (flag) {
			boolean fl2=true;
			boolean fl=false;
			float temp;
			int l=left;
			int r=right;
			while(true)
			{
				fl2=true;
				if(fl==false)
				{
					fl=true;
					for(int i=l;i<r;i++)
					{
						if(list[i]<list[i+1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i+1];
							list[i+1]=temp;
						}
					}
					r--;
				}
				else
				{
					fl=false;
					for(int i=r;i>l+1;i--)
					{
						if(list[i]>list[i-1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i-1];
							list[i-1]=temp;
						}
					}
					l++;
				}
				if(fl2==true)
				{
					break;
				}
			}
		} else {
			boolean fl2=true;
			boolean fl=false;
			float temp;
			int l=left;
			int r=right;
			while(true)
			{
				fl2=true;
				if(fl==false)
				{
					fl=true;
					for(int i=left;i<right;i++)
					{
						if(list[i]>list[i+1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i+1];
							list[i+1]=temp;
						}
					}
					r--;
				}
				else
				{
					fl=false;
					for(int i=right;i>left+1;i--)
					{
						if(list[i]<list[i-1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i-1];
							list[i-1]=temp;
						}
					}
					l++;
				}
				if(fl2==true)
				{
					break;
				}
			}
		}
	}
	/**
	 * 鸡尾酒排序long[]
	 *双向冒泡排序
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static void cocktailSort(long[] list, int left, int right, boolean flag) {
		if (flag) {
			boolean fl2=true;
			boolean fl=false;
			long temp;
			int l=left;
			int r=right;
			while(true)
			{
				fl2=true;
				if(fl==false)
				{
					fl=true;
					for(int i=l;i<r;i++)
					{
						if(list[i]<list[i+1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i+1];
							list[i+1]=temp;
						}
					}
					r--;
				}
				else
				{
					fl=false;
					for(int i=r;i>l+1;i--)
					{
						if(list[i]>list[i-1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i-1];
							list[i-1]=temp;
						}
					}
					l++;
				}
				if(fl2==true)
				{
					break;
				}
			}
		} else {
			boolean fl2=true;
			boolean fl=false;
			long temp;
			int l=left;
			int r=right;
			while(true)
			{
				fl2=true;
				if(fl==false)
				{
					fl=true;
					for(int i=left;i<right;i++)
					{
						if(list[i]>list[i+1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i+1];
							list[i+1]=temp;
						}
					}
					r--;
				}
				else
				{
					fl=false;
					for(int i=right;i>left+1;i--)
					{
						if(list[i]<list[i-1])
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i-1];
							list[i-1]=temp;
						}
					}
					l++;
				}
				if(fl2==true)
				{
					break;
				}
			}
		}
	}
	/**
	 * 鸡尾酒排序T[] 泛型
	 *双向冒泡排序
	 * @param list为
	 *            list的相关 extend类型
	 * @param left
	 *            左侧 index
	 * @param right
	 *            右侧index
	 * @param flag如果为true则表示
	 *            desc false为 asc（true为大根堆，false为小根堆）
	 */
	public static <T extends Comparable<? super T>> void cocktailSort(T[] list, int left, int right, boolean flag) {
		if (flag) {
			boolean fl2=true;
			boolean fl=false;
			T temp;
			int l=left;
			int r=right;
			while(true)
			{
				fl2=true;
				if(fl==false)
				{
					fl=true;
					for(int i=l;i<r;i++)
					{
						if(list[i].compareTo(list[i+1])<0)
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i+1];
							list[i+1]=temp;
						}
					}
					r--;
				}
				else
				{
					fl=false;
					for(int i=r;i>l+1;i--)
					{
						if(list[i].compareTo(list[i-1])>0)
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i-1];
							list[i-1]=temp;
						}
					}
					l++;
				}
				if(fl2==true)
				{
					break;
				}
			}
		} else {
			boolean fl2=true;
			boolean fl=false;
			T temp;
			int l=left;
			int r=right;
			while(true)
			{
				fl2=true;
				if(fl==false)
				{
					fl=true;
					for(int i=left;i<right;i++)
					{
						if(list[i].compareTo(list[i+1])>0)
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i+1];
							list[i+1]=temp;
						}
					}
					r--;
				}
				else
				{
					fl=false;
					for(int i=right;i>left+1;i--)
					{
						if(list[i].compareTo(list[i-1])<0)
						{
							fl2=false;
							temp=list[i];
							list[i]=list[i-1];
							list[i-1]=temp;
						}
					}
					l++;
				}
				if(fl2==true)
				{
					break;
				}
			}
		}
	}
	public static void main(String[] args) throws ClassNotFoundException {

		// Integer[] t = { 9, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 4, 3, 2, 1, 7, 7, 8,
		// 5, 3 };
		int[] t = { 9, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 4, 3, 2, 1, 7, 7, 8, 5, 3 };
		// 调用插入排序
		// AllSort.insertSort(t, 10, true);
		// for (int i = 0; i < t.length; i++) {
		// System.out.print(t[i] + ":");
		// }

		// 插入排序
		// AllSort.shellSort(t, t.length, 3, true);
		// for (int i = 0; i < t.length; i++) {
		// System.out.print(t[i] + ":");
		// }

		// 选择排序
		// AllSort.selectSort(t, t.length, true);
		// for (int i = 0; i < t.length; i++) {
		// System.out.print(t[i] + ":");
		// }
		// 基数排序
		// AllSort.baseSort(t, t.length,t.length, false);
		// for (int i = 0; i < t.length; i++) {
		// System.out.print(t[i] + ":");
		// }
		// 桶排序
		// AllSort.bucketSort(t, t.length,1,9, true);
		// for (int i = 0; i < t.length; i++) {
		// System.out.print(t[i] + ":");
		// }
		// 快速排序
//		 AllSort.quickSort(t, 6, t.length - 1, true);
//		 for (int i = 0; i < t.length; i++) {
//		 System.out.print(t[i] + ":");
//		 }
		// 归并排序
		// AllSort.mergerSort(t,0,t.length - 1,1, false);
		// for (int i = 0; i < t.length; i++) {
		// System.out.print(t[i] + ":");
		// }
		// 记数排序
		// AllSort.countSort(t,10);
		// for (int i = 0; i < t.length; i++) {
		// System.out.print(t[i] + ":");
		// }
		// 大根堆排序
		 AllSort.heapSort(t,t.length-1,false);
		 for (int i = 0; i < t.length; i++) {
		 System.out.print(t[i] + ":");
		 }
		// 冒泡排序
		// AllSort.bubbleSort(t,0,t.length-1,true);
		// for (int i = 0; i < t.length; i++) {
		// System.out.print(t[i] + ":");
		// }
		// 地精排序
		// AllSort.tkSort(t, 0, t.length-1,true);
		// for (int i = 0; i < t.length; i++) {
		// System.out.print(t[i] + ":");
		// }
		// 奇偶排序
//		AllSort.oddEvenSort(t, 0, t.length-1, true);
//		for (int i = 0; i < t.length; i++) {
//			System.out.print(t[i] + ":");
//		}
		//鸡尾酒排序
//		AllSort.cocktailSort(t, 0, t.length-1, true);
//		for (int i = 0; i < t.length; i++) {
//			System.out.print(t[i] + ":");
//		}
	}
}
