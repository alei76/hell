package ps.hell.math.base.struct;

import java.util.LinkedList;

/**
 * 排列组合无序全排序
 * @author Administrator
 *
 */
public class PernSort {
	
	private int startNum;
	private int endNum;
	public LinkedList<String[]> output=new LinkedList<String[]>();
	
	/**
	 * 所有的排列组合有序
	 * @param start
	 * @param end
	 * @param data
	 * @return
	 */
	 public LinkedList<String[]> pernAllS(int start,int end,LinkedList<String> data)
	 {
		 startNum=start;
		 endNum=end;
		// System.out.println("数据");
		 String[] data1=new String[data.size()];
		 for(int i=0;i<data.size();i++)
		 {
			// System.out.print(data.get(i)+":");
			 data1[i]=data.get(i);
		 }
		 PernSort ne=new PernSort();
		//取所有的无序全排
		 LinkedList<String[]> nl=pernAll(2,data1.length,data1);
		 for(String[] nl2:nl)
		 {
			 ne.pernAlls(nl2, 0, nl2.length-1);
		 }
		 return ne.output;
	 }
	
	
	
	
	
	
	/**
	 * 定长全排序有序全排序有序
	 * @param n
	 * @param n2
	 */
	 public void pernAlls(char[] buf,int start,int end)
	{
		 startNum=start;
		 endNum=end;
		 if(start==end)
		 {
			 //当只要求对数组中一个字母进行全排列时，只要就按该数组输出即可  
		    for(int i=0;i<=end;i++)
		    {  
		    	 System.out.print(buf[i]);  
		     }  
		       System.out.println();     
		  }  
		   else{//多个字母全排列  
		          for(int i=start;i<=end;i++)
		          {  
		                char temp=buf[start];//交换数组第一个元素与后续的元素  
		                buf[start]=buf[i];  
		                buf[i]=temp;  
		                pernAlls(buf,start+1,end);//后续元素递归全排列     
		                temp=buf[start];//将交换后的数组还原  
		                buf[start]=buf[i];  
		                buf[i]=temp;  
		            }  
		   }  
	}  
	 /**
		 * 定长全排序有序
		 * @param n
		 * @param n2
		 */
		 public void pernAlls(String[] buf,int start,int end)
		{
			 startNum=start;
			 endNum=end;
			 if(start==end)
			 {
				 //当只要求对数组中一个字母进行全排列时，只要就按该数组输出即可  
				 String[] ne=new String[end+1];
			    for(int i=0;i<=end;i++)
			    { 
			    	// System.out.print(buf[i]); 
			    	ne[i]=buf[i];
			    	
			     }
			      // System.out.println();
			    output.add(ne);
			  }  
			   else{//多个字母全排列  
			          for(int i=start;i<=end;i++)
			          {  
			                String temp=buf[start];//交换数组第一个元素与后续的元素  
			                buf[start]=buf[i];  
			                buf[i]=temp;  
			                pernAlls(buf,start+1,end);//后续元素递归全排列     
			                temp=buf[start];//将交换后的数组还原  
			                buf[start]=buf[i];  
			                buf[i]=temp;  
			            }  
			   }  
		}  
	 
	/**
	 * 全排列无序
	 * @param start 取下限从1开始
	 * @param end 取上限
	 * @param data 输入数据
	 * @return 全排列无序
	 */
	 public LinkedList<String[]> pernAll(int start,int end,String[] data)
	 {
		 startNum=start;
		 endNum=end;
		 System.out.println("数据");
		 String[] data1=new String[data.length];
		 for(int i=0;i<data.length;i++)
		 {
			 System.out.print(data[i]+":");
			 data1[i]=data[i];
		 }
		 
		 System.out.println();
		 LinkedList<String[]> output=new LinkedList<String[]>();
		 for(int i=start;i<=end;i++)
		 {
			 select(i,data1,output);
		 }
		 return output;
	 }
	 public LinkedList<String[]> pernAll2(int start,int end,LinkedList<String> data)
	 {
		 startNum=start;
		 endNum=end;
		// System.out.println("数据");
		 String[] data1=new String[data.size()];
		 for(int i=0;i<data.size();i++)
		 {
			// System.out.print(data.get(i)+":");
			 data1[i]=data.get(i);
		 }
		 PernSort ne=new PernSort();
		//取所有的无序全排
		 LinkedList<String[]> nl=pernAll(2,data1.length,data1);
		 for(String[] nl2:nl)
		 {
			 ne.pernAlls(nl2, 0, nl2.length-1);
		 }
		 return ne.output;
	 }
	 
	 
	 public LinkedList<String[]> pernAll(int start,int end,LinkedList<String> data)
	 {
		 startNum=start;
		 endNum=end;
		 System.out.println("数据");
		 String[] data1=new String[data.size()];
		 for(int i=0;i<data.size();i++)
		 {
			 System.out.print(data.get(i)+":");
			 data1[i]=data.get(i);
		 }
		 
		 System.out.println();
		 LinkedList<String[]> output=new LinkedList<String[]>();
		 for(int i=start;i<=end;i++)
		 {
			 select(i,data1,output);
		 }
		 return output;
	 }
	 /**
	  * 全排序调用
	  * @param k 取的长度
	  * @param data 源数据
	  * @param output 输入
	  */
		private void select(int k,String[] data,LinkedList<String[]> output) { 
			   String[] result = new String[k]; 
			   subselect(0, 1,k, result,data, output); 

			} 
		
		/**
		 * 
		 * @param head 起始
		 * @param index 索引
		 * @param k 最终长度
		 * @param temp 临时表空
		 * @param data 输入数据
		 * @param output 输出
		 */
		private void subselect(int head, int index, int k ,String[] temp,String[] data,LinkedList<String[]> output) 
		{ 
			 for (int i = head; i < data.length + index - k; i++) 
			   { 
				    if (index < k) 
				    {
				    	
				    	temp[index - 1] = data[i]; 
				     //System.out.println("i="+(i)+";index="+(index)); 
				     subselect(i + 1, index + 1,k, temp,data, output); 
				     
				    } 
				    else if (index == k) 
				    { 
				    	temp[index - 1] = data[i]; 
				    // System.out.println(";i="+(i)+";index="+(index)+";index==k:"+(index==k)); 
				    // System.out.print(i+"==="); 
				    // System.out.println(r); 
//				     for(String ln:r)
//				     {
//				    	 System.out.print("[]"+ln+"[]");
//				     }
//				     System.out.println();
				     output.add(temp.clone());
				     subselect(i + 1, index + 1, k, temp,data,output); 
				    }
				    else 
				    { 
				    // System.out.println("++"); 
				     return;//返回到何处？奇怪 
				    } 

			   } 
			} 
	 
public static void main(String[] args) {
	PernSort pern=new PernSort();
	String[] ne={"a","e","c","d"};
	pern.pernAlls(ne,0,3);
	LinkedList<String[]> nez=pern.output;
	for(String[] ll:nez)
	{
		for(String ll2:ll)
		{
			System.out.print(ll2+"\t");
	
		}
		System.out.println();
	}
}
}
