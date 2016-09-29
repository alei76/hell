package ps.hell.math.base.persons.fft.transform.dct;//package ps.landerbuluse.ml.math.fft.transform.dct;
//
//import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class FloatDCT_1D {
//	private int n;
//	private int[] ip;
//	private float[] w;
//	private int nw;
//	private int nc;
//	private boolean isPowerOfTwo = false;
//	private FloatFFT_1D fft;
//	private static final double PI = 3.141592653589793D;
//
//	public FloatDCT_1D(int paramInt) {
//		if (paramInt < 1)
//			throw new IllegalArgumentException("n must be greater than 0");
//		this.n = paramInt;
//		if (ConcurrencyUtils.isPowerOf2(paramInt)) {
//			this.isPowerOfTwo = true;
//			this.ip = new int[(int) Math.ceil(2 + (1 << (int) (Math
//					.log(paramInt / 2 + 0.5D) / Math.log(2.0D)) / 2))];
//			this.w = new float[paramInt * 5 / 4];
//			this.nw = this.ip[0];
//			if (paramInt > this.nw << 2) {
//				this.nw = (paramInt >> 2);
//				makewt(this.nw);
//			}
//			this.nc = this.ip[1];
//			if (paramInt <= this.nc)
//				return;
//			this.nc = paramInt;
//			makect(this.nc, this.w, this.nw);
//		} else {
//			this.w = makect(paramInt);
//			this.fft = new FloatFFT_1D(2 * paramInt);
//		}
//	}
//
//	public void forward(float[] paramArrayOfFloat, boolean paramBoolean) {
//		forward(paramArrayOfFloat, 0, paramBoolean);
//	}
//
//	public void forward(float[] paramArrayOfFloat, int paramInt,
//			boolean paramBoolean) {
//		if (this.n == 1)
//			return;
//		if (this.isPowerOfTwo) {
//			float f = paramArrayOfFloat[(paramInt + this.n - 1)];
//			for (int j = this.n - 2; j >= 2; j -= 2) {
//				paramArrayOfFloat[(paramInt + j + 1)] = (paramArrayOfFloat[(paramInt + j)] - paramArrayOfFloat[(paramInt
//						+ j - 1)]);
//				paramArrayOfFloat[(paramInt + j)] += paramArrayOfFloat[(paramInt
//						+ j - 1)];
//			}
//			paramArrayOfFloat[(paramInt + 1)] = (paramArrayOfFloat[paramInt] - f);
//			paramArrayOfFloat[paramInt] += f;
//			if (this.n > 4) {
//				rftbsub(this.n, paramArrayOfFloat, paramInt, this.nc, this.w,
//						this.nw);
//				cftbsub(this.n, paramArrayOfFloat, paramInt, this.ip, this.nw,
//						this.w);
//			} else if (this.n == 4) {
//				cftbsub(this.n, paramArrayOfFloat, paramInt, this.ip, this.nw,
//						this.w);
//			}
//			dctsub(this.n, paramArrayOfFloat, paramInt, this.nc, this.w,
//					this.nw);
//			if (paramBoolean) {
//				scale((float) Math.sqrt(2.0D / this.n), paramArrayOfFloat,
//						paramInt);
//				paramArrayOfFloat[paramInt] = (float) (paramArrayOfFloat[paramInt] / Math
//						.sqrt(2.0D));
//			}
//		} else {
//			int i = 2 * this.n;
//			float[] arrayOfFloat = new float[i];
//			System.arraycopy(paramArrayOfFloat, paramInt, arrayOfFloat, 0,
//					this.n);
//			int k = ConcurrencyUtils.getNumberOfThreads();
//			for (int l = this.n; l < i; ++l)
//				arrayOfFloat[l] = arrayOfFloat[(i - l - 1)];
//			this.fft.realForward(arrayOfFloat);
//			int i1;
//			int i3;
//			if ((k > 1)
//					&& (this.n > ConcurrencyUtils
//							.getThreadsBeginN_1D_FFT_2Threads())) {
//				k = 2;
//				i1 = this.n / k;
//				Future[] arrayOfFuture = new Future[k];
//				for (i3 = 0; i3 < k; ++i3) {
//					int i4 = i3 * i1;
//					int i5 = (i3 == k - 1) ? this.n : i4 + i1;
//					arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(
//							i4, i5, paramInt, paramArrayOfFloat, arrayOfFloat) {
//						public void run() {
//							for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								int j = 2 * i;
//								int k = this.val$offa + i;
//								this.val$a[k] = (FloatDCT_1D
//										.access$000(FloatDCT_1D.this)[j]
//										* this.val$t[j] - (FloatDCT_1D
//										.access$000(FloatDCT_1D.this)[(j + 1)] * this.val$t[(j + 1)]));
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (i1 = 0; i1 < this.n; ++i1) {
//					int i2 = 2 * i1;
//					i3 = paramInt + i1;
//					paramArrayOfFloat[i3] = (this.w[i2] * arrayOfFloat[i2] - (this.w[(i2 + 1)] * arrayOfFloat[(i2 + 1)]));
//				}
//			}
//			if (!(paramBoolean))
//				return;
//			scale((float) (1.0D / Math.sqrt(i)), paramArrayOfFloat, paramInt);
//			paramArrayOfFloat[paramInt] = (float) (paramArrayOfFloat[paramInt] / Math
//					.sqrt(2.0D));
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
//		if (this.isPowerOfTwo) {
//			if (paramBoolean) {
//				scale((float) Math.sqrt(2.0D / this.n), paramArrayOfFloat,
//						paramInt);
//				paramArrayOfFloat[paramInt] = (float) (paramArrayOfFloat[paramInt] / Math
//						.sqrt(2.0D));
//			}
//			dctsub(this.n, paramArrayOfFloat, paramInt, this.nc, this.w,
//					this.nw);
//			if (this.n > 4) {
//				cftfsub(this.n, paramArrayOfFloat, paramInt, this.ip, this.nw,
//						this.w);
//				rftfsub(this.n, paramArrayOfFloat, paramInt, this.nc, this.w,
//						this.nw);
//			} else if (this.n == 4) {
//				cftfsub(this.n, paramArrayOfFloat, paramInt, this.ip, this.nw,
//						this.w);
//			}
//			float f1 = paramArrayOfFloat[paramInt]
//					- paramArrayOfFloat[(paramInt + 1)];
//			paramArrayOfFloat[paramInt] += paramArrayOfFloat[(paramInt + 1)];
//			for (int j = 2; j < this.n; j += 2) {
//				paramArrayOfFloat[(paramInt + j - 1)] = (paramArrayOfFloat[(paramInt + j)] - paramArrayOfFloat[(paramInt
//						+ j + 1)]);
//				paramArrayOfFloat[(paramInt + j)] += paramArrayOfFloat[(paramInt
//						+ j + 1)];
//			}
//			paramArrayOfFloat[(paramInt + this.n - 1)] = f1;
//		} else {
//			int i = 2 * this.n;
//			if (paramBoolean) {
//				scale((float) Math.sqrt(i), paramArrayOfFloat, paramInt);
//				paramArrayOfFloat[paramInt] = (float) (paramArrayOfFloat[paramInt] * Math
//						.sqrt(2.0D));
//			}
//			float[] arrayOfFloat = new float[i];
//			int k = ConcurrencyUtils.getNumberOfThreads();
//			int l;
//			if ((k > 1)
//					&& (this.n > ConcurrencyUtils
//							.getThreadsBeginN_1D_FFT_2Threads())) {
//				k = 2;
//				l = this.n / k;
//				Future[] arrayOfFuture = new Future[k];
//				for (int i2 = 0; i2 < k; ++i2) {
//					int i3 = i2 * l;
//					int i4 = (i2 == k - 1) ? this.n : i3 + l;
//					arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(
//							i3, i4, paramArrayOfFloat, paramInt, arrayOfFloat) {
//						public void run() {
//							for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								int j = 2 * i;
//								float f = this.val$a[(this.val$offa + i)];
//								this.val$t[j] = (FloatDCT_1D
//										.access$000(FloatDCT_1D.this)[j] * f);
//								this.val$t[(j + 1)] = (-FloatDCT_1D
//										.access$000(FloatDCT_1D.this)[(j + 1)] * f);
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (l = 0; l < this.n; ++l) {
//					int i1 = 2 * l;
//					float f2 = paramArrayOfFloat[(paramInt + l)];
//					arrayOfFloat[i1] = (this.w[i1] * f2);
//					arrayOfFloat[(i1 + 1)] = (-this.w[(i1 + 1)] * f2);
//				}
//			}
//			this.fft.realInverse(arrayOfFloat, true);
//			System.arraycopy(arrayOfFloat, 0, paramArrayOfFloat, paramInt,
//					this.n);
//		}
//	}
//
//	private float[] makect(int paramInt) {
//		int i = 2 * paramInt;
//		double d1 = 3.141592653589793D / i;
//		float[] arrayOfFloat = new float[i];
//		arrayOfFloat[0] = 1.0F;
//		for (int k = 1; k < paramInt; ++k) {
//			int j = 2 * k;
//			double d2 = d1 * k;
//			arrayOfFloat[j] = (float) Math.cos(d2);
//			arrayOfFloat[(j + 1)] = (float) (-Math.sin(d2));
//		}
//		return arrayOfFloat;
//	}
//
//	private void makewt(int paramInt) {
//		this.ip[0] = paramInt;
//		this.ip[1] = 1;
//		if (paramInt <= 2)
//			return;
//		int j = paramInt >> 1;
//		double d1 = 0.7853981633974483D / j;
//		double d2 = d1 * 2.0D;
//		float f1 = (float) Math.cos(d1 * j);
//		this.w[0] = 1.0F;
//		this.w[1] = f1;
//		int i;
//		if (j == 4) {
//			this.w[2] = (float) Math.cos(d2);
//			this.w[3] = (float) Math.sin(d2);
//		} else if (j > 4) {
//			makeipt(paramInt);
//			this.w[2] = (float) (0.5D / Math.cos(d2));
//			this.w[3] = (float) (0.5D / Math.cos(d1 * 6.0D));
//			for (i = 4; i < j; i += 4) {
//				double d3 = d1 * i;
//				double d4 = 3.0D * d3;
//				this.w[i] = (float) Math.cos(d3);
//				this.w[(i + 1)] = (float) Math.sin(d3);
//				this.w[(i + 2)] = (float) Math.cos(d4);
//				this.w[(i + 3)] = (float) (-Math.sin(d4));
//			}
//		}
//		int l;
//		for (int k = 0; j > 2; k = l) {
//			l = k + j;
//			j >>= 1;
//			this.w[l] = 1.0F;
//			this.w[(l + 1)] = f1;
//			float f2;
//			float f3;
//			if (j == 4) {
//				f2 = this.w[(k + 4)];
//				f3 = this.w[(k + 5)];
//				this.w[(l + 2)] = f2;
//				this.w[(l + 3)] = f3;
//			} else {
//				if (j <= 4)
//					continue;
//				f2 = this.w[(k + 4)];
//				float f4 = this.w[(k + 6)];
//				this.w[(l + 2)] = (float) (0.5D / f2);
//				this.w[(l + 3)] = (float) (0.5D / f4);
//				for (i = 4; i < j; i += 4) {
//					int i1 = k + 2 * i;
//					int i2 = l + i;
//					f2 = this.w[i1];
//					f3 = this.w[(i1 + 1)];
//					f4 = this.w[(i1 + 2)];
//					float f5 = this.w[(i1 + 3)];
//					this.w[i2] = f2;
//					this.w[(i2 + 1)] = f3;
//					this.w[(i2 + 2)] = f4;
//					this.w[(i2 + 3)] = f5;
//				}
//			}
//		}
//	}
//
//	private void makeipt(int paramInt) {
//		this.ip[2] = 0;
//		this.ip[3] = 16;
//		int k = 2;
//		int j = paramInt;
//		while (j > 32) {
//			int l = k << 1;
//			int i2 = l << 3;
//			for (int i = k; i < l; ++i) {
//				int i1 = this.ip[i] << 2;
//				this.ip[(k + i)] = i1;
//				this.ip[(l + i)] = (i1 + i2);
//			}
//			k = l;
//			j >>= 2;
//		}
//	}
//
//	private void makect(int paramInt1, float[] paramArrayOfFloat, int paramInt2) {
//		this.ip[1] = paramInt1;
//		if (paramInt1 <= 1)
//			return;
//		int j = paramInt1 >> 1;
//		double d1 = 0.7853981633974483D / j;
//		paramArrayOfFloat[paramInt2] = (float) Math.cos(d1 * j);
//		paramArrayOfFloat[(paramInt2 + j)] = (float) (0.5D * paramArrayOfFloat[paramInt2]);
//		for (int i = 1; i < j; ++i) {
//			double d2 = d1 * i;
//			paramArrayOfFloat[(paramInt2 + i)] = (float) (0.5D * Math.cos(d2));
//			paramArrayOfFloat[(paramInt2 + paramInt1 - i)] = (float) (0.5D * Math
//					.sin(d2));
//		}
//	}
//
//	private void cftfsub(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, int[] paramArrayOfInt, int paramInt3,
//			float[] paramArrayOfFloat2) {
//		if (paramInt1 > 8) {
//			if (paramInt1 > 32) {
//				cftf1st(paramInt1, paramArrayOfFloat1, paramInt2,
//						paramArrayOfFloat2, paramInt3 - (paramInt1 >> 2));
//				if ((ConcurrencyUtils.getNumberOfThreads() > 1)
//						&& (paramInt1 > ConcurrencyUtils
//								.getThreadsBeginN_1D_FFT_2Threads()))
//					cftrec4_th(paramInt1, paramArrayOfFloat1, paramInt2,
//							paramInt3, paramArrayOfFloat2);
//				else if (paramInt1 > 512)
//					cftrec4(paramInt1, paramArrayOfFloat1, paramInt2,
//							paramInt3, paramArrayOfFloat2);
//				else if (paramInt1 > 128)
//					cftleaf(paramInt1, 1, paramArrayOfFloat1, paramInt2,
//							paramInt3, paramArrayOfFloat2);
//				else
//					cftfx41(paramInt1, paramArrayOfFloat1, paramInt2,
//							paramInt3, paramArrayOfFloat2);
//				bitrv2(paramInt1, paramArrayOfInt, paramArrayOfFloat1,
//						paramInt2);
//			} else if (paramInt1 == 32) {
//				cftf161(paramArrayOfFloat1, paramInt2, paramArrayOfFloat2,
//						paramInt3 - 8);
//				bitrv216(paramArrayOfFloat1, paramInt2);
//			} else {
//				cftf081(paramArrayOfFloat1, paramInt2, paramArrayOfFloat2, 0);
//				bitrv208(paramArrayOfFloat1, paramInt2);
//			}
//		} else if (paramInt1 == 8) {
//			cftf040(paramArrayOfFloat1, paramInt2);
//		} else {
//			if (paramInt1 != 4)
//				return;
//			cftx020(paramArrayOfFloat1, paramInt2);
//		}
//	}
//
//	private void cftbsub(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, int[] paramArrayOfInt, int paramInt3,
//			float[] paramArrayOfFloat2) {
//		if (paramInt1 > 8) {
//			if (paramInt1 > 32) {
//				cftb1st(paramInt1, paramArrayOfFloat1, paramInt2,
//						paramArrayOfFloat2, paramInt3 - (paramInt1 >> 2));
//				if ((ConcurrencyUtils.getNumberOfThreads() > 1)
//						&& (paramInt1 > ConcurrencyUtils
//								.getThreadsBeginN_1D_FFT_2Threads()))
//					cftrec4_th(paramInt1, paramArrayOfFloat1, paramInt2,
//							paramInt3, paramArrayOfFloat2);
//				else if (paramInt1 > 512)
//					cftrec4(paramInt1, paramArrayOfFloat1, paramInt2,
//							paramInt3, paramArrayOfFloat2);
//				else if (paramInt1 > 128)
//					cftleaf(paramInt1, 1, paramArrayOfFloat1, paramInt2,
//							paramInt3, paramArrayOfFloat2);
//				else
//					cftfx41(paramInt1, paramArrayOfFloat1, paramInt2,
//							paramInt3, paramArrayOfFloat2);
//				bitrv2conj(paramInt1, paramArrayOfInt, paramArrayOfFloat1,
//						paramInt2);
//			} else if (paramInt1 == 32) {
//				cftf161(paramArrayOfFloat1, paramInt2, paramArrayOfFloat2,
//						paramInt3 - 8);
//				bitrv216neg(paramArrayOfFloat1, paramInt2);
//			} else {
//				cftf081(paramArrayOfFloat1, paramInt2, paramArrayOfFloat2, 0);
//				bitrv208neg(paramArrayOfFloat1, paramInt2);
//			}
//		} else if (paramInt1 == 8) {
//			cftb040(paramArrayOfFloat1, paramInt2);
//		} else {
//			if (paramInt1 != 4)
//				return;
//			cftx020(paramArrayOfFloat1, paramInt2);
//		}
//	}
//
//	private void bitrv2(int paramInt1, int[] paramArrayOfInt,
//			float[] paramArrayOfFloat, int paramInt2) {
//		int l = 1;
//		int k = paramInt1 >> 2;
//		while (k > 8) {
//			l <<= 1;
//			k >>= 2;
//		}
//		int i1 = paramInt1 >> 1;
//		int i2 = 4 * l;
//		int i5;
//		int i6;
//		int i;
//		int j;
//		int i3;
//		int i4;
//		float f1;
//		float f2;
//		float f3;
//		float f4;
//		if (k == 8)
//			for (i5 = 0; i5 < l; ++i5) {
//				for (i6 = 0; i6 < i5; ++i6) {
//					i = 4 * i6 + 2 * paramArrayOfInt[(l + i5)];
//					j = 4 * i5 + 2 * paramArrayOfInt[(l + i6)];
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j -= i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i1;
//					j += 2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j += i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += 2;
//					j += i1;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j -= i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i1;
//					j -= 2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j += i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//				}
//				j = 4 * i5 + 2 * paramArrayOfInt[(l + i5)];
//				i = j + 2;
//				j += i1;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				i += i2;
//				j += 2 * i2;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				i += i2;
//				j -= i2;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				i -= 2;
//				j -= i1;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				i += i1 + 2;
//				j += i1 + 2;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				i -= i1 - i2;
//				j += 2 * i2 - 2;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//			}
//		else
//			for (i5 = 0; i5 < l; ++i5) {
//				for (i6 = 0; i6 < i5; ++i6) {
//					i = 4 * i6 + paramArrayOfInt[(l + i5)];
//					j = 4 * i5 + paramArrayOfInt[(l + i6)];
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i1;
//					j += 2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += 2;
//					j += i1;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i1;
//					j -= 2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//				}
//				j = 4 * i5 + paramArrayOfInt[(l + i5)];
//				i = j + 2;
//				j += i1;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				i += i2;
//				j += i2;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//			}
//	}
//
//	private void bitrv2conj(int paramInt1, int[] paramArrayOfInt,
//			float[] paramArrayOfFloat, int paramInt2) {
//		int l = 1;
//		int k = paramInt1 >> 2;
//		while (k > 8) {
//			l <<= 1;
//			k >>= 2;
//		}
//		int i1 = paramInt1 >> 1;
//		int i2 = 4 * l;
//		int i5;
//		int i6;
//		int i;
//		int j;
//		int i3;
//		int i4;
//		float f1;
//		float f2;
//		float f3;
//		float f4;
//		if (k == 8)
//			for (i5 = 0; i5 < l; ++i5) {
//				for (i6 = 0; i6 < i5; ++i6) {
//					i = 4 * i6 + 2 * paramArrayOfInt[(l + i5)];
//					j = 4 * i5 + 2 * paramArrayOfInt[(l + i6)];
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j -= i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i1;
//					j += 2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j += i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += 2;
//					j += i1;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j -= i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i1;
//					j -= 2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j += i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//				}
//				j = 4 * i5 + 2 * paramArrayOfInt[(l + i5)];
//				i = j + 2;
//				j += i1;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				paramArrayOfFloat[(i3 - 1)] = (-paramArrayOfFloat[(i3 - 1)]);
//				f1 = paramArrayOfFloat[i3];
//				f2 = -paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = -paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				paramArrayOfFloat[(i4 + 3)] = (-paramArrayOfFloat[(i4 + 3)]);
//				i += i2;
//				j += 2 * i2;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = -paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = -paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				i += i2;
//				j -= i2;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = -paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = -paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				i -= 2;
//				j -= i1;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = -paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = -paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				i += i1 + 2;
//				j += i1 + 2;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i3];
//				f2 = -paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = -paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				i -= i1 - i2;
//				j += 2 * i2 - 2;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				paramArrayOfFloat[(i3 - 1)] = (-paramArrayOfFloat[(i3 - 1)]);
//				f1 = paramArrayOfFloat[i3];
//				f2 = -paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = -paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				paramArrayOfFloat[(i4 + 3)] = (-paramArrayOfFloat[(i4 + 3)]);
//			}
//		else
//			for (i5 = 0; i5 < l; ++i5) {
//				for (i6 = 0; i6 < i5; ++i6) {
//					i = 4 * i6 + paramArrayOfInt[(l + i5)];
//					j = 4 * i5 + paramArrayOfInt[(l + i6)];
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i1;
//					j += 2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += 2;
//					j += i1;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i += i2;
//					j += i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i1;
//					j -= 2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//					i -= i2;
//					j -= i2;
//					i3 = paramInt2 + i;
//					i4 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i3];
//					f2 = -paramArrayOfFloat[(i3 + 1)];
//					f3 = paramArrayOfFloat[i4];
//					f4 = -paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[i3] = f3;
//					paramArrayOfFloat[(i3 + 1)] = f4;
//					paramArrayOfFloat[i4] = f1;
//					paramArrayOfFloat[(i4 + 1)] = f2;
//				}
//				j = 4 * i5 + paramArrayOfInt[(l + i5)];
//				i = j + 2;
//				j += i1;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				paramArrayOfFloat[(i3 - 1)] = (-paramArrayOfFloat[(i3 - 1)]);
//				f1 = paramArrayOfFloat[i3];
//				f2 = -paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = -paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				paramArrayOfFloat[(i4 + 3)] = (-paramArrayOfFloat[(i4 + 3)]);
//				i += i2;
//				j += i2;
//				i3 = paramInt2 + i;
//				i4 = paramInt2 + j;
//				paramArrayOfFloat[(i3 - 1)] = (-paramArrayOfFloat[(i3 - 1)]);
//				f1 = paramArrayOfFloat[i3];
//				f2 = -paramArrayOfFloat[(i3 + 1)];
//				f3 = paramArrayOfFloat[i4];
//				f4 = -paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[i3] = f3;
//				paramArrayOfFloat[(i3 + 1)] = f4;
//				paramArrayOfFloat[i4] = f1;
//				paramArrayOfFloat[(i4 + 1)] = f2;
//				paramArrayOfFloat[(i4 + 3)] = (-paramArrayOfFloat[(i4 + 3)]);
//			}
//	}
//
//	private void bitrv216(float[] paramArrayOfFloat, int paramInt) {
//		float f1 = paramArrayOfFloat[(paramInt + 2)];
//		float f2 = paramArrayOfFloat[(paramInt + 3)];
//		float f3 = paramArrayOfFloat[(paramInt + 4)];
//		float f4 = paramArrayOfFloat[(paramInt + 5)];
//		float f5 = paramArrayOfFloat[(paramInt + 6)];
//		float f6 = paramArrayOfFloat[(paramInt + 7)];
//		float f7 = paramArrayOfFloat[(paramInt + 8)];
//		float f8 = paramArrayOfFloat[(paramInt + 9)];
//		float f9 = paramArrayOfFloat[(paramInt + 10)];
//		float f10 = paramArrayOfFloat[(paramInt + 11)];
//		float f11 = paramArrayOfFloat[(paramInt + 14)];
//		float f12 = paramArrayOfFloat[(paramInt + 15)];
//		float f13 = paramArrayOfFloat[(paramInt + 16)];
//		float f14 = paramArrayOfFloat[(paramInt + 17)];
//		float f15 = paramArrayOfFloat[(paramInt + 20)];
//		float f16 = paramArrayOfFloat[(paramInt + 21)];
//		float f17 = paramArrayOfFloat[(paramInt + 22)];
//		float f18 = paramArrayOfFloat[(paramInt + 23)];
//		float f19 = paramArrayOfFloat[(paramInt + 24)];
//		float f20 = paramArrayOfFloat[(paramInt + 25)];
//		float f21 = paramArrayOfFloat[(paramInt + 26)];
//		float f22 = paramArrayOfFloat[(paramInt + 27)];
//		float f23 = paramArrayOfFloat[(paramInt + 28)];
//		float f24 = paramArrayOfFloat[(paramInt + 29)];
//		paramArrayOfFloat[(paramInt + 2)] = f13;
//		paramArrayOfFloat[(paramInt + 3)] = f14;
//		paramArrayOfFloat[(paramInt + 4)] = f7;
//		paramArrayOfFloat[(paramInt + 5)] = f8;
//		paramArrayOfFloat[(paramInt + 6)] = f19;
//		paramArrayOfFloat[(paramInt + 7)] = f20;
//		paramArrayOfFloat[(paramInt + 8)] = f3;
//		paramArrayOfFloat[(paramInt + 9)] = f4;
//		paramArrayOfFloat[(paramInt + 10)] = f15;
//		paramArrayOfFloat[(paramInt + 11)] = f16;
//		paramArrayOfFloat[(paramInt + 14)] = f23;
//		paramArrayOfFloat[(paramInt + 15)] = f24;
//		paramArrayOfFloat[(paramInt + 16)] = f1;
//		paramArrayOfFloat[(paramInt + 17)] = f2;
//		paramArrayOfFloat[(paramInt + 20)] = f9;
//		paramArrayOfFloat[(paramInt + 21)] = f10;
//		paramArrayOfFloat[(paramInt + 22)] = f21;
//		paramArrayOfFloat[(paramInt + 23)] = f22;
//		paramArrayOfFloat[(paramInt + 24)] = f5;
//		paramArrayOfFloat[(paramInt + 25)] = f6;
//		paramArrayOfFloat[(paramInt + 26)] = f17;
//		paramArrayOfFloat[(paramInt + 27)] = f18;
//		paramArrayOfFloat[(paramInt + 28)] = f11;
//		paramArrayOfFloat[(paramInt + 29)] = f12;
//	}
//
//	private void bitrv216neg(float[] paramArrayOfFloat, int paramInt) {
//		float f1 = paramArrayOfFloat[(paramInt + 2)];
//		float f2 = paramArrayOfFloat[(paramInt + 3)];
//		float f3 = paramArrayOfFloat[(paramInt + 4)];
//		float f4 = paramArrayOfFloat[(paramInt + 5)];
//		float f5 = paramArrayOfFloat[(paramInt + 6)];
//		float f6 = paramArrayOfFloat[(paramInt + 7)];
//		float f7 = paramArrayOfFloat[(paramInt + 8)];
//		float f8 = paramArrayOfFloat[(paramInt + 9)];
//		float f9 = paramArrayOfFloat[(paramInt + 10)];
//		float f10 = paramArrayOfFloat[(paramInt + 11)];
//		float f11 = paramArrayOfFloat[(paramInt + 12)];
//		float f12 = paramArrayOfFloat[(paramInt + 13)];
//		float f13 = paramArrayOfFloat[(paramInt + 14)];
//		float f14 = paramArrayOfFloat[(paramInt + 15)];
//		float f15 = paramArrayOfFloat[(paramInt + 16)];
//		float f16 = paramArrayOfFloat[(paramInt + 17)];
//		float f17 = paramArrayOfFloat[(paramInt + 18)];
//		float f18 = paramArrayOfFloat[(paramInt + 19)];
//		float f19 = paramArrayOfFloat[(paramInt + 20)];
//		float f20 = paramArrayOfFloat[(paramInt + 21)];
//		float f21 = paramArrayOfFloat[(paramInt + 22)];
//		float f22 = paramArrayOfFloat[(paramInt + 23)];
//		float f23 = paramArrayOfFloat[(paramInt + 24)];
//		float f24 = paramArrayOfFloat[(paramInt + 25)];
//		float f25 = paramArrayOfFloat[(paramInt + 26)];
//		float f26 = paramArrayOfFloat[(paramInt + 27)];
//		float f27 = paramArrayOfFloat[(paramInt + 28)];
//		float f28 = paramArrayOfFloat[(paramInt + 29)];
//		float f29 = paramArrayOfFloat[(paramInt + 30)];
//		float f30 = paramArrayOfFloat[(paramInt + 31)];
//		paramArrayOfFloat[(paramInt + 2)] = f29;
//		paramArrayOfFloat[(paramInt + 3)] = f30;
//		paramArrayOfFloat[(paramInt + 4)] = f13;
//		paramArrayOfFloat[(paramInt + 5)] = f14;
//		paramArrayOfFloat[(paramInt + 6)] = f21;
//		paramArrayOfFloat[(paramInt + 7)] = f22;
//		paramArrayOfFloat[(paramInt + 8)] = f5;
//		paramArrayOfFloat[(paramInt + 9)] = f6;
//		paramArrayOfFloat[(paramInt + 10)] = f25;
//		paramArrayOfFloat[(paramInt + 11)] = f26;
//		paramArrayOfFloat[(paramInt + 12)] = f9;
//		paramArrayOfFloat[(paramInt + 13)] = f10;
//		paramArrayOfFloat[(paramInt + 14)] = f17;
//		paramArrayOfFloat[(paramInt + 15)] = f18;
//		paramArrayOfFloat[(paramInt + 16)] = f1;
//		paramArrayOfFloat[(paramInt + 17)] = f2;
//		paramArrayOfFloat[(paramInt + 18)] = f27;
//		paramArrayOfFloat[(paramInt + 19)] = f28;
//		paramArrayOfFloat[(paramInt + 20)] = f11;
//		paramArrayOfFloat[(paramInt + 21)] = f12;
//		paramArrayOfFloat[(paramInt + 22)] = f19;
//		paramArrayOfFloat[(paramInt + 23)] = f20;
//		paramArrayOfFloat[(paramInt + 24)] = f3;
//		paramArrayOfFloat[(paramInt + 25)] = f4;
//		paramArrayOfFloat[(paramInt + 26)] = f23;
//		paramArrayOfFloat[(paramInt + 27)] = f24;
//		paramArrayOfFloat[(paramInt + 28)] = f7;
//		paramArrayOfFloat[(paramInt + 29)] = f8;
//		paramArrayOfFloat[(paramInt + 30)] = f15;
//		paramArrayOfFloat[(paramInt + 31)] = f16;
//	}
//
//	private void bitrv208(float[] paramArrayOfFloat, int paramInt) {
//		float f1 = paramArrayOfFloat[(paramInt + 2)];
//		float f2 = paramArrayOfFloat[(paramInt + 3)];
//		float f3 = paramArrayOfFloat[(paramInt + 6)];
//		float f4 = paramArrayOfFloat[(paramInt + 7)];
//		float f5 = paramArrayOfFloat[(paramInt + 8)];
//		float f6 = paramArrayOfFloat[(paramInt + 9)];
//		float f7 = paramArrayOfFloat[(paramInt + 12)];
//		float f8 = paramArrayOfFloat[(paramInt + 13)];
//		paramArrayOfFloat[(paramInt + 2)] = f5;
//		paramArrayOfFloat[(paramInt + 3)] = f6;
//		paramArrayOfFloat[(paramInt + 6)] = f7;
//		paramArrayOfFloat[(paramInt + 7)] = f8;
//		paramArrayOfFloat[(paramInt + 8)] = f1;
//		paramArrayOfFloat[(paramInt + 9)] = f2;
//		paramArrayOfFloat[(paramInt + 12)] = f3;
//		paramArrayOfFloat[(paramInt + 13)] = f4;
//	}
//
//	private void bitrv208neg(float[] paramArrayOfFloat, int paramInt) {
//		float f1 = paramArrayOfFloat[(paramInt + 2)];
//		float f2 = paramArrayOfFloat[(paramInt + 3)];
//		float f3 = paramArrayOfFloat[(paramInt + 4)];
//		float f4 = paramArrayOfFloat[(paramInt + 5)];
//		float f5 = paramArrayOfFloat[(paramInt + 6)];
//		float f6 = paramArrayOfFloat[(paramInt + 7)];
//		float f7 = paramArrayOfFloat[(paramInt + 8)];
//		float f8 = paramArrayOfFloat[(paramInt + 9)];
//		float f9 = paramArrayOfFloat[(paramInt + 10)];
//		float f10 = paramArrayOfFloat[(paramInt + 11)];
//		float f11 = paramArrayOfFloat[(paramInt + 12)];
//		float f12 = paramArrayOfFloat[(paramInt + 13)];
//		float f13 = paramArrayOfFloat[(paramInt + 14)];
//		float f14 = paramArrayOfFloat[(paramInt + 15)];
//		paramArrayOfFloat[(paramInt + 2)] = f13;
//		paramArrayOfFloat[(paramInt + 3)] = f14;
//		paramArrayOfFloat[(paramInt + 4)] = f5;
//		paramArrayOfFloat[(paramInt + 5)] = f6;
//		paramArrayOfFloat[(paramInt + 6)] = f9;
//		paramArrayOfFloat[(paramInt + 7)] = f10;
//		paramArrayOfFloat[(paramInt + 8)] = f1;
//		paramArrayOfFloat[(paramInt + 9)] = f2;
//		paramArrayOfFloat[(paramInt + 10)] = f11;
//		paramArrayOfFloat[(paramInt + 11)] = f12;
//		paramArrayOfFloat[(paramInt + 12)] = f3;
//		paramArrayOfFloat[(paramInt + 13)] = f4;
//		paramArrayOfFloat[(paramInt + 14)] = f7;
//		paramArrayOfFloat[(paramInt + 15)] = f8;
//	}
//
//	private void cftf1st(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, float[] paramArrayOfFloat2, int paramInt3) {
//		int i3 = paramInt1 >> 3;
//		int i2 = 2 * i3;
//		int j = i2;
//		int k = j + i2;
//		int l = k + i2;
//		int i5 = paramInt2 + j;
//		int i6 = paramInt2 + k;
//		int i7 = paramInt2 + l;
//		float f12 = paramArrayOfFloat1[paramInt2] + paramArrayOfFloat1[i6];
//		float f13 = paramArrayOfFloat1[(paramInt2 + 1)]
//				+ paramArrayOfFloat1[(i6 + 1)];
//		float f14 = paramArrayOfFloat1[paramInt2] - paramArrayOfFloat1[i6];
//		float f15 = paramArrayOfFloat1[(paramInt2 + 1)]
//				- paramArrayOfFloat1[(i6 + 1)];
//		float f16 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//		float f17 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//		float f18 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//		float f19 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//		paramArrayOfFloat1[paramInt2] = (f12 + f16);
//		paramArrayOfFloat1[(paramInt2 + 1)] = (f13 + f17);
//		paramArrayOfFloat1[i5] = (f12 - f16);
//		paramArrayOfFloat1[(i5 + 1)] = (f13 - f17);
//		paramArrayOfFloat1[i6] = (f14 - f19);
//		paramArrayOfFloat1[(i6 + 1)] = (f15 + f18);
//		paramArrayOfFloat1[i7] = (f14 + f19);
//		paramArrayOfFloat1[(i7 + 1)] = (f15 - f18);
//		float f1 = paramArrayOfFloat2[(paramInt3 + 1)];
//		float f2 = paramArrayOfFloat2[(paramInt3 + 2)];
//		float f3 = paramArrayOfFloat2[(paramInt3 + 3)];
//		float f8 = 1.0F;
//		float f9 = 0.0F;
//		float f10 = 1.0F;
//		float f11 = 0.0F;
//		int i1 = 0;
//		for (int i10 = 2; i10 < i3 - 2; i10 += 4) {
//			int i8 = paramInt3 + (i1 += 4);
//			f4 = f2 * (f8 + paramArrayOfFloat2[i8]);
//			f5 = f2 * (f9 + paramArrayOfFloat2[(i8 + 1)]);
//			f6 = f3 * (f10 + paramArrayOfFloat2[(i8 + 2)]);
//			f7 = f3 * (f11 + paramArrayOfFloat2[(i8 + 3)]);
//			f8 = paramArrayOfFloat2[i8];
//			f9 = paramArrayOfFloat2[(i8 + 1)];
//			f10 = paramArrayOfFloat2[(i8 + 2)];
//			f11 = paramArrayOfFloat2[(i8 + 3)];
//			j = i10 + i2;
//			k = j + i2;
//			l = k + i2;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			int i9 = paramInt2 + i10;
//			f12 = paramArrayOfFloat1[i9] + paramArrayOfFloat1[i6];
//			f13 = paramArrayOfFloat1[(i9 + 1)] + paramArrayOfFloat1[(i6 + 1)];
//			f14 = paramArrayOfFloat1[i9] - paramArrayOfFloat1[i6];
//			f15 = paramArrayOfFloat1[(i9 + 1)] - paramArrayOfFloat1[(i6 + 1)];
//			float f20 = paramArrayOfFloat1[(i9 + 2)]
//					+ paramArrayOfFloat1[(i6 + 2)];
//			float f21 = paramArrayOfFloat1[(i9 + 3)]
//					+ paramArrayOfFloat1[(i6 + 3)];
//			float f22 = paramArrayOfFloat1[(i9 + 2)]
//					- paramArrayOfFloat1[(i6 + 2)];
//			float f23 = paramArrayOfFloat1[(i9 + 3)]
//					- paramArrayOfFloat1[(i6 + 3)];
//			f16 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//			f17 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//			f18 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//			f19 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//			float f24 = paramArrayOfFloat1[(i5 + 2)]
//					+ paramArrayOfFloat1[(i7 + 2)];
//			float f25 = paramArrayOfFloat1[(i5 + 3)]
//					+ paramArrayOfFloat1[(i7 + 3)];
//			float f26 = paramArrayOfFloat1[(i5 + 2)]
//					- paramArrayOfFloat1[(i7 + 2)];
//			float f27 = paramArrayOfFloat1[(i5 + 3)]
//					- paramArrayOfFloat1[(i7 + 3)];
//			paramArrayOfFloat1[i9] = (f12 + f16);
//			paramArrayOfFloat1[(i9 + 1)] = (f13 + f17);
//			paramArrayOfFloat1[(i9 + 2)] = (f20 + f24);
//			paramArrayOfFloat1[(i9 + 3)] = (f21 + f25);
//			paramArrayOfFloat1[i5] = (f12 - f16);
//			paramArrayOfFloat1[(i5 + 1)] = (f13 - f17);
//			paramArrayOfFloat1[(i5 + 2)] = (f20 - f24);
//			paramArrayOfFloat1[(i5 + 3)] = (f21 - f25);
//			f12 = f14 - f19;
//			f13 = f15 + f18;
//			paramArrayOfFloat1[i6] = (f4 * f12 - (f5 * f13));
//			paramArrayOfFloat1[(i6 + 1)] = (f4 * f13 + f5 * f12);
//			f12 = f22 - f27;
//			f13 = f23 + f26;
//			paramArrayOfFloat1[(i6 + 2)] = (f8 * f12 - (f9 * f13));
//			paramArrayOfFloat1[(i6 + 3)] = (f8 * f13 + f9 * f12);
//			f12 = f14 + f19;
//			f13 = f15 - f18;
//			paramArrayOfFloat1[i7] = (f6 * f12 + f7 * f13);
//			paramArrayOfFloat1[(i7 + 1)] = (f6 * f13 - (f7 * f12));
//			f12 = f22 + f27;
//			f13 = f23 - f26;
//			paramArrayOfFloat1[(i7 + 2)] = (f10 * f12 + f11 * f13);
//			paramArrayOfFloat1[(i7 + 3)] = (f10 * f13 - (f11 * f12));
//			i = i2 - i10;
//			j = i + i2;
//			k = j + i2;
//			l = k + i2;
//			i4 = paramInt2 + i;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			f12 = paramArrayOfFloat1[i4] + paramArrayOfFloat1[i6];
//			f13 = paramArrayOfFloat1[(i4 + 1)] + paramArrayOfFloat1[(i6 + 1)];
//			f14 = paramArrayOfFloat1[i4] - paramArrayOfFloat1[i6];
//			f15 = paramArrayOfFloat1[(i4 + 1)] - paramArrayOfFloat1[(i6 + 1)];
//			f20 = paramArrayOfFloat1[(i4 - 2)] + paramArrayOfFloat1[(i6 - 2)];
//			f21 = paramArrayOfFloat1[(i4 - 1)] + paramArrayOfFloat1[(i6 - 1)];
//			f22 = paramArrayOfFloat1[(i4 - 2)] - paramArrayOfFloat1[(i6 - 2)];
//			f23 = paramArrayOfFloat1[(i4 - 1)] - paramArrayOfFloat1[(i6 - 1)];
//			f16 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//			f17 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//			f18 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//			f19 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//			f24 = paramArrayOfFloat1[(i5 - 2)] + paramArrayOfFloat1[(i7 - 2)];
//			f25 = paramArrayOfFloat1[(i5 - 1)] + paramArrayOfFloat1[(i7 - 1)];
//			f26 = paramArrayOfFloat1[(i5 - 2)] - paramArrayOfFloat1[(i7 - 2)];
//			f27 = paramArrayOfFloat1[(i5 - 1)] - paramArrayOfFloat1[(i7 - 1)];
//			paramArrayOfFloat1[i4] = (f12 + f16);
//			paramArrayOfFloat1[(i4 + 1)] = (f13 + f17);
//			paramArrayOfFloat1[(i4 - 2)] = (f20 + f24);
//			paramArrayOfFloat1[(i4 - 1)] = (f21 + f25);
//			paramArrayOfFloat1[i5] = (f12 - f16);
//			paramArrayOfFloat1[(i5 + 1)] = (f13 - f17);
//			paramArrayOfFloat1[(i5 - 2)] = (f20 - f24);
//			paramArrayOfFloat1[(i5 - 1)] = (f21 - f25);
//			f12 = f14 - f19;
//			f13 = f15 + f18;
//			paramArrayOfFloat1[i6] = (f5 * f12 - (f4 * f13));
//			paramArrayOfFloat1[(i6 + 1)] = (f5 * f13 + f4 * f12);
//			f12 = f22 - f27;
//			f13 = f23 + f26;
//			paramArrayOfFloat1[(i6 - 2)] = (f9 * f12 - (f8 * f13));
//			paramArrayOfFloat1[(i6 - 1)] = (f9 * f13 + f8 * f12);
//			f12 = f14 + f19;
//			f13 = f15 - f18;
//			paramArrayOfFloat1[i7] = (f7 * f12 + f6 * f13);
//			paramArrayOfFloat1[(i7 + 1)] = (f7 * f13 - (f6 * f12));
//			f12 = f22 + f27;
//			f13 = f23 - f26;
//			paramArrayOfFloat1[(paramInt2 + l - 2)] = (f11 * f12 + f10 * f13);
//			paramArrayOfFloat1[(paramInt2 + l - 1)] = (f11 * f13 - (f10 * f12));
//		}
//		float f4 = f2 * (f8 + f1);
//		float f5 = f2 * (f9 + f1);
//		float f6 = f3 * (f10 - f1);
//		float f7 = f3 * (f11 - f1);
//		int i = i3;
//		j = i + i2;
//		k = j + i2;
//		l = k + i2;
//		int i4 = paramInt2 + i;
//		i5 = paramInt2 + j;
//		i6 = paramInt2 + k;
//		i7 = paramInt2 + l;
//		f12 = paramArrayOfFloat1[(i4 - 2)] + paramArrayOfFloat1[(i6 - 2)];
//		f13 = paramArrayOfFloat1[(i4 - 1)] + paramArrayOfFloat1[(i6 - 1)];
//		f14 = paramArrayOfFloat1[(i4 - 2)] - paramArrayOfFloat1[(i6 - 2)];
//		f15 = paramArrayOfFloat1[(i4 - 1)] - paramArrayOfFloat1[(i6 - 1)];
//		f16 = paramArrayOfFloat1[(i5 - 2)] + paramArrayOfFloat1[(i7 - 2)];
//		f17 = paramArrayOfFloat1[(i5 - 1)] + paramArrayOfFloat1[(i7 - 1)];
//		f18 = paramArrayOfFloat1[(i5 - 2)] - paramArrayOfFloat1[(i7 - 2)];
//		f19 = paramArrayOfFloat1[(i5 - 1)] - paramArrayOfFloat1[(i7 - 1)];
//		paramArrayOfFloat1[(i4 - 2)] = (f12 + f16);
//		paramArrayOfFloat1[(i4 - 1)] = (f13 + f17);
//		paramArrayOfFloat1[(i5 - 2)] = (f12 - f16);
//		paramArrayOfFloat1[(i5 - 1)] = (f13 - f17);
//		f12 = f14 - f19;
//		f13 = f15 + f18;
//		paramArrayOfFloat1[(i6 - 2)] = (f4 * f12 - (f5 * f13));
//		paramArrayOfFloat1[(i6 - 1)] = (f4 * f13 + f5 * f12);
//		f12 = f14 + f19;
//		f13 = f15 - f18;
//		paramArrayOfFloat1[(i7 - 2)] = (f6 * f12 + f7 * f13);
//		paramArrayOfFloat1[(i7 - 1)] = (f6 * f13 - (f7 * f12));
//		f12 = paramArrayOfFloat1[i4] + paramArrayOfFloat1[i6];
//		f13 = paramArrayOfFloat1[(i4 + 1)] + paramArrayOfFloat1[(i6 + 1)];
//		f14 = paramArrayOfFloat1[i4] - paramArrayOfFloat1[i6];
//		f15 = paramArrayOfFloat1[(i4 + 1)] - paramArrayOfFloat1[(i6 + 1)];
//		f16 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//		f17 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//		f18 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//		f19 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//		paramArrayOfFloat1[i4] = (f12 + f16);
//		paramArrayOfFloat1[(i4 + 1)] = (f13 + f17);
//		paramArrayOfFloat1[i5] = (f12 - f16);
//		paramArrayOfFloat1[(i5 + 1)] = (f13 - f17);
//		f12 = f14 - f19;
//		f13 = f15 + f18;
//		paramArrayOfFloat1[i6] = (f1 * (f12 - f13));
//		paramArrayOfFloat1[(i6 + 1)] = (f1 * (f13 + f12));
//		f12 = f14 + f19;
//		f13 = f15 - f18;
//		paramArrayOfFloat1[i7] = (-f1 * (f12 + f13));
//		paramArrayOfFloat1[(i7 + 1)] = (-f1 * (f13 - f12));
//		f12 = paramArrayOfFloat1[(i4 + 2)] + paramArrayOfFloat1[(i6 + 2)];
//		f13 = paramArrayOfFloat1[(i4 + 3)] + paramArrayOfFloat1[(i6 + 3)];
//		f14 = paramArrayOfFloat1[(i4 + 2)] - paramArrayOfFloat1[(i6 + 2)];
//		f15 = paramArrayOfFloat1[(i4 + 3)] - paramArrayOfFloat1[(i6 + 3)];
//		f16 = paramArrayOfFloat1[(i5 + 2)] + paramArrayOfFloat1[(i7 + 2)];
//		f17 = paramArrayOfFloat1[(i5 + 3)] + paramArrayOfFloat1[(i7 + 3)];
//		f18 = paramArrayOfFloat1[(i5 + 2)] - paramArrayOfFloat1[(i7 + 2)];
//		f19 = paramArrayOfFloat1[(i5 + 3)] - paramArrayOfFloat1[(i7 + 3)];
//		paramArrayOfFloat1[(i4 + 2)] = (f12 + f16);
//		paramArrayOfFloat1[(i4 + 3)] = (f13 + f17);
//		paramArrayOfFloat1[(i5 + 2)] = (f12 - f16);
//		paramArrayOfFloat1[(i5 + 3)] = (f13 - f17);
//		f12 = f14 - f19;
//		f13 = f15 + f18;
//		paramArrayOfFloat1[(i6 + 2)] = (f5 * f12 - (f4 * f13));
//		paramArrayOfFloat1[(i6 + 3)] = (f5 * f13 + f4 * f12);
//		f12 = f14 + f19;
//		f13 = f15 - f18;
//		paramArrayOfFloat1[(i7 + 2)] = (f7 * f12 + f6 * f13);
//		paramArrayOfFloat1[(i7 + 3)] = (f7 * f13 - (f6 * f12));
//	}
//
//	private void cftb1st(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, float[] paramArrayOfFloat2, int paramInt3) {
//		int i3 = paramInt1 >> 3;
//		int i2 = 2 * i3;
//		int j = i2;
//		int k = j + i2;
//		int l = k + i2;
//		int i5 = paramInt2 + j;
//		int i6 = paramInt2 + k;
//		int i7 = paramInt2 + l;
//		float f12 = paramArrayOfFloat1[paramInt2] + paramArrayOfFloat1[i6];
//		float f13 = -paramArrayOfFloat1[(paramInt2 + 1)]
//				- paramArrayOfFloat1[(i6 + 1)];
//		float f14 = paramArrayOfFloat1[paramInt2] - paramArrayOfFloat1[i6];
//		float f15 = -paramArrayOfFloat1[(paramInt2 + 1)]
//				+ paramArrayOfFloat1[(i6 + 1)];
//		float f16 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//		float f17 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//		float f18 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//		float f19 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//		paramArrayOfFloat1[paramInt2] = (f12 + f16);
//		paramArrayOfFloat1[(paramInt2 + 1)] = (f13 - f17);
//		paramArrayOfFloat1[i5] = (f12 - f16);
//		paramArrayOfFloat1[(i5 + 1)] = (f13 + f17);
//		paramArrayOfFloat1[i6] = (f14 + f19);
//		paramArrayOfFloat1[(i6 + 1)] = (f15 + f18);
//		paramArrayOfFloat1[i7] = (f14 - f19);
//		paramArrayOfFloat1[(i7 + 1)] = (f15 - f18);
//		float f1 = paramArrayOfFloat2[(paramInt3 + 1)];
//		float f2 = paramArrayOfFloat2[(paramInt3 + 2)];
//		float f3 = paramArrayOfFloat2[(paramInt3 + 3)];
//		float f8 = 1.0F;
//		float f9 = 0.0F;
//		float f10 = 1.0F;
//		float f11 = 0.0F;
//		int i1 = 0;
//		for (int i10 = 2; i10 < i3 - 2; i10 += 4) {
//			int i8 = paramInt3 + (i1 += 4);
//			f4 = f2 * (f8 + paramArrayOfFloat2[i8]);
//			f5 = f2 * (f9 + paramArrayOfFloat2[(i8 + 1)]);
//			f6 = f3 * (f10 + paramArrayOfFloat2[(i8 + 2)]);
//			f7 = f3 * (f11 + paramArrayOfFloat2[(i8 + 3)]);
//			f8 = paramArrayOfFloat2[i8];
//			f9 = paramArrayOfFloat2[(i8 + 1)];
//			f10 = paramArrayOfFloat2[(i8 + 2)];
//			f11 = paramArrayOfFloat2[(i8 + 3)];
//			j = i10 + i2;
//			k = j + i2;
//			l = k + i2;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			int i9 = paramInt2 + i10;
//			f12 = paramArrayOfFloat1[i9] + paramArrayOfFloat1[i6];
//			f13 = -paramArrayOfFloat1[(i9 + 1)] - paramArrayOfFloat1[(i6 + 1)];
//			f14 = paramArrayOfFloat1[i9] - paramArrayOfFloat1[(paramInt2 + k)];
//			f15 = -paramArrayOfFloat1[(i9 + 1)] + paramArrayOfFloat1[(i6 + 1)];
//			float f20 = paramArrayOfFloat1[(i9 + 2)]
//					+ paramArrayOfFloat1[(i6 + 2)];
//			float f21 = -paramArrayOfFloat1[(i9 + 3)]
//					- paramArrayOfFloat1[(i6 + 3)];
//			float f22 = paramArrayOfFloat1[(i9 + 2)]
//					- paramArrayOfFloat1[(i6 + 2)];
//			float f23 = -paramArrayOfFloat1[(i9 + 3)]
//					+ paramArrayOfFloat1[(i6 + 3)];
//			f16 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//			f17 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//			f18 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//			f19 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//			float f24 = paramArrayOfFloat1[(i5 + 2)]
//					+ paramArrayOfFloat1[(i7 + 2)];
//			float f25 = paramArrayOfFloat1[(i5 + 3)]
//					+ paramArrayOfFloat1[(i7 + 3)];
//			float f26 = paramArrayOfFloat1[(i5 + 2)]
//					- paramArrayOfFloat1[(i7 + 2)];
//			float f27 = paramArrayOfFloat1[(i5 + 3)]
//					- paramArrayOfFloat1[(i7 + 3)];
//			paramArrayOfFloat1[i9] = (f12 + f16);
//			paramArrayOfFloat1[(i9 + 1)] = (f13 - f17);
//			paramArrayOfFloat1[(i9 + 2)] = (f20 + f24);
//			paramArrayOfFloat1[(i9 + 3)] = (f21 - f25);
//			paramArrayOfFloat1[i5] = (f12 - f16);
//			paramArrayOfFloat1[(i5 + 1)] = (f13 + f17);
//			paramArrayOfFloat1[(i5 + 2)] = (f20 - f24);
//			paramArrayOfFloat1[(i5 + 3)] = (f21 + f25);
//			f12 = f14 + f19;
//			f13 = f15 + f18;
//			paramArrayOfFloat1[i6] = (f4 * f12 - (f5 * f13));
//			paramArrayOfFloat1[(i6 + 1)] = (f4 * f13 + f5 * f12);
//			f12 = f22 + f27;
//			f13 = f23 + f26;
//			paramArrayOfFloat1[(i6 + 2)] = (f8 * f12 - (f9 * f13));
//			paramArrayOfFloat1[(i6 + 3)] = (f8 * f13 + f9 * f12);
//			f12 = f14 - f19;
//			f13 = f15 - f18;
//			paramArrayOfFloat1[i7] = (f6 * f12 + f7 * f13);
//			paramArrayOfFloat1[(i7 + 1)] = (f6 * f13 - (f7 * f12));
//			f12 = f22 - f27;
//			f13 = f23 - f26;
//			paramArrayOfFloat1[(i7 + 2)] = (f10 * f12 + f11 * f13);
//			paramArrayOfFloat1[(i7 + 3)] = (f10 * f13 - (f11 * f12));
//			i = i2 - i10;
//			j = i + i2;
//			k = j + i2;
//			l = k + i2;
//			i4 = paramInt2 + i;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			f12 = paramArrayOfFloat1[i4] + paramArrayOfFloat1[i6];
//			f13 = -paramArrayOfFloat1[(i4 + 1)] - paramArrayOfFloat1[(i6 + 1)];
//			f14 = paramArrayOfFloat1[i4] - paramArrayOfFloat1[i6];
//			f15 = -paramArrayOfFloat1[(i4 + 1)] + paramArrayOfFloat1[(i6 + 1)];
//			f20 = paramArrayOfFloat1[(i4 - 2)] + paramArrayOfFloat1[(i6 - 2)];
//			f21 = -paramArrayOfFloat1[(i4 - 1)] - paramArrayOfFloat1[(i6 - 1)];
//			f22 = paramArrayOfFloat1[(i4 - 2)] - paramArrayOfFloat1[(i6 - 2)];
//			f23 = -paramArrayOfFloat1[(i4 - 1)] + paramArrayOfFloat1[(i6 - 1)];
//			f16 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//			f17 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//			f18 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//			f19 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//			f24 = paramArrayOfFloat1[(i5 - 2)] + paramArrayOfFloat1[(i7 - 2)];
//			f25 = paramArrayOfFloat1[(i5 - 1)] + paramArrayOfFloat1[(i7 - 1)];
//			f26 = paramArrayOfFloat1[(i5 - 2)] - paramArrayOfFloat1[(i7 - 2)];
//			f27 = paramArrayOfFloat1[(i5 - 1)] - paramArrayOfFloat1[(i7 - 1)];
//			paramArrayOfFloat1[i4] = (f12 + f16);
//			paramArrayOfFloat1[(i4 + 1)] = (f13 - f17);
//			paramArrayOfFloat1[(i4 - 2)] = (f20 + f24);
//			paramArrayOfFloat1[(i4 - 1)] = (f21 - f25);
//			paramArrayOfFloat1[i5] = (f12 - f16);
//			paramArrayOfFloat1[(i5 + 1)] = (f13 + f17);
//			paramArrayOfFloat1[(i5 - 2)] = (f20 - f24);
//			paramArrayOfFloat1[(i5 - 1)] = (f21 + f25);
//			f12 = f14 + f19;
//			f13 = f15 + f18;
//			paramArrayOfFloat1[i6] = (f5 * f12 - (f4 * f13));
//			paramArrayOfFloat1[(i6 + 1)] = (f5 * f13 + f4 * f12);
//			f12 = f22 + f27;
//			f13 = f23 + f26;
//			paramArrayOfFloat1[(i6 - 2)] = (f9 * f12 - (f8 * f13));
//			paramArrayOfFloat1[(i6 - 1)] = (f9 * f13 + f8 * f12);
//			f12 = f14 - f19;
//			f13 = f15 - f18;
//			paramArrayOfFloat1[i7] = (f7 * f12 + f6 * f13);
//			paramArrayOfFloat1[(i7 + 1)] = (f7 * f13 - (f6 * f12));
//			f12 = f22 - f27;
//			f13 = f23 - f26;
//			paramArrayOfFloat1[(i7 - 2)] = (f11 * f12 + f10 * f13);
//			paramArrayOfFloat1[(i7 - 1)] = (f11 * f13 - (f10 * f12));
//		}
//		float f4 = f2 * (f8 + f1);
//		float f5 = f2 * (f9 + f1);
//		float f6 = f3 * (f10 - f1);
//		float f7 = f3 * (f11 - f1);
//		int i = i3;
//		j = i + i2;
//		k = j + i2;
//		l = k + i2;
//		int i4 = paramInt2 + i;
//		i5 = paramInt2 + j;
//		i6 = paramInt2 + k;
//		i7 = paramInt2 + l;
//		f12 = paramArrayOfFloat1[(i4 - 2)] + paramArrayOfFloat1[(i6 - 2)];
//		f13 = -paramArrayOfFloat1[(i4 - 1)] - paramArrayOfFloat1[(i6 - 1)];
//		f14 = paramArrayOfFloat1[(i4 - 2)] - paramArrayOfFloat1[(i6 - 2)];
//		f15 = -paramArrayOfFloat1[(i4 - 1)] + paramArrayOfFloat1[(i6 - 1)];
//		f16 = paramArrayOfFloat1[(i5 - 2)] + paramArrayOfFloat1[(i7 - 2)];
//		f17 = paramArrayOfFloat1[(i5 - 1)] + paramArrayOfFloat1[(i7 - 1)];
//		f18 = paramArrayOfFloat1[(i5 - 2)] - paramArrayOfFloat1[(i7 - 2)];
//		f19 = paramArrayOfFloat1[(i5 - 1)] - paramArrayOfFloat1[(i7 - 1)];
//		paramArrayOfFloat1[(i4 - 2)] = (f12 + f16);
//		paramArrayOfFloat1[(i4 - 1)] = (f13 - f17);
//		paramArrayOfFloat1[(i5 - 2)] = (f12 - f16);
//		paramArrayOfFloat1[(i5 - 1)] = (f13 + f17);
//		f12 = f14 + f19;
//		f13 = f15 + f18;
//		paramArrayOfFloat1[(i6 - 2)] = (f4 * f12 - (f5 * f13));
//		paramArrayOfFloat1[(i6 - 1)] = (f4 * f13 + f5 * f12);
//		f12 = f14 - f19;
//		f13 = f15 - f18;
//		paramArrayOfFloat1[(i7 - 2)] = (f6 * f12 + f7 * f13);
//		paramArrayOfFloat1[(i7 - 1)] = (f6 * f13 - (f7 * f12));
//		f12 = paramArrayOfFloat1[i4] + paramArrayOfFloat1[i6];
//		f13 = -paramArrayOfFloat1[(i4 + 1)] - paramArrayOfFloat1[(i6 + 1)];
//		f14 = paramArrayOfFloat1[i4] - paramArrayOfFloat1[i6];
//		f15 = -paramArrayOfFloat1[(i4 + 1)] + paramArrayOfFloat1[(i6 + 1)];
//		f16 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//		f17 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//		f18 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//		f19 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//		paramArrayOfFloat1[i4] = (f12 + f16);
//		paramArrayOfFloat1[(i4 + 1)] = (f13 - f17);
//		paramArrayOfFloat1[i5] = (f12 - f16);
//		paramArrayOfFloat1[(i5 + 1)] = (f13 + f17);
//		f12 = f14 + f19;
//		f13 = f15 + f18;
//		paramArrayOfFloat1[i6] = (f1 * (f12 - f13));
//		paramArrayOfFloat1[(i6 + 1)] = (f1 * (f13 + f12));
//		f12 = f14 - f19;
//		f13 = f15 - f18;
//		paramArrayOfFloat1[i7] = (-f1 * (f12 + f13));
//		paramArrayOfFloat1[(i7 + 1)] = (-f1 * (f13 - f12));
//		f12 = paramArrayOfFloat1[(i4 + 2)] + paramArrayOfFloat1[(i6 + 2)];
//		f13 = -paramArrayOfFloat1[(i4 + 3)] - paramArrayOfFloat1[(i6 + 3)];
//		f14 = paramArrayOfFloat1[(i4 + 2)] - paramArrayOfFloat1[(i6 + 2)];
//		f15 = -paramArrayOfFloat1[(i4 + 3)] + paramArrayOfFloat1[(i6 + 3)];
//		f16 = paramArrayOfFloat1[(i5 + 2)] + paramArrayOfFloat1[(i7 + 2)];
//		f17 = paramArrayOfFloat1[(i5 + 3)] + paramArrayOfFloat1[(i7 + 3)];
//		f18 = paramArrayOfFloat1[(i5 + 2)] - paramArrayOfFloat1[(i7 + 2)];
//		f19 = paramArrayOfFloat1[(i5 + 3)] - paramArrayOfFloat1[(i7 + 3)];
//		paramArrayOfFloat1[(i4 + 2)] = (f12 + f16);
//		paramArrayOfFloat1[(i4 + 3)] = (f13 - f17);
//		paramArrayOfFloat1[(i5 + 2)] = (f12 - f16);
//		paramArrayOfFloat1[(i5 + 3)] = (f13 + f17);
//		f12 = f14 + f19;
//		f13 = f15 + f18;
//		paramArrayOfFloat1[(i6 + 2)] = (f5 * f12 - (f4 * f13));
//		paramArrayOfFloat1[(i6 + 3)] = (f5 * f13 + f4 * f12);
//		f12 = f14 - f19;
//		f13 = f15 - f18;
//		paramArrayOfFloat1[(i7 + 2)] = (f7 * f12 + f6 * f13);
//		paramArrayOfFloat1[(i7 + 3)] = (f7 * f13 - (f6 * f12));
//	}
//
//	private void cftrec4_th(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, int paramInt3, float[] paramArrayOfFloat2) {
//		int l = 0;
//		int k = 2;
//		int i = 0;
//		int j = paramInt1 >> 1;
//		if (paramInt1 > ConcurrencyUtils.getThreadsBeginN_1D_FFT_4Threads()) {
//			k = 4;
//			i = 1;
//			j >>= 1;
//		}
//		Future[] arrayOfFuture = new Future[k];
//		int i1 = j;
//		for (int i2 = 0; i2 < k; ++i2) {
//			int i3 = paramInt2 + i2 * j;
//			if (i2 != i)
//				arrayOfFuture[(l++)] = ConcurrencyUtils.submit(new Runnable(i3,
//						i1, paramInt1, paramArrayOfFloat1, paramArrayOfFloat2,
//						paramInt3) {
//					public void run() {
//						int l = this.val$firstIdx + this.val$mf;
//						int k = this.val$n;
//						while (k > 512) {
//							k >>= 2;
//							FloatDCT_1D.this.cftmdl1(k, this.val$a, l - k,
//									this.val$w, this.val$nw - (k >> 1));
//						}
//						FloatDCT_1D.this.cftleaf(k, 1, this.val$a, l - k,
//								this.val$nw, this.val$w);
//						int j = 0;
//						int i1 = this.val$firstIdx - k;
//						int i2 = this.val$mf - k;
//						while (i2 > 0) {
//							int i = FloatDCT_1D.this.cfttree(k, i2, ++j,
//									this.val$a, this.val$firstIdx, this.val$nw,
//									this.val$w);
//							FloatDCT_1D.this.cftleaf(k, i, this.val$a, i1 + i2,
//									this.val$nw, this.val$w);
//							i2 -= k;
//						}
//					}
//				});
//			else
//				arrayOfFuture[(l++)] = ConcurrencyUtils.submit(new Runnable(i3,
//						i1, paramInt1, paramArrayOfFloat1, paramArrayOfFloat2,
//						paramInt3) {
//					public void run() {
//						int l = this.val$firstIdx + this.val$mf;
//						int j = 1;
//						int k = this.val$n;
//						while (k > 512) {
//							k >>= 2;
//							j <<= 2;
//							FloatDCT_1D.this.cftmdl2(k, this.val$a, l - k,
//									this.val$w, this.val$nw - k);
//						}
//						FloatDCT_1D.this.cftleaf(k, 0, this.val$a, l - k,
//								this.val$nw, this.val$w);
//						j >>= 1;
//						int i1 = this.val$firstIdx - k;
//						int i2 = this.val$mf - k;
//						while (i2 > 0) {
//							int i = FloatDCT_1D.this.cfttree(k, i2, ++j,
//									this.val$a, this.val$firstIdx, this.val$nw,
//									this.val$w);
//							FloatDCT_1D.this.cftleaf(k, i, this.val$a, i1 + i2,
//									this.val$nw, this.val$w);
//							i2 -= k;
//						}
//					}
//				});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private void cftrec4(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, int paramInt3, float[] paramArrayOfFloat2) {
//		int l = paramInt2 + paramInt1;
//		int k = paramInt1;
//		while (k > 512) {
//			k >>= 2;
//			cftmdl1(k, paramArrayOfFloat1, l - k, paramArrayOfFloat2, paramInt3
//					- (k >> 1));
//		}
//		cftleaf(k, 1, paramArrayOfFloat1, l - k, paramInt3, paramArrayOfFloat2);
//		int j = 0;
//		int i1 = paramInt2 - k;
//		int i2 = paramInt1 - k;
//		while (i2 > 0) {
//			int i = cfttree(k, i2, ++j, paramArrayOfFloat1, paramInt2,
//					paramInt3, paramArrayOfFloat2);
//			cftleaf(k, i, paramArrayOfFloat1, i1 + i2, paramInt3,
//					paramArrayOfFloat2);
//			i2 -= k;
//		}
//	}
//
//	private int cfttree(int paramInt1, int paramInt2, int paramInt3,
//			float[] paramArrayOfFloat1, int paramInt4, int paramInt5,
//			float[] paramArrayOfFloat2) {
//		int l = paramInt4 - paramInt1;
//		int j;
//		if ((paramInt3 & 0x3) != 0) {
//			j = paramInt3 & 0x1;
//			if (j != 0)
//				cftmdl1(paramInt1, paramArrayOfFloat1, l + paramInt2,
//						paramArrayOfFloat2, paramInt5 - (paramInt1 >> 1));
//			else
//				cftmdl2(paramInt1, paramArrayOfFloat1, l + paramInt2,
//						paramArrayOfFloat2, paramInt5 - paramInt1);
//		} else {
//			int k = paramInt1;
//			int i = paramInt3;
//			while ((i & 0x3) == 0) {
//				k <<= 2;
//				i >>= 2;
//			}
//			j = i & 0x1;
//			int i1 = paramInt4 + paramInt2;
//			if (j != 0)
//				while (true) {
//					if (k <= 128)
//						break label185;
//					cftmdl1(k, paramArrayOfFloat1, i1 - k, paramArrayOfFloat2,
//							paramInt5 - (k >> 1));
//					k >>= 2;
//				}
//			while (k > 128) {
//				cftmdl2(k, paramArrayOfFloat1, i1 - k, paramArrayOfFloat2,
//						paramInt5 - k);
//				k >>= 2;
//			}
//		}
//		label185: return j;
//	}
//
//	private void cftleaf(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3, int paramInt4,
//			float[] paramArrayOfFloat2) {
//		if (paramInt1 == 512) {
//			cftmdl1(128, paramArrayOfFloat1, paramInt3, paramArrayOfFloat2,
//					paramInt4 - 64);
//			cftf161(paramArrayOfFloat1, paramInt3, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf162(paramArrayOfFloat1, paramInt3 + 32, paramArrayOfFloat2,
//					paramInt4 - 32);
//			cftf161(paramArrayOfFloat1, paramInt3 + 64, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf161(paramArrayOfFloat1, paramInt3 + 96, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftmdl2(128, paramArrayOfFloat1, paramInt3 + 128,
//					paramArrayOfFloat2, paramInt4 - 128);
//			cftf161(paramArrayOfFloat1, paramInt3 + 128, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf162(paramArrayOfFloat1, paramInt3 + 160, paramArrayOfFloat2,
//					paramInt4 - 32);
//			cftf161(paramArrayOfFloat1, paramInt3 + 192, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf162(paramArrayOfFloat1, paramInt3 + 224, paramArrayOfFloat2,
//					paramInt4 - 32);
//			cftmdl1(128, paramArrayOfFloat1, paramInt3 + 256,
//					paramArrayOfFloat2, paramInt4 - 64);
//			cftf161(paramArrayOfFloat1, paramInt3 + 256, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf162(paramArrayOfFloat1, paramInt3 + 288, paramArrayOfFloat2,
//					paramInt4 - 32);
//			cftf161(paramArrayOfFloat1, paramInt3 + 320, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf161(paramArrayOfFloat1, paramInt3 + 352, paramArrayOfFloat2,
//					paramInt4 - 8);
//			if (paramInt2 != 0) {
//				cftmdl1(128, paramArrayOfFloat1, paramInt3 + 384,
//						paramArrayOfFloat2, paramInt4 - 64);
//				cftf161(paramArrayOfFloat1, paramInt3 + 480,
//						paramArrayOfFloat2, paramInt4 - 8);
//			} else {
//				cftmdl2(128, paramArrayOfFloat1, paramInt3 + 384,
//						paramArrayOfFloat2, paramInt4 - 128);
//				cftf162(paramArrayOfFloat1, paramInt3 + 480,
//						paramArrayOfFloat2, paramInt4 - 32);
//			}
//			cftf161(paramArrayOfFloat1, paramInt3 + 384, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf162(paramArrayOfFloat1, paramInt3 + 416, paramArrayOfFloat2,
//					paramInt4 - 32);
//			cftf161(paramArrayOfFloat1, paramInt3 + 448, paramArrayOfFloat2,
//					paramInt4 - 8);
//		} else {
//			cftmdl1(64, paramArrayOfFloat1, paramInt3, paramArrayOfFloat2,
//					paramInt4 - 32);
//			cftf081(paramArrayOfFloat1, paramInt3, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf082(paramArrayOfFloat1, paramInt3 + 16, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfFloat1, paramInt3 + 32, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfFloat1, paramInt3 + 48, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftmdl2(64, paramArrayOfFloat1, paramInt3 + 64, paramArrayOfFloat2,
//					paramInt4 - 64);
//			cftf081(paramArrayOfFloat1, paramInt3 + 64, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf082(paramArrayOfFloat1, paramInt3 + 80, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfFloat1, paramInt3 + 96, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf082(paramArrayOfFloat1, paramInt3 + 112, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftmdl1(64, paramArrayOfFloat1, paramInt3 + 128,
//					paramArrayOfFloat2, paramInt4 - 32);
//			cftf081(paramArrayOfFloat1, paramInt3 + 128, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf082(paramArrayOfFloat1, paramInt3 + 144, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfFloat1, paramInt3 + 160, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfFloat1, paramInt3 + 176, paramArrayOfFloat2,
//					paramInt4 - 8);
//			if (paramInt2 != 0) {
//				cftmdl1(64, paramArrayOfFloat1, paramInt3 + 192,
//						paramArrayOfFloat2, paramInt4 - 32);
//				cftf081(paramArrayOfFloat1, paramInt3 + 240,
//						paramArrayOfFloat2, paramInt4 - 8);
//			} else {
//				cftmdl2(64, paramArrayOfFloat1, paramInt3 + 192,
//						paramArrayOfFloat2, paramInt4 - 64);
//				cftf082(paramArrayOfFloat1, paramInt3 + 240,
//						paramArrayOfFloat2, paramInt4 - 8);
//			}
//			cftf081(paramArrayOfFloat1, paramInt3 + 192, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf082(paramArrayOfFloat1, paramInt3 + 208, paramArrayOfFloat2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfFloat1, paramInt3 + 224, paramArrayOfFloat2,
//					paramInt4 - 8);
//		}
//	}
//
//	private void cftmdl1(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, float[] paramArrayOfFloat2, int paramInt3) {
//		int i3 = paramInt1 >> 3;
//		int i2 = 2 * i3;
//		int j = i2;
//		int k = j + i2;
//		int l = k + i2;
//		int i5 = paramInt2 + j;
//		int i6 = paramInt2 + k;
//		int i7 = paramInt2 + l;
//		float f6 = paramArrayOfFloat1[paramInt2] + paramArrayOfFloat1[i6];
//		float f7 = paramArrayOfFloat1[(paramInt2 + 1)]
//				+ paramArrayOfFloat1[(i6 + 1)];
//		float f8 = paramArrayOfFloat1[paramInt2] - paramArrayOfFloat1[i6];
//		float f9 = paramArrayOfFloat1[(paramInt2 + 1)]
//				- paramArrayOfFloat1[(i6 + 1)];
//		float f10 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//		float f11 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//		float f12 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//		float f13 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//		paramArrayOfFloat1[paramInt2] = (f6 + f10);
//		paramArrayOfFloat1[(paramInt2 + 1)] = (f7 + f11);
//		paramArrayOfFloat1[i5] = (f6 - f10);
//		paramArrayOfFloat1[(i5 + 1)] = (f7 - f11);
//		paramArrayOfFloat1[i6] = (f8 - f13);
//		paramArrayOfFloat1[(i6 + 1)] = (f9 + f12);
//		paramArrayOfFloat1[i7] = (f8 + f13);
//		paramArrayOfFloat1[(i7 + 1)] = (f9 - f12);
//		float f1 = paramArrayOfFloat2[(paramInt3 + 1)];
//		int i1 = 0;
//		for (int i10 = 2; i10 < i3; i10 += 2) {
//			int i8 = paramInt3 + (i1 += 4);
//			float f2 = paramArrayOfFloat2[i8];
//			float f3 = paramArrayOfFloat2[(i8 + 1)];
//			float f4 = paramArrayOfFloat2[(i8 + 2)];
//			float f5 = paramArrayOfFloat2[(i8 + 3)];
//			j = i10 + i2;
//			k = j + i2;
//			l = k + i2;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			int i9 = paramInt2 + i10;
//			f6 = paramArrayOfFloat1[i9] + paramArrayOfFloat1[i6];
//			f7 = paramArrayOfFloat1[(i9 + 1)] + paramArrayOfFloat1[(i6 + 1)];
//			f8 = paramArrayOfFloat1[i9] - paramArrayOfFloat1[i6];
//			f9 = paramArrayOfFloat1[(i9 + 1)] - paramArrayOfFloat1[(i6 + 1)];
//			f10 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//			f11 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//			f12 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//			f13 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//			paramArrayOfFloat1[i9] = (f6 + f10);
//			paramArrayOfFloat1[(i9 + 1)] = (f7 + f11);
//			paramArrayOfFloat1[i5] = (f6 - f10);
//			paramArrayOfFloat1[(i5 + 1)] = (f7 - f11);
//			f6 = f8 - f13;
//			f7 = f9 + f12;
//			paramArrayOfFloat1[i6] = (f2 * f6 - (f3 * f7));
//			paramArrayOfFloat1[(i6 + 1)] = (f2 * f7 + f3 * f6);
//			f6 = f8 + f13;
//			f7 = f9 - f12;
//			paramArrayOfFloat1[i7] = (f4 * f6 + f5 * f7);
//			paramArrayOfFloat1[(i7 + 1)] = (f4 * f7 - (f5 * f6));
//			i = i2 - i10;
//			j = i + i2;
//			k = j + i2;
//			l = k + i2;
//			i4 = paramInt2 + i;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			f6 = paramArrayOfFloat1[i4] + paramArrayOfFloat1[i6];
//			f7 = paramArrayOfFloat1[(i4 + 1)] + paramArrayOfFloat1[(i6 + 1)];
//			f8 = paramArrayOfFloat1[i4] - paramArrayOfFloat1[i6];
//			f9 = paramArrayOfFloat1[(i4 + 1)] - paramArrayOfFloat1[(i6 + 1)];
//			f10 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//			f11 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//			f12 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//			f13 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//			paramArrayOfFloat1[i4] = (f6 + f10);
//			paramArrayOfFloat1[(i4 + 1)] = (f7 + f11);
//			paramArrayOfFloat1[i5] = (f6 - f10);
//			paramArrayOfFloat1[(i5 + 1)] = (f7 - f11);
//			f6 = f8 - f13;
//			f7 = f9 + f12;
//			paramArrayOfFloat1[i6] = (f3 * f6 - (f2 * f7));
//			paramArrayOfFloat1[(i6 + 1)] = (f3 * f7 + f2 * f6);
//			f6 = f8 + f13;
//			f7 = f9 - f12;
//			paramArrayOfFloat1[i7] = (f5 * f6 + f4 * f7);
//			paramArrayOfFloat1[(i7 + 1)] = (f5 * f7 - (f4 * f6));
//		}
//		int i = i3;
//		j = i + i2;
//		k = j + i2;
//		l = k + i2;
//		int i4 = paramInt2 + i;
//		i5 = paramInt2 + j;
//		i6 = paramInt2 + k;
//		i7 = paramInt2 + l;
//		f6 = paramArrayOfFloat1[i4] + paramArrayOfFloat1[i6];
//		f7 = paramArrayOfFloat1[(i4 + 1)] + paramArrayOfFloat1[(i6 + 1)];
//		f8 = paramArrayOfFloat1[i4] - paramArrayOfFloat1[i6];
//		f9 = paramArrayOfFloat1[(i4 + 1)] - paramArrayOfFloat1[(i6 + 1)];
//		f10 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[i7];
//		f11 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[(i7 + 1)];
//		f12 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[i7];
//		f13 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[(i7 + 1)];
//		paramArrayOfFloat1[i4] = (f6 + f10);
//		paramArrayOfFloat1[(i4 + 1)] = (f7 + f11);
//		paramArrayOfFloat1[i5] = (f6 - f10);
//		paramArrayOfFloat1[(i5 + 1)] = (f7 - f11);
//		f6 = f8 - f13;
//		f7 = f9 + f12;
//		paramArrayOfFloat1[i6] = (f1 * (f6 - f7));
//		paramArrayOfFloat1[(i6 + 1)] = (f1 * (f7 + f6));
//		f6 = f8 + f13;
//		f7 = f9 - f12;
//		paramArrayOfFloat1[i7] = (-f1 * (f6 + f7));
//		paramArrayOfFloat1[(i7 + 1)] = (-f1 * (f7 - f6));
//	}
//
//	private void cftmdl2(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, float[] paramArrayOfFloat2, int paramInt3) {
//		int i4 = paramInt1 >> 3;
//		int i3 = 2 * i4;
//		float f1 = paramArrayOfFloat2[(paramInt3 + 1)];
//		int j = i3;
//		int k = j + i3;
//		int l = k + i3;
//		int i6 = paramInt2 + j;
//		int i7 = paramInt2 + k;
//		int i8 = paramInt2 + l;
//		float f10 = paramArrayOfFloat1[paramInt2]
//				- paramArrayOfFloat1[(i7 + 1)];
//		float f11 = paramArrayOfFloat1[(paramInt2 + 1)]
//				+ paramArrayOfFloat1[i7];
//		float f12 = paramArrayOfFloat1[paramInt2]
//				+ paramArrayOfFloat1[(i7 + 1)];
//		float f13 = paramArrayOfFloat1[(paramInt2 + 1)]
//				- paramArrayOfFloat1[i7];
//		float f14 = paramArrayOfFloat1[i6] - paramArrayOfFloat1[(i8 + 1)];
//		float f15 = paramArrayOfFloat1[(i6 + 1)] + paramArrayOfFloat1[i8];
//		float f16 = paramArrayOfFloat1[i6] + paramArrayOfFloat1[(i8 + 1)];
//		float f17 = paramArrayOfFloat1[(i6 + 1)] - paramArrayOfFloat1[i8];
//		float f18 = f1 * (f14 - f15);
//		float f19 = f1 * (f15 + f14);
//		paramArrayOfFloat1[paramInt2] = (f10 + f18);
//		paramArrayOfFloat1[(paramInt2 + 1)] = (f11 + f19);
//		paramArrayOfFloat1[i6] = (f10 - f18);
//		paramArrayOfFloat1[(i6 + 1)] = (f11 - f19);
//		f18 = f1 * (f16 - f17);
//		f19 = f1 * (f17 + f16);
//		paramArrayOfFloat1[i7] = (f12 - f19);
//		paramArrayOfFloat1[(i7 + 1)] = (f13 + f18);
//		paramArrayOfFloat1[i8] = (f12 + f19);
//		paramArrayOfFloat1[(i8 + 1)] = (f13 - f18);
//		int i1 = 0;
//		int i2 = 2 * i3;
//		for (int i12 = 2; i12 < i4; i12 += 2) {
//			int i9 = paramInt3 + (i1 += 4);
//			f2 = paramArrayOfFloat2[i9];
//			f3 = paramArrayOfFloat2[(i9 + 1)];
//			float f4 = paramArrayOfFloat2[(i9 + 2)];
//			float f5 = paramArrayOfFloat2[(i9 + 3)];
//			int i10 = paramInt3 + (i2 -= 4);
//			float f7 = paramArrayOfFloat2[i10];
//			float f6 = paramArrayOfFloat2[(i10 + 1)];
//			float f9 = paramArrayOfFloat2[(i10 + 2)];
//			float f8 = paramArrayOfFloat2[(i10 + 3)];
//			j = i12 + i3;
//			k = j + i3;
//			l = k + i3;
//			i6 = paramInt2 + j;
//			i7 = paramInt2 + k;
//			i8 = paramInt2 + l;
//			int i11 = paramInt2 + i12;
//			f10 = paramArrayOfFloat1[i11] - paramArrayOfFloat1[(i7 + 1)];
//			f11 = paramArrayOfFloat1[(i11 + 1)] + paramArrayOfFloat1[i7];
//			f12 = paramArrayOfFloat1[i11] + paramArrayOfFloat1[(i7 + 1)];
//			f13 = paramArrayOfFloat1[(i11 + 1)] - paramArrayOfFloat1[i7];
//			f14 = paramArrayOfFloat1[i6] - paramArrayOfFloat1[(i8 + 1)];
//			f15 = paramArrayOfFloat1[(i6 + 1)] + paramArrayOfFloat1[i8];
//			f16 = paramArrayOfFloat1[i6] + paramArrayOfFloat1[(i8 + 1)];
//			f17 = paramArrayOfFloat1[(i6 + 1)] - paramArrayOfFloat1[i8];
//			f18 = f2 * f10 - (f3 * f11);
//			f19 = f2 * f11 + f3 * f10;
//			f20 = f6 * f14 - (f7 * f15);
//			f21 = f6 * f15 + f7 * f14;
//			paramArrayOfFloat1[i11] = (f18 + f20);
//			paramArrayOfFloat1[(i11 + 1)] = (f19 + f21);
//			paramArrayOfFloat1[i6] = (f18 - f20);
//			paramArrayOfFloat1[(i6 + 1)] = (f19 - f21);
//			f18 = f4 * f12 + f5 * f13;
//			f19 = f4 * f13 - (f5 * f12);
//			f20 = f8 * f16 + f9 * f17;
//			f21 = f8 * f17 - (f9 * f16);
//			paramArrayOfFloat1[i7] = (f18 + f20);
//			paramArrayOfFloat1[(i7 + 1)] = (f19 + f21);
//			paramArrayOfFloat1[i8] = (f18 - f20);
//			paramArrayOfFloat1[(i8 + 1)] = (f19 - f21);
//			i = i3 - i12;
//			j = i + i3;
//			k = j + i3;
//			l = k + i3;
//			i5 = paramInt2 + i;
//			i6 = paramInt2 + j;
//			i7 = paramInt2 + k;
//			i8 = paramInt2 + l;
//			f10 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[(i7 + 1)];
//			f11 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[i7];
//			f12 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[(i7 + 1)];
//			f13 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[i7];
//			f14 = paramArrayOfFloat1[i6] - paramArrayOfFloat1[(i8 + 1)];
//			f15 = paramArrayOfFloat1[(i6 + 1)] + paramArrayOfFloat1[i8];
//			f16 = paramArrayOfFloat1[i6] + paramArrayOfFloat1[(i8 + 1)];
//			f17 = paramArrayOfFloat1[(i6 + 1)] - paramArrayOfFloat1[i8];
//			f18 = f7 * f10 - (f6 * f11);
//			f19 = f7 * f11 + f6 * f10;
//			f20 = f3 * f14 - (f2 * f15);
//			f21 = f3 * f15 + f2 * f14;
//			paramArrayOfFloat1[i5] = (f18 + f20);
//			paramArrayOfFloat1[(i5 + 1)] = (f19 + f21);
//			paramArrayOfFloat1[i6] = (f18 - f20);
//			paramArrayOfFloat1[(i6 + 1)] = (f19 - f21);
//			f18 = f9 * f12 + f8 * f13;
//			f19 = f9 * f13 - (f8 * f12);
//			f20 = f5 * f16 + f4 * f17;
//			f21 = f5 * f17 - (f4 * f16);
//			paramArrayOfFloat1[i7] = (f18 + f20);
//			paramArrayOfFloat1[(i7 + 1)] = (f19 + f21);
//			paramArrayOfFloat1[i8] = (f18 - f20);
//			paramArrayOfFloat1[(i8 + 1)] = (f19 - f21);
//		}
//		float f2 = paramArrayOfFloat2[(paramInt3 + i3)];
//		float f3 = paramArrayOfFloat2[(paramInt3 + i3 + 1)];
//		int i = i4;
//		j = i + i3;
//		k = j + i3;
//		l = k + i3;
//		int i5 = paramInt2 + i;
//		i6 = paramInt2 + j;
//		i7 = paramInt2 + k;
//		i8 = paramInt2 + l;
//		f10 = paramArrayOfFloat1[i5] - paramArrayOfFloat1[(i7 + 1)];
//		f11 = paramArrayOfFloat1[(i5 + 1)] + paramArrayOfFloat1[i7];
//		f12 = paramArrayOfFloat1[i5] + paramArrayOfFloat1[(i7 + 1)];
//		f13 = paramArrayOfFloat1[(i5 + 1)] - paramArrayOfFloat1[i7];
//		f14 = paramArrayOfFloat1[i6] - paramArrayOfFloat1[(i8 + 1)];
//		f15 = paramArrayOfFloat1[(i6 + 1)] + paramArrayOfFloat1[i8];
//		f16 = paramArrayOfFloat1[i6] + paramArrayOfFloat1[(i8 + 1)];
//		f17 = paramArrayOfFloat1[(i6 + 1)] - paramArrayOfFloat1[i8];
//		f18 = f2 * f10 - (f3 * f11);
//		f19 = f2 * f11 + f3 * f10;
//		float f20 = f3 * f14 - (f2 * f15);
//		float f21 = f3 * f15 + f2 * f14;
//		paramArrayOfFloat1[i5] = (f18 + f20);
//		paramArrayOfFloat1[(i5 + 1)] = (f19 + f21);
//		paramArrayOfFloat1[i6] = (f18 - f20);
//		paramArrayOfFloat1[(i6 + 1)] = (f19 - f21);
//		f18 = f3 * f12 - (f2 * f13);
//		f19 = f3 * f13 + f2 * f12;
//		f20 = f2 * f16 - (f3 * f17);
//		f21 = f2 * f17 + f3 * f16;
//		paramArrayOfFloat1[i7] = (f18 - f20);
//		paramArrayOfFloat1[(i7 + 1)] = (f19 - f21);
//		paramArrayOfFloat1[i8] = (f18 + f20);
//		paramArrayOfFloat1[(i8 + 1)] = (f19 + f21);
//	}
//
//	private void cftfx41(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, int paramInt3, float[] paramArrayOfFloat2) {
//		if (paramInt1 == 128) {
//			cftf161(paramArrayOfFloat1, paramInt2, paramArrayOfFloat2,
//					paramInt3 - 8);
//			cftf162(paramArrayOfFloat1, paramInt2 + 32, paramArrayOfFloat2,
//					paramInt3 - 32);
//			cftf161(paramArrayOfFloat1, paramInt2 + 64, paramArrayOfFloat2,
//					paramInt3 - 8);
//			cftf161(paramArrayOfFloat1, paramInt2 + 96, paramArrayOfFloat2,
//					paramInt3 - 8);
//		} else {
//			cftf081(paramArrayOfFloat1, paramInt2, paramArrayOfFloat2,
//					paramInt3 - 8);
//			cftf082(paramArrayOfFloat1, paramInt2 + 16, paramArrayOfFloat2,
//					paramInt3 - 8);
//			cftf081(paramArrayOfFloat1, paramInt2 + 32, paramArrayOfFloat2,
//					paramInt3 - 8);
//			cftf081(paramArrayOfFloat1, paramInt2 + 48, paramArrayOfFloat2,
//					paramInt3 - 8);
//		}
//	}
//
//	private void cftf161(float[] paramArrayOfFloat1, int paramInt1,
//			float[] paramArrayOfFloat2, int paramInt2) {
//		float f1 = paramArrayOfFloat2[(paramInt2 + 1)];
//		float f2 = paramArrayOfFloat2[(paramInt2 + 2)];
//		float f3 = paramArrayOfFloat2[(paramInt2 + 3)];
//		float f4 = paramArrayOfFloat1[paramInt1]
//				+ paramArrayOfFloat1[(paramInt1 + 16)];
//		float f5 = paramArrayOfFloat1[(paramInt1 + 1)]
//				+ paramArrayOfFloat1[(paramInt1 + 17)];
//		float f6 = paramArrayOfFloat1[paramInt1]
//				- paramArrayOfFloat1[(paramInt1 + 16)];
//		float f7 = paramArrayOfFloat1[(paramInt1 + 1)]
//				- paramArrayOfFloat1[(paramInt1 + 17)];
//		float f8 = paramArrayOfFloat1[(paramInt1 + 8)]
//				+ paramArrayOfFloat1[(paramInt1 + 24)];
//		float f9 = paramArrayOfFloat1[(paramInt1 + 9)]
//				+ paramArrayOfFloat1[(paramInt1 + 25)];
//		float f10 = paramArrayOfFloat1[(paramInt1 + 8)]
//				- paramArrayOfFloat1[(paramInt1 + 24)];
//		float f11 = paramArrayOfFloat1[(paramInt1 + 9)]
//				- paramArrayOfFloat1[(paramInt1 + 25)];
//		float f12 = f4 + f8;
//		float f13 = f5 + f9;
//		float f20 = f4 - f8;
//		float f21 = f5 - f9;
//		float f28 = f6 - f11;
//		float f29 = f7 + f10;
//		float f36 = f6 + f11;
//		float f37 = f7 - f10;
//		f4 = paramArrayOfFloat1[(paramInt1 + 2)]
//				+ paramArrayOfFloat1[(paramInt1 + 18)];
//		f5 = paramArrayOfFloat1[(paramInt1 + 3)]
//				+ paramArrayOfFloat1[(paramInt1 + 19)];
//		f6 = paramArrayOfFloat1[(paramInt1 + 2)]
//				- paramArrayOfFloat1[(paramInt1 + 18)];
//		f7 = paramArrayOfFloat1[(paramInt1 + 3)]
//				- paramArrayOfFloat1[(paramInt1 + 19)];
//		f8 = paramArrayOfFloat1[(paramInt1 + 10)]
//				+ paramArrayOfFloat1[(paramInt1 + 26)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 11)]
//				+ paramArrayOfFloat1[(paramInt1 + 27)];
//		f10 = paramArrayOfFloat1[(paramInt1 + 10)]
//				- paramArrayOfFloat1[(paramInt1 + 26)];
//		f11 = paramArrayOfFloat1[(paramInt1 + 11)]
//				- paramArrayOfFloat1[(paramInt1 + 27)];
//		float f14 = f4 + f8;
//		float f15 = f5 + f9;
//		float f22 = f4 - f8;
//		float f23 = f5 - f9;
//		f4 = f6 - f11;
//		f5 = f7 + f10;
//		float f30 = f2 * f4 - (f3 * f5);
//		float f31 = f2 * f5 + f3 * f4;
//		f4 = f6 + f11;
//		f5 = f7 - f10;
//		float f38 = f3 * f4 - (f2 * f5);
//		float f39 = f3 * f5 + f2 * f4;
//		f4 = paramArrayOfFloat1[(paramInt1 + 4)]
//				+ paramArrayOfFloat1[(paramInt1 + 20)];
//		f5 = paramArrayOfFloat1[(paramInt1 + 5)]
//				+ paramArrayOfFloat1[(paramInt1 + 21)];
//		f6 = paramArrayOfFloat1[(paramInt1 + 4)]
//				- paramArrayOfFloat1[(paramInt1 + 20)];
//		f7 = paramArrayOfFloat1[(paramInt1 + 5)]
//				- paramArrayOfFloat1[(paramInt1 + 21)];
//		f8 = paramArrayOfFloat1[(paramInt1 + 12)]
//				+ paramArrayOfFloat1[(paramInt1 + 28)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 13)]
//				+ paramArrayOfFloat1[(paramInt1 + 29)];
//		f10 = paramArrayOfFloat1[(paramInt1 + 12)]
//				- paramArrayOfFloat1[(paramInt1 + 28)];
//		f11 = paramArrayOfFloat1[(paramInt1 + 13)]
//				- paramArrayOfFloat1[(paramInt1 + 29)];
//		float f16 = f4 + f8;
//		float f17 = f5 + f9;
//		float f24 = f4 - f8;
//		float f25 = f5 - f9;
//		f4 = f6 - f11;
//		f5 = f7 + f10;
//		float f32 = f1 * (f4 - f5);
//		float f33 = f1 * (f5 + f4);
//		f4 = f6 + f11;
//		f5 = f7 - f10;
//		float f40 = f1 * (f4 + f5);
//		float f41 = f1 * (f5 - f4);
//		f4 = paramArrayOfFloat1[(paramInt1 + 6)]
//				+ paramArrayOfFloat1[(paramInt1 + 22)];
//		f5 = paramArrayOfFloat1[(paramInt1 + 7)]
//				+ paramArrayOfFloat1[(paramInt1 + 23)];
//		f6 = paramArrayOfFloat1[(paramInt1 + 6)]
//				- paramArrayOfFloat1[(paramInt1 + 22)];
//		f7 = paramArrayOfFloat1[(paramInt1 + 7)]
//				- paramArrayOfFloat1[(paramInt1 + 23)];
//		f8 = paramArrayOfFloat1[(paramInt1 + 14)]
//				+ paramArrayOfFloat1[(paramInt1 + 30)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 15)]
//				+ paramArrayOfFloat1[(paramInt1 + 31)];
//		f10 = paramArrayOfFloat1[(paramInt1 + 14)]
//				- paramArrayOfFloat1[(paramInt1 + 30)];
//		f11 = paramArrayOfFloat1[(paramInt1 + 15)]
//				- paramArrayOfFloat1[(paramInt1 + 31)];
//		float f18 = f4 + f8;
//		float f19 = f5 + f9;
//		float f26 = f4 - f8;
//		float f27 = f5 - f9;
//		f4 = f6 - f11;
//		f5 = f7 + f10;
//		float f34 = f3 * f4 - (f2 * f5);
//		float f35 = f3 * f5 + f2 * f4;
//		f4 = f6 + f11;
//		f5 = f7 - f10;
//		float f42 = f2 * f4 - (f3 * f5);
//		float f43 = f2 * f5 + f3 * f4;
//		f4 = f36 - f40;
//		f5 = f37 - f41;
//		f6 = f36 + f40;
//		f7 = f37 + f41;
//		f8 = f38 - f42;
//		f9 = f39 - f43;
//		f10 = f38 + f42;
//		f11 = f39 + f43;
//		paramArrayOfFloat1[(paramInt1 + 24)] = (f4 + f8);
//		paramArrayOfFloat1[(paramInt1 + 25)] = (f5 + f9);
//		paramArrayOfFloat1[(paramInt1 + 26)] = (f4 - f8);
//		paramArrayOfFloat1[(paramInt1 + 27)] = (f5 - f9);
//		paramArrayOfFloat1[(paramInt1 + 28)] = (f6 - f11);
//		paramArrayOfFloat1[(paramInt1 + 29)] = (f7 + f10);
//		paramArrayOfFloat1[(paramInt1 + 30)] = (f6 + f11);
//		paramArrayOfFloat1[(paramInt1 + 31)] = (f7 - f10);
//		f4 = f28 + f32;
//		f5 = f29 + f33;
//		f6 = f28 - f32;
//		f7 = f29 - f33;
//		f8 = f30 + f34;
//		f9 = f31 + f35;
//		f10 = f30 - f34;
//		f11 = f31 - f35;
//		paramArrayOfFloat1[(paramInt1 + 16)] = (f4 + f8);
//		paramArrayOfFloat1[(paramInt1 + 17)] = (f5 + f9);
//		paramArrayOfFloat1[(paramInt1 + 18)] = (f4 - f8);
//		paramArrayOfFloat1[(paramInt1 + 19)] = (f5 - f9);
//		paramArrayOfFloat1[(paramInt1 + 20)] = (f6 - f11);
//		paramArrayOfFloat1[(paramInt1 + 21)] = (f7 + f10);
//		paramArrayOfFloat1[(paramInt1 + 22)] = (f6 + f11);
//		paramArrayOfFloat1[(paramInt1 + 23)] = (f7 - f10);
//		f4 = f22 - f27;
//		f5 = f23 + f26;
//		f8 = f1 * (f4 - f5);
//		f9 = f1 * (f5 + f4);
//		f4 = f22 + f27;
//		f5 = f23 - f26;
//		f10 = f1 * (f4 - f5);
//		f11 = f1 * (f5 + f4);
//		f4 = f20 - f25;
//		f5 = f21 + f24;
//		f6 = f20 + f25;
//		f7 = f21 - f24;
//		paramArrayOfFloat1[(paramInt1 + 8)] = (f4 + f8);
//		paramArrayOfFloat1[(paramInt1 + 9)] = (f5 + f9);
//		paramArrayOfFloat1[(paramInt1 + 10)] = (f4 - f8);
//		paramArrayOfFloat1[(paramInt1 + 11)] = (f5 - f9);
//		paramArrayOfFloat1[(paramInt1 + 12)] = (f6 - f11);
//		paramArrayOfFloat1[(paramInt1 + 13)] = (f7 + f10);
//		paramArrayOfFloat1[(paramInt1 + 14)] = (f6 + f11);
//		paramArrayOfFloat1[(paramInt1 + 15)] = (f7 - f10);
//		f4 = f12 + f16;
//		f5 = f13 + f17;
//		f6 = f12 - f16;
//		f7 = f13 - f17;
//		f8 = f14 + f18;
//		f9 = f15 + f19;
//		f10 = f14 - f18;
//		f11 = f15 - f19;
//		paramArrayOfFloat1[paramInt1] = (f4 + f8);
//		paramArrayOfFloat1[(paramInt1 + 1)] = (f5 + f9);
//		paramArrayOfFloat1[(paramInt1 + 2)] = (f4 - f8);
//		paramArrayOfFloat1[(paramInt1 + 3)] = (f5 - f9);
//		paramArrayOfFloat1[(paramInt1 + 4)] = (f6 - f11);
//		paramArrayOfFloat1[(paramInt1 + 5)] = (f7 + f10);
//		paramArrayOfFloat1[(paramInt1 + 6)] = (f6 + f11);
//		paramArrayOfFloat1[(paramInt1 + 7)] = (f7 - f10);
//	}
//
//	private void cftf162(float[] paramArrayOfFloat1, int paramInt1,
//			float[] paramArrayOfFloat2, int paramInt2) {
//		float f1 = paramArrayOfFloat2[(paramInt2 + 1)];
//		float f2 = paramArrayOfFloat2[(paramInt2 + 4)];
//		float f3 = paramArrayOfFloat2[(paramInt2 + 5)];
//		float f6 = paramArrayOfFloat2[(paramInt2 + 6)];
//		float f7 = -paramArrayOfFloat2[(paramInt2 + 7)];
//		float f4 = paramArrayOfFloat2[(paramInt2 + 8)];
//		float f5 = paramArrayOfFloat2[(paramInt2 + 9)];
//		float f10 = paramArrayOfFloat1[paramInt1]
//				- paramArrayOfFloat1[(paramInt1 + 17)];
//		float f11 = paramArrayOfFloat1[(paramInt1 + 1)]
//				+ paramArrayOfFloat1[(paramInt1 + 16)];
//		float f8 = paramArrayOfFloat1[(paramInt1 + 8)]
//				- paramArrayOfFloat1[(paramInt1 + 25)];
//		float f9 = paramArrayOfFloat1[(paramInt1 + 9)]
//				+ paramArrayOfFloat1[(paramInt1 + 24)];
//		float f12 = f1 * (f8 - f9);
//		float f13 = f1 * (f9 + f8);
//		float f14 = f10 + f12;
//		float f15 = f11 + f13;
//		float f22 = f10 - f12;
//		float f23 = f11 - f13;
//		f10 = paramArrayOfFloat1[paramInt1]
//				+ paramArrayOfFloat1[(paramInt1 + 17)];
//		f11 = paramArrayOfFloat1[(paramInt1 + 1)]
//				- paramArrayOfFloat1[(paramInt1 + 16)];
//		f8 = paramArrayOfFloat1[(paramInt1 + 8)]
//				+ paramArrayOfFloat1[(paramInt1 + 25)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 9)]
//				- paramArrayOfFloat1[(paramInt1 + 24)];
//		f12 = f1 * (f8 - f9);
//		f13 = f1 * (f9 + f8);
//		float f30 = f10 - f13;
//		float f31 = f11 + f12;
//		float f38 = f10 + f13;
//		float f39 = f11 - f12;
//		f8 = paramArrayOfFloat1[(paramInt1 + 2)]
//				- paramArrayOfFloat1[(paramInt1 + 19)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 3)]
//				+ paramArrayOfFloat1[(paramInt1 + 18)];
//		f10 = f2 * f8 - (f3 * f9);
//		f11 = f2 * f9 + f3 * f8;
//		f8 = paramArrayOfFloat1[(paramInt1 + 10)]
//				- paramArrayOfFloat1[(paramInt1 + 27)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 11)]
//				+ paramArrayOfFloat1[(paramInt1 + 26)];
//		f12 = f7 * f8 - (f6 * f9);
//		f13 = f7 * f9 + f6 * f8;
//		float f16 = f10 + f12;
//		float f17 = f11 + f13;
//		float f24 = f10 - f12;
//		float f25 = f11 - f13;
//		f8 = paramArrayOfFloat1[(paramInt1 + 2)]
//				+ paramArrayOfFloat1[(paramInt1 + 19)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 3)]
//				- paramArrayOfFloat1[(paramInt1 + 18)];
//		f10 = f6 * f8 - (f7 * f9);
//		f11 = f6 * f9 + f7 * f8;
//		f8 = paramArrayOfFloat1[(paramInt1 + 10)]
//				+ paramArrayOfFloat1[(paramInt1 + 27)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 11)]
//				- paramArrayOfFloat1[(paramInt1 + 26)];
//		f12 = f2 * f8 + f3 * f9;
//		f13 = f2 * f9 - (f3 * f8);
//		float f32 = f10 - f12;
//		float f33 = f11 - f13;
//		float f40 = f10 + f12;
//		float f41 = f11 + f13;
//		f8 = paramArrayOfFloat1[(paramInt1 + 4)]
//				- paramArrayOfFloat1[(paramInt1 + 21)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 5)]
//				+ paramArrayOfFloat1[(paramInt1 + 20)];
//		f10 = f4 * f8 - (f5 * f9);
//		f11 = f4 * f9 + f5 * f8;
//		f8 = paramArrayOfFloat1[(paramInt1 + 12)]
//				- paramArrayOfFloat1[(paramInt1 + 29)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 13)]
//				+ paramArrayOfFloat1[(paramInt1 + 28)];
//		f12 = f5 * f8 - (f4 * f9);
//		f13 = f5 * f9 + f4 * f8;
//		float f18 = f10 + f12;
//		float f19 = f11 + f13;
//		float f26 = f10 - f12;
//		float f27 = f11 - f13;
//		f8 = paramArrayOfFloat1[(paramInt1 + 4)]
//				+ paramArrayOfFloat1[(paramInt1 + 21)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 5)]
//				- paramArrayOfFloat1[(paramInt1 + 20)];
//		f10 = f5 * f8 - (f4 * f9);
//		f11 = f5 * f9 + f4 * f8;
//		f8 = paramArrayOfFloat1[(paramInt1 + 12)]
//				+ paramArrayOfFloat1[(paramInt1 + 29)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 13)]
//				- paramArrayOfFloat1[(paramInt1 + 28)];
//		f12 = f4 * f8 - (f5 * f9);
//		f13 = f4 * f9 + f5 * f8;
//		float f34 = f10 - f12;
//		float f35 = f11 - f13;
//		float f42 = f10 + f12;
//		float f43 = f11 + f13;
//		f8 = paramArrayOfFloat1[(paramInt1 + 6)]
//				- paramArrayOfFloat1[(paramInt1 + 23)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 7)]
//				+ paramArrayOfFloat1[(paramInt1 + 22)];
//		f10 = f6 * f8 - (f7 * f9);
//		f11 = f6 * f9 + f7 * f8;
//		f8 = paramArrayOfFloat1[(paramInt1 + 14)]
//				- paramArrayOfFloat1[(paramInt1 + 31)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 15)]
//				+ paramArrayOfFloat1[(paramInt1 + 30)];
//		f12 = f3 * f8 - (f2 * f9);
//		f13 = f3 * f9 + f2 * f8;
//		float f20 = f10 + f12;
//		float f21 = f11 + f13;
//		float f28 = f10 - f12;
//		float f29 = f11 - f13;
//		f8 = paramArrayOfFloat1[(paramInt1 + 6)]
//				+ paramArrayOfFloat1[(paramInt1 + 23)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 7)]
//				- paramArrayOfFloat1[(paramInt1 + 22)];
//		f10 = f3 * f8 + f2 * f9;
//		f11 = f3 * f9 - (f2 * f8);
//		f8 = paramArrayOfFloat1[(paramInt1 + 14)]
//				+ paramArrayOfFloat1[(paramInt1 + 31)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 15)]
//				- paramArrayOfFloat1[(paramInt1 + 30)];
//		f12 = f7 * f8 - (f6 * f9);
//		f13 = f7 * f9 + f6 * f8;
//		float f36 = f10 + f12;
//		float f37 = f11 + f13;
//		float f44 = f10 - f12;
//		float f45 = f11 - f13;
//		f10 = f14 + f18;
//		f11 = f15 + f19;
//		f12 = f16 + f20;
//		f13 = f17 + f21;
//		paramArrayOfFloat1[paramInt1] = (f10 + f12);
//		paramArrayOfFloat1[(paramInt1 + 1)] = (f11 + f13);
//		paramArrayOfFloat1[(paramInt1 + 2)] = (f10 - f12);
//		paramArrayOfFloat1[(paramInt1 + 3)] = (f11 - f13);
//		f10 = f14 - f18;
//		f11 = f15 - f19;
//		f12 = f16 - f20;
//		f13 = f17 - f21;
//		paramArrayOfFloat1[(paramInt1 + 4)] = (f10 - f13);
//		paramArrayOfFloat1[(paramInt1 + 5)] = (f11 + f12);
//		paramArrayOfFloat1[(paramInt1 + 6)] = (f10 + f13);
//		paramArrayOfFloat1[(paramInt1 + 7)] = (f11 - f12);
//		f10 = f22 - f27;
//		f11 = f23 + f26;
//		f8 = f24 - f29;
//		f9 = f25 + f28;
//		f12 = f1 * (f8 - f9);
//		f13 = f1 * (f9 + f8);
//		paramArrayOfFloat1[(paramInt1 + 8)] = (f10 + f12);
//		paramArrayOfFloat1[(paramInt1 + 9)] = (f11 + f13);
//		paramArrayOfFloat1[(paramInt1 + 10)] = (f10 - f12);
//		paramArrayOfFloat1[(paramInt1 + 11)] = (f11 - f13);
//		f10 = f22 + f27;
//		f11 = f23 - f26;
//		f8 = f24 + f29;
//		f9 = f25 - f28;
//		f12 = f1 * (f8 - f9);
//		f13 = f1 * (f9 + f8);
//		paramArrayOfFloat1[(paramInt1 + 12)] = (f10 - f13);
//		paramArrayOfFloat1[(paramInt1 + 13)] = (f11 + f12);
//		paramArrayOfFloat1[(paramInt1 + 14)] = (f10 + f13);
//		paramArrayOfFloat1[(paramInt1 + 15)] = (f11 - f12);
//		f10 = f30 + f34;
//		f11 = f31 + f35;
//		f12 = f32 - f36;
//		f13 = f33 - f37;
//		paramArrayOfFloat1[(paramInt1 + 16)] = (f10 + f12);
//		paramArrayOfFloat1[(paramInt1 + 17)] = (f11 + f13);
//		paramArrayOfFloat1[(paramInt1 + 18)] = (f10 - f12);
//		paramArrayOfFloat1[(paramInt1 + 19)] = (f11 - f13);
//		f10 = f30 - f34;
//		f11 = f31 - f35;
//		f12 = f32 + f36;
//		f13 = f33 + f37;
//		paramArrayOfFloat1[(paramInt1 + 20)] = (f10 - f13);
//		paramArrayOfFloat1[(paramInt1 + 21)] = (f11 + f12);
//		paramArrayOfFloat1[(paramInt1 + 22)] = (f10 + f13);
//		paramArrayOfFloat1[(paramInt1 + 23)] = (f11 - f12);
//		f10 = f38 - f43;
//		f11 = f39 + f42;
//		f8 = f40 + f45;
//		f9 = f41 - f44;
//		f12 = f1 * (f8 - f9);
//		f13 = f1 * (f9 + f8);
//		paramArrayOfFloat1[(paramInt1 + 24)] = (f10 + f12);
//		paramArrayOfFloat1[(paramInt1 + 25)] = (f11 + f13);
//		paramArrayOfFloat1[(paramInt1 + 26)] = (f10 - f12);
//		paramArrayOfFloat1[(paramInt1 + 27)] = (f11 - f13);
//		f10 = f38 + f43;
//		f11 = f39 - f42;
//		f8 = f40 - f45;
//		f9 = f41 + f44;
//		f12 = f1 * (f8 - f9);
//		f13 = f1 * (f9 + f8);
//		paramArrayOfFloat1[(paramInt1 + 28)] = (f10 - f13);
//		paramArrayOfFloat1[(paramInt1 + 29)] = (f11 + f12);
//		paramArrayOfFloat1[(paramInt1 + 30)] = (f10 + f13);
//		paramArrayOfFloat1[(paramInt1 + 31)] = (f11 - f12);
//	}
//
//	private void cftf081(float[] paramArrayOfFloat1, int paramInt1,
//			float[] paramArrayOfFloat2, int paramInt2) {
//		float f1 = paramArrayOfFloat2[(paramInt2 + 1)];
//		float f2 = paramArrayOfFloat1[paramInt1]
//				+ paramArrayOfFloat1[(paramInt1 + 8)];
//		float f3 = paramArrayOfFloat1[(paramInt1 + 1)]
//				+ paramArrayOfFloat1[(paramInt1 + 9)];
//		float f4 = paramArrayOfFloat1[paramInt1]
//				- paramArrayOfFloat1[(paramInt1 + 8)];
//		float f5 = paramArrayOfFloat1[(paramInt1 + 1)]
//				- paramArrayOfFloat1[(paramInt1 + 9)];
//		float f6 = paramArrayOfFloat1[(paramInt1 + 4)]
//				+ paramArrayOfFloat1[(paramInt1 + 12)];
//		float f7 = paramArrayOfFloat1[(paramInt1 + 5)]
//				+ paramArrayOfFloat1[(paramInt1 + 13)];
//		float f8 = paramArrayOfFloat1[(paramInt1 + 4)]
//				- paramArrayOfFloat1[(paramInt1 + 12)];
//		float f9 = paramArrayOfFloat1[(paramInt1 + 5)]
//				- paramArrayOfFloat1[(paramInt1 + 13)];
//		float f10 = f2 + f6;
//		float f11 = f3 + f7;
//		float f14 = f2 - f6;
//		float f15 = f3 - f7;
//		float f12 = f4 - f9;
//		float f13 = f5 + f8;
//		float f16 = f4 + f9;
//		float f17 = f5 - f8;
//		f2 = paramArrayOfFloat1[(paramInt1 + 2)]
//				+ paramArrayOfFloat1[(paramInt1 + 10)];
//		f3 = paramArrayOfFloat1[(paramInt1 + 3)]
//				+ paramArrayOfFloat1[(paramInt1 + 11)];
//		f4 = paramArrayOfFloat1[(paramInt1 + 2)]
//				- paramArrayOfFloat1[(paramInt1 + 10)];
//		f5 = paramArrayOfFloat1[(paramInt1 + 3)]
//				- paramArrayOfFloat1[(paramInt1 + 11)];
//		f6 = paramArrayOfFloat1[(paramInt1 + 6)]
//				+ paramArrayOfFloat1[(paramInt1 + 14)];
//		f7 = paramArrayOfFloat1[(paramInt1 + 7)]
//				+ paramArrayOfFloat1[(paramInt1 + 15)];
//		f8 = paramArrayOfFloat1[(paramInt1 + 6)]
//				- paramArrayOfFloat1[(paramInt1 + 14)];
//		f9 = paramArrayOfFloat1[(paramInt1 + 7)]
//				- paramArrayOfFloat1[(paramInt1 + 15)];
//		float f18 = f2 + f6;
//		float f19 = f3 + f7;
//		float f22 = f2 - f6;
//		float f23 = f3 - f7;
//		f2 = f4 - f9;
//		f3 = f5 + f8;
//		f6 = f4 + f9;
//		f7 = f5 - f8;
//		float f20 = f1 * (f2 - f3);
//		float f21 = f1 * (f2 + f3);
//		float f24 = f1 * (f6 - f7);
//		float f25 = f1 * (f6 + f7);
//		paramArrayOfFloat1[(paramInt1 + 8)] = (f12 + f20);
//		paramArrayOfFloat1[(paramInt1 + 9)] = (f13 + f21);
//		paramArrayOfFloat1[(paramInt1 + 10)] = (f12 - f20);
//		paramArrayOfFloat1[(paramInt1 + 11)] = (f13 - f21);
//		paramArrayOfFloat1[(paramInt1 + 12)] = (f16 - f25);
//		paramArrayOfFloat1[(paramInt1 + 13)] = (f17 + f24);
//		paramArrayOfFloat1[(paramInt1 + 14)] = (f16 + f25);
//		paramArrayOfFloat1[(paramInt1 + 15)] = (f17 - f24);
//		paramArrayOfFloat1[paramInt1] = (f10 + f18);
//		paramArrayOfFloat1[(paramInt1 + 1)] = (f11 + f19);
//		paramArrayOfFloat1[(paramInt1 + 2)] = (f10 - f18);
//		paramArrayOfFloat1[(paramInt1 + 3)] = (f11 - f19);
//		paramArrayOfFloat1[(paramInt1 + 4)] = (f14 - f23);
//		paramArrayOfFloat1[(paramInt1 + 5)] = (f15 + f22);
//		paramArrayOfFloat1[(paramInt1 + 6)] = (f14 + f23);
//		paramArrayOfFloat1[(paramInt1 + 7)] = (f15 - f22);
//	}
//
//	private void cftf082(float[] paramArrayOfFloat1, int paramInt1,
//			float[] paramArrayOfFloat2, int paramInt2) {
//		float f1 = paramArrayOfFloat2[(paramInt2 + 1)];
//		float f2 = paramArrayOfFloat2[(paramInt2 + 2)];
//		float f3 = paramArrayOfFloat2[(paramInt2 + 3)];
//		float f8 = paramArrayOfFloat1[paramInt1]
//				- paramArrayOfFloat1[(paramInt1 + 9)];
//		float f9 = paramArrayOfFloat1[(paramInt1 + 1)]
//				+ paramArrayOfFloat1[(paramInt1 + 8)];
//		float f10 = paramArrayOfFloat1[paramInt1]
//				+ paramArrayOfFloat1[(paramInt1 + 9)];
//		float f11 = paramArrayOfFloat1[(paramInt1 + 1)]
//				- paramArrayOfFloat1[(paramInt1 + 8)];
//		float f4 = paramArrayOfFloat1[(paramInt1 + 4)]
//				- paramArrayOfFloat1[(paramInt1 + 13)];
//		float f5 = paramArrayOfFloat1[(paramInt1 + 5)]
//				+ paramArrayOfFloat1[(paramInt1 + 12)];
//		float f12 = f1 * (f4 - f5);
//		float f13 = f1 * (f5 + f4);
//		f4 = paramArrayOfFloat1[(paramInt1 + 4)]
//				+ paramArrayOfFloat1[(paramInt1 + 13)];
//		f5 = paramArrayOfFloat1[(paramInt1 + 5)]
//				- paramArrayOfFloat1[(paramInt1 + 12)];
//		float f14 = f1 * (f4 - f5);
//		float f15 = f1 * (f5 + f4);
//		f4 = paramArrayOfFloat1[(paramInt1 + 2)]
//				- paramArrayOfFloat1[(paramInt1 + 11)];
//		f5 = paramArrayOfFloat1[(paramInt1 + 3)]
//				+ paramArrayOfFloat1[(paramInt1 + 10)];
//		float f16 = f2 * f4 - (f3 * f5);
//		float f17 = f2 * f5 + f3 * f4;
//		f4 = paramArrayOfFloat1[(paramInt1 + 2)]
//				+ paramArrayOfFloat1[(paramInt1 + 11)];
//		f5 = paramArrayOfFloat1[(paramInt1 + 3)]
//				- paramArrayOfFloat1[(paramInt1 + 10)];
//		float f18 = f3 * f4 - (f2 * f5);
//		float f19 = f3 * f5 + f2 * f4;
//		f4 = paramArrayOfFloat1[(paramInt1 + 6)]
//				- paramArrayOfFloat1[(paramInt1 + 15)];
//		f5 = paramArrayOfFloat1[(paramInt1 + 7)]
//				+ paramArrayOfFloat1[(paramInt1 + 14)];
//		float f20 = f3 * f4 - (f2 * f5);
//		float f21 = f3 * f5 + f2 * f4;
//		f4 = paramArrayOfFloat1[(paramInt1 + 6)]
//				+ paramArrayOfFloat1[(paramInt1 + 15)];
//		f5 = paramArrayOfFloat1[(paramInt1 + 7)]
//				- paramArrayOfFloat1[(paramInt1 + 14)];
//		float f22 = f2 * f4 - (f3 * f5);
//		float f23 = f2 * f5 + f3 * f4;
//		f4 = f8 + f12;
//		f5 = f9 + f13;
//		float f6 = f16 + f20;
//		float f7 = f17 + f21;
//		paramArrayOfFloat1[paramInt1] = (f4 + f6);
//		paramArrayOfFloat1[(paramInt1 + 1)] = (f5 + f7);
//		paramArrayOfFloat1[(paramInt1 + 2)] = (f4 - f6);
//		paramArrayOfFloat1[(paramInt1 + 3)] = (f5 - f7);
//		f4 = f8 - f12;
//		f5 = f9 - f13;
//		f6 = f16 - f20;
//		f7 = f17 - f21;
//		paramArrayOfFloat1[(paramInt1 + 4)] = (f4 - f7);
//		paramArrayOfFloat1[(paramInt1 + 5)] = (f5 + f6);
//		paramArrayOfFloat1[(paramInt1 + 6)] = (f4 + f7);
//		paramArrayOfFloat1[(paramInt1 + 7)] = (f5 - f6);
//		f4 = f10 - f15;
//		f5 = f11 + f14;
//		f6 = f18 - f22;
//		f7 = f19 - f23;
//		paramArrayOfFloat1[(paramInt1 + 8)] = (f4 + f6);
//		paramArrayOfFloat1[(paramInt1 + 9)] = (f5 + f7);
//		paramArrayOfFloat1[(paramInt1 + 10)] = (f4 - f6);
//		paramArrayOfFloat1[(paramInt1 + 11)] = (f5 - f7);
//		f4 = f10 + f15;
//		f5 = f11 - f14;
//		f6 = f18 + f22;
//		f7 = f19 + f23;
//		paramArrayOfFloat1[(paramInt1 + 12)] = (f4 - f7);
//		paramArrayOfFloat1[(paramInt1 + 13)] = (f5 + f6);
//		paramArrayOfFloat1[(paramInt1 + 14)] = (f4 + f7);
//		paramArrayOfFloat1[(paramInt1 + 15)] = (f5 - f6);
//	}
//
//	private void cftf040(float[] paramArrayOfFloat, int paramInt) {
//		float f1 = paramArrayOfFloat[paramInt]
//				+ paramArrayOfFloat[(paramInt + 4)];
//		float f2 = paramArrayOfFloat[(paramInt + 1)]
//				+ paramArrayOfFloat[(paramInt + 5)];
//		float f3 = paramArrayOfFloat[paramInt]
//				- paramArrayOfFloat[(paramInt + 4)];
//		float f4 = paramArrayOfFloat[(paramInt + 1)]
//				- paramArrayOfFloat[(paramInt + 5)];
//		float f5 = paramArrayOfFloat[(paramInt + 2)]
//				+ paramArrayOfFloat[(paramInt + 6)];
//		float f6 = paramArrayOfFloat[(paramInt + 3)]
//				+ paramArrayOfFloat[(paramInt + 7)];
//		float f7 = paramArrayOfFloat[(paramInt + 2)]
//				- paramArrayOfFloat[(paramInt + 6)];
//		float f8 = paramArrayOfFloat[(paramInt + 3)]
//				- paramArrayOfFloat[(paramInt + 7)];
//		paramArrayOfFloat[paramInt] = (f1 + f5);
//		paramArrayOfFloat[(paramInt + 1)] = (f2 + f6);
//		paramArrayOfFloat[(paramInt + 2)] = (f3 - f8);
//		paramArrayOfFloat[(paramInt + 3)] = (f4 + f7);
//		paramArrayOfFloat[(paramInt + 4)] = (f1 - f5);
//		paramArrayOfFloat[(paramInt + 5)] = (f2 - f6);
//		paramArrayOfFloat[(paramInt + 6)] = (f3 + f8);
//		paramArrayOfFloat[(paramInt + 7)] = (f4 - f7);
//	}
//
//	private void cftb040(float[] paramArrayOfFloat, int paramInt) {
//		float f1 = paramArrayOfFloat[paramInt]
//				+ paramArrayOfFloat[(paramInt + 4)];
//		float f2 = paramArrayOfFloat[(paramInt + 1)]
//				+ paramArrayOfFloat[(paramInt + 5)];
//		float f3 = paramArrayOfFloat[paramInt]
//				- paramArrayOfFloat[(paramInt + 4)];
//		float f4 = paramArrayOfFloat[(paramInt + 1)]
//				- paramArrayOfFloat[(paramInt + 5)];
//		float f5 = paramArrayOfFloat[(paramInt + 2)]
//				+ paramArrayOfFloat[(paramInt + 6)];
//		float f6 = paramArrayOfFloat[(paramInt + 3)]
//				+ paramArrayOfFloat[(paramInt + 7)];
//		float f7 = paramArrayOfFloat[(paramInt + 2)]
//				- paramArrayOfFloat[(paramInt + 6)];
//		float f8 = paramArrayOfFloat[(paramInt + 3)]
//				- paramArrayOfFloat[(paramInt + 7)];
//		paramArrayOfFloat[paramInt] = (f1 + f5);
//		paramArrayOfFloat[(paramInt + 1)] = (f2 + f6);
//		paramArrayOfFloat[(paramInt + 2)] = (f3 + f8);
//		paramArrayOfFloat[(paramInt + 3)] = (f4 - f7);
//		paramArrayOfFloat[(paramInt + 4)] = (f1 - f5);
//		paramArrayOfFloat[(paramInt + 5)] = (f2 - f6);
//		paramArrayOfFloat[(paramInt + 6)] = (f3 - f8);
//		paramArrayOfFloat[(paramInt + 7)] = (f4 + f7);
//	}
//
//	private void cftx020(float[] paramArrayOfFloat, int paramInt) {
//		float f1 = paramArrayOfFloat[paramInt]
//				- paramArrayOfFloat[(paramInt + 2)];
//		float f2 = paramArrayOfFloat[(paramInt + 1)]
//				- paramArrayOfFloat[(paramInt + 3)];
//		paramArrayOfFloat[paramInt] += paramArrayOfFloat[(paramInt + 2)];
//		paramArrayOfFloat[(paramInt + 1)] += paramArrayOfFloat[(paramInt + 3)];
//		paramArrayOfFloat[(paramInt + 2)] = f1;
//		paramArrayOfFloat[(paramInt + 3)] = f2;
//	}
//
//	private void rftfsub(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, int paramInt3, float[] paramArrayOfFloat2,
//			int paramInt4) {
//		int l = paramInt1 >> 1;
//		int k = 2 * paramInt3 / l;
//		int j = 0;
//		for (int i3 = 2; i3 < l; i3 += 2) {
//			int i = paramInt1 - i3;
//			j += k;
//			float f1 = (float) (0.5D - paramArrayOfFloat2[(paramInt4
//					+ paramInt3 - j)]);
//			float f2 = paramArrayOfFloat2[(paramInt4 + j)];
//			int i1 = paramInt2 + i3;
//			int i2 = paramInt2 + i;
//			float f3 = paramArrayOfFloat1[i1] - paramArrayOfFloat1[i2];
//			float f4 = paramArrayOfFloat1[(i1 + 1)]
//					+ paramArrayOfFloat1[(i2 + 1)];
//			float f5 = f1 * f3 - (f2 * f4);
//			float f6 = f1 * f4 + f2 * f3;
//			paramArrayOfFloat1[i1] -= f5;
//			paramArrayOfFloat1[(i1 + 1)] -= f6;
//			paramArrayOfFloat1[i2] += f5;
//			paramArrayOfFloat1[(i2 + 1)] -= f6;
//		}
//	}
//
//	private void rftbsub(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, int paramInt3, float[] paramArrayOfFloat2,
//			int paramInt4) {
//		int l = paramInt1 >> 1;
//		int k = 2 * paramInt3 / l;
//		int j = 0;
//		for (int i3 = 2; i3 < l; i3 += 2) {
//			int i = paramInt1 - i3;
//			j += k;
//			float f1 = (float) (0.5D - paramArrayOfFloat2[(paramInt4
//					+ paramInt3 - j)]);
//			float f2 = paramArrayOfFloat2[(paramInt4 + j)];
//			int i1 = paramInt2 + i3;
//			int i2 = paramInt2 + i;
//			float f3 = paramArrayOfFloat1[i1] - paramArrayOfFloat1[i2];
//			float f4 = paramArrayOfFloat1[(i1 + 1)]
//					+ paramArrayOfFloat1[(i2 + 1)];
//			float f5 = f1 * f3 + f2 * f4;
//			float f6 = f1 * f4 - (f2 * f3);
//			paramArrayOfFloat1[i1] -= f5;
//			paramArrayOfFloat1[(i1 + 1)] -= f6;
//			paramArrayOfFloat1[i2] += f5;
//			paramArrayOfFloat1[(i2 + 1)] -= f6;
//		}
//	}
//
//	private void dctsub(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, int paramInt3, float[] paramArrayOfFloat2,
//			int paramInt4) {
//		int l = paramInt1 >> 1;
//		int k = paramInt3 / paramInt1;
//		int j = 0;
//		for (int i4 = 1; i4 < l; ++i4) {
//			int i = paramInt1 - i4;
//			j += k;
//			int i1 = paramInt4 + j;
//			int i2 = paramInt2 + i4;
//			int i3 = paramInt2 + i;
//			float f1 = paramArrayOfFloat2[i1]
//					- paramArrayOfFloat2[(paramInt4 + paramInt3 - j)];
//			float f2 = paramArrayOfFloat2[i1]
//					+ paramArrayOfFloat2[(paramInt4 + paramInt3 - j)];
//			float f3 = f2 * paramArrayOfFloat1[i2]
//					- (f1 * paramArrayOfFloat1[i3]);
//			paramArrayOfFloat1[i2] = (f1 * paramArrayOfFloat1[i2] + f2
//					* paramArrayOfFloat1[i3]);
//			paramArrayOfFloat1[i3] = f3;
//		}
//		paramArrayOfFloat1[(paramInt2 + l)] *= paramArrayOfFloat2[paramInt4];
//	}
//
//	private void scale(float paramFloat, float[] paramArrayOfFloat, int paramInt) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int j;
//		int l;
//		if ((i > 1)
//				&& (this.n > ConcurrencyUtils
//						.getThreadsBeginN_1D_FFT_2Threads())) {
//			i = 2;
//			j = this.n / i;
//			Future[] arrayOfFuture = new Future[i];
//			for (l = 0; l < i; ++l) {
//				int i1 = paramInt + l * j;
//				int i2 = (l == i - 1) ? paramInt + this.n : i1 + j;
//				arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1, i2,
//						paramArrayOfFloat, paramFloat) {
//					public void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i)
//							this.val$a[i] *= this.val$m;
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			j = paramInt;
//			int k = paramInt + this.n;
//			for (l = j; l < k; ++l)
//				paramArrayOfFloat[l] *= paramFloat;
//		}
//	}
//}