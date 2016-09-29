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
package ps.hell.ml.forest.regression.test;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ps.hell.base.data.AttributeDataset;
import ps.hell.base.data.parse.ArffParser;
import ps.hell.ml.forest.regression.GradientTreeBoost;
import ps.hell.math.base.MathBase;
import ps.hell.ml.statistics.validation.CrossValidation;
import ps.hell.ml.statistics.validation.Validation;
import ps.hell.base.sort.QuickSort;

/**
 *
 * @author Haifeng Li
 */
public class GradientTreeBoostTest {
    
    public GradientTreeBoostTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    public void test(GradientTreeBoost.Loss loss, String dataset, String url, int response) {
        System.out.println(dataset + "\t" + loss);
        ArffParser parser = new ArffParser();
        parser.setResponseIndex(response);
        try {
            AttributeDataset data = parser.parse(this.getClass().getResourceAsStream(url));
            double[] datay = data.toArray(new double[data.size()]);
            double[][] datax = data.toArray(new double[data.size()][]);
            
            int n = datax.length;
            int k = 10;

            CrossValidation cv = new CrossValidation(n, k);
            double rss = 0.0;
            double ad = 0.0;
            for (int i = 0; i < k; i++) {
                double[][] trainx = MathBase.slice(datax, cv.train[i]);
                double[] trainy = MathBase.slice(datay, cv.train[i]);
                double[][] testx = MathBase.slice(datax, cv.test[i]);
                double[] testy = MathBase.slice(datay, cv.test[i]);

                GradientTreeBoost boost = new GradientTreeBoost(data.attributes(), trainx, trainy, loss, 100, 6, 0.05, 0.7);

                for (int j = 0; j < testx.length; j++) {
                    double r = testy[j] - boost.predict(testx[j]);
                    ad += MathBase.abs(r);
                    rss += r * r;
                }
            }

            System.out.format("10-CV RMSE = %.4f \t AbsoluteDeviation = %.4f\n", MathBase.sqrt(rss/n), ad/n);
         } catch (Exception ex) {
             System.err.println(ex);
         }
    }
    
    /**
     * Test of learn method, of class RegressionTree.
     */
    @Test
    public void testLS() {
        test(GradientTreeBoost.Loss.LeastSquares, "CPU", "/smile/data/weka/cpu.arff", 6);
        //test(GradientTreeBoost.Loss.LeastSquares, "2dplanes", "/smile/data/weka/regression/2dplanes.arff", 6);
        //test(GradientTreeBoost.Loss.LeastSquares, "abalone", "/smile/data/weka/regression/abalone.arff", 8);
        //test(GradientTreeBoost.Loss.LeastSquares, "ailerons", "/smile/data/weka/regression/ailerons.arff", 40);
        //test(GradientTreeBoost.Loss.LeastSquares, "bank32nh", "/smile/data/weka/regression/bank32nh.arff", 32);
        test(GradientTreeBoost.Loss.LeastSquares, "autoMPG", "/smile/data/weka/regression/autoMpg.arff", 7);
        test(GradientTreeBoost.Loss.LeastSquares, "cal_housing", "/smile/data/weka/regression/cal_housing.arff", 8);
        //test(GradientTreeBoost.Loss.LeastSquares, "puma8nh", "/smile/data/weka/regression/puma8nh.arff", 8);
        //test(GradientTreeBoost.Loss.LeastSquares, "kin8nm", "/smile/data/weka/regression/kin8nm.arff", 8);
    }
    
    /**
     * Test of learn method, of class RegressionTree.
     */
    @Test
    public void testLAD() {
        test(GradientTreeBoost.Loss.LeastAbsoluteDeviation, "CPU", "/smile/data/weka/cpu.arff", 6);
        //test(GradientTreeBoost.Loss.LeastAbsoluteDeviation, "2dplanes", "/smile/data/weka/regression/2dplanes.arff", 6);
        //test(GradientTreeBoost.Loss.LeastAbsoluteDeviation, "abalone", "/smile/data/weka/regression/abalone.arff", 8);
        //test(GradientTreeBoost.Loss.LeastAbsoluteDeviation, "ailerons", "/smile/data/weka/regression/ailerons.arff", 40);
        //test(GradientTreeBoost.Loss.LeastAbsoluteDeviation, "bank32nh", "/smile/data/weka/regression/bank32nh.arff", 32);
        test(GradientTreeBoost.Loss.LeastAbsoluteDeviation, "autoMPG", "/smile/data/weka/regression/autoMpg.arff", 7);
        test(GradientTreeBoost.Loss.LeastAbsoluteDeviation, "cal_housing", "/smile/data/weka/regression/cal_housing.arff", 8);
        //test(GradientTreeBoost.Loss.LeastAbsoluteDeviation, "puma8nh", "/smile/data/weka/regression/puma8nh.arff", 8);
        //test(GradientTreeBoost.Loss.LeastAbsoluteDeviation, "kin8nm", "/smile/data/weka/regression/kin8nm.arff", 8);
    }
    
    /**
     * Test of learn method, of class RegressionTree.
     */
    @Test
    public void testHuber() {
        test(GradientTreeBoost.Loss.Huber, "CPU", "/smile/data/weka/cpu.arff", 6);
        //test(GradientTreeBoost.Loss.Huber, "2dplanes", "/smile/data/weka/regression/2dplanes.arff", 6);
        //test(GradientTreeBoost.Loss.Huber, "abalone", "/smile/data/weka/regression/abalone.arff", 8);
        //test(GradientTreeBoost.Loss.Huber, "ailerons", "/smile/data/weka/regression/ailerons.arff", 40);
        //test(GradientTreeBoost.Loss.Huber, "bank32nh", "/smile/data/weka/regression/bank32nh.arff", 32);
        test(GradientTreeBoost.Loss.Huber, "autoMPG", "/smile/data/weka/regression/autoMpg.arff", 7);
        test(GradientTreeBoost.Loss.Huber, "cal_housing", "/smile/data/weka/regression/cal_housing.arff", 8);
        //test(GradientTreeBoost.Loss.Huber, "puma8nh", "/smile/data/weka/regression/puma8nh.arff", 8);
        //test(GradientTreeBoost.Loss.Huber, "kin8nm", "/smile/data/weka/regression/kin8nm.arff", 8);
    }
    
    /**
     * Test of learn method, of class GradientTreeBoost.
     */
    @Test
    public void testCPU() {
        System.out.println("CPU");
        ArffParser parser = new ArffParser();
        parser.setResponseIndex(6);
        try {
            AttributeDataset data = parser.parse(this.getClass().getResourceAsStream("/smile/data/weka/cpu.arff"));
            double[] datay = data.toArray(new double[data.size()]);
            double[][] datax = data.toArray(new double[data.size()][]);

            int n = datax.length;
            int m = 3 * n / 4;
            int[] index = MathBase.permutate(n);
            
            double[][] trainx = new double[m][];
            double[] trainy = new double[m];            
            for (int i = 0; i < m; i++) {
                trainx[i] = datax[index[i]];
                trainy[i] = datay[index[i]];
            }
            
            double[][] testx = new double[n-m][];
            double[] testy = new double[n-m];            
            for (int i = m; i < n; i++) {
                testx[i-m] = datax[index[i]];
                testy[i-m] = datay[index[i]];                
            }

            GradientTreeBoost boost = new GradientTreeBoost(data.attributes(), trainx, trainy, 100);
            System.out.format("RMSE = %.4f\n", Validation.test(boost, testx, testy));
            
            double[] rmse = boost.test(testx, testy);
            for (int i = 1; i <= rmse.length; i++) {
                System.out.format("%d trees RMSE = %.4f\n", i, rmse[i-1]);
            }

            double[] importance = boost.importance();
            index = QuickSort.sort(importance);
            for (int i = importance.length; i-- > 0; ) {
                System.out.format("%s importance is %.4f\n", data.attributes()[index[i]], importance[i]);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
