//
// svm_model
//
package ps.hell.ml.forest.model.libsvm;

import java.util.HashMap;

/**
 * svm模型基础类
 * @author Administrator
 *
 */
public class SvmModel implements java.io.Serializable
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5541907962318936137L;
	
	/**
	 * 输入参数
	 */
	public SvmParameter param;	// parameter
	/**
	 * 分类数量
	 */
	public int nr_class;		// number of classes, = 2 in regression/one class svm
	/**
	 * 支持向量数量
	 */
	public int l;			// total #SV
	/**
	 * sv数据
	 */
	public SvmNode[][] SV;	// SVs (SV[l])
	/**
	 * 误差
	 */
	public double[][] sv_coef;	// coefficients for SVs in decision functions (sv_coef[k-1][l])
	/**
	 * 常量决策函数
	 */
	public double[] rho;		// constants in decision functions (rho[k*(k-1)/2])
	/**
	 * pariwise 概率信息
	 */
	public double[] probA;         // pariwise probability information

	public double[] probB;
	/**
	 * 训练集 的目标值
	 */
	public int[] sv_indices;       // sv_indices[0,...,nSV-1] are values in [1,...,num_traning_data] to indicate SVs in the training set

	// for classification only
	/**
	 * 类的标签
	 */
	public int[] label;		// label of each class (label[k])
	public int[] nSV;		// number of SVs for each class (nSV[k])
				// nSV[0] + nSV[1] + ... + nSV[k-1] = l
	
	/**
	 * y值对应mapping
	 */
	public HashMap<Integer, String> mapString = null;

	public HashMap<String, Integer> mapStringRel = null;
	/**
	 * x的输入string
	 * key对应 从1开始
	 */
	public HashMap<Integer,HashMap<Integer,String>> indexMapString=null;
	public HashMap<Integer,HashMap<String,Integer>> indexMapStringRel=null;
	/**
	 * x对应的类
	 * 从0映射1
	 */
	public String[] xClass=null;
};
