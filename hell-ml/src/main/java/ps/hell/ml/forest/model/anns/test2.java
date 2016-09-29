package ps.hell.ml.forest.model.anns;

import java.util.LinkedList;
import java.util.Random;

import ps.hell.math.base.matrix.read.MatrixReadSource;

public class test2 {
public static void main(String[] args) {
	Random random=new Random();
	for(int i=0;i<10;i++)
	System.out.println(random.nextInt()%10000.0/10000.0+0.005);
	
	
	int ll[][]={{1,2,3},{4,5,6}};
	System.out.println(ll);
	System.out.println(ll.length);
	System.out.println(ll[0][2]);
	
	LinkedList<String[]> data=MatrixReadSource.readToLinkedListStringGBK("E://data.txt");
	LinkedList<String[]> test=MatrixReadSource.readToLinkedListStringGBK("E://3.txt");
	
	for(int i=0;i<test.size();i++)
	{
		for(int j=0;j<data.size();j++)
		{
			if(test.get(i)[0].equals(data.get(j)[1]) && test.get(i)[1].equals(data.get(j)[2]))
			{
				System.out.println(test.get(i)[0]+"\t"+ test.get(i)[1]+"\t"+data.get(j)[3]);
			}
		}
	}
	
	
	String ll2="abcdefg";
	System.out.println(ll2.substring(0,0));
}
}
