package ps.hell.ml.dist.base;

/**
 * 基于混合的相似度计算：LogLikelihoodSimilarity*TrackItemSimilarity。代码如下 一般只计算物品与物品之间的相似度
 * 请查看 mahout中的数据
 * 
 * @author Administrator
 *
 */
public class HybridSimilarity {

	private LogLikelihoodRatioSimilarity cfSimilarity = null;
	private TrackItemSimilarity contentSimilarity = null;

	public HybridSimilarity(LogLikelihoodRatioSimilarity cfSimilarity,
			TrackItemSimilarity contentSimilarity) {
		this.cfSimilarity = cfSimilarity;
		this.contentSimilarity = contentSimilarity;
	}

	public double itemSimilarity(long itemID1, long itemID2, int k11, int k12,
			int k21, int k22) {
		return contentSimilarity.itemSimilarity(itemID1, itemID2)
				* cfSimilarity.logLikelihoodRatio(k11, k12, k21, k22);
	}

	// @Override
	// public double[] itemSimilarities(long itemID1, long[] itemID2s) throws
	// TasteException {
	// double[] result = contentSimilarity.itemSimilarities(itemID1, itemID2s);
	// double[] multipliers = cfSimilarity.itemSimilarities(itemID1, itemID2s);
	// for (int i = 0; i < result.length; i++) {
	// result[i] *= multipliers[i];
	// }
	// return result;
	// }
}
