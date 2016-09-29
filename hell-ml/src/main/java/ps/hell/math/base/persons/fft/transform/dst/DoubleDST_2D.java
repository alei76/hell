package ps.hell.math.base.persons.fft.transform.dst;//package ps.landerbuluse.ml.math.fft.transform.dst;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class DoubleDST_2D {
//	private int rows;
//	private int columns;
//	private double[] t;
//	private DoubleDST_1D dstColumns;
//	private DoubleDST_1D dstRows;
//	private int oldNthreads;
//	private int nt;
//	private boolean isPowerOfTwo = false;
//	private boolean useThreads = false;
//
//	public DoubleDST_2D(int paramInt1, int paramInt2) {
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
//			this.nt = (4 * this.oldNthreads * paramInt1);
//			if (paramInt2 == 2 * this.oldNthreads)
//				this.nt >>= 1;
//			else if (paramInt2 < 2 * this.oldNthreads)
//				this.nt >>= 2;
//			this.t = new double[this.nt];
//		}
//		this.dstColumns = new DoubleDST_1D(paramInt2);
//		if (paramInt2 == paramInt1)
//			this.dstRows = this.dstColumns;
//		else
//			this.dstRows = new DoubleDST_1D(paramInt1);
//	}
//
//	public void forward(double[] paramArrayOfDouble, boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			if (i != this.oldNthreads) {
//				this.nt = (4 * i * this.rows);
//				if (this.columns == 2 * i)
//					this.nt >>= 1;
//				else if (this.columns < 2 * i)
//					this.nt >>= 2;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt2d_subth(-1, paramArrayOfDouble, paramBoolean);
//				ddxt2d0_subth(-1, paramArrayOfDouble, paramBoolean);
//			} else {
//				ddxt2d_sub(-1, paramArrayOfDouble, paramBoolean);
//				for (int j = 0; j < this.rows; ++j)
//					this.dstColumns.forward(paramArrayOfDouble, j
//							* this.columns, paramBoolean);
//			}
//		} else {
//			int l;
//			int i1;
//			if ((i > 1) && (this.useThreads) && (this.rows >= i)
//					&& (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				l = this.rows / i;
//				int i2;
//				int i3;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.rows : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								DoubleDST_2D.this.dstColumns.forward(
//										this.val$a, i
//												* DoubleDST_2D.this.columns,
//										this.val$scale);
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
//							double[] arrayOfDouble = new double[DoubleDST_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								for (int j = 0; j < DoubleDST_2D.this.rows; ++j)
//									arrayOfDouble[j] = this.val$a[(j
//											* DoubleDST_2D
//													.access$000(DoubleDST_2D.this) + i)];
//								DoubleDST_2D.this.dstRows.forward(
//										arrayOfDouble, this.val$scale);
//								for (j = 0; j < DoubleDST_2D.this.rows; ++j)
//									this.val$a[(j * DoubleDST_2D.this.columns + i)] = arrayOfDouble[j];
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.dstColumns.forward(paramArrayOfDouble, k
//							* this.columns, paramBoolean);
//				double[] arrayOfDouble = new double[this.rows];
//				for (l = 0; l < this.columns; ++l) {
//					for (i1 = 0; i1 < this.rows; ++i1)
//						arrayOfDouble[i1] = paramArrayOfDouble[(i1
//								* this.columns + l)];
//					this.dstRows.forward(arrayOfDouble, paramBoolean);
//					for (i1 = 0; i1 < this.rows; ++i1)
//						paramArrayOfDouble[(i1 * this.columns + l)] = arrayOfDouble[i1];
//				}
//			}
//		}
//	}
//
//	public void forward(double[][] paramArrayOfDouble, boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			if (i != this.oldNthreads) {
//				this.nt = (4 * i * this.rows);
//				if (this.columns == 2 * i)
//					this.nt >>= 1;
//				else if (this.columns < 2 * i)
//					this.nt >>= 2;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt2d_subth(-1, paramArrayOfDouble, paramBoolean);
//				ddxt2d0_subth(-1, paramArrayOfDouble, paramBoolean);
//			} else {
//				ddxt2d_sub(-1, paramArrayOfDouble, paramBoolean);
//				for (int j = 0; j < this.rows; ++j)
//					this.dstColumns
//							.forward(paramArrayOfDouble[j], paramBoolean);
//			}
//		} else {
//			int l;
//			int i1;
//			if ((i > 1) && (this.useThreads) && (this.rows >= i)
//					&& (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				l = this.rows / i;
//				int i2;
//				int i3;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.rows : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								DoubleDST_2D.this.dstColumns.forward(
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
//							double[] arrayOfDouble = new double[DoubleDST_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								for (int j = 0; j < DoubleDST_2D.this.rows; ++j)
//									arrayOfDouble[j] = this.val$a[j][i];
//								DoubleDST_2D.this.dstRows.forward(
//										arrayOfDouble, this.val$scale);
//								for (j = 0; j < DoubleDST_2D.this.rows; ++j)
//									this.val$a[j][i] = arrayOfDouble[j];
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.dstColumns
//							.forward(paramArrayOfDouble[k], paramBoolean);
//				double[] arrayOfDouble = new double[this.rows];
//				for (l = 0; l < this.columns; ++l) {
//					for (i1 = 0; i1 < this.rows; ++i1)
//						arrayOfDouble[i1] = paramArrayOfDouble[i1][l];
//					this.dstRows.forward(arrayOfDouble, paramBoolean);
//					for (i1 = 0; i1 < this.rows; ++i1)
//						paramArrayOfDouble[i1][l] = arrayOfDouble[i1];
//				}
//			}
//		}
//	}
//
//	public void inverse(double[] paramArrayOfDouble, boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			if (i != this.oldNthreads) {
//				this.nt = (4 * i * this.rows);
//				if (this.columns == 2 * i)
//					this.nt >>= 1;
//				else if (this.columns < 2 * i)
//					this.nt >>= 2;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt2d_subth(1, paramArrayOfDouble, paramBoolean);
//				ddxt2d0_subth(1, paramArrayOfDouble, paramBoolean);
//			} else {
//				ddxt2d_sub(1, paramArrayOfDouble, paramBoolean);
//				for (int j = 0; j < this.rows; ++j)
//					this.dstColumns.inverse(paramArrayOfDouble, j
//							* this.columns, paramBoolean);
//			}
//		} else {
//			int l;
//			int i1;
//			if ((i > 1) && (this.useThreads) && (this.rows >= i)
//					&& (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				l = this.rows / i;
//				int i2;
//				int i3;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.rows : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								DoubleDST_2D.this.dstColumns.inverse(
//										this.val$a, i
//												* DoubleDST_2D.this.columns,
//										this.val$scale);
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
//							double[] arrayOfDouble = new double[DoubleDST_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								for (int j = 0; j < DoubleDST_2D.this.rows; ++j)
//									arrayOfDouble[j] = this.val$a[(j
//											* DoubleDST_2D
//													.access$000(DoubleDST_2D.this) + i)];
//								DoubleDST_2D.this.dstRows.inverse(
//										arrayOfDouble, this.val$scale);
//								for (j = 0; j < DoubleDST_2D.this.rows; ++j)
//									this.val$a[(j * DoubleDST_2D.this.columns + i)] = arrayOfDouble[j];
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.dstColumns.inverse(paramArrayOfDouble, k
//							* this.columns, paramBoolean);
//				double[] arrayOfDouble = new double[this.rows];
//				for (l = 0; l < this.columns; ++l) {
//					for (i1 = 0; i1 < this.rows; ++i1)
//						arrayOfDouble[i1] = paramArrayOfDouble[(i1
//								* this.columns + l)];
//					this.dstRows.inverse(arrayOfDouble, paramBoolean);
//					for (i1 = 0; i1 < this.rows; ++i1)
//						paramArrayOfDouble[(i1 * this.columns + l)] = arrayOfDouble[i1];
//				}
//			}
//		}
//	}
//
//	public void inverse(double[][] paramArrayOfDouble, boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			if (i != this.oldNthreads) {
//				this.nt = (4 * i * this.rows);
//				if (this.columns == 2 * i)
//					this.nt >>= 1;
//				else if (this.columns < 2 * i)
//					this.nt >>= 2;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt2d_subth(1, paramArrayOfDouble, paramBoolean);
//				ddxt2d0_subth(1, paramArrayOfDouble, paramBoolean);
//			} else {
//				ddxt2d_sub(1, paramArrayOfDouble, paramBoolean);
//				for (int j = 0; j < this.rows; ++j)
//					this.dstColumns
//							.inverse(paramArrayOfDouble[j], paramBoolean);
//			}
//		} else {
//			int l;
//			int i1;
//			if ((i > 1) && (this.useThreads) && (this.rows >= i)
//					&& (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				l = this.rows / i;
//				int i2;
//				int i3;
//				for (i1 = 0; i1 < i; ++i1) {
//					i2 = i1 * l;
//					i3 = (i1 == i - 1) ? this.rows : i2 + l;
//					arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//							i2, i3, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								DoubleDST_2D.this.dstColumns.inverse(
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
//							double[] arrayOfDouble = new double[DoubleDST_2D.this.rows];
//							for (int i = this.val$firstColumn; i < this.val$lastColumn; ++i) {
//								for (int j = 0; j < DoubleDST_2D.this.rows; ++j)
//									arrayOfDouble[j] = this.val$a[j][i];
//								DoubleDST_2D.this.dstRows.inverse(
//										arrayOfDouble, this.val$scale);
//								for (j = 0; j < DoubleDST_2D.this.rows; ++j)
//									this.val$a[j][i] = arrayOfDouble[j];
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int k = 0; k < this.rows; ++k)
//					this.dstColumns
//							.inverse(paramArrayOfDouble[k], paramBoolean);
//				double[] arrayOfDouble = new double[this.rows];
//				for (l = 0; l < this.columns; ++l) {
//					for (i1 = 0; i1 < this.rows; ++i1)
//						arrayOfDouble[i1] = paramArrayOfDouble[i1][l];
//					this.dstRows.inverse(arrayOfDouble, paramBoolean);
//					for (i1 = 0; i1 < this.rows; ++i1)
//						paramArrayOfDouble[i1][l] = arrayOfDouble[i1];
//				}
//			}
//		}
//	}
//
//	private void ddxt2d_subth(int paramInt, double[] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int j = 4 * this.rows;
//		if (this.columns == 2 * i) {
//			j >>= 1;
//		} else if (this.columns < 2 * i) {
//			i = this.columns;
//			j >>= 2;
//		}
//		int k = i;
//		Future[] arrayOfFuture = new Future[k];
//		for (int l = 0; l < k; ++l) {
//			int i1 = l;
//			int i2 = j * l;
//			arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(k,
//					paramInt, i1, i2, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int k;
//					int i;
//					int j;
//					if (DoubleDST_2D.this.columns > 2 * this.val$nthreads) {
//						int l;
//						if (this.val$isgn == -1) {
//							k = 4 * this.val$n0;
//							while (k < DoubleDST_2D.this.columns) {
//								for (l = 0; l < DoubleDST_2D.this.rows; ++l) {
//									i = l * DoubleDST_2D.this.columns + k;
//									j = this.val$startt
//											+ DoubleDST_2D.this.rows + l;
//									DoubleDST_2D.this.t[(this.val$startt + l)] = this.val$a[i];
//									DoubleDST_2D.this.t[j] = this.val$a[(i + 1)];
//									DoubleDST_2D.this.t[(j + DoubleDST_2D.this.rows)] = this.val$a[(i + 2)];
//									DoubleDST_2D.this.t[(j + 2 * DoubleDST_2D.this.rows)] = this.val$a[(i + 3)];
//								}
//								DoubleDST_2D.this.dstRows.forward(
//										DoubleDST_2D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.forward(
//										DoubleDST_2D.this.t, this.val$startt
//												+ DoubleDST_2D.this.rows,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.forward(
//										DoubleDST_2D.this.t, this.val$startt
//												+ 2 * DoubleDST_2D.this.rows,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.forward(
//										DoubleDST_2D.this.t, this.val$startt
//												+ 3 * DoubleDST_2D.this.rows,
//										this.val$scale);
//								for (l = 0; l < DoubleDST_2D.this.rows; ++l) {
//									i = l * DoubleDST_2D.this.columns + k;
//									j = this.val$startt
//											+ DoubleDST_2D.this.rows + l;
//									this.val$a[i] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(this.val$startt + l)];
//									this.val$a[(i + 1)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[j];
//									this.val$a[(i + 2)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(j + DoubleDST_2D
//											.access$200(DoubleDST_2D.this))];
//									this.val$a[(i + 3)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(j + 2 * DoubleDST_2D
//											.access$200(DoubleDST_2D.this))];
//								}
//								k += 4 * this.val$nthreads;
//							}
//						} else {
//							k = 4 * this.val$n0;
//							while (k < DoubleDST_2D.this.columns) {
//								for (l = 0; l < DoubleDST_2D.this.rows; ++l) {
//									i = l * DoubleDST_2D.this.columns + k;
//									j = this.val$startt
//											+ DoubleDST_2D.this.rows + l;
//									DoubleDST_2D.this.t[(this.val$startt + l)] = this.val$a[i];
//									DoubleDST_2D.this.t[j] = this.val$a[(i + 1)];
//									DoubleDST_2D.this.t[(j + DoubleDST_2D.this.rows)] = this.val$a[(i + 2)];
//									DoubleDST_2D.this.t[(j + 2 * DoubleDST_2D.this.rows)] = this.val$a[(i + 3)];
//								}
//								DoubleDST_2D.this.dstRows.inverse(
//										DoubleDST_2D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.inverse(
//										DoubleDST_2D.this.t, this.val$startt
//												+ DoubleDST_2D.this.rows,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.inverse(
//										DoubleDST_2D.this.t, this.val$startt
//												+ 2 * DoubleDST_2D.this.rows,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.inverse(
//										DoubleDST_2D.this.t, this.val$startt
//												+ 3 * DoubleDST_2D.this.rows,
//										this.val$scale);
//								for (l = 0; l < DoubleDST_2D.this.rows; ++l) {
//									i = l * DoubleDST_2D.this.columns + k;
//									j = this.val$startt
//											+ DoubleDST_2D.this.rows + l;
//									this.val$a[i] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(this.val$startt + l)];
//									this.val$a[(i + 1)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[j];
//									this.val$a[(i + 2)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(j + DoubleDST_2D
//											.access$200(DoubleDST_2D.this))];
//									this.val$a[(i + 3)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(j + 2 * DoubleDST_2D
//											.access$200(DoubleDST_2D.this))];
//								}
//								k += 4 * this.val$nthreads;
//							}
//						}
//					} else if (DoubleDST_2D.this.columns == 2 * this.val$nthreads) {
//						for (k = 0; k < DoubleDST_2D.this.rows; ++k) {
//							i = k * DoubleDST_2D.this.columns + 2 * this.val$n0;
//							j = this.val$startt + k;
//							DoubleDST_2D.this.t[j] = this.val$a[i];
//							DoubleDST_2D.this.t[(j + DoubleDST_2D.this.rows)] = this.val$a[(i + 1)];
//						}
//						if (this.val$isgn == -1) {
//							DoubleDST_2D.this.dstRows.forward(
//									DoubleDST_2D.this.t, this.val$startt,
//									this.val$scale);
//							DoubleDST_2D.this.dstRows.forward(
//									DoubleDST_2D.this.t, this.val$startt
//											+ DoubleDST_2D.this.rows,
//									this.val$scale);
//						} else {
//							DoubleDST_2D.this.dstRows.inverse(
//									DoubleDST_2D.this.t, this.val$startt,
//									this.val$scale);
//							DoubleDST_2D.this.dstRows.inverse(
//									DoubleDST_2D.this.t, this.val$startt
//											+ DoubleDST_2D.this.rows,
//									this.val$scale);
//						}
//						for (k = 0; k < DoubleDST_2D.this.rows; ++k) {
//							i = k * DoubleDST_2D.this.columns + 2 * this.val$n0;
//							j = this.val$startt + k;
//							this.val$a[i] = DoubleDST_2D
//									.access$400(DoubleDST_2D.this)[j];
//							this.val$a[(i + 1)] = DoubleDST_2D
//									.access$400(DoubleDST_2D.this)[(j + DoubleDST_2D
//									.access$200(DoubleDST_2D.this))];
//						}
//					} else {
//						if (DoubleDST_2D.this.columns != this.val$nthreads)
//							return;
//						for (k = 0; k < DoubleDST_2D.this.rows; ++k)
//							DoubleDST_2D.this.t[(this.val$startt + k)] = this.val$a[(k
//									* DoubleDST_2D
//											.access$000(DoubleDST_2D.this) + this.val$n0)];
//						if (this.val$isgn == -1)
//							DoubleDST_2D.this.dstRows.forward(
//									DoubleDST_2D.this.t, this.val$startt,
//									this.val$scale);
//						else
//							DoubleDST_2D.this.dstRows.inverse(
//									DoubleDST_2D.this.t, this.val$startt,
//									this.val$scale);
//						for (k = 0; k < DoubleDST_2D.this.rows; ++k)
//							this.val$a[(k * DoubleDST_2D.this.columns + this.val$n0)] = DoubleDST_2D
//									.access$400(DoubleDST_2D.this)[(this.val$startt + k)];
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private void ddxt2d_subth(int paramInt, double[][] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int j = 4 * this.rows;
//		if (this.columns == 2 * i) {
//			j >>= 1;
//		} else if (this.columns < 2 * i) {
//			i = this.columns;
//			j >>= 2;
//		}
//		int k = i;
//		Future[] arrayOfFuture = new Future[k];
//		for (int l = 0; l < k; ++l) {
//			int i1 = l;
//			int i2 = j * l;
//			arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(k,
//					paramInt, i1, i2, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int j;
//					int i;
//					if (DoubleDST_2D.this.columns > 2 * this.val$nthreads) {
//						int k;
//						if (this.val$isgn == -1) {
//							j = 4 * this.val$n0;
//							while (j < DoubleDST_2D.this.columns) {
//								for (k = 0; k < DoubleDST_2D.this.rows; ++k) {
//									i = this.val$startt
//											+ DoubleDST_2D.this.rows + k;
//									DoubleDST_2D.this.t[(this.val$startt + k)] = this.val$a[k][j];
//									DoubleDST_2D.this.t[i] = this.val$a[k][(j + 1)];
//									DoubleDST_2D.this.t[(i + DoubleDST_2D.this.rows)] = this.val$a[k][(j + 2)];
//									DoubleDST_2D.this.t[(i + 2 * DoubleDST_2D.this.rows)] = this.val$a[k][(j + 3)];
//								}
//								DoubleDST_2D.this.dstRows.forward(
//										DoubleDST_2D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.forward(
//										DoubleDST_2D.this.t, this.val$startt
//												+ DoubleDST_2D.this.rows,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.forward(
//										DoubleDST_2D.this.t, this.val$startt
//												+ 2 * DoubleDST_2D.this.rows,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.forward(
//										DoubleDST_2D.this.t, this.val$startt
//												+ 3 * DoubleDST_2D.this.rows,
//										this.val$scale);
//								for (k = 0; k < DoubleDST_2D.this.rows; ++k) {
//									i = this.val$startt
//											+ DoubleDST_2D.this.rows + k;
//									this.val$a[k][j] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(this.val$startt + k)];
//									this.val$a[k][(j + 1)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[i];
//									this.val$a[k][(j + 2)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(i + DoubleDST_2D
//											.access$200(DoubleDST_2D.this))];
//									this.val$a[k][(j + 3)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(i + 2 * DoubleDST_2D
//											.access$200(DoubleDST_2D.this))];
//								}
//								j += 4 * this.val$nthreads;
//							}
//						} else {
//							j = 4 * this.val$n0;
//							while (j < DoubleDST_2D.this.columns) {
//								for (k = 0; k < DoubleDST_2D.this.rows; ++k) {
//									i = this.val$startt
//											+ DoubleDST_2D.this.rows + k;
//									DoubleDST_2D.this.t[(this.val$startt + k)] = this.val$a[k][j];
//									DoubleDST_2D.this.t[i] = this.val$a[k][(j + 1)];
//									DoubleDST_2D.this.t[(i + DoubleDST_2D.this.rows)] = this.val$a[k][(j + 2)];
//									DoubleDST_2D.this.t[(i + 2 * DoubleDST_2D.this.rows)] = this.val$a[k][(j + 3)];
//								}
//								DoubleDST_2D.this.dstRows.inverse(
//										DoubleDST_2D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.inverse(
//										DoubleDST_2D.this.t, this.val$startt
//												+ DoubleDST_2D.this.rows,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.inverse(
//										DoubleDST_2D.this.t, this.val$startt
//												+ 2 * DoubleDST_2D.this.rows,
//										this.val$scale);
//								DoubleDST_2D.this.dstRows.inverse(
//										DoubleDST_2D.this.t, this.val$startt
//												+ 3 * DoubleDST_2D.this.rows,
//										this.val$scale);
//								for (k = 0; k < DoubleDST_2D.this.rows; ++k) {
//									i = this.val$startt
//											+ DoubleDST_2D.this.rows + k;
//									this.val$a[k][j] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(this.val$startt + k)];
//									this.val$a[k][(j + 1)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[i];
//									this.val$a[k][(j + 2)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(i + DoubleDST_2D
//											.access$200(DoubleDST_2D.this))];
//									this.val$a[k][(j + 3)] = DoubleDST_2D
//											.access$400(DoubleDST_2D.this)[(i + 2 * DoubleDST_2D
//											.access$200(DoubleDST_2D.this))];
//								}
//								j += 4 * this.val$nthreads;
//							}
//						}
//					} else if (DoubleDST_2D.this.columns == 2 * this.val$nthreads) {
//						for (j = 0; j < DoubleDST_2D.this.rows; ++j) {
//							i = this.val$startt + j;
//							DoubleDST_2D.this.t[i] = this.val$a[j][(2 * this.val$n0)];
//							DoubleDST_2D.this.t[(i + DoubleDST_2D.this.rows)] = this.val$a[j][(2 * this.val$n0 + 1)];
//						}
//						if (this.val$isgn == -1) {
//							DoubleDST_2D.this.dstRows.forward(
//									DoubleDST_2D.this.t, this.val$startt,
//									this.val$scale);
//							DoubleDST_2D.this.dstRows.forward(
//									DoubleDST_2D.this.t, this.val$startt
//											+ DoubleDST_2D.this.rows,
//									this.val$scale);
//						} else {
//							DoubleDST_2D.this.dstRows.inverse(
//									DoubleDST_2D.this.t, this.val$startt,
//									this.val$scale);
//							DoubleDST_2D.this.dstRows.inverse(
//									DoubleDST_2D.this.t, this.val$startt
//											+ DoubleDST_2D.this.rows,
//									this.val$scale);
//						}
//						for (j = 0; j < DoubleDST_2D.this.rows; ++j) {
//							i = this.val$startt + j;
//							this.val$a[j][(2 * this.val$n0)] = DoubleDST_2D
//									.access$400(DoubleDST_2D.this)[i];
//							this.val$a[j][(2 * this.val$n0 + 1)] = DoubleDST_2D
//									.access$400(DoubleDST_2D.this)[(i + DoubleDST_2D
//									.access$200(DoubleDST_2D.this))];
//						}
//					} else {
//						if (DoubleDST_2D.this.columns != this.val$nthreads)
//							return;
//						for (j = 0; j < DoubleDST_2D.this.rows; ++j)
//							DoubleDST_2D.this.t[(this.val$startt + j)] = this.val$a[j][this.val$n0];
//						if (this.val$isgn == -1)
//							DoubleDST_2D.this.dstRows.forward(
//									DoubleDST_2D.this.t, this.val$startt,
//									this.val$scale);
//						else
//							DoubleDST_2D.this.dstRows.inverse(
//									DoubleDST_2D.this.t, this.val$startt,
//									this.val$scale);
//						for (j = 0; j < DoubleDST_2D.this.rows; ++j)
//							this.val$a[j][this.val$n0] = DoubleDST_2D
//									.access$400(DoubleDST_2D.this)[(this.val$startt + j)];
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private void ddxt2d0_subth(int paramInt, double[] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		Future[] arrayOfFuture = new Future[i];
//		for (int j = 0; j < i; ++j) {
//			int k = j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt,
//					k, i, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int i;
//					if (this.val$isgn == -1) {
//						i = this.val$n0;
//						while (i < DoubleDST_2D.this.rows) {
//							DoubleDST_2D.this.dstColumns
//									.forward(this.val$a, i
//											* DoubleDST_2D.this.columns,
//											this.val$scale);
//							i += this.val$nthreads;
//						}
//					} else {
//						i = this.val$n0;
//						while (i < DoubleDST_2D.this.rows) {
//							DoubleDST_2D.this.dstColumns
//									.inverse(this.val$a, i
//											* DoubleDST_2D.this.columns,
//											this.val$scale);
//							i += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private void ddxt2d0_subth(int paramInt, double[][] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		Future[] arrayOfFuture = new Future[i];
//		for (int j = 0; j < i; ++j) {
//			int k = j;
//			arrayOfFuture[j] = ConcurrencyUtils.submit(new Runnable(paramInt,
//					k, i, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int i;
//					if (this.val$isgn == -1) {
//						i = this.val$n0;
//						while (i < DoubleDST_2D.this.rows) {
//							DoubleDST_2D.this.dstColumns.forward(this.val$a[i],
//									this.val$scale);
//							i += this.val$nthreads;
//						}
//					} else {
//						i = this.val$n0;
//						while (i < DoubleDST_2D.this.rows) {
//							DoubleDST_2D.this.dstColumns.inverse(this.val$a[i],
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
//	private void ddxt2d_sub(int paramInt, double[] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int k;
//		int i;
//		if (this.columns > 2) {
//			int l;
//			int j;
//			if (paramInt == -1)
//				for (k = 0; k < this.columns; k += 4) {
//					for (l = 0; l < this.rows; ++l) {
//						i = l * this.columns + k;
//						j = this.rows + l;
//						this.t[l] = paramArrayOfDouble[i];
//						this.t[j] = paramArrayOfDouble[(i + 1)];
//						this.t[(j + this.rows)] = paramArrayOfDouble[(i + 2)];
//						this.t[(j + 2 * this.rows)] = paramArrayOfDouble[(i + 3)];
//					}
//					this.dstRows.forward(this.t, 0, paramBoolean);
//					this.dstRows.forward(this.t, this.rows, paramBoolean);
//					this.dstRows.forward(this.t, 2 * this.rows, paramBoolean);
//					this.dstRows.forward(this.t, 3 * this.rows, paramBoolean);
//					for (l = 0; l < this.rows; ++l) {
//						i = l * this.columns + k;
//						j = this.rows + l;
//						paramArrayOfDouble[i] = this.t[l];
//						paramArrayOfDouble[(i + 1)] = this.t[j];
//						paramArrayOfDouble[(i + 2)] = this.t[(j + this.rows)];
//						paramArrayOfDouble[(i + 3)] = this.t[(j + 2 * this.rows)];
//					}
//				}
//			else
//				for (k = 0; k < this.columns; k += 4) {
//					for (l = 0; l < this.rows; ++l) {
//						i = l * this.columns + k;
//						j = this.rows + l;
//						this.t[l] = paramArrayOfDouble[i];
//						this.t[j] = paramArrayOfDouble[(i + 1)];
//						this.t[(j + this.rows)] = paramArrayOfDouble[(i + 2)];
//						this.t[(j + 2 * this.rows)] = paramArrayOfDouble[(i + 3)];
//					}
//					this.dstRows.inverse(this.t, 0, paramBoolean);
//					this.dstRows.inverse(this.t, this.rows, paramBoolean);
//					this.dstRows.inverse(this.t, 2 * this.rows, paramBoolean);
//					this.dstRows.inverse(this.t, 3 * this.rows, paramBoolean);
//					for (l = 0; l < this.rows; ++l) {
//						i = l * this.columns + k;
//						j = this.rows + l;
//						paramArrayOfDouble[i] = this.t[l];
//						paramArrayOfDouble[(i + 1)] = this.t[j];
//						paramArrayOfDouble[(i + 2)] = this.t[(j + this.rows)];
//						paramArrayOfDouble[(i + 3)] = this.t[(j + 2 * this.rows)];
//					}
//				}
//		} else {
//			if (this.columns != 2)
//				return;
//			for (k = 0; k < this.rows; ++k) {
//				i = k * this.columns;
//				this.t[k] = paramArrayOfDouble[i];
//				this.t[(this.rows + k)] = paramArrayOfDouble[(i + 1)];
//			}
//			if (paramInt == -1) {
//				this.dstRows.forward(this.t, 0, paramBoolean);
//				this.dstRows.forward(this.t, this.rows, paramBoolean);
//			} else {
//				this.dstRows.inverse(this.t, 0, paramBoolean);
//				this.dstRows.inverse(this.t, this.rows, paramBoolean);
//			}
//			for (k = 0; k < this.rows; ++k) {
//				i = k * this.columns;
//				paramArrayOfDouble[i] = this.t[k];
//				paramArrayOfDouble[(i + 1)] = this.t[(this.rows + k)];
//			}
//		}
//	}
//
//	private void ddxt2d_sub(int paramInt, double[][] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int j;
//		if (this.columns > 2) {
//			int k;
//			int i;
//			if (paramInt == -1)
//				for (j = 0; j < this.columns; j += 4) {
//					for (k = 0; k < this.rows; ++k) {
//						i = this.rows + k;
//						this.t[k] = paramArrayOfDouble[k][j];
//						this.t[i] = paramArrayOfDouble[k][(j + 1)];
//						this.t[(i + this.rows)] = paramArrayOfDouble[k][(j + 2)];
//						this.t[(i + 2 * this.rows)] = paramArrayOfDouble[k][(j + 3)];
//					}
//					this.dstRows.forward(this.t, 0, paramBoolean);
//					this.dstRows.forward(this.t, this.rows, paramBoolean);
//					this.dstRows.forward(this.t, 2 * this.rows, paramBoolean);
//					this.dstRows.forward(this.t, 3 * this.rows, paramBoolean);
//					for (k = 0; k < this.rows; ++k) {
//						i = this.rows + k;
//						paramArrayOfDouble[k][j] = this.t[k];
//						paramArrayOfDouble[k][(j + 1)] = this.t[i];
//						paramArrayOfDouble[k][(j + 2)] = this.t[(i + this.rows)];
//						paramArrayOfDouble[k][(j + 3)] = this.t[(i + 2 * this.rows)];
//					}
//				}
//			else
//				for (j = 0; j < this.columns; j += 4) {
//					for (k = 0; k < this.rows; ++k) {
//						i = this.rows + k;
//						this.t[k] = paramArrayOfDouble[k][j];
//						this.t[i] = paramArrayOfDouble[k][(j + 1)];
//						this.t[(i + this.rows)] = paramArrayOfDouble[k][(j + 2)];
//						this.t[(i + 2 * this.rows)] = paramArrayOfDouble[k][(j + 3)];
//					}
//					this.dstRows.inverse(this.t, 0, paramBoolean);
//					this.dstRows.inverse(this.t, this.rows, paramBoolean);
//					this.dstRows.inverse(this.t, 2 * this.rows, paramBoolean);
//					this.dstRows.inverse(this.t, 3 * this.rows, paramBoolean);
//					for (k = 0; k < this.rows; ++k) {
//						i = this.rows + k;
//						paramArrayOfDouble[k][j] = this.t[k];
//						paramArrayOfDouble[k][(j + 1)] = this.t[i];
//						paramArrayOfDouble[k][(j + 2)] = this.t[(i + this.rows)];
//						paramArrayOfDouble[k][(j + 3)] = this.t[(i + 2 * this.rows)];
//					}
//				}
//		} else {
//			if (this.columns != 2)
//				return;
//			for (j = 0; j < this.rows; ++j) {
//				this.t[j] = paramArrayOfDouble[j][0];
//				this.t[(this.rows + j)] = paramArrayOfDouble[j][1];
//			}
//			if (paramInt == -1) {
//				this.dstRows.forward(this.t, 0, paramBoolean);
//				this.dstRows.forward(this.t, this.rows, paramBoolean);
//			} else {
//				this.dstRows.inverse(this.t, 0, paramBoolean);
//				this.dstRows.inverse(this.t, this.rows, paramBoolean);
//			}
//			for (j = 0; j < this.rows; ++j) {
//				paramArrayOfDouble[j][0] = this.t[j];
//				paramArrayOfDouble[j][1] = this.t[(this.rows + j)];
//			}
//		}
//	}
//}