package ps.hell.math.base.persons.fft.transform.dst;//package ps.landerbuluse.ml.math.fft.transform.dst;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class DoubleDST_3D {
//	private int slices;
//	private int rows;
//	private int columns;
//	private int sliceStride;
//	private int rowStride;
//	private double[] t;
//	private DoubleDST_1D dstSlices;
//	private DoubleDST_1D dstRows;
//	private DoubleDST_1D dstColumns;
//	private int oldNthreads;
//	private int nt;
//	private boolean isPowerOfTwo = false;
//	private boolean useThreads = false;
//
//	public DoubleDST_3D(int paramInt1, int paramInt2, int paramInt3) {
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
//			this.nt *= 4;
//			if (this.oldNthreads > 1)
//				this.nt *= this.oldNthreads;
//			if (paramInt3 == 2)
//				this.nt >>= 1;
//			this.t = new double[this.nt];
//		}
//		this.dstSlices = new DoubleDST_1D(paramInt1);
//		if (paramInt1 == paramInt2)
//			this.dstRows = this.dstSlices;
//		else
//			this.dstRows = new DoubleDST_1D(paramInt2);
//		if (paramInt1 == paramInt3)
//			this.dstColumns = this.dstSlices;
//		else if (paramInt2 == paramInt3)
//			this.dstColumns = this.dstRows;
//		else
//			this.dstColumns = new DoubleDST_1D(paramInt3);
//	}
//
//	public void forward(double[] paramArrayOfDouble, boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 4;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 2)
//					this.nt >>= 1;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt3da_subth(-1, paramArrayOfDouble, paramBoolean);
//				ddxt3db_subth(-1, paramArrayOfDouble, paramBoolean);
//			} else {
//				ddxt3da_sub(-1, paramArrayOfDouble, paramBoolean);
//				ddxt3db_sub(-1, paramArrayOfDouble, paramBoolean);
//			}
//		} else {
//			int k;
//			int l;
//			int i1;
//			int i2;
//			if ((i > 1) && (this.useThreads) && (this.slices >= i)
//					&& (this.rows >= i) && (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				k = this.slices / i;
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * DoubleDST_3D.this.sliceStride;
//								for (int k = 0; k < DoubleDST_3D.this.rows; ++k)
//									DoubleDST_3D.this.dstColumns
//											.forward(
//													this.val$a,
//													j
//															+ k
//															* DoubleDST_3D.this.rowStride,
//													this.val$scale);
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDST_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * DoubleDST_3D.this.sliceStride;
//								for (int k = 0; k < DoubleDST_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < DoubleDST_3D.this.rows; ++l) {
//										i1 = j + l
//												* DoubleDST_3D.this.rowStride
//												+ k;
//										arrayOfDouble[l] = this.val$a[i1];
//									}
//									DoubleDST_3D.this.dstRows.forward(
//											arrayOfDouble, this.val$scale);
//									for (l = 0; l < DoubleDST_3D.this.rows; ++l) {
//										i1 = j + l
//												* DoubleDST_3D.this.rowStride
//												+ k;
//										this.val$a[i1] = arrayOfDouble[l];
//									}
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				k = this.rows / i;
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.rows : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDST_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//								int j = i * DoubleDST_3D.this.rowStride;
//								for (int k = 0; k < DoubleDST_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < DoubleDST_3D.this.slices; ++l) {
//										i1 = l * DoubleDST_3D.this.sliceStride
//												+ j + k;
//										arrayOfDouble[l] = this.val$a[i1];
//									}
//									DoubleDST_3D.this.dstSlices.forward(
//											arrayOfDouble, this.val$scale);
//									for (l = 0; l < DoubleDST_3D.this.slices; ++l) {
//										i1 = l * DoubleDST_3D.this.sliceStride
//												+ j + k;
//										this.val$a[i1] = arrayOfDouble[l];
//									}
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int j = 0; j < this.slices; ++j) {
//					k = j * this.sliceStride;
//					for (l = 0; l < this.rows; ++l)
//						this.dstColumns.forward(paramArrayOfDouble, k + l
//								* this.rowStride, paramBoolean);
//				}
//				double[] arrayOfDouble = new double[this.rows];
//				int i3;
//				for (k = 0; k < this.slices; ++k) {
//					l = k * this.sliceStride;
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							i3 = l + i2 * this.rowStride + i1;
//							arrayOfDouble[i2] = paramArrayOfDouble[i3];
//						}
//						this.dstRows.forward(arrayOfDouble, paramBoolean);
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							i3 = l + i2 * this.rowStride + i1;
//							paramArrayOfDouble[i3] = arrayOfDouble[i2];
//						}
//					}
//				}
//				arrayOfDouble = new double[this.slices];
//				for (k = 0; k < this.rows; ++k) {
//					l = k * this.rowStride;
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							arrayOfDouble[i2] = paramArrayOfDouble[i3];
//						}
//						this.dstSlices.forward(arrayOfDouble, paramBoolean);
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							paramArrayOfDouble[i3] = arrayOfDouble[i2];
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public void forward(double[][][] paramArrayOfDouble, boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 4;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 2)
//					this.nt >>= 1;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt3da_subth(-1, paramArrayOfDouble, paramBoolean);
//				ddxt3db_subth(-1, paramArrayOfDouble, paramBoolean);
//			} else {
//				ddxt3da_sub(-1, paramArrayOfDouble, paramBoolean);
//				ddxt3db_sub(-1, paramArrayOfDouble, paramBoolean);
//			}
//		} else {
//			int k;
//			int l;
//			int i1;
//			if ((i > 1) && (this.useThreads) && (this.slices >= i)
//					&& (this.rows >= i) && (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				k = this.slices / i;
//				int i2;
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < DoubleDST_3D.this.rows; ++j)
//									DoubleDST_3D.this.dstColumns.forward(
//											this.val$a[i][j], this.val$scale);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDST_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < DoubleDST_3D.this.columns; ++j) {
//									for (int k = 0; k < DoubleDST_3D.this.rows; ++k)
//										arrayOfDouble[k] = this.val$a[i][k][j];
//									DoubleDST_3D.this.dstRows.forward(
//											arrayOfDouble, this.val$scale);
//									for (k = 0; k < DoubleDST_3D.this.rows; ++k)
//										this.val$a[i][k][j] = arrayOfDouble[k];
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				k = this.rows / i;
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.rows : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDST_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								for (int j = 0; j < DoubleDST_3D.this.columns; ++j) {
//									for (int k = 0; k < DoubleDST_3D.this.slices; ++k)
//										arrayOfDouble[k] = this.val$a[k][i][j];
//									DoubleDST_3D.this.dstSlices.forward(
//											arrayOfDouble, this.val$scale);
//									for (k = 0; k < DoubleDST_3D.this.slices; ++k)
//										this.val$a[k][i][j] = arrayOfDouble[k];
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int j = 0; j < this.slices; ++j)
//					for (k = 0; k < this.rows; ++k)
//						this.dstColumns.forward(paramArrayOfDouble[j][k],
//								paramBoolean);
//				double[] arrayOfDouble = new double[this.rows];
//				for (k = 0; k < this.slices; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.rows; ++i1)
//							arrayOfDouble[i1] = paramArrayOfDouble[k][i1][l];
//						this.dstRows.forward(arrayOfDouble, paramBoolean);
//						for (i1 = 0; i1 < this.rows; ++i1)
//							paramArrayOfDouble[k][i1][l] = arrayOfDouble[i1];
//					}
//				arrayOfDouble = new double[this.slices];
//				for (k = 0; k < this.rows; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.slices; ++i1)
//							arrayOfDouble[i1] = paramArrayOfDouble[i1][k][l];
//						this.dstSlices.forward(arrayOfDouble, paramBoolean);
//						for (i1 = 0; i1 < this.slices; ++i1)
//							paramArrayOfDouble[i1][k][l] = arrayOfDouble[i1];
//					}
//			}
//		}
//	}
//
//	public void inverse(double[] paramArrayOfDouble, boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 4;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 2)
//					this.nt >>= 1;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt3da_subth(1, paramArrayOfDouble, paramBoolean);
//				ddxt3db_subth(1, paramArrayOfDouble, paramBoolean);
//			} else {
//				ddxt3da_sub(1, paramArrayOfDouble, paramBoolean);
//				ddxt3db_sub(1, paramArrayOfDouble, paramBoolean);
//			}
//		} else {
//			int k;
//			int l;
//			int i1;
//			int i2;
//			if ((i > 1) && (this.useThreads) && (this.slices >= i)
//					&& (this.rows >= i) && (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				k = this.slices / i;
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * DoubleDST_3D.this.sliceStride;
//								for (int k = 0; k < DoubleDST_3D.this.rows; ++k)
//									DoubleDST_3D.this.dstColumns
//											.inverse(
//													this.val$a,
//													j
//															+ k
//															* DoubleDST_3D.this.rowStride,
//													this.val$scale);
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDST_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * DoubleDST_3D.this.sliceStride;
//								for (int k = 0; k < DoubleDST_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < DoubleDST_3D.this.rows; ++l) {
//										i1 = j + l
//												* DoubleDST_3D.this.rowStride
//												+ k;
//										arrayOfDouble[l] = this.val$a[i1];
//									}
//									DoubleDST_3D.this.dstRows.inverse(
//											arrayOfDouble, this.val$scale);
//									for (l = 0; l < DoubleDST_3D.this.rows; ++l) {
//										i1 = j + l
//												* DoubleDST_3D.this.rowStride
//												+ k;
//										this.val$a[i1] = arrayOfDouble[l];
//									}
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				k = this.rows / i;
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.rows : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDST_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//								int j = i * DoubleDST_3D.this.rowStride;
//								for (int k = 0; k < DoubleDST_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < DoubleDST_3D.this.slices; ++l) {
//										i1 = l * DoubleDST_3D.this.sliceStride
//												+ j + k;
//										arrayOfDouble[l] = this.val$a[i1];
//									}
//									DoubleDST_3D.this.dstSlices.inverse(
//											arrayOfDouble, this.val$scale);
//									for (l = 0; l < DoubleDST_3D.this.slices; ++l) {
//										i1 = l * DoubleDST_3D.this.sliceStride
//												+ j + k;
//										this.val$a[i1] = arrayOfDouble[l];
//									}
//								}
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int j = 0; j < this.slices; ++j) {
//					k = j * this.sliceStride;
//					for (l = 0; l < this.rows; ++l)
//						this.dstColumns.inverse(paramArrayOfDouble, k + l
//								* this.rowStride, paramBoolean);
//				}
//				double[] arrayOfDouble = new double[this.rows];
//				int i3;
//				for (k = 0; k < this.slices; ++k) {
//					l = k * this.sliceStride;
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							i3 = l + i2 * this.rowStride + i1;
//							arrayOfDouble[i2] = paramArrayOfDouble[i3];
//						}
//						this.dstRows.inverse(arrayOfDouble, paramBoolean);
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							i3 = l + i2 * this.rowStride + i1;
//							paramArrayOfDouble[i3] = arrayOfDouble[i2];
//						}
//					}
//				}
//				arrayOfDouble = new double[this.slices];
//				for (k = 0; k < this.rows; ++k) {
//					l = k * this.rowStride;
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							arrayOfDouble[i2] = paramArrayOfDouble[i3];
//						}
//						this.dstSlices.inverse(arrayOfDouble, paramBoolean);
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							paramArrayOfDouble[i3] = arrayOfDouble[i2];
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public void inverse(double[][][] paramArrayOfDouble, boolean paramBoolean) {
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		if (this.isPowerOfTwo) {
//			if (i != this.oldNthreads) {
//				this.nt = this.slices;
//				if (this.nt < this.rows)
//					this.nt = this.rows;
//				this.nt *= 4;
//				if (i > 1)
//					this.nt *= i;
//				if (this.columns == 2)
//					this.nt >>= 1;
//				this.t = new double[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt3da_subth(1, paramArrayOfDouble, paramBoolean);
//				ddxt3db_subth(1, paramArrayOfDouble, paramBoolean);
//			} else {
//				ddxt3da_sub(1, paramArrayOfDouble, paramBoolean);
//				ddxt3db_sub(1, paramArrayOfDouble, paramBoolean);
//			}
//		} else {
//			int k;
//			int l;
//			int i1;
//			if ((i > 1) && (this.useThreads) && (this.slices >= i)
//					&& (this.rows >= i) && (this.columns >= i)) {
//				Future[] arrayOfFuture = new Future[i];
//				k = this.slices / i;
//				int i2;
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < DoubleDST_3D.this.rows; ++j)
//									DoubleDST_3D.this.dstColumns.inverse(
//											this.val$a[i][j], this.val$scale);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDST_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < DoubleDST_3D.this.columns; ++j) {
//									for (int k = 0; k < DoubleDST_3D.this.rows; ++k)
//										arrayOfDouble[k] = this.val$a[i][k][j];
//									DoubleDST_3D.this.dstRows.inverse(
//											arrayOfDouble, this.val$scale);
//									for (k = 0; k < DoubleDST_3D.this.rows; ++k)
//										this.val$a[i][k][j] = arrayOfDouble[k];
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				k = this.rows / i;
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.rows : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble, paramBoolean) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDST_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								for (int j = 0; j < DoubleDST_3D.this.columns; ++j) {
//									for (int k = 0; k < DoubleDST_3D.this.slices; ++k)
//										arrayOfDouble[k] = this.val$a[k][i][j];
//									DoubleDST_3D.this.dstSlices.inverse(
//											arrayOfDouble, this.val$scale);
//									for (k = 0; k < DoubleDST_3D.this.slices; ++k)
//										this.val$a[k][i][j] = arrayOfDouble[k];
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int j = 0; j < this.slices; ++j)
//					for (k = 0; k < this.rows; ++k)
//						this.dstColumns.inverse(paramArrayOfDouble[j][k],
//								paramBoolean);
//				double[] arrayOfDouble = new double[this.rows];
//				for (k = 0; k < this.slices; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.rows; ++i1)
//							arrayOfDouble[i1] = paramArrayOfDouble[k][i1][l];
//						this.dstRows.inverse(arrayOfDouble, paramBoolean);
//						for (i1 = 0; i1 < this.rows; ++i1)
//							paramArrayOfDouble[k][i1][l] = arrayOfDouble[i1];
//					}
//				arrayOfDouble = new double[this.slices];
//				for (k = 0; k < this.rows; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.slices; ++i1)
//							arrayOfDouble[i1] = paramArrayOfDouble[i1][k][l];
//						this.dstSlices.inverse(arrayOfDouble, paramBoolean);
//						for (i1 = 0; i1 < this.slices; ++i1)
//							paramArrayOfDouble[i1][k][l] = arrayOfDouble[i1];
//					}
//			}
//		}
//	}
//
//	private void ddxt3da_sub(int paramInt, double[] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int l;
//		int i;
//		int i1;
//		int i2;
//		int j;
//		int k;
//		if (paramInt == -1)
//			for (l = 0; l < this.slices; ++l) {
//				i = l * this.sliceStride;
//				for (i1 = 0; i1 < this.rows; ++i1)
//					this.dstColumns.forward(paramArrayOfDouble, i + i1
//							* this.rowStride, paramBoolean);
//				if (this.columns > 2) {
//					for (i1 = 0; i1 < this.columns; i1 += 4) {
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							j = i + i2 * this.rowStride + i1;
//							k = this.rows + i2;
//							this.t[i2] = paramArrayOfDouble[j];
//							this.t[k] = paramArrayOfDouble[(j + 1)];
//							this.t[(k + this.rows)] = paramArrayOfDouble[(j + 2)];
//							this.t[(k + 2 * this.rows)] = paramArrayOfDouble[(j + 3)];
//						}
//						this.dstRows.forward(this.t, 0, paramBoolean);
//						this.dstRows.forward(this.t, this.rows, paramBoolean);
//						this.dstRows.forward(this.t, 2 * this.rows,
//								paramBoolean);
//						this.dstRows.forward(this.t, 3 * this.rows,
//								paramBoolean);
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							j = i + i2 * this.rowStride + i1;
//							k = this.rows + i2;
//							paramArrayOfDouble[j] = this.t[i2];
//							paramArrayOfDouble[(j + 1)] = this.t[k];
//							paramArrayOfDouble[(j + 2)] = this.t[(k + this.rows)];
//							paramArrayOfDouble[(j + 3)] = this.t[(k + 2 * this.rows)];
//						}
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (i1 = 0; i1 < this.rows; ++i1) {
//						j = i + i1 * this.rowStride;
//						this.t[i1] = paramArrayOfDouble[j];
//						this.t[(this.rows + i1)] = paramArrayOfDouble[(j + 1)];
//					}
//					this.dstRows.forward(this.t, 0, paramBoolean);
//					this.dstRows.forward(this.t, this.rows, paramBoolean);
//					for (i1 = 0; i1 < this.rows; ++i1) {
//						j = i + i1 * this.rowStride;
//						paramArrayOfDouble[j] = this.t[i1];
//						paramArrayOfDouble[(j + 1)] = this.t[(this.rows + i1)];
//					}
//				}
//			}
//		else
//			for (l = 0; l < this.slices; ++l) {
//				i = l * this.sliceStride;
//				for (i1 = 0; i1 < this.rows; ++i1)
//					this.dstColumns.inverse(paramArrayOfDouble, i + i1
//							* this.rowStride, paramBoolean);
//				if (this.columns > 2) {
//					for (i1 = 0; i1 < this.columns; i1 += 4) {
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							j = i + i2 * this.rowStride + i1;
//							k = this.rows + i2;
//							this.t[i2] = paramArrayOfDouble[j];
//							this.t[k] = paramArrayOfDouble[(j + 1)];
//							this.t[(k + this.rows)] = paramArrayOfDouble[(j + 2)];
//							this.t[(k + 2 * this.rows)] = paramArrayOfDouble[(j + 3)];
//						}
//						this.dstRows.inverse(this.t, 0, paramBoolean);
//						this.dstRows.inverse(this.t, this.rows, paramBoolean);
//						this.dstRows.inverse(this.t, 2 * this.rows,
//								paramBoolean);
//						this.dstRows.inverse(this.t, 3 * this.rows,
//								paramBoolean);
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							j = i + i2 * this.rowStride + i1;
//							k = this.rows + i2;
//							paramArrayOfDouble[j] = this.t[i2];
//							paramArrayOfDouble[(j + 1)] = this.t[k];
//							paramArrayOfDouble[(j + 2)] = this.t[(k + this.rows)];
//							paramArrayOfDouble[(j + 3)] = this.t[(k + 2 * this.rows)];
//						}
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (i1 = 0; i1 < this.rows; ++i1) {
//						j = i + i1 * this.rowStride;
//						this.t[i1] = paramArrayOfDouble[j];
//						this.t[(this.rows + i1)] = paramArrayOfDouble[(j + 1)];
//					}
//					this.dstRows.inverse(this.t, 0, paramBoolean);
//					this.dstRows.inverse(this.t, this.rows, paramBoolean);
//					for (i1 = 0; i1 < this.rows; ++i1) {
//						j = i + i1 * this.rowStride;
//						paramArrayOfDouble[j] = this.t[i1];
//						paramArrayOfDouble[(j + 1)] = this.t[(this.rows + i1)];
//					}
//				}
//			}
//	}
//
//	private void ddxt3da_sub(int paramInt, double[][][] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int j;
//		int k;
//		int l;
//		int i;
//		if (paramInt == -1)
//			for (j = 0; j < this.slices; ++j) {
//				for (k = 0; k < this.rows; ++k)
//					this.dstColumns.forward(paramArrayOfDouble[j][k],
//							paramBoolean);
//				if (this.columns > 2) {
//					for (k = 0; k < this.columns; k += 4) {
//						for (l = 0; l < this.rows; ++l) {
//							i = this.rows + l;
//							this.t[l] = paramArrayOfDouble[j][l][k];
//							this.t[i] = paramArrayOfDouble[j][l][(k + 1)];
//							this.t[(i + this.rows)] = paramArrayOfDouble[j][l][(k + 2)];
//							this.t[(i + 2 * this.rows)] = paramArrayOfDouble[j][l][(k + 3)];
//						}
//						this.dstRows.forward(this.t, 0, paramBoolean);
//						this.dstRows.forward(this.t, this.rows, paramBoolean);
//						this.dstRows.forward(this.t, 2 * this.rows,
//								paramBoolean);
//						this.dstRows.forward(this.t, 3 * this.rows,
//								paramBoolean);
//						for (l = 0; l < this.rows; ++l) {
//							i = this.rows + l;
//							paramArrayOfDouble[j][l][k] = this.t[l];
//							paramArrayOfDouble[j][l][(k + 1)] = this.t[i];
//							paramArrayOfDouble[j][l][(k + 2)] = this.t[(i + this.rows)];
//							paramArrayOfDouble[j][l][(k + 3)] = this.t[(i + 2 * this.rows)];
//						}
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (k = 0; k < this.rows; ++k) {
//						this.t[k] = paramArrayOfDouble[j][k][0];
//						this.t[(this.rows + k)] = paramArrayOfDouble[j][k][1];
//					}
//					this.dstRows.forward(this.t, 0, paramBoolean);
//					this.dstRows.forward(this.t, this.rows, paramBoolean);
//					for (k = 0; k < this.rows; ++k) {
//						paramArrayOfDouble[j][k][0] = this.t[k];
//						paramArrayOfDouble[j][k][1] = this.t[(this.rows + k)];
//					}
//				}
//			}
//		else
//			for (j = 0; j < this.slices; ++j) {
//				for (k = 0; k < this.rows; ++k)
//					this.dstColumns.inverse(paramArrayOfDouble[j][k],
//							paramBoolean);
//				if (this.columns > 2) {
//					for (k = 0; k < this.columns; k += 4) {
//						for (l = 0; l < this.rows; ++l) {
//							i = this.rows + l;
//							this.t[l] = paramArrayOfDouble[j][l][k];
//							this.t[i] = paramArrayOfDouble[j][l][(k + 1)];
//							this.t[(i + this.rows)] = paramArrayOfDouble[j][l][(k + 2)];
//							this.t[(i + 2 * this.rows)] = paramArrayOfDouble[j][l][(k + 3)];
//						}
//						this.dstRows.inverse(this.t, 0, paramBoolean);
//						this.dstRows.inverse(this.t, this.rows, paramBoolean);
//						this.dstRows.inverse(this.t, 2 * this.rows,
//								paramBoolean);
//						this.dstRows.inverse(this.t, 3 * this.rows,
//								paramBoolean);
//						for (l = 0; l < this.rows; ++l) {
//							i = this.rows + l;
//							paramArrayOfDouble[j][l][k] = this.t[l];
//							paramArrayOfDouble[j][l][(k + 1)] = this.t[i];
//							paramArrayOfDouble[j][l][(k + 2)] = this.t[(i + this.rows)];
//							paramArrayOfDouble[j][l][(k + 3)] = this.t[(i + 2 * this.rows)];
//						}
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (k = 0; k < this.rows; ++k) {
//						this.t[k] = paramArrayOfDouble[j][k][0];
//						this.t[(this.rows + k)] = paramArrayOfDouble[j][k][1];
//					}
//					this.dstRows.inverse(this.t, 0, paramBoolean);
//					this.dstRows.inverse(this.t, this.rows, paramBoolean);
//					for (k = 0; k < this.rows; ++k) {
//						paramArrayOfDouble[j][k][0] = this.t[k];
//						paramArrayOfDouble[j][k][1] = this.t[(this.rows + k)];
//					}
//				}
//			}
//	}
//
//	private void ddxt3db_sub(int paramInt, double[] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int l;
//		int i;
//		int i1;
//		int i2;
//		int j;
//		int k;
//		if (paramInt == -1) {
//			if (this.columns > 2) {
//				for (l = 0; l < this.rows; ++l) {
//					i = l * this.rowStride;
//					for (i1 = 0; i1 < this.columns; i1 += 4) {
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							j = i2 * this.sliceStride + i + i1;
//							k = this.slices + i2;
//							this.t[i2] = paramArrayOfDouble[j];
//							this.t[k] = paramArrayOfDouble[(j + 1)];
//							this.t[(k + this.slices)] = paramArrayOfDouble[(j + 2)];
//							this.t[(k + 2 * this.slices)] = paramArrayOfDouble[(j + 3)];
//						}
//						this.dstSlices.forward(this.t, 0, paramBoolean);
//						this.dstSlices.forward(this.t, this.slices,
//								paramBoolean);
//						this.dstSlices.forward(this.t, 2 * this.slices,
//								paramBoolean);
//						this.dstSlices.forward(this.t, 3 * this.slices,
//								paramBoolean);
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							j = i2 * this.sliceStride + i + i1;
//							k = this.slices + i2;
//							paramArrayOfDouble[j] = this.t[i2];
//							paramArrayOfDouble[(j + 1)] = this.t[k];
//							paramArrayOfDouble[(j + 2)] = this.t[(k + this.slices)];
//							paramArrayOfDouble[(j + 3)] = this.t[(k + 2 * this.slices)];
//						}
//					}
//				}
//			} else {
//				if (this.columns != 2)
//					return;
//				for (l = 0; l < this.rows; ++l) {
//					i = l * this.rowStride;
//					for (i1 = 0; i1 < this.slices; ++i1) {
//						j = i1 * this.sliceStride + i;
//						this.t[i1] = paramArrayOfDouble[j];
//						this.t[(this.slices + i1)] = paramArrayOfDouble[(j + 1)];
//					}
//					this.dstSlices.forward(this.t, 0, paramBoolean);
//					this.dstSlices.forward(this.t, this.slices, paramBoolean);
//					for (i1 = 0; i1 < this.slices; ++i1) {
//						j = i1 * this.sliceStride + i;
//						paramArrayOfDouble[j] = this.t[i1];
//						paramArrayOfDouble[(j + 1)] = this.t[(this.slices + i1)];
//					}
//				}
//			}
//		} else if (this.columns > 2) {
//			for (l = 0; l < this.rows; ++l) {
//				i = l * this.rowStride;
//				for (i1 = 0; i1 < this.columns; i1 += 4) {
//					for (i2 = 0; i2 < this.slices; ++i2) {
//						j = i2 * this.sliceStride + i + i1;
//						k = this.slices + i2;
//						this.t[i2] = paramArrayOfDouble[j];
//						this.t[k] = paramArrayOfDouble[(j + 1)];
//						this.t[(k + this.slices)] = paramArrayOfDouble[(j + 2)];
//						this.t[(k + 2 * this.slices)] = paramArrayOfDouble[(j + 3)];
//					}
//					this.dstSlices.inverse(this.t, 0, paramBoolean);
//					this.dstSlices.inverse(this.t, this.slices, paramBoolean);
//					this.dstSlices.inverse(this.t, 2 * this.slices,
//							paramBoolean);
//					this.dstSlices.inverse(this.t, 3 * this.slices,
//							paramBoolean);
//					for (i2 = 0; i2 < this.slices; ++i2) {
//						j = i2 * this.sliceStride + i + i1;
//						k = this.slices + i2;
//						paramArrayOfDouble[j] = this.t[i2];
//						paramArrayOfDouble[(j + 1)] = this.t[k];
//						paramArrayOfDouble[(j + 2)] = this.t[(k + this.slices)];
//						paramArrayOfDouble[(j + 3)] = this.t[(k + 2 * this.slices)];
//					}
//				}
//			}
//		} else {
//			if (this.columns != 2)
//				return;
//			for (l = 0; l < this.rows; ++l) {
//				i = l * this.rowStride;
//				for (i1 = 0; i1 < this.slices; ++i1) {
//					j = i1 * this.sliceStride + i;
//					this.t[i1] = paramArrayOfDouble[j];
//					this.t[(this.slices + i1)] = paramArrayOfDouble[(j + 1)];
//				}
//				this.dstSlices.inverse(this.t, 0, paramBoolean);
//				this.dstSlices.inverse(this.t, this.slices, paramBoolean);
//				for (i1 = 0; i1 < this.slices; ++i1) {
//					j = i1 * this.sliceStride + i;
//					paramArrayOfDouble[j] = this.t[i1];
//					paramArrayOfDouble[(j + 1)] = this.t[(this.slices + i1)];
//				}
//			}
//		}
//	}
//
//	private void ddxt3db_sub(int paramInt, double[][][] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int j;
//		int k;
//		int l;
//		int i;
//		if (paramInt == -1) {
//			if (this.columns > 2) {
//				for (j = 0; j < this.rows; ++j)
//					for (k = 0; k < this.columns; k += 4) {
//						for (l = 0; l < this.slices; ++l) {
//							i = this.slices + l;
//							this.t[l] = paramArrayOfDouble[l][j][k];
//							this.t[i] = paramArrayOfDouble[l][j][(k + 1)];
//							this.t[(i + this.slices)] = paramArrayOfDouble[l][j][(k + 2)];
//							this.t[(i + 2 * this.slices)] = paramArrayOfDouble[l][j][(k + 3)];
//						}
//						this.dstSlices.forward(this.t, 0, paramBoolean);
//						this.dstSlices.forward(this.t, this.slices,
//								paramBoolean);
//						this.dstSlices.forward(this.t, 2 * this.slices,
//								paramBoolean);
//						this.dstSlices.forward(this.t, 3 * this.slices,
//								paramBoolean);
//						for (l = 0; l < this.slices; ++l) {
//							i = this.slices + l;
//							paramArrayOfDouble[l][j][k] = this.t[l];
//							paramArrayOfDouble[l][j][(k + 1)] = this.t[i];
//							paramArrayOfDouble[l][j][(k + 2)] = this.t[(i + this.slices)];
//							paramArrayOfDouble[l][j][(k + 3)] = this.t[(i + 2 * this.slices)];
//						}
//					}
//			} else {
//				if (this.columns != 2)
//					return;
//				for (j = 0; j < this.rows; ++j) {
//					for (k = 0; k < this.slices; ++k) {
//						this.t[k] = paramArrayOfDouble[k][j][0];
//						this.t[(this.slices + k)] = paramArrayOfDouble[k][j][1];
//					}
//					this.dstSlices.forward(this.t, 0, paramBoolean);
//					this.dstSlices.forward(this.t, this.slices, paramBoolean);
//					for (k = 0; k < this.slices; ++k) {
//						paramArrayOfDouble[k][j][0] = this.t[k];
//						paramArrayOfDouble[k][j][1] = this.t[(this.slices + k)];
//					}
//				}
//			}
//		} else if (this.columns > 2) {
//			for (j = 0; j < this.rows; ++j)
//				for (k = 0; k < this.columns; k += 4) {
//					for (l = 0; l < this.slices; ++l) {
//						i = this.slices + l;
//						this.t[l] = paramArrayOfDouble[l][j][k];
//						this.t[i] = paramArrayOfDouble[l][j][(k + 1)];
//						this.t[(i + this.slices)] = paramArrayOfDouble[l][j][(k + 2)];
//						this.t[(i + 2 * this.slices)] = paramArrayOfDouble[l][j][(k + 3)];
//					}
//					this.dstSlices.inverse(this.t, 0, paramBoolean);
//					this.dstSlices.inverse(this.t, this.slices, paramBoolean);
//					this.dstSlices.inverse(this.t, 2 * this.slices,
//							paramBoolean);
//					this.dstSlices.inverse(this.t, 3 * this.slices,
//							paramBoolean);
//					for (l = 0; l < this.slices; ++l) {
//						i = this.slices + l;
//						paramArrayOfDouble[l][j][k] = this.t[l];
//						paramArrayOfDouble[l][j][(k + 1)] = this.t[i];
//						paramArrayOfDouble[l][j][(k + 2)] = this.t[(i + this.slices)];
//						paramArrayOfDouble[l][j][(k + 3)] = this.t[(i + 2 * this.slices)];
//					}
//				}
//		} else {
//			if (this.columns != 2)
//				return;
//			for (j = 0; j < this.rows; ++j) {
//				for (k = 0; k < this.slices; ++k) {
//					this.t[k] = paramArrayOfDouble[k][j][0];
//					this.t[(this.slices + k)] = paramArrayOfDouble[k][j][1];
//				}
//				this.dstSlices.inverse(this.t, 0, paramBoolean);
//				this.dstSlices.inverse(this.t, this.slices, paramBoolean);
//				for (k = 0; k < this.slices; ++k) {
//					paramArrayOfDouble[k][j][0] = this.t[k];
//					paramArrayOfDouble[k][j][1] = this.t[(this.slices + k)];
//				}
//			}
//		}
//	}
//
//	private void ddxt3da_subth(int paramInt, double[] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.slices) ? this.slices
//				: ConcurrencyUtils.getNumberOfThreads();
//		int j = 4 * this.rows;
//		if (this.columns == 2)
//			j >>= 1;
//		Future[] arrayOfFuture = new Future[i];
//		for (int k = 0; k < i; ++k) {
//			int l = k;
//			int i1 = j * k;
//			arrayOfFuture[k] = ConcurrencyUtils.submit(new Runnable(paramInt,
//					l, i, paramArrayOfDouble, paramBoolean, i1) {
//				public void run() {
//					int l;
//					int i;
//					int i1;
//					int i2;
//					int j;
//					int k;
//					if (this.val$isgn == -1) {
//						l = this.val$n0;
//						while (l < DoubleDST_3D.this.slices) {
//							i = l * DoubleDST_3D.this.sliceStride;
//							for (i1 = 0; i1 < DoubleDST_3D.this.rows; ++i1)
//								DoubleDST_3D.this.dstColumns.forward(
//										this.val$a, i + i1
//												* DoubleDST_3D.this.rowStride,
//										this.val$scale);
//							if (DoubleDST_3D.this.columns > 2) {
//								for (i1 = 0; i1 < DoubleDST_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < DoubleDST_3D.this.rows; ++i2) {
//										j = i + i2
//												* DoubleDST_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ DoubleDST_3D.this.rows + i2;
//										DoubleDST_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										DoubleDST_3D.this.t[k] = this.val$a[(j + 1)];
//										DoubleDST_3D.this.t[(k + DoubleDST_3D.this.rows)] = this.val$a[(j + 2)];
//										DoubleDST_3D.this.t[(k + 2 * DoubleDST_3D.this.rows)] = this.val$a[(j + 3)];
//									}
//									DoubleDST_3D.this.dstRows.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									DoubleDST_3D.this.dstRows.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt
//													+ DoubleDST_3D.this.rows,
//											this.val$scale);
//									DoubleDST_3D.this.dstRows.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt + 2
//													* DoubleDST_3D.this.rows,
//											this.val$scale);
//									DoubleDST_3D.this.dstRows.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt + 3
//													* DoubleDST_3D.this.rows,
//											this.val$scale);
//									for (i2 = 0; i2 < DoubleDST_3D.this.rows; ++i2) {
//										j = i + i2
//												* DoubleDST_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ DoubleDST_3D.this.rows + i2;
//										this.val$a[j] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[k];
//										this.val$a[(j + 2)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(k + DoubleDST_3D
//												.access$100(DoubleDST_3D.this))];
//										this.val$a[(j + 3)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(k + 2 * DoubleDST_3D
//												.access$100(DoubleDST_3D.this))];
//									}
//								}
//							} else if (DoubleDST_3D.this.columns == 2) {
//								for (i1 = 0; i1 < DoubleDST_3D.this.rows; ++i1) {
//									j = i + i1 * DoubleDST_3D.this.rowStride;
//									DoubleDST_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									DoubleDST_3D.this.t[(this.val$startt
//											+ DoubleDST_3D.this.rows + i1)] = this.val$a[(j + 1)];
//								}
//								DoubleDST_3D.this.dstRows.forward(
//										DoubleDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_3D.this.dstRows.forward(
//										DoubleDST_3D.this.t, this.val$startt
//												+ DoubleDST_3D.this.rows,
//										this.val$scale);
//								for (i1 = 0; i1 < DoubleDST_3D.this.rows; ++i1) {
//									j = i + i1 * DoubleDST_3D.this.rowStride;
//									this.val$a[j] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt
//											+ DoubleDST_3D
//													.access$100(DoubleDST_3D.this) + i1)];
//								}
//							}
//							l += this.val$nthreads;
//						}
//					} else {
//						l = this.val$n0;
//						while (l < DoubleDST_3D.this.slices) {
//							i = l * DoubleDST_3D.this.sliceStride;
//							for (i1 = 0; i1 < DoubleDST_3D.this.rows; ++i1)
//								DoubleDST_3D.this.dstColumns.inverse(
//										this.val$a, i + i1
//												* DoubleDST_3D.this.rowStride,
//										this.val$scale);
//							if (DoubleDST_3D.this.columns > 2) {
//								for (i1 = 0; i1 < DoubleDST_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < DoubleDST_3D.this.rows; ++i2) {
//										j = i + i2
//												* DoubleDST_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ DoubleDST_3D.this.rows + i2;
//										DoubleDST_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										DoubleDST_3D.this.t[k] = this.val$a[(j + 1)];
//										DoubleDST_3D.this.t[(k + DoubleDST_3D.this.rows)] = this.val$a[(j + 2)];
//										DoubleDST_3D.this.t[(k + 2 * DoubleDST_3D.this.rows)] = this.val$a[(j + 3)];
//									}
//									DoubleDST_3D.this.dstRows.inverse(
//											DoubleDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									DoubleDST_3D.this.dstRows.inverse(
//											DoubleDST_3D.this.t,
//											this.val$startt
//													+ DoubleDST_3D.this.rows,
//											this.val$scale);
//									DoubleDST_3D.this.dstRows.inverse(
//											DoubleDST_3D.this.t,
//											this.val$startt + 2
//													* DoubleDST_3D.this.rows,
//											this.val$scale);
//									DoubleDST_3D.this.dstRows.inverse(
//											DoubleDST_3D.this.t,
//											this.val$startt + 3
//													* DoubleDST_3D.this.rows,
//											this.val$scale);
//									for (i2 = 0; i2 < DoubleDST_3D.this.rows; ++i2) {
//										j = i + i2
//												* DoubleDST_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ DoubleDST_3D.this.rows + i2;
//										this.val$a[j] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[k];
//										this.val$a[(j + 2)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(k + DoubleDST_3D
//												.access$100(DoubleDST_3D.this))];
//										this.val$a[(j + 3)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(k + 2 * DoubleDST_3D
//												.access$100(DoubleDST_3D.this))];
//									}
//								}
//							} else if (DoubleDST_3D.this.columns == 2) {
//								for (i1 = 0; i1 < DoubleDST_3D.this.rows; ++i1) {
//									j = i + i1 * DoubleDST_3D.this.rowStride;
//									DoubleDST_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									DoubleDST_3D.this.t[(this.val$startt
//											+ DoubleDST_3D.this.rows + i1)] = this.val$a[(j + 1)];
//								}
//								DoubleDST_3D.this.dstRows.inverse(
//										DoubleDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_3D.this.dstRows.inverse(
//										DoubleDST_3D.this.t, this.val$startt
//												+ DoubleDST_3D.this.rows,
//										this.val$scale);
//								for (i1 = 0; i1 < DoubleDST_3D.this.rows; ++i1) {
//									j = i + i1 * DoubleDST_3D.this.rowStride;
//									this.val$a[j] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt
//											+ DoubleDST_3D
//													.access$100(DoubleDST_3D.this) + i1)];
//								}
//							}
//							l += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private void ddxt3da_subth(int paramInt, double[][][] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.slices) ? this.slices
//				: ConcurrencyUtils.getNumberOfThreads();
//		int j = 4 * this.rows;
//		if (this.columns == 2)
//			j >>= 1;
//		Future[] arrayOfFuture = new Future[i];
//		for (int k = 0; k < i; ++k) {
//			int l = k;
//			int i1 = j * k;
//			arrayOfFuture[k] = ConcurrencyUtils.submit(new Runnable(paramInt,
//					l, i, paramArrayOfDouble, paramBoolean, i1) {
//				public void run() {
//					int j;
//					int k;
//					int l;
//					int i;
//					if (this.val$isgn == -1) {
//						j = this.val$n0;
//						while (j < DoubleDST_3D.this.slices) {
//							for (k = 0; k < DoubleDST_3D.this.rows; ++k)
//								DoubleDST_3D.this.dstColumns.forward(
//										this.val$a[j][k], this.val$scale);
//							if (DoubleDST_3D.this.columns > 2) {
//								for (k = 0; k < DoubleDST_3D.this.columns; k += 4) {
//									for (l = 0; l < DoubleDST_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ DoubleDST_3D.this.rows + l;
//										DoubleDST_3D.this.t[(this.val$startt + l)] = this.val$a[j][l][k];
//										DoubleDST_3D.this.t[i] = this.val$a[j][l][(k + 1)];
//										DoubleDST_3D.this.t[(i + DoubleDST_3D.this.rows)] = this.val$a[j][l][(k + 2)];
//										DoubleDST_3D.this.t[(i + 2 * DoubleDST_3D.this.rows)] = this.val$a[j][l][(k + 3)];
//									}
//									DoubleDST_3D.this.dstRows.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									DoubleDST_3D.this.dstRows.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt
//													+ DoubleDST_3D.this.rows,
//											this.val$scale);
//									DoubleDST_3D.this.dstRows.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt + 2
//													* DoubleDST_3D.this.rows,
//											this.val$scale);
//									DoubleDST_3D.this.dstRows.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt + 3
//													* DoubleDST_3D.this.rows,
//											this.val$scale);
//									for (l = 0; l < DoubleDST_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ DoubleDST_3D.this.rows + l;
//										this.val$a[j][l][k] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(this.val$startt + l)];
//										this.val$a[j][l][(k + 1)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[i];
//										this.val$a[j][l][(k + 2)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(i + DoubleDST_3D
//												.access$100(DoubleDST_3D.this))];
//										this.val$a[j][l][(k + 3)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(i + 2 * DoubleDST_3D
//												.access$100(DoubleDST_3D.this))];
//									}
//								}
//							} else if (DoubleDST_3D.this.columns == 2) {
//								for (k = 0; k < DoubleDST_3D.this.rows; ++k) {
//									DoubleDST_3D.this.t[(this.val$startt + k)] = this.val$a[j][k][0];
//									DoubleDST_3D.this.t[(this.val$startt
//											+ DoubleDST_3D.this.rows + k)] = this.val$a[j][k][1];
//								}
//								DoubleDST_3D.this.dstRows.forward(
//										DoubleDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_3D.this.dstRows.forward(
//										DoubleDST_3D.this.t, this.val$startt
//												+ DoubleDST_3D.this.rows,
//										this.val$scale);
//								for (k = 0; k < DoubleDST_3D.this.rows; ++k) {
//									this.val$a[j][k][0] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt + k)];
//									this.val$a[j][k][1] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt
//											+ DoubleDST_3D
//													.access$100(DoubleDST_3D.this) + k)];
//								}
//							}
//							j += this.val$nthreads;
//						}
//					} else {
//						j = this.val$n0;
//						while (j < DoubleDST_3D.this.slices) {
//							for (k = 0; k < DoubleDST_3D.this.rows; ++k)
//								DoubleDST_3D.this.dstColumns.inverse(
//										this.val$a[j][k], this.val$scale);
//							if (DoubleDST_3D.this.columns > 2) {
//								for (k = 0; k < DoubleDST_3D.this.columns; k += 4) {
//									for (l = 0; l < DoubleDST_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ DoubleDST_3D.this.rows + l;
//										DoubleDST_3D.this.t[(this.val$startt + l)] = this.val$a[j][l][k];
//										DoubleDST_3D.this.t[i] = this.val$a[j][l][(k + 1)];
//										DoubleDST_3D.this.t[(i + DoubleDST_3D.this.rows)] = this.val$a[j][l][(k + 2)];
//										DoubleDST_3D.this.t[(i + 2 * DoubleDST_3D.this.rows)] = this.val$a[j][l][(k + 3)];
//									}
//									DoubleDST_3D.this.dstRows.inverse(
//											DoubleDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									DoubleDST_3D.this.dstRows.inverse(
//											DoubleDST_3D.this.t,
//											this.val$startt
//													+ DoubleDST_3D.this.rows,
//											this.val$scale);
//									DoubleDST_3D.this.dstRows.inverse(
//											DoubleDST_3D.this.t,
//											this.val$startt + 2
//													* DoubleDST_3D.this.rows,
//											this.val$scale);
//									DoubleDST_3D.this.dstRows.inverse(
//											DoubleDST_3D.this.t,
//											this.val$startt + 3
//													* DoubleDST_3D.this.rows,
//											this.val$scale);
//									for (l = 0; l < DoubleDST_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ DoubleDST_3D.this.rows + l;
//										this.val$a[j][l][k] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(this.val$startt + l)];
//										this.val$a[j][l][(k + 1)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[i];
//										this.val$a[j][l][(k + 2)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(i + DoubleDST_3D
//												.access$100(DoubleDST_3D.this))];
//										this.val$a[j][l][(k + 3)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(i + 2 * DoubleDST_3D
//												.access$100(DoubleDST_3D.this))];
//									}
//								}
//							} else if (DoubleDST_3D.this.columns == 2) {
//								for (k = 0; k < DoubleDST_3D.this.rows; ++k) {
//									DoubleDST_3D.this.t[(this.val$startt + k)] = this.val$a[j][k][0];
//									DoubleDST_3D.this.t[(this.val$startt
//											+ DoubleDST_3D.this.rows + k)] = this.val$a[j][k][1];
//								}
//								DoubleDST_3D.this.dstRows.inverse(
//										DoubleDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_3D.this.dstRows.inverse(
//										DoubleDST_3D.this.t, this.val$startt
//												+ DoubleDST_3D.this.rows,
//										this.val$scale);
//								for (k = 0; k < DoubleDST_3D.this.rows; ++k) {
//									this.val$a[j][k][0] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt + k)];
//									this.val$a[j][k][1] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt
//											+ DoubleDST_3D
//													.access$100(DoubleDST_3D.this) + k)];
//								}
//							}
//							j += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private void ddxt3db_subth(int paramInt, double[] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		int j = 4 * this.slices;
//		if (this.columns == 2)
//			j >>= 1;
//		Future[] arrayOfFuture = new Future[i];
//		for (int k = 0; k < i; ++k) {
//			int l = k;
//			int i1 = j * k;
//			arrayOfFuture[k] = ConcurrencyUtils.submit(new Runnable(paramInt,
//					l, i, i1, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int l;
//					int i;
//					int i1;
//					int i2;
//					int j;
//					int k;
//					if (this.val$isgn == -1) {
//						if (DoubleDST_3D.this.columns > 2) {
//							l = this.val$n0;
//							while (l < DoubleDST_3D.this.rows) {
//								i = l * DoubleDST_3D.this.rowStride;
//								for (i1 = 0; i1 < DoubleDST_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < DoubleDST_3D.this.slices; ++i2) {
//										j = i2 * DoubleDST_3D.this.sliceStride
//												+ i + i1;
//										k = this.val$startt
//												+ DoubleDST_3D.this.slices + i2;
//										DoubleDST_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										DoubleDST_3D.this.t[k] = this.val$a[(j + 1)];
//										DoubleDST_3D.this.t[(k + DoubleDST_3D.this.slices)] = this.val$a[(j + 2)];
//										DoubleDST_3D.this.t[(k + 2 * DoubleDST_3D.this.slices)] = this.val$a[(j + 3)];
//									}
//									DoubleDST_3D.this.dstSlices.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									DoubleDST_3D.this.dstSlices.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt
//													+ DoubleDST_3D.this.slices,
//											this.val$scale);
//									DoubleDST_3D.this.dstSlices.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt + 2
//													* DoubleDST_3D.this.slices,
//											this.val$scale);
//									DoubleDST_3D.this.dstSlices.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt + 3
//													* DoubleDST_3D.this.slices,
//											this.val$scale);
//									for (i2 = 0; i2 < DoubleDST_3D.this.slices; ++i2) {
//										j = i2 * DoubleDST_3D.this.sliceStride
//												+ i + i1;
//										k = this.val$startt
//												+ DoubleDST_3D.this.slices + i2;
//										this.val$a[j] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[k];
//										this.val$a[(j + 2)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(k + DoubleDST_3D
//												.access$600(DoubleDST_3D.this))];
//										this.val$a[(j + 3)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(k + 2 * DoubleDST_3D
//												.access$600(DoubleDST_3D.this))];
//									}
//								}
//								l += this.val$nthreads;
//							}
//						} else {
//							if (DoubleDST_3D.this.columns != 2)
//								return;
//							l = this.val$n0;
//							while (l < DoubleDST_3D.this.rows) {
//								i = l * DoubleDST_3D.this.rowStride;
//								for (i1 = 0; i1 < DoubleDST_3D.this.slices; ++i1) {
//									j = i1 * DoubleDST_3D.this.sliceStride + i;
//									DoubleDST_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									DoubleDST_3D.this.t[(this.val$startt
//											+ DoubleDST_3D.this.slices + i1)] = this.val$a[(j + 1)];
//								}
//								DoubleDST_3D.this.dstSlices.forward(
//										DoubleDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_3D.this.dstSlices.forward(
//										DoubleDST_3D.this.t, this.val$startt
//												+ DoubleDST_3D.this.slices,
//										this.val$scale);
//								for (i1 = 0; i1 < DoubleDST_3D.this.slices; ++i1) {
//									j = i1 * DoubleDST_3D.this.sliceStride + i;
//									this.val$a[j] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt
//											+ DoubleDST_3D
//													.access$600(DoubleDST_3D.this) + i1)];
//								}
//								l += this.val$nthreads;
//							}
//						}
//					} else if (DoubleDST_3D.this.columns > 2) {
//						l = this.val$n0;
//						while (l < DoubleDST_3D.this.rows) {
//							i = l * DoubleDST_3D.this.rowStride;
//							for (i1 = 0; i1 < DoubleDST_3D.this.columns; i1 += 4) {
//								for (i2 = 0; i2 < DoubleDST_3D.this.slices; ++i2) {
//									j = i2 * DoubleDST_3D.this.sliceStride + i
//											+ i1;
//									k = this.val$startt
//											+ DoubleDST_3D.this.slices + i2;
//									DoubleDST_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//									DoubleDST_3D.this.t[k] = this.val$a[(j + 1)];
//									DoubleDST_3D.this.t[(k + DoubleDST_3D.this.slices)] = this.val$a[(j + 2)];
//									DoubleDST_3D.this.t[(k + 2 * DoubleDST_3D.this.slices)] = this.val$a[(j + 3)];
//								}
//								DoubleDST_3D.this.dstSlices.inverse(
//										DoubleDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_3D.this.dstSlices.inverse(
//										DoubleDST_3D.this.t, this.val$startt
//												+ DoubleDST_3D.this.slices,
//										this.val$scale);
//								DoubleDST_3D.this.dstSlices.inverse(
//										DoubleDST_3D.this.t, this.val$startt
//												+ 2 * DoubleDST_3D.this.slices,
//										this.val$scale);
//								DoubleDST_3D.this.dstSlices.inverse(
//										DoubleDST_3D.this.t, this.val$startt
//												+ 3 * DoubleDST_3D.this.slices,
//										this.val$scale);
//								for (i2 = 0; i2 < DoubleDST_3D.this.slices; ++i2) {
//									j = i2 * DoubleDST_3D.this.sliceStride + i
//											+ i1;
//									k = this.val$startt
//											+ DoubleDST_3D.this.slices + i2;
//									this.val$a[j] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt + i2)];
//									this.val$a[(j + 1)] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[k];
//									this.val$a[(j + 2)] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(k + DoubleDST_3D
//											.access$600(DoubleDST_3D.this))];
//									this.val$a[(j + 3)] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(k + 2 * DoubleDST_3D
//											.access$600(DoubleDST_3D.this))];
//								}
//							}
//							l += this.val$nthreads;
//						}
//					} else {
//						if (DoubleDST_3D.this.columns != 2)
//							return;
//						l = this.val$n0;
//						while (l < DoubleDST_3D.this.rows) {
//							i = l * DoubleDST_3D.this.rowStride;
//							for (i1 = 0; i1 < DoubleDST_3D.this.slices; ++i1) {
//								j = i1 * DoubleDST_3D.this.sliceStride + i;
//								DoubleDST_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//								DoubleDST_3D.this.t[(this.val$startt
//										+ DoubleDST_3D.this.slices + i1)] = this.val$a[(j + 1)];
//							}
//							DoubleDST_3D.this.dstSlices.inverse(
//									DoubleDST_3D.this.t, this.val$startt,
//									this.val$scale);
//							DoubleDST_3D.this.dstSlices.inverse(
//									DoubleDST_3D.this.t, this.val$startt
//											+ DoubleDST_3D.this.slices,
//									this.val$scale);
//							for (i1 = 0; i1 < DoubleDST_3D.this.slices; ++i1) {
//								j = i1 * DoubleDST_3D.this.sliceStride + i;
//								this.val$a[j] = DoubleDST_3D
//										.access$800(DoubleDST_3D.this)[(this.val$startt + i1)];
//								this.val$a[(j + 1)] = DoubleDST_3D
//										.access$800(DoubleDST_3D.this)[(this.val$startt
//										+ DoubleDST_3D
//												.access$600(DoubleDST_3D.this) + i1)];
//							}
//							l += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private void ddxt3db_subth(int paramInt, double[][][] paramArrayOfDouble,
//			boolean paramBoolean) {
//		int i = (ConcurrencyUtils.getNumberOfThreads() > this.rows) ? this.rows
//				: ConcurrencyUtils.getNumberOfThreads();
//		int j = 4 * this.slices;
//		if (this.columns == 2)
//			j >>= 1;
//		Future[] arrayOfFuture = new Future[i];
//		for (int k = 0; k < i; ++k) {
//			int l = k;
//			int i1 = j * k;
//			arrayOfFuture[k] = ConcurrencyUtils.submit(new Runnable(paramInt,
//					l, i, i1, paramArrayOfDouble, paramBoolean) {
//				public void run() {
//					int j;
//					int k;
//					int l;
//					int i;
//					if (this.val$isgn == -1) {
//						if (DoubleDST_3D.this.columns > 2) {
//							j = this.val$n0;
//							while (j < DoubleDST_3D.this.rows) {
//								for (k = 0; k < DoubleDST_3D.this.columns; k += 4) {
//									for (l = 0; l < DoubleDST_3D.this.slices; ++l) {
//										i = this.val$startt
//												+ DoubleDST_3D.this.slices + l;
//										DoubleDST_3D.this.t[(this.val$startt + l)] = this.val$a[l][j][k];
//										DoubleDST_3D.this.t[i] = this.val$a[l][j][(k + 1)];
//										DoubleDST_3D.this.t[(i + DoubleDST_3D.this.slices)] = this.val$a[l][j][(k + 2)];
//										DoubleDST_3D.this.t[(i + 2 * DoubleDST_3D.this.slices)] = this.val$a[l][j][(k + 3)];
//									}
//									DoubleDST_3D.this.dstSlices.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									DoubleDST_3D.this.dstSlices.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt
//													+ DoubleDST_3D.this.slices,
//											this.val$scale);
//									DoubleDST_3D.this.dstSlices.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt + 2
//													* DoubleDST_3D.this.slices,
//											this.val$scale);
//									DoubleDST_3D.this.dstSlices.forward(
//											DoubleDST_3D.this.t,
//											this.val$startt + 3
//													* DoubleDST_3D.this.slices,
//											this.val$scale);
//									for (l = 0; l < DoubleDST_3D.this.slices; ++l) {
//										i = this.val$startt
//												+ DoubleDST_3D.this.slices + l;
//										this.val$a[l][j][k] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(this.val$startt + l)];
//										this.val$a[l][j][(k + 1)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[i];
//										this.val$a[l][j][(k + 2)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(i + DoubleDST_3D
//												.access$600(DoubleDST_3D.this))];
//										this.val$a[l][j][(k + 3)] = DoubleDST_3D
//												.access$800(DoubleDST_3D.this)[(i + 2 * DoubleDST_3D
//												.access$600(DoubleDST_3D.this))];
//									}
//								}
//								j += this.val$nthreads;
//							}
//						} else {
//							if (DoubleDST_3D.this.columns != 2)
//								return;
//							j = this.val$n0;
//							while (j < DoubleDST_3D.this.rows) {
//								for (k = 0; k < DoubleDST_3D.this.slices; ++k) {
//									DoubleDST_3D.this.t[(this.val$startt + k)] = this.val$a[k][j][0];
//									DoubleDST_3D.this.t[(this.val$startt
//											+ DoubleDST_3D.this.slices + k)] = this.val$a[k][j][1];
//								}
//								DoubleDST_3D.this.dstSlices.forward(
//										DoubleDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_3D.this.dstSlices.forward(
//										DoubleDST_3D.this.t, this.val$startt
//												+ DoubleDST_3D.this.slices,
//										this.val$scale);
//								for (k = 0; k < DoubleDST_3D.this.slices; ++k) {
//									this.val$a[k][j][0] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt + k)];
//									this.val$a[k][j][1] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt
//											+ DoubleDST_3D
//													.access$600(DoubleDST_3D.this) + k)];
//								}
//								j += this.val$nthreads;
//							}
//						}
//					} else if (DoubleDST_3D.this.columns > 2) {
//						j = this.val$n0;
//						while (j < DoubleDST_3D.this.rows) {
//							for (k = 0; k < DoubleDST_3D.this.columns; k += 4) {
//								for (l = 0; l < DoubleDST_3D.this.slices; ++l) {
//									i = this.val$startt
//											+ DoubleDST_3D.this.slices + l;
//									DoubleDST_3D.this.t[(this.val$startt + l)] = this.val$a[l][j][k];
//									DoubleDST_3D.this.t[i] = this.val$a[l][j][(k + 1)];
//									DoubleDST_3D.this.t[(i + DoubleDST_3D.this.slices)] = this.val$a[l][j][(k + 2)];
//									DoubleDST_3D.this.t[(i + 2 * DoubleDST_3D.this.slices)] = this.val$a[l][j][(k + 3)];
//								}
//								DoubleDST_3D.this.dstSlices.inverse(
//										DoubleDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDST_3D.this.dstSlices.inverse(
//										DoubleDST_3D.this.t, this.val$startt
//												+ DoubleDST_3D.this.slices,
//										this.val$scale);
//								DoubleDST_3D.this.dstSlices.inverse(
//										DoubleDST_3D.this.t, this.val$startt
//												+ 2 * DoubleDST_3D.this.slices,
//										this.val$scale);
//								DoubleDST_3D.this.dstSlices.inverse(
//										DoubleDST_3D.this.t, this.val$startt
//												+ 3 * DoubleDST_3D.this.slices,
//										this.val$scale);
//								for (l = 0; l < DoubleDST_3D.this.slices; ++l) {
//									i = this.val$startt
//											+ DoubleDST_3D.this.slices + l;
//									this.val$a[l][j][k] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(this.val$startt + l)];
//									this.val$a[l][j][(k + 1)] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[i];
//									this.val$a[l][j][(k + 2)] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(i + DoubleDST_3D
//											.access$600(DoubleDST_3D.this))];
//									this.val$a[l][j][(k + 3)] = DoubleDST_3D
//											.access$800(DoubleDST_3D.this)[(i + 2 * DoubleDST_3D
//											.access$600(DoubleDST_3D.this))];
//								}
//							}
//							j += this.val$nthreads;
//						}
//					} else {
//						if (DoubleDST_3D.this.columns != 2)
//							return;
//						j = this.val$n0;
//						while (j < DoubleDST_3D.this.rows) {
//							for (k = 0; k < DoubleDST_3D.this.slices; ++k) {
//								DoubleDST_3D.this.t[(this.val$startt + k)] = this.val$a[k][j][0];
//								DoubleDST_3D.this.t[(this.val$startt
//										+ DoubleDST_3D.this.slices + k)] = this.val$a[k][j][1];
//							}
//							DoubleDST_3D.this.dstSlices.inverse(
//									DoubleDST_3D.this.t, this.val$startt,
//									this.val$scale);
//							DoubleDST_3D.this.dstSlices.inverse(
//									DoubleDST_3D.this.t, this.val$startt
//											+ DoubleDST_3D.this.slices,
//									this.val$scale);
//							for (k = 0; k < DoubleDST_3D.this.slices; ++k) {
//								this.val$a[k][j][0] = DoubleDST_3D
//										.access$800(DoubleDST_3D.this)[(this.val$startt + k)];
//								this.val$a[k][j][1] = DoubleDST_3D
//										.access$800(DoubleDST_3D.this)[(this.val$startt
//										+ DoubleDST_3D
//												.access$600(DoubleDST_3D.this) + k)];
//							}
//							j += this.val$nthreads;
//						}
//					}
//				}
//			});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//}