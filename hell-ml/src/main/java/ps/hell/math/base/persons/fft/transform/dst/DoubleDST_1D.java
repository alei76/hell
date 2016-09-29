package ps.hell.math.base.persons.fft.transform.dst;//package ps.landerbuluse.ml.math.fft.transform.dst;
//
//import edu.emory.mathcs.jtransforms.dct.DoubleDCT_1D;
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class DoubleDST_1D {
//	private int n;
//	private DoubleDCT_1D dct;
//
//	public DoubleDST_1D(int paramInt) {
//		this.n = paramInt;
//		this.dct = new DoubleDCT_1D(paramInt);
//	}
//
//	public void forward(double[] paramArrayOfDouble, boolean paramBoolean) {
//		forward(paramArrayOfDouble, 0, paramBoolean);
//	}
//
//	public void forward(double[] paramArrayOfDouble, int paramInt,
//			boolean paramBoolean) {
//		if (this.n == 1)
//			return;
//		int i = this.n / 2;
//		int j = 1 + paramInt;
//		int k = paramInt + this.n;
//		for (int l = j; l < k; l += 2)
//			paramArrayOfDouble[l] = (-paramArrayOfDouble[l]);
//		this.dct.forward(paramArrayOfDouble, paramInt, paramBoolean);
//		l = ConcurrencyUtils.getNumberOfThreads();
//		int i1;
//		int i3;
//		int i4;
//		if ((l > 1)
//				&& (i > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads())) {
//			l = 2;
//			i1 = i / l;
//			Future[] arrayOfFuture = new Future[l];
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i1;
//				int i5 = (i3 == l - 1) ? i : i4 + i1;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(
//						paramInt, i4, i5, paramArrayOfDouble) {
//					public void run() {
//						int i = this.val$offa + DoubleDST_1D.this.n - 1;
//						for (int l = this.val$firstIdx; l < this.val$lastIdx; ++l) {
//							int k = this.val$offa + l;
//							double d = this.val$a[k];
//							int j = i - l;
//							this.val$a[k] = this.val$a[j];
//							this.val$a[j] = d;
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			i1 = paramInt + this.n - 1;
//			for (i4 = 0; i4 < i; ++i4) {
//				i3 = paramInt + i4;
//				double d = paramArrayOfDouble[i3];
//				int i2 = i1 - i4;
//				paramArrayOfDouble[i3] = paramArrayOfDouble[i2];
//				paramArrayOfDouble[i2] = d;
//			}
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
//		int i = this.n / 2;
//		int j = ConcurrencyUtils.getNumberOfThreads();
//		if ((j > 1)
//				&& (i > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads())) {
//			j = 2;
//			k = i / j;
//			Future[] arrayOfFuture = new Future[j];
//			for (i1 = 0; i1 < j; ++i1) {
//				int i2 = i1 * k;
//				int i3 = (i1 == j - 1) ? i : i2 + k;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//						paramInt, i2, i3, paramArrayOfDouble) {
//					public void run() {
//						int i = this.val$offa + DoubleDST_1D.this.n - 1;
//						for (int l = this.val$firstIdx; l < this.val$lastIdx; ++l) {
//							int k = this.val$offa + l;
//							double d = this.val$a[k];
//							int j = i - l;
//							this.val$a[k] = this.val$a[j];
//							this.val$a[j] = d;
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			k = paramInt + this.n - 1;
//			for (l = 0; l < i; ++l) {
//				double d = paramArrayOfDouble[(paramInt + l)];
//				paramArrayOfDouble[(paramInt + l)] = paramArrayOfDouble[(k - l)];
//				paramArrayOfDouble[(k - l)] = d;
//			}
//		}
//		this.dct.inverse(paramArrayOfDouble, paramInt, paramBoolean);
//		int k = 1 + paramInt;
//		int l = paramInt + this.n;
//		for (int i1 = k; i1 < l; i1 += 2)
//			paramArrayOfDouble[i1] = (-paramArrayOfDouble[i1]);
//	}
//}