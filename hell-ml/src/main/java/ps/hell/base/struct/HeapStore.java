package ps.hell.base.struct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * 小根对存储结构 用于存储key value 其中value为int数值
 * @author Administrator
 *
 */
public class HeapStore {

	public ArrayList<String> keys=new ArrayList<String>();
	public ArrayList<Integer> values=new ArrayList<Integer>();
	private Long size=0L;
	private Object obj1=new Object();
	public HeapStore()
	{
		
	}
	public void firstOne(String key,int value)
	{
		keys.add(key);
		values.add(value);
		size++;
	}
	public String poolKey()
	{
		synchronized (obj1) {
			return keys.remove(keys.size()-1);
		}
		
	}
	public Integer poolValue()
	{
		synchronized (obj1) {
			return values.remove(values.size()-1);
		}
		
	}
	/**
	 *调节存储结构//其中表中一定要有至少1条数据
	 *小根堆
	 */
	public void add(String key,int value)
	{
		synchronized (obj1) {
			if(keys.size()<5)
			{
				boolean flag=false;
				for(int i=0;i<keys.size();i++)
				{
					if(keys.get(i).compareTo(key)==0)
					{
						values.set(i, values.get(i)+value);
					}
					else if(keys.get(i).compareTo(key)>0)
					{
						keys.add(i, key);
						values.add(i,value);
						size++;
						flag=true;
						break;
					}
					
				}
				if(flag==false)
				{
					keys.add(key);
					values.add(value);
					size++;
				}
				return;
			}
			//文件查询
			int file=keys.size()/2;
			int fileLeft=0;
			int fileRight=keys.size()-1;
			while(true)
			{
				//System.out.println(key+"\t"+value+"\tfile:"+fileLeft+"\t"+file+"\t"+fileRight);
				try
				{
				if(keys.get(file).compareTo(key)>0)
				{//key小
					if(file-1==fileLeft)
					{
						if(keys.get(fileLeft).compareTo(key)<0)
						{
							//如果比早的一个的一个还大则添加到最后
							keys.add(file,key);
							values.add(file,value);
							size++;
						}
						else if(keys.get(fileLeft).compareTo(key)>0)
						{//如果比早的一个小则添加在倒数第一个位置
							keys.add(fileLeft,key);
							values.add(fileLeft,value);
							size++;
							
						}
						else
						{
							values.set(fileLeft, values.get(fileLeft)+value);
						}
						break;
						
					}
					fileRight=file;
					file=(file+fileLeft)/2;
					//切半
				}
				else if(keys.get(file).compareTo(key)<0)
				{
					//key大
					if(file+1==fileRight)
					{
						if(keys.get(fileRight).compareTo(key)<0)
						{
							//如果比最后的一个还大则添加到最后
							keys.add(fileRight+1,key);
							values.add(fileRight+1,value);
							size++;
						}
						else if(keys.get(fileRight).compareTo(key)>0)
						{
							//如果比最后一个小则添加在倒数第一个位置
							keys.add(fileRight,key);
							values.add(fileRight,value);
							size++;
						}
						else
						{
							values.set(fileRight, values.get(fileRight)+value);
						}
						break;
						
					}
					fileLeft=file;
					file=(file+fileRight)/2;
				}
				else
				{
					this.values.set(file, values.get(file)+value);
					break;
				}
				}
				catch(Exception e)
				{
					System.out.println(fileLeft+"\t"+file+"\t"+fileRight+"\t"+keys.size());
					//e.printStackTrace();
					break;
				}
			}
		}
		
	}
	
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	/**
	 * 打印函数
	 */
	public void print()
	{
		synchronized (obj1) {
			for(int i=0;i<keys.size();i++)
			{
				System.out.println(keys.get(i)+"\t"+values.get(i));
			}
		}
		
	}
	public static void main(String[] args) throws IOException, InterruptedException {
		//用随机数测试效率
		String[] word={"a","b","c",""};
		HeapStore heap=new HeapStore();
//		heap.firstOne("a",2);
//		heap.print();
//		String[] key={"b","bc","b","c","K","d","F","ec","z","with","c","b","abc","ec","a","abc","with","with"};
//		int[] value={1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
//		for(int i=0;i<key.length;i++)
//		{
//			heap.add(key[i],value[i]);
//			System.out.println(key[i]+"------------------");
//			heap.print();
//		}
//		System.out.println("end");
//		heap.print();
		
		ArrayList<String> keys=new ArrayList<String>();
		ArrayList<Integer> values=new ArrayList<Integer>();
		
		File file1=new File("src/com/ml/participle/mmseg/衙内当官.txt");
		System.out.println(file1.getAbsolutePath());
		BufferedReader reader=null;
		try{
			InputStreamReader readStream=new InputStreamReader(new FileInputStream(file1),"gbk");
			reader=new BufferedReader(readStream);
			String temp=null;
			while((temp=reader.readLine())!=null){
				if(!temp.isEmpty())
				{
					temp.replace("."," ");
					temp.replace("—", "");
					temp.replace("-","");
					String[] temp1=temp.split("[\\s-_——-——\\|【】：“”！；（\\[\\]\\)\\(\\/\\.。，？?））《》]");
					for(String str:temp1)
					{
						keys.add(str);
						values.add(1);
					}
					
				}
			}
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			reader.close();
		}
	
		
//		heap.firstOne("a",2);
		//heap.print();	
		for(int i=0;i<keys.size();i++)
		{
			if(i%1000==0 &&i!=0)
			{
				
				System.out.println("测试:"+i+"\t"+keys.get(i)+"\t"+keys.size()+"\t"+heap.keys.size());
				break;
			
			}
			//System.out.println("测试:"+i+"\t"+keys.get(i)+"\t"+heap.keys.size());
			heap.add(keys.get(i).toLowerCase().trim(),values.get(i));
			
			//heap.print();
			//Thread.sleep(200);
		}
		heap.print();
		System.out.println(heap.getSize());
		
		
	}
}
