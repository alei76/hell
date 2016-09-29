package ps.hell.ml.forest.model.libsvm;
/**
 * 
 * @author Administrator
 *
 */
public class SvmProblem implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1936349532908623522L;
	public int l;
	public double[] y;
	public SvmNode[][] x;
}
