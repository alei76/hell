package ps.hell.math.base.persons.fft.transform.fft;//package ps.landerbuluse.ml.math.fft.transform.fft;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class DoubleFFT_2D {
//	private int rows;
//	private int columns;
//	private double[] t;
//	private DoubleFFT_1D fftColumns;
//	private DoubleFFT_1D fftRows;
//	private int oldNthreads;
//	private int nt;
//	private boolean isPowerOfTwo = false;
//	private boolean useThreads = false;
//
//	public DoubleFFT_2D(int paramInt1, int paramInt2) {
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
//			this.t = new double[this.nt];
//		}
//		this.fftRows = new DoubleFFT_1D(paramInt1);
//		if (paramInt1 == paramInt2)
//			this.fftColumns = this.fftRows;
//		else
//			this.fftColumns = new DoubleFFT_1D(paramInt2);
//	}
//
//	public void complexForward(double[] paramArrayOfDouble) {
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
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(0, -1, paramArrayOfDouble, true);
//				cdft2d_subth(-1, paramArrayOfDouble, true);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.fftColumns.complexForward(paramArrayOfDouble, k
//							* this.columns);
//				cdft2d_sub(-1, paramArrayOfDouble, true);
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
//							i3, i4, paramArrayOfDouble, j) {
//						public void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								DoubleFFT_2D.this.fftColumns.complexForward(
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
//							i3, i4, j, paramArrayOfDouble) {
//						public void run() {
//							double[] arrayOfDouble = new double[2 * DoubleFFT_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								int j = 2 * i;
//								int l;
//								int i1;
//								for (int k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									i1 = k * this.val$rowStride + j;
//									arrayOfDouble[l] = this.val$a[i1];
//									arrayOfDouble[(l + 1)] = this.val$a[(i1 + 1)];
//								}
//								DoubleFFT_2D.this.fftRows
//										.complexForward(arrayOfDouble);
//								for (k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									i1 = k * this.val$rowStride + j;
//									this.val$a[i1] = arrayOfDouble[l];
//									this.val$a[(i1 + 1)] = arrayOfDouble[(l + 1)];
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int l = 0; l < this.rows; ++l)
//					this.fftColumns.complexForward(paramArrayOfDouble, l * j);
//				double[] arrayOfDouble = new double[2 * this.rows];
//				for (i1 = 0; i1 < this.columns; ++i1) {
//					i2 = 2 * i1;
//					int i5;
//					for (i3 = 0; i3 < this.rows; ++i3) {
//						i4 = 2 * i3;
//						i5 = i3 * j + i2;
//						arrayOfDouble[i4] = paramArrayOfDouble[i5];
//						arrayOfDouble[(i4 + 1)] = paramArrayOfDouble[(i5 + 1)];
//					}
//					this.fftRows.complexForward(arrayOfDouble);
//					for (i3 = 0; i3 < this.rows; ++i3) {
//						i4 = 2 * i3;
//						i5 = i3 * j + i2;
//						paramArrayOfDouble[i5] = arrayOfDouble[i4];
//						paramArrayOfDouble[(i5 + 1)] = arrayOfDouble[(i4 + 1)];
//					}
//				}
//			}
//		}
//	}
//
//	public void complexForward(double[][] paramArrayOfDouble) {
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
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(0, -1, paramArrayOfDouble, true);
//				cdft2d_subth(-1, paramArrayOfDouble, true);
//			} else {
//				for (l = 0; l < this.rows; ++l)
//					this.fftColumns.complexForward(paramArrayOfDouble[l]);
//				cdft2d_sub(-1, paramArrayOfDouble, true);
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
//							i2, i3, paramArrayOfDouble) {
//						public void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								DoubleFFT_2D.this.fftColumns
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
//							i2, i3, paramArrayOfDouble) {
//						public void run() {
//							double[] arrayOfDouble = new double[2 * DoubleFFT_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								int j = 2 * i;
//								int l;
//								for (int k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									arrayOfDouble[l] = this.val$a[k][j];
//									arrayOfDouble[(l + 1)] = this.val$a[k][(j + 1)];
//								}
//								DoubleFFT_2D.this.fftRows
//										.complexForward(arrayOfDouble);
//								for (k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									this.val$a[k][j] = arrayOfDouble[l];
//									this.val$a[k][(j + 1)] = arrayOfDouble[(l + 1)];
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.fftColumns.complexForward(paramArrayOfDouble[k]);
//				double[] arrayOfDouble = new double[2 * this.rows];
//				for (l = 0; l < this.columns; ++l) {
//					i1 = 2 * l;
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i3 = 2 * i2;
//						arrayOfDouble[i3] = paramArrayOfDouble[i2][i1];
//						arrayOfDouble[(i3 + 1)] = paramArrayOfDouble[i2][(i1 + 1)];
//					}
//					this.fftRows.complexForward(arrayOfDouble);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i3 = 2 * i2;
//						paramArrayOfDouble[i2][i1] = arrayOfDouble[i3];
//						paramArrayOfDouble[i2][(i1 + 1)] = arrayOfDouble[(i3 + 1)];
//					}
//				}
//			}
//		}
//	}
//
//	public void complexInverse(double[] paramArrayOfDouble, boolean paramBoolean) {
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
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(0, 1, paramArrayOfDouble, paramBoolean);
//				cdft2d_subth(1, paramArrayOfDouble, paramBoolean);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.fftColumns.complexInverse(paramArrayOfDouble, k
//							* this.columns, paramBoolean);
//				cdft2d_sub(1, paramArrayOfDouble, paramBoolean);
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
//							i3, i4, paramArrayOfDouble, j, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								DoubleFFT_2D.this.fftColumns.complexInverse(
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
//							i3, i4, j, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							double[] arrayOfDouble = new double[2 * DoubleFFT_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								int j = 2 * i;
//								int l;
//								int i1;
//								for (int k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									i1 = k * this.val$rowspan + j;
//									arrayOfDouble[l] = this.val$a[i1];
//									arrayOfDouble[(l + 1)] = this.val$a[(i1 + 1)];
//								}
//								DoubleFFT_2D.this.fftRows.complexInverse(
//										arrayOfDouble, this.val$scale);
//								for (k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									i1 = k * this.val$rowspan + j;
//									this.val$a[i1] = arrayOfDouble[l];
//									this.val$a[(i1 + 1)] = arrayOfDouble[(l + 1)];
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int l = 0; l < this.rows; ++l)
//					this.fftColumns.complexInverse(paramArrayOfDouble, l * j,
//							paramBoolean);
//				double[] arrayOfDouble = new double[2 * this.rows];
//				for (i1 = 0; i1 < this.columns; ++i1) {
//					i2 = 2 * i1;
//					int i5;
//					for (i3 = 0; i3 < this.rows; ++i3) {
//						i4 = 2 * i3;
//						i5 = i3 * j + i2;
//						arrayOfDouble[i4] = paramArrayOfDouble[i5];
//						arrayOfDouble[(i4 + 1)] = paramArrayOfDouble[(i5 + 1)];
//					}
//					this.fftRows.complexInverse(arrayOfDouble, paramBoolean);
//					for (i3 = 0; i3 < this.rows; ++i3) {
//						i4 = 2 * i3;
//						i5 = i3 * j + i2;
//						paramArrayOfDouble[i5] = arrayOfDouble[i4];
//						paramArrayOfDouble[(i5 + 1)] = arrayOfDouble[(i4 + 1)];
//					}
//				}
//			}
//		}
//	}
//
//	public void complexInverse(double[][] paramArrayOfDouble,
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
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(0, 1, paramArrayOfDouble, paramBoolean);
//				cdft2d_subth(1, paramArrayOfDouble, paramBoolean);
//			} else {
//				for (l = 0; l < this.rows; ++l)
//					this.fftColumns.complexInverse(paramArrayOfDouble[l],
//							paramBoolean);
//				cdft2d_sub(1, paramArrayOfDouble, paramBoolean);
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
//							i2, i3, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								DoubleFFT_2D.this.fftColumns.complexInverse(
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
//							i2, i3, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							double[] arrayOfDouble = new double[2 * DoubleFFT_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								int j = 2 * i;
//								int l;
//								for (int k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									arrayOfDouble[l] = this.val$a[k][j];
//									arrayOfDouble[(l + 1)] = this.val$a[k][(j + 1)];
//								}
//								DoubleFFT_2D.this.fftRows.complexInverse(
//										arrayOfDouble, this.val$scale);
//								for (k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//									l = 2 * k;
//									this.val$a[k][j] = arrayOfDouble[l];
//									this.val$a[k][(j + 1)] = arrayOfDouble[(l + 1)];
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.fftColumns.complexInverse(paramArrayOfDouble[k],
//							paramBoolean);
//				double[] arrayOfDouble = new double[2 * this.rows];
//				for (l = 0; l < this.columns; ++l) {
//					i1 = 2 * l;
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i3 = 2 * i2;
//						arrayOfDouble[i3] = paramArrayOfDouble[i2][i1];
//						arrayOfDouble[(i3 + 1)] = paramArrayOfDouble[i2][(i1 + 1)];
//					}
//					this.fftRows.complexInverse(arrayOfDouble, paramBoolean);
//					for (i2 = 0; i2 < this.rows; ++i2) {
//						i3 = 2 * i2;
//						paramArrayOfDouble[i2][i1] = arrayOfDouble[i3];
//						paramArrayOfDouble[i2][(i1 + 1)] = arrayOfDouble[(i3 + 1)];
//					}
//				}
//			}
//		}
//	}
//
//	public void realForward(double[] paramArrayOfDouble) {
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
//			this.t = new double[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			xdft2d0_subth1(1, 1, paramArrayOfDouble, true);
//			cdft2d_subth(-1, paramArrayOfDouble, true);
//			rdft2d_sub(1, paramArrayOfDouble);
//		} else {
//			for (int j = 0; j < this.rows; ++j)
//				this.fftColumns.realForward(paramArrayOfDouble, j
//						* this.columns);
//			cdft2d_sub(-1, paramArrayOfDouble, true);
//			rdft2d_sub(1, paramArrayOfDouble);
//		}
//	}
//
//	public void realForward(double[][] paramArrayOfDouble) {
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
//			this.t = new double[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			xdft2d0_subth1(1, 1, paramArrayOfDouble, true);
//			cdft2d_subth(-1, paramArrayOfDouble, true);
//			rdft2d_sub(1, paramArrayOfDouble);
//		} else {
//			for (int j = 0; j < this.rows; ++j)
//				this.fftColumns.realForward(paramArrayOfDouble[j]);
//			cdft2d_sub(-1, paramArrayOfDouble, true);
//			rdft2d_sub(1, paramArrayOfDouble);
//		}
//	}
//
//	public void realForwardFull(double[] paramArrayOfDouble) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(1, 1, paramArrayOfDouble, true);
//				cdft2d_subth(-1, paramArrayOfDouble, true);
//				rdft2d_sub(1, paramArrayOfDouble);
//			} else {
//				for (int j = 0; j < this.rows; ++j)
//					this.fftColumns.realForward(paramArrayOfDouble, j
//							* this.columns);
//				cdft2d_sub(-1, paramArrayOfDouble, true);
//				rdft2d_sub(1, paramArrayOfDouble);
//			}
//			fillSymmetric(paramArrayOfDouble);
//		} else {
//			mixedRadixRealForwardFull(paramArrayOfDouble);
//		}
//	}
//
//	public void realForwardFull(double[][] paramArrayOfDouble) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth1(1, 1, paramArrayOfDouble, true);
//				cdft2d_subth(-1, paramArrayOfDouble, true);
//				rdft2d_sub(1, paramArrayOfDouble);
//			} else {
//				for (int j = 0; j < this.rows; ++j)
//					this.fftColumns.realForward(paramArrayOfDouble[j]);
//				cdft2d_sub(-1, paramArrayOfDouble, true);
//				rdft2d_sub(1, paramArrayOfDouble);
//			}
//			fillSymmetric(paramArrayOfDouble);
//		} else {
//			mixedRadixRealForwardFull(paramArrayOfDouble);
//		}
//	}
//
//	public void realInverse(double[] paramArrayOfDouble, boolean paramBoolean) {
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
//			this.t = new double[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			rdft2d_sub(-1, paramArrayOfDouble);
//			cdft2d_subth(1, paramArrayOfDouble, paramBoolean);
//			xdft2d0_subth1(1, -1, paramArrayOfDouble, paramBoolean);
//		} else {
//			rdft2d_sub(-1, paramArrayOfDouble);
//			cdft2d_sub(1, paramArrayOfDouble, paramBoolean);
//			for (int j = 0; j < this.rows; ++j)
//				this.fftColumns.realInverse(paramArrayOfDouble, j
//						* this.columns, paramBoolean);
//		}
//	}
//
//	public void realInverse(double[][] paramArrayOfDouble, boolean paramBoolean) {
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
//			this.t = new double[this.nt];
//			this.oldNthreads = i;
//		}
//		if ((i > 1) && (this.useThreads)) {
//			rdft2d_sub(-1, paramArrayOfDouble);
//			cdft2d_subth(1, paramArrayOfDouble, paramBoolean);
//			xdft2d0_subth1(1, -1, paramArrayOfDouble, paramBoolean);
//		} else {
//			rdft2d_sub(-1, paramArrayOfDouble);
//			cdft2d_sub(1, paramArrayOfDouble, paramBoolean);
//			for (int j = 0; j < this.rows; ++j)
//				this.fftColumns
//						.realInverse(paramArrayOfDouble[j], paramBoolean);
//		}
//	}
//
//	public void realInverseFull(double[] paramArrayOfDouble,
//			boolean paramBoolean) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth2(1, -1, paramArrayOfDouble, paramBoolean);
//				cdft2d_subth(1, paramArrayOfDouble, paramBoolean);
//				rdft2d_sub(1, paramArrayOfDouble);
//			} else {
//				for (int j = 0; j < this.rows; ++j)
//					this.fftColumns.realInverse2(paramArrayOfDouble, j
//							* this.columns, paramBoolean);
//				cdft2d_sub(1, paramArrayOfDouble, paramBoolean);
//				rdft2d_sub(1, paramArrayOfDouble);
//			}
//			fillSymmetric(paramArrayOfDouble);
//		} else {
//			mixedRadixRealInverseFull(paramArrayOfDouble, paramBoolean);
//		}
//	}
//
//	public void realInverseFull(double[][] paramArrayOfDouble,
//			boolean paramBoolean) {
//		if (this.isPowerOfTwo) {
//			int i = ConcurrencyUtils.getNumberOfThreads();
//			if (i != this.oldNthreads) {
//				this.nt = (8 * i * this.rows);
//				if (this.columns == 4 * i)
//					this.nt >>= 1;
//				else if (this.columns < 4 * i)
//					this.nt >>= 2;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				xdft2d0_subth2(1, -1, paramArrayOfDouble, paramBoolean);
//				cdft2d_subth(1, paramArrayOfDouble, paramBoolean);
//				rdft2d_sub(1, paramArrayOfDouble);
//			} else {
//				for (int j = 0; j < this.rows; ++j)
//					this.fftColumns.realInverse2(paramArrayOfDouble[j], 0,
//							paramBoolean);
//				cdft2d_sub(1, paramArrayOfDouble, paramBoolean);
//				rdft2d_sub(1, paramArrayOfDouble);
//			}
//			fillSymmetric(paramArrayOfDouble);
//		} else {
//			mixedRadixRealInverseFull(paramArrayOfDouble, paramBoolean);
//		}
//	}
//
//	private void mixedRadixRealForwardFull(double[][] paramArrayOfDouble) {
//		int i = this.columns / 2 + 1;
//		double[][] arrayOfDouble = new double[i][2 * this.rows];
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
//						i3, paramArrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//							DoubleFFT_2D.this.fftColumns
//									.realForward(this.val$a[i]);
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i1 = 0; i1 < this.rows; ++i1)
//				arrayOfDouble[0][i1] = paramArrayOfDouble[i1][0];
//			this.fftRows.realForwardFull(arrayOfDouble[0]);
//			l = i / j;
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = 1 + i1 * l;
//				i3 = (i1 == j - 1) ? i - 1 : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, arrayOfDouble, paramArrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//								int l = 2 * k;
//								this.val$temp[i][l] = this.val$a[k][j];
//								this.val$temp[i][(l + 1)] = this.val$a[k][(j + 1)];
//							}
//							DoubleFFT_2D.this.fftRows
//									.complexForward(this.val$temp[i]);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			if (this.columns % 2 == 0) {
//				for (i1 = 0; i1 < this.rows; ++i1)
//					arrayOfDouble[(i - 1)][i1] = paramArrayOfDouble[i1][1];
//				this.fftRows.realForwardFull(arrayOfDouble[(i - 1)]);
//			} else {
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i2 = 2 * i1;
//					i3 = i - 1;
//					arrayOfDouble[i3][i2] = paramArrayOfDouble[i1][(2 * i3)];
//					arrayOfDouble[i3][(i2 + 1)] = paramArrayOfDouble[i1][1];
//				}
//				this.fftRows.complexForward(arrayOfDouble[(i - 1)]);
//			}
//			l = this.rows / j;
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = i1 * l;
//				i3 = (i1 == j - 1) ? this.rows : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, i, paramArrayOfDouble, arrayOfDouble) {
//					public void run() {
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
//						i3, i, paramArrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = DoubleFFT_2D.this.rows - i;
//							for (int k = this.val$n2d2; k < DoubleFFT_2D.this.columns; ++k) {
//								int l = 2 * k;
//								int i1 = 2 * (DoubleFFT_2D.this.columns - k);
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
//				this.fftColumns.realForward(paramArrayOfDouble[k]);
//			for (k = 0; k < this.rows; ++k)
//				arrayOfDouble[0][k] = paramArrayOfDouble[k][0];
//			this.fftRows.realForwardFull(arrayOfDouble[0]);
//			for (k = 1; k < i - 1; ++k) {
//				l = 2 * k;
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i2 = 2 * i1;
//					arrayOfDouble[k][i2] = paramArrayOfDouble[i1][l];
//					arrayOfDouble[k][(i2 + 1)] = paramArrayOfDouble[i1][(l + 1)];
//				}
//				this.fftRows.complexForward(arrayOfDouble[k]);
//			}
//			if (this.columns % 2 == 0) {
//				for (k = 0; k < this.rows; ++k)
//					arrayOfDouble[(i - 1)][k] = paramArrayOfDouble[k][1];
//				this.fftRows.realForwardFull(arrayOfDouble[(i - 1)]);
//			} else {
//				for (k = 0; k < this.rows; ++k) {
//					l = 2 * k;
//					i1 = i - 1;
//					arrayOfDouble[i1][l] = paramArrayOfDouble[k][(2 * i1)];
//					arrayOfDouble[i1][(l + 1)] = paramArrayOfDouble[k][1];
//				}
//				this.fftRows.complexForward(arrayOfDouble[(i - 1)]);
//			}
//			for (k = 0; k < this.rows; ++k) {
//				l = 2 * k;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = 2 * i1;
//					paramArrayOfDouble[k][i2] = arrayOfDouble[i1][l];
//					paramArrayOfDouble[k][(i2 + 1)] = arrayOfDouble[i1][(l + 1)];
//				}
//			}
//			for (k = 1; k < this.rows; ++k) {
//				l = this.rows - k;
//				for (i1 = i; i1 < this.columns; ++i1) {
//					i2 = 2 * i1;
//					i3 = 2 * (this.columns - i1);
//					paramArrayOfDouble[0][i2] = paramArrayOfDouble[0][i3];
//					paramArrayOfDouble[0][(i2 + 1)] = (-paramArrayOfDouble[0][(i3 + 1)]);
//					paramArrayOfDouble[k][i2] = paramArrayOfDouble[l][i3];
//					paramArrayOfDouble[k][(i2 + 1)] = (-paramArrayOfDouble[l][(i3 + 1)]);
//				}
//			}
//		}
//	}
//
//	private void mixedRadixRealForwardFull(double[] paramArrayOfDouble) {
//		int i = 2 * this.columns;
//		int j = this.columns / 2 + 1;
//		double[][] arrayOfDouble = new double[j][2 * this.rows];
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
//						i4, paramArrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//							DoubleFFT_2D.this.fftColumns.realForward(
//									this.val$a, i * DoubleFFT_2D.this.columns);
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i2 = 0; i2 < this.rows; ++i2)
//				arrayOfDouble[0][i2] = paramArrayOfDouble[(i2 * this.columns)];
//			this.fftRows.realForwardFull(arrayOfDouble[0]);
//			i1 = j / k;
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = 1 + i2 * i1;
//				i4 = (i2 == k - 1) ? j - 1 : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, arrayOfDouble, paramArrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//								int l = 2 * k;
//								int i1 = k * DoubleFFT_2D.this.columns + j;
//								this.val$temp[i][l] = this.val$a[i1];
//								this.val$temp[i][(l + 1)] = this.val$a[(i1 + 1)];
//							}
//							DoubleFFT_2D.this.fftRows
//									.complexForward(this.val$temp[i]);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			if (this.columns % 2 == 0) {
//				for (i2 = 0; i2 < this.rows; ++i2)
//					arrayOfDouble[(j - 1)][i2] = paramArrayOfDouble[(i2
//							* this.columns + 1)];
//				this.fftRows.realForwardFull(arrayOfDouble[(j - 1)]);
//			} else {
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i3 = 2 * i2;
//					i4 = i2 * this.columns;
//					i5 = j - 1;
//					arrayOfDouble[i5][i3] = paramArrayOfDouble[(i4 + 2 * i5)];
//					arrayOfDouble[i5][(i3 + 1)] = paramArrayOfDouble[(i4 + 1)];
//				}
//				this.fftRows.complexForward(arrayOfDouble[(j - 1)]);
//			}
//			i1 = this.rows / k;
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = i2 * i1;
//				i4 = (i2 == k - 1) ? this.rows : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, j, i, paramArrayOfDouble, arrayOfDouble) {
//					public void run() {
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
//						i4, i, j, paramArrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = i * this.val$rowStride;
//							int k = (DoubleFFT_2D.this.rows - i + 1)
//									* this.val$rowStride;
//							for (int l = this.val$n2d2; l < DoubleFFT_2D.this.columns; ++l) {
//								int i1 = 2 * l;
//								int i2 = 2 * (DoubleFFT_2D.this.columns - l);
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
//				this.fftColumns.realForward(paramArrayOfDouble, l
//						* this.columns);
//			for (l = 0; l < this.rows; ++l)
//				arrayOfDouble[0][l] = paramArrayOfDouble[(l * this.columns)];
//			this.fftRows.realForwardFull(arrayOfDouble[0]);
//			for (l = 1; l < j - 1; ++l) {
//				i1 = 2 * l;
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i3 = 2 * i2;
//					i4 = i2 * this.columns + i1;
//					arrayOfDouble[l][i3] = paramArrayOfDouble[i4];
//					arrayOfDouble[l][(i3 + 1)] = paramArrayOfDouble[(i4 + 1)];
//				}
//				this.fftRows.complexForward(arrayOfDouble[l]);
//			}
//			if (this.columns % 2 == 0) {
//				for (l = 0; l < this.rows; ++l)
//					arrayOfDouble[(j - 1)][l] = paramArrayOfDouble[(l
//							* this.columns + 1)];
//				this.fftRows.realForwardFull(arrayOfDouble[(j - 1)]);
//			} else {
//				for (l = 0; l < this.rows; ++l) {
//					i1 = 2 * l;
//					i2 = l * this.columns;
//					i3 = j - 1;
//					arrayOfDouble[i3][i1] = paramArrayOfDouble[(i2 + 2 * i3)];
//					arrayOfDouble[i3][(i1 + 1)] = paramArrayOfDouble[(i2 + 1)];
//				}
//				this.fftRows.complexForward(arrayOfDouble[(j - 1)]);
//			}
//			for (l = 0; l < this.rows; ++l) {
//				i1 = 2 * l;
//				for (i2 = 0; i2 < j; ++i2) {
//					i3 = 2 * i2;
//					i4 = l * i + i3;
//					paramArrayOfDouble[i4] = arrayOfDouble[i2][i1];
//					paramArrayOfDouble[(i4 + 1)] = arrayOfDouble[i2][(i1 + 1)];
//				}
//			}
//			for (l = 1; l < this.rows; ++l) {
//				i1 = l * i;
//				i2 = (this.rows - l + 1) * i;
//				for (i3 = j; i3 < this.columns; ++i3) {
//					i4 = 2 * i3;
//					i5 = 2 * (this.columns - i3);
//					paramArrayOfDouble[i4] = paramArrayOfDouble[i5];
//					paramArrayOfDouble[(i4 + 1)] = (-paramArrayOfDouble[(i5 + 1)]);
//					int i6 = i1 + i4;
//					int i7 = i2 - i4;
//					paramArrayOfDouble[i6] = paramArrayOfDouble[i7];
//					paramArrayOfDouble[(i6 + 1)] = (-paramArrayOfDouble[(i7 + 1)]);
//				}
//			}
//		}
//	}
//
//	private void mixedRadixRealInverseFull(double[][] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int i = this.columns / 2 + 1;
//		double[][] arrayOfDouble = new double[i][2 * this.rows];
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
//						i3, paramArrayOfDouble, paramBoolean) {
//					public void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//							DoubleFFT_2D.this.fftColumns.realInverse2(
//									this.val$a[i], 0, this.val$scale);
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i1 = 0; i1 < this.rows; ++i1)
//				arrayOfDouble[0][i1] = paramArrayOfDouble[i1][0];
//			this.fftRows.realInverseFull(arrayOfDouble[0], paramBoolean);
//			l = i / j;
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = 1 + i1 * l;
//				i3 = (i1 == j - 1) ? i - 1 : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, arrayOfDouble, paramArrayOfDouble, paramBoolean) {
//					public void run() {
//						for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//								int l = 2 * k;
//								this.val$temp[i][l] = this.val$a[k][j];
//								this.val$temp[i][(l + 1)] = this.val$a[k][(j + 1)];
//							}
//							DoubleFFT_2D.this.fftRows.complexInverse(
//									this.val$temp[i], this.val$scale);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			if (this.columns % 2 == 0) {
//				for (i1 = 0; i1 < this.rows; ++i1)
//					arrayOfDouble[(i - 1)][i1] = paramArrayOfDouble[i1][1];
//				this.fftRows.realInverseFull(arrayOfDouble[(i - 1)],
//						paramBoolean);
//			} else {
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i2 = 2 * i1;
//					i3 = i - 1;
//					arrayOfDouble[i3][i2] = paramArrayOfDouble[i1][(2 * i3)];
//					arrayOfDouble[i3][(i2 + 1)] = paramArrayOfDouble[i1][1];
//				}
//				this.fftRows.complexInverse(arrayOfDouble[(i - 1)],
//						paramBoolean);
//			}
//			l = this.rows / j;
//			for (i1 = 0; i1 < j; ++i1) {
//				i2 = i1 * l;
//				i3 = (i1 == j - 1) ? this.rows : i2 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i2,
//						i3, i, paramArrayOfDouble, arrayOfDouble) {
//					public void run() {
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
//						i3, i, paramArrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = DoubleFFT_2D.this.rows - i;
//							for (int k = this.val$n2d2; k < DoubleFFT_2D.this.columns; ++k) {
//								int l = 2 * k;
//								int i1 = 2 * (DoubleFFT_2D.this.columns - k);
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
//				this.fftColumns.realInverse2(paramArrayOfDouble[k], 0,
//						paramBoolean);
//			for (k = 0; k < this.rows; ++k)
//				arrayOfDouble[0][k] = paramArrayOfDouble[k][0];
//			this.fftRows.realInverseFull(arrayOfDouble[0], paramBoolean);
//			for (k = 1; k < i - 1; ++k) {
//				l = 2 * k;
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i2 = 2 * i1;
//					arrayOfDouble[k][i2] = paramArrayOfDouble[i1][l];
//					arrayOfDouble[k][(i2 + 1)] = paramArrayOfDouble[i1][(l + 1)];
//				}
//				this.fftRows.complexInverse(arrayOfDouble[k], paramBoolean);
//			}
//			if (this.columns % 2 == 0) {
//				for (k = 0; k < this.rows; ++k)
//					arrayOfDouble[(i - 1)][k] = paramArrayOfDouble[k][1];
//				this.fftRows.realInverseFull(arrayOfDouble[(i - 1)],
//						paramBoolean);
//			} else {
//				for (k = 0; k < this.rows; ++k) {
//					l = 2 * k;
//					i1 = i - 1;
//					arrayOfDouble[i1][l] = paramArrayOfDouble[k][(2 * i1)];
//					arrayOfDouble[i1][(l + 1)] = paramArrayOfDouble[k][1];
//				}
//				this.fftRows.complexInverse(arrayOfDouble[(i - 1)],
//						paramBoolean);
//			}
//			for (k = 0; k < this.rows; ++k) {
//				l = 2 * k;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = 2 * i1;
//					paramArrayOfDouble[k][i2] = arrayOfDouble[i1][l];
//					paramArrayOfDouble[k][(i2 + 1)] = arrayOfDouble[i1][(l + 1)];
//				}
//			}
//			for (k = 1; k < this.rows; ++k) {
//				l = this.rows - k;
//				for (i1 = i; i1 < this.columns; ++i1) {
//					i2 = 2 * i1;
//					i3 = 2 * (this.columns - i1);
//					paramArrayOfDouble[0][i2] = paramArrayOfDouble[0][i3];
//					paramArrayOfDouble[0][(i2 + 1)] = (-paramArrayOfDouble[0][(i3 + 1)]);
//					paramArrayOfDouble[k][i2] = paramArrayOfDouble[l][i3];
//					paramArrayOfDouble[k][(i2 + 1)] = (-paramArrayOfDouble[l][(i3 + 1)]);
//				}
//			}
//		}
//	}
//
//	private void mixedRadixRealInverseFull(double[] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int i = 2 * this.columns;
//		int j = this.columns / 2 + 1;
//		double[][] arrayOfDouble = new double[j][2 * this.rows];
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
//						i4, paramArrayOfDouble, paramBoolean) {
//					public void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//							DoubleFFT_2D.this.fftColumns.realInverse2(
//									this.val$a, i * DoubleFFT_2D.this.columns,
//									this.val$scale);
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			for (i2 = 0; i2 < this.rows; ++i2)
//				arrayOfDouble[0][i2] = paramArrayOfDouble[(i2 * this.columns)];
//			this.fftRows.realInverseFull(arrayOfDouble[0], paramBoolean);
//			i1 = j / k;
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = 1 + i2 * i1;
//				i4 = (i2 == k - 1) ? j - 1 : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, arrayOfDouble, paramArrayOfDouble, paramBoolean) {
//					public void run() {
//						for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//							int j = 2 * i;
//							for (int k = 0; k < DoubleFFT_2D.this.rows; ++k) {
//								int l = 2 * k;
//								int i1 = k * DoubleFFT_2D.this.columns + j;
//								this.val$temp[i][l] = this.val$a[i1];
//								this.val$temp[i][(l + 1)] = this.val$a[(i1 + 1)];
//							}
//							DoubleFFT_2D.this.fftRows.complexInverse(
//									this.val$temp[i], this.val$scale);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			if (this.columns % 2 == 0) {
//				for (i2 = 0; i2 < this.rows; ++i2)
//					arrayOfDouble[(j - 1)][i2] = paramArrayOfDouble[(i2
//							* this.columns + 1)];
//				this.fftRows.realInverseFull(arrayOfDouble[(j - 1)],
//						paramBoolean);
//			} else {
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i3 = 2 * i2;
//					i4 = i2 * this.columns;
//					i5 = j - 1;
//					arrayOfDouble[i5][i3] = paramArrayOfDouble[(i4 + 2 * i5)];
//					arrayOfDouble[i5][(i3 + 1)] = paramArrayOfDouble[(i4 + 1)];
//				}
//				this.fftRows.complexInverse(arrayOfDouble[(j - 1)],
//						paramBoolean);
//			}
//			i1 = this.rows / k;
//			for (i2 = 0; i2 < k; ++i2) {
//				i3 = i2 * i1;
//				i4 = (i2 == k - 1) ? this.rows : i3 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, j, i, paramArrayOfDouble, arrayOfDouble) {
//					public void run() {
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
//						i4, i, j, paramArrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//							int j = i * this.val$rowStride;
//							int k = (DoubleFFT_2D.this.rows - i + 1)
//									* this.val$rowStride;
//							for (int l = this.val$n2d2; l < DoubleFFT_2D.this.columns; ++l) {
//								int i1 = 2 * l;
//								int i2 = 2 * (DoubleFFT_2D.this.columns - l);
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
//				this.fftColumns.realInverse2(paramArrayOfDouble, l
//						* this.columns, paramBoolean);
//			for (l = 0; l < this.rows; ++l)
//				arrayOfDouble[0][l] = paramArrayOfDouble[(l * this.columns)];
//			this.fftRows.realInverseFull(arrayOfDouble[0], paramBoolean);
//			for (l = 1; l < j - 1; ++l) {
//				i1 = 2 * l;
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i3 = 2 * i2;
//					i4 = i2 * this.columns + i1;
//					arrayOfDouble[l][i3] = paramArrayOfDouble[i4];
//					arrayOfDouble[l][(i3 + 1)] = paramArrayOfDouble[(i4 + 1)];
//				}
//				this.fftRows.complexInverse(arrayOfDouble[l], paramBoolean);
//			}
//			if (this.columns % 2 == 0) {
//				for (l = 0; l < this.rows; ++l)
//					arrayOfDouble[(j - 1)][l] = paramArrayOfDouble[(l
//							* this.columns + 1)];
//				this.fftRows.realInverseFull(arrayOfDouble[(j - 1)],
//						paramBoolean);
//			} else {
//				for (l = 0; l < this.rows; ++l) {
//					i1 = 2 * l;
//					i2 = l * this.columns;
//					i3 = j - 1;
//					arrayOfDouble[i3][i1] = paramArrayOfDouble[(i2 + 2 * i3)];
//					arrayOfDouble[i3][(i1 + 1)] = paramArrayOfDouble[(i2 + 1)];
//				}
//				this.fftRows.complexInverse(arrayOfDouble[(j - 1)],
//						paramBoolean);
//			}
//			for (l = 0; l < this.rows; ++l) {
//				i1 = 2 * l;
//				for (i2 = 0; i2 < j; ++i2) {
//					i3 = 2 * i2;
//					i4 = l * i + i3;
//					paramArrayOfDouble[i4] = arrayOfDouble[i2][i1];
//					paramArrayOfDouble[(i4 + 1)] = arrayOfDouble[i2][(i1 + 1)];
//				}
//			}
//			for (l = 1; l < this.rows; ++l) {
//				i1 = l * i;
//				i2 = (this.rows - l + 1) * i;
//				for (i3 = j; i3 < this.columns; ++i3) {
//					i4 = 2 * i3;
//					i5 = 2 * (this.columns - i3);
//					paramArrayOfDouble[i4] = paramArrayOfDouble[i5];
//					paramArrayOfDouble[(i4 + 1)] = (-paramArrayOfDouble[(i5 + 1)]);
//					int i6 = i1 + i4;
//					int i7 = i2 - i4;
//					paramArrayOfDouble[i6] = paramArrayOfDouble[i7];
//					paramArrayOfDouble[(i6 + 1)] = (-paramArrayOfDouble[(i7 + 1)]);
//				}
//			}
//		}
//	}
//
//	private void rdft2d_sub(int paramInt, double[] paramArrayOfDouble) {
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
//				double d = paramArrayOfDouble[k] - paramArrayOfDouble[l];
//				paramArrayOfDouble[k] += paramArrayOfDouble[l];
//				paramArrayOfDouble[l] = d;
//				d = paramArrayOfDouble[(l + 1)] - paramArrayOfDouble[(k + 1)];
//				paramArrayOfDouble[(k + 1)] += paramArrayOfDouble[(l + 1)];
//				paramArrayOfDouble[(l + 1)] = d;
//			}
//		else
//			for (i1 = 1; i1 < i; ++i1) {
//				j = this.rows - i1;
//				k = i1 * this.columns;
//				l = j * this.columns;
//				paramArrayOfDouble[l] = (0.5D * (paramArrayOfDouble[k] - paramArrayOfDouble[l]));
//				paramArrayOfDouble[k] -= paramArrayOfDouble[l];
//				paramArrayOfDouble[(l + 1)] = (0.5D * (paramArrayOfDouble[(k + 1)] + paramArrayOfDouble[(l + 1)]));
//				paramArrayOfDouble[(k + 1)] -= paramArrayOfDouble[(l + 1)];
//			}
//	}
//
//	private void rdft2d_sub(int paramInt, double[][] paramArrayOfDouble) {
//		int i = this.rows >> 1;
//		int k;
//		int j;
//		if (paramInt < 0)
//			for (k = 1; k < i; ++k) {
//				j = this.rows - k;
//				double d = paramArrayOfDouble[k][0] - paramArrayOfDouble[j][0];
//				paramArrayOfDouble[k][0] += paramArrayOfDouble[j][0];
//				paramArrayOfDouble[j][0] = d;
//				d = paramArrayOfDouble[j][1] - paramArrayOfDouble[k][1];
//				paramArrayOfDouble[k][1] += paramArrayOfDouble[j][1];
//				paramArrayOfDouble[j][1] = d;
//			}
//		else
//			for (k = 1; k < i; ++k) {
//				j = this.rows - k;
//				paramArrayOfDouble[j][0] = (0.5D * (paramArrayOfDouble[k][0] - paramArrayOfDouble[j][0]));
//				paramArrayOfDouble[k][0] -= paramArrayOfDouble[j][0];
//				paramArrayOfDouble[j][1] = (0.5D * (paramArrayOfDouble[k][1] + paramArrayOfDouble[j][1]));
//				paramArrayOfDouble[k][1] -= paramArrayOfDouble[j][1];
//			}
//	}
//
//	private void cdft2d_sub(int paramInt, double[] paramArrayOfDouble,
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
//						this.t[j] = paramArrayOfDouble[i];
//						this.t[(j + 1)] = paramArrayOfDouble[(i + 1)];
//						this.t[k] = paramArrayOfDouble[(i + 2)];
//						this.t[(k + 1)] = paramArrayOfDouble[(i + 3)];
//						this.t[l] = paramArrayOfDouble[(i + 4)];
//						this.t[(l + 1)] = paramArrayOfDouble[(i + 5)];
//						this.t[i1] = paramArrayOfDouble[(i + 6)];
//						this.t[(i1 + 1)] = paramArrayOfDouble[(i + 7)];
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
//						paramArrayOfDouble[i] = this.t[j];
//						paramArrayOfDouble[(i + 1)] = this.t[(j + 1)];
//						paramArrayOfDouble[(i + 2)] = this.t[k];
//						paramArrayOfDouble[(i + 3)] = this.t[(k + 1)];
//						paramArrayOfDouble[(i + 4)] = this.t[l];
//						paramArrayOfDouble[(i + 5)] = this.t[(l + 1)];
//						paramArrayOfDouble[(i + 6)] = this.t[i1];
//						paramArrayOfDouble[(i + 7)] = this.t[(i1 + 1)];
//					}
//				}
//			} else if (this.columns == 4) {
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i = i2 * this.columns;
//					j = 2 * i2;
//					k = 2 * this.rows + 2 * i2;
//					this.t[j] = paramArrayOfDouble[i];
//					this.t[(j + 1)] = paramArrayOfDouble[(i + 1)];
//					this.t[k] = paramArrayOfDouble[(i + 2)];
//					this.t[(k + 1)] = paramArrayOfDouble[(i + 3)];
//				}
//				this.fftRows.complexForward(this.t, 0);
//				this.fftRows.complexForward(this.t, 2 * this.rows);
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i = i2 * this.columns;
//					j = 2 * i2;
//					k = 2 * this.rows + 2 * i2;
//					paramArrayOfDouble[i] = this.t[j];
//					paramArrayOfDouble[(i + 1)] = this.t[(j + 1)];
//					paramArrayOfDouble[(i + 2)] = this.t[k];
//					paramArrayOfDouble[(i + 3)] = this.t[(k + 1)];
//				}
//			} else {
//				if (this.columns != 2)
//					return;
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i = i2 * this.columns;
//					j = 2 * i2;
//					this.t[j] = paramArrayOfDouble[i];
//					this.t[(j + 1)] = paramArrayOfDouble[(i + 1)];
//				}
//				this.fftRows.complexForward(this.t, 0);
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i = i2 * this.columns;
//					j = 2 * i2;
//					paramArrayOfDouble[i] = this.t[j];
//					paramArrayOfDouble[(i + 1)] = this.t[(j + 1)];
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
//					this.t[j] = paramArrayOfDouble[i];
//					this.t[(j + 1)] = paramArrayOfDouble[(i + 1)];
//					this.t[k] = paramArrayOfDouble[(i + 2)];
//					this.t[(k + 1)] = paramArrayOfDouble[(i + 3)];
//					this.t[l] = paramArrayOfDouble[(i + 4)];
//					this.t[(l + 1)] = paramArrayOfDouble[(i + 5)];
//					this.t[i1] = paramArrayOfDouble[(i + 6)];
//					this.t[(i1 + 1)] = paramArrayOfDouble[(i + 7)];
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
//					paramArrayOfDouble[i] = this.t[j];
//					paramArrayOfDouble[(i + 1)] = this.t[(j + 1)];
//					paramArrayOfDouble[(i + 2)] = this.t[k];
//					paramArrayOfDouble[(i + 3)] = this.t[(k + 1)];
//					paramArrayOfDouble[(i + 4)] = this.t[l];
//					paramArrayOfDouble[(i + 5)] = this.t[(l + 1)];
//					paramArrayOfDouble[(i + 6)] = this.t[i1];
//					paramArrayOfDouble[(i + 7)] = this.t[(i1 + 1)];
//				}
//			}
//		} else if (this.columns == 4) {
//			for (i2 = 0; i2 < this.rows; ++i2) {
//				i = i2 * this.columns;
//				j = 2 * i2;
//				k = 2 * this.rows + 2 * i2;
//				this.t[j] = paramArrayOfDouble[i];
//				this.t[(j + 1)] = paramArrayOfDouble[(i + 1)];
//				this.t[k] = paramArrayOfDouble[(i + 2)];
//				this.t[(k + 1)] = paramArrayOfDouble[(i + 3)];
//			}
//			this.fftRows.complexInverse(this.t, 0, paramBoolean);
//			this.fftRows.complexInverse(this.t, 2 * this.rows, paramBoolean);
//			for (i2 = 0; i2 < this.rows; ++i2) {
//				i = i2 * this.columns;
//				j = 2 * i2;
//				k = 2 * this.rows + 2 * i2;
//				paramArrayOfDouble[i] = this.t[j];
//				paramArrayOfDouble[(i + 1)] = this.t[(j + 1)];
//				paramArrayOfDouble[(i + 2)] = this.t[k];
//				paramArrayOfDouble[(i + 3)] = this.t[(k + 1)];
//			}
//		} else {
//			if (this.columns != 2)
//				return;
//			for (i2 = 0; i2 < this.rows; ++i2) {
//				i = i2 * this.columns;
//				j = 2 * i2;
//				this.t[j] = paramArrayOfDouble[i];
//				this.t[(j + 1)] = paramArrayOfDouble[(i + 1)];
//			}
//			this.fftRows.complexInverse(this.t, 0, paramBoolean);
//			for (i2 = 0; i2 < this.rows; ++i2) {
//				i = i2 * this.columns;
//				j = 2 * i2;
//				paramArrayOfDouble[i] = this.t[j];
//				paramArrayOfDouble[(i + 1)] = this.t[(j + 1)];
//			}
//		}
//	}
//
//	private void cdft2d_sub(int paramInt, double[][] paramArrayOfDouble,
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
//						this.t[i] = paramArrayOfDouble[i2][i1];
//						this.t[(i + 1)] = paramArrayOfDouble[i2][(i1 + 1)];
//						this.t[j] = paramArrayOfDouble[i2][(i1 + 2)];
//						this.t[(j + 1)] = paramArrayOfDouble[i2][(i1 + 3)];
//						this.t[k] = paramArrayOfDouble[i2][(i1 + 4)];
//						this.t[(k + 1)] = paramArrayOfDouble[i2][(i1 + 5)];
//						this.t[l] = paramArrayOfDouble[i2][(i1 + 6)];
//						this.t[(l + 1)] = paramArrayOfDouble[i2][(i1 + 7)];
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
//						paramArrayOfDouble[i2][i1] = this.t[i];
//						paramArrayOfDouble[i2][(i1 + 1)] = this.t[(i + 1)];
//						paramArrayOfDouble[i2][(i1 + 2)] = this.t[j];
//						paramArrayOfDouble[i2][(i1 + 3)] = this.t[(j + 1)];
//						paramArrayOfDouble[i2][(i1 + 4)] = this.t[k];
//						paramArrayOfDouble[i2][(i1 + 5)] = this.t[(k + 1)];
//						paramArrayOfDouble[i2][(i1 + 6)] = this.t[l];
//						paramArrayOfDouble[i2][(i1 + 7)] = this.t[(l + 1)];
//					}
//				}
//			} else if (this.columns == 4) {
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i = 2 * i1;
//					j = 2 * this.rows + 2 * i1;
//					this.t[i] = paramArrayOfDouble[i1][0];
//					this.t[(i + 1)] = paramArrayOfDouble[i1][1];
//					this.t[j] = paramArrayOfDouble[i1][2];
//					this.t[(j + 1)] = paramArrayOfDouble[i1][3];
//				}
//				this.fftRows.complexForward(this.t, 0);
//				this.fftRows.complexForward(this.t, 2 * this.rows);
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i = 2 * i1;
//					j = 2 * this.rows + 2 * i1;
//					paramArrayOfDouble[i1][0] = this.t[i];
//					paramArrayOfDouble[i1][1] = this.t[(i + 1)];
//					paramArrayOfDouble[i1][2] = this.t[j];
//					paramArrayOfDouble[i1][3] = this.t[(j + 1)];
//				}
//			} else {
//				if (this.columns != 2)
//					return;
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i = 2 * i1;
//					this.t[i] = paramArrayOfDouble[i1][0];
//					this.t[(i + 1)] = paramArrayOfDouble[i1][1];
//				}
//				this.fftRows.complexForward(this.t, 0);
//				for (i1 = 0; i1 < this.rows; ++i1) {
//					i = 2 * i1;
//					paramArrayOfDouble[i1][0] = this.t[i];
//					paramArrayOfDouble[i1][1] = this.t[(i + 1)];
//				}
//			}
//		} else if (this.columns > 4) {
//			for (i1 = 0; i1 < this.columns; i1 += 8) {
//				for (i2 = 0; i2 < this.rows; ++i2) {
//					i = 2 * i2;
//					j = 2 * this.rows + 2 * i2;
//					k = j + 2 * this.rows;
//					l = k + 2 * this.rows;
//					this.t[i] = paramArrayOfDouble[i2][i1];
//					this.t[(i + 1)] = paramArrayOfDouble[i2][(i1 + 1)];
//					this.t[j] = paramArrayOfDouble[i2][(i1 + 2)];
//					this.t[(j + 1)] = paramArrayOfDouble[i2][(i1 + 3)];
//					this.t[k] = paramArrayOfDouble[i2][(i1 + 4)];
//					this.t[(k + 1)] = paramArrayOfDouble[i2][(i1 + 5)];
//					this.t[l] = paramArrayOfDouble[i2][(i1 + 6)];
//					this.t[(l + 1)] = paramArrayOfDouble[i2][(i1 + 7)];
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
//					paramArrayOfDouble[i2][i1] = this.t[i];
//					paramArrayOfDouble[i2][(i1 + 1)] = this.t[(i + 1)];
//					paramArrayOfDouble[i2][(i1 + 2)] = this.t[j];
//					paramArrayOfDouble[i2][(i1 + 3)] = this.t[(j + 1)];
//					paramArrayOfDouble[i2][(i1 + 4)] = this.t[k];
//					paramArrayOfDouble[i2][(i1 + 5)] = this.t[(k + 1)];
//					paramArrayOfDouble[i2][(i1 + 6)] = this.t[l];
//					paramArrayOfDouble[i2][(i1 + 7)] = this.t[(l + 1)];
//				}
//			}
//		} else if (this.columns == 4) {
//			for (i1 = 0; i1 < this.rows; ++i1) {
//				i = 2 * i1;
//				j = 2 * this.rows + 2 * i1;
//				this.t[i] = paramArrayOfDouble[i1][0];
//				this.t[(i + 1)] = paramArrayOfDouble[i1][1];
//				this.t[j] = paramArrayOfDouble[i1][2];
//				this.t[(j + 1)] = paramArrayOfDouble[i1][3];
//			}
//			this.fftRows.complexInverse(this.t, 0, paramBoolean);
//			this.fftRows.complexInverse(this.t, 2 * this.rows, paramBoolean);
//			for (i1 = 0; i1 < this.rows; ++i1) {
//				i = 2 * i1;
//				j = 2 * this.rows + 2 * i1;
//				paramArrayOfDouble[i1][0] = this.t[i];
//				paramArrayOfDouble[i1][1] = this.t[(i + 1)];
//				paramArrayOfDouble[i1][2] = this.t[j];
//				paramArrayOfDouble[i1][3] = this.t[(j + 1)];
//			}
//		} else {
//			if (this.columns != 2)
//				return;
//			for (i1 = 0; i1 < this.rows; ++i1) {
//				i = 2 * i1;
//				this.t[i] = paramArrayOfDouble[i1][0];
//				this.t[(i + 1)] = paramArrayOfDouble[i1][1];
//			}
//			this.fftRows.complexInverse(this.t, 0, paramBoolean);
//			for (i1 = 0; i1 < this.rows; ++i1) {
//				i = 2 * i1;
//				paramArrayOfDouble[i1][0] = this.t[i];
//				paramArrayOfDouble[i1][1] = this.t[(i + 1)];
//			}
//		}
//	}
//
//	private void xdft2d0_subth1(int paramInt1, int paramInt2,
//			double[] paramArrayOfDouble, boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		Future[] arrayOfFuture = new Future[i];
//		for (int j = 0; j < i; ++j) {
//			int k = j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt1,
//					paramInt2, k, i, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int i;
//					if (this.val$icr == 0) {
//						if (this.val$isgn == -1) {
//							i = this.val$n0;
//							while (i < DoubleFFT_2D.this.rows) {
//								DoubleFFT_2D.this.fftColumns.complexForward(
//										this.val$a, i
//												* DoubleFFT_2D.this.columns);
//								i += this.val$nthreads;
//							}
//						} else {
//							i = this.val$n0;
//							while (i < DoubleFFT_2D.this.rows) {
//								DoubleFFT_2D.this.fftColumns.complexInverse(
//										this.val$a, i
//												* DoubleFFT_2D.this.columns,
//										this.val$scale);
//								i += this.val$nthreads;
//							}
//						}
//					} else if (this.val$isgn == 1) {
//						i = this.val$n0;
//						while (i < DoubleFFT_2D.this.rows) {
//							DoubleFFT_2D.this.fftColumns.realForward(
//									this.val$a, i * DoubleFFT_2D.this.columns);
//							i += this.val$nthreads;
//						}
//					} else {
//						i = this.val$n0;
//						while (i < DoubleFFT_2D.this.rows) {
//							DoubleFFT_2D.this.fftColumns.realInverse(
//									this.val$a, i * DoubleFFT_2D.this.columns,
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
//	private void xdft2d0_subth2(int paramInt1, int paramInt2,
//			double[] paramArrayOfDouble, boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		Future[] arrayOfFuture = new Future[i];
//		for (int j = 0; j < i; ++j) {
//			int k = j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt1,
//					paramInt2, k, i, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int i;
//					if (this.val$icr == 0) {
//						if (this.val$isgn == -1) {
//							i = this.val$n0;
//							while (i < DoubleFFT_2D.this.rows) {
//								DoubleFFT_2D.this.fftColumns.complexForward(
//										this.val$a, i
//												* DoubleFFT_2D.this.columns);
//								i += this.val$nthreads;
//							}
//						} else {
//							i = this.val$n0;
//							while (i < DoubleFFT_2D.this.rows) {
//								DoubleFFT_2D.this.fftColumns.complexInverse(
//										this.val$a, i
//												* DoubleFFT_2D.this.columns,
//										this.val$scale);
//								i += this.val$nthreads;
//							}
//						}
//					} else if (this.val$isgn == 1) {
//						i = this.val$n0;
//						while (i < DoubleFFT_2D.this.rows) {
//							DoubleFFT_2D.this.fftColumns.realForward(
//									this.val$a, i * DoubleFFT_2D.this.columns);
//							i += this.val$nthreads;
//						}
//					} else {
//						i = this.val$n0;
//						while (i < DoubleFFT_2D.this.rows) {
//							DoubleFFT_2D.this.fftColumns.realInverse2(
//									this.val$a, i * DoubleFFT_2D.this.columns,
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
//	private void xdft2d0_subth1(int paramInt1, int paramInt2,
//			double[][] paramArrayOfDouble, boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		Future[] arrayOfFuture = new Future[i];
//		for (int j = 0; j < i; ++j) {
//			int k = j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt1,
//					paramInt2, k, i, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int i;
//					if (this.val$icr == 0) {
//						if (this.val$isgn == -1) {
//							i = this.val$n0;
//							while (i < DoubleFFT_2D.this.rows) {
//								DoubleFFT_2D.this.fftColumns
//										.complexForward(this.val$a[i]);
//								i += this.val$nthreads;
//							}
//						} else {
//							i = this.val$n0;
//							while (i < DoubleFFT_2D.this.rows) {
//								DoubleFFT_2D.this.fftColumns.complexInverse(
//										this.val$a[i], this.val$scale);
//								i += this.val$nthreads;
//							}
//						}
//					} else if (this.val$isgn == 1) {
//						i = this.val$n0;
//						while (i < DoubleFFT_2D.this.rows) {
//							DoubleFFT_2D.this.fftColumns
//									.realForward(this.val$a[i]);
//							i += this.val$nthreads;
//						}
//					} else {
//						i = this.val$n0;
//						while (i < DoubleFFT_2D.this.rows) {
//							DoubleFFT_2D.this.fftColumns.realInverse(
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
//	private void xdft2d0_subth2(int paramInt1, int paramInt2,
//			double[][] paramArrayOfDouble, boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		Future[] arrayOfFuture = new Future[i];
//		for (int j = 0; j < i; ++j) {
//			int k = j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt1,
//					paramInt2, k, i, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int i;
//					if (this.val$icr == 0) {
//						if (this.val$isgn == -1) {
//							i = this.val$n0;
//							while (i < DoubleFFT_2D.this.rows) {
//								DoubleFFT_2D.this.fftColumns
//										.complexForward(this.val$a[i]);
//								i += this.val$nthreads;
//							}
//						} else {
//							i = this.val$n0;
//							while (i < DoubleFFT_2D.this.rows) {
//								DoubleFFT_2D.this.fftColumns.complexInverse(
//										this.val$a[i], this.val$scale);
//								i += this.val$nthreads;
//							}
//						}
//					} else if (this.val$isgn == 1) {
//						i = this.val$n0;
//						while (i < DoubleFFT_2D.this.rows) {
//							DoubleFFT_2D.this.fftColumns
//									.realForward(this.val$a[i]);
//							i += this.val$nthreads;
//						}
//					} else {
//						i = this.val$n0;
//						while (i < DoubleFFT_2D.this.rows) {
//							DoubleFFT_2D.this.fftColumns.realInverse2(
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
//	private void cdft2d_subth(int paramInt, double[] paramArrayOfDouble,
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
//					k, i1, i2, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int i2;
//					int i3;
//					int i;
//					int j;
//					int k;
//					int l;
//					int i1;
//					if (this.val$isgn == -1) {
//						if (DoubleFFT_2D.this.columns > 4 * this.val$nthreads) {
//							i2 = 8 * this.val$n0;
//							while (i2 < DoubleFFT_2D.this.columns) {
//								for (i3 = 0; i3 < DoubleFFT_2D.this.rows; ++i3) {
//									i = i3 * DoubleFFT_2D.this.columns + i2;
//									j = this.val$startt + 2 * i3;
//									k = this.val$startt + 2
//											* DoubleFFT_2D.this.rows + 2 * i3;
//									l = k + 2 * DoubleFFT_2D.this.rows;
//									i1 = l + 2 * DoubleFFT_2D.this.rows;
//									DoubleFFT_2D.this.t[j] = this.val$a[i];
//									DoubleFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//									DoubleFFT_2D.this.t[k] = this.val$a[(i + 2)];
//									DoubleFFT_2D.this.t[(k + 1)] = this.val$a[(i + 3)];
//									DoubleFFT_2D.this.t[l] = this.val$a[(i + 4)];
//									DoubleFFT_2D.this.t[(l + 1)] = this.val$a[(i + 5)];
//									DoubleFFT_2D.this.t[i1] = this.val$a[(i + 6)];
//									DoubleFFT_2D.this.t[(i1 + 1)] = this.val$a[(i + 7)];
//								}
//								DoubleFFT_2D.this.fftRows.complexForward(
//										DoubleFFT_2D.this.t, this.val$startt);
//								DoubleFFT_2D.this.fftRows.complexForward(
//										DoubleFFT_2D.this.t, this.val$startt
//												+ 2 * DoubleFFT_2D.this.rows);
//								DoubleFFT_2D.this.fftRows.complexForward(
//										DoubleFFT_2D.this.t, this.val$startt
//												+ 4 * DoubleFFT_2D.this.rows);
//								DoubleFFT_2D.this.fftRows.complexForward(
//										DoubleFFT_2D.this.t, this.val$startt
//												+ 6 * DoubleFFT_2D.this.rows);
//								for (i3 = 0; i3 < DoubleFFT_2D.this.rows; ++i3) {
//									i = i3 * DoubleFFT_2D.this.columns + i2;
//									j = this.val$startt + 2 * i3;
//									k = this.val$startt + 2
//											* DoubleFFT_2D.this.rows + 2 * i3;
//									l = k + 2 * DoubleFFT_2D.this.rows;
//									i1 = l + 2 * DoubleFFT_2D.this.rows;
//									this.val$a[i] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[j];
//									this.val$a[(i + 1)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[(j + 1)];
//									this.val$a[(i + 2)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[k];
//									this.val$a[(i + 3)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[(k + 1)];
//									this.val$a[(i + 4)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[l];
//									this.val$a[(i + 5)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[(l + 1)];
//									this.val$a[(i + 6)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[i1];
//									this.val$a[(i + 7)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[(i1 + 1)];
//								}
//								i2 += 8 * this.val$nthreads;
//							}
//						} else if (DoubleFFT_2D.this.columns == 4 * this.val$nthreads) {
//							for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//								i = i2 * DoubleFFT_2D.this.columns + 4
//										* this.val$n0;
//								j = this.val$startt + 2 * i2;
//								k = this.val$startt + 2
//										* DoubleFFT_2D.this.rows + 2 * i2;
//								DoubleFFT_2D.this.t[j] = this.val$a[i];
//								DoubleFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//								DoubleFFT_2D.this.t[k] = this.val$a[(i + 2)];
//								DoubleFFT_2D.this.t[(k + 1)] = this.val$a[(i + 3)];
//							}
//							DoubleFFT_2D.this.fftRows.complexForward(
//									DoubleFFT_2D.this.t, this.val$startt);
//							DoubleFFT_2D.this.fftRows.complexForward(
//									DoubleFFT_2D.this.t, this.val$startt + 2
//											* DoubleFFT_2D.this.rows);
//							for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//								i = i2 * DoubleFFT_2D.this.columns + 4
//										* this.val$n0;
//								j = this.val$startt + 2 * i2;
//								k = this.val$startt + 2
//										* DoubleFFT_2D.this.rows + 2 * i2;
//								this.val$a[i] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[j];
//								this.val$a[(i + 1)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(j + 1)];
//								this.val$a[(i + 2)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[k];
//								this.val$a[(i + 3)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(k + 1)];
//							}
//						} else {
//							if (DoubleFFT_2D.this.columns != 2 * this.val$nthreads)
//								return;
//							for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//								i = i2 * DoubleFFT_2D.this.columns + 2
//										* this.val$n0;
//								j = this.val$startt + 2 * i2;
//								DoubleFFT_2D.this.t[j] = this.val$a[i];
//								DoubleFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//							}
//							DoubleFFT_2D.this.fftRows.complexForward(
//									DoubleFFT_2D.this.t, this.val$startt);
//							for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//								i = i2 * DoubleFFT_2D.this.columns + 2
//										* this.val$n0;
//								j = this.val$startt + 2 * i2;
//								this.val$a[i] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[j];
//								this.val$a[(i + 1)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(j + 1)];
//							}
//						}
//					} else if (DoubleFFT_2D.this.columns > 4 * this.val$nthreads) {
//						i2 = 8 * this.val$n0;
//						while (i2 < DoubleFFT_2D.this.columns) {
//							for (i3 = 0; i3 < DoubleFFT_2D.this.rows; ++i3) {
//								i = i3 * DoubleFFT_2D.this.columns + i2;
//								j = this.val$startt + 2 * i3;
//								k = this.val$startt + 2
//										* DoubleFFT_2D.this.rows + 2 * i3;
//								l = k + 2 * DoubleFFT_2D.this.rows;
//								i1 = l + 2 * DoubleFFT_2D.this.rows;
//								DoubleFFT_2D.this.t[j] = this.val$a[i];
//								DoubleFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//								DoubleFFT_2D.this.t[k] = this.val$a[(i + 2)];
//								DoubleFFT_2D.this.t[(k + 1)] = this.val$a[(i + 3)];
//								DoubleFFT_2D.this.t[l] = this.val$a[(i + 4)];
//								DoubleFFT_2D.this.t[(l + 1)] = this.val$a[(i + 5)];
//								DoubleFFT_2D.this.t[i1] = this.val$a[(i + 6)];
//								DoubleFFT_2D.this.t[(i1 + 1)] = this.val$a[(i + 7)];
//							}
//							DoubleFFT_2D.this.fftRows.complexInverse(
//									DoubleFFT_2D.this.t, this.val$startt,
//									this.val$scale);
//							DoubleFFT_2D.this.fftRows.complexInverse(
//									DoubleFFT_2D.this.t, this.val$startt + 2
//											* DoubleFFT_2D.this.rows,
//									this.val$scale);
//							DoubleFFT_2D.this.fftRows.complexInverse(
//									DoubleFFT_2D.this.t, this.val$startt + 4
//											* DoubleFFT_2D.this.rows,
//									this.val$scale);
//							DoubleFFT_2D.this.fftRows.complexInverse(
//									DoubleFFT_2D.this.t, this.val$startt + 6
//											* DoubleFFT_2D.this.rows,
//									this.val$scale);
//							for (i3 = 0; i3 < DoubleFFT_2D.this.rows; ++i3) {
//								i = i3 * DoubleFFT_2D.this.columns + i2;
//								j = this.val$startt + 2 * i3;
//								k = this.val$startt + 2
//										* DoubleFFT_2D.this.rows + 2 * i3;
//								l = k + 2 * DoubleFFT_2D.this.rows;
//								i1 = l + 2 * DoubleFFT_2D.this.rows;
//								this.val$a[i] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[j];
//								this.val$a[(i + 1)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(j + 1)];
//								this.val$a[(i + 2)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[k];
//								this.val$a[(i + 3)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(k + 1)];
//								this.val$a[(i + 4)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[l];
//								this.val$a[(i + 5)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(l + 1)];
//								this.val$a[(i + 6)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[i1];
//								this.val$a[(i + 7)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(i1 + 1)];
//							}
//							i2 += 8 * this.val$nthreads;
//						}
//					} else if (DoubleFFT_2D.this.columns == 4 * this.val$nthreads) {
//						for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//							i = i2 * DoubleFFT_2D.this.columns + 4
//									* this.val$n0;
//							j = this.val$startt + 2 * i2;
//							k = this.val$startt + 2 * DoubleFFT_2D.this.rows
//									+ 2 * i2;
//							DoubleFFT_2D.this.t[j] = this.val$a[i];
//							DoubleFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//							DoubleFFT_2D.this.t[k] = this.val$a[(i + 2)];
//							DoubleFFT_2D.this.t[(k + 1)] = this.val$a[(i + 3)];
//						}
//						DoubleFFT_2D.this.fftRows.complexInverse(
//								DoubleFFT_2D.this.t, this.val$startt,
//								this.val$scale);
//						DoubleFFT_2D.this.fftRows.complexInverse(
//								DoubleFFT_2D.this.t, this.val$startt + 2
//										* DoubleFFT_2D.this.rows,
//								this.val$scale);
//						for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//							i = i2 * DoubleFFT_2D.this.columns + 4
//									* this.val$n0;
//							j = this.val$startt + 2 * i2;
//							k = this.val$startt + 2 * DoubleFFT_2D.this.rows
//									+ 2 * i2;
//							this.val$a[i] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[j];
//							this.val$a[(i + 1)] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[(j + 1)];
//							this.val$a[(i + 2)] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[k];
//							this.val$a[(i + 3)] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[(k + 1)];
//						}
//					} else {
//						if (DoubleFFT_2D.this.columns != 2 * this.val$nthreads)
//							return;
//						for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//							i = i2 * DoubleFFT_2D.this.columns + 2
//									* this.val$n0;
//							j = this.val$startt + 2 * i2;
//							DoubleFFT_2D.this.t[j] = this.val$a[i];
//							DoubleFFT_2D.this.t[(j + 1)] = this.val$a[(i + 1)];
//						}
//						DoubleFFT_2D.this.fftRows.complexInverse(
//								DoubleFFT_2D.this.t, this.val$startt,
//								this.val$scale);
//						for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//							i = i2 * DoubleFFT_2D.this.columns + 2
//									* this.val$n0;
//							j = this.val$startt + 2 * i2;
//							this.val$a[i] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[j];
//							this.val$a[(i + 1)] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[(j + 1)];
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private void cdft2d_subth(int paramInt, double[][] paramArrayOfDouble,
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
//		for (int l = 0; l < k; ++l) {
//			int i1 = l;
//			int i2 = j * l;
//			arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(paramInt,
//					k, i1, i2, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int i1;
//					int i2;
//					int i;
//					int j;
//					int k;
//					int l;
//					if (this.val$isgn == -1) {
//						if (DoubleFFT_2D.this.columns > 4 * this.val$nthreads) {
//							i1 = 8 * this.val$n0;
//							while (i1 < DoubleFFT_2D.this.columns) {
//								for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* DoubleFFT_2D.this.rows + 2 * i2;
//									k = j + 2 * DoubleFFT_2D.this.rows;
//									l = k + 2 * DoubleFFT_2D.this.rows;
//									DoubleFFT_2D.this.t[i] = this.val$a[i2][i1];
//									DoubleFFT_2D.this.t[(i + 1)] = this.val$a[i2][(i1 + 1)];
//									DoubleFFT_2D.this.t[j] = this.val$a[i2][(i1 + 2)];
//									DoubleFFT_2D.this.t[(j + 1)] = this.val$a[i2][(i1 + 3)];
//									DoubleFFT_2D.this.t[k] = this.val$a[i2][(i1 + 4)];
//									DoubleFFT_2D.this.t[(k + 1)] = this.val$a[i2][(i1 + 5)];
//									DoubleFFT_2D.this.t[l] = this.val$a[i2][(i1 + 6)];
//									DoubleFFT_2D.this.t[(l + 1)] = this.val$a[i2][(i1 + 7)];
//								}
//								DoubleFFT_2D.this.fftRows.complexForward(
//										DoubleFFT_2D.this.t, this.val$startt);
//								DoubleFFT_2D.this.fftRows.complexForward(
//										DoubleFFT_2D.this.t, this.val$startt
//												+ 2 * DoubleFFT_2D.this.rows);
//								DoubleFFT_2D.this.fftRows.complexForward(
//										DoubleFFT_2D.this.t, this.val$startt
//												+ 4 * DoubleFFT_2D.this.rows);
//								DoubleFFT_2D.this.fftRows.complexForward(
//										DoubleFFT_2D.this.t, this.val$startt
//												+ 6 * DoubleFFT_2D.this.rows);
//								for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//									i = this.val$startt + 2 * i2;
//									j = this.val$startt + 2
//											* DoubleFFT_2D.this.rows + 2 * i2;
//									k = j + 2 * DoubleFFT_2D.this.rows;
//									l = k + 2 * DoubleFFT_2D.this.rows;
//									this.val$a[i2][i1] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[i];
//									this.val$a[i2][(i1 + 1)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[(i + 1)];
//									this.val$a[i2][(i1 + 2)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[j];
//									this.val$a[i2][(i1 + 3)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[(j + 1)];
//									this.val$a[i2][(i1 + 4)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[k];
//									this.val$a[i2][(i1 + 5)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[(k + 1)];
//									this.val$a[i2][(i1 + 6)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[l];
//									this.val$a[i2][(i1 + 7)] = DoubleFFT_2D
//											.access$400(DoubleFFT_2D.this)[(l + 1)];
//								}
//								i1 += 8 * this.val$nthreads;
//							}
//						} else if (DoubleFFT_2D.this.columns == 4 * this.val$nthreads) {
//							for (i1 = 0; i1 < DoubleFFT_2D.this.rows; ++i1) {
//								i = this.val$startt + 2 * i1;
//								j = this.val$startt + 2
//										* DoubleFFT_2D.this.rows + 2 * i1;
//								DoubleFFT_2D.this.t[i] = this.val$a[i1][(4 * this.val$n0)];
//								DoubleFFT_2D.this.t[(i + 1)] = this.val$a[i1][(4 * this.val$n0 + 1)];
//								DoubleFFT_2D.this.t[j] = this.val$a[i1][(4 * this.val$n0 + 2)];
//								DoubleFFT_2D.this.t[(j + 1)] = this.val$a[i1][(4 * this.val$n0 + 3)];
//							}
//							DoubleFFT_2D.this.fftRows.complexForward(
//									DoubleFFT_2D.this.t, this.val$startt);
//							DoubleFFT_2D.this.fftRows.complexForward(
//									DoubleFFT_2D.this.t, this.val$startt + 2
//											* DoubleFFT_2D.this.rows);
//							for (i1 = 0; i1 < DoubleFFT_2D.this.rows; ++i1) {
//								i = this.val$startt + 2 * i1;
//								j = this.val$startt + 2
//										* DoubleFFT_2D.this.rows + 2 * i1;
//								this.val$a[i1][(4 * this.val$n0)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[i];
//								this.val$a[i1][(4 * this.val$n0 + 1)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(i + 1)];
//								this.val$a[i1][(4 * this.val$n0 + 2)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[j];
//								this.val$a[i1][(4 * this.val$n0 + 3)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(j + 1)];
//							}
//						} else {
//							if (DoubleFFT_2D.this.columns != 2 * this.val$nthreads)
//								return;
//							for (i1 = 0; i1 < DoubleFFT_2D.this.rows; ++i1) {
//								i = this.val$startt + 2 * i1;
//								DoubleFFT_2D.this.t[i] = this.val$a[i1][(2 * this.val$n0)];
//								DoubleFFT_2D.this.t[(i + 1)] = this.val$a[i1][(2 * this.val$n0 + 1)];
//							}
//							DoubleFFT_2D.this.fftRows.complexForward(
//									DoubleFFT_2D.this.t, this.val$startt);
//							for (i1 = 0; i1 < DoubleFFT_2D.this.rows; ++i1) {
//								i = this.val$startt + 2 * i1;
//								this.val$a[i1][(2 * this.val$n0)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[i];
//								this.val$a[i1][(2 * this.val$n0 + 1)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(i + 1)];
//							}
//						}
//					} else if (DoubleFFT_2D.this.columns > 4 * this.val$nthreads) {
//						i1 = 8 * this.val$n0;
//						while (i1 < DoubleFFT_2D.this.columns) {
//							for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//								i = this.val$startt + 2 * i2;
//								j = this.val$startt + 2
//										* DoubleFFT_2D.this.rows + 2 * i2;
//								k = j + 2 * DoubleFFT_2D.this.rows;
//								l = k + 2 * DoubleFFT_2D.this.rows;
//								DoubleFFT_2D.this.t[i] = this.val$a[i2][i1];
//								DoubleFFT_2D.this.t[(i + 1)] = this.val$a[i2][(i1 + 1)];
//								DoubleFFT_2D.this.t[j] = this.val$a[i2][(i1 + 2)];
//								DoubleFFT_2D.this.t[(j + 1)] = this.val$a[i2][(i1 + 3)];
//								DoubleFFT_2D.this.t[k] = this.val$a[i2][(i1 + 4)];
//								DoubleFFT_2D.this.t[(k + 1)] = this.val$a[i2][(i1 + 5)];
//								DoubleFFT_2D.this.t[l] = this.val$a[i2][(i1 + 6)];
//								DoubleFFT_2D.this.t[(l + 1)] = this.val$a[i2][(i1 + 7)];
//							}
//							DoubleFFT_2D.this.fftRows.complexInverse(
//									DoubleFFT_2D.this.t, this.val$startt,
//									this.val$scale);
//							DoubleFFT_2D.this.fftRows.complexInverse(
//									DoubleFFT_2D.this.t, this.val$startt + 2
//											* DoubleFFT_2D.this.rows,
//									this.val$scale);
//							DoubleFFT_2D.this.fftRows.complexInverse(
//									DoubleFFT_2D.this.t, this.val$startt + 4
//											* DoubleFFT_2D.this.rows,
//									this.val$scale);
//							DoubleFFT_2D.this.fftRows.complexInverse(
//									DoubleFFT_2D.this.t, this.val$startt + 6
//											* DoubleFFT_2D.this.rows,
//									this.val$scale);
//							for (i2 = 0; i2 < DoubleFFT_2D.this.rows; ++i2) {
//								i = this.val$startt + 2 * i2;
//								j = this.val$startt + 2
//										* DoubleFFT_2D.this.rows + 2 * i2;
//								k = j + 2 * DoubleFFT_2D.this.rows;
//								l = k + 2 * DoubleFFT_2D.this.rows;
//								this.val$a[i2][i1] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[i];
//								this.val$a[i2][(i1 + 1)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(i + 1)];
//								this.val$a[i2][(i1 + 2)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[j];
//								this.val$a[i2][(i1 + 3)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(j + 1)];
//								this.val$a[i2][(i1 + 4)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[k];
//								this.val$a[i2][(i1 + 5)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(k + 1)];
//								this.val$a[i2][(i1 + 6)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[l];
//								this.val$a[i2][(i1 + 7)] = DoubleFFT_2D
//										.access$400(DoubleFFT_2D.this)[(l + 1)];
//							}
//							i1 += 8 * this.val$nthreads;
//						}
//					} else if (DoubleFFT_2D.this.columns == 4 * this.val$nthreads) {
//						for (i1 = 0; i1 < DoubleFFT_2D.this.rows; ++i1) {
//							i = this.val$startt + 2 * i1;
//							j = this.val$startt + 2 * DoubleFFT_2D.this.rows
//									+ 2 * i1;
//							DoubleFFT_2D.this.t[i] = this.val$a[i1][(4 * this.val$n0)];
//							DoubleFFT_2D.this.t[(i + 1)] = this.val$a[i1][(4 * this.val$n0 + 1)];
//							DoubleFFT_2D.this.t[j] = this.val$a[i1][(4 * this.val$n0 + 2)];
//							DoubleFFT_2D.this.t[(j + 1)] = this.val$a[i1][(4 * this.val$n0 + 3)];
//						}
//						DoubleFFT_2D.this.fftRows.complexInverse(
//								DoubleFFT_2D.this.t, this.val$startt,
//								this.val$scale);
//						DoubleFFT_2D.this.fftRows.complexInverse(
//								DoubleFFT_2D.this.t, this.val$startt + 2
//										* DoubleFFT_2D.this.rows,
//								this.val$scale);
//						for (i1 = 0; i1 < DoubleFFT_2D.this.rows; ++i1) {
//							i = this.val$startt + 2 * i1;
//							j = this.val$startt + 2 * DoubleFFT_2D.this.rows
//									+ 2 * i1;
//							this.val$a[i1][(4 * this.val$n0)] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[i];
//							this.val$a[i1][(4 * this.val$n0 + 1)] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[(i + 1)];
//							this.val$a[i1][(4 * this.val$n0 + 2)] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[j];
//							this.val$a[i1][(4 * this.val$n0 + 3)] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[(j + 1)];
//						}
//					} else {
//						if (DoubleFFT_2D.this.columns != 2 * this.val$nthreads)
//							return;
//						for (i1 = 0; i1 < DoubleFFT_2D.this.rows; ++i1) {
//							i = this.val$startt + 2 * i1;
//							DoubleFFT_2D.this.t[i] = this.val$a[i1][(2 * this.val$n0)];
//							DoubleFFT_2D.this.t[(i + 1)] = this.val$a[i1][(2 * this.val$n0 + 1)];
//						}
//						DoubleFFT_2D.this.fftRows.complexInverse(
//								DoubleFFT_2D.this.t, this.val$startt,
//								this.val$scale);
//						for (i1 = 0; i1 < DoubleFFT_2D.this.rows; ++i1) {
//							i = this.val$startt + 2 * i1;
//							this.val$a[i1][(2 * this.val$n0)] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[i];
//							this.val$a[i1][(2 * this.val$n0 + 1)] = DoubleFFT_2D
//									.access$400(DoubleFFT_2D.this)[(i + 1)];
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private void fillSymmetric(double[] paramArrayOfDouble) {
//		int i = 2 * this.columns;
//		int i2 = this.rows / 2;
//		int k;
//		for (int i3 = this.rows - 1; i3 >= 1; --i3) {
//			j = i3 * this.columns;
//			k = 2 * j;
//			for (int i4 = 0; i4 < this.columns; i4 += 2) {
//				paramArrayOfDouble[(k + i4)] = paramArrayOfDouble[(j + i4)];
//				paramArrayOfDouble[(j + i4)] = 0.0D;
//				paramArrayOfDouble[(k + i4 + 1)] = paramArrayOfDouble[(j + i4 + 1)];
//				paramArrayOfDouble[(j + i4 + 1)] = 0.0D;
//			}
//		}
//		i3 = ConcurrencyUtils.getNumberOfThreads();
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
//						i10, i7, paramArrayOfDouble, i11, i12) {
//					public void run() {
//						int i;
//						int j;
//						int k;
//						for (int i1 = this.val$l1offa; i1 < this.val$l1stopa; ++i1) {
//							i = i1 * this.val$newn2;
//							j = (DoubleFFT_2D.this.rows - i1) * this.val$newn2;
//							k = i + DoubleFFT_2D.this.columns;
//							this.val$a[k] = this.val$a[(j + 1)];
//							this.val$a[(k + 1)] = (-this.val$a[j]);
//						}
//						int i2;
//						int l;
//						for (i1 = this.val$l1offa; i1 < this.val$l1stopa; ++i1) {
//							i = i1 * this.val$newn2;
//							k = (DoubleFFT_2D.this.rows - i1 + 1)
//									* this.val$newn2;
//							for (i2 = DoubleFFT_2D.this.columns + 2; i2 < this.val$newn2; i2 += 2) {
//								j = k - i2;
//								l = i + i2;
//								this.val$a[l] = this.val$a[j];
//								this.val$a[(l + 1)] = (-this.val$a[(j + 1)]);
//							}
//						}
//						for (i1 = this.val$l2offa; i1 < this.val$l2stopa; ++i1) {
//							k = (DoubleFFT_2D.this.rows - i1)
//									% DoubleFFT_2D.this.rows * this.val$newn2;
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
//				paramArrayOfDouble[(k + this.columns)] = paramArrayOfDouble[(l + 1)];
//				paramArrayOfDouble[(k + this.columns + 1)] = (-paramArrayOfDouble[l]);
//			}
//			for (i5 = 1; i5 < i2; ++i5) {
//				k = i5 * i;
//				l = (this.rows - i5 + 1) * i;
//				for (i6 = this.columns + 2; i6 < i; i6 += 2) {
//					paramArrayOfDouble[(k + i6)] = paramArrayOfDouble[(l - i6)];
//					paramArrayOfDouble[(k + i6 + 1)] = (-paramArrayOfDouble[(l
//							- i6 + 1)]);
//				}
//			}
//			for (i5 = 0; i5 <= this.rows / 2; ++i5) {
//				j = i5 * i;
//				int i1 = (this.rows - i5) % this.rows * i;
//				for (i6 = 0; i6 < i; i6 += 2) {
//					k = j + i6;
//					l = i1 + (i - i6) % i;
//					paramArrayOfDouble[l] = paramArrayOfDouble[k];
//					paramArrayOfDouble[(l + 1)] = (-paramArrayOfDouble[(k + 1)]);
//				}
//			}
//		}
//		paramArrayOfDouble[this.columns] = (-paramArrayOfDouble[1]);
//		paramArrayOfDouble[1] = 0.0D;
//		int j = i2 * i;
//		paramArrayOfDouble[(j + this.columns)] = (-paramArrayOfDouble[(j + 1)]);
//		paramArrayOfDouble[(j + 1)] = 0.0D;
//		paramArrayOfDouble[(j + this.columns + 1)] = 0.0D;
//	}
//
//	private void fillSymmetric(double[][] paramArrayOfDouble) {
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
//						i4, paramArrayOfDouble, i, i5, i6) {
//					public void run() {
//						int i;
//						for (int k = this.val$l1offa; k < this.val$l1stopa; ++k) {
//							i = DoubleFFT_2D.this.rows - k;
//							this.val$a[k][DoubleFFT_2D.this.columns] = this.val$a[i][1];
//							this.val$a[k][(DoubleFFT_2D.this.columns + 1)] = (-this.val$a[i][0]);
//						}
//						int l;
//						int j;
//						for (k = this.val$l1offa; k < this.val$l1stopa; ++k) {
//							i = DoubleFFT_2D.this.rows - k;
//							for (l = DoubleFFT_2D.this.columns + 2; l < this.val$newn2; l += 2) {
//								j = this.val$newn2 - l;
//								this.val$a[k][l] = this.val$a[i][j];
//								this.val$a[k][(l + 1)] = (-this.val$a[i][(j + 1)]);
//							}
//						}
//						for (k = this.val$l2offa; k < this.val$l2stopa; ++k) {
//							i = (DoubleFFT_2D.this.rows - k)
//									% DoubleFFT_2D.this.rows;
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
//				paramArrayOfDouble[l][this.columns] = paramArrayOfDouble[i1][1];
//				paramArrayOfDouble[l][(this.columns + 1)] = (-paramArrayOfDouble[i1][0]);
//			}
//			for (l = 1; l < j; ++l) {
//				i1 = this.rows - l;
//				for (i2 = this.columns + 2; i2 < i; i2 += 2) {
//					i3 = i - i2;
//					paramArrayOfDouble[l][i2] = paramArrayOfDouble[i1][i3];
//					paramArrayOfDouble[l][(i2 + 1)] = (-paramArrayOfDouble[i1][(i3 + 1)]);
//				}
//			}
//			for (l = 0; l <= this.rows / 2; ++l) {
//				i1 = (this.rows - l) % this.rows;
//				for (i2 = 0; i2 < i; i2 += 2) {
//					i3 = (i - i2) % i;
//					paramArrayOfDouble[i1][i3] = paramArrayOfDouble[l][i2];
//					paramArrayOfDouble[i1][(i3 + 1)] = (-paramArrayOfDouble[l][(i2 + 1)]);
//				}
//			}
//		}
//		paramArrayOfDouble[0][this.columns] = (-paramArrayOfDouble[0][1]);
//		paramArrayOfDouble[0][1] = 0.0D;
//		paramArrayOfDouble[j][this.columns] = (-paramArrayOfDouble[j][1]);
//		paramArrayOfDouble[j][1] = 0.0D;
//		paramArrayOfDouble[j][(this.columns + 1)] = 0.0D;
//	}
//}