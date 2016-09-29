/*******************************************************************************
 * Copyright (c) 2010 Haifeng Li
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package ps.hell.ml.gap;

/**
 * 人工染色体遗传算法/编程编码的候选人
*解决方案的优化问题。注意,染色体
*实现类似的接口来支持他们的比较健康
 * Artificial chromosomes in genetic algorithm/programming encoding candidate
 * solutions to an optimization problem. Note that chromosomes have to
 * implement Comparable interface to support comparison of their fitness.
 *
 * @author Haifeng Li
 */
public interface Chromosome extends Comparable<Chromosome> {

    /**
     * Returns the fitness of chromosome.
     */
    public double fitness();

    /**
     * Returns a new random instance.
     */
    public Chromosome newInstance();

    /**
     * Returns a pair of offsprings by crossovering this one with another one
     * according to the crossover rate, which determines how often will be
     * crossover performed. If there is no crossover, offspring is exact copy of
     * parents. Various crossover strategies can be employed.
     */
    public Chromosome[] crossover(Chromosome another);

    /**
     * For genetic algorithms, this method mutates the chromosome randomly.
     * The offspring may have no changes since the mutation rate is usually
     * very low. For Lamarckian algorithms, this method actually does the local
     * search such as such as hill-climbing.
     */
    public void mutate();
}
