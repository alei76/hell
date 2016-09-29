package ps.hell.ml.statistics;

/**
 * 为主题模型
 * 模型公式为 
 * p(w|d)=p(w|t)*p(t|d)
 * p(词语|文档)=sum(p(词语|主题)*p(主题|文档))
 * 
 * em方法 为e 通过 词语|主题 去 计算 主题|文档
 * m 为从主题|文档 到 词语|主题 的迭代过程
 * 其中 em过程为 plsa方法
 * 

    文档集合D，topic集合T

    D中每个文档d看作一个单词序列< w1,w2,...,wn >，wi表示第i个单词，设d有n个单词。（LDA里面称之为word bag，实际上每个单词的出现位置对LDA算法无影响）

    D中涉及的所有不同单词组成一个大集合VOCABULARY（简称VOC）

LDA以文档集合D作为输入（会有切词，去停用词，取词干等常见的预处理，略去不表），希望训练出的两个结果向量（设聚成k个Topic，VOC中共包含m个词）：

    对每个D中的文档d，对应到不同topic的概率θd < pt1,..., ptk >，其中，pti表示d对应T中第i个topic的概率。计算方法是直观的，pti=nti/n，其中nti表示d中对应第i个topic的词的数目，n是d中所有词的总数。

    对每个T中的topic t，生成不同单词的概率φt < pw1,..., pwm >，其中，pwi表示t生成VOC中第i个单词的概率。计算方法同样很直观，pwi=Nwi/N，其中Nwi表示对应到topic t的VOC中第i个单词的数目，N表示所有对应到topic t的单词总数。

LDA的核心公式如下：

p(w|d) = p(w|t)*p(t|d)
 * 
 * 
 * lda方法为 统计估计方法 gibbis sample方法
 * @author Administrator
 *http://www.xuephp.com/main/detail.php?cid=34158
 */
public class LDATopic {
	/**
	 * 词语对文档概率
	 */
	private double[][] docAndTopicMatrix=null;
	
}
