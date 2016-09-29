package ps.hell.util.plot.opencv;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_highgui;

import java.util.*;

import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;

/**
 * 社交网络图 图形迭代算法
 * 也为 弹性算法
 * @author Administrator
 *
 */
public class SnsGroup {

	
	public int maxIndex=10000;
	/**
	 * 存储所有点
	 */
	public Point[] pointList=null;
	/**
	 * 两点间的权重值
	 */
	public HashMap<Integer,Double> weight=new HashMap<Integer,Double>();
	
	/**
	 * 偏移量累积上限
	 */
	public double excursionLimit;
	/**
	 * 老的偏移距离
	 */
	public double oldExcursion=0.0;
	/**
	 * 新偏移距离
	 */
	public double newExcursion=0.0;
	/**
	 * 迭代数量
	 */
	public double iteratorLimit;
	/**
	 * 实际迭代次数
	 */
	public int iteratorCount=0;
	/**
	 * 点坐标类
	 * @author Administrator
	 *
	 */
	public class Point
	{
		double x;
		double y;
		int index;
		public Point(double x,double y,int index)
		{
			this.x=x;
			this.y=y;
			this.index=index;
		}
		/**
		 * 
		 * @param point2
		 * @param weight
		 * @param dist 获取偏移数量
		 * @return
		 */
		public Point minusModel(Point point2,double weight)
		{
			//计算向量偏移量
			double xCl=(point2.x-this.x)*weight;
			double yCl=(point2.y-this.y)*weight;
			//计算 当前点对应的偏移
			return new Point(x+xCl,y+yCl,index);
		}
		/**
		 * 返回 weight中key值的计算值
		 * @param point2
		 * @return
		 */
		public int getKey(Point point2)
		{
			return (this.index+1)*maxIndex+(point2.index+1);
		}
		
		@Override
		public Point clone()
		{
			return new Point(this.x,this.y,this.index);
		}
	}
	/**
	 * 通过计算point1 与point2 的作用并
	 * 返回 point1的移动偏移量
	 * @param point1
	 * @param listPoint为所有存储的Point点
	 * @param weight 1 2之间的链接权重 越低越相关、、
	 * 其中weight中Integer中的值为
	 * 反向权重值 全部存储在map中
	 * @return
	 */
	public double compute(Point point1,int index,Point[] pointList,HashMap<Integer,Double> weight)
	{
		Point result=point1.clone();
		for(Point point:pointList)
		{
			int ll=result.getKey(point);
		//计算偏移向量
			Double do2=weight.get(result.getKey(point));
			if(do2==null)
			{
				do2=1D;
			}
			result=result.minusModel(point,do2);
			//System.out.println("po:"+point1.x+"\t"+point1.y+"\tpoint2:"+result.x+"\t"+result.y);
		}
		//计算偏移距离
		double dist=Math.pow(result.x-point1.x,2D)+Math.pow(result.y-point1.y, 2D);
		//System.out.println("re:"+result.x+"\t"+result.y);
		pointList[index]=result;
		System.out.println("dist:"+dist);
		return dist;
	}
	/**
	 * 关联点及存储的权重值
	 */
	public class RelevancePoints
	{
		Point firstPoint=null;
		Point secondPoint=null;
		double weight;
		public RelevancePoints(Point firstPoint,Point secondPoint,double weight)
		{
			this.firstPoint=firstPoint;
			this.secondPoint=secondPoint;
			this.weight=weight;
		}
	}
	/**
	 * 
	 * @param points 关联点集 
	 * @param max points点总数量
	 * @param excursionNum 偏移量
	 * @param iteratorLimit 迭代数量
	 */
	public SnsGroup(LinkedList<RelevancePoints> points,int max,double excursionLimit,int iteratorLimit)
	{
		this.maxIndex=max;
		this.excursionLimit=excursionLimit;
		this.iteratorLimit=iteratorLimit;
		pointList=new Point[max];
		for(RelevancePoints pointToPoint:points)
		{
			//将将两点间的相关性等信息放入hashMap中
			int lp=pointToPoint.firstPoint.getKey(pointToPoint.secondPoint);
			weight.put(pointToPoint.firstPoint.getKey(pointToPoint.secondPoint), pointToPoint.weight);
			//将point信息放入pointlist中
			if(pointList[pointToPoint.firstPoint.index]==null)
			{
				pointList[pointToPoint.firstPoint.index]=pointToPoint.firstPoint;
			}
			if(pointList[pointToPoint.secondPoint.index]==null)
			{
				pointList[pointToPoint.secondPoint.index]=pointToPoint.secondPoint;
			}
		}
	}
	/**
	 * 迭代过程
	 */
	public void exec()
	{
		System.out.println("开始迭代");
		while(true)
		{
			iteratorCount++;
			if(iteratorCount>this.iteratorLimit)
			{
				System.out.println("已达最大迭代上限");
			}
			int index=-1;
			newExcursion=0.0;
			for(Point point:pointList)
			{
				index++;
				//对每一个点做一次偏移计算
				double dist=0.0;
				dist=compute(point,index,pointList,weight);
				System.out.println("偏移距离:"+index+":"+dist);
				newExcursion+=dist;
			}
			if(Math.abs(newExcursion-oldExcursion)<this.excursionLimit)
			{
				oldExcursion=newExcursion;
				System.out.println("迭代收敛:");
				System.out.println("迭代次数:"+iteratorCount+"\t偏移量:"+oldExcursion);
				break;
			}
			System.out.println("迭代次数:"+iteratorCount+"\t偏移量:"+oldExcursion);
		}
	}
	/**
	 * 返回0表示结束
	 * 返回1表示本次迭代完成
	 * @return
	 */
	public int iteration()
	{
		iteratorCount++;
		if(iteratorCount>this.iteratorLimit)
		{
			System.out.println("已达最大迭代上限");
		}
		int index=-1;
		newExcursion=0.0;
		for(Point point:pointList)
		{
			index++;
			//对每一个点做一次偏移计算
			Double dist=0.0;
			System.out.println(point.x+"\t"+point.y);
			dist=compute(point,index,pointList,weight);
			System.out.println("偏移距离:"+index+":"+dist);
			System.out.println(pointList[index].x+"\t"+pointList[index].y);
			newExcursion+=dist;
		}
		System.out.println("newExcursion:"+newExcursion);
		if(Math.abs(newExcursion-oldExcursion)<this.excursionLimit)
		{
			oldExcursion=newExcursion;
			System.out.println("迭代收敛:");
			System.out.println("迭代次数:"+iteratorCount+"\t偏移量:"+oldExcursion);
			return 0;
		}
		System.out.println("迭代次数:"+iteratorCount+"\t偏移量:"+oldExcursion);
		return 1;
	}
	
	public static void main(String[] args) {
		//初始化数据
		LinkedList<RelevancePoints> points=new LinkedList<RelevancePoints>();
		int max=30;
		double excursionLimit=1E-3;
		int iteratorLimit=500;
		ArrayList<Point> list=new ArrayList<Point>();
		SnsGroup sns2=new SnsGroup(points,max,excursionLimit,iteratorLimit);
		for(int i=0;i<max;i++)
		{
			list.add(sns2.new Point(Math.random(),Math.random(),i));
		}
		Random random=new Random();
		for(int i=0;i<100;i++)
		{
			RelevancePoints rele=sns2.new RelevancePoints(list.get(Math.abs(random.nextInt()%max)), list.get(Math.abs(random.nextInt()%max)),Math.abs(Math.random()));
			points.add(rele);
		}
		SnsGroup sns=new SnsGroup(points,max,excursionLimit,iteratorLimit);
		//每做一次迭代打印一次
		BubbleGraph graph=new BubbleGraph(); 
		
		graph.parentent_config();//计算改变的所有全局参量
		
	    
	  //初始化图片 
		BubbleGraph.img_trans img1=new BubbleGraph.img_trans();
	    //main_type="scatter";
	    img1.img1=opencv_core.cvCreateImage(opencv_core.cvSize(graph.width,graph.height),8,3);
	    img1.value.x_max=0;
	    img1.value.x_min=0;
	    img1.value.y_max=0;
	    img1.value.y_min=0;
	    img1.value.x_o=0;
	    img1.value.y_o=0;
	    graph.main_type="qipao2";
	    graph.width = 600;
		graph.height = 400;

		graph.parentent_config();// 计算改变的所有全局参量
		// 初始化图片
		BubbleGraph.img_trans img = new BubbleGraph.img_trans();
		// main_type="scatter";
		img.img1 = opencv_core.cvCreateImage(
				opencv_core.cvSize(graph.width, graph.height), 8, 3);
		graph.main_type = "qipao2";

	
		 cvNamedWindow("Example",1);
		while(true)
		{
			int flag=sns.iteration();
			Vector<Double> x = new Vector<Double>();
			Vector<Double> y = new Vector<Double>();
			Vector<Integer> cluster = new Vector<Integer>();
			for(Point point:sns.pointList)
			{
				x.add(point.x);
				y.add(point.y);
				cluster.add(1);
			}
			if (graph.main_type == "scatter" || graph.main_type == "qipao"
					|| graph.main_type == "qipao2" || graph.main_type == "qipao3") {
				System.out.println("初始化图片");
				graph.plotScatter(img, x, y, graph.main_type, cluster, 0);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  if(img.img1==null)
				   break;
			  cvShowImage("Example",img.img1);
			  int c=opencv_highgui.cvWaitKey(33);//等待33 ms]如果其间用户触发了一个按键，c会被设置成这个按键的ASCII码，否则，c会被设置成-1。
			  if( c == 27 )
			   break;
			  System.out.println("flag:"+flag);
			if(flag==0)
			{
				break;
			}
		}
//		cvNamedWindow("Example2", CV_WINDOW_AUTOSIZE);
//		 CvCapture* capture = cvCreateFileCapture("1.avi");//通过参数设置确定要读入的AVI文件，返回一个指向CvCapture结构的指针。这个结构包括了所有关于要读入AVI文件的信息，其中包含状态信息
//		 IplImage* frame;
//		 while(1) {
//		  frame = cvQueryFrame( capture );//用来将下一帧视频文件载入内存(实际是填充或更新CvCapture结 构中)。
//		  if( !frame )
//		   break;
//		  cvShowImage( "Example2", frame );
//
//		  char c = cvWaitKey(33);//等待33 ms]如果其间用户触发了一个按键，c会被设置成这个按键的ASCII码，否则，c会被设置成-1。
//		  if( c == 27 )
//		   break;//如果用户触发了ESC键(ASCII 27)，循环被退出，读入视频停止
//		 }
//		 cvReleaseCapture( &capture );
//		 cvDestroyWindow( "Example2" );
	}
}
