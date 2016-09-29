package ps.hell.ml.forest.classification.decisionTree.mahout;

import java.util.Random;

/**
 * Abstract base class for TreeBuilders
 */
public interface TreeBuilder {
  
  /**
   * Builds a Decision tree using the training data
   * 
   * @param rng
   *          random-numbers generator
   * @param data
   *          training data
   * @return root Node
   */
  Node build(Random rng, Data data);
  
}