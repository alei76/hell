package ps.hell.math.base.persons.fft.transform.fft;//package ps.landerbuluse.ml.math.fft.transform.fft;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class FloatFFT_3D {
//	private int slices;
//	private int rows;
//	private int columns;
//	private int sliceStride;
//	private int rowStride;
//	private float[] t;
//	private FloatFFT_1D fftSlices;
//	private FloatFFT_1D fftRows;
//	private FloatFFT_1D fftColumns;
//	private int oldNthreads;
//	private int nt;
//	private boolean isPowerOfTwo = false;
//	private boolean useThreads = false;
//
//	public strictfp FloatFFT_3D(int paramInt1, int paramInt2, int paramInt3) {
//		if ((paramInt1 <= 1) || (paramInt2 <= 1) || (paramInt3 <= 1))
//			throw new IllegalArgumentException(
//					"slices, rows and columns must be greater than 1");
//		this.slices = paramInt1;
//		this.rows = paramInt2;
//		this.columns = paramInt3;
//		this.sliceStride = (paramInt2 * paramInt3);
//		this.rowStride = paramInt3;
//		if (paramInt1 * paramInt2 * paramInt3 >= ConcurrencyUtils
//				.getThreadsBeginN_3D())
//			this.useThreads = true;
//		if ((ConcurrencyUtils.isPowerOf2(paramInt1))
//				&& (ConcurrencyUtils.isPowerOf2(paramInt2))
//				&& (ConcurrencyUtils.isPowerOf2(paramInt3))) {
//			this.isPowerOfTwo = true;
//			this.oldNthreads = ConcurrencyUtils.getNumberOfThreads();
//			this.nt = paramInt1;
//			if (this.nt < paramInt2)
//				this.nt = paramInt2;
//			this.nt *= 8;
//			if (this.oldNthreads > 1)
//				this.nt *= this.oldNthreads;
//			if (2 * paramInt3 == 4)
//				this.nt >>= 1;
//			else if (2 * paramInt3 < 4)
//				this.nt >>= 2;
//			this.t = new float[this.nt];
//		}
//		this.fftSlices = new FloatFFT_1D(paramInt1);
//		if (paramInt1 == paramInt2)
//			this.fftRows = this.fftSlices;
//		else
//			this.fftRows = new FloatFFT_1D(paramInt2);
//		if (paramInt1 == paramInt3)
//			this.fftColumns = this.fftSlices;
//		else if (paramInt2 == paramInt3)
//			this.fftColumns = this.fftRows;
//		else
//			this.fftColumns = new FloatFFT_1D(paramInt3);
//	}
//
//	public strictfp void complexForward(float[] paramArrayOfFloat) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			int j = this.columns;
//			this.columns = (2 * this.columns);
//			this.sliceStride = (this.rows * this.columns);
//			this.rowStride = this.columns;
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 8;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 4)
//					this.nt >>= 1;
//				else if (this.columns < 4)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft3da_subth2(0, -1, paramArrayOfFloat, true);
//				cdft3db_subth(-1, paramArrayOfFloat, true);
//			} else {
//				xdft3da_sub2(0, -1, paramArrayOfFloat, true);
//				cdft3db_sub(-1, paramArrayOfFloat, true);
//			}
//			this.columns = j;
//			this.sliceStride = (this.rows * this.columns);
//			this.rowStride = this.columns;
//		} else {
//			this.sliceStride = (2 * this.rows * this.columns);
//			this.rowStride = (2 * this.columns);
//			int l;
//			int i1;
//			int i2;
//			int i3;
//			if ((i > 1) && (this.useThreads) && (this.slices >= i)
//					&& (this.rows >= i) && (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				l = this.slices / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.slices : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat) {
//						public strictfp void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * FloatFFT_3D.this.sliceStride;
//								for (int k = 0; k < FloatFFT_3D.this.rows; ++k)
//									FloatFFT_3D.this.fftColumns.complexForward(
//											this.val$a,
//											j + k * FloatFFT_3D.this.rowStride);
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.slices : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * FloatFFT_3D.this.sliceStride;
//								for (int k = 0; k < FloatFFT_3D.this.columns; ++k) {
//									int l = 2 * k;
//									int i2;
//									int i3;
//									for (int i1 = 0; i1 < FloatFFT_3D.this.rows; ++i1) {
//										i2 = j + l + i1
//												* FloatFFT_3D.this.rowStride;
//										i3 = 2 * i1;
//										arrayOfFloat[i3] = this.val$a[i2];
//										arrayOfFloat[(i3 + 1)] = this.val$a[(i2 + 1)];
//									}
//									FloatFFT_3D.this.fftRows
//											.complexForward(arrayOfFloat);
//									for (i1 = 0; i1 < FloatFFT_3D.this.rows; ++i1) {
//										i2 = j + l + i1
//												* FloatFFT_3D.this.rowStride;
//										i3 = 2 * i1;
//										this.val$a[i2] = arrayOfFloat[i3];
//										this.val$a[(i2 + 1)] = arrayOfFloat[(i3 + 1)];
//									}
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				l = this.rows / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.rows : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//								int j = i * FloatFFT_3D.this.rowStride;
//								for (int k = 0; k < FloatFFT_3D.this.columns; ++k) {
//									int l = 2 * k;
//									int i2;
//									int i3;
//									for (int i1 = 0; i1 < FloatFFT_3D.this.slices; ++i1) {
//										i2 = i1 * FloatFFT_3D.this.sliceStride
//												+ j + l;
//										i3 = 2 * i1;
//										arrayOfFloat[i3] = this.val$a[i2];
//										arrayOfFloat[(i3 + 1)] = this.val$a[(i2 + 1)];
//									}
//									FloatFFT_3D.this.fftSlices
//											.complexForward(arrayOfFloat);
//									for (i1 = 0; i1 < FloatFFT_3D.this.slices; ++i1) {
//										i2 = i1 * FloatFFT_3D.this.sliceStride
//												+ j + l;
//										i3 = 2 * i1;
//										this.val$a[i2] = arrayOfFloat[i3];
//										this.val$a[(i2 + 1)] = arrayOfFloat[(i3 + 1)];
//									}
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.slices; ++k) {
//					l = k * this.sliceStride;
//					for (i1 = 0; i1 < this.rows; ++i1)
//						this.fftColumns.complexForward(paramArrayOfFloat, l
//								+ i1 * this.rowStride);
//				}
//				float[] arrayOfFloat = new float[2 * this.rows];
//				int i4;
//				int i5;
//				int i6;
//				for (l = 0; l < this.slices; ++l) {
//					i1 = l * this.sliceStride;
//					for (i2 = 0; i2 < this.columns; ++i2) {
//						i3 = 2 * i2;
//						for (i4 = 0; i4 < this.rows; ++i4) {
//							i5 = i1 + i3 + i4 * this.rowStride;
//							i6 = 2 * i4;
//							arrayOfFloat[i6] = paramArrayOfFloat[i5];
//							arrayOfFloat[(i6 + 1)] = paramArrayOfFloat[(i5 + 1)];
//						}
//						this.fftRows.complexForward(arrayOfFloat);
//						for (i4 = 0; i4 < this.rows; ++i4) {
//							i5 = i1 + i3 + i4 * this.rowStride;
//							i6 = 2 * i4;
//							paramArrayOfFloat[i5] = arrayOfFloat[i6];
//							paramArrayOfFloat[(i5 + 1)] = arrayOfFloat[(i6 + 1)];
//						}
//					}
//				}
//				arrayOfFloat = new float[2 * this.slices];
//				for (l = 0; l < this.rows; ++l) {
//					i1 = l * this.rowStride;
//					for (i2 = 0; i2 < this.columns; ++i2) {
//						i3 = 2 * i2;
//						for (i4 = 0; i4 < this.slices; ++i4) {
//							i5 = i4 * this.sliceStride + i1 + i3;
//							i6 = 2 * i4;
//							arrayOfFloat[i6] = paramArrayOfFloat[i5];
//							arrayOfFloat[(i6 + 1)] = paramArrayOfFloat[(i5 + 1)];
//						}
//						this.fftSlices.complexForward(arrayOfFloat);
//						for (i4 = 0; i4 < this.slices; ++i4) {
//							i5 = i4 * this.sliceStride + i1 + i3;
//							i6 = 2 * i4;
//							paramArrayOfFloat[i5] = arrayOfFloat[i6];
//							paramArrayOfFloat[(i5 + 1)] = arrayOfFloat[(i6 + 1)];
//						}
//					}
//				}
//			}
//			this.sliceStride = (this.rows * this.columns);
//			this.rowStride = this.columns;
//		}
//	}
//
//	public strictfp void complexForward(float[][][] paramArrayOfFloat) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			int j = this.columns;
//			this.columns = (2 * this.columns);
//			this.sliceStride = (this.rows * this.columns);
//			this.rowStride = this.columns;
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 8;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 4)
//					this.nt >>= 1;
//				else if (this.columns < 4)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft3da_subth2(0, -1, paramArrayOfFloat, true);
//				cdft3db_subth(-1, paramArrayOfFloat, true);
//			} else {
//				xdft3da_sub2(0, -1, paramArrayOfFloat, true);
//				cdft3db_sub(-1, paramArrayOfFloat, true);
//			}
//			this.columns = j;
//			this.sliceStride = (this.rows * this.columns);
//			this.rowStride = this.columns;
//		} else {
//			int l;
//			int i1;
//			int i2;
//			int i3;
//			if ((i > 1) && (this.useThreads) && (this.slices >= i)
//					&& (this.rows >= i) && (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				l = this.slices / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.slices : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat) {
//						public strictfp void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < FloatFFT_3D.this.rows; ++j)
//									FloatFFT_3D.this.fftColumns
//											.complexForward(this.val$a[i][j]);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.slices : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < FloatFFT_3D.this.columns; ++j) {
//									int k = 2 * j;
//									int i1;
//									for (int l = 0; l < FloatFFT_3D.this.rows; ++l) {
//										i1 = 2 * l;
//										arrayOfFloat[i1] = this.val$a[i][l][k];
//										arrayOfFloat[(i1 + 1)] = this.val$a[i][l][(k + 1)];
//									}
//									FloatFFT_3D.this.fftRows
//											.complexForward(arrayOfFloat);
//									for (l = 0; l < FloatFFT_3D.this.rows; ++l) {
//										i1 = 2 * l;
//										this.val$a[i][l][k] = arrayOfFloat[i1];
//										this.val$a[i][l][(k + 1)] = arrayOfFloat[(i1 + 1)];
//									}
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				l = this.rows / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.rows : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								for (int j = 0; j < FloatFFT_3D.this.columns; ++j) {
//									int k = 2 * j;
//									int i1;
//									for (int l = 0; l < FloatFFT_3D.this.slices; ++l) {
//										i1 = 2 * l;
//										arrayOfFloat[i1] = this.val$a[l][i][k];
//										arrayOfFloat[(i1 + 1)] = this.val$a[l][i][(k + 1)];
//									}
//									FloatFFT_3D.this.fftSlices
//											.complexForward(arrayOfFloat);
//									for (l = 0; l < FloatFFT_3D.this.slices; ++l) {
//										i1 = 2 * l;
//										this.val$a[l][i][k] = arrayOfFloat[i1];
//										this.val$a[l][i][(k + 1)] = arrayOfFloat[(i1 + 1)];
//									}
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.slices; ++k)
//					for (l = 0; l < this.rows; ++l)
//						this.fftColumns.complexForward(paramArrayOfFloat[k][l]);
//				float[] arrayOfFloat = new float[2 * this.rows];
//				int i4;
//				for (l = 0; l < this.slices; ++l)
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						i2 = 2 * i1;
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i4 = 2 * i3;
//							arrayOfFloat[i4] = paramArrayOfFloat[l][i3][i2];
//							arrayOfFloat[(i4 + 1)] = paramArrayOfFloat[l][i3][(i2 + 1)];
//						}
//						this.fftRows.complexForward(arrayOfFloat);
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i4 = 2 * i3;
//							paramArrayOfFloat[l][i3][i2] = arrayOfFloat[i4];
//							paramArrayOfFloat[l][i3][(i2 + 1)] = arrayOfFloat[(i4 + 1)];
//						}
//					}
//				arrayOfFloat = new float[2 * this.slices];
//				for (l = 0; l < this.rows; ++l)
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						i2 = 2 * i1;
//						for (i3 = 0; i3 < this.slices; ++i3) {
//							i4 = 2 * i3;
//							arrayOfFloat[i4] = paramArrayOfFloat[i3][l][i2];
//							arrayOfFloat[(i4 + 1)] = paramArrayOfFloat[i3][l][(i2 + 1)];
//						}
//						this.fftSlices.complexForward(arrayOfFloat);
//						for (i3 = 0; i3 < this.slices; ++i3) {
//							i4 = 2 * i3;
//							paramArrayOfFloat[i3][l][i2] = arrayOfFloat[i4];
//							paramArrayOfFloat[i3][l][(i2 + 1)] = arrayOfFloat[(i4 + 1)];
//						}
//					}
//			}
//		}
//	}
//
//	public strictfp void complexInverse(float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			int j = this.columns;
//			this.columns = (2 * this.columns);
//			this.sliceStride = (this.rows * this.columns);
//			this.rowStride = this.columns;
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 8;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 4)
//					this.nt >>= 1;
//				else if (this.columns < 4)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft3da_subth2(0, 1, paramArrayOfFloat, paramBoolean);
//				cdft3db_subth(1, paramArrayOfFloat, paramBoolean);
//			} else {
//				xdft3da_sub2(0, 1, paramArrayOfFloat, paramBoolean);
//				cdft3db_sub(1, paramArrayOfFloat, paramBoolean);
//			}
//			this.columns = j;
//			this.sliceStride = (this.rows * this.columns);
//			this.rowStride = this.columns;
//		} else {
//			this.sliceStride = (2 * this.rows * this.columns);
//			this.rowStride = (2 * this.columns);
//			int l;
//			int i1;
//			int i2;
//			int i3;
//			if ((i > 1) && (this.useThreads) && (this.slices >= i)
//					&& (this.rows >= i) && (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				l = this.slices / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.slices : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat, paramBoolean) {
//						public strictfp void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * FloatFFT_3D.this.sliceStride;
//								for (int k = 0; k < FloatFFT_3D.this.rows; ++k)
//									FloatFFT_3D.this.fftColumns.complexInverse(
//											this.val$a,
//											j + k * FloatFFT_3D.this.rowStride,
//											this.val$scale);
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.slices : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat, paramBoolean) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * FloatFFT_3D.this.sliceStride;
//								for (int k = 0; k < FloatFFT_3D.this.columns; ++k) {
//									int l = 2 * k;
//									int i2;
//									int i3;
//									for (int i1 = 0; i1 < FloatFFT_3D.this.rows; ++i1) {
//										i2 = j + l + i1
//												* FloatFFT_3D.this.rowStride;
//										i3 = 2 * i1;
//										arrayOfFloat[i3] = this.val$a[i2];
//										arrayOfFloat[(i3 + 1)] = this.val$a[(i2 + 1)];
//									}
//									FloatFFT_3D.this.fftRows.complexInverse(
//											arrayOfFloat, this.val$scale);
//									for (i1 = 0; i1 < FloatFFT_3D.this.rows; ++i1) {
//										i2 = j + l + i1
//												* FloatFFT_3D.this.rowStride;
//										i3 = 2 * i1;
//										this.val$a[i2] = arrayOfFloat[i3];
//										this.val$a[(i2 + 1)] = arrayOfFloat[(i3 + 1)];
//									}
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				l = this.rows / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.rows : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat, paramBoolean) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//								int j = i * FloatFFT_3D.this.rowStride;
//								for (int k = 0; k < FloatFFT_3D.this.columns; ++k) {
//									int l = 2 * k;
//									int i2;
//									int i3;
//									for (int i1 = 0; i1 < FloatFFT_3D.this.slices; ++i1) {
//										i2 = i1 * FloatFFT_3D.this.sliceStride
//												+ j + l;
//										i3 = 2 * i1;
//										arrayOfFloat[i3] = this.val$a[i2];
//										arrayOfFloat[(i3 + 1)] = this.val$a[(i2 + 1)];
//									}
//									FloatFFT_3D.this.fftSlices.complexInverse(
//											arrayOfFloat, this.val$scale);
//									for (i1 = 0; i1 < FloatFFT_3D.this.slices; ++i1) {
//										i2 = i1 * FloatFFT_3D.this.sliceStride
//												+ j + l;
//										i3 = 2 * i1;
//										this.val$a[i2] = arrayOfFloat[i3];
//										this.val$a[(i2 + 1)] = arrayOfFloat[(i3 + 1)];
//									}
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.slices; ++k) {
//					l = k * this.sliceStride;
//					for (i1 = 0; i1 < this.rows; ++i1)
//						this.fftColumns.complexInverse(paramArrayOfFloat, l
//								+ i1 * this.rowStride, paramBoolean);
//				}
//				float[] arrayOfFloat = new float[2 * this.rows];
//				int i4;
//				int i5;
//				int i6;
//				for (l = 0; l < this.slices; ++l) {
//					i1 = l * this.sliceStride;
//					for (i2 = 0; i2 < this.columns; ++i2) {
//						i3 = 2 * i2;
//						for (i4 = 0; i4 < this.rows; ++i4) {
//							i5 = i1 + i3 + i4 * this.rowStride;
//							i6 = 2 * i4;
//							arrayOfFloat[i6] = paramArrayOfFloat[i5];
//							arrayOfFloat[(i6 + 1)] = paramArrayOfFloat[(i5 + 1)];
//						}
//						this.fftRows.complexInverse(arrayOfFloat, paramBoolean);
//						for (i4 = 0; i4 < this.rows; ++i4) {
//							i5 = i1 + i3 + i4 * this.rowStride;
//							i6 = 2 * i4;
//							paramArrayOfFloat[i5] = arrayOfFloat[i6];
//							paramArrayOfFloat[(i5 + 1)] = arrayOfFloat[(i6 + 1)];
//						}
//					}
//				}
//				arrayOfFloat = new float[2 * this.slices];
//				for (l = 0; l < this.rows; ++l) {
//					i1 = l * this.rowStride;
//					for (i2 = 0; i2 < this.columns; ++i2) {
//						i3 = 2 * i2;
//						for (i4 = 0; i4 < this.slices; ++i4) {
//							i5 = i4 * this.sliceStride + i1 + i3;
//							i6 = 2 * i4;
//							arrayOfFloat[i6] = paramArrayOfFloat[i5];
//							arrayOfFloat[(i6 + 1)] = paramArrayOfFloat[(i5 + 1)];
//						}
//						this.fftSlices.complexInverse(arrayOfFloat,
//								paramBoolean);
//						for (i4 = 0; i4 < this.slices; ++i4) {
//							i5 = i4 * this.sliceStride + i1 + i3;
//							i6 = 2 * i4;
//							paramArrayOfFloat[i5] = arrayOfFloat[i6];
//							paramArrayOfFloat[(i5 + 1)] = arrayOfFloat[(i6 + 1)];
//						}
//					}
//				}
//			}
//			this.sliceStride = (this.rows * this.columns);
//			this.rowStride = this.columns;
//		}
//	}
//
//	public strictfp void complexInverse(float[][][] paramArrayOfFloat,
//			boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			int j = this.columns;
//			this.columns = (2 * this.columns);
//			this.sliceStride = (this.rows * this.columns);
//			this.rowStride = this.columns;
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 8;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 4)
//					this.nt >>= 1;
//				else if (this.columns < 4)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft3da_subth2(0, 1, paramArrayOfFloat, paramBoolean);
//				cdft3db_subth(1, paramArrayOfFloat, paramBoolean);
//			} else {
//				xdft3da_sub2(0, 1, paramArrayOfFloat, paramBoolean);
//				cdft3db_sub(1, paramArrayOfFloat, paramBoolean);
//			}
//			this.columns = j;
//			this.sliceStride = (this.rows * this.columns);
//			this.rowStride = this.columns;
//		} else {
//			int l;
//			int i1;
//			int i2;
//			int i3;
//			if ((i > 1) && (this.useThreads) && (this.slices >= i)
//					&& (this.rows >= i) && (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				l = this.slices / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.slices : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat, paramBoolean) {
//						public strictfp void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < FloatFFT_3D.this.rows; ++j)
//									FloatFFT_3D.this.fftColumns.complexInverse(
//											this.val$a[i][j], this.val$scale);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.slices : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat, paramBoolean) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < FloatFFT_3D.this.columns; ++j) {
//									int k = 2 * j;
//									int i1;
//									for (int l = 0; l < FloatFFT_3D.this.rows; ++l) {
//										i1 = 2 * l;
//										arrayOfFloat[i1] = this.val$a[i][l][k];
//										arrayOfFloat[(i1 + 1)] = this.val$a[i][l][(k + 1)];
//									}
//									FloatFFT_3D.this.fftRows.complexInverse(
//											arrayOfFloat, this.val$scale);
//									for (l = 0; l < FloatFFT_3D.this.rows; ++l) {
//										i1 = 2 * l;
//										this.val$a[i][l][k] = arrayOfFloat[i1];
//										this.val$a[i][l][(k + 1)] = arrayOfFloat[(i1 + 1)];
//									}
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				l = this.rows / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.rows : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat, paramBoolean) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								for (int j = 0; j < FloatFFT_3D.this.columns; ++j) {
//									int k = 2 * j;
//									int i1;
//									for (int l = 0; l < FloatFFT_3D.this.slices; ++l) {
//										i1 = 2 * l;
//										arrayOfFloat[i1] = this.val$a[l][i][k];
//										arrayOfFloat[(i1 + 1)] = this.val$a[l][i][(k + 1)];
//									}
//									FloatFFT_3D.this.fftSlices.complexInverse(
//											arrayOfFloat, this.val$scale);
//									for (l = 0; l < FloatFFT_3D.this.slices; ++l) {
//										i1 = 2 * l;
//										this.val$a[l][i][k] = arrayOfFloat[i1];
//										this.val$a[l][i][(k + 1)] = arrayOfFloat[(i1 + 1)];
//									}
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.slices; ++k)
//					for (l = 0; l < this.rows; ++l)
//						this.fftColumns.complexInverse(paramArrayOfFloat[k][l],
//								paramBoolean);
//				float[] arrayOfFloat = new float[2 * this.rows];
//				int i4;
//				for (l = 0; l < this.slices; ++l)
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						i2 = 2 * i1;
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i4 = 2 * i3;
//							arrayOfFloat[i4] = paramArrayOfFloat[l][i3][i2];
//							arrayOfFloat[(i4 + 1)] = paramArrayOfFloat[l][i3][(i2 + 1)];
//						}
//						this.fftRows.complexInverse(arrayOfFloat, paramBoolean);
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i4 = 2 * i3;
//							paramArrayOfFloat[l][i3][i2] = arrayOfFloat[i4];
//							paramArrayOfFloat[l][i3][(i2 + 1)] = arrayOfFloat[(i4 + 1)];
//						}
//					}
//				arrayOfFloat = new float[2 * this.slices];
//				for (l = 0; l < this.rows; ++l)
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						i2 = 2 * i1;
//						for (i3 = 0; i3 < this.slices; ++i3) {
//							i4 = 2 * i3;
//							arrayOfFloat[i4] = paramArrayOfFloat[i3][l][i2];
//							arrayOfFloat[(i4 + 1)] = paramArrayOfFloat[i3][l][(i2 + 1)];
//						}
//						this.fftSlices.complexInverse(arrayOfFloat,
//								paramBoolean);
//						for (i3 = 0; i3 < this.slices; ++i3) {
//							i4 = 2 * i3;
//							paramArrayOfFloat[i3][l][i2] = arrayOfFloat[i4];
//							paramArrayOfFloat[i3][l][(i2 + 1)] = arrayOfFloat[(i4 + 1)];
//						}
//					}
//			}
//		}
//	}
//
//	public strictfp void realForward(float[] paramArrayOfFloat) {
//		if (!(this.isPowerOfTwo))
//			throw new IllegalArgumentException(
//					"slices, rows and columns must be power of two numbers");
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (i != this.oldNthreads) {
//			this.nt = this.slices;
//			if (this.nt < this.rows)
//				this.nt = this.rows;
//			this.nt *= 8;
//			if (i > 1)
//				this.nt *= i;
//			if (this.columns == 4)
//				this.nt >>= 1;
//			else if (this.columns < 4)
//				this.nt >>= 2;
//			this.t = new float[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			xdft3da_subth1(1, -1, paramArrayOfFloat, true);
//			cdft3db_subth(-1, paramArrayOfFloat, true);
//			rdft3d_sub(1, paramArrayOfFloat);
//		} else {
//			xdft3da_sub1(1, -1, paramArrayOfFloat, true);
//			cdft3db_sub(-1, paramArrayOfFloat, true);
//			rdft3d_sub(1, paramArrayOfFloat);
//		}
//	}
//
//	public strictfp void realForward(float[][][] paramArrayOfFloat) {
//		if (!(this.isPowerOfTwo))
//			throw new IllegalArgumentException(
//					"slices, rows and columns must be power of two numbers");
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (i != this.oldNthreads) {
//			this.nt = this.slices;
//			if (this.nt < this.rows)
//				this.nt = this.rows;
//			this.nt *= 8;
//			if (i > 1)
//				this.nt *= i;
//			if (this.columns == 4)
//				this.nt >>= 1;
//			else if (this.columns < 4)
//				this.nt >>= 2;
//			this.t = new float[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			xdft3da_subth1(1, -1, paramArrayOfFloat, true);
//			cdft3db_subth(-1, paramArrayOfFloat, true);
//			rdft3d_sub(1, paramArrayOfFloat);
//		} else {
//			xdft3da_sub1(1, -1, paramArrayOfFloat, true);
//			cdft3db_sub(-1, paramArrayOfFloat, true);
//			rdft3d_sub(1, paramArrayOfFloat);
//		}
//	}
//
//	public strictfp void realForwardFull(float[] paramArrayOfFloat) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 8;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 4)
//					this.nt >>= 1;
//				else if (this.columns < 4)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft3da_subth2(1, -1, paramArrayOfFloat, true);
//				cdft3db_subth(-1, paramArrayOfFloat, true);
//				rdft3d_sub(1, paramArrayOfFloat);
//			} else {
//				xdft3da_sub2(1, -1, paramArrayOfFloat, true);
//				cdft3db_sub(-1, paramArrayOfFloat, true);
//				rdft3d_sub(1, paramArrayOfFloat);
//			}
//			fillSymmetric(paramArrayOfFloat);
//		} else {
//			mixedRadixRealForwardFull(paramArrayOfFloat);
//		}
//	}
//
//	public strictfp void realForwardFull(float[][][] paramArrayOfFloat) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 8;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 4)
//					this.nt >>= 1;
//				else if (this.columns < 4)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft3da_subth2(1, -1, paramArrayOfFloat, true);
//				cdft3db_subth(-1, paramArrayOfFloat, true);
//				rdft3d_sub(1, paramArrayOfFloat);
//			} else {
//				xdft3da_sub2(1, -1, paramArrayOfFloat, true);
//				cdft3db_sub(-1, paramArrayOfFloat, true);
//				rdft3d_sub(1, paramArrayOfFloat);
//			}
//			fillSymmetric(paramArrayOfFloat);
//		} else {
//			mixedRadixRealForwardFull(paramArrayOfFloat);
//		}
//	}
//
//	public strictfp void realInverse(float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		if (!(this.isPowerOfTwo))
//			throw new IllegalArgumentException(
//					"slices, rows and columns must be power of two numbers");
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (i != this.oldNthreads) {
//			this.nt = this.slices;
//			if (this.nt < this.rows)
//				this.nt = this.rows;
//			this.nt *= 8;
//			if (i > 1)
//				this.nt *= i;
//			if (this.columns == 4)
//				this.nt >>= 1;
//			else if (this.columns < 4)
//				this.nt >>= 2;
//			this.t = new float[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			rdft3d_sub(-1, paramArrayOfFloat);
//			cdft3db_subth(1, paramArrayOfFloat, paramBoolean);
//			xdft3da_subth1(1, 1, paramArrayOfFloat, paramBoolean);
//		} else {
//			rdft3d_sub(-1, paramArrayOfFloat);
//			cdft3db_sub(1, paramArrayOfFloat, paramBoolean);
//			xdft3da_sub1(1, 1, paramArrayOfFloat, paramBoolean);
//		}
//	}
//
//	public strictfp void realInverse(float[][][] paramArrayOfFloat,
//			boolean paramBoolean) {
//		if (!(this.isPowerOfTwo))
//			throw new IllegalArgumentException(
//					"slices, rows and columns must be power of two numbers");
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (i != this.oldNthreads) {
//			this.nt = this.slices;
//			if (this.nt < this.rows)
//				this.nt = this.rows;
//			this.nt *= 8;
//			if (i > 1)
//				this.nt *= i;
//			if (this.columns == 4)
//				this.nt >>= 1;
//			else if (this.columns < 4)
//				this.nt >>= 2;
//			this.t = new float[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			rdft3d_sub(-1, paramArrayOfFloat);
//			cdft3db_subth(1, paramArrayOfFloat, paramBoolean);
//			xdft3da_subth1(1, 1, paramArrayOfFloat, paramBoolean);
//		} else {
//			rdft3d_sub(-1, paramArrayOfFloat);
//			cdft3db_sub(1, paramArrayOfFloat, paramBoolean);
//			xdft3da_sub1(1, 1, paramArrayOfFloat, paramBoolean);
//		}
//	}
//
//	public strictfp void realInverseFull(float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 8;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 4)
//					this.nt >>= 1;
//				else if (this.columns < 4)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft3da_subth2(1, 1, paramArrayOfFloat, paramBoolean);
//				cdft3db_subth(1, paramArrayOfFloat, paramBoolean);
//				rdft3d_sub(1, paramArrayOfFloat);
//			} else {
//				xdft3da_sub2(1, 1, paramArrayOfFloat, paramBoolean);
//				cdft3db_sub(1, paramArrayOfFloat, paramBoolean);
//				rdft3d_sub(1, paramArrayOfFloat);
//			}
//			fillSymmetric(paramArrayOfFloat);
//		} else {
//			mixedRadixRealInverseFull(paramArrayOfFloat, paramBoolean);
//		}
//	}
//
//	public strictfp void realInverseFull(float[][][] paramArrayOfFloat,
//			boolean paramBoolean) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 8;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 4)
//					this.nt >>= 1;
//				else if (this.columns < 4)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft3da_subth2(1, 1, paramArrayOfFloat, paramBoolean);
//				cdft3db_subth(1, paramArrayOfFloat, paramBoolean);
//				rdft3d_sub(1, paramArrayOfFloat);
//			} else {
//				xdft3da_sub2(1, 1, paramArrayOfFloat, paramBoolean);
//				cdft3db_sub(1, paramArrayOfFloat, paramBoolean);
//				rdft3d_sub(1, paramArrayOfFloat);
//			}
//			fillSymmetric(paramArrayOfFloat);
//		} else {
//			mixedRadixRealInverseFull(paramArrayOfFloat, paramBoolean);
//		}
//	}
//
//	private strictfp void mixedRadixRealForwardFull(
//			float[][][] paramArrayOfFloat) {
//		float[] arrayOfFloat = new float[2 * this.rows];
//		int i = this.rows / 2 + 1;
//		int j = 2 * this.columns;
//		int k;
//		if (this.rows % 2 == 0)
//			k = this.rows / 2;
//		else
//			k = (this.rows + 1) / 2;
//		int l = ConcurrencyUtils.getNumberOfThreads();
//		int i2;
//		int i3;
//		int i4;
//		int i5;
//		if ((l > 1) && (this.useThreads) && (this.slices >= l)
//				&& (this.columns >= l) && (i >= l)) {
//			Future[] arrayOfFuture = new Future[l];
//			i2 = this.slices / l;
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i2;
//				i5 = (i3 == l - 1) ? this.slices : i4 + i2;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(i4,
//						i5, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//							for (int j = 0; j < FloatFFT_3D.this.rows; ++j)
//								FloatFFT_3D.this.fftColumns
//										.realForwardFull(this.val$a[i][j]);
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i2;
//				i5 = (i3 == l - 1) ? this.slices : i4 + i2;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(i4,
//						i5, paramArrayOfFloat) {
//					public strictfp void run() {
//						float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.rows];
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//							for (int j = 0; j < FloatFFT_3D.this.columns; ++j) {
//								int k = 2 * j;
//								int i1;
//								for (int l = 0; l < FloatFFT_3D.this.rows; ++l) {
//									i1 = 2 * l;
//									arrayOfFloat[i1] = this.val$a[i][l][k];
//									arrayOfFloat[(i1 + 1)] = this.val$a[i][l][(k + 1)];
//								}
//								FloatFFT_3D.this.fftRows
//										.complexForward(arrayOfFloat);
//								for (l = 0; l < FloatFFT_3D.this.rows; ++l) {
//									i1 = 2 * l;
//									this.val$a[i][l][k] = arrayOfFloat[i1];
//									this.val$a[i][l][(k + 1)] = arrayOfFloat[(i1 + 1)];
//								}
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			i2 = i / l;
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i2;
//				i5 = (i3 == l - 1) ? i : i4 + i2;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(i4,
//						i5, paramArrayOfFloat) {
//					public strictfp void run() {
//						float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.slices];
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//							for (int j = 0; j < FloatFFT_3D.this.columns; ++j) {
//								int k = 2 * j;
//								int i1;
//								for (int l = 0; l < FloatFFT_3D.this.slices; ++l) {
//									i1 = 2 * l;
//									arrayOfFloat[i1] = this.val$a[l][i][k];
//									arrayOfFloat[(i1 + 1)] = this.val$a[l][i][(k + 1)];
//								}
//								FloatFFT_3D.this.fftSlices
//										.complexForward(arrayOfFloat);
//								for (l = 0; l < FloatFFT_3D.this.slices; ++l) {
//									i1 = 2 * l;
//									this.val$a[l][i][k] = arrayOfFloat[i1];
//									this.val$a[l][i][(k + 1)] = arrayOfFloat[(i1 + 1)];
//								}
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			i2 = this.slices / l;
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i2;
//				i5 = (i3 == l - 1) ? this.slices : i4 + i2;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(i4,
//						i5, k, j, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = (FloatFFT_3D.this.slices - i)
//									% FloatFFT_3D.this.slices;
//							for (int k = 1; k < this.val$n2d2; ++k) {
//								int l = FloatFFT_3D.this.rows - k;
//								for (int i1 = 0; i1 < FloatFFT_3D.this.columns; ++i1) {
//									int i2 = 2 * i1;
//									int i3 = this.val$newn3 - i2;
//									this.val$a[j][l][(i3 % this.val$newn3)] = this.val$a[i][k][i2];
//									this.val$a[j][l][((i3 + 1) % this.val$newn3)] = (-this.val$a[i][k][(i2 + 1)]);
//								}
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int i1 = 0; i1 < this.slices; ++i1)
//				for (i2 = 0; i2 < this.rows; ++i2)
//					this.fftColumns.realForwardFull(paramArrayOfFloat[i1][i2]);
//			for (i1 = 0; i1 < this.slices; ++i1)
//				for (i2 = 0; i2 < this.columns; ++i2) {
//					i3 = 2 * i2;
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						i5 = 2 * i4;
//						arrayOfFloat[i5] = paramArrayOfFloat[i1][i4][i3];
//						arrayOfFloat[(i5 + 1)] = paramArrayOfFloat[i1][i4][(i3 + 1)];
//					}
//					this.fftRows.complexForward(arrayOfFloat);
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						i5 = 2 * i4;
//						paramArrayOfFloat[i1][i4][i3] = arrayOfFloat[i5];
//						paramArrayOfFloat[i1][i4][(i3 + 1)] = arrayOfFloat[(i5 + 1)];
//					}
//				}
//			arrayOfFloat = new float[2 * this.slices];
//			for (i1 = 0; i1 < i; ++i1)
//				for (i2 = 0; i2 < this.columns; ++i2) {
//					i3 = 2 * i2;
//					for (i4 = 0; i4 < this.slices; ++i4) {
//						i5 = 2 * i4;
//						arrayOfFloat[i5] = paramArrayOfFloat[i4][i1][i3];
//						arrayOfFloat[(i5 + 1)] = paramArrayOfFloat[i4][i1][(i3 + 1)];
//					}
//					this.fftSlices.complexForward(arrayOfFloat);
//					for (i4 = 0; i4 < this.slices; ++i4) {
//						i5 = 2 * i4;
//						paramArrayOfFloat[i4][i1][i3] = arrayOfFloat[i5];
//						paramArrayOfFloat[i4][i1][(i3 + 1)] = arrayOfFloat[(i5 + 1)];
//					}
//				}
//			for (i1 = 0; i1 < this.slices; ++i1) {
//				i2 = (this.slices - i1) % this.slices;
//				for (i3 = 1; i3 < k; ++i3) {
//					i4 = this.rows - i3;
//					for (i5 = 0; i5 < this.columns; ++i5) {
//						int i6 = 2 * i5;
//						int i7 = j - i6;
//						paramArrayOfFloat[i2][i4][(i7 % j)] = paramArrayOfFloat[i1][i3][i6];
//						paramArrayOfFloat[i2][i4][((i7 + 1) % j)] = (-paramArrayOfFloat[i1][i3][(i6 + 1)]);
//					}
//				}
//			}
//		}
//	}
//
//	private strictfp void mixedRadixRealInverseFull(
//			float[][][] paramArrayOfFloat, boolean paramBoolean) {
//		float[] arrayOfFloat = new float[2 * this.rows];
//		int i = this.rows / 2 + 1;
//		int j = 2 * this.columns;
//		int k;
//		if (this.rows % 2 == 0)
//			k = this.rows / 2;
//		else
//			k = (this.rows + 1) / 2;
//		int l = ConcurrencyUtils.getNumberOfThreads();
//		int i2;
//		int i3;
//		int i4;
//		int i5;
//		if ((l > 1) && (this.useThreads) && (this.slices >= l)
//				&& (this.columns >= l) && (i >= l)) {
//			Future[] arrayOfFuture = new Future[l];
//			i2 = this.slices / l;
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i2;
//				i5 = (i3 == l - 1) ? this.slices : i4 + i2;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(i4,
//						i5, paramArrayOfFloat, paramBoolean) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//							for (int j = 0; j < FloatFFT_3D.this.rows; ++j)
//								FloatFFT_3D.this.fftColumns.realInverseFull(
//										this.val$a[i][j], this.val$scale);
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i2;
//				i5 = (i3 == l - 1) ? this.slices : i4 + i2;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(i4,
//						i5, paramArrayOfFloat, paramBoolean) {
//					public strictfp void run() {
//						float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.rows];
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//							for (int j = 0; j < FloatFFT_3D.this.columns; ++j) {
//								int k = 2 * j;
//								int i1;
//								for (int l = 0; l < FloatFFT_3D.this.rows; ++l) {
//									i1 = 2 * l;
//									arrayOfFloat[i1] = this.val$a[i][l][k];
//									arrayOfFloat[(i1 + 1)] = this.val$a[i][l][(k + 1)];
//								}
//								FloatFFT_3D.this.fftRows.complexInverse(
//										arrayOfFloat, this.val$scale);
//								for (l = 0; l < FloatFFT_3D.this.rows; ++l) {
//									i1 = 2 * l;
//									this.val$a[i][l][k] = arrayOfFloat[i1];
//									this.val$a[i][l][(k + 1)] = arrayOfFloat[(i1 + 1)];
//								}
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			i2 = i / l;
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i2;
//				i5 = (i3 == l - 1) ? i : i4 + i2;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(i4,
//						i5, paramArrayOfFloat, paramBoolean) {
//					public strictfp void run() {
//						float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.slices];
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//							for (int j = 0; j < FloatFFT_3D.this.columns; ++j) {
//								int k = 2 * j;
//								int i1;
//								for (int l = 0; l < FloatFFT_3D.this.slices; ++l) {
//									i1 = 2 * l;
//									arrayOfFloat[i1] = this.val$a[l][i][k];
//									arrayOfFloat[(i1 + 1)] = this.val$a[l][i][(k + 1)];
//								}
//								FloatFFT_3D.this.fftSlices.complexInverse(
//										arrayOfFloat, this.val$scale);
//								for (l = 0; l < FloatFFT_3D.this.slices; ++l) {
//									i1 = 2 * l;
//									this.val$a[l][i][k] = arrayOfFloat[i1];
//									this.val$a[l][i][(k + 1)] = arrayOfFloat[(i1 + 1)];
//								}
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			i2 = this.slices / l;
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i2;
//				i5 = (i3 == l - 1) ? this.slices : i4 + i2;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(i4,
//						i5, k, j, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = (FloatFFT_3D.this.slices - i)
//									% FloatFFT_3D.this.slices;
//							for (int k = 1; k < this.val$n2d2; ++k) {
//								int l = FloatFFT_3D.this.rows - k;
//								for (int i1 = 0; i1 < FloatFFT_3D.this.columns; ++i1) {
//									int i2 = 2 * i1;
//									int i3 = this.val$newn3 - i2;
//									this.val$a[j][l][(i3 % this.val$newn3)] = this.val$a[i][k][i2];
//									this.val$a[j][l][((i3 + 1) % this.val$newn3)] = (-this.val$a[i][k][(i2 + 1)]);
//								}
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int i1 = 0; i1 < this.slices; ++i1)
//				for (i2 = 0; i2 < this.rows; ++i2)
//					this.fftColumns.realInverseFull(paramArrayOfFloat[i1][i2],
//							paramBoolean);
//			for (i1 = 0; i1 < this.slices; ++i1)
//				for (i2 = 0; i2 < this.columns; ++i2) {
//					i3 = 2 * i2;
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						i5 = 2 * i4;
//						arrayOfFloat[i5] = paramArrayOfFloat[i1][i4][i3];
//						arrayOfFloat[(i5 + 1)] = paramArrayOfFloat[i1][i4][(i3 + 1)];
//					}
//					this.fftRows.complexInverse(arrayOfFloat, paramBoolean);
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						i5 = 2 * i4;
//						paramArrayOfFloat[i1][i4][i3] = arrayOfFloat[i5];
//						paramArrayOfFloat[i1][i4][(i3 + 1)] = arrayOfFloat[(i5 + 1)];
//					}
//				}
//			arrayOfFloat = new float[2 * this.slices];
//			for (i1 = 0; i1 < i; ++i1)
//				for (i2 = 0; i2 < this.columns; ++i2) {
//					i3 = 2 * i2;
//					for (i4 = 0; i4 < this.slices; ++i4) {
//						i5 = 2 * i4;
//						arrayOfFloat[i5] = paramArrayOfFloat[i4][i1][i3];
//						arrayOfFloat[(i5 + 1)] = paramArrayOfFloat[i4][i1][(i3 + 1)];
//					}
//					this.fftSlices.complexInverse(arrayOfFloat, paramBoolean);
//					for (i4 = 0; i4 < this.slices; ++i4) {
//						i5 = 2 * i4;
//						paramArrayOfFloat[i4][i1][i3] = arrayOfFloat[i5];
//						paramArrayOfFloat[i4][i1][(i3 + 1)] = arrayOfFloat[(i5 + 1)];
//					}
//				}
//			for (i1 = 0; i1 < this.slices; ++i1) {
//				i2 = (this.slices - i1) % this.slices;
//				for (i3 = 1; i3 < k; ++i3) {
//					i4 = this.rows - i3;
//					for (i5 = 0; i5 < this.columns; ++i5) {
//						int i6 = 2 * i5;
//						int i7 = j - i6;
//						paramArrayOfFloat[i2][i4][(i7 % j)] = paramArrayOfFloat[i1][i3][i6];
//						paramArrayOfFloat[i2][i4][((i7 + 1) % j)] = (-paramArrayOfFloat[i1][i3][(i6 + 1)]);
//					}
//				}
//			}
//		}
//	}
//
//	private strictfp void mixedRadixRealForwardFull(float[] paramArrayOfFloat) {
//		int i = 2 * this.columns;
//		float[] arrayOfFloat = new float[i];
//		int j = this.rows / 2 + 1;
//		int k;
//		if (this.rows % 2 == 0)
//			k = this.rows / 2;
//		else
//			k = (this.rows + 1) / 2;
//		int l = 2 * this.sliceStride;
//		int i1 = 2 * this.rowStride;
//		int i2 = this.slices / 2;
//		int i3 = ConcurrencyUtils.getNumberOfThreads();
//		int i5;
//		int i8;
//		int i9;
//		int i10;
//		if ((i3 > 1) && (this.useThreads) && (i2 >= i3) && (this.columns >= i3)
//				&& (j >= i3)) {
//			Future[] arrayOfFuture = new Future[i3];
//			i5 = i2 / i3;
//			for (int i6 = 0; i6 < i3; ++i6) {
//				i8 = this.slices - 1 - (i6 * i5);
//				i9 = (i6 == i3 - 1) ? i2 + 1 : i8 - i5;
//				arrayOfFuture[i6] = ConcurrencyUtils.submit(new Runnable(i, i8,
//						i9, l, paramArrayOfFloat, i1) {
//					public strictfp void run() {
//						float[] arrayOfFloat = new float[this.val$twon3];
//						for (int i = this.val$firstSlice; i >= this.val$lastSlice; --i) {
//							int j = i * FloatFFT_3D.this.sliceStride;
//							int k = i * this.val$twoSliceStride;
//							for (int l = FloatFFT_3D.this.rows - 1; l >= 0; --l) {
//								System.arraycopy(this.val$a, j + l
//										* FloatFFT_3D.this.rowStride,
//										arrayOfFloat, 0,
//										FloatFFT_3D.this.columns);
//								FloatFFT_3D.this.fftColumns
//										.realForwardFull(arrayOfFloat);
//								System.arraycopy(arrayOfFloat, 0, this.val$a, k
//										+ l * this.val$twoRowStride,
//										this.val$twon3);
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			float[][][] arrayOfFloat1 = new float[i2 + 1][this.rows][i];
//			for (i8 = 0; i8 < i3; ++i8) {
//				i9 = i8 * i5;
//				i10 = (i8 == i3 - 1) ? i2 + 1 : i9 + i5;
//				arrayOfFuture[i8] = ConcurrencyUtils.submit(new Runnable(i9,
//						i10, paramArrayOfFloat, arrayOfFloat1) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = i * FloatFFT_3D.this.sliceStride;
//							for (int k = 0; k < FloatFFT_3D.this.rows; ++k) {
//								System.arraycopy(this.val$a, j + k
//										* FloatFFT_3D.this.rowStride,
//										this.val$temp2[i][k], 0,
//										FloatFFT_3D.this.columns);
//								FloatFFT_3D.this.fftColumns
//										.realForwardFull(this.val$temp2[i][k]);
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i8 = 0; i8 < i3; ++i8) {
//				i9 = i8 * i5;
//				i10 = (i8 == i3 - 1) ? i2 + 1 : i9 + i5;
//				arrayOfFuture[i8] = ConcurrencyUtils.submit(new Runnable(i9,
//						i10, l, arrayOfFloat1, paramArrayOfFloat, i1, i) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = i * this.val$twoSliceStride;
//							for (int k = 0; k < FloatFFT_3D.this.rows; ++k)
//								System.arraycopy(this.val$temp2[i][k], 0,
//										this.val$a, j + k
//												* this.val$twoRowStride,
//										this.val$twon3);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			i5 = this.slices / i3;
//			for (i8 = 0; i8 < i3; ++i8) {
//				i9 = i8 * i5;
//				i10 = (i8 == i3 - 1) ? this.slices : i9 + i5;
//				arrayOfFuture[i8] = ConcurrencyUtils.submit(new Runnable(i9,
//						i10, l, i1, paramArrayOfFloat) {
//					public strictfp void run() {
//						float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.rows];
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = i * this.val$twoSliceStride;
//							for (int k = 0; k < FloatFFT_3D.this.columns; ++k) {
//								int l = 2 * k;
//								int i2;
//								int i3;
//								for (int i1 = 0; i1 < FloatFFT_3D.this.rows; ++i1) {
//									i2 = j + i1 * this.val$twoRowStride + l;
//									i3 = 2 * i1;
//									arrayOfFloat[i3] = this.val$a[i2];
//									arrayOfFloat[(i3 + 1)] = this.val$a[(i2 + 1)];
//								}
//								FloatFFT_3D.this.fftRows
//										.complexForward(arrayOfFloat);
//								for (i1 = 0; i1 < FloatFFT_3D.this.rows; ++i1) {
//									i2 = j + i1 * this.val$twoRowStride + l;
//									i3 = 2 * i1;
//									this.val$a[i2] = arrayOfFloat[i3];
//									this.val$a[(i2 + 1)] = arrayOfFloat[(i3 + 1)];
//								}
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			i5 = j / i3;
//			for (i8 = 0; i8 < i3; ++i8) {
//				i9 = i8 * i5;
//				i10 = (i8 == i3 - 1) ? j : i9 + i5;
//				arrayOfFuture[i8] = ConcurrencyUtils.submit(new Runnable(i9,
//						i10, i1, l, paramArrayOfFloat) {
//					public strictfp void run() {
//						float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.slices];
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = i * this.val$twoRowStride;
//							for (int k = 0; k < FloatFFT_3D.this.columns; ++k) {
//								int l = 2 * k;
//								int i2;
//								int i3;
//								for (int i1 = 0; i1 < FloatFFT_3D.this.slices; ++i1) {
//									i2 = 2 * i1;
//									i3 = i1 * this.val$twoSliceStride + j + l;
//									arrayOfFloat[i2] = this.val$a[i3];
//									arrayOfFloat[(i2 + 1)] = this.val$a[(i3 + 1)];
//								}
//								FloatFFT_3D.this.fftSlices
//										.complexForward(arrayOfFloat);
//								for (i1 = 0; i1 < FloatFFT_3D.this.slices; ++i1) {
//									i2 = 2 * i1;
//									i3 = i1 * this.val$twoSliceStride + j + l;
//									this.val$a[i3] = arrayOfFloat[i2];
//									this.val$a[(i3 + 1)] = arrayOfFloat[(i2 + 1)];
//								}
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			i5 = this.slices / i3;
//			for (i8 = 0; i8 < i3; ++i8) {
//				i9 = i8 * i5;
//				i10 = (i8 == i3 - 1) ? this.slices : i9 + i5;
//				arrayOfFuture[i8] = ConcurrencyUtils.submit(new Runnable(i9,
//						i10, l, k, i1, i, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = (FloatFFT_3D.this.slices - i)
//									% FloatFFT_3D.this.slices;
//							int k = j * this.val$twoSliceStride;
//							int l = i * this.val$twoSliceStride;
//							for (int i1 = 1; i1 < this.val$n2d2; ++i1) {
//								int i2 = FloatFFT_3D.this.rows - i1;
//								int i3 = i2 * this.val$twoRowStride;
//								int i4 = i1 * this.val$twoRowStride;
//								int i5 = k + i3;
//								for (int i6 = 0; i6 < FloatFFT_3D.this.columns; ++i6) {
//									int i7 = 2 * i6;
//									int i8 = this.val$twon3 - i7;
//									int i9 = l + i4 + i7;
//									this.val$a[(i5 + i8 % this.val$twon3)] = this.val$a[i9];
//									this.val$a[(i5 + (i8 + 1) % this.val$twon3)] = (-this.val$a[(i9 + 1)]);
//								}
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			int i7;
//			for (int i4 = this.slices - 1; i4 >= 0; --i4) {
//				i5 = i4 * this.sliceStride;
//				i7 = i4 * l;
//				for (i8 = this.rows - 1; i8 >= 0; --i8) {
//					System.arraycopy(paramArrayOfFloat, i5 + i8
//							* this.rowStride, arrayOfFloat, 0, this.columns);
//					this.fftColumns.realForwardFull(arrayOfFloat);
//					System.arraycopy(arrayOfFloat, 0, paramArrayOfFloat, i7
//							+ i8 * i1, i);
//				}
//			}
//			arrayOfFloat = new float[2 * this.rows];
//			int i11;
//			for (i4 = 0; i4 < this.slices; ++i4) {
//				i5 = i4 * l;
//				for (i7 = 0; i7 < this.columns; ++i7) {
//					i8 = 2 * i7;
//					for (i9 = 0; i9 < this.rows; ++i9) {
//						i10 = 2 * i9;
//						i11 = i5 + i9 * i1 + i8;
//						arrayOfFloat[i10] = paramArrayOfFloat[i11];
//						arrayOfFloat[(i10 + 1)] = paramArrayOfFloat[(i11 + 1)];
//					}
//					this.fftRows.complexForward(arrayOfFloat);
//					for (i9 = 0; i9 < this.rows; ++i9) {
//						i10 = 2 * i9;
//						i11 = i5 + i9 * i1 + i8;
//						paramArrayOfFloat[i11] = arrayOfFloat[i10];
//						paramArrayOfFloat[(i11 + 1)] = arrayOfFloat[(i10 + 1)];
//					}
//				}
//			}
//			arrayOfFloat = new float[2 * this.slices];
//			for (i4 = 0; i4 < j; ++i4) {
//				i5 = i4 * i1;
//				for (i7 = 0; i7 < this.columns; ++i7) {
//					i8 = 2 * i7;
//					for (i9 = 0; i9 < this.slices; ++i9) {
//						i10 = 2 * i9;
//						i11 = i9 * l + i5 + i8;
//						arrayOfFloat[i10] = paramArrayOfFloat[i11];
//						arrayOfFloat[(i10 + 1)] = paramArrayOfFloat[(i11 + 1)];
//					}
//					this.fftSlices.complexForward(arrayOfFloat);
//					for (i9 = 0; i9 < this.slices; ++i9) {
//						i10 = 2 * i9;
//						i11 = i9 * l + i5 + i8;
//						paramArrayOfFloat[i11] = arrayOfFloat[i10];
//						paramArrayOfFloat[(i11 + 1)] = arrayOfFloat[(i10 + 1)];
//					}
//				}
//			}
//			for (i4 = 0; i4 < this.slices; ++i4) {
//				i5 = (this.slices - i4) % this.slices;
//				i7 = i5 * l;
//				i8 = i4 * l;
//				for (i9 = 1; i9 < k; ++i9) {
//					i10 = this.rows - i9;
//					i11 = i10 * i1;
//					int i12 = i9 * i1;
//					int i13 = i7 + i11;
//					for (int i14 = 0; i14 < this.columns; ++i14) {
//						int i15 = 2 * i14;
//						int i16 = i - i15;
//						int i17 = i8 + i12 + i15;
//						paramArrayOfFloat[(i13 + i16 % i)] = paramArrayOfFloat[i17];
//						paramArrayOfFloat[(i13 + (i16 + 1) % i)] = (-paramArrayOfFloat[(i17 + 1)]);
//					}
//				}
//			}
//		}
//	}
//
//	private strictfp void mixedRadixRealInverseFull(float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		int i = 2 * this.columns;
//		float[] arrayOfFloat = new float[i];
//		int j = this.rows / 2 + 1;
//		int k;
//		if (this.rows % 2 == 0)
//			k = this.rows / 2;
//		else
//			k = (this.rows + 1) / 2;
//		int l = 2 * this.sliceStride;
//		int i1 = 2 * this.rowStride;
//		int i2 = this.slices / 2;
//		int i3 = ConcurrencyUtils.getNumberOfThreads();
//		int i5;
//		int i8;
//		int i9;
//		int i10;
//		if ((i3 > 1) && (this.useThreads) && (i2 >= i3) && (this.columns >= i3)
//				&& (j >= i3)) {
//			Future[] arrayOfFuture = new Future[i3];
//			i5 = i2 / i3;
//			for (int i6 = 0; i6 < i3; ++i6) {
//				i8 = this.slices - 1 - (i6 * i5);
//				i9 = (i6 == i3 - 1) ? i2 + 1 : i8 - i5;
//				arrayOfFuture[i6] = ConcurrencyUtils.submit(new Runnable(i, i8,
//						i9, l, paramArrayOfFloat, paramBoolean, i1) {
//					public strictfp void run() {
//						float[] arrayOfFloat = new float[this.val$twon3];
//						for (int i = this.val$firstSlice; i >= this.val$lastSlice; --i) {
//							int j = i * FloatFFT_3D.this.sliceStride;
//							int k = i * this.val$twoSliceStride;
//							for (int l = FloatFFT_3D.this.rows - 1; l >= 0; --l) {
//								System.arraycopy(this.val$a, j + l
//										* FloatFFT_3D.this.rowStride,
//										arrayOfFloat, 0,
//										FloatFFT_3D.this.columns);
//								FloatFFT_3D.this.fftColumns.realInverseFull(
//										arrayOfFloat, this.val$scale);
//								System.arraycopy(arrayOfFloat, 0, this.val$a, k
//										+ l * this.val$twoRowStride,
//										this.val$twon3);
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			float[][][] arrayOfFloat1 = new float[i2 + 1][this.rows][i];
//			for (i8 = 0; i8 < i3; ++i8) {
//				i9 = i8 * i5;
//				i10 = (i8 == i3 - 1) ? i2 + 1 : i9 + i5;
//				arrayOfFuture[i8] = ConcurrencyUtils.submit(new Runnable(i9,
//						i10, paramArrayOfFloat, arrayOfFloat1, paramBoolean) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = i * FloatFFT_3D.this.sliceStride;
//							for (int k = 0; k < FloatFFT_3D.this.rows; ++k) {
//								System.arraycopy(this.val$a, j + k
//										* FloatFFT_3D.this.rowStride,
//										this.val$temp2[i][k], 0,
//										FloatFFT_3D.this.columns);
//								FloatFFT_3D.this.fftColumns.realInverseFull(
//										this.val$temp2[i][k], this.val$scale);
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i8 = 0; i8 < i3; ++i8) {
//				i9 = i8 * i5;
//				i10 = (i8 == i3 - 1) ? i2 + 1 : i9 + i5;
//				arrayOfFuture[i8] = ConcurrencyUtils.submit(new Runnable(i9,
//						i10, l, arrayOfFloat1, paramArrayOfFloat, i1, i) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = i * this.val$twoSliceStride;
//							for (int k = 0; k < FloatFFT_3D.this.rows; ++k)
//								System.arraycopy(this.val$temp2[i][k], 0,
//										this.val$a, j + k
//												* this.val$twoRowStride,
//										this.val$twon3);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			i5 = this.slices / i3;
//			for (i8 = 0; i8 < i3; ++i8) {
//				i9 = i8 * i5;
//				i10 = (i8 == i3 - 1) ? this.slices : i9 + i5;
//				arrayOfFuture[i8] = ConcurrencyUtils.submit(new Runnable(i9,
//						i10, l, i1, paramArrayOfFloat, paramBoolean) {
//					public strictfp void run() {
//						float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.rows];
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = i * this.val$twoSliceStride;
//							for (int k = 0; k < FloatFFT_3D.this.columns; ++k) {
//								int l = 2 * k;
//								int i2;
//								int i3;
//								for (int i1 = 0; i1 < FloatFFT_3D.this.rows; ++i1) {
//									i2 = j + i1 * this.val$twoRowStride + l;
//									i3 = 2 * i1;
//									arrayOfFloat[i3] = this.val$a[i2];
//									arrayOfFloat[(i3 + 1)] = this.val$a[(i2 + 1)];
//								}
//								FloatFFT_3D.this.fftRows.complexInverse(
//										arrayOfFloat, this.val$scale);
//								for (i1 = 0; i1 < FloatFFT_3D.this.rows; ++i1) {
//									i2 = j + i1 * this.val$twoRowStride + l;
//									i3 = 2 * i1;
//									this.val$a[i2] = arrayOfFloat[i3];
//									this.val$a[(i2 + 1)] = arrayOfFloat[(i3 + 1)];
//								}
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			i5 = j / i3;
//			for (i8 = 0; i8 < i3; ++i8) {
//				i9 = i8 * i5;
//				i10 = (i8 == i3 - 1) ? j : i9 + i5;
//				arrayOfFuture[i8] = ConcurrencyUtils.submit(new Runnable(i9,
//						i10, i1, l, paramArrayOfFloat, paramBoolean) {
//					public strictfp void run() {
//						float[] arrayOfFloat = new float[2 * FloatFFT_3D.this.slices];
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = i * this.val$twoRowStride;
//							for (int k = 0; k < FloatFFT_3D.this.columns; ++k) {
//								int l = 2 * k;
//								int i2;
//								int i3;
//								for (int i1 = 0; i1 < FloatFFT_3D.this.slices; ++i1) {
//									i2 = 2 * i1;
//									i3 = i1 * this.val$twoSliceStride + j + l;
//									arrayOfFloat[i2] = this.val$a[i3];
//									arrayOfFloat[(i2 + 1)] = this.val$a[(i3 + 1)];
//								}
//								FloatFFT_3D.this.fftSlices.complexInverse(
//										arrayOfFloat, this.val$scale);
//								for (i1 = 0; i1 < FloatFFT_3D.this.slices; ++i1) {
//									i2 = 2 * i1;
//									i3 = i1 * this.val$twoSliceStride + j + l;
//									this.val$a[i3] = arrayOfFloat[i2];
//									this.val$a[(i3 + 1)] = arrayOfFloat[(i2 + 1)];
//								}
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			i5 = this.slices / i3;
//			for (i8 = 0; i8 < i3; ++i8) {
//				i9 = i8 * i5;
//				i10 = (i8 == i3 - 1) ? this.slices : i9 + i5;
//				arrayOfFuture[i8] = ConcurrencyUtils.submit(new Runnable(i9,
//						i10, l, k, i1, i, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = (FloatFFT_3D.this.slices - i)
//									% FloatFFT_3D.this.slices;
//							int k = j * this.val$twoSliceStride;
//							int l = i * this.val$twoSliceStride;
//							for (int i1 = 1; i1 < this.val$n2d2; ++i1) {
//								int i2 = FloatFFT_3D.this.rows - i1;
//								int i3 = i2 * this.val$twoRowStride;
//								int i4 = i1 * this.val$twoRowStride;
//								int i5 = k + i3;
//								for (int i6 = 0; i6 < FloatFFT_3D.this.columns; ++i6) {
//									int i7 = 2 * i6;
//									int i8 = this.val$twon3 - i7;
//									int i9 = l + i4 + i7;
//									this.val$a[(i5 + i8 % this.val$twon3)] = this.val$a[i9];
//									this.val$a[(i5 + (i8 + 1) % this.val$twon3)] = (-this.val$a[(i9 + 1)]);
//								}
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			int i7;
//			for (int i4 = this.slices - 1; i4 >= 0; --i4) {
//				i5 = i4 * this.sliceStride;
//				i7 = i4 * l;
//				for (i8 = this.rows - 1; i8 >= 0; --i8) {
//					System.arraycopy(paramArrayOfFloat, i5 + i8
//							* this.rowStride, arrayOfFloat, 0, this.columns);
//					this.fftColumns.realInverseFull(arrayOfFloat, paramBoolean);
//					System.arraycopy(arrayOfFloat, 0, paramArrayOfFloat, i7
//							+ i8 * i1, i);
//				}
//			}
//			arrayOfFloat = new float[2 * this.rows];
//			int i11;
//			for (i4 = 0; i4 < this.slices; ++i4) {
//				i5 = i4 * l;
//				for (i7 = 0; i7 < this.columns; ++i7) {
//					i8 = 2 * i7;
//					for (i9 = 0; i9 < this.rows; ++i9) {
//						i10 = 2 * i9;
//						i11 = i5 + i9 * i1 + i8;
//						arrayOfFloat[i10] = paramArrayOfFloat[i11];
//						arrayOfFloat[(i10 + 1)] = paramArrayOfFloat[(i11 + 1)];
//					}
//					this.fftRows.complexInverse(arrayOfFloat, paramBoolean);
//					for (i9 = 0; i9 < this.rows; ++i9) {
//						i10 = 2 * i9;
//						i11 = i5 + i9 * i1 + i8;
//						paramArrayOfFloat[i11] = arrayOfFloat[i10];
//						paramArrayOfFloat[(i11 + 1)] = arrayOfFloat[(i10 + 1)];
//					}
//				}
//			}
//			arrayOfFloat = new float[2 * this.slices];
//			for (i4 = 0; i4 < j; ++i4) {
//				i5 = i4 * i1;
//				for (i7 = 0; i7 < this.columns; ++i7) {
//					i8 = 2 * i7;
//					for (i9 = 0; i9 < this.slices; ++i9) {
//						i10 = 2 * i9;
//						i11 = i9 * l + i5 + i8;
//						arrayOfFloat[i10] = paramArrayOfFloat[i11];
//						arrayOfFloat[(i10 + 1)] = paramArrayOfFloat[(i11 + 1)];
//					}
//					this.fftSlices.complexInverse(arrayOfFloat, paramBoolean);
//					for (i9 = 0; i9 < this.slices; ++i9) {
//						i10 = 2 * i9;
//						i11 = i9 * l + i5 + i8;
//						paramArrayOfFloat[i11] = arrayOfFloat[i10];
//						paramArrayOfFloat[(i11 + 1)] = arrayOfFloat[(i10 + 1)];
//					}
//				}
//			}
//			for (i4 = 0; i4 < this.slices; ++i4) {
//				i5 = (this.slices - i4) % this.slices;
//				i7 = i5 * l;
//				i8 = i4 * l;
//				for (i9 = 1; i9 < k; ++i9) {
//					i10 = this.rows - i9;
//					i11 = i10 * i1;
//					int i12 = i9 * i1;
//					int i13 = i7 + i11;
//					for (int i14 = 0; i14 < this.columns; ++i14) {
//						int i15 = 2 * i14;
//						int i16 = i - i15;
//						int i17 = i8 + i12 + i15;
//						paramArrayOfFloat[(i13 + i16 % i)] = paramArrayOfFloat[i17];
//						paramArrayOfFloat[(i13 + (i16 + 1) % i)] = (-paramArrayOfFloat[(i17 + 1)]);
//					}
//				}
//			}
//		}
//	}
//
//	private strictfp void xdft3da_sub1(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat, boolean paramBoolean) {
//		int i3;
//		int i;
//		int i4;
//		int i5;
//		int j;
//		int k;
//		int l;
//		int i1;
//		int i2;
//		if (paramInt2 == -1)
//			for (i3 = 0; i3 < this.slices; ++i3) {
//				i = i3 * this.sliceStride;
//				if (paramInt1 == 0)
//					for (i4 = 0; i4 < this.rows; ++i4)
//						this.fftColumns.complexForward(paramArrayOfFloat, i
//								+ i4 * this.rowStride);
//				else
//					for (i4 = 0; i4 < this.rows; ++i4)
//						this.fftColumns.realInverse(paramArrayOfFloat, i + i4
//								* this.rowStride, paramBoolean);
//				if (this.columns > 4) {
//					for (i4 = 0; i4 < this.columns; i4 += 8) {
//						for (i5 = 0; i5 < this.rows; ++i5) {
//							j = i + i5 * this.rowStride + i4;
//							k = 2 * i5;
//							l = 2 * this.rows + 2 * i5;
//							i1 = l + 2 * this.rows;
//							i2 = i1 + 2 * this.rows;
//							this.t[k] = paramArrayOfFloat[j];
//							this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//							this.t[l] = paramArrayOfFloat[(j + 2)];
//							this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//							this.t[i1] = paramArrayOfFloat[(j + 4)];
//							this.t[(i1 + 1)] = paramArrayOfFloat[(j + 5)];
//							this.t[i2] = paramArrayOfFloat[(j + 6)];
//							this.t[(i2 + 1)] = paramArrayOfFloat[(j + 7)];
//						}
//						this.fftRows.complexForward(this.t, 0);
//						this.fftRows.complexForward(this.t, 2 * this.rows);
//						this.fftRows.complexForward(this.t, 4 * this.rows);
//						this.fftRows.complexForward(this.t, 6 * this.rows);
//						for (i5 = 0; i5 < this.rows; ++i5) {
//							j = i + i5 * this.rowStride + i4;
//							k = 2 * i5;
//							l = 2 * this.rows + 2 * i5;
//							i1 = l + 2 * this.rows;
//							i2 = i1 + 2 * this.rows;
//							paramArrayOfFloat[j] = this.t[k];
//							paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//							paramArrayOfFloat[(j + 2)] = this.t[l];
//							paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//							paramArrayOfFloat[(j + 4)] = this.t[i1];
//							paramArrayOfFloat[(j + 5)] = this.t[(i1 + 1)];
//							paramArrayOfFloat[(j + 6)] = this.t[i2];
//							paramArrayOfFloat[(j + 7)] = this.t[(i2 + 1)];
//						}
//					}
//				} else if (this.columns == 4) {
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						l = 2 * this.rows + 2 * i4;
//						this.t[k] = paramArrayOfFloat[j];
//						this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//						this.t[l] = paramArrayOfFloat[(j + 2)];
//						this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//					}
//					this.fftRows.complexForward(this.t, 0);
//					this.fftRows.complexForward(this.t, 2 * this.rows);
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						l = 2 * this.rows + 2 * i4;
//						paramArrayOfFloat[j] = this.t[k];
//						paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//						paramArrayOfFloat[(j + 2)] = this.t[l];
//						paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						this.t[k] = paramArrayOfFloat[j];
//						this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//					}
//					this.fftRows.complexForward(this.t, 0);
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						paramArrayOfFloat[j] = this.t[k];
//						paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//					}
//				}
//			}
//		else
//			for (i3 = 0; i3 < this.slices; ++i3) {
//				i = i3 * this.sliceStride;
//				if (paramInt1 == 0)
//					for (i4 = 0; i4 < this.rows; ++i4)
//						this.fftColumns.complexInverse(paramArrayOfFloat, i
//								+ i4 * this.rowStride, paramBoolean);
//				if (this.columns > 4) {
//					for (i4 = 0; i4 < this.columns; i4 += 8) {
//						for (i5 = 0; i5 < this.rows; ++i5) {
//							j = i + i5 * this.rowStride + i4;
//							k = 2 * i5;
//							l = 2 * this.rows + 2 * i5;
//							i1 = l + 2 * this.rows;
//							i2 = i1 + 2 * this.rows;
//							this.t[k] = paramArrayOfFloat[j];
//							this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//							this.t[l] = paramArrayOfFloat[(j + 2)];
//							this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//							this.t[i1] = paramArrayOfFloat[(j + 4)];
//							this.t[(i1 + 1)] = paramArrayOfFloat[(j + 5)];
//							this.t[i2] = paramArrayOfFloat[(j + 6)];
//							this.t[(i2 + 1)] = paramArrayOfFloat[(j + 7)];
//						}
//						this.fftRows.complexInverse(this.t, 0, paramBoolean);
//						this.fftRows.complexInverse(this.t, 2 * this.rows,
//								paramBoolean);
//						this.fftRows.complexInverse(this.t, 4 * this.rows,
//								paramBoolean);
//						this.fftRows.complexInverse(this.t, 6 * this.rows,
//								paramBoolean);
//						for (i5 = 0; i5 < this.rows; ++i5) {
//							j = i + i5 * this.rowStride + i4;
//							k = 2 * i5;
//							l = 2 * this.rows + 2 * i5;
//							i1 = l + 2 * this.rows;
//							i2 = i1 + 2 * this.rows;
//							paramArrayOfFloat[j] = this.t[k];
//							paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//							paramArrayOfFloat[(j + 2)] = this.t[l];
//							paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//							paramArrayOfFloat[(j + 4)] = this.t[i1];
//							paramArrayOfFloat[(j + 5)] = this.t[(i1 + 1)];
//							paramArrayOfFloat[(j + 6)] = this.t[i2];
//							paramArrayOfFloat[(j + 7)] = this.t[(i2 + 1)];
//						}
//					}
//				} else if (this.columns == 4) {
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						l = 2 * this.rows + 2 * i4;
//						this.t[k] = paramArrayOfFloat[j];
//						this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//						this.t[l] = paramArrayOfFloat[(j + 2)];
//						this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//					}
//					this.fftRows.complexInverse(this.t, 0, paramBoolean);
//					this.fftRows.complexInverse(this.t, 2 * this.rows,
//							paramBoolean);
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						l = 2 * this.rows + 2 * i4;
//						paramArrayOfFloat[j] = this.t[k];
//						paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//						paramArrayOfFloat[(j + 2)] = this.t[l];
//						paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//					}
//				} else if (this.columns == 2) {
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						this.t[k] = paramArrayOfFloat[j];
//						this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//					}
//					this.fftRows.complexInverse(this.t, 0, paramBoolean);
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						paramArrayOfFloat[j] = this.t[k];
//						paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//					}
//				}
//				if (paramInt1 == 0)
//					continue;
//				for (i4 = 0; i4 < this.rows; ++i4)
//					this.fftColumns.realForward(paramArrayOfFloat, i + i4
//							* this.rowStride);
//			}
//	}
//
//	private strictfp void xdft3da_sub2(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat, boolean paramBoolean) {
//		int i3;
//		int i;
//		int i4;
//		int i5;
//		int j;
//		int k;
//		int l;
//		int i1;
//		int i2;
//		if (paramInt2 == -1)
//			for (i3 = 0; i3 < this.slices; ++i3) {
//				i = i3 * this.sliceStride;
//				if (paramInt1 == 0)
//					for (i4 = 0; i4 < this.rows; ++i4)
//						this.fftColumns.complexForward(paramArrayOfFloat, i
//								+ i4 * this.rowStride);
//				else
//					for (i4 = 0; i4 < this.rows; ++i4)
//						this.fftColumns.realForward(paramArrayOfFloat, i + i4
//								* this.rowStride);
//				if (this.columns > 4) {
//					for (i4 = 0; i4 < this.columns; i4 += 8) {
//						for (i5 = 0; i5 < this.rows; ++i5) {
//							j = i + i5 * this.rowStride + i4;
//							k = 2 * i5;
//							l = 2 * this.rows + 2 * i5;
//							i1 = l + 2 * this.rows;
//							i2 = i1 + 2 * this.rows;
//							this.t[k] = paramArrayOfFloat[j];
//							this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//							this.t[l] = paramArrayOfFloat[(j + 2)];
//							this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//							this.t[i1] = paramArrayOfFloat[(j + 4)];
//							this.t[(i1 + 1)] = paramArrayOfFloat[(j + 5)];
//							this.t[i2] = paramArrayOfFloat[(j + 6)];
//							this.t[(i2 + 1)] = paramArrayOfFloat[(j + 7)];
//						}
//						this.fftRows.complexForward(this.t, 0);
//						this.fftRows.complexForward(this.t, 2 * this.rows);
//						this.fftRows.complexForward(this.t, 4 * this.rows);
//						this.fftRows.complexForward(this.t, 6 * this.rows);
//						for (i5 = 0; i5 < this.rows; ++i5) {
//							j = i + i5 * this.rowStride + i4;
//							k = 2 * i5;
//							l = 2 * this.rows + 2 * i5;
//							i1 = l + 2 * this.rows;
//							i2 = i1 + 2 * this.rows;
//							paramArrayOfFloat[j] = this.t[k];
//							paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//							paramArrayOfFloat[(j + 2)] = this.t[l];
//							paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//							paramArrayOfFloat[(j + 4)] = this.t[i1];
//							paramArrayOfFloat[(j + 5)] = this.t[(i1 + 1)];
//							paramArrayOfFloat[(j + 6)] = this.t[i2];
//							paramArrayOfFloat[(j + 7)] = this.t[(i2 + 1)];
//						}
//					}
//				} else if (this.columns == 4) {
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						l = 2 * this.rows + 2 * i4;
//						this.t[k] = paramArrayOfFloat[j];
//						this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//						this.t[l] = paramArrayOfFloat[(j + 2)];
//						this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//					}
//					this.fftRows.complexForward(this.t, 0);
//					this.fftRows.complexForward(this.t, 2 * this.rows);
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						l = 2 * this.rows + 2 * i4;
//						paramArrayOfFloat[j] = this.t[k];
//						paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//						paramArrayOfFloat[(j + 2)] = this.t[l];
//						paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						this.t[k] = paramArrayOfFloat[j];
//						this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//					}
//					this.fftRows.complexForward(this.t, 0);
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						paramArrayOfFloat[j] = this.t[k];
//						paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//					}
//				}
//			}
//		else
//			for (i3 = 0; i3 < this.slices; ++i3) {
//				i = i3 * this.sliceStride;
//				if (paramInt1 == 0)
//					for (i4 = 0; i4 < this.rows; ++i4)
//						this.fftColumns.complexInverse(paramArrayOfFloat, i
//								+ i4 * this.rowStride, paramBoolean);
//				else
//					for (i4 = 0; i4 < this.rows; ++i4)
//						this.fftColumns.realInverse2(paramArrayOfFloat, i + i4
//								* this.rowStride, paramBoolean);
//				if (this.columns > 4) {
//					for (i4 = 0; i4 < this.columns; i4 += 8) {
//						for (i5 = 0; i5 < this.rows; ++i5) {
//							j = i + i5 * this.rowStride + i4;
//							k = 2 * i5;
//							l = 2 * this.rows + 2 * i5;
//							i1 = l + 2 * this.rows;
//							i2 = i1 + 2 * this.rows;
//							this.t[k] = paramArrayOfFloat[j];
//							this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//							this.t[l] = paramArrayOfFloat[(j + 2)];
//							this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//							this.t[i1] = paramArrayOfFloat[(j + 4)];
//							this.t[(i1 + 1)] = paramArrayOfFloat[(j + 5)];
//							this.t[i2] = paramArrayOfFloat[(j + 6)];
//							this.t[(i2 + 1)] = paramArrayOfFloat[(j + 7)];
//						}
//						this.fftRows.complexInverse(this.t, 0, paramBoolean);
//						this.fftRows.complexInverse(this.t, 2 * this.rows,
//								paramBoolean);
//						this.fftRows.complexInverse(this.t, 4 * this.rows,
//								paramBoolean);
//						this.fftRows.complexInverse(this.t, 6 * this.rows,
//								paramBoolean);
//						for (i5 = 0; i5 < this.rows; ++i5) {
//							j = i + i5 * this.rowStride + i4;
//							k = 2 * i5;
//							l = 2 * this.rows + 2 * i5;
//							i1 = l + 2 * this.rows;
//							i2 = i1 + 2 * this.rows;
//							paramArrayOfFloat[j] = this.t[k];
//							paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//							paramArrayOfFloat[(j + 2)] = this.t[l];
//							paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//							paramArrayOfFloat[(j + 4)] = this.t[i1];
//							paramArrayOfFloat[(j + 5)] = this.t[(i1 + 1)];
//							paramArrayOfFloat[(j + 6)] = this.t[i2];
//							paramArrayOfFloat[(j + 7)] = this.t[(i2 + 1)];
//						}
//					}
//				} else if (this.columns == 4) {
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						l = 2 * this.rows + 2 * i4;
//						this.t[k] = paramArrayOfFloat[j];
//						this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//						this.t[l] = paramArrayOfFloat[(j + 2)];
//						this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//					}
//					this.fftRows.complexInverse(this.t, 0, paramBoolean);
//					this.fftRows.complexInverse(this.t, 2 * this.rows,
//							paramBoolean);
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						l = 2 * this.rows + 2 * i4;
//						paramArrayOfFloat[j] = this.t[k];
//						paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//						paramArrayOfFloat[(j + 2)] = this.t[l];
//						paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						this.t[k] = paramArrayOfFloat[j];
//						this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//					}
//					this.fftRows.complexInverse(this.t, 0, paramBoolean);
//					for (i4 = 0; i4 < this.rows; ++i4) {
//						j = i + i4 * this.rowStride;
//						k = 2 * i4;
//						paramArrayOfFloat[j] = this.t[k];
//						paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//					}
//				}
//			}
//	}
//
//	private strictfp void xdft3da_sub1(int paramInt1, int paramInt2,
//			float[][][] paramArrayOfFloat, boolean paramBoolean) {
//		int i1;
//		int i2;
//		int i3;
//		int i;
//		int j;
//		int k;
//		int l;
//		if (paramInt2 == -1)
//			for (i1 = 0; i1 < this.slices; ++i1) {
//				if (paramInt1 == 0)
//					for (i2 = 0; i2 < this.rows; ++i2)
//						this.fftColumns
//								.complexForward(paramArrayOfFloat[i1][i2]);
//				else
//					for (i2 = 0; i2 < this.rows; ++i2)
//						this.fftColumns.realInverse(paramArrayOfFloat[i1][i2],
//								0, paramBoolean);
//				if (this.columns > 4) {
//					for (i2 = 0; i2 < this.columns; i2 += 8) {
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i = 2 * i3;
//							j = 2 * this.rows + 2 * i3;
//							k = j + 2 * this.rows;
//							l = k + 2 * this.rows;
//							this.t[i] = paramArrayOfFloat[i1][i3][i2];
//							this.t[(i + 1)] = paramArrayOfFloat[i1][i3][(i2 + 1)];
//							this.t[j] = paramArrayOfFloat[i1][i3][(i2 + 2)];
//							this.t[(j + 1)] = paramArrayOfFloat[i1][i3][(i2 + 3)];
//							this.t[k] = paramArrayOfFloat[i1][i3][(i2 + 4)];
//							this.t[(k + 1)] = paramArrayOfFloat[i1][i3][(i2 + 5)];
//							this.t[l] = paramArrayOfFloat[i1][i3][(i2 + 6)];
//							this.t[(l + 1)] = paramArrayOfFloat[i1][i3][(i2 + 7)];
//						}
//						this.fftRows.complexForward(this.t, 0);
//						this.fftRows.complexForward(this.t, 2 * this.rows);
//						this.fftRows.complexForward(this.t, 4 * this.rows);
//						this.fftRows.complexForward(this.t, 6 * this.rows);
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i = 2 * i3;
//							j = 2 * this.rows + 2 * i3;
//							k = j + 2 * this.rows;
//							l = k + 2 * this.rows;
//							paramArrayOfFloat[i1][i3][i2] = this.t[i];
//							paramArrayOfFloat[i1][i3][(i2 + 1)] = this.t[(i + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 2)] = this.t[j];
//							paramArrayOfFloat[i1][i3][(i2 + 3)] = this.t[(j + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 4)] = this.t[k];
//							paramArrayOfFloat[i1][i3][(i2 + 5)] = this.t[(k + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 6)] = this.t[l];
//							paramArrayOfFloat[i1][i3][(i2 + 7)] = this.t[(l + 1)];
//						}
//					}
//				} else if (this.columns == 4) {
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.rows + 2 * i2;
//						this.t[i] = paramArrayOfFloat[i1][i2][0];
//						this.t[(i + 1)] = paramArrayOfFloat[i1][i2][1];
//						this.t[j] = paramArrayOfFloat[i1][i2][2];
//						this.t[(j + 1)] = paramArrayOfFloat[i1][i2][3];
//					}
//					this.fftRows.complexForward(this.t, 0);
//					this.fftRows.complexForward(this.t, 2 * this.rows);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.rows + 2 * i2;
//						paramArrayOfFloat[i1][i2][0] = this.t[i];
//						paramArrayOfFloat[i1][i2][1] = this.t[(i + 1)];
//						paramArrayOfFloat[i1][i2][2] = this.t[j];
//						paramArrayOfFloat[i1][i2][3] = this.t[(j + 1)];
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						this.t[i] = paramArrayOfFloat[i1][i2][0];
//						this.t[(i + 1)] = paramArrayOfFloat[i1][i2][1];
//					}
//					this.fftRows.complexForward(this.t, 0);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						paramArrayOfFloat[i1][i2][0] = this.t[i];
//						paramArrayOfFloat[i1][i2][1] = this.t[(i + 1)];
//					}
//				}
//			}
//		else
//			for (i1 = 0; i1 < this.slices; ++i1) {
//				if (paramInt1 == 0)
//					for (i2 = 0; i2 < this.rows; ++i2)
//						this.fftColumns.complexInverse(
//								paramArrayOfFloat[i1][i2], paramBoolean);
//				if (this.columns > 4) {
//					for (i2 = 0; i2 < this.columns; i2 += 8) {
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i = 2 * i3;
//							j = 2 * this.rows + 2 * i3;
//							k = j + 2 * this.rows;
//							l = k + 2 * this.rows;
//							this.t[i] = paramArrayOfFloat[i1][i3][i2];
//							this.t[(i + 1)] = paramArrayOfFloat[i1][i3][(i2 + 1)];
//							this.t[j] = paramArrayOfFloat[i1][i3][(i2 + 2)];
//							this.t[(j + 1)] = paramArrayOfFloat[i1][i3][(i2 + 3)];
//							this.t[k] = paramArrayOfFloat[i1][i3][(i2 + 4)];
//							this.t[(k + 1)] = paramArrayOfFloat[i1][i3][(i2 + 5)];
//							this.t[l] = paramArrayOfFloat[i1][i3][(i2 + 6)];
//							this.t[(l + 1)] = paramArrayOfFloat[i1][i3][(i2 + 7)];
//						}
//						this.fftRows.complexInverse(this.t, 0, paramBoolean);
//						this.fftRows.complexInverse(this.t, 2 * this.rows,
//								paramBoolean);
//						this.fftRows.complexInverse(this.t, 4 * this.rows,
//								paramBoolean);
//						this.fftRows.complexInverse(this.t, 6 * this.rows,
//								paramBoolean);
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i = 2 * i3;
//							j = 2 * this.rows + 2 * i3;
//							k = j + 2 * this.rows;
//							l = k + 2 * this.rows;
//							paramArrayOfFloat[i1][i3][i2] = this.t[i];
//							paramArrayOfFloat[i1][i3][(i2 + 1)] = this.t[(i + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 2)] = this.t[j];
//							paramArrayOfFloat[i1][i3][(i2 + 3)] = this.t[(j + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 4)] = this.t[k];
//							paramArrayOfFloat[i1][i3][(i2 + 5)] = this.t[(k + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 6)] = this.t[l];
//							paramArrayOfFloat[i1][i3][(i2 + 7)] = this.t[(l + 1)];
//						}
//					}
//				} else if (this.columns == 4) {
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.rows + 2 * i2;
//						this.t[i] = paramArrayOfFloat[i1][i2][0];
//						this.t[(i + 1)] = paramArrayOfFloat[i1][i2][1];
//						this.t[j] = paramArrayOfFloat[i1][i2][2];
//						this.t[(j + 1)] = paramArrayOfFloat[i1][i2][3];
//					}
//					this.fftRows.complexInverse(this.t, 0, paramBoolean);
//					this.fftRows.complexInverse(this.t, 2 * this.rows,
//							paramBoolean);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.rows + 2 * i2;
//						paramArrayOfFloat[i1][i2][0] = this.t[i];
//						paramArrayOfFloat[i1][i2][1] = this.t[(i + 1)];
//						paramArrayOfFloat[i1][i2][2] = this.t[j];
//						paramArrayOfFloat[i1][i2][3] = this.t[(j + 1)];
//					}
//				} else if (this.columns == 2) {
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						this.t[i] = paramArrayOfFloat[i1][i2][0];
//						this.t[(i + 1)] = paramArrayOfFloat[i1][i2][1];
//					}
//					this.fftRows.complexInverse(this.t, 0, paramBoolean);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						paramArrayOfFloat[i1][i2][0] = this.t[i];
//						paramArrayOfFloat[i1][i2][1] = this.t[(i + 1)];
//					}
//				}
//				if (paramInt1 == 0)
//					continue;
//				for (i2 = 0; i2 < this.rows; ++i2)
//					this.fftColumns.realForward(paramArrayOfFloat[i1][i2], 0);
//			}
//	}
//
//	private strictfp void xdft3da_sub2(int paramInt1, int paramInt2,
//			float[][][] paramArrayOfFloat, boolean paramBoolean) {
//		int i1;
//		int i2;
//		int i3;
//		int i;
//		int j;
//		int k;
//		int l;
//		if (paramInt2 == -1)
//			for (i1 = 0; i1 < this.slices; ++i1) {
//				if (paramInt1 == 0)
//					for (i2 = 0; i2 < this.rows; ++i2)
//						this.fftColumns
//								.complexForward(paramArrayOfFloat[i1][i2]);
//				else
//					for (i2 = 0; i2 < this.rows; ++i2)
//						this.fftColumns.realForward(paramArrayOfFloat[i1][i2]);
//				if (this.columns > 4) {
//					for (i2 = 0; i2 < this.columns; i2 += 8) {
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i = 2 * i3;
//							j = 2 * this.rows + 2 * i3;
//							k = j + 2 * this.rows;
//							l = k + 2 * this.rows;
//							this.t[i] = paramArrayOfFloat[i1][i3][i2];
//							this.t[(i + 1)] = paramArrayOfFloat[i1][i3][(i2 + 1)];
//							this.t[j] = paramArrayOfFloat[i1][i3][(i2 + 2)];
//							this.t[(j + 1)] = paramArrayOfFloat[i1][i3][(i2 + 3)];
//							this.t[k] = paramArrayOfFloat[i1][i3][(i2 + 4)];
//							this.t[(k + 1)] = paramArrayOfFloat[i1][i3][(i2 + 5)];
//							this.t[l] = paramArrayOfFloat[i1][i3][(i2 + 6)];
//							this.t[(l + 1)] = paramArrayOfFloat[i1][i3][(i2 + 7)];
//						}
//						this.fftRows.complexForward(this.t, 0);
//						this.fftRows.complexForward(this.t, 2 * this.rows);
//						this.fftRows.complexForward(this.t, 4 * this.rows);
//						this.fftRows.complexForward(this.t, 6 * this.rows);
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i = 2 * i3;
//							j = 2 * this.rows + 2 * i3;
//							k = j + 2 * this.rows;
//							l = k + 2 * this.rows;
//							paramArrayOfFloat[i1][i3][i2] = this.t[i];
//							paramArrayOfFloat[i1][i3][(i2 + 1)] = this.t[(i + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 2)] = this.t[j];
//							paramArrayOfFloat[i1][i3][(i2 + 3)] = this.t[(j + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 4)] = this.t[k];
//							paramArrayOfFloat[i1][i3][(i2 + 5)] = this.t[(k + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 6)] = this.t[l];
//							paramArrayOfFloat[i1][i3][(i2 + 7)] = this.t[(l + 1)];
//						}
//					}
//				} else if (this.columns == 4) {
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.rows + 2 * i2;
//						this.t[i] = paramArrayOfFloat[i1][i2][0];
//						this.t[(i + 1)] = paramArrayOfFloat[i1][i2][1];
//						this.t[j] = paramArrayOfFloat[i1][i2][2];
//						this.t[(j + 1)] = paramArrayOfFloat[i1][i2][3];
//					}
//					this.fftRows.complexForward(this.t, 0);
//					this.fftRows.complexForward(this.t, 2 * this.rows);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.rows + 2 * i2;
//						paramArrayOfFloat[i1][i2][0] = this.t[i];
//						paramArrayOfFloat[i1][i2][1] = this.t[(i + 1)];
//						paramArrayOfFloat[i1][i2][2] = this.t[j];
//						paramArrayOfFloat[i1][i2][3] = this.t[(j + 1)];
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						this.t[i] = paramArrayOfFloat[i1][i2][0];
//						this.t[(i + 1)] = paramArrayOfFloat[i1][i2][1];
//					}
//					this.fftRows.complexForward(this.t, 0);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						paramArrayOfFloat[i1][i2][0] = this.t[i];
//						paramArrayOfFloat[i1][i2][1] = this.t[(i + 1)];
//					}
//				}
//			}
//		else
//			for (i1 = 0; i1 < this.slices; ++i1) {
//				if (paramInt1 == 0)
//					for (i2 = 0; i2 < this.rows; ++i2)
//						this.fftColumns.complexInverse(
//								paramArrayOfFloat[i1][i2], paramBoolean);
//				else
//					for (i2 = 0; i2 < this.rows; ++i2)
//						this.fftColumns.realInverse2(paramArrayOfFloat[i1][i2],
//								0, paramBoolean);
//				if (this.columns > 4) {
//					for (i2 = 0; i2 < this.columns; i2 += 8) {
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i = 2 * i3;
//							j = 2 * this.rows + 2 * i3;
//							k = j + 2 * this.rows;
//							l = k + 2 * this.rows;
//							this.t[i] = paramArrayOfFloat[i1][i3][i2];
//							this.t[(i + 1)] = paramArrayOfFloat[i1][i3][(i2 + 1)];
//							this.t[j] = paramArrayOfFloat[i1][i3][(i2 + 2)];
//							this.t[(j + 1)] = paramArrayOfFloat[i1][i3][(i2 + 3)];
//							this.t[k] = paramArrayOfFloat[i1][i3][(i2 + 4)];
//							this.t[(k + 1)] = paramArrayOfFloat[i1][i3][(i2 + 5)];
//							this.t[l] = paramArrayOfFloat[i1][i3][(i2 + 6)];
//							this.t[(l + 1)] = paramArrayOfFloat[i1][i3][(i2 + 7)];
//						}
//						this.fftRows.complexInverse(this.t, 0, paramBoolean);
//						this.fftRows.complexInverse(this.t, 2 * this.rows,
//								paramBoolean);
//						this.fftRows.complexInverse(this.t, 4 * this.rows,
//								paramBoolean);
//						this.fftRows.complexInverse(this.t, 6 * this.rows,
//								paramBoolean);
//						for (i3 = 0; i3 < this.rows; ++i3) {
//							i = 2 * i3;
//							j = 2 * this.rows + 2 * i3;
//							k = j + 2 * this.rows;
//							l = k + 2 * this.rows;
//							paramArrayOfFloat[i1][i3][i2] = this.t[i];
//							paramArrayOfFloat[i1][i3][(i2 + 1)] = this.t[(i + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 2)] = this.t[j];
//							paramArrayOfFloat[i1][i3][(i2 + 3)] = this.t[(j + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 4)] = this.t[k];
//							paramArrayOfFloat[i1][i3][(i2 + 5)] = this.t[(k + 1)];
//							paramArrayOfFloat[i1][i3][(i2 + 6)] = this.t[l];
//							paramArrayOfFloat[i1][i3][(i2 + 7)] = this.t[(l + 1)];
//						}
//					}
//				} else if (this.columns == 4) {
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.rows + 2 * i2;
//						this.t[i] = paramArrayOfFloat[i1][i2][0];
//						this.t[(i + 1)] = paramArrayOfFloat[i1][i2][1];
//						this.t[j] = paramArrayOfFloat[i1][i2][2];
//						this.t[(j + 1)] = paramArrayOfFloat[i1][i2][3];
//					}
//					this.fftRows.complexInverse(this.t, 0, paramBoolean);
//					this.fftRows.complexInverse(this.t, 2 * this.rows,
//							paramBoolean);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.rows + 2 * i2;
//						paramArrayOfFloat[i1][i2][0] = this.t[i];
//						paramArrayOfFloat[i1][i2][1] = this.t[(i + 1)];
//						paramArrayOfFloat[i1][i2][2] = this.t[j];
//						paramArrayOfFloat[i1][i2][3] = this.t[(j + 1)];
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						this.t[i] = paramArrayOfFloat[i1][i2][0];
//						this.t[(i + 1)] = paramArrayOfFloat[i1][i2][1];
//					}
//					this.fftRows.complexInverse(this.t, 0, paramBoolean);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						paramArrayOfFloat[i1][i2][0] = this.t[i];
//						paramArrayOfFloat[i1][i2][1] = this.t[(i + 1)];
//					}
//				}
//			}
//	}
//
//	private strictfp void cdft3db_sub(int paramInt, float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		int i3;
//		int i;
//		int i4;
//		int i5;
//		int j;
//		int k;
//		int l;
//		int i1;
//		int i2;
//		if (paramInt == -1) {
//			if (this.columns > 4) {
//				for (i3 = 0; i3 < this.rows; ++i3) {
//					i = i3 * this.rowStride;
//					for (i4 = 0; i4 < this.columns; i4 += 8) {
//						for (i5 = 0; i5 < this.slices; ++i5) {
//							j = i5 * this.sliceStride + i + i4;
//							k = 2 * i5;
//							l = 2 * this.slices + 2 * i5;
//							i1 = l + 2 * this.slices;
//							i2 = i1 + 2 * this.slices;
//							this.t[k] = paramArrayOfFloat[j];
//							this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//							this.t[l] = paramArrayOfFloat[(j + 2)];
//							this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//							this.t[i1] = paramArrayOfFloat[(j + 4)];
//							this.t[(i1 + 1)] = paramArrayOfFloat[(j + 5)];
//							this.t[i2] = paramArrayOfFloat[(j + 6)];
//							this.t[(i2 + 1)] = paramArrayOfFloat[(j + 7)];
//						}
//						this.fftSlices.complexForward(this.t, 0);
//						this.fftSlices.complexForward(this.t, 2 * this.slices);
//						this.fftSlices.complexForward(this.t, 4 * this.slices);
//						this.fftSlices.complexForward(this.t, 6 * this.slices);
//						for (i5 = 0; i5 < this.slices; ++i5) {
//							j = i5 * this.sliceStride + i + i4;
//							k = 2 * i5;
//							l = 2 * this.slices + 2 * i5;
//							i1 = l + 2 * this.slices;
//							i2 = i1 + 2 * this.slices;
//							paramArrayOfFloat[j] = this.t[k];
//							paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//							paramArrayOfFloat[(j + 2)] = this.t[l];
//							paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//							paramArrayOfFloat[(j + 4)] = this.t[i1];
//							paramArrayOfFloat[(j + 5)] = this.t[(i1 + 1)];
//							paramArrayOfFloat[(j + 6)] = this.t[i2];
//							paramArrayOfFloat[(j + 7)] = this.t[(i2 + 1)];
//						}
//					}
//				}
//			} else if (this.columns == 4) {
//				for (i3 = 0; i3 < this.rows; ++i3) {
//					i = i3 * this.rowStride;
//					for (i4 = 0; i4 < this.slices; ++i4) {
//						j = i4 * this.sliceStride + i;
//						k = 2 * i4;
//						l = 2 * this.slices + 2 * i4;
//						this.t[k] = paramArrayOfFloat[j];
//						this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//						this.t[l] = paramArrayOfFloat[(j + 2)];
//						this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//					}
//					this.fftSlices.complexForward(this.t, 0);
//					this.fftSlices.complexForward(this.t, 2 * this.slices);
//					for (i4 = 0; i4 < this.slices; ++i4) {
//						j = i4 * this.sliceStride + i;
//						k = 2 * i4;
//						l = 2 * this.slices + 2 * i4;
//						paramArrayOfFloat[j] = this.t[k];
//						paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//						paramArrayOfFloat[(j + 2)] = this.t[l];
//						paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//					}
//				}
//			} else {
//				if (this.columns != 2)
//					return;
//				for (i3 = 0; i3 < this.rows; ++i3) {
//					i = i3 * this.rowStride;
//					for (i4 = 0; i4 < this.slices; ++i4) {
//						j = i4 * this.sliceStride + i;
//						k = 2 * i4;
//						this.t[k] = paramArrayOfFloat[j];
//						this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//					}
//					this.fftSlices.complexForward(this.t, 0);
//					for (i4 = 0; i4 < this.slices; ++i4) {
//						j = i4 * this.sliceStride + i;
//						k = 2 * i4;
//						paramArrayOfFloat[j] = this.t[k];
//						paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//					}
//				}
//			}
//		} else if (this.columns > 4) {
//			for (i3 = 0; i3 < this.rows; ++i3) {
//				i = i3 * this.rowStride;
//				for (i4 = 0; i4 < this.columns; i4 += 8) {
//					for (i5 = 0; i5 < this.slices; ++i5) {
//						j = i5 * this.sliceStride + i + i4;
//						k = 2 * i5;
//						l = 2 * this.slices + 2 * i5;
//						i1 = l + 2 * this.slices;
//						i2 = i1 + 2 * this.slices;
//						this.t[k] = paramArrayOfFloat[j];
//						this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//						this.t[l] = paramArrayOfFloat[(j + 2)];
//						this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//						this.t[i1] = paramArrayOfFloat[(j + 4)];
//						this.t[(i1 + 1)] = paramArrayOfFloat[(j + 5)];
//						this.t[i2] = paramArrayOfFloat[(j + 6)];
//						this.t[(i2 + 1)] = paramArrayOfFloat[(j + 7)];
//					}
//					this.fftSlices.complexInverse(this.t, 0, paramBoolean);
//					this.fftSlices.complexInverse(this.t, 2 * this.slices,
//							paramBoolean);
//					this.fftSlices.complexInverse(this.t, 4 * this.slices,
//							paramBoolean);
//					this.fftSlices.complexInverse(this.t, 6 * this.slices,
//							paramBoolean);
//					for (i5 = 0; i5 < this.slices; ++i5) {
//						j = i5 * this.sliceStride + i + i4;
//						k = 2 * i5;
//						l = 2 * this.slices + 2 * i5;
//						i1 = l + 2 * this.slices;
//						i2 = i1 + 2 * this.slices;
//						paramArrayOfFloat[j] = this.t[k];
//						paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//						paramArrayOfFloat[(j + 2)] = this.t[l];
//						paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//						paramArrayOfFloat[(j + 4)] = this.t[i1];
//						paramArrayOfFloat[(j + 5)] = this.t[(i1 + 1)];
//						paramArrayOfFloat[(j + 6)] = this.t[i2];
//						paramArrayOfFloat[(j + 7)] = this.t[(i2 + 1)];
//					}
//				}
//			}
//		} else if (this.columns == 4) {
//			for (i3 = 0; i3 < this.rows; ++i3) {
//				i = i3 * this.rowStride;
//				for (i4 = 0; i4 < this.slices; ++i4) {
//					j = i4 * this.sliceStride + i;
//					k = 2 * i4;
//					l = 2 * this.slices + 2 * i4;
//					this.t[k] = paramArrayOfFloat[j];
//					this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//					this.t[l] = paramArrayOfFloat[(j + 2)];
//					this.t[(l + 1)] = paramArrayOfFloat[(j + 3)];
//				}
//				this.fftSlices.complexInverse(this.t, 0, paramBoolean);
//				this.fftSlices.complexInverse(this.t, 2 * this.slices,
//						paramBoolean);
//				for (i4 = 0; i4 < this.slices; ++i4) {
//					j = i4 * this.sliceStride + i;
//					k = 2 * i4;
//					l = 2 * this.slices + 2 * i4;
//					paramArrayOfFloat[j] = this.t[k];
//					paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//					paramArrayOfFloat[(j + 2)] = this.t[l];
//					paramArrayOfFloat[(j + 3)] = this.t[(l + 1)];
//				}
//			}
//		} else {
//			if (this.columns != 2)
//				return;
//			for (i3 = 0; i3 < this.rows; ++i3) {
//				i = i3 * this.rowStride;
//				for (i4 = 0; i4 < this.slices; ++i4) {
//					j = i4 * this.sliceStride + i;
//					k = 2 * i4;
//					this.t[k] = paramArrayOfFloat[j];
//					this.t[(k + 1)] = paramArrayOfFloat[(j + 1)];
//				}
//				this.fftSlices.complexInverse(this.t, 0, paramBoolean);
//				for (i4 = 0; i4 < this.slices; ++i4) {
//					j = i4 * this.sliceStride + i;
//					k = 2 * i4;
//					paramArrayOfFloat[j] = this.t[k];
//					paramArrayOfFloat[(j + 1)] = this.t[(k + 1)];
//				}
//			}
//		}
//	}
//
//	private strictfp void cdft3db_sub(int paramInt,
//			float[][][] paramArrayOfFloat, boolean paramBoolean) {
//		int i1;
//		int i2;
//		int i3;
//		int i;
//		int j;
//		int k;
//		int l;
//		if (paramInt == -1) {
//			if (this.columns > 4) {
//				for (i1 = 0; i1 < this.rows; ++i1)
//					for (i2 = 0; i2 < this.columns; i2 += 8) {
//						for (i3 = 0; i3 < this.slices; ++i3) {
//							i = 2 * i3;
//							j = 2 * this.slices + 2 * i3;
//							k = j + 2 * this.slices;
//							l = k + 2 * this.slices;
//							this.t[i] = paramArrayOfFloat[i3][i1][i2];
//							this.t[(i + 1)] = paramArrayOfFloat[i3][i1][(i2 + 1)];
//							this.t[j] = paramArrayOfFloat[i3][i1][(i2 + 2)];
//							this.t[(j + 1)] = paramArrayOfFloat[i3][i1][(i2 + 3)];
//							this.t[k] = paramArrayOfFloat[i3][i1][(i2 + 4)];
//							this.t[(k + 1)] = paramArrayOfFloat[i3][i1][(i2 + 5)];
//							this.t[l] = paramArrayOfFloat[i3][i1][(i2 + 6)];
//							this.t[(l + 1)] = paramArrayOfFloat[i3][i1][(i2 + 7)];
//						}
//						this.fftSlices.complexForward(this.t, 0);
//						this.fftSlices.complexForward(this.t, 2 * this.slices);
//						this.fftSlices.complexForward(this.t, 4 * this.slices);
//						this.fftSlices.complexForward(this.t, 6 * this.slices);
//						for (i3 = 0; i3 < this.slices; ++i3) {
//							i = 2 * i3;
//							j = 2 * this.slices + 2 * i3;
//							k = j + 2 * this.slices;
//							l = k + 2 * this.slices;
//							paramArrayOfFloat[i3][i1][i2] = this.t[i];
//							paramArrayOfFloat[i3][i1][(i2 + 1)] = this.t[(i + 1)];
//							paramArrayOfFloat[i3][i1][(i2 + 2)] = this.t[j];
//							paramArrayOfFloat[i3][i1][(i2 + 3)] = this.t[(j + 1)];
//							paramArrayOfFloat[i3][i1][(i2 + 4)] = this.t[k];
//							paramArrayOfFloat[i3][i1][(i2 + 5)] = this.t[(k + 1)];
//							paramArrayOfFloat[i3][i1][(i2 + 6)] = this.t[l];
//							paramArrayOfFloat[i3][i1][(i2 + 7)] = this.t[(l + 1)];
//						}
//					}
//			} else if (this.columns == 4) {
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					for (i2 = 0; i2 < this.slices; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.slices + 2 * i2;
//						this.t[i] = paramArrayOfFloat[i2][i1][0];
//						this.t[(i + 1)] = paramArrayOfFloat[i2][i1][1];
//						this.t[j] = paramArrayOfFloat[i2][i1][2];
//						this.t[(j + 1)] = paramArrayOfFloat[i2][i1][3];
//					}
//					this.fftSlices.complexForward(this.t, 0);
//					this.fftSlices.complexForward(this.t, 2 * this.slices);
//					for (i2 = 0; i2 < this.slices; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.slices + 2 * i2;
//						paramArrayOfFloat[i2][i1][0] = this.t[i];
//						paramArrayOfFloat[i2][i1][1] = this.t[(i + 1)];
//						paramArrayOfFloat[i2][i1][2] = this.t[j];
//						paramArrayOfFloat[i2][i1][3] = this.t[(j + 1)];
//					}
//				}
//			} else {
//				if (this.columns != 2)
//					return;
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					for (i2 = 0; i2 < this.slices; ++i2) {
//						i = 2 * i2;
//						this.t[i] = paramArrayOfFloat[i2][i1][0];
//						this.t[(i + 1)] = paramArrayOfFloat[i2][i1][1];
//					}
//					this.fftSlices.complexForward(this.t, 0);
//					for (i2 = 0; i2 < this.slices; ++i2) {
//						i = 2 * i2;
//						paramArrayOfFloat[i2][i1][0] = this.t[i];
//						paramArrayOfFloat[i2][i1][1] = this.t[(i + 1)];
//					}
//				}
//			}
//		} else if (this.columns > 4) {
//			for (i1 = 0; i1 < this.rows; ++i1)
//				for (i2 = 0; i2 < this.columns; i2 += 8) {
//					for (i3 = 0; i3 < this.slices; ++i3) {
//						i = 2 * i3;
//						j = 2 * this.slices + 2 * i3;
//						k = j + 2 * this.slices;
//						l = k + 2 * this.slices;
//						this.t[i] = paramArrayOfFloat[i3][i1][i2];
//						this.t[(i + 1)] = paramArrayOfFloat[i3][i1][(i2 + 1)];
//						this.t[j] = paramArrayOfFloat[i3][i1][(i2 + 2)];
//						this.t[(j + 1)] = paramArrayOfFloat[i3][i1][(i2 + 3)];
//						this.t[k] = paramArrayOfFloat[i3][i1][(i2 + 4)];
//						this.t[(k + 1)] = paramArrayOfFloat[i3][i1][(i2 + 5)];
//						this.t[l] = paramArrayOfFloat[i3][i1][(i2 + 6)];
//						this.t[(l + 1)] = paramArrayOfFloat[i3][i1][(i2 + 7)];
//					}
//					this.fftSlices.complexInverse(this.t, 0, paramBoolean);
//					this.fftSlices.complexInverse(this.t, 2 * this.slices,
//							paramBoolean);
//					this.fftSlices.complexInverse(this.t, 4 * this.slices,
//							paramBoolean);
//					this.fftSlices.complexInverse(this.t, 6 * this.slices,
//							paramBoolean);
//					for (i3 = 0; i3 < this.slices; ++i3) {
//						i = 2 * i3;
//						j = 2 * this.slices + 2 * i3;
//						k = j + 2 * this.slices;
//						l = k + 2 * this.slices;
//						paramArrayOfFloat[i3][i1][i2] = this.t[i];
//						paramArrayOfFloat[i3][i1][(i2 + 1)] = this.t[(i + 1)];
//						paramArrayOfFloat[i3][i1][(i2 + 2)] = this.t[j];
//						paramArrayOfFloat[i3][i1][(i2 + 3)] = this.t[(j + 1)];
//						paramArrayOfFloat[i3][i1][(i2 + 4)] = this.t[k];
//						paramArrayOfFloat[i3][i1][(i2 + 5)] = this.t[(k + 1)];
//						paramArrayOfFloat[i3][i1][(i2 + 6)] = this.t[l];
//						paramArrayOfFloat[i3][i1][(i2 + 7)] = this.t[(l + 1)];
//					}
//				}
//		} else if (this.columns == 4) {
//			for (i1 = 0; i1 < this.rows; ++i1) {
//				for (i2 = 0; i2 < this.slices; ++i2) {
//					i = 2 * i2;
//					j = 2 * this.slices + 2 * i2;
//					this.t[i] = paramArrayOfFloat[i2][i1][0];
//					this.t[(i + 1)] = paramArrayOfFloat[i2][i1][1];
//					this.t[j] = paramArrayOfFloat[i2][i1][2];
//					this.t[(j + 1)] = paramArrayOfFloat[i2][i1][3];
//				}
//				this.fftSlices.complexInverse(this.t, 0, paramBoolean);
//				this.fftSlices.complexInverse(this.t, 2 * this.slices,
//						paramBoolean);
//				for (i2 = 0; i2 < this.slices; ++i2) {
//					i = 2 * i2;
//					j = 2 * this.slices + 2 * i2;
//					paramArrayOfFloat[i2][i1][0] = this.t[i];
//					paramArrayOfFloat[i2][i1][1] = this.t[(i + 1)];
//					paramArrayOfFloat[i2][i1][2] = this.t[j];
//					paramArrayOfFloat[i2][i1][3] = this.t[(j + 1)];
//				}
//			}
//		} else {
//			if (this.columns != 2)
//				return;
//			for (i1 = 0; i1 < this.rows; ++i1) {
//				for (i2 = 0; i2 < this.slices; ++i2) {
//					i = 2 * i2;
//					this.t[i] = paramArrayOfFloat[i2][i1][0];
//					this.t[(i + 1)] = paramArrayOfFloat[i2][i1][1];
//				}
//				this.fftSlices.complexInverse(this.t, 0, paramBoolean);
//				for (i2 = 0; i2 < this.slices; ++i2) {
//					i = 2 * i2;
//					paramArrayOfFloat[i2][i1][0] = this.t[i];
//					paramArrayOfFloat[i2][i1][1] = this.t[(i + 1)];
//				}
//			}
//		}
//	}
//
//	private strictfp void xdft3da_subth1(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat, boolean paramBoolean) {
//		int k = Math.min(ConcurrencyUtils.getNumberOfThreads(), this.slices);
//		int i = 8 * this.rows;
//		if (this.columns == 4)
//			i >>= 1;
//		else if (this.columns < 4)
//			i >>= 2;
//		Future[] arrayOfFuture = new Future[k];
//		for (int j = 0; j < k; ++j) {
//			int l = j;
//			int i1 = i * j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt2,
//					l, k, paramInt1, paramArrayOfFloat, paramBoolean, i1) {
//				public strictfp void run() {
//					int i3;
//					int i;
//					int i4;
//					int i5;
//					int j;
//					int k;
//					int l;
//					int i1;
//					int i2;
//					if (this.val$isgn == -1) {
//						i3 = this.val$n0;
//						while (i3 < FloatFFT_3D.this.slices) {
//							i = i3 * FloatFFT_3D.this.sliceStride;
//							if (this.val$icr == 0)
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4)
//									FloatFFT_3D.this.fftColumns
//											.complexForward(
//													this.val$a,
//													i
//															+ i4
//															* FloatFFT_3D.this.rowStride);
//							else
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4)
//									FloatFFT_3D.this.fftColumns
//											.realInverse(
//													this.val$a,
//													i
//															+ i4
//															* FloatFFT_3D.this.rowStride,
//													this.val$scale);
//							if (FloatFFT_3D.this.columns > 4) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.columns; i4 += 8) {
//									for (i5 = 0; i5 < FloatFFT_3D.this.rows; ++i5) {
//										j = i + i5 * FloatFFT_3D.this.rowStride
//												+ i4;
//										k = this.val$startt + 2 * i5;
//										l = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i5;
//										i1 = l + 2 * FloatFFT_3D.this.rows;
//										i2 = i1 + 2 * FloatFFT_3D.this.rows;
//										FloatFFT_3D.this.t[k] = this.val$a[j];
//										FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//										FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//										FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//										FloatFFT_3D.this.t[i1] = this.val$a[(j + 4)];
//										FloatFFT_3D.this.t[(i1 + 1)] = this.val$a[(j + 5)];
//										FloatFFT_3D.this.t[i2] = this.val$a[(j + 6)];
//										FloatFFT_3D.this.t[(i2 + 1)] = this.val$a[(j + 7)];
//									}
//									FloatFFT_3D.this.fftRows
//											.complexForward(FloatFFT_3D.this.t,
//													this.val$startt);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 2
//															* FloatFFT_3D.this.rows);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 4
//															* FloatFFT_3D.this.rows);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 6
//															* FloatFFT_3D.this.rows);
//									for (i5 = 0; i5 < FloatFFT_3D.this.rows; ++i5) {
//										j = i + i5 * FloatFFT_3D.this.rowStride
//												+ i4;
//										k = this.val$startt + 2 * i5;
//										l = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i5;
//										i1 = l + 2 * FloatFFT_3D.this.rows;
//										i2 = i1 + 2 * FloatFFT_3D.this.rows;
//										this.val$a[j] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[k];
//										this.val$a[(j + 1)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(k + 1)];
//										this.val$a[(j + 2)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[l];
//										this.val$a[(j + 3)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(l + 1)];
//										this.val$a[(j + 4)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i1];
//										this.val$a[(j + 5)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i1 + 1)];
//										this.val$a[(j + 6)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i2];
//										this.val$a[(j + 7)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i2 + 1)];
//									}
//								}
//							} else if (FloatFFT_3D.this.columns == 4) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i4;
//									FloatFFT_3D.this.t[k] = this.val$a[j];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//									FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//									FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//								}
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.rows);
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i4;
//									this.val$a[j] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[(j + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//									this.val$a[(j + 2)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[l];
//									this.val$a[(j + 3)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(l + 1)];
//								}
//							} else if (FloatFFT_3D.this.columns == 2) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									FloatFFT_3D.this.t[k] = this.val$a[j];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//								}
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									this.val$a[j] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[(j + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//								}
//							}
//							i3 += this.val$nthreads;
//						}
//					} else {
//						i3 = this.val$n0;
//						while (i3 < FloatFFT_3D.this.slices) {
//							i = i3 * FloatFFT_3D.this.sliceStride;
//							if (this.val$icr == 0)
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4)
//									FloatFFT_3D.this.fftColumns
//											.complexInverse(
//													this.val$a,
//													i
//															+ i4
//															* FloatFFT_3D.this.rowStride,
//													this.val$scale);
//							if (FloatFFT_3D.this.columns > 4) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.columns; i4 += 8) {
//									for (i5 = 0; i5 < FloatFFT_3D.this.rows; ++i5) {
//										j = i + i5 * FloatFFT_3D.this.rowStride
//												+ i4;
//										k = this.val$startt + 2 * i5;
//										l = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i5;
//										i1 = l + 2 * FloatFFT_3D.this.rows;
//										i2 = i1 + 2 * FloatFFT_3D.this.rows;
//										FloatFFT_3D.this.t[k] = this.val$a[j];
//										FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//										FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//										FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//										FloatFFT_3D.this.t[i1] = this.val$a[(j + 4)];
//										FloatFFT_3D.this.t[(i1 + 1)] = this.val$a[(j + 5)];
//										FloatFFT_3D.this.t[i2] = this.val$a[(j + 6)];
//										FloatFFT_3D.this.t[(i2 + 1)] = this.val$a[(j + 7)];
//									}
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 2
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 4
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 6
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									for (i5 = 0; i5 < FloatFFT_3D.this.rows; ++i5) {
//										j = i + i5 * FloatFFT_3D.this.rowStride
//												+ i4;
//										k = this.val$startt + 2 * i5;
//										l = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i5;
//										i1 = l + 2 * FloatFFT_3D.this.rows;
//										i2 = i1 + 2 * FloatFFT_3D.this.rows;
//										this.val$a[j] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[k];
//										this.val$a[(j + 1)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(k + 1)];
//										this.val$a[(j + 2)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[l];
//										this.val$a[(j + 3)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(l + 1)];
//										this.val$a[(j + 4)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i1];
//										this.val$a[(j + 5)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i1 + 1)];
//										this.val$a[(j + 6)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i2];
//										this.val$a[(j + 7)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i2 + 1)];
//									}
//								}
//							} else if (FloatFFT_3D.this.columns == 4) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i4;
//									FloatFFT_3D.this.t[k] = this.val$a[j];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//									FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//									FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//								}
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.rows,
//										this.val$scale);
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i4;
//									this.val$a[j] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[(j + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//									this.val$a[(j + 2)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[l];
//									this.val$a[(j + 3)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(l + 1)];
//								}
//							} else if (FloatFFT_3D.this.columns == 2) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									FloatFFT_3D.this.t[k] = this.val$a[j];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//								}
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt,
//										this.val$scale);
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									this.val$a[j] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[(j + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//								}
//							}
//							if (this.val$icr != 0)
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4)
//									FloatFFT_3D.this.fftColumns
//											.realForward(
//													this.val$a,
//													i
//															+ i4
//															* FloatFFT_3D.this.rowStride);
//							i3 += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void xdft3da_subth2(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat, boolean paramBoolean) {
//		int k = Math.min(ConcurrencyUtils.getNumberOfThreads(), this.slices);
//		int i = 8 * this.rows;
//		if (this.columns == 4)
//			i >>= 1;
//		else if (this.columns < 4)
//			i >>= 2;
//		Future[] arrayOfFuture = new Future[k];
//		for (int j = 0; j < k; ++j) {
//			int l = j;
//			int i1 = i * j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt2,
//					l, k, paramInt1, paramArrayOfFloat, i1, paramBoolean) {
//				public strictfp void run() {
//					int i3;
//					int i;
//					int i4;
//					int i5;
//					int j;
//					int k;
//					int l;
//					int i1;
//					int i2;
//					if (this.val$isgn == -1) {
//						i3 = this.val$n0;
//						while (i3 < FloatFFT_3D.this.slices) {
//							i = i3 * FloatFFT_3D.this.sliceStride;
//							if (this.val$icr == 0)
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4)
//									FloatFFT_3D.this.fftColumns
//											.complexForward(
//													this.val$a,
//													i
//															+ i4
//															* FloatFFT_3D.this.rowStride);
//							else
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4)
//									FloatFFT_3D.this.fftColumns
//											.realForward(
//													this.val$a,
//													i
//															+ i4
//															* FloatFFT_3D.this.rowStride);
//							if (FloatFFT_3D.this.columns > 4) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.columns; i4 += 8) {
//									for (i5 = 0; i5 < FloatFFT_3D.this.rows; ++i5) {
//										j = i + i5 * FloatFFT_3D.this.rowStride
//												+ i4;
//										k = this.val$startt + 2 * i5;
//										l = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i5;
//										i1 = l + 2 * FloatFFT_3D.this.rows;
//										i2 = i1 + 2 * FloatFFT_3D.this.rows;
//										FloatFFT_3D.this.t[k] = this.val$a[j];
//										FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//										FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//										FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//										FloatFFT_3D.this.t[i1] = this.val$a[(j + 4)];
//										FloatFFT_3D.this.t[(i1 + 1)] = this.val$a[(j + 5)];
//										FloatFFT_3D.this.t[i2] = this.val$a[(j + 6)];
//										FloatFFT_3D.this.t[(i2 + 1)] = this.val$a[(j + 7)];
//									}
//									FloatFFT_3D.this.fftRows
//											.complexForward(FloatFFT_3D.this.t,
//													this.val$startt);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 2
//															* FloatFFT_3D.this.rows);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 4
//															* FloatFFT_3D.this.rows);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 6
//															* FloatFFT_3D.this.rows);
//									for (i5 = 0; i5 < FloatFFT_3D.this.rows; ++i5) {
//										j = i + i5 * FloatFFT_3D.this.rowStride
//												+ i4;
//										k = this.val$startt + 2 * i5;
//										l = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i5;
//										i1 = l + 2 * FloatFFT_3D.this.rows;
//										i2 = i1 + 2 * FloatFFT_3D.this.rows;
//										this.val$a[j] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[k];
//										this.val$a[(j + 1)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(k + 1)];
//										this.val$a[(j + 2)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[l];
//										this.val$a[(j + 3)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(l + 1)];
//										this.val$a[(j + 4)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i1];
//										this.val$a[(j + 5)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i1 + 1)];
//										this.val$a[(j + 6)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i2];
//										this.val$a[(j + 7)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i2 + 1)];
//									}
//								}
//							} else if (FloatFFT_3D.this.columns == 4) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i4;
//									FloatFFT_3D.this.t[k] = this.val$a[j];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//									FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//									FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//								}
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.rows);
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i4;
//									this.val$a[j] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[(j + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//									this.val$a[(j + 2)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[l];
//									this.val$a[(j + 3)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(l + 1)];
//								}
//							} else if (FloatFFT_3D.this.columns == 2) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									FloatFFT_3D.this.t[k] = this.val$a[j];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//								}
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									this.val$a[j] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[(j + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//								}
//							}
//							i3 += this.val$nthreads;
//						}
//					} else {
//						i3 = this.val$n0;
//						while (i3 < FloatFFT_3D.this.slices) {
//							i = i3 * FloatFFT_3D.this.sliceStride;
//							if (this.val$icr == 0)
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4)
//									FloatFFT_3D.this.fftColumns
//											.complexInverse(
//													this.val$a,
//													i
//															+ i4
//															* FloatFFT_3D.this.rowStride,
//													this.val$scale);
//							else
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4)
//									FloatFFT_3D.this.fftColumns
//											.realInverse2(
//													this.val$a,
//													i
//															+ i4
//															* FloatFFT_3D.this.rowStride,
//													this.val$scale);
//							if (FloatFFT_3D.this.columns > 4) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.columns; i4 += 8) {
//									for (i5 = 0; i5 < FloatFFT_3D.this.rows; ++i5) {
//										j = i + i5 * FloatFFT_3D.this.rowStride
//												+ i4;
//										k = this.val$startt + 2 * i5;
//										l = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i5;
//										i1 = l + 2 * FloatFFT_3D.this.rows;
//										i2 = i1 + 2 * FloatFFT_3D.this.rows;
//										FloatFFT_3D.this.t[k] = this.val$a[j];
//										FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//										FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//										FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//										FloatFFT_3D.this.t[i1] = this.val$a[(j + 4)];
//										FloatFFT_3D.this.t[(i1 + 1)] = this.val$a[(j + 5)];
//										FloatFFT_3D.this.t[i2] = this.val$a[(j + 6)];
//										FloatFFT_3D.this.t[(i2 + 1)] = this.val$a[(j + 7)];
//									}
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 2
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 4
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 6
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									for (i5 = 0; i5 < FloatFFT_3D.this.rows; ++i5) {
//										j = i + i5 * FloatFFT_3D.this.rowStride
//												+ i4;
//										k = this.val$startt + 2 * i5;
//										l = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i5;
//										i1 = l + 2 * FloatFFT_3D.this.rows;
//										i2 = i1 + 2 * FloatFFT_3D.this.rows;
//										this.val$a[j] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[k];
//										this.val$a[(j + 1)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(k + 1)];
//										this.val$a[(j + 2)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[l];
//										this.val$a[(j + 3)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(l + 1)];
//										this.val$a[(j + 4)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i1];
//										this.val$a[(j + 5)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i1 + 1)];
//										this.val$a[(j + 6)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i2];
//										this.val$a[(j + 7)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i2 + 1)];
//									}
//								}
//							} else if (FloatFFT_3D.this.columns == 4) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i4;
//									FloatFFT_3D.this.t[k] = this.val$a[j];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//									FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//									FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//								}
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.rows,
//										this.val$scale);
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i4;
//									this.val$a[j] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[(j + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//									this.val$a[(j + 2)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[l];
//									this.val$a[(j + 3)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(l + 1)];
//								}
//							} else if (FloatFFT_3D.this.columns == 2) {
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									FloatFFT_3D.this.t[k] = this.val$a[j];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//								}
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt,
//										this.val$scale);
//								for (i4 = 0; i4 < FloatFFT_3D.this.rows; ++i4) {
//									j = i + i4 * FloatFFT_3D.this.rowStride;
//									k = this.val$startt + 2 * i4;
//									this.val$a[j] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[(j + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//								}
//							}
//							i3 += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void xdft3da_subth1(int paramInt1, int paramInt2,
//			float[][][] paramArrayOfFloat, boolean paramBoolean) {
//		int k = Math.min(ConcurrencyUtils.getNumberOfThreads(), this.slices);
//		int i = 8 * this.rows;
//		if (this.columns == 4)
//			i >>= 1;
//		else if (this.columns < 4)
//			i >>= 2;
//		Future[] arrayOfFuture = new Future[k];
//		for (int j = 0; j < k; ++j) {
//			int l = j;
//			int i1 = i * j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt2,
//					l, k, paramInt1, paramArrayOfFloat, paramBoolean, i1) {
//				public strictfp void run() {
//					int i1;
//					int i2;
//					int i3;
//					int i;
//					int j;
//					int k;
//					int l;
//					if (this.val$isgn == -1) {
//						i1 = this.val$n0;
//						while (i1 < FloatFFT_3D.this.slices) {
//							if (this.val$icr == 0)
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2)
//									FloatFFT_3D.this.fftColumns
//											.complexForward(this.val$a[i1][i2]);
//							else
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2)
//									FloatFFT_3D.this.fftColumns.realInverse(
//											this.val$a[i1][i2], 0,
//											this.val$scale);
//							if (FloatFFT_3D.this.columns > 4) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.columns; i2 += 8) {
//									for (i3 = 0; i3 < FloatFFT_3D.this.rows; ++i3) {
//										i = this.val$startt + 2 * i3;
//										j = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i3;
//										k = j + 2 * FloatFFT_3D.this.rows;
//										l = k + 2 * FloatFFT_3D.this.rows;
//										FloatFFT_3D.this.t[i] = this.val$a[i1][i3][i2];
//										FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i3][(i2 + 1)];
//										FloatFFT_3D.this.t[j] = this.val$a[i1][i3][(i2 + 2)];
//										FloatFFT_3D.this.t[(j + 1)] = this.val$a[i1][i3][(i2 + 3)];
//										FloatFFT_3D.this.t[k] = this.val$a[i1][i3][(i2 + 4)];
//										FloatFFT_3D.this.t[(k + 1)] = this.val$a[i1][i3][(i2 + 5)];
//										FloatFFT_3D.this.t[l] = this.val$a[i1][i3][(i2 + 6)];
//										FloatFFT_3D.this.t[(l + 1)] = this.val$a[i1][i3][(i2 + 7)];
//									}
//									FloatFFT_3D.this.fftRows
//											.complexForward(FloatFFT_3D.this.t,
//													this.val$startt);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 2
//															* FloatFFT_3D.this.rows);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 4
//															* FloatFFT_3D.this.rows);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 6
//															* FloatFFT_3D.this.rows);
//									for (i3 = 0; i3 < FloatFFT_3D.this.rows; ++i3) {
//										i = this.val$startt + 2 * i3;
//										j = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i3;
//										k = j + 2 * FloatFFT_3D.this.rows;
//										l = k + 2 * FloatFFT_3D.this.rows;
//										this.val$a[i1][i3][i2] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i];
//										this.val$a[i1][i3][(i2 + 1)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i + 1)];
//										this.val$a[i1][i3][(i2 + 2)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[j];
//										this.val$a[i1][i3][(i2 + 3)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(j + 1)];
//										this.val$a[i1][i3][(i2 + 4)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[k];
//										this.val$a[i1][i3][(i2 + 5)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(k + 1)];
//										this.val$a[i1][i3][(i2 + 6)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[l];
//										this.val$a[i1][i3][(i2 + 7)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(l + 1)];
//									}
//								}
//							} else if (FloatFFT_3D.this.columns == 4) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i2;
//									FloatFFT_3D.this.t[i] = this.val$a[i1][i2][0];
//									FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i2][1];
//									FloatFFT_3D.this.t[j] = this.val$a[i1][i2][2];
//									FloatFFT_3D.this.t[(j + 1)] = this.val$a[i1][i2][3];
//								}
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.rows);
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i2;
//									this.val$a[i1][i2][0] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i];
//									this.val$a[i1][i2][1] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i + 1)];
//									this.val$a[i1][i2][2] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[j];
//									this.val$a[i1][i2][3] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(j + 1)];
//								}
//							} else if (FloatFFT_3D.this.columns == 2) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									FloatFFT_3D.this.t[i] = this.val$a[i1][i2][0];
//									FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i2][1];
//								}
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									this.val$a[i1][i2][0] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i];
//									this.val$a[i1][i2][1] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i + 1)];
//								}
//							}
//							i1 += this.val$nthreads;
//						}
//					} else {
//						i1 = this.val$n0;
//						while (i1 < FloatFFT_3D.this.slices) {
//							if (this.val$icr == 0)
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2)
//									FloatFFT_3D.this.fftColumns.complexInverse(
//											this.val$a[i1][i2], this.val$scale);
//							if (FloatFFT_3D.this.columns > 4) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.columns; i2 += 8) {
//									for (i3 = 0; i3 < FloatFFT_3D.this.rows; ++i3) {
//										i = this.val$startt + 2 * i3;
//										j = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i3;
//										k = j + 2 * FloatFFT_3D.this.rows;
//										l = k + 2 * FloatFFT_3D.this.rows;
//										FloatFFT_3D.this.t[i] = this.val$a[i1][i3][i2];
//										FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i3][(i2 + 1)];
//										FloatFFT_3D.this.t[j] = this.val$a[i1][i3][(i2 + 2)];
//										FloatFFT_3D.this.t[(j + 1)] = this.val$a[i1][i3][(i2 + 3)];
//										FloatFFT_3D.this.t[k] = this.val$a[i1][i3][(i2 + 4)];
//										FloatFFT_3D.this.t[(k + 1)] = this.val$a[i1][i3][(i2 + 5)];
//										FloatFFT_3D.this.t[l] = this.val$a[i1][i3][(i2 + 6)];
//										FloatFFT_3D.this.t[(l + 1)] = this.val$a[i1][i3][(i2 + 7)];
//									}
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 2
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 4
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 6
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									for (i3 = 0; i3 < FloatFFT_3D.this.rows; ++i3) {
//										i = this.val$startt + 2 * i3;
//										j = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i3;
//										k = j + 2 * FloatFFT_3D.this.rows;
//										l = k + 2 * FloatFFT_3D.this.rows;
//										this.val$a[i1][i3][i2] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i];
//										this.val$a[i1][i3][(i2 + 1)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i + 1)];
//										this.val$a[i1][i3][(i2 + 2)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[j];
//										this.val$a[i1][i3][(i2 + 3)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(j + 1)];
//										this.val$a[i1][i3][(i2 + 4)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[k];
//										this.val$a[i1][i3][(i2 + 5)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(k + 1)];
//										this.val$a[i1][i3][(i2 + 6)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[l];
//										this.val$a[i1][i3][(i2 + 7)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(l + 1)];
//									}
//								}
//							} else if (FloatFFT_3D.this.columns == 4) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i2;
//									FloatFFT_3D.this.t[i] = this.val$a[i1][i2][0];
//									FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i2][1];
//									FloatFFT_3D.this.t[j] = this.val$a[i1][i2][2];
//									FloatFFT_3D.this.t[(j + 1)] = this.val$a[i1][i2][3];
//								}
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.rows,
//										this.val$scale);
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i2;
//									this.val$a[i1][i2][0] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i];
//									this.val$a[i1][i2][1] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i + 1)];
//									this.val$a[i1][i2][2] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[j];
//									this.val$a[i1][i2][3] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(j + 1)];
//								}
//							} else if (FloatFFT_3D.this.columns == 2) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									FloatFFT_3D.this.t[i] = this.val$a[i1][i2][0];
//									FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i2][1];
//								}
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt,
//										this.val$scale);
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									this.val$a[i1][i2][0] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i];
//									this.val$a[i1][i2][1] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i + 1)];
//								}
//							}
//							if (this.val$icr != 0)
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2)
//									FloatFFT_3D.this.fftColumns
//											.realForward(this.val$a[i1][i2]);
//							i1 += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void xdft3da_subth2(int paramInt1, int paramInt2,
//			float[][][] paramArrayOfFloat, boolean paramBoolean) {
//		int k = Math.min(ConcurrencyUtils.getNumberOfThreads(), this.slices);
//		int i = 8 * this.rows;
//		if (this.columns == 4)
//			i >>= 1;
//		else if (this.columns < 4)
//			i >>= 2;
//		Future[] arrayOfFuture = new Future[k];
//		for (int j = 0; j < k; ++j) {
//			int l = j;
//			int i1 = i * j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt2,
//					l, k, paramInt1, paramArrayOfFloat, i1, paramBoolean) {
//				public strictfp void run() {
//					int i1;
//					int i2;
//					int i3;
//					int i;
//					int j;
//					int k;
//					int l;
//					if (this.val$isgn == -1) {
//						i1 = this.val$n0;
//						while (i1 < FloatFFT_3D.this.slices) {
//							if (this.val$icr == 0)
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2)
//									FloatFFT_3D.this.fftColumns
//											.complexForward(this.val$a[i1][i2]);
//							else
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2)
//									FloatFFT_3D.this.fftColumns
//											.realForward(this.val$a[i1][i2]);
//							if (FloatFFT_3D.this.columns > 4) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.columns; i2 += 8) {
//									for (i3 = 0; i3 < FloatFFT_3D.this.rows; ++i3) {
//										i = this.val$startt + 2 * i3;
//										j = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i3;
//										k = j + 2 * FloatFFT_3D.this.rows;
//										l = k + 2 * FloatFFT_3D.this.rows;
//										FloatFFT_3D.this.t[i] = this.val$a[i1][i3][i2];
//										FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i3][(i2 + 1)];
//										FloatFFT_3D.this.t[j] = this.val$a[i1][i3][(i2 + 2)];
//										FloatFFT_3D.this.t[(j + 1)] = this.val$a[i1][i3][(i2 + 3)];
//										FloatFFT_3D.this.t[k] = this.val$a[i1][i3][(i2 + 4)];
//										FloatFFT_3D.this.t[(k + 1)] = this.val$a[i1][i3][(i2 + 5)];
//										FloatFFT_3D.this.t[l] = this.val$a[i1][i3][(i2 + 6)];
//										FloatFFT_3D.this.t[(l + 1)] = this.val$a[i1][i3][(i2 + 7)];
//									}
//									FloatFFT_3D.this.fftRows
//											.complexForward(FloatFFT_3D.this.t,
//													this.val$startt);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 2
//															* FloatFFT_3D.this.rows);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 4
//															* FloatFFT_3D.this.rows);
//									FloatFFT_3D.this.fftRows
//											.complexForward(
//													FloatFFT_3D.this.t,
//													this.val$startt
//															+ 6
//															* FloatFFT_3D.this.rows);
//									for (i3 = 0; i3 < FloatFFT_3D.this.rows; ++i3) {
//										i = this.val$startt + 2 * i3;
//										j = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i3;
//										k = j + 2 * FloatFFT_3D.this.rows;
//										l = k + 2 * FloatFFT_3D.this.rows;
//										this.val$a[i1][i3][i2] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i];
//										this.val$a[i1][i3][(i2 + 1)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i + 1)];
//										this.val$a[i1][i3][(i2 + 2)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[j];
//										this.val$a[i1][i3][(i2 + 3)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(j + 1)];
//										this.val$a[i1][i3][(i2 + 4)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[k];
//										this.val$a[i1][i3][(i2 + 5)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(k + 1)];
//										this.val$a[i1][i3][(i2 + 6)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[l];
//										this.val$a[i1][i3][(i2 + 7)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(l + 1)];
//									}
//								}
//							} else if (FloatFFT_3D.this.columns == 4) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i2;
//									FloatFFT_3D.this.t[i] = this.val$a[i1][i2][0];
//									FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i2][1];
//									FloatFFT_3D.this.t[j] = this.val$a[i1][i2][2];
//									FloatFFT_3D.this.t[(j + 1)] = this.val$a[i1][i2][3];
//								}
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.rows);
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i2;
//									this.val$a[i1][i2][0] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i];
//									this.val$a[i1][i2][1] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i + 1)];
//									this.val$a[i1][i2][2] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[j];
//									this.val$a[i1][i2][3] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(j + 1)];
//								}
//							} else if (FloatFFT_3D.this.columns == 2) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									FloatFFT_3D.this.t[i] = this.val$a[i1][i2][0];
//									FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i2][1];
//								}
//								FloatFFT_3D.this.fftRows.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									this.val$a[i1][i2][0] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i];
//									this.val$a[i1][i2][1] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i + 1)];
//								}
//							}
//							i1 += this.val$nthreads;
//						}
//					} else {
//						i1 = this.val$n0;
//						while (i1 < FloatFFT_3D.this.slices) {
//							if (this.val$icr == 0)
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2)
//									FloatFFT_3D.this.fftColumns.complexInverse(
//											this.val$a[i1][i2], this.val$scale);
//							else
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2)
//									FloatFFT_3D.this.fftColumns.realInverse2(
//											this.val$a[i1][i2], 0,
//											this.val$scale);
//							if (FloatFFT_3D.this.columns > 4) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.columns; i2 += 8) {
//									for (i3 = 0; i3 < FloatFFT_3D.this.rows; ++i3) {
//										i = this.val$startt + 2 * i3;
//										j = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i3;
//										k = j + 2 * FloatFFT_3D.this.rows;
//										l = k + 2 * FloatFFT_3D.this.rows;
//										FloatFFT_3D.this.t[i] = this.val$a[i1][i3][i2];
//										FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i3][(i2 + 1)];
//										FloatFFT_3D.this.t[j] = this.val$a[i1][i3][(i2 + 2)];
//										FloatFFT_3D.this.t[(j + 1)] = this.val$a[i1][i3][(i2 + 3)];
//										FloatFFT_3D.this.t[k] = this.val$a[i1][i3][(i2 + 4)];
//										FloatFFT_3D.this.t[(k + 1)] = this.val$a[i1][i3][(i2 + 5)];
//										FloatFFT_3D.this.t[l] = this.val$a[i1][i3][(i2 + 6)];
//										FloatFFT_3D.this.t[(l + 1)] = this.val$a[i1][i3][(i2 + 7)];
//									}
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 2
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 4
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									FloatFFT_3D.this.fftRows.complexInverse(
//											FloatFFT_3D.this.t,
//											this.val$startt + 6
//													* FloatFFT_3D.this.rows,
//											this.val$scale);
//									for (i3 = 0; i3 < FloatFFT_3D.this.rows; ++i3) {
//										i = this.val$startt + 2 * i3;
//										j = this.val$startt + 2
//												* FloatFFT_3D.this.rows + 2
//												* i3;
//										k = j + 2 * FloatFFT_3D.this.rows;
//										l = k + 2 * FloatFFT_3D.this.rows;
//										this.val$a[i1][i3][i2] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i];
//										this.val$a[i1][i3][(i2 + 1)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i + 1)];
//										this.val$a[i1][i3][(i2 + 2)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[j];
//										this.val$a[i1][i3][(i2 + 3)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(j + 1)];
//										this.val$a[i1][i3][(i2 + 4)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[k];
//										this.val$a[i1][i3][(i2 + 5)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(k + 1)];
//										this.val$a[i1][i3][(i2 + 6)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[l];
//										this.val$a[i1][i3][(i2 + 7)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(l + 1)];
//									}
//								}
//							} else if (FloatFFT_3D.this.columns == 4) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i2;
//									FloatFFT_3D.this.t[i] = this.val$a[i1][i2][0];
//									FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i2][1];
//									FloatFFT_3D.this.t[j] = this.val$a[i1][i2][2];
//									FloatFFT_3D.this.t[(j + 1)] = this.val$a[i1][i2][3];
//								}
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.rows,
//										this.val$scale);
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.rows + 2 * i2;
//									this.val$a[i1][i2][0] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i];
//									this.val$a[i1][i2][1] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i + 1)];
//									this.val$a[i1][i2][2] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[j];
//									this.val$a[i1][i2][3] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(j + 1)];
//								}
//							} else if (FloatFFT_3D.this.columns == 2) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									FloatFFT_3D.this.t[i] = this.val$a[i1][i2][0];
//									FloatFFT_3D.this.t[(i + 1)] = this.val$a[i1][i2][1];
//								}
//								FloatFFT_3D.this.fftRows.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt,
//										this.val$scale);
//								for (i2 = 0; i2 < FloatFFT_3D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									this.val$a[i1][i2][0] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i];
//									this.val$a[i1][i2][1] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i + 1)];
//								}
//							}
//							i1 += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void cdft3db_subth(int paramInt,
//			float[] paramArrayOfFloat, boolean paramBoolean) {
//		int k = Math.min(ConcurrencyUtils.getNumberOfThreads(), this.rows);
//		int i = 8 * this.slices;
//		if (this.columns == 4)
//			i >>= 1;
//		else if (this.columns < 4)
//			i >>= 2;
//		Future[] arrayOfFuture = new Future[k];
//		for (int j = 0; j < k; ++j) {
//			int l = j;
//			int i1 = i * j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt,
//					l, k, i1, paramArrayOfFloat, paramBoolean) {
//				public strictfp void run() {
//					int i3;
//					int i;
//					int i4;
//					int i5;
//					int j;
//					int k;
//					int l;
//					int i1;
//					int i2;
//					if (this.val$isgn == -1) {
//						if (FloatFFT_3D.this.columns > 4) {
//							i3 = this.val$n0;
//							while (i3 < FloatFFT_3D.this.rows) {
//								i = i3 * FloatFFT_3D.this.rowStride;
//								for (i4 = 0; i4 < FloatFFT_3D.this.columns; i4 += 8) {
//									for (i5 = 0; i5 < FloatFFT_3D.this.slices; ++i5) {
//										j = i5 * FloatFFT_3D.this.sliceStride
//												+ i + i4;
//										k = this.val$startt + 2 * i5;
//										l = this.val$startt + 2
//												* FloatFFT_3D.this.slices + 2
//												* i5;
//										i1 = l + 2 * FloatFFT_3D.this.slices;
//										i2 = i1 + 2 * FloatFFT_3D.this.slices;
//										FloatFFT_3D.this.t[k] = this.val$a[j];
//										FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//										FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//										FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//										FloatFFT_3D.this.t[i1] = this.val$a[(j + 4)];
//										FloatFFT_3D.this.t[(i1 + 1)] = this.val$a[(j + 5)];
//										FloatFFT_3D.this.t[i2] = this.val$a[(j + 6)];
//										FloatFFT_3D.this.t[(i2 + 1)] = this.val$a[(j + 7)];
//									}
//									FloatFFT_3D.this.fftSlices
//											.complexForward(FloatFFT_3D.this.t,
//													this.val$startt);
//									FloatFFT_3D.this.fftSlices.complexForward(
//											FloatFFT_3D.this.t, this.val$startt
//													+ 2
//													* FloatFFT_3D.this.slices);
//									FloatFFT_3D.this.fftSlices.complexForward(
//											FloatFFT_3D.this.t, this.val$startt
//													+ 4
//													* FloatFFT_3D.this.slices);
//									FloatFFT_3D.this.fftSlices.complexForward(
//											FloatFFT_3D.this.t, this.val$startt
//													+ 6
//													* FloatFFT_3D.this.slices);
//									for (i5 = 0; i5 < FloatFFT_3D.this.slices; ++i5) {
//										j = i5 * FloatFFT_3D.this.sliceStride
//												+ i + i4;
//										k = this.val$startt + 2 * i5;
//										l = this.val$startt + 2
//												* FloatFFT_3D.this.slices + 2
//												* i5;
//										i1 = l + 2 * FloatFFT_3D.this.slices;
//										i2 = i1 + 2 * FloatFFT_3D.this.slices;
//										this.val$a[j] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[k];
//										this.val$a[(j + 1)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(k + 1)];
//										this.val$a[(j + 2)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[l];
//										this.val$a[(j + 3)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(l + 1)];
//										this.val$a[(j + 4)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i1];
//										this.val$a[(j + 5)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i1 + 1)];
//										this.val$a[(j + 6)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i2];
//										this.val$a[(j + 7)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i2 + 1)];
//									}
//								}
//								i3 += this.val$nthreads;
//							}
//						} else if (FloatFFT_3D.this.columns == 4) {
//							i3 = this.val$n0;
//							while (i3 < FloatFFT_3D.this.rows) {
//								i = i3 * FloatFFT_3D.this.rowStride;
//								for (i4 = 0; i4 < FloatFFT_3D.this.slices; ++i4) {
//									j = i4 * FloatFFT_3D.this.sliceStride + i;
//									k = this.val$startt + 2 * i4;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.slices + 2 * i4;
//									FloatFFT_3D.this.t[k] = this.val$a[j];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//									FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//									FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//								}
//								FloatFFT_3D.this.fftSlices.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								FloatFFT_3D.this.fftSlices.complexForward(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.slices);
//								for (i4 = 0; i4 < FloatFFT_3D.this.slices; ++i4) {
//									j = i4 * FloatFFT_3D.this.sliceStride + i;
//									k = this.val$startt + 2 * i4;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.slices + 2 * i4;
//									this.val$a[j] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[(j + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//									this.val$a[(j + 2)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[l];
//									this.val$a[(j + 3)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(l + 1)];
//								}
//								i3 += this.val$nthreads;
//							}
//						} else {
//							if (FloatFFT_3D.this.columns != 2)
//								return;
//							i3 = this.val$n0;
//							while (i3 < FloatFFT_3D.this.rows) {
//								i = i3 * FloatFFT_3D.this.rowStride;
//								for (i4 = 0; i4 < FloatFFT_3D.this.slices; ++i4) {
//									j = i4 * FloatFFT_3D.this.sliceStride + i;
//									k = this.val$startt + 2 * i4;
//									FloatFFT_3D.this.t[k] = this.val$a[j];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//								}
//								FloatFFT_3D.this.fftSlices.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								for (i4 = 0; i4 < FloatFFT_3D.this.slices; ++i4) {
//									j = i4 * FloatFFT_3D.this.sliceStride + i;
//									k = this.val$startt + 2 * i4;
//									this.val$a[j] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[(j + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//								}
//								i3 += this.val$nthreads;
//							}
//						}
//					} else if (FloatFFT_3D.this.columns > 4) {
//						i3 = this.val$n0;
//						while (i3 < FloatFFT_3D.this.rows) {
//							i = i3 * FloatFFT_3D.this.rowStride;
//							for (i4 = 0; i4 < FloatFFT_3D.this.columns; i4 += 8) {
//								for (i5 = 0; i5 < FloatFFT_3D.this.slices; ++i5) {
//									j = i5 * FloatFFT_3D.this.sliceStride + i
//											+ i4;
//									k = this.val$startt + 2 * i5;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.slices + 2 * i5;
//									i1 = l + 2 * FloatFFT_3D.this.slices;
//									i2 = i1 + 2 * FloatFFT_3D.this.slices;
//									FloatFFT_3D.this.t[k] = this.val$a[j];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//									FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//									FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//									FloatFFT_3D.this.t[i1] = this.val$a[(j + 4)];
//									FloatFFT_3D.this.t[(i1 + 1)] = this.val$a[(j + 5)];
//									FloatFFT_3D.this.t[i2] = this.val$a[(j + 6)];
//									FloatFFT_3D.this.t[(i2 + 1)] = this.val$a[(j + 7)];
//								}
//								FloatFFT_3D.this.fftSlices.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatFFT_3D.this.fftSlices.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.slices,
//										this.val$scale);
//								FloatFFT_3D.this.fftSlices.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt + 4
//												* FloatFFT_3D.this.slices,
//										this.val$scale);
//								FloatFFT_3D.this.fftSlices.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt + 6
//												* FloatFFT_3D.this.slices,
//										this.val$scale);
//								for (i5 = 0; i5 < FloatFFT_3D.this.slices; ++i5) {
//									j = i5 * FloatFFT_3D.this.sliceStride + i
//											+ i4;
//									k = this.val$startt + 2 * i5;
//									l = this.val$startt + 2
//											* FloatFFT_3D.this.slices + 2 * i5;
//									i1 = l + 2 * FloatFFT_3D.this.slices;
//									i2 = i1 + 2 * FloatFFT_3D.this.slices;
//									this.val$a[j] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[(j + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//									this.val$a[(j + 2)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[l];
//									this.val$a[(j + 3)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(l + 1)];
//									this.val$a[(j + 4)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i1];
//									this.val$a[(j + 5)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i1 + 1)];
//									this.val$a[(j + 6)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i2];
//									this.val$a[(j + 7)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i2 + 1)];
//								}
//							}
//							i3 += this.val$nthreads;
//						}
//					} else if (FloatFFT_3D.this.columns == 4) {
//						i3 = this.val$n0;
//						while (i3 < FloatFFT_3D.this.rows) {
//							i = i3 * FloatFFT_3D.this.rowStride;
//							for (i4 = 0; i4 < FloatFFT_3D.this.slices; ++i4) {
//								j = i4 * FloatFFT_3D.this.sliceStride + i;
//								k = this.val$startt + 2 * i4;
//								l = this.val$startt + 2
//										* FloatFFT_3D.this.slices + 2 * i4;
//								FloatFFT_3D.this.t[k] = this.val$a[j];
//								FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//								FloatFFT_3D.this.t[l] = this.val$a[(j + 2)];
//								FloatFFT_3D.this.t[(l + 1)] = this.val$a[(j + 3)];
//							}
//							FloatFFT_3D.this.fftSlices.complexInverse(
//									FloatFFT_3D.this.t, this.val$startt,
//									this.val$scale);
//							FloatFFT_3D.this.fftSlices.complexInverse(
//									FloatFFT_3D.this.t, this.val$startt + 2
//											* FloatFFT_3D.this.slices,
//									this.val$scale);
//							for (i4 = 0; i4 < FloatFFT_3D.this.slices; ++i4) {
//								j = i4 * FloatFFT_3D.this.sliceStride + i;
//								k = this.val$startt + 2 * i4;
//								l = this.val$startt + 2
//										* FloatFFT_3D.this.slices + 2 * i4;
//								this.val$a[j] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[k];
//								this.val$a[(j + 1)] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[(k + 1)];
//								this.val$a[(j + 2)] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[l];
//								this.val$a[(j + 3)] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[(l + 1)];
//							}
//							i3 += this.val$nthreads;
//						}
//					} else {
//						if (FloatFFT_3D.this.columns != 2)
//							return;
//						i3 = this.val$n0;
//						while (i3 < FloatFFT_3D.this.rows) {
//							i = i3 * FloatFFT_3D.this.rowStride;
//							for (i4 = 0; i4 < FloatFFT_3D.this.slices; ++i4) {
//								j = i4 * FloatFFT_3D.this.sliceStride + i;
//								k = this.val$startt + 2 * i4;
//								FloatFFT_3D.this.t[k] = this.val$a[j];
//								FloatFFT_3D.this.t[(k + 1)] = this.val$a[(j + 1)];
//							}
//							FloatFFT_3D.this.fftSlices.complexInverse(
//									FloatFFT_3D.this.t, this.val$startt,
//									this.val$scale);
//							for (i4 = 0; i4 < FloatFFT_3D.this.slices; ++i4) {
//								j = i4 * FloatFFT_3D.this.sliceStride + i;
//								k = this.val$startt + 2 * i4;
//								this.val$a[j] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[k];
//								this.val$a[(j + 1)] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[(k + 1)];
//							}
//							i3 += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void cdft3db_subth(int paramInt,
//			float[][][] paramArrayOfFloat, boolean paramBoolean) {
//		int k = Math.min(ConcurrencyUtils.getNumberOfThreads(), this.rows);
//		int i = 8 * this.slices;
//		if (this.columns == 4)
//			i >>= 1;
//		else if (this.columns < 4)
//			i >>= 2;
//		Future[] arrayOfFuture = new Future[k];
//		for (int j = 0; j < k; ++j) {
//			int l = j;
//			int i1 = i * j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt,
//					l, k, i1, paramArrayOfFloat, paramBoolean) {
//				public strictfp void run() {
//					int i1;
//					int i2;
//					int i3;
//					int i;
//					int j;
//					int k;
//					int l;
//					if (this.val$isgn == -1) {
//						if (FloatFFT_3D.this.columns > 4) {
//							i1 = this.val$n0;
//							while (i1 < FloatFFT_3D.this.rows) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.columns; i2 += 8) {
//									for (i3 = 0; i3 < FloatFFT_3D.this.slices; ++i3) {
//										i = this.val$startt + 2 * i3;
//										j = this.val$startt + 2
//												* FloatFFT_3D.this.slices + 2
//												* i3;
//										k = j + 2 * FloatFFT_3D.this.slices;
//										l = k + 2 * FloatFFT_3D.this.slices;
//										FloatFFT_3D.this.t[i] = this.val$a[i3][i1][i2];
//										FloatFFT_3D.this.t[(i + 1)] = this.val$a[i3][i1][(i2 + 1)];
//										FloatFFT_3D.this.t[j] = this.val$a[i3][i1][(i2 + 2)];
//										FloatFFT_3D.this.t[(j + 1)] = this.val$a[i3][i1][(i2 + 3)];
//										FloatFFT_3D.this.t[k] = this.val$a[i3][i1][(i2 + 4)];
//										FloatFFT_3D.this.t[(k + 1)] = this.val$a[i3][i1][(i2 + 5)];
//										FloatFFT_3D.this.t[l] = this.val$a[i3][i1][(i2 + 6)];
//										FloatFFT_3D.this.t[(l + 1)] = this.val$a[i3][i1][(i2 + 7)];
//									}
//									FloatFFT_3D.this.fftSlices
//											.complexForward(FloatFFT_3D.this.t,
//													this.val$startt);
//									FloatFFT_3D.this.fftSlices.complexForward(
//											FloatFFT_3D.this.t, this.val$startt
//													+ 2
//													* FloatFFT_3D.this.slices);
//									FloatFFT_3D.this.fftSlices.complexForward(
//											FloatFFT_3D.this.t, this.val$startt
//													+ 4
//													* FloatFFT_3D.this.slices);
//									FloatFFT_3D.this.fftSlices.complexForward(
//											FloatFFT_3D.this.t, this.val$startt
//													+ 6
//													* FloatFFT_3D.this.slices);
//									for (i3 = 0; i3 < FloatFFT_3D.this.slices; ++i3) {
//										i = this.val$startt + 2 * i3;
//										j = this.val$startt + 2
//												* FloatFFT_3D.this.slices + 2
//												* i3;
//										k = j + 2 * FloatFFT_3D.this.slices;
//										l = k + 2 * FloatFFT_3D.this.slices;
//										this.val$a[i3][i1][i2] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[i];
//										this.val$a[i3][i1][(i2 + 1)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(i + 1)];
//										this.val$a[i3][i1][(i2 + 2)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[j];
//										this.val$a[i3][i1][(i2 + 3)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(j + 1)];
//										this.val$a[i3][i1][(i2 + 4)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[k];
//										this.val$a[i3][i1][(i2 + 5)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(k + 1)];
//										this.val$a[i3][i1][(i2 + 6)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[l];
//										this.val$a[i3][i1][(i2 + 7)] = FloatFFT_3D
//												.access$800(FloatFFT_3D.this)[(l + 1)];
//									}
//								}
//								i1 += this.val$nthreads;
//							}
//						} else if (FloatFFT_3D.this.columns == 4) {
//							i1 = this.val$n0;
//							while (i1 < FloatFFT_3D.this.rows) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.slices; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.slices + 2 * i2;
//									FloatFFT_3D.this.t[i] = this.val$a[i2][i1][0];
//									FloatFFT_3D.this.t[(i + 1)] = this.val$a[i2][i1][1];
//									FloatFFT_3D.this.t[j] = this.val$a[i2][i1][2];
//									FloatFFT_3D.this.t[(j + 1)] = this.val$a[i2][i1][3];
//								}
//								FloatFFT_3D.this.fftSlices.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								FloatFFT_3D.this.fftSlices.complexForward(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.slices);
//								for (i2 = 0; i2 < FloatFFT_3D.this.slices; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.slices + 2 * i2;
//									this.val$a[i2][i1][0] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i];
//									this.val$a[i2][i1][1] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i + 1)];
//									this.val$a[i2][i1][2] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[j];
//									this.val$a[i2][i1][3] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(j + 1)];
//								}
//								i1 += this.val$nthreads;
//							}
//						} else {
//							if (FloatFFT_3D.this.columns != 2)
//								return;
//							i1 = this.val$n0;
//							while (i1 < FloatFFT_3D.this.rows) {
//								for (i2 = 0; i2 < FloatFFT_3D.this.slices; ++i2) {
//									i = this.val$startt + 2 * i2;
//									FloatFFT_3D.this.t[i] = this.val$a[i2][i1][0];
//									FloatFFT_3D.this.t[(i + 1)] = this.val$a[i2][i1][1];
//								}
//								FloatFFT_3D.this.fftSlices.complexForward(
//										FloatFFT_3D.this.t, this.val$startt);
//								for (i2 = 0; i2 < FloatFFT_3D.this.slices; ++i2) {
//									i = this.val$startt + 2 * i2;
//									this.val$a[i2][i1][0] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i];
//									this.val$a[i2][i1][1] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i + 1)];
//								}
//								i1 += this.val$nthreads;
//							}
//						}
//					} else if (FloatFFT_3D.this.columns > 4) {
//						i1 = this.val$n0;
//						while (i1 < FloatFFT_3D.this.rows) {
//							for (i2 = 0; i2 < FloatFFT_3D.this.columns; i2 += 8) {
//								for (i3 = 0; i3 < FloatFFT_3D.this.slices; ++i3) {
//									i = this.val$startt + 2 * i3;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.slices + 2 * i3;
//									k = j + 2 * FloatFFT_3D.this.slices;
//									l = k + 2 * FloatFFT_3D.this.slices;
//									FloatFFT_3D.this.t[i] = this.val$a[i3][i1][i2];
//									FloatFFT_3D.this.t[(i + 1)] = this.val$a[i3][i1][(i2 + 1)];
//									FloatFFT_3D.this.t[j] = this.val$a[i3][i1][(i2 + 2)];
//									FloatFFT_3D.this.t[(j + 1)] = this.val$a[i3][i1][(i2 + 3)];
//									FloatFFT_3D.this.t[k] = this.val$a[i3][i1][(i2 + 4)];
//									FloatFFT_3D.this.t[(k + 1)] = this.val$a[i3][i1][(i2 + 5)];
//									FloatFFT_3D.this.t[l] = this.val$a[i3][i1][(i2 + 6)];
//									FloatFFT_3D.this.t[(l + 1)] = this.val$a[i3][i1][(i2 + 7)];
//								}
//								FloatFFT_3D.this.fftSlices.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatFFT_3D.this.fftSlices.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt + 2
//												* FloatFFT_3D.this.slices,
//										this.val$scale);
//								FloatFFT_3D.this.fftSlices.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt + 4
//												* FloatFFT_3D.this.slices,
//										this.val$scale);
//								FloatFFT_3D.this.fftSlices.complexInverse(
//										FloatFFT_3D.this.t, this.val$startt + 6
//												* FloatFFT_3D.this.slices,
//										this.val$scale);
//								for (i3 = 0; i3 < FloatFFT_3D.this.slices; ++i3) {
//									i = this.val$startt + 2 * i3;
//									j = this.val$startt + 2
//											* FloatFFT_3D.this.slices + 2 * i3;
//									k = j + 2 * FloatFFT_3D.this.slices;
//									l = k + 2 * FloatFFT_3D.this.slices;
//									this.val$a[i3][i1][i2] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[i];
//									this.val$a[i3][i1][(i2 + 1)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(i + 1)];
//									this.val$a[i3][i1][(i2 + 2)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[j];
//									this.val$a[i3][i1][(i2 + 3)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(j + 1)];
//									this.val$a[i3][i1][(i2 + 4)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[k];
//									this.val$a[i3][i1][(i2 + 5)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(k + 1)];
//									this.val$a[i3][i1][(i2 + 6)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[l];
//									this.val$a[i3][i1][(i2 + 7)] = FloatFFT_3D
//											.access$800(FloatFFT_3D.this)[(l + 1)];
//								}
//							}
//							i1 += this.val$nthreads;
//						}
//					} else if (FloatFFT_3D.this.columns == 4) {
//						i1 = this.val$n0;
//						while (i1 < FloatFFT_3D.this.rows) {
//							for (i2 = 0; i2 < FloatFFT_3D.this.slices; ++i2) {
//								i = this.val$startt + 2 * i2;
//								j = this.val$startt + 2
//										* FloatFFT_3D.this.slices + 2 * i2;
//								FloatFFT_3D.this.t[i] = this.val$a[i2][i1][0];
//								FloatFFT_3D.this.t[(i + 1)] = this.val$a[i2][i1][1];
//								FloatFFT_3D.this.t[j] = this.val$a[i2][i1][2];
//								FloatFFT_3D.this.t[(j + 1)] = this.val$a[i2][i1][3];
//							}
//							FloatFFT_3D.this.fftSlices.complexInverse(
//									FloatFFT_3D.this.t, this.val$startt,
//									this.val$scale);
//							FloatFFT_3D.this.fftSlices.complexInverse(
//									FloatFFT_3D.this.t, this.val$startt + 2
//											* FloatFFT_3D.this.slices,
//									this.val$scale);
//							for (i2 = 0; i2 < FloatFFT_3D.this.slices; ++i2) {
//								i = this.val$startt + 2 * i2;
//								j = this.val$startt + 2
//										* FloatFFT_3D.this.slices + 2 * i2;
//								this.val$a[i2][i1][0] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[i];
//								this.val$a[i2][i1][1] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[(i + 1)];
//								this.val$a[i2][i1][2] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[j];
//								this.val$a[i2][i1][3] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[(j + 1)];
//							}
//							i1 += this.val$nthreads;
//						}
//					} else {
//						if (FloatFFT_3D.this.columns != 2)
//							return;
//						i1 = this.val$n0;
//						while (i1 < FloatFFT_3D.this.rows) {
//							for (i2 = 0; i2 < FloatFFT_3D.this.slices; ++i2) {
//								i = this.val$startt + 2 * i2;
//								FloatFFT_3D.this.t[i] = this.val$a[i2][i1][0];
//								FloatFFT_3D.this.t[(i + 1)] = this.val$a[i2][i1][1];
//							}
//							FloatFFT_3D.this.fftSlices.complexInverse(
//									FloatFFT_3D.this.t, this.val$startt,
//									this.val$scale);
//							for (i2 = 0; i2 < FloatFFT_3D.this.slices; ++i2) {
//								i = this.val$startt + 2 * i2;
//								this.val$a[i2][i1][0] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[i];
//								this.val$a[i2][i1][1] = FloatFFT_3D
//										.access$800(FloatFFT_3D.this)[(i + 1)];
//							}
//							i1 += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void rdft3d_sub(int paramInt, float[] paramArrayOfFloat) {
//		int i = this.slices >> 1;
//		int j = this.rows >> 1;
//		int l;
//		int i3;
//		int i4;
//		int i5;
//		int i6;
//		int i2;
//		if (paramInt < 0) {
//			float f;
//			for (k = 1; k < i; ++k) {
//				l = this.slices - k;
//				i3 = k * this.sliceStride;
//				i4 = l * this.sliceStride;
//				i5 = k * this.sliceStride + j * this.rowStride;
//				i6 = l * this.sliceStride + j * this.rowStride;
//				f = paramArrayOfFloat[i3] - paramArrayOfFloat[i4];
//				paramArrayOfFloat[i3] += paramArrayOfFloat[i4];
//				paramArrayOfFloat[i4] = f;
//				f = paramArrayOfFloat[(i4 + 1)] - paramArrayOfFloat[(i3 + 1)];
//				paramArrayOfFloat[(i3 + 1)] += paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[(i4 + 1)] = f;
//				f = paramArrayOfFloat[i5] - paramArrayOfFloat[i6];
//				paramArrayOfFloat[i5] += paramArrayOfFloat[i6];
//				paramArrayOfFloat[i6] = f;
//				f = paramArrayOfFloat[(i6 + 1)] - paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[(i5 + 1)] += paramArrayOfFloat[(i6 + 1)];
//				paramArrayOfFloat[(i6 + 1)] = f;
//				for (i1 = 1; i1 < j; ++i1) {
//					i2 = this.rows - i1;
//					i3 = k * this.sliceStride + i1 * this.rowStride;
//					i4 = l * this.sliceStride + i2 * this.rowStride;
//					f = paramArrayOfFloat[i3] - paramArrayOfFloat[i4];
//					paramArrayOfFloat[i3] += paramArrayOfFloat[i4];
//					paramArrayOfFloat[i4] = f;
//					f = paramArrayOfFloat[(i4 + 1)]
//							- paramArrayOfFloat[(i3 + 1)];
//					paramArrayOfFloat[(i3 + 1)] += paramArrayOfFloat[(i4 + 1)];
//					paramArrayOfFloat[(i4 + 1)] = f;
//					i5 = l * this.sliceStride + i1 * this.rowStride;
//					i6 = k * this.sliceStride + i2 * this.rowStride;
//					f = paramArrayOfFloat[i5] - paramArrayOfFloat[i6];
//					paramArrayOfFloat[i5] += paramArrayOfFloat[i6];
//					paramArrayOfFloat[i6] = f;
//					f = paramArrayOfFloat[(i6 + 1)]
//							- paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[(i5 + 1)] += paramArrayOfFloat[(i6 + 1)];
//					paramArrayOfFloat[(i6 + 1)] = f;
//				}
//			}
//			for (i1 = 1;; ++i1) {
//				if (i1 >= j)
//					return;
//				i2 = this.rows - i1;
//				i3 = i1 * this.rowStride;
//				i4 = i2 * this.rowStride;
//				f = paramArrayOfFloat[i3] - paramArrayOfFloat[i4];
//				paramArrayOfFloat[i3] += paramArrayOfFloat[i4];
//				paramArrayOfFloat[i4] = f;
//				f = paramArrayOfFloat[(i4 + 1)] - paramArrayOfFloat[(i3 + 1)];
//				paramArrayOfFloat[(i3 + 1)] += paramArrayOfFloat[(i4 + 1)];
//				paramArrayOfFloat[(i4 + 1)] = f;
//				i5 = i * this.sliceStride + i1 * this.rowStride;
//				i6 = i * this.sliceStride + i2 * this.rowStride;
//				f = paramArrayOfFloat[i5] - paramArrayOfFloat[i6];
//				paramArrayOfFloat[i5] += paramArrayOfFloat[i6];
//				paramArrayOfFloat[i6] = f;
//				f = paramArrayOfFloat[(i6 + 1)] - paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[(i5 + 1)] += paramArrayOfFloat[(i6 + 1)];
//				paramArrayOfFloat[(i6 + 1)] = f;
//			}
//		}
//		for (int k = 1; k < i; ++k) {
//			l = this.slices - k;
//			i3 = l * this.sliceStride;
//			i4 = k * this.sliceStride;
//			paramArrayOfFloat[i3] = (0.5F * (paramArrayOfFloat[i4] - paramArrayOfFloat[i3]));
//			paramArrayOfFloat[i4] -= paramArrayOfFloat[i3];
//			paramArrayOfFloat[(i3 + 1)] = (0.5F * (paramArrayOfFloat[(i4 + 1)] + paramArrayOfFloat[(i3 + 1)]));
//			paramArrayOfFloat[(i4 + 1)] -= paramArrayOfFloat[(i3 + 1)];
//			i5 = l * this.sliceStride + j * this.rowStride;
//			i6 = k * this.sliceStride + j * this.rowStride;
//			paramArrayOfFloat[i5] = (0.5F * (paramArrayOfFloat[i6] - paramArrayOfFloat[i5]));
//			paramArrayOfFloat[i6] -= paramArrayOfFloat[i5];
//			paramArrayOfFloat[(i5 + 1)] = (0.5F * (paramArrayOfFloat[(i6 + 1)] + paramArrayOfFloat[(i5 + 1)]));
//			paramArrayOfFloat[(i6 + 1)] -= paramArrayOfFloat[(i5 + 1)];
//			for (i1 = 1; i1 < j; ++i1) {
//				i2 = this.rows - i1;
//				i3 = l * this.sliceStride + i2 * this.rowStride;
//				i4 = k * this.sliceStride + i1 * this.rowStride;
//				paramArrayOfFloat[i3] = (0.5F * (paramArrayOfFloat[i4] - paramArrayOfFloat[i3]));
//				paramArrayOfFloat[i4] -= paramArrayOfFloat[i3];
//				paramArrayOfFloat[(i3 + 1)] = (0.5F * (paramArrayOfFloat[(i4 + 1)] + paramArrayOfFloat[(i3 + 1)]));
//				paramArrayOfFloat[(i4 + 1)] -= paramArrayOfFloat[(i3 + 1)];
//				i5 = k * this.sliceStride + i2 * this.rowStride;
//				i6 = l * this.sliceStride + i1 * this.rowStride;
//				paramArrayOfFloat[i5] = (0.5F * (paramArrayOfFloat[i6] - paramArrayOfFloat[i5]));
//				paramArrayOfFloat[i6] -= paramArrayOfFloat[i5];
//				paramArrayOfFloat[(i5 + 1)] = (0.5F * (paramArrayOfFloat[(i6 + 1)] + paramArrayOfFloat[(i5 + 1)]));
//				paramArrayOfFloat[(i6 + 1)] -= paramArrayOfFloat[(i5 + 1)];
//			}
//		}
//		for (int i1 = 1; i1 < j; ++i1) {
//			i2 = this.rows - i1;
//			i3 = i2 * this.rowStride;
//			i4 = i1 * this.rowStride;
//			paramArrayOfFloat[i3] = (0.5F * (paramArrayOfFloat[i4] - paramArrayOfFloat[i3]));
//			paramArrayOfFloat[i4] -= paramArrayOfFloat[i3];
//			paramArrayOfFloat[(i3 + 1)] = (0.5F * (paramArrayOfFloat[(i4 + 1)] + paramArrayOfFloat[(i3 + 1)]));
//			paramArrayOfFloat[(i4 + 1)] -= paramArrayOfFloat[(i3 + 1)];
//			i5 = i * this.sliceStride + i2 * this.rowStride;
//			i6 = i * this.sliceStride + i1 * this.rowStride;
//			paramArrayOfFloat[i5] = (0.5F * (paramArrayOfFloat[i6] - paramArrayOfFloat[i5]));
//			paramArrayOfFloat[i6] -= paramArrayOfFloat[i5];
//			paramArrayOfFloat[(i5 + 1)] = (0.5F * (paramArrayOfFloat[(i6 + 1)] + paramArrayOfFloat[(i5 + 1)]));
//			paramArrayOfFloat[(i6 + 1)] -= paramArrayOfFloat[(i5 + 1)];
//		}
//	}
//
//	private strictfp void rdft3d_sub(int paramInt, float[][][] paramArrayOfFloat) {
//		int i = this.slices >> 1;
//		int j = this.rows >> 1;
//		int l;
//		int i2;
//		if (paramInt < 0) {
//			float f;
//			for (k = 1; k < i; ++k) {
//				l = this.slices - k;
//				f = paramArrayOfFloat[k][0][0] - paramArrayOfFloat[l][0][0];
//				paramArrayOfFloat[k][0][0] += paramArrayOfFloat[l][0][0];
//				paramArrayOfFloat[l][0][0] = f;
//				f = paramArrayOfFloat[l][0][1] - paramArrayOfFloat[k][0][1];
//				paramArrayOfFloat[k][0][1] += paramArrayOfFloat[l][0][1];
//				paramArrayOfFloat[l][0][1] = f;
//				f = paramArrayOfFloat[k][j][0] - paramArrayOfFloat[l][j][0];
//				paramArrayOfFloat[k][j][0] += paramArrayOfFloat[l][j][0];
//				paramArrayOfFloat[l][j][0] = f;
//				f = paramArrayOfFloat[l][j][1] - paramArrayOfFloat[k][j][1];
//				paramArrayOfFloat[k][j][1] += paramArrayOfFloat[l][j][1];
//				paramArrayOfFloat[l][j][1] = f;
//				for (i1 = 1; i1 < j; ++i1) {
//					i2 = this.rows - i1;
//					f = paramArrayOfFloat[k][i1][0]
//							- paramArrayOfFloat[l][i2][0];
//					paramArrayOfFloat[k][i1][0] += paramArrayOfFloat[l][i2][0];
//					paramArrayOfFloat[l][i2][0] = f;
//					f = paramArrayOfFloat[l][i2][1]
//							- paramArrayOfFloat[k][i1][1];
//					paramArrayOfFloat[k][i1][1] += paramArrayOfFloat[l][i2][1];
//					paramArrayOfFloat[l][i2][1] = f;
//					f = paramArrayOfFloat[l][i1][0]
//							- paramArrayOfFloat[k][i2][0];
//					paramArrayOfFloat[l][i1][0] += paramArrayOfFloat[k][i2][0];
//					paramArrayOfFloat[k][i2][0] = f;
//					f = paramArrayOfFloat[k][i2][1]
//							- paramArrayOfFloat[l][i1][1];
//					paramArrayOfFloat[l][i1][1] += paramArrayOfFloat[k][i2][1];
//					paramArrayOfFloat[k][i2][1] = f;
//				}
//			}
//			for (i1 = 1;; ++i1) {
//				if (i1 >= j)
//					return;
//				i2 = this.rows - i1;
//				f = paramArrayOfFloat[0][i1][0] - paramArrayOfFloat[0][i2][0];
//				paramArrayOfFloat[0][i1][0] += paramArrayOfFloat[0][i2][0];
//				paramArrayOfFloat[0][i2][0] = f;
//				f = paramArrayOfFloat[0][i2][1] - paramArrayOfFloat[0][i1][1];
//				paramArrayOfFloat[0][i1][1] += paramArrayOfFloat[0][i2][1];
//				paramArrayOfFloat[0][i2][1] = f;
//				f = paramArrayOfFloat[i][i1][0] - paramArrayOfFloat[i][i2][0];
//				paramArrayOfFloat[i][i1][0] += paramArrayOfFloat[i][i2][0];
//				paramArrayOfFloat[i][i2][0] = f;
//				f = paramArrayOfFloat[i][i2][1] - paramArrayOfFloat[i][i1][1];
//				paramArrayOfFloat[i][i1][1] += paramArrayOfFloat[i][i2][1];
//				paramArrayOfFloat[i][i2][1] = f;
//			}
//		}
//		for (int k = 1; k < i; ++k) {
//			l = this.slices - k;
//			paramArrayOfFloat[l][0][0] = (0.5F * (paramArrayOfFloat[k][0][0] - paramArrayOfFloat[l][0][0]));
//			paramArrayOfFloat[k][0][0] -= paramArrayOfFloat[l][0][0];
//			paramArrayOfFloat[l][0][1] = (0.5F * (paramArrayOfFloat[k][0][1] + paramArrayOfFloat[l][0][1]));
//			paramArrayOfFloat[k][0][1] -= paramArrayOfFloat[l][0][1];
//			paramArrayOfFloat[l][j][0] = (0.5F * (paramArrayOfFloat[k][j][0] - paramArrayOfFloat[l][j][0]));
//			paramArrayOfFloat[k][j][0] -= paramArrayOfFloat[l][j][0];
//			paramArrayOfFloat[l][j][1] = (0.5F * (paramArrayOfFloat[k][j][1] + paramArrayOfFloat[l][j][1]));
//			paramArrayOfFloat[k][j][1] -= paramArrayOfFloat[l][j][1];
//			for (i1 = 1; i1 < j; ++i1) {
//				i2 = this.rows - i1;
//				paramArrayOfFloat[l][i2][0] = (0.5F * (paramArrayOfFloat[k][i1][0] - paramArrayOfFloat[l][i2][0]));
//				paramArrayOfFloat[k][i1][0] -= paramArrayOfFloat[l][i2][0];
//				paramArrayOfFloat[l][i2][1] = (0.5F * (paramArrayOfFloat[k][i1][1] + paramArrayOfFloat[l][i2][1]));
//				paramArrayOfFloat[k][i1][1] -= paramArrayOfFloat[l][i2][1];
//				paramArrayOfFloat[k][i2][0] = (0.5F * (paramArrayOfFloat[l][i1][0] - paramArrayOfFloat[k][i2][0]));
//				paramArrayOfFloat[l][i1][0] -= paramArrayOfFloat[k][i2][0];
//				paramArrayOfFloat[k][i2][1] = (0.5F * (paramArrayOfFloat[l][i1][1] + paramArrayOfFloat[k][i2][1]));
//				paramArrayOfFloat[l][i1][1] -= paramArrayOfFloat[k][i2][1];
//			}
//		}
//		for (int i1 = 1; i1 < j; ++i1) {
//			i2 = this.rows - i1;
//			paramArrayOfFloat[0][i2][0] = (0.5F * (paramArrayOfFloat[0][i1][0] - paramArrayOfFloat[0][i2][0]));
//			paramArrayOfFloat[0][i1][0] -= paramArrayOfFloat[0][i2][0];
//			paramArrayOfFloat[0][i2][1] = (0.5F * (paramArrayOfFloat[0][i1][1] + paramArrayOfFloat[0][i2][1]));
//			paramArrayOfFloat[0][i1][1] -= paramArrayOfFloat[0][i2][1];
//			paramArrayOfFloat[i][i2][0] = (0.5F * (paramArrayOfFloat[i][i1][0] - paramArrayOfFloat[i][i2][0]));
//			paramArrayOfFloat[i][i1][0] -= paramArrayOfFloat[i][i2][0];
//			paramArrayOfFloat[i][i2][1] = (0.5F * (paramArrayOfFloat[i][i1][1] + paramArrayOfFloat[i][i2][1]));
//			paramArrayOfFloat[i][i1][1] -= paramArrayOfFloat[i][i2][1];
//		}
//	}
//
//	private strictfp void fillSymmetric(float[][][] paramArrayOfFloat) {
//		int i = 2 * this.columns;
//		int j = this.rows / 2;
//		int k = this.slices / 2;
//		int l = ConcurrencyUtils.getNumberOfThreads();
//		int i2;
//		int i3;
//		int i4;
//		int i5;
//		if ((l > 1) && (this.useThreads) && (this.slices >= l)) {
//			Future[] arrayOfFuture = new Future[l];
//			i2 = this.slices / l;
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i2;
//				i5 = (i3 == l - 1) ? this.slices : i4 + i2;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(i4,
//						i5, i, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = (FloatFFT_3D.this.slices - i)
//									% FloatFFT_3D.this.slices;
//							for (int k = 0; k < FloatFFT_3D.this.rows; ++k) {
//								int l = (FloatFFT_3D.this.rows - k)
//										% FloatFFT_3D.this.rows;
//								for (int i1 = 1; i1 < FloatFFT_3D.this.columns; i1 += 2) {
//									int i2 = this.val$twon3 - i1;
//									this.val$a[j][l][i2] = (-this.val$a[i][k][(i1 + 2)]);
//									this.val$a[j][l][(i2 - 1)] = this.val$a[i][k][(i1 + 1)];
//								}
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i2;
//				i5 = (i3 == l - 1) ? this.slices : i4 + i2;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(i4,
//						i5, j, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = (FloatFFT_3D.this.slices - i)
//									% FloatFFT_3D.this.slices;
//							for (int k = 1; k < this.val$n2d2; ++k) {
//								int l = FloatFFT_3D.this.rows - k;
//								this.val$a[j][k][FloatFFT_3D.this.columns] = this.val$a[i][l][1];
//								this.val$a[i][l][FloatFFT_3D.this.columns] = this.val$a[i][l][1];
//								this.val$a[j][k][(FloatFFT_3D.this.columns + 1)] = (-this.val$a[i][l][0]);
//								this.val$a[i][l][(FloatFFT_3D.this.columns + 1)] = this.val$a[i][l][0];
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i3 = 0; i3 < l; ++i3) {
//				i4 = i3 * i2;
//				i5 = (i3 == l - 1) ? this.slices : i4 + i2;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(i4,
//						i5, j, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = (FloatFFT_3D.this.slices - i)
//									% FloatFFT_3D.this.slices;
//							for (int k = 1; k < this.val$n2d2; ++k) {
//								int l = FloatFFT_3D.this.rows - k;
//								this.val$a[j][l][0] = this.val$a[i][k][0];
//								this.val$a[j][l][1] = (-this.val$a[i][k][1]);
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (i1 = 0; i1 < this.slices; ++i1) {
//				i2 = (this.slices - i1) % this.slices;
//				for (i3 = 0; i3 < this.rows; ++i3) {
//					i4 = (this.rows - i3) % this.rows;
//					for (i5 = 1; i5 < this.columns; i5 += 2) {
//						int i6 = i - i5;
//						paramArrayOfFloat[i2][i4][i6] = (-paramArrayOfFloat[i1][i3][(i5 + 2)]);
//						paramArrayOfFloat[i2][i4][(i6 - 1)] = paramArrayOfFloat[i1][i3][(i5 + 1)];
//					}
//				}
//			}
//			for (i1 = 0; i1 < this.slices; ++i1) {
//				i2 = (this.slices - i1) % this.slices;
//				for (i3 = 1; i3 < j; ++i3) {
//					i4 = this.rows - i3;
//					paramArrayOfFloat[i2][i3][this.columns] = paramArrayOfFloat[i1][i4][1];
//					paramArrayOfFloat[i1][i4][this.columns] = paramArrayOfFloat[i1][i4][1];
//					paramArrayOfFloat[i2][i3][(this.columns + 1)] = (-paramArrayOfFloat[i1][i4][0]);
//					paramArrayOfFloat[i1][i4][(this.columns + 1)] = paramArrayOfFloat[i1][i4][0];
//				}
//			}
//			for (i1 = 0; i1 < this.slices; ++i1) {
//				i2 = (this.slices - i1) % this.slices;
//				for (i3 = 1; i3 < j; ++i3) {
//					i4 = this.rows - i3;
//					paramArrayOfFloat[i2][i4][0] = paramArrayOfFloat[i1][i3][0];
//					paramArrayOfFloat[i2][i4][1] = (-paramArrayOfFloat[i1][i3][1]);
//				}
//			}
//		}
//		for (int i1 = 1; i1 < k; ++i1) {
//			i2 = this.slices - i1;
//			paramArrayOfFloat[i1][0][this.columns] = paramArrayOfFloat[i2][0][1];
//			paramArrayOfFloat[i2][0][this.columns] = paramArrayOfFloat[i2][0][1];
//			paramArrayOfFloat[i1][0][(this.columns + 1)] = (-paramArrayOfFloat[i2][0][0]);
//			paramArrayOfFloat[i2][0][(this.columns + 1)] = paramArrayOfFloat[i2][0][0];
//			paramArrayOfFloat[i1][j][this.columns] = paramArrayOfFloat[i2][j][1];
//			paramArrayOfFloat[i2][j][this.columns] = paramArrayOfFloat[i2][j][1];
//			paramArrayOfFloat[i1][j][(this.columns + 1)] = (-paramArrayOfFloat[i2][j][0]);
//			paramArrayOfFloat[i2][j][(this.columns + 1)] = paramArrayOfFloat[i2][j][0];
//			paramArrayOfFloat[i2][0][0] = paramArrayOfFloat[i1][0][0];
//			paramArrayOfFloat[i2][0][1] = (-paramArrayOfFloat[i1][0][1]);
//			paramArrayOfFloat[i2][j][0] = paramArrayOfFloat[i1][j][0];
//			paramArrayOfFloat[i2][j][1] = (-paramArrayOfFloat[i1][j][1]);
//		}
//		paramArrayOfFloat[0][0][this.columns] = paramArrayOfFloat[0][0][1];
//		paramArrayOfFloat[0][0][1] = 0.0F;
//		paramArrayOfFloat[0][j][this.columns] = paramArrayOfFloat[0][j][1];
//		paramArrayOfFloat[0][j][1] = 0.0F;
//		paramArrayOfFloat[k][0][this.columns] = paramArrayOfFloat[k][0][1];
//		paramArrayOfFloat[k][0][1] = 0.0F;
//		paramArrayOfFloat[k][j][this.columns] = paramArrayOfFloat[k][j][1];
//		paramArrayOfFloat[k][j][1] = 0.0F;
//		paramArrayOfFloat[k][0][(this.columns + 1)] = 0.0F;
//		paramArrayOfFloat[k][j][(this.columns + 1)] = 0.0F;
//	}
//
//	private strictfp void fillSymmetric(float[] paramArrayOfFloat) {
//		int i = 2 * this.columns;
//		int j = this.rows / 2;
//		int k = this.slices / 2;
//		int l = this.rows * i;
//		int i1 = i;
//		int i5;
//		int i9;
//		int i6;
//		int i7;
//		int i11;
//		for (int i8 = this.slices - 1; i8 >= 1; --i8) {
//			i4 = i8 * this.sliceStride;
//			i5 = 2 * i4;
//			for (i9 = 0; i9 < this.rows; ++i9) {
//				i6 = i9 * this.rowStride;
//				i7 = 2 * i6;
//				for (i11 = 0; i11 < this.columns; i11 += 2) {
//					i2 = i4 + i6 + i11;
//					i3 = i5 + i7 + i11;
//					paramArrayOfFloat[i3] = paramArrayOfFloat[i2];
//					paramArrayOfFloat[i2] = 0.0F;
//					paramArrayOfFloat[(++i3)] = paramArrayOfFloat[(++i2)];
//					paramArrayOfFloat[i2] = 0.0F;
//				}
//			}
//		}
//		for (i8 = 1; i8 < this.rows; ++i8) {
//			i4 = (this.rows - i8) * this.rowStride;
//			i5 = (this.rows - i8) * i1;
//			for (i9 = 0; i9 < this.columns; i9 += 2) {
//				i2 = i4 + i9;
//				i3 = i5 + i9;
//				paramArrayOfFloat[i3] = paramArrayOfFloat[i2];
//				paramArrayOfFloat[i2] = 0.0F;
//				paramArrayOfFloat[(++i3)] = paramArrayOfFloat[(++i2)];
//				paramArrayOfFloat[i2] = 0.0F;
//			}
//		}
//		i8 = ConcurrencyUtils.getNumberOfThreads();
//		int i12;
//		if ((i8 > 1) && (this.useThreads) && (this.slices >= i8)) {
//			Future[] arrayOfFuture = new Future[i8];
//			i11 = this.slices / i8;
//			int i13;
//			int i14;
//			for (i12 = 0; i12 < i8; ++i12) {
//				i13 = i12 * i11;
//				i14 = (i12 == i8 - 1) ? this.slices : i13 + i11;
//				arrayOfFuture[i12] = ConcurrencyUtils.submit(new Runnable(i13,
//						i14, l, i1, i, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = (FloatFFT_3D.this.slices - i)
//									% FloatFFT_3D.this.slices
//									* this.val$twoSliceStride;
//							int k = i * this.val$twoSliceStride;
//							for (int l = 0; l < FloatFFT_3D.this.rows; ++l) {
//								int i1 = (FloatFFT_3D.this.rows - l)
//										% FloatFFT_3D.this.rows
//										* this.val$twoRowStride;
//								int i2 = l * this.val$twoRowStride;
//								for (int i3 = 1; i3 < FloatFFT_3D.this.columns; i3 += 2) {
//									int i4 = j + i1 + this.val$twon3 - i3;
//									int i5 = k + i2 + i3;
//									this.val$a[i4] = (-this.val$a[(i5 + 2)]);
//									this.val$a[(i4 - 1)] = this.val$a[(i5 + 1)];
//								}
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i12 = 0; i12 < i8; ++i12) {
//				i13 = i12 * i11;
//				i14 = (i12 == i8 - 1) ? this.slices : i13 + i11;
//				arrayOfFuture[i12] = ConcurrencyUtils.submit(new Runnable(i13,
//						i14, l, j, i1, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = (FloatFFT_3D.this.slices - i)
//									% FloatFFT_3D.this.slices
//									* this.val$twoSliceStride;
//							int k = i * this.val$twoSliceStride;
//							for (int l = 1; l < this.val$n2d2; ++l) {
//								int i1 = k + (FloatFFT_3D.this.rows - l)
//										* this.val$twoRowStride;
//								int i2 = j + l * this.val$twoRowStride
//										+ FloatFFT_3D.this.columns;
//								int i3 = i1 + FloatFFT_3D.this.columns;
//								int i4 = i1 + 1;
//								this.val$a[i2] = this.val$a[i4];
//								this.val$a[i3] = this.val$a[i4];
//								this.val$a[(i2 + 1)] = (-this.val$a[i1]);
//								this.val$a[(i3 + 1)] = this.val$a[i1];
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i12 = 0; i12 < i8; ++i12) {
//				i13 = i12 * i11;
//				i14 = (i12 == i8 - 1) ? this.slices : i13 + i11;
//				arrayOfFuture[i12] = ConcurrencyUtils.submit(new Runnable(i13,
//						i14, l, j, i1, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//							int j = (FloatFFT_3D.this.slices - i)
//									% FloatFFT_3D.this.slices
//									* this.val$twoSliceStride;
//							int k = i * this.val$twoSliceStride;
//							for (int l = 1; l < this.val$n2d2; ++l) {
//								int i1 = j + (FloatFFT_3D.this.rows - l)
//										* this.val$twoRowStride;
//								int i2 = k + l * this.val$twoRowStride;
//								this.val$a[i1] = this.val$a[i2];
//								this.val$a[(i1 + 1)] = (-this.val$a[(i2 + 1)]);
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (i10 = 0; i10 < this.slices; ++i10) {
//				i4 = (this.slices - i10) % this.slices * l;
//				i6 = i10 * l;
//				for (i11 = 0; i11 < this.rows; ++i11) {
//					i5 = (this.rows - i11) % this.rows * i1;
//					i7 = i11 * i1;
//					for (i12 = 1; i12 < this.columns; i12 += 2) {
//						i2 = i4 + i5 + i - i12;
//						i3 = i6 + i7 + i12;
//						paramArrayOfFloat[i2] = (-paramArrayOfFloat[(i3 + 2)]);
//						paramArrayOfFloat[(i2 - 1)] = paramArrayOfFloat[(i3 + 1)];
//					}
//				}
//			}
//			for (i10 = 0; i10 < this.slices; ++i10) {
//				i6 = (this.slices - i10) % this.slices * l;
//				i7 = i10 * l;
//				for (i11 = 1; i11 < j; ++i11) {
//					i5 = i7 + (this.rows - i11) * i1;
//					i2 = i6 + i11 * i1 + this.columns;
//					i3 = i5 + this.columns;
//					i4 = i5 + 1;
//					paramArrayOfFloat[i2] = paramArrayOfFloat[i4];
//					paramArrayOfFloat[i3] = paramArrayOfFloat[i4];
//					paramArrayOfFloat[(i2 + 1)] = (-paramArrayOfFloat[i5]);
//					paramArrayOfFloat[(i3 + 1)] = paramArrayOfFloat[i5];
//				}
//			}
//			for (i10 = 0; i10 < this.slices; ++i10) {
//				i4 = (this.slices - i10) % this.slices * l;
//				i5 = i10 * l;
//				for (i11 = 1; i11 < j; ++i11) {
//					i2 = i4 + (this.rows - i11) * i1;
//					i3 = i5 + i11 * i1;
//					paramArrayOfFloat[i2] = paramArrayOfFloat[i3];
//					paramArrayOfFloat[(i2 + 1)] = (-paramArrayOfFloat[(i3 + 1)]);
//				}
//			}
//		}
//		for (int i10 = 1; i10 < k; ++i10) {
//			i2 = i10 * l;
//			i3 = (this.slices - i10) * l;
//			i4 = j * i1;
//			i5 = i2 + i4;
//			i6 = i3 + i4;
//			paramArrayOfFloat[(i2 + this.columns)] = paramArrayOfFloat[(i3 + 1)];
//			paramArrayOfFloat[(i3 + this.columns)] = paramArrayOfFloat[(i3 + 1)];
//			paramArrayOfFloat[(i2 + this.columns + 1)] = (-paramArrayOfFloat[i3]);
//			paramArrayOfFloat[(i3 + this.columns + 1)] = paramArrayOfFloat[i3];
//			paramArrayOfFloat[(i5 + this.columns)] = paramArrayOfFloat[(i6 + 1)];
//			paramArrayOfFloat[(i6 + this.columns)] = paramArrayOfFloat[(i6 + 1)];
//			paramArrayOfFloat[(i5 + this.columns + 1)] = (-paramArrayOfFloat[i6]);
//			paramArrayOfFloat[(i6 + this.columns + 1)] = paramArrayOfFloat[i6];
//			paramArrayOfFloat[i3] = paramArrayOfFloat[i2];
//			paramArrayOfFloat[(i3 + 1)] = (-paramArrayOfFloat[(i2 + 1)]);
//			paramArrayOfFloat[i6] = paramArrayOfFloat[i5];
//			paramArrayOfFloat[(i6 + 1)] = (-paramArrayOfFloat[(i5 + 1)]);
//		}
//		paramArrayOfFloat[this.columns] = paramArrayOfFloat[1];
//		paramArrayOfFloat[1] = 0.0F;
//		int i2 = j * i1;
//		int i3 = k * l;
//		int i4 = i2 + i3;
//		paramArrayOfFloat[(i2 + this.columns)] = paramArrayOfFloat[(i2 + 1)];
//		paramArrayOfFloat[(i2 + 1)] = 0.0F;
//		paramArrayOfFloat[(i3 + this.columns)] = paramArrayOfFloat[(i3 + 1)];
//		paramArrayOfFloat[(i3 + 1)] = 0.0F;
//		paramArrayOfFloat[(i4 + this.columns)] = paramArrayOfFloat[(i4 + 1)];
//		paramArrayOfFloat[(i4 + 1)] = 0.0F;
//		paramArrayOfFloat[(i3 + this.columns + 1)] = 0.0F;
//		paramArrayOfFloat[(i4 + this.columns + 1)] = 0.0F;
//	}
//}