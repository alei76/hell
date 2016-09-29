package ps.hell.ml.patternRecognition.algorithm.base;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
/**
 * 图像变换
 * 色差变换等
 * @author sx
 *
 */
public class TranslateImage {
	public static ColorModel sColor=ColorModel.getRGBdefault();
	/**
	 * 放大变小
	 * 伸缩变换
	 * @param image
	 * @param size 变换倍数 
	 * @param size 缩小只支持黑白图
	 * @return
	 */
	public static BufferedImage TranslateSize(BufferedImage image,double size,int limitRed)
	{
		BufferedImage image2=new BufferedImage((int)(image.getWidth()*size),(int)(image.getHeight()*size),BufferedImage.TYPE_3BYTE_BGR);
		if(size>=1.0)
		{
			for(int i=0;i<image2.getWidth();i++)
			{
				int x=(int)(i/size);
				
				for(int j=0;j<image2.getHeight();j++)
				{
					int y=(int)(j/size);
					int pixel=image.getRGB(x, y);
					image2.setRGB(i,j,pixel);
				}
			}
		}
		else
		{
			//缩小 方法为取局部区域
			int col_w=new Color(255,255,255).getRGB();
			int col_b=new Color(0,0,0).getRGB();
			//int change=(int) Math.ceil(1.0/limitRed);
			double point=(1.0/size);
			for(int i=0;i<image2.getWidth();i++)
			{
				int left;
				int right;

				if(i==image2.getWidth()-1)
				{
					 left=(int)Math.floor(i*point);
					 right=(int)Math.floor(left+point);
				}
				else
				{
					 left=(int)(i*point);
					 right=(int)(left+point);
				}
					int length=right-left;
					for(int j=0;j<image2.getHeight();j++)
					{
						//坐标转换
						int up;
						int down;
						if(j==image2.getHeight()-1)
						{
							 up=(int)Math.floor(j*point);
							 down=(int)Math.floor(up+point);
						}
						else
						{
							up=(int)(j*point);
							down=(int)(up+point);
						}
						int value=0;
						for(int m=left;m<right;m++)
						{
							for(int n=up;n<down;n++)
							{
								value+=sColor.getRed(image.getRGB(m,n));
							}
						}
						//System.out.println(value/(length*(down-up)));
						if(value/(length*(down-up))<limitRed)
						{
							image2.setRGB(i,j, col_b);
						}
						else
						{
							image2.setRGB(i,j, col_w);
						}
					}
				}
			}
		return image2;
	}
	/**
	 * 锐化
	 * @param image
	 * @return
	 */
	public static BufferedImage TranslateSharpen(BufferedImage image)
	{
		BufferedImage image2=new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
		return image2;
	}

}
