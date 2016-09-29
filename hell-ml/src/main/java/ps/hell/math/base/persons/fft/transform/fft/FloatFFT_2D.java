package ps.hell.math.base.persons.fft.transform.fft;//package ps.landerbuluse.ml.math.fft.transform.fft;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class FloatFFT_2D {
//	private int rows;
//	private int columns;
//	private float[] t;
//	private FloatFFT_1D fftColumns;
//	private FloatFFT_1D fftRows;
//	private int oldNthreads;
//	private int nt;
//	private boolean isPowerOfTwo = false;
//	private boolean useThreads = false;
//
//	public strictfp FloatFFT_2D(int paramInt1, int paramInt2) {
//		if ((paramInt1 <= 1) || (paramInt2 <= 1))
//			throw new IllegalArgumentException(
//					"rows and columns must be greater than 1");
//		this.rows = paramInt1;
//		this.columns = paramInt2;
//		if (paramInt1 * paramInt2 >= ConcurrencyUtils.getThreadsBeginN_2D())
//			this.useThreads = true;
//		if ((ConcurrencyUtils.isPowerOf2(paramInt1))
//				&& (ConcurrencyUtils.isPowerOf2(paramInt2))) {
//			this.isPowerOfTwo = true;
//			this.oldNthreads = ConcurrencyUtils.getNumberOfThreads();
//			this.nt = (8 * this.oldNthreads * paramInt1);
//			if (2 * paramInt2 == 4 * this.oldNthreads)
//				this.nt >>= 1;
//			else if (2 * paramInt2 < 4 * this.oldNthreads)
//				this.nt >>= 2;
//			this.t = new float[this.nt];
//		}
//		this.fftRows = new FloatFFT_1D(paramInt1);
//		if (paramInt1 == paramInt2)
//			this.fftColumns = this.fftRows;
//		else
//			this.fftColumns = new FloatFFT_1D(paramInt2);
//	}
//
//	public strictfp void complexForward(float[] paramArrayOfFloat) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int j;
//		if (this.isPowerOfTwo) {
//			j = this.columns;
//			this.columns = (2 * this.columns);
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(0, -1, paramArrayOfFloat, true);
//				cdft2d_subth(-1, paramArrayOfFloat, true);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.fftColumns.complexForward(paramArrayOfFloat, k
//							* this.columns);
//				cdft2d_sub(-1, paramArrayOfFloat, true);
//			}
//			this.columns = j;
//		} else {
//			j = 2 * this.columns;
//			int i1;
//			int i2;
//			int i3;
//			int i4;
//			if ((i > 1) && (this.useThreads) && (this.rows >= i)
//					&& (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				i1 = this.rows / i;
//				for (i2 = 0; i2 < i; ++i2) {
//					i3 = i2 * i1;
//					i4 = (i2 == i - 1) ? this.rows : i3 + i1;
//					arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(
//							i3, i4, paramArrayOfFloat, j) {
//						public strictfp void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								FloatFFT_2D.this.fftColumns.complexForward(
//										this.val$a, i * this.val$rowStride);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				i1 = this.columns / i;
//				for (i2 = 0; i2 < i; ++i2) {
//					i3 = i2 * i1;
//					i4 = (i2 == i - 1) ? this.columns : i3 + i1;
//					arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(
//							i3, i4, j, paramArrayOfFloat) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								int j = 2 * i;
//								int l;
//								int i1;
//								for (int k = 0; k < FloatFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									i1 = k * this.val$rowStride + j;
//									arrayOfFloat[l] = this.val$a[i1];
//									arrayOfFloat[(l + 1)] = this.val$a[(i1 + 1)];
//								}
//								FloatFFT_2D.this.fftRows
//										.complexForward(arrayOfFloat);
//								for (k = 0; k < FloatFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									i1 = k * this.val$rowStride + j;
//									this.val$a[i1] = arrayOfFloat[l];
//									this.val$a[(i1 + 1)] = arrayOfFloat[(l + 1)];
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int l = 0; l < this.rows; ++l)
//					this.fftColumns.complexForward(paramArrayOfFloat, l * j);
//				float[] arrayOfFloat = new float[2 * this.rows];
//				for (i1 = 0; i1 < this.columns; ++i1) {
//					i2 = 2 * i1;
//					int i5;
//					for (i3 = 0; i3 < this.rows; ++i3) {
//						i4 = 2 * i3;
//						i5 = i3 * j + i2;
//						arrayOfFloat[i4] = paramArrayOfFloat[i5];
//						arrayOfFloat[(i4 + 1)] = paramArrayOfFloat[(i5 + 1)];
//					}
//					this.fftRows.complexForward(arrayOfFloat);
//					for (i3 = 0; i3 < this.rows; ++i3) {
//						i4 = 2 * i3;
//						i5 = i3 * j + i2;
//						paramArrayOfFloat[i5] = arrayOfFloat[i4];
//						paramArrayOfFloat[(i5 + 1)] = arrayOfFloat[(i4 + 1)];
//					}
//				}
//			}
//		}
//	}
//
//	public strictfp void complexForward(float[][] paramArrayOfFloat) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int l;
//		if (this.isPowerOfTwo) {
//			int j = this.columns;
//			this.columns = (2 * this.columns);
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(0, -1, paramArrayOfFloat, true);
//				cdft2d_subth(-1, paramArrayOfFloat, true);
//			} else {
//				for (l = 0; l < this.rows; ++l)
//					this.fftColumns.complexForward(paramArrayOfFloat[l]);
//				cdft2d_sub(-1, paramArrayOfFloat, true);
//			}
//			this.columns = j;
//		} else {
//			int i1;
//			int i2;
//			int i3;
//			if ((i > 1) && (this.useThreads) && (this.rows >= i)
//					&& (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				l = this.rows / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.rows : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat) {
//						public strictfp void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								FloatFFT_2D.this.fftColumns
//										.complexForward(this.val$a[i]);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				l = this.columns / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.columns : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								int j = 2 * i;
//								int l;
//								for (int k = 0; k < FloatFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									arrayOfFloat[l] = this.val$a[k][j];
//									arrayOfFloat[(l + 1)] = this.val$a[k][(j + 1)];
//								}
//								FloatFFT_2D.this.fftRows
//										.complexForward(arrayOfFloat);
//								for (k = 0; k < FloatFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									this.val$a[k][j] = arrayOfFloat[l];
//									this.val$a[k][(j + 1)] = arrayOfFloat[(l + 1)];
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.fftColumns.complexForward(paramArrayOfFloat[k]);
//				float[] arrayOfFloat = new float[2 * this.rows];
//				for (l = 0; l < this.columns; ++l) {
//					i1 = 2 * l;
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i3 = 2 * i2;
//						arrayOfFloat[i3] = paramArrayOfFloat[i2][i1];
//						arrayOfFloat[(i3 + 1)] = paramArrayOfFloat[i2][(i1 + 1)];
//					}
//					this.fftRows.complexForward(arrayOfFloat);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i3 = 2 * i2;
//						paramArrayOfFloat[i2][i1] = arrayOfFloat[i3];
//						paramArrayOfFloat[i2][(i1 + 1)] = arrayOfFloat[(i3 + 1)];
//					}
//				}
//			}
//		}
//	}
//
//	public strictfp void complexInverse(float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int j;
//		if (this.isPowerOfTwo) {
//			j = this.columns;
//			this.columns = (2 * this.columns);
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(0, 1, paramArrayOfFloat, paramBoolean);
//				cdft2d_subth(1, paramArrayOfFloat, paramBoolean);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.fftColumns.complexInverse(paramArrayOfFloat, k
//							* this.columns, paramBoolean);
//				cdft2d_sub(1, paramArrayOfFloat, paramBoolean);
//			}
//			this.columns = j;
//		} else {
//			j = 2 * this.columns;
//			int i1;
//			int i2;
//			int i3;
//			int i4;
//			if ((i > 1) && (this.useThreads) && (this.rows >= i)
//					&& (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				i1 = this.rows / i;
//				for (i2 = 0; i2 < i; ++i2) {
//					i3 = i2 * i1;
//					i4 = (i2 == i - 1) ? this.rows : i3 + i1;
//					arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(
//							i3, i4, paramArrayOfFloat, j, paramBoolean) {
//						public strictfp void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								FloatFFT_2D.this.fftColumns.complexInverse(
//										this.val$a, i * this.val$rowspan,
//										this.val$scale);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				i1 = this.columns / i;
//				for (i2 = 0; i2 < i; ++i2) {
//					i3 = i2 * i1;
//					i4 = (i2 == i - 1) ? this.columns : i3 + i1;
//					arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(
//							i3, i4, j, paramArrayOfFloat, paramBoolean) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								int j = 2 * i;
//								int l;
//								int i1;
//								for (int k = 0; k < FloatFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									i1 = k * this.val$rowspan + j;
//									arrayOfFloat[l] = this.val$a[i1];
//									arrayOfFloat[(l + 1)] = this.val$a[(i1 + 1)];
//								}
//								FloatFFT_2D.this.fftRows.complexInverse(
//										arrayOfFloat, this.val$scale);
//								for (k = 0; k < FloatFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									i1 = k * this.val$rowspan + j;
//									this.val$a[i1] = arrayOfFloat[l];
//									this.val$a[(i1 + 1)] = arrayOfFloat[(l + 1)];
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int l = 0; l < this.rows; ++l)
//					this.fftColumns.complexInverse(paramArrayOfFloat, l * j,
//							paramBoolean);
//				float[] arrayOfFloat = new float[2 * this.rows];
//				for (i1 = 0; i1 < this.columns; ++i1) {
//					i2 = 2 * i1;
//					int i5;
//					for (i3 = 0; i3 < this.rows; ++i3) {
//						i4 = 2 * i3;
//						i5 = i3 * j + i2;
//						arrayOfFloat[i4] = paramArrayOfFloat[i5];
//						arrayOfFloat[(i4 + 1)] = paramArrayOfFloat[(i5 + 1)];
//					}
//					this.fftRows.complexInverse(arrayOfFloat, paramBoolean);
//					for (i3 = 0; i3 < this.rows; ++i3) {
//						i4 = 2 * i3;
//						i5 = i3 * j + i2;
//						paramArrayOfFloat[i5] = arrayOfFloat[i4];
//						paramArrayOfFloat[(i5 + 1)] = arrayOfFloat[(i4 + 1)];
//					}
//				}
//			}
//		}
//	}
//
//	public strictfp void complexInverse(float[][] paramArrayOfFloat,
//			boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int l;
//		if (this.isPowerOfTwo) {
//			int j = this.columns;
//			this.columns = (2 * this.columns);
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(0, 1, paramArrayOfFloat, paramBoolean);
//				cdft2d_subth(1, paramArrayOfFloat, paramBoolean);
//			} else {
//				for (l = 0; l < this.rows; ++l)
//					this.fftColumns.complexInverse(paramArrayOfFloat[l],
//							paramBoolean);
//				cdft2d_sub(1, paramArrayOfFloat, paramBoolean);
//			}
//			this.columns = j;
//		} else {
//			int i1;
//			int i2;
//			int i3;
//			if ((i > 1) && (this.useThreads) && (this.rows >= i)
//					&& (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				l = this.rows / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.rows : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat, paramBoolean) {
//						public strictfp void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								FloatFFT_2D.this.fftColumns.complexInverse(
//										this.val$a[i], this.val$scale);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				l = this.columns / i;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.columns : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfFloat, paramBoolean) {
//						public strictfp void run() {
//							float[] arrayOfFloat = new float[2 * FloatFFT_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								int j = 2 * i;
//								int l;
//								for (int k = 0; k < FloatFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									arrayOfFloat[l] = this.val$a[k][j];
//									arrayOfFloat[(l + 1)] = this.val$a[k][(j + 1)];
//								}
//								FloatFFT_2D.this.fftRows.complexInverse(
//										arrayOfFloat, this.val$scale);
//								for (k = 0; k < FloatFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									this.val$a[k][j] = arrayOfFloat[l];
//									this.val$a[k][(j + 1)] = arrayOfFloat[(l + 1)];
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.fftColumns.complexInverse(paramArrayOfFloat[k],
//							paramBoolean);
//				float[] arrayOfFloat = new float[2 * this.rows];
//				for (l = 0; l < this.columns; ++l) {
//					i1 = 2 * l;
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i3 = 2 * i2;
//						arrayOfFloat[i3] = paramArrayOfFloat[i2][i1];
//						arrayOfFloat[(i3 + 1)] = paramArrayOfFloat[i2][(i1 + 1)];
//					}
//					this.fftRows.complexInverse(arrayOfFloat, paramBoolean);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i3 = 2 * i2;
//						paramArrayOfFloat[i2][i1] = arrayOfFloat[i3];
//						paramArrayOfFloat[i2][(i1 + 1)] = arrayOfFloat[(i3 + 1)];
//					}
//				}
//			}
//		}
//	}
//
//	public strictfp void realForward(float[] paramArrayOfFloat) {
//		if (!(this.isPowerOfTwo))
//			throw new IllegalArgumentException(
//					"rows and columns must be power of two numbers");
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (i != this.oldNthreads) {
//			this.nt = (8 * i * this.rows);
//			if (this.columns == 4 * i)
//				this.nt >>= 1;
//			else if (this.columns < 4 * i)
//				this.nt >>= 2;
//			this.t = new float[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			xdft2d0_subth1(1, 1, paramArrayOfFloat, true);
//			cdft2d_subth(-1, paramArrayOfFloat, true);
//			rdft2d_sub(1, paramArrayOfFloat);
//		} else {
//			for (int j = 0; j < this.rows; ++j)
//				this.fftColumns
//						.realForward(paramArrayOfFloat, j * this.columns);
//			cdft2d_sub(-1, paramArrayOfFloat, true);
//			rdft2d_sub(1, paramArrayOfFloat);
//		}
//	}
//
//	public strictfp void realForward(float[][] paramArrayOfFloat) {
//		if (!(this.isPowerOfTwo))
//			throw new IllegalArgumentException(
//					"rows and columns must be power of two numbers");
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (i != this.oldNthreads) {
//			this.nt = (8 * i * this.rows);
//			if (this.columns == 4 * i)
//				this.nt >>= 1;
//			else if (this.columns < 4 * i)
//				this.nt >>= 2;
//			this.t = new float[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			xdft2d0_subth1(1, 1, paramArrayOfFloat, true);
//			cdft2d_subth(-1, paramArrayOfFloat, true);
//			rdft2d_sub(1, paramArrayOfFloat);
//		} else {
//			for (int j = 0; j < this.rows; ++j)
//				this.fftColumns.realForward(paramArrayOfFloat[j]);
//			cdft2d_sub(-1, paramArrayOfFloat, true);
//			rdft2d_sub(1, paramArrayOfFloat);
//		}
//	}
//
//	public strictfp void realForwardFull(float[] paramArrayOfFloat) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(1, 1, paramArrayOfFloat, true);
//				cdft2d_subth(-1, paramArrayOfFloat, true);
//				rdft2d_sub(1, paramArrayOfFloat);
//			} else {
//				for (int j = 0; j < this.rows; ++j)
//					this.fftColumns.realForward(paramArrayOfFloat, j
//							* this.columns);
//				cdft2d_sub(-1, paramArrayOfFloat, true);
//				rdft2d_sub(1, paramArrayOfFloat);
//			}
//			fillSymmetric(paramArrayOfFloat);
//		} else {
//			mixedRadixRealForwardFull(paramArrayOfFloat);
//		}
//	}
//
//	public strictfp void realForwardFull(float[][] paramArrayOfFloat) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(1, 1, paramArrayOfFloat, true);
//				cdft2d_subth(-1, paramArrayOfFloat, true);
//				rdft2d_sub(1, paramArrayOfFloat);
//			} else {
//				for (int j = 0; j < this.rows; ++j)
//					this.fftColumns.realForward(paramArrayOfFloat[j]);
//				cdft2d_sub(-1, paramArrayOfFloat, true);
//				rdft2d_sub(1, paramArrayOfFloat);
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
//					"rows and columns must be power of two numbers");
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (i != this.oldNthreads) {
//			this.nt = (8 * i * this.rows);
//			if (this.columns == 4 * i)
//				this.nt >>= 1;
//			else if (this.columns < 4 * i)
//				this.nt >>= 2;
//			this.t = new float[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			rdft2d_sub(-1, paramArrayOfFloat);
//			cdft2d_subth(1, paramArrayOfFloat, paramBoolean);
//			xdft2d0_subth1(1, -1, paramArrayOfFloat, paramBoolean);
//		} else {
//			rdft2d_sub(-1, paramArrayOfFloat);
//			cdft2d_sub(1, paramArrayOfFloat, paramBoolean);
//			for (int j = 0; j < this.rows; ++j)
//				this.fftColumns.realInverse(paramArrayOfFloat,
//						j * this.columns, paramBoolean);
//		}
//	}
//
//	public strictfp void realInverse(float[][] paramArrayOfFloat,
//			boolean paramBoolean) {
//		if (!(this.isPowerOfTwo))
//			throw new IllegalArgumentException(
//					"rows and columns must be power of two numbers");
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (i != this.oldNthreads) {
//			this.nt = (8 * i * this.rows);
//			if (this.columns == 4 * i)
//				this.nt >>= 1;
//			else if (this.columns < 4 * i)
//				this.nt >>= 2;
//			this.t = new float[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			rdft2d_sub(-1, paramArrayOfFloat);
//			cdft2d_subth(1, paramArrayOfFloat, paramBoolean);
//			xdft2d0_subth1(1, -1, paramArrayOfFloat, paramBoolean);
//		} else {
//			rdft2d_sub(-1, paramArrayOfFloat);
//			cdft2d_sub(1, paramArrayOfFloat, paramBoolean);
//			for (int j = 0; j < this.rows; ++j)
//				this.fftColumns.realInverse(paramArrayOfFloat[j], paramBoolean);
//		}
//	}
//
//	public strictfp void realInverseFull(float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth2(1, -1, paramArrayOfFloat, paramBoolean);
//				cdft2d_subth(1, paramArrayOfFloat, paramBoolean);
//				rdft2d_sub(1, paramArrayOfFloat);
//			} else {
//				for (int j = 0; j < this.rows; ++j)
//					this.fftColumns.realInverse2(paramArrayOfFloat, j
//							* this.columns, paramBoolean);
//				cdft2d_sub(1, paramArrayOfFloat, paramBoolean);
//				rdft2d_sub(1, paramArrayOfFloat);
//			}
//			fillSymmetric(paramArrayOfFloat);
//		} else {
//			mixedRadixRealInverseFull(paramArrayOfFloat, paramBoolean);
//		}
//	}
//
//	public strictfp void realInverseFull(float[][] paramArrayOfFloat,
//			boolean paramBoolean) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth2(1, -1, paramArrayOfFloat, paramBoolean);
//				cdft2d_subth(1, paramArrayOfFloat, paramBoolean);
//				rdft2d_sub(1, paramArrayOfFloat);
//			} else {
//				for (int j = 0; j < this.rows; ++j)
//					this.fftColumns.realInverse2(paramArrayOfFloat[j], 0,
//							paramBoolean);
//				cdft2d_sub(1, paramArrayOfFloat, paramBoolean);
//				rdft2d_sub(1, paramArrayOfFloat);
//			}
//			fillSymmetric(paramArrayOfFloat);
//		} else {
//			mixedRadixRealInverseFull(paramArrayOfFloat, paramBoolean);
//		}
//	}
//
//	private strictfp void mixedRadixRealForwardFull(float[][] paramArrayOfFloat) {
//		int i = this.columns / 2 + 1;
//		float[][] arrayOfFloat = new float[i][2 * this.rows];
//		int j = ConcurrencyUtils.getNumberOfThreads();
//		int l;
//		int i1;
//		int i2;
//		int i3;
//		if ((j > 1) && (this.useThreads) && (this.rows >= j) && (i >= j)) {
//			Future[] arrayOfFuture = new Future[j];
//			l = this.rows / j;
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = i1 * l;
//				i3 = (i1 == j - 1) ? this.rows : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//							FloatFFT_2D.this.fftColumns
//									.realForward(this.val$a[i]);
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i1 = 0; i1 < this.rows; ++i1)
//				arrayOfFloat[0][i1] = paramArrayOfFloat[i1][0];
//			this.fftRows.realForwardFull(arrayOfFloat[0]);
//			l = i / j;
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = 1 + i1 * l;
//				i3 = (i1 == j - 1) ? i - 1 : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, arrayOfFloat, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < FloatFFT_2D.this.rows; ++k) {
//								int l = 2 * k;
//								this.val$temp[i][l] = this.val$a[k][j];
//								this.val$temp[i][(l + 1)] = this.val$a[k][(j + 1)];
//							}
//							FloatFFT_2D.this.fftRows
//									.complexForward(this.val$temp[i]);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			if (this.columns % 2 == 0) {
//				for (i1 = 0; i1 < this.rows; ++i1)
//					arrayOfFloat[(i - 1)][i1] = paramArrayOfFloat[i1][1];
//				this.fftRows.realForwardFull(arrayOfFloat[(i - 1)]);
//			} else {
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i2 = 2 * i1;
//					i3 = i - 1;
//					arrayOfFloat[i3][i2] = paramArrayOfFloat[i1][(2 * i3)];
//					arrayOfFloat[i3][(i2 + 1)] = paramArrayOfFloat[i1][1];
//				}
//				this.fftRows.complexForward(arrayOfFloat[(i - 1)]);
//			}
//			l = this.rows / j;
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = i1 * l;
//				i3 = (i1 == j - 1) ? this.rows : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, i, paramArrayOfFloat, arrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < this.val$n2d2; ++k) {
//								int l = 2 * k;
//								this.val$a[i][l] = this.val$temp[k][j];
//								this.val$a[i][(l + 1)] = this.val$temp[k][(j + 1)];
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = 1 + i1 * l;
//				i3 = (i1 == j - 1) ? this.rows : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, i, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = FloatFFT_2D.this.rows - i;
//							for (int k = this.val$n2d2; k < FloatFFT_2D.this.columns; ++k) {
//								int l = 2 * k;
//								int i1 = 2 * (FloatFFT_2D.this.columns - k);
//								this.val$a[0][l] = this.val$a[0][i1];
//								this.val$a[0][(l + 1)] = (-this.val$a[0][(i1 + 1)]);
//								this.val$a[i][l] = this.val$a[j][i1];
//								this.val$a[i][(l + 1)] = (-this.val$a[j][(i1 + 1)]);
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int k = 0; k < this.rows; ++k)
//				this.fftColumns.realForward(paramArrayOfFloat[k]);
//			for (k = 0; k < this.rows; ++k)
//				arrayOfFloat[0][k] = paramArrayOfFloat[k][0];
//			this.fftRows.realForwardFull(arrayOfFloat[0]);
//			for (k = 1; k < i - 1; ++k) {
//				l = 2 * k;
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i2 = 2 * i1;
//					arrayOfFloat[k][i2] = paramArrayOfFloat[i1][l];
//					arrayOfFloat[k][(i2 + 1)] = paramArrayOfFloat[i1][(l + 1)];
//				}
//				this.fftRows.complexForward(arrayOfFloat[k]);
//			}
//			if (this.columns % 2 == 0) {
//				for (k = 0; k < this.rows; ++k)
//					arrayOfFloat[(i - 1)][k] = paramArrayOfFloat[k][1];
//				this.fftRows.realForwardFull(arrayOfFloat[(i - 1)]);
//			} else {
//				for (k = 0; k < this.rows; ++k) {
//					l = 2 * k;
//					i1 = i - 1;
//					arrayOfFloat[i1][l] = paramArrayOfFloat[k][(2 * i1)];
//					arrayOfFloat[i1][(l + 1)] = paramArrayOfFloat[k][1];
//				}
//				this.fftRows.complexForward(arrayOfFloat[(i - 1)]);
//			}
//			for (k = 0; k < this.rows; ++k) {
//				l = 2 * k;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = 2 * i1;
//					paramArrayOfFloat[k][i2] = arrayOfFloat[i1][l];
//					paramArrayOfFloat[k][(i2 + 1)] = arrayOfFloat[i1][(l + 1)];
//				}
//			}
//			for (k = 1; k < this.rows; ++k) {
//				l = this.rows - k;
//				for (i1 = i; i1 < this.columns; ++i1) {
//					i2 = 2 * i1;
//					i3 = 2 * (this.columns - i1);
//					paramArrayOfFloat[0][i2] = paramArrayOfFloat[0][i3];
//					paramArrayOfFloat[0][(i2 + 1)] = (-paramArrayOfFloat[0][(i3 + 1)]);
//					paramArrayOfFloat[k][i2] = paramArrayOfFloat[l][i3];
//					paramArrayOfFloat[k][(i2 + 1)] = (-paramArrayOfFloat[l][(i3 + 1)]);
//				}
//			}
//		}
//	}
//
//	private strictfp void mixedRadixRealForwardFull(float[] paramArrayOfFloat) {
//		int i = 2 * this.columns;
//		int j = this.columns / 2 + 1;
//		float[][] arrayOfFloat = new float[j][2 * this.rows];
//		int k = ConcurrencyUtils.getNumberOfThreads();
//		int i1;
//		int i2;
//		int i3;
//		int i4;
//		int i5;
//		if ((k > 1) && (this.useThreads) && (this.rows >= k) && (j >= k)) {
//			Future[] arrayOfFuture = new Future[k];
//			i1 = this.rows / k;
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = i2 * i1;
//				i4 = (i2 == k - 1) ? this.rows : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//							FloatFFT_2D.this.fftColumns.realForward(this.val$a,
//									i * FloatFFT_2D.this.columns);
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i2 = 0; i2 < this.rows; ++i2)
//				arrayOfFloat[0][i2] = paramArrayOfFloat[(i2 * this.columns)];
//			this.fftRows.realForwardFull(arrayOfFloat[0]);
//			i1 = j / k;
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = 1 + i2 * i1;
//				i4 = (i2 == k - 1) ? j - 1 : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, arrayOfFloat, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < FloatFFT_2D.this.rows; ++k) {
//								int l = 2 * k;
//								int i1 = k * FloatFFT_2D.this.columns + j;
//								this.val$temp[i][l] = this.val$a[i1];
//								this.val$temp[i][(l + 1)] = this.val$a[(i1 + 1)];
//							}
//							FloatFFT_2D.this.fftRows
//									.complexForward(this.val$temp[i]);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			if (this.columns % 2 == 0) {
//				for (i2 = 0; i2 < this.rows; ++i2)
//					arrayOfFloat[(j - 1)][i2] = paramArrayOfFloat[(i2
//							* this.columns + 1)];
//				this.fftRows.realForwardFull(arrayOfFloat[(j - 1)]);
//			} else {
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i3 = 2 * i2;
//					i4 = i2 * this.columns;
//					i5 = j - 1;
//					arrayOfFloat[i5][i3] = paramArrayOfFloat[(i4 + 2 * i5)];
//					arrayOfFloat[i5][(i3 + 1)] = paramArrayOfFloat[(i4 + 1)];
//				}
//				this.fftRows.complexForward(arrayOfFloat[(j - 1)]);
//			}
//			i1 = this.rows / k;
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = i2 * i1;
//				i4 = (i2 == k - 1) ? this.rows : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, j, i, paramArrayOfFloat, arrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < this.val$n2d2; ++k) {
//								int l = 2 * k;
//								int i1 = i * this.val$rowStride + l;
//								this.val$a[i1] = this.val$temp[k][j];
//								this.val$a[(i1 + 1)] = this.val$temp[k][(j + 1)];
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = 1 + i2 * i1;
//				i4 = (i2 == k - 1) ? this.rows : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, i, j, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = i * this.val$rowStride;
//							int k = (FloatFFT_2D.this.rows - i + 1)
//									* this.val$rowStride;
//							for (int l = this.val$n2d2; l < FloatFFT_2D.this.columns; ++l) {
//								int i1 = 2 * l;
//								int i2 = 2 * (FloatFFT_2D.this.columns - l);
//								this.val$a[i1] = this.val$a[i2];
//								this.val$a[(i1 + 1)] = (-this.val$a[(i2 + 1)]);
//								int i3 = j + i1;
//								int i4 = k - i1;
//								this.val$a[i3] = this.val$a[i4];
//								this.val$a[(i3 + 1)] = (-this.val$a[(i4 + 1)]);
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int l = 0; l < this.rows; ++l)
//				this.fftColumns
//						.realForward(paramArrayOfFloat, l * this.columns);
//			for (l = 0; l < this.rows; ++l)
//				arrayOfFloat[0][l] = paramArrayOfFloat[(l * this.columns)];
//			this.fftRows.realForwardFull(arrayOfFloat[0]);
//			for (l = 1; l < j - 1; ++l) {
//				i1 = 2 * l;
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i3 = 2 * i2;
//					i4 = i2 * this.columns + i1;
//					arrayOfFloat[l][i3] = paramArrayOfFloat[i4];
//					arrayOfFloat[l][(i3 + 1)] = paramArrayOfFloat[(i4 + 1)];
//				}
//				this.fftRows.complexForward(arrayOfFloat[l]);
//			}
//			if (this.columns % 2 == 0) {
//				for (l = 0; l < this.rows; ++l)
//					arrayOfFloat[(j - 1)][l] = paramArrayOfFloat[(l
//							* this.columns + 1)];
//				this.fftRows.realForwardFull(arrayOfFloat[(j - 1)]);
//			} else {
//				for (l = 0; l < this.rows; ++l) {
//					i1 = 2 * l;
//					i2 = l * this.columns;
//					i3 = j - 1;
//					arrayOfFloat[i3][i1] = paramArrayOfFloat[(i2 + 2 * i3)];
//					arrayOfFloat[i3][(i1 + 1)] = paramArrayOfFloat[(i2 + 1)];
//				}
//				this.fftRows.complexForward(arrayOfFloat[(j - 1)]);
//			}
//			for (l = 0; l < this.rows; ++l) {
//				i1 = 2 * l;
//				for (i2 = 0; i2 < j; ++i2) {
//					i3 = 2 * i2;
//					i4 = l * i + i3;
//					paramArrayOfFloat[i4] = arrayOfFloat[i2][i1];
//					paramArrayOfFloat[(i4 + 1)] = arrayOfFloat[i2][(i1 + 1)];
//				}
//			}
//			for (l = 1; l < this.rows; ++l) {
//				i1 = l * i;
//				i2 = (this.rows - l + 1) * i;
//				for (i3 = j; i3 < this.columns; ++i3) {
//					i4 = 2 * i3;
//					i5 = 2 * (this.columns - i3);
//					paramArrayOfFloat[i4] = paramArrayOfFloat[i5];
//					paramArrayOfFloat[(i4 + 1)] = (-paramArrayOfFloat[(i5 + 1)]);
//					int i6 = i1 + i4;
//					int i7 = i2 - i4;
//					paramArrayOfFloat[i6] = paramArrayOfFloat[i7];
//					paramArrayOfFloat[(i6 + 1)] = (-paramArrayOfFloat[(i7 + 1)]);
//				}
//			}
//		}
//	}
//
//	private strictfp void mixedRadixRealInverseFull(
//			float[][] paramArrayOfFloat, boolean paramBoolean) {
//		int i = this.columns / 2 + 1;
//		float[][] arrayOfFloat = new float[i][2 * this.rows];
//		int j = ConcurrencyUtils.getNumberOfThreads();
//		int l;
//		int i1;
//		int i2;
//		int i3;
//		if ((j > 1) && (this.useThreads) && (this.rows >= j) && (i >= j)) {
//			Future[] arrayOfFuture = new Future[j];
//			l = this.rows / j;
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = i1 * l;
//				i3 = (i1 == j - 1) ? this.rows : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, paramArrayOfFloat, paramBoolean) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//							FloatFFT_2D.this.fftColumns.realInverse2(
//									this.val$a[i], 0, this.val$scale);
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i1 = 0; i1 < this.rows; ++i1)
//				arrayOfFloat[0][i1] = paramArrayOfFloat[i1][0];
//			this.fftRows.realInverseFull(arrayOfFloat[0], paramBoolean);
//			l = i / j;
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = 1 + i1 * l;
//				i3 = (i1 == j - 1) ? i - 1 : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, arrayOfFloat, paramArrayOfFloat, paramBoolean) {
//					public strictfp void run() {
//						for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < FloatFFT_2D.this.rows; ++k) {
//								int l = 2 * k;
//								this.val$temp[i][l] = this.val$a[k][j];
//								this.val$temp[i][(l + 1)] = this.val$a[k][(j + 1)];
//							}
//							FloatFFT_2D.this.fftRows.complexInverse(
//									this.val$temp[i], this.val$scale);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			if (this.columns % 2 == 0) {
//				for (i1 = 0; i1 < this.rows; ++i1)
//					arrayOfFloat[(i - 1)][i1] = paramArrayOfFloat[i1][1];
//				this.fftRows.realInverseFull(arrayOfFloat[(i - 1)],
//						paramBoolean);
//			} else {
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i2 = 2 * i1;
//					i3 = i - 1;
//					arrayOfFloat[i3][i2] = paramArrayOfFloat[i1][(2 * i3)];
//					arrayOfFloat[i3][(i2 + 1)] = paramArrayOfFloat[i1][1];
//				}
//				this.fftRows
//						.complexInverse(arrayOfFloat[(i - 1)], paramBoolean);
//			}
//			l = this.rows / j;
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = i1 * l;
//				i3 = (i1 == j - 1) ? this.rows : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, i, paramArrayOfFloat, arrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < this.val$n2d2; ++k) {
//								int l = 2 * k;
//								this.val$a[i][l] = this.val$temp[k][j];
//								this.val$a[i][(l + 1)] = this.val$temp[k][(j + 1)];
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = 1 + i1 * l;
//				i3 = (i1 == j - 1) ? this.rows : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, i, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = FloatFFT_2D.this.rows - i;
//							for (int k = this.val$n2d2; k < FloatFFT_2D.this.columns; ++k) {
//								int l = 2 * k;
//								int i1 = 2 * (FloatFFT_2D.this.columns - k);
//								this.val$a[0][l] = this.val$a[0][i1];
//								this.val$a[0][(l + 1)] = (-this.val$a[0][(i1 + 1)]);
//								this.val$a[i][l] = this.val$a[j][i1];
//								this.val$a[i][(l + 1)] = (-this.val$a[j][(i1 + 1)]);
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int k = 0; k < this.rows; ++k)
//				this.fftColumns.realInverse2(paramArrayOfFloat[k], 0,
//						paramBoolean);
//			for (k = 0; k < this.rows; ++k)
//				arrayOfFloat[0][k] = paramArrayOfFloat[k][0];
//			this.fftRows.realInverseFull(arrayOfFloat[0], paramBoolean);
//			for (k = 1; k < i - 1; ++k) {
//				l = 2 * k;
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i2 = 2 * i1;
//					arrayOfFloat[k][i2] = paramArrayOfFloat[i1][l];
//					arrayOfFloat[k][(i2 + 1)] = paramArrayOfFloat[i1][(l + 1)];
//				}
//				this.fftRows.complexInverse(arrayOfFloat[k], paramBoolean);
//			}
//			if (this.columns % 2 == 0) {
//				for (k = 0; k < this.rows; ++k)
//					arrayOfFloat[(i - 1)][k] = paramArrayOfFloat[k][1];
//				this.fftRows.realInverseFull(arrayOfFloat[(i - 1)],
//						paramBoolean);
//			} else {
//				for (k = 0; k < this.rows; ++k) {
//					l = 2 * k;
//					i1 = i - 1;
//					arrayOfFloat[i1][l] = paramArrayOfFloat[k][(2 * i1)];
//					arrayOfFloat[i1][(l + 1)] = paramArrayOfFloat[k][1];
//				}
//				this.fftRows
//						.complexInverse(arrayOfFloat[(i - 1)], paramBoolean);
//			}
//			for (k = 0; k < this.rows; ++k) {
//				l = 2 * k;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = 2 * i1;
//					paramArrayOfFloat[k][i2] = arrayOfFloat[i1][l];
//					paramArrayOfFloat[k][(i2 + 1)] = arrayOfFloat[i1][(l + 1)];
//				}
//			}
//			for (k = 1; k < this.rows; ++k) {
//				l = this.rows - k;
//				for (i1 = i; i1 < this.columns; ++i1) {
//					i2 = 2 * i1;
//					i3 = 2 * (this.columns - i1);
//					paramArrayOfFloat[0][i2] = paramArrayOfFloat[0][i3];
//					paramArrayOfFloat[0][(i2 + 1)] = (-paramArrayOfFloat[0][(i3 + 1)]);
//					paramArrayOfFloat[k][i2] = paramArrayOfFloat[l][i3];
//					paramArrayOfFloat[k][(i2 + 1)] = (-paramArrayOfFloat[l][(i3 + 1)]);
//				}
//			}
//		}
//	}
//
//	private strictfp void mixedRadixRealInverseFull(float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		int i = 2 * this.columns;
//		int j = this.columns / 2 + 1;
//		float[][] arrayOfFloat = new float[j][2 * this.rows];
//		int k = ConcurrencyUtils.getNumberOfThreads();
//		int i1;
//		int i2;
//		int i3;
//		int i4;
//		int i5;
//		if ((k > 1) && (this.useThreads) && (this.rows >= k) && (j >= k)) {
//			Future[] arrayOfFuture = new Future[k];
//			i1 = this.rows / k;
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = i2 * i1;
//				i4 = (i2 == k - 1) ? this.rows : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, paramArrayOfFloat, paramBoolean) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//							FloatFFT_2D.this.fftColumns.realInverse2(
//									this.val$a, i * FloatFFT_2D.this.columns,
//									this.val$scale);
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i2 = 0; i2 < this.rows; ++i2)
//				arrayOfFloat[0][i2] = paramArrayOfFloat[(i2 * this.columns)];
//			this.fftRows.realInverseFull(arrayOfFloat[0], paramBoolean);
//			i1 = j / k;
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = 1 + i2 * i1;
//				i4 = (i2 == k - 1) ? j - 1 : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, arrayOfFloat, paramArrayOfFloat, paramBoolean) {
//					public strictfp void run() {
//						for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < FloatFFT_2D.this.rows; ++k) {
//								int l = 2 * k;
//								int i1 = k * FloatFFT_2D.this.columns + j;
//								this.val$temp[i][l] = this.val$a[i1];
//								this.val$temp[i][(l + 1)] = this.val$a[(i1 + 1)];
//							}
//							FloatFFT_2D.this.fftRows.complexInverse(
//									this.val$temp[i], this.val$scale);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			if (this.columns % 2 == 0) {
//				for (i2 = 0; i2 < this.rows; ++i2)
//					arrayOfFloat[(j - 1)][i2] = paramArrayOfFloat[(i2
//							* this.columns + 1)];
//				this.fftRows.realInverseFull(arrayOfFloat[(j - 1)],
//						paramBoolean);
//			} else {
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i3 = 2 * i2;
//					i4 = i2 * this.columns;
//					i5 = j - 1;
//					arrayOfFloat[i5][i3] = paramArrayOfFloat[(i4 + 2 * i5)];
//					arrayOfFloat[i5][(i3 + 1)] = paramArrayOfFloat[(i4 + 1)];
//				}
//				this.fftRows
//						.complexInverse(arrayOfFloat[(j - 1)], paramBoolean);
//			}
//			i1 = this.rows / k;
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = i2 * i1;
//				i4 = (i2 == k - 1) ? this.rows : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, j, i, paramArrayOfFloat, arrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < this.val$n2d2; ++k) {
//								int l = 2 * k;
//								int i1 = i * this.val$rowStride + l;
//								this.val$a[i1] = this.val$temp[k][j];
//								this.val$a[(i1 + 1)] = this.val$temp[k][(j + 1)];
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = 1 + i2 * i1;
//				i4 = (i2 == k - 1) ? this.rows : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, i, j, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = i * this.val$rowStride;
//							int k = (FloatFFT_2D.this.rows - i + 1)
//									* this.val$rowStride;
//							for (int l = this.val$n2d2; l < FloatFFT_2D.this.columns; ++l) {
//								int i1 = 2 * l;
//								int i2 = 2 * (FloatFFT_2D.this.columns - l);
//								this.val$a[i1] = this.val$a[i2];
//								this.val$a[(i1 + 1)] = (-this.val$a[(i2 + 1)]);
//								int i3 = j + i1;
//								int i4 = k - i1;
//								this.val$a[i3] = this.val$a[i4];
//								this.val$a[(i3 + 1)] = (-this.val$a[(i4 + 1)]);
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int l = 0; l < this.rows; ++l)
//				this.fftColumns.realInverse2(paramArrayOfFloat, l
//						* this.columns, paramBoolean);
//			for (l = 0; l < this.rows; ++l)
//				arrayOfFloat[0][l] = paramArrayOfFloat[(l * this.columns)];
//			this.fftRows.realInverseFull(arrayOfFloat[0], paramBoolean);
//			for (l = 1; l < j - 1; ++l) {
//				i1 = 2 * l;
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i3 = 2 * i2;
//					i4 = i2 * this.columns + i1;
//					arrayOfFloat[l][i3] = paramArrayOfFloat[i4];
//					arrayOfFloat[l][(i3 + 1)] = paramArrayOfFloat[(i4 + 1)];
//				}
//				this.fftRows.complexInverse(arrayOfFloat[l], paramBoolean);
//			}
//			if (this.columns % 2 == 0) {
//				for (l = 0; l < this.rows; ++l)
//					arrayOfFloat[(j - 1)][l] = paramArrayOfFloat[(l
//							* this.columns + 1)];
//				this.fftRows.realInverseFull(arrayOfFloat[(j - 1)],
//						paramBoolean);
//			} else {
//				for (l = 0; l < this.rows; ++l) {
//					i1 = 2 * l;
//					i2 = l * this.columns;
//					i3 = j - 1;
//					arrayOfFloat[i3][i1] = paramArrayOfFloat[(i2 + 2 * i3)];
//					arrayOfFloat[i3][(i1 + 1)] = paramArrayOfFloat[(i2 + 1)];
//				}
//				this.fftRows
//						.complexInverse(arrayOfFloat[(j - 1)], paramBoolean);
//			}
//			for (l = 0; l < this.rows; ++l) {
//				i1 = 2 * l;
//				for (i2 = 0; i2 < j; ++i2) {
//					i3 = 2 * i2;
//					i4 = l * i + i3;
//					paramArrayOfFloat[i4] = arrayOfFloat[i2][i1];
//					paramArrayOfFloat[(i4 + 1)] = arrayOfFloat[i2][(i1 + 1)];
//				}
//			}
//			for (l = 1; l < this.rows; ++l) {
//				i1 = l * i;
//				i2 = (this.rows - l + 1) * i;
//				for (i3 = j; i3 < this.columns; ++i3) {
//					i4 = 2 * i3;
//					i5 = 2 * (this.columns - i3);
//					paramArrayOfFloat[i4] = paramArrayOfFloat[i5];
//					paramArrayOfFloat[(i4 + 1)] = (-paramArrayOfFloat[(i5 + 1)]);
//					int i6 = i1 + i4;
//					int i7 = i2 - i4;
//					paramArrayOfFloat[i6] = paramArrayOfFloat[i7];
//					paramArrayOfFloat[(i6 + 1)] = (-paramArrayOfFloat[(i7 + 1)]);
//				}
//			}
//		}
//	}
//
//	private strictfp void rdft2d_sub(int paramInt, float[] paramArrayOfFloat) {
//		int i = this.rows >> 1;
//		int i1;
//		int j;
//		int k;
//		int l;
//		if (paramInt < 0)
//			for (i1 = 1; i1 < i; ++i1) {
//				j = this.rows - i1;
//				k = i1 * this.columns;
//				l = j * this.columns;
//				float f = paramArrayOfFloat[k] - paramArrayOfFloat[l];
//				paramArrayOfFloat[k] += paramArrayOfFloat[l];
//				paramArrayOfFloat[l] = f;
//				f = paramArrayOfFloat[(l + 1)] - paramArrayOfFloat[(k + 1)];
//				paramArrayOfFloat[(k + 1)] += paramArrayOfFloat[(l + 1)];
//				paramArrayOfFloat[(l + 1)] = f;
//			}
//		else
//			for (i1 = 1; i1 < i; ++i1) {
//				j = this.rows - i1;
//				k = i1 * this.columns;
//				l = j * this.columns;
//				paramArrayOfFloat[l] = (0.5F * (paramArrayOfFloat[k] - paramArrayOfFloat[l]));
//				paramArrayOfFloat[k] -= paramArrayOfFloat[l];
//				paramArrayOfFloat[(l + 1)] = (0.5F * (paramArrayOfFloat[(k + 1)] + paramArrayOfFloat[(l + 1)]));
//				paramArrayOfFloat[(k + 1)] -= paramArrayOfFloat[(l + 1)];
//			}
//	}
//
//	private strictfp void rdft2d_sub(int paramInt, float[][] paramArrayOfFloat) {
//		int i = this.rows >> 1;
//		int k;
//		int j;
//		if (paramInt < 0)
//			for (k = 1; k < i; ++k) {
//				j = this.rows - k;
//				float f = paramArrayOfFloat[k][0] - paramArrayOfFloat[j][0];
//				paramArrayOfFloat[k][0] += paramArrayOfFloat[j][0];
//				paramArrayOfFloat[j][0] = f;
//				f = paramArrayOfFloat[j][1] - paramArrayOfFloat[k][1];
//				paramArrayOfFloat[k][1] += paramArrayOfFloat[j][1];
//				paramArrayOfFloat[j][1] = f;
//			}
//		else
//			for (k = 1; k < i; ++k) {
//				j = this.rows - k;
//				paramArrayOfFloat[j][0] = (0.5F * (paramArrayOfFloat[k][0] - paramArrayOfFloat[j][0]));
//				paramArrayOfFloat[k][0] -= paramArrayOfFloat[j][0];
//				paramArrayOfFloat[j][1] = (0.5F * (paramArrayOfFloat[k][1] + paramArrayOfFloat[j][1]));
//				paramArrayOfFloat[k][1] -= paramArrayOfFloat[j][1];
//			}
//	}
//
//	private strictfp void cdft2d_sub(int paramInt, float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		int i2;
//		int i3;
//		int i;
//		int j;
//		int k;
//		int l;
//		int i1;
//		if (paramInt == -1) {
//			if (this.columns > 4) {
//				for (i2 = 0; i2 < this.columns; i2 += 8) {
//					for (i3 = 0; i3 < this.rows; ++i3) {
//						i = i3 * this.columns + i2;
//						j = 2 * i3;
//						k = 2 * this.rows + 2 * i3;
//						l = k + 2 * this.rows;
//						i1 = l + 2 * this.rows;
//						this.t[j] = paramArrayOfFloat[i];
//						this.t[(j + 1)] = paramArrayOfFloat[(i + 1)];
//						this.t[k] = paramArrayOfFloat[(i + 2)];
//						this.t[(k + 1)] = paramArrayOfFloat[(i + 3)];
//						this.t[l] = paramArrayOfFloat[(i + 4)];
//						this.t[(l + 1)] = paramArrayOfFloat[(i + 5)];
//						this.t[i1] = paramArrayOfFloat[(i + 6)];
//						this.t[(i1 + 1)] = paramArrayOfFloat[(i + 7)];
//					}
//					this.fftRows.complexForward(this.t, 0);
//					this.fftRows.complexForward(this.t, 2 * this.rows);
//					this.fftRows.complexForward(this.t, 4 * this.rows);
//					this.fftRows.complexForward(this.t, 6 * this.rows);
//					for (i3 = 0; i3 < this.rows; ++i3) {
//						i = i3 * this.columns + i2;
//						j = 2 * i3;
//						k = 2 * this.rows + 2 * i3;
//						l = k + 2 * this.rows;
//						i1 = l + 2 * this.rows;
//						paramArrayOfFloat[i] = this.t[j];
//						paramArrayOfFloat[(i + 1)] = this.t[(j + 1)];
//						paramArrayOfFloat[(i + 2)] = this.t[k];
//						paramArrayOfFloat[(i + 3)] = this.t[(k + 1)];
//						paramArrayOfFloat[(i + 4)] = this.t[l];
//						paramArrayOfFloat[(i + 5)] = this.t[(l + 1)];
//						paramArrayOfFloat[(i + 6)] = this.t[i1];
//						paramArrayOfFloat[(i + 7)] = this.t[(i1 + 1)];
//					}
//				}
//			} else if (this.columns == 4) {
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i = i2 * this.columns;
//					j = 2 * i2;
//					k = 2 * this.rows + 2 * i2;
//					this.t[j] = paramArrayOfFloat[i];
//					this.t[(j + 1)] = paramArrayOfFloat[(i + 1)];
//					this.t[k] = paramArrayOfFloat[(i + 2)];
//					this.t[(k + 1)] = paramArrayOfFloat[(i + 3)];
//				}
//				this.fftRows.complexForward(this.t, 0);
//				this.fftRows.complexForward(this.t, 2 * this.rows);
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i = i2 * this.columns;
//					j = 2 * i2;
//					k = 2 * this.rows + 2 * i2;
//					paramArrayOfFloat[i] = this.t[j];
//					paramArrayOfFloat[(i + 1)] = this.t[(j + 1)];
//					paramArrayOfFloat[(i + 2)] = this.t[k];
//					paramArrayOfFloat[(i + 3)] = this.t[(k + 1)];
//				}
//			} else {
//				if (this.columns != 2)
//					return;
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i = i2 * this.columns;
//					j = 2 * i2;
//					this.t[j] = paramArrayOfFloat[i];
//					this.t[(j + 1)] = paramArrayOfFloat[(i + 1)];
//				}
//				this.fftRows.complexForward(this.t, 0);
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i = i2 * this.columns;
//					j = 2 * i2;
//					paramArrayOfFloat[i] = this.t[j];
//					paramArrayOfFloat[(i + 1)] = this.t[(j + 1)];
//				}
//			}
//		} else if (this.columns > 4) {
//			for (i2 = 0; i2 < this.columns; i2 += 8) {
//				for (i3 = 0; i3 < this.rows; ++i3) {
//					i = i3 * this.columns + i2;
//					j = 2 * i3;
//					k = 2 * this.rows + 2 * i3;
//					l = k + 2 * this.rows;
//					i1 = l + 2 * this.rows;
//					this.t[j] = paramArrayOfFloat[i];
//					this.t[(j + 1)] = paramArrayOfFloat[(i + 1)];
//					this.t[k] = paramArrayOfFloat[(i + 2)];
//					this.t[(k + 1)] = paramArrayOfFloat[(i + 3)];
//					this.t[l] = paramArrayOfFloat[(i + 4)];
//					this.t[(l + 1)] = paramArrayOfFloat[(i + 5)];
//					this.t[i1] = paramArrayOfFloat[(i + 6)];
//					this.t[(i1 + 1)] = paramArrayOfFloat[(i + 7)];
//				}
//				this.fftRows.complexInverse(this.t, 0, paramBoolean);
//				this.fftRows
//						.complexInverse(this.t, 2 * this.rows, paramBoolean);
//				this.fftRows
//						.complexInverse(this.t, 4 * this.rows, paramBoolean);
//				this.fftRows
//						.complexInverse(this.t, 6 * this.rows, paramBoolean);
//				for (i3 = 0; i3 < this.rows; ++i3) {
//					i = i3 * this.columns + i2;
//					j = 2 * i3;
//					k = 2 * this.rows + 2 * i3;
//					l = k + 2 * this.rows;
//					i1 = l + 2 * this.rows;
//					paramArrayOfFloat[i] = this.t[j];
//					paramArrayOfFloat[(i + 1)] = this.t[(j + 1)];
//					paramArrayOfFloat[(i + 2)] = this.t[k];
//					paramArrayOfFloat[(i + 3)] = this.t[(k + 1)];
//					paramArrayOfFloat[(i + 4)] = this.t[l];
//					paramArrayOfFloat[(i + 5)] = this.t[(l + 1)];
//					paramArrayOfFloat[(i + 6)] = this.t[i1];
//					paramArrayOfFloat[(i + 7)] = this.t[(i1 + 1)];
//				}
//			}
//		} else if (this.columns == 4) {
//			for (i2 = 0; i2 < this.rows; ++i2) {
//				i = i2 * this.columns;
//				j = 2 * i2;
//				k = 2 * this.rows + 2 * i2;
//				this.t[j] = paramArrayOfFloat[i];
//				this.t[(j + 1)] = paramArrayOfFloat[(i + 1)];
//				this.t[k] = paramArrayOfFloat[(i + 2)];
//				this.t[(k + 1)] = paramArrayOfFloat[(i + 3)];
//			}
//			this.fftRows.complexInverse(this.t, 0, paramBoolean);
//			this.fftRows.complexInverse(this.t, 2 * this.rows, paramBoolean);
//			for (i2 = 0; i2 < this.rows; ++i2) {
//				i = i2 * this.columns;
//				j = 2 * i2;
//				k = 2 * this.rows + 2 * i2;
//				paramArrayOfFloat[i] = this.t[j];
//				paramArrayOfFloat[(i + 1)] = this.t[(j + 1)];
//				paramArrayOfFloat[(i + 2)] = this.t[k];
//				paramArrayOfFloat[(i + 3)] = this.t[(k + 1)];
//			}
//		} else {
//			if (this.columns != 2)
//				return;
//			for (i2 = 0; i2 < this.rows; ++i2) {
//				i = i2 * this.columns;
//				j = 2 * i2;
//				this.t[j] = paramArrayOfFloat[i];
//				this.t[(j + 1)] = paramArrayOfFloat[(i + 1)];
//			}
//			this.fftRows.complexInverse(this.t, 0, paramBoolean);
//			for (i2 = 0; i2 < this.rows; ++i2) {
//				i = i2 * this.columns;
//				j = 2 * i2;
//				paramArrayOfFloat[i] = this.t[j];
//				paramArrayOfFloat[(i + 1)] = this.t[(j + 1)];
//			}
//		}
//	}
//
//	private strictfp void cdft2d_sub(int paramInt, float[][] paramArrayOfFloat,
//			boolean paramBoolean) {
//		int i1;
//		int i2;
//		int i;
//		int j;
//		int k;
//		int l;
//		if (paramInt == -1) {
//			if (this.columns > 4) {
//				for (i1 = 0; i1 < this.columns; i1 += 8) {
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.rows + 2 * i2;
//						k = j + 2 * this.rows;
//						l = k + 2 * this.rows;
//						this.t[i] = paramArrayOfFloat[i2][i1];
//						this.t[(i + 1)] = paramArrayOfFloat[i2][(i1 + 1)];
//						this.t[j] = paramArrayOfFloat[i2][(i1 + 2)];
//						this.t[(j + 1)] = paramArrayOfFloat[i2][(i1 + 3)];
//						this.t[k] = paramArrayOfFloat[i2][(i1 + 4)];
//						this.t[(k + 1)] = paramArrayOfFloat[i2][(i1 + 5)];
//						this.t[l] = paramArrayOfFloat[i2][(i1 + 6)];
//						this.t[(l + 1)] = paramArrayOfFloat[i2][(i1 + 7)];
//					}
//					this.fftRows.complexForward(this.t, 0);
//					this.fftRows.complexForward(this.t, 2 * this.rows);
//					this.fftRows.complexForward(this.t, 4 * this.rows);
//					this.fftRows.complexForward(this.t, 6 * this.rows);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i = 2 * i2;
//						j = 2 * this.rows + 2 * i2;
//						k = j + 2 * this.rows;
//						l = k + 2 * this.rows;
//						paramArrayOfFloat[i2][i1] = this.t[i];
//						paramArrayOfFloat[i2][(i1 + 1)] = this.t[(i + 1)];
//						paramArrayOfFloat[i2][(i1 + 2)] = this.t[j];
//						paramArrayOfFloat[i2][(i1 + 3)] = this.t[(j + 1)];
//						paramArrayOfFloat[i2][(i1 + 4)] = this.t[k];
//						paramArrayOfFloat[i2][(i1 + 5)] = this.t[(k + 1)];
//						paramArrayOfFloat[i2][(i1 + 6)] = this.t[l];
//						paramArrayOfFloat[i2][(i1 + 7)] = this.t[(l + 1)];
//					}
//				}
//			} else if (this.columns == 4) {
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i = 2 * i1;
//					j = 2 * this.rows + 2 * i1;
//					this.t[i] = paramArrayOfFloat[i1][0];
//					this.t[(i + 1)] = paramArrayOfFloat[i1][1];
//					this.t[j] = paramArrayOfFloat[i1][2];
//					this.t[(j + 1)] = paramArrayOfFloat[i1][3];
//				}
//				this.fftRows.complexForward(this.t, 0);
//				this.fftRows.complexForward(this.t, 2 * this.rows);
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i = 2 * i1;
//					j = 2 * this.rows + 2 * i1;
//					paramArrayOfFloat[i1][0] = this.t[i];
//					paramArrayOfFloat[i1][1] = this.t[(i + 1)];
//					paramArrayOfFloat[i1][2] = this.t[j];
//					paramArrayOfFloat[i1][3] = this.t[(j + 1)];
//				}
//			} else {
//				if (this.columns != 2)
//					return;
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i = 2 * i1;
//					this.t[i] = paramArrayOfFloat[i1][0];
//					this.t[(i + 1)] = paramArrayOfFloat[i1][1];
//				}
//				this.fftRows.complexForward(this.t, 0);
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i = 2 * i1;
//					paramArrayOfFloat[i1][0] = this.t[i];
//					paramArrayOfFloat[i1][1] = this.t[(i + 1)];
//				}
//			}
//		} else if (this.columns > 4) {
//			for (i1 = 0; i1 < this.columns; i1 += 8) {
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i = 2 * i2;
//					j = 2 * this.rows + 2 * i2;
//					k = j + 2 * this.rows;
//					l = k + 2 * this.rows;
//					this.t[i] = paramArrayOfFloat[i2][i1];
//					this.t[(i + 1)] = paramArrayOfFloat[i2][(i1 + 1)];
//					this.t[j] = paramArrayOfFloat[i2][(i1 + 2)];
//					this.t[(j + 1)] = paramArrayOfFloat[i2][(i1 + 3)];
//					this.t[k] = paramArrayOfFloat[i2][(i1 + 4)];
//					this.t[(k + 1)] = paramArrayOfFloat[i2][(i1 + 5)];
//					this.t[l] = paramArrayOfFloat[i2][(i1 + 6)];
//					this.t[(l + 1)] = paramArrayOfFloat[i2][(i1 + 7)];
//				}
//				this.fftRows.complexInverse(this.t, 0, paramBoolean);
//				this.fftRows
//						.complexInverse(this.t, 2 * this.rows, paramBoolean);
//				this.fftRows
//						.complexInverse(this.t, 4 * this.rows, paramBoolean);
//				this.fftRows
//						.complexInverse(this.t, 6 * this.rows, paramBoolean);
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i = 2 * i2;
//					j = 2 * this.rows + 2 * i2;
//					k = j + 2 * this.rows;
//					l = k + 2 * this.rows;
//					paramArrayOfFloat[i2][i1] = this.t[i];
//					paramArrayOfFloat[i2][(i1 + 1)] = this.t[(i + 1)];
//					paramArrayOfFloat[i2][(i1 + 2)] = this.t[j];
//					paramArrayOfFloat[i2][(i1 + 3)] = this.t[(j + 1)];
//					paramArrayOfFloat[i2][(i1 + 4)] = this.t[k];
//					paramArrayOfFloat[i2][(i1 + 5)] = this.t[(k + 1)];
//					paramArrayOfFloat[i2][(i1 + 6)] = this.t[l];
//					paramArrayOfFloat[i2][(i1 + 7)] = this.t[(l + 1)];
//				}
//			}
//		} else if (this.columns == 4) {
//			for (i1 = 0; i1 < this.rows; ++i1) {
//				i = 2 * i1;
//				j = 2 * this.rows + 2 * i1;
//				this.t[i] = paramArrayOfFloat[i1][0];
//				this.t[(i + 1)] = paramArrayOfFloat[i1][1];
//				this.t[j] = paramArrayOfFloat[i1][2];
//				this.t[(j + 1)] = paramArrayOfFloat[i1][3];
//			}
//			this.fftRows.complexInverse(this.t, 0, paramBoolean);
//			this.fftRows.complexInverse(this.t, 2 * this.rows, paramBoolean);
//			for (i1 = 0; i1 < this.rows; ++i1) {
//				i = 2 * i1;
//				j = 2 * this.rows + 2 * i1;
//				paramArrayOfFloat[i1][0] = this.t[i];
//				paramArrayOfFloat[i1][1] = this.t[(i + 1)];
//				paramArrayOfFloat[i1][2] = this.t[j];
//				paramArrayOfFloat[i1][3] = this.t[(j + 1)];
//			}
//		} else {
//			if (this.columns != 2)
//				return;
//			for (i1 = 0; i1 < this.rows; ++i1) {
//				i = 2 * i1;
//				this.t[i] = paramArrayOfFloat[i1][0];
//				this.t[(i + 1)] = paramArrayOfFloat[i1][1];
//			}
//			this.fftRows.complexInverse(this.t, 0, paramBoolean);
//			for (i1 = 0; i1 < this.rows; ++i1) {
//				i = 2 * i1;
//				paramArrayOfFloat[i1][0] = this.t[i];
//				paramArrayOfFloat[i1][1] = this.t[(i + 1)];
//			}
//		}
//	}
//
//	private strictfp void xdft2d0_subth1(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat, boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		Future[] arrayOfFuture = new Future[i];
//		for (int j = 0; j < i; ++j) {
//			int k = j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt1,
//					paramInt2, k, i, paramArrayOfFloat, paramBoolean) {
//				public strictfp void run() {
//					int i;
//					if (this.val$icr == 0) {
//						if (this.val$isgn == -1) {
//							i = this.val$n0;
//							while (i < FloatFFT_2D.this.rows) {
//								FloatFFT_2D.this.fftColumns.complexForward(
//										this.val$a, i
//												* FloatFFT_2D.this.columns);
//								i += this.val$nthreads;
//							}
//						} else {
//							i = this.val$n0;
//							while (i < FloatFFT_2D.this.rows) {
//								FloatFFT_2D.this.fftColumns.complexInverse(
//										this.val$a, i
//												* FloatFFT_2D.this.columns,
//										this.val$scale);
//								i += this.val$nthreads;
//							}
//						}
//					} else if (this.val$isgn == 1) {
//						i = this.val$n0;
//						while (i < FloatFFT_2D.this.rows) {
//							FloatFFT_2D.this.fftColumns.realForward(this.val$a,
//									i * FloatFFT_2D.this.columns);
//							i += this.val$nthreads;
//						}
//					} else {
//						i = this.val$n0;
//						while (i < FloatFFT_2D.this.rows) {
//							FloatFFT_2D.this.fftColumns.realInverse(this.val$a,
//									i * FloatFFT_2D.this.columns,
//									this.val$scale);
//							i += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void xdft2d0_subth2(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat, boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		Future[] arrayOfFuture = new Future[i];
//		for (int j = 0; j < i; ++j) {
//			int k = j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt1,
//					paramInt2, k, i, paramArrayOfFloat, paramBoolean) {
//				public strictfp void run() {
//					int i;
//					if (this.val$icr == 0) {
//						if (this.val$isgn == -1) {
//							i = this.val$n0;
//							while (i < FloatFFT_2D.this.rows) {
//								FloatFFT_2D.this.fftColumns.complexForward(
//										this.val$a, i
//												* FloatFFT_2D.this.columns);
//								i += this.val$nthreads;
//							}
//						} else {
//							i = this.val$n0;
//							while (i < FloatFFT_2D.this.rows) {
//								FloatFFT_2D.this.fftColumns.complexInverse(
//										this.val$a, i
//												* FloatFFT_2D.this.columns,
//										this.val$scale);
//								i += this.val$nthreads;
//							}
//						}
//					} else if (this.val$isgn == 1) {
//						i = this.val$n0;
//						while (i < FloatFFT_2D.this.rows) {
//							FloatFFT_2D.this.fftColumns.realForward(this.val$a,
//									i * FloatFFT_2D.this.columns);
//							i += this.val$nthreads;
//						}
//					} else {
//						i = this.val$n0;
//						while (i < FloatFFT_2D.this.rows) {
//							FloatFFT_2D.this.fftColumns.realInverse2(
//									this.val$a, i * FloatFFT_2D.this.columns,
//									this.val$scale);
//							i += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void xdft2d0_subth1(int paramInt1, int paramInt2,
//			float[][] paramArrayOfFloat, boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		Future[] arrayOfFuture = new Future[i];
//		for (int j = 0; j < i; ++j) {
//			int k = j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt1,
//					paramInt2, k, i, paramArrayOfFloat, paramBoolean) {
//				public strictfp void run() {
//					int i;
//					if (this.val$icr == 0) {
//						if (this.val$isgn == -1) {
//							i = this.val$n0;
//							while (i < FloatFFT_2D.this.rows) {
//								FloatFFT_2D.this.fftColumns
//										.complexForward(this.val$a[i]);
//								i += this.val$nthreads;
//							}
//						} else {
//							i = this.val$n0;
//							while (i < FloatFFT_2D.this.rows) {
//								FloatFFT_2D.this.fftColumns.complexInverse(
//										this.val$a[i], this.val$scale);
//								i += this.val$nthreads;
//							}
//						}
//					} else if (this.val$isgn == 1) {
//						i = this.val$n0;
//						while (i < FloatFFT_2D.this.rows) {
//							FloatFFT_2D.this.fftColumns
//									.realForward(this.val$a[i]);
//							i += this.val$nthreads;
//						}
//					} else {
//						i = this.val$n0;
//						while (i < FloatFFT_2D.this.rows) {
//							FloatFFT_2D.this.fftColumns.realInverse(
//									this.val$a[i], this.val$scale);
//							i += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void xdft2d0_subth2(int paramInt1, int paramInt2,
//			float[][] paramArrayOfFloat, boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		Future[] arrayOfFuture = new Future[i];
//		for (int j = 0; j < i; ++j) {
//			int k = j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt1,
//					paramInt2, k, i, paramArrayOfFloat, paramBoolean) {
//				public strictfp void run() {
//					int i;
//					if (this.val$icr == 0) {
//						if (this.val$isgn == -1) {
//							i = this.val$n0;
//							while (i < FloatFFT_2D.this.rows) {
//								FloatFFT_2D.this.fftColumns
//										.complexForward(this.val$a[i]);
//								i += this.val$nthreads;
//							}
//						} else {
//							i = this.val$n0;
//							while (i < FloatFFT_2D.this.rows) {
//								FloatFFT_2D.this.fftColumns.complexInverse(
//										this.val$a[i], this.val$scale);
//								i += this.val$nthreads;
//							}
//						}
//					} else if (this.val$isgn == 1) {
//						i = this.val$n0;
//						while (i < FloatFFT_2D.this.rows) {
//							FloatFFT_2D.this.fftColumns
//									.realForward(this.val$a[i]);
//							i += this.val$nthreads;
//						}
//					} else {
//						i = this.val$n0;
//						while (i < FloatFFT_2D.this.rows) {
//							FloatFFT_2D.this.fftColumns.realInverse2(
//									this.val$a[i], 0, this.val$scale);
//							i += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void cdft2d_subth(int paramInt, float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int j = 8 * this.rows;
//		if (this.columns == 4 * i) {
//			j >>= 1;
//		} else if (this.columns < 4 * i) {
//			i = this.columns >> 1;
//			j >>= 2;
//		}
//		Future[] arrayOfFuture = new Future[i];
//		int k = i;
//		for (int l = 0; l < i; ++l) {
//			int i1 = l;
//			int i2 = j * l;
//			arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(paramInt,
//					k, i1, i2, paramArrayOfFloat, paramBoolean) {
//				public strictfp void run() {
//					int i2;
//					int i3;
//					int i;
//					int j;
//					int k;
//					int l;
//					int i1;
//					if (this.val$isgn == -1) {
//						if (FloatFFT_2D.this.columns > 4 * this.val$nthreads) {
//							i2 = 8 * this.val$n0;
//							while (i2 < FloatFFT_2D.this.columns) {
//								for (i3 = 0; i3 < FloatFFT_2D.this.rows; ++i3) {
//									i = i3 * FloatFFT_2D.this.columns + i2;
//									j = this.val$startt + 2 * i3;
//									k = this.val$startt + 2
//											* FloatFFT_2D.this.rows + 2 * i3;
//									l = k + 2 * FloatFFT_2D.this.rows;
//									i1 = l + 2 * FloatFFT_2D.this.rows;
//									FloatFFT_2D.this.t[j] = this.val$a[i];
//									FloatFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//									FloatFFT_2D.this.t[k] = this.val$a[(i + 2)];
//									FloatFFT_2D.this.t[(k + 1)] = this.val$a[(i + 3)];
//									FloatFFT_2D.this.t[l] = this.val$a[(i + 4)];
//									FloatFFT_2D.this.t[(l + 1)] = this.val$a[(i + 5)];
//									FloatFFT_2D.this.t[i1] = this.val$a[(i + 6)];
//									FloatFFT_2D.this.t[(i1 + 1)] = this.val$a[(i + 7)];
//								}
//								FloatFFT_2D.this.fftRows.complexForward(
//										FloatFFT_2D.this.t, this.val$startt);
//								FloatFFT_2D.this.fftRows.complexForward(
//										FloatFFT_2D.this.t, this.val$startt + 2
//												* FloatFFT_2D.this.rows);
//								FloatFFT_2D.this.fftRows.complexForward(
//										FloatFFT_2D.this.t, this.val$startt + 4
//												* FloatFFT_2D.this.rows);
//								FloatFFT_2D.this.fftRows.complexForward(
//										FloatFFT_2D.this.t, this.val$startt + 6
//												* FloatFFT_2D.this.rows);
//								for (i3 = 0; i3 < FloatFFT_2D.this.rows; ++i3) {
//									i = i3 * FloatFFT_2D.this.columns + i2;
//									j = this.val$startt + 2 * i3;
//									k = this.val$startt + 2
//											* FloatFFT_2D.this.rows + 2 * i3;
//									l = k + 2 * FloatFFT_2D.this.rows;
//									i1 = l + 2 * FloatFFT_2D.this.rows;
//									this.val$a[i] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[j];
//									this.val$a[(i + 1)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[(j + 1)];
//									this.val$a[(i + 2)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[k];
//									this.val$a[(i + 3)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[(k + 1)];
//									this.val$a[(i + 4)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[l];
//									this.val$a[(i + 5)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[(l + 1)];
//									this.val$a[(i + 6)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[i1];
//									this.val$a[(i + 7)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[(i1 + 1)];
//								}
//								i2 += 8 * this.val$nthreads;
//							}
//						} else if (FloatFFT_2D.this.columns == 4 * this.val$nthreads) {
//							for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//								i = i2 * FloatFFT_2D.this.columns + 4
//										* this.val$n0;
//								j = this.val$startt + 2 * i2;
//								k = this.val$startt + 2 * FloatFFT_2D.this.rows
//										+ 2 * i2;
//								FloatFFT_2D.this.t[j] = this.val$a[i];
//								FloatFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//								FloatFFT_2D.this.t[k] = this.val$a[(i + 2)];
//								FloatFFT_2D.this.t[(k + 1)] = this.val$a[(i + 3)];
//							}
//							FloatFFT_2D.this.fftRows.complexForward(
//									FloatFFT_2D.this.t, this.val$startt);
//							FloatFFT_2D.this.fftRows.complexForward(
//									FloatFFT_2D.this.t, this.val$startt + 2
//											* FloatFFT_2D.this.rows);
//							for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//								i = i2 * FloatFFT_2D.this.columns + 4
//										* this.val$n0;
//								j = this.val$startt + 2 * i2;
//								k = this.val$startt + 2 * FloatFFT_2D.this.rows
//										+ 2 * i2;
//								this.val$a[i] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[j];
//								this.val$a[(i + 1)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(j + 1)];
//								this.val$a[(i + 2)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[k];
//								this.val$a[(i + 3)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(k + 1)];
//							}
//						} else {
//							if (FloatFFT_2D.this.columns != 2 * this.val$nthreads)
//								return;
//							for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//								i = i2 * FloatFFT_2D.this.columns + 2
//										* this.val$n0;
//								j = this.val$startt + 2 * i2;
//								FloatFFT_2D.this.t[j] = this.val$a[i];
//								FloatFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//							}
//							FloatFFT_2D.this.fftRows.complexForward(
//									FloatFFT_2D.this.t, this.val$startt);
//							for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//								i = i2 * FloatFFT_2D.this.columns + 2
//										* this.val$n0;
//								j = this.val$startt + 2 * i2;
//								this.val$a[i] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[j];
//								this.val$a[(i + 1)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(j + 1)];
//							}
//						}
//					} else if (FloatFFT_2D.this.columns > 4 * this.val$nthreads) {
//						i2 = 8 * this.val$n0;
//						while (i2 < FloatFFT_2D.this.columns) {
//							for (i3 = 0; i3 < FloatFFT_2D.this.rows; ++i3) {
//								i = i3 * FloatFFT_2D.this.columns + i2;
//								j = this.val$startt + 2 * i3;
//								k = this.val$startt + 2 * FloatFFT_2D.this.rows
//										+ 2 * i3;
//								l = k + 2 * FloatFFT_2D.this.rows;
//								i1 = l + 2 * FloatFFT_2D.this.rows;
//								FloatFFT_2D.this.t[j] = this.val$a[i];
//								FloatFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//								FloatFFT_2D.this.t[k] = this.val$a[(i + 2)];
//								FloatFFT_2D.this.t[(k + 1)] = this.val$a[(i + 3)];
//								FloatFFT_2D.this.t[l] = this.val$a[(i + 4)];
//								FloatFFT_2D.this.t[(l + 1)] = this.val$a[(i + 5)];
//								FloatFFT_2D.this.t[i1] = this.val$a[(i + 6)];
//								FloatFFT_2D.this.t[(i1 + 1)] = this.val$a[(i + 7)];
//							}
//							FloatFFT_2D.this.fftRows.complexInverse(
//									FloatFFT_2D.this.t, this.val$startt,
//									this.val$scale);
//							FloatFFT_2D.this.fftRows.complexInverse(
//									FloatFFT_2D.this.t, this.val$startt + 2
//											* FloatFFT_2D.this.rows,
//									this.val$scale);
//							FloatFFT_2D.this.fftRows.complexInverse(
//									FloatFFT_2D.this.t, this.val$startt + 4
//											* FloatFFT_2D.this.rows,
//									this.val$scale);
//							FloatFFT_2D.this.fftRows.complexInverse(
//									FloatFFT_2D.this.t, this.val$startt + 6
//											* FloatFFT_2D.this.rows,
//									this.val$scale);
//							for (i3 = 0; i3 < FloatFFT_2D.this.rows; ++i3) {
//								i = i3 * FloatFFT_2D.this.columns + i2;
//								j = this.val$startt + 2 * i3;
//								k = this.val$startt + 2 * FloatFFT_2D.this.rows
//										+ 2 * i3;
//								l = k + 2 * FloatFFT_2D.this.rows;
//								i1 = l + 2 * FloatFFT_2D.this.rows;
//								this.val$a[i] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[j];
//								this.val$a[(i + 1)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(j + 1)];
//								this.val$a[(i + 2)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[k];
//								this.val$a[(i + 3)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(k + 1)];
//								this.val$a[(i + 4)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[l];
//								this.val$a[(i + 5)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(l + 1)];
//								this.val$a[(i + 6)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[i1];
//								this.val$a[(i + 7)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(i1 + 1)];
//							}
//							i2 += 8 * this.val$nthreads;
//						}
//					} else if (FloatFFT_2D.this.columns == 4 * this.val$nthreads) {
//						for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//							i = i2 * FloatFFT_2D.this.columns + 4 * this.val$n0;
//							j = this.val$startt + 2 * i2;
//							k = this.val$startt + 2 * FloatFFT_2D.this.rows + 2
//									* i2;
//							FloatFFT_2D.this.t[j] = this.val$a[i];
//							FloatFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//							FloatFFT_2D.this.t[k] = this.val$a[(i + 2)];
//							FloatFFT_2D.this.t[(k + 1)] = this.val$a[(i + 3)];
//						}
//						FloatFFT_2D.this.fftRows.complexInverse(
//								FloatFFT_2D.this.t, this.val$startt,
//								this.val$scale);
//						FloatFFT_2D.this.fftRows
//								.complexInverse(FloatFFT_2D.this.t,
//										this.val$startt + 2
//												* FloatFFT_2D.this.rows,
//										this.val$scale);
//						for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//							i = i2 * FloatFFT_2D.this.columns + 4 * this.val$n0;
//							j = this.val$startt + 2 * i2;
//							k = this.val$startt + 2 * FloatFFT_2D.this.rows + 2
//									* i2;
//							this.val$a[i] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[j];
//							this.val$a[(i + 1)] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[(j + 1)];
//							this.val$a[(i + 2)] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[k];
//							this.val$a[(i + 3)] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[(k + 1)];
//						}
//					} else {
//						if (FloatFFT_2D.this.columns != 2 * this.val$nthreads)
//							return;
//						for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//							i = i2 * FloatFFT_2D.this.columns + 2 * this.val$n0;
//							j = this.val$startt + 2 * i2;
//							FloatFFT_2D.this.t[j] = this.val$a[i];
//							FloatFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//						}
//						FloatFFT_2D.this.fftRows.complexInverse(
//								FloatFFT_2D.this.t, this.val$startt,
//								this.val$scale);
//						for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//							i = i2 * FloatFFT_2D.this.columns + 2 * this.val$n0;
//							j = this.val$startt + 2 * i2;
//							this.val$a[i] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[j];
//							this.val$a[(i + 1)] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[(j + 1)];
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void cdft2d_subth(int paramInt,
//			float[][] paramArrayOfFloat, boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int j = 8 * this.rows;
//		if (this.columns == 4 * i) {
//			j >>= 1;
//		} else if (this.columns < 4 * i) {
//			i = this.columns >> 1;
//			j >>= 2;
//		}
//		Future[] arrayOfFuture = new Future[i];
//		int k = i;
//		for (int l = 0; l < k; ++l) {
//			int i1 = l;
//			int i2 = j * l;
//			arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(paramInt,
//					k, i1, i2, paramArrayOfFloat, paramBoolean) {
//				public strictfp void run() {
//					int i1;
//					int i2;
//					int i;
//					int j;
//					int k;
//					int l;
//					if (this.val$isgn == -1) {
//						if (FloatFFT_2D.this.columns > 4 * this.val$nthreads) {
//							i1 = 8 * this.val$n0;
//							while (i1 < FloatFFT_2D.this.columns) {
//								for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_2D.this.rows + 2 * i2;
//									k = j + 2 * FloatFFT_2D.this.rows;
//									l = k + 2 * FloatFFT_2D.this.rows;
//									FloatFFT_2D.this.t[i] = this.val$a[i2][i1];
//									FloatFFT_2D.this.t[(i + 1)] = this.val$a[i2][(i1 + 1)];
//									FloatFFT_2D.this.t[j] = this.val$a[i2][(i1 + 2)];
//									FloatFFT_2D.this.t[(j + 1)] = this.val$a[i2][(i1 + 3)];
//									FloatFFT_2D.this.t[k] = this.val$a[i2][(i1 + 4)];
//									FloatFFT_2D.this.t[(k + 1)] = this.val$a[i2][(i1 + 5)];
//									FloatFFT_2D.this.t[l] = this.val$a[i2][(i1 + 6)];
//									FloatFFT_2D.this.t[(l + 1)] = this.val$a[i2][(i1 + 7)];
//								}
//								FloatFFT_2D.this.fftRows.complexForward(
//										FloatFFT_2D.this.t, this.val$startt);
//								FloatFFT_2D.this.fftRows.complexForward(
//										FloatFFT_2D.this.t, this.val$startt + 2
//												* FloatFFT_2D.this.rows);
//								FloatFFT_2D.this.fftRows.complexForward(
//										FloatFFT_2D.this.t, this.val$startt + 4
//												* FloatFFT_2D.this.rows);
//								FloatFFT_2D.this.fftRows.complexForward(
//										FloatFFT_2D.this.t, this.val$startt + 6
//												* FloatFFT_2D.this.rows);
//								for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* FloatFFT_2D.this.rows + 2 * i2;
//									k = j + 2 * FloatFFT_2D.this.rows;
//									l = k + 2 * FloatFFT_2D.this.rows;
//									this.val$a[i2][i1] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[i];
//									this.val$a[i2][(i1 + 1)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[(i + 1)];
//									this.val$a[i2][(i1 + 2)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[j];
//									this.val$a[i2][(i1 + 3)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[(j + 1)];
//									this.val$a[i2][(i1 + 4)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[k];
//									this.val$a[i2][(i1 + 5)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[(k + 1)];
//									this.val$a[i2][(i1 + 6)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[l];
//									this.val$a[i2][(i1 + 7)] = FloatFFT_2D
//											.access$400(FloatFFT_2D.this)[(l + 1)];
//								}
//								i1 += 8 * this.val$nthreads;
//							}
//						} else if (FloatFFT_2D.this.columns == 4 * this.val$nthreads) {
//							for (i1 = 0; i1 < FloatFFT_2D.this.rows; ++i1) {
//								i = this.val$startt + 2 * i1;
//								j = this.val$startt + 2 * FloatFFT_2D.this.rows
//										+ 2 * i1;
//								FloatFFT_2D.this.t[i] = this.val$a[i1][(4 * this.val$n0)];
//								FloatFFT_2D.this.t[(i + 1)] = this.val$a[i1][(4 * this.val$n0 + 1)];
//								FloatFFT_2D.this.t[j] = this.val$a[i1][(4 * this.val$n0 + 2)];
//								FloatFFT_2D.this.t[(j + 1)] = this.val$a[i1][(4 * this.val$n0 + 3)];
//							}
//							FloatFFT_2D.this.fftRows.complexForward(
//									FloatFFT_2D.this.t, this.val$startt);
//							FloatFFT_2D.this.fftRows.complexForward(
//									FloatFFT_2D.this.t, this.val$startt + 2
//											* FloatFFT_2D.this.rows);
//							for (i1 = 0; i1 < FloatFFT_2D.this.rows; ++i1) {
//								i = this.val$startt + 2 * i1;
//								j = this.val$startt + 2 * FloatFFT_2D.this.rows
//										+ 2 * i1;
//								this.val$a[i1][(4 * this.val$n0)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[i];
//								this.val$a[i1][(4 * this.val$n0 + 1)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(i + 1)];
//								this.val$a[i1][(4 * this.val$n0 + 2)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[j];
//								this.val$a[i1][(4 * this.val$n0 + 3)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(j + 1)];
//							}
//						} else {
//							if (FloatFFT_2D.this.columns != 2 * this.val$nthreads)
//								return;
//							for (i1 = 0; i1 < FloatFFT_2D.this.rows; ++i1) {
//								i = this.val$startt + 2 * i1;
//								FloatFFT_2D.this.t[i] = this.val$a[i1][(2 * this.val$n0)];
//								FloatFFT_2D.this.t[(i + 1)] = this.val$a[i1][(2 * this.val$n0 + 1)];
//							}
//							FloatFFT_2D.this.fftRows.complexForward(
//									FloatFFT_2D.this.t, this.val$startt);
//							for (i1 = 0; i1 < FloatFFT_2D.this.rows; ++i1) {
//								i = this.val$startt + 2 * i1;
//								this.val$a[i1][(2 * this.val$n0)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[i];
//								this.val$a[i1][(2 * this.val$n0 + 1)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(i + 1)];
//							}
//						}
//					} else if (FloatFFT_2D.this.columns > 4 * this.val$nthreads) {
//						i1 = 8 * this.val$n0;
//						while (i1 < FloatFFT_2D.this.columns) {
//							for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//								i = this.val$startt + 2 * i2;
//								j = this.val$startt + 2 * FloatFFT_2D.this.rows
//										+ 2 * i2;
//								k = j + 2 * FloatFFT_2D.this.rows;
//								l = k + 2 * FloatFFT_2D.this.rows;
//								FloatFFT_2D.this.t[i] = this.val$a[i2][i1];
//								FloatFFT_2D.this.t[(i + 1)] = this.val$a[i2][(i1 + 1)];
//								FloatFFT_2D.this.t[j] = this.val$a[i2][(i1 + 2)];
//								FloatFFT_2D.this.t[(j + 1)] = this.val$a[i2][(i1 + 3)];
//								FloatFFT_2D.this.t[k] = this.val$a[i2][(i1 + 4)];
//								FloatFFT_2D.this.t[(k + 1)] = this.val$a[i2][(i1 + 5)];
//								FloatFFT_2D.this.t[l] = this.val$a[i2][(i1 + 6)];
//								FloatFFT_2D.this.t[(l + 1)] = this.val$a[i2][(i1 + 7)];
//							}
//							FloatFFT_2D.this.fftRows.complexInverse(
//									FloatFFT_2D.this.t, this.val$startt,
//									this.val$scale);
//							FloatFFT_2D.this.fftRows.complexInverse(
//									FloatFFT_2D.this.t, this.val$startt + 2
//											* FloatFFT_2D.this.rows,
//									this.val$scale);
//							FloatFFT_2D.this.fftRows.complexInverse(
//									FloatFFT_2D.this.t, this.val$startt + 4
//											* FloatFFT_2D.this.rows,
//									this.val$scale);
//							FloatFFT_2D.this.fftRows.complexInverse(
//									FloatFFT_2D.this.t, this.val$startt + 6
//											* FloatFFT_2D.this.rows,
//									this.val$scale);
//							for (i2 = 0; i2 < FloatFFT_2D.this.rows; ++i2) {
//								i = this.val$startt + 2 * i2;
//								j = this.val$startt + 2 * FloatFFT_2D.this.rows
//										+ 2 * i2;
//								k = j + 2 * FloatFFT_2D.this.rows;
//								l = k + 2 * FloatFFT_2D.this.rows;
//								this.val$a[i2][i1] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[i];
//								this.val$a[i2][(i1 + 1)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(i + 1)];
//								this.val$a[i2][(i1 + 2)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[j];
//								this.val$a[i2][(i1 + 3)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(j + 1)];
//								this.val$a[i2][(i1 + 4)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[k];
//								this.val$a[i2][(i1 + 5)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(k + 1)];
//								this.val$a[i2][(i1 + 6)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[l];
//								this.val$a[i2][(i1 + 7)] = FloatFFT_2D
//										.access$400(FloatFFT_2D.this)[(l + 1)];
//							}
//							i1 += 8 * this.val$nthreads;
//						}
//					} else if (FloatFFT_2D.this.columns == 4 * this.val$nthreads) {
//						for (i1 = 0; i1 < FloatFFT_2D.this.rows; ++i1) {
//							i = this.val$startt + 2 * i1;
//							j = this.val$startt + 2 * FloatFFT_2D.this.rows + 2
//									* i1;
//							FloatFFT_2D.this.t[i] = this.val$a[i1][(4 * this.val$n0)];
//							FloatFFT_2D.this.t[(i + 1)] = this.val$a[i1][(4 * this.val$n0 + 1)];
//							FloatFFT_2D.this.t[j] = this.val$a[i1][(4 * this.val$n0 + 2)];
//							FloatFFT_2D.this.t[(j + 1)] = this.val$a[i1][(4 * this.val$n0 + 3)];
//						}
//						FloatFFT_2D.this.fftRows.complexInverse(
//								FloatFFT_2D.this.t, this.val$startt,
//								this.val$scale);
//						FloatFFT_2D.this.fftRows
//								.complexInverse(FloatFFT_2D.this.t,
//										this.val$startt + 2
//												* FloatFFT_2D.this.rows,
//										this.val$scale);
//						for (i1 = 0; i1 < FloatFFT_2D.this.rows; ++i1) {
//							i = this.val$startt + 2 * i1;
//							j = this.val$startt + 2 * FloatFFT_2D.this.rows + 2
//									* i1;
//							this.val$a[i1][(4 * this.val$n0)] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[i];
//							this.val$a[i1][(4 * this.val$n0 + 1)] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[(i + 1)];
//							this.val$a[i1][(4 * this.val$n0 + 2)] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[j];
//							this.val$a[i1][(4 * this.val$n0 + 3)] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[(j + 1)];
//						}
//					} else {
//						if (FloatFFT_2D.this.columns != 2 * this.val$nthreads)
//							return;
//						for (i1 = 0; i1 < FloatFFT_2D.this.rows; ++i1) {
//							i = this.val$startt + 2 * i1;
//							FloatFFT_2D.this.t[i] = this.val$a[i1][(2 * this.val$n0)];
//							FloatFFT_2D.this.t[(i + 1)] = this.val$a[i1][(2 * this.val$n0 + 1)];
//						}
//						FloatFFT_2D.this.fftRows.complexInverse(
//								FloatFFT_2D.this.t, this.val$startt,
//								this.val$scale);
//						for (i1 = 0; i1 < FloatFFT_2D.this.rows; ++i1) {
//							i = this.val$startt + 2 * i1;
//							this.val$a[i1][(2 * this.val$n0)] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[i];
//							this.val$a[i1][(2 * this.val$n0 + 1)] = FloatFFT_2D
//									.access$400(FloatFFT_2D.this)[(i + 1)];
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void fillSymmetric(float[] paramArrayOfFloat) {
//		int i = 2 * this.columns;
//		int i2 = this.rows / 2;
//		int k;
//		for (int i3 = this.rows - 1; i3 >= 1; --i3) {
//			int j = i3 * this.columns;
//			k = 2 * j;
//			for (int i4 = 0; i4 < this.columns; i4 += 2) {
//				paramArrayOfFloat[(k + i4)] = paramArrayOfFloat[(j + i4)];
//				paramArrayOfFloat[(j + i4)] = 0.0F;
//				paramArrayOfFloat[(k + i4 + 1)] = paramArrayOfFloat[(j + i4 + 1)];
//				paramArrayOfFloat[(j + i4 + 1)] = 0.0F;
//			}
//		}
//		int i3 = ConcurrencyUtils.getNumberOfThreads();
//		int i6;
//		if ((i3 > 1) && (this.useThreads) && (i2 >= i3)) {
//			Future[] arrayOfFuture = new Future[i3];
//			i6 = i2 / i3;
//			int i7 = 2 * this.columns;
//			for (int i8 = 0; i8 < i3; ++i8) {
//				int i9;
//				if (i8 == 0)
//					i9 = i8 * i6 + 1;
//				else
//					i9 = i8 * i6;
//				int i10 = i8 * i6 + i6;
//				int i11 = i8 * i6;
//				int i12;
//				if (i8 == i3 - 1)
//					i12 = i8 * i6 + i6 + 1;
//				else
//					i12 = i8 * i6 + i6;
//				arrayOfFuture[i8] = ConcurrencyUtils.submit(new Runnable(i9,
//						i10, i7, paramArrayOfFloat, i11, i12) {
//					public strictfp void run() {
//						int i;
//						int j;
//						int k;
//						for (int i1 = this.val$l1offa; i1 < this.val$l1stopa; ++i1) {
//							i = i1 * this.val$newn2;
//							j = (FloatFFT_2D.this.rows - i1) * this.val$newn2;
//							k = i + FloatFFT_2D.this.columns;
//							this.val$a[k] = this.val$a[(j + 1)];
//							this.val$a[(k + 1)] = (-this.val$a[j]);
//						}
//						int i2;
//						int l;
//						for (i1 = this.val$l1offa; i1 < this.val$l1stopa; ++i1) {
//							i = i1 * this.val$newn2;
//							k = (FloatFFT_2D.this.rows - i1 + 1)
//									* this.val$newn2;
//							for (i2 = FloatFFT_2D.this.columns + 2; i2 < this.val$newn2; i2 += 2) {
//								j = k - i2;
//								l = i + i2;
//								this.val$a[l] = this.val$a[j];
//								this.val$a[(l + 1)] = (-this.val$a[(j + 1)]);
//							}
//						}
//						for (i1 = this.val$l2offa; i1 < this.val$l2stopa; ++i1) {
//							k = (FloatFFT_2D.this.rows - i1)
//									% FloatFFT_2D.this.rows * this.val$newn2;
//							l = i1 * this.val$newn2;
//							for (i2 = 0; i2 < this.val$newn2; i2 += 2) {
//								i = k + (this.val$newn2 - i2) % this.val$newn2;
//								j = l + i2;
//								this.val$a[i] = this.val$a[j];
//								this.val$a[(i + 1)] = (-this.val$a[(j + 1)]);
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			int l;
//			for (int i5 = 1; i5 < i2; ++i5) {
//				k = i5 * i;
//				l = (this.rows - i5) * i;
//				paramArrayOfFloat[(k + this.columns)] = paramArrayOfFloat[(l + 1)];
//				paramArrayOfFloat[(k + this.columns + 1)] = (-paramArrayOfFloat[l]);
//			}
//			for (i5 = 1; i5 < i2; ++i5) {
//				k = i5 * i;
//				l = (this.rows - i5 + 1) * i;
//				for (i6 = this.columns + 2; i6 < i; i6 += 2) {
//					paramArrayOfFloat[(k + i6)] = paramArrayOfFloat[(l - i6)];
//					paramArrayOfFloat[(k + i6 + 1)] = (-paramArrayOfFloat[(l
//							- i6 + 1)]);
//				}
//			}
//			for (i5 = 0; i5 <= this.rows / 2; ++i5) {
//				j = i5 * i;
//				int i1 = (this.rows - i5) % this.rows * i;
//				for (i6 = 0; i6 < i; i6 += 2) {
//					k = j + i6;
//					l = i1 + (i - i6) % i;
//					paramArrayOfFloat[l] = paramArrayOfFloat[k];
//					paramArrayOfFloat[(l + 1)] = (-paramArrayOfFloat[(k + 1)]);
//				}
//			}
//		}
//		paramArrayOfFloat[this.columns] = (-paramArrayOfFloat[1]);
//		paramArrayOfFloat[1] = 0.0F;
//		int j = i2 * i;
//		paramArrayOfFloat[(j + this.columns)] = (-paramArrayOfFloat[(j + 1)]);
//		paramArrayOfFloat[(j + 1)] = 0.0F;
//		paramArrayOfFloat[(j + this.columns + 1)] = 0.0F;
//	}
//
//	private strictfp void fillSymmetric(float[][] paramArrayOfFloat) {
//		int i = 2 * this.columns;
//		int j = this.rows / 2;
//		int k = ConcurrencyUtils.getNumberOfThreads();
//		int i1;
//		int i2;
//		int i3;
//		if ((k > 1) && (this.useThreads) && (j >= k)) {
//			Future[] arrayOfFuture = new Future[k];
//			i1 = j / k;
//			for (i2 = 0; i2 < k; ++i2) {
//				if (i2 == 0)
//					i3 = i2 * i1 + 1;
//				else
//					i3 = i2 * i1;
//				int i4 = i2 * i1 + i1;
//				int i5 = i2 * i1;
//				int i6;
//				if (i2 == k - 1)
//					i6 = i2 * i1 + i1 + 1;
//				else
//					i6 = i2 * i1 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, paramArrayOfFloat, i, i5, i6) {
//					public strictfp void run() {
//						int i;
//						for (int k = this.val$l1offa; k < this.val$l1stopa; ++k) {
//							i = FloatFFT_2D.this.rows - k;
//							this.val$a[k][FloatFFT_2D.this.columns] = this.val$a[i][1];
//							this.val$a[k][(FloatFFT_2D.this.columns + 1)] = (-this.val$a[i][0]);
//						}
//						int l;
//						int j;
//						for (k = this.val$l1offa; k < this.val$l1stopa; ++k) {
//							i = FloatFFT_2D.this.rows - k;
//							for (l = FloatFFT_2D.this.columns + 2; l < this.val$newn2; l += 2) {
//								j = this.val$newn2 - l;
//								this.val$a[k][l] = this.val$a[i][j];
//								this.val$a[k][(l + 1)] = (-this.val$a[i][(j + 1)]);
//							}
//						}
//						for (k = this.val$l2offa; k < this.val$l2stopa; ++k) {
//							i = (FloatFFT_2D.this.rows - k)
//									% FloatFFT_2D.this.rows;
//							l = 0;
//							while (l < this.val$newn2) {
//								j = (this.val$newn2 - l) % this.val$newn2;
//								this.val$a[i][j] = this.val$a[k][l];
//								this.val$a[i][(j + 1)] = (-this.val$a[k][(l + 1)]);
//								l += 2;
//							}
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int l = 1; l < j; ++l) {
//				i1 = this.rows - l;
//				paramArrayOfFloat[l][this.columns] = paramArrayOfFloat[i1][1];
//				paramArrayOfFloat[l][(this.columns + 1)] = (-paramArrayOfFloat[i1][0]);
//			}
//			for (l = 1; l < j; ++l) {
//				i1 = this.rows - l;
//				for (i2 = this.columns + 2; i2 < i; i2 += 2) {
//					i3 = i - i2;
//					paramArrayOfFloat[l][i2] = paramArrayOfFloat[i1][i3];
//					paramArrayOfFloat[l][(i2 + 1)] = (-paramArrayOfFloat[i1][(i3 + 1)]);
//				}
//			}
//			for (l = 0; l <= this.rows / 2; ++l) {
//				i1 = (this.rows - l) % this.rows;
//				for (i2 = 0; i2 < i; i2 += 2) {
//					i3 = (i - i2) % i;
//					paramArrayOfFloat[i1][i3] = paramArrayOfFloat[l][i2];
//					paramArrayOfFloat[i1][(i3 + 1)] = (-paramArrayOfFloat[l][(i2 + 1)]);
//				}
//			}
//		}
//		paramArrayOfFloat[0][this.columns] = (-paramArrayOfFloat[0][1]);
//		paramArrayOfFloat[0][1] = 0.0F;
//		paramArrayOfFloat[j][this.columns] = (-paramArrayOfFloat[j][1]);
//		paramArrayOfFloat[j][1] = 0.0F;
//		paramArrayOfFloat[j][(this.columns + 1)] = 0.0F;
//	}
//}