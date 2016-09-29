package ps.hell.base.struct;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		Runtime.getRuntime().gc();
		System.out.println(Runtime.getRuntime().totalMemory()/1024/1024+"M");
		Thread.sleep(1000);
		Runtime.getRuntime().gc();
		int row=30000;
		int col=5000;
		float[][] matrix=new float[row][];
		for(int i=0;i<row;i++){
			matrix[i]=new float[col];
		}
		System.out.println(Runtime.getRuntime().totalMemory()/1024/1024+"M");
	}
}
