package ps.hell.ml.groupAi;


/**
 * #SA退火算法（最小值点）
 *	#是比较直接但是全依靠随机数做步长的随机模拟算法
 *	#以下一次的适应度函数是否最小，确定新x是否被接受
 *	#以exp(-(fnew-fold)/temp)与很小的随机数作为在不满足上条件的情况下跳出该点，如没跳出则x不更新
 *	#下述为求min(20+x1^2+x2^2-10*(cos(2*pi*x1)+cos(2*pi*x2)))
 *	SA模拟退火算法R代码（为精度和特殊函数，增加了循环次数和结构）
 *	#其中0，0，为最小值点为0
 * @author Administrator
 *
 */
public class Sa {

}
