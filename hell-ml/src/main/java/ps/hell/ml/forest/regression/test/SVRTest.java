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
import ps.hell.ml.forest.regression.SVR;
import ps.hell.math.base.MathBase;
import ps.hell.math.base.persons.kernel.PolynomialKernel;
import ps.hell.ml.statistics.validation.CrossValidation;

/**
 *
 * @author Haifeng Li
 */
public class SVRTest {

    public SVRTest() {
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

    /**
     * Test of learn method, of class SVR.
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
            MathBase.normalize(datax);

            int n = datax.length;
            int k = 10;

            CrossValidation cv = new CrossValidation(n, k);
            double rss = 0.0;
            for (int i = 0; i < k; i++) {
                double[][] trainx = MathBase.slice(datax, cv.train[i]);
                double[] trainy = MathBase.slice(datay, cv.train[i]);
                double[][] testx = MathBase.slice(datax, cv.test[i]);
                double[] testy = MathBase.slice(datay, cv.test[i]);

                SVR<double[]> svr = new SVR<double[]>(trainx, trainy, new PolynomialKernel(3, 1.0, 1.0), 0.1, 1.0);

                for (int j = 0; j < testx.length; j++) {
                    double r = testy[j] - svr.predict(testx[j]);
                    rss += r * r;
                }
            }

            System.out.println("10-CV RMSE = " + MathBase.sqrt(rss / n));
         } catch (Exception ex) {
             System.err.println(ex);
         }
    }
}