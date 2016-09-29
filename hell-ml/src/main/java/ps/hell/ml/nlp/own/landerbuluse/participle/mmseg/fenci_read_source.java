package ps.hell.ml.nlp.own.landerbuluse.participle.mmseg;

import java.io.*;
import java.util.LinkedList;

public class fenci_read_source {
	
	private String filename;
	//专门存储词频数据的链表树
	//获取目录下的所有文件名
	public LinkedList<String> read_director(String dire)
	{
		LinkedList<String> ll=new LinkedList<String>();
		File file=new File(dire);
		if(file.isDirectory())
		{
			File[] l=file.listFiles();
			for(int i=0;i<l.length;i++)
			{
				if(l[i].isFile()&&l[i].toString().substring(l[i].toString().lastIndexOf(".")+1).compareTo("txt")==0)
				{
					ll.add(l[i].toString());
				}
			}
		}
		return ll;
	}
	public LinkedList<String> read_director_director(String dire)
	{
		LinkedList<String> ll=new LinkedList<String>();
		File file=new File(dire);
		if(file.isDirectory())
		{
			File[] l=file.listFiles();
			for(int i=0;i<l.length;i++)
			{
				if(l[i].isDirectory())
				{
					ll.add(l[i].toString());
				}
			}
		}
		return ll;
	}
	
	public fenci_read_source()
	{
		
	}
	public void writefilename(String fileName)
	{
		this.filename=fileName;
	}
	private struct tre=new struct();
	public void writeFile( String fileContent)   
	{     
	    try   
	    {      
	        File f = new File(this.filename);      
	        if (!f.exists())   
	        {       
	            f.createNewFile();      
	        }      
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f,true),"utf-8");      
	        BufferedWriter writer=new BufferedWriter(write);          
	      //  writer.write();
	       
	        writer.write(fileContent);
	        writer.flush();
	            
	    } catch (Exception e)   
	    {      
	        e.printStackTrace();     
	    }  
	} 
	public struct read_source(String filename,int cla)
	{
		File file1=new File(filename);
		if(!file1.exists())
		{//读取词语 和使用频率source
			if(cla==1)
			System.out.println("word sequency error");
			return this.tre;
		}
		BufferedReader reader=null;
		try{
		InputStreamReader read_stream=new InputStreamReader(new FileInputStream(file1),"utf-8");
	
		reader=new BufferedReader(read_stream);
		String temp=null;
		int ll=0;
		while((temp=reader.readLine())!=null){
			if(ll>=0)
			{
			String[] str=temp.split("\t");
			this.tre.insert(str[1],str[4]);
			}
			if(ll>200000)
				break;
			ll++;
			if(ll%1000==0)
			{
				System.out.println(ll+":读入");
			}
			
		}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally {
			
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                }
                }
         
            }
		System.out.println("词频数据读取完成");
		   return this.tre;
		
	}
	public struct read_souce_word_seq(String filename,int cla)
	{
		File file1=new File(filename);
		if(!file1.exists())
		{//读取词语 和使用频率source
			if(cla==1)
			System.out.println("word sequency error");
			return this.tre;
		}
		BufferedReader reader=null;
		try{
		InputStreamReader read_stream=new InputStreamReader(new FileInputStream(file1),"utf-8");
	
		reader=new BufferedReader(read_stream);
		String temp=null;
		int ll=0;
		while((temp=reader.readLine())!=null){
			String[] str=temp.split("\t");
			this.tre.insert(str[0],str[1]);
			ll++;
			if(ll%1000==0)
			{
				System.out.println(ll+":读入");
			}
			
		}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally {
			
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                }
                }
         
            }
		System.out.println("词频数据读取完成");
		   return this.tre;
		
	}
	public LinkedList<String> read(String filename,String code)
	{
		LinkedList<String> str=new LinkedList<String>();
		File file1=new File(filename);
		if(!file1.exists())
		{//读取词语 和使用频率source
			//if(cla==1)
			//System.out.println("word sequency error");
			return str;
		}
		BufferedReader reader=null;
		try{
		InputStreamReader read_stream=new InputStreamReader(new FileInputStream(file1),code);
	
		reader=new BufferedReader(read_stream);
		String temp=null;
		int ll=0;
		while((temp=reader.readLine())!=null){
			str.add(temp);
			
		}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally {
			
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                }
                }
         
            }
		System.out.println("词频数据读取完成");
		   return str;
	}
	public int clear(String filename,String code)//清空数据
	{
		try   
	    {      
	        File f = new File(filename);      
	        if (!f.exists())   
	        {       
	            System.out.println("拷贝失败"); 
	            return 0;
	        }      
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),code);      
	        BufferedWriter writer=new BufferedWriter(write);          
	      //  writer.write();
	       
	        writer.write("");
	        writer.flush();
	            
	    } catch (Exception e)   
	    {      
	        e.printStackTrace(); 
	        return 0;
	    } 
		return 1;
	}
	public int f_copy_f(String filename,String filename1,String code)//拷贝数据
	{
		File file1=new File(filename);
		if(!file1.exists())
		{//读取词语 和使用频率source
			System.out.println("文件1不存在");
			return 0;
		}
		File file2=new File(filename1);
		if(!file2.exists())
		{//读取词语 和使用频率source
			System.out.println("文件2不存在");
			return 0;
		}
		BufferedReader reader=null;
		try{
		InputStreamReader read_stream=new InputStreamReader(new FileInputStream(file1),code);
		reader=new BufferedReader(read_stream);
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file2,true),code);      
	    BufferedWriter writer=new BufferedWriter(write);      
		
		String temp=null;
		int ll=0;
		while((temp=reader.readLine())!=null){
			ll++;
			if(ll%10000==0)
			{
				writer.flush();
				System.out.println("拷贝了:"+ll+"行");
			}
			writer.write(temp+"\r\n");
			
		}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return 0;
		}
		finally {
			
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                }
                }
         
            }
		System.out.println("数据拷贝完成");
		   return 1;
	}
	public struct read_f_copy_f(String filename,String code)
	{
		File file1=new File(filename);
		if(!file1.exists())
		{//读取词语 和使用频率source
			System.out.println("word sequency error");
			return this.tre;
		}
		BufferedReader reader=null;
		try{
		InputStreamReader read_stream=new InputStreamReader(new FileInputStream(file1),code);
	
		reader=new BufferedReader(read_stream);
		String temp=null;
		int ll=0;
		while((temp=reader.readLine())!=null){
			if(ll>=0)
			{
			String[] str=temp.split("\t");
			this.tre.insert(str[0],str[1]);
			}
			ll++;
			if(ll%1000==0)
			{
				System.out.println(ll+":读入");
			}
			
		}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally {
			
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                }
                }
         
            }
		System.out.println("词频数据读取完成");
		   return this.tre;
		
	}
	public struct read_f_copy_f_to_linkedlist(String filename,String code)
	{
		struct lll=new struct();	
		File file1=new File(filename);
		if(!file1.exists())
		{//读取词语 和使用频率source
			System.out.println("word sequency error");
			return lll;
		}
		BufferedReader reader=null;
		try{
		InputStreamReader read_stream=new InputStreamReader(new FileInputStream(file1),code);
	
		reader=new BufferedReader(read_stream);
		String temp=null;
		int ll=0;
		while((temp=reader.readLine())!=null){
			String[] str=temp.split("\t");
			if(str[0].length()==2 && Integer.parseInt(str[1])>9)
			{
				lll.insert(str[0],str[1]);
				ll++;
				if(ll%1000==0)
				{
					System.out.println(ll+":读入");
				}
				continue;
			}
			if(str[0].length()==3 && Integer.parseInt(str[1])>5)
			{
				lll.insert(str[0],str[1]);
				ll++;
				if(ll%1000==0)
				{
					System.out.println(ll+":读入");
				}
				continue;
			}
			if(str[0].length()==4 && Integer.parseInt(str[1])>2)
			{
				lll.insert(str[0],str[1]);
				ll++;
				if(ll%1000==0)
				{
					System.out.println(ll+":读入");
				}
				continue;
			}
			ll++;
			if(ll%1000==0)
			{
				System.out.println(ll+":读入");
			}
		}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally {
			
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                }
                }
         
            }
		System.out.println("词频数据读取完成");
		   return lll;
		
	}
}
