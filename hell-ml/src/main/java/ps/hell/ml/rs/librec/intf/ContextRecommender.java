package ps.hell.ml.rs.librec.intf;

import java.util.Map;

import ps.hell.ml.rs.librec.data.ItemContext;
import ps.hell.ml.rs.librec.data.RatingContext;
import ps.hell.ml.rs.librec.data.SparseMatrix;
import ps.hell.ml.rs.librec.data.UserContext;

import com.google.common.collect.Table;

/**
 * Generic recommenders where contextual information is used. The context can be user-, item- and rating-related.
 * 
 * @author guoguibing
 * 
 */
public class ContextRecommender extends IterativeRecommender {

	/**
	 *  {user, user-context}
	 */
	protected static Map<Integer, UserContext> userContexts;
	/**
	 *  {item, item-context}
	 */
	protected static Map<Integer, ItemContext> itemContexts;
	/**
	 *  {user, item, rating-context}
	 */
	protected static Table<Integer, Integer, RatingContext> ratingContexts;

	// initialization
	static {

		// read context information here
	}

	public ContextRecommender(SparseMatrix trainMatrix, SparseMatrix testMatrix, int fold) {
		super(trainMatrix, testMatrix, fold);
	}
	
}
