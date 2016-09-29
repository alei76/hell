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

package ps.hell.ml.statistics.validation.test;

import org.junit.*;
import ps.hell.math.base.MathBase;
import ps.hell.ml.statistics.validation.Bootstrap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Haifeng Li
 */
public class BootstrapTest {

    public BootstrapTest() {
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
     * Test if the train and test dataset are complete, of class CrossValidation.
     */
    @Test
    public void testComplete() {
        System.out.println("Complete");
        int n = 57;
        int k = 100;
        Bootstrap instance = new Bootstrap(n, k);
        boolean[] hit = new boolean[n];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                hit[j] = false;
            }

            int[] train = instance.train[i];
            for (int j = 0; j < train.length; j++) {
                hit[train[j]] = true;
            }

            int[] test = instance.test[i];
            for (int j = 0; j < test.length; j++) {
                assertFalse(hit[test[j]]);
                hit[test[j]] = true;
            }

            for (int j = 0; j < n; j++) {
                assertTrue(hit[j]);
            }
        }
    }

    /**
     * Test the coverage of samples, of class CrossValidation.
     */
    @Test
    public void testOrthogonal() {
        System.out.println("Coverage");
        int n = 57;
        int k = 100;
        Bootstrap instance = new Bootstrap(n, k);
        int[] trainhit = new int[n];
        int[] testhit = new int[n];
        for (int i = 0; i < k; i++) {
            int[] train = instance.train[i];
            for (int j = 0; j < train.length; j++) {
                trainhit[train[j]]++;
            }

            int[] test = instance.test[i];
            for (int j = 0; j < test.length; j++) {
                testhit[test[j]]++;
            }
        }

        System.out.format("Train coverage: %d\t%d\t%d\n", MathBase.min(trainhit), MathBase.median(trainhit), MathBase.max(trainhit));
        System.out.format("Test coverage: %d\t%d\t%d\n", MathBase.min(testhit), MathBase.median(testhit), MathBase.max(testhit));

        for (int j = 0; j < n; j++) {
            assertTrue(trainhit[j] > 60);
            assertTrue(testhit[j] > 20);
        }
    }

}