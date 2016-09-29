package ps.hell.ml.revaluate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import ps.hell.util.JsonUtil;

/**
 * 信息熵工具类
 * @author Administrator
 *
 */
public class EntropyUtil<T> {

	public static double log2=0.6931471805599453f;
	/**
	 * 获取信息熵值
	 * value 对应实际值
	 * @param value
	 * @return
	 */
	public double entry(double[] value){
		double entry=0f;
		for(int i=0;i<value.length;i++)
		{
			entry+=value[i]*Math.log(value[i])/log2;
		}
		return -entry;
	}

	/**
	 * 从列表中统计数据并获取信息熵
	 * @param valueList
	 * @return
	 */
	public double entry(List<Comparable<T>> valueList){
		//统计各个值的数量
		double sum=0;
		HashMap<Comparable<T>,Integer> counter=getCounter(valueList);
		sum=valueList.size();
		double[] value=new double[counter.size()];
		int index=-1;
		for(Entry<Comparable<T>,Integer> map:counter.entrySet()){
			index++;
			value[index]=map.getValue()/sum;
		}
		return entry(value);
	}
	/**
	 * 统计列表中各类的数量
	 * @param valueList
	 * @return
	 */
	public HashMap<Comparable<T>,Integer> getCounter(List<Comparable<T>> valueList){
		HashMap<Comparable<T>,Integer> counter=new HashMap<Comparable<T>,Integer>();
		for(Comparable<T> v:valueList){
			if(counter.containsKey(v)){
				counter.put(v,counter.get(v)+1);
			}else{
				counter.put(v,1);
			}
		}
		return counter;
		
	}
	
	/**
	 * 统计列表中各类的数量
	 * @param valueList
	 * @return
	 */
	public HashMap<Comparable<T>,Integer> getCounter(Comparable<T>[] valueList){
		HashMap<Comparable<T>,Integer> counter=new HashMap<Comparable<T>,Integer>();
		for(Comparable<T> v:valueList){
			if(counter.containsKey(v)){
				counter.put(v,counter.get(v)+1);
			}else{
				counter.put(v,1);
			}
		}
		return counter;
		
	}
	/**
	 * 从列表中统计数据并获取信息熵
	 * @param valueList
	 * @return
	 */
	public double entry(Comparable<T>[] valueList){
		//统计各个值的数量
		double sum=0;
		HashMap<Comparable<T>,Integer> counter=getCounter(valueList);
		sum=valueList.length;
		double[] value=new double[counter.size()];
		int index=-1;
		for(Entry<Comparable<T>,Integer> map:counter.entrySet()){
			index++;
			value[index]=map.getValue()/sum;
		}
		return entry(value);
	}
	
	
	/**
	 * 统计列表中各类的数量
	 * @param valueList
	 * @return
	 */
	public HashMap<Integer,Integer> getCounter(int[] valueList){
		HashMap<Integer,Integer> counter=new HashMap<Integer,Integer>();
		for(int v:valueList){
			if(counter.containsKey(v)){
				counter.put(v,counter.get(v)+1);
			}else{
				counter.put(v,1);
			}
		}
		return counter;
		
	}
	/**
	 * 从列表中统计数据并获取信息熵
	 * @param valueList
	 * @return
	 */
	public double entry(int[] valueList){
		//统计各个值的数量
		double sum=0;
		HashMap<Integer,Integer> counter=getCounter(valueList);
		sum=valueList.length;
		double[] value=new double[counter.size()];
		int index=-1;
		for(Entry<Integer,Integer> map:counter.entrySet()){
			index++;
			value[index]=map.getValue()/sum;
		}
		//System.out.println(Arrays.toString(value));
		return entry(value);
	}
	
	/**
	 * 统计列表中各类的数量
	 * @param valueList
	 * @return
	 */
	public HashMap<Long,Integer> getCounter(long[] valueList){
		HashMap<Long,Integer> counter=new HashMap<Long,Integer>();
		for(long v:valueList){
			if(counter.containsKey(v)){
				counter.put(v,counter.get(v)+1);
			}else{
				counter.put(v,1);
			}
		}
		return counter;
		
	}
	/**
	 * 从列表中统计数据并获取信息熵
	 * @param valueList
	 * @return
	 */
	public double entry(long[] valueList){
		//统计各个值的数量
		double sum=0;
		HashMap<Long,Integer> counter=getCounter(valueList);
		sum=valueList.length;
		double[] value=new double[counter.size()];
		int index=-1;
		for(Entry<Long,Integer> map:counter.entrySet()){
			index++;
			value[index]=map.getValue()/sum;
		}
		return entry(value);
	}
	
	/**
	 * 统计列表中各类的数量
	 * @param valueList
	 * @return
	 */
	public HashMap<Character,Integer> getCounter(char[] valueList){
		HashMap<Character,Integer> counter=new HashMap<Character,Integer>();
		for(char v:valueList){
			if(counter.containsKey(v)){
				counter.put(v,counter.get(v)+1);
			}else{
				counter.put(v,1);
			}
		}
		return counter;
		
	}
	/**
	 * 从列表中统计数据并获取信息熵
	 * @param valueList
	 * @return
	 */
	public double entry(char[] valueList){
		//统计各个值的数量
		double sum=0;
		HashMap<Character,Integer> counter=getCounter(valueList);
		sum=valueList.length;
		double[] value=new double[counter.size()];
		int index=-1;
		for(Entry<Character,Integer> map:counter.entrySet()){
			index++;
			value[index]=map.getValue()/sum;
		}
		return entry(value);
	}
	/**
	 * kl散度 也叫相对熵
	 * ，是描述两个概率分布P和Q差异的一种方法
	 * value2 value1 都应该有值并>0;
	 * @return
	 */
	public double kLD(double[] value1,double[] value2){
		double re=0f;
		for(int i=0;i<value1.length;i++){
			re+=value1[i]*Math.log(value1[i]/value2[i])/this.log2;
		}
		return re;
	}
	/**
	 * 获取交叉熵
	 * @return
	 */
	public double crossEntropy2(double[] value1,double[] value2){
		double re=0f;
		for(int i=0;i<value1.length;i++){
			re+=value1[i]*Math.log(value2[i])/this.log2;
		}
		return re;
	}
	
	/**
	 * @param x 预测值
	 * @param y评估值--通常评估值为0,1sigmod的解码值
	 * @return
	 *  交叉熵 
	 * 主要用在 sigmod函数 用来评估误差值
	 *	l(x,y)=-sum(xi*log(yi)+(1-xi)*log(1-yi))
	 */
	public double crossEntropy(double[] x,double[] y){
		double re=0;
		for(int i=0;i<x.length;i++){
			re+=x[i]*Math.log(y[i])/this.log2+(1-x[i])*Math.log(1-y[i])/this.log2;
		}
		return re;
	}
	/**
	 * 条件熵
	 * H(x/y)=-sum_i{sum_j{p(xi,yj)*log(p(xi/yj))}}
	 * @param value1
	 * @param value2
	 * @return
	 */
	public double conditionalEntropyXY(double[][] xy,double[][] x_div_y){
		double val=0f;
		for(int i=0;i<xy.length;i++){
			for(int j=0;j<xy[i].length;j++){
				val+=xy[i][j]*Math.log(x_div_y[i][j])/this.log2;
			}
		}
		return -val;
	}
	/**
	 * 互信息
	 * @return
	 */
	public double mutualInformation(double[] x,double[] y,double[][] xy){
		double val=0f;
		for(int i=0;i<x.length;i++){
			for(int j=0;j<y.length;j++){
				val+=xy[i][j]*Math.log((xy[i][j])/x[i]/y[j])/this.log2;
			}
		}
		return val;
	}
	
	public static void main(String[] args) {
		int[] list=new int[]{1,2,3,4,3,2,3,2,31,3,23,4,3,2,1,1,1,2,3,4,3};
		EntropyUtil entry=new EntropyUtil();
		System.out.println(entry.entry(list));
		HashMap<Integer,Integer> map=entry.getCounter(list);
		System.out.println(JsonUtil.getJsonStr(map));
		//获取相对熵
		double[] value1=new double[]{0.2f,0.2f,0.4f};
		double[] value2=new double[]{0.1f,0.3f,0.24f};
		System.out.println(entry.kLD(value1, value2));
	}
}
