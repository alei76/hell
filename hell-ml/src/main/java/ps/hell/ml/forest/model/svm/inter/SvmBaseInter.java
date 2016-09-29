package ps.hell.ml.forest.model.svm.inter;

/**
 * svm基本算法接口
 * @author Administrator
 *
 */
public interface SvmBaseInter {

	//public SvmBase(Double[][] data,double sd,double std,double c,double alpha);
    //data为输入矩阵，最后一列为输出,sd为精度，c为损失权重,alp为初始化alpha
    public void kernalGaussianCompute();//计算kernal高斯核矩阵
    public void kernalPolynomialCompute(double len);//多项式核
    public void computeWxValue();//就算对kernal变换后的wx值
    public void computeAll();//执行整个svm调度过程
    
    public void transforOutput();//将output01转换为-1 1
    public void train();//svm训练
	
	
}
