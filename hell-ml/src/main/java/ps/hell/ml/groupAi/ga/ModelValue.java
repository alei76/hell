package ps.hell.ml.groupAi.ga;

/**
 * 用来返回当前参数对应的全部模型中rmse中模型最小的数据
 * @author Administrator
 *
 */
public class ModelValue {
	/**
	 * 模型id
	 */
	private int index=0;
	/**
	 * 模型对应的rmse值
	 */
	private double rmse=Double.MAX_VALUE;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public double getRmse() {
		return rmse;
	}
	public void setRmse(double rmse) {
		this.rmse = rmse;
	}
	
	
}
