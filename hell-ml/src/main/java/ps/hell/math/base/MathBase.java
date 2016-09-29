package ps.hell.math.base;


import ps.hell.math.base.struct.Function;
import ps.hell.math.base.struct.DifferentiableFunction;
import ps.hell.math.base.struct.DifferentiableMultivariateFunction;
import ps.hell.math.base.struct.MultivariateFunction;
import ps.hell.math.base.struct.SparseArray;
import ps.hell.math.base.matrix.Matrix;
import ps.hell.math.base.matrix.decomposition.*;
import ps.hell.math.base.matrix.inter.IMatrix;
import ps.hell.base.sort.AllSort;

import java.util.*;

/**
 * 数学库 进出方法区域 基础方法区
 * 
 * @author Administrator
 *         ftp://ftp.idsoftware.com/idstuff/source/quake3-1.32b-source.zip
 *
 */
public class MathBase {
	public static final double DEFAULT_DOUBLE_EQUAL = 0.0000000000001;

	/**
	 * Euler's number.
	 */
	public static final double EULERS_NUMBER = 2.718281828;

	/**
	 * Degrees in a semicircle.
	 */
	public static final double DEG_SEMICIRCLE = 180.0;

	/**
	 * Degrees in a circle.
	 */
	public static final double DEG_CIRCLE = 360.0;
	/**
	 * The base of the exponent of the double type.
	 */
	public static int RADIX = 2;


	public static Random random = new Random();

	public static double round(double x) {
		return Math.round(x);
	}



	/**
	 * Round a double vale to given digits such as 10^n, where n is a positive
	 * or negative integer.
	 */
	public static double round(double x, int decimal) {
		if (decimal < 0) {
			return round(x / pow(10, -decimal)) * pow(10, -decimal);
		} else {
			return round(x * pow(10, decimal)) / pow(10, decimal);
		}
	}

	/**
	 * 0-1 sigmod 函数 1/(1+exp(-x))
	 * 
	 * @param val
	 * @return
	 */
	public static double sigmod(double val) {
		return 1 / (1 + Math.exp(-val));
	}

	/**
	 * softmax函数 使用最大的值做归一化处理
	 * 
	 * @param val
	 * @return
	 */
	public double[] softmax(double[] val) {
		double max = max(val);
		double[] re = new double[val.length];
		double sum = 0;
		for (int i = 0; i < val.length; i++) {
			re[i] = Math.exp(val[i] - max);
			sum += re[i];
		}
		for (int i = 0; i < val.length; i++) {
			re[i] /= sum;
		}
		return re;
	}

	/**
	 * -1 ---1 tanh 函数 (exp(x)-exp(-x))/(exp(x)+exp(-x))
	 * 
	 * @param val
	 * @return
	 */
	public static double tanh(double val) {
		double v1 = Math.exp(val);
		double v2 = Math.exp(-val);
		return (v1 - v2) / (v1 + v2);
	}

	/**
	 * -1 ---1 tanh 函数 (exp(x)-exp(-x))/(exp(x)+exp(-x))
	 * 
	 * @param val
	 * @return
	 */
	public static float tanh(float val) {
		float v1 = (float) Math.exp(val);
		float v2 = (float) Math.exp(-val);
		return (v1 - v2) / (v1 + v2);
	}

	/**
	 * sigmod 的导函数 s()*(1-s())
	 * 
	 * @param val
	 * @return
	 */
	public static double sigmodDerivedFunction(double val) {
		double sigmod = sigmod(val);
		return sigmod * (1 - sigmod);
	}

	/**
	 * sigmod 的导函数 s()*(1-s())
	 * 
	 * @param val
	 * @return
	 */
	public static float sigmodDerivedFunction(float val) {
		float sigmod = sigmod(val);
		return sigmod * (1 - sigmod);
	}

	/**
	 * tanh 的导函数 1-(h())^2
	 * 
	 * @param val
	 * @return
	 */
	public static double tanhDerivedFunction(double val) {
		double v1 = tanh(val);
		return 1 - (v1 * v1);
	}

	/**
	 * tanh 的导函数 1-(h())^2
	 * 
	 * @param val
	 * @return
	 */
	public static float tanhDerivedFunction(float val) {
		float v1 = tanh(val);
		return 1 - (v1 * v1);
	}

	/**
	 * sigmod 函数
	 * 
	 * @param val
	 * @return
	 */
	public static float sigmod(float val) {
		return 1f / (1 + (float) Math.exp(-val));
	}

	public static float[] sigmod(float[] val) {
		float[] temp = new float[val.length];
		for (int i = 0; i < val.length; i++) {
			temp[i] = sigmod(val[i]);
		}
		return temp;
	}

	public static float[][] sigmod(float[][] val) {
		float[][] temp = new float[val.length][];
		for (int i = 0; i < val.length; i++) {
			temp[i] = sigmod(val[i]);
		}
		return temp;
	}

	/**
	 * 计算固定的pow方法
	 * 
	 * @param x
	 * @param count
	 * @return
	 */
	public static float pow(float x, int count) {
		float fval = x;
		while (count > 1) {
			fval *= x;
			count--;
		}
		return fval;
	}

	/**
	 * 计算固定的pow方法
	 * 
	 * @param x
	 * @param count
	 * @return
	 */
	public static double pow(double x, int count) {
		double fval = x;
		while (count > 1) {
			fval *= x;
			count--;
		}
		return fval;
	}

	/**
	 * 计算 long 固定的pow方法
	 * 
	 * @param x
	 * @param count
	 * @return
	 */
	public static long pow(long x, int count) {
		long fval = x;
		while (count > 1) {
			fval *= x;
			count--;
		}
		return fval;
	}

	/**
	 * 平方根 快速方法
	 * 
	 * @param x
	 * @return
	 */
	public static float sqrt(float x) {
		float xhalf = 0.5f * x;
		int i = Float.floatToIntBits(x); // get bits for floating VALUE
		i = 0x5f375a86 - (i >> 1); // gives initial guess y0
		x = Float.intBitsToFloat(i); // convert bits BACK to float
		x = x * (1.5f - xhalf * x * x); // Newton step, repeating increases
										// accuracy
		// x = x *(1.5f-xhalf* x* x); // Newton step, repeating increases
		// accuracy
		x = x * (1.5f - xhalf * x * x); // Newton step, repeating increases
										// accuracy
		return 1 / x;
	}

	public static double sqrt(double x) {
		return Math.sqrt(x);
	}

	/**
	 * 平方根 的 倒数 快速方法
	 * 
	 * @param x
	 * @return
	 */
	public static float invSqrt(float x) {
		float xhalf = 0.5f * x;
		int i = Float.floatToIntBits(x); // get bits for floating VALUE
		i = 0x5f375a86 - (i >> 1); // gives initial guess y0
		x = Float.intBitsToFloat(i); // convert bits BACK to float
		x = x * (1.5f - xhalf * x * x); // Newton step, repeating increases
										// accuracy
		// x = x *(1.5f-xhalf* x* x); // Newton step, repeating increases
		// accuracy
		x = x * (1.5f - xhalf * x * x); // Newton step, repeating increases
										// accuracy
		return x;
	}

	public static int abs(int a) {
		int temp = (a >> 31);
		return (a + temp) ^ temp;
	}

	public static double abs(double a) {
		return Math.abs(a);
	}

	public static float abs(float a) {
		return Math.abs(a);
	}

	public static long abs(long a) {
		return Math.abs(a);
	}

	/**
	 * Convert degrees to radians.
	 * 
	 * @param deg
	 *            Degrees.
	 * @return Radians.
	 */
	public static double deg2rad(final double deg) {
		return deg * (Math.PI / DEG_SEMICIRCLE);
	}

	/**
	 * Determine if one double equals another, within the default percision.
	 * 
	 * @param d1
	 *            The first number.
	 * @param d2
	 *            The second number.
	 * @return True if the two doubles are equal.
	 */
	public static boolean doubleEquals(final double d1, final double d2) {
		return Math.abs(d1 - d2) < DEFAULT_DOUBLE_EQUAL;
	}

	/**
	 * sqrt(a^2 + b^2) without under/overflow.
	 * 
	 * @param a
	 *            First param.
	 * @param b
	 *            Second param.
	 * @return The result.
	 */
	public static double hypot(final double a, final double b) {
		double r;
		if (Math.abs(a) > Math.abs(b)) {
			r = b / a;
			r = Math.abs(a) * Math.sqrt(1 + r * r);
		} else if (b != 0) {
			r = a / b;
			r = Math.abs(b) * Math.sqrt(1 + r * r);
		} else {
			r = 0.0;
		}
		return r;
	}

	/**
	 * sqrt(a^2 + b^2) without under/overflow.
	 * 
	 * @param a
	 *            First param.
	 * @param b
	 *            Second param.
	 * @return The result.
	 */
	public static float hypot(final float a, final float b) {
		float r;
		if (Math.abs(a) > Math.abs(b)) {
			r = b / a;
			r = Math.abs(a) * MathBase.sqrt(1 + r * r);
		} else if (b != 0) {
			r = a / b;
			r = Math.abs(b) * MathBase.sqrt(1 + r * r);
		} else {
			r = 0.0f;
		}
		return r;
	}

	/**
	 * Get the index to the greatest number in a double array.
	 * 
	 * @param array
	 *            The array to search.
	 * @return The index of the greatest value, or -1 if empty.
	 */
	public static int maxIndex(final double[] array) {
		int result = -1;

		for (int i = 0; i < array.length; i++) {
			if ((result == -1) || (array[result] < array[i])) {
				result = i;
			}
		}

		return result;
	}

	/**
	 * Get the index to the smallest number in a double array.
	 * 
	 * @param array
	 *            The array to search.
	 * @return The index of the smallest value, or -1 if empty.
	 */
	public static int minIndex(final double[] array) {
		int result = -1;

		for (int i = 0; i < array.length; i++) {
			if ((result == -1) || (array[result] > array[i])) {
				result = i;
			}
		}

		return result;
	}

	/**
	 * Convert radians to degrees.
	 * 
	 * @param rad
	 *            Radians
	 * @return Degrees.
	 */
	public static double rad2deg(final double rad) {
		return rad * (DEG_SEMICIRCLE / Math.PI);
	}

	/**
	 * Calculate x!.
	 * 
	 * @param x
	 *            The number to calculate for.
	 * @return The factorial of x.
	 */
	public static double factorial(int x) {
		double result = 1.0;

		for (int i = 1; i <= x; i++) {
			result *= i;
		}

		return result;
	}

	/**
	 * 平方
	 * 
	 * @param d
	 * @return
	 */
	public static double square(double d) {
		return d * d;
	}

	/**
	 * log10 Returns <tt>log<sub>10</sub>value</tt>.
	 */
	static public double log10(double value) {
		// 1.0 / Math.log(10) == 0.43429448190325176
		return Math.log(value) * 0.43429448190325176;
	}

	/**
	 * log2 Returns <tt>log<sub>2</sub>value</tt>.
	 */
	static public double log2(double value) {
		// 1.0 / Math.log(2) == 1.4426950408889634
		return Math.log(value) * 1.4426950408889634;
	}

	/**
	 * Determine the sign of the value.
	 * 
	 * @param value
	 *            The value to check.
	 * @return -1 if less than zero, 1 if greater, or 0 if zero.
	 */
	public static int sign(final double value) {
		if (Math.abs(value) < DEFAULT_DOUBLE_EQUAL) {
			return 0;
		} else if (value > 0) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * Transform a number in the range (-1,1) to a tri-state value indicated by
	 * -1, 0 or 1.
	 * 
	 * @param value
	 *            The value to consider.
	 * @return -1 if the value is below 1/3, 1 if above 1/3, zero otherwise.
	 */
	public static int thirds(final double value) {
		if (value < -(1.0 / 3.0))
			return -1;
		else if (value > (1.0 / 3.0))
			return 1;
		else
			return 0;
	}

	// // Real cosh function (used to compute complex trig functions)
	// public double cosh(double theta) {
	// return (Math.exp(theta) + Math.exp(-theta)) / 2;
	// }

	// Real sinh function (used to compute complex trig functions)
	public double sinh(double theta) {
		return (Math.exp(theta) - Math.exp(-theta)) / 2;
	}

	/**
	 * Modulus of this Complex number (the distance from the origin in polar
	 * coordinates).
	 * 
	 * @return |z| where z is this Complex number.
	 */
	public double mod(double x, double y) {
		if (x != 0 || y != 0) {
			return Math.sqrt(x * x + y * y);
		} else {
			return 0d;
		}
	}

	/**
	 * Argument of this Complex number (the angle in radians with the x-axis in
	 * polar coordinates).
	 * 
	 * @return arg(z) where z is this Complex number.
	 */
	public double arg(double x, double y) {
		return Math.atan2(y, x);
	}

	/**
	 * Returns the correlation coefficient of two double vectors.
	 *
	 * @param y1
	 *            double vector 1
	 * @param y2
	 *            double vector 2
	 * @param n
	 *            the length of two double vectors
	 * @return the correlation coefficient
	 */
	public static final double correlation(double y1[], double y2[], int n) {

		int i;
		double av1 = 0.0, av2 = 0.0, y11 = 0.0, y22 = 0.0, y12 = 0.0, c;

		if (n <= 1) {
			return 1.0;
		}
		for (i = 0; i < n; i++) {
			av1 += y1[i];
			av2 += y2[i];
		}
		av1 /= (double) n;
		av2 /= (double) n;
		for (i = 0; i < n; i++) {
			y11 += (y1[i] - av1) * (y1[i] - av1);
			y22 += (y2[i] - av2) * (y2[i] - av2);
			y12 += (y1[i] - av1) * (y2[i] - av2);
		}
		if (y11 * y22 == 0.0) {
			c = 1.0;
		} else {
			c = y12 / Math.sqrt(Math.abs(y11 * y22));
		}

		return c;
	}

	/**
	 * Normalizes the doubles in the array by their sum.
	 *
	 * @param doubles
	 *            the array of double
	 * @exception IllegalArgumentException
	 *                if sum is Zero or NaN
	 */
	public static void normalize(double[] doubles) {

		double sum = 0;
		for (int i = 0; i < doubles.length; i++) {
			sum += doubles[i];
		}
		normalize(doubles, sum);
	}

	/**
	 * Normalizes the doubles in the array using the given value.
	 *
	 * @param doubles
	 *            the array of double
	 * @param sum
	 *            the value by which the doubles are to be normalized
	 * @exception IllegalArgumentException
	 *                if sum is zero or NaN
	 */
	public static void normalize(double[] doubles, double sum) {

		if (Double.isNaN(sum)) {
			throw new IllegalArgumentException(
					"Can't normalize array. Sum is NaN.");
		}
		if (sum == 0) {
			// Maybe this should just be a return.
			throw new IllegalArgumentException(
					"Can't normalize array. Sum is zero.");
		}
		for (int i = 0; i < doubles.length; i++) {
			doubles[i] /= sum;
		}
	}

	/**
	 * Returns the kth-smallest value in the array
	 *
	 * @param array
	 *            the array of double
	 * @param k
	 *            the value of k
	 * @return the kth-smallest value
	 */
	public static double kthSmallestValue(double[] array, int k) {

		int[] index = new int[array.length];

		for (int i = 0; i < index.length; i++) {
			index[i] = i;
		}

		return array[index[select(array, index, 0, array.length - 1, k)]];
	}

	/**
	 * Implements computation of the kth-smallest element according to Manber's
	 * "Introduction to Algorithms".
	 *
	 * @param array
	 *            the array of double
	 * @param index
	 *            the index into the array of doubles
	 * @param left
	 *            the first index of the subset
	 * @param right
	 *            the last index of the subset
	 * @param k
	 *            the value of k
	 *
	 * @return the index of the kth-smallest element
	 */
	// @ requires 0 <= first && first <= right && right < array.length;
	private static int select(/* @non_null@ */double[] array, /* @non_null@ */
			int[] index, int left, int right, int k) {

		if (left == right) {
			return left;
		} else {
			int middle = partition(array, index, left, right);
			if ((middle - left + 1) >= k) {
				return select(array, index, left, middle, k);
			} else {
				return select(array, index, middle + 1, right, k
						- (middle - left + 1));
			}
		}
	}

	/**
	 * Partitions the instances around a pivot. Used by quicksort and
	 * kthSmallestValue.
	 *
	 * @param array
	 *            the array of integers to be sorted
	 * @param index
	 *            the index into the array of integers
	 * @param l
	 *            the first index of the subset
	 * @param r
	 *            the last index of the subset
	 *
	 * @return the index of the middle element
	 */
	private static int partition(int[] array, int[] index, int l, int r) {

		double pivot = array[index[(l + r) / 2]];
		int help;

		while (l < r) {
			while ((array[index[l]] < pivot) && (l < r)) {
				l++;
			}
			while ((array[index[r]] > pivot) && (l < r)) {
				r--;
			}
			if (l < r) {
				help = index[l];
				index[l] = index[r];
				index[r] = help;
				l++;
				r--;
			}
		}
		if ((l == r) && (array[index[r]] > pivot)) {
			r--;
		}

		return r;
	}

	/**
	 * Partitions the instances around a pivot. Used by quicksort and
	 * kthSmallestValue.
	 *
	 * @param array
	 *            the array of doubles to be sorted
	 * @param index
	 *            the index into the array of doubles
	 * @param l
	 *            the first index of the subset
	 * @param r
	 *            the last index of the subset
	 *
	 * @return the index of the middle element
	 */
	private static int partition(double[] array, int[] index, int l, int r) {

		double pivot = array[index[(l + r) / 2]];
		int help;

		while (l < r) {
			while ((array[index[l]] < pivot) && (l < r)) {
				l++;
			}
			while ((array[index[r]] > pivot) && (l < r)) {
				r--;
			}
			if (l < r) {
				help = index[l];
				index[l] = index[r];
				index[r] = help;
				l++;
				r--;
			}
		}
		if ((l == r) && (array[index[r]] > pivot)) {
			r--;
		}

		return r;
	}

	/**
	 * Implements computation of the kth-smallest element according to Manber's
	 * "Introduction to Algorithms".
	 *
	 * @param array
	 *            the array of integers
	 * @param index
	 *            the index into the array of integers
	 * @param left
	 *            the first index of the subset
	 * @param right
	 *            the last index of the subset
	 * @param k
	 *            the value of k
	 *
	 * @return the index of the kth-smallest element
	 */
	// @ requires 0 <= first && first <= right && right < array.length;
	private static int select(/* @non_null@ */int[] array, /* @non_null@ */
			int[] index, int left, int right, int k) {

		if (left == right) {
			return left;
		} else {
			int middle = partition(array, index, left, right);
			if ((middle - left + 1) >= k) {
				return select(array, index, left, middle, k);
			} else {
				return select(array, index, middle + 1, right, k
						- (middle - left + 1));
			}
		}
	}

	/**
	 * Calculates the harmonic mean of the values in the supplied array.
	 * 
	 * @param g
	 * @return
	 */
	public static double harmonicMean(double[] g) {
		double sum = 0;

		for (double d : g)
			sum += 1 / d;

		return g.length / sum;
	}

	/**
	 * Returns the log (base 2) of the &Gamma; function. The &Gamma; function is
	 * defined by:
	 *
	 * <blockquote>
	 * 
	 * <pre>
	 * &Gamma;(z) = <big><big><big><big>&#8747;</big></big></big></big><sub><sub><sub><big>0</big></sub></sub></sub><sup><sup><sup><big>&#8734;</big></sup></sup></sup> t<sup>z-1</sup> * e<sup>-t</sup> <i>d</i>t
	 * </pre>
	 * 
	 * </blockquote>
	 *
	 * <p>
	 * The &Gamma; function is the continuous generalization of the factorial
	 * function, so that for real numbers <code>z &gt; 0</code>:
	 *
	 * <blockquote><code>&Gamma;(z+1) = z * &Gamma;(z)</code></blockquote>
	 *
	 * In particular, integers <code>n &gt;= 0</code>, we have:
	 *
	 * <blockquote><code>&Gamma;(n+1) = n!</code></blockquote>
	 *
	 * <p>
	 * In general, &Gamma; satisfies:
	 *
	 * <blockquote>
	 * 
	 * <pre>
	 * &Gamma;(z) = &pi; / (sin(&pi; * z) * &Gamma;(1-z))
	 * </pre>
	 * 
	 * </blockquote>
	 *
	 * <p>
	 * This method uses the Lanczos approximation which is accurate nearly to
	 * the full power of double-precision arithmetic. The Lanczos approximation
	 * is used for inputs in the range <code>[0.5,1.5]</code>, converting
	 * numbers less than 0.5 using the above formulas, and reducing arguments
	 * greater than 1.5 using the factorial-like expansion above.
	 *
	 * <p>
	 * For more information on the &Gamma; function and its computation, see:
	 *
	 * <ul>
	 * <li>Weisstein, Eric W. <a
	 * href="http://mathworld.wolfram.com/GammaFunction.html">Gamma
	 * Function</a>. From MathWorld--A Wolfram Web Resource.</li>
	 * <li>
	 * Weisstein, Eric W. <a
	 * href="http://mathworld.wolfram.com/LanczosApproximation.html">Lanczos
	 * Approximation</a>. From MathWorld--A Wolfram Web Resource.
	 * 
	 * <li>Wikipedia. <a
	 * href="http://en.wikipedia.org/wiki/Gamma_function">Gamma Function</a>.</li>
	 * <li>Wikipedia. <a
	 * href="http://en.wikipedia.org/wiki/Lanczos_approximation">Lanczos
	 * Approximation</a>.</li>
	 * </ul>
	 *
	 * @param z
	 *            The argument to the gamma function.
	 * @return The value of <code>&Gamma;(z)</code>.
	 */
	public static double log2Gamma(double z) {
		if (z < 0.5) {
			return log2(Math.PI)
					- log2(Math.sin(Math.PI * z))
					- log2Gamma(1.0 - z);
		}
		double result = 0.0;
		while (z > 1.5) {
			result += log2(z - 1);
			z -= 1.0;
		}
		return result + log2(lanczosGamma(z));
	}

	static double[] LANCZOS_COEFFS = new double[] { 0.99999999999980993,
			676.5203681218851, -1259.1392167224028, 771.32342877765313,
			-176.61502916214059, 12.507343278686905, -0.13857109526572012,
			9.9843695780195716e-6, 1.5056327351493116e-7 };

	static double SQRT_2_PI = Math.sqrt(2.0 * Math.PI);

	// assumes input in [0.5,1.5] inclusive
	static double lanczosGamma(double z) {
		double zMinus1 = z - 1;
		double x = LANCZOS_COEFFS[0];
		for (int i = 1; i < LANCZOS_COEFFS.length - 2; ++i)
			x += LANCZOS_COEFFS[i] / (zMinus1 + i);
		double t = zMinus1 + (LANCZOS_COEFFS.length - 2) + 0.5;
		return SQRT_2_PI * Math.pow(t, zMinus1 + 0.5)
				* Math.exp(-t) * x;
	}

	/**
	 * Returns the value of the digamma function for the specified value. The
	 * returned values are accurate to at least 13 decimal places.
	 *
	 * <p>
	 * The digamma function is the derivative of the log of the gamma function;
	 * see the method documentation for {@link #log2Gamma(double)} for more
	 * information on the gamma function itself.
	 *
	 * <blockquote>
	 *
	 * <pre>
	 * &Psi;(z)
	 * = <i>d</i> log &Gamma;(z) / <i>d</i>z
	 * = &Gamma;'(z) / &Gamma;(z)
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * <p>
	 * The numerical approximation is derived from:
	 *
	 * <ul>
	 * <li>Richard J. Mathar. 2005. <a
	 * href="http://arxiv.org/abs/math/0403344">Chebyshev Series Expansion of
	 * Inverse Polynomials</a>.
	 * <li>
	 * <li>Richard J. Mathar. 2005. <a
	 * href="http://www.strw.leidenuniv.nl/~mathar/progs/digamma.c"
	 * >digamma.c</a>. (C Program implementing algorithm.)</li>
	 * </ul>
	 *
	 * <i>Implementation Note:</i> The recursive calls in the C implementation
	 * have been transformed into loops and accumulators, and the recursion for
	 * values greater than three replaced with a simpler reduction. The number
	 * of loops required before the fixed length expansion is approximately
	 * integer value of the absolute value of the input. Each loop requires a
	 * floating point division, two additions and a local variable assignment.
	 * The fixed portion of the algorithm is roughly 30 steps requiring four
	 * multiplications, three additions, one static final array lookup, and four
	 * assignments per loop iteration.
	 *
	 * @param x
	 *            Value at which to evaluate the digamma function.
	 * @return The value of the digamma function at the specified value.
	 */
	public static double digamma(double x) {
		if (x <= 0.0 && (x == (double) ((long) x)))
			return Double.NaN;

		double accum = 0.0;
		if (x < 0.0) {
			accum += Math.PI
					/ Math.tan(Math.PI * (1.0 - x));
			x = 1.0 - x;
		}

		if (x < 1.0) {
			while (x < 1.0)
				accum -= 1.0 / x++;
		}

		if (x == 1.0)
			return accum - NEGATIVE_DIGAMMA_1;

		if (x == 2.0)
			return accum + 1.0 - NEGATIVE_DIGAMMA_1;

		if (x == 3.0)
			return accum + 1.5 - NEGATIVE_DIGAMMA_1;

		// simpler recursion than Mahar to reduce recursion
		if (x > 3.0) {
			while (x > 3.0)
				accum += 1.0 / --x;
			return accum + digamma(x);
		}

		x -= 2.0;
		double tNMinus1 = 1.0;
		double tN = x;
		double digamma = DIGAMMA_COEFFS[0] + DIGAMMA_COEFFS[1] * tN;
		for (int n = 2; n < DIGAMMA_COEFFS.length; n++) {
			double tN1 = 2.0 * x * tN - tNMinus1;
			digamma += DIGAMMA_COEFFS[n] * tN1;
			tNMinus1 = tN;
			tN = tN1;
		}
		return accum + digamma;
	}

	/**
	 * The &gamma; constant for computing the digamma function.
	 *
	 * <p>
	 * The value is defined as the negative of the digamma funtion evaluated at
	 * 1:
	 *
	 * <blockquote>
	 *
	 * <pre>
	 * &gamma; = - &Psi;(1)
	 *
	 */
	static double NEGATIVE_DIGAMMA_1 = 0.5772156649015328606065120900824024;

	private static final double DIGAMMA_COEFFS[] = {
			.30459198558715155634315638246624251,
			.72037977439182833573548891941219706,
			-.12454959243861367729528855995001087,
			.27769457331927827002810119567456810e-1,
			-.67762371439822456447373550186163070e-2,
			.17238755142247705209823876688592170e-2,
			-.44817699064252933515310345718960928e-3,
			.11793660000155572716272710617753373e-3,
			-.31253894280980134452125172274246963e-4,
			.83173997012173283398932708991137488e-5,
			-.22191427643780045431149221890172210e-5,
			.59302266729329346291029599913617915e-6,
			-.15863051191470655433559920279603632e-6,
			.42459203983193603241777510648681429e-7,
			-.11369129616951114238848106591780146e-7,
			.304502217295931698401459168423403510e-8,
			-.81568455080753152802915013641723686e-9,
			.21852324749975455125936715817306383e-9,
			-.58546491441689515680751900276454407e-10,
			.15686348450871204869813586459513648e-10,
			-.42029496273143231373796179302482033e-11,
			.11261435719264907097227520956710754e-11,
			-.30174353636860279765375177200637590e-12,
			.80850955256389526647406571868193768e-13,
			-.21663779809421233144009565199997351e-13,
			.58047634271339391495076374966835526e-14,
			-.15553767189204733561108869588173845e-14,
			.41676108598040807753707828039353330e-15,
			-.11167065064221317094734023242188463e-15 };

	/**
	 * Returns the relative absolute difference between the specified values,
	 * defined to be:
	 *
	 * <blockquote>
	 *
	 * <pre>
	 * relAbsDiff(x, y) = abs(x - y) / (abs(x) + abs(y))
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * @param x
	 *            First value.
	 * @param y
	 *            Second value.
	 * @return The absolute relative difference between the values.
	 */
	public static double relativeAbsoluteDifference(double x, double y) {
		return (Double.isInfinite(x) || Double.isInfinite(y)) ? Double.POSITIVE_INFINITY
				: (Math.abs(x - y) / (Math.abs(x) + Math
						.abs(y)));
	}

	/**
	 * This method returns the log of the sum of the natural exponentiated
	 * values in the specified array. Mathematically, the result is
	 *
	 * <blockquote>
	 *
	 * <pre>
	 * logSumOfExponentials(xs) = log <big><big>( &Sigma;</big></big><sub>i</sub> exp(xs[i]) <big><big>)</big></big>
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * But the result is not calculated directly. Instead, the calculation
	 * performed is:
	 *
	 * <blockquote>
	 *
	 * <pre>
	 * logSumOfExponentials(xs) = max(xs) + log <big><big>( &Sigma;</big></big><sub>i</sub> exp(xs[i] - max(xs)) <big><big>)</big></big>
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * which produces the same result, but is much more arithmetically stable,
	 * because the largest value for which <code>exp()</code> is calculated is
	 * 0.0.
	 *
	 * <p>
	 * Values of {@code Double.NEGATIVE_INFINITY} are treated as having
	 * exponentials of 0 and logs of negative infinity. That is, they are
	 * ignored for the purposes of this computation.
	 *
	 * @param xs
	 *            Array of values.
	 * @return The log of the sum of the exponentiated values in the array.
	 */
	public static double logSumOfExponentials(double[] xs) {
		if (xs.length == 1)
			return xs[0];
		double max = maximum(xs);
		double sum = 0.0;
		for (int i = 0; i < xs.length; ++i)
			if (xs[i] != Double.NEGATIVE_INFINITY)
				sum += Math.exp(xs[i] - max);
		return max + Math.log(sum);
	}

	/**
	 * Returns the maximum value of an element in xs. If any of the values are
	 * {@code Double.NaN}, or if the input array is empty, the result is
	 * {@code Double.NaN}.
	 *
	 * @param xs
	 *            Array in which to find maximum.
	 * @return Maximum value in array.
	 */
	public static double max(double... xs) {
		if (xs.length == 0)
			return Double.NaN;
		double max = xs[0];
		for (int i = 1; i < xs.length; ++i)
			max = Math.max(max, xs[i]);
		return max;
	}

	/**
	 * Returns the maximum value of an element in xs. If any of the values are
	 * {@code Double.NaN}, or if the input array is empty, the result is
	 * {@code Double.NaN}.
	 *
	 * @param xs
	 *            Array in which to find maximum.
	 * @return Maximum value in array.
	 */
	public static double min(double... xs) {
		if (xs.length == 0)
			return Double.NaN;
		double max = xs[0];
		for (int i = 1; i < xs.length; ++i)
			max = Math.min(max, xs[i]);
		return max;
	}

	/**
	 * Returns the minimum of a matrix.
	 */
	public static int min(int[][] matrix) {
		int m = matrix[0][0];

		for (int[] x : matrix) {
			for (int y : x) {
				if (m > y) {
					m = y;
				}
			}
		}

		return m;
	}

	/**
	 * Returns the minimum of a matrix.
	 */
	public static double min(double[][] matrix) {
		double m = Double.POSITIVE_INFINITY;

		for (double[] x : matrix) {
			for (double y : x) {
				if (m > y) {
					m = y;
				}
			}
		}

		return m;
	}

	/**
	 * Returns the maximum of a matrix.
	 */
	public static int max(int[][] matrix) {
		int m = matrix[0][0];

		for (int[] x : matrix) {
			for (int y : x) {
				if (m < y) {
					m = y;
				}
			}
		}

		return m;
	}

	/**
	 * Returns the maximum of a matrix.
	 */
	public static double max(double[][] matrix) {
		double m = Double.NEGATIVE_INFINITY;

		for (double[] x : matrix) {
			for (double y : x) {
				if (m < y) {
					m = y;
				}
			}
		}

		return m;
	}

	/**
	 * Returns the maximum value of an element in the specified array.
	 *
	 * @param xs
	 *            Array in which to find maximum.
	 * @return Maximum value in the array.
	 * @throws ArrayIndexOutOfBoundsException
	 *             If the specified array does not contai at least one element.
	 */
	public static int max(int... xs) {
		int max = xs[0];
		for (int i = 1; i < xs.length; ++i)
			if (xs[i] > max)
				max = xs[i];
		return max;
	}

	/**
	 * Returns the sum of the specified integer array. Note that there is no
	 * check for overflow. If the array is of length 0, the sum is defined to be
	 * 0.
	 *
	 * @param xs
	 *            Array of integers to sum.
	 * @return Sum of the array.
	 */
	public static int sum(int... xs) {
		int sum = 0;
		for (int i = 0; i < xs.length; ++i)
			sum += xs[i];
		return sum;
	}

	/**
	 * Returns the minimum of the specified array of double values. If the
	 * length of the array is zero, the result is {@link Double#NaN}.
	 *
	 * @param xs
	 *            Variable length list of values, or an array.
	 * @return Minimum value in array.
	 */
	public static double minimum(double... xs) {
		if (xs.length == 0)
			return Double.NaN;
		double min = xs[0];
		for (int i = 1; i < xs.length; ++i)
			if (xs[i] < min)
				min = xs[i];
		return min;
	}

	/**
	 * Returns the maximum of the specified array of double values. If the
	 * length of the array is zero, the result is {@link Double#NaN}.
	 *
	 * @param xs
	 *            Variable length list of values, or an array.
	 * @return Maximum value in array.
	 */
	public static double maximum(double... xs) {
		if (xs.length == 0)
			return Double.NaN;
		double max = xs[0];
		for (int i = 1; i < xs.length; ++i)
			if (xs[i] > max)
				max = xs[i];
		return max;
	}

	/**
	 * 获取分位数的值
	 *
	 * @param data
	 * @param q
	 * @return
	 */
	public double quant(double[] data, double q) {
		double[] sortx = data.clone();
		AllSort.quickSort(sortx, 0, data.length, true);
		if (q > 1 || q < 0)
			return (0);
		else {
			double index = (data.length + 1) * q;
			if (index - (int) index == 0)
				return sortx[(int) index - 1];
			else
				return q * sortx[(int) Math.floor(index) - 1] + (1 - q)
						* sortx[(int) Math.ceil(index) - 1];
		}
	}

	// From libbow, dirichlet.c
	// Written by Tom Minka <minka@stat.cmu.edu>
	public static final double logGamma(double x) {
		double result, y, xnum, xden;
		int i;
		final double d1 = -5.772156649015328605195174e-1;
		final double p1[] = { 4.945235359296727046734888e0,
				2.018112620856775083915565e2, 2.290838373831346393026739e3,
				1.131967205903380828685045e4, 2.855724635671635335736389e4,
				3.848496228443793359990269e4, 2.637748787624195437963534e4,
				7.225813979700288197698961e3 };
		final double q1[] = { 6.748212550303777196073036e1,
				1.113332393857199323513008e3, 7.738757056935398733233834e3,
				2.763987074403340708898585e4, 5.499310206226157329794414e4,
				6.161122180066002127833352e4, 3.635127591501940507276287e4,
				8.785536302431013170870835e3 };
		final double d2 = 4.227843350984671393993777e-1;
		final double p2[] = { 4.974607845568932035012064e0,
				5.424138599891070494101986e2, 1.550693864978364947665077e4,
				1.847932904445632425417223e5, 1.088204769468828767498470e6,
				3.338152967987029735917223e6, 5.106661678927352456275255e6,
				3.074109054850539556250927e6 };
		final double q2[] = { 1.830328399370592604055942e2,
				7.765049321445005871323047e3, 1.331903827966074194402448e5,
				1.136705821321969608938755e6, 5.267964117437946917577538e6,
				1.346701454311101692290052e7, 1.782736530353274213975932e7,
				9.533095591844353613395747e6 };
		final double d4 = 1.791759469228055000094023e0;
		final double p4[] = { 1.474502166059939948905062e4,
				2.426813369486704502836312e6, 1.214755574045093227939592e8,
				2.663432449630976949898078e9, 2.940378956634553899906876e10,
				1.702665737765398868392998e11, 4.926125793377430887588120e11,
				5.606251856223951465078242e11 };
		final double q4[] = { 2.690530175870899333379843e3,
				6.393885654300092398984238e5, 4.135599930241388052042842e7,
				1.120872109616147941376570e9, 1.488613728678813811542398e10,
				1.016803586272438228077304e11, 3.417476345507377132798597e11,
				4.463158187419713286462081e11 };
		final double c[] = { -1.910444077728e-03, 8.4171387781295e-04,
				-5.952379913043012e-04, 7.93650793500350248e-04,
				-2.777777777777681622553e-03, 8.333333333333333331554247e-02,
				5.7083835261e-03 };
		final double a = 0.6796875;

		if ((x <= 0.5) || ((x > a) && (x <= 1.5))) {
			if (x <= 0.5) {
				result = -Math.log(x);
				/* Test whether X < machine epsilon. */
				if (x + 1 == 1) {
					return result;
				}
			} else {
				result = 0;
				x = (x - 0.5) - 0.5;
			}
			xnum = 0;
			xden = 1;
			for (i = 0; i < 8; i++) {
				xnum = xnum * x + p1[i];
				xden = xden * x + q1[i];
			}
			result += x * (d1 + x * (xnum / xden));
		} else if ((x <= a) || ((x > 1.5) && (x <= 4))) {
			if (x <= a) {
				result = -Math.log(x);
				x = (x - 0.5) - 0.5;
			} else {
				result = 0;
				x -= 2;
			}
			xnum = 0;
			xden = 1;
			for (i = 0; i < 8; i++) {
				xnum = xnum * x + p2[i];
				xden = xden * x + q2[i];
			}
			result += x * (d2 + x * (xnum / xden));
		} else if (x <= 12) {
			x -= 4;
			xnum = 0;
			xden = -1;
			for (i = 0; i < 8; i++) {
				xnum = xnum * x + p4[i];
				xden = xden * x + q4[i];
			}
			result = d4 + x * (xnum / xden);
		}
		/* X > 12 */
		else {
			y = Math.log(x);
			result = x * (y - 1) - y * 0.5 + .9189385332046727417803297;
			x = 1 / x;
			y = x * x;
			xnum = c[6];
			for (i = 0; i < 6; i++) {
				xnum = xnum * y + c[i];
			}
			xnum *= x;
			result += xnum;
		}
		return result;
	}

	// This is from "Numeric Recipes in C"
	public static double oldLogGamma(double x) {
		int j;
		double y, tmp, ser;
		double[] cof = { 76.18009172947146, -86.50532032941677,
				24.01409824083091, -1.231739572450155, 0.1208650973866179e-2,
				-0.5395239384953e-5 };
		y = x;
		tmp = x + 5.5 - (x + 0.5) * Math.log(x + 5.5);
		ser = 1.000000000190015;
		for (j = 0; j <= 5; j++)
			ser += cof[j] / ++y;
		return Math.log(2.5066282746310005 * ser / x) - tmp;
	}

	public static double logBeta(double a, double b) {
		return logGamma(a) + logGamma(b) - logGamma(a + b);
	}

	public static double beta(double a, double b) {
		return Math.exp(logBeta(a, b));
	}

	public static double gamma(double x) {
		return Math.exp(logGamma(x));
	}

	public static double factorialGama(int n) {
		return Math.exp(logGamma(n + 1));
	}

	public static double logFactorial(int n) {
		return logGamma(n + 1);
	}

	/**
	 * Computes p(x;n,p) where x~B(n,p)
	 */
	// Copied as the "classic" method from Catherine Loader.
	// Fast and Accurate Computation of Binomial Probabilities.
	// 2001. (This is not the fast and accurate version.)
	public static double logBinom(int x, int n, double p) {
		return logFactorial(n) - logFactorial(x) - logFactorial(n - x)
				+ (x * Math.log(p)) + ((n - x) * Math.log(1 - p));
	}

	/**
	 * Vastly inefficient O(x) method to compute cdf of B(n,p)
	 */
	public static double pbinom(int x, int n, double p) {
		double sum = Double.NEGATIVE_INFINITY;
		for (int i = 0; i <= x; i++) {
			sum = sumLogProb(sum, logBinom(i, n, p));
		}
		return Math.exp(sum);
	}

	public static double sigmod_rev(double sig) {
		return (double) Math.log(sig / (1 - sig));
	}

	public static double logit(double p) {
		return Math.log(p / (1 - p));
	}

	// Combination?
	public static double numCombinations(int n, int r) {
		return Math
				.exp(logFactorial(n) - logFactorial(r) - logFactorial(n - r));
	}

	// Permutation?
	public static double numPermutations(int n, int r) {
		return Math.exp(logFactorial(n) - logFactorial(r));
	}

	public static double cosh(double a) {
		if (a < 0)
			return 0.5 * (Math.exp(-a) + Math.exp(a));
		else
			return 0.5 * (Math.exp(a) + Math.exp(-a));
	}

	// public static double tanh(double a) {
	// return (Math.exp(a) - Math.exp(-a)) / (Math.exp(a) + Math.exp(-a));
	// }

	/**
	 * Numbers that are closer than this are considered equal by almostEquals.
	 */
	public static double EPSILON = 0.000001;

	public static boolean almostEquals(double d1, double d2) {
		return almostEquals(d1, d2, EPSILON);
	}

	public static boolean almostEquals(double d1, double d2, double epsilon) {
		return Math.abs(d1 - d2) < epsilon;
	}

	public static boolean almostEquals(double[] d1, double[] d2, double eps) {
		for (int i = 0; i < d1.length; i++) {
			double v1 = d1[i];
			double v2 = d2[i];
			if (!almostEquals(v1, v2, eps))
				return false;
		}
		return true;
	}

	// gsc
	/**
	 * Checks if <tt>min &lt;= value &lt;= max</tt>.
	 */
	public static boolean checkWithinRange(double value, double min, double max) {
		return (value > min || almostEquals(value, min, EPSILON))
				&& (value < max || almostEquals(value, max, EPSILON));
	}

	public static final double log2 = Math.log(2);

	// gsc
	/**
	 * Returns the KL divergence, K(p1 || p2).
	 *
	 * The log is w.r.t. base 2.
	 * <p>
	 *
	 * *Note*: If any value in <tt>p2</tt> is <tt>0.0</tt> then the
	 * KL-divergence is <tt>infinite</tt>.
	 *
	 */
	public static double klDivergence(double[] p1, double[] p2) {
		assert (p1.length == p2.length);
		double klDiv = 0.0;
		for (int i = 0; i < p1.length; ++i) {
			if (p1[i] == 0) {
				continue;
			}
			if (p2[i] == 0) {
				return Double.POSITIVE_INFINITY;
			}
			klDiv += p1[i] * Math.log(p1[i] / p2[i]);
		}
		return klDiv / log2; // moved this division out of the loop -DM
	}

	// gsc
	/**
	 * Returns the Jensen-Shannon divergence.
	 */
	public static double jensenShannonDivergence(double[] p1, double[] p2) {
		assert (p1.length == p2.length);
		double[] average = new double[p1.length];
		for (int i = 0; i < p1.length; ++i) {
			average[i] += (p1[i] + p2[i]) / 2;
		}
		return (klDivergence(p1, average) + klDivergence(p2, average)) / 2;
	}
	/**
	 * Returns the sum of two doubles expressed in log space, that is,
	 *
	 * <pre>
	 *    sumLogProb = log (e^a + e^b)
	 *               = log e^a(1 + e^(b-a))
	 *               = a + log (1 + e^(b-a))
	 * </pre>
	 *
	 * By exponentiating <tt>b-a</tt>, we obtain better numerical precision than
	 * we would if we calculated <tt>e^a</tt> or <tt>e^b</tt> directly.
	 * <P>
	 * Note: This function is just like
	 * in
	 * <TT>Transducer</TT>, except that the logs aren't negated.
	 */
	public static double sumLogProb(double a, double b) {
		if (a == Double.NEGATIVE_INFINITY)
			return b;
		else if (b == Double.NEGATIVE_INFINITY)
			return a;
		else if (b < a)
			return a + Math.log(1 + Math.exp(b - a));
		else
			return b + Math.log(1 + Math.exp(a - b));
	}

	// Below from Stanford NLP package, SloppyMath.java

	private static final double LOGTOLERANCE = 30.0;

	/**
	 * Sums an array of numbers log(x1)...log(xn). This saves some of the
	 * unnecessary calls to Math.log in the two-argument version.
	 * <p>
	 * Note that this implementation IGNORES elements of the input array that
	 * are more than LOGTOLERANCE (currently 30.0) less than the maximum
	 * element.
	 * <p>
	 * Cursory testing makes me wonder if this is actually much faster than
	 * repeated use of the 2-argument version, however -cas.
	 *
	 * @param vals
	 *            An array log(x1), log(x2), ..., log(xn)
	 * @return log(x1+x2+...+xn)
	 */
	public static double sumLogProb(double[] vals) {
		double max = Double.NEGATIVE_INFINITY;
		int len = vals.length;
		int maxidx = 0;

		for (int i = 0; i < len; i++) {
			if (vals[i] > max) {
				max = vals[i];
				maxidx = i;
			}
		}

		boolean anyAdded = false;
		double intermediate = 0.0;
		double cutoff = max - LOGTOLERANCE;

		for (int i = 0; i < maxidx; i++) {
			if (vals[i] >= cutoff) {
				anyAdded = true;
				intermediate += Math.exp(vals[i] - max);
			}
		}
		for (int i = maxidx + 1; i < len; i++) {
			if (vals[i] >= cutoff) {
				anyAdded = true;
				intermediate += Math.exp(vals[i] - max);
			}
		}

		if (anyAdded) {
			return max + Math.log(1.0 + intermediate);
		} else {
			return max;
		}

	}

	/**
	 * Returns the difference of two doubles expressed in log space, that is,
	 *
	 * <pre>
	 *    sumLogProb = log (e^a - e^b)
	 *               = log e^a(1 - e^(b-a))
	 *               = a + log (1 - e^(b-a))
	 * </pre>
	 *
	 * By exponentiating <tt>b-a</tt>, we obtain better numerical precision than
	 * we would if we calculated <tt>e^a</tt> or <tt>e^b</tt> directly.
	 * <p>
	 * Returns <tt>NaN</tt> if b > a (so that log(e^a - e^b) is undefined).
	 */
	public static double subtractLogProb(double a, double b) {
		if (b == Double.NEGATIVE_INFINITY)
			return a;
		else
			return a + Math.log(1 - Math.exp(b - a));
	}

	public static double getEntropy(double[] dist) {
		double entropy = 0;
		for (int i = 0; i < dist.length; i++) {
			if (dist[i] != 0) {
				entropy -= dist[i] * Math.log(dist[i]);
			}
		}
		return entropy;
	}

	// for method stirlingCorrection(...)
	private static final double[] stirlingCorrection = { 0.0,
			8.106146679532726e-02, 4.134069595540929e-02,
			2.767792568499834e-02, 2.079067210376509e-02,
			1.664469118982119e-02, 1.387612882307075e-02,
			1.189670994589177e-02, 1.041126526197209e-02,
			9.255462182712733e-03, 8.330563433362871e-03,
			7.573675487951841e-03, 6.942840107209530e-03,
			6.408994188004207e-03, 5.951370112758848e-03,
			5.554733551962801e-03, 5.207655919609640e-03,
			4.901395948434738e-03, 4.629153749334029e-03,
			4.385560249232324e-03, 4.166319691996922e-03,
			3.967954218640860e-03, 3.787618068444430e-03,
			3.622960224683090e-03, 3.472021382978770e-03,
			3.333155636728090e-03, 3.204970228055040e-03,
			3.086278682608780e-03, 2.976063983550410e-03,
			2.873449362352470e-03, 2.777674929752690e-03, };

	// for method logFactorial(...)
	// log(k!) for k = 0, ..., 29
	protected static final double[] logFactorials = { 0.00000000000000000,
			0.00000000000000000, 0.69314718055994531, 1.79175946922805500,
			3.17805383034794562, 4.78749174278204599, 6.57925121201010100,
			8.52516136106541430, 10.60460290274525023, 12.80182748008146961,
			15.10441257307551530, 17.50230784587388584, 19.98721449566188615,
			22.55216385312342289, 25.19122118273868150, 27.89927138384089157,
			30.67186010608067280, 33.50507345013688888, 36.39544520803305358,
			39.33988418719949404, 42.33561646075348503, 45.38013889847690803,
			48.47118135183522388, 51.60667556776437357, 54.78472939811231919,
			58.00360522298051994, 61.26170176100200198, 64.55753862700633106,
			67.88974313718153498, 71.25703896716800901 };

	// k! for k = 0, ..., 20
	protected static final long[] longFactorials = { 1L, 1L, 2L, 6L, 24L, 120L,
			720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L,
			6227020800L, 87178291200L, 1307674368000L, 20922789888000L,
			355687428096000L, 6402373705728000L, 121645100408832000L,
			2432902008176640000L };

	// k! for k = 21, ..., 170
	protected static final double[] doubleFactorials = { 5.109094217170944E19,
			1.1240007277776077E21, 2.585201673888498E22, 6.204484017332394E23,
			1.5511210043330984E25, 4.032914611266057E26, 1.0888869450418352E28,
			3.048883446117138E29, 8.841761993739701E30, 2.652528598121911E32,
			8.222838654177924E33, 2.6313083693369355E35, 8.68331761881189E36,
			2.952327990396041E38, 1.0333147966386144E40, 3.719933267899013E41,
			1.3763753091226346E43, 5.23022617466601E44, 2.0397882081197447E46,
			8.15915283247898E47, 3.34525266131638E49, 1.4050061177528801E51,
			6.041526306337384E52, 2.6582715747884495E54, 1.196222208654802E56,
			5.502622159812089E57, 2.5862324151116827E59, 1.2413915592536068E61,
			6.082818640342679E62, 3.0414093201713376E64, 1.5511187532873816E66,
			8.06581751709439E67, 4.274883284060024E69, 2.308436973392413E71,
			1.2696403353658264E73, 7.109985878048632E74, 4.052691950487723E76,
			2.350561331282879E78, 1.386831185456898E80, 8.32098711274139E81,
			5.075802138772246E83, 3.146997326038794E85, 1.9826083154044396E87,
			1.2688693218588414E89, 8.247650592082472E90, 5.443449390774432E92,
			3.6471110918188705E94, 2.48003554243683E96, 1.7112245242814127E98,
			1.1978571669969892E100, 8.504785885678624E101,
			6.123445837688612E103, 4.470115461512686E105,
			3.307885441519387E107, 2.4809140811395404E109,
			1.8854947016660506E111, 1.451830920282859E113,
			1.1324281178206295E115, 8.94618213078298E116, 7.15694570462638E118,
			5.797126020747369E120, 4.7536433370128435E122,
			3.94552396972066E124, 3.314240134565354E126,
			2.8171041143805494E128, 2.4227095383672744E130,
			2.107757298379527E132, 1.854826422573984E134,
			1.6507955160908465E136, 1.4857159644817605E138,
			1.3520015276784033E140, 1.2438414054641305E142,
			1.156772507081641E144, 1.0873661566567426E146,
			1.0329978488239061E148, 9.916779348709491E149,
			9.619275968248216E151, 9.426890448883248E153,
			9.332621544394415E155, 9.332621544394418E157, 9.42594775983836E159,
			9.614466715035125E161, 9.902900716486178E163,
			1.0299016745145631E166, 1.0813967582402912E168,
			1.1462805637347086E170, 1.2265202031961373E172,
			1.324641819451829E174, 1.4438595832024942E176,
			1.5882455415227423E178, 1.7629525510902457E180,
			1.974506857221075E182, 2.2311927486598138E184,
			2.543559733472186E186, 2.925093693493014E188,
			3.393108684451899E190, 3.96993716080872E192,
			4.6845258497542896E194, 5.574585761207606E196,
			6.689502913449135E198, 8.094298525273444E200,
			9.875044200833601E202, 1.2146304367025332E205,
			1.506141741511141E207, 1.882677176888926E209,
			2.3721732428800483E211, 3.0126600184576624E213,
			3.856204823625808E215, 4.974504222477287E217,
			6.466855489220473E219, 8.471580690878813E221,
			1.1182486511960037E224, 1.4872707060906847E226,
			1.99294274616152E228, 2.690472707318049E230,
			3.6590428819525483E232, 5.0128887482749884E234,
			6.917786472619482E236, 9.615723196941089E238,
			1.3462012475717523E241, 1.8981437590761713E243,
			2.6953641378881633E245, 3.8543707171800694E247,
			5.550293832739308E249, 8.047926057471989E251,
			1.1749972043909107E254, 1.72724589045464E256,
			2.5563239178728637E258, 3.8089226376305687E260,
			5.7133839564458575E262, 8.627209774233244E264,
			1.3113358856834527E267, 2.0063439050956838E269,
			3.0897696138473515E271, 4.789142901463393E273,
			7.471062926282892E275, 1.1729568794264134E278,
			1.8532718694937346E280, 2.946702272495036E282,
			4.714723635992061E284, 7.590705053947223E286,
			1.2296942187394494E289, 2.0044015765453032E291,
			3.287218585534299E293, 5.423910666131583E295,
			9.003691705778434E297, 1.5036165148649983E300,
			2.5260757449731988E302, 4.2690680090047056E304,
			7.257415615308004E306 };

	/**
	 * Efficiently returns the binomial coefficient, often also referred to as
	 * "n over k" or "n choose k". The binomial coefficient is defined as
	 * <tt>(n * n-1 * ... * n-k+1 ) / ( 1 * 2 * ... * k )</tt>.
	 * <ul>
	 * <li>k<0<tt>: <tt>0</tt>.
	 * <li>k==0<tt>: <tt>1</tt>.
	 * <li>k==1<tt>: <tt>n</tt>.
	 * <li>else: <tt>(n * n-1 * ... * n-k+1 ) / ( 1 * 2 * ... * k )</tt>.
	 * </ul>
	 *
	 * @return the binomial coefficient.
	 */
	public static double binomial(double n, long k) {
		if (k < 0)
			return 0;
		if (k == 0)
			return 1;
		if (k == 1)
			return n;

		// binomial(n,k) = (n * n-1 * ... * n-k+1 ) / ( 1 * 2 * ... * k )
		double a = n - k + 1;
		double b = 1;
		double binomial = 1;
		for (long i = k; i-- > 0;) {
			binomial *= (a++) / (b++);
		}
		return binomial;
	}

	/**
	 * Efficiently returns the binomial coefficient, often also referred to as
	 * "n over k" or "n choose k". The binomial coefficient is defined as
	 * <ul>
	 * <li>k<0<tt>: <tt>0</tt>.
	 * <li>k==0 || k==n<tt>: <tt>1</tt>.
	 * <li>k==1 || k==n-1<tt>: <tt>n</tt>.
	 * <li>else: <tt>(n * n-1 * ... * n-k+1 ) / ( 1 * 2 * ... * k )</tt>.
	 * </ul>
	 *
	 * @return the binomial coefficient.
	 */
	public static double binomial(long n, long k) {
		if (k < 0)
			return 0;
		if (k == 0 || k == n)
			return 1;
		if (k == 1 || k == n - 1)
			return n;

		// try quick version and see whether we get numeric overflows.
		// factorial(..) is O(1); requires no loop; only a table lookup.
		if (n > k) {
			int max = longFactorials.length + doubleFactorials.length;
			if (n < max) { // if (n! < inf && k! < inf)
				double n_fac = factorial((int) n);
				double k_fac = factorial((int) k);
				double n_minus_k_fac = factorial((int) (n - k));
				double nk = n_minus_k_fac * k_fac;
				if (nk != Double.POSITIVE_INFINITY) { // no numeric overflow?
					// now this is completely safe and accurate
					return n_fac / nk;
				}
			}
			if (k > n / 2)
				k = n - k; // quicker
		}

		// binomial(n,k) = (n * n-1 * ... * n-k+1 ) / ( 1 * 2 * ... * k )
		long a = n - k + 1;
		long b = 1;
		double binomial = 1;
		for (long i = k; i-- > 0;) {
			binomial *= ((double) (a++)) / (b++);
		}
		return binomial;
	}

	/**
	 * Returns the smallest <code>long &gt;= value</code>. <dt>Examples:
	 * <code>1.0 -> 1, 1.2 -> 2, 1.9 -> 2</code>. This method is safer than
	 * using (long) Math.ceil(value), because of possible rounding error.
	 */
	public static long ceil(double value) {
		return Math.round(Math.ceil(value));
	}

	/**
	 * Evaluates the series of Chebyshev polynomials Ti at argument x/2. The
	 * series is given by
	 *
	 * <pre>
	 *        N-1
	 *         - '
	 *  y  =   >   coef[i] T (x/2)
	 *         -            i
	 *        i=0
	 * </pre>
	 *
	 * Coefficients are stored in reverse order, i.e. the zero order term is
	 * last in the array. Note N is the number of coefficients, not the order.
	 * <p>
	 * If coefficients are for the interval a to b, x must have been transformed
	 * to x -> 2(2x - b - a)/(b-a) before entering the routine. This maps x from
	 * (a, b) to (-1, 1), over which the Chebyshev polynomials are defined.
	 * <p>
	 * If the coefficients are for the inverted interval, in which (a, b) is
	 * mapped to (1/b, 1/a), the transformation required is x -> 2(2ab/x - b -
	 * a)/(b-a). If b is infinity, this becomes x -> 4a/x - 1.
	 * <p>
	 * SPEED:
	 * <p>
	 * Taking advantage of the recurrence properties of the Chebyshev
	 * polynomials, the routine requires one more addition per loop than
	 * evaluating a nested polynomial of the same degree.
	 *
	 * @param x
	 *            argument to the polynomial.
	 * @param coef
	 *            the coefficients of the polynomial.
	 * @param N
	 *            the number of coefficients.
	 */
	public static double chbevl(double x, double coef[], int N)
			throws ArithmeticException {
		double b0, b1, b2;

		int p = 0;
		int i;

		b0 = coef[p++];
		b1 = 0.0;
		i = N - 1;

		do {
			b2 = b1;
			b1 = b0;
			b0 = x * b1 - b2 + coef[p++];
		} while (--i > 0);

		return (0.5 * (b0 - b2));
	}

	/**
	 * Returns the factorial of the argument.
	 */
	static private long fac1(int j) {
		long i = j;
		if (j < 0)
			i = Math.abs(j);
		if (i > longFactorials.length)
			throw new IllegalArgumentException("Overflow");

		long d = 1;
		while (i > 1)
			d *= i--;

		if (j < 0)
			return -d;
		else
			return d;
	}

	/**
	 * Returns the factorial of the argument.
	 */
	static private double fac2(int j) {
		long i = j;
		if (j < 0)
			i = Math.abs(j);

		double d = 1.0;
		while (i > 1)
			d *= i--;

		if (j < 0)
			return -d;
		else
			return d;
	}

	/**
	 * Returns the largest <code>long &lt;= value</code>. <dt>Examples: <code>
	 * 1.0 -> 1, 1.2 -> 1, 1.9 -> 1 <dt>
	 * 2.0 -> 2, 2.2 -> 2, 2.9 -> 2 </code><dt>
	 * This method is safer than using (long) Math.floor(value), because of
	 * possible rounding error.
	 */
	public static long floor(double value) {
		return Math.round(Math.floor(value));
	}

	/**
	 * Returns <tt>log<sub>base</sub>value</tt>.
	 */
	public static double log(double base, double value) {
		return Math.log(value) / Math.log(base);
	}

	/**
	 * Instantly returns the factorial <tt>k!</tt>.
	 *
	 * @param k
	 *            must hold <tt>k &gt;= 0 && k &lt; 21</tt>.
	 */
	static public long longFactorial(int k) throws IllegalArgumentException {
		if (k < 0)
			throw new IllegalArgumentException("Negative k");

		if (k < longFactorials.length)
			return longFactorials[k];
		throw new IllegalArgumentException("Overflow");
	}

	/**
	 * Returns the StirlingCorrection.
	 * <p>
	 * Correction term of the Stirling approximation for <tt>log(k!)</tt>
	 * (series in 1/k, or table values for small k) with int parameter k.
	 * <p>
	 * <tt>
	 * log k! = (k + 1/2)log(k + 1) - (k + 1) + (1/2)log(2Pi) +
	 *          stirlingCorrection(k + 1)
	 * <p>
	 * log k! = (k + 1/2)log(k)     -  k      + (1/2)log(2Pi) +
	 *          stirlingCorrection(k)
	 * </tt>
	 */
	public static double stirlingCorrection(int k) {
		final double C1 = 8.33333333333333333e-02; // +1/12
		final double C3 = -2.77777777777777778e-03; // -1/360
		final double C5 = 7.93650793650793651e-04; // +1/1260
		final double C7 = -5.95238095238095238e-04; // -1/1680

		double r, rr;

		if (k > 30) {
			r = 1.0 / (double) k;
			rr = r * r;
			return r * (C1 + rr * (C3 + rr * (C5 + rr * C7)));
		} else
			return stirlingCorrection[k];
	}

	/**
	 * Equivalent to <tt>Math.round(binomial(n,k))</tt>.
	 */
	private static long xlongBinomial(long n, long k) {
		return Math.round(binomial(n, k));
	}

	/**
	 * Evaluates the given polynomial of degree <tt>N</tt> at <tt>x</tt>,
	 * assuming coefficient of N is 1.0. Otherwise same as <tt>polevl()</tt>.
	 *
	 * <pre>
	 *                     2          N
	 * y  =  C  + C x + C x  +...+ C x
	 *        0    1     2          N
	 *
	 * where C  = 1 and hence is omitted from the array.
	 *        N
	 *
	 * Coefficients are stored in reverse order:
	 *
	 * coef[0] = C  , ..., coef[N-1] = C  .
	 *            N-1                   0
	 *
	 * Calling arguments are otherwise the same as polevl().
	 * </pre>
	 *
	 * In the interest of speed, there are no checks for out of bounds
	 * arithmetic.
	 *
	 * @param x
	 *            argument to the polynomial.
	 * @param coef
	 *            the coefficients of the polynomial.
	 * @param N
	 *            the degree of the polynomial.
	 */
	public static double p1evl(double x, double coef[], int N)
			throws ArithmeticException {
		double ans;

		ans = x + coef[0];

		for (int i = 1; i < N; i++) {
			ans = ans * x + coef[i];
		}

		return ans;
	}

	/**
	 * Evaluates the given polynomial of degree <tt>N</tt> at <tt>x</tt>.
	 *
	 * <pre>
	 *                     2          N
	 * y  =  C  + C x + C x  +...+ C x
	 *        0    1     2          N
	 *
	 * Coefficients are stored in reverse order:
	 *
	 * coef[0] = C  , ..., coef[N] = C  .
	 *            N                   0
	 * </pre>
	 *
	 * In the interest of speed, there are no checks for out of bounds
	 * arithmetic.
	 *
	 * @param x
	 *            argument to the polynomial.
	 * @param coef
	 *            the coefficients of the polynomial.
	 * @param N
	 *            the degree of the polynomial.
	 */
	public static double polevl(double x, double coef[], int N)
			throws ArithmeticException {
		double ans;
		ans = coef[0];

		for (int i = 1; i <= N; i++)
			ans = ans * x + coef[i];

		return ans;
	}

	/*
	 * machine constants
	 */
	public static final double MACHEP = 1.11022302462515654042E-16;
	public static final double MAXLOG = 7.09782712893383996732E2;
	public static final double MINLOG = -7.451332191019412076235E2;
	public static final double MAXGAM = 171.624376956302725;
	public static final double SQTPI = 2.50662827463100050242E0;
	public static final double SQRTH = 7.07106781186547524401E-1;
	public static final double LOGPI = 1.14472988584940017414;

	public static final double big = 4.503599627370496e15;
	public static final double biginv = 2.22044604925031308085e-16;

	/*
	 * MACHEP = 1.38777878078144567553E-17 2**-56 MAXLOG =
	 * 8.8029691931113054295988E1 log(2**127) MINLOG =
	 * -8.872283911167299960540E1 log(2**-128) MAXNUM =
	 * 1.701411834604692317316873e38 2**127
	 *
	 * For IEEE arithmetic (IBMPC): MACHEP = 1.11022302462515654042E-16 2**-53
	 * MAXLOG = 7.09782712893383996843E2 log(2**1024) MINLOG =
	 * -7.08396418532264106224E2 log(2**-1022) MAXNUM = 1.7976931348623158E308
	 * 2**1024
	 *
	 * The global symbols for mathematical constants are PI =
	 * 3.14159265358979323846 pi PIO2 = 1.57079632679489661923 pi/2 PIO4 =
	 * 7.85398163397448309616E-1 pi/4 SQRT2 = 1.41421356237309504880 sqrt(2)
	 * SQRTH = 7.07106781186547524401E-1 sqrt(2)/2 LOG2E =
	 * 1.4426950408889634073599 1/log(2) SQ2OPI = 7.9788456080286535587989E-1
	 * sqrt( 2/pi ) LOGE2 = 6.93147180559945309417E-1 log(2) LOGSQ2 =
	 * 3.46573590279972654709E-1 log(2)/2 THPIO4 = 2.35619449019234492885 3*pi/4
	 * TWOOPI = 6.36619772367581343075535E-1 2/pi
	 */

	/**
	 * 打印
	 *
	 * @param data
	 */
	public static void print(double[] data) {
		System.out.println("size:" + data.length);
		for (int i = 0; i < data.length; i++) {
			System.out.print(data[i] + "\t");
		}
		System.out.println();
	}

	/**
	 * 打印
	 *
	 * @param data
	 */
	public static void print(float[] data) {
		System.out.println("size:" + data.length);
		for (int i = 0; i < data.length; i++) {
			System.out.print(data[i] + "\t");
		}
		System.out.println();
	}

	/**
	 * 纵向打印
	 *
	 * @param data
	 */
	public static void printCol(double[] data) {
		System.out.println("size:" + data.length);
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i]);
		}
	}

	/**
	 * 纵向打印
	 *
	 * @param data
	 */
	public static void printCol(float[] data) {
		System.out.println("size:" + data.length);
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i]);
		}
	}

	/**
	 * 正常打印
	 *
	 * @param data
	 */
	public static void print(double[][] data) {
		System.out.println("size:" + data.length + "  X  " + data[0].length);
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				System.out.print(data[i][j] + "\t");
			}
			System.out.println();
		}
	}

	public static void print(float[][] data) {
		System.out.println("size:" + data.length + "  X  " + data[0].length);
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				System.out.print(data[i][j] + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @param axis
	 *            是否为 按照y
	 * @return
	 */
	public static double[] sum(double[][] data, boolean axis) {
		if (axis) {
			double[] re = new double[data[0].length];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					re[j] += data[i][j];
				}
			}
			return re;
		} else {
			double[] re = new double[data.length];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					re[i] += data[i][j];
				}
			}
			return re;
		}
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @param data2
	 *            是否为 按照y
	 * @return
	 */
	public static double[][] sum(double[][] data, double[][] data2) {
		return times(data, data2);
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @param axis
	 *            是否为 按照y
	 * @return
	 */
	public static float[] sum(float[][] data, boolean axis) {
		if (axis) {
			float[] re = new float[data[0].length];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					re[j] += data[i][j];
				}
			}
			return re;
		} else {
			float[] re = new float[data.length];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					re[i] += data[i][j];
				}
			}
			return re;
		}
	}

	/**
	 * 全部数据之和
	 *
	 * @param data
	 *            是否为 按照y
	 * @return
	 */
	public static double sum(double[][] data) {
		double re = 0d;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				re += data[i][j];
			}
		}
		return re;
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @param axis
	 *            是否为 按照y
	 * @return
	 */
	public static double[][] sum(double[][] data, double[] data2, boolean axis) {
		if (axis) {
			double[][] re = new double[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new double[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] + data2[j];
				}
			}
			return re;
		} else {
			double[][] re = new double[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new double[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] + data2[i];
				}
			}
			return re;
		}
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @param axis
	 *            是否为 按照y
	 * @return
	 */
	public static float[][] sum(float[][] data, float[] data2, boolean axis) {
		if (axis) {
			float[][] re = new float[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new float[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] + data2[j];
				}
			}
			return re;
		} else {
			float[][] re = new float[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new float[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] + data2[i];
				}
			}
			return re;
		}
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @param axis
	 *            是否为 按照y
	 * @return
	 */
	public static double[][] minus(double[][] data, double[] data2, boolean axis) {
		if (axis) {
			double[][] re = new double[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new double[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] - data2[j];
				}
			}
			return re;
		} else {
			double[][] re = new double[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new double[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] - data2[i];
				}
			}
			return re;
		}
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @param axis
	 *            是否为 按照y
	 * @return
	 */
	public static double[][] times(double[][] data, double[] data2, boolean axis) {
		if (axis) {
			double[][] re = new double[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new double[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] * data2[j];
				}
			}
			return re;
		} else {
			double[][] re = new double[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new double[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] * data2[i];
				}
			}
			return re;
		}
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @return
	 */
	public static double[][] times(double[][] data, double[][] data2) {
		double[][] re = new double[data.length][];
		for (int i = 0; i < data.length; i++) {
			re[i] = new double[data[0].length];
			for (int j = 0; j < data[i].length; j++) {
				re[i][j] = data[i][j] * data2[j][i];
			}
		}
		return re;
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @return
	 */
	public static double[][] times(double[][] data, double data2) {
		double[][] re = new double[data.length][];
		for (int i = 0; i < data.length; i++) {
			re[i] = new double[data[0].length];
			for (int j = 0; j < data[i].length; j++) {
				re[i][j] = data[i][j] * data2;
			}
		}
		return re;
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @return
	 */
	public static double[] times(double[] data, double data2) {
		double[] re = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i] * data2;
		}
		return re;
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @param axis
	 *            是否为 按照y
	 * @return
	 */
	public static float[][] times(float[][] data, float[] data2, boolean axis) {
		if (axis) {
			float[][] re = new float[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new float[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] * data2[j];
				}
			}
			return re;
		} else {
			float[][] re = new float[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new float[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] * data2[i];
				}
			}
			return re;
		}
	}

	/**
	 * 按照行or列做求和计算
	 *
	 * @param data
	 * @param axis
	 *            是否为 按照y
	 * @return
	 */
	public static double[][] divide(double[][] data, double[] data2,
			boolean axis) {
		if (axis) {
			double[][] re = new double[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new double[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] / data2[j];
				}
			}
			return re;
		} else {
			double[][] re = new double[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new double[data[0].length];
				for (int j = 0; j < data[i].length; j++) {
					re[i][j] = data[i][j] / data2[i];
				}
			}
			return re;
		}
	}

	/**
	 * 按照行or列 做均值计算
	 *
	 * @param data
	 * @param axis
	 *            是否为 按照y
	 * @return
	 */
	public static double[] avg(double[][] data, boolean axis) {
		if (axis) {
			double[] re = new double[data[0].length];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					re[j] += data[i][j];
				}
			}
			for (int i = 0; i < re.length; i++) {
				re[i] /= data.length;
			}
			return re;
		} else {
			double[] re = new double[data.length];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					re[i] += data[i][j];
				}
			}
			for (int i = 0; i < re.length; i++) {
				re[i] /= data[0].length;
			}
			return re;
		}
	}

	/**
	 * 获取sigmod值
	 *
	 * @param val
	 */
	public static double[] sigmod(double[] val) {
		double[] re = new double[val.length];
		for (int i = 0; i < val.length; i++) {
			re[i] = sigmod(val[i]);
		}
		return re;
	}

	/**
	 * 获取sigmod值
	 *
	 * @param val
	 */
	public static double[][] sigmod(double[][] val) {
		double[][] re = new double[val.length][];
		for (int i = 0; i < val.length; i++) {
			re[i] = new double[val[0].length];
			for (int j = 0; j < val[0].length; j++) {
				re[i][j] = sigmod(val[i][j]);
			}
		}
		return re;
	}

	/**
	 * 重复多次
	 *
	 * @param data
	 * @param length
	 *            重复数量
	 * @param axis
	 *            是否为纵坐标
	 * @return
	 */
	public static double[][] repeat(double[] data, int length, boolean axis) {
		if (!axis) {
			double[][] re = new double[length][];
			for (int i = 0; i < length; i++) {
				re[i] = data.clone();
			}
			return re;
		} else {
			double[][] re = new double[data.length][];
			for (int i = 0; i < data.length; i++) {
				re[i] = new double[length];
				for (int j = 0; j < length; j++) {
					re[i][j] = data[i];
				}
			}
			return re;
		}
	}

	/**
	 * 获取从整数 start开始到end的数
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static double[] range(int start, int end) {
		double[] val = new double[end - start + 1];
		for (int i = 0; i < end - start + 1; i++) {
			val[i] = start + i;
		}
		return val;
	}

	/**
	 * 获取从整数 start开始到end的数
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static float[] rangeFloat(int start, int end) {
		float[] val = new float[end - start + 1];
		for (int i = 0; i < end - start + 1; i++) {
			val[i] = start + i;
		}
		return val;
	}

	/**
	 * 获取从整数 start开始到end的数
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static int[] rangeInt(int start, int end) {
		int[] val = new int[end - start + 1];
		for (int i = 0; i < end - start + 1; i++) {
			val[i] = start + i;
		}
		return val;
	}

	/**
	 * 获取从整数 start开始到end的数
	 *
	 * @param start
	 * @param end
	 * @param size
	 *            循环的数量
	 * @return
	 */
	public static double[] range(int start, int end, int size) {
		double[] val = new double[(end - start + 1) * size];
		for (int s = 0; s < size; s++) {
			for (int i = 0; i < end - start + 1; i++) {
				val[s * (end - start + 1) + i] = start + i;
			}
		}
		return val;
	}

	/**
	 * 获取从整数 start开始到end的数
	 *
	 * @param start
	 * @param end
	 * @param size
	 *            循环的数量
	 * @return
	 */
	public static float[] rangeFloat(int start, int end, int size) {
		float[] val = new float[(end - start + 1) * size];
		for (int s = 0; s < size; s++) {
			for (int i = 0; i < end - start + 1; i++) {
				val[s * (end - start + 1) + i] = start + i;
			}
		}
		return val;
	}

	/**
	 * 获取从整数 start开始到end的数
	 *
	 * @param start
	 * @param end
	 * @param size
	 *            循环的数量
	 * @return
	 */
	public static int[] rangeInt(int start, int end, int size) {
		int[] val = new int[(end - start + 1) * size];
		for (int s = 0; s < size; s++) {
			for (int i = 0; i < end - start + 1; i++) {
				val[s * (end - start + 1) + i] = start + i;
			}
		}
		return val;
	}

	/**
	 * 获取从start到end的数据
	 *
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	public static double[] getData(double[] data, int start, int end) {
		double[] val = new double[end - start + 1];
		for (int i = start; i <= end; i++) {
			val[i - start] = data[i];
		}
		return val;
	}

	/**
	 * 获取从start到end的数据
	 *
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	public static float[] getData(float[] data, int start, int end) {
		float[] val = new float[end - start + 1];
		for (int i = start; i <= end; i++) {
			val[i - start] = data[i];
		}
		return val;
	}

	/**
	 * 获取数据并随机排序
	 *
	 * @param data
	 * @return
	 */
	public static double[] getDataRandom(double[] data) {
		LinkedList<Double> list = new LinkedList<Double>();
		for (double v : data) {
			list.add(v);
		}
		double[] re = new double[data.length];
		int index = -1;
		Random random = new Random();
		while (list.size() > 0) {
			re[++index] = list.remove(Math.abs(random.nextInt() % list.size()));
		}
		return re;
	}

	/**
	 * 获取数据并随机排序
	 *
	 * @param data
	 * @return
	 */
	public static int[] getDataRandomInt(double[] data) {
		LinkedList<Double> list = new LinkedList<Double>();
		for (double v : data) {
			list.add(v);
		}
		int[] re = new int[data.length];
		int index = -1;
		Random random = new Random();
		while (list.size() > 0) {
			re[++index] = (list
					.remove(Math.abs(random.nextInt() % list.size())))
					.intValue();
		}
		return re;
	}

	/**
	 * 获取数据并随机排序
	 *
	 * @param data
	 * @return
	 */
	public static int[] getDataRandomInt(float[] data) {
		LinkedList<Float> list = new LinkedList<Float>();
		for (float v : data) {
			list.add(v);
		}
		int[] re = new int[data.length];
		int index = -1;
		Random random = new Random();
		while (list.size() > 0) {
			re[++index] = (list
					.remove(Math.abs(random.nextInt() % list.size())))
					.intValue();
		}
		return re;
	}

	/**
	 * 按照index的顺序获取data内容
	 *
	 * @param data
	 * @param index
	 * @return
	 */
	public static double[] getData(double[] data, int[] index) {
		double[] re = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[index[i]];
		}
		return re;
	}

	/**
	 * 按照index的顺序获取data内容
	 *
	 * @param data
	 * @param index
	 * @return
	 */
	public static float[] getData(float[] data, int[] index) {
		float[] re = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[index[i]];
		}
		return re;
	}

	/**
	 * 将二维数组拆分成 一维数组
	 *
	 * @param data
	 * @return
	 */
	public static double[] ravel(double[][] data) {
		double[] val = new double[data.length * data[0].length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				val[i * data.length + j] = data[i][j];
			}
		}
		return val;
	}

	/**
	 * 获取逆矩阵
	 *
	 * @param data
	 * @return
	 */
	public static double[][] getInver(double[][] data) {
		Matrix matrix = new Matrix(data);
		Matrix result = matrix.inverse();
		return result.data;
	}

	/**
	 * 求两个矩阵的乘积
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static float[][] times(float[][] data, float[][] data2) {
		Matrix matrix = new Matrix(data);
		double[][] val = matrix.times(new Matrix(data2)).data;
		return doubleToFloat(val);
	}

	public static float[][] doubleToFloat(double[][] val) {
		float[][] re = new float[val.length][];
		for (int i = 0; i < val.length; i++) {
			re[i] = new float[re[0].length];
			for (int j = 0; j < re[0].length; j++) {
				re[i][j] = (float) val[i][j];
			}
		}
		return re;
	}

	public static double[][] floatToDouble(float[][] val) {
		double[][] re = new double[val.length][];
		for (int i = 0; i < val.length; i++) {
			re[i] = new double[re[0].length];
			for (int j = 0; j < re[0].length; j++) {
				re[i][j] = val[i][j];
			}
		}
		return re;
	}

	public static float[] timesOne(float[][] data, float[][] data2) {
		float[] re = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i][0] * data2[i][0];
		}
		return re;
	}

	public static double[] timesOne(double[][] data, double[][] data2) {
		double[] re = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i][0] * data2[i][0];
		}
		return re;
	}

	/**
	 * 的乘积
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static float[] times(float[] data, float[] data2) {
		float[] re = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i] * data2[i];
		}
		return re;
	}

	/**
	 * 的乘积
	 *
	 * @param data
	 * @param val
	 * @return
	 */
	public static float[] times(float[] data, float val) {
		float[] re = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i] * val;
		}
		return re;
	}

	/**
	 * 的乘积
	 *
	 * @param data
	 * @param val
	 * @return
	 */
	public static float[][] times(float[][] data, float val) {
		float[][] re = new float[data.length][];
		for (int i = 0; i < data.length; i++) {
			re[i] = new float[data[0].length];
			for (int j = 0; j < data[0].length; j++) {
				re[i][j] = data[i][j] * val;
			}
		}
		return re;
	}

	/**
	 * 的乘积
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static float[] sum(float[] data, float[] data2) {
		float[] re = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i] + data2[i];
		}
		return re;
	}

	public static float sum(float[] data) {
		float sum = 0f;
		for (float f : data) {
			sum += f;
		}
		return sum;
	}

	/**
	 * 矩阵相和
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static float[][] sum(float[][] data, float[][] data2) {
		Matrix matrix = new Matrix(data);
		return doubleToFloat(matrix.plus(new Matrix(data2)).data);
	}

	/**
	 * 矩阵相和
	 *
	 * @param data
	 * @return
	 */
	public static float sum(float[][] data) {
		float va = 0f;
		for (float[] fv : data) {
			for (float f : fv) {
				va += f;
			}
		}
		return va;
	}

	/**
	 * 矩阵相减
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static float[][] minus(float[][] data, float[][] data2) {
		Matrix matrix = new Matrix(data);
		return doubleToFloat(matrix.minus(new Matrix(data2)).data);
	}

	/**
	 * 矩阵相减
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static double[][] minus(double[][] data, double[][] data2) {
		Matrix matrix = new Matrix(data);
		return matrix.minus(new Matrix(data2)).data;
	}

	/**
	 * 的-
	 *
	 * @param data
	 * @param val
	 * @return
	 */
	public static float[][] minus(float[][] data, float val) {
		float[][] re = new float[data.length][];
		for (int i = 0; i < data.length; i++) {
			re[i] = new float[data[0].length];
			for (int j = 0; j < data[0].length; j++) {
				re[i][j] = data[i][j] - val;
			}
		}
		return re;
	}

	/**
	 * 的-
	 *
	 * @param data
	 * @param val
	 * @return
	 */
	public static double[][] minus(double[][] data, double val) {
		double[][] re = new double[data.length][];
		for (int i = 0; i < data.length; i++) {
			re[i] = new double[data[0].length];
			for (int j = 0; j < data[0].length; j++) {
				re[i][j] = data[i][j] - val;
			}
		}
		return re;
	}

	/**
	 * 矩阵相减
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static float[] minus(float[] data, float[] data2) {
		float[] re = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i] - data2[i];
		}
		return re;
	}

	/**
	 * 矩阵相减
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static double[] minus(double[] data, double[] data2) {
		double[] re = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i] - data2[i];
		}
		return re;
	}

	/**
	 * 矩阵相减
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static float[][] divide(float[][] data, float[][] data2) {
		Matrix matrix = new Matrix(data);
		return doubleToFloat(matrix.divide(new Matrix(data2)).data);
	}

	/**
	 * 的-
	 *
	 * @param data
	 * @param val
	 * @return
	 */
	public static float[][] divide(float[][] data, float val) {
		float[][] re = new float[data.length][];
		for (int i = 0; i < data.length; i++) {
			re[i] = new float[data[0].length];
			for (int j = 0; j < data[0].length; j++) {
				re[i][j] = data[i][j] / val;
			}
		}
		return re;
	}

	/**
	 * 矩阵相减
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static float[] divide(float[] data, float[] data2) {
		float[] re = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i] / data2[i];
		}
		return re;
	}

	/**
	 * 矩阵相减
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static float[] divide(float[] data, float data2) {
		float[] re = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i] / data2;
		}
		return re;
	}

	/**
	 * 的-
	 *
	 * @param data
	 * @param val
	 * @return
	 */
	public static double[][] divide(double[][] data, double val) {
		double[][] re = new double[data.length][];
		for (int i = 0; i < data.length; i++) {
			re[i] = new double[data[0].length];
			for (int j = 0; j < data[0].length; j++) {
				re[i][j] = data[i][j] / val;
			}
		}
		return re;
	}

	/**
	 * 矩阵相减
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static double[] divide(double[] data, double[] data2) {
		double[] re = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i] / data2[i];
		}
		return re;
	}

	/**
	 * 矩阵相减
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static double[] divide(double[] data, double data2) {
		double[] re = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i] / data2;
		}
		return re;
	}

	/**
	 * 矩阵相加
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static float[][] plus(float[][] data, float[][] data2) {
		Matrix matrix = new Matrix(data);
		return doubleToFloat(matrix.plus(new Matrix(data2)).data);
	}

	/**
	 * 矩阵相加
	 *
	 * @param data
	 * @param data2
	 * @return
	 */
	public static double[][] plus(double[][] data, double[][] data2) {
		Matrix matrix = new Matrix(data);
		return matrix.plus(new Matrix(data2)).data;
	}

	/**
	 * 矩阵相加
	 *
	 * @param data
	 * @param data2
	 * @param axis
	 *            按照列相加行相加
	 * @return
	 */
	public static float[][] plus(float[][] data, float[] data2, boolean axis) {
		float[][] re = new float[data.length][];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i].clone();
		}
		if (axis) {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data.length; j++) {
					data[i][j] += data2[j];
				}
			}
		} else {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data.length; j++) {
					data[i][j] += data2[i];
				}
			}
		}
		return re;
	}

	/**
	 * 矩阵相加
	 *
	 * @param data
	 * @param data2
	 * @param axis
	 *            按照列相加行相加
	 * @return
	 */
	public static double[][] plus(double[][] data, double[] data2, boolean axis) {
		double[][] re = new double[data.length][];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i].clone();
		}
		if (axis) {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data.length; j++) {
					data[i][j] += data2[j];
				}
			}
		} else {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data.length; j++) {
					data[i][j] += data2[i];
				}
			}
		}
		return re;
	}

	/**
	 * 转置
	 *
	 * @param data
	 * @return
	 */
	public static float[][] transport(float[][] data) {
		Matrix matrix = new Matrix(data);
		return doubleToFloat(matrix.transpose().data);
	}

	/**
	 * 转置
	 *
	 * @param data
	 * @return
	 */
	public static double[][] transport(double[][] data) {
		Matrix matrix = new Matrix(data);
		return matrix.transpose().data;
	}

	/**
	 * 均值
	 *
	 * @param data
	 *            数据源
	 * @return
	 */
	public static double mean(double[] data) {
		double sum = sum(data);
		return sum / data.length;
	}

	/**
	 * 差的平方和
	 *
	 * @param data
	 *            基础数据
	 * @param mean
	 *            值
	 * @return
	 */
	public static double sqrtMinus(double[] data, double mean) {
		double sum = 0d;
		for (int i = 0; i < data.length; i++) {
			double val = (data[i] - mean);
			sum += val * val;
		}
		return sum;
	}

	/**
	 * 差的平方和
	 *
	 * @param data
	 *            基础数据
	 * @param data2
	 *            值
	 * @return
	 */
	public static double sqrtMinus(double[] data, double[] data2) {
		double sum = 0d;
		for (int i = 0; i < data.length; i++) {
			double val = (data[i] - data2[i]);
			sum += val * val;
		}
		return sum;
	}

	/**
	 * 计算求和
	 *
	 * @param data
	 * @return
	 * @return
	 * @return
	 */
	public static double sum(double[] data) {
		double value = 0.0;
		for (double da : data) {
			value += da;
		}
		return value;
	}

	/**
	 * 计算求和
	 *
	 * @param data
	 * @return
	 * @return
	 * @return
	 */
	public static double[] sum(double[] data, double[] data2) {
		double[] re = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			re[i] = data[i] + data2[i];
		}
		return re;
	}

	/**
	 * 计算方差
	 *
	 * @param data
	 * @return
	 */
	public static double var(double[] data) {
		double mean = mean(data);
		double var = 0.0;
		for (double da : data) {
			var += Math.pow((da - mean), 2.0);
		}
		return var / data.length;
	}

	/**
	 * 获取协方差
	 *
	 * @param data
	 * @return
	 */
	public static double[][] cov(double[][] data) {
		int size = data[0].length;
		double[][] result = new double[size][];
		for (int i = 0; i < size; i++) {
			result[i] = new double[size];
		}
		// 按照列做协方差
		double[] val = new double[data.length];
		double[] means = new double[size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < data.length; j++) {
				val[j] = data[j][i];
			}
			means[i] = mean(val);
		}
		for (int i = 0; i < size; i++) {
			for (int j = i; j < size; j++) {
				double cov = 0;
				double temp = 0;
				if (i == j) {
					for (int m = 0; m < data.length; m++) {
						temp = (data[m][i] - means[i]);
						cov += temp * temp;
					}
				} else {
					for (int m = 0; m < data.length; m++) {
						cov += (data[m][i] - means[i])
								* (data[m][j] - means[j]);
					}
				}
				cov = cov / (data.length - 1);
				result[i][j] = cov;
				result[j][i] = cov;
			}
		}
		return result;
	}

	/**
	 * 计算绝对差 sum(|x-mean|)/n
	 *
	 * @param data
	 * @return
	 */
	public static double VarAbs(double[] data) {
		double mean = mean(data);
		double var = 0.0;
		for (double da : data) {
			var += Math.abs((da - mean));
		}
		return var / data.length;
	}

	/**
	 * 计算标准差
	 *
	 * @param data
	 * @return
	 */
	public static double std(double[] data) {
		double var = var(data);

		return Math.sqrt(var);
	}

	/**
	 * 获取n日均值
	 *
	 * @param data
	 *            标记为获取几日均值**x**计算 返回长度和输入长度一样
	 * @param dataCount
	 *            标记为单侧***
	 * @param direction
	 *            =0 表示 ***x***
	 *            =-1表示****x
	 *            =1 表示x*****
	 * @return
	 */
	public static double[] MeanN(double[] data, int dataCount, int direction) {
		double[] temp = data.clone();
		if (direction == 0) {
			// 数据的总长度
			int dataLength = data.length;
			// 长度
			int dataCount2 = dataCount * 2 + 1;
			// 有效的端点
			int dataCount3 = data.length - 1 - dataCount;
			for (int i = 0; i < data.length; i++) {
				double value = 0.0;
				if (i > dataCount - 1 && (i < dataCount3)) {
					for (int j = -dataCount; j <= dataCount; j++) {
						value += data[i + j];
					}
					temp[i] = value / dataCount2;
				} else {
					int count = 0;
					for (int j = -dataCount; j <= dataCount; j++) {
						if (i + j < 0 || i + j >= dataLength) {

						} else {
							count++;
							value += data[i + j];
						}
					}
					temp[i] = value / count;
				}
			}
		} else if (direction == -1) {
			int dataLength = data.length;
			// 将最左侧不足dataCount的单独处理
			for (int i = 0; i < dataCount; i++) {
				double value = 0.0;
				int countC = 0;
				for (int j = 0; j < i + 1; j++) {
					value += data[j];
					countC++;
				}

				temp[i] = value / countC;
			}
			// 标准处理
			for (int i = dataCount; i < data.length; i++) {
				double value = 0.0;
				for (int j = -dataCount + 1; j <= 0; j++) {
					value += data[i + j];
				}
				temp[i] = value / dataCount;
			}
		} else if (direction == 1) {
			int dataLength = data.length;
			// 将最左侧不足dataCount的单独处理
			for (int i = dataLength - dataCount - 1; i < dataLength; i++) {
				double value = 0.0;
				int countC = 0;
				for (int j = i; j <= dataCount; j++) {
					value += data[j];
					countC++;
				}

				temp[i] = value / countC;
			}
			// 标准处理
			for (int i = 0; i < data.length - dataCount - 1; i++) {
				double value = 0.0;
				for (int j = 0; j <= dataCount; j++) {
					value += data[i + j];
				}
				temp[i] = value / dataCount;
			}
		}
		return temp;
	}

	/**
	 * 梯度计算
	 *
	 * @param value
	 * @param flag
	 *            为true时为当前值间上一个值为当前值 否则为 赋值到上一个值
	 * @return
	 */
	public static double[] gradient(double[] value, boolean flag) {
		double[] det = new double[value.length];
		int length = value.length;
		if (length <= 1) {
			return det;
		}
		if (flag == true) {
			for (int i = 1; i < length; i++) {
				det[i] = value[i] - value[i - 1];
			}
			det[0] = det[1];
		} else {
			for (int i = 0; i < length; i++) {
				det[i] = value[i + 1] - value[i];
			}
			det[length - 1] = det[length - 2];
		}
		return det;
	}

	/**
	 * 分布切分 0平稳，-1下降,1上升,最高点
	 */
	public static void split() {

	}

	/**
	 * 获取数组中最大点的Index
	 *
	 * @return
	 */
	public static int getMaxIndex(int[] input) {
		int max = Integer.MIN_VALUE;
		int index = -1;
		for (int i = 0; i < input.length; i++) {
			if (input[i] > max) {
				max = input[i];
				index = i;
			}
		}
		return index;
	}

	/**
	 * 获取某一列的数据
	 *
	 * @param input
	 * @param index
	 * @return
	 */
	public static int[] getCol(int[][] input, int index) {
		// System.out.println("index:" + index);
		if (input.length == 0) {
			return null;
		}
		int[] ll = new int[input.length];
		for (int i = 0; i < input.length; i++) {
			ll[i] = input[i][index];
		}
		return ll;
	}

	/**
	 * 获取某一列的数据
	 *
	 * @param input
	 * @param index
	 * @return
	 */
	public static float[] getCol(float[][] input, int index) {
		// System.out.println("index:" + index);
		if (input.length == 0) {
			return null;
		}
		float[] ll = new float[input.length];
		for (int i = 0; i < input.length; i++) {
			ll[i] = input[i][index];
		}
		return ll;
	}

	/**
	 * 获取多行
	 *
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	public static double[][] getRows(double[][] data, int start, int end) {
		double[][] re = new double[end - start + 1][];
		for (int i = start; i <= end; i++) {
			re[i - start] = data[i].clone();
		}
		return re;
	}

	/**
	 * 获取某一列的数据
	 *
	 * @param input
	 * @param index
	 * @return
	 */
	public static double[] getCol(double[][] input, int index) {
		// System.out.println("index:" + index);
		if (input.length == 0) {
			return null;
		}
		double[] ll = new double[input.length];
		for (int i = 0; i < input.length; i++) {
			ll[i] = input[i][index];
		}
		return ll;
	}

	/**
	 * 重新调整为标准格式
	 *
	 * @param x
	 * @param y
	 * @param axis
	 *            是否按照y排序
	 * @return
	 */
	public static double[][] reshape(double[] data, int x, int y, boolean axis) {
		double[][] re = new double[x][];
		for (int i = 0; i < x; i++) {
			re[i] = new double[y];
		}
		if (axis) {
			for (int i = 0; i < data.length; i++) {
				re[i / x][i % y] = data[i];
			}
		} else {
			for (int i = 0; i < data.length; i++) {
				re[i % x][i / y] = data[i];
			}
		}
		return re;
	}

	/**
	 * 重新调整为标准格式
	 *
	 * @param x
	 * @param y
	 * @param axis
	 *            是否按照y排序
	 * @return
	 */
	public static float[][] reshape(float[] data, int x, int y, boolean axis) {
		float[][] re = new float[x][];
		for (int i = 0; i < x; i++) {
			re[i] = new float[y];
		}
		if (axis) {
			for (int i = 0; i < data.length; i++) {
				re[i / x][i % y] = data[i];
			}
		} else {
			for (int i = 0; i < data.length; i++) {
				re[i % x][i / y] = data[i];
			}
		}
		return re;
	}

	/**
	 * 删除某一列的数据
	 *
	 * @param input
	 * @param index
	 * @return
	 */
	public static int[][] moveCol(int[][] input, int index) {
		if (input.length == 0) {
			return null;
		} else if (input[0].length == 0) {
			return null;
		}
		int[][] move = new int[input.length][input[0].length - 1];
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length; j++) {
				if (j < index) {
					move[i][j] = input[i][j];
				} else if (j > index) {
					move[i][j - 1] = input[i][j];
				}
			}
		}
		return move;
	}

	/**
	 * 删除某一列的数据
	 *
	 * @param input
	 * @param index
	 * @return
	 */
	public static String[][] moveCol(String[][] input, int index) {
		if (input.length == 0) {
			return null;
		} else if (input[0].length == 0) {
			return null;
		}
		String[][] move = new String[input.length][input[0].length - 1];
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length; j++) {
				if (j < index) {
					move[i][j] = input[i][j];
				} else if (j > index) {
					move[i][j - 1] = input[i][j];
				}
			}
		}
		return move;
	}

	/**
	 * 获取中值的值
	 *
	 * @return
	 */
	public static double getMedian(double[] data) {
		// 6, t.length - 1, true
		AllSort.quickSort(data, data.length / 2, data.length - 1, true);
		return data[data.length / 2];
	}

	/**
	 * 按照 classify 分组 成多个 list inputData
	 *
	 * @param inputDataStringTemp
	 *            分组的输入名
	 * @param inputData
	 *            其他输入列
	 * @param classify
	 *            最大gain列
	 * @param outputDataStringTemp
	 *            分组后的 输出名
	 * @param outputDataTemp
	 *            为分组后的输出值
	 *            gain列对应的map值
	 * @return map 对应分组的int值
	 */
	public static ArrayList<int[][]> getGroupSon(
			ArrayList<String[][]> inputDataStringTemp, int[][] inputData,
			String[][] inputDataString, int[] classify, int[] outputData,
			String[] outputDataString,
			ArrayList<String[]> outputDataStringTemp,
			ArrayList<int[]> outputDataTemp, HashMap<Integer, Integer> map) {
		int index = 0;
		if (inputData.length == 0) {
			return new ArrayList<int[][]>();
		}
		int len = inputData[0].length;
		for (int lp : classify) {
			if (map.containsKey(lp)) {
			} else {
				map.put(lp, index);
				index++;
			}
		}
		ArrayList<ArrayList<int[]>> result = new ArrayList<ArrayList<int[]>>();
		ArrayList<ArrayList<String[]>> resultString = new ArrayList<ArrayList<String[]>>();
		ArrayList<ArrayList<Integer>> rez = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<String>> rezString = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < map.size(); i++) {
			result.add(new ArrayList<int[]>());
			resultString.add(new ArrayList<String[]>());
			rez.add(new ArrayList<Integer>());
			rezString.add(new ArrayList<String>());
		}
		for (int i = 0; i < inputData.length; i++) {
			// 输入的单个组
			result.get(map.get(classify[i])).add(inputData[i].clone());
			resultString.get(map.get(classify[i])).add(
					inputDataString[i].clone());
			// 输出的单个组
			rez.get(map.get(classify[i])).add(outputData[i]);
			rezString.get(map.get(classify[i])).add(outputDataString[i]);
		}
		ArrayList<int[][]> result2 = new ArrayList<int[][]>();
		int zqp = 0;
		for (ArrayList<int[]> ln : result) {
			int[][] temp = new int[ln.size()][len];
			String[][] tempString = new String[ln.size()][len];
			int[] outputte = new int[ln.size()];
			String[] outputteString = new String[ln.size()];
			int index2 = 0;
			for (int[] ln2 : ln) {
				temp[index2] = ln2.clone();
				tempString[index2] = resultString.get(zqp).get(index2).clone();
				outputte[index2] = rez.get(zqp).get(index2);
				outputteString[index2] = rezString.get(zqp).get(index2);
				index2++;
			}
			zqp++;
			result2.add(temp);
			inputDataStringTemp.add(tempString);
			outputDataTemp.add(outputte);
			outputDataStringTemp.add(outputteString);
		}

		return result2;
	}

	/**
	 * 获取数组中最大点的Index
	 *
	 * @return
	 */
	public static int getMaxIndex(double[] input) {
		double max = Double.MIN_VALUE;
		int index = -1;
		for (int i = 0; i < input.length; i++) {
			// System.out.println("gainval:" + input[i]);
			if (input[i] > max) {
				max = input[i];
				index = i;
			}
		}
		if (index == -1) {
			return 0;
		}
		return index;
	}

	/**
	 * 获取数组中最大点的Index
	 *
	 * @return
	 */
	public static int getMaxIndex(long[] input) {
		long max = Long.MIN_VALUE;
		int index = -1;
		for (int i = 0; i < input.length; i++) {
			if (input[i] > max) {
				max = input[i];
				index = i;
			}
		}
		return index;
	}

	/**
	 * 获取众数
	 */
	public static int getMode(int[] data) {
		int len = data.length / 2;
		ArrayList<Integer> key = new ArrayList<Integer>();
		ArrayList<Integer> value = new ArrayList<Integer>();
		boolean flag = false;
		for (int da : data) {
			flag = false;
			for (int i = 0; i < key.size(); i++) {
				if (key.get(i) == da) {
					value.set(i, value.get(i) + 1);
					flag = true;
					break;
				} else {
					continue;
				}
			}
			if (flag) {
				continue;
			} else {
				key.add(da);
				value.add(1);
			}
		}
		int max = 0;
		int index = -1;
		for (int i = 0; i < value.size(); i++) {
			if (value.get(i) > max) {
				index = key.get(i);
				max = value.get(i);
			}
		}
		return index;
	}

	/**
	 * 返回的table表 nameRow 为输入的第一个table(x,y)中x
	 *
	 * @author Administrator
	 *
	 */
	public static class MathTable {
		/**
		 * 名
		 */
		public String[] nameRow = null;
		public String[] nameCol = null;
		/**
		 * 对应值
		 */
		public int[][] num = null;

		MathTable(String[] nameRow, String[] nameCol, int[][] num) {
			this.nameRow = nameRow;
			this.nameCol = nameCol;
			this.num = new int[num.length][];
			for (int i = 0; i < num.length; i++) {
				this.num[i] = num[i].clone();
			}
		}

		/**
		 * 第几行取最大位置
		 *
		 * @return
		 */
		public TableCell getMaxRow(int index) {
			int max = -1;
			int indexL = -1;
			for (int i = 0; i < num[index].length; i++) {
				if (max < num[index][i]) {
					max = num[index][i];
					indexL = i;
				}
			}
			TableCell tableCell = new TableCell(nameRow[index], index,
					nameCol[indexL], indexL, max);
			return tableCell;
		}

		/**
		 * 第几列取最大位置
		 *
		 * @return
		 */
		public TableCell getMaxCol(int index) {
			int max = -1;
			int indexL = -1;
			for (int i = 0; i < num.length; i++) {
				if (max < num[i][index]) {
					max = num[i][index];
					indexL = i;
				}
			}
			TableCell tableCell = new TableCell(nameRow[indexL], indexL,
					nameCol[index], index, max);
			return tableCell;
		}

		/**
		 * 获取某行的总数
		 *
		 * @param index
		 * @return
		 */
		public int getSumRow(int index) {
			int sum = 0;
			for (int in : num[index]) {
				sum += in;
			}
			return sum;
		}

		/**
		 * 获取某列的总数
		 *
		 * @param index
		 * @return
		 */
		public int getSumCol(int index) {
			int sum = 0;
			for (int i = 0; i < num.length; i++) {
				sum += num[i][index];
			}
			return sum;
		}
	}

	public static class TableCell {
		public String nameRow;
		public String nameCol;
		public int indexRow;
		public int indexCol;
		public int num;

		TableCell(String nameRow, int indexRow, String nameCol, int indexCol,
				int num) {
			this.nameRow = nameRow;
			this.nameCol = nameCol;
			this.indexRow = indexRow;
			this.indexCol = indexCol;
			this.num = num;
		}
	}

	/**
	 * @return 返回table if y为空 则默认y为[1,1,1,1..]与x同长度
	 */
	public static MathTable getTable(int[] x, int[] y) {
		if (y == null) {
			y = new int[x.length];
			for (int i = 0; i < x.length; i++) {
				y[i] = 1;
			}
		} else if (x.length != y.length) {
			System.out.println("x,y长度不同");
			return null;
		}
		// 计算table表
		HashMap<Integer, Integer> setX = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> setY = new HashMap<Integer, Integer>();
		List<Integer> tempX = new LinkedList<Integer>();
		List<Integer> tempY = new LinkedList<Integer>();
		int xIndex = 0;
		int yIndex = 0;
		for (int i = 0; i < x.length; i++) {
			if (setX.containsKey(x[i])) {
				// setX.put(x[i],setX.get(x[i])+1);
			} else {
				setX.put(x[i], xIndex);
				tempX.add(x[i]);
				xIndex++;
			}
			if (setY.containsKey(y[i])) {
				// setY.put(x[i],setY.get(x[i])+1);
			} else {
				setY.put(y[i], yIndex);
				tempY.add(y[i]);
				yIndex++;
			}
		}
		// 统计到 二维数组中
		int[][] tab = new int[setX.size()][setY.size()];
		String[] nameRow = new String[setX.size()];
		String[] nameCol = new String[setY.size()];
		int lnum = 0;
		for (Integer st : tempX) {
			nameRow[lnum] = Integer.toString(st);
		}
		lnum = 0;
		for (Integer st : tempY) {
			nameCol[lnum] = Integer.toString(st);
		}
		for (int i = 0; i < x.length; i++) {
			tab[setX.get(x[i])][setY.get(y[i])]++;
		}
		MathTable table = new MathTable(nameRow, nameCol, tab);
		return table;
	}

	/*************************************************
	 * COEFFICIENTS FOR METHOD normalInverse() *
	 *************************************************/
	/* approximation for 0 <= |y - 0.5| <= 3/8 */
	protected static final double P0[] = { -5.99633501014107895267E1,
			9.80010754185999661536E1, -5.66762857469070293439E1,
			1.39312609387279679503E1, -1.23916583867381258016E0, };
	protected static final double Q0[] = {
	/* 1.00000000000000000000E0, */
	1.95448858338141759834E0, 4.67627912898881538453E0,
			8.63602421390890590575E1, -2.25462687854119370527E2,
			2.00260212380060660359E2, -8.20372256168333339912E1,
			1.59056225126211695515E1, -1.18331621121330003142E0, };

	/*
	 * Approximation for interval z = sqrt(-2 log y ) between 2 and 8 i.e., y
	 * between exp(-2) = .135 and exp(-32) = 1.27e-14.
	 */
	protected static final double P1[] = { 4.05544892305962419923E0,
			3.15251094599893866154E1, 5.71628192246421288162E1,
			4.40805073893200834700E1, 1.46849561928858024014E1,
			2.18663306850790267539E0, -1.40256079171354495875E-1,
			-3.50424626827848203418E-2, -8.57456785154685413611E-4, };
	protected static final double Q1[] = {
	/* 1.00000000000000000000E0, */
	1.57799883256466749731E1, 4.53907635128879210584E1,
			4.13172038254672030440E1, 1.50425385692907503408E1,
			2.50464946208309415979E0, -1.42182922854787788574E-1,
			-3.80806407691578277194E-2, -9.33259480895457427372E-4, };

	/*
	 * Approximation for interval z = sqrt(-2 log y ) between 8 and 64 i.e., y
	 * between exp(-32) = 1.27e-14 and exp(-2048) = 3.67e-890.
	 */
	protected static final double P2[] = { 3.23774891776946035970E0,
			6.91522889068984211695E0, 3.93881025292474443415E0,
			1.33303460815807542389E0, 2.01485389549179081538E-1,
			1.23716634817820021358E-2, 3.01581553508235416007E-4,
			2.65806974686737550832E-6, 6.23974539184983293730E-9, };
	protected static final double Q2[] = {
	/* 1.00000000000000000000E0, */
	6.02427039364742014255E0, 3.67983563856160859403E0,
			1.37702099489081330271E0, 2.16236993594496635890E-1,
			1.34204006088543189037E-2, 3.28014464682127739104E-4,
			2.89247864745380683936E-6, 6.79019408009981274425E-9, };

	/**
	 * Returns the value, <tt>x</tt>, for which the area under the Normal
	 * (Gaussian) probability density function (integrated from minus infinity
	 * to <tt>x</tt>) is equal to the argument <tt>y</tt> (assumes mean is zero,
	 * variance is one).
	 * <p>
	 * For small arguments <tt>0 < y < exp(-2)</tt>, the program computes
	 * <tt>z = sqrt( -2.0 * log(y) )</tt>; then the approximation is
	 * <tt>x = z - log(z)/z  - (1/z) P(1/z) / Q(1/z)</tt>. There are two
	 * rational functions P/Q, one for <tt>0 < y < exp(-32)</tt> and the other
	 * for <tt>y</tt> up to <tt>exp(-2)</tt>. For larger arguments,
	 * <tt>w = y - 0.5</tt>, and
	 * <tt>x/sqrt(2pi) = w + w**3 R(w**2)/S(w**2))</tt>.
	 *
	 * @param y0
	 *            the area under the normal pdf
	 * @return the z-value
	 */
	public static double normalInverse(double y0) {

		double x, y, z, y2, x0, x1;
		int code;

		final double s2pi = Math.sqrt(2.0 * Math.PI);

		if (y0 <= 0.0) {
			throw new IllegalArgumentException();
		}
		if (y0 >= 1.0) {
			throw new IllegalArgumentException();
		}
		code = 1;
		y = y0;
		if (y > (1.0 - 0.13533528323661269189)) { /* 0.135... = exp(-2) */
			y = 1.0 - y;
			code = 0;
		}

		if (y > 0.13533528323661269189) {
			y = y - 0.5;
			y2 = y * y;
			x = y + y * (y2 * polevl(y2, P0, 4) / p1evl(y2, Q0, 8));
			x = x * s2pi;
			return (x);
		}

		x = Math.sqrt(-2.0 * Math.log(y));
		x0 = x - Math.log(x) / x;

		z = 1.0 / x;
		if (x < 8.0) {
			x1 = z * polevl(z, P1, 8) / p1evl(z, Q1, 8);
		} else {
			x1 = z * polevl(z, P2, 8) / p1evl(z, Q2, 8);
		}
		x = x0 - x1;
		if (code != 0) {
			x = -x;
		}
		return (x);
	}

	/**
	 * 数据标准化
	 *
	 * @param inputData
	 *            输入数据
	 * @param min
	 *            期望标准化后的最小值
	 * @param max
	 *            期望标准互后的最大值
	 * @return 返回标准化后的矩阵
	 * @param strName
	 *            如果为 range 则为极值标准化 (x-min)/(max-min+1E-10)
	 * @param minMaxValue
	 *            为返回的列对应的[0]最小，[1]最大 [2] 最小索引 [3]最大索引
	 */
	public static double[][] normalizationRange(double[][] inputData,
			double min, double max, String strName, double[][] minMaxValue) {
		if (inputData == null || inputData.length == 0) {
			System.out.println("请确认需要标准化的数据是否正确");
			System.exit(1);
			;
		}
		double[][] retu = new double[inputData.length][inputData[0].length];
		// 获取最小值及最大值
		for (int i = 0; i < inputData[0].length; i++) {
			double mi = Double.MAX_VALUE;
			double ma = -Double.MAX_VALUE;
			int miIndex = -1;
			int maIndex = -1;
			for (int j = 0; j < inputData.length; j++) {
				if (inputData[j][i] > ma) {
					ma = inputData[j][i];
					maIndex = j;
				}
				if (inputData[j][i] < mi) {
					mi = inputData[j][i];
					miIndex = j;
				}
			}
			minMaxValue[0][i] = mi;
			minMaxValue[1][i] = ma;
			minMaxValue[2][i] = miIndex;
			minMaxValue[3][i] = maIndex;
		}
		// 极值标准化
		if (strName.equals("range")) {
			for (int i = 0; i < inputData[0].length; i++) {
				double range = minMaxValue[1][i] - minMaxValue[0][i] + 1E-10;
				for (int j = 0; j < inputData.length; j++) {
					retu[j][i] = (inputData[j][i] - minMaxValue[0][i]) / range;
				}
			}
		}
		return retu;
	}

	/**
	 * 计算每一列的方差 输入矩阵
	 *
	 * @return 返回为 每一列的 方差值
	 */
	public static double[] colStd(double[][] inputData, double[] mean) {
		double[] std = new double[inputData[0].length];

		// 计算 方差
		for (int i = 0; i < inputData[0].length; i++) {
			for (int j = 0; j < inputData.length; j++) {
				std[i] += Math.pow(inputData[j][i] - mean[i], 2D);
			}
		}
		return std;
	}

	/**
	 * 获取矩阵每列的相关系数矩阵
	 *
	 * @param inputData
	 * @param colStd
	 *            列对一个的方差
	 * @param colMean
	 *            列对应的均值
	 * @return
	 */
	public static double[][] correlationMatrix(double[][] inputData,
			double[] colStd, double[] colMean) {
		int len = inputData[0].length;
		double[][] corr = new double[len][len];
		for (int i = 0; i < len; i++) {
			for (int j = i; j < len; j++) {
				if (i == j) {
					corr[i][j] = 1D;
					continue;
				}
				// (x-meanx).*(y-meany)/sqrt(x.*x)*sqrt(y.*y)
				for (int l = 0; l < inputData.length; l++) {
					corr[i][j] += (inputData[l][i] - colMean[i])
							* (inputData[l][j] - colMean[j]);
				}
				corr[i][j] /= Math.sqrt(colStd[i]) * Math.sqrt(colStd[j]);
				corr[j][i] = corr[i][j];
			}
		}
		return corr;
	}

	/**
	 * 求数据的二范数
	 *
	 * @param data
	 * @return
	 */
	public static float norm(float[] data) {
		return MathBase.sqrt(sum(times(data, data)));
	}

	/**
	 * 求数据的二范数
	 *
	 * @param data
	 * @return
	 */
	public static float norm(float[][] data) {
		return MathBase.sqrt(sum(times(data, data)));
	}

	public class Temp implements Comparable<Temp> {
		double value;
		int index;

		public Temp(double value, int index) {
			this.value = value;
			this.index = index;
		}

		@Override
		public int compareTo(Temp o) {
			// TODO Auto-generated method stub
			return Double.compare(o.value, this.value);
		}
	}

	/**
	 * 获取中位数对应的位置
	 *
	 * @param data
	 * @return
	 */
	public static int getMedianIndex(float[] data) {
		ArrayList<Temp> list = new ArrayList<Temp>();
		MathBase t = new MathBase();
		for (int i = 0; i < data.length; i++) {
			list.add(t.new Temp(data[i], i));
		}
		Collections.sort(list);
		return list.get(list.size() / 2).index;
	}

	/**
	 * 获取中位数对应的位置
	 *
	 * @param data
	 * @return
	 */
	public static int getMedianIndex(double[] data) {
		ArrayList<Temp> list = new ArrayList<Temp>();
		MathBase t = new MathBase();
		for (int i = 0; i < data.length; i++) {
			list.add(t.new Temp(data[i], i));
		}
		Collections.sort(list);
		return list.get(list.size() / 2).index;
	}

	/**
	 * 获取中位数对应的位置
	 *
	 * @param data
	 * @param notIndex 不算入计算的数据
	 * @return
	 */
	public static int getMedianIndex(double[] data,int notIndex) {
		ArrayList<Temp> list = new ArrayList<Temp>();
		MathBase t = new MathBase();
		for (int i = 0; i < data.length; i++) {
			if(i==notIndex){
				continue;
			}
			list.add(t.new Temp(data[i], i));
		}
		Collections.sort(list);
		return list.get(list.size() / 2).index;
	}

	/**
	 * 数据拆分
	 *
	 * @param data
	 * @param useIndex
	 *            所使用的 行或者列
	 * @param isRow
	 *            是否为行
	 * @return
	 */
	public static double[][] getSplitData(double[][] data, int[] useIndex,
			boolean isRow) {
		if (isRow) {
			double[][] re = new double[useIndex.length][];
			for (int i = 0; i < useIndex.length; i++) {
				re[i] = data[useIndex[i]].clone();
			}
			return re;
		} else {
			double[][] re = new double[data.length][];
			for (int i = 0; i < data.length; i++) {
				double[] temp = new double[useIndex.length];
				for (int j = 0; j < useIndex.length; j++) {
					temp[j] = data[i][useIndex[j]];
				}
				re[i] = temp;
			}
			return re;
		}
	}

	/**
	 * 数据拆分
	 *
	 * @param data
	 * @param useIndex
	 *            所使用的 行或者列
	 * @param isRow
	 *            是否为行
	 * @return
	 */
	public static double[][] getSplitData(double[][] data,
			List<Integer> useIndex, boolean isRow) {
		if (isRow) {
			double[][] re = new double[useIndex.size()][];
			for (int i = 0; i < useIndex.size(); i++) {
				re[i] = data[useIndex.get(i)].clone();
			}
			return re;
		} else {
			double[][] re = new double[data.length][];
			for (int i = 0; i < data.length; i++) {
				double[] temp = new double[useIndex.size()];
				for (int j = 0; j < useIndex.size(); j++) {
					temp[j] = data[i][useIndex.get(j)];
				}
				re[i] = temp;
			}
			return re;
		}
	}

	public static double[] random(int count) {
		double[] temp = new double[count];
		for (int i = 0; i < count; i++) {
			temp[i] = Math.random();
		}
		return temp;
	}

	/**
	 * Returns a random integer in [0, n). This method is properly synchronized
	 * to allow correct use by more than one thread. However, if many threads
	 * need to generate pseudorandom numbers at a great rate, it may reduce
	 * contention for each thread to have its own pseudorandom-number generator.
	 */
	public static synchronized int randomInt(int n) {
		return random.nextInt(n);
	}

	public static double random(int x, int y) {
		return Math.random() * (y - x) + x;
	}

	/**
	 * log of n choose k
	 */
	public static double logChoose(int n, int k) {
		if (n < 0 || k < 0 || k > n) {
			throw new IllegalArgumentException(String.format(
					"Invalid n = %d, k = %d", n, k));
		}

		return logFactorial(n) - logFactorial(k) - logFactorial(n - k);
	}

	/**
	 * 获取随机数
	 *
	 * @param x
	 * @param y
	 * @param rate
	 *            0-1值*比率
	 * @return
	 */
	public static double[][] random(int x, int y, double rate) {
		double[][] val = new double[x][];
		if (rate == 1f) {
			for (int i = 0; i < x; i++) {
				double[] row = new double[y];
				for (int j = 0; j < y; j++) {
					row[j] = Math.random();
				}
				val[i] = row;
			}
		} else {
			for (int i = 0; i < x; i++) {
				double[] row = new double[y];
				for (int j = 0; j < y; j++) {
					row[j] = Math.random() * rate;
				}
				val[i] = row;
			}
		}
		return val;
	}

	/**
	 * 获取随机数
	 *
	 * @param x
	 * @param y
	 * @param rate
	 *            0-1值*比率
	 * @return
	 */
	public static float[][] random(int x, int y, float rate) {
		float[][] val = new float[x][];
		if (rate == 1f) {
			for (int i = 0; i < x; i++) {
				float[] row = new float[y];
				for (int j = 0; j < y; j++) {
					row[j] = (float) Math.random();
				}
				val[i] = row;
			}
		} else {
			for (int i = 0; i < x; i++) {
				float[] row = new float[y];
				for (int j = 0; j < y; j++) {
					row[j] = (float) Math.random() * rate;
				}
				val[i] = row;
			}
		}
		return val;
	}

	/**
	 * @param x
	 * @param y
	 * @param rate
	 * @return
	 */
	public static double[][] eye(int x, int y, double rate) {
		double[][] val = new double[x][];
		for (int i = 0; i < x; i++) {
			double[] row = new double[y];
			val[i] = row;
			for (int j = 0; j < y; j++) {
				val[i][j] = rate;
			}
		}
		return val;
	}

	/**
	 * @param x
	 * @param y
	 * @param rate
	 * @return
	 */
	public static double[][] number(int x, int y, double rate) {
		double[][] val = new double[x][];
		if (rate == 0d) {
			for (int i = 0; i < x; i++) {
				double[] row = new double[y];
				for (int j = 0; j < y; j++) {
					row[j] = rate;
				}
				val[i] = row;
			}
		} else {
			for (int i = 0; i < x; i++) {
				double[] row = new double[y];
				for (int j = 0; j < y; j++) {
					row[j] = rate;
				}
				val[i] = row;
			}
		}
		return val;
	}

	/**
	 * @param x
	 * @param y
	 * @param rate
	 * @return
	 */
	public static float[][] number(int x, int y, float rate) {
		float[][] val = new float[x][];
		if (rate == 0d) {
			for (int i = 0; i < x; i++) {
				float[] row = new float[y];
				for (int j = 0; j < y; j++) {
					row[j] = rate;
				}
				val[i] = row;
			}
		} else {
			for (int i = 0; i < x; i++) {
				float[] row = new float[y];
				for (int j = 0; j < y; j++) {
					row[j] = rate;
				}
				val[i] = row;
			}
		}
		return val;
	}

	/**
	 * 获取固定值
	 *
	 * @param x
	 * @param rate
	 * @return
	 */
	public static double[] number(int x, double rate) {
		double[] val = new double[x];
		if (rate == 0d) {
		} else {
			for (int i = 0; i < x; i++) {
				val[i] = rate;
			}
		}
		return val;
	}

	/**
	 * 获取固定值
	 *
	 * @param x
	 * @param rate
	 * @return
	 */
	public static float[] number(int x, float rate) {
		float[] val = new float[x];
		if (rate == 0d) {
		} else {
			for (int i = 0; i < x; i++) {
				val[i] = rate;
			}
		}
		return val;
	}

	/**
	 * Returns the arc cosine of an angle, in the range of 0.0 through pi.
	 */
	public static double acos(double a) {
		return Math.acos(a);
	}

	/**
	 * Returns the arc sine of an angle, in the range of -pi/2 through pi/2.
	 */
	public static double asin(double a) {
		return Math.asin(a);
	}

	/**
	 * Returns the arc tangent of an angle, in the range of -pi/2 through pi/2.
	 */
	public static double atan(double a) {
		return Math.atan(a);
	}

	/**
	 * Converts rectangular coordinates (x, y) to polar (r, theta).
	 */
	public static double atan2(double y, double x) {
		return Math.atan2(y, x);
	}

	/**
	 * Returns the cube root of a double value.
	 */
	public static double cbrt(double a) {
		return Math.cbrt(a);
	}

	/**
	 * Returns the first floating-point argument with the sign of the second
	 * floating-point argument.
	 */
	public static double copySign(double magnitude, double sign) {
		return Math.copySign(magnitude, sign);
	}

	/**
	 * Returns the first floating-point argument with the sign of the second
	 * floating-point argument.
	 */
	public static float copySign(float magnitude, float sign) {
		return Math.copySign(magnitude, sign);
	}

	/**
	 * Returns the trigonometric cosine of an angle.
	 */
	public static double cos(double a) {
		return Math.cos(a);
	}

	/**
	 * Returns Euler's number e raised to the power of a double value.
	 */
	public static double exp(double a) {
		return Math.exp(a);
	}

	/**
	 * Returns e<sup>x</sup>-1.
	 */
	public static double expm1(double x) {
		return Math.expm1(x);
	}

	/**
	 * Returns the unbiased exponent used in the representation of a double.
	 */
	public static int getExponent(double d) {
		return Math.getExponent(d);
	}

	/**
	 * Returns the unbiased exponent used in the representation of a float.
	 */
	public static int getExponent(float f) {
		return Math.getExponent(f);
	}

	/**
	 * Computes the remainder operation on two arguments as prescribed by the
	 * IEEE 754 standard.
	 */
	public static double IEEEremainder(double f1, double f2) {
		return Math.IEEEremainder(f1, f2);
	}

	/**
	 * Returns the natural logarithm (base e) of a double value.
	 */
	public static double log(double a) {
		return Math.log(a);
	}

	/**
	 * Returns the natural logarithm of the sum of the argument and 1.
	 */
	public static double log1p(double x) {
		return Math.log1p(x);
	}

	/**
	 * Returns the greater of two double values.
	 */
	public static double max(double a, double b) {
		return Math.max(a, b);
	}

	/**
	 * Returns the greater of two float values.
	 */
	public static float max(float a, float b) {
		return Math.max(a, b);
	}

	/**
	 * Returns the greater of two int values.
	 */
	public static int max(int a, int b) {
		return Math.max(a, b);
	}

	/**
	 * Returns the greater of two long values.
	 */
	public static long max(long a, long b) {
		return Math.max(a, b);
	}

	/**
	 * Returns the smaller of two double values.
	 */
	public static double min(double a, double b) {
		return Math.min(a, b);
	}

	/**
	 * Returns the smaller of two float values.
	 */
	public static float min(float a, float b) {
		return Math.min(a, b);
	}

	/**
	 * Returns the smaller of two int values.
	 */
	public static int min(int a, int b) {
		return Math.min(a, b);
	}

	/**
	 * Returns the smaller of two long values.
	 */
	public static long min(long a, long b) {
		return Math.min(a, b);
	}

	/**
	 * Returns the floating-point number adjacent to the first argument in the
	 * direction of the second argument.
	 */
	public static double nextAfter(double start, double direction) {
		return Math.nextAfter(start, direction);
	}

	/**
	 * Returns the floating-point number adjacent to the first argument in the
	 * direction of the second argument.
	 */
	public static float nextAfter(float start, double direction) {
		return Math.nextAfter(start, direction);
	}

	/**
	 * Returns the floating-point value adjacent to d in the direction of
	 * positive infinity.
	 */
	public static double nextUp(double d) {
		return Math.nextUp(d);
	}

	/**
	 * Returns the floating-point value adjacent to f in the direction of
	 * positive infinity.
	 */
	public static float nextUp(float f) {
		return Math.nextUp(f);
	}

	/**
	 * Returns the value of the first argument raised to the power of the second
	 * argument.
	 */
	public static double pow(double a, double b) {
		return Math.pow(a, b);
	}

	/**
	 * Returns the double value that is closest in value to the argument and is
	 * equal to a mathematical integer.
	 */
	public static double rint(double a) {
		return Math.rint(a);
	}

	/**
	 * Returns the closest int to the argument.
	 */
	public static int round(float a) {
		return Math.round(a);
	}

	/**
	 * Returns d x 2<sup>scaleFactor</sup> rounded as if performed by a single
	 * correctly rounded floating-point multiply to a member of the double value
	 * set.
	 */
	public static double scalb(double d, int scaleFactor) {
		return Math.scalb(d, scaleFactor);
	}

	/**
	 * Returns f x 2<sup>scaleFactor</sup> rounded as if performed by a single
	 * correctly rounded floating-point multiply to a member of the float value
	 * set.
	 */
	public static float scalb(float f, int scaleFactor) {
		return Math.scalb(f, scaleFactor);
	}

	/**
	 * Returns the signum of the argument; zero if the argument is zero, 1.0 if
	 * the argument is greater than zero, -1.0 if the argument is less than
	 * zero.
	 */
	public static double signum(double d) {
		return Math.signum(d);
	}

	/**
	 * Returns the signum function of the argument; zero if the argument is
	 * zero, 1.0f if the argument is greater than zero, -1.0f if the argument is
	 * less than zero.
	 */
	public static float signum(float f) {
		return Math.signum(f);
	}

	/**
	 * Returns the trigonometric sine of an angle.
	 */
	public static double sin(double a) {
		return Math.sin(a);
	}

	/**
	 * Returns the trigonometric tangent of an angle.
	 */
	public static double tan(double a) {
		return Math.tan(a);
	}

	/**
	 * Converts an angle measured in radians to an approximately equivalent
	 * angle measured in degrees.
	 */
	public static double toDegrees(double angrad) {
		return Math.toDegrees(angrad);
	}

	/**
	 * Converts an angle measured in degrees to an approximately equivalent
	 * angle measured in radians.
	 */
	public static double toRadians(double angdeg) {
		return Math.toRadians(angdeg);
	}

	/**
	 * Returns the size of an ulp of the argument.
	 */
	public static double ulp(double d) {
		return Math.ulp(d);
	}

	/**
	 * * Returns the size of an ulp of the argument.
	 */
	public static float ulp(float f) {
		return Math.ulp(f);
	}

	/**
	 * Returns true if two double values equals to each other in the system
	 * precision.
	 *
	 * @param a
	 *            a double value.
	 * @param b
	 *            a double value.
	 * @return true if two double values equals to each other in the system
	 *         precision
	 */
	public static boolean equals(double a, double b) {
		if (a == b) {
			return true;
		}

		double absa = Math.abs(a);
		double absb = Math.abs(b);
		return Math.abs(a - b) <= Math.min(absa, absb) * 2.2204460492503131e-16;
	}

	/**
	 * Logistic sigmoid function.
	 */
	public static double logistic(double x) {
		double y = 0.0;
		if (x < -40) {
			y = 2.353853e+17;
		} else if (x > 40) {
			y = 1.0 + 4.248354e-18;
		} else {
			y = 1.0 + Math.exp(-x);
		}

		return 1.0 / y;
	}

	/**
	 * Returns x * x.
	 */
	public static double sqr(double x) {
		return x * x;
	}

	/**
	 * Returns true if x is a power of 2.
	 */
	public static boolean isPower2(int x) {
		return x > 0 && (x & (x - 1)) == 0;
	}

	/**
	 * Given a set of n probabilities, generate a random number in [0, n).
	 *
	 * @param prob
	 *            probabilities of size n. The prob argument can be used to give
	 *            a vector of weights for obtaining the elements of the vector
	 *            being sampled. They need not sum to one, but they should be
	 *            nonnegative and not all zero.
	 * @return a random integer in [0, n).
	 */
	public static int random(double[] prob) {
		int[] ans = random(prob, 1);
		return ans[0];
	}

	/**
	 * Given a set of m probabilities, draw with replacement a set of n random
	 * number in [0, m).
	 *
	 * @param prob
	 *            probabilities of size n. The prob argument can be used to give
	 *            a vector of weights for obtaining the elements of the vector
	 *            being sampled. They need not sum to one, but they should be
	 *            nonnegative and not all zero.
	 * @return an random array of length n in range of [0, m).
	 */
	public static int[] random(double[] prob, int n) {
		// set up alias table
		double[] q = new double[prob.length];
		for (int i = 0; i < prob.length; i++) {
			q[i] = prob[i] * prob.length;
		}

		// initialize a with indices
		int[] a = new int[prob.length];
		for (int i = 0; i < prob.length; i++) {
			a[i] = i;
		}

		// set up H and L
		int[] HL = new int[prob.length];
		int head = 0;
		int tail = prob.length - 1;
		for (int i = 0; i < prob.length; i++) {
			if (q[i] >= 1.0) {
				HL[head++] = i;
			} else {
				HL[tail--] = i;
			}
		}

		while (head != 0 && tail != prob.length - 1) {
			int j = HL[tail + 1];
			int k = HL[head - 1];
			a[j] = k;
			q[k] += q[j] - 1;
			tail++; // remove j from L
			if (q[k] < 1.0) {
				HL[tail--] = k; // add k to L
				head--; // remove k
			}
		}

		// generate sample
		int[] ans = new int[n];
		for (int i = 0; i < n; i++) {
			double rU = random() * prob.length;

			int k = (int) (rU);
			rU -= k; /* rU becomes rU-[rU] */

			if (rU < q[k]) {
				ans[i] = k;
			} else {
				ans[i] = a[k];
			}
		}

		return ans;
	}

	public static double random() {
		return Math.random();
	}

	/**
	 * Returns a slice of data for given indices.
	 */
	public static <E> E[] slice(E[] data, int[] index) {
		int n = index.length;

		@SuppressWarnings("unchecked")
		E[] x = (E[]) java.lang.reflect.Array.newInstance(data.getClass()
				.getComponentType(), n);

		for (int i = 0; i < n; i++) {
			x[i] = data[index[i]];
		}

		return x;
	}

	/**
	 * Returns a slice of data for given indices.
	 */
	public static int[] slice(int[] data, int[] index) {
		int n = index.length;
		int[] x = new int[n];
		for (int i = 0; i < n; i++) {
			x[i] = data[index[i]];
		}

		return x;
	}

	/**
	 * Returns a slice of data for given indices.
	 */
	public static float[] slice(float[] data, int[] index) {
		int n = index.length;
		float[] x = new float[n];
		for (int i = 0; i < n; i++) {
			x[i] = data[index[i]];
		}

		return x;
	}

	/**
	 * Returns a slice of data for given indices.
	 */
	public static double[] slice(double[] data, int[] index) {
		int n = index.length;
		double[] x = new double[n];
		for (int i = 0; i < n; i++) {
			x[i] = data[index[i]];
		}

		return x;
	}

	/**
	 * Determines if the polygon contains the specified coordinates.
	 *
	 * @param point
	 *            the coordinates of specified point to be tested.
	 * @return true if the Polygon contains the specified coordinates; false
	 *         otherwise.
	 */
	public static boolean contains(double[][] polygon, double[] point) {
		return contains(polygon, point[0], point[1]);
	}

	/**
	 * Determines if the polygon contains the specified coordinates.
	 *
	 * @param x
	 *            the specified x coordinate.
	 * @param y
	 *            the specified y coordinate.
	 * @return true if the Polygon contains the specified coordinates; false
	 *         otherwise.
	 */
	public static boolean contains(double[][] polygon, double x, double y) {
		if (polygon.length <= 2) {
			return false;
		}

		int hits = 0;

		int n = polygon.length;
		double lastx = polygon[n - 1][0];
		double lasty = polygon[n - 1][1];
		double curx, cury;

		// Walk the edges of the polygon
		for (int i = 0; i < n; lastx = curx, lasty = cury, i++) {
			curx = polygon[i][0];
			cury = polygon[i][1];

			if (cury == lasty) {
				continue;
			}

			double leftx;
			if (curx < lastx) {
				if (x >= lastx) {
					continue;
				}
				leftx = curx;
			} else {
				if (x >= curx) {
					continue;
				}
				leftx = lastx;
			}

			double test1, test2;
			if (cury < lasty) {
				if (y < cury || y >= lasty) {
					continue;
				}
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - curx;
				test2 = y - cury;
			} else {
				if (y < lasty || y >= cury) {
					continue;
				}
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - lastx;
				test2 = y - lasty;
			}

			if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
				hits++;
			}
		}

		return ((hits & 1) != 0);
	}

	/**
	 * Reverses the order of the elements in the specified array.
	 *
	 * @param a
	 *            an array to reverse.
	 */
	public static void reverse(int[] a) {
		int i = 0, j = a.length - 1;
		while (i < j) {
			swap(a, i++, j--); // code for swap not shown, but easy enough
		}
	}

	/**
	 * Reverses the order of the elements in the specified array.
	 *
	 * @param a
	 *            an array to reverse.
	 */
	public static void reverse(float[] a) {
		int i = 0, j = a.length - 1;
		while (i < j) {
			swap(a, i++, j--); // code for swap not shown, but easy enough
		}
	}

	/**
	 * To restore the max-heap condition when a node's priority is increased. We
	 * move up the heap, exchaning the node at position k with its parent (at
	 * postion k/2) if necessary, continuing as long as a[k/2] &lt; a[k] or
	 * until we reach the top of the heap.
	 */
	public static void siftUp(int[] arr, int k) {
		while (k > 1 && arr[k / 2] < arr[k]) {
			swap(arr, k, k / 2);
			k = k / 2;
		}
	}

	/**
	 * To restore the max-heap condition when a node's priority is increased. We
	 * move up the heap, exchaning the node at position k with its parent (at
	 * postion k/2) if necessary, continuing as long as a[k/2] &lt; a[k] or
	 * until we reach the top of the heap.
	 */
	public static void siftUp(float[] arr, int k) {
		while (k > 1 && arr[k / 2] < arr[k]) {
			swap(arr, k, k / 2);
			k = k / 2;
		}
	}

	/**
	 * To restore the max-heap condition when a node's priority is increased. We
	 * move up the heap, exchaning the node at position k with its parent (at
	 * postion k/2) if necessary, continuing as long as a[k/2] &lt; a[k] or
	 * until we reach the top of the heap.
	 */
	public static void siftUp(double[] arr, int k) {
		while (k > 1 && arr[k / 2] < arr[k]) {
			swap(arr, k, k / 2);
			k = k / 2;
		}
	}

	/**
	 * To restore the max-heap condition when a node's priority is increased. We
	 * move up the heap, exchaning the node at position k with its parent (at
	 * postion k/2) if necessary, continuing as long as a[k/2] &lt; a[k] or
	 * until we reach the top of the heap.
	 */
	public static <T extends Comparable<? super T>> void siftUp(T[] arr, int k) {
		while (k > 1 && arr[k / 2].compareTo(arr[k]) < 0) {
			swap(arr, k, k / 2);
			k = k / 2;
		}
	}

	/**
	 * To restore the max-heap condition when a node's priority is decreased. We
	 * move down the heap, exchanging the node at position k with the larger of
	 * that node's two children if necessary and stopping when the node at k is
	 * not smaller than either child or the bottom is reached. Note that if n is
	 * even and k is n/2, then the node at k has only one child -- this case
	 * must be treated properly.
	 */
	public static void siftDown(int[] arr, int k, int n) {
		while (2 * k <= n) {
			int j = 2 * k;
			if (j < n && arr[j] < arr[j + 1]) {
				j++;
			}
			if (arr[k] >= arr[j]) {
				break;
			}
			swap(arr, k, j);
			k = j;
		}
	}

	/**
	 * To restore the max-heap condition when a node's priority is decreased. We
	 * move down the heap, exchanging the node at position k with the larger of
	 * that node's two children if necessary and stopping when the node at k is
	 * not smaller than either child or the bottom is reached. Note that if n is
	 * even and k is n/2, then the node at k has only one child -- this case
	 * must be treated properly.
	 */
	public static void siftDown(float[] arr, int k, int n) {
		while (2 * k <= n) {
			int j = 2 * k;
			if (j < n && arr[j] < arr[j + 1]) {
				j++;
			}
			if (arr[k] >= arr[j]) {
				break;
			}
			swap(arr, k, j);
			k = j;
		}
	}

	/**
	 * To restore the max-heap condition when a node's priority is decreased. We
	 * move down the heap, exchanging the node at position k with the larger of
	 * that node's two children if necessary and stopping when the node at k is
	 * not smaller than either child or the bottom is reached. Note that if n is
	 * even and k is n/2, then the node at k has only one child -- this case
	 * must be treated properly.
	 */
	public static void siftDown(double[] arr, int k, int n) {
		while (2 * k <= n) {
			int j = 2 * k;
			if (j < n && arr[j] < arr[j + 1]) {
				j++;
			}
			if (arr[k] >= arr[j]) {
				break;
			}
			swap(arr, k, j);
			k = j;
		}
	}

	/**
	 * To restore the max-heap condition when a node's priority is decreased. We
	 * move down the heap, exchanging the node at position k with the larger of
	 * that node's two children if necessary and stopping when the node at k is
	 * not smaller than either child or the bottom is reached. Note that if n is
	 * even and k is n/2, then the node at k has only one child -- this case
	 * must be treated properly.
	 */
	public static <T extends Comparable<? super T>> void siftDown(T[] arr,
			int k, int n) {
		while (2 * k <= n) {
			int j = 2 * k;
			if (j < n && arr[j].compareTo(arr[j + 1]) < 0) {
				j++;
			}
			if (arr[k].compareTo(arr[j]) >= 0) {
				break;
			}
			swap(arr, k, j);
			k = j;
		}
	}

	/**
	 * Reverses the order of the elements in the specified array.
	 *
	 * @param a
	 *            an array to reverse.
	 */
	public static void reverse(double[] a) {
		int i = 0, j = a.length - 1;
		while (i < j) {
			swap(a, i++, j--); // code for swap not shown, but easy enough
		}
	}

	/**
	 * Reverses the order of the elements in the specified array.
	 *
	 * @param a
	 *            an array to reverse.
	 */
	public static <T> void reverse(T[] a) {
		int i = 0, j = a.length - 1;
		while (i < j) {
			swap(a, i++, j--);
		}
	}

	/**
	 * minimum of 3 integers
	 */
	public static int min(int a, int b, int c) {
		return min(min(a, b), c);
	}

	/**
	 * minimum of 3 floats
	 */
	public static double min(float a, float b, float c) {
		return min(min(a, b), c);
	}

	/**
	 * minimum of 3 doubles
	 */
	public static double min(double a, double b, double c) {
		return min(min(a, b), c);
	}

	/**
	 * maximum of 3 integers
	 */
	public static int max(int a, int b, int c) {
		return max(max(a, b), c);
	}

	/**
	 * maximum of 3 floats
	 */
	public static float max(float a, float b, float c) {
		return max(max(a, b), c);
	}

	/**
	 * maximum of 3 doubles
	 */
	public static double max(double a, double b, double c) {
		return max(max(a, b), c);
	}

	/**
	 * Returns the minimum value of an array.
	 */
	public static int min(int... x) {
		int m = x[0];

		for (int n : x) {
			if (n < m) {
				m = n;
			}
		}

		return m;
	}

	/**
	 * Returns the minimum value of an array.
	 */
	public static float min(float... x) {
		float m = Float.POSITIVE_INFINITY;

		for (float n : x) {
			if (n < m) {
				m = n;
			}
		}

		return m;
	}

	/**
	 * Returns the index of minimum value of an array.
	 */
	public static int whichMin(int... x) {
		int m = x[0];
		int which = 0;

		for (int i = 1; i < x.length; i++) {
			if (x[i] < m) {
				m = x[i];
				which = i;
			}
		}

		return which;
	}

	/**
	 * Returns the index of minimum value of an array.
	 */
	public static int whichMin(float... x) {
		float m = Float.POSITIVE_INFINITY;
		int which = 0;

		for (int i = 0; i < x.length; i++) {
			if (x[i] < m) {
				m = x[i];
				which = i;
			}
		}

		return which;
	}

	/**
	 * Returns the index of minimum value of an array.
	 */
	public static int whichMin(double... x) {
		double m = Double.POSITIVE_INFINITY;
		int which = 0;

		for (int i = 0; i < x.length; i++) {
			if (x[i] < m) {
				m = x[i];
				which = i;
			}
		}

		return which;
	}

	/**
	 * Returns the maximum value of an array.
	 */
	public static float max(float... x) {
		float m = Float.NEGATIVE_INFINITY;

		for (float n : x) {
			if (n > m) {
				m = n;
			}
		}

		return m;
	}

	/**
	 * Returns the index of maximum value of an array.
	 */
	public static int whichMax(int... x) {
		int m = x[0];
		int which = 0;

		for (int i = 1; i < x.length; i++) {
			if (x[i] > m) {
				m = x[i];
				which = i;
			}
		}

		return which;
	}

	/**
	 * Returns the index of maximum value of an array.
	 */
	public static int whichMax(float... x) {
		float m = Float.NEGATIVE_INFINITY;
		int which = 0;

		for (int i = 0; i < x.length; i++) {
			if (x[i] > m) {
				m = x[i];
				which = i;
			}
		}

		return which;
	}

	/**
	 * Returns the index of maximum value of an array.
	 */
	public static int whichMax(double... x) {
		double m = Double.NEGATIVE_INFINITY;
		int which = 0;

		for (int i = 0; i < x.length; i++) {
			if (x[i] > m) {
				m = x[i];
				which = i;
			}
		}

		return which;
	}

	/**
	 * Returns the row minimum for a matrix.
	 */
	public static double[] rowMin(double[][] data) {
		double[] x = new double[data.length];

		for (int i = 0; i < x.length; i++) {
			x[i] = min(data[i]);
		}

		return x;
	}

	/**
	 * Returns the row maximum for a matrix.
	 */
	public static double[] rowMax(double[][] data) {
		double[] x = new double[data.length];

		for (int i = 0; i < x.length; i++) {
			x[i] = max(data[i]);
		}

		return x;
	}

	/**
	 * Returns the row sums for a matrix.
	 */
	public static double[] rowSum(double[][] data) {
		double[] x = new double[data.length];

		for (int i = 0; i < x.length; i++) {
			x[i] = sum(data[i]);
		}

		return x;
	}

	/**
	 * Returns the row means for a matrix.
	 */
	public static double[] rowMean(double[][] data) {
		double[] x = new double[data.length];

		for (int i = 0; i < x.length; i++) {
			x[i] = mean(data[i]);
		}

		return x;
	}

	/**
	 * Returns the row standard deviations for a matrix.
	 */
	public static double[] rowSd(double[][] data) {
		double[] x = new double[data.length];

		for (int i = 0; i < x.length; i++) {
			x[i] = sd(data[i]);
		}

		return x;
	}

	/**
	 * Returns the column minimum for a matrix.
	 */
	public static double[] colMin(double[][] data) {
		double[] x = new double[data[0].length];
		for (int i = 0; i < x.length; i++) {
			x[i] = Double.POSITIVE_INFINITY;
		}

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < x.length; j++) {
				if (x[j] > data[i][j]) {
					x[j] = data[i][j];
				}
			}
		}

		return x;
	}

	/**
	 * Returns the column maximum for a matrix.
	 */
	public static double[] colMax(double[][] data) {
		double[] x = new double[data[0].length];
		for (int i = 0; i < x.length; i++) {
			x[i] = Double.NEGATIVE_INFINITY;
		}

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < x.length; j++) {
				if (x[j] < data[i][j]) {
					x[j] = data[i][j];
				}
			}
		}

		return x;
	}

	/**
	 * Returns the column sums for a matrix.
	 */
	public static double[] colSum(double[][] data) {
		double[] x = data[0].clone();

		for (int i = 1; i < data.length; i++) {
			for (int j = 0; j < x.length; j++) {
				x[j] += data[i][j];
			}
		}

		return x;
	}

	/**
	 * Returns the column sums for a matrix.
	 */
	public static double[] colMean(double[][] data) {
		double[] x = data[0].clone();

		for (int i = 1; i < data.length; i++) {
			for (int j = 0; j < x.length; j++) {
				x[j] += data[i][j];
			}
		}

		scale(1.0 / data.length, x);

		return x;
	}

	/**
	 * Returns the column deviations for a matrix.
	 */
	public static double[] colSd(double[][] data) {
		if (data.length < 2) {
			throw new IllegalArgumentException("Array length is less than 2.");
		}

		int p = data[0].length;
		double[] sum = new double[p];
		double[] sumsq = new double[p];
		for (double[] x : data) {
			for (int i = 0; i < p; i++) {
				sum[i] += x[i];
				sumsq[i] += x[i] * x[i];
			}
		}

		int n = data.length - 1;
		for (int i = 0; i < p; i++) {
			sumsq[i] = Math.sqrt(sumsq[i] / n
					- (sum[i] / data.length) * (sum[i] / n));
		}

		return sumsq;
	}

	/**
	 * Given k in [0, n-1], returns an array value from arr such that k array
	 * values are less than or equal to the one returned. The input array will
	 * be rearranged to have this value in location arr[k], with all smaller
	 * elements moved to arr[0, k-1] (in arbitrary order) and all larger
	 * elements in arr[k+1, n-1] (also in arbitrary order).
	 */
	public static int select(int[] arr, int k) {
		int n = arr.length;
		int l = 0;
		int ir = n - 1;

		int a;
		int i, j, mid;
		for (;;) {
			if (ir <= l + 1) {
				if (ir == l + 1 && arr[ir] < arr[l]) {
					swap(arr, l, ir);
				}
				return arr[k];
			} else {
				mid = (l + ir) >> 1;
				swap(arr, mid, l + 1);
				if (arr[l] > arr[ir]) {
					swap(arr, l, ir);
				}
				if (arr[l + 1] > arr[ir]) {
					swap(arr, l + 1, ir);
				}
				if (arr[l] > arr[l + 1]) {
					swap(arr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i] < a);
					do {
						j--;
					} while (arr[j] > a);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				if (j >= k) {
					ir = j - 1;
				}
				if (j <= k) {
					l = i;
				}
			}
		}
	}

	/**
	 * Given k in [0, n-1], returns an array value from arr such that k array
	 * values are less than or equal to the one returned. The input array will
	 * be rearranged to have this value in location arr[k], with all smaller
	 * elements moved to arr[0, k-1] (in arbitrary order) and all larger
	 * elements in arr[k+1, n-1] (also in arbitrary order).
	 */
	public static float select(float[] arr, int k) {
		int n = arr.length;
		int l = 0;
		int ir = n - 1;

		float a;
		int i, j, mid;
		for (;;) {
			if (ir <= l + 1) {
				if (ir == l + 1 && arr[ir] < arr[l]) {
					swap(arr, l, ir);
				}
				return arr[k];
			} else {
				mid = (l + ir) >> 1;
				swap(arr, mid, l + 1);
				if (arr[l] > arr[ir]) {
					swap(arr, l, ir);
				}
				if (arr[l + 1] > arr[ir]) {
					swap(arr, l + 1, ir);
				}
				if (arr[l] > arr[l + 1]) {
					swap(arr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i] < a);
					do {
						j--;
					} while (arr[j] > a);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				if (j >= k) {
					ir = j - 1;
				}
				if (j <= k) {
					l = i;
				}
			}
		}
	}

	/**
	 * Given k in [0, n-1], returns an array value from arr such that k array
	 * values are less than or equal to the one returned. The input array will
	 * be rearranged to have this value in location arr[k], with all smaller
	 * elements moved to arr[0, k-1] (in arbitrary order) and all larger
	 * elements in arr[k+1, n-1] (also in arbitrary order).
	 */
	public static double select(double[] arr, int k) {
		int n = arr.length;
		int l = 0;
		int ir = n - 1;

		double a;
		int i, j, mid;
		for (;;) {
			if (ir <= l + 1) {
				if (ir == l + 1 && arr[ir] < arr[l]) {
					swap(arr, l, ir);
				}
				return arr[k];
			} else {
				mid = (l + ir) >> 1;
				swap(arr, mid, l + 1);
				if (arr[l] > arr[ir]) {
					swap(arr, l, ir);
				}
				if (arr[l + 1] > arr[ir]) {
					swap(arr, l + 1, ir);
				}
				if (arr[l] > arr[l + 1]) {
					swap(arr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i] < a);
					do {
						j--;
					} while (arr[j] > a);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				if (j >= k) {
					ir = j - 1;
				}
				if (j <= k) {
					l = i;
				}
			}
		}
	}

	/**
	 * Given k in [0, n-1], returns an array value from arr such that k array
	 * values are less than or equal to the one returned. The input array will
	 * be rearranged to have this value in location arr[k], with all smaller
	 * elements moved to arr[0, k-1] (in arbitrary order) and all larger
	 * elements in arr[k+1, n-1] (also in arbitrary order).
	 */
	public static <T extends Comparable<? super T>> T select(T[] arr, int k) {
		int n = arr.length;
		int l = 0;
		int ir = n - 1;

		T a;
		int i, j, mid;
		for (;;) {
			if (ir <= l + 1) {
				if (ir == l + 1 && arr[ir].compareTo(arr[l]) < 0) {
					swap(arr, l, ir);
				}
				return arr[k];
			} else {
				mid = (l + ir) >> 1;
				swap(arr, mid, l + 1);
				if (arr[l].compareTo(arr[ir]) > 0) {
					swap(arr, l, ir);
				}
				if (arr[l + 1].compareTo(arr[ir]) > 0) {
					swap(arr, l + 1, ir);
				}
				if (arr[l].compareTo(arr[l + 1]) > 0) {
					swap(arr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i].compareTo(a) < 0);
					do {
						j--;
					} while (arr[j].compareTo(a) > 0);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				if (j >= k) {
					ir = j - 1;
				}
				if (j <= k) {
					l = i;
				}
			}
		}
	}

	/**
	 * Find the median of an array of type integer.
	 */
	public static int median(int[] a) {
		int k = a.length / 2;
		return select(a, k);
	}

	/**
	 * Find the median of an array of type float.
	 */
	public static float median(float[] a) {
		int k = a.length / 2;
		return select(a, k);
	}

	/**
	 * Find the median of an array of type double.
	 */
	public static double median(double[] a) {
		int k = a.length / 2;
		return select(a, k);
	}

	/**
	 * Find the median of an array of type double.
	 */
	public static <T extends Comparable<? super T>> T median(T[] a) {
		int k = a.length / 2;
		return select(a, k);
	}

	/**
	 * Find the first quantile (p = 1/4) of an array of type integer.
	 */
	public static int q1(int[] a) {
		int k = a.length / 4;
		return select(a, k);
	}

	/**
	 * Find the first quantile (p = 1/4) of an array of type float.
	 */
	public static float q1(float[] a) {
		int k = a.length / 4;
		return select(a, k);
	}

	/**
	 * Find the first quantile (p = 1/4) of an array of type double.
	 */
	public static double q1(double[] a) {
		int k = a.length / 4;
		return select(a, k);
	}

	/**
	 * Find the first quantile (p = 1/4) of an array of type double.
	 */
	public static <T extends Comparable<? super T>> T q1(T[] a) {
		int k = a.length / 4;
		return select(a, k);
	}

	/**
	 * Find the third quantile (p = 3/4) of an array of type integer.
	 */
	public static int q3(int[] a) {
		int k = 3 * a.length / 4;
		return select(a, k);
	}

	/**
	 * Find the third quantile (p = 3/4) of an array of type float.
	 */
	public static float q3(float[] a) {
		int k = 3 * a.length / 4;
		return select(a, k);
	}

	/**
	 * Find the third quantile (p = 3/4) of an array of type double.
	 */
	public static double q3(double[] a) {
		int k = 3 * a.length / 4;
		return select(a, k);
	}

	/**
	 * Returns the mean of an array.
	 */
	public static double mean(int[] x) {
		return (double) sum(x) / x.length;
	}

	/**
	 * Returns the mean of an array.
	 */
	public static double mean(float[] x) {
		return sum(x) / x.length;
	}

	/**
	 * Returns the variance of an array.
	 */
	public static double var(int[] x) {
		if (x.length < 2) {
			throw new IllegalArgumentException("Array length is less than 2.");
		}

		double sum = 0.0;
		double sumsq = 0.0;
		for (int xi : x) {
			sum += xi;
			sumsq += xi * xi;
		}

		int n = x.length - 1;
		return sumsq / n - (sum / x.length) * (sum / n);
	}

	/**
	 * Returns the variance of an array.
	 */
	public static double var(float[] x) {
		if (x.length < 2) {
			throw new IllegalArgumentException("Array length is less than 2.");
		}

		double sum = 0.0;
		double sumsq = 0.0;
		for (float xi : x) {
			sum += xi;
			sumsq += xi * xi;
		}

		int n = x.length - 1;
		return sumsq / n - (sum / x.length) * (sum / n);
	}

	/**
	 * Returns the standard deviation of an array.
	 */
	public static double sd(int[] x) {
		return sqrt(var(x));
	}

	/**
	 * Returns the standard deviation of an array.
	 */
	public static double sd(float[] x) {
		return sqrt(var(x));
	}

	/**
	 * Returns the standard deviation of an array.
	 */
	public static double sd(double[] x) {
		return sqrt(var(x));
	}

	/**
	 * Returns the median absolute deviation (MAD). Note that input array will
	 * be altered after the computation. MAD is a robust measure of the
	 * variability of a univariate sample of quantitative data. For a univariate
	 * data set X<sub>1</sub>, X<sub>2</sub>, ..., X<sub>n</sub>, the MAD is
	 * defined as the median of the absolute deviations from the data's median:
	 * <p>
	 * MAD(X) = median(|X<sub>i</sub> - median(X<sub>i</sub>)|)
	 * <p>
	 * that is, starting with the residuals (deviations) from the data's median,
	 * the MAD is the median of their absolute values.
	 * <p>
	 * MAD is a more robust estimator of scale than the sample variance or
	 * standard deviation. For instance, MAD is more resilient to outliers in a
	 * data set than the standard deviation. It thus behaves better with
	 * distributions without a mean or variance, such as the Cauchy
	 * distribution.
	 * <p>
	 * In order to use the MAD as a consistent estimator for the estimation of
	 * the standard deviation &sigma;, one takes &sigma; = K * MAD, where K is a
	 * constant scale factor, which depends on the distribution. For normally
	 * distributed data K is taken to be 1.4826. Other distributions behave
	 * differently: for example for large samples from a uniform continuous
	 * distribution, this factor is about 1.1547.
	 */
	public static double mad(int[] x) {
		int m = median(x);
		for (int i = 0; i < x.length; i++) {
			x[i] = Math.abs(x[i] - m);
		}

		return median(x);
	}

	/**
	 * Returns the median absolute deviation (MAD). Note that input array will
	 * be altered after the computation. MAD is a robust measure of the
	 * variability of a univariate sample of quantitative data. For a univariate
	 * data set X<sub>1</sub>, X<sub>2</sub>, ..., X<sub>n</sub>, the MAD is
	 * defined as the median of the absolute deviations from the data's median:
	 * <p>
	 * MAD(X) = median(|X<sub>i</sub> - median(X<sub>i</sub>)|)
	 * <p>
	 * that is, starting with the residuals (deviations) from the data's median,
	 * the MAD is the median of their absolute values.
	 * <p>
	 * MAD is a more robust estimator of scale than the sample variance or
	 * standard deviation. For instance, MAD is more resilient to outliers in a
	 * data set than the standard deviation. It thus behaves better with
	 * distributions without a mean or variance, such as the Cauchy
	 * distribution.
	 * <p>
	 * In order to use the MAD as a consistent estimator for the estimation of
	 * the standard deviation &sigma;, one takes &sigma; = K * MAD, where K is a
	 * constant scale factor, which depends on the distribution. For normally
	 * distributed data K is taken to be 1.4826. Other distributions behave
	 * differently: for example for large samples from a uniform continuous
	 * distribution, this factor is about 1.1547.
	 */
	public static double mad(float[] x) {
		float m = median(x);
		for (int i = 0; i < x.length; i++) {
			x[i] = Math.abs(x[i] - m);
		}

		return median(x);
	}

	/**
	 * Returns the median absolute deviation (MAD). Note that input array will
	 * be altered after the computation. MAD is a robust measure of the
	 * variability of a univariate sample of quantitative data. For a univariate
	 * data set X<sub>1</sub>, X<sub>2</sub>, ..., X<sub>n</sub>, the MAD is
	 * defined as the median of the absolute deviations from the data's median:
	 * <p>
	 * MAD(X) = median(|X<sub>i</sub> - median(X<sub>i</sub>)|)
	 * <p>
	 * that is, starting with the residuals (deviations) from the data's median,
	 * the MAD is the median of their absolute values.
	 * <p>
	 * MAD is a more robust estimator of scale than the sample variance or
	 * standard deviation. For instance, MAD is more resilient to outliers in a
	 * data set than the standard deviation. It thus behaves better with
	 * distributions without a mean or variance, such as the Cauchy
	 * distribution.
	 * <p>
	 * In order to use the MAD as a consistent estimator for the estimation of
	 * the standard deviation &sigma;, one takes &sigma; = K * MAD, where K is a
	 * constant scale factor, which depends on the distribution. For normally
	 * distributed data K is taken to be 1.4826. Other distributions behave
	 * differently: for example for large samples from a uniform continuous
	 * distribution, this factor is about 1.1547.
	 */
	public static double mad(double[] x) {
		double m = median(x);
		for (int i = 0; i < x.length; i++) {
			x[i] = Math.abs(x[i] - m);
		}

		return median(x);
	}

	/**
	 * Given a set of boolean values, are all of the values true?
	 */
	public static boolean all(boolean[] x) {
		for (boolean b : x) {
			if (!b) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Given a set of boolean values, is at least one of the values true?
	 */
	public static boolean any(boolean[] x) {
		for (boolean b : x) {
			if (b) {
				return true;
			}
		}

		return false;
	}

	/**
	 * The Euclidean distance.
	 */
	public static double distance(int[] x, int[] y) {
		return Math.sqrt(squaredDistance(x, y));
	}

	/**
	 * The Euclidean distance.
	 */
	public static double distance(float[] x, float[] y) {
		return Math.sqrt(squaredDistance(x, y));
	}

	/**
	 * The Euclidean distance.
	 */
	public static double distance(double[] x, double[] y) {
		return Math.sqrt(squaredDistance(x, y));
	}

	/**
	 * The squared Euclidean distance.
	 */
	public static double squaredDistance(int[] x, int[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(
					"Input vector sizes are different.");
		}

		double sum = 0.0;
		for (int i = 0; i < x.length; i++) {
			sum += sqr(x[i] - y[i]);
		}

		return sum;
	}

	/**
	 * The squared Euclidean distance.
	 */
	public static double squaredDistance(float[] x, float[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(
					"Input vector sizes are different.");
		}

		double sum = 0.0;
		for (int i = 0; i < x.length; i++) {
			sum += sqr(x[i] - y[i]);
		}

		return sum;
	}

	/**
	 * The squared Euclidean distance.
	 */
	public static double squaredDistance(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(
					"Input vector sizes are different.");
		}

		double sum = 0.0;
		for (int i = 0; i < x.length; i++) {
			sum += sqr(x[i] - y[i]);
		}

		return sum;
	}

	// /**
	// * The Euclidean distance.
	// */
	// public static double squaredDistance(SparseArray x, SparseArray y) {
	// Iterator<SparseArray.Entry> it1 = x.iterator();
	// Iterator<SparseArray.Entry> it2 = y.iterator();
	// SparseArray.Entry e1 = it1.hasNext() ? it1.next() : null;
	// SparseArray.Entry e2 = it2.hasNext() ? it2.next() : null;
	//
	// double sum = 0.0;
	// while (e1 != null && e2 != null) {
	// if (e1.i == e2.i) {
	// sum += Math.sqr(e1.x - e2.x);
	// e1 = it1.hasNext() ? it1.next() : null;
	// e2 = it2.hasNext() ? it2.next() : null;
	// } else if (e1.i > e2.i) {
	// sum += Math.sqr(e2.x);
	// e2 = it2.hasNext() ? it2.next() : null;
	// } else {
	// sum += Math.sqr(e1.x);
	// e1 = it1.hasNext() ? it1.next() : null;
	// }
	// }
	//
	// while (it1.hasNext()) {
	// sum += Math.sqr(it1.next().x);
	// }
	//
	// while (it2.hasNext()) {
	// sum += Math.sqr(it2.next().x);
	// }
	//
	// return sum;
	// }

	/**
	 * Kullback-Leibler divergence. The Kullback-Leibler divergence (also
	 * information divergence, information gain, relative entropy, or KLIC) is a
	 * non-symmetric measure of the difference between two probability
	 * distributions P and Q. KL measures the expected number of extra bits
	 * required to code samples from P when using a code based on Q, rather than
	 * using a code based on P. Typically P represents the "true" distribution
	 * of data, observations, or a precise calculated theoretical distribution.
	 * The measure Q typically represents a theory, model, description, or
	 * approximation of P.
	 * <p>
	 * Although it is often intuited as a distance metric, the KL divergence is
	 * not a true metric - for example, the KL from P to Q is not necessarily
	 * the same as the KL from Q to P.
	 */
	public static double KullbackLeiblerDivergence(double[] x, double[] y) {
		boolean intersection = false;
		double kl = 0.0;

		for (int i = 0; i < x.length; i++) {
			if (x[i] != 0.0 && y[i] != 0.0) {
				intersection = true;
				kl += x[i] * Math.log(x[i] / y[i]);
			}
		}

		if (intersection) {
			return kl;
		} else {
			return Double.POSITIVE_INFINITY;
		}
	}

	// /**
	// * Kullback-Leibler divergence. The Kullback-Leibler divergence (also
	// * information divergence, information gain, relative entropy, or KLIC)
	// * is a non-symmetric measure of the difference between two probability
	// * distributions P and Q. KL measures the expected number of extra bits
	// * required to code samples from P when using a code based on Q, rather
	// * than using a code based on P. Typically P represents the "true"
	// * distribution of data, observations, or a precise calculated theoretical
	// * distribution. The measure Q typically represents a theory, model,
	// * description, or approximation of P.
	// * <p>
	// * Although it is often intuited as a distance metric, the KL divergence
	// is
	// * not a true metric - for example, the KL from P to Q is not necessarily
	// * the same as the KL from Q to P.
	// */
	// public static double KullbackLeiblerDivergence(SparseArray x, SparseArray
	// y) {
	// if (x.isEmpty()) {
	// throw new IllegalArgumentException("List x is empty.");
	// }
	//
	// if (y.isEmpty()) {
	// throw new IllegalArgumentException("List y is empty.");
	// }
	//
	// Iterator<SparseArray.Entry> iterX = x.iterator();
	// Iterator<SparseArray.Entry> iterY = y.iterator();
	//
	// SparseArray.Entry a = iterX.hasNext() ? iterX.next() : null;
	// SparseArray.Entry b = iterY.hasNext() ? iterY.next() : null;
	//
	// boolean intersection = false;
	// double kl = 0.0;
	//
	// while (a != null && b != null) {
	// if (a.i < b.i) {
	// a = iterX.hasNext() ? iterX.next() : null;
	// } else if (a.i > b.i) {
	// b = iterY.hasNext() ? iterY.next() : null;
	// } else {
	// intersection = true;
	// kl += a.x * Math.log(a.x / b.x);
	//
	// a = iterX.hasNext() ? iterX.next() : null;
	// b = iterY.hasNext() ? iterY.next() : null;
	// }
	// }
	//
	// if (intersection) {
	// return kl;
	// } else {
	// return Double.POSITIVE_INFINITY;
	// }
	// }

	// /**
	// * Kullback-Leibler divergence. The Kullback-Leibler divergence (also
	// * information divergence, information gain, relative entropy, or KLIC)
	// * is a non-symmetric measure of the difference between two probability
	// * distributions P and Q. KL measures the expected number of extra bits
	// * required to code samples from P when using a code based on Q, rather
	// * than using a code based on P. Typically P represents the "true"
	// * distribution of data, observations, or a precise calculated theoretical
	// * distribution. The measure Q typically represents a theory, model,
	// * description, or approximation of P.
	// * <p>
	// * Although it is often intuited as a distance metric, the KL divergence
	// is
	// * not a true metric - for example, the KL from P to Q is not necessarily
	// * the same as the KL from Q to P.
	// */
	// public static double KullbackLeiblerDivergence(double[] x, SparseArray y)
	// {
	// return KullbackLeiblerDivergence(y, x);
	// }

	// /**
	// * Kullback-Leibler divergence. The Kullback-Leibler divergence (also
	// * information divergence, information gain, relative entropy, or KLIC)
	// * is a non-symmetric measure of the difference between two probability
	// * distributions P and Q. KL measures the expected number of extra bits
	// * required to code samples from P when using a code based on Q, rather
	// * than using a code based on P. Typically P represents the "true"
	// * distribution of data, observations, or a precise calculated theoretical
	// * distribution. The measure Q typically represents a theory, model,
	// * description, or approximation of P.
	// * <p>
	// * Although it is often intuited as a distance metric, the KL divergence
	// is
	// * not a true metric - for example, the KL from P to Q is not necessarily
	// * the same as the KL from Q to P.
	// */
	// public static double KullbackLeiblerDivergence(SparseArray x, double[] y)
	// {
	// if (x.isEmpty()) {
	// throw new IllegalArgumentException("List x is empty.");
	// }
	//
	// Iterator<SparseArray.Entry> iter = x.iterator();
	//
	// boolean intersection = false;
	// double kl = 0.0;
	// while (iter.hasNext()) {
	// SparseArray.Entry b = iter.next();
	// int i = b.i;
	// if (y[i] > 0) {
	// intersection = true;
	// kl += b.x * Math.log(b.x / y[i]);
	// }
	// }
	//
	// if (intersection) {
	// return kl;
	// } else {
	// return Double.POSITIVE_INFINITY;
	// }
	// }

	/**
	 * Jensen-Shannon divergence JS(P||Q) = (KL(P||M) + KL(Q||M)) / 2, where M =
	 * (P+Q)/2. The Jensen-Shannon divergence is a popular method of measuring
	 * the similarity between two probability distributions. It is also known as
	 * information radius or total divergence to the average. It is based on the
	 * Kullback-Leibler divergence, with the difference that it is always a
	 * finite value. The square root of the Jensen-Shannon divergence is a
	 * metric.
	 */
	public static double JensenShannonDivergence(double[] x, double[] y) {
		double[] m = new double[x.length];
		for (int i = 0; i < m.length; i++) {
			m[i] = (x[i] + y[i]) / 2;
		}

		return (KullbackLeiblerDivergence(x, m) + KullbackLeiblerDivergence(y,
				m)) / 2;
	}

	/**
	 * Jensen-Shannon divergence JS(P||Q) = (KL(P||M) + KL(Q||M)) / 2, where M =
	 * (P+Q)/2. The Jensen-Shannon divergence is a popular method of measuring
	 * the similarity between two probability distributions. It is also known as
	 * information radius or total divergence to the average. It is based on the
	 * Kullback-Leibler divergence, with the difference that it is always a
	 * finite value. The square root of the Jensen-Shannon divergence is a
	 * metric.
	 */
	public static double JensenShannonDivergence(SparseArray x, SparseArray y) {
		if (x.isEmpty()) {
			throw new IllegalArgumentException("List x is empty.");
		}

		if (y.isEmpty()) {
			throw new IllegalArgumentException("List y is empty.");
		}

		Iterator<SparseArray.Entry> iterX = x.iterator();
		Iterator<SparseArray.Entry> iterY = y.iterator();

		SparseArray.Entry a = iterX.hasNext() ? iterX.next() : null;
		SparseArray.Entry b = iterY.hasNext() ? iterY.next() : null;

		double js = 0.0;

		while (a != null && b != null) {
			if (a.i < b.i) {
				double mi = a.x / 2;
				js += a.x * Math.log(a.x / mi);
				a = iterX.hasNext() ? iterX.next() : null;
			} else if (a.i > b.i) {
				double mi = b.x / 2;
				js += b.x * Math.log(b.x / mi);
				b = iterY.hasNext() ? iterY.next() : null;
			} else {
				double mi = (a.x + b.x) / 2;
				js += a.x * Math.log(a.x / mi) + b.x * Math.log(b.x / mi);

				a = iterX.hasNext() ? iterX.next() : null;
				b = iterY.hasNext() ? iterY.next() : null;
			}
		}

		return js / 2;
	}

	/**
	 * Jensen-Shannon divergence JS(P||Q) = (KL(P||M) + KL(Q||M)) / 2, where M =
	 * (P+Q)/2. The Jensen-Shannon divergence is a popular method of measuring
	 * the similarity between two probability distributions. It is also known as
	 * information radius or total divergence to the average. It is based on the
	 * Kullback-Leibler divergence, with the difference that it is always a
	 * finite value. The square root of the Jensen-Shannon divergence is a
	 * metric.
	 */
	public static double JensenShannonDivergence(double[] x, SparseArray y) {
		return JensenShannonDivergence(y, x);
	}

	/**
	 * Jensen-Shannon divergence JS(P||Q) = (KL(P||M) + KL(Q||M)) / 2, where M =
	 * (P+Q)/2. The Jensen-Shannon divergence is a popular method of measuring
	 * the similarity between two probability distributions. It is also known as
	 * information radius or total divergence to the average. It is based on the
	 * Kullback-Leibler divergence, with the difference that it is always a
	 * finite value. The square root of the Jensen-Shannon divergence is a
	 * metric.
	 */
	public static double JensenShannonDivergence(SparseArray x, double[] y) {
		if (x.isEmpty()) {
			throw new IllegalArgumentException("List x is empty.");
		}

		Iterator<SparseArray.Entry> iter = x.iterator();

		double js = 0.0;
		while (iter.hasNext()) {
			SparseArray.Entry b = iter.next();
			int i = b.i;
			double mi = (b.x + y[i]) / 2;
			js += b.x * Math.log(b.x / mi);
			if (y[i] > 0) {
				js += y[i] * Math.log(y[i] / mi);
			}
		}

		return js / 2;
	}

	/**
	 * Returns the dot product between two vectors.
	 */
	public static double dot(int[] x, int[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("Arrays have different length.");
		}

		double p = 0.0;
		for (int i = 0; i < x.length; i++) {
			p += x[i] * y[i];
		}

		return p;
	}

	/**
	 * Returns the dot product between two vectors.
	 */
	public static double dot(float[] x, float[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("Arrays have different length.");
		}

		double p = 0.0;
		for (int i = 0; i < x.length; i++) {
			p += x[i] * y[i];
		}

		return p;
	}

	/**
	 * Returns the dot product between two vectors.
	 */
	public static double dot(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("Arrays have different length.");
		}

		double p = 0.0;
		for (int i = 0; i < x.length; i++) {
			p += x[i] * y[i];
		}

		return p;
	}

	// /**
	// * Returns the dot product between two sparse arrays.
	// */
	// public static double dot(SparseArray x, SparseArray y) {
	// Iterator<SparseArray.Entry> it1 = x.iterator();
	// Iterator<SparseArray.Entry> it2 = y.iterator();
	// SparseArray.Entry e1 = it1.hasNext() ? it1.next() : null;
	// SparseArray.Entry e2 = it2.hasNext() ? it2.next() : null;
	//
	// double s = 0.0;
	// while (e1 != null && e2 != null) {
	// if (e1.i == e2.i) {
	// s += e1.x * e2.x;
	// e1 = it1.hasNext() ? it1.next() : null;
	// e2 = it2.hasNext() ? it2.next() : null;
	// } else if (e1.i > e2.i) {
	// e2 = it2.hasNext() ? it2.next() : null;
	// } else {
	// e1 = it1.hasNext() ? it1.next() : null;
	// }
	// }
	//
	// return s;
	// }

	/**
	 * Returns the covariance between two vectors.
	 */
	public static double cov(int[] x, int[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("Arrays have different length.");
		}

		if (x.length < 3) {
			throw new IllegalArgumentException(
					"array length has to be at least 3.");
		}

		double mx = mean(x);
		double my = mean(y);

		double Sxy = 0.0;
		for (int i = 0; i < x.length; i++) {
			double dx = x[i] - mx;
			double dy = y[i] - my;
			Sxy += dx * dy;
		}

		return Sxy / (x.length - 1);
	}

	/**
	 * Returns the covariance between two vectors.
	 */
	public static double cov(float[] x, float[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("Arrays have different length.");
		}

		if (x.length < 3) {
			throw new IllegalArgumentException(
					"array length has to be at least 3.");
		}

		double mx = mean(x);
		double my = mean(y);

		double Sxy = 0.0;
		for (int i = 0; i < x.length; i++) {
			double dx = x[i] - mx;
			double dy = y[i] - my;
			Sxy += dx * dy;
		}

		return Sxy / (x.length - 1);
	}

	/**
	 * Returns the covariance between two vectors.
	 */
	public static double cov(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("Arrays have different length.");
		}

		if (x.length < 3) {
			throw new IllegalArgumentException(
					"array length has to be at least 3.");
		}

		double mx = mean(x);
		double my = mean(y);

		double Sxy = 0.0;
		for (int i = 0; i < x.length; i++) {
			double dx = x[i] - mx;
			double dy = y[i] - my;
			Sxy += dx * dy;
		}

		return Sxy / (x.length - 1);
	}

	/**
	 * Returns the sample covariance matrix.
	 *
	 * @param mu
	 *            the known mean of data.
	 */
	public static double[][] cov(double[][] data, double[] mu) {
		double[][] sigma = new double[data[0].length][data[0].length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < mu.length; j++) {
				for (int k = 0; k <= j; k++) {
					sigma[j][k] += (data[i][j] - mu[j]) * (data[i][k] - mu[k]);
				}
			}
		}

		int n = data.length - 1;
		for (int j = 0; j < mu.length; j++) {
			for (int k = 0; k <= j; k++) {
				sigma[j][k] /= n;
				sigma[k][j] = sigma[j][k];
			}
		}

		return sigma;
	}

	/**
	 * Returns the correlation coefficient between two vectors.
	 */
	public static double cor(int[] x, int[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("Arrays have different length.");
		}

		if (x.length < 3) {
			throw new IllegalArgumentException(
					"array length has to be at least 3.");
		}

		double Sxy = cov(x, y);
		double Sxx = var(x);
		double Syy = var(y);

		if (Sxx == 0 || Syy == 0) {
			return Double.NaN;
		}

		return Sxy / Math.sqrt(Sxx * Syy);
	}

	/**
	 * Returns the correlation coefficient between two vectors.
	 */
	public static double cor(float[] x, float[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("Arrays have different length.");
		}

		if (x.length < 3) {
			throw new IllegalArgumentException(
					"array length has to be at least 3.");
		}

		double Sxy = cov(x, y);
		double Sxx = var(x);
		double Syy = var(y);

		if (Sxx == 0 || Syy == 0) {
			return Double.NaN;
		}

		return Sxy / Math.sqrt(Sxx * Syy);
	}

	/**
	 * Returns the correlation coefficient between two vectors.
	 */
	public static double cor(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("Arrays have different length.");
		}

		if (x.length < 3) {
			throw new IllegalArgumentException(
					"array length has to be at least 3.");
		}

		double Sxy = cov(x, y);
		double Sxx = var(x);
		double Syy = var(y);

		if (Sxx == 0 || Syy == 0) {
			return Double.NaN;
		}

		return Sxy / Math.sqrt(Sxx * Syy);
	}

	/**
	 * Returns the sample correlation matrix.
	 */
	public static double[][] cor(double[][] data) {
		return cor(data, colMean(data));
	}

	/**
	 * Returns the sample correlation matrix.
	 * 
	 * @param mu
	 *            the known mean of data.
	 */
	public static double[][] cor(double[][] data, double[] mu) {
		double[][] sigma = cov(data, mu);

		int n = data[0].length;
		double[] sd = new double[n];
		for (int i = 0; i < n; i++) {
			sd[i] = sqrt(sigma[i][i]);
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j <= i; j++) {
				sigma[i][j] /= sd[i] * sd[j];
				sigma[j][i] = sigma[i][j];
			}
		}

		return sigma;
	}

	/**
	 * Given a sorted array, replaces the elements by their rank, including
	 * midranking of ties, and returns as s the sum of f<sup>3</sup> - f, where
	 * f is the number of elements in each tie.
	 */
	private static double crank(double[] w) {
		int n = w.length;
		double s = 0.0;
		int j = 1;
		while (j < n) {
			if (w[j] != w[j - 1]) {
				w[j - 1] = j;
				++j;
			} else {
				int jt = j + 1;
				while (jt <= n && w[jt - 1] == w[j - 1]) {
					jt++;
				}

				double rank = 0.5 * (j + jt - 1);
				for (int ji = j; ji <= (jt - 1); ji++) {
					w[ji - 1] = rank;
				}

				double t = jt - j;
				s += (t * t * t - t);
				j = jt;
			}
		}

		if (j == n) {
			w[n - 1] = n;
		}

		return s;
	}

	/**
	 * The Spearman Rank Correlation Coefficient is a form of the Pearson
	 * coefficient with the data converted to rankings (ie. when variables are
	 * ordinal). It can be used when there is non-parametric data and hence
	 * Pearson cannot be used.
	 */
	public static double spearman(int[] x, int[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(
					"Input vector sizes are different.");
		}

		int n = x.length;
		double[] wksp1 = new double[n];
		double[] wksp2 = new double[n];
		for (int j = 0; j < n; j++) {
			wksp1[j] = x[j];
			wksp2[j] = y[j];
		}

		sort(wksp1, wksp2);
		double sf = crank(wksp1);
		sort(wksp2, wksp1);
		double sg = crank(wksp2);

		double d = 0.0;
		for (int j = 0; j < n; j++) {
			d += sqrt(wksp1[j] - wksp2[j]);
		}

		int en = n;
		double en3n = en * en * en - en;
		double fac = (1.0 - sf / en3n) * (1.0 - sg / en3n);
		double rs = (1.0 - (6.0 / en3n) * (d + (sf + sg) / 12.0))
				/ Math.sqrt(fac);
		return rs;
	}

	/**
	 * The Spearman Rank Correlation Coefficient is a form of the Pearson
	 * coefficient with the data converted to rankings (ie. when variables are
	 * ordinal). It can be used when there is non-parametric data and hence
	 * Pearson cannot be used.
	 */
	public static double spearman(float[] x, float[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(
					"Input vector sizes are different.");
		}

		int n = x.length;
		double[] wksp1 = new double[n];
		double[] wksp2 = new double[n];
		for (int j = 0; j < n; j++) {
			wksp1[j] = x[j];
			wksp2[j] = y[j];
		}

		sort(wksp1, wksp2);
		double sf = crank(wksp1);
		sort(wksp2, wksp1);
		double sg = crank(wksp2);

		double d = 0.0;
		for (int j = 0; j < n; j++) {
			d += sqr(wksp1[j] - wksp2[j]);
		}

		int en = n;
		double en3n = en * en * en - en;
		double fac = (1.0 - sf / en3n) * (1.0 - sg / en3n);
		double rs = (1.0 - (6.0 / en3n) * (d + (sf + sg) / 12.0))
				/ Math.sqrt(fac);
		return rs;
	}

	/**
	 * The Spearman Rank Correlation Coefficient is a form of the Pearson
	 * coefficient with the data converted to rankings (ie. when variables are
	 * ordinal). It can be used when there is non-parametric data and hence
	 * Pearson cannot be used.
	 */
	public static double spearman(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(
					"Input vector sizes are different.");
		}

		int n = x.length;
		double[] wksp1 = new double[n];
		double[] wksp2 = new double[n];
		for (int j = 0; j < n; j++) {
			wksp1[j] = x[j];
			wksp2[j] = y[j];
		}

		sort(wksp1, wksp2);
		double sf = crank(wksp1);
		sort(wksp2, wksp1);
		double sg = crank(wksp2);

		double d = 0.0;
		for (int j = 0; j < n; j++) {
			d += sqr(wksp1[j] - wksp2[j]);
		}

		int en = n;
		double en3n = en * en * en - en;
		double fac = (1.0 - sf / en3n) * (1.0 - sg / en3n);
		double rs = (1.0 - (6.0 / en3n) * (d + (sf + sg) / 12.0))
				/ Math.sqrt(fac);
		return rs;
	}

	/**
	 * The Kendall Tau Rank Correlation Coefficient is used to measure the
	 * degree of correspondence between sets of rankings where the measures are
	 * not equidistant. It is used with non-parametric data.
	 */
	public static double kendall(int[] x, int[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(
					"Input vector sizes are different.");
		}

		int is = 0, n2 = 0, n1 = 0, n = x.length;
		double aa, a2, a1;
		for (int j = 0; j < n - 1; j++) {
			for (int k = j + 1; k < n; k++) {
				a1 = x[j] - x[k];
				a2 = y[j] - y[k];
				aa = a1 * a2;
				if (aa != 0.0) {
					++n1;
					++n2;
					if (aa > 0) {
						++is;
					} else {
						--is;
					}

				} else {
					if (a1 != 0.0) {
						++n1;
					}
					if (a2 != 0.0) {
						++n2;
					}
				}
			}
		}

		double tau = is / (Math.sqrt(n1) * Math.sqrt(n2));
		return tau;
	}

	/**
	 * The Kendall Tau Rank Correlation Coefficient is used to measure the
	 * degree of correspondence between sets of rankings where the measures are
	 * not equidistant. It is used with non-parametric data.
	 */
	public static double kendall(float[] x, float[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(
					"Input vector sizes are different.");
		}

		int is = 0, n2 = 0, n1 = 0, n = x.length;
		double aa, a2, a1;
		for (int j = 0; j < n - 1; j++) {
			for (int k = j + 1; k < n; k++) {
				a1 = x[j] - x[k];
				a2 = y[j] - y[k];
				aa = a1 * a2;
				if (aa != 0.0) {
					++n1;
					++n2;
					if (aa > 0) {
						++is;
					} else {
						--is;
					}

				} else {
					if (a1 != 0.0) {
						++n1;
					}
					if (a2 != 0.0) {
						++n2;
					}
				}
			}
		}

		double tau = is / (Math.sqrt(n1) * Math.sqrt(n2));
		return tau;
	}

	/**
	 * The Kendall Tau Rank Correlation Coefficient is used to measure the
	 * degree of correspondence between sets of rankings where the measures are
	 * not equidistant. It is used with non-parametric data.
	 */
	public static double kendall(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(
					"Input vector sizes are different.");
		}

		int is = 0, n2 = 0, n1 = 0, n = x.length;
		double aa, a2, a1;
		for (int j = 0; j < n - 1; j++) {
			for (int k = j + 1; k < n; k++) {
				a1 = x[j] - x[k];
				a2 = y[j] - y[k];
				aa = a1 * a2;
				if (aa != 0.0) {
					++n1;
					++n2;
					if (aa > 0) {
						++is;
					} else {
						--is;
					}

				} else {
					if (a1 != 0.0) {
						++n1;
					}
					if (a2 != 0.0) {
						++n2;
					}
				}
			}
		}

		double tau = is / (Math.sqrt(n1) * Math.sqrt(n2));
		return tau;
	}

	/**
	 * L1 vector norm.
	 */
	public static double norm1(double[] x) {
		double norm = 0.0;

		for (double n : x) {
			norm += Math.abs(n);
		}

		return norm;
	}

	/**
	 * L2 vector norm.
	 */
	public static double norm2(double[] x) {
		double norm = 0.0;

		for (double n : x) {
			norm += n * n;
		}

		norm = Math.sqrt(norm);

		return norm;
	}

	/**
	 * L-infinity vector norm. Maximum absolute value.
	 */
	public static double normInf(double[] x) {
		int n = x.length;

		double f = Math.abs(x[0]);
		for (int i = 1; i < n; i++) {
			f = Math.max(f, Math.abs(x[i]));
		}

		return f;
	}

	/**
	 * L2 vector norm.
	 */
	public static double norm(double[] x) {
		return norm2(x);
	}

	/**
	 * L1 matrix norm. Maximum column sum.
	 */
	public static double norm1(double[][] x) {
		int m = x.length;
		int n = x[0].length;

		double f = 0.0;
		for (int j = 0; j < n; j++) {
			double s = 0.0;
			for (int i = 0; i < m; i++) {
				s += Math.abs(x[i][j]);
			}
			f = Math.max(f, s);
		}

		return f;
	}

	/**
	 * L2 matrix norm. Maximum singular value.
	 */
	public static double norm2(double[][] x) {
		return (new SingularValueDecomposition(new Matrix(x))).norm2();
	}

	/**
	 * L2 matrix norm. Maximum singular value.
	 */
	public static double norm(double[][] x) {
		return norm2(x);
	}

	/**
	 * Infinity matrix norm. Maximum row sum.
	 */
	public static double normInf(double[][] x) {
		int m = x.length;

		double f = 0.0;
		for (int i = 0; i < m; i++) {
			double s = norm1(x[i]);
			f = Math.max(f, s);
		}

		return f;
	}

	/**
	 * Frobenius matrix norm. Sqrt of sum of squares of all elements.
	 */
	public static double normFro(double[][] x) {
		int m = x.length;
		int n = x[0].length;

		double f = 0.0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				f = Math.hypot(f, x[i][j]);
			}
		}

		return f;
	}

	/**
	 * Normalizes each column of a matrix to mean 0 and variance 1.
	 */
	public static void normalize(double[][] x) {
		int n = x.length;
		int p = x[0].length;

		for (int j = 0; j < p; j++) {
			double mu = 0.0;
			double sd = 0.0;
			for (int i = 0; i < n; i++) {
				mu += x[i][j];
				sd += x[i][j] * x[i][j];
			}

			sd = Math.sqrt(sd / (n - 1) - (mu / n) * (mu / (n - 1)));
			mu /= n;

			if (sd <= 0) {
				throw new IllegalArgumentException(String.format(
						"Column %d has variance of 0.", j));
			}

			for (int i = 0; i < n; i++) {
				x[i][j] = (x[i][j] - mu) / sd;
			}
		}
	}

	/**
	 * Unitize an array so that L2 norm of x = 1.
	 *
	 * @param x
	 *            the array of double
	 */
	public static void unitize(double[] x) {
		unitize2(x);
	}

	/**
	 * Unitize an array so that L1 norm of x is 1.
	 *
	 * @param x
	 *            an array of nonnegative double
	 */
	public static void unitize1(double[] x) {
		double n = norm1(x);

		for (int i = 0; i < x.length; i++) {
			x[i] /= n;
		}
	}

	/**
	 * Unitize an array so that L2 norm of x = 1.
	 *
	 * @param x
	 *            the array of double
	 */
	public static void unitize2(double[] x) {
		double n = norm(x);

		for (int i = 0; i < x.length; i++) {
			x[i] /= n;
		}
	}

	/**
	 * Returns the fitted linear function value y = intercept + slope * log(x).
	 */
	private static double smoothed(int x, double slope, double intercept) {
		return Math.exp(intercept + slope * Math.log(x));
	}

	/**
	 * Returns the index of given frequency.
	 * 
	 * @param r
	 *            the frequency list.
	 * @param f
	 *            the given frequency.
	 * @return the index of given frequency or -1 if it doesn't exist in the
	 *         list.
	 */
	private static int row(int[] r, int f) {
		int i = 0;

		while (i < r.length && r[i] < f) {
			++i;
		}

		return ((i < r.length && r[i] == f) ? i : -1);
	}

	/**
	 * Takes a set of (frequency, frequency-of-frequency) pairs, and applies the
	 * "Simple Good-Turing" technique for estimating the probabilities
	 * corresponding to the observed frequencies, and P<sub>0</sub>, the joint
	 * probability of all unobserved species.
	 * 
	 * @param r
	 *            the frequency in ascending order.
	 * @param Nr
	 *            the frequency of frequencies.
	 * @param p
	 *            on output, it is the estimated probabilities.
	 * @return P<sub>0</sub> for all unobserved species.
	 */
	public static double GoodTuring(int[] r, int[] Nr, double[] p) {
		final double CONFID_FACTOR = 1.96;

		if (r.length != Nr.length) {
			throw new IllegalArgumentException(
					"The sizes of r and Nr are not same.");
		}

		int len = r.length;
		double[] logR = new double[len];
		double[] logZ = new double[len];
		double[] Z = new double[len];

		int N = 0;
		for (int j = 0; j < len; ++j) {
			N += r[j] * Nr[j];
		}

		int n1 = (r[0] != 1) ? 0 : Nr[0];
		double p0 = n1 / (double) N;

		for (int j = 0; j < len; ++j) {
			int q = j == 0 ? 0 : r[j - 1];
			int t = j == len - 1 ? 2 * r[j] - q : r[j + 1];
			Z[j] = 2.0 * Nr[j] / (t - q);
			logR[j] = Math.log(r[j]);
			logZ[j] = Math.log(Z[j]);
		}

		// Simple linear regression.
		double XYs = 0.0, Xsquares = 0.0, meanX = 0.0, meanY = 0.0;
		for (int i = 0; i < len; ++i) {
			meanX += logR[i];
			meanY += logZ[i];
		}
		meanX /= len;
		meanY /= len;
		for (int i = 0; i < len; ++i) {
			XYs += (logR[i] - meanX) * (logZ[i] - meanY);
			Xsquares += sqrt(logR[i] - meanX);
		}

		double slope = XYs / Xsquares;
		double intercept = meanY - slope * meanX;

		boolean indiffValsSeen = false;
		for (int j = 0; j < len; ++j) {
			double y = (r[j] + 1) * smoothed(r[j] + 1, slope, intercept)
					/ smoothed(r[j], slope, intercept);

			if (row(r, r[j] + 1) < 0) {
				indiffValsSeen = true;
			}

			if (!indiffValsSeen) {
				int n = Nr[row(r, r[j] + 1)];
				double x = (r[j] + 1) * n / (double) Nr[j];
				if (Math.abs(x - y) <= CONFID_FACTOR
						* Math.sqrt(sqr(r[j] + 1.0) * n / sqr(Nr[j])
								* (1 + n / (double) Nr[j]))) {
					indiffValsSeen = true;
				} else {
					p[j] = x;
				}
			}

			if (indiffValsSeen) {
				p[j] = y;
			}
		}

		double Nprime = 0.0;
		for (int j = 0; j < len; ++j) {
			Nprime += Nr[j] * p[j];
		}

		for (int j = 0; j < len; ++j) {
			p[j] = (1 - p0) * p[j] / Nprime;
		}

		return p0;
	}

	/**
	 * Check if x element-wisely equals y.
	 */
	public static boolean equals(int[] x, int[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		for (int i = 0; i < x.length; i++) {
			if (x[i] != y[i]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Check if x element-wisely equals y with default epsilon 1E-7.
	 */
	public static boolean equals(float[] x, float[] y) {
		return equals(x, y, 1.0E-7f);
	}

	/**
	 * Check if x element-wisely equals y.
	 */
	public static boolean equals(float[] x, float[] y, float epsilon) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		for (int i = 0; i < x.length; i++) {
			if (Math.abs(x[i] - y[i]) > epsilon) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Check if x element-wisely equals y with default epsilon 1E-10.
	 */
	public static boolean equals(double[] x, double[] y) {
		return equals(x, y, 1.0E-10);
	}

	/**
	 * Check if x element-wisely equals y.
	 */
	public static boolean equals(double[] x, double[] y, double eps) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		if (eps <= 0.0) {
			throw new IllegalArgumentException("Invalid epsilon: " + eps);
		}

		for (int i = 0; i < x.length; i++) {
			if (Math.abs(x[i] - y[i]) > eps) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Check if x element-wisely equals y.
	 */
	public static <T extends Comparable<? super T>> boolean equals(T[] x, T[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		for (int i = 0; i < x.length; i++) {
			if (x[i].compareTo(y[i]) != 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Check if x element-wisely equals y.
	 */
	public static boolean equals(int[][] x, int[][] y) {
		if (x.length != y.length || x[0].length != y[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrices have different rows: %d x %d vs %d x %d",
					x.length, x[0].length, y.length, y[0].length));
		}

		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[i].length; j++) {
				if (x[i][j] != y[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if x element-wisely equals y with default epsilon 1E-7.
	 */
	public static boolean equals(float[][] x, float[][] y) {
		return equals(x, y, 1.0E-7f);
	}

	/**
	 * Check if x element-wisely equals y.
	 */
	public static boolean equals(float[][] x, float[][] y, float epsilon) {
		if (x.length != y.length || x[0].length != y[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrices have different rows: %d x %d vs %d x %d",
					x.length, x[0].length, y.length, y[0].length));
		}

		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[i].length; j++) {
				if (Math.abs(x[i][j] - y[i][j]) > epsilon) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if x element-wisely equals y with default epsilon 1E-10.
	 */
	public static boolean equals(double[][] x, double[][] y) {
		return equals(x, y, 1.0E-10);
	}

	/**
	 * Check if x element-wisely equals y.
	 */
	public static boolean equals(double[][] x, double[][] y, double eps) {
		if (x.length != y.length || x[0].length != y[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrices have different rows: %d x %d vs %d x %d",
					x.length, x[0].length, y.length, y[0].length));
		}

		if (eps <= 0.0) {
			throw new IllegalArgumentException("Invalid epsilon: " + eps);
		}

		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[i].length; j++) {
				if (Math.abs(x[i][j] - y[i][j]) > eps) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if x element-wisely equals y.
	 */
	public static <T extends Comparable<? super T>> boolean equals(T[][] x,
			T[][] y) {
		if (x.length != y.length || x[0].length != y[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrices have different rows: %d x %d vs %d x %d",
					x.length, x[0].length, y.length, y[0].length));
		}

		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[i].length; j++) {
				if (x[i][j].compareTo(y[i][j]) != 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Deep clone a two-dimensional array.
	 */
	public static int[][] clone(int[][] x) {
		int[][] matrix = new int[x.length][];
		for (int i = 0; i < x.length; i++) {
			matrix[i] = x[i].clone();
		}

		return matrix;
	}

	/**
	 * Deep clone a two-dimensional array.
	 */
	public static float[][] clone(float[][] x) {
		float[][] matrix = new float[x.length][];
		for (int i = 0; i < x.length; i++) {
			matrix[i] = x[i].clone();
		}

		return matrix;
	}

	/**
	 * Deep clone a two-dimensional array.
	 */
	public static double[][] clone(double[][] x) {
		double[][] matrix = new double[x.length][];
		for (int i = 0; i < x.length; i++) {
			matrix[i] = x[i].clone();
		}

		return matrix;
	}

	/**
	 * Swap two elements of an array.
	 */
	public static void swap(int[] x, int i, int j) {
		int s = x[i];
		x[i] = x[j];
		x[j] = s;
	}

	/**
	 * Swap two elements of an array.
	 */
	public static void swap(float[] x, int i, int j) {
		float s = x[i];
		x[i] = x[j];
		x[j] = s;
	}

	/**
	 * Swap two elements of an array.
	 */
	public static void swap(double[] x, int i, int j) {
		double s = x[i];
		x[i] = x[j];
		x[j] = s;
	}

	/**
	 * Swap two elements of an array.
	 */
	public static void swap(Object[] x, int i, int j) {
		Object s = x[i];
		x[i] = x[j];
		x[j] = s;
	}

	/**
	 * Swap two arrays.
	 */
	public static void swap(int[] x, int[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		for (int i = 0; i < x.length; i++) {
			int s = x[i];
			x[i] = y[i];
			y[i] = s;
		}
	}

	/**
	 * Swap two arrays.
	 */
	public static void swap(float[] x, float[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		for (int i = 0; i < x.length; i++) {
			float s = x[i];
			x[i] = y[i];
			y[i] = s;
		}
	}

	/**
	 * Swap two arrays.
	 */
	public static void swap(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		for (int i = 0; i < x.length; i++) {
			double s = x[i];
			x[i] = y[i];
			y[i] = s;
		}
	}

	/**
	 * Swap two arrays.
	 */
	public static <E> void swap(E[] x, E[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		for (int i = 0; i < x.length; i++) {
			E s = x[i];
			x[i] = y[i];
			y[i] = s;
		}
	}

	/**
	 * Copy x into y.
	 */
	public static void copy(int[] x, int[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		System.arraycopy(x, 0, y, 0, x.length);
	}

	/**
	 * Copy x into y.
	 */
	public static void copy(float[] x, float[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		System.arraycopy(x, 0, y, 0, x.length);
	}

	/**
	 * Copy x into y.
	 */
	public static void copy(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		System.arraycopy(x, 0, y, 0, x.length);
	}

	/**
	 * Copy x into y.
	 */
	public static void copy(int[][] x, int[][] y) {
		if (x.length != y.length || x[0].length != y[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrices have different rows: %d x %d vs %d x %d",
					x.length, x[0].length, y.length, y[0].length));
		}

		for (int i = 0; i < x.length; i++) {
			System.arraycopy(x[i], 0, y[i], 0, x[i].length);
		}
	}

	/**
	 * Copy x into y.
	 */
	public static void copy(float[][] x, float[][] y) {
		if (x.length != y.length || x[0].length != y[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrices have different rows: %d x %d vs %d x %d",
					x.length, x[0].length, y.length, y[0].length));
		}

		for (int i = 0; i < x.length; i++) {
			System.arraycopy(x[i], 0, y[i], 0, x[i].length);
		}
	}

	/**
	 * Copy x into y.
	 */
	public static void copy(double[][] x, double[][] y) {
		if (x.length != y.length || x[0].length != y[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrices have different rows: %d x %d vs %d x %d",
					x.length, x[0].length, y.length, y[0].length));
		}

		for (int i = 0; i < x.length; i++) {
			System.arraycopy(x[i], 0, y[i], 0, x[i].length);
		}
	}

	/**
	 * Element-wise sum of two arrays y = x + y.
	 */
	public static void plus(double[] y, double[] x) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		for (int i = 0; i < x.length; i++) {
			y[i] += x[i];
		}
	}

	/**
	 * Scale each element of an array by a constant x = a * x.
	 */
	public static void scale(double a, double[] x) {
		for (int i = 0; i < x.length; i++) {
			x[i] *= a;
		}
	}

	/**
	 * Scale each element of an array by a constant y = a * x.
	 */
	public static void scale(double a, double[] x, double[] y) {
		for (int i = 0; i < x.length; i++) {
			y[i] = a * x[i];
		}
	}

	/**
	 * Update an array by adding a multiple of another array y = a * x + y.
	 */
	public static void axpy(double a, double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException(String.format(
					"Arrays have different length: x[%d], y[%d]", x.length,
					y.length));
		}

		for (int i = 0; i < x.length; i++) {
			y[i] += a * x[i];
		}
	}

	/**
	 * Product of a matrix and a vector y = A * x according to the rules of
	 * linear algebra. Number of columns in A must equal number of elements in
	 * x.
	 */
	public static void ax(double[][] A, double[] x, double[] y) {
		if (A[0].length != x.length) {
			throw new IllegalArgumentException(
					String.format(
							"Array dimensions do not match for matrix multiplication: %dx%d vs %dx1",
							A.length, A[0].length, x.length));
		}

		if (A.length != y.length) {
			throw new IllegalArgumentException(
					String.format("Array dimensions do not match"));
		}

		Arrays.fill(y, 0.0);
		for (int i = 0; i < y.length; i++) {
			for (int k = 0; k < A[i].length; k++) {
				y[i] += A[i][k] * x[k];
			}
		}
	}

	/**
	 * Product of a matrix and a vector y = A * x + y according to the rules of
	 * linear algebra. Number of columns in A must equal number of elements in
	 * x.
	 */
	public static void axpy(double[][] A, double[] x, double[] y) {
		if (A[0].length != x.length) {
			throw new IllegalArgumentException(
					String.format(
							"Array dimensions do not match for matrix multiplication: %dx%d vs %dx1",
							A.length, A[0].length, x.length));
		}

		if (A.length != y.length) {
			throw new IllegalArgumentException(
					String.format("Array dimensions do not match"));
		}

		for (int i = 0; i < y.length; i++) {
			for (int k = 0; k < A[i].length; k++) {
				y[i] += A[i][k] * x[k];
			}
		}
	}

	/**
	 * Product of a matrix and a vector y = A * x + b * y according to the rules
	 * of linear algebra. Number of columns in A must equal number of elements
	 * in x.
	 */
	public static void axpy(double[][] A, double[] x, double[] y, double b) {
		if (A[0].length != x.length) {
			throw new IllegalArgumentException(
					String.format(
							"Array dimensions do not match for matrix multiplication: %dx%d vs %dx1",
							A.length, A[0].length, x.length));
		}

		if (A.length != y.length) {
			throw new IllegalArgumentException(
					String.format("Array dimensions do not match"));
		}

		for (int i = 0; i < y.length; i++) {
			y[i] *= b;
			for (int k = 0; k < A[i].length; k++) {
				y[i] += A[i][k] * x[k];
			}
		}
	}

	/**
	 * Product of a matrix and a vector y = A<sup>T</sup> * x according to the
	 * rules of linear algebra. Number of elements in x must equal number of
	 * rows in A.
	 */
	public static void atx(double[][] A, double[] x, double[] y) {
		if (A.length != x.length) {
			throw new IllegalArgumentException(
					String.format(
							"Array dimensions do not match for matrix multiplication: %d x %d vs 1 x %d",
							A.length, A[0].length, x.length));
		}

		if (A[0].length != y.length) {
			throw new IllegalArgumentException(
					String.format("Array dimensions do not match"));
		}

		Arrays.fill(y, 0.0);
		for (int i = 0; i < y.length; i++) {
			for (int k = 0; k < x.length; k++) {
				y[i] += x[k] * A[k][i];
			}
		}
	}

	/**
	 * Product of a matrix and a vector y = A<sup>T</sup> * x + y according to
	 * the rules of linear algebra. Number of elements in x must equal number of
	 * rows in A.
	 */
	public static void atxpy(double[][] A, double[] x, double[] y) {
		if (A.length != x.length) {
			throw new IllegalArgumentException(
					String.format(
							"Array dimensions do not match for matrix multiplication: 1 x %d vs %d x %d",
							x.length, A.length, A[0].length));
		}

		if (A[0].length != y.length) {
			throw new IllegalArgumentException(
					String.format("Array dimensions do not match"));
		}

		for (int i = 0; i < y.length; i++) {
			for (int k = 0; k < x.length; k++) {
				y[i] += x[k] * A[k][i];
			}
		}
	}

	/**
	 * Product of a matrix and a vector y = A<sup>T</sup> * x + b * y according
	 * to the rules of linear algebra. Number of elements in x must equal number
	 * of rows in A.
	 */
	public static void atxpy(double[][] A, double[] x, double[] y, double b) {
		if (A.length != x.length) {
			throw new IllegalArgumentException(
					String.format(
							"Array dimensions do not match for matrix multiplication: 1 x %d vs %d x %d",
							x.length, A.length, A[0].length));
		}

		if (A[0].length != y.length) {
			throw new IllegalArgumentException(
					String.format("Array dimensions do not match"));
		}

		for (int i = 0; i < y.length; i++) {
			y[i] *= b;
			for (int k = 0; k < x.length; k++) {
				y[i] += x[k] * A[k][i];
			}
		}
	}

	/**
	 * Returns x' * A * x.
	 */
	public static double xax(double[][] A, double[] x) {
		if (A.length != A[0].length) {
			throw new IllegalArgumentException("The matrix is not square");
		}

		if (A.length != x.length) {
			throw new IllegalArgumentException(String.format(
					"x' * A * x: 1 x %d vs %d x %d", x.length, A.length,
					A[0].length));
		}

		int n = A.length;
		double s = 0.0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				s += A[i][j] * x[i] * x[j];
			}
		}

		return s;
	}

	/**
	 * Matrix multiplication A * A' according to the rules of linear algebra.
	 */
	public static double[][] aatmm(double[][] A) {
		int m = A.length;
		int n = A[0].length;
		double[][] C = new double[m][m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				for (int k = 0; k < n; k++) {
					C[i][j] += A[i][k] * A[j][k];
				}
			}
		}

		return C;
	}

	/**
	 * Matrix multiplication C = A * A' according to the rules of linear
	 * algebra.
	 */
	public static void aatmm(double[][] A, double[][] C) {
		int m = A.length;
		int n = A[0].length;

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				for (int k = 0; k < n; k++) {
					C[i][j] += A[i][k] * A[j][k];
				}
			}
		}
	}

	/**
	 * Matrix multiplication A' * A according to the rules of linear algebra.
	 */
	public static double[][] atamm(double[][] A) {
		int m = A.length;
		int n = A[0].length;

		double[][] C = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < m; k++) {
					C[i][j] += A[k][i] * A[k][j];
				}
			}
		}

		return C;
	}

	/**
	 * Matrix multiplication C = A' * A according to the rules of linear
	 * algebra.
	 */
	public static void atamm(double[][] A, double[][] C) {
		int m = A.length;
		int n = A[0].length;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < m; k++) {
					C[i][j] += A[k][i] * A[k][j];
				}
			}
		}
	}

	/**
	 * Matrix multiplication A * B according to the rules of linear algebra.
	 */
	public static double[][] abmm(double[][] A, double[][] B) {
		if (A[0].length != B.length) {
			throw new IllegalArgumentException(String.format(
					"Matrix multiplication A * B: %d x %d vs %d x %d",
					A.length, A[0].length, B.length, B[0].length));
		}

		int m = A.length;
		int n = B[0].length;
		int l = B.length;
		double[][] C = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < l; k++) {
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}

		return C;
	}

	/**
	 * Matrix multiplication C = A * B according to the rules of linear algebra.
	 */
	public static void abmm(double[][] A, double[][] B, double[][] C) {
		if (A[0].length != B.length) {
			throw new IllegalArgumentException(String.format(
					"Matrix multiplication A * B: %d x %d vs %d x %d",
					A.length, A[0].length, B.length, B[0].length));
		}

		int m = A.length;
		int n = B[0].length;
		int l = B.length;

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < l; k++) {
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}
	}

	/**
	 * Matrix multiplication A' * B according to the rules of linear algebra.
	 */
	public static double[][] atbmm(double[][] A, double[][] B) {
		if (A.length != B.length) {
			throw new IllegalArgumentException(String.format(
					"Matrix multiplication A' * B: %d x %d vs %d x %d",
					A.length, A[0].length, B.length, B[0].length));
		}

		int m = A[0].length;
		int n = B[0].length;
		int l = B.length;
		double[][] C = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < l; k++) {
					C[i][j] += A[k][i] * B[k][j];
				}
			}
		}

		return C;
	}

	/**
	 * Matrix multiplication C = A' * B according to the rules of linear
	 * algebra.
	 */
	public static void atbmm(double[][] A, double[][] B, double[][] C) {
		if (A.length != B.length) {
			throw new IllegalArgumentException(String.format(
					"Matrix multiplication A' * B: %d x %d vs %d x %d",
					A.length, A[0].length, B.length, B[0].length));
		}

		int m = A[0].length;
		int n = B[0].length;
		int l = B.length;

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < l; k++) {
					C[i][j] += A[k][i] * B[k][j];
				}
			}
		}
	}

	/**
	 * Matrix multiplication A * B' according to the rules of linear algebra.
	 */
	public static double[][] abtmm(double[][] A, double[][] B) {
		if (A[0].length != B[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrix multiplication A * B': %d x %d vs %d x %d",
					A.length, A[0].length, B.length, B[0].length));
		}

		int m = A.length;
		int n = B.length;
		int l = B[0].length;
		double[][] C = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < l; k++) {
					C[i][j] += A[i][k] * B[j][k];
				}
			}
		}

		return C;
	}

	/**
	 * Matrix multiplication C = A * B' according to the rules of linear
	 * algebra.
	 */
	public static void abtmm(double[][] A, double[][] B, double[][] C) {
		if (A[0].length != B[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrix multiplication A * B': %d x %d vs %d x %d",
					A.length, A[0].length, B.length, B[0].length));
		}

		int m = A.length;
		int n = B.length;
		int l = B[0].length;

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < l; k++) {
					C[i][j] += A[i][k] * B[j][k];
				}
			}
		}
	}

	/**
	 * Matrix multiplication A' * B' according to the rules of linear algebra.
	 */
	public static double[][] atbtmm(double[][] A, double[][] B) {
		if (A.length != B[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrix multiplication A' * B': %d x %d vs %d x %d",
					A.length, A[0].length, B.length, B[0].length));
		}

		int m = A[0].length;
		int n = B.length;
		int l = A.length;
		double[][] C = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < l; k++) {
					C[i][j] += A[k][i] * B[j][k];
				}
			}
		}

		return C;
	}

	/**
	 * Matrix multiplication C = A' * B' according to the rules of linear
	 * algebra.
	 */
	public static void atbtmm(double[][] A, double[][] B, double[][] C) {
		if (A.length != B[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrix multiplication A' * B': %d x %d vs %d x %d",
					A.length, A[0].length, B.length, B[0].length));
		}

		int m = A[0].length;
		int n = B.length;
		int l = A.length;

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < l; k++) {
					C[i][j] += A[k][i] * B[j][k];
				}
			}
		}
	}

	/**
	 * Raise each element of an array to a scalar power.
	 * 
	 * @param x
	 *            array
	 * @param n
	 *            scalar exponent
	 * @return x<sup>n</sup>
	 */
	public static double[] pow(double[] x, double n) {
		double[] array = new double[x.length];
		for (int i = 0; i < x.length; i++) {
			array[i] = Math.pow(x[i], n);
		}
		return array;
	}

	/**
	 * Raise each element of a matrix to a scalar power.
	 * 
	 * @param x
	 *            matrix
	 * @param n
	 *            exponent
	 * @return x<sup>n</sup>
	 */
	public static double[][] pow(double[][] x, double n) {
		double[][] array = new double[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[i].length; j++) {
				array[i][j] = Math.pow(x[i][j], n);
			}
		}
		return array;
	}

	/**
	 * Find unique elements of vector.
	 * 
	 * @param x
	 *            an integer array.
	 * @return the same values as in x but with no repetitions.
	 */
	public static int[] unique(int[] x) {
		HashSet<Integer> hash = new HashSet<Integer>();
		for (int i = 0; i < x.length; i++) {
			hash.add(x[i]);
		}

		int[] y = new int[hash.size()];

		Iterator<Integer> keys = hash.iterator();
		for (int i = 0; i < y.length; i++) {
			y[i] = keys.next();
		}

		return y;
	}

	/**
	 * Find unique elements of vector.
	 * 
	 * @param x
	 *            an array of strings.
	 * @return the same values as in x but with no repetitions.
	 */
	public static String[] unique(String[] x) {
		HashSet<String> hash = new HashSet<String>(Arrays.asList(x));

		String[] y = new String[hash.size()];

		Iterator<String> keys = hash.iterator();
		for (int i = 0; i < y.length; i++) {
			y[i] = keys.next();
		}

		return y;
	}

	/**
	 * Sorts each variable and returns the index of values in ascending order.
	 * Note that the order of original array is NOT altered.
	 * 
	 * @param x
	 *            a set of variables to be sorted. Each row is an instance. Each
	 *            column is a variable.
	 * @return the index of values in ascending order
	 */
	public static int[][] sort(double[][] x) {
		int n = x.length;
		int p = x[0].length;

		double[] a = new double[n];
		int[][] index = new int[p][];

		for (int j = 0; j < p; j++) {
			for (int i = 0; i < n; i++) {
				a[i] = x[i][j];
			}
			index[j] = sort(a);
		}

		return index;
	}

	/**
	 * Returns a square identity matrix of size n.
	 * 
	 * @return An n-by-n matrix with ones on the diagonal and zeros elsewhere.
	 */
	public static double[][] eye(int n) {
		double[][] x = new double[n][n];
		for (int i = 0; i < n; i++) {
			x[i][i] = 1.0;
		}
		return x;
	}

	/**
	 * Returns an identity matrix of size m by n.
	 * 
	 * @param m
	 *            the number of rows.
	 * @param n
	 *            the number of columns.
	 * @return an m-by-n matrix with ones on the diagonal and zeros elsewhere.
	 */
	public static double[][] eye(int m, int n) {
		double[][] x = new double[m][n];
		int k = Math.min(m, n);
		for (int i = 0; i < k; i++) {
			x[i][i] = 1.0;
		}
		return x;
	}

	/**
	 * Returns the matrix trace. The sum of the diagonal elements.
	 */
	public static double trace(double[][] A) {
		int n = Math.min(A.length, A[0].length);

		double t = 0.0;
		for (int i = 0; i < n; i++) {
			t += A[i][i];
		}

		return t;
	}

	/**
	 * Returns the matrix transpose.
	 */
	public static double[][] transpose(double[][] A) {
		int m = A.length;
		int n = A[0].length;

		double[][] matrix = new double[n][m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrix[j][i] = A[i][j];
			}
		}

		return matrix;
	}

	// /**
	// * Solve the tridiagonal linear set which is of diagonal dominance
	// * |b<sub>i</sub>| &gt; |a<sub>i</sub>| + |c<sub>i</sub>|.
	// * <pre>
	// * | b0 c0 0 0 0 ... |
	// * | a1 b1 c1 0 0 ... |
	// * | 0 a2 b2 c2 0 ... |
	// * | ... |
	// * | ... a(n-2) b(n-2) c(n-2) |
	// * | ... 0 a(n-1) b(n-1) |
	// * </pre>
	// * @param a the lower part of tridiagonal matrix. a[0] is undefined and
	// not
	// * referenced by the method.
	// * @param b the diagonal of tridiagonal matrix.
	// * @param c the upper of tridiagonal matrix. c[n-1] is undefined and not
	// * referenced by the method.
	// * @param r the right-hand side of linear equations.
	// * @return the solution.
	// */
	// public static double[] solve(double[] a, double[] b, double[] c, double[]
	// r) {
	// if (b[0] == 0.0) {
	// throw new
	// IllegalArgumentException("Invalid value of b[0] == 0. The equations should be rewritten as a set of order n - 1.");
	// }
	//
	// int n = a.length;
	// double[] u = new double[n];
	// double[] gam = new double[n];
	//
	// double bet = b[0];
	// u[0] = r[0] / bet;
	//
	// for (int j = 1; j < n; j++) {
	// gam[j] = c[j - 1] / bet;
	// bet = b[j] - a[j] * gam[j];
	// if (bet == 0.0) {
	// throw new
	// IllegalArgumentException("The tridagonal matrix is not of diagonal dominance.");
	// }
	// u[j] = (r[j] - a[j] * u[j - 1]) / bet;
	// }
	//
	// for (int j = (n - 2); j >= 0; j--) {
	// u[j] -= gam[j + 1] * u[j + 1];
	// }
	//
	// return u;
	// }

	private static final int M = 7;
	private static final int NSTACK = 64;

	/**
	 * Sorts the specified array into ascending numerical order.
	 * 
	 * @return the original index of elements after sorting in range [0, n).
	 */
	public static int[] sort(int[] arr) {
		int[] order = new int[arr.length];
		for (int i = 0; i < order.length; i++) {
			order[i] = i;
		}
		sort(arr, order);
		return order;
	}

	/**
	 * Besides sorting the array arr, the array brr will be also rearranged as
	 * the same order of arr.
	 */
	public static void sort(int[] arr, int[] brr) {
		sort(arr, brr, arr.length);
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the first n elements of array arr, the first n
	 * elements of array brr will be also rearranged as the same order of arr.
	 */
	public static void sort(int[] arr, int[] brr, int n) {
		int jstack = -1;
		int l = 0;
		int[] istack = new int[NSTACK];
		int ir = n - 1;

		int i, j, k, a, b;
		for (;;) {
			if (ir - l < M) {
				for (j = l + 1; j <= ir; j++) {
					a = arr[j];
					b = brr[j];
					for (i = j - 1; i >= l; i--) {
						if (arr[i] <= a) {
							break;
						}
						arr[i + 1] = arr[i];
						brr[i + 1] = brr[i];
					}
					arr[i + 1] = a;
					brr[i + 1] = b;
				}
				if (jstack < 0) {
					break;
				}
				ir = istack[jstack--];
				l = istack[jstack--];
			} else {
				k = (l + ir) >> 1;
				swap(arr, k, l + 1);
				swap(brr, k, l + 1);
				if (arr[l] > arr[ir]) {
					swap(arr, l, ir);
					swap(brr, l, ir);
				}
				if (arr[l + 1] > arr[ir]) {
					swap(arr, l + 1, ir);
					swap(brr, l + 1, ir);
				}
				if (arr[l] > arr[l + 1]) {
					swap(arr, l, l + 1);
					swap(brr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				b = brr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i] < a);
					do {
						j--;
					} while (arr[j] > a);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
					swap(brr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				brr[l + 1] = brr[j];
				brr[j] = b;
				jstack += 2;

				if (jstack >= NSTACK) {
					throw new IllegalStateException("NSTACK too small in sort.");
				}

				if (ir - i + 1 >= j - l) {
					istack[jstack] = ir;
					istack[jstack - 1] = i;
					ir = j - 1;
				} else {
					istack[jstack] = j - 1;
					istack[jstack - 1] = l;
					l = i;
				}
			}
		}
	}

	/**
	 * Besides sorting the array arr, the array brr will be also rearranged as
	 * the same order of arr.
	 */
	public static void sort(int[] arr, Object[] brr) {
		sort(arr, brr, arr.length);
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the first n elements of array arr, the first n
	 * elements of array brr will be also rearranged as the same order of arr.
	 */
	public static void sort(int[] arr, Object[] brr, int n) {
		int jstack = -1;
		int l = 0;
		int[] istack = new int[NSTACK];
		int ir = n - 1;

		int i, j, k, a;
		Object b;
		for (;;) {
			if (ir - l < M) {
				for (j = l + 1; j <= ir; j++) {
					a = arr[j];
					b = brr[j];
					for (i = j - 1; i >= l; i--) {
						if (arr[i] <= a) {
							break;
						}
						arr[i + 1] = arr[i];
						brr[i + 1] = brr[i];
					}
					arr[i + 1] = a;
					brr[i + 1] = b;
				}
				if (jstack < 0) {
					break;
				}
				ir = istack[jstack--];
				l = istack[jstack--];
			} else {
				k = (l + ir) >> 1;
				swap(arr, k, l + 1);
				swap(brr, k, l + 1);
				if (arr[l] > arr[ir]) {
					swap(arr, l, ir);
					swap(brr, l, ir);
				}
				if (arr[l + 1] > arr[ir]) {
					swap(arr, l + 1, ir);
					swap(brr, l + 1, ir);
				}
				if (arr[l] > arr[l + 1]) {
					swap(arr, l, l + 1);
					swap(brr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				b = brr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i] < a);
					do {
						j--;
					} while (arr[j] > a);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
					swap(brr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				brr[l + 1] = brr[j];
				brr[j] = b;
				jstack += 2;

				if (jstack >= NSTACK) {
					throw new IllegalStateException("NSTACK too small in sort.");
				}

				if (ir - i + 1 >= j - l) {
					istack[jstack] = ir;
					istack[jstack - 1] = i;
					ir = j - 1;
				} else {
					istack[jstack] = j - 1;
					istack[jstack - 1] = l;
					l = i;
				}
			}
		}
	}

	/**
	 * Sorts the specified array into ascending numerical order.
	 * 
	 * @return the original index of elements after sorting in range [0, n).
	 */
	public static int[] sort(float[] arr) {
		int[] order = new int[arr.length];
		for (int i = 0; i < order.length; i++) {
			order[i] = i;
		}
		sort(arr, order);
		return order;
	}

	/**
	 * Besides sorting the array arr, the array brr will be also rearranged as
	 * the same order of arr.
	 */
	public static void sort(float[] arr, int[] brr) {
		sort(arr, brr, arr.length);
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the first n elements of array arr, the first n
	 * elements of array brr will be also rearranged as the same order of arr.
	 */
	public static void sort(float[] arr, int[] brr, int n) {
		int jstack = -1;
		int l = 0;
		int[] istack = new int[NSTACK];
		int ir = n - 1;

		int i, j, k;
		float a;
		int b;
		for (;;) {
			if (ir - l < M) {
				for (j = l + 1; j <= ir; j++) {
					a = arr[j];
					b = brr[j];
					for (i = j - 1; i >= l; i--) {
						if (arr[i] <= a) {
							break;
						}
						arr[i + 1] = arr[i];
						brr[i + 1] = brr[i];
					}
					arr[i + 1] = a;
					brr[i + 1] = b;
				}
				if (jstack < 0) {
					break;
				}
				ir = istack[jstack--];
				l = istack[jstack--];
			} else {
				k = (l + ir) >> 1;
				swap(arr, k, l + 1);
				swap(brr, k, l + 1);
				if (arr[l] > arr[ir]) {
					swap(arr, l, ir);
					swap(brr, l, ir);
				}
				if (arr[l + 1] > arr[ir]) {
					swap(arr, l + 1, ir);
					swap(brr, l + 1, ir);
				}
				if (arr[l] > arr[l + 1]) {
					swap(arr, l, l + 1);
					swap(brr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				b = brr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i] < a);
					do {
						j--;
					} while (arr[j] > a);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
					swap(brr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				brr[l + 1] = brr[j];
				brr[j] = b;
				jstack += 2;

				if (jstack >= NSTACK) {
					throw new IllegalStateException("NSTACK too small in sort.");
				}

				if (ir - i + 1 >= j - l) {
					istack[jstack] = ir;
					istack[jstack - 1] = i;
					ir = j - 1;
				} else {
					istack[jstack] = j - 1;
					istack[jstack - 1] = l;
					l = i;
				}
			}
		}
	}

	/**
	 * Besides sorting the array arr, the array brr will be also rearranged as
	 * the same order of arr.
	 */
	public static void sort(float[] arr, float[] brr) {
		sort(arr, brr, arr.length);
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the first n elements of array arr, the first n
	 * elements of array brr will be also rearranged as the same order of arr.
	 */
	public static void sort(float[] arr, float[] brr, int n) {
		int jstack = -1;
		int l = 0;
		int[] istack = new int[NSTACK];
		int ir = n - 1;

		int i, j, k;
		float a, b;
		for (;;) {
			if (ir - l < M) {
				for (j = l + 1; j <= ir; j++) {
					a = arr[j];
					b = brr[j];
					for (i = j - 1; i >= l; i--) {
						if (arr[i] <= a) {
							break;
						}
						arr[i + 1] = arr[i];
						brr[i + 1] = brr[i];
					}
					arr[i + 1] = a;
					brr[i + 1] = b;
				}
				if (jstack < 0) {
					break;
				}
				ir = istack[jstack--];
				l = istack[jstack--];
			} else {
				k = (l + ir) >> 1;
				swap(arr, k, l + 1);
				swap(brr, k, l + 1);
				if (arr[l] > arr[ir]) {
					swap(arr, l, ir);
					swap(brr, l, ir);
				}
				if (arr[l + 1] > arr[ir]) {
					swap(arr, l + 1, ir);
					swap(brr, l + 1, ir);
				}
				if (arr[l] > arr[l + 1]) {
					swap(arr, l, l + 1);
					swap(brr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				b = brr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i] < a);
					do {
						j--;
					} while (arr[j] > a);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
					swap(brr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				brr[l + 1] = brr[j];
				brr[j] = b;
				jstack += 2;

				if (jstack >= NSTACK) {
					throw new IllegalStateException("NSTACK too small in sort.");
				}

				if (ir - i + 1 >= j - l) {
					istack[jstack] = ir;
					istack[jstack - 1] = i;
					ir = j - 1;
				} else {
					istack[jstack] = j - 1;
					istack[jstack - 1] = l;
					l = i;
				}
			}
		}
	}

	/**
	 * Besides sorting the array arr, the array brr will be also rearranged as
	 * the same order of arr.
	 */
	public static void sort(float[] arr, Object[] brr) {
		sort(arr, brr, arr.length);
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the first n elements of array arr, the first n
	 * elements of array brr will be also rearranged as the same order of arr.
	 */
	public static void sort(float[] arr, Object[] brr, int n) {
		int jstack = -1;
		int l = 0;
		int[] istack = new int[NSTACK];
		int ir = n - 1;

		int i, j, k;
		float a;
		Object b;
		for (;;) {
			if (ir - l < M) {
				for (j = l + 1; j <= ir; j++) {
					a = arr[j];
					b = brr[j];
					for (i = j - 1; i >= l; i--) {
						if (arr[i] <= a) {
							break;
						}
						arr[i + 1] = arr[i];
						brr[i + 1] = brr[i];
					}
					arr[i + 1] = a;
					brr[i + 1] = b;
				}
				if (jstack < 0) {
					break;
				}
				ir = istack[jstack--];
				l = istack[jstack--];
			} else {
				k = (l + ir) >> 1;
				swap(arr, k, l + 1);
				swap(brr, k, l + 1);
				if (arr[l] > arr[ir]) {
					swap(arr, l, ir);
					swap(brr, l, ir);
				}
				if (arr[l + 1] > arr[ir]) {
					swap(arr, l + 1, ir);
					swap(brr, l + 1, ir);
				}
				if (arr[l] > arr[l + 1]) {
					swap(arr, l, l + 1);
					swap(brr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				b = brr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i] < a);
					do {
						j--;
					} while (arr[j] > a);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
					swap(brr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				brr[l + 1] = brr[j];
				brr[j] = b;
				jstack += 2;

				if (jstack >= NSTACK) {
					throw new IllegalStateException("NSTACK too small in sort.");
				}

				if (ir - i + 1 >= j - l) {
					istack[jstack] = ir;
					istack[jstack - 1] = i;
					ir = j - 1;
				} else {
					istack[jstack] = j - 1;
					istack[jstack - 1] = l;
					l = i;
				}
			}
		}
	}

	/**
	 * Sorts the specified array into ascending numerical order.
	 * 
	 * @return the original index of elements after sorting in range [0, n).
	 */
	public static int[] sort(double[] arr) {
		int[] order = new int[arr.length];
		for (int i = 0; i < order.length; i++) {
			order[i] = i;
		}
		sort(arr, order);
		return order;
	}

	/**
	 * Besides sorting the array arr, the array brr will be also rearranged as
	 * the same order of arr.
	 */
	public static void sort(double[] arr, int[] brr) {
		sort(arr, brr, arr.length);
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the first n elements of array arr, the first n
	 * elements of array brr will be also rearranged as the same order of arr.
	 */
	public static void sort(double[] arr, int[] brr, int n) {
		int jstack = -1;
		int l = 0;
		int[] istack = new int[NSTACK];
		int ir = n - 1;

		int i, j, k;
		double a;
		int b;
		for (;;) {
			if (ir - l < M) {
				for (j = l + 1; j <= ir; j++) {
					a = arr[j];
					b = brr[j];
					for (i = j - 1; i >= l; i--) {
						if (arr[i] <= a) {
							break;
						}
						arr[i + 1] = arr[i];
						brr[i + 1] = brr[i];
					}
					arr[i + 1] = a;
					brr[i + 1] = b;
				}
				if (jstack < 0) {
					break;
				}
				ir = istack[jstack--];
				l = istack[jstack--];
			} else {
				k = (l + ir) >> 1;
				swap(arr, k, l + 1);
				swap(brr, k, l + 1);
				if (arr[l] > arr[ir]) {
					swap(arr, l, ir);
					swap(brr, l, ir);
				}
				if (arr[l + 1] > arr[ir]) {
					swap(arr, l + 1, ir);
					swap(brr, l + 1, ir);
				}
				if (arr[l] > arr[l + 1]) {
					swap(arr, l, l + 1);
					swap(brr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				b = brr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i] < a);
					do {
						j--;
					} while (arr[j] > a);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
					swap(brr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				brr[l + 1] = brr[j];
				brr[j] = b;
				jstack += 2;

				if (jstack >= NSTACK) {
					throw new IllegalStateException("NSTACK too small in sort.");
				}

				if (ir - i + 1 >= j - l) {
					istack[jstack] = ir;
					istack[jstack - 1] = i;
					ir = j - 1;
				} else {
					istack[jstack] = j - 1;
					istack[jstack - 1] = l;
					l = i;
				}
			}
		}
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the array arr, the array brr will be also
	 * rearranged as the same order of arr.
	 */
	public static void sort(double[] arr, double[] brr) {
		sort(arr, brr, arr.length);
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the first n elements of array arr, the first n
	 * elements of array brr will be also rearranged as the same order of arr.
	 */
	public static void sort(double[] arr, double[] brr, int n) {
		int jstack = -1;
		int l = 0;
		int[] istack = new int[NSTACK];
		int ir = n - 1;

		int i, j, k;
		double a, b;
		for (;;) {
			if (ir - l < M) {
				for (j = l + 1; j <= ir; j++) {
					a = arr[j];
					b = brr[j];
					for (i = j - 1; i >= l; i--) {
						if (arr[i] <= a) {
							break;
						}
						arr[i + 1] = arr[i];
						brr[i + 1] = brr[i];
					}
					arr[i + 1] = a;
					brr[i + 1] = b;
				}
				if (jstack < 0) {
					break;
				}
				ir = istack[jstack--];
				l = istack[jstack--];
			} else {
				k = (l + ir) >> 1;
				swap(arr, k, l + 1);
				swap(brr, k, l + 1);
				if (arr[l] > arr[ir]) {
					swap(arr, l, ir);
					swap(brr, l, ir);
				}
				if (arr[l + 1] > arr[ir]) {
					swap(arr, l + 1, ir);
					swap(brr, l + 1, ir);
				}
				if (arr[l] > arr[l + 1]) {
					swap(arr, l, l + 1);
					swap(brr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				b = brr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i] < a);
					do {
						j--;
					} while (arr[j] > a);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
					swap(brr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				brr[l + 1] = brr[j];
				brr[j] = b;
				jstack += 2;

				if (jstack >= NSTACK) {
					throw new IllegalStateException("NSTACK too small in sort.");
				}

				if (ir - i + 1 >= j - l) {
					istack[jstack] = ir;
					istack[jstack - 1] = i;
					ir = j - 1;
				} else {
					istack[jstack] = j - 1;
					istack[jstack - 1] = l;
					l = i;
				}
			}
		}
	}

	/**
	 * Besides sorting the array arr, the array brr will be also rearranged as
	 * the same order of arr.
	 */
	public static void sort(double[] arr, Object[] brr) {
		sort(arr, brr, arr.length);
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the first n elements of array arr, the first n
	 * elements of array brr will be also rearranged as the same order of arr.
	 */
	public static void sort(double[] arr, Object[] brr, int n) {
		int jstack = -1;
		int l = 0;
		int[] istack = new int[NSTACK];
		int ir = n - 1;

		int i, j, k;
		double a;
		Object b;
		for (;;) {
			if (ir - l < M) {
				for (j = l + 1; j <= ir; j++) {
					a = arr[j];
					b = brr[j];
					for (i = j - 1; i >= l; i--) {
						if (arr[i] <= a) {
							break;
						}
						arr[i + 1] = arr[i];
						brr[i + 1] = brr[i];
					}
					arr[i + 1] = a;
					brr[i + 1] = b;
				}
				if (jstack < 0) {
					break;
				}
				ir = istack[jstack--];
				l = istack[jstack--];
			} else {
				k = (l + ir) >> 1;
				swap(arr, k, l + 1);
				swap(brr, k, l + 1);
				if (arr[l] > arr[ir]) {
					swap(arr, l, ir);
					swap(brr, l, ir);
				}
				if (arr[l + 1] > arr[ir]) {
					swap(arr, l + 1, ir);
					swap(brr, l + 1, ir);
				}
				if (arr[l] > arr[l + 1]) {
					swap(arr, l, l + 1);
					swap(brr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				b = brr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i] < a);
					do {
						j--;
					} while (arr[j] > a);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
					swap(brr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				brr[l + 1] = brr[j];
				brr[j] = b;
				jstack += 2;

				if (jstack >= NSTACK) {
					throw new IllegalStateException("NSTACK too small in sort.");
				}

				if (ir - i + 1 >= j - l) {
					istack[jstack] = ir;
					istack[jstack - 1] = i;
					ir = j - 1;
				} else {
					istack[jstack] = j - 1;
					istack[jstack - 1] = l;
					l = i;
				}
			}
		}
	}

	/**
	 * Sorts the specified array into ascending order.
	 * 
	 * @return the original index of elements after sorting in range [0, n).
	 */
	public static <T extends Comparable<? super T>> int[] sort(T[] arr) {
		int[] order = new int[arr.length];
		for (int i = 0; i < order.length; i++) {
			order[i] = i;
		}
		sort(arr, order);
		return order;
	}

	/**
	 * Besides sorting the array arr, the array brr will be also rearranged as
	 * the same order of arr.
	 */
	public static <T extends Comparable<? super T>> void sort(T[] arr, int[] brr) {
		sort(arr, brr, arr.length);
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the first n elements of array arr, the first n
	 * elements of array brr will be also rearranged as the same order of arr.
	 */
	public static <T extends Comparable<? super T>> void sort(T[] arr,
			int[] brr, int n) {
		int jstack = -1;
		int l = 0;
		int[] istack = new int[NSTACK];
		int ir = n - 1;

		int i, j, k;
		T a;
		int b;
		for (;;) {
			if (ir - l < M) {
				for (j = l + 1; j <= ir; j++) {
					a = arr[j];
					b = brr[j];
					for (i = j - 1; i >= l; i--) {
						if (arr[i].compareTo(a) <= 0) {
							break;
						}
						arr[i + 1] = arr[i];
						brr[i + 1] = brr[i];
					}
					arr[i + 1] = a;
					brr[i + 1] = b;
				}
				if (jstack < 0) {
					break;
				}
				ir = istack[jstack--];
				l = istack[jstack--];
			} else {
				k = (l + ir) >> 1;
				swap(arr, k, l + 1);
				swap(brr, k, l + 1);
				if (arr[l].compareTo(arr[ir]) > 0) {
					swap(arr, l, ir);
					swap(brr, l, ir);
				}
				if (arr[l + 1].compareTo(arr[ir]) > 0) {
					swap(arr, l + 1, ir);
					swap(brr, l + 1, ir);
				}
				if (arr[l].compareTo(arr[l + 1]) > 0) {
					swap(arr, l, l + 1);
					swap(brr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				b = brr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i].compareTo(a) < 0);
					do {
						j--;
					} while (arr[j].compareTo(a) > 0);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
					swap(brr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				brr[l + 1] = brr[j];
				brr[j] = b;
				jstack += 2;

				if (jstack >= NSTACK) {
					throw new IllegalStateException("NSTACK too small in sort.");
				}

				if (ir - i + 1 >= j - l) {
					istack[jstack] = ir;
					istack[jstack - 1] = i;
					ir = j - 1;
				} else {
					istack[jstack] = j - 1;
					istack[jstack - 1] = l;
					l = i;
				}
			}
		}
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the first n elements of array arr, the first n
	 * elements of array brr will be also rearranged as the same order of arr.
	 */
	public static <T> void sort(T[] arr, int[] brr, int n,
			Comparator<T> comparator) {
		int jstack = -1;
		int l = 0;
		int[] istack = new int[NSTACK];
		int ir = n - 1;

		int i, j, k;
		T a;
		int b;
		for (;;) {
			if (ir - l < M) {
				for (j = l + 1; j <= ir; j++) {
					a = arr[j];
					b = brr[j];
					for (i = j - 1; i >= l; i--) {
						if (comparator.compare(arr[i], a) <= 0) {
							break;
						}
						arr[i + 1] = arr[i];
						brr[i + 1] = brr[i];
					}
					arr[i + 1] = a;
					brr[i + 1] = b;
				}
				if (jstack < 0) {
					break;
				}
				ir = istack[jstack--];
				l = istack[jstack--];
			} else {
				k = (l + ir) >> 1;
				swap(arr, k, l + 1);
				swap(brr, k, l + 1);
				if (comparator.compare(arr[l], arr[ir]) > 0) {
					swap(arr, l, ir);
					swap(brr, l, ir);
				}
				if (comparator.compare(arr[l + 1], arr[ir]) > 0) {
					swap(arr, l + 1, ir);
					swap(brr, l + 1, ir);
				}
				if (comparator.compare(arr[l], arr[l + 1]) > 0) {
					swap(arr, l, l + 1);
					swap(brr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				b = brr[l + 1];
				for (;;) {
					do {
						i++;
					} while (comparator.compare(arr[i], a) < 0);
					do {
						j--;
					} while (comparator.compare(arr[j], a) > 0);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
					swap(brr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				brr[l + 1] = brr[j];
				brr[j] = b;
				jstack += 2;

				if (jstack >= NSTACK) {
					throw new IllegalStateException("NSTACK too small in sort.");
				}

				if (ir - i + 1 >= j - l) {
					istack[jstack] = ir;
					istack[jstack - 1] = i;
					ir = j - 1;
				} else {
					istack[jstack] = j - 1;
					istack[jstack - 1] = l;
					l = i;
				}
			}
		}
	}

	/**
	 * Besides sorting the array arr, the array brr will be also rearranged as
	 * the same order of arr.
	 */
	public static <T extends Comparable<? super T>> void sort(T[] arr,
			Object[] brr) {
		sort(arr, brr, arr.length);
	}

	/**
	 * This is an effecient implementation Quick Sort algorithm without
	 * recursive. Besides sorting the first n elements of array arr, the first n
	 * elements of array brr will be also rearranged as the same order of arr.
	 */
	public static <T extends Comparable<? super T>> void sort(T[] arr,
			Object[] brr, int n) {
		int jstack = -1;
		int l = 0;
		int[] istack = new int[NSTACK];
		int ir = n - 1;

		int i, j, k;
		T a;
		Object b;
		for (;;) {
			if (ir - l < M) {
				for (j = l + 1; j <= ir; j++) {
					a = arr[j];
					b = brr[j];
					for (i = j - 1; i >= l; i--) {
						if (arr[i].compareTo(a) <= 0) {
							break;
						}
						arr[i + 1] = arr[i];
						brr[i + 1] = brr[i];
					}
					arr[i + 1] = a;
					brr[i + 1] = b;
				}
				if (jstack < 0) {
					break;
				}
				ir = istack[jstack--];
				l = istack[jstack--];
			} else {
				k = (l + ir) >> 1;
				swap(arr, k, l + 1);
				swap(brr, k, l + 1);
				if (arr[l].compareTo(arr[ir]) > 0) {
					swap(arr, l, ir);
					swap(brr, l, ir);
				}
				if (arr[l + 1].compareTo(arr[ir]) > 0) {
					swap(arr, l + 1, ir);
					swap(brr, l + 1, ir);
				}
				if (arr[l].compareTo(arr[l + 1]) > 0) {
					swap(arr, l, l + 1);
					swap(brr, l, l + 1);
				}
				i = l + 1;
				j = ir;
				a = arr[l + 1];
				b = brr[l + 1];
				for (;;) {
					do {
						i++;
					} while (arr[i].compareTo(a) < 0);
					do {
						j--;
					} while (arr[j].compareTo(a) > 0);
					if (j < i) {
						break;
					}
					swap(arr, i, j);
					swap(brr, i, j);
				}
				arr[l + 1] = arr[j];
				arr[j] = a;
				brr[l + 1] = brr[j];
				brr[j] = b;
				jstack += 2;

				if (jstack >= NSTACK) {
					throw new IllegalStateException("NSTACK too small in sort.");
				}

				if (ir - i + 1 >= j - l) {
					istack[jstack] = ir;
					istack[jstack - 1] = i;
					ir = j - 1;
				} else {
					istack[jstack] = j - 1;
					istack[jstack - 1] = l;
					l = i;
				}
			}
		}
	}

	/**
	 * Returns the root of a function known to lie between x1 and x2 by Brent's
	 * method. The root will be refined until its accuracy is tol. The method is
	 * guaranteed to converge as long as the function can be evaluated within
	 * the initial interval known to contain a root.
	 * 
	 * @param func
	 *            the function to be evaluated.
	 * @param x1
	 *            the left end of search interval.
	 * @param x2
	 *            the right end of search interval.
	 * @param tol
	 *            the accuracy tolerance.
	 * @return the root.
	 */
	public static double root(Function func, double x1, double x2, double tol) {
		return root(func, x1, x2, tol, 100);
	}

	/**
	 * Returns the root of a function known to lie between x1 and x2 by Brent's
	 * method. The root will be refined until its accuracy is tol. The method is
	 * guaranteed to converge as long as the function can be evaluated within
	 * the initial interval known to contain a root.
	 * 
	 * @param func
	 *            the function to be evaluated.
	 * @param x1
	 *            the left end of search interval.
	 * @param x2
	 *            the right end of search interval.
	 * @param tol
	 *            the accuracy tolerance.
	 * @param maxIter
	 *            the maximum number of allowed iterations.
	 * @return the root.
	 */
	public static double root(Function func, double x1, double x2, double tol,
			int maxIter) {
		if (tol <= 0.0) {
			throw new IllegalArgumentException("Invalid tolerance: " + tol);
		}

		if (maxIter <= 0) {
			throw new IllegalArgumentException(
					"Invalid maximum number of iterations: " + maxIter);
		}

		double a = x1, b = x2, c = x2, d = 0, e = 0, fa = func.f(a), fb = func
				.f(b), fc, p, q, r, s, xm;
		if ((fa > 0.0 && fb > 0.0) || (fa < 0.0 && fb < 0.0)) {
			throw new IllegalArgumentException("Root must be bracketed.");
		}

		fc = fb;
		for (int iter = 1; iter <= maxIter; iter++) {
			if ((fb > 0.0 && fc > 0.0) || (fb < 0.0 && fc < 0.0)) {
				c = a;
				fc = fa;
				e = d = b - a;
			}

			if (abs(fc) < abs(fb)) {
				a = b;
				b = c;
				c = a;
				fa = fb;
				fb = fc;
				fc = fa;
			}

			tol = 2.0 * EPSILON * abs(b) + 0.5 * tol;
			xm = 0.5 * (c - b);

			if (iter % 10 == 0) {
				System.out
						.format("Brent: the root after %3d iterations: %.5g, error = %.5g\n",
								iter, b, xm);
			}

			if (abs(xm) <= tol || fb == 0.0) {
				System.out
						.format("Brent: the root after %3d iterations: %.5g, error = %.5g\n",
								iter, b, xm);
				return b;
			}

			if (abs(e) >= tol && abs(fa) > abs(fb)) {
				s = fb / fa;
				if (a == c) {
					p = 2.0 * xm * s;
					q = 1.0 - s;
				} else {
					q = fa / fc;
					r = fb / fc;
					p = s * (2.0 * xm * q * (q - r) - (b - a) * (r - 1.0));
					q = (q - 1.0) * (r - 1.0) * (s - 1.0);
				}

				if (p > 0.0) {
					q = -q;
				}

				p = abs(p);
				double min1 = 3.0 * xm * q - abs(tol * q);
				double min2 = abs(e * q);
				if (2.0 * p < (min1 < min2 ? min1 : min2)) {
					e = d;
					d = p / q;
				} else {
					d = xm;
					e = d;
				}
			} else {
				d = xm;
				e = d;
			}

			a = b;
			fa = fb;
			if (abs(d) > tol) {
				b += d;
			} else {
				b += copySign(tol, xm);
			}
			fb = func.f(b);
		}

		System.err
				.println("Brent's method exceeded the maximum number of iterations.");
		return b;
	}

	/**
	 * Returns the root of a function whose derivative is available known to lie
	 * between x1 and x2 by Newton-Raphson method. The root will be refined
	 * until its accuracy is within xacc.
	 * 
	 * @param func
	 *            the function to be evaluated.
	 * @param x1
	 *            the left end of search interval.
	 * @param x2
	 *            the right end of search interval.
	 * @param tol
	 *            the accuracy tolerance.
	 * @return the root.
	 */
	public static double root(DifferentiableFunction func, double x1,
			double x2, double tol) {
		return root(func, x1, x2, tol, 100);
	}

	/**
	 * Returns the root of a function whose derivative is available known to lie
	 * between x1 and x2 by Newton-Raphson method. The root will be refined
	 * until its accuracy is within xacc.
	 * 
	 * @param func
	 *            the function to be evaluated.
	 * @param x1
	 *            the left end of search interval.
	 * @param x2
	 *            the right end of search interval.
	 * @param tol
	 *            the accuracy tolerance.
	 * @param maxIter
	 *            the maximum number of allowed iterations.
	 * @return the root.
	 */
	public static double root(DifferentiableFunction func, double x1,
			double x2, double tol, int maxIter) {
		if (tol <= 0.0) {
			throw new IllegalArgumentException("Invalid tolerance: " + tol);
		}

		if (maxIter <= 0) {
			throw new IllegalArgumentException(
					"Invalid maximum number of iterations: " + maxIter);
		}

		double fl = func.f(x1);
		double fh = func.f(x2);
		if ((fl > 0.0 && fh > 0.0) || (fl < 0.0 && fh < 0.0)) {
			throw new IllegalArgumentException(
					"Root must be bracketed in rtsafe");
		}

		if (fl == 0.0) {
			return x1;
		}
		if (fh == 0.0) {
			return x2;
		}

		double xh, xl;
		if (fl < 0.0) {
			xl = x1;
			xh = x2;
		} else {
			xh = x1;
			xl = x2;
		}
		double rts = 0.5 * (x1 + x2);
		double dxold = abs(x2 - x1);
		double dx = dxold;
		double f = func.f(rts);
		double df = func.df(rts);
		for (int iter = 1; iter <= maxIter; iter++) {
			if ((((rts - xh) * df - f) * ((rts - xl) * df - f) > 0.0)
					|| (abs(2.0 * f) > abs(dxold * df))) {
				dxold = dx;
				dx = 0.5 * (xh - xl);
				rts = xl + dx;
				if (xl == rts) {
					System.out
							.format("Newton-Raphson: the root after %3d iterations: %.5g, error = %.5g\n",
									iter, rts, dx);
					return rts;
				}
			} else {
				dxold = dx;
				dx = f / df;
				double temp = rts;
				rts -= dx;
				if (temp == rts) {
					System.out
							.format("Newton-Raphson: the root after %3d iterations: %.5g, error = %.5g\n",
									iter, rts, dx);
					return rts;
				}
			}

			if (iter % 10 == 0) {
				System.out
						.format("Newton-Raphson: the root after %3d iterations: %.5g, error = %.5g\n",
								iter, rts, dx);
			}

			if (abs(dx) < tol) {
				System.out
						.format("Newton-Raphson: the root after %3d iterations: %.5g, error = %.5g\n",
								iter, rts, dx);
				return rts;
			}

			f = func.f(rts);
			df = func.df(rts);
			if (f < 0.0) {
				xl = rts;
			} else {
				xh = rts;
			}
		}

		System.err
				.println("Newton-Raphson method exceeded the maximum number of iterations.");
		return rts;
	}

	/**
	 * Minimize a function along a search direction by find a step which
	 * satisfies a sufficient decrease condition and a curvature condition.
	 * <p>
	 * At each stage this function updates an interval of uncertainty with
	 * endpoints <code>stx</code> and <code>sty</code>. The interval of
	 * uncertainty is initially chosen so that it contains a minimizer of the
	 * modified function
	 * 
	 * <pre>
	 *      f(x+stp*s) - f(x) - ftol*stp*(gradf(x)'s).
	 * </pre>
	 * 
	 * If a step is obtained for which the modified function has a nonpositive
	 * function value and nonnegative derivative, then the interval of
	 * uncertainty is chosen so that it contains a minimizer of
	 * <code>f(x+stp*s)</code>.
	 * <p>
	 * The algorithm is designed to find a step which satisfies the sufficient
	 * decrease condition
	 * 
	 * <pre>
	 *       f(x+stp*s) &lt;= f(X) + ftol*stp*(gradf(x)'s),
	 * </pre>
	 * 
	 * and the curvature condition
	 * 
	 * <pre>
	 *       abs(gradf(x+stp*s)'s)) &lt;= gtol*abs(gradf(x)'s).
	 * </pre>
	 * 
	 * If <code>ftol</code> is less than <code>gtol</code> and if, for example,
	 * the function is bounded below, then there is always a step which
	 * satisfies both conditions. If no step can be found which satisfies both
	 * conditions, then the algorithm usually stops when rounding errors prevent
	 * further progress. In this case <code>stp</code> only satisfies the
	 * sufficient decrease condition.
	 * <p>
	 *
	 * @param xold
	 *            on input this contains the base point for the line search.
	 *
	 * @param fold
	 *            on input this contains the value of the objective function at
	 *            <code>x</code>.
	 *
	 * @param g
	 *            on input this contains the gradient of the objective function
	 *            at <code>x</code>.
	 *
	 * @param p
	 *            the search direction.
	 *
	 * @param x
	 *            on output, it contains <code>xold + alam*p</code>.
	 *
	 * @param stpmax
	 *            specify upper bound for the step in the line search so that we
	 *            do not try to evaluate the function in regions where it is
	 *            undefined or subject to overflow.
	 *
	 * @return the new function value.
	 */
	static double linesearch(MultivariateFunction func, double[] xold,
			double fold, double[] g, double[] p, double[] x, double stpmax) {
		if (stpmax <= 0) {
			throw new IllegalArgumentException(
					"Invalid upper bound of linear search step: " + stpmax);
		}

		// Termination occurs when the relative width of the interval
		// of uncertainty is at most xtol.
		final double xtol = EPSILON;
		// Tolerance for the sufficient decrease condition.
		final double ftol = 1.0E-4;

		int n = xold.length;

		// Scale if attempted step is too big
		double pnorm = norm(p);
		if (pnorm > stpmax) {
			double r = stpmax / pnorm;
			for (int i = 0; i < n; i++) {
				p[i] *= r;
			}
		}

		// Check if s is a descent direction.
		double slope = 0.0;
		for (int i = 0; i < n; i++) {
			slope += g[i] * p[i];
		}

		if (slope >= 0) {
			throw new IllegalArgumentException(
					"Line Search: the search direction is not a descent direction, which may be caused by roundoff problem.");
		}

		// Calculate minimum step.
		double test = 0.0;
		for (int i = 0; i < n; i++) {
			double temp = abs(p[i]) / max(xold[i], 1.0);
			if (temp > test) {
				test = temp;
			}
		}

		double alammin = xtol / test;
		double alam = 1.0;

		double alam2 = 0.0, f2 = 0.0;
		double a, b, disc, rhs1, rhs2, tmpalam;
		while (true) {
			// Evaluate the function and gradient at stp
			// and compute the directional derivative.
			for (int i = 0; i < n; i++) {
				x[i] = xold[i] + alam * p[i];
			}

			double f = func.f(x);

			// Convergence on &Delta; x.
			if (alam < alammin) {
				System.arraycopy(xold, 0, x, 0, n);
				return f;
			} else if (f <= fold + ftol * alam * slope) {
				// Sufficient function decrease.
				return f;
			} else {
				// Backtrack
				if (alam == 1.0) {
					// First time
					tmpalam = -slope / (2.0 * (f - fold - slope));
				} else {
					// Subsequent backtracks.
					rhs1 = f - fold - alam * slope;
					rhs2 = f2 - fold - alam2 * slope;
					a = (rhs1 / (alam * alam) - rhs2 / (alam2 * alam2))
							/ (alam - alam2);
					b = (-alam2 * rhs1 / (alam * alam) + alam * rhs2
							/ (alam2 * alam2))
							/ (alam - alam2);
					if (a == 0.0) {
						tmpalam = -slope / (2.0 * b);
					} else {
						disc = b * b - 3.0 * a * slope;
						if (disc < 0.0) {
							tmpalam = 0.5 * alam;
						} else if (b <= 0.0) {
							tmpalam = (-b + sqrt(disc)) / (3.0 * a);
						} else {
							tmpalam = -slope / (b + sqrt(disc));
						}
					}
					if (tmpalam > 0.5 * alam) {
						tmpalam = 0.5 * alam;
					}
				}
			}
			alam2 = alam;
			f2 = f;
			alam = max(tmpalam, 0.1 * alam);
		}
	}

	/**
	 * This method solves the unconstrained minimization problem
	 * 
	 * <pre>
	 *     min f(x),    x = (x1,x2,...,x_n),
	 * </pre>
	 * 
	 * using the limited-memory BFGS method. The method is especially effective
	 * on problems involving a large number of variables. In a typical iteration
	 * of this method an approximation <code>Hk</code> to the inverse of the
	 * Hessian is obtained by applying <code>m</code> BFGS updates to a diagonal
	 * matrix <code>Hk0</code>, using information from the previous M steps. The
	 * user specifies the number <code>m</code>, which determines the amount of
	 * storage required by the routine. The algorithm is described in "On the
	 * limited memory BFGS method for large scale optimization", by D. Liu and
	 * J. Nocedal, Mathematical Programming B 45 (1989) 503-528.
	 *
	 * @param func
	 *            the function to be minimized.
	 *
	 * @param m
	 *            the number of corrections used in the L-BFGS update. Values of
	 *            <code>m</code> less than 3 are not recommended; large values
	 *            of <code>m</code> will result in excessive computing time.
	 *            <code>3 &lt;= m &lt;= 7</code> is recommended.
	 *
	 * @param x
	 *            on initial entry this must be set by the user to the values of
	 *            the initial estimate of the solution vector. On exit with
	 *            <code>iflag = 0</code>, it contains the values of the
	 *            variables at the best point found (usually a solution).
	 *
	 * @param gtol
	 *            the convergence requirement on zeroing the gradient.
	 *
	 * @return the minimum value of the function.
	 */
	public static double min(DifferentiableMultivariateFunction func, int m,
			double[] x, double gtol) {
		return min(func, m, x, gtol, 200);
	}

	/**
	 * This method solves the unconstrained minimization problem
	 * 
	 * <pre>
	 *     min f(x),    x = (x1,x2,...,x_n),
	 * </pre>
	 * 
	 * using the limited-memory BFGS method. The method is especially effective
	 * on problems involving a large number of variables. In a typical iteration
	 * of this method an approximation <code>Hk</code> to the inverse of the
	 * Hessian is obtained by applying <code>m</code> BFGS updates to a diagonal
	 * matrix <code>Hk0</code>, using information from the previous M steps. The
	 * user specifies the number <code>m</code>, which determines the amount of
	 * storage required by the routine. The algorithm is described in "On the
	 * limited memory BFGS method for large scale optimization", by D. Liu and
	 * J. Nocedal, Mathematical Programming B 45 (1989) 503-528.
	 *
	 * @param func
	 *            the function to be minimized.
	 *
	 * @param m
	 *            the number of corrections used in the L-BFGS update. Values of
	 *            <code>m</code> less than 3 are not recommended; large values
	 *            of <code>m</code> will result in excessive computing time.
	 *            <code>3 &lt;= m &lt;= 7</code> is recommended. A common choice
	 *            for m is m = 5.
	 *
	 * @param x
	 *            on initial entry this must be set by the user to the values of
	 *            the initial estimate of the solution vector. On exit with
	 *            <code>iflag = 0</code>, it contains the values of the
	 *            variables at the best point found (usually a solution).
	 *
	 * @param gtol
	 *            the convergence requirement on zeroing the gradient.
	 *
	 * @param maxIter
	 *            the maximum allowed number of iterations.
	 *
	 * @return the minimum value of the function.
	 */
	public static double min(DifferentiableMultivariateFunction func, int m,
			double[] x, double gtol, int maxIter) {
		// Initialize.
		if (m <= 0) {
			throw new IllegalArgumentException("Invalid m: " + m);
		}

		// The convergence criterion on x values.
		final double TOLX = 4 * EPSILON;
		// The scaled maximum step length allowed in line searches.
		final double STPMX = 100.0;

		int n = x.length;

		// The solution vector of line search.
		double[] xnew = new double[n];
		// The gradient vector of line search.
		double[] gnew = new double[n];
		// Line search direction.
		double[] xi = new double[n];

		// difference of x from previous step.
		double[][] s = new double[m][n];
		// difference of g from previous step.
		double[][] y = new double[m][n];

		// buffer for 1 / (y' * s)
		double[] rho = new double[m];
		double[] a = new double[m];

		// Diagonal of initial H0.
		double diag = 1.0;

		// Current gradient.
		double[] g = new double[n];
		// Current function value.
		double f = func.f(x, g);

		System.out.format("L-BFGS: initial function value: %.5g\n", f);

		double sum = 0.0;
		// Initial line search direction.
		for (int i = 0; i < n; i++) {
			xi[i] = -g[i];
			sum += x[i] * x[i];
		}

		// Upper limit for line search step.
		double stpmax = STPMX * max(sqrt(sum), n);

		for (int iter = 1, k = 0; iter <= maxIter; iter++) {
			linesearch(func, x, f, g, xi, xnew, stpmax);

			f = func.f(xnew, gnew);
			for (int i = 0; i < n; i++) {
				s[k][i] = xnew[i] - x[i];
				y[k][i] = gnew[i] - g[i];
				x[i] = xnew[i];
				g[i] = gnew[i];
			}

			// Test for convergence on x.
			double test = 0.0;
			for (int i = 0; i < n; i++) {
				double temp = abs(s[k][i]) / max(abs(x[i]), 1.0);
				if (temp > test) {
					test = temp;
				}
			}

			if (test < TOLX) {
				System.out
						.format("L-BFGS: the function value after %3d iterations: %.5g\n",
								iter, f);
				return f;
			}

			// Test for convergence on zero gradient.
			test = 0.0;
			double den = max(f, 1.0);

			for (int i = 0; i < n; i++) {
				double temp = abs(g[i]) * max(abs(x[i]), 1.0) / den;
				if (temp > test) {
					test = temp;
				}
			}

			if (test < gtol) {
				System.out
						.format("L-BFGS: the function value after %3d iterations: %.5g\n",
								iter, f);
				return f;
			}

			if (iter % 10 == 0) {
				System.out
						.format("L-BFGS: the function value after %3d iterations: %.5g\n",
								iter, f);
			}

			double ys = dot(y[k], s[k]);
			double yy = dot(y[k], y[k]);

			diag = ys / yy;

			rho[k] = 1.0 / ys;

			for (int i = 0; i < n; i++) {
				xi[i] = -g[i];
			}

			int cp = k;
			int bound = iter > m ? m : iter;
			for (int i = 0; i < bound; i++) {
				a[cp] = rho[cp] * dot(s[cp], xi);
				axpy(-a[cp], y[cp], xi);
				if (--cp == -1) {
					cp = m - 1;
				}
			}

			for (int i = 0; i < n; i++) {
				xi[i] *= diag;
			}

			for (int i = 0; i < bound; i++) {
				if (++cp == m) {
					cp = 0;
				}
				double b = rho[cp] * dot(y[cp], xi);
				axpy(a[cp] - b, s[cp], xi);
			}

			if (++k == m) {
				k = 0;
			}
		}

		throw new IllegalStateException("L-BFGS: Too many iterations.");
	}

	/**
	 * This method solves the unconstrained minimization problem
	 * 
	 * <pre>
	 *     min f(x),    x = (x1,x2,...,x_n),
	 * </pre>
	 * 
	 * using the BFGS method.
	 *
	 * @param func
	 *            the function to be minimized.
	 *
	 * @param x
	 *            on initial entry this must be set by the user to the values of
	 *            the initial estimate of the solution vector. On exit, it
	 *            contains the values of the variables at the best point found
	 *            (usually a solution).
	 *
	 * @param gtol
	 *            the convergence requirement on zeroing the gradient.
	 *
	 * @return the minimum value of the function.
	 */
	public static double min(DifferentiableMultivariateFunction func,
			double[] x, double gtol) {
		return min(func, x, gtol, 200);
	}

	/**
	 * This method solves the unconstrained minimization problem
	 * 
	 * <pre>
	 *     min f(x),    x = (x1,x2,...,x_n),
	 * </pre>
	 * 
	 * using the BFGS method.
	 *
	 * @param func
	 *            the function to be minimized.
	 *
	 * @param x
	 *            on initial entry this must be set by the user to the values of
	 *            the initial estimate of the solution vector. On exit, it
	 *            contains the values of the variables at the best point found
	 *            (usually a solution).
	 *
	 * @param gtol
	 *            the convergence requirement on zeroing the gradient.
	 *
	 * @param maxIter
	 *            the maximum allowed number of iterations.
	 *
	 * @return the minimum value of the function.
	 */
	public static double min(DifferentiableMultivariateFunction func,
			double[] x, double gtol, int maxIter) {
		// The convergence criterion on x values.
		final double TOLX = 4 * EPSILON;
		// The scaled maximum step length allowed in line searches.
		final double STPMX = 100.0;

		double den, fac, fad, fae, sumdg, sumxi, temp, test;

		int n = x.length;
		double[] dg = new double[n];
		double[] g = new double[n];
		double[] hdg = new double[n];
		double[] xnew = new double[n];
		double[] xi = new double[n];
		double[][] hessin = new double[n][n];

		// Calculate starting function value and gradient and initialize the
		// inverse Hessian to the unit matrix.
		double f = func.f(x, g);
		System.out.format("BFGS: initial function value: %.5g\n", f);

		double sum = 0.0;
		for (int i = 0; i < n; i++) {
			hessin[i][i] = 1.0;
			// Initialize line direction.
			xi[i] = -g[i];
			sum += x[i] * x[i];
		}

		double stpmax = STPMX * max(sqrt(sum), n);

		for (int iter = 1; iter <= maxIter; iter++) {
			// The new function evaluation occurs in line search.
			f = linesearch(func, x, f, g, xi, xnew, stpmax);

			if (iter % 10 == 0) {
				System.out
						.format("BFGS: the function value after %3d iterations: %.5g\n",
								iter, f);
			}

			// update the line direction and current point.
			for (int i = 0; i < n; i++) {
				xi[i] = xnew[i] - x[i];
				x[i] = xnew[i];
			}

			// Test for convergence on x.
			test = 0.0;
			for (int i = 0; i < n; i++) {
				temp = abs(xi[i]) / max(abs(x[i]), 1.0);
				if (temp > test) {
					test = temp;
				}
			}

			if (test < TOLX) {
				System.out
						.format("BFGS: the function value after %3d iterations: %.5g\n",
								iter, f);
				return f;
			}

			System.arraycopy(g, 0, dg, 0, n);

			func.f(x, g);

			// Test for convergence on zero gradient.
			den = max(f, 1.0);
			test = 0.0;
			for (int i = 0; i < n; i++) {
				temp = abs(g[i]) * max(abs(x[i]), 1.0) / den;
				if (temp > test) {
					test = temp;
				}
			}

			if (test < gtol) {
				System.out
						.format("BFGS: the function value after %3d iterations: %.5g\n",
								iter, f);
				return f;
			}

			for (int i = 0; i < n; i++) {
				dg[i] = g[i] - dg[i];
			}

			for (int i = 0; i < n; i++) {
				hdg[i] = 0.0;
				for (int j = 0; j < n; j++) {
					hdg[i] += hessin[i][j] * dg[j];
				}
			}

			fac = fae = sumdg = sumxi = 0.0;
			for (int i = 0; i < n; i++) {
				fac += dg[i] * xi[i];
				fae += dg[i] * hdg[i];
				sumdg += dg[i] * dg[i];
				sumxi += xi[i] * xi[i];
			}

			// Skip upudate if fac is not sufficiently positive.
			if (fac > sqrt(EPSILON * sumdg * sumxi)) {
				fac = 1.0 / fac;
				fad = 1.0 / fae;

				// The vector that makes BFGS different from DFP.
				for (int i = 0; i < n; i++) {
					dg[i] = fac * xi[i] - fad * hdg[i];
				}

				// BFGS updating formula.
				for (int i = 0; i < n; i++) {
					for (int j = i; j < n; j++) {
						hessin[i][j] += fac * xi[i] * xi[j] - fad * hdg[i]
								* hdg[j] + fae * dg[i] * dg[j];
						hessin[j][i] = hessin[i][j];
					}
				}
			}

			// Calculate the next direction to go.
			Arrays.fill(xi, 0.0);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					xi[i] -= hessin[i][j] * g[j];
				}
			}
		}

		throw new IllegalStateException("BFGS: Too many iterations.");
	}

	/**
	 * Returns the largest eigen pair of matrix with the power iteration under
	 * the assumptions A has an eigenvalue that is strictly greater in magnitude
	 * than its other its other eigenvalues and the starting vector has a
	 * nonzero component in the direction of an eigenvector associated with the
	 * dominant eigenvalue.
	 * 
	 * @param v
	 *            on input, it is the non-zero initial guess of the eigen
	 *            vector. On output, it is the eigen vector corresponding
	 *            largest eigen value.
	 * @return the largest eigen value.
	 */
	public static double eigen(double[][] A, double[] v) {
		return EigenValueDecomposition2.eigen(new Matrix(A), v);
	}

	/**
	 * Returns the largest eigen pair of matrix with the power iteration under
	 * the assumptions A has an eigenvalue that is strictly greater in magnitude
	 * than its other its other eigenvalues and the starting vector has a
	 * nonzero component in the direction of an eigenvector associated with the
	 * dominant eigenvalue.
	 * 
	 * @param v
	 *            on input, it is the non-zero initial guess of the eigen
	 *            vector. On output, it is the eigen vector corresponding
	 *            largest eigen value.
	 * @param tol
	 *            the desired convergence tolerance.
	 * @return the largest eigen value.
	 */
	public static double eigen(double[][] A, double[] v, double tol) {
		return EigenValueDecomposition2.eigen(new Matrix(A), v, tol);
	}

	/**
	 * Returns the largest eigen pair of matrix with the power iteration under
	 * the assumptions A has an eigenvalue that is strictly greater in magnitude
	 * than its other its other eigenvalues and the starting vector has a
	 * nonzero component in the direction of an eigenvector associated with the
	 * dominant eigenvalue.
	 * 
	 * @param v
	 *            on input, it is the non-zero initial guess of the eigen
	 *            vector. On output, it is the eigen vector corresponding
	 *            largest eigen value.
	 * @return the largest eigen value.
	 */
	public static double eigen(IMatrix A, double[] v) {
		return EigenValueDecomposition2.eigen(A, v);
	}

	/**
	 * Returns the largest eigen pair of matrix with the power iteration under
	 * the assumptions A has an eigenvalue that is strictly greater in magnitude
	 * than its other its other eigenvalues and the starting vector has a
	 * nonzero component in the direction of an eigenvector associated with the
	 * dominant eigenvalue.
	 * 
	 * @param v
	 *            on input, it is the non-zero initial guess of the eigen
	 *            vector. On output, it is the eigen vector corresponding
	 *            largest eigen value.
	 * @param tol
	 *            the desired convergence tolerance.
	 * @return the largest eigen value.
	 */
	public static double eigen(IMatrix A, double[] v, double tol) {
		return EigenValueDecomposition2.eigen(A, v, tol);
	}

	/**
	 * Find k largest approximate eigen pairs of a symmetric matrix by an
	 * iterative Lanczos algorithm.
	 */
	public static EigenValueDecomposition2 eigen(double[][] A, int k) {
		return EigenValueDecomposition2.decompose(new Matrix(A), k);
	}

	/**
	 * Returns the eigen value decomposition of a square matrix. Note that the
	 * input matrix will be altered during decomposition.
	 * 
	 * @param A
	 *            square matrix which will be altered after decomposition.
	 */
	public static EigenValueDecomposition2 eigen(double[][] A) {
		return EigenValueDecomposition2.decompose(A);
	}

	/**
	 * Returns the eigen value decomposition of a square matrix. Note that the
	 * input matrix will be altered during decomposition.
	 * 
	 * @param A
	 *            square matrix which will be altered after decomposition.
	 * @param symmetric
	 *            true if the matrix is assumed to be symmetric.
	 */
	public static EigenValueDecomposition2 eigen(double[][] A, boolean symmetric) {
		return EigenValueDecomposition2.decompose(A, symmetric);
	}

	/**
	 * Returns the eigen value decomposition of a square matrix. Note that the
	 * input matrix will be altered during decomposition.
	 * 
	 * @param A
	 *            square matrix which will be altered after decomposition.
	 * @param symmetric
	 *            true if the matrix is assumed to be symmetric.
	 * @param onlyValues
	 *            true if only compute eigenvalues; the default is to compute
	 *            eigenvectors also.
	 */
	public static EigenValueDecomposition2 eigen(double[][] A,
			boolean symmetric, boolean onlyValues) {
		return EigenValueDecomposition2.decompose(A, symmetric, onlyValues);
	}

	/**
	 * Returns the singular value decomposition. Note that the input matrix will
	 * be altered after decomposition.
	 */
	public static SingularValueDecomposition2 svd(double[][] A) {
		return SingularValueDecomposition2.decompose(A);
	}

	/**
	 * Solve A*x = b (exact solution if A is square, least squares solution
	 * otherwise), which means the LU or QR decomposition will take place in A
	 * and the results will be stored in b.
	 * 
	 * @return the solution, which is actually the vector b in case of exact
	 *         solution.
	 */
	public static double[] solve(double[][] A, double[] b) {
		if (A.length == A[0].length) {
			LUDecomposition2 lu = new LUDecomposition2(A, true);
			lu.solve(b);
			return b;
		} else {
			double[] x = new double[A[0].length];
			QRDecomposition2 qr = new QRDecomposition2(A, false);
			qr.solve(b, x);
			return x;
		}
	}

	/**
	 * Solve A*X = B (exact solution if A is square, least squares solution
	 * otherwise), which means the LU or QR decomposition will take place in A
	 * and the results will be stored in B.
	 * 
	 * @return the solution, which is actually the matrix B in case of exact
	 *         solution.
	 */
	public static double[][] solve(double[][] A, double[][] B) {
		if (A.length == A[0].length) {
			LUDecomposition2 lu = new LUDecomposition2(A, true);
			lu.solve(B);
			return B;
		} else {
			double[][] X = new double[A[0].length][B[0].length];
			QRDecomposition2 qr = new QRDecomposition2(A, false);
			qr.solve(B, X);
			return X;
		}
	}

	/**
	 * Solve the tridiagonal linear set which is of diagonal dominance
	 * |b<sub>i</sub>| &gt; |a<sub>i</sub>| + |c<sub>i</sub>|.
	 * 
	 * <pre>
	 * | b0 c0  0  0  0 ...                        |
	 * | a1 b1 c1  0  0 ...                        |
	 * |  0 a2 b2 c2  0 ...                        |
	 * |                ...                        |
	 * |                ... a(n-2)  b(n-2)  c(n-2) |
	 * |                ... 0       a(n-1)  b(n-1) |
	 * </pre>
	 * 
	 * @param a
	 *            the lower part of tridiagonal matrix. a[0] is undefined and
	 *            not referenced by the method.
	 * @param b
	 *            the diagonal of tridiagonal matrix.
	 * @param c
	 *            the upper of tridiagonal matrix. c[n-1] is undefined and not
	 *            referenced by the method.
	 * @param r
	 *            the right-hand side of linear equations.
	 * @return the solution.
	 */
	public static double[] solve(double[] a, double[] b, double[] c, double[] r) {
		if (b[0] == 0.0) {
			throw new IllegalArgumentException(
					"Invalid value of b[0] == 0. The equations should be rewritten as a set of order n - 1.");
		}

		int n = a.length;
		double[] u = new double[n];
		double[] gam = new double[n];

		double bet = b[0];
		u[0] = r[0] / bet;

		for (int j = 1; j < n; j++) {
			gam[j] = c[j - 1] / bet;
			bet = b[j] - a[j] * gam[j];
			if (bet == 0.0) {
				throw new IllegalArgumentException(
						"The tridagonal matrix is not of diagonal dominance.");
			}
			u[j] = (r[j] - a[j] * u[j - 1]) / bet;
		}

		for (int j = (n - 2); j >= 0; j--) {
			u[j] -= gam[j + 1] * u[j + 1];
		}

		return u;
	}

	/**
	 * Solves A * x = b by iterative biconjugate gradient method.
	 * 
	 * @param A
	 *            the matrix supporting matrix vector multiplication operation.
	 * @param b
	 *            the right hand side of linear equations.
	 * @param x
	 *            on input, x should be set to an initial guess of the solution
	 *            (or all zeros). On output, x is reset to the improved
	 *            solution.
	 * @return the estimated error.
	 */
	public static double solve(IMatrix A, double[] b, double[] x) {
		return solve(A, A, b, x);
	}

	/**
	 * Solves A * x = b by iterative biconjugate gradient method.
	 * 
	 * @param A
	 *            the matrix supporting matrix vector multiplication operation.
	 * @param Ap
	 *            the preconditioned matrix of A.
	 * @param b
	 *            the right hand side of linear equations.
	 * @param x
	 *            on input, x should be set to an initial guess of the solution
	 *            (or all zeros). On output, x is reset to the improved
	 *            solution.
	 * @return the estimated error.
	 */
	public static double solve(IMatrix A, IMatrix Ap, double[] b, double[] x) {
		return solve(A, Ap, b, x, 1E-10);
	}

	/**
	 * Solves A * x = b by iterative biconjugate gradient method.
	 * 
	 * @param A
	 *            the matrix supporting matrix vector multiplication operation.
	 * @param b
	 *            the right hand side of linear equations.
	 * @param x
	 *            on input, x should be set to an initial guess of the solution
	 *            (or all zeros). On output, x is reset to the improved
	 *            solution.
	 * @param tol
	 *            the desired convergence tolerance.
	 * @return the estimated error.
	 */
	public static double solve(IMatrix A, double[] b, double[] x, double tol) {
		return solve(A, A, b, x, tol);
	}

	/**
	 * Solves A * x = b by iterative biconjugate gradient method.
	 * 
	 * @param A
	 *            the matrix supporting matrix vector multiplication operation.
	 * @param Ap
	 *            the preconditioned matrix of A.
	 * @param b
	 *            the right hand side of linear equations.
	 * @param x
	 *            on input, x should be set to an initial guess of the solution
	 *            (or all zeros). On output, x is reset to the improved
	 *            solution.
	 * @param tol
	 *            the desired convergence tolerance.
	 * @return the estimated error.
	 */
	public static double solve(IMatrix A, IMatrix Ap, double[] b, double[] x,
			double tol) {
		return solve(A, Ap, b, x, tol, 1);
	}

	/**
	 * Solves A * x = b by iterative biconjugate gradient method.
	 * 
	 * @param A
	 *            the matrix supporting matrix vector multiplication operation.
	 * @param b
	 *            the right hand side of linear equations.
	 * @param x
	 *            on input, x should be set to an initial guess of the solution
	 *            (or all zeros). On output, x is reset to the improved
	 *            solution.
	 * @param itol
	 *            specify which convergence test is applied. If itol = 1,
	 *            iteration stops when |Ax - b| / |b| is less than the parameter
	 *            tolerance. If itol = 2, the stop criterion is |A<sup>-1</sup>
	 *            (Ax - b)| / |A<sup>-1</sup>b| is less than tolerance. If tol =
	 *            3, |x<sub>k+1</sub> - x<sub>k</sub>|<sub>2</sub> is less than
	 *            tolerance. The setting of tol = 4 is same as tol = 3 except
	 *            that the L<sub>&infin;</sub> norm instead of L<sub>2</sub>.
	 * @param tol
	 *            the desired convergence tolerance.
	 * @return the estimated error.
	 */
	public static double solve(IMatrix A, double[] b, double[] x, double tol,
			int itol) {
		return solve(A, A, b, x, tol, itol);
	}

	/**
	 * Solves A * x = b by iterative biconjugate gradient method.
	 * 
	 * @param A
	 *            the matrix supporting matrix vector multiplication operation.
	 * @param Ap
	 *            the preconditioned matrix of A.
	 * @param b
	 *            the right hand side of linear equations.
	 * @param x
	 *            on input, x should be set to an initial guess of the solution
	 *            (or all zeros). On output, x is reset to the improved
	 *            solution.
	 * @param itol
	 *            specify which convergence test is applied. If itol = 1,
	 *            iteration stops when |Ax - b| / |b| is less than the parameter
	 *            tolerance. If itol = 2, the stop criterion is |A<sup>-1</sup>
	 *            (Ax - b)| / |A<sup>-1</sup>b| is less than tolerance. If tol =
	 *            3, |x<sub>k+1</sub> - x<sub>k</sub>|<sub>2</sub> is less than
	 *            tolerance. The setting of tol = 4 is same as tol = 3 except
	 *            that the L<sub>&infin;</sub> norm instead of L<sub>2</sub>.
	 * @param tol
	 *            the desired convergence tolerance.
	 * @return the estimated error.
	 */
	public static double solve(IMatrix A, IMatrix Ap, double[] b, double[] x,
			double tol, int itol) {
		return solve(A, Ap, b, x, tol, itol, 2 * Math.max(A.nrows(), A.ncols()));
	}

	/**
	 * Solves A * x = b by iterative biconjugate gradient method. This method
	 * can be called repeatedly, with maxIter &lt; n, to monitor how error
	 * decreases.
	 * 
	 * @param A
	 *            the matrix supporting matrix vector multiplication operation.
	 * @param b
	 *            the right hand side of linear equations.
	 * @param x
	 *            on input, x should be set to an initial guess of the solution
	 *            (or all zeros). On output, x is reset to the improved
	 *            solution.
	 * @param itol
	 *            specify which convergence test is applied. If itol = 1,
	 *            iteration stops when |Ax - b| / |b| is less than the parameter
	 *            tolerance. If itol = 2, the stop criterion is |A<sup>-1</sup>
	 *            (Ax - b)| / |A<sup>-1</sup>b| is less than tolerance. If tol =
	 *            3, |x<sub>k+1</sub> - x<sub>k</sub>|<sub>2</sub> is less than
	 *            tolerance. The setting of tol = 4 is same as tol = 3 except
	 *            that the L<sub>&infin;</sub> norm instead of L<sub>2</sub>.
	 * @param tol
	 *            the desired convergence tolerance.
	 * @param maxIter
	 *            the maximum number of allowed iterations.
	 * @return the estimated error.
	 */
	public static double solve(IMatrix A, double[] b, double[] x, double tol,
			int itol, int maxIter) {
		return solve(A, A, b, x, tol, itol, maxIter);
	}

	/**
	 * Compute L2 or L-infinity norms for a vector x, as signaled by itol.
	 */
	private static double snorm(double[] x, int itol) {
		int n = x.length;

		if (itol <= 3) {
			double ans = 0.0;
			for (int i = 0; i < n; i++) {
				ans += x[i] * x[i];
			}
			return Math.sqrt(ans);
		} else {
			int isamax = 0;
			for (int i = 0; i < n; i++) {
				if (Math.abs(x[i]) > Math.abs(x[isamax])) {
					isamax = i;
				}
			}

			return Math.abs(x[isamax]);
		}
	}

	/**
	 * Solves A * x = b by iterative biconjugate gradient method. This method
	 * can be called repeatedly, with maxIter &lt; n, to monitor how error
	 * decreases.
	 * 
	 * @param A
	 *            the matrix supporting matrix vector multiplication operation.
	 * @param Ap
	 *            the preconditioned matrix of A.
	 * @param b
	 *            the right hand side of linear equations.
	 * @param x
	 *            on input, x should be set to an initial guess of the solution
	 *            (or all zeros). On output, x is reset to the improved
	 *            solution.
	 * @param itol
	 *            specify which convergence test is applied. If itol = 1,
	 *            iteration stops when |Ax - b| / |b| is less than the parameter
	 *            tolerance. If itol = 2, the stop criterion is |A<sup>-1</sup>
	 *            (Ax - b)| / |A<sup>-1</sup>b| is less than tolerance. If tol =
	 *            3, |x<sub>k+1</sub> - x<sub>k</sub>|<sub>2</sub> is less than
	 *            tolerance. The setting of tol = 4 is same as tol = 3 except
	 *            that the L<sub>&infin;</sub> norm instead of L<sub>2</sub>.
	 * @param tol
	 *            the desired convergence tolerance.
	 * @param maxIter
	 *            the maximum number of allowed iterations.
	 * @return the estimated error.
	 */
	public static double solve(IMatrix A, IMatrix Ap, double[] b, double[] x,
			double tol, int itol, int maxIter) {
		if (tol <= 0.0) {
			throw new IllegalArgumentException("Invalid tolerance: " + tol);
		}

		if (maxIter <= 0) {
			throw new IllegalArgumentException(
					"Invalid maximum number of iterations: " + maxIter);
		}

		if (itol < 1 || itol > 4) {
			throw new IllegalArgumentException(String.format(
					"Illegal itol: %d", itol));
		}

		double err = 0.0;
		double ak, akden, bk, bkden = 1.0, bknum, bnrm, dxnrm, xnrm, zm1nrm, znrm = 0.0;
		int j, n = b.length;

		double[] p = new double[n];
		double[] pp = new double[n];
		double[] r = new double[n];
		double[] rr = new double[n];
		double[] z = new double[n];
		double[] zz = new double[n];

		A.ax(x, r);
		for (j = 0; j < n; j++) {
			r[j] = b[j] - r[j];
			rr[j] = r[j];
		}

		if (itol == 1) {
			bnrm = snorm(b, itol);
			Ap.asolve(r, z);
		} else if (itol == 2) {
			Ap.asolve(b, z);
			bnrm = snorm(z, itol);
			Ap.asolve(r, z);
		} else if (itol == 3 || itol == 4) {
			Ap.asolve(b, z);
			bnrm = snorm(z, itol);
			Ap.asolve(r, z);
			znrm = snorm(z, itol);
		} else {
			throw new IllegalArgumentException(String.format(
					"Illegal itol: %d", itol));
		}

		for (int iter = 1; iter <= maxIter; iter++) {
			Ap.asolve(rr, zz);
			for (bknum = 0.0, j = 0; j < n; j++) {
				bknum += z[j] * rr[j];
			}
			if (iter == 1) {
				for (j = 0; j < n; j++) {
					p[j] = z[j];
					pp[j] = zz[j];
				}
			} else {
				bk = bknum / bkden;
				for (j = 0; j < n; j++) {
					p[j] = bk * p[j] + z[j];
					pp[j] = bk * pp[j] + zz[j];
				}
			}
			bkden = bknum;
			A.ax(p, z);
			for (akden = 0.0, j = 0; j < n; j++) {
				akden += z[j] * pp[j];
			}
			ak = bknum / akden;
			A.atx(pp, zz);
			for (j = 0; j < n; j++) {
				x[j] += ak * p[j];
				r[j] -= ak * z[j];
				rr[j] -= ak * zz[j];
			}
			Ap.asolve(r, z);
			if (itol == 1) {
				err = snorm(r, itol) / bnrm;
			} else if (itol == 2) {
				err = snorm(z, itol) / bnrm;
			} else if (itol == 3 || itol == 4) {
				zm1nrm = znrm;
				znrm = snorm(z, itol);
				if (Math.abs(zm1nrm - znrm) > EPSILON * znrm) {
					dxnrm = Math.abs(ak) * snorm(p, itol);
					err = znrm / Math.abs(zm1nrm - znrm) * dxnrm;
				} else {
					err = znrm / bnrm;
					continue;
				}
				xnrm = snorm(x, itol);
				if (err <= 0.5 * xnrm) {
					err /= xnrm;
				} else {
					err = znrm / bnrm;
					continue;
				}
			}

			if (iter % 10 == 0) {
				System.out.format(
						"BCG: the error after %3d iterations: %.5g\n", iter,
						err);
			}

			if (err <= tol) {
				System.out.format(
						"BCG: the error after %3d iterations: %.5g\n", iter,
						err);
				break;
			}
		}

		return err;
	}

	/**
	 * Returns the matrix inverse or pseudo inverse.
	 * 
	 * @return matrix inverse if A is square, pseudo inverse otherwise.
	 */
	public static double[][] inverse(double[][] A) {
		double[][] inv = eye(A[0].length, A.length);

		if (A.length == A[0].length) {
			LUDecomposition2 lu = new LUDecomposition2(A, false);
			lu.solve(inv);
		} else {
			QRDecomposition2 qr = new QRDecomposition2(A, false);
			qr.solve(inv);
		}

		return inv;
	}

	/**
	 * Returns the matrix determinant
	 */
	public static double det(double[][] A) {
		if (A.length != A[0].length) {
			throw new IllegalArgumentException(String.format(
					"Matrix is not square: %d x %d", A.length, A[0].length));
		}

		LUDecomposition2 lu = new LUDecomposition2(A, false);
		return lu.det();
	}

	/**
	 * Returns the matrix rank. Note that the input matrix will be altered.
	 * 
	 * @return Effective numerical rank.
	 */
	public static int rank(double[][] A) {
		return SingularValueDecomposition2.decompose(A).rank();
	}

	/**
	 * Generate a uniform random number in the range [lo, hi). This method is
	 * properly synchronized to allow correct use by more than one thread.
	 * However, if many threads need to generate pseudorandom numbers at a great
	 * rate, it may reduce contention for each thread to have its own
	 * pseudorandom-number generator.
	 * 
	 * @param lo
	 *            lower limit of range
	 * @param hi
	 *            upper limit of range
	 * @return a uniform random real in the range [lo, hi)
	 */
	public static synchronized double random(double lo, double hi) {
		return nextDouble(lo, hi);
	}

	/**
	 * Generator a random number uniformly distributed in [0, 1).
	 * 
	 * @return a pseudo random number
	 */
	public static double nextDouble() {
		return random.nextDouble();
	}

	/**
	 * Generate n uniform random numbers in the range [0, 1)
	 * 
	 * @param d
	 *            array of random numbers to be generated
	 */
	public static void nextDoubles(double[] d) {
		for (int i = 0; i < d.length; i++) {
			d[i] = nextDouble();
		}
	}

	/**
	 * Generate a uniform random number in the range [lo, hi)
	 * 
	 * @param lo
	 *            lower limit of range
	 * @param hi
	 *            upper limit of range
	 * @return a uniform random real in the range [lo, hi)
	 */
	public static double nextDouble(double lo, double hi) {
		return (lo + (hi - lo) * nextDouble());
	}

	/**
	 * Returns the dot product between two sparse arrays.
	 */
	public static double dot(SparseArray x, SparseArray y) {
		Iterator<SparseArray.Entry> it1 = x.iterator();
		Iterator<SparseArray.Entry> it2 = y.iterator();
		SparseArray.Entry e1 = it1.hasNext() ? it1.next() : null;
		SparseArray.Entry e2 = it2.hasNext() ? it2.next() : null;

		double s = 0.0;
		while (e1 != null && e2 != null) {
			if (e1.i == e2.i) {
				s += e1.x * e2.x;
				e1 = it1.hasNext() ? it1.next() : null;
				e2 = it2.hasNext() ? it2.next() : null;
			} else if (e1.i > e2.i) {
				e2 = it2.hasNext() ? it2.next() : null;
			} else {
				e1 = it1.hasNext() ? it1.next() : null;
			}
		}

		return s;
	}

	/**
	 * The Euclidean distance.
	 */
	public static double distance(SparseArray x, SparseArray y) {
		return Math.sqrt(squaredDistance(x, y));
	}

	/**
	 * The Euclidean distance.
	 */
	public static double squaredDistance(SparseArray x, SparseArray y) {
		Iterator<SparseArray.Entry> it1 = x.iterator();
		Iterator<SparseArray.Entry> it2 = y.iterator();
		SparseArray.Entry e1 = it1.hasNext() ? it1.next() : null;
		SparseArray.Entry e2 = it2.hasNext() ? it2.next() : null;

		double sum = 0.0;
		while (e1 != null && e2 != null) {
			if (e1.i == e2.i) {
				sum += sqr(e1.x - e2.x);
				e1 = it1.hasNext() ? it1.next() : null;
				e2 = it2.hasNext() ? it2.next() : null;
			} else if (e1.i > e2.i) {
				sum += sqr(e2.x);
				e2 = it2.hasNext() ? it2.next() : null;
			} else {
				sum += sqr(e1.x);
				e1 = it1.hasNext() ? it1.next() : null;
			}
		}

		while (it1.hasNext()) {
			sum += sqr(it1.next().x);
		}

		while (it2.hasNext()) {
			sum += sqr(it2.next().x);
		}

		return sum;
	}

	/**
	 * n choose k. Returns 0 if n is less than k.
	 */
	public static double choose(int n, int k) {
		if (n < 0 || k < 0) {
			throw new IllegalArgumentException(String.format(
					"Invalid n = %d, k = %d", n, k));
		}

		if (n < k) {
			return 0.0;
		}

		return floor(0.5 + Math.exp(logChoose(n, k)));
	}

	/**
	 * 生成一个排列为0,1,2,…n - 1,它是有用的 不重复抽样 Generates a permutation of 0, 1, 2, ...,
	 * n-1, which is useful for sampling without replacement.
	 */
	public static int[] permutate(int n) {
		int[] x = new int[n];
		for (int i = 0; i < n; i++) {
			x[i] = i;
		}

		permutate(x);

		return x;
	}

	/**
	 * 置换 Generates a permutation of given array.
	 */
	public static void permutate(int[] x) {
		for (int i = 0; i < x.length; i++) {
			int j = i + nextInt(x.length - i);
			swap(x, i, j);
		}
	}

	/**
	 * Generates a permutation of given array.
	 */
	public static void permutate(float[] x) {
		for (int i = 0; i < x.length; i++) {
			int j = i + nextInt(x.length - i);
			swap(x, i, j);
		}
	}

	/**
	 * Generates a permutation of given array.
	 */
	public static void permutate(double[] x) {
		for (int i = 0; i < x.length; i++) {
			int j = i + nextInt(x.length - i);
			swap(x, i, j);
		}
	}

	/**
	 * Generates a permutation of given array.
	 */
	public static void permutate(Object[] x) {
		for (int i = 0; i < x.length; i++) {
			int j = i + nextInt(x.length - i);
			swap(x, i, j);
		}
	}

	public static int nextInt(int x) {
		return random.nextInt() % x;
	}

}
