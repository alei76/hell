package ps.hell.ml.cluster.divide;

public class test {

	
	private int i;
	private String str;
	public double[][] data;
	public test(int j,String str)
	{
		str="ccc";
		this.str=str;
		this.i=j;
		
		System.out.println(str);
	}
	
	
	public double[][] getData() {
		return data;
	}


	public void setData(double[][] data) {
		this.data = data.clone();
		this.data=new double[data.length][data[0].length];
		for(int i=0;i<data.length;i++)
		{
			this.data[i]=data[i].clone();
		}
	}


	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public static String change(String ll)
	{
		return "chage";
	}
	public static void main(String[] args) {
		int j=1;
		String ll="aaaa";
		test t1=new test(j,ll);
		System.out.println(t1.getI()+"\t"+t1.getStr()+"\t"+ll);
		j=2;
		ll="bb";
		System.out.println(t1.getI()+"\t"+t1.getStr());
		ll=test.change(ll);
		System.out.println(ll);
		
		double[][] ta={{1.0,1.0},{1.0,1.0}};
		t1.setData(ta);
		t1.data[0][1]=2.0;
		for(int i=0;i<ta.length;i++)
		{
			for(double l:t1.data[i])
			{
				System.out.print(l);
			}
			System.out.println();
		}
		
		
		
	}
}
