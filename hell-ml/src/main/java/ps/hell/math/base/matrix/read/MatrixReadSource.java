package ps.hell.math.base.matrix.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

import ps.hell.math.base.matrix.Matrix;
/**
 * 读取矩阵包括所有信息放入矩阵中
 * @author Administrator
 *
 */
public class MatrixReadSource{

	
	
	public MatrixReadSource()
	{
		
	}
	public static LinkedList<Double[]> readToLinkedList(String filename)
	{
			File file=new File(filename);
			if(!file.exists())
			{
				System.out.println("文件不存在");
			}
			LinkedList<Double[]> retu=new LinkedList<Double[]>();
			BufferedReader reader = null;
			try{
			 
			 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
	         reader = new BufferedReader(read);
	         String tempString = null;
	         //int line = 1;
	         // 一次读入一行，直到读入null为文件结束
	         while ((tempString = reader.readLine()) != null)
	         {
	        	 if(tempString.equals(""))
	        	 {
	        		 continue;
	        	 }
	        	 String[] ll=tempString.split("\t");
	        	 Double[] str=new Double[ll.length];
	        	 for(int i=0;i<ll.length;i++)
	        	 str[i]=Double.parseDouble(ll[i]);
	        	 retu.add(str);
	         }
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			finally
			{
				if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
			}
	         
		}
			return retu;
	}
	public static ArrayList<double[]> readToListD(String filename)
	{
			File file=new File(filename);
			if(!file.exists())
			{
				System.out.println("文件不存在");
			}
			ArrayList<double[]> retu=new ArrayList<double[]>();
			BufferedReader reader = null;
			try{
			 
			 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
	         reader = new BufferedReader(read);
	         String tempString = null;
	         //int line = 1;
	         // 一次读入一行，直到读入null为文件结束
	         while ((tempString = reader.readLine()) != null)
	         {
	        	 if(tempString.equals(""))
	        	 {
	        		 continue;
	        	 }
	        	 String[] ll=tempString.split("\t");
	        	 double[] str=new double[ll.length];
	        	 for(int i=0;i<ll.length;i++)
	        	 str[i]=Double.parseDouble(ll[i]);
	        	 retu.add(str);
	         }
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			finally
			{
				if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
			}
	         
		}
			return retu;
	}
	
	public static LinkedList<String[]> readToLinkedListString(String filename)
	{
			File file=new File(filename);
			if(!file.exists())
			{
				System.out.println("文件不存在");
			}
			LinkedList<String[]> retu=new LinkedList<String[]>();
			BufferedReader reader = null;
			try{
			 
			 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
	         reader = new BufferedReader(read);
	         String tempString = null;
	         //int line = 1;
	         // 一次读入一行，直到读入null为文件结束
	         while ((tempString = reader.readLine()) != null)
	         {
	        	 if(tempString.equals(""))
	        	 {
	        		 continue;
	        	 }
	        	 String[] ll=tempString.split("\t");
	        	 retu.add(ll);
	         }
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			finally
			{
				if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
			}
	         
		}
			return retu;
	}
	
	public static LinkedList<String[]> readToLinkedListStringGBK(String filename)
	{
			File file=new File(filename);
			if(!file.exists())
			{
				System.out.println("文件不存在");
			}
			LinkedList<String[]> retu=new LinkedList<String[]>();
			BufferedReader reader = null;
			try{
			 
			 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"gbk");  
	         reader = new BufferedReader(read);
	         String tempString = null;
	         //int line = 1;
	         // 一次读入一行，直到读入null为文件结束
	         while ((tempString = reader.readLine()) != null)
	         {
	        	 if(tempString.equals(""))
	        	 {
	        		 continue;
	        	 }
	        	 String[] ll=tempString.split("\t");
	        	 retu.add(ll);
	         }
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			finally
			{
				if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
			}
	         
		}
			return retu;
	}
	/**
	 * 
	 * @param filename
	 * @param Regex 分隔符
	 * @return
	 */
	public static LinkedList<String[]> readToLinkedListString(String filename,String Regex)
	{
			File file=new File(filename);
			if(!file.exists())
			{
				System.out.println("文件不存在");
			}
			LinkedList<String[]> retu=new LinkedList<String[]>();
			BufferedReader reader = null;
			try{
			 
			 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
	         reader = new BufferedReader(read);
	         String tempString = null;
	         //int line = 1;
	         // 一次读入一行，直到读入null为文件结束
	         while ((tempString = reader.readLine()) != null)
	         {
	        	 if(tempString.equals(""))
	        	 {
	        		 continue;
	        	 }
	        	 //System.out.println(tempString);
	        	 String[] ll=tempString.split(Regex);
	        	 retu.add(ll);
	         }
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			finally
			{
				if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
			}
	         
		}
			return retu;
	}
	
	
	
	public static Double[][] readToDouble(String filename)
	{
			File file=new File(filename);
			if(!file.exists())
			{
				System.out.println("文件不存在");
			}
			Double[][] retu=null;
			BufferedReader reader = null;
			try{
			 
			 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
	         reader = new BufferedReader(read);
	         String tempString = null;
	         //int line = 1;
	         // 一次读入一行，直到读入null为文件结束
	         int i=0;
	         int j=0;
	         while ((tempString = reader.readLine()) != null)
	         {
	        	 if(tempString.equals(""))
	        	 {
	        		 continue;
	        	 }
	        	 i++;
	        	 if(i>1)
	        	 {
	        		 continue;
	        	 }
	        	 String[] ll=tempString.split("\t");
	        	 j=ll.length;
	         }
	         retu=new Double[i][j];
	         
	         i=0;
	         while ((tempString = reader.readLine()) != null)
	         {
	        	 if(tempString.equals(""))
	        	 {
	        		 continue;
	        	 }
	        	
	        	 String[] ll=tempString.split("\t");
	        	 Double[] str=new Double[ll.length];
	        	 for(int ii=0;ii<ll.length;ii++)
	        		 retu[i][ii]=Double.parseDouble(ll[ii]);
	        	 i++;
	         }
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			finally
			{
				if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
			}
	         
		}
			return retu;
	}
	
	
	public static Matrix readToMatrix(String filename)
	{
			File file=new File(filename);
			if(!file.exists())
			{
				System.out.println("文件不存在");
			}
			Matrix retu=null;
			BufferedReader reader = null;
			try{
			 
			 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");  
	         reader = new BufferedReader(read);
	         String tempString = null;
	         //int line = 1;
	         // 一次读入一行，直到读入null为文件结束
	         int i=0;
	         int j=0;
	         while ((tempString = reader.readLine()) != null)
	         {
	        	 if(tempString.equals(""))
	        	 {
	        		 continue;
	        	 }
	        	 i++;
	        	 if(i>1)
	        	 {
	        		 continue;
	        	 }
	        	 String[] ll=tempString.split("\t");
	        	 j=ll.length;
	         }
	         retu=new Matrix(i,j);
	         
	         i=0;
	         while ((tempString = reader.readLine()) != null)
	         {
	        	 if(tempString.equals(""))
	        	 {
	        		 continue;
	        	 }
	        	
	        	 String[] ll=tempString.split("\t");
	        	 Float[] str=new Float[ll.length];
	        	 for(int ii=0;ii<ll.length;ii++)
	        		 retu.getData()[i][ii]=Float.parseFloat(ll[ii]);
	        	 i++;
	         }
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			finally
			{
				if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
			}
	         
		}
			return retu;
	}
}
