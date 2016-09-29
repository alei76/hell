package ps.hell.ml.statistics.base;


import java.util.Arrays;
import java.util.Random;

public class Uniform implements DistributionInter {

	private double downLimit;
	private double upLimit;
	private boolean isDefault = true;

	/**
	 * 默认 方式 为0-1之间的随机数
	 */
	public Uniform() {
	}

	/**
	 * 
	 * @param downLimit
	 *            下界
	 * @param upLimit
	 *            上界
	 */
	public Uniform(double downLimit, double upLimit) {
		this.downLimit = downLimit;
		this.upLimit = upLimit;
		this.isDefault = false;
	}

	@Override
	public double getRandom() {
		Random ran = new Random();
		if (isDefault) {
			return ran.nextDouble();
		} else {
			return translate(ran.nextDouble());
		}
	}

	@Override
	/**
	 * 
	 * @param count 获取随机数的数量
	 */
	public double[] getRandom(int count) {
		// TODO Auto-generated method stub
		double[] ren = new double[count];
		Random ran = new Random();
		if (isDefault) {
			for (int i = 0; i < ren.length; i++) {
				ren[i] = ran.nextDouble();
			}
		} else {
			for (int i = 0; i < ren.length; i++) {
				ren[i] = translate(ran.nextDouble());
			}
		}
		return ren;
	}

	/**
	 * 将值转换成标准值
	 * 
	 * @param val
	 * @return
	 */
	public double translate(double val) {
		return val * (upLimit - downLimit) + downLimit;
	}

	public static void main(String[] args) {
		DistributionInter random = new Uniform(4, 7);
		double[] val = random.getRandom(10);
		System.out.print(Arrays.toString(val));
	}
}
