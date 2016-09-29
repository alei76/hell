package ps.hell.ml.cluster.divide;
/**
 * PAM算法的问题在于伸缩性不好，需要测试所有的替换，只适用于小数据量的聚类。
   * 为了提高该算法的可伸缩性，有人提出了CLARAN算法，本质如下：
   * 从总体数据中生成多个样本数据，在每个样本数据上应用PAM算法
    *得到一组K中值点；取出所有样本中结果最好的那一组作为最后的
    *解。CLARAN算法存在的问题是，算法的聚类质量依赖于样本的质量
 * @author Administrator
 *
 */
public class Clara {

}
