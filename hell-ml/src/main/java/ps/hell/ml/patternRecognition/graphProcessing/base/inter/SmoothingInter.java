package ps.hell.ml.patternRecognition.graphProcessing.base.inter;

import java.awt.image.BufferedImage;

/**
 * 滤波器接口
 * @author Administrator
 *
 */
public interface SmoothingInter {
	
	/**
	 * 高斯滤波 --灰度值
	 * @param sytle 滤波级别
	 * @param colorModel 0表示灰度 1表示彩色 3
	 * @param 5为 5*5
	 * @param 3为 3*3
	 * @param 1为 3*3均值滤波
	 * @param 2为3*3均值滤波2
	 */
	public void FilterGaussion(BufferedImage image, int colorModel, int sytle);
	
	/**
	 * 高斯滤波 --灰度值
	 * @param sytle 滤波级别
	 * @param colorModel 0表示灰度 1表示彩色 3
	 * @param 5为 5*5
	 * @param 3为 3*3
	 * @param 1为 3*3均值滤波
	 * @param 2为3*3均值滤波2
	 */
	public BufferedImage FilterGaussionGet(BufferedImage image, int colorModel, int sytle);

	/**
	 * 获取灰度值
	 * @param pixel
	 * @return
	 */
	public  int GetGrayValue(int pixel);
	
	/**
	 * 获取灰度值
	 * @param pixel
	 * @return
	 */
	public  int GetGrayValue(BufferedImage image, int x, int y);
	
	/**
	 * 转灰度图
	 * @param image
	 * @limit 如果为<0则为不二值化，都则二值化
	 */
	public void FilterGrayLevel(BufferedImage image, int limit);
	
	/**
	 * 转灰度图 返回灰度
	 * @param image
	 * @limit 如果为<0则为不二值化，都则二值化
	 */
	public BufferedImage FilterGrayLevelGet(BufferedImage image, int limit);
	
	
	/**
	 * 
	 * @param image
	 * @param count腐蚀次数
	 * 3*3
	 */
	public void FilterErosion(BufferedImage image, int count, int red);
	
	
	/**
	 * 
	 * @param image
	 * @param count膨胀次数
	 * 3*3
	 */
	public void FilterExpand(BufferedImage image, int count, int red);
	
	
	/**
	 * 只处理黑白已经二值化后的图像
	 * @param image
	 * @param    1
	 * @param  1 0 1
	 * @param    1
	 * @param  image 中red值得判断条件
	 * @param fillRed 填充色
	 * @param fillGreen填充色
	 * @param fillBlue 填充色
	 */
	public  void FilterFill(BufferedImage image, int red, int fillRed, int fillGreen, int fillBlue);
	
	/**
	 *@param 均值删除器
	 *@param 在5*5范围内如果比均值低并且 比limit值小的保留否则设置为白色
	 *@param rate 为25*比率值
	 */
	public BufferedImage FilterMeanDel(BufferedImage image, double rate, int Limitred, int LimitredWhite);

}
