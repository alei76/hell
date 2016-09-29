package ps.hell.math.base.persons.fft.transform.dht;//package ps.landerbuluse.ml.math.fft.transform.dht;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class DoubleDHT_3D {
//	private int slices;
//	private int rows;
//	private int columns;
//	private int sliceStride;
//	private int rowStride;
//	private double[] t;
//	private DoubleDHT_1D dhtSlices;
//	private DoubleDHT_1D dhtRows;
//	private DoubleDHT_1D dhtColumns;
//	private int oldNthreads;
//	private int nt;
//	private boolean isPowerOfTwo = false;
//	private boolean useThreads = false;
//
//	public DoubleDHT_3D(int paramInt1, int paramInt2, int paramInt3) {
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
//		this.dhtSlices = new DoubleDHT_1D(paramInt1);
//		if (paramInt1 == paramInt2)
//			this.dhtRows = this.dhtSlices;
//		else
//			this.dhtRows = new DoubleDHT_1D(paramInt2);
//		if (paramInt1 == paramInt3)
//			this.dhtColumns = this.dhtSlices;
//		else if (paramInt2 == paramInt3)
//			this.dhtColumns = this.dhtRows;
//		else
//			this.dhtColumns = new DoubleDHT_1D(paramInt3);
//	}
//
//	public void forward(double[] paramArrayOfDouble) {
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
//				ddxt3da_subth(-1, paramArrayOfDouble, true);
//				ddxt3db_subth(-1, paramArrayOfDouble, true);
//			} else {
//				ddxt3da_sub(-1, paramArrayOfDouble, true);
//				ddxt3db_sub(-1, paramArrayOfDouble, true);
//			}
//			yTransform(paramArrayOfDouble);
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
//							i2, paramArrayOfDouble) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * DoubleDHT_3D.this.sliceStride;
//								for (int k = 0; k < DoubleDHT_3D.this.rows; ++k)
//									DoubleDHT_3D.this.dhtColumns
//											.forward(
//													this.val$a,
//													j
//															+ k
//															* DoubleDHT_3D.this.rowStride);
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDHT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * DoubleDHT_3D.this.sliceStride;
//								for (int k = 0; k < DoubleDHT_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < DoubleDHT_3D.this.rows; ++l) {
//										i1 = j + l
//												* DoubleDHT_3D.this.rowStride
//												+ k;
//										arrayOfDouble[l] = this.val$a[i1];
//									}
//									DoubleDHT_3D.this.dhtRows
//											.forward(arrayOfDouble);
//									for (l = 0; l < DoubleDHT_3D.this.rows; ++l) {
//										i1 = j + l
//												* DoubleDHT_3D.this.rowStride
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
//					if (l == i - 1)
//						i2 = this.rows;
//					else
//						i2 = i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDHT_3D.this.slices];
//							for (int i = this.val$startRow; i < this.val$stopRow; ++i) {
//								int j = i * DoubleDHT_3D.this.rowStride;
//								for (int k = 0; k < DoubleDHT_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < DoubleDHT_3D.this.slices; ++l) {
//										i1 = l * DoubleDHT_3D.this.sliceStride
//												+ j + k;
//										arrayOfDouble[l] = this.val$a[i1];
//									}
//									DoubleDHT_3D.this.dhtSlices
//											.forward(arrayOfDouble);
//									for (l = 0; l < DoubleDHT_3D.this.slices; ++l) {
//										i1 = l * DoubleDHT_3D.this.sliceStride
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
//						this.dhtColumns.forward(paramArrayOfDouble, k + l
//								* this.rowStride);
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
//						this.dhtRows.forward(arrayOfDouble);
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
//						this.dhtSlices.forward(arrayOfDouble);
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							paramArrayOfDouble[i3] = arrayOfDouble[i2];
//						}
//					}
//				}
//			}
//			yTransform(paramArrayOfDouble);
//		}
//	}
//
//	public void forward(double[][][] paramArrayOfDouble) {
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
//				ddxt3da_subth(-1, paramArrayOfDouble, true);
//				ddxt3db_subth(-1, paramArrayOfDouble, true);
//			} else {
//				ddxt3da_sub(-1, paramArrayOfDouble, true);
//				ddxt3db_sub(-1, paramArrayOfDouble, true);
//			}
//			yTransform(paramArrayOfDouble);
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
//							i2, paramArrayOfDouble) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < DoubleDHT_3D.this.rows; ++j)
//									DoubleDHT_3D.this.dhtColumns
//											.forward(this.val$a[i][j]);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfDouble) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDHT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < DoubleDHT_3D.this.columns; ++j) {
//									for (int k = 0; k < DoubleDHT_3D.this.rows; ++k)
//										arrayOfDouble[k] = this.val$a[i][k][j];
//									DoubleDHT_3D.this.dhtRows
//											.forward(arrayOfDouble);
//									for (k = 0; k < DoubleDHT_3D.this.rows; ++k)
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
//							i2, paramArrayOfDouble) {
//						public void run() {
//							double[] arrayOfDouble = new double[DoubleDHT_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								for (int j = 0; j < DoubleDHT_3D.this.columns; ++j) {
//									for (int k = 0; k < DoubleDHT_3D.this.slices; ++k)
//										arrayOfDouble[k] = this.val$a[k][i][j];
//									DoubleDHT_3D.this.dhtSlices
//											.forward(arrayOfDouble);
//									for (k = 0; k < DoubleDHT_3D.this.slices; ++k)
//										this.val$a[k][i][j] = arrayOfDouble[k];
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int j = 0; j < this.slices; ++j)
//					for (k = 0; k < this.rows; ++k)
//						this.dhtColumns.forward(paramArrayOfDouble[j][k]);
//				double[] arrayOfDouble = new double[this.rows];
//				for (k = 0; k < this.slices; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.rows; ++i1)
//							arrayOfDouble[i1] = paramArrayOfDouble[k][i1][l];
//						this.dhtRows.forward(arrayOfDouble);
//						for (i1 = 0; i1 < this.rows; ++i1)
//							paramArrayOfDouble[k][i1][l] = arrayOfDouble[i1];
//					}
//				arrayOfDouble = new double[this.slices];
//				for (k = 0; k < this.rows; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.slices; ++i1)
//							arrayOfDouble[i1] = paramArrayOfDouble[i1][k][l];
//						this.dhtSlices.forward(arrayOfDouble);
//						for (i1 = 0; i1 < this.slices; ++i1)
//							paramArrayOfDouble[i1][k][l] = arrayOfDouble[i1];
//					}
//			}
//			yTransform(paramArrayOfDouble);
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
//			yTransform(paramArrayOfDouble);
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
//								int j = i * DoubleDHT_3D.this.sliceStride;
//								for (int k = 0; k < DoubleDHT_3D.this.rows; ++k)
//									DoubleDHT_3D.this.dhtColumns
//											.inverse(
//													this.val$a,
//													j
//															+ k
//															* DoubleDHT_3D.this.rowStride,
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
//							double[] arrayOfDouble = new double[DoubleDHT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * DoubleDHT_3D.this.sliceStride;
//								for (int k = 0; k < DoubleDHT_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < DoubleDHT_3D.this.rows; ++l) {
//										i1 = j + l
//												* DoubleDHT_3D.this.rowStride
//												+ k;
//										arrayOfDouble[l] = this.val$a[i1];
//									}
//									DoubleDHT_3D.this.dhtRows.inverse(
//											arrayOfDouble, this.val$scale);
//									for (l = 0; l < DoubleDHT_3D.this.rows; ++l) {
//										i1 = j + l
//												* DoubleDHT_3D.this.rowStride
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
//							double[] arrayOfDouble = new double[DoubleDHT_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//								int j = i * DoubleDHT_3D.this.rowStride;
//								for (int k = 0; k < DoubleDHT_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < DoubleDHT_3D.this.slices; ++l) {
//										i1 = l * DoubleDHT_3D.this.sliceStride
//												+ j + k;
//										arrayOfDouble[l] = this.val$a[i1];
//									}
//									DoubleDHT_3D.this.dhtSlices.inverse(
//											arrayOfDouble, this.val$scale);
//									for (l = 0; l < DoubleDHT_3D.this.slices; ++l) {
//										i1 = l * DoubleDHT_3D.this.sliceStride
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
//						this.dhtColumns.inverse(paramArrayOfDouble, k + l
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
//						this.dhtRows.inverse(arrayOfDouble, paramBoolean);
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
//						this.dhtSlices.inverse(arrayOfDouble, paramBoolean);
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							paramArrayOfDouble[i3] = arrayOfDouble[i2];
//						}
//					}
//				}
//			}
//			yTransform(paramArrayOfDouble);
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
//			yTransform(paramArrayOfDouble);
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
//								for (int j = 0; j < DoubleDHT_3D.this.rows; ++j)
//									DoubleDHT_3D.this.dhtColumns.inverse(
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
//							double[] arrayOfDouble = new double[DoubleDHT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < DoubleDHT_3D.this.columns; ++j) {
//									for (int k = 0; k < DoubleDHT_3D.this.rows; ++k)
//										arrayOfDouble[k] = this.val$a[i][k][j];
//									DoubleDHT_3D.this.dhtRows.inverse(
//											arrayOfDouble, this.val$scale);
//									for (k = 0; k < DoubleDHT_3D.this.rows; ++k)
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
//							double[] arrayOfDouble = new double[DoubleDHT_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								for (int j = 0; j < DoubleDHT_3D.this.columns; ++j) {
//									for (int k = 0; k < DoubleDHT_3D.this.slices; ++k)
//										arrayOfDouble[k] = this.val$a[k][i][j];
//									DoubleDHT_3D.this.dhtSlices.inverse(
//											arrayOfDouble, this.val$scale);
//									for (k = 0; k < DoubleDHT_3D.this.slices; ++k)
//										this.val$a[k][i][j] = arrayOfDouble[k];
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int j = 0; j < this.slices; ++j)
//					for (k = 0; k < this.rows; ++k)
//						this.dhtColumns.inverse(paramArrayOfDouble[j][k],
//								paramBoolean);
//				double[] arrayOfDouble = new double[this.rows];
//				for (k = 0; k < this.slices; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.rows; ++i1)
//							arrayOfDouble[i1] = paramArrayOfDouble[k][i1][l];
//						this.dhtRows.inverse(arrayOfDouble, paramBoolean);
//						for (i1 = 0; i1 < this.rows; ++i1)
//							paramArrayOfDouble[k][i1][l] = arrayOfDouble[i1];
//					}
//				arrayOfDouble = new double[this.slices];
//				for (k = 0; k < this.rows; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.slices; ++i1)
//							arrayOfDouble[i1] = paramArrayOfDouble[i1][k][l];
//						this.dhtSlices.inverse(arrayOfDouble, paramBoolean);
//						for (i1 = 0; i1 < this.slices; ++i1)
//							paramArrayOfDouble[i1][k][l] = arrayOfDouble[i1];
//					}
//			}
//			yTransform(paramArrayOfDouble);
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
//					this.dhtColumns.forward(paramArrayOfDouble, i + i1
//							* this.rowStride);
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
//						this.dhtRows.forward(this.t, 0);
//						this.dhtRows.forward(this.t, this.rows);
//						this.dhtRows.forward(this.t, 2 * this.rows);
//						this.dhtRows.forward(this.t, 3 * this.rows);
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
//					this.dhtRows.forward(this.t, 0);
//					this.dhtRows.forward(this.t, this.rows);
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
//					this.dhtColumns.inverse(paramArrayOfDouble, i + i1
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
//						this.dhtRows.inverse(this.t, 0, paramBoolean);
//						this.dhtRows.inverse(this.t, this.rows, paramBoolean);
//						this.dhtRows.inverse(this.t, 2 * this.rows,
//								paramBoolean);
//						this.dhtRows.inverse(this.t, 3 * this.rows,
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
//					this.dhtRows.inverse(this.t, 0, paramBoolean);
//					this.dhtRows.inverse(this.t, this.rows, paramBoolean);
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
//					this.dhtColumns.forward(paramArrayOfDouble[j][k]);
//				if (this.columns > 2) {
//					for (k = 0; k < this.columns; k += 4) {
//						for (l = 0; l < this.rows; ++l) {
//							i = this.rows + l;
//							this.t[l] = paramArrayOfDouble[j][l][k];
//							this.t[i] = paramArrayOfDouble[j][l][(k + 1)];
//							this.t[(i + this.rows)] = paramArrayOfDouble[j][l][(k + 2)];
//							this.t[(i + 2 * this.rows)] = paramArrayOfDouble[j][l][(k + 3)];
//						}
//						this.dhtRows.forward(this.t, 0);
//						this.dhtRows.forward(this.t, this.rows);
//						this.dhtRows.forward(this.t, 2 * this.rows);
//						this.dhtRows.forward(this.t, 3 * this.rows);
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
//					this.dhtRows.forward(this.t, 0);
//					this.dhtRows.forward(this.t, this.rows);
//					for (k = 0; k < this.rows; ++k) {
//						paramArrayOfDouble[j][k][0] = this.t[k];
//						paramArrayOfDouble[j][k][1] = this.t[(this.rows + k)];
//					}
//				}
//			}
//		else
//			for (j = 0; j < this.slices; ++j) {
//				for (k = 0; k < this.rows; ++k)
//					this.dhtColumns.inverse(paramArrayOfDouble[j][k],
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
//						this.dhtRows.inverse(this.t, 0, paramBoolean);
//						this.dhtRows.inverse(this.t, this.rows, paramBoolean);
//						this.dhtRows.inverse(this.t, 2 * this.rows,
//								paramBoolean);
//						this.dhtRows.inverse(this.t, 3 * this.rows,
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
//					this.dhtRows.inverse(this.t, 0, paramBoolean);
//					this.dhtRows.inverse(this.t, this.rows, paramBoolean);
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
//						this.dhtSlices.forward(this.t, 0);
//						this.dhtSlices.forward(this.t, this.slices);
//						this.dhtSlices.forward(this.t, 2 * this.slices);
//						this.dhtSlices.forward(this.t, 3 * this.slices);
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
//					this.dhtSlices.forward(this.t, 0);
//					this.dhtSlices.forward(this.t, this.slices);
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
//					this.dhtSlices.inverse(this.t, 0, paramBoolean);
//					this.dhtSlices.inverse(this.t, this.slices, paramBoolean);
//					this.dhtSlices.inverse(this.t, 2 * this.slices,
//							paramBoolean);
//					this.dhtSlices.inverse(this.t, 3 * this.slices,
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
//				this.dhtSlices.inverse(this.t, 0, paramBoolean);
//				this.dhtSlices.inverse(this.t, this.slices, paramBoolean);
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
//						this.dhtSlices.forward(this.t, 0);
//						this.dhtSlices.forward(this.t, this.slices);
//						this.dhtSlices.forward(this.t, 2 * this.slices);
//						this.dhtSlices.forward(this.t, 3 * this.slices);
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
//					this.dhtSlices.forward(this.t, 0);
//					this.dhtSlices.forward(this.t, this.slices);
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
//					this.dhtSlices.inverse(this.t, 0, paramBoolean);
//					this.dhtSlices.inverse(this.t, this.slices, paramBoolean);
//					this.dhtSlices.inverse(this.t, 2 * this.slices,
//							paramBoolean);
//					this.dhtSlices.inverse(this.t, 3 * this.slices,
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
//				this.dhtSlices.inverse(this.t, 0, paramBoolean);
//				this.dhtSlices.inverse(this.t, this.slices, paramBoolean);
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
//					l, i, paramArrayOfDouble, i1, paramBoolean) {
//				public void run() {
//					int l;
//					int i;
//					int i1;
//					int i2;
//					int j;
//					int k;
//					if (this.val$isgn == -1) {
//						l = this.val$n0;
//						while (l < DoubleDHT_3D.this.slices) {
//							i = l * DoubleDHT_3D.this.sliceStride;
//							for (i1 = 0; i1 < DoubleDHT_3D.this.rows; ++i1)
//								DoubleDHT_3D.this.dhtColumns.forward(
//										this.val$a, i + i1
//												* DoubleDHT_3D.this.rowStride);
//							if (DoubleDHT_3D.this.columns > 2) {
//								for (i1 = 0; i1 < DoubleDHT_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < DoubleDHT_3D.this.rows; ++i2) {
//										j = i + i2
//												* DoubleDHT_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ DoubleDHT_3D.this.rows + i2;
//										DoubleDHT_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										DoubleDHT_3D.this.t[k] = this.val$a[(j + 1)];
//										DoubleDHT_3D.this.t[(k + DoubleDHT_3D.this.rows)] = this.val$a[(j + 2)];
//										DoubleDHT_3D.this.t[(k + 2 * DoubleDHT_3D.this.rows)] = this.val$a[(j + 3)];
//									}
//									DoubleDHT_3D.this.dhtRows.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt);
//									DoubleDHT_3D.this.dhtRows.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt
//													+ DoubleDHT_3D.this.rows);
//									DoubleDHT_3D.this.dhtRows.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 2
//													* DoubleDHT_3D.this.rows);
//									DoubleDHT_3D.this.dhtRows.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 3
//													* DoubleDHT_3D.this.rows);
//									for (i2 = 0; i2 < DoubleDHT_3D.this.rows; ++i2) {
//										j = i + i2
//												* DoubleDHT_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ DoubleDHT_3D.this.rows + i2;
//										this.val$a[j] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[k];
//										this.val$a[(j + 2)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(k + DoubleDHT_3D
//												.access$100(DoubleDHT_3D.this))];
//										this.val$a[(j + 3)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(k + 2 * DoubleDHT_3D
//												.access$100(DoubleDHT_3D.this))];
//									}
//								}
//							} else if (DoubleDHT_3D.this.columns == 2) {
//								for (i1 = 0; i1 < DoubleDHT_3D.this.rows; ++i1) {
//									j = i + i1 * DoubleDHT_3D.this.rowStride;
//									DoubleDHT_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									DoubleDHT_3D.this.t[(this.val$startt
//											+ DoubleDHT_3D.this.rows + i1)] = this.val$a[(j + 1)];
//								}
//								DoubleDHT_3D.this.dhtRows.forward(
//										DoubleDHT_3D.this.t, this.val$startt);
//								DoubleDHT_3D.this.dhtRows.forward(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ DoubleDHT_3D.this.rows);
//								for (i1 = 0; i1 < DoubleDHT_3D.this.rows; ++i1) {
//									j = i + i1 * DoubleDHT_3D.this.rowStride;
//									this.val$a[j] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt
//											+ DoubleDHT_3D
//													.access$100(DoubleDHT_3D.this) + i1)];
//								}
//							}
//							l += this.val$nthreads;
//						}
//					} else {
//						l = this.val$n0;
//						while (l < DoubleDHT_3D.this.slices) {
//							i = l * DoubleDHT_3D.this.sliceStride;
//							for (i1 = 0; i1 < DoubleDHT_3D.this.rows; ++i1)
//								DoubleDHT_3D.this.dhtColumns.inverse(
//										this.val$a, i + i1
//												* DoubleDHT_3D.this.rowStride,
//										this.val$scale);
//							if (DoubleDHT_3D.this.columns > 2) {
//								for (i1 = 0; i1 < DoubleDHT_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < DoubleDHT_3D.this.rows; ++i2) {
//										j = i + i2
//												* DoubleDHT_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ DoubleDHT_3D.this.rows + i2;
//										DoubleDHT_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										DoubleDHT_3D.this.t[k] = this.val$a[(j + 1)];
//										DoubleDHT_3D.this.t[(k + DoubleDHT_3D.this.rows)] = this.val$a[(j + 2)];
//										DoubleDHT_3D.this.t[(k + 2 * DoubleDHT_3D.this.rows)] = this.val$a[(j + 3)];
//									}
//									DoubleDHT_3D.this.dhtRows.inverse(
//											DoubleDHT_3D.this.t,
//											this.val$startt, this.val$scale);
//									DoubleDHT_3D.this.dhtRows.inverse(
//											DoubleDHT_3D.this.t,
//											this.val$startt
//													+ DoubleDHT_3D.this.rows,
//											this.val$scale);
//									DoubleDHT_3D.this.dhtRows.inverse(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 2
//													* DoubleDHT_3D.this.rows,
//											this.val$scale);
//									DoubleDHT_3D.this.dhtRows.inverse(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 3
//													* DoubleDHT_3D.this.rows,
//											this.val$scale);
//									for (i2 = 0; i2 < DoubleDHT_3D.this.rows; ++i2) {
//										j = i + i2
//												* DoubleDHT_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ DoubleDHT_3D.this.rows + i2;
//										this.val$a[j] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[k];
//										this.val$a[(j + 2)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(k + DoubleDHT_3D
//												.access$100(DoubleDHT_3D.this))];
//										this.val$a[(j + 3)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(k + 2 * DoubleDHT_3D
//												.access$100(DoubleDHT_3D.this))];
//									}
//								}
//							} else if (DoubleDHT_3D.this.columns == 2) {
//								for (i1 = 0; i1 < DoubleDHT_3D.this.rows; ++i1) {
//									j = i + i1 * DoubleDHT_3D.this.rowStride;
//									DoubleDHT_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									DoubleDHT_3D.this.t[(this.val$startt
//											+ DoubleDHT_3D.this.rows + i1)] = this.val$a[(j + 1)];
//								}
//								DoubleDHT_3D.this.dhtRows.inverse(
//										DoubleDHT_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDHT_3D.this.dhtRows.inverse(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ DoubleDHT_3D.this.rows,
//										this.val$scale);
//								for (i1 = 0; i1 < DoubleDHT_3D.this.rows; ++i1) {
//									j = i + i1 * DoubleDHT_3D.this.rowStride;
//									this.val$a[j] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt
//											+ DoubleDHT_3D
//													.access$100(DoubleDHT_3D.this) + i1)];
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
//					l, i, paramArrayOfDouble, i1, paramBoolean) {
//				public void run() {
//					int j;
//					int k;
//					int l;
//					int i;
//					if (this.val$isgn == -1) {
//						j = this.val$n0;
//						while (j < DoubleDHT_3D.this.slices) {
//							for (k = 0; k < DoubleDHT_3D.this.rows; ++k)
//								DoubleDHT_3D.this.dhtColumns
//										.forward(this.val$a[j][k]);
//							if (DoubleDHT_3D.this.columns > 2) {
//								for (k = 0; k < DoubleDHT_3D.this.columns; k += 4) {
//									for (l = 0; l < DoubleDHT_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ DoubleDHT_3D.this.rows + l;
//										DoubleDHT_3D.this.t[(this.val$startt + l)] = this.val$a[j][l][k];
//										DoubleDHT_3D.this.t[i] = this.val$a[j][l][(k + 1)];
//										DoubleDHT_3D.this.t[(i + DoubleDHT_3D.this.rows)] = this.val$a[j][l][(k + 2)];
//										DoubleDHT_3D.this.t[(i + 2 * DoubleDHT_3D.this.rows)] = this.val$a[j][l][(k + 3)];
//									}
//									DoubleDHT_3D.this.dhtRows.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt);
//									DoubleDHT_3D.this.dhtRows.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt
//													+ DoubleDHT_3D.this.rows);
//									DoubleDHT_3D.this.dhtRows.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 2
//													* DoubleDHT_3D.this.rows);
//									DoubleDHT_3D.this.dhtRows.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 3
//													* DoubleDHT_3D.this.rows);
//									for (l = 0; l < DoubleDHT_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ DoubleDHT_3D.this.rows + l;
//										this.val$a[j][l][k] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(this.val$startt + l)];
//										this.val$a[j][l][(k + 1)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[i];
//										this.val$a[j][l][(k + 2)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(i + DoubleDHT_3D
//												.access$100(DoubleDHT_3D.this))];
//										this.val$a[j][l][(k + 3)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(i + 2 * DoubleDHT_3D
//												.access$100(DoubleDHT_3D.this))];
//									}
//								}
//							} else if (DoubleDHT_3D.this.columns == 2) {
//								for (k = 0; k < DoubleDHT_3D.this.rows; ++k) {
//									DoubleDHT_3D.this.t[(this.val$startt + k)] = this.val$a[j][k][0];
//									DoubleDHT_3D.this.t[(this.val$startt
//											+ DoubleDHT_3D.this.rows + k)] = this.val$a[j][k][1];
//								}
//								DoubleDHT_3D.this.dhtRows.forward(
//										DoubleDHT_3D.this.t, this.val$startt);
//								DoubleDHT_3D.this.dhtRows.forward(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ DoubleDHT_3D.this.rows);
//								for (k = 0; k < DoubleDHT_3D.this.rows; ++k) {
//									this.val$a[j][k][0] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt + k)];
//									this.val$a[j][k][1] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt
//											+ DoubleDHT_3D
//													.access$100(DoubleDHT_3D.this) + k)];
//								}
//							}
//							j += this.val$nthreads;
//						}
//					} else {
//						j = this.val$n0;
//						while (j < DoubleDHT_3D.this.slices) {
//							for (k = 0; k < DoubleDHT_3D.this.rows; ++k)
//								DoubleDHT_3D.this.dhtColumns.inverse(
//										this.val$a[j][k], this.val$scale);
//							if (DoubleDHT_3D.this.columns > 2) {
//								for (k = 0; k < DoubleDHT_3D.this.columns; k += 4) {
//									for (l = 0; l < DoubleDHT_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ DoubleDHT_3D.this.rows + l;
//										DoubleDHT_3D.this.t[(this.val$startt + l)] = this.val$a[j][l][k];
//										DoubleDHT_3D.this.t[i] = this.val$a[j][l][(k + 1)];
//										DoubleDHT_3D.this.t[(i + DoubleDHT_3D.this.rows)] = this.val$a[j][l][(k + 2)];
//										DoubleDHT_3D.this.t[(i + 2 * DoubleDHT_3D.this.rows)] = this.val$a[j][l][(k + 3)];
//									}
//									DoubleDHT_3D.this.dhtRows.inverse(
//											DoubleDHT_3D.this.t,
//											this.val$startt, this.val$scale);
//									DoubleDHT_3D.this.dhtRows.inverse(
//											DoubleDHT_3D.this.t,
//											this.val$startt
//													+ DoubleDHT_3D.this.rows,
//											this.val$scale);
//									DoubleDHT_3D.this.dhtRows.inverse(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 2
//													* DoubleDHT_3D.this.rows,
//											this.val$scale);
//									DoubleDHT_3D.this.dhtRows.inverse(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 3
//													* DoubleDHT_3D.this.rows,
//											this.val$scale);
//									for (l = 0; l < DoubleDHT_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ DoubleDHT_3D.this.rows + l;
//										this.val$a[j][l][k] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(this.val$startt + l)];
//										this.val$a[j][l][(k + 1)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[i];
//										this.val$a[j][l][(k + 2)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(i + DoubleDHT_3D
//												.access$100(DoubleDHT_3D.this))];
//										this.val$a[j][l][(k + 3)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(i + 2 * DoubleDHT_3D
//												.access$100(DoubleDHT_3D.this))];
//									}
//								}
//							} else if (DoubleDHT_3D.this.columns == 2) {
//								for (k = 0; k < DoubleDHT_3D.this.rows; ++k) {
//									DoubleDHT_3D.this.t[(this.val$startt + k)] = this.val$a[j][k][0];
//									DoubleDHT_3D.this.t[(this.val$startt
//											+ DoubleDHT_3D.this.rows + k)] = this.val$a[j][k][1];
//								}
//								DoubleDHT_3D.this.dhtRows.inverse(
//										DoubleDHT_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDHT_3D.this.dhtRows.inverse(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ DoubleDHT_3D.this.rows,
//										this.val$scale);
//								for (k = 0; k < DoubleDHT_3D.this.rows; ++k) {
//									this.val$a[j][k][0] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt + k)];
//									this.val$a[j][k][1] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt
//											+ DoubleDHT_3D
//													.access$100(DoubleDHT_3D.this) + k)];
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
//						if (DoubleDHT_3D.this.columns > 2) {
//							l = this.val$n0;
//							while (l < DoubleDHT_3D.this.rows) {
//								i = l * DoubleDHT_3D.this.rowStride;
//								for (i1 = 0; i1 < DoubleDHT_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < DoubleDHT_3D.this.slices; ++i2) {
//										j = i2 * DoubleDHT_3D.this.sliceStride
//												+ i + i1;
//										k = this.val$startt
//												+ DoubleDHT_3D.this.slices + i2;
//										DoubleDHT_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										DoubleDHT_3D.this.t[k] = this.val$a[(j + 1)];
//										DoubleDHT_3D.this.t[(k + DoubleDHT_3D.this.slices)] = this.val$a[(j + 2)];
//										DoubleDHT_3D.this.t[(k + 2 * DoubleDHT_3D.this.slices)] = this.val$a[(j + 3)];
//									}
//									DoubleDHT_3D.this.dhtSlices.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt);
//									DoubleDHT_3D.this.dhtSlices.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt
//													+ DoubleDHT_3D.this.slices);
//									DoubleDHT_3D.this.dhtSlices.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 2
//													* DoubleDHT_3D.this.slices);
//									DoubleDHT_3D.this.dhtSlices.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 3
//													* DoubleDHT_3D.this.slices);
//									for (i2 = 0; i2 < DoubleDHT_3D.this.slices; ++i2) {
//										j = i2 * DoubleDHT_3D.this.sliceStride
//												+ i + i1;
//										k = this.val$startt
//												+ DoubleDHT_3D.this.slices + i2;
//										this.val$a[j] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[k];
//										this.val$a[(j + 2)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(k + DoubleDHT_3D
//												.access$600(DoubleDHT_3D.this))];
//										this.val$a[(j + 3)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(k + 2 * DoubleDHT_3D
//												.access$600(DoubleDHT_3D.this))];
//									}
//								}
//								l += this.val$nthreads;
//							}
//						} else {
//							if (DoubleDHT_3D.this.columns != 2)
//								return;
//							l = this.val$n0;
//							while (l < DoubleDHT_3D.this.rows) {
//								i = l * DoubleDHT_3D.this.rowStride;
//								for (i1 = 0; i1 < DoubleDHT_3D.this.slices; ++i1) {
//									j = i1 * DoubleDHT_3D.this.sliceStride + i;
//									DoubleDHT_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									DoubleDHT_3D.this.t[(this.val$startt
//											+ DoubleDHT_3D.this.slices + i1)] = this.val$a[(j + 1)];
//								}
//								DoubleDHT_3D.this.dhtSlices.forward(
//										DoubleDHT_3D.this.t, this.val$startt);
//								DoubleDHT_3D.this.dhtSlices.forward(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ DoubleDHT_3D.this.slices);
//								for (i1 = 0; i1 < DoubleDHT_3D.this.slices; ++i1) {
//									j = i1 * DoubleDHT_3D.this.sliceStride + i;
//									this.val$a[j] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt
//											+ DoubleDHT_3D
//													.access$600(DoubleDHT_3D.this) + i1)];
//								}
//								l += this.val$nthreads;
//							}
//						}
//					} else if (DoubleDHT_3D.this.columns > 2) {
//						l = this.val$n0;
//						while (l < DoubleDHT_3D.this.rows) {
//							i = l * DoubleDHT_3D.this.rowStride;
//							for (i1 = 0; i1 < DoubleDHT_3D.this.columns; i1 += 4) {
//								for (i2 = 0; i2 < DoubleDHT_3D.this.slices; ++i2) {
//									j = i2 * DoubleDHT_3D.this.sliceStride + i
//											+ i1;
//									k = this.val$startt
//											+ DoubleDHT_3D.this.slices + i2;
//									DoubleDHT_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//									DoubleDHT_3D.this.t[k] = this.val$a[(j + 1)];
//									DoubleDHT_3D.this.t[(k + DoubleDHT_3D.this.slices)] = this.val$a[(j + 2)];
//									DoubleDHT_3D.this.t[(k + 2 * DoubleDHT_3D.this.slices)] = this.val$a[(j + 3)];
//								}
//								DoubleDHT_3D.this.dhtSlices.inverse(
//										DoubleDHT_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDHT_3D.this.dhtSlices.inverse(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ DoubleDHT_3D.this.slices,
//										this.val$scale);
//								DoubleDHT_3D.this.dhtSlices.inverse(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ 2 * DoubleDHT_3D.this.slices,
//										this.val$scale);
//								DoubleDHT_3D.this.dhtSlices.inverse(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ 3 * DoubleDHT_3D.this.slices,
//										this.val$scale);
//								for (i2 = 0; i2 < DoubleDHT_3D.this.slices; ++i2) {
//									j = i2 * DoubleDHT_3D.this.sliceStride + i
//											+ i1;
//									k = this.val$startt
//											+ DoubleDHT_3D.this.slices + i2;
//									this.val$a[j] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt + i2)];
//									this.val$a[(j + 1)] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[k];
//									this.val$a[(j + 2)] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(k + DoubleDHT_3D
//											.access$600(DoubleDHT_3D.this))];
//									this.val$a[(j + 3)] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(k + 2 * DoubleDHT_3D
//											.access$600(DoubleDHT_3D.this))];
//								}
//							}
//							l += this.val$nthreads;
//						}
//					} else {
//						if (DoubleDHT_3D.this.columns != 2)
//							return;
//						l = this.val$n0;
//						while (l < DoubleDHT_3D.this.rows) {
//							i = l * DoubleDHT_3D.this.rowStride;
//							for (i1 = 0; i1 < DoubleDHT_3D.this.slices; ++i1) {
//								j = i1 * DoubleDHT_3D.this.sliceStride + i;
//								DoubleDHT_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//								DoubleDHT_3D.this.t[(this.val$startt
//										+ DoubleDHT_3D.this.slices + i1)] = this.val$a[(j + 1)];
//							}
//							DoubleDHT_3D.this.dhtSlices.inverse(
//									DoubleDHT_3D.this.t, this.val$startt,
//									this.val$scale);
//							DoubleDHT_3D.this.dhtSlices.inverse(
//									DoubleDHT_3D.this.t, this.val$startt
//											+ DoubleDHT_3D.this.slices,
//									this.val$scale);
//							for (i1 = 0; i1 < DoubleDHT_3D.this.slices; ++i1) {
//								j = i1 * DoubleDHT_3D.this.sliceStride + i;
//								this.val$a[j] = DoubleDHT_3D
//										.access$800(DoubleDHT_3D.this)[(this.val$startt + i1)];
//								this.val$a[(j + 1)] = DoubleDHT_3D
//										.access$800(DoubleDHT_3D.this)[(this.val$startt
//										+ DoubleDHT_3D
//												.access$600(DoubleDHT_3D.this) + i1)];
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
//						if (DoubleDHT_3D.this.columns > 2) {
//							j = this.val$n0;
//							while (j < DoubleDHT_3D.this.rows) {
//								for (k = 0; k < DoubleDHT_3D.this.columns; k += 4) {
//									for (l = 0; l < DoubleDHT_3D.this.slices; ++l) {
//										i = this.val$startt
//												+ DoubleDHT_3D.this.slices + l;
//										DoubleDHT_3D.this.t[(this.val$startt + l)] = this.val$a[l][j][k];
//										DoubleDHT_3D.this.t[i] = this.val$a[l][j][(k + 1)];
//										DoubleDHT_3D.this.t[(i + DoubleDHT_3D.this.slices)] = this.val$a[l][j][(k + 2)];
//										DoubleDHT_3D.this.t[(i + 2 * DoubleDHT_3D.this.slices)] = this.val$a[l][j][(k + 3)];
//									}
//									DoubleDHT_3D.this.dhtSlices.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt);
//									DoubleDHT_3D.this.dhtSlices.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt
//													+ DoubleDHT_3D.this.slices);
//									DoubleDHT_3D.this.dhtSlices.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 2
//													* DoubleDHT_3D.this.slices);
//									DoubleDHT_3D.this.dhtSlices.forward(
//											DoubleDHT_3D.this.t,
//											this.val$startt + 3
//													* DoubleDHT_3D.this.slices);
//									for (l = 0; l < DoubleDHT_3D.this.slices; ++l) {
//										i = this.val$startt
//												+ DoubleDHT_3D.this.slices + l;
//										this.val$a[l][j][k] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(this.val$startt + l)];
//										this.val$a[l][j][(k + 1)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[i];
//										this.val$a[l][j][(k + 2)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(i + DoubleDHT_3D
//												.access$600(DoubleDHT_3D.this))];
//										this.val$a[l][j][(k + 3)] = DoubleDHT_3D
//												.access$800(DoubleDHT_3D.this)[(i + 2 * DoubleDHT_3D
//												.access$600(DoubleDHT_3D.this))];
//									}
//								}
//								j += this.val$nthreads;
//							}
//						} else {
//							if (DoubleDHT_3D.this.columns != 2)
//								return;
//							j = this.val$n0;
//							while (j < DoubleDHT_3D.this.rows) {
//								for (k = 0; k < DoubleDHT_3D.this.slices; ++k) {
//									DoubleDHT_3D.this.t[(this.val$startt + k)] = this.val$a[k][j][0];
//									DoubleDHT_3D.this.t[(this.val$startt
//											+ DoubleDHT_3D.this.slices + k)] = this.val$a[k][j][1];
//								}
//								DoubleDHT_3D.this.dhtSlices.forward(
//										DoubleDHT_3D.this.t, this.val$startt);
//								DoubleDHT_3D.this.dhtSlices.forward(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ DoubleDHT_3D.this.slices);
//								for (k = 0; k < DoubleDHT_3D.this.slices; ++k) {
//									this.val$a[k][j][0] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt + k)];
//									this.val$a[k][j][1] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt
//											+ DoubleDHT_3D
//													.access$600(DoubleDHT_3D.this) + k)];
//								}
//								j += this.val$nthreads;
//							}
//						}
//					} else if (DoubleDHT_3D.this.columns > 2) {
//						j = this.val$n0;
//						while (j < DoubleDHT_3D.this.rows) {
//							for (k = 0; k < DoubleDHT_3D.this.columns; k += 4) {
//								for (l = 0; l < DoubleDHT_3D.this.slices; ++l) {
//									i = this.val$startt
//											+ DoubleDHT_3D.this.slices + l;
//									DoubleDHT_3D.this.t[(this.val$startt + l)] = this.val$a[l][j][k];
//									DoubleDHT_3D.this.t[i] = this.val$a[l][j][(k + 1)];
//									DoubleDHT_3D.this.t[(i + DoubleDHT_3D.this.slices)] = this.val$a[l][j][(k + 2)];
//									DoubleDHT_3D.this.t[(i + 2 * DoubleDHT_3D.this.slices)] = this.val$a[l][j][(k + 3)];
//								}
//								DoubleDHT_3D.this.dhtSlices.inverse(
//										DoubleDHT_3D.this.t, this.val$startt,
//										this.val$scale);
//								DoubleDHT_3D.this.dhtSlices.inverse(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ DoubleDHT_3D.this.slices,
//										this.val$scale);
//								DoubleDHT_3D.this.dhtSlices.inverse(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ 2 * DoubleDHT_3D.this.slices,
//										this.val$scale);
//								DoubleDHT_3D.this.dhtSlices.inverse(
//										DoubleDHT_3D.this.t, this.val$startt
//												+ 3 * DoubleDHT_3D.this.slices,
//										this.val$scale);
//								for (l = 0; l < DoubleDHT_3D.this.slices; ++l) {
//									i = this.val$startt
//											+ DoubleDHT_3D.this.slices + l;
//									this.val$a[l][j][k] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(this.val$startt + l)];
//									this.val$a[l][j][(k + 1)] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[i];
//									this.val$a[l][j][(k + 2)] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(i + DoubleDHT_3D
//											.access$600(DoubleDHT_3D.this))];
//									this.val$a[l][j][(k + 3)] = DoubleDHT_3D
//											.access$800(DoubleDHT_3D.this)[(i + 2 * DoubleDHT_3D
//											.access$600(DoubleDHT_3D.this))];
//								}
//							}
//							j += this.val$nthreads;
//						}
//					} else {
//						if (DoubleDHT_3D.this.columns != 2)
//							return;
//						j = this.val$n0;
//						while (j < DoubleDHT_3D.this.rows) {
//							for (k = 0; k < DoubleDHT_3D.this.slices; ++k) {
//								DoubleDHT_3D.this.t[(this.val$startt + k)] = this.val$a[k][j][0];
//								DoubleDHT_3D.this.t[(this.val$startt
//										+ DoubleDHT_3D.this.slices + k)] = this.val$a[k][j][1];
//							}
//							DoubleDHT_3D.this.dhtSlices.inverse(
//									DoubleDHT_3D.this.t, this.val$startt,
//									this.val$scale);
//							DoubleDHT_3D.this.dhtSlices.inverse(
//									DoubleDHT_3D.this.t, this.val$startt
//											+ DoubleDHT_3D.this.slices,
//									this.val$scale);
//							for (k = 0; k < DoubleDHT_3D.this.slices; ++k) {
//								this.val$a[k][j][0] = DoubleDHT_3D
//										.access$800(DoubleDHT_3D.this)[(this.val$startt + k)];
//								this.val$a[k][j][1] = DoubleDHT_3D
//										.access$800(DoubleDHT_3D.this)[(this.val$startt
//										+ DoubleDHT_3D
//												.access$600(DoubleDHT_3D.this) + k)];
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
//	private void yTransform(double[] paramArrayOfDouble) {
//		for (int i12 = 0; i12 <= this.slices / 2; ++i12) {
//			int k = (this.slices - i12) % this.slices;
//			int i8 = i12 * this.sliceStride;
//			int i9 = k * this.sliceStride;
//			for (int i13 = 0; i13 <= this.rows / 2; ++i13) {
//				int j = (this.rows - i13) % this.rows;
//				int i10 = i13 * this.rowStride;
//				int i11 = j * this.rowStride;
//				for (int i14 = 0; i14 <= this.columns / 2; ++i14) {
//					int i = (this.columns - i14) % this.columns;
//					int l = i8 + i11 + i14;
//					int i1 = i8 + i10 + i;
//					int i2 = i9 + i10 + i14;
//					int i3 = i9 + i11 + i;
//					int i4 = i9 + i11 + i14;
//					int i5 = i9 + i10 + i;
//					int i6 = i8 + i10 + i14;
//					int i7 = i8 + i11 + i;
//					double d1 = paramArrayOfDouble[l];
//					double d2 = paramArrayOfDouble[i1];
//					double d3 = paramArrayOfDouble[i2];
//					double d4 = paramArrayOfDouble[i3];
//					double d5 = paramArrayOfDouble[i4];
//					double d6 = paramArrayOfDouble[i5];
//					double d7 = paramArrayOfDouble[i6];
//					double d8 = paramArrayOfDouble[i7];
//					paramArrayOfDouble[i6] = ((d1 + d2 + d3 - d4) / 2.0D);
//					paramArrayOfDouble[i2] = ((d5 + d6 + d7 - d8) / 2.0D);
//					paramArrayOfDouble[l] = ((d7 + d8 + d5 - d6) / 2.0D);
//					paramArrayOfDouble[i4] = ((d3 + d4 + d1 - d2) / 2.0D);
//					paramArrayOfDouble[i1] = ((d8 + d7 + d6 - d5) / 2.0D);
//					paramArrayOfDouble[i5] = ((d4 + d3 + d2 - d1) / 2.0D);
//					paramArrayOfDouble[i7] = ((d2 + d1 + d4 - d3) / 2.0D);
//					paramArrayOfDouble[i3] = ((d6 + d5 + d8 - d7) / 2.0D);
//				}
//			}
//		}
//	}
//
//	private void yTransform(double[][][] paramArrayOfDouble) {
//		for (int l = 0; l <= this.slices / 2; ++l) {
//			int k = (this.slices - l) % this.slices;
//			for (int i1 = 0; i1 <= this.rows / 2; ++i1) {
//				int j = (this.rows - i1) % this.rows;
//				for (int i2 = 0; i2 <= this.columns / 2; ++i2) {
//					int i = (this.columns - i2) % this.columns;
//					double d1 = paramArrayOfDouble[l][j][i2];
//					double d2 = paramArrayOfDouble[l][i1][i];
//					double d3 = paramArrayOfDouble[k][i1][i2];
//					double d4 = paramArrayOfDouble[k][j][i];
//					double d5 = paramArrayOfDouble[k][j][i2];
//					double d6 = paramArrayOfDouble[k][i1][i];
//					double d7 = paramArrayOfDouble[l][i1][i2];
//					double d8 = paramArrayOfDouble[l][j][i];
//					paramArrayOfDouble[l][i1][i2] = ((d1 + d2 + d3 - d4) / 2.0D);
//					paramArrayOfDouble[k][i1][i2] = ((d5 + d6 + d7 - d8) / 2.0D);
//					paramArrayOfDouble[l][j][i2] = ((d7 + d8 + d5 - d6) / 2.0D);
//					paramArrayOfDouble[k][j][i2] = ((d3 + d4 + d1 - d2) / 2.0D);
//					paramArrayOfDouble[l][i1][i] = ((d8 + d7 + d6 - d5) / 2.0D);
//					paramArrayOfDouble[k][i1][i] = ((d4 + d3 + d2 - d1) / 2.0D);
//					paramArrayOfDouble[l][j][i] = ((d2 + d1 + d4 - d3) / 2.0D);
//					paramArrayOfDouble[k][j][i] = ((d6 + d5 + d8 - d7) / 2.0D);
//				}
//			}
//		}
//	}
//}