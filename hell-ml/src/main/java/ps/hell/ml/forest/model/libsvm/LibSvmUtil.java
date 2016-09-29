package ps.hell.ml.forest.model.libsvm;

import java.io.IOException;
import java.util.Arrays;


public class LibSvmUtil {
	
	public boolean isPrint=false;
	
	//使用字符输入
	SvmTrain train = new SvmTrain();
	SvmPredict predict = new SvmPredict();
	/**
	 * 输入的x
	 */
	public String[][] x=null;
	/**
	 * 输入的y
	 */
	public String[] y=null;
	/**
	 * x中每列类型
	 */
	public String[] classZ=null;
	
	public String inputFile=null;
	/**
	 * libsvm使用的输入参数
	 */
	public String args[]=null;
	
	/**
	 * 
	 * @param x 数据集
	 * @param classZ 数据集类型 null 为double值,s为字符串
	 * @param y 预测集
	 * @param inputModelFile 输入的model地址
	 */
	public LibSvmUtil(String[][] x,String[] classZ,String[] y,String inputModelFile){
		this.x=x;
		this.classZ=classZ;
		this.y=y;
		this.inputFile=inputModelFile;
	}
	public void init(){
		train.isPrint=isPrint;
	}
	
	public void train(){
		//训练集训练
		train.train(args, x, classZ, y, inputFile);
		//预测集初始化
		predict.test(args,train);
	}
	/**
	 * 预测值
	 * @param inputX
	 * @return
	 */
	public Object test(String[][] inputX){
		return predict.predictString(inputX);
	}
	
	public boolean writeModel(String path){
		train.writeToFile(path);
		return true;
	}
	

	public static void main(String[] args) throws IOException {
//		String file = System.getProperty("user.dir")
//				+ "/src/com/ml/libsvm/txt.txt";
//		String[] arg = { file, "f:\\model_r.txt" };
//		String[] parg = { file, "f:\\model_r.txt",
//				System.getProperty("user.dir") + "/src/com/ml/libsvm/out.txt" };
//		String[] parg2={null,"f:\\model_r.txt"};
//		SvmTrain t = new SvmTrain();
//		SvmPredict p = new SvmPredict();
//		//读取文件并写入模型到目录中
//		//t.main(arg);
//		//读取模型别预测写入目录中
////		p.main(parg);
////		1 1:171 2:65
////		1 1:173 2:66
////		2 1:156 2:45
////		2 1:157 2:46
//		double[][] x=new double[][]{{171,65},{173,66},{156,45},{157,46},{156,77},{157,75}};//
//		double[] y=new double[]{1,1,2,2,3,3};
//		String[] yString =new String[]{"g","g","b","b","a","a"};//
//		//t.train(null,x, y,file);
//		t.isPrint=false;
//		t.train(null,x, yString,file);
//		//从文件读取数据并生成模型
//		//t.train(null,null,new double[0],file);
//		//模型写入
//		//t.writeToFile("f:\\model_r.txt");
////		p.test(parg2, null);
//		p.test(null,t);
//		//标志位从1开始
//		//System.out.println(p.predict(new double[] { 157D, 46D }));
//		//System.out.println(p.predictString(new double[] {173,66}));
//		System.out.println(Arrays.toString(p.predict(x)));
//		System.out.println(Arrays.toString(p.predictString(x)));
//		
//		//使用字符输入
//		SvmTrain t2 = new SvmTrain();
//		SvmPredict p2 = new SvmPredict();
		String[][] x2=new String[][]{{"171","65","a"},{"173","66","a"},{"156","45","a"},{"157","46","b"},{"156","77","b"},{"157","75","b"}};
		String[] yString2 =new String[]{"g","g","b","b","a","a"};//
		String[] xClass=new String[]{null,null,"s"};
//		t2.train(null, x2, xClass, yString2, file);
//		t2.writeToFile("f:\\model_r_s.txt");
//		String[] parg3={null,"f:\\model_r_s.txt"};
//		p2.test(null,t2);
//		//p2.test(parg3,null);
//		System.out.println(Arrays.toString(p2.predictString(x2)));
		
		LibSvmUtil lib=new LibSvmUtil(x2,xClass,yString2,null);
		lib.train();
		System.out.println(Arrays.toString((String[]) lib.test(x2)));
	}
}