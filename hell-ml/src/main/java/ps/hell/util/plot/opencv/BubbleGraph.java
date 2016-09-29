package ps.hell.util.plot.opencv;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_highgui;
import ps.hell.math.base.matrix.read.MatrixReadSource;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class BubbleGraph {
	
	
	public static class img_des
	{
		 public double x_min=0.0;
		 public double x_max=0.0;//实际显示的值
		 public double y_min=0.0;
		 public double y_max;
		 public int x_o=0;
		 public int y_o=0;
		 public img_des()
		 {}
	}
	
	public static class img_trans
	{
	    public IplImage img1;
	    public img_des value=new img_des();
	    public img_trans()
	    {}
	}
	
	

	public int y_break=10;//全局y的间间断数量
	public int x_break=20;//x轴的间断数量
	public int height_point=20;//标记左下角向上平移位置
	public int width_point=20;//标记左下角向右平移位置
	public int height_point2=20;//标记右上角向下平移位置
	public int width_point2=-1;//标记右上角向左平移位置
	public int width=400;//全局图像x大小
	public int height=350;//图片y大小
	public String back_ground_col="white";
	
	public CvScalar backGroundCol;
	public int back_ground_flag=1;//如果为1则背景为上，为0则图片
	public String main_type="qipaotu";

	//标题文字显示
	public String x_axis_str="x_axis_str";
	public String x_axis_str_col="yellow";
	public double x_axis_str_width=-1;
	public int x_axis_str_x=-1;//坐标x
	public int x_axis_str_y=-1;//坐标y
	public int x_axis_str_flag=1;//是否绘制1为绘制

	public String main_axis_str="main_axis_str";
	public String main_axis_str_col="red";
	public double main_axis_str_width=-1;
	public int main_axis_str_x=-1;
	public int main_axis_str_y=-1;
	public int main_axis_str_flag=1;//是否绘制1为绘制
	public String y_axis_str="y_axis_str";
	public String y_axis_str_col="blue";
	public double y_axis_str_width=-1;
	public int y_axis_str_x=-1;
	public int y_axis_str_y=-1;
	public int y_axis_flag=1;//y，1为纵向显示，0为行向显示
	public int y_axis_str_flag=1;//是否绘制1为绘制

	public String main_legend_rec_type="l";//主边框格式
	public String main_legend_col="white";//主窗体背景颜色
	public String main_legend_rec_col="red";//主窗体frame颜色
	//设置图例属性
	public String legend_rec_col="white";//设置图例背景颜色
	public String legend_rec_frame_col="blue";//设置图例边框显示形式
	public int legend_rec_left_x=-1;//设置图例左上角坐标
	public int legend_rec_left_y=-1;//y坐标
	public int legend_rec_right_x=-1;//设置图例右下角坐标
	public int legend_rec_right_y=-1;//y坐标
	public int legend_rec_width=2;//边框大小
	public int legend_count=0;//图例上的绘制位置
	public double legend_rec_font_rate=-1;//图例字体比率
	public double legend_rec_tu_rate=-1;//图例图像比率
	public int legend_rec_flag=-1;//是否显示图例主框架
	public int legend_rec_graph_flag=1;//是否显示图例上的图
	public int legend_rec_col_flag=1;//图例是否有背景色


	//绘制xy轴
	public int x_eig=1000,y_eig=1000;//x轴保留小数点数精度需要<10
	public String axis_font_x_col="pink";//设置x，y轴字体的颜色
	public String axis_font_y_col="black";
	public int axis_font_x_re_x=-5;//绘制字体的时候x轴字体的x轴偏移量
	public int axis_font_x_re_y=-1;//绘制字体的时候x轴字体的y轴偏移量
	public int axis_font_y_re_x=10;//绘制字体的时候y轴字体的x轴偏移量
	public int axis_font_y_re_y=3;//绘制字体的时候y轴字体的y轴偏移量
	public String axis_x_col="black";//设置x，y轴的颜色
	public String axis_y_col="black";
	public int axis_x_width=3;//设置x轴的粗细
	public int axis_y_width=3;//设置y轴的粗细
	public int axis_x_width_scale=1;//设置x轴的刻度粗细
	public int axis_y_width_scale=1;//设置y轴的刻度粗细
	public String axis_x_width_scale_col="black";//设置x轴的刻度颜色
	public String axis_y_width_scale_col="black";//设置y轴的刻度颜色
	public int axis_x_width_len=15;//设置x轴的刻度轴的长度
	public int axis_y_width_len=15;//设置y轴的刻度轴的长度
	public double axis_x_font_rate=-1;//设置航坐标的字体比率
	public double axis_y_font_rate=-1;//设置纵坐标的字体比率
	public int axis_x_flag=1;//是否绘制x轴
	public int axis_y_flag=1;//是否绘制y轴
	public int axis_x_y_flag=1;//是否会之x上的刻度
	public int axis_y_x_flag=1;//是否会之y上的刻度
	public int axis_x_font_flag=1;//是否显示x轴上值
	public int axis_y_font_flag=1;//是否显示y轴上值



	///绘制grid
	public String grid_type_x="dl";//标记为虚线
	public int grid_type_x_real=3;//实线长度
	public int grid_type_x_img=2;//虚线长度
	public String grid_col_x="150,150,150";//网格为灰色
	public String grid_type_y="dl";//标记为虚线格式//当前为无
	public String grid_col_y="grey";//网格为灰色
	public int grid_type_y_real=3;//实线长度
	public int grid_type_y_img=2;//虚线长度
	public int axis_x_grid_flag=1;//是否显示x轴上网格
	public int axis_y_grid_flag=1;//是否显示y轴上网格





	//气泡图参量
	public double qipao_min=-1;
	public double qipao_max=-1;


	/**
	 * 配置调度函数
	 */
	public void parentent_config()
	{
	    //图例坐标
	    if(width_point2==-1)
	        width_point2=width/5;
	    if(legend_rec_left_x==-1)
	        legend_rec_left_x=width-width_point2+3;//设置图例左上角坐标
	    if(legend_rec_left_y==-1)
	        legend_rec_left_y=height_point2;//y坐标
	    if(legend_rec_right_x==-1)
	        legend_rec_right_x=width-3;//设置图例右下角坐标
	    if(legend_rec_right_y==-1)
	        legend_rec_right_y=height-height_point;//y坐标

	    //x，y轴分割数量
	    if(axis_font_x_re_y==-1)
	        axis_font_x_re_y=height/y_break/3;//绘制字体的时候x轴字体的y轴偏移量
	    if(axis_x_font_rate==-1)//设置航坐标的字体比率
	    axis_x_font_rate=((height*1.0)/100+1)/10;
	    if(axis_y_font_rate==-1)//设置纵坐标的字体比率
	    axis_y_font_rate=((height*1.0)/100+1)/10;
	    //x，y轴精度
	    if(x_eig==1000)
	        x_eig=1;//x，y轴精度显示
	    if(y_eig==1000)
	        y_eig=0;
	    
	    //图例比率，大小
	    if(legend_rec_font_rate==-1)//图例字体比率
	    legend_rec_font_rate=((height*1.0)/100+1)/8;
	    if(legend_rec_tu_rate==-1)//图例图像比率
	    legend_rec_tu_rate=((height*1.0)/100+1)/8;


	    //主题大小坐标
	    if(y_axis_str_width==-1)
	        y_axis_str_width=legend_rec_font_rate;
	    if(x_axis_str_width==-1)
	        x_axis_str_width=legend_rec_font_rate;
	    if(main_axis_str_width==-1)
	        main_axis_str_width=legend_rec_font_rate;
	    if( x_axis_str_x==-1)//坐标x
	        x_axis_str_x=(width-width_point2)/2;
	    if(x_axis_str_y==-1)//坐标y
	        x_axis_str_y=height-height_point/2;
	    if(main_axis_str_x==-1)//坐标
	        main_axis_str_x=(width-width_point2)/2;
	    if(main_axis_str_y==-1)
	        main_axis_str_y=height_point2/3*2;
	    if(y_axis_str_x==-1)
	        y_axis_str_x=(width_point)/2;
	    if(y_axis_str_y==-1)
	        y_axis_str_y=(height-height_point-height_point2)/3;


	    //气泡图显示最大最小值
	    if(qipao_min==-1)
	        qipao_min=40;//气泡的最小比率
	    if(qipao_max==-1)
	        qipao_max=25;//气泡最大比率
	}

	//气泡图参数

	//16进制转换成2进制数
	/**
	 * 将16进制转化为标准的2进制颜色
	 */
	public int sixten_two(String a)
	{
	    int sum=0;
	    for(int i=0;i<2;i++)
	    {
	        int sum1=0;
	        if(String.valueOf(i).equals('f')||String.valueOf(i).equals('F'))
	        {
	            sum1=15;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }
	        if(String.valueOf(i).equals('e')||String.valueOf(i).equals('E'))
	        {
	            sum1=14;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }

	        if(String.valueOf(i).equals('d')||String.valueOf(i).equals('D'))
	        {
	            sum1=13;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }

	        if(String.valueOf(i).equals('c')||String.valueOf(i).equals('C'))
	        {
	            sum1=12;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }

	        if(String.valueOf(i).equals('b')||String.valueOf(i).equals('B'))
	        {
	            sum1=11;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }

	        if(String.valueOf(i).equals('a')||String.valueOf(i).equals('A'))
	        {
	            sum1=10;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }
	        if(String.valueOf(i).equals('0'))
	        {
	            sum1=0;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }
	        if(String.valueOf(i).equals('0'))
	        {
	            sum1=1;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }
	        if(String.valueOf(i).equals('2'))
	        {
	            sum1=2;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }
	        if(String.valueOf(i).equals('3'))
	        {
	            sum1=3;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }
	        if(String.valueOf(i).equals('4'))
	        {
	            sum1=4;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }
	        if(String.valueOf(i).equals('5'))
	        {
	            sum1=5;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }
	        if(String.valueOf(i).equals('6'))
	        {
	            sum1=6;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }
	        if(String.valueOf(i).equals('7'))
	        {
	            sum1=7;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }
	        if(String.valueOf(i).equals('8'))
	        {
	            sum1=8;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }
	        if(String.valueOf(i).equals('9'))
	        {
	            sum1=9;
	            if(i==0)
	            sum+=sum1*16;
	            else
	            sum+=sum1;
	            continue;
	        }


	    }
	    return sum;
	}


	/**
	 * 配色提取方案
	 * @param str
	 * @return
	 */
	public CvScalar col_platter(String str)
	{//如果是直接，或者可以输入"230,130,234",rgb格式
	    CvScalar cal=new CvScalar();
//	    /cal.blue();
	    double r=0.0,g=0.0,b=0.0;
	    if(String.valueOf(0).equals('#'))
	    {//添加颜色
	        for(int l=0;l<3;l++)
	        {
	            int l2;
	            l2=sixten_two(str.substring(1+l*2,2));
	            if(l==0)
	            {
	                r=l2;
	                continue;
	            }
	            if(l==1)
	            {
	                g=l2;
	                continue;
	            }
	            if(l==2)
	                b=l2;
	        }
	    }
	    else
	    {
	    	//System.out.println("color:"+str);
	        if(str.indexOf(',')>=0 && str.indexOf(',')<str.length())
	        {//如果是rgb格式则
	            r=Integer.parseInt(str.substring(0,str.indexOf(',')));
	            g=Integer.parseInt(str.substring(str.indexOf(',')+1,str.indexOf(',',str.indexOf(',')+1)));
	            b=Integer.parseInt(str.substring(str.indexOf(',',str.indexOf(',')+1)+1));
	        }
	        else
	        {
	            if(str.equals("red"))
	            {
	                r=255;g=0;b=0;
	                
	            }
	            else if(str.equals("black"))
	            {
	                r=0;g=0;b=0;
	            }
	            if(str.equals("white"))
	            {
	                r=255;g=255;b=255;
	            }
	            if(str.equals("pink"))
	            {
	                r=255;g=0;b=255;
	            }
	            if(str.equals("grey"))
	            {
	                r=160;g=160;b=160;
	            }
	            if(str.equals("blue"))
	            {
	                r=0;g=0;b=255;
	            }
	            if(str.equals("yellow"))
	            {
	                r=255;g=255;b=0;
	            }
	            if(str.equals("light yellow"))
	            {
	                r=255;g=255;b=100;
	            }
	            if(str.equals("green"))
	            {
	                r=0;g=255;b=0;
	            }

	        }
	    }

	    return CV_RGB(r,g,b);
	}
	///绘制主边框
	
	/**
	 * 主题
	 */
	public void main_legend_set(IplImage img)
	{
	     CvPoint x_left_up=new CvPoint(width_point,height_point2);
	     CvPoint x_left_down=new CvPoint(width_point,img.height()-height_point);
	     CvPoint x_right_up=new CvPoint(img.width()-width_point2,height_point2);
	     CvPoint x_right_down=new CvPoint(img.width()-width_point2,img.height()-height_point);
	    if(main_legend_rec_type=="l")
	    {
	        CvScalar cal;
	        cal=col_platter(main_legend_col);
	        
	        //cvLine()
	        //cvLine(colorDst, pt1, pt2, CV_RGB(255, 0, 0), 3, CV_AA, 0);
	        
	        cvLine(img,x_left_up,x_left_down,cal, 3, CV_AA, 0);
	        cvLine(img,x_left_up,x_right_up,cal, 3, CV_AA, 0);
	        cvLine(img,x_left_down,x_right_down,cal, 3, CV_AA, 0);
	        cvLine(img,x_right_up,x_right_down,cal, 3, CV_AA, 0);
	    }
	}

	//绘制图例属性边框
	/**
	 * 边框
	 */
	public void legend_set(IplImage img1)
	{
	    
	    if(legend_rec_flag==1)
	    {
	        CvScalar col_frame=col_platter(legend_rec_frame_col);//边框
	        //绘制边框
	        CvPoint left_up=new CvPoint(legend_rec_left_x,legend_rec_left_y);
	        CvPoint left_down=new CvPoint(legend_rec_left_x,legend_rec_right_y);
	        CvPoint right_up=new CvPoint(legend_rec_right_x,legend_rec_left_y);
	        CvPoint right_down=new CvPoint(legend_rec_right_x,legend_rec_right_y);
	        cvLine(img1,left_up,left_down,col_frame,legend_rec_width,CV_AA,0);
	        cvLine(img1,left_up,right_up,col_frame,legend_rec_width,CV_AA,0);
	        cvLine(img1,left_down,right_down,col_frame,legend_rec_width,CV_AA,0);
	        cvLine(img1,right_up,right_down,col_frame,legend_rec_width,CV_AA,0);
	    }
	    if(legend_rec_col_flag==1)
	    {
	        CvScalar col_background=col_platter(legend_rec_col);//背景
	        //填充并绘制框体
	        for(int i=legend_rec_left_y+1;i<legend_rec_right_y-1;i++)
	        {
	            for(int j=legend_rec_left_x+1;j<legend_rec_right_x-1;j++)
	            {
	            	opencv_core.cvSet2D(img1,i,j,col_background);          
	            }
	        }
	    }
	}
	//图例文字显示
	/**
	 * 绘制图例
	 */
	public void put_font_tuli(IplImage img1,CvPoint center,String str,CvScalar col_x_font)
	{
	        CvFont font=new CvFont();//初始化字体
	        opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.45*legend_rec_font_rate,0.45*legend_rec_font_rate,0,0,CV_AA);//字体设置
	        opencv_core.cvPutText(img1,str,center,font,col_x_font);
	}

	//背景图
	
	/**
	 * 添加背景图或者将背景全部
	 */
	public void back_ground(IplImage img1,String col,int flag)//img1原图，back背景图地址
	{//用flag=0则表示从col中选择，如果不为0则从col中选择颜色否则为图片地址
		if(col==null)
		{
			col="white";
		}
	    if(flag==1)
	    {//黑色
	        //提取颜色
	       // System.out.println("col:"+col);
	        CvScalar col_co=col_platter(col);
	       // System.out.println(col_co.blue+"\t"+col_co..green()+"\t"+col_co.red());
	        for(int i=height_point2;i<img1.height()-height_point;i++)
	        {
	            for(int j=width_point;j<img1.width()-width_point2;j++)
	            {
	            	opencv_core.cvSet2D(img1,i,j,col_co);
	            }
	        }
	    }
	    else
	    {//图片添加背景
	            int width1=img1.width();
	            int height1=img1.height();
	            int step1=img1.widthStep();
	            int nchannel1=img1.nChannels();
	            IplImage img2;
	            if((img2=opencv_highgui.cvLoadImage(col,3)).isNull())
	            {
	                System.out.println("路径错误或者图像不可用");
	                return ;
	            }
	            System.out.println("读取背景成功");
	            int width2=img2.width();
	            int height2=img2.height();
	            int step2=img2.widthStep();
	            int nchannel2=img2.nChannels();
	            //计算映射比率参数
	            double widht_rate=(width2*1.0)/width1;
	            double height_rate=(height2*1.0)/height1;
	            //按照比率调整到img1图片中
	            System.out.println("图片简单变换");
	            for(int i=0;i<height1;i++)
	            {
	                for(int j=0;j<width1;j++)
	                {
	                    double hei_min,hei_max,wid_min,wid_max;
	                    if(i==0)
	                        hei_min=0;
	                    else
	                        hei_min=(i)*height_rate;
	                    if(i==height1-1)
	                        hei_max=height2;
	                    else
	                        hei_max=(i+1)*height_rate;
	                    //System.out.println(hei_max+"\tmax:"+height_rate);
	                    if(j==0)
	                        wid_min=0;
	                    else
	                        wid_min=(j)*widht_rate;
	                    if(j==width1-1)
	                        wid_max=width2;
	                    else
	                        wid_max=(j+1)*widht_rate;
	                    //System.out.println(hei_min+"\t"+hei_max);
	                    //System.out.println(wid_min+"\t"+wid_max);
	                    double b=0.0,g=0.0,r=0.0,count_n=0;
	                    if(hei_max+1>height2)
	                        hei_max=height2;
	                    if(wid_max+1>width2)
	                        wid_max=width2;
	                    for(int m=(int)hei_min;m<(int)hei_max+1;m++)
	                    {
	                        for(int n=(int)wid_min;n<(int)wid_max+1;n++)
	                        {
	                            CvScalar ll=cvGet2D(img2,i,j);
	                            b+=ll.blue();
	                            g+=ll.green();
	                            r+=ll.red();
	                            count_n++;
	                        }
	                    }
	                    opencv_core.cvSet2D(img1,i,j,opencv_core.cvScalar(r/count_n,g/count_n,b/count_n,1.0));
	                
	            }
	        }
	    }
	    
	}

	//绘制x，y轴
	
	/**
	 * 坐标轴
	 */
	public void axis_x_y(IplImage img1,int x_o,int y_o,double x_min,double x_max,double y_min,double y_max)
	{
	    
	    
	    CvScalar col_x_scale=col_platter(axis_x_width_scale_col);//x轴刻度颜色
	    CvScalar col_y_scale=col_platter(axis_y_width_scale_col);//y轴刻度颜色
	    CvScalar col_x_font=col_platter(axis_font_x_col);//x轴字体颜色
	    CvScalar col_y_font=col_platter(axis_font_y_col);//y轴字体颜色
	    CvScalar col_grid_x=col_platter(grid_col_x);//纵向网格
	    CvScalar col_grid_y=col_platter(grid_col_y);//行向网格
	    //绘制xy主轴
	    //System.out.println(width_point+"\t"+y_o);
	    
	    //System.out.println(x_o+"\t"+height_point);
	     CvPoint    y_min_1=new CvPoint(x_o,height_point2);
	    //System.out.println(x_o+"\t"+height-height_point);
	    CvPoint    y_max_1=new CvPoint(x_o,height-height_point);
	    if(axis_x_flag==1)//如果显示x轴
	    {
	        CvScalar col_x=col_platter(axis_x_col);//x轴颜色
	         CvPoint x_min_1=new CvPoint(width_point,y_o);
	        //System.out.println(width-width_point+"\t"+y_o);
	         CvPoint x_max_1=new CvPoint(width-width_point2,y_o);
	        cvLine(img1,x_min_1,x_max_1,col_x,axis_x_width,CV_AA, 0);
	    }
	    if(axis_y_flag==1)//如果显示y轴
	    {    
	       CvScalar col_y=col_platter(axis_y_col);//y轴颜色
	       CvPoint y_min_z_1=new CvPoint(x_o,height_point2);
	        //System.out.println(x_o+"\t"+height-height_point);
	       CvPoint y_max_z_1=new CvPoint(x_o,height-height_point);
	       cvLine(img1,y_min_z_1,y_max_z_1,col_y,axis_y_width,CV_AA, 0);
	    }
	    //绘制xy轴上的刻度
	    CvFont font=new CvFont();//初始化字体
	     opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.35*axis_x_font_rate,0.35*axis_x_font_rate,0,0,CV_AA);//字体设置
	        
	        if(axis_x_y_flag==1||axis_x_grid_flag==1||axis_x_font_flag==1)
	            //x轴上刻度和网格,数值其中有一项绘制
	        {
	            for(int i=width_point+1;i<width-width_point2-1;i++)
	            {//20份绘制x轴上点
	                //System.out.println("1");
	                if(Math.abs(x_o-i)%((width-width_point-width_point2)/x_break)==0)
	                {//为间断点则绘制
	                    if(axis_x_y_flag==1)
	                    {//绘制间断点
	                         CvPoint x_min_jianduan1=new CvPoint(i,y_o);
	                         CvPoint x_min_jianduan2=new CvPoint(i,y_o-axis_x_width_len);
	                        cvLine(img1,x_min_jianduan1,x_min_jianduan2,col_x_scale,axis_x_width_scale,CV_AA,0);
	                    }
	                    //绘制标尺
	                    //绘制纵向网格
	                    if(axis_x_grid_flag==1 && i!=x_o)
	                    {//则绘制网格
	                         CvPoint x_min_jianduan11=new CvPoint(i,height_point2);
	                         CvPoint x_min_jianduan21=new CvPoint(i,height-height_point);
	                    
	                        for(int l=height_point2+2;l<height-height_point-2;l++)
	                        {
	                            if((l-height_point2)%(grid_type_x_real+grid_type_x_img)-grid_type_x_real>0)
	                            {
	                            	cvSet2D(img1,l,i,col_grid_x);
	                            }
	                        }

	                    }
	                    //字体
	                    if(axis_x_font_flag==1)
	                    {
	                        CvPoint x_font=new CvPoint(i+axis_font_x_re_x,y_o+axis_font_x_re_y);//左位移距离
	                        double mk;
	                        if(x_min<=0 && x_max>=0)
	                        {
	                            //System.out.println(i+"\t"+i-width_point+"\t"+width-width_point-width_point2+"\t"+x_max-x_min+"\t"+x_min);
	                            //mk=double(i-width_point)/double(width-width_point-width_point2)*(x_max-x_min)+x_min;
	                            mk=(i-x_o)*1.0/(width-width_point-width_point2)*(x_max-x_min);

	                        }
	                        else
	                        {
	                            if(x_min>0)
	                                //mk=double(i-width_point)/double(width-width_point-width_point2)*x_max;
	                                mk=(i-width_point)*1.0/(width-width_point-width_point2)*x_max;
	                            else
	                                //mk=double(i-width_point)/double(width-width_point-width_point2)*(-x_min)+x_min;
	                                mk=(i-width_point)*1.0/(width-width_point-width_point2)*(-x_min)+x_min;
	                        }
	                        //gcvt(mk,3,ncha);
	                        //System.out.println("mk::"+mk+"\t"+sizeof(mk));
	                        if(x_eig>=0)
	                        {//x轴显示精度
	                        	 DecimalFormat df = new DecimalFormat("#.00");
	                            opencv_core.cvPutText(img1,df.format(mk),x_font,font,col_x_font);
	                        }
	                        else
	                        {
	                        	 DecimalFormat df = new DecimalFormat("#.");
	                            opencv_core.cvPutText(img1,df.format(mk),x_font,font,col_x_font);
	                        }
	                    }
	                }
	            }
	        }
	        //cvInitFont(&font,CV_FONT_HERSHEY_COMPLEX,0.4,0.4,0,0,CV_AA);//字体设置
	        if(axis_y_x_flag==1||axis_y_font_flag==1||axis_y_grid_flag==1)
	        {//绘制y轴信息
	            opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.35*axis_y_font_rate,0.35*axis_y_font_rate,0,0,CV_AA);//字体设置
	            for(int i=height_point2+1;i<height-height_point-1;i++)
	            {//20份//绘制y轴上点
	                if(Math.abs(y_o-i)%((height-height_point-height_point2)/y_break)==0)
	                {//为间断点则绘制
	                    if(axis_y_x_flag==1)
	                    {
	                        CvPoint x_min_jianduan1=new CvPoint(x_o,i);
	                        CvPoint x_min_jianduan2=new CvPoint(x_o+axis_y_width_len,i);
	                        cvLine(img1,x_min_jianduan1,x_min_jianduan2,col_y_scale,axis_y_width_scale,CV_AA,0);
	                    }
	                    if(axis_y_grid_flag==1 && i!=y_o)
	                    {//则绘制网格
	                        CvPoint x_min_jianduan11=new CvPoint(i,height_point2);
	                        CvPoint x_min_jianduan21=new CvPoint(i,height-height_point);
	                    
	                        for(int l=width_point+2;l<width-width_point2-2;l++)
	                        {
	                            if((l-height_point2)%(grid_type_y_real+grid_type_y_img)-grid_type_y_real>0)
	                            {
	                            	cvSet2D(img1,i,l,col_grid_y);
	                            }
	                        }

	                    }
	                    //cvInitFont(&font,CV_FONT_HERSHEY_COMPLEX,0.35,0.35,0,0,CV_AA);//字体设置
	                    if(axis_y_font_flag==1)
	                    {
	                        CvPoint x_font=new CvPoint(x_o+axis_font_y_re_x,i+axis_font_y_re_y);
	                        double mk;
	                        if(y_min<=0 && y_max>=0)
	                        {
	                            //mk=double(height-i+height_point)/double(height-height_point-height_point2)*(y_max-y_min)+y_min;
	                            mk=(y_o-i)*1.0/(height-height_point-height_point2)*(y_max-y_min);
	                        }
	                        else
	                        {
	                            if(y_min>0)
	                                //mk=double(height-i+height_point)/double(width-height_point-height_point2)*y_max;
	                                mk=(height-i+height_point)*1.0/(height-height_point-height_point2)*y_max;
	                            else
	                                //mk=double(height-i+height_point)/double(width-height_point-height_point2)*(-y_min)+y_min;
	                                mk=(height-i+height_point)*1.0/(height-height_point-height_point2)*(-y_min)+y_min;
	                        }
	                        //gcvt(mk,3,ncha);
	                        if(y_eig>=0)
	                        {//x轴显示精度
	                        	DecimalFormat df = new DecimalFormat("#.00");
	                            opencv_core.cvPutText(img1,df.format(mk),x_font,font,col_y_font);
	                        }
	                        else
	                        {
	                        	DecimalFormat df = new DecimalFormat("#.");
	                        	opencv_core.cvPutText(img1,df.format(mk),x_font,font,col_y_font);
	                        }
	                    }
	                }
	            }
	        }
	}

	
	
	/**
	 * 提取背景色的调色板信息
	 */
	public void setBackGroundCol()
	{
		this.backGroundCol=col_platter(this.back_ground_col);
	}


	//绘制主题
	public void main_x_y_des(IplImage img1)
	{
	    CvFont font=new CvFont();//初始化字体
	    if(x_axis_str_flag==1)
	    {
	        CvPoint x_ax_down_center=new CvPoint(x_axis_str_x,x_axis_str_y);
	        opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.6*x_axis_str_width,0.6*x_axis_str_width,0,0,CV_AA);//字体设置
	        opencv_core.cvPutText(img1,x_axis_str,x_ax_down_center,font,col_platter(x_axis_str_col));
	        //put_font_tuli(img1,x_ax_down_center,x_axis_str,cvScalar(0,255,255));
	    }
	    //显示主标题
	    if(main_axis_str_flag==1)
	    {
	        CvPoint x_ax_down_center=new CvPoint(main_axis_str_x,main_axis_str_y);
	        opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.8*main_axis_str_width,0.8*main_axis_str_width,0,0,CV_AA);//字体设置
	        opencv_core.cvPutText(img1,main_axis_str,x_ax_down_center,font,col_platter(main_axis_str_col));
	        //put_font_tuli(img1,x_ax_down_center,main_axis_str,cvScalar(0,255,255));
	        //显示y标题//并纵向显示
	    }    
	        
	    //显示y上
	    if(y_axis_str_flag==1)
	    {
	        int zn=0;
	        if(y_axis_flag==1)
	        {//纵向显示
	        	opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.6*y_axis_str_width,0.6*y_axis_str_width,0,0,CV_AA);
	            CvScalar cal_y= col_platter(y_axis_str_col);
	            //System.out.println("str2\t"+str22+"\t"+str22[0]);
	            for(int i=0;i<y_axis_str.length();i++)
	            {
	                String str33=String.valueOf(i);
	                CvPoint x_ax_down_center=new CvPoint(y_axis_str_x,(int)(y_axis_str_y+height_point2+zn*12*y_axis_str_width));
	                opencv_core.cvPutText(img1,str33,x_ax_down_center,font,cal_y);
	                //put_font_tuli(img1,x_ax_down_center,str33.substr(0,1),cvScalar(0,255,255));
	                zn++;
	            }
	        }
	        else
	        {//行向显示
	        	opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.6*y_axis_str_width,0.6*y_axis_str_width,0,0,CV_AA);
	            CvPoint x_ax_down_center=new CvPoint(y_axis_str_x,y_axis_str_y+height_point2);
	            opencv_core.cvPutText(img1,String.valueOf(0),x_ax_down_center,font,col_platter(y_axis_str_col));
	            //put_font_tuli(img1,x_ax_down_center,y_axis_str,col_platter(y_axis_str_col));
	        }
	    }
	}


	/**
	 * 气泡图
	 * @param img 图片
	 * @param point 点坐标
	 * @param r 半径
	 * @param rgb 颜色
	 * @param canliang 增减参量
	 * @param tuli 是否要图例
	 */
	public void qipaotu(IplImage img,CvPoint point,double r,CvScalar rgb,double canliang,int tuli)
	{
	    int step=img.widthStep();
	    int nchan=img.nChannels();
	    int w=img.width();
	    int h=img.height();
	    double r_change=r*canliang;
	    int w_len_min,w_len_max,h_len_min,h_len_max;
	    //System.out.println("r:"+r+"\t"+r_change+"\t"+canliang);
	    if(tuli==1)//如果标记为图例则限定范围到图例绘图
	    {
	        w_len_min=(int)(point.x()-r_change-1);
	        w_len_max=(int)(point.x()+r_change+1);
	        h_len_min=(int)(point.y()-r_change-1);
	        h_len_max=(int)(point.y()+r_change+1);
	    }
	    else
	    {//否则需要减小范围到1
	         w_len_min=(int)(point.x()-r_change-1);//减小搜索范围
	    if(w_len_min<=width_point)
	        w_len_min=width_point;
	         w_len_max=(int)(point.x()+r_change+1);
	    if(w_len_max>=width-width_point2)
	        w_len_max=width-width_point2;
	         h_len_min=(int)(point.y()-r_change-1);
	    if(h_len_min<=height_point2)
	        h_len_min=height_point2;
	         h_len_max=(int)(point.y()+r_change+1);
	    if(h_len_max>=height-height_point)
	        h_len_max=height-height_point;
	    }
	    //System.out.println(h_len_min+"\t"+h_len_max);
	    //System.out.println(w_len_min+"\t"+w_len_max);


	    for(int i=h_len_min;i<h_len_max;i++)
	    {
	        for(int j=w_len_min;j<w_len_max;j++)
	        {
	            double rl=Math.sqrt(((j-point.x())*(j-point.x())+(i-point.y())*(i-point.y()))*1.0);
	            if(rl<r_change-r_change/50)
	            {
	                    int max6=0;
	                    if(rgb.blue()>max6)
	                        max6=(int)(rgb.blue());
	                    if(rgb.green()>max6)
	                        max6=(int)(rgb.green());
	                    if(rgb.red()>max6)
	                        max6=(int)(rgb.red());
	                    int max7=(int)(200-(200+max6)*1.0*(1.0-Math.pow(((r_change-rl)/r_change),1.3)));
	                    CvScalar color=cvGet2D(img,i,j);
	                    if(rgb.blue()+max7>=255)
	                      //  ((uchar)(img.imageData()))[i*step+j*nchan]=254;
	                    	color.setVal(0, 254.0);
	                    else
	                    {
	                        if(rgb.blue()+max7<=0)
	                        	color.setVal(0,1.0);
	                        
	                        else
	                        	color.setVal(0,(rgb.blue())+max7);
	                    }
	                    if(rgb.green()+max7>=255)
	                    	color.setVal(1, 254.0);
	                    else
	                    {
	                        if(rgb.green()+max7<=0)
	                        	color.setVal(1,1.0);
	                        else
	                        	color.setVal(1,(rgb.green())+max7);
	                    }
	                    if(rgb.red()+max7>=255)
	                    	color.setVal(2, 254.0);
	                    else
	                    {
	                        if(rgb.red()+max7<=0)
	                        	color.setVal(2,1.0);
	                        else
	                        	color.setVal(2,(rgb.red())+max7);
	                    }
	                /*((uchar)(img.imageData()))[i*step+j*nchan]=rgb.blue+(255-rgb.blue)*(2*r-rl)/r;
	                ((uchar)(img.imageData()))[i*step+j*nchan+1]=rgb..green()+(255-rgb..green())*(2*r-rl)/r;
	                ((uchar)(img.imageData()))[i*step+j*nchan+2]=rgb.red()+(255-rgb.red())*(2*r-rl)/r;*/
	                //}
	                    cvSet2D(img,i,j,color);
	            }   
	            else if(rl<r_change+r_change/100)
	            {
	            	  int max6=0;
	                    if(rgb.blue()>max6)
	                        max6=(int)(rgb.blue());
	                    if(rgb.green()>max6)
	                        max6=(int)(rgb.green());
	                    if(rgb.red()>max6)
	                        max6=(int)(rgb.red());
	                    int max7=(int)(200-(200+max6)*1.0*(1.0-Math.pow(((r_change-rl)/r_change),1.3)));
	                    CvScalar color=cvGet2D(img,i,j);
	                    if(rgb.blue()+max7>=255)
	                      //  ((uchar)(img.imageData()))[i*step+j*nchan]=254;
	                    	color.setVal(0, 254.0);
	                    else
	                    {
	                        if(rgb.blue()+max7<=0)
	                        	color.setVal(0,1.0);
	                        
	                        else
	                        	color.setVal(0,(rgb.blue())+max7);
	                    }
	                    if(rgb.green()+max7>=255)
	                    	color.setVal(1, 254.0);
	                    else
	                    {
	                        if(rgb.green()+max7<=0)
	                        	color.setVal(1,1.0);
	                        else
	                        	color.setVal(1,(rgb.green())+max7);
	                    }
	                    if(rgb.red()+max7>=255)
	                    	color.setVal(2, 254.0);
	                    else
	                    {
	                        if(rgb.red()+max7<=0)
	                        	color.setVal(2,1.0);
	                        else
	                        	color.setVal(2,(rgb.red())+max7);
	                    }
	            	//与背景色混合
	            	CvScalar color1=new CvScalar((color.blue()+backGroundCol.blue())/2,
	            			(color.green()+backGroundCol.green())/2,
	            			(color.red()+backGroundCol.red())/2,
	            			1.0
	            			);
	                cvSet2D(img,i,j,color1);
	            }
	            else if(rl<r_change+r_change/100)
	            {
	            	  int max6=0;
	                    if(rgb.blue()>max6)
	                        max6=(int)(rgb.blue());
	                    if(rgb.green()>max6)
	                        max6=(int)(rgb.green());
	                    if(rgb.red()>max6)
	                        max6=(int)(rgb.red());
	                    int max7=(int)(200-(200+max6)*1.0*(1.0-Math.pow(((r_change-rl)/r_change),1.3)));
	                    CvScalar color=cvGet2D(img,i,j);
	                    if(rgb.blue()+max7>=255)
	                      //  ((uchar)(img.imageData()))[i*step+j*nchan]=254;
	                    	color.setVal(0, 254.0);
	                    else
	                    {
	                        if(rgb.blue()+max7<=0)
	                        	color.setVal(0,1.0);
	                        
	                        else
	                        	color.setVal(0,(rgb.blue())+max7);
	                    }
	                    if(rgb.green()+max7>=255)
	                    	color.setVal(1, 254.0);
	                    else
	                    {
	                        if(rgb.green()+max7<=0)
	                        	color.setVal(1,1.0);
	                        else
	                        	color.setVal(1,(rgb.green())+max7);
	                    }
	                    if(rgb.red()+max7>=255)
	                    	color.setVal(2, 254.0);
	                    else
	                    {
	                        if(rgb.red()+max7<=0)
	                        	color.setVal(2,1.0);
	                        else
	                        	color.setVal(2,(rgb.red())+max7);
	                    }
	            	//与背景色混合
	            	CvScalar color1=new CvScalar((color.blue()*0.3+backGroundCol.blue()*0.7),
	            			(color.green()*0.3+backGroundCol.green()*0.7),
	            			(color.red()*0.3+backGroundCol.red()*0.7),
	            			1.0
	            			);
	                cvSet2D(img,i,j,color1);
	            }
	            //if(rl-r<1 && rl-r>=0)
	            //{
	            //    ((uchar)(img.imageData()))[i*step+j*nchan]=255;
	            //    ((uchar)(img.imageData()))[i*step+j*nchan+1]=255;
	            //    ((uchar)(img.imageData()))[i*step+j*nchan+2]=255;
	            //}
	        }
	    }
	}
	
	/**
	 * 气泡图
	 * @param img 图片
	 * @param point 点坐标
	 * @param r 半径
	 * @param rgb 颜色
	 * @param canliang 增减参量
	 * @param tuli 是否要图例
	 */
	public void qipaotu2(IplImage img,CvPoint point,double r,CvScalar rgb,double canliang,int tuli)
	{
	    int step=img.widthStep();
	    int nchan=img.nChannels();
	    int w=img.width();
	    int h=img.height();
	    double r_change=r*canliang;
	    int w_len_min,w_len_max,h_len_min,h_len_max;
	    //System.out.println("r:"+r+"\t"+r_change+"\t"+canliang);
	    if(tuli==1)//如果标记为图例则限定范围到图例绘图
	    {
	        w_len_min=(int)(point.x()-r_change-1);
	        w_len_max=(int)(point.x()+r_change+1);
	        h_len_min=(int)(point.y()-r_change-1);
	        h_len_max=(int)(point.y()+r_change+1);
	    }
	    else
	    {//否则需要减小范围到1
	         w_len_min=(int)(point.x()-r_change-1);//减小搜索范围
	    if(w_len_min<=width_point)
	        w_len_min=width_point;
	         w_len_max=(int)(point.x()+r_change+1);
	    if(w_len_max>=width-width_point2)
	        w_len_max=width-width_point2;
	         h_len_min=(int)(point.y()-r_change-1);
	    if(h_len_min<=height_point2)
	        h_len_min=height_point2;
	         h_len_max=(int)(point.y()+r_change+1);
	    if(h_len_max>=height-height_point)
	        h_len_max=height-height_point;
	    }
	    for(int i=h_len_min;i<h_len_max;i++)
	    {
	        for(int j=w_len_min;j<w_len_max;j++)
	        {
	            double rl=Math.sqrt(((j-point.x())*(j-point.x())+(i-point.y())*(i-point.y()))*1.0);
	            if(rl<r_change-r_change/50)
	            {
	                CvScalar color=new CvScalar((rgb.blue())+((255-(rgb.blue()))*(r_change-rl)/r_change),
	                		(rgb.green())+((255-(rgb.green()))*(r_change-rl)/r_change),
	                		(rgb.red())+((255-(rgb.red()))*(r_change-rl)/r_change),
	                		1.0);
	                try{
		                cvSet2D(img,i,j,color);
		            	}
		            	catch(Exception e)
		            	{
		            		System.out.println(i+"\t"+j+"\t"+"\t"+this.width+"\t"+this.height+"\t"+color.blue()+"\t"+color.green()+"\t"+color.red());
		            		//System.exit(1);
		            	}

	            }
	            else if(rl<r_change+r_change/100)
	            {
	            	CvScalar color=new CvScalar((rgb.blue())+((255-(rgb.blue()))*(r_change-rl)/r_change),
	                		(rgb.green())+((255-(rgb.green()))*(r_change-rl)/r_change),
	                		(rgb.red())+((255-(rgb.red()))*(r_change-rl)/r_change),
	                		1.0);
	            	//与背景色混合
	            	CvScalar color1=new CvScalar((color.blue()+backGroundCol.blue())/2,
	            			(color.green()+backGroundCol.green())/2,
	            			(color.red()+backGroundCol.red())/2,
	            			1.0
	            			);
	            	try{
		                cvSet2D(img,i,j,color1);
		            	}
		            	catch(Exception e)
		            	{
		            		System.out.println(i+"\t"+j+"\t"+"\t"+this.width+"\t"+this.height+"\t"+color1.blue()+"\t"+color1.green()+"\t"+color1.red());
		            		//System.exit(1);
		            	}
	            }
	            else if(rl<r_change+r_change/50)
	            {
	            	CvScalar color=new CvScalar((rgb.blue())+((255-(rgb.blue()))*(r_change-rl)/r_change),
	                		(rgb.green())+((255-(rgb.green()))*(r_change-rl)/r_change),
	                		(rgb.red())+((255-(rgb.red()))*(r_change-rl)/r_change),
	                		1.0);
	            	//与背景色混合
	            	CvScalar color1=new CvScalar((color.blue()*0.3+backGroundCol.blue()*0.7),
	            			(color.green()*0.3+backGroundCol.green()*0.7),
	            			(color.red()*0.3+backGroundCol.red()*0.7),
	            			1.0
	            			);
	            	try{
	                cvSet2D(img,i,j,color1);
	            	}
	            	catch(Exception e)
	            	{
	            		System.out.println(i+"\t"+j+"\t"+"\t"+this.width+"\t"+this.height+"\t"+color1.blue()+"\t"+color1.green()+"\t"+color1.red());
	            		//System.exit(1);
	            	}
	            }
	        }
	    }
	}
	
	
	/**
	 * 气泡图
	 * @param img 图片
	 * @param point 点坐标
	 * @param r 半径
	 * @param rgb 颜色
	 * @param canliang 增减参量
	 * @param tuli 是否要图例
	 */
	public void qipaotu3(IplImage img, CvPoint point,double r,CvScalar rgb,double canliang,int tuli)
	{
	    int step=img.widthStep();
	    int nchan=img.nChannels();
	    int w=img.width();
	    int h=img.height();
	    double r_change=r*canliang;
	    int w_len_min,w_len_max,h_len_min,h_len_max;
	    //System.out.println("r:"+r+"\t"+r_change+"\t"+canliang);
	    if(tuli==1)//如果标记为图例则限定范围到图例绘图
	    {
	        w_len_min=(int)(point.x()-r_change-1);
	        w_len_max=(int)(point.x()+r_change+1);
	        h_len_min=(int)(point.y()-r_change-1);
	        h_len_max=(int)(point.y()+r_change+1);
	    }
	    else
	    {//否则需要减小范围到1
	         w_len_min=(int)(point.x()-r_change-1);//减小搜索范围
	    if(w_len_min<=width_point)
	        w_len_min=width_point;
	         w_len_max=(int)(point.x()+r_change+1);
	    if(w_len_max>=width-width_point2)
	        w_len_max=width-width_point2;
	         h_len_min=(int)(point.y()-r_change-1);
	    if(h_len_min<=height_point2)
	        h_len_min=height_point2;
	         h_len_max=(int)(point.y()+r_change+1);
	    if(h_len_max>=height-height_point)
	        h_len_max=height-height_point;
	    }

	    for(int i=h_len_min;i<h_len_max;i++)
	    {
	        for(int j=w_len_min;j<w_len_max;j++)
	        {
	            double rl=Math.sqrt(((j-point.x())*(j-point.x())+(i-point.y())*(i-point.y()))*1.0);
	            if(rl<r_change-r_change/50)
	            {
	                //做图形叠加
	                    double max6=0.0;
	                    if(rgb.blue()>max6)
	                        max6=(rgb.blue());
	                    if(rgb.green()>max6)
	                        max6=(rgb.green());
	                    if(rgb.red()>max6)
	                        max6=(rgb.red());
	                    double max7=(200-(200+max6)*(1.0-Math.pow(((r_change-rl)/r_change),1.3)));//气泡图递减变化参数
	                    CvScalar color=cvGet2D(img,i,j);
	                    if(rgb.blue()+max7+color.blue()>=255)
	                    	color.setVal(0, 254.0);
	                    else
	                    {
	                        if(rgb.blue()+max7<=0)
	                        color.setVal(0,rgb.blue());
	                        else
	                        {
	                            color.setVal(0,+max7+color.blue());
	                        }
	                    }
	                    if(rgb.green()+max7+color.green()>=255)
	                    	color.setVal(1, 254.0);
	                    else
	                    {
	                        if(rgb.green()+max7<=0)
	                        	color.setVal(1,rgb.green());
	                        else
	                        color.setVal(1,rgb.green()+max7+color.green());
	                    }
	                    if(rgb.red()+max7+color.red()>=255)
	                    	color.setVal(2, 254.0);
	                    else
	                    {
	                        if(rgb.red()+max7<=0)
	                        		color.setVal(1,rgb.red());
	                        else
	                        {
	                        	color.setVal(1,rgb.red()+max7+color.red());
	                        }
	                    }
	                    cvSet2D(img,i,j,color);
	                }
	            else if(rl<r_change+r_change/100)
	            {
	            	double max6=0.0;
                    if(rgb.blue()>max6)
                        max6=(rgb.blue());
                    if(rgb.green()>max6)
                        max6=(rgb.green());
                    if(rgb.red()>max6)
                        max6=(rgb.red());
                    double max7=(200-(200+max6)*(1.0-Math.pow(((r_change-rl)/r_change),1.3)));//气泡图递减变化参数
                    CvScalar color=cvGet2D(img,i,j);
                    if(rgb.blue()+max7+color.blue()>=255)
                    	color.setVal(0, 254.0);
                    else
                    {
                        if(rgb.blue()+max7<=0)
                        color.setVal(0,rgb.blue());
                        else
                        {
                            color.setVal(0,+max7+color.blue());
                        }
                    }
                    if(rgb.green()+max7+color.green()>=255)
                    	color.setVal(1, 254.0);
                    else
                    {
                        if(rgb.green()+max7<=0)
                        	color.setVal(1,rgb.green());
                        else
                        color.setVal(1,rgb.green()+max7+color.green());
                    }
                    if(rgb.red()+max7+color.red()>=255)
                    	color.setVal(2, 254.0);
                    else
                    {
                        if(rgb.red()+max7<=0)
                        		color.setVal(1,rgb.red());
                        else
                        {
                        	color.setVal(1,rgb.red()+max7+color.red());
                        }
                    }
	            CvScalar color1=new CvScalar((color.blue()+backGroundCol.blue())/2,
            			(color.green()+backGroundCol.green())/2,
            			(color.red()+backGroundCol.red())/2,
            			1.0
            			);
                cvSet2D(img,i,j,color1);
	            }
	            else if(rl<r_change+r_change/50)
	            {
	            	double max6=0.0;
                    if(rgb.blue()>max6)
                        max6=(rgb.blue());
                    if(rgb.green()>max6)
                        max6=(rgb.green());
                    if(rgb.red()>max6)
                        max6=(rgb.red());
                    double max7=(200-(200+max6)*(1.0-Math.pow(((r_change-rl)/r_change),1.3)));//气泡图递减变化参数
                    CvScalar color=cvGet2D(img,i,j);
                    if(rgb.blue()+max7+color.blue()>=255)
                    	color.setVal(0, 254.0);
                    else
                    {
                        if(rgb.blue()+max7<=0)
                        color.setVal(0,rgb.blue());
                        else
                        {
                            color.setVal(0,+max7+color.blue());
                        }
                    }
                    if(rgb.green()+max7+color.green()>=255)
                    	color.setVal(1, 254.0);
                    else
                    {
                        if(rgb.green()+max7<=0)
                        	color.setVal(1,rgb.green());
                        else
                        color.setVal(1,rgb.green()+max7+color.green());
                    }
                    if(rgb.red()+max7+color.red()>=255)
                    	color.setVal(2, 254.0);
                    else
                    {
                        if(rgb.red()+max7<=0)
                        		color.setVal(1,rgb.red());
                        else
                        {
                        	color.setVal(1,rgb.red()+max7+color.red());
                        }
                    }
	            CvScalar color1=new CvScalar((color.blue()*0.3+backGroundCol.blue()*0.7),
            			(color.green()*0.3+backGroundCol.green()*0.7),
            			(color.red()*0.3+backGroundCol.red()*0.7),
            			1.0
            			);
                cvSet2D(img,i,j,color1);
	            }
	            
	            
	            }
	        }
	}


	/**
	 * 图例绘制详细信息
	 * @param img1
	 * @param col_count 对应的数量
	 * @param temp_col 对应颜色
	 * @param data_des 对应的详细信息
	 */
	public void put_des_tuli(IplImage img1,int col_count,Vector<String> temp_col,Vector<String> data_des)
	{//颜色，对应名字
	    if(legend_rec_graph_flag==1)
	    {
	        if(main_type=="qipao")
	        {
	        for(int i=0;i<col_count;i++)
	            {
	                CvPoint tuli_qipao_center=new CvPoint(legend_rec_left_x+(legend_rec_right_x-legend_rec_left_x)/3,
	                    (legend_count+1)*(legend_rec_right_y-legend_rec_left_y)/10+legend_rec_left_y);
	                legend_count++;
	                //System.out.println(tuli_qipao_center.x+"\t"+tuli_qipao_center.y);
	                double tuli_r=(legend_rec_right_y-legend_rec_left_y)*1.0/24;
	                if(tuli_r<3)
	                    tuli_r=3;

	                //System.out.println("r:"+tuli_r);
	                CvScalar tuli_col=col_platter(temp_col.get(i));
	                //System.out.println("col:"+tuli_col.blue+"\t"+tuli_col..green()+"\t"+tuli_col.red());
	                qipaotu(img1,tuli_qipao_center,tuli_r,tuli_col,1.0,1);
	                tuli_qipao_center=new CvPoint(legend_rec_left_x+2*(legend_rec_right_x-legend_rec_left_x)/3,
	                    (legend_count)*(legend_rec_right_y-legend_rec_left_y)/10+legend_rec_left_y);
	                    put_font_tuli(img1,tuli_qipao_center,data_des.get(i),new CvScalar(0.0,0.0,0.0,1.0));
	            }
	        }
	        if(main_type=="scatter")
	        {
	        for(int i=0;i<col_count;i++)
	            {
	                CvPoint tuli_qipao_center=new CvPoint(legend_rec_left_x+(legend_rec_right_x-legend_rec_left_x)/3,
	                    (legend_count+1)*(legend_rec_right_y-legend_rec_left_y)/10+legend_rec_left_y);
	                legend_count++;
	                //System.out.println(tuli_qipao_center.x+"\t"+tuli_qipao_center.y);
	                double tuli_r=(legend_rec_right_y-legend_rec_left_y)*1.0/24;
	                if(tuli_r<3)
	                    tuli_r=3;

	                //System.out.println("r:"+tuli_r);
	                CvScalar tuli_col=col_platter(temp_col.get(i));
	                //System.out.println("col:"+tuli_col.blue+"\t"+tuli_col..green()+"\t"+tuli_col.red());
	               // opencv_core.cvCircle(arg0, arg1, arg2, arg3, arg4, arg5, arg6)
	                opencv_core.cvCircle(img1,tuli_qipao_center,(int)tuli_r,tuli_col,1,0,0);
	                tuli_qipao_center=new CvPoint(legend_rec_left_x+2*(legend_rec_right_x-legend_rec_left_x)/3,
	                    (legend_count)*(legend_rec_right_y-legend_rec_left_y)/10+legend_rec_left_y);
	                //System.out.println(str33);
	                    put_font_tuli(img1,tuli_qipao_center,data_des.get(i),new CvScalar(0.0,0.0,0.0,1.0));
	            }
	        }
	        if(main_type=="qipao2")
	        {
	        for(int i=0;i<col_count;i++)
	            {
	                CvPoint tuli_qipao_center=new CvPoint(legend_rec_left_x+(legend_rec_right_x-legend_rec_left_x)/3,
	                    (legend_count+1)*(legend_rec_right_y-legend_rec_left_y)/10+legend_rec_left_y);
	                legend_count++;
	                //System.out.println(tuli_qipao_center.x+"\t"+tuli_qipao_center.y);
	                double tuli_r=(legend_rec_right_y-legend_rec_left_y)*1.0/24;
	                if(tuli_r<3)
	                    tuli_r=3;

	                //System.out.println("r:"+tuli_r);
	                CvScalar tuli_col=col_platter(temp_col.get(i));
	                //System.out.println("col:"+tuli_col.blue+"\t"+tuli_col..green()+"\t"+tuli_col.red());
	                qipaotu2(img1,tuli_qipao_center,tuli_r,tuli_col,1.0,1);
	                tuli_qipao_center=new CvPoint(legend_rec_left_x+2*(legend_rec_right_x-legend_rec_left_x)/3,
	                    (legend_count)*(legend_rec_right_y-legend_rec_left_y)/10+legend_rec_left_y);
	                //System.out.println(str33);
	                    put_font_tuli(img1,tuli_qipao_center,data_des.get(i),new CvScalar(0.0,0.0,0.0,1.0));
	            }
	        }
	        if(main_type=="qipao3")
	        {
	        for(int i=0;i<col_count;i++)
	            {
	                 CvPoint tuli_qipao_center=new CvPoint(legend_rec_left_x+(legend_rec_right_x-legend_rec_left_x)/3,
	                    (legend_count+1)*(legend_rec_right_y-legend_rec_left_y)/10+legend_rec_left_y);
	                legend_count++;
	                //System.out.println(tuli_qipao_center.x+"\t"+tuli_qipao_center.y);
	                double tuli_r=(legend_rec_right_y-legend_rec_left_y)*1.0/24;
	                if(tuli_r<3)
	                    tuli_r=3;

	                //System.out.println("r:"+tuli_r);
	                CvScalar tuli_col=col_platter(temp_col.get(i));
	                //System.out.println("col:"+tuli_col.blue+"\t"+tuli_col..green()+"\t"+tuli_col.red());
	                qipaotu3(img1,tuli_qipao_center,tuli_r,tuli_col,1.0,1);
	                tuli_qipao_center=new CvPoint(legend_rec_left_x+2*(legend_rec_right_x-legend_rec_left_x)/3,
	                    (legend_count)*(legend_rec_right_y-legend_rec_left_y)/10+legend_rec_left_y);
	                //System.out.println(str33);
	                    put_font_tuli(img1,tuli_qipao_center,data_des.get(i),new CvScalar(0.0,0.0,0.0,1.0));
	            }
	        }
	    }
	}

	
	
	//
	/**
	 * 将数值型的类型转换为配色颜色表内容
	 * 并转化为 255,255,255结构的string
	 */
	public Vector<String> color_transport(Vector<Integer> colIndex)
	{
		//计算col中的分类种类
        int flag_col=0,col_count=0;;
        Vector<Integer> tempColorCount=new Vector<Integer>();
        for(int i=0;i<colIndex.size();i++)
        {
            flag_col=0;
            for(int j=0;j<tempColorCount.size();j++)
            {
                if(colIndex.get(i)==tempColorCount.get(j))
                {
                    flag_col++;
                    break;
                }
            }
            if(flag_col==0)
            {
            	tempColorCount.add(colIndex.get(i));
                col_count++;
            }
        }
        //替换颜色
        Vector<String> colIndexString=new Vector<String>();
        Random random=new Random();
        int index=random.nextInt()%4+2;
        int col_sum=16581375;
        int col_en=col_sum/(col_count+index+Math.abs(random.nextInt())%3+1);
        int r=0,g=0,b=0;
        int flag_rgb=0;
      
      //  System.out.println("背景颜色数值:"+backgroundcol.blue());
        for(int i=index;i<col_count+index;i++)
        {
        	double i_index=0.0;
        	int i_index_count=1;
        	while(true)
        	{
	            if(flag_rgb==0)
	            {
	                flag_rgb=1;
	                r=(int)((i+i_index)*col_en)%255;
	                g=(int)((i+i_index)*col_en)/255%255;
	                b=(int)((i+i_index)*col_en)/255/255%255;
	                //判断和背景颜色差异度 
	            }
	            else
	            {
	                b=(int)((i+i_index)*col_en)%255;
	                g=(int)((i+i_index)*col_en)/255%255;
	                r=(int)((i+i_index)*col_en)/255/255%255;
	                flag_rgb=0;
	            }
	            //需要判断和背景色差异外并和其他已经配置的样色不同
	            boolean color_flag=true;//表示颜色为有效
	            for(String coln:colIndexString)
	            {
	            	 CvScalar plamColor=this.col_platter(coln);
	            	 if(Math.abs(this.backGroundCol.blue()-b)>45)
	                 {
	                 	continue;
	                 }
	                 else if(Math.abs(backGroundCol.red()-r)>25)
	                 {
	                 	continue;
	                 }
	                 else if(Math.abs(backGroundCol.green()-g)>15)
	                 {
	                 	continue;
	                 }
	                 else
	                 {
	                	 color_flag=false;
	                 	break;
	                 }
	            	 
	            }
	            if(color_flag==true)
	            {
		            if(Math.abs(backGroundCol.blue()-b)>45)
	                {
	                	break;
	                }
	                else if(Math.abs(backGroundCol.red()-r)>25)
	                {
	                	break;
	                }
	                else if(Math.abs(backGroundCol.green()-g)>15)
	                {
	                	break;
	                }
	                else
	                {
	                	color_flag=false;
	                }
	            }
	            if(color_flag==false)
	            {
	            	i_index+=0.1;
	            	if(i_index==1)
	            	{
	            		i_index_count++;
	            		i_index=i_index/10/i_index_count;
	            	}
	            	continue;
	            }
        	}
            colIndexString.add(Integer.toString(r)+","+Integer.toString(g)+","+Integer.toString(b));
        }
        System.out.println("颜色方案:"+col_count+"种类");
        
        
        Vector<String> color=new Vector<String>();
        for(int i=0;i<colIndex.size();i++)
        {
        	for(int j=0;j<tempColorCount.size();j++)
        	{
        		if(colIndex.get(i)==tempColorCount.get(j))
        		{
        			color.add(colIndexString.get(j));
        			break;
        		}
        	}
        }
        return color;
	}
	
	/**
	 * 将数值型的类型转换为配色颜色表内容
	 * 并转化为 255,255,255结构的string
	 */
	public Vector<String> color_transport2(Vector<String> colIndex)
	{
		//计算col中的分类种类
        int flag_col=0,col_count=0;;
        Vector<String> tempColorCount=new Vector<String>();
        for(int i=0;i<colIndex.size();i++)
        {
            flag_col=0;
            for(int j=0;j<tempColorCount.size();j++)
            {
                if(colIndex.get(i).equals(tempColorCount.get(j)))
                {
                    flag_col++;
                    break;
                }
            }
            if(flag_col==0)
            {
            	tempColorCount.add(colIndex.get(i));
                col_count++;
            }
        }
        //替换颜色
        Vector<String> colIndexString=new Vector<String>();
        Random random=new Random();
        int index=random.nextInt()%4+2;
        int col_sum=16581375;
        int col_en=col_sum/(col_count+index+random.nextInt()%3);
        int r=0,g=0,b=0;
        int flag_rgb=0;
        for(int i=index;i<col_count+index;i++)
        {
        	double i_index=0.0;
        	int i_index_count=1;
        	while(true)
        	{
	            if(flag_rgb==0)
	            {
	                flag_rgb=1;
	                r=(int)((i+i_index)*col_en)%255;
	                g=(int)((i+i_index)*col_en)/255%255;
	                b=(int)((i+i_index)*col_en)/255/255%255;
	                //判断和背景颜色差异度 
	            }
	            else
	            {
	                b=(int)((i+i_index)*col_en)%255;
	                g=(int)((i+i_index)*col_en)/255%255;
	                r=(int)((i+i_index)*col_en)/255/255%255;
	                flag_rgb=0;
	            }
	            //需要判断和背景色差异外并和其他已经配置的样色不同
	            boolean color_flag=true;//表示颜色为有效
	            for(String coln:colIndexString)
	            {
	            	 CvScalar plamColor=this.col_platter(coln);
	            	 if(Math.abs(this.backGroundCol.blue()-b)>45)
	                 {
	                 	continue;
	                 }
	                 else if(Math.abs(backGroundCol.red()-r)>25)
	                 {
	                 	continue;
	                 }
	                 else if(Math.abs(backGroundCol.green()-g)>15)
	                 {
	                 	continue;
	                 }
	                 else
	                 {
	                	 color_flag=false;
	                 	break;
	                 }
	            	 
	            }
	            if(color_flag==true)
	            {
		            if(Math.abs(backGroundCol.blue()-b)>45)
	                {
	                	break;
	                }
	                else if(Math.abs(backGroundCol.red()-r)>25)
	                {
	                	break;
	                }
	                else if(Math.abs(backGroundCol.green()-g)>15)
	                {
	                	break;
	                }
	                else
	                {
	                	color_flag=false;
	                }
	            }
	            if(color_flag==false)
	            {
	            	i_index+=0.1;
	            	if(i_index==1)
	            	{
	            		i_index_count++;
	            		i_index=i_index/10/i_index_count;
	            	}
	            	continue;
	            }
        	}
            colIndexString.add(Integer.toString(r)+","+Integer.toString(g)+","+Integer.toString(b));
        }
        System.out.println("颜色方案:"+col_count+"种类");
        
        
        Vector<String> color=new Vector<String>();
        for(int i=0;i<colIndex.size();i++)
        {
        	for(int j=0;j<tempColorCount.size();j++)
        	{
        		if(colIndex.get(i).equals(tempColorCount.get(j)))
        		{
        			color.add(colIndexString.get(j));
        			break;
        		}
        	}
        }
        return color;
	}
	
	
	//绘制散点气泡图
	/**
	 * img1 为图片对应的一些坐标轴信息和图片
	 * x为 x坐标
	 * y 为y坐标
	 * class_g 为绘制气泡图样式
	 * col 为颜色组
	 * len 为xy对应的长度参数
	 * datadesc 为数据的描述信息
	 *append 是否在原图上添加
	 */
	public void plot_scatter(img_trans img1,Vector<Double> x,Vector<Double> y,String class_g,Vector<String> col,Vector<Double> len,Vector<String> data_des,int append)
	{//append标记是否在已经绘制的图上绘图
	    //初始化图片
	    //x为x轴，y为y轴，class_g为绘图样式,col为颜色，len为大小
		this.setBackGroundCol();
	    System.out.println("初始化位置");
	    Vector<Integer> x_data=new Vector<Integer>();
	    Vector<Integer> y_data=new Vector<Integer>();//存储映射到图像上的位置
	    int step=img1.img1.widthStep();
	    int nchannel=img1.img1.nChannels();
	    //添加背景
	    System.out.println("更换背景");
	    if(append==0)
	    {//如果为不为增量则绘图则添加背景
	        back_ground(img1.img1,back_ground_col,back_ground_flag);
	    //绘制主窗口边框
	        System.out.println("更换主窗口");
	        main_legend_set(img1.img1);
	        //绘制图例
	        System.out.println("更换图例");
	        legend_set(img1.img1);
	        //显示main，x,y的标题
	        main_x_y_des(img1.img1);
	    }
	    //绘制行坐标
	        System.out.println("绘制坐标");
	    int filelen=(x.size());
	    System.out.println("filelen:"+filelen);
	    double canliang=(10.0/filelen);//如果数据量过大则会影响绘图效果
	    if(canliang>=1)
	        canliang=1;
	    if(canliang<=0.2)
	        canliang=0.2;
	    System.out.println("参量:"+canliang);
	    
	    CvPoint point_o=null;
	    point_o=new CvPoint(width_point,height_point);//左下角坐标
	    CvPoint point_oo=null;
	    point_o=new CvPoint(width_point2,height_point2);//右上角坐标
	    //绘制纵坐标
	    //计算最大最小值
	    double x_max=Double.MIN_VALUE;
	    double x_min=Double.MAX_VALUE;
	    double y_max=Double.MIN_VALUE;
	    double y_min=Double.MAX_VALUE;
	    int x_o;
	    int y_o;
	    if(append==0)
	    {
	        for(int i=0;i<filelen;i++)
	        {
	            if(x.get(i)>x_max)
	            {
	                x_max=x.get(i);
	            }
	            if(x.get(i)<x_min)
	            {
	                x_min=x.get(i);
	            }
	            if(y.get(i)>y_max)
	            {
	                y_max=y.get(i);
	            }
	            if(y.get(i)<y_min)
	            {
	                y_min=y.get(i);
	            }
	        }
	        double l11=x_max-x_min;
	        double l21=y_max-y_min;
	        x_max=x_max+l11/10;
	        x_min=x_min-l11/10;
	        y_max=y_max+l21/10;
	        y_min=y_min-l21/10;
	        /*System.out.println("max");
	        System.out.println(x_max+"\t"+x_min);
	        System.out.println(y_max+"\t"+y_min);*/
	        //设定画图区域为5-5
	        //将值映射到图像维度上
	        //val=width-(xi-xmin)/(xmax-xmin)*(width-width_point-width_point2)-width_point
	        //double x_max_p=x_max=(x_max-x_min)/(x_max-x_min)*(width-width_point-width_point2)+width_point;
	        //标记20个区间
	        //标记0点的实际图像位置
	    
	        if(x_min<=0 && x_max>=0)
	            x_o=(int)((0.0-x_min)*1.0/(x_max-x_min)*(width-width_point-width_point2)+width_point);
	        else
	        {
	            if(x_min>0)
	            x_o=width_point;
	            else
	            x_o=width-width_point;
	        }
	        
	        if(y_min<0)
	            y_o=(int)(height*1.0-(0.0-y_min)/(y_max-y_min)*(height-height_point-height_point2)-height_point);
	        else
	        {
	            if(x_min>0)
	            y_o=width_point;
	            else
	            y_o=height-height_point;
	        }
	        //System.out.println("--------------"+x_min+"\t"+x_max);
	        img1.value.x_min=x_min;
	        //System.out.println(img1.value.x_min);
	        img1.value.x_max=x_max;
	        //System.out.println(img1.value.x_max);
	        img1.value.y_min=y_min;
	        img1.value.y_max=y_max;
	        img1.value.x_o=x_o;
	        img1.value.y_o=y_o;
	    }
	    else
	    {//如果为增量则
	        x_min=img1.value.x_min;
	        x_max=img1.value.x_max;
	        y_min=img1.value.y_min;
	        y_max=img1.value.y_max;
	        x_o=img1.value.x_o;
	        y_o=img1.value.y_o;
	    }
	    
	    //计算映射后的x,y
	    System.out.println("计算映射位置");
	    //System.out.println(x_min+"\t"+x_max);
	    //System.out.println(y_min+"\t"+y_max);
	    //System.out.println(x_o+"\t"+y_o);
	    for(int i=0;i<filelen;i++)
	    {
	        x_data.add((int)((x.get(i)-x_min)*1.0/(x_max-x_min)*(width-width_point-width_point2)+width_point));
	        y_data.add((int)(height-(y.get(i)-y_min)*1.0/(y_max-y_min)*(height-height_point-height_point2)-height_point));
	    }
	    //计算颜色种类
	    Vector<String> temp_col=new Vector<String>();
	    int flag_col,col_count=0;;
	    for(int i=0;i<filelen;i++)
	    {
	        flag_col=0;
	        for(int j=0;j<(temp_col.size());j++)
	        {
	            if(col.get(i).equals(temp_col.get(j)))
	            {
	                flag_col++;
	                break;
	            }
	        }
	        if(flag_col==0)
	        {
	            temp_col.add(col.get(i));
	            col_count++;
	        }
	    }
	        //判断是否绘制气泡如果不是则绘制散点
	        
	        //计算大小
	        double max1=-1e10;
	        double min1=1e10;
	        double l1=0;
	        for(int i=0;i<filelen;i++)
	        {
	            l1+=len.get(i);
	        }
	        l1/=(filelen);
	        double l1_sd=0;
	        for(int i=0;i<filelen;i++)
	        {//计算方差
	            l1_sd+=(len.get(i)-l1)*(len.get(i)-l1);
	        }
	        l1_sd=Math.sqrt(l1_sd);
	        for(int i=0;i<filelen;i++)
	        {
	            if((len.get(i)-l1)/l1_sd<5)
	            {
	                if(len.get(i)>max1)
	                    max1=len.get(i);
	                if(len.get(i)<min1)
	                    min1=len.get(i);
	            }
	        }
	        double length_len=max1-min1;
	      System.out.println("绘制散点");
	        if(class_g=="scatter")
	        {
	            for(int l=0;l<filelen;l++)
	            {        
	              //  System.out.println(l+":"+x_data[l]+"\t"+y_data[l]+col.get(l));
	                CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col.get(l));
	              //  System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=(len.get(l)+1-min1)/(max1-min1)*(height-height_point)/qipao_max+(height-height_point)/qipao_min;
	              //  System.out.println(center.x+" "+center.y+"\tcal"+r);
	              //  System.out.println(width_point+"\t"+width-width_point2);
	              //  System.out.println(height_point2+"\t"+height-height_point);
	                //qipaotu(img1,center,r,cal,canliang);
	                opencv_core.cvCircle(img1.img1,center,(int)r,cal,1,0,0);
	            }
	            put_des_tuli(img1.img1,col_count,temp_col,data_des);
	        }
	        if(class_g=="qipao")
	        {
	            for(int l=0;l<filelen;l++)
	            {        
	                //System.out.println(l+":"+x_data[l]+"\t"+y_data[l]+col.get(l));
	                 CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col.get(l));
	               // System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=(len.get(l)+1-min1)/(max1-min1)*(height-height_point)/qipao_max+(height-height_point)/qipao_min;
	               // System.out.println(center.x+" "+center.y+"\tcal"+r);
	               // System.out.println(width_point+"\t"+width-width_point2);
	               // System.out.println(height_point2+"\t"+height-height_point);
	                qipaotu(img1.img1,center,r,cal,canliang,0);
	                
	            }
	            //在图例上绘制说明图样
	            //按照球体颜色绘制
	            //图例绘制坐标
	            put_des_tuli(img1.img1,col_count,temp_col,data_des);
	        }
	        if(class_g=="qipao2")
	        {
	            for(int l=0;l<filelen;l++)
	            {
	                    
	                //System.out.println(l+":"+x_data[l]+"\t"+y_data[l]+col.get(l));
	                CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col.get(l));
	               // System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=(len.get(l)+1-min1)/(max1-min1)*(height-height_point)/qipao_max+(height-height_point)/qipao_min;
	               // System.out.println(center.x+" "+center.y+"\tcal"+r);
	              //  System.out.println(width_point+"\t"+width-width_point2);
	                //System.out.println(height_point2+"\t"+height-height_point);
	                    qipaotu2(img1.img1,center,r,cal,canliang,0);
	                
	            }
	            put_des_tuli(img1.img1,col_count,temp_col,data_des);
	        }
	        if(class_g=="qipao3")
	        {
	            for(int l=0;l<filelen;l++)
	            {
	                    
	               // System.out.println(l+":"+x_datal]+"\t"+y_data[l]+col.get(l));
	                CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col.get(l));
	                //System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=(len.get(l)+1-min1)/(max1-min1)*(height-height_point)/qipao_max+(height-height_point)/qipao_min;
	                //System.out.println(center.x+" "+center.y+"\tcal"+r);
	               // System.out.println(width_point+"\t"+width-width_point2);
	              //  System.out.println(height_point2+"\t"+height-height_point);
	                    qipaotu3(img1.img1,center,r,cal,canliang,0);
	                
	            }
	            put_des_tuli(img1.img1,col_count,temp_col,data_des);        
	        }
	        //绘制x，y轴
	        if(append==0)
	        axis_x_y(img1.img1,x_o,y_o,x_min,x_max,y_min,y_max);
	        //x_o标记图像中的远点
	        //x_min是实际的大小
	    //return img1;
	}
	/**
	 * img1 为图片对应的一些坐标轴信息和图片
	 * x为 x坐标
	 * y 为y坐标
	 * class_g 为绘制气泡图样式scatter,qipao,qipao2,qipao3
	 * col 为颜色组
	 *append 是否在原图上添加
	 */
	public void plotScatter(img_trans img1,Vector<Double> x,Vector<Double> y,String class_g,Vector<Integer> col,int append)
	{//append标记是否在已经绘制的图上绘图
	    //初始化图片
	    //x为x轴，y为y轴，class_g为绘图样式,col为颜色，len为大小
		
		this.setBackGroundCol();
		
	    System.out.println("初始化位置");
	    Vector<Integer> x_data=new Vector<Integer>();
	    Vector<Integer> y_data=new Vector<Integer>();//存储映射到图像上的位置
	    int step=img1.img1.widthStep();
	    int nchannel=img1.img1.nChannels();
	    //添加背景
	    System.out.println("更换背景");
	    if(append==0)
	    {//如果为不为增量则绘图则添加背景
	        back_ground(img1.img1,back_ground_col,back_ground_flag);
	    //绘制主窗口边框
	        System.out.println("更换主窗口");
	        main_legend_set(img1.img1);
	        //绘制图例
	        System.out.println("更换图例");
	        legend_set(img1.img1);
	        //显示main，x,y的标题
	        main_x_y_des(img1.img1);
	    }
	    //绘制行坐标
	        System.out.println("绘制坐标");
	    int filelen=(x.size());
	    System.out.println("filelen:"+filelen);
	    double canliang=(40.0/filelen);//如果数据量过大则会影响绘图效果
	    if(canliang>=1)
	        canliang=0.8;
	    if(canliang<=0.3)
	        canliang=0.3;
	    System.out.println("参量:"+canliang);
	    
	    CvPoint point_o=null;
	    point_o=new CvPoint(width_point,height_point);//左下角坐标
	    CvPoint point_oo=null;
	    point_o=new CvPoint(width_point2,height_point2);//右上角坐标
	    //绘制纵坐标
	    //计算最大最小值
	    double x_max=-1e10;
	    double x_min=1e10;
	    double y_max=-1e10;
	    double y_min=1e10;
	    int x_o;
	    int y_o;
	    if(append==0)
	    {
	        for(int i=0;i<filelen;i++)
	        {
	            if(x.get(i)>x_max)
	            {
	                x_max=x.get(i);
	            }
	            if(x.get(i)<x_min)
	            {
	                x_min=x.get(i);
	            }
	            if(y.get(i)>y_max)
	            {
	                y_max=y.get(i);
	            }
	            if(y.get(i)<y_min)
	            {
	                y_min=y.get(i);
	            }
	        }
	        double l11=x_max-x_min;
	        double l21=y_max-y_min;
	        x_max=x_max+l11/10;
	        x_min=x_min-l11/10;
	        y_max=y_max+l21/10;
	        y_min=y_min-l21/10;
	        /*System.out.println("max");
	        System.out.println(x_max+"\t"+x_min);
	        System.out.println(y_max+"\t"+y_min);*/
	        //设定画图区域为5-5
	        //将值映射到图像维度上
	        //val=width-(xi-xmin)/(xmax-xmin)*(width-width_point-width_point2)-width_point
	        //double x_max_p=x_max=(x_max-x_min)/(x_max-x_min)*(width-width_point-width_point2)+width_point;
	        //标记20个区间
	        //标记0点的实际图像位置
	    
	        if(x_min<=0 && x_max>=0)
	            x_o=(int)((0.0-x_min)*1.0/(x_max-x_min)*(width-width_point-width_point2)+width_point);
	        else
	        {
	            if(x_min>0)
	            x_o=width_point;
	            else
	            x_o=width-width_point;
	        }
	        
	        if(y_min<0)
	            y_o=(int)(height*1.0-(0.0-y_min)/(y_max-y_min)*(height-height_point-height_point2)-height_point);
	        else
	        {
	            if(x_min>0)
	            y_o=width_point;
	            else
	            y_o=height-height_point;
	        }
	        //System.out.println("--------------"+x_min+"\t"+x_max);
	        img1.value.x_min=x_min;
	        //System.out.println(img1.value.x_min);
	        img1.value.x_max=x_max;
	        //System.out.println(img1.value.x_max);
	        img1.value.y_min=y_min;
	        img1.value.y_max=y_max;
	        img1.value.x_o=x_o;
	        img1.value.y_o=y_o;
	    }
	    else
	    {//如果为增量则
	        x_min=img1.value.x_min;
	        x_max=img1.value.x_max;
	        y_min=img1.value.y_min;
	        y_max=img1.value.y_max;
	        x_o=img1.value.x_o;
	        y_o=img1.value.y_o;
	    }
	    
	    //计算映射后的x,y
	    System.out.println("计算映射位置");
	    //System.out.println(x_min+"\t"+x_max);
	    //System.out.println(y_min+"\t"+y_max);
	    //System.out.println(x_o+"\t"+y_o);
	    for(int i=0;i<filelen;i++)
	    {
	        x_data.add((int)((x.get(i)-x_min)*1.0/(x_max-x_min)*(width-width_point-width_point2)+width_point));
	        y_data.add((int)(height-(y.get(i)-y_min)*1.0/(y_max-y_min)*(height-height_point-height_point2)-height_point));
	    }
	    //计算颜色种类
	    Vector<Integer> temp_col=new Vector<Integer>();
	    //存储对应的序号
	    Vector<Integer> temp_col_index=new Vector<Integer>();
	    int flag_col=0,col_count=0;;
	    for(int i=0;i<filelen;i++)
	    {
	        flag_col=0;
	        for(int j=0;j<temp_col.size();j++)
	        {
	            if(col.get(i)==temp_col.get(j))
	            {
	                flag_col++;
	                break;
	            }
	        }
	        if(flag_col==0)
	        {
	            temp_col.add(col.get(i));
	            temp_col_index.add(i);
	            col_count++;
	        }
	    }
	        //判断是否绘制气泡如果不是则绘制散点
	        
	        //计算大小
	    	//将大小整体替换为min(长,宽像素)/数据量*2
	    Vector<Double> len=new Vector<Double>();
	    Vector<String> data_des=new Vector<String>();//图例的描述
	    Vector<String> data_color=new Vector<String>();//图例的颜色
	    System.out.println("半径大小:"+Math.min(width,height)*1.0/40);
	    for(int i=0;i<filelen;i++)
	    {
	    	len.add(Math.min(width,height)*1.0/40);
	    	
	    	//将分类转化为data_des
	    	//System.out.println();
	    }

	    //计算临时色彩模板
	    Vector<String> col_trans=color_transport(col);
	    for(int i=0;i<col_count;i++)
	    {
	    	data_des.add(Integer.toString(temp_col.get(i)));
	    	data_color.add(col_trans.get(temp_col_index.get(i)));
	    }
	        System.out.println("file:"+filelen+"\t"+col_trans.size()+"\t"+col.size());
	      System.out.println("绘制散点");
	        if(class_g=="scatter")
	        {
	            for(int l=0;l<filelen;l++)
	            {        
	              //  System.out.println(l+":"+x_data[l]+"\t"+y_data[l]+col.get(l));
	                CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col_trans.get(l));
	              //  System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=len.get(l);
	              //  System.out.println(center.x+" "+center.y+"\tcal"+r);
	              //  System.out.println(width_point+"\t"+width-width_point2);
	              //  System.out.println(height_point2+"\t"+height-height_point);
	                //qipaotu(img1,center,r,cal,canliang);
	                opencv_core.cvCircle(img1.img1,center,(int)r,cal,1,0,0);
	            }
	            put_des_tuli(img1.img1,col_count,data_color,data_des);
	        }
	        if(class_g=="qipao")
	        {
	            for(int l=0;l<filelen;l++)
	            {        
	                //System.out.println(l+":"+x_data[l]+"\t"+y_data[l]+col.get(l));
	                 CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col_trans.get(l));
	               // System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=len.get(l);
	               // System.out.println(center.x+" "+center.y+"\tcal"+r);
	               // System.out.println(width_point+"\t"+width-width_point2);
	               // System.out.println(height_point2+"\t"+height-height_point);
	                qipaotu(img1.img1,center,r,cal,canliang,0);
	                
	            }
	            //在图例上绘制说明图样
	            //按照球体颜色绘制
	            //图例绘制坐标
	            put_des_tuli(img1.img1,col_count,data_color,data_des);
	        }
	        if(class_g=="qipao2")
	        {
	            for(int l=0;l<filelen;l++)
	            {
	                    
	                //System.out.println(l+":"+x_data[l]+"\t"+y_data[l]+col.get(l));
	                CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col_trans.get(l));
	               // System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=len.get(l);
	               // System.out.println(center.x+" "+center.y+"\tcal"+r);
	              //  System.out.println(width_point+"\t"+width-width_point2);
	                //System.out.println(height_point2+"\t"+height-height_point);
	                    qipaotu2(img1.img1,center,r,cal,canliang,0);
	                
	            }
	            put_des_tuli(img1.img1,col_count,data_color,data_des);
	        }
	        if(class_g=="qipao3")
	        {
	            for(int l=0;l<filelen;l++)
	            {
	                    
	               // System.out.println(l+":"+x_datal]+"\t"+y_data[l]+col.get(l));
	                CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col_trans.get(l));
	                //System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=len.get(l);
	                //System.out.println(center.x+" "+center.y+"\tcal"+r);
	               // System.out.println(width_point+"\t"+width-width_point2);
	              //  System.out.println(height_point2+"\t"+height-height_point);
	                    qipaotu3(img1.img1,center,r,cal,canliang,0);
	                
	            }
	            put_des_tuli(img1.img1,col_count,data_color,data_des);        
	        }
	        //绘制x，y轴
	        if(append==0)
	        axis_x_y(img1.img1,x_o,y_o,x_min,x_max,y_min,y_max);
	        //x_o标记图像中的远点
	        //x_min是实际的大小
	    //return img1;
	}
	

	public void plotScatter2(img_trans img1,Vector<Double> x,Vector<Double> y,String class_g,Vector<String> col,int append)
	{//append标记是否在已经绘制的图上绘图
	    //初始化图片
	    //x为x轴，y为y轴，class_g为绘图样式,col为颜色，len为大小
		
		this.setBackGroundCol();
		
	    System.out.println("初始化位置");
	    Vector<Integer> x_data=new Vector<Integer>();
	    Vector<Integer> y_data=new Vector<Integer>();//存储映射到图像上的位置
	    int step=img1.img1.widthStep();
	    int nchannel=img1.img1.nChannels();
	    //添加背景
	    System.out.println("更换背景");
	    if(append==0)
	    {//如果为不为增量则绘图则添加背景
	        back_ground(img1.img1,back_ground_col,back_ground_flag);
	    //绘制主窗口边框
	        System.out.println("更换主窗口");
	        main_legend_set(img1.img1);
	        //绘制图例
	        System.out.println("更换图例");
	        legend_set(img1.img1);
	        //显示main，x,y的标题
	        main_x_y_des(img1.img1);
	    }
	    //绘制行坐标
	        System.out.println("绘制坐标");
	    int filelen=(x.size());
	    System.out.println("filelen:"+filelen);
	    double canliang=(10.0/filelen);//如果数据量过大则会影响绘图效果
	    if(canliang>=1)
	        canliang=1;
	    if(canliang<=0.2)
	        canliang=0.2;
	    System.out.println("参量:"+canliang);
	    
	    CvPoint point_o=null;
	    point_o=new CvPoint(width_point,height_point);//左下角坐标
	    CvPoint point_oo=null;
	    point_o=new CvPoint(width_point2,height_point2);//右上角坐标
	    //绘制纵坐标
	    //计算最大最小值
	    double x_max=-1e10;
	    double x_min=1e10;
	    double y_max=-1e10;
	    double y_min=1e10;
	    int x_o;
	    int y_o;
	    if(append==0)
	    {
	        for(int i=0;i<filelen;i++)
	        {
	            if(x.get(i)>x_max)
	            {
	                x_max=x.get(i);
	            }
	            if(x.get(i)<x_min)
	            {
	                x_min=x.get(i);
	            }
	            if(y.get(i)>y_max)
	            {
	                y_max=y.get(i);
	            }
	            if(y.get(i)<y_min)
	            {
	                y_min=y.get(i);
	            }
	        }
	        double l11=x_max-x_min;
	        double l21=y_max-y_min;
	        x_max=x_max+l11/10;
	        x_min=x_min-l11/10;
	        y_max=y_max+l21/10;
	        y_min=y_min-l21/10;
	        /*System.out.println("max");
	        System.out.println(x_max+"\t"+x_min);
	        System.out.println(y_max+"\t"+y_min);*/
	        //设定画图区域为5-5
	        //将值映射到图像维度上
	        //val=width-(xi-xmin)/(xmax-xmin)*(width-width_point-width_point2)-width_point
	        //double x_max_p=x_max=(x_max-x_min)/(x_max-x_min)*(width-width_point-width_point2)+width_point;
	        //标记20个区间
	        //标记0点的实际图像位置
	    
	        if(x_min<=0 && x_max>=0)
	            x_o=(int)((0.0-x_min)*1.0/(x_max-x_min)*(width-width_point-width_point2)+width_point);
	        else
	        {
	            if(x_min>0)
	            x_o=width_point;
	            else
	            x_o=width-width_point;
	        }
	        
	        if(y_min<0)
	            y_o=(int)(height*1.0-(0.0-y_min)/(y_max-y_min)*(height-height_point-height_point2)-height_point);
	        else
	        {
	            if(x_min>0)
	            y_o=width_point;
	            else
	            y_o=height-height_point;
	        }
	        //System.out.println("--------------"+x_min+"\t"+x_max);
	        img1.value.x_min=x_min;
	        //System.out.println(img1.value.x_min);
	        img1.value.x_max=x_max;
	        //System.out.println(img1.value.x_max);
	        img1.value.y_min=y_min;
	        img1.value.y_max=y_max;
	        img1.value.x_o=x_o;
	        img1.value.y_o=y_o;
	    }
	    else
	    {//如果为增量则
	        x_min=img1.value.x_min;
	        x_max=img1.value.x_max;
	        y_min=img1.value.y_min;
	        y_max=img1.value.y_max;
	        x_o=img1.value.x_o;
	        y_o=img1.value.y_o;
	    }
	    
	    //计算映射后的x,y
	    System.out.println("计算映射位置");
	    //System.out.println(x_min+"\t"+x_max);
	    //System.out.println(y_min+"\t"+y_max);
	    //System.out.println(x_o+"\t"+y_o);
	    for(int i=0;i<filelen;i++)
	    {
	        x_data.add((int)((x.get(i)-x_min)*1.0/(x_max-x_min)*(width-width_point-width_point2)+width_point));
	        y_data.add((int)(height-(y.get(i)-y_min)*1.0/(y_max-y_min)*(height-height_point-height_point2)-height_point));
	    }
	    //计算颜色种类
	    Vector<String> temp_col=new Vector<String>();
	    //存储对应的序号
	    Vector<Integer> temp_col_index=new Vector<Integer>();
	    int flag_col=0,col_count=0;;
	    for(int i=0;i<filelen;i++)
	    {
	        flag_col=0;
	        for(int j=0;j<temp_col.size();j++)
	        {
	            if(col.get(i).equals(temp_col.get(j)))
	            {
	                flag_col++;
	                break;
	            }
	        }
	        if(flag_col==0)
	        {
	            temp_col.add(col.get(i));
	            temp_col_index.add(i);
	            col_count++;
	        }
	    }
	        //判断是否绘制气泡如果不是则绘制散点
	        
	        //计算大小
	    	//将大小整体替换为min(长,宽像素)/数据量*2
	    Vector<Double> len=new Vector<Double>();
	    Vector<String> data_des=new Vector<String>();//图例的描述
	    Vector<String> data_color=new Vector<String>();//图例的颜色
	    System.out.println("半径大小:"+Math.min(width,height)/filelen);
	    for(int i=0;i<filelen;i++)
	    {
	    	len.add(Math.min(width,height)*1.0/4);
	    	
	    	//将分类转化为data_des
	    	//System.out.println();
	    }

	    //计算临时色彩模板
	    Vector<String> col_trans=color_transport2(col);
	    for(int i=0;i<col_count;i++)
	    {
	    	data_des.add(temp_col.get(i));
	    	data_color.add(col_trans.get(temp_col_index.get(i)));
	    }
	        System.out.println("file:"+filelen+"\t"+col_trans.size()+"\t"+col.size());
	      System.out.println("绘制散点");
	        if(class_g=="scatter")
	        {
	            for(int l=0;l<filelen;l++)
	            {        
	              //  System.out.println(l+":"+x_data[l]+"\t"+y_data[l]+col.get(l));
	                CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col_trans.get(l));
	              //  System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=len.get(l);
	              //  System.out.println(center.x+" "+center.y+"\tcal"+r);
	              //  System.out.println(width_point+"\t"+width-width_point2);
	              //  System.out.println(height_point2+"\t"+height-height_point);
	                //qipaotu(img1,center,r,cal,canliang);
	                opencv_core.cvCircle(img1.img1,center,(int)r,cal,1,0,0);
	            }
	            put_des_tuli(img1.img1,col_count,data_color,data_des);
	        }
	        if(class_g=="qipao")
	        {
	            for(int l=0;l<filelen;l++)
	            {        
	                //System.out.println(l+":"+x_data[l]+"\t"+y_data[l]+col.get(l));
	                 CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col_trans.get(l));
	               // System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=len.get(l)*1.0;
	               // System.out.println(center.x+" "+center.y+"\tcal"+r);
	               // System.out.println(width_point+"\t"+width-width_point2);
	               // System.out.println(height_point2+"\t"+height-height_point);
	                qipaotu(img1.img1,center,r,cal,canliang,0);
	                
	            }
	            //在图例上绘制说明图样
	            //按照球体颜色绘制
	            //图例绘制坐标
	            put_des_tuli(img1.img1,col_count,data_color,data_des);
	        }
	        if(class_g=="qipao2")
	        {
	            for(int l=0;l<filelen;l++)
	            {
	                    
	                //System.out.println(l+":"+x_data[l]+"\t"+y_data[l]+col.get(l));
	                CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col_trans.get(l));
	               // System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=len.get(l);
	               // System.out.println(center.x+" "+center.y+"\tcal"+r);
	              //  System.out.println(width_point+"\t"+width-width_point2);
	                //System.out.println(height_point2+"\t"+height-height_point);
	                    qipaotu2(img1.img1,center,r,cal,canliang,0);
	                
	            }
	            put_des_tuli(img1.img1,col_count,data_color,data_des);
	        }
	        if(class_g=="qipao3")
	        {
	            for(int l=0;l<filelen;l++)
	            {
	                    
	               // System.out.println(l+":"+x_datal]+"\t"+y_data[l]+col.get(l));
	                CvPoint center = new CvPoint(x_data.get(l),y_data.get(l));
	                CvScalar cal=col_platter(col_trans.get(l));
	                //System.out.println("len:"+len.get(l)+"\t"+min1+"\t"+max1);
	                double r=len.get(l);
	                //System.out.println(center.x+" "+center.y+"\tcal"+r);
	               // System.out.println(width_point+"\t"+width-width_point2);
	              //  System.out.println(height_point2+"\t"+height-height_point);
	                    qipaotu3(img1.img1,center,r,cal,canliang,0);
	                
	            }
	            put_des_tuli(img1.img1,col_count,data_color,data_des);        
	        }
	        //绘制x，y轴
	        if(append==0)
	        axis_x_y(img1.img1,x_o,y_o,x_min,x_max,y_min,y_max);
	        //x_o标记图像中的远点
	        //x_min是实际的大小
	    //return img1;
	}	

	 /**
	  *  抛物线差值二次拉格朗日差值
	  * @param x
	  * @param x1
	  * @param y1
	  * @return
	  */
	public double comp_cal(int x,Vector<Double> x1,Vector<Double> y1)
	{
	    double x2=(x)*1.0;
	    double sum_1=0;
	    for(int i=0;i<3;i++)
	    {
	        double xt=1.0;
	        //if(i==0)
	        for(int j=0;j<3;j++)
	        {
	            if(i!=j)
	            {
	                xt*=(1.0*(x2-x1.get(j))/(x1.get(i)-x1.get(j)));
	            }
	        }
	        sum_1+=y1.get(i)*xt;
	    }
	    return sum_1;
	}
	////////绘制线图
	public IplImage plot_line(Vector<Vector<Double>> y,Vector<Double> x,String back_pah,int x_axis,int y_axis)//data为数据，seq为序列编号
	{
	    //初始化图片
	    //x为x轴，y为y轴，class_g为绘图样式,col为颜色，len为大小
	    IplImage img1=opencv_core.cvCreateImage(opencv_core.cvSize(width,height),8,3);
	    Vector<Vector<Integer>> y_data=new Vector<Vector<Integer>>();
	    Vector<Integer> x_data=new Vector<Integer>();//存储映射到图像上的位置
	    int step=img1.widthStep();
	    int nchannel=img1.nChannels();
	    y_break=y_axis;//全局y的间断点
	    x_break=x_axis;
	    System.out.println("初始化完成");
	    back_ground(img1,back_pah,0);
	    //cvZero(img1);
	    //绘制行坐标
	    int filelen=(x.size());
	    int length=(y.get(0).size());
	    int height_point=20;//标记纵移位置
	    int width_point=10;//标记平移位置
	    CvPoint point_o=new CvPoint(width_point,height_point);//顶点坐标
	    //绘制纵坐标
	    //计算最大最小值
	    double x_max=-1e10;
	    double x_min=1e10;
	    double y_max=-1e10;
	    double y_min=1e10;
	    for(int i=0;i<filelen;i++)
	    {
	        for(int j=0;j<length;j++)
	        {
	            if(y.get(i).get(j)>y_max)
	            {
	                y_max=y.get(i).get(j);
	            }
	            if(y.get(i).get(j)<y_min)
	            {
	                y_min=y.get(i).get(j);
	            }
	        }
	        if(x.get(i)>x_max)
	        {
	            x_max=x.get(i);
	        }
	        if(x.get(i)<x_min)
	        {
	            x_min=x.get(i);
	        }
	    }
	    /*System.out.println("max");
	    System.out.println(x_max+"\t"+x_min);
	    System.out.println(y_max+"\t"+y_min);*/
	    //设定画图区域为5-5
	    //将值映射到图像维度上
	    //val=width-(xi-xmin)/(xmax-xmin)*(width-width_point-width_point2)-width_point
	    //double x_max_p=x_max=(x_max-x_min)/(x_max-x_min)*(width-width_point-width_point2)+width_point;
	    //标记20个区间
	    //标记0点的实际图像位置
	    int x_o=0;
	    if(x_min<=0 && x_max>=0)
	        x_o=(int)((0.0-x_min)*1.0/(x_max-x_min)*(width-width_point-width_point2)+width_point);
	    else
	    {
	        if(x_min>0)
	        x_o=width_point;
	        else
	        x_o=width-width_point;
	    }
	    int y_o=0;
	    if(y_min<0)
	        y_o=(int)(height-(0.0-y_min)*1.0/(y_max-y_min)*(height-height_point-height_point2)-height_point);
	    else
	    {
	        if(y_min>0)
	        y_o=width_point;
	        else
	        y_o=height-height_point;
	    }
	    //计算映射后的x,y
	    for(int i=0;i<filelen;i++)
	    {
	        Vector<Integer> temp_x=new Vector<Integer>();
	        for(int j=0;j<length;j++)
	        {
	            temp_x.add((int)(height-(y.get(i).get(j)-y_min)*1.0/(y_max-y_min)*(height-height_point-height_point2)-height_point));
	        }
	        y_data.add(temp_x);
	        x_data.add((int)((x.get(i)-x_min)*1.0/(x_max-x_min)*(width-width_point-width_point2)+width_point));
	    }
	    //绘制xy轴
	        //System.out.println(width_point+"\t"+y_o);
	        CvPoint x_min_1=new CvPoint(width_point,y_o);
	        //System.out.println(width-width_point+"\t"+y_o);
	        CvPoint    x_max_1=new CvPoint(width-width_point,y_o);
	        //System.out.println(x_o+"\t"+height_point);
	        CvPoint    y_min_1=new CvPoint(x_o,height_point);
	        //System.out.println(x_o+"\t"+height-height_point);
	        CvPoint    y_max_1=new CvPoint(x_o,height-height_point);
	        cvLine(img1,x_min_1,x_max_1,new CvScalar(0.0,0.0,0.0,0.0),1,CV_AA,0);
	        cvLine(img1,y_min_1,y_max_1,new CvScalar(0.0,0.0,0.0,0.0),1,CV_AA,0);
	        //判断是否绘制气泡如果不是则绘制散点
	        //计算col中的分类种类
	        Vector<Double> temp_col=new Vector<Double>();
	        int col_count=length;
	        //替换颜色
	        Vector<Vector<Integer>> temp_col_bgr=new Vector<Vector<Integer>>();
	        int col_sum=16581375;
	        int col_en=col_sum/(col_count+2);
	        int r=0,g=0,b=0;
	        int flag_rgb=0;
	        for(int i=2;i<col_count+2;i++)
	        {
	            if(flag_rgb==0)
	            {
	                flag_rgb=1;
	                r=i*col_en%255;
	                g=i*col_en/255%255;
	                b=i*col_en/255/255%255;
	            }
	            else
	            {
	                b=i*col_en%255;
	                g=i*col_en/255%255;
	                r=i*col_en/255/255%255;
	                flag_rgb=0;
	            }
	            
	            Vector<Integer> temp_1=new Vector<Integer>();
	            temp_1.add(b);
	            temp_1.add(g);
	            temp_1.add(r);
	            temp_col_bgr.add(temp_1);
	        }
	        System.out.println("颜色方案:"+col_count);
	        for(int i=0;i<col_count;i++)
	        {
	            for(int j=0;j<3;j++)
	                System.out.println(temp_col_bgr.get(i).get(j)+"\t");
	            System.out.println();
	        }
	        //调用函数绘制面积图
	        //先绘制线
	        //将点拓展到图像区域中
	        //差值
	        Vector<Integer> x_data_end=new Vector<Integer>();
	        Vector<Vector<Double>> y_data_end=new Vector<Vector<Double>>();
	        for(int i=0;i<filelen-2;i++)
	        {
	            //System.out.println(i);
	            Vector<Double> temp_x=new Vector<Double>();
	            temp_x.add(x_data.get(i)*1.0);//差值输入的3个值
	            temp_x.add(x_data.get(i+1)*1.0);
	            temp_x.add(x_data.get(i+2)*1.0);
	            if(i==filelen-3)
	            {
	                Vector<Double> temp_cm=new Vector<Double>();
	                for(int j=0;j<length;j++)
	                {//存储第一个
	                    temp_cm.add(y_data.get(i).get(j)*1.0);
	                }
	                y_data_end.add(temp_cm);
	                x_data_end.add(x_data.get(i));
	                for(int m=0;m<x_data.get(i+2)-x_data.get(i);m++)
	                {
	                    Vector<Double> temp_cm1=new Vector<Double>();
	                    for(int j=0;j<length;j++)
	                    {//后面的计算
	                        Vector<Double> temp_y=new Vector<Double>();
	                        temp_y.add(y_data.get(i).get(j)*1.0);//差值输入的3个值
	                        temp_y.add(y_data.get(i+1).get(j)*1.0);
	                        temp_y.add(y_data.get(i+2).get(j)*1.0);
	                        temp_cm1.add((comp_cal(x_data.get(i)+m,temp_x,temp_y)));//计算值
	                        //if(i<1 &&j==0)
	                        //System.out.println(i+":"+j+":"+m+"\t"+x_data[i]+m+"\t值： "+comp_cal(x_data[i]+m,temp_x,temp_y)+"\t"+int(comp_cal(x_data[i]+m,temp_x,temp_y)));
	                    }
	                    x_data_end.add(x_data.get(i)+m);
	                    y_data_end.add(temp_cm1);
	                }
	            }
	            else
	            {
	                
	                for(int m=0;m<x_data.get(i+1)-x_data.get(i);m++)
	                {
	                    Vector<Double> temp_cm2=new Vector<Double>();
	                    for(int j=0;j<length;j++)
	                    {//后面的计算
	                        Vector<Double> temp_y=new Vector<Double>();
	                        temp_y.add(y_data.get(i).get(j)*1.0);//差值输入的3个值
	                        temp_y.add(y_data.get(i+1).get(j)*1.0);
	                        temp_y.add(y_data.get(i+2).get(j)*1.0);
	                        temp_cm2.add((comp_cal(x_data.get(i)+m,temp_x,temp_y)));//计算值
	                        //if(i<1 && j==0)
	                        //System.out.println(i+":"+j+":"+m+"\t"+x_data[i]+m+"\t值： "+comp_cal(x_data[i]+m,temp_x,temp_y)+"\t"+int(comp_cal(x_data[i]+m,temp_x,temp_y)));
	                    }
	                    x_data_end.add(x_data.get(i)+m);
	                    y_data_end.add(temp_cm2);
	                }
	            }
	        }
	        System.out.println("总长度:"+(y_data_end.size()));
	        //for(int i=0;i<int(y_data_end.size());i++)
	        //{
	        //    double j=0;
	        //    if(i<20)
	        //    {
	        //        System.out.println(i+":"+x_data_end[i]+":"+y_data_end[i][j]+"\t");
	        //    }
	        //}
	        System.out.println("结束差值");
	        for(int j=0;j<length;j++)
	        {
	            for(int i=0;i<(y_data_end.size())-1;i++)
	            {
	            //System.out.println(i+"::\t";
	                CvScalar cal;
	                cal=new CvScalar(temp_col_bgr.get(j).get(0),temp_col_bgr.get(j).get(1),temp_col_bgr.get(j).get(2),1.0);
	                ///绘图
	                CvPoint x_l=new CvPoint(x_data_end.get(i),y_data_end.get(i).get(j).intValue());
	                CvPoint x_r=new CvPoint(x_data_end.get(i+1),y_data_end.get(i+1).get(j).intValue());
	                cvLine(img1,x_l,x_r,cal,1,CV_AA,0);
	                    /*((uchar)(img1.imageData()))[int(y_data_end[i][j]*step)+x_data_end[i]*nchannel]=temp_col_bgr.get(j).get(0);
	                    ((uchar)(img1.imageData()))[int(y_data_end[i][j]*step)+x_data_end[i]*nchannel+1]=temp_col_bgr.get(j).get(1);
	                    ((uchar)(img1.imageData()))[int(y_data_end[i][j]*step)+x_data_end[i]*nchannel+2]=temp_col_bgr.get(j).get(2);*/
	            }
	        }
	        System.out.println("绘图结束");
	        //绘制截点以及值
	        //初始化字体
	        CvFont font=new CvFont();//初始化字体
	        opencv_core.cvInitFont(font,opencv_core.CV_FONT_HERSHEY_COMPLEX,0.35,0.35,0,0,CV_AA);//字体设置
	        for(int i=width_point+1;i<width-width_point-1;i++)
	        {//20份绘制x轴上点
	            //System.out.println("1");
	            if(Math.abs(x_o-i)%((width-width_point-width_point2)/x_break)==0)
	            {//为间断点则绘制
	                CvPoint x_min_jianduan1=new CvPoint(i,y_o);
	                CvPoint x_min_jianduan2=new CvPoint(i,y_o-height/100);
	                cvLine(img1,x_min_jianduan1,x_min_jianduan2,new CvScalar(0.0,0.0,0.0,1.0),1,CV_AA,0);
	                CvPoint x_font=new CvPoint(i-5,y_o+height/30);
	                //System.out.println("234");
	                //int(height-(0.0-y_min)/(y_max-y_min)*(height-height_point-height_point2)-height_point);
	                //double mk;
	                int mk;
	                if(x_min<=0 && x_max>=0)
	                {
	                    //System.out.println(i+"\t"+i-width_point+"\t"+width-width_point-width_point2+"\t"+x_max-x_min+"\t"+x_min);
	                    //mk=double(i-width_point)/double(width-width_point-width_point2)*(x_max-x_min)+x_min;
	                    mk=(int)((i-width_point)*1.0/(width-width_point-width_point2)*(x_max-x_min)+x_min);
	                }
	                else
	                {
	                    if(x_min>0)
	                        //mk=double(i-width_point)/double(width-width_point-width_point2)*x_max;
	                        mk=(int)((i-width_point)*1.0/(width-width_point-width_point2)*x_max);
	                    else
	                        //mk=double(i-width_point)/double(width-width_point-width_point2)*(-x_min)+x_min;
	                        mk=(int)((i-width_point)*1.0/(width-width_point-width_point2)*(-x_min)+x_min);
	                }
	                //gcvt(mk,3,ncha);
	               
	                opencv_core.cvPutText(img1,Integer.toString(mk),x_font,font,CV_RGB(0,0,0));
	            }
	        }
	        //cvInitFont(&font,CV_FONT_HERSHEY_COMPLEX,0.4,0.4,0,0,CV_AA);//字体设置
	        for(int i=height_point+1;i<height-height_point-1;i++)
	        {//20份//绘制y轴上点
	            if(Math.abs(y_o-i)%((height-height_point-height_point2)/y_break)==0)
	            {//为间断点则绘制
	                CvPoint x_min_jianduan1=new CvPoint(x_o,i);
	                CvPoint x_min_jianduan2=new CvPoint(x_o+width/200,i);
	                cvLine(img1,x_min_jianduan1,x_min_jianduan2,new CvScalar(0.0,0.0,0.0,1.0),1,CV_AA,0);
	                //cvInitFont(&font,CV_FONT_HERSHEY_COMPLEX,0.35,0.35,0,0,CV_AA);//字体设置
	                CvPoint x_font=new CvPoint(x_o+8,i+3);
	                //System.out.println("234");
	                //int(height-(0.0-y_min)/(y_max-y_min)*(height-height_point-height_point2)-height_point);
	                //double mk;
	                int mk;
	                if(y_min<=0 && y_max>=0)
	                {
	                    //mk=double(height-i+height_point)/double(height-height_point-height_point2)*(y_max-y_min)+y_min;
	                    mk=(int)((height-i+height_point)*1.0/(height-height_point-height_point2)*(y_max-y_min)+y_min);
	                }
	                else
	                {
	                    if(y_min>0)
	                        //mk=double(height-i+height_point)/double(width-height_point-height_point2)*y_max;
	                        mk=(int)((height-i+height_point)*1.0/(height-height_point-height_point2)*y_max);
	                    else
	                        //mk=double(height-i+height_point)/double(width-height_point-height_point2)*(-y_min)+y_min;
	                        mk=(int)((height-i+height_point)*1.0/(height-height_point-height_point2)*(-y_min)+y_min);
	                }
	                //gcvt(mk,3,ncha);
	                
	                opencv_core.cvPutText(img1,Integer.toString(mk),x_font,font,CV_RGB(0,0,0));
	            }
	        }
	    return img1;
	}
	//对y轴润色
	public void cvLine_runse(IplImage img1, CvPoint x_l, CvPoint x_r,CvScalar rgb)
	{
	    
	    int min=x_l.y(),max=x_l.y();
	    if(x_r.y()<min)
	        min=x_r.y();
	    if(x_r.y()>max)
	        max=x_r.y();
	    int center_len=(max-min)/2;//标记长度/2
	    int center_point=(max+min)/2;//标记中点坐标
	    for(int i=min;i<max;i++)
	    {
	        double r=Math.abs(i-center_point);
	        int max6=0;
	        if(rgb.blue()>max6)
	            max6=(int)(rgb.blue());
	        if(rgb.green()>max6)
	            max6=(int)(rgb.green());
	        if(rgb.red()>max6)
	            max6=(int)(rgb.red());
	        //double rw=200-double(200+max6)*(1.0-pow(double(center_len-r)/double(center_len),1.3));
	        CvScalar color=new CvScalar((rgb.blue())+((max6-(rgb.blue()))*Math.pow((center_len-r)/center_len,1.7)),
	        		(rgb.green())+((max6-(rgb.green()))*Math.pow((center_len-r)/center_len,1.7)),
	        		(rgb.red())+((max6-(rgb.red()))*Math.pow((center_len-r)/center_len,1.7)),
	        		1.0);
	    }
	}

	///面积图绘图

	public IplImage plot_mianji(Vector<Vector<Double>> y,Vector<Double> x,String back_pah,int x_axis,int y_axis)//data为数据，seq为序列编号
	{
	    //初始化图片
	    //x为x轴，y为y轴，class_g为绘图样式,col为颜色，len为大小
	    IplImage img1=opencv_core.cvCreateImage(opencv_core.cvSize(width,height),8,3);
	    Vector<Vector<Integer>> y_data=new Vector<Vector<Integer>>();
	    Vector<Integer> x_data=new Vector<Integer>();//存储映射到图像上的位置
	    int step=img1.widthStep();
	    int nchannel=img1.nChannels();
	    y_break=y_axis;//全局y的间断点
	    x_break=x_axis;
	    System.out.println("初始化完成");
	    back_ground(img1,back_pah,0);
	    //cvZero(img1);
	    //绘制行坐标
	    int filelen=(x.size());
	    int length=(y.get(0).size());
	    int height_point=20;//标记纵移位置
	    int width_point=10;//标记平移位置
	    CvPoint point_o;
	    point_o=new CvPoint(width_point,height_point);//顶点坐标
	    //绘制纵坐标
	    //计算最大最小值
	    double x_max=-1e10;
	    double x_min=1e10;
	    double y_max=-1e10;
	    double y_min=0.0;
	    for(int i=0;i<filelen;i++)
	    {
	        double y_sum=0.0;
	        for(int j=0;j<length;j++)
	        {
	            //y_sum+=y[i][j];    
	            y_sum+=Math.abs(y.get(i).get(j));    
	        }
	        if(y_sum>y_max)
	        {
	            y_max=y_sum;
	        }
	        if(y_sum<y_min)
	        {
	            y_min=y_sum;
	        }
	        if(x.get(i)>x_max)
	        {
	            x_max=x.get(i);
	        }
	        if(x.get(i)<x_min)
	        {
	            x_min=x.get(i);
	        }
	    }
	    System.out.println("max");
	    System.out.println(x_max+"\t"+x_min);
	    System.out.println(y_max+"\t"+y_min);
	    //设定画图区域为5-5
	    //将值映射到图像维度上
	    //val=width-(xi-xmin)/(xmax-xmin)*(width-width_point-width_point2)-width_point
	    //double x_max_p=x_max=(x_max-x_min)/(x_max-x_min)*(width-width_point-width_point2)+width_point;
	    //标记20个区间
	    //标记0点的实际图像位置
	    int x_o=0;
	    if(x_min<=0 && x_max>=0)
	        x_o=((Double)((0.0-x_min)/(x_max-x_min)*(width-width_point-width_point2)+width_point)).intValue();
	    else
	    {
	        if(x_min>0)
	        x_o=width_point;
	        else
	        x_o=width-width_point;
	    }
	    int y_o;
	    if(y_min<0)
	        y_o=(int)(height-(0.0-y_min)/(y_max-y_min)*(height-height_point-height_point2)-height_point);
	    else
	    {
	        if(y_min>0)
	        y_o=width_point;
	        else
	        y_o=height-height_point;
	    }
	    System.out.println("定点坐标"+x_o+":"+y_o);
	    //计算映射后的x,y
	    for(int i=0;i<filelen;i++)
	    {
	        Vector<Integer> temp_y=new Vector<Integer>();
	        for(int j=0;j<length;j++)
	        {
	            temp_y.add((int)(height-(y.get(i).get(j)-y_min)/(y_max-y_min)*(height-height_point-height_point2)-height_point));
	            //System.out.println("val:"+y[i][j]+":"+int(height-double(y[i][j]-y_min)/double(y_max-y_min)*(height-height_point-height_point2)-height_point));
	        }
	        y_data.add(temp_y);
	        x_data.add((int)((x.get(i)-x_min)*1.0/(x_max-x_min)*(width-width_point-width_point2)+width_point));

	        ////////
	        //////
	    }
	    //绘制xy轴
	        //System.out.println(width_point+"\t"+y_o);
	        CvPoint x_min_1=new CvPoint(width_point,y_o);
	        //System.out.println(width-width_point+"\t"+y_o);
	        CvPoint    x_max_1=new CvPoint(width-width_point,y_o);
	        //System.out.println(x_o+"\t"+height_point);
	        CvPoint    y_min_1=new CvPoint(x_o,height_point);
	        //System.out.println(x_o+"\t"+height-height_point);
	        CvPoint y_max_1=new CvPoint(x_o,height-height_point);
	        System.out.println("尺度大小x:"+x_min_1.x()+"\t"+x_min_1.y()+":"+x_max_1.x()+"\t"+x_max_1.y());
	        System.out.println("尺度大小y:"+y_min_1.x()+"\t"+y_min_1.y()+":"+y_max_1.x()+"\t"+y_max_1.y());
	        opencv_core.cvLine(img1,x_min_1,x_max_1,new CvScalar(0.0,0.0,0.0,1.0),1,CV_AA,1);
	        opencv_core.cvLine(img1,y_min_1,y_max_1,new CvScalar(0.0,0.0,0.0,1.0),1,CV_AA,1);
	        //判断是否绘制气泡如果不是则绘制散点
	        //计算col中的分类种类
	        Vector<Double> temp_col=new Vector<Double>();
	        int col_count=length;
	        //替换颜色
	        Vector<Vector<Integer>> temp_col_bgr=new Vector<Vector<Integer>>();
	        int col_sum=16581375;
	        int col_en=col_sum/(col_count+2);
	        int r=0,g=0,b=0;
	        int flag_rgb=0;
	        for(int i=2;i<col_count+2;i++)
	        {
	            if(flag_rgb==0)
	            {
	                flag_rgb=1;
	                r=i*col_en%255;
	                g=i*col_en/255%255;
	                b=i*col_en/255/255%255;
	            }
	            else
	            {
	                b=i*col_en%255;
	                g=i*col_en/255%255;
	                r=i*col_en/255/255%255;
	                flag_rgb=0;
	            }
	            
	            Vector<Integer> temp_1=new Vector<Integer>();
	            temp_1.add(b);
	            temp_1.add(g);
	            temp_1.add(r);
	            temp_col_bgr.add(temp_1);
	        }
	        System.out.println("颜色方案:"+col_count);
//	        for(int i=0;i<col_count;i++)
//	        {
//	            for(int j=0;j<3;j++)
//	                System.out.println(temp_col_bgr[i][j]+"\t";
//	            System.out.println(endl;
//	        }
	        //调用函数绘制面积图
	        //先绘制线
	        //将点拓展到图像区域中
	        //差值
	        Vector<Integer> x_data_end=new Vector<Integer>();
	        Vector<Vector<Double>> y_data_end=new Vector<Vector<Double>>();
	        for(int i=0;i<filelen-2;i++)
	        {
	            //System.out.println(i);
	            Vector<Double> temp_x=new Vector<Double>();
	            temp_x.add(x_data.get(i)*1.0);//差值输入的3个值
	            temp_x.add(x_data.get(i+1)*1.0);
	            temp_x.add(x_data.get(i+2)*1.0);
	            if(i==filelen-3)
	            {
	                Vector<Double> temp_cm=new Vector<Double>();
	                for(int j=0;j<length;j++)
	                {//存储第一个
	                    temp_cm.add(y_data.get(i).get(j)*1.0);
	                }
	                y_data_end.add(temp_cm);
	                x_data_end.add(x_data.get(i));
	                for(int m=0;m<x_data.get(i+2)-x_data.get(i);m++)
	                {
	                    Vector<Double> temp_cm1=new Vector<Double>();
	                    for(int j=0;j<length;j++)
	                    {//后面的计算
	                        Vector<Double> temp_y=new Vector<Double>();
	                        temp_y.add(y_data.get(i).get(j)*1.0);//差值输入的3个值
	                        temp_y.add(y_data.get(i+1).get(j)*1.0);
	                        temp_y.add(y_data.get(i+2).get(j)*1.0);
	                        temp_cm1.add((comp_cal(x_data.get(i)+m,temp_x,temp_y)));//计算值
	                    }
	                    x_data_end.add(x_data.get(i)+m);
	                    y_data_end.add(temp_cm1);
	                }
	            }
	            else
	            {
	                
	                for(int m=0;m<x_data.get(i+1)-x_data.get(i);m++)
	                {
	                    Vector<Double> temp_cm2=new Vector<Double>();
	                    for(int j=0;j<length;j++)
	                    {//后面的计算
	                        Vector<Double> temp_y=new Vector<Double>();
	                        temp_y.add(y_data.get(i).get(j)*1.0);//差值输入的3个值
	                        temp_y.add(y_data.get(i+1).get(j)*1.0);
	                        temp_y.add(y_data.get(i+2).get(j)*1.0);
	                        temp_cm2.add((comp_cal(x_data.get(i)+m,temp_x,temp_y)));//计算值
	                    }
	                    x_data_end.add(x_data.get(i)+m);
	                    y_data_end.add(temp_cm2);
	                }
	            }
	        }
	        System.out.println("总长度:"+y_data_end.size());
	        System.out.println("结束差值");
	        //计算上下高度
	        Vector<Integer> sum_leiji=new Vector<Integer>();
	        for(int i=0;i<(y_data_end.size());i++)
	        {
	            sum_leiji.add(height-height_point);
	        }
	        for(int i=0;i<(y_data_end.size());i++)
	        {
	            //System.out.println(i+"::\t";
	            for(int j=0;j<length;j++)
	            {
	                CvScalar cal=new CvScalar(temp_col_bgr.get(j).get(0),temp_col_bgr.get(j).get(1),temp_col_bgr.get(j).get(2),1.0);
	                ///绘图
	                //sum_leiji[i]+=int(y_data_end[i][j]);
	                CvPoint x_l=new CvPoint(x_data_end.get(i),sum_leiji.get(i));
	                sum_leiji.set(i,sum_leiji.get(i)-height-height_point-Math.abs((y_data_end.get(i).get(j)).intValue()));
	                //System.out.println("sum_leiji[]"+i+":"+sum_leiji[i]);
	                //sum_leiji[i]+=int(y_data_end[i][j+1]);
	                //线润色
	                CvPoint x_r=new CvPoint(x_data_end.get(i),sum_leiji.get(i));
	                //cvLine(img1,x_l,x_r,cal,0.1);
	                //System.out.println("i:"+i);
	                //cvLine(img1,x_l,x_r,cal,0.5);
	                cvLine_runse(img1,x_l,x_r,cal);
	            }
	        }
	        System.out.println("绘图结束");
	        //绘制截点以及值
	        //初始化字体
	        CvFont font=new CvFont();//初始化字体
	        opencv_core.cvInitFont(font,CV_FONT_HERSHEY_COMPLEX,0.35,0.35,0,0,CV_AA);//字体设置
	        for(int i=width_point+1;i<width-width_point-1;i++)
	        {//20份绘制x轴上点
	            //System.out.println("1");
	            if(Math.abs(x_o-i)%((width-width_point-width_point2)/x_break)==0)
	            {//为间断点则绘制
	                CvPoint x_min_jianduan1=new CvPoint(i,y_o);
	                CvPoint x_min_jianduan2=new CvPoint(i,y_o-height/100);
	                cvLine(img1,x_min_jianduan1,x_min_jianduan2,new CvScalar(0.0,0.0,0.0,1.0),1,CV_AA,0);
	                CvPoint x_font=new CvPoint(i-5,y_o+height/30);
	                //System.out.println("234");
	                //int(height-(0.0-y_min)/(y_max-y_min)*(height-height_point-height_point2)-height_point);
	                //double mk;
	                int mk;
	                if(x_min<=0 && x_max>=0)
	                {
	                    //System.out.println(i+"\t"+i-width_point+"\t"+width-width_point-width_point2+"\t"+x_max-x_min+"\t"+x_min);
	                    //mk=double(i-width_point)/double(width-width_point-width_point2)*(x_max-x_min)+x_min;
	                    mk=(int)((i-width_point)*1.0/(width-width_point-width_point2)*(x_max-x_min)+x_min);
	                }
	                else
	                {
	                    if(x_min>0)
	                        //mk=double(i-width_point)/double(width-width_point-width_point2)*x_max;
	                        mk=(int)((i-width_point)*1.0/(width-width_point-width_point2)*x_max);
	                    else
	                        //mk=double(i-width_point)/double(width-width_point-width_point2)*(-x_min)+x_min;
	                        mk=(int)((i-width_point)*1.0/(width-width_point-width_point2)*(-x_min)+x_min);
	                }
	                //gcvt(mk,3,ncha);
	               
	                opencv_core.cvPutText(img1,Integer.toString(mk),x_font,font,CV_RGB(0,0,0));
	            }
	        }
	        //cvInitFont(&font,CV_FONT_HERSHEY_COMPLEX,0.4,0.4,0,0,CV_AA);//字体设置
	        for(int i=height_point+1;i<height-height_point-1;i++)
	        {//20份//绘制y轴上点
	            if(Math.abs(y_o-i)%((height-height_point-height_point2)/y_break)==0)
	            {//为间断点则绘制
	                 CvPoint x_min_jianduan1=new CvPoint(x_o,i);
	                 CvPoint x_min_jianduan2=new CvPoint(x_o+width/200,i);
	                cvLine(img1,x_min_jianduan1,x_min_jianduan2,new CvScalar(0.0,0.0,0.0,1.0),1,CV_AA,1);
	                //cvInitFont(&font,CV_FONT_HERSHEY_COMPLEX,0.35,0.35,0,0,CV_AA);//字体设置
	                 CvPoint x_font=new CvPoint(x_o+8,i+3);
	                //System.out.println("234");
	                //int(height-(0.0-y_min)/(y_max-y_min)*(height-height_point-height_point2)-height_point);
	                //double mk;
	                int mk;
	                if(y_min<=0 && y_max>=0)
	                {
	                    //mk=double(height-i+height_point)/double(height-height_point-height_point2)*(y_max-y_min)+y_min;
	                    mk=(int)((height-i+height_point)*1.0/(height-height_point-height_point2)*(y_max-y_min)+y_min);
	                }
	                else
	                {
	                    if(y_min>0)
	                        //mk=double(height-i+height_point)/double(width-height_point-height_point2)*y_max;
	                        mk=(int)((height-i+height_point)*1.0/(height-height_point-height_point2)*y_max);
	                    else
	                        //mk=double(height-i+height_point)/double(width-height_point-height_point2)*(-y_min)+y_min;
	                        mk=(int)((height-i+height_point)*1.0/(height-height_point-height_point2)*(-y_min)+y_min);
	                }
	                //gcvt(mk,3,ncha);
	              
	                opencv_core.cvPutText(img1,Integer.toString(mk),x_font,font,CV_RGB(0,0,0));
	            }
	        }
	    return img1;
	}
	

	//绘图而的线绘图
	public void cvLine_runse2(IplImage img1,CvPoint x_l,CvPoint x_r,CvScalar rgb,int flag)//标记是否是中间点
	{
	    
	    int min=x_l.y(),max=x_l.y();
	    if(x_r.y()<min)
	        min=x_r.y();
	    if(x_r.y()>max)
	        max=x_r.y();
	    int center_len=(max-min)/2;//标记长度/2
	    int center_point=(max+min)/2;//标记中点坐标
	    //System.out.println("min:"+min+"\tmax:"+max);
	    for(int i=min;i<max;i++)
	    {
	        
	        double r=Math.abs(i-center_point);
	        int max6=0;
	        if(rgb.blue()>max6)
	            max6=(int)(rgb.blue());
	        if(rgb.green()>max6)
	            max6=(int)(rgb.green());
	        if(rgb.red()>max6)
	            max6=(int)(rgb.red());
	        //double rw=200-double(200+max6)*(1.0-pow(double(center_len-r)/double(center_len),1.3));
	        if(flag==0)
	        {//如果为中点点
	        //if(abs(i-x_l.y)/center_len>2 )//&& r/double(center_len)>0.3)
	        //{
	        //    /*((uchar)(img1.imageData()))[i*img1.width()Step+x_l.x*img1.nChanels()]=int(rgb.blue)+int((max6-int(rgb.blue))*pow((center_len-r)/center_len,pow((center_len-r)/center_len,0.8)*4.0+0.3));
	        //    ((uchar)(img1.imageData()))[i*img1.width()Step+x_l.x*img1.nChanels()+1]=int(rgb..green())+int((max6-int(rgb..green()))*pow((center_len-r)/center_len,pow((center_len-r)/center_len,0.8)*4.0+0.3));
	        //    ((uchar)(img1.imageData()))[i*img1.width()Step+x_l.x*img1.nChanels()+2]=int(rgb.red())+int((max6-int(rgb.red()))*pow((center_len-r)/center_len,pow((center_len-r)/center_len,0.8)*4.0+0.3));*/
	        //    ((uchar)(img1.imageData()))[i*img1.width()Step+x_l.x*img1.nChanels()]=int(rgb.blue)+int((max6-int(rgb.blue))*pow((center_len-r)/center_len,0.8));
	        //    ((uchar)(img1.imageData()))[i*img1.width()Step+x_l.x*img1.nChanels()+1]=int(rgb..green())+int((max6-int(rgb..green()))*pow((center_len-r)/center_len,0.8));
	        //    ((uchar)(img1.imageData()))[i*img1.width()Step+x_l.x*img1.nChanels()+2]=int(rgb.red())+int((max6-int(rgb.red()))*pow((center_len-r)/center_len,0.8));
	        //}
	        //else
	        //{
	            /*if(abs(i-x_l.y)/center_len<2)
	            {*/
	                double b_val=((rgb.blue())*Math.pow((Math.abs(i-x_l.y()))/center_len/2,0.55));
	                if(b_val<=0)
	                    b_val=0.0;
	                double g_val=((rgb.green())*Math.pow((Math.abs(i-x_l.y()))/center_len*2/2,0.55));
	                if(g_val<=0)
	                    g_val=0.0;
	                double r_val=((rgb.red())*Math.pow((Math.abs(i-x_l.y()))/center_len*2/2,0.55));
	                if(r_val<=0)
	                    r_val=0.0;
	                
	                CvScalar color=new CvScalar(b_val,g_val,r_val,1.0);
	                cvSet2D(img1,i,x_l.x(),color);
	               // BytePointer ll=img1.imageData();
	               
	            //}
	            //else
	            //{
	            //    ((uchar)(img1.imageData()))[i*img1.width()Step+x_l.x*img1.nChanels()]=int(rgb.blue);
	            //    ((uchar)(img1.imageData()))[i*img1.width()Step+x_l.x*img1.nChanels()+1]=int(rgb..green());
	            //    ((uchar)(img1.imageData()))[i*img1.width()Step+x_l.x*img1.nChanels()+2]=int(rgb.red());
	            //}
	        //}
	        }
	        else
	        {
	            double b_val=rgb.blue()-(rgb.blue())*Math.pow((Math.abs(center_len-r))/center_len/2,0.35);
	                if(b_val<=0)
	                    b_val=0.0;
	                double g_val=rgb.green()-(rgb.green())*Math.pow((Math.abs(center_len-r))/center_len/2,0.35);
	                if(g_val<=0)
	                    g_val=0.0;
	                double r_val=rgb.red()-(rgb.red())*Math.pow((Math.abs(center_len-r))/center_len/2,0.35);
	                if(r_val<=0)
	                    r_val=0.0;
	                CvScalar color=new CvScalar(b_val,g_val,r_val,1.0);
	                cvSet2D(img1,i,x_l.x(),color);
	                
	        }
	    }
	}
	

	////绘制面积图二

	IplImage plot_mianji2(Vector<Vector<Double>> y,Vector<Double> x,String back_pah,int x_axis,int y_axis)//data为数据，seq为序列编号
	{
	    //初始化图片
	    //x为x轴，y为y轴，class_g为绘图样式,col为颜色，len为大小
	    IplImage img1=opencv_core.cvCreateImage(opencv_core.cvSize(width,height),8,3);
	    Vector<Vector<Integer>> y_data=new Vector<Vector<Integer>>();
	    Vector<Integer> x_data=new Vector<Integer>();//存储映射到图像上的位置
	    int step=img1.widthStep();
	    int nchannel=img1.nChannels();
	    y_break=y_axis;//全局y的间断点
	    x_break=x_axis;
	    System.out.println("初始化完成");
	    back_ground(img1,back_pah,0);
	    //cvZero(img1);
	    //绘制行坐标
	    int filelen=x.size();
	    int length=y.get(0).size();
	    int height_point=20;//标记纵移位置
	    int width_point=10;//标记平移位置
	     CvPoint point_o;
	    point_o=new CvPoint(width_point,height_point);//顶点坐标
	    //绘制纵坐标
	    //计算最大最小值
	    double x_max=-1e10;
	    double x_min=1e10;
	    double y_max=-1e10;
	    double y_min=0.0;
	    for(int i=0;i<filelen;i++)
	    {
	        double y_sum=0.0;
	        for(int j=0;j<length;j++)
	        {
	            //y_sum+=y[i][j];    
	            y_sum+=Math.abs(y.get(i).get(j));    
	        }
	        if(y_sum>y_max)
	        {
	            y_max=y_sum;
	        }
	        if(y_sum<y_min)
	        {
	            y_min=y_sum;
	        }
	        if(x.get(i)>x_max)
	        {
	            x_max=x.get(i);
	        }
	        if(x.get(i)<x_min)
	        {
	            x_min=x.get(i);
	        }
	    }
	    Vector<Integer> seq_len=new Vector<Integer>();
	    Vector<Double> seq_data=new Vector<Double>();
	    for(int i=0;i<length;i++)
	    {//重新标记排序规则
	        seq_len.add(i);
	        seq_data.add(y.get(0).get(i));
	    }
	    for(int i=length-1;i>0;i--)
	    {//重新标记排序规则
	        for(int j=0;j<length-1;j++)
	        {
	            if(seq_data.get(i)>seq_data.get(j))
	            {
	                double temp=seq_data.get(j);
	                seq_data.set(j,seq_data.get(i));
	                seq_data.set(i,temp);
	                temp=seq_len.get(j);
	                seq_len.set(j,seq_data.get(i).intValue());
	                seq_len.set(i,(int)(temp));
	            }
	        }
	    }
	    System.out.println("max");
	    System.out.println(x_max+"\t"+x_min);
	    System.out.println(y_max+"\t"+y_min);
	    //设定画图区域为5-5
	    //将值映射到图像维度上
	    //val=width-(xi-xmin)/(xmax-xmin)*(width-width_point-width_point2)-width_point
	    //double x_max_p=x_max=(x_max-x_min)/(x_max-x_min)*(width-width_point-width_point2)+width_point;
	    //标记20个区间
	    //标记0点的实际图像位置
	    int x_o;
	    if(x_min<=0 && x_max>=0)
	        x_o=(int)((0.0-x_min)*1.0/(x_max-x_min)*(width-width_point-width_point2)+width_point);
	    else
	    {
	        if(x_min>0)
	        x_o=width_point;
	        else
	        x_o=width-width_point;
	    }
	    int y_o;
	    y_o=(height-height_point)/2;
	    System.out.println("定点坐标"+x_o+":"+y_o);
	    //计算映射后的x,y
	    //放大y_max
	    y_max=y_max*1.2;
	    for(int i=0;i<filelen;i++)
	    {
	        Vector<Integer> temp_y=new Vector<Integer>();
	        for(int j=0;j<length;j++)
	        {//为以统计坐标为主
	            temp_y.add((int)(height-(y.get(i).get(seq_len.get(j))-0.0)/(y_max-0)*(height-height_point-height_point2)-height_point));
	            //System.out.println("val:"+y[i][j]+":"+int(height-double(y[i][j]-y_min)/double(y_max-y_min)*(height-height_point-height_point2)-height_point));
	        }
	        y_data.add(temp_y);
	        x_data.add((int)((x.get(i)-x_min)*1.0/(x_max-x_min)*(width-width_point-width_point2)+width_point));

	        ////////
	        //////
	    }
	    
	        //判断是否绘制气泡如果不是则绘制散点
	        //计算col中的分类种类
	        Vector<Double> temp_col;
	        int col_count=length;
	        //替换颜色
	        Vector<Vector<Integer>> temp_col_bgr=new Vector<Vector<Integer>>();
	        int col_sum=16581375;
	        int col_en=col_sum/(col_count+2);
	        int r=0,g=0,b=0;
	        int flag_rgb=0;
	        for(int i=2;i<col_count+2;i++)
	        {
	            if(flag_rgb==0)
	            {
	                flag_rgb=1;
	                r=i*col_en%255;
	                g=i*col_en/255%255;
	                b=i*col_en/255/255%255;
	            }
	            else
	            {
	                b=i*col_en%255;
	                g=i*col_en/255%255;
	                r=i*col_en/255/255%255;
	                flag_rgb=0;
	            }
	            
	            Vector<Integer> temp_1=new Vector<Integer>();
	            temp_1.add(b);
	            temp_1.add(g);
	            temp_1.add(r);
	            temp_col_bgr.add(temp_1);
	        }
	        System.out.println("颜色方案:"+col_count);
	        for(int i=0;i<col_count;i++)
	        {
	            for(int j=0;j<3;j++)
	                System.out.println(temp_col_bgr.get(i).get(j)+"\t");
	            System.out.println();
	        }
	        //调用函数绘制面积图
	        //先绘制线
	        //将点拓展到图像区域中
	        //差值
	        Vector<Integer> x_data_end=new  Vector<Integer>();
	        Vector<Vector<Double>> y_data_end=new Vector<Vector<Double>>();
	        for(int i=0;i<filelen-2;i++)
	        {
	            //System.out.println(i);
	            Vector<Double> temp_x=new Vector<Double>();
	            temp_x.add(x_data.get(i)*1.0);//差值输入的3个值
	            temp_x.add(x_data.get(i+1)*1.0);
	            temp_x.add(x_data.get(i+2)*1.0);
	            if(i==filelen-3)
	            {
	                Vector<Double> temp_cm=new Vector<Double>();
	                for(int j=0;j<length;j++)
	                {//存储第一个
	                    temp_cm.add(y_data.get(i).get(j)*1.0);
	                }
	                y_data_end.add(temp_cm);
	                x_data_end.add(x_data.get(i));
	                for(int m=0;m<x_data.get(i+2)-x_data.get(i);m++)
	                {
	                    Vector<Double> temp_cm1=new Vector<Double>();
	                    for(int j=0;j<length;j++)
	                    {//后面的计算
	                        Vector<Double> temp_y=new Vector<Double>();
	                        temp_y.add(y_data.get(i).get(j)*1.0);//差值输入的3个值
	                        temp_y.add(y_data.get(i+1).get(j)*1.0);
	                        temp_y.add(y_data.get(i+2).get(j)*1.0);
	                        temp_cm1.add((comp_cal(x_data.get(i)+m,temp_x,temp_y)));//计算值
	                    }
	                    x_data_end.add(x_data.get(i)+m);
	                    y_data_end.add(temp_cm1);
	                }
	            }
	            else
	            {
	                
	                for(int m=0;m<x_data.get(i+1)-x_data.get(i);m++)
	                {
	                    Vector<Double> temp_cm2=new Vector<Double>();
	                    for(int j=0;j<length;j++)
	                    {//后面的计算
	                        Vector<Double> temp_y=new Vector<Double>();
	                        temp_y.add(y_data.get(i).get(j)*1.0);//差值输入的3个值
	                        temp_y.add(y_data.get(i+1).get(j)*1.0);
	                        temp_y.add(y_data.get(i+2).get(j)*1.0);
	                        temp_cm2.add((comp_cal(x_data.get(i)+m,temp_x,temp_y)));//计算值
	                    }
	                    x_data_end.add(x_data.get(i)+m);
	                    y_data_end.add(temp_cm2);
	                }
	            }
	        }
	        System.out.println("总长度:"+y_data_end.size());
	        //for(int i=0;i<int(y_data_end.size());i++)
	        //{
	        //    double j=0;
	        //    if(i<20)
	        //    {
	        //        System.out.println(i+":"+x_data_end[i]+":"+y_data_end[i][j]+"\t");
	        //    }
	        //}
	        System.out.println("结束差值");
	        //计算上下高度
	        Vector<Integer> sum_leiji_up=new Vector<Integer>();
	        Vector<Integer> sum_leiji_down=new Vector<Integer>();
	        int flag=0;
	       // srand(unsigned(time(0)));
	        Random random=new Random();
	        int rand_n=0;
	        sum_leiji_down.add(y_o);
	        sum_leiji_up.add(y_o);
	        int center_zero=0;
	        int rand_l=random.nextInt();
	        if(rand_l>0)
	            flag=0;
	        else
	            flag=1;
	        for(int i=0;i<(y_data_end.size());i++)
	        {
	            //System.out.println(i+"::\t";
	            //做随机扰动
	            if(i%2==0)
	            {
	            if(flag==0)
	            {
	                rand_n=Math.abs(random.nextInt()%2);
	                center_zero+=rand_n;
	                if(Math.abs(random.nextInt()%100/100.0)>0.9)
	                {
	                    flag=1;
	                }
	                if((Math.abs(center_zero))*1.0/(y_o)>0.15)
	                {
	                    flag=1;
	                }
	            }
	            if(flag==1)
	            {
	                rand_n=-Math.abs(random.nextInt()%2);
	                center_zero+=rand_n;
	                if(Math.abs(random.nextInt()%100/100.0)>0.9)
	                {
	                    flag=0;
	                }
	                if((Math.abs(center_zero))*1.0/(y_o)>0.15)
	                {
	                    flag=0;
	                }
	            }
	            }
	            //System.out.println("rand_n:"+rand_n);
	            //System.out.println("y_o+random:"+y_o+center_zero+":"+y_o-center_zero);
	            sum_leiji_down.add(y_o+center_zero);
	            sum_leiji_up.add(y_o+center_zero);//维持中点
	            for(int j=0;j<length;j++)
	            {
	                //System.out.println(endl;
	                //System.out.println("ivalue:"+i);
	                CvScalar cal;
	                cal=new CvScalar(temp_col_bgr.get(seq_len.get(j)).get(0),temp_col_bgr.get(seq_len.get(j)).get(1),temp_col_bgr.get(seq_len.get(j)).get(2),1.0);
	                ///绘图
	                //sum_leiji[i]+=int(y_data_end[i][j]);
	                //System.out.println("chandu:"+height-height_point+"\t"+int(y_data_end[i][j])+"\t";
	                int temp_su=(int)(height-height_point-(y_data_end.get(i).get(j)))/2;
	                int temp_su2=(int)(height-height_point-(y_data_end.get(i).get(j))-temp_su);
	                //System.out.println(temp_su);
	                CvPoint x_down_cen=new CvPoint(x_data_end.get(i),sum_leiji_down.get(i));
	                CvPoint x_up_cen=new CvPoint(x_data_end.get(i),sum_leiji_up.get(i));
	                //System.out.println(sum_leiji_up[i]+":"+sum_leiji_down[i]);
	                sum_leiji_up.set(i,sum_leiji_up.get(i)-rand_n-temp_su);
	                sum_leiji_down.set(i,sum_leiji_down.get(i)-rand_n+temp_su2);
	                //System.out.println(sum_leiji_up[i]+":"+sum_leiji_down[i]);
	                CvPoint x_down=new CvPoint(x_data_end.get(i),sum_leiji_down.get(i));
	                //sum_leiji[i]-=height-height_point-int(y_data_end[i][j]);
	                //System.out.println("sum_leiji[]"+i+":"+sum_leiji[i]);
	                //sum_leiji[i]+=int(y_data_end[i][j+1]);
	                //线润色
	                CvPoint x_up=new CvPoint(x_data_end.get(i),sum_leiji_up.get(i));
	                //cvLine(img1,x_l,x_r,cal,0.1);
	                //cvLine(img1,x_l,x_r,cal,0.5);
	                cvLine_runse2(img1,x_down_cen,x_down,cal,j);
	                cvLine_runse2(img1,x_up_cen,x_up,cal,j);
	            }
	        }
	        System.out.println("绘图结束");
	        //绘制xy轴
	        //System.out.println(width_point+"\t"+y_o);
	        CvPoint x_min_1=new CvPoint(width_point,y_o);
	        //System.out.println(width-width_point+"\t"+y_o);
	        CvPoint    x_max_1=new CvPoint(width-width_point,y_o);
	        //System.out.println(x_o+"\t"+height_point);
	        CvPoint    y_min_1=new CvPoint(x_o,height_point);
	        //System.out.println(x_o+"\t"+height-height_point);
	        CvPoint    y_max_1=new CvPoint(x_o,height-height_point);
	        System.out.println("尺度大小x:"+x_min_1.x()+"\t"+x_min_1.y()+":"+x_max_1.x()+"\t"+x_max_1.y());
	        System.out.println("尺度大小y:"+y_min_1.x()+"\t"+y_min_1.y()+":"+y_max_1.x()+"\t"+y_max_1.y());
	        cvLine(img1,x_min_1,x_max_1,new CvScalar(0.0,0.0,0.0,1.0),1,CV_AA,0);
	        cvLine(img1,y_min_1,y_max_1,new CvScalar(0.0,0.0,0.0,1.0),1,CV_AA,0);
	        //绘制截点以及值
	        //初始化字体
	        CvFont font=new CvFont();//初始化字体
	        opencv_core.cvInitFont(font,CV_FONT_HERSHEY_COMPLEX,0.35,0.35,0,0,CV_AA);//字体设置
	        for(int i=width_point+1;i<width-width_point-1;i++)
	        {//20份绘制x轴上点
	            //System.out.println("1");
	            if(Math.abs(x_o-i)%((width-width_point-width_point2)/x_break)==0)
	            {//为间断点则绘制
	                CvPoint x_min_jianduan1=new CvPoint(i,y_o);
	                CvPoint x_min_jianduan2=new CvPoint(i,y_o-height/100);
	                cvLine(img1,x_min_jianduan1,x_min_jianduan2,new CvScalar(0.0,0.0,0.0,1.0),1,CV_AA,0);
	                CvPoint x_font=new CvPoint(i-5,y_o+height/30);
	                //System.out.println("234");
	                //int(height-(0.0-y_min)/(y_max-y_min)*(height-height_point-height_point2)-height_point);
	                //double mk;
	                int mk;
	                if(x_min<=0 && x_max>=0)
	                {
	                    //System.out.println(i+"\t"+i-width_point+"\t"+width-width_point-width_point2+"\t"+x_max-x_min+"\t"+x_min);
	                    //mk=double(i-width_point)/double(width-width_point-width_point2)*(x_max-x_min)+x_min;
	                    mk=(int)((i-width_point)*1.0/(width-width_point-width_point2)*(x_max-x_min)+x_min);
	                }
	                else
	                {
	                    if(x_min>0)
	                        //mk=double(i-width_point)/double(width-width_point-width_point2)*x_max;
	                        mk=(int)((i-width_point)*1.0/(width-width_point-width_point2)*x_max);
	                    else
	                        //mk=double(i-width_point)/double(width-width_point-width_point2)*(-x_min)+x_min;
	                        mk=(int)((i-width_point)*1.0/(width-width_point-width_point2)*(-x_min)+x_min);
	                }
	                //gcvt(mk,3,ncha);
	                
	                opencv_core.cvPutText(img1,Integer.toString(mk),x_font,font,CV_RGB(0,255,0));
	            }
	        }
	        System.out.println("max:"+y_min+"\t"+y_max);
	        //cvInitFont(&font,CV_FONT_HERSHEY_COMPLEX,0.4,0.4,0,0,CV_AA);//字体设置
	        for(int i=height_point+1;i<height-height_point-1;i++)
	        {//20份//绘制y轴上点
	            if(Math.abs(y_o-i)%((height-height_point-height_point2)/y_break)==0)
	            {//为间断点则绘制
	                CvPoint x_min_jianduan1=new CvPoint(x_o,2*y_o-i);
	                CvPoint x_min_jianduan2=new CvPoint(x_o+width/200,2*y_o-i);
	                cvLine(img1,x_min_jianduan1,x_min_jianduan2,new CvScalar(0.0,0.0,0.0,1.0),1,CV_AA,0);
	                //cvInitFont(&font,CV_FONT_HERSHEY_COMPLEX,0.35,0.35,0,0,CV_AA);//字体设置
	                CvPoint x_font=new CvPoint(x_o+8,2*y_o-i+3);
	                //System.out.println("234");
	                //int(height-(0.0-y_min)/(y_max-y_min)*(height-height_point-height_point2)-height_point);
	                //double mk;
	                int mk;
	                if(y_min<0 && y_max>=0)
	                {
	                    //mk=double(height-i+height_point)/double(height-height_point-height_point2)*(y_max-y_min)+y_min;
	                    mk=(int)((height-i-height_point)*1.0/(height-height_point-height_point2)*(y_max-y_min)+y_min);
	                }
	                else
	                {
	                    if(y_min>=0)
	                        //mk=double(height-i+height_point)/double(width-height_point-height_point2)*y_max;
	                        mk=Math.abs((int)((y_o-i)*1.9/(height-height_point-height_point2)*y_max));
	                    else
	                        //mk=double(height-i+height_point)/double(width-height_point-height_point2)*(-y_min)+y_min;
	                        mk=(int)((height-i-height_point)*1.0/(height-height_point-height_point2)*(-y_min)+y_min);
	                }
	                //gcvt(mk,3,ncha);
	               
	                opencv_core.cvPutText(img1,Integer.toString(mk),x_font,font,CV_RGB(0,255,0));
	            }
	        }
	    return img1;
	}
	
	
	public static void main(String[] args) {
	    //IplImage img1=plot_scatter(x,y,"scatter",col,len);
		System.out.println("读取数据");
		LinkedList<String[]> data=MatrixReadSource.readToLinkedListString("data/graphData.txt");
		
		Vector<Double> x=new Vector<Double>();
		Vector<Double> y=new Vector<Double>();
		Vector<String> col=new Vector<String>();
		Vector<Double> len=new Vector<Double>();
		Vector<String> dataDesc=new Vector<String>();
		int zn=0;
		for(String[] ll:data)
		{
			zn++;
			if(ll.length==4)
			{
				System.out.println(ll[0]+"\t"+ll[1]+"\t"+ll[2]+"\t"+ll[3]);
				x.add(Double.parseDouble(ll[0]));
				y.add(Double.parseDouble(ll[1]));
				col.add(ll[2]);
				len.add(Double.parseDouble(ll[3]));
				dataDesc.add(Integer.toString(zn));
			}
		}
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
	    graph.main_type="qipao";
	    if(graph.main_type=="scatter" ||graph.main_type=="qipao" ||graph.main_type=="qipao2" ||graph.main_type=="qipao3" )
	    {
	        System.out.println("初始化图片");
	        graph.plot_scatter(img1,x,y,graph.main_type,col,len,dataDesc,0);
	    }
	    
	    
	    //IplImage img1=graph.plot_scatter(x,y,"qipao",col,len);
	    //IplImage img1=plot_scatter(x,y,"qipao2",col,len);
	    //IplImage img1=plot_scatter(x,y,"qipao3",col,len);
	    cvSaveImage("e:\\opencv_qipao2.bmp",img1.img1);
	    
	    ////////////////
	    //Vector<Vector<Double>> data=matinput_data("d:\\test\\rtest_leiji.txt");//线性图
	    //Vector<Vector<Double>> y;
	    //Vector<Double> x;
	    //for(int i=0;i<int(data.size());i++)
	    //{
	    //    Vector<Double> temp;
	    //    for(int j=0;j<int(data[0].size())-1;j++)
	    //    {
	    //        temp.add(data[i][j]);
	    //    }
	    //    y.add(temp);
	    //    x.add(data[i][int(data[0].size())-1]);
	    //}
	    //IplImage *img1=plot_line(y,x,"d:\\test\\back_ground.bmp",7,10);//x表示纵轴，y表示行
	    ///////////////
	    //Vector<Vector<Double>> data=matinput_data("d:\\test\\rtest_leiji.txt");//累计图
	    //Vector<Vector<Double>> x;
	    //Vector<Double> y;
	    //for(int i=0;i<int(data.size());i++)
	    //{
	    //    Vector<Double> temp;
	    //    for(int j=0;j<int(data[0].size())-1;j++)
	    //    {
	    //        temp.add(data[i][j]);
	    //    }
	    //    x.add(temp);
	    //    y.add(data[i][int(data[0].size())-1]);
	    //}
	    //IplImage *img1=plot_mianji(x,y,"d:\\test\\back_ground.bmp",7,10);//x表示纵轴，y表示行
	    //IplImage *img1=plot_mianji2(x,y,"d:\\test\\back_ground.bmp",7,10);//x表示纵轴，y表示行
	        // "wqy-zenhei.ttf"为文泉驿正黑体
	    cvNamedWindow("test",1);
	    cvShowImage("test",img1.img1);
	    cvWaitKey(0);
	    //cvReleaseImage(&img1.img1);  
	   //cvDestroyWindow("test");
	    
	    
	}
	
	
	
	
}
