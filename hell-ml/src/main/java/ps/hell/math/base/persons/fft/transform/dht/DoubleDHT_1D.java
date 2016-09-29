package ps.hell.math.base.persons.fft.transform.dht;//package ps.landerbuluse.ml.math.fft.transform.dht;
//
//import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class DoubleDHT_1D {
//	private int n;
//	private DoubleFFT_1D fft;
//
//	public DoubleDHT_1D(int paramInt) {
//		this.n = paramInt;
//		this.fft = new DoubleFFT_1D(paramInt);
//	}
//
//	public void forward(double[] paramArrayOfDouble) {
//		forward(paramArrayOfDouble, 0);
//	}
//
//	public void forward(double[] paramArrayOfDouble, int paramInt) {
//		if (this.n == 1)
//			return;
//		this.fft.realForward(paramArrayOfDouble, paramInt);
//		double[] arrayOfDouble = new double[this.n];
//		System.arraycopy(paramArrayOfDouble, paramInt, arrayOfDouble, 0, this.n);
//		int i = this.n / 2;
//		int j = ConcurrencyUtils.getNumberOfThreads();
//		int k;
//		if ((j > 1)
//				&& (i > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads())) {
//			j = 2;
//			k = i / j;
//			Future[] arrayOfFuture = new Future[j];
//			for (int i1 = 0; i1 < j; ++i1) {
//				int i3 = 1 + i1 * k;
//				int i4 = (i1 == j - 1) ? i : i3 + k;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, paramArrayOfDouble, paramInt, arrayOfDouble) {
//					public void run() {
//						for (int k = this.val$firstIdx; k < this.val$lastIdx; ++k) {
//							int i = 2 * k;
//							int j = i + 1;
//							this.val$a[(this.val$offa + k)] = (this.val$b[i] - this.val$b[j]);
//							this.val$a[(this.val$offa + DoubleDHT_1D.this.n - k)] = (this.val$b[i] + this.val$b[j]);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int i2 = 1; i2 < i; ++i2) {
//				k = 2 * i2;
//				int l = k + 1;
//				paramArrayOfDouble[(paramInt + i2)] = (arrayOfDouble[k] - arrayOfDouble[l]);
//				paramArrayOfDouble[(paramInt + this.n - i2)] = (arrayOfDouble[k] + arrayOfDouble[l]);
//			}
//		}
//		if (this.n % 2 == 0) {
//			paramArrayOfDouble[(paramInt + i)] = arrayOfDouble[1];
//		} else {
//			paramArrayOfDouble[(paramInt + i)] = (arrayOfDouble[(this.n - 1)] - arrayOfDouble[1]);
//			paramArrayOfDouble[(paramInt + i + 1)] = (arrayOfDouble[(this.n - 1)] + arrayOfDouble[1]);
//		}
//	}
//
//	public void inverse(double[] paramArrayOfDouble, boolean paramBoolean) {
//		inverse(paramArrayOfDouble, 0, paramBoolean);
//	}
//
//	public void inverse(double[] paramArrayOfDouble, int paramInt,
//			boolean paramBoolean) {
//		if (this.n == 1)
//			return;
//		forward(paramArrayOfDouble, paramInt);
//		if (!(paramBoolean))
//			return;
//		scale(this.n, paramArrayOfDouble, paramInt);
//	}
//
//	private void scale(double paramDouble, double[] paramArrayOfDouble,
//			int paramInt) {
//		double d = 1.0D / paramDouble;
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int j;
//		if ((i > 1)
//				&& (this.n >= ConcurrencyUtils
//						.getThreadsBeginN_1D_FFT_2Threads())) {
//			i = 2;
//			j = this.n / i;
//			Future[] arrayOfFuture = new Future[i];
//			for (int l = 0; l < i; ++l) {
//				int i1 = paramInt + l * j;
//				int i2 = (l == i - 1) ? paramInt + this.n : i1 + j;
//				arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1, i2,
//						paramArrayOfDouble, d) {
//					public void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i)
//							this.val$a[i] *= this.val$norm;
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			j = paramInt + this.n;
//			for (int k = paramInt; k < j; ++k)
//				paramArrayOfDouble[k] *= d;
//		}
//	}
//}