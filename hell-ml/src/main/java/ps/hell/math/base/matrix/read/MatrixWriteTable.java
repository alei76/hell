package ps.hell.math.base.matrix.read;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MatrixWriteTable{
	private static OutputStreamWriter  write;
	private static BufferedWriter writer;
	
	public MatrixWriteTable()
	{
	
	}
	public static void writeToTxt(String fileName,ArrayList<Double[]> matrix)
	{
		if(matrix.size()<1)
		{
			System.out.println("输入矩阵有误:size:"+matrix.size());
			return;
		}
	  try   
	    {      
	        File f = new File(fileName);      
	        if (!f.exists())   
	        {       
	            f.createNewFile();      
	        }      
	        MatrixWriteTable.write =new OutputStreamWriter(new FileOutputStream(f,true),"utf-8");      
	        MatrixWriteTable.writer=new BufferedWriter(MatrixWriteTable.write);          

	        for(Double[] dol:matrix)
	        {
	        	for(int j=0;j<dol.length;j++)
	        	{
	        		if(j==dol.length-1)
	        		{
	        			writer.write(Double.toString(dol[j])+"\r\n");
	        		}
	        		writer.write(Double.toString(dol[j])+"\t");
	        	}
	        }
	        writer.close();
	            
	    } catch (Exception e)   
	    {      
	        e.printStackTrace();     
	    }  
	    finally
	    {
	    	
	    }
	}
	public static void writeToTxt(String fileName,Double[][] matrix)
	{
		if(matrix.length<1)
		{
			System.out.println("输入矩阵有误:size:"+matrix.length);
			return;
		}
	  try   
	    {      
	        File f = new File(fileName);      
	        if (!f.exists())   
	        {       
	            f.createNewFile();      
	        }      
	        MatrixWriteTable.write =new OutputStreamWriter(new FileOutputStream(f,true),"utf-8");      
	        MatrixWriteTable.writer=new BufferedWriter(MatrixWriteTable.write);          

	        for(int i=0;i<matrix.length;i++)
	        {
	        	for(int j=0;j<matrix[0].length;j++)
	        	{
	        		if(j==matrix[0].length-1)
	        		{
	        			writer.write(Double.toString(matrix[i][j])+"\r\n");
	        		}
	        		writer.write(Double.toString(matrix[i][j])+"\t");
	        	}
	        }
	        writer.close();
	            
	    } catch (Exception e)   
	    {      
	        e.printStackTrace();     
	    }  
	    finally
	    {
	    	
	    }
	}
	public static void writeToTxt(String fileName,Double[][] matrix,Double[] output)
	{
		if(matrix.length<1 || output.length<1)
		{
			System.out.println("输入矩阵有误:size:"+matrix.length);
			return;
		}
		if(matrix.length!=output.length)
		{
			System.out.println("输入长度不同:input:"+matrix.length+"\toutput:"+output.length);
		}
	  try   
	    {      
	        File f = new File(fileName);      
	        if (!f.exists())   
	        {       
	            f.createNewFile();      
	        }      
	        MatrixWriteTable.write =new OutputStreamWriter(new FileOutputStream(f,true),"utf-8");      
	        MatrixWriteTable.writer=new BufferedWriter(MatrixWriteTable.write);
	        for(int i=0;i<matrix.length;i++)
	        {
	        	for(int j=0;j<matrix[0].length;j++)
	        	{
	        		if(j==matrix[0].length-1)
	        		{
	        			writer.write(Double.toString(matrix[i][j])+"\r\n");
	        		}
	        		writer.write(Double.toString(matrix[i][j])+"\t");
	        	}
	        }
	        writer.close();
	            
	    } catch (Exception e)   
	    {      
	        e.printStackTrace();     
	    }  
	    finally
	    {
	    	
	    }
	}
	

}
