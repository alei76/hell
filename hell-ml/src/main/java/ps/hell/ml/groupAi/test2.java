package ps.hell.ml.groupAi;

public class test2 {
	public static  int getBigInterLength(long px)
	{
		int length=0;
		long temp=px;
		while(true)
		{
			if(temp==0)
			{
				break;
			}
			System.out.println(temp);
			temp=temp>>2;
			
			length++;
		}
		return length;
	}
	public static void main(String[] args) {
		long temp=10L;
		System.out.println(test2.getBigInterLength(temp));
		
		System.out.println(55&10);
	}
}
