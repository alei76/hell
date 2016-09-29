package ps.hell.ml.forest.model.libsvm;
/**
 * 每一个参数的值 支持稀疏输入方法
 * 但是如果是svmNode[] 则必须是index小的在前面 不支持复杂（）可修改
 * @author Administrator
 *
 */
public class SvmNode implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5251603274817304513L;
	/**
	 * 第几维度
	 */
	public int index;
	/**
	 * 对应值
	 */
	public double value;
	
	@Override
	public String toString(){
	    return "["+index+","+value+"]";
	}
}
