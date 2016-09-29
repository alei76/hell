package ps.hell.math.base.matrix.decomposition;
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

import java.util.Arrays;

import ps.hell.math.base.struct.Complex;
import ps.hell.math.base.MathBase;
import ps.hell.math.base.matrix.inter.IMatrix;


/**
 * Eigen decomposition of a real matrix. Eigen decomposition is the factorization
 * of a matrix into a canonical form, whereby the matrix is represented in terms
 * of its eigenvalues and eigenvectors:
 * <p>
 * A = V*D*V<sup>-1</sup>
 * <p>
 * If A is symmetric, then A = V*D*V' where the eigenvalue matrix D is
 * diagonal and the eigenvector matrix V is orthogonal.
 * <p>
 * Given a linear transformation A, a non-zero vector x is defined to be an
 * eigenvector of the transformation if it satisfies the eigenvalue equation
 * <p>
 * A x = &lambda; x
 * <p>
 * for some scalar &lambda;. In this situation, the scalar &lambda; is called
 * an eigenvalue of A corresponding to the eigenvector x.
 * <p>
 * The word eigenvector formally refers to the right eigenvector, which is
 * defined by the above eigenvalue equation A x = &lambda; x, and is the most
 * commonly used eigenvector. However, the left eigenvector exists as well, and
 * is defined by x A = &lambda; x.
 * <p>
 * Let A be a real n-by-n matrix with strictly positive entries a<sub>ij</sub>
 * &gt; 0. Then the following statements hold.
 * <ol>
 * <li> There is a positive real number r, called the Perron-Frobenius
 * eigenvalue, such that r is an eigenvalue of A and any other eigenvalue &lambda;
 * (possibly complex) is strictly smaller than r in absolute value,
 * |&lambda;| &lt; r.
 * <li> The Perron-Frobenius eigenvalue is simple: r is a simple root of the
 * characteristic polynomial of A. Consequently, both the right and the left
 * eigenspace associated to r is one-dimensional.
 * <li> There exists a left eigenvector v of A associated with r (row vector)
 * having strictly positive components. Likewise, there exists a right
 * eigenvector w associated with r (column vector) having strictly positive
 * components.
 * <li> The left eigenvector v (respectively right w) associated with r, is the
 * only eigenvector which has positive components, i.e. for all other
 * eigenvectors of A there exists a component which is not positive.
 * </ol>
 * <p>
 * A stochastic matrix, probability matrix, or transition matrix is used to
 * describe the transitions of a Markov chain. A right stochastic matrix is
 * a square matrix each of whose rows consists of nonnegative real numbers,
 * with each row summing to 1. A left stochastic matrix is a square matrix
 * whose columns consist of nonnegative real numbers whose sum is 1. A doubly
 * stochastic matrix where all entries are nonnegative and all rows and all
 * columns sum to 1. A stationary probability vector &pi; is defined as a
 * vector that does not change under application of the transition matrix;
 * that is, it is defined as a left eigenvector of the probability matrix,
 * associated with eigenvalue 1: &pi;P = &pi;. The Perron-Frobenius theorem
 * ensures that such a vector exists, and that the largest eigenvalue
 * associated with a stochastic matrix is always 1. For a matrix with strictly
 * positive entries, this vector is unique. In general, however, there may be
 * several such vectors.
 * 
 * @author Haifeng Li
 */
public class EigenValueDecomposition2 {

    /**
     * Array of (real part of) eigenvalues.
     */
    private double[] d;
    /**
     * Array of imaginary part of eigenvalues.
     */
    private double[] e;
    /**
     * Array of eigenvectors.
     */
    private double[][] V;

    /**
     * Private constructor.
     * @param V eigenvectors.
     * @param d eigenvalues.
     */
    private EigenValueDecomposition2(double[][] V, double[] d) {
        this.V = V;
        this.d = d;
    }

    /**
     * Private constructor.
     * @param V eigenvectors.
     * @param d real part of eigenvalues.
     * @param e imaginary part of eigenvalues.
     */
    private EigenValueDecomposition2(double[][] V, double[] d, double[] e) {
        this.V = V;
        this.d = d;
        this.e = e;
    }

    /**
     * Returns the eigenvector matrix, ordered by eigen values from largest to smallest.
     */
    public double[][] getEigenVectors() {
        return V;
    }

    /**
     * Returns the eigenvalues, ordered from largest to smallest.
     */
    public double[] getEigenValues() {
        return d;
    }

    /**
     * Returns the real parts of the eigenvalues, ordered in real part from
     * largest to smallest.
     */
    public double[] getRealEigenValues() {
        return d;
    }

    /**
     * Returns the imaginary parts of the eigenvalues, ordered in real part
     * from largest to smallest.
     */
    public double[] getImagEigenValues() {
        return e;
    }

    /**
     * Returns the block diagonal eigenvalue matrix whose diagonal are the real
     * part of eigenvalues, lower subdiagonal are positive imaginary parts, and
     * upper subdiagonal are negative imaginary parts.
     */
    public double[][] getD() {
        int n = V.length;
        double[][] D = new double[n][n];
        for (int i = 0; i < n; i++) {
            D[i][i] = d[i];
            if (e != null) {
                if (e[i] > 0) {
                    D[i][i + 1] = e[i];
                } else if (e[i] < 0) {
                    D[i][i - 1] = e[i];
                }
            }
        }
        return D;
    }

    /**
     * Returns the largest eigen pair of matrix with the power iteration
     * under the assumptions A has an eigenvalue that is strictly greater
     * in magnitude than its other eigenvalues and the starting
     * vector has a nonzero component in the direction of an eigenvector
     * associated with the dominant eigenvalue.
     * @param A the matrix supporting matrix vector multiplication operation.
     * @param v on input, it is the non-zero initial guess of the eigen vector.
     * On output, it is the eigen vector corresponding largest eigen value.
     * @return the largest eigen value.
     */
    public static double eigen(IMatrix A, double[] v) {
        return eigen(A, v, MathBase.max(1.0E-10, A.nrows() * MathBase.EPSILON));
    }

    /**
     * Returns the largest eigen pair of matrix with the power iteration
     * under the assumptions A has an eigenvalue that is strictly greater
     * in magnitude than its other eigenvalues and the starting
     * vector has a nonzero component in the direction of an eigenvector
     * associated with the dominant eigenvalue.
     * @param A the matrix supporting matrix vector multiplication operation.
     * @param v on input, it is the non-zero initial guess of the eigen vector.
     * On output, it is the eigen vector corresponding largest eigen value.
     * @param tol the desired convergence tolerance.
     * @return the largest eigen value.
     */
    public static double eigen(IMatrix A, double[] v, double tol) {
        return eigen(A, v, 0.0, tol);
    }

    /**
     * Returns the largest eigen pair of matrix with the power iteration
     * under the assumptions A has an eigenvalue that is strictly greater
     * in magnitude than its other eigenvalues and the starting
     * vector has a nonzero component in the direction of an eigenvector
     * associated with the dominant eigenvalue.
     * @param A the matrix supporting matrix vector multiplication operation.
     * @param v on input, it is the non-zero initial guess of the eigen vector.
     * On output, it is the eigen vector corresponding largest eigen value.
     * @param tol the desired convergence tolerance.
     * @param maxIter the maximum number of iterations in case that the algorithm
     * does not converge.
     * @return the largest eigen value.
     */
    public static double eigen(IMatrix A, double[] v, double tol, int maxIter) {
        return eigen(A, v, 0.0, tol, maxIter);
    }

    /**
     * Returns the largest eigen pair of matrix with the power iteration
     * under the assumptions A has an eigenvalue that is strictly greater
     * in magnitude than its other eigenvalues and the starting
     * vector has a nonzero component in the direction of an eigenvector
     * associated with the dominant eigenvalue.
     * @param A the matrix supporting matrix vector multiplication operation.
     * @param v on input, it is the non-zero initial guess of the eigen vector.
     * On output, it is the eigen vector corresponding largest eigen value.
     * @param p the origin in the shifting power method. A - pI will be
     * used in the iteration to accelerate the method. p should be such that
     * |(&lambda;<sub>2</sub> - p) / (&lambda;<sub>1</sub> - p)| &lt; |&lambda;<sub>2</sub> / &lambda;<sub>1</sub>|,
     * where &lambda;<sub>2</sub> is the second largest eigenvalue in magnitude.
     * If we known the eigenvalue spectrum of A, (&lambda;<sub>2</sub> + &lambda;<sub>n</sub>)/2
     * is the optimal choice of p, where &lambda;<sub>n</sub> is the smallest eigenvalue
     * in magnitude. Good estimates of &lambda;<sub>2</sub> are more difficult
     * to compute. However, if &mu; is an approximation to largest eigenvector,
     * then using any x<sub>0</sub> such that x<sub>0</sub>*&mu; = 0 as the initial
     * vector for a few iterations may yield a reasonable estimate of &lambda;<sub>2</sub>.
     * @param tol the desired convergence tolerance.
     * @return the largest eigen value.
     */
    public static double eigen(IMatrix A, double[] v, double p, double tol) {
        return eigen(A, v, p, tol, MathBase.max(20, 2 * A.nrows()));
    }

    /**
     * Returns the largest eigen pair of matrix with the power iteration
     * under the assumptions A has an eigenvalue that is strictly greater
     * in magnitude than its other eigenvalues and the starting
     * vector has a nonzero component in the direction of an eigenvector
     * associated with the dominant eigenvalue.
     * @param A the matrix supporting matrix vector multiplication operation.
     * @param v on input, it is the non-zero initial guess of the eigen vector.
     * On output, it is the eigen vector corresponding largest eigen value.
     * @param p the origin in the shifting power method. A - pI will be
     * used in the iteration to accelerate the method. p should be such that
     * |(&lambda;<sub>2</sub> - p) / (&lambda;<sub>1</sub> - p)| &lt; |&lambda;<sub>2</sub> / &lambda;<sub>1</sub>|,
     * where &lambda;<sub>2</sub> is the second largest eigenvalue in magnitude.
     * If we known the eigenvalue spectrum of A, (&lambda;<sub>2</sub> + &lambda;<sub>n</sub>)/2
     * is the optimal choice of p, where &lambda;<sub>n</sub> is the smallest eigenvalue
     * in magnitude. Good estimates of &lambda;<sub>2</sub> are more difficult
     * to compute. However, if &mu; is an approximation to largest eigenvector,
     * then using any x<sub>0</sub> such that x<sub>0</sub>*&mu; = 0 as the initial
     * vector for a few iterations may yield a reasonable estimate of &lambda;<sub>2</sub>.
     * @param tol the desired convergence tolerance.
     * @param maxIter the maximum number of iterations in case that the algorithm
     * does not converge.
     * @return the largest eigen value.
     */
    public static double eigen(IMatrix A, double[] v, double p, double tol, int maxIter) {
        if (A.nrows() != A.ncols()) {
            throw new IllegalArgumentException("Matrix is not square.");
        }

        if (tol <= 0.0) {
            throw new IllegalArgumentException("Invalid tolerance: " + tol);            
        }
        
        if (maxIter <= 0) {
            throw new IllegalArgumentException("Invalid maximum number of iterations: " + maxIter);            
        }
        
        int n = A.nrows();
        tol = MathBase.max(tol, MathBase.EPSILON * n);

        double[] z = new double[n];
        double lambda = ax(A, v, z, p);

        for (int iter = 1; iter <= maxIter; iter++) {
            double l = lambda;
            lambda = ax(A, v, z, p);

            double eps = MathBase.abs(lambda - l);
            if (iter % 10 == 0) {
                System.out.format("Largest eigenvalue after %3d power iterations: %.5f\n", iter, lambda + p);
            }

            if (eps < tol) {
                System.out.format("Largest eigenvalue after %3d power iterations: %.5f\n", iter, lambda + p);
                return lambda + p;
            }
        }

        System.out.format("Largest eigenvalue after %3d power iterations: %.5f\n", maxIter, lambda + p);
        System.err.println("Power iteration exceeded the maximum number of iterations.");
        return lambda + p;
    }

    /**
     * Calculate the page rank vector.
     * @param A the matrix supporting matrix vector multiplication operation.
     * @return the page rank vector.
     */
    public static double[] pagerank(IMatrix A) {
    	int n = A.nrows();    	
    	double[] v = new double[n];
    	Arrays.fill(v,  1.0 / n);
    	return pagerank(A, v);
    }
    
    /**
     * Calculate the page rank vector.
     * @param A the matrix supporting matrix vector multiplication operation.
     * @param v the teleportation vector.
     * @return the page rank vector.
     */
    public static double[] pagerank(IMatrix A, double[] v) {
    	return pagerank(A, v, 0.85, 1E-7, 57);
    }
    
    /**
     * Calculate the page rank vector.
     * @param A the matrix supporting matrix vector multiplication operation.
     * @param v the teleportation vector.
     * @param damping the damper factor.
     * @param tol the desired convergence tolerance.
     * @param maxIter the maximum number of iterations in case that the algorithm
     * does not converge.
     * @return the page rank vector.
     */
    public static double[] pagerank(IMatrix A, double[] v, double damping, double tol, int maxIter) {
        if (A.nrows() != A.ncols()) {
            throw new IllegalArgumentException("Matrix is not square.");
        }

        if (tol <= 0.0) {
            throw new IllegalArgumentException("Invalid tolerance: " + tol);            
        }
        
        if (maxIter <= 0) {
            throw new IllegalArgumentException("Invalid maximum number of iterations: " + maxIter);            
        }
        
        int n = A.nrows();
        tol = MathBase.max(tol, MathBase.EPSILON * n);

        double[] z = new double[n];
        double[] p = Arrays.copyOf(v, n);

        for (int iter = 1; iter <= maxIter; iter++) {
            A.ax(p, z);
            double beta = 1.0 - damping * MathBase.norm1(z);
            
            double delta = 0.0;
            for (int i = 0; i < n; i++) {
            	double q = damping * z[i] + beta * v[i];
            	delta += MathBase.abs(q - p[i]);
            	p[i] = q;
            }

            if (iter % 10 == 0) {
                System.out.format("PageRank residual after %3d power iterations: %.7f\n", iter, delta);
            }

            if (delta < tol) {
                System.out.format("PageRank residual after %3d power iterations: %.7f\n", iter, delta);
                return p;
            }
        }

        System.err.println("PageRank iteration exceeded the maximum number of iterations.");
        return p;
    }

    /**
     * Calculate and normalize y = (A - pI) x.* Returns the largest element of y
     * in magnitude.
     */
    private static double ax(IMatrix A, double[] x, double[] y, double p) {
        A.ax(x, y);

        if (p != 0.0) {
            for (int i = 0; i < y.length; i++) {
                y[i] -= p * x[i];
            }
        }

        double lambda = y[0];
        for (int i = 1; i < y.length; i++) {
            if (MathBase.abs(y[i]) > MathBase.abs(lambda)) {
                lambda = y[i];
            }
        }

        for (int i = 0; i < y.length; i++) {
            x[i] = y[i] / lambda;
        }

        return lambda;
    }

    /**
     * Find k largest approximate eigen pairs of a symmetric matrix by the
     * Lanczos algorithm.
     *
     * @param A the matrix supporting matrix vector multiplication operation.
     * @param k the number of eigenvalues we wish to compute for the input matrix.
     * This number cannot exceed the size of A.
     */
    public static EigenValueDecomposition2 decompose(IMatrix A, int k) {
        return decompose(A, k, 1.0E-6);
    }

    /**
     * Find k largest approximate eigen pairs of a symmetric matrix by the
     * Lanczos algorithm.
     *
     * @param A the matrix supporting matrix vector multiplication operation.
     * @param k the number of eigenvalues we wish to compute for the input matrix.
     * This number cannot exceed the size of A.
     * @param kappa relative accuracy of ritz values acceptable as eigenvalues.
     */
    public static EigenValueDecomposition2 decompose(IMatrix A, int k, double kappa) {
        if (A.nrows() != A.ncols()) {
            throw new IllegalArgumentException("Matrix is not square.");
        }

        if (k < 1 || k > A.nrows()) {
            throw new IllegalArgumentException("k is larger than the size of A: " + k + " > " + A.nrows());
        }

        int n = A.nrows();
        int intro = 0;

        // roundoff estimate for dot product of two unit vectors
        double eps = MathBase.EPSILON * MathBase.sqrt(n);
        double reps = MathBase.sqrt(MathBase.EPSILON);
        double eps34 = reps * MathBase.sqrt(reps);
        kappa = MathBase.max(kappa, eps34);

        // Workspace
        // wptr[0]             r[j]
        // wptr[1]             q[j]
        // wptr[2]             q[j-1]
        // wptr[3]             p
        // wptr[4]             p[j-1]
        // wptr[5]             temporary worksapce
        double[][] wptr = new double[6][n];

        // orthogonality estimate of Lanczos vectors at step j
        double[] eta = new double[n];
        // orthogonality estimate of Lanczos vectors at step j-1
        double[] oldeta = new double[n];
        // the error bounds
        double[] bnd = new double[n];

        // diagonal elements of T
        double[] alf = new double[n];
        // off-diagonal elements of T
        double[] bet = new double[n + 1];

        // basis vectors for the Krylov subspace
        double[][] q = new double[n][];
        // initial Lanczos vectors
        double[][] p = new double[2][];

        // arrays used in the QL decomposition
        double[] ritz = new double[n + 1];
        // eigenvectors calculated in the QL decomposition
        double[][] z = null;

        // First step of the Lanczos algorithm. It also does a step of extended
        // local re-orthogonalization.
        // get initial vector; default is random
        double rnm = startv(A, q, wptr, 0);

        // normalize starting vector
        double t = 1.0 / rnm;
        MathBase.scale(t, wptr[0], wptr[1]);
        MathBase.scale(t, wptr[3]);

        // take the first step
        A.ax(wptr[3], wptr[0]);
        alf[0] = MathBase.dot(wptr[0], wptr[3]);
        MathBase.axpy(-alf[0], wptr[1], wptr[0]);
        t = MathBase.dot(wptr[0], wptr[3]);
        MathBase.axpy(-t, wptr[1], wptr[0]);
        alf[0] += t;
        MathBase.copy(wptr[0], wptr[4]);
        rnm = MathBase.norm(wptr[0]);
        double anorm = rnm + MathBase.abs(alf[0]);
        double tol = reps * anorm;

        if (0 == rnm) {
            throw new IllegalStateException("Lanczos method was unable to find a starting vector within range.");
        }

        eta[0] = eps;
        oldeta[0] = eps;

        // number of ritz values stabilized
        int neig = 0;
        // number of intitial Lanczos vectors in local orthog. (has value of 0, 1 or 2)
        int ll = 0;

        // start of index through loop
        int first = 1;
        // end of index through loop
        int last = MathBase.min(k + MathBase.max(8, k), n);

        // number of Lanczos steps actually taken
        int j = 0;

        // stop flag
        boolean enough = false;

        // algorithm iterations
        while (!enough) {
            if (rnm <= tol) {
                rnm = 0.0;
            }

            // a single Lanczos step
            for (j = first; j < last; j++) {
                MathBase.swap(wptr, 1, 2);
                MathBase.swap(wptr, 3, 4);

                store(q, j - 1, wptr[2]);
                if (j - 1 < 2) {
                    p[j - 1] = wptr[4].clone();
                }
                bet[j] = rnm;

                // restart if invariant subspace is found
                if (0 == bet[j]) {
                    rnm = startv(A, q, wptr, j);
                    if (rnm < 0.0) {
                        rnm = 0.0;
                        break;
                    }

                    if (rnm == 0) {
                        enough = true;
                    }
                }

                if (enough) {
                    // These lines fix a bug that occurs with low-rank matrices
                    MathBase.swap(wptr, 1, 2);
                    break;
                }

                // take a lanczos step
                t = 1.0 / rnm;
                MathBase.scale(t, wptr[0], wptr[1]);
                MathBase.scale(t, wptr[3]);
                A.ax(wptr[3], wptr[0]);
                MathBase.axpy(-rnm, wptr[2], wptr[0]);
                alf[j] = MathBase.dot(wptr[0], wptr[3]);
                MathBase.axpy(-alf[j], wptr[1], wptr[0]);

                // orthogonalize against initial lanczos vectors
                if (j <= 2 && (MathBase.abs(alf[j - 1]) > 4.0 * MathBase.abs(alf[j]))) {
                    ll = j;
                }

                for (int i = 0; i < MathBase.min(ll, j - 1); i++) {
                    t = MathBase.dot(p[i], wptr[0]);
                    MathBase.axpy(-t, q[i], wptr[0]);
                    eta[i] = eps;
                    oldeta[i] = eps;
                }

                // extended local reorthogonalization
                t = MathBase.dot(wptr[0], wptr[4]);
                MathBase.axpy(-t, wptr[2], wptr[0]);
                if (bet[j] > 0.0) {
                    bet[j] = bet[j] + t;
                }
                t = MathBase.dot(wptr[0], wptr[3]);
                MathBase.axpy(-t, wptr[1], wptr[0]);
                alf[j] = alf[j] + t;
                MathBase.copy(wptr[0], wptr[4]);
                rnm = MathBase.norm(wptr[0]);
                anorm = bet[j] + MathBase.abs(alf[j]) + rnm;
                tol = reps * anorm;

                // update the orthogonality bounds
                ortbnd(alf, bet, eta, oldeta, j, rnm, eps);

                // restore the orthogonality state when needed
                rnm = purge(ll, q, wptr[0], wptr[1], wptr[4], wptr[3], eta, oldeta, j, rnm, tol, eps, reps);
                if (rnm <= tol) {
                    rnm = 0.0;
                }
            }

            if (enough) {
                j = j - 1;
            } else {
                j = last - 1;
            }

            first = j + 1;
            bet[j + 1] = rnm;

            // analyze T
            System.arraycopy(alf, 0, ritz, 0, j + 1);
            System.arraycopy(bet, 0, wptr[5], 0, j + 1);

            z = new double[j + 1][j + 1];
            for (int i = 0; i <= j; i++) {
                z[i][i] = 1.0;
            }

            // compute the eigenvalues and eigenvectors of the
            // tridiagonal matrix
            tql2(z, ritz, wptr[5], j + 1);

            for (int i = 0; i <= j; i++) {
                bnd[i] = rnm * MathBase.abs(z[j][i]);
            }

            // massage error bounds for very close ritz values
            boolean[] ref_enough = {enough};
            neig = error_bound(ref_enough, ritz, bnd, j, tol, eps34);
            enough = ref_enough[0];

            // should we stop?
            if (neig < k) {
                if (0 == neig) {
                    last = first + 9;
                    intro = first;
                } else {
                    last = first + MathBase.max(3, 1 + ((j - intro) * (k - neig)) / MathBase.max(3,neig));
                }
                last = MathBase.min(last, n);
            } else {
                enough = true;
            }
            enough = enough || first >= n;
        }
        store(q, j, wptr[1]);

        k = MathBase.min(k, neig);

        double[] eigenvalues = new double[k];
        double[][] eigenvectors = new double[n][k];
        for (int i = 0, index = 0; i <= j && index < k; i++) {
            if (bnd[i] <= kappa * MathBase.abs(ritz[i])) {
                for (int row = 0; row < n; row++) {
                    for (int l = 0; l <= j; l++) {
                        eigenvectors[row][index] += q[l][row] * z[l][i];
                    }
                }
                eigenvalues[index++] = ritz[i];
            }
        }

        return new EigenValueDecomposition2(eigenvectors, eigenvalues);
    }

    /**
     * Generate a starting vector in r and returns |r|. It returns zero if the
     * range is spanned, and throws exception if no starting vector within range
     * of operator can be found.
     * @param step   starting index for a Lanczos run
     */
    private static double startv(IMatrix A, double[][] q, double[][] wptr, int step) {
        // get initial vector; default is random
        double rnm = MathBase.dot(wptr[0], wptr[0]);
        double[] r = wptr[0];
        for (int id = 0; id < 3; id++) {
            if (id > 0 || step > 0 || rnm == 0) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = MathBase.random();
                }
            }
            MathBase.copy(wptr[0], wptr[3]);

            // apply operator to put r in range (essential if m singular)
            A.ax(wptr[3], wptr[0]);
            MathBase.copy(wptr[0], wptr[3]);
            rnm = MathBase.dot(wptr[0], wptr[3]);
            if (rnm > 0.0) {
                break;
            }
        }

        // fatal error
        if (rnm <= 0.0) {
            System.err.println("Lanczos method was unable to find a starting vector within range.");
            return -1;
        }

        if (step > 0) {
            for (int i = 0; i < step; i++) {
                double t = MathBase.dot(wptr[3], q[i]);
                MathBase.axpy(-t, q[i], wptr[0]);
            }

            // make sure q[step] is orthogonal to q[step-1]
            double t = MathBase.dot(wptr[4], wptr[0]);
            MathBase.axpy(-t, wptr[2], wptr[0]);
            MathBase.copy(wptr[0], wptr[3]);
            t = MathBase.dot(wptr[3], wptr[0]);
            if (t <= MathBase.EPSILON * rnm) {
                t = 0.0;
            }
            rnm = t;
        }

        return MathBase.sqrt(rnm);
    }

    /**
     * Update the eta recurrence.
     * @param alf      array to store diagonal of the tridiagonal matrix T
     * @param bet      array to store off-diagonal of T
     * @param eta      on input, orthogonality estimate of Lanczos vectors at step j.
     * On output, orthogonality estimate of Lanczos vectors at step j+1 .
     * @param oldeta   on input, orthogonality estimate of Lanczos vectors at step j-1
     * On output orthogonality estimate of Lanczos vectors at step j
     * @param step     dimension of T
     * @param rnm      norm of the next residual vector
     * @param eps      roundoff estimate for dot product of two unit vectors
     */
    private static void ortbnd(double[] alf, double[] bet, double[] eta, double[] oldeta, int step, double rnm, double eps) {
        if (step < 1) {
            return;
        }

        if (0 != rnm) {
            if (step > 1) {
                oldeta[0] = (bet[1] * eta[1] + (alf[0] - alf[step]) * eta[0] - bet[step] * oldeta[0]) / rnm + eps;
            }

            for (int i = 1; i <= step - 2; i++) {
                oldeta[i] = (bet[i + 1] * eta[i + 1] + (alf[i] - alf[step]) * eta[i] + bet[i] * eta[i - 1] - bet[step] * oldeta[i]) / rnm + eps;
            }
        }

        oldeta[step - 1] = eps;
        for (int i = 0; i < step; i++) {
            double swap = eta[i];
            eta[i] = oldeta[i];
            oldeta[i] = swap;
        }

        eta[step] = eps;
    }

    /**
     * Examine the state of orthogonality between the new Lanczos
     * vector and the previous ones to decide whether re-orthogonalization
     * should be performed.
     * @param n        dimension of the eigenproblem for matrix B
     * @param ll       number of intitial Lanczos vectors in local orthog.
     * @param r        on input, residual vector to become next Lanczos vector.
     * On output, residual vector orthogonalized against previous Lanczos.
     * @param q        on input, current Lanczos vector. On Output, current
     * Lanczos vector orthogonalized against previous ones.
     * @param ra       previous Lanczos vector
     * @param qa       previous Lanczos vector
     * @param wrk      temporary vector to store the previous Lanczos vector
     * @param eta      state of orthogonality between r and prev. Lanczos vectors
     * @param oldeta   state of orthogonality between q and prev. Lanczos vectors
     * @param j        current Lanczos step
     */
    private static double purge(int ll, double[][] Q, double[] r, double[] q, double[] ra, double[] qa, double[] eta, double[] oldeta, int step, double rnm, double tol, double eps, double reps) {
        if (step < ll + 2) {
            return rnm;
        }

        double t, tq, tr;
        int k = idamax(step - (ll + 1), eta, ll, 1) + ll;
        if (MathBase.abs(eta[k]) > reps) {
            double reps1 = eps / reps;
            int iteration = 0;
            boolean flag = true;
            while (iteration < 2 && flag) {
                if (rnm > tol) {
                    // bring in a lanczos vector t and orthogonalize both
                    // r and q against it
                    tq = 0.0;
                    tr = 0.0;
                    for (int i = ll; i < step; i++) {
                        t = -MathBase.dot(qa, Q[i]);
                        tq += MathBase.abs(t);
                        MathBase.axpy(t, Q[i], q);
                        t = -MathBase.dot(ra, Q[i]);
                        tr += MathBase.abs(t);
                        MathBase.axpy(t, Q[i], r);
                    }
                    MathBase.copy(q, qa);
                    t = -MathBase.dot(r, qa);
                    tr += MathBase.abs(t);
                    MathBase.axpy(t, q, r);
                    MathBase.copy(r, ra);
                    rnm = MathBase.sqrt(MathBase.dot(ra, r));
                    if (tq <= reps1 && tr <= reps1 * rnm) {
                        flag = false;
                    }
                }
                iteration++;
            }

            for (int i = ll; i <= step; i++) {
                eta[i] = eps;
                oldeta[i] = eps;
            }
        }

        return rnm;
    }

    /**
     * Find the index of element having maximum absolute value.
     */
    private static int idamax(int n, double[] dx, int ix0, int incx) {
        int ix, imax;
        double dmax;
        if (n < 1) {
            return -1;
        }
        if (n == 1) {
            return 0;
        }
        if (incx == 0) {
            return -1;
        }
        ix = (incx < 0) ? ix0 + ((-n + 1) * incx) : ix0;
        imax = ix;
        dmax = MathBase.abs(dx[ix]);
        for (int i = 1; i < n; i++) {
            ix += incx;
            double dtemp = MathBase.abs(dx[ix]);
            if (dtemp > dmax) {
                dmax = dtemp;
                imax = ix;
            }
        }
        return imax;
    }

    /**
     * Massage error bounds for very close ritz values by placing a gap between
     * them.  The error bounds are then refined to reflect this.
     * @param ritz     array to store the ritz values
     * @param bnd      array to store the error bounds
     * @param enough   stop flag
     */
    private static int error_bound(boolean[] enough, double[] ritz, double[] bnd, int step, double tol, double eps34) {
        double gapl, gap;

        // massage error bounds for very close ritz values
        int mid = idamax(step + 1, bnd, 0, 1);

        for (int i = ((step + 1) + (step - 1)) / 2; i >= mid + 1; i -= 1) {
            if (MathBase.abs(ritz[i - 1] - ritz[i]) < eps34 * MathBase.abs(ritz[i])) {
                if (bnd[i] > tol && bnd[i - 1] > tol) {
                    bnd[i - 1] = MathBase.sqrt(bnd[i] * bnd[i] + bnd[i - 1] * bnd[i - 1]);
                    bnd[i] = 0.0;
                }
            }
        }

        for (int i = ((step + 1) - (step - 1)) / 2; i <= mid - 1; i += 1) {
            if (MathBase.abs(ritz[i + 1] - ritz[i]) < eps34 * MathBase.abs(ritz[i])) {
                if (bnd[i] > tol && bnd[i + 1] > tol) {
                    bnd[i + 1] = MathBase.sqrt(bnd[i] * bnd[i] + bnd[i + 1] * bnd[i + 1]);
                    bnd[i] = 0.0;
                }
            }
        }

        // refine the error bounds
        int neig = 0;
        gapl = ritz[step] - ritz[0];
        for (int i = 0; i <= step; i++) {
            gap = gapl;
            if (i < step) {
                gapl = ritz[i + 1] - ritz[i];
            }

            gap = MathBase.min(gap, gapl);
            if (gap > bnd[i]) {
                bnd[i] = bnd[i] * (bnd[i] / gap);
            }

            if (bnd[i] <= 16.0 * MathBase.EPSILON * MathBase.abs(ritz[i])) {
                neig++;
                if (!enough[0]) {
                    enough[0] = -MathBase.EPSILON < ritz[i] && ritz[i] < MathBase.EPSILON;
                }
            }
        }

        System.out.format("Lancozs method found %3d converged eigenvalues of the %3d-by-%-3d matrix", neig, step + 1, step + 1);
        if (neig == 0) {
            System.out.println(".");
        } else {
            System.out.print(": (");
            for (int i = 0, n = 0; i <= step; i++) {
                if (bnd[i] <= 16.0 * MathBase.EPSILON * MathBase.abs(ritz[i])) {
                    if (++n == neig) {
                        System.out.format("%.4f).\n", ritz[i]);
                    } else {
                        System.out.format("%.4f, ", ritz[i]);
                    }
                }
            }
        }

        return neig;
    }

    /**
     * Based on the input operation flag, stores to or retrieves from memory a vector.
     * @param s	   contains the vector to be stored
     */
    private static void store(double[][] q, int j, double[] s) {
        if (null == q[j]) {
            q[j] = s.clone();
        } else {
            MathBase.copy(s, q[j]);
        }
    }

    /**
     * Full eigen value decomposition of a square matrix. Note that the input
     * matrix will be altered during decomposition.
     * @param A    square matrix which will be altered during decomposition.
     */
    public static EigenValueDecomposition2 decompose(double[][] A) {
        if (A.length != A[0].length) {
            throw new IllegalArgumentException(String.format("Matrix is not square: %d x %d", A.length, A[0].length));
        }

        int n = A.length;
        double tol = 100 * MathBase.EPSILON;
        boolean symmetric = true;
        for (int i = 0; (i < n) && symmetric; i++) {
            for (int j = 0; (j < n) && symmetric; j++) {
                symmetric = MathBase.abs(A[i][j] - A[j][i]) < tol;
            }
        }

        return decompose(A, symmetric);
    }

    /**
     * Full eigen value decomposition of a square matrix. Note that the input
     * matrix will be altered during decomposition.
     * @param A    square matrix which will be altered during decomposition.
     * @param symmetric if true, the matrix is assumed to be symmetric.
     */
    public static EigenValueDecomposition2 decompose(double[][] A, boolean symmetric) {
        return decompose(A, symmetric, false);
    }

    /**
     * Full eigen value decomposition of a square matrix. Note that the input
     * matrix will be altered during decomposition.
     * @param A    square matrix which will be altered during decomposition.
     * @param symmetric if true, the matrix is assumed to be symmetric.
     * @param onlyValues if true, only compute eigenvalues; the default is to compute eigenvectors also.
     */
    public static EigenValueDecomposition2 decompose(double[][] A, boolean symmetric, boolean onlyValues) {
        if (A.length != A[0].length) {
            throw new IllegalArgumentException(String.format("Matrix is not square: %d x %d", A.length, A[0].length));
        }

        int n = A.length;
        double[] d = new double[n];
        double[] e = new double[n];

        if (symmetric) {
            double[][] V = A;

            if (onlyValues) {
                // Tridiagonalize.
                tred(V, d, e);
                // Diagonalize.
                tql(d, e, n);
                return new EigenValueDecomposition2(null, d);
            } else {
                // Tridiagonalize.
                tred2(V, d, e);
                // Diagonalize.
                tql2(V, d, e, n);
                return new EigenValueDecomposition2(V, d);
            }

        } else {
            double[] scale = balance(A);
            int[] perm = elmhes(A);
            if (onlyValues) {
                hqr(A, d, e);
                sort(d, e);
                return new EigenValueDecomposition2(null, d, e);
            } else {
                double[][] V = new double[n][n];
                for (int i = 0; i < n; i++) {
                    V[i][i] = 1.0;
                }

                eltran(A, V, perm);

                hqr2(A, V, d, e);
                balbak(V, scale);
                sort(d, e, V);
                return new EigenValueDecomposition2(V, d, e);
            }
        }
    }

    /**
     * Symmetric Householder reduction to tridiagonal form.
     */
    private static void tred(double[][] V, double[] d, double[] e) {
        int n = V.length;
        System.arraycopy(V[n - 1], 0, d, 0, n);

        // Householder reduction to tridiagonal form.
        for (int i = n - 1; i > 0; i--) {

            // Scale to avoid under/overflow.
            double scale = 0.0;
            double h = 0.0;
            for (int k = 0; k < i; k++) {
                scale = scale + MathBase.abs(d[k]);
            }
            if (scale == 0.0) {
                e[i] = d[i - 1];
                for (int j = 0; j < i; j++) {
                    d[j] = V[i - 1][j];
                    V[i][j] = 0.0;
                    V[j][i] = 0.0;
                }
            } else {

                // Generate Householder vector.
                for (int k = 0; k < i; k++) {
                    d[k] /= scale;
                    h += d[k] * d[k];
                }
                double f = d[i - 1];
                double g = MathBase.sqrt(h);
                if (f > 0) {
                    g = -g;
                }
                e[i] = scale * g;
                h = h - f * g;
                d[i - 1] = f - g;
                for (int j = 0; j < i; j++) {
                    e[j] = 0.0;
                }

                // Apply similarity transformation to remaining columns.
                for (int j = 0; j < i; j++) {
                    f = d[j];
                    V[j][i] = f;
                    g = e[j] + V[j][j] * f;
                    for (int k = j + 1; k <= i - 1; k++) {
                        g += V[k][j] * d[k];
                        e[k] += V[k][j] * f;
                    }
                    e[j] = g;
                }
                f = 0.0;
                for (int j = 0; j < i; j++) {
                    e[j] /= h;
                    f += e[j] * d[j];
                }
                double hh = f / (h + h);
                for (int j = 0; j < i; j++) {
                    e[j] -= hh * d[j];
                }
                for (int j = 0; j < i; j++) {
                    f = d[j];
                    g = e[j];
                    for (int k = j; k <= i - 1; k++) {
                        V[k][j] -= (f * e[k] + g * d[k]);
                    }
                    d[j] = V[i - 1][j];
                    V[i][j] = 0.0;
                }
            }
            d[i] = h;
        }

        for (int j = 0; j < n; j++) {
            d[j] = V[j][j];
        }
        e[0] = 0.0;
    }

    /**
     * Symmetric Householder reduction to tridiagonal form.
     */
    private static void tred2(double[][] V, double[] d, double[] e) {
        int n = V.length;
        System.arraycopy(V[n - 1], 0, d, 0, n);

        // Householder reduction to tridiagonal form.
        for (int i = n - 1; i > 0; i--) {

            // Scale to avoid under/overflow.
            double scale = 0.0;
            double h = 0.0;
            for (int k = 0; k < i; k++) {
                scale = scale + MathBase.abs(d[k]);
            }
            if (scale == 0.0) {
                e[i] = d[i - 1];
                for (int j = 0; j < i; j++) {
                    d[j] = V[i - 1][j];
                    V[i][j] = 0.0;
                    V[j][i] = 0.0;
                }
            } else {

                // Generate Householder vector.
                for (int k = 0; k < i; k++) {
                    d[k] /= scale;
                    h += d[k] * d[k];
                }
                double f = d[i - 1];
                double g = MathBase.sqrt(h);
                if (f > 0) {
                    g = -g;
                }
                e[i] = scale * g;
                h = h - f * g;
                d[i - 1] = f - g;
                for (int j = 0; j < i; j++) {
                    e[j] = 0.0;
                }

                // Apply similarity transformation to remaining columns.
                for (int j = 0; j < i; j++) {
                    f = d[j];
                    V[j][i] = f;
                    g = e[j] + V[j][j] * f;
                    for (int k = j + 1; k <= i - 1; k++) {
                        g += V[k][j] * d[k];
                        e[k] += V[k][j] * f;
                    }
                    e[j] = g;
                }
                f = 0.0;
                for (int j = 0; j < i; j++) {
                    e[j] /= h;
                    f += e[j] * d[j];
                }
                double hh = f / (h + h);
                for (int j = 0; j < i; j++) {
                    e[j] -= hh * d[j];
                }
                for (int j = 0; j < i; j++) {
                    f = d[j];
                    g = e[j];
                    for (int k = j; k <= i - 1; k++) {
                        V[k][j] -= (f * e[k] + g * d[k]);
                    }
                    d[j] = V[i - 1][j];
                    V[i][j] = 0.0;
                }
            }
            d[i] = h;
        }

        // Accumulate transformations.
        for (int i = 0; i < n - 1; i++) {
            V[n - 1][i] = V[i][i];
            V[i][i] = 1.0;
            double h = d[i + 1];
            if (h != 0.0) {
                for (int k = 0; k <= i; k++) {
                    d[k] = V[k][i + 1] / h;
                }
                for (int j = 0; j <= i; j++) {
                    double g = 0.0;
                    for (int k = 0; k <= i; k++) {
                        g += V[k][i + 1] * V[k][j];
                    }
                    for (int k = 0; k <= i; k++) {
                        V[k][j] -= g * d[k];
                    }
                }
            }
            for (int k = 0; k <= i; k++) {
                V[k][i + 1] = 0.0;
            }
        }
        for (int j = 0; j < n; j++) {
            d[j] = V[n - 1][j];
            V[n - 1][j] = 0.0;
        }
        V[n - 1][n - 1] = 1.0;
        e[0] = 0.0;
    }

    /**
     * Tridiagonal QL Implicit routine for computing eigenvalues of a symmetric,
     * real, tridiagonal matrix.
     *
     * The routine works extremely well in practice. The number of iterations for the first few
     * eigenvalues might be 4 or 5, say, but meanwhile the off-diagonal elements in the lower right-hand
     * corner have been reduced too. The later eigenvalues are liberated with very little work. The
     * average number of iterations per eigenvalue is typically 1.3 - 1.6. The operation count per
     * iteration is O(n), with a fairly large effective coefficient, say, ~20n. The total operation count
     * for the diagonalization is then ~20n * (1.3 - 1.6)n = ~30n^2. If the eigenvectors are required,
     * there is an additional, much larger, workload of about 3n^3 operations.
     *
     * @param d on input, it contains the diagonal elements of the tridiagonal matrix.
     * On output, it contains the eigenvalues.
     * @param e on input, it contains the subdiagonal elements of the tridiagonal
     * matrix, with e[0] arbitrary. On output, its contents are destroyed.
     * @param n the size of all parameter arrays.
     */
    private static void tql(double[] d, double[] e, int n) {
        for (int i = 1; i < n; i++) {
            e[i - 1] = e[i];
        }
        e[n - 1] = 0.0;

        double f = 0.0;
        double tst1 = 0.0;
        for (int l = 0; l < n; l++) {

            // Find small subdiagonal element
            tst1 = MathBase.max(tst1, MathBase.abs(d[l]) + MathBase.abs(e[l]));
            int m = l;
            for (; m < n; m++) {
                if (MathBase.abs(e[m]) <= MathBase.EPSILON * tst1) {
                    break;
                }
            }

            // If m == l, d[l] is an eigenvalue,
            // otherwise, iterate.
            if (m > l) {
                int iter = 0;
                do {
                    if (++iter >= 30) {
                        throw new RuntimeException("Too many iterations");
                    }

                    // Compute implicit shift
                    double g = d[l];
                    double p = (d[l + 1] - d[l]) / (2.0 * e[l]);
                    double r = MathBase.hypot(p, 1.0);
                    if (p < 0) {
                        r = -r;
                    }
                    d[l] = e[l] / (p + r);
                    d[l + 1] = e[l] * (p + r);
                    double dl1 = d[l + 1];
                    double h = g - d[l];
                    for (int i = l + 2; i < n; i++) {
                        d[i] -= h;
                    }
                    f = f + h;

                    // Implicit QL transformation.
                    p = d[m];
                    double c = 1.0;
                    double c2 = c;
                    double c3 = c;
                    double el1 = e[l + 1];
                    double s = 0.0;
                    double s2 = 0.0;
                    for (int i = m - 1; i >= l; i--) {
                        c3 = c2;
                        c2 = c;
                        s2 = s;
                        g = c * e[i];
                        h = c * p;
                        r = MathBase.hypot(p, e[i]);
                        e[i + 1] = s * r;
                        s = e[i] / r;
                        c = p / r;
                        p = c * d[i] - s * g;
                        d[i + 1] = h + s * (c * g + s * d[i]);
                    }
                    p = -s * s2 * c3 * el1 * e[l] / dl1;
                    e[l] = s * p;
                    d[l] = c * p;

                // Check for convergence.
                } while (MathBase.abs(e[l]) > MathBase.EPSILON * tst1);
            }
            d[l] = d[l] + f;
            e[l] = 0.0;
        }

        // Sort eigenvalues and corresponding vectors.
        for (int i = 0; i < n - 1; i++) {
            int k = i;
            double p = d[i];
            for (int j = i + 1; j < n; j++) {
                if (d[j] > p) {
                    k = j;
                    p = d[j];
                }
            }
            if (k != i) {
                d[k] = d[i];
                d[i] = p;
            }
        }
    }

    /**
     * Tridiagonal QL Implicit routine for computing eigenvalues and eigenvectors of a symmetric,
     * real, tridiagonal matrix.
     *
     * The routine works extremely well in practice. The number of iterations for the first few
     * eigenvalues might be 4 or 5, say, but meanwhile the off-diagonal elements in the lower right-hand
     * corner have been reduced too. The later eigenvalues are liberated with very little work. The
     * average number of iterations per eigenvalue is typically 1.3 - 1.6. The operation count per
     * iteration is O(n), with a fairly large effective coefficient, say, ~20n. The total operation count
     * for the diagonalization is then ~20n * (1.3 - 1.6)n = ~30n^2. If the eigenvectors are required,
     * there is an additional, much larger, workload of about 3n^3 operations.
     *
     * @param V on input, it contains the identity matrix. On output, the kth column
     * of V returns the normalized eigenvector corresponding to d[k].
     * @param d on input, it contains the diagonal elements of the tridiagonal matrix.
     * On output, it contains the eigenvalues.
     * @param e on input, it contains the subdiagonal elements of the tridiagonal
     * matrix, with e[0] arbitrary. On output, its contents are destroyed.
     * @param n the size of all parameter arrays.
     */
    private static void tql2(double[][] V, double[] d, double[] e, int n) {
        for (int i = 1; i < n; i++) {
            e[i - 1] = e[i];
        }
        e[n - 1] = 0.0;

        double f = 0.0;
        double tst1 = 0.0;
        for (int l = 0; l < n; l++) {

            // Find small subdiagonal element
            tst1 = MathBase.max(tst1, MathBase.abs(d[l]) + MathBase.abs(e[l]));
            int m = l;
            for (; m < n; m++) {
                if (MathBase.abs(e[m]) <= MathBase.EPSILON * tst1) {
                    break;
                }
            }

            // If m == l, d[l] is an eigenvalue,
            // otherwise, iterate.
            if (m > l) {
                int iter = 0;
                do {
                    if (++iter >= 30) {
                        throw new RuntimeException("Too many iterations");
                    }

                    // Compute implicit shift
                    double g = d[l];
                    double p = (d[l + 1] - d[l]) / (2.0 * e[l]);
                    double r = MathBase.hypot(p, 1.0);
                    if (p < 0) {
                        r = -r;
                    }
                    d[l] = e[l] / (p + r);
                    d[l + 1] = e[l] * (p + r);
                    double dl1 = d[l + 1];
                    double h = g - d[l];
                    for (int i = l + 2; i < n; i++) {
                        d[i] -= h;
                    }
                    f = f + h;

                    // Implicit QL transformation.
                    p = d[m];
                    double c = 1.0;
                    double c2 = c;
                    double c3 = c;
                    double el1 = e[l + 1];
                    double s = 0.0;
                    double s2 = 0.0;
                    for (int i = m - 1; i >= l; i--) {
                        c3 = c2;
                        c2 = c;
                        s2 = s;
                        g = c * e[i];
                        h = c * p;
                        r = MathBase.hypot(p, e[i]);
                        e[i + 1] = s * r;
                        s = e[i] / r;
                        c = p / r;
                        p = c * d[i] - s * g;
                        d[i + 1] = h + s * (c * g + s * d[i]);

                        // Accumulate transformation.
                        for (int k = 0; k < n; k++) {
                            h = V[k][i + 1];
                            V[k][i + 1] = s * V[k][i] + c * h;
                            V[k][i] = c * V[k][i] - s * h;
                        }
                    }
                    p = -s * s2 * c3 * el1 * e[l] / dl1;
                    e[l] = s * p;
                    d[l] = c * p;

                // Check for convergence.
                } while (MathBase.abs(e[l]) > MathBase.EPSILON * tst1);
            }
            d[l] = d[l] + f;
            e[l] = 0.0;
        }

        // Sort eigenvalues and corresponding vectors.
        for (int i = 0; i < n - 1; i++) {
            int k = i;
            double p = d[i];
            for (int j = i + 1; j < n; j++) {
                if (d[j] > p) {
                    k = j;
                    p = d[j];
                }
            }
            if (k != i) {
                d[k] = d[i];
                d[i] = p;
                for (int j = 0; j < n; j++) {
                    p = V[j][i];
                    V[j][i] = V[j][k];
                    V[j][k] = p;
                }
            }
        }
    }

    /**
     * Given a square matrix, this routine replaces it by a balanced matrix with
     * identical eigenvalues. A symmetric matrix is already balanced and is
     * unaffected by this procedure.
     */
    private static double[] balance(double[][] A) {
        double sqrdx = MathBase.RADIX * MathBase.RADIX;

        int n = A.length;

        double[] scale = new double[n];
        for (int i = 0; i < n; i++) {
            scale[i] = 1.0;
        }

        boolean done = false;
        while (!done) {
            done = true;
            for (int i = 0; i < n; i++) {
                double r = 0.0, c = 0.0;
                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        c += MathBase.abs(A[j][i]);
                        r += MathBase.abs(A[i][j]);
                    }
                }
                if (c != 0.0 && r != 0.0) {
                    double g = r / MathBase.RADIX;
                    double f = 1.0;
                    double s = c + r;
                    while (c < g) {
                        f *= MathBase.RADIX;
                        c *= sqrdx;
                    }
                    g = r * MathBase.RADIX;
                    while (c > g) {
                        f /= MathBase.RADIX;
                        c /= sqrdx;
                    }
                    if ((c + r) / f < 0.95 * s) {
                        done = false;
                        g = 1.0 / f;
                        scale[i] *= f;
                        for (int j = 0; j < n; j++) {
                            A[i][j] *= g;
                        }
                        for (int j = 0; j < n; j++) {
                            A[j][i] *= f;
                        }
                    }
                }
            }
        }

        return scale;
    }

    /**
     * Form the eigenvectors of a real nonsymmetric matrix by back transforming
     * those of the corresponding balanced matrix determined by balance.
     */
    private static void balbak(double[][] V, double[] scale) {
        int n = V.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                V[i][j] *= scale[i];
            }
        }
    }

    /**
     * Reduce a real nonsymmetric matrix to upper Hessenberg form.
     */
    private static int[] elmhes(double[][] A) {
        int n = A.length;
        int[] perm = new int[n];

        for (int m = 1; m < n - 1; m++) {
            double x = 0.0;
            int i = m;
            for (int j = m; j < n; j++) {
                if (MathBase.abs(A[j][m - 1]) > MathBase.abs(x)) {
                    x = A[j][m - 1];
                    i = j;
                }
            }
            perm[m] = i;
            if (i != m) {
                for (int j = m - 1; j < n; j++) {
                    double swap = A[i][j];
                    A[i][j] = A[m][j];
                    A[m][j] = swap;
                }
                for (int j = 0; j < n; j++) {
                    double swap = A[j][i];
                    A[j][i] = A[j][m];
                    A[j][m] = swap;
                }
            }
            if (x != 0.0) {
                for (i = m + 1; i < n; i++) {
                    double y = A[i][m - 1];
                    if (y != 0.0) {
                        y /= x;
                        A[i][m - 1] = y;
                        for (int j = m; j < n; j++) {
                            A[i][j] -= y * A[m][j];
                        }
                        for (int j = 0; j < n; j++) {
                            A[j][m] += y * A[j][i];
                        }
                    }
                }
            }
        }

        return perm;
    }

    /**
     * Accumulate the stabilized elementary similarity transformations used
     * in the reduction of a real nonsymmetric matrix to upper Hessenberg form by elmhes.
     */
    private static void eltran(double[][] A, double[][] V, int[] perm) {
        int n = A.length;
        for (int mp = n - 2; mp > 0; mp--) {
            for (int k = mp + 1; k < n; k++) {
                V[k][mp] = A[k][mp - 1];
            }
            int i = perm[mp];
            if (i != mp) {
                for (int j = mp; j < n; j++) {
                    V[mp][j] = V[i][j];
                    V[i][j] = 0.0;
                }
                V[i][mp] = 1.0;
            }
        }
    }

    /**
     * Find all eigenvalues of an upper Hessenberg matrix. On input, A can be
     * exactly as output from elmhes. On output, it is destroyed.
     */
    private static void hqr(double[][] A, double[] d, double[] e) {
        int n = A.length;
        int nn, m, l, k, j, its, i, mmin;
        double z, y, x, w, v, u, t, s, r = 0.0, q = 0.0, p = 0.0, anorm = 0.0;

        for (i = 0; i < n; i++) {
            for (j = MathBase.max(i - 1, 0); j < n; j++) {
                anorm += MathBase.abs(A[i][j]);
            }
        }
        nn = n - 1;
        t = 0.0;
        while (nn >= 0) {
            its = 0;
            do {
                for (l = nn; l > 0; l--) {
                    s = MathBase.abs(A[l - 1][l - 1]) + MathBase.abs(A[l][l]);
                    if (s == 0.0) {
                        s = anorm;
                    }
                    if (MathBase.abs(A[l][l - 1]) <= MathBase.EPSILON * s) {
                        A[l][l - 1] = 0.0;
                        break;
                    }
                }
                x = A[nn][nn];
                if (l == nn) {
                    d[nn--] = x + t;
                } else {
                    y = A[nn - 1][nn - 1];
                    w = A[nn][nn - 1] * A[nn - 1][nn];
                    if (l == nn - 1) {
                        p = 0.5 * (y - x);
                        q = p * p + w;
                        z = MathBase.sqrt(MathBase.abs(q));
                        x += t;
                        if (q >= 0.0) {
                            z = p + MathBase.copySign(z, p);
                            d[nn - 1] = d[nn] = x + z;
                            if (z != 0.0) {
                                d[nn] = x - w / z;
                            }
                        } else {
                            d[nn] = x + p;
                            e[nn] = -z;
                            d[nn - 1] = d[nn];
                            e[nn - 1] = -e[nn];
                        }
                        nn -= 2;
                    } else {
                        if (its == 30) {
                            throw new IllegalStateException("Too many iterations in hqr");
                        }
                        if (its == 10 || its == 20) {
                            t += x;
                            for (i = 0; i < nn + 1; i++) {
                                A[i][i] -= x;
                            }
                            s = MathBase.abs(A[nn][nn - 1]) + MathBase.abs(A[nn - 1][nn - 2]);
                            y = x = 0.75 * s;
                            w = -0.4375 * s * s;
                        }
                        ++its;
                        for (m = nn - 2; m >= l; m--) {
                            z = A[m][m];
                            r = x - z;
                            s = y - z;
                            p = (r * s - w) / A[m + 1][m] + A[m][m + 1];
                            q = A[m + 1][m + 1] - z - r - s;
                            r = A[m + 2][m + 1];
                            s = MathBase.abs(p) + MathBase.abs(q) + MathBase.abs(r);
                            p /= s;
                            q /= s;
                            r /= s;
                            if (m == l) {
                                break;
                            }
                            u = MathBase.abs(A[m][m - 1]) * (MathBase.abs(q) + MathBase.abs(r));
                            v = MathBase.abs(p) * (MathBase.abs(A[m - 1][m - 1]) + MathBase.abs(z) + MathBase.abs(A[m + 1][m + 1]));
                            if (u <= MathBase.EPSILON * v) {
                                break;
                            }
                        }
                        for (i = m; i < nn - 1; i++) {
                            A[i + 2][i] = 0.0;
                            if (i != m) {
                                A[i + 2][i - 1] = 0.0;
                            }
                        }
                        for (k = m; k < nn; k++) {
                            if (k != m) {
                                p = A[k][k - 1];
                                q = A[k + 1][k - 1];
                                r = 0.0;
                                if (k + 1 != nn) {
                                    r = A[k + 2][k - 1];
                                }
                                if ((x = MathBase.abs(p) + MathBase.abs(q) + MathBase.abs(r)) != 0.0) {
                                    p /= x;
                                    q /= x;
                                    r /= x;
                                }
                            }
                            if ((s = MathBase.copySign(MathBase.sqrt(p * p + q * q + r * r), p)) != 0.0) {
                                if (k == m) {
                                    if (l != m) {
                                        A[k][k - 1] = -A[k][k - 1];
                                    }
                                } else {
                                    A[k][k - 1] = -s * x;
                                }
                                p += s;
                                x = p / s;
                                y = q / s;
                                z = r / s;
                                q /= p;
                                r /= p;
                                for (j = k; j < nn + 1; j++) {
                                    p = A[k][j] + q * A[k + 1][j];
                                    if (k + 1 != nn) {
                                        p += r * A[k + 2][j];
                                        A[k + 2][j] -= p * z;
                                    }
                                    A[k + 1][j] -= p * y;
                                    A[k][j] -= p * x;
                                }
                                mmin = nn < k + 3 ? nn : k + 3;
                                for (i = l; i < mmin + 1; i++) {
                                    p = x * A[i][k] + y * A[i][k + 1];
                                    if (k + 1 != nn) {
                                        p += z * A[i][k + 2];
                                        A[i][k + 2] -= p * r;
                                    }
                                    A[i][k + 1] -= p * q;
                                    A[i][k] -= p;
                                }
                            }
                        }
                    }
                }
            } while (l + 1 < nn);
        }
    }

    /**
     * Finds all eigenvalues of an upper Hessenberg matrix A[0..n-1][0..n-1].
     * On input A can be exactly as output from elmhes and eltran. On output, d and e
     * contain the eigenvalues of A, while V is a matrix whose columns contain
     * the corresponding eigenvectors. The eigenvalues are not sorted, except
     * that complex conjugate pairs appear consecutively with the eigenvalue
     * having the positive imaginary part. For a complex eigenvalue, only the
     * eigenvector corresponding to the eigenvalue with positive imaginary part
     * is stored, with real part in V[0..n-1][i] and imaginary part in V[0..n-1][i+1].
     * The eigenvectors are not normalized.
     */
    private static void hqr2(double[][] A, double[][] V, double[] d, double[] e) {
        int n = A.length;
        int nn, m, l, k, j, its, i, mmin, na;
        double z = 0.0, y, x, w, v, u, t, s = 0.0, r = 0.0, q = 0.0, p = 0.0, anorm = 0.0, ra, sa, vr, vi;

        for (i = 0; i < n; i++) {
            for (j = MathBase.max(i - 1, 0); j < n; j++) {
                anorm += MathBase.abs(A[i][j]);
            }
        }
        nn = n - 1;
        t = 0.0;
        while (nn >= 0) {
            its = 0;
            do {
                for (l = nn; l > 0; l--) {
                    s = MathBase.abs(A[l - 1][l - 1]) + MathBase.abs(A[l][l]);
                    if (s == 0.0) {
                        s = anorm;
                    }
                    if (MathBase.abs(A[l][l - 1]) <= MathBase.EPSILON * s) {
                        A[l][l - 1] = 0.0;
                        break;
                    }
                }
                x = A[nn][nn];
                if (l == nn) {
                    d[nn] = A[nn][nn] = x + t;
                    nn--;
                } else {
                    y = A[nn - 1][nn - 1];
                    w = A[nn][nn - 1] * A[nn - 1][nn];
                    if (l == nn - 1) {
                        p = 0.5 * (y - x);
                        q = p * p + w;
                        z = MathBase.sqrt(MathBase.abs(q));
                        x += t;
                        A[nn][nn] = x;
                        A[nn - 1][nn - 1] = y + t;
                        if (q >= 0.0) {
                            z = p + MathBase.copySign(z, p);
                            d[nn - 1] = d[nn] = x + z;
                            if (z != 0.0) {
                                d[nn] = x - w / z;
                            }
                            x = A[nn][nn - 1];
                            s = MathBase.abs(x) + MathBase.abs(z);
                            p = x / s;
                            q = z / s;
                            r = MathBase.sqrt(p * p + q * q);
                            p /= r;
                            q /= r;
                            for (j = nn - 1; j < n; j++) {
                                z = A[nn - 1][j];
                                A[nn - 1][j] = q * z + p * A[nn][j];
                                A[nn][j] = q * A[nn][j] - p * z;
                            }
                            for (i = 0; i <= nn; i++) {
                                z = A[i][nn - 1];
                                A[i][nn - 1] = q * z + p * A[i][nn];
                                A[i][nn] = q * A[i][nn] - p * z;
                            }
                            for (i = 0; i < n; i++) {
                                z = V[i][nn - 1];
                                V[i][nn - 1] = q * z + p * V[i][nn];
                                V[i][nn] = q * V[i][nn] - p * z;
                            }
                        } else {
                            d[nn] = x + p;
                            e[nn] = -z;
                            d[nn - 1] = d[nn];
                            e[nn - 1] = -e[nn];
                        }
                        nn -= 2;
                    } else {
                        if (its == 30) {
                            throw new IllegalArgumentException("Too many iterations in hqr");
                        }
                        if (its == 10 || its == 20) {
                            t += x;
                            for (i = 0; i < nn + 1; i++) {
                                A[i][i] -= x;
                            }
                            s = MathBase.abs(A[nn][nn - 1]) + MathBase.abs(A[nn - 1][nn - 2]);
                            y = x = 0.75 * s;
                            w = -0.4375 * s * s;
                        }
                        ++its;
                        for (m = nn - 2; m >= l; m--) {
                            z = A[m][m];
                            r = x - z;
                            s = y - z;
                            p = (r * s - w) / A[m + 1][m] + A[m][m + 1];
                            q = A[m + 1][m + 1] - z - r - s;
                            r = A[m + 2][m + 1];
                            s = MathBase.abs(p) + MathBase.abs(q) + MathBase.abs(r);
                            p /= s;
                            q /= s;
                            r /= s;
                            if (m == l) {
                                break;
                            }
                            u = MathBase.abs(A[m][m - 1]) * (MathBase.abs(q) + MathBase.abs(r));
                            v = MathBase.abs(p) * (MathBase.abs(A[m - 1][m - 1]) + MathBase.abs(z) + MathBase.abs(A[m + 1][m + 1]));
                            if (u <= MathBase.EPSILON * v) {
                                break;
                            }
                        }
                        for (i = m; i < nn - 1; i++) {
                            A[i + 2][i] = 0.0;
                            if (i != m) {
                                A[i + 2][i - 1] = 0.0;
                            }
                        }
                        for (k = m; k < nn; k++) {
                            if (k != m) {
                                p = A[k][k - 1];
                                q = A[k + 1][k - 1];
                                r = 0.0;
                                if (k + 1 != nn) {
                                    r = A[k + 2][k - 1];
                                }
                                if ((x = MathBase.abs(p) + MathBase.abs(q) + MathBase.abs(r)) != 0.0) {
                                    p /= x;
                                    q /= x;
                                    r /= x;
                                }
                            }
                            if ((s = MathBase.copySign(MathBase.sqrt(p * p + q * q + r * r), p)) != 0.0) {
                                if (k == m) {
                                    if (l != m) {
                                        A[k][k - 1] = -A[k][k - 1];
                                    }
                                } else {
                                    A[k][k - 1] = -s * x;
                                }
                                p += s;
                                x = p / s;
                                y = q / s;
                                z = r / s;
                                q /= p;
                                r /= p;
                                for (j = k; j < n; j++) {
                                    p = A[k][j] + q * A[k + 1][j];
                                    if (k + 1 != nn) {
                                        p += r * A[k + 2][j];
                                        A[k + 2][j] -= p * z;
                                    }
                                    A[k + 1][j] -= p * y;
                                    A[k][j] -= p * x;
                                }
                                mmin = nn < k + 3 ? nn : k + 3;
                                for (i = 0; i < mmin + 1; i++) {
                                    p = x * A[i][k] + y * A[i][k + 1];
                                    if (k + 1 != nn) {
                                        p += z * A[i][k + 2];
                                        A[i][k + 2] -= p * r;
                                    }
                                    A[i][k + 1] -= p * q;
                                    A[i][k] -= p;
                                }
                                for (i = 0; i < n; i++) {
                                    p = x * V[i][k] + y * V[i][k + 1];
                                    if (k + 1 != nn) {
                                        p += z * V[i][k + 2];
                                        V[i][k + 2] -= p * r;
                                    }
                                    V[i][k + 1] -= p * q;
                                    V[i][k] -= p;
                                }
                            }
                        }
                    }
                }
            } while (l + 1 < nn);
        }

        if (anorm != 0.0) {
            for (nn = n - 1; nn >= 0; nn--) {
                p = d[nn];
                q = e[nn];
                na = nn - 1;
                if (q == 0.0) {
                    m = nn;
                    A[nn][nn] = 1.0;
                    for (i = nn - 1; i >= 0; i--) {
                        w = A[i][i] - p;
                        r = 0.0;
                        for (j = m; j <= nn; j++) {
                            r += A[i][j] * A[j][nn];
                        }
                        if (e[i] < 0.0) {
                            z = w;
                            s = r;
                        } else {
                            m = i;

                            if (e[i] == 0.0) {
                                t = w;
                                if (t == 0.0) {
                                    t = MathBase.EPSILON * anorm;
                                }
                                A[i][nn] = -r / t;
                            } else {
                                x = A[i][i + 1];
                                y = A[i + 1][i];
                                q = MathBase.sqr(d[i] - p) + MathBase.sqr(e[i]);
                                t = (x * s - z * r) / q;
                                A[i][nn] = t;
                                if (MathBase.abs(x) > MathBase.abs(z)) {
                                    A[i + 1][nn] = (-r - w * t) / x;
                                } else {
                                    A[i + 1][nn] = (-s - y * t) / z;
                                }
                            }
                            t = MathBase.abs(A[i][nn]);
                            if (MathBase.EPSILON * t * t > 1) {
                                for (j = i; j <= nn; j++) {
                                    A[j][nn] /= t;
                                }
                            }
                        }
                    }
                } else if (q < 0.0) {
                    m = na;
                    if (MathBase.abs(A[nn][na]) > MathBase.abs(A[na][nn])) {
                        A[na][na] = q / A[nn][na];
                        A[na][nn] = -(A[nn][nn] - p) / A[nn][na];
                    } else {
                        Complex temp = cdiv(0.0, -A[na][nn], A[na][na] - p, q);
                        A[na][na] = temp.re();
                        A[na][nn] = temp.im();
                    }
                    A[nn][na] = 0.0;
                    A[nn][nn] = 1.0;
                    for (i = nn - 2; i >= 0; i--) {
                        w = A[i][i] - p;
                        ra = sa = 0.0;
                        for (j = m; j <= nn; j++) {
                            ra += A[i][j] * A[j][na];
                            sa += A[i][j] * A[j][nn];
                        }
                        if (e[i] < 0.0) {
                            z = w;
                            r = ra;
                            s = sa;
                        } else {
                            m = i;
                            if (e[i] == 0.0) {
                                Complex temp = cdiv(-ra, -sa, w, q);
                                A[i][na] = temp.re();
                                A[i][nn] = temp.im();
                            } else {
                                x = A[i][i + 1];
                                y = A[i + 1][i];
                                vr = MathBase.sqr(d[i] - p) + MathBase.sqr(e[i]) - q * q;
                                vi = 2.0 * q * (d[i] - p);
                                if (vr == 0.0 && vi == 0.0) {
                                    vr = MathBase.EPSILON * anorm * (MathBase.abs(w) + MathBase.abs(q) + MathBase.abs(x) + MathBase.abs(y) + MathBase.abs(z));
                                }
                                Complex temp = cdiv(x * r - z * ra + q * sa, x * s - z * sa - q * ra, vr, vi);
                                A[i][na] = temp.re();
                                A[i][nn] = temp.im();
                                if (MathBase.abs(x) > MathBase.abs(z) + MathBase.abs(q)) {
                                    A[i + 1][na] = (-ra - w * A[i][na] + q * A[i][nn]) / x;
                                    A[i + 1][nn] = (-sa - w * A[i][nn] - q * A[i][na]) / x;
                                } else {
                                    temp = cdiv(-r - y * A[i][na], -s - y * A[i][nn], z, q);
                                    A[i + 1][na] = temp.re();
                                    A[i + 1][nn] = temp.im();
                                }
                            }
                        }
                        t = MathBase.max(MathBase.abs(A[i][na]), MathBase.abs(A[i][nn]));
                        if (MathBase.EPSILON * t * t > 1) {
                            for (j = i; j <= nn; j++) {
                                A[j][na] /= t;
                                A[j][nn] /= t;
                            }
                        }
                    }
                }
            }

            for (j = n - 1; j >= 0; j--) {
                for (i = 0; i < n; i++) {
                    z = 0.0;
                    for (k = 0; k <= j; k++) {
                        z += V[i][k] * A[k][j];
                    }
                    V[i][j] = z;
                }
            }
        }
    }

    /**
     *  Complex scalar division.
     */
    private static Complex cdiv(double xr, double xi, double yr, double yi) {
        double cdivr, cdivi;
        double r, d;
        if (MathBase.abs(yr) > MathBase.abs(yi)) {
            r = yi / yr;
            d = yr + r * yi;
            cdivr = (xr + r * xi) / d;
            cdivi = (xi - r * xr) / d;
        } else {
            r = yr / yi;
            d = yi + r * yr;
            cdivr = (r * xr + xi) / d;
            cdivi = (r * xi - xr) / d;
        }

        return new Complex(cdivr, cdivi);
    }

    /**
     * Sort eigenvalues.
     */
    private static void sort(double[] d, double[] e) {
        int i = 0;
        int n = d.length;
        for (int j = 1; j < n; j++) {
            double real = d[j];
            double img = e[j];
            for (i = j - 1; i >= 0; i--) {
                if (d[i] >= d[j]) {
                    break;
                }
                d[i + 1] = d[i];
                e[i + 1] = e[i];
            }
            d[i + 1] = real;
            e[i + 1] = img;
        }
    }

    /**
     * Sort eigenvalues and eigenvectors.
     */
    private static void sort(double[] d, double[] e, double[][] V) {
        int i = 0;
        int n = d.length;
        double[] temp = new double[n];
        for (int j = 1; j < n; j++) {
            double real = d[j];
            double img = e[j];
            for (int k = 0; k < n; k++) {
                temp[k] = V[k][j];
            }
            for (i = j - 1; i >= 0; i--) {
                if (d[i] >= d[j]) {
                    break;
                }
                d[i + 1] = d[i];
                e[i + 1] = e[i];
                for (int k = 0; k < n; k++) {
                    V[k][i + 1] = V[k][i];
                }
            }
            d[i + 1] = real;
            e[i + 1] = img;
            for (int k = 0; k < n; k++) {
                V[k][i + 1] = temp[k];
            }
        }
    }
}
