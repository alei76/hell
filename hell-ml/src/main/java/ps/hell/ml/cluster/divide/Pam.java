package ps.hell.ml.cluster.divide;

/**
 * PAM（Partition Around Medoids）是K-medoid的基础算法，
 * 基本流程如下：首先随机选择k个对象作为中心，
 * 把每个对象分配给离它最近的中心。然后随机地选择一个非中
 * 心对象替换中心对象，计算分配后的距离改进量。聚类的过程
 * 就是不断迭代，进行中心对象和非中心对象的反复替换过程，
 * 直到目标函数不再有改进为止。非中心点和中心点替换的具体
 * 类别如下图分析（用h替换i相对j的开销）。
 * @author Administrator
 *
 */
public class Pam {

}
