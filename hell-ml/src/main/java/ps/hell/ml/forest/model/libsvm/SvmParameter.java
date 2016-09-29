package ps.hell.ml.forest.model.libsvm;
public class SvmParameter implements Cloneable,java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3862318895369009432L;
	/* svm_type */
	public static final int C_SVC = 0;
	public static final int NU_SVC = 1;
	public static final int ONE_CLASS = 2;
	public static final int EPSILON_SVR = 3;
	public static final int NU_SVR = 4;

	/* kernel_type */
	public static final int LINEAR = 0;
	public static final int POLY = 1;
	public static final int RBF = 2;
	public static final int SIGMOID = 3;
	public static final int PRECOMPUTED = 4;

	/**
	 * 使用的svm类别 
	 */
	public int svm_type;
	/**
	 * kernel 核的选择
	 */
	public int kernel_type;
	/**
	 * for poly 多项式对应的度
	 * 或则次方数
	 */
	public int degree;	// for poly 多项式对应的度
	/**
	 * for poly/rbf/sigmoid
	 */
	public double gamma;	// for poly/rbf/sigmoid
	/**
	 *  for poly/sigmoid
	 *  偏移量 一般为 1e-12
	 */
	public double coef0;	// for poly/sigmoid

	// these are for training only
	/**
	 * 缓存大小
	 */
	public double cache_size; // in MB
	/**
	 * 终止条件
	 */
	public double eps;	// stopping criteria
	/**
	 * for C_SVC, EPSILON_SVR and NU_SVR
	 * c值越大 越容易过拟合
	 */
	public double C;	// for C_SVC, EPSILON_SVR and NU_SVR
	/**
	 * for C_SVC
	 */
	public int nr_weight;		// for C_SVC
	/**
	 * for C_SVC
	 */
	public int[] weight_label;	// for C_SVC
	/**
	 * for C_SVC
	 */
	public double[] weight;		// for C_SVC
	/**
	 * for NU_SVC, ONE_CLASS, and NU_SVR
	 */
	public double nu;	// for NU_SVC, ONE_CLASS, and NU_SVR
	/**
	 * for EPSILON_SVR
	 * 邻域svr
	 */
	public double p;	// for EPSILON_SVR
	/**
	 * 启发式 收缩率
	 */
	public int shrinking;	// use the shrinking heuristics
	/**
	 * 估计
	 */
	public int probability; // do probability estimates

	@Override
	public Object clone() 
	{
		try 
		{
			return super.clone();
		} catch (CloneNotSupportedException e) 
		{
			return null;
		}
	}

}
