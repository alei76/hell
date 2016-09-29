package ps.hell.math.base.persons.fft.transform.dht;//package ps.landerbuluse.ml.math.fft.transform.dht;
//
//import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class FloatDHT_1D {
//	private int n;
//	private FloatFFT_1D fft;
//
//	public FloatDHT_1D(int paramInt) {
//		this.n = paramInt;
//		this.fft = new FloatFFT_1D(paramInt);
//	}
//
//	public void forward(float[] paramArrayOfFloat) {
//		forward(paramArrayOfFloat, 0);
//	}
//
//	public void forward(float[] paramArrayOfFloat, int paramInt) {
//		if (this.n == 1)
//			return;
//		this.fft.realForward(paramArrayOfFloat, paramInt);
//		float[] arrayOfFloat = new float[this.n];
//		System.arraycopy(paramArrayOfFloat, paramInt, arrayOfFloat, 0, this.n);
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
//						i4, paramArrayOfFloat, paramInt, arrayOfFloat) {
//					public void run() {
//						for (int k = this.val$firstIdx; k < this.val$lastIdx; ++k) {
//							int i = 2 * k;
//							int j = i + 1;
//							this.val$a[(this.val$offa + k)] = (this.val$b[i] - this.val$b[j]);
//							this.val$a[(this.val$offa + FloatDHT_1D.this.n - k)] = (this.val$b[i] + this.val$b[j]);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int i2 = 1; i2 < i; ++i2) {
//				k = 2 * i2;
//				int l = k + 1;
//				paramArrayOfFloat[(paramInt + i2)] = (arrayOfFloat[k] - arrayOfFloat[l]);
//				paramArrayOfFloat[(paramInt + this.n - i2)] = (arrayOfFloat[k] + arrayOfFloat[l]);
//			}
//		}
//		if (this.n % 2 == 0) {
//			paramArrayOfFloat[(paramInt + i)] = arrayOfFloat[1];
//		} else {
//			paramArrayOfFloat[(paramInt + i)] = (arrayOfFloat[(this.n - 1)] - arrayOfFloat[1]);
//			paramArrayOfFloat[(paramInt + i + 1)] = (arrayOfFloat[(this.n - 1)] + arrayOfFloat[1]);
//		}
//	}
//
//	public void inverse(float[] paramArrayOfFloat, boolean paramBoolean) {
//		inverse(paramArrayOfFloat, 0, paramBoolean);
//	}
//
//	public void inverse(float[] paramArrayOfFloat, int paramInt,
//			boolean paramBoolean) {
//		if (this.n == 1)
//			return;
//		forward(paramArrayOfFloat, paramInt);
//		if (!(paramBoolean))
//			return;
//		scale(this.n, paramArrayOfFloat, paramInt);
//	}
//
//	private void scale(float paramFloat, float[] paramArrayOfFloat, int paramInt) {
//		float f = (float) (1.0D / paramFloat);
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
//						paramArrayOfFloat, f) {
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
//				paramArrayOfFloat[k] *= f;
//		}
//	}
//}