package ps.hell.math.base.persons.fft.transform.dst;//package ps.landerbuluse.ml.math.fft.transform.dst;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class FloatDST_3D {
//	private int slices;
//	private int rows;
//	private int columns;
//	private int sliceStride;
//	private int rowStride;
//	private float[] t;
//	private FloatDST_1D dstSlices;
//	private FloatDST_1D dstRows;
//	private FloatDST_1D dstColumns;
//	private int oldNthreads;
//	private int nt;
//	private boolean isPowerOfTwo = false;
//	private boolean useThreads = false;
//
//	public FloatDST_3D(int paramInt1, int paramInt2, int paramInt3) {
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
//			this.t = new float[this.nt];
//		}
//		this.dstSlices = new FloatDST_1D(paramInt1);
//		if (paramInt1 == paramInt2)
//			this.dstRows = this.dstSlices;
//		else
//			this.dstRows = new FloatDST_1D(paramInt2);
//		if (paramInt1 == paramInt3)
//			this.dstColumns = this.dstSlices;
//		else if (paramInt2 == paramInt3)
//			this.dstColumns = this.dstRows;
//		else
//			this.dstColumns = new FloatDST_1D(paramInt3);
//	}
//
//	public void forward(float[] paramArrayOfFloat, boolean paramBoolean) {
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
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt3da_subth(-1, paramArrayOfFloat, paramBoolean);
//				ddxt3db_subth(-1, paramArrayOfFloat, paramBoolean);
//			} else {
//				ddxt3da_sub(-1, paramArrayOfFloat, paramBoolean);
//				ddxt3db_sub(-1, paramArrayOfFloat, paramBoolean);
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
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * FloatDST_3D.this.sliceStride;
//								for (int k = 0; k < FloatDST_3D.this.rows; ++k)
//									FloatDST_3D.this.dstColumns.forward(
//											this.val$a,
//											j + k * FloatDST_3D.this.rowStride,
//											this.val$scale);
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDST_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * FloatDST_3D.this.sliceStride;
//								for (int k = 0; k < FloatDST_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < FloatDST_3D.this.rows; ++l) {
//										i1 = j + l * FloatDST_3D.this.rowStride
//												+ k;
//										arrayOfFloat[l] = this.val$a[i1];
//									}
//									FloatDST_3D.this.dstRows.forward(
//											arrayOfFloat, this.val$scale);
//									for (l = 0; l < FloatDST_3D.this.rows; ++l) {
//										i1 = j + l * FloatDST_3D.this.rowStride
//												+ k;
//										this.val$a[i1] = arrayOfFloat[l];
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
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDST_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//								int j = i * FloatDST_3D.this.rowStride;
//								for (int k = 0; k < FloatDST_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < FloatDST_3D.this.slices; ++l) {
//										i1 = l * FloatDST_3D.this.sliceStride
//												+ j + k;
//										arrayOfFloat[l] = this.val$a[i1];
//									}
//									FloatDST_3D.this.dstSlices.forward(
//											arrayOfFloat, this.val$scale);
//									for (l = 0; l < FloatDST_3D.this.slices; ++l) {
//										i1 = l * FloatDST_3D.this.sliceStride
//												+ j + k;
//										this.val$a[i1] = arrayOfFloat[l];
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
//						this.dstColumns.forward(paramArrayOfFloat, k + l
//								* this.rowStride, paramBoolean);
//				}
//				float[] arrayOfFloat = new float[this.rows];
//				int i3;
//				for (k = 0; k < this.slices; ++k) {
//					l = k * this.sliceStride;
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							i3 = l + i2 * this.rowStride + i1;
//							arrayOfFloat[i2] = paramArrayOfFloat[i3];
//						}
//						this.dstRows.forward(arrayOfFloat, paramBoolean);
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							i3 = l + i2 * this.rowStride + i1;
//							paramArrayOfFloat[i3] = arrayOfFloat[i2];
//						}
//					}
//				}
//				arrayOfFloat = new float[this.slices];
//				for (k = 0; k < this.rows; ++k) {
//					l = k * this.rowStride;
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							arrayOfFloat[i2] = paramArrayOfFloat[i3];
//						}
//						this.dstSlices.forward(arrayOfFloat, paramBoolean);
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							paramArrayOfFloat[i3] = arrayOfFloat[i2];
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public void forward(float[][][] paramArrayOfFloat, boolean paramBoolean) {
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
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt3da_subth(-1, paramArrayOfFloat, paramBoolean);
//				ddxt3db_subth(-1, paramArrayOfFloat, paramBoolean);
//			} else {
//				ddxt3da_sub(-1, paramArrayOfFloat, paramBoolean);
//				ddxt3db_sub(-1, paramArrayOfFloat, paramBoolean);
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
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < FloatDST_3D.this.rows; ++j)
//									FloatDST_3D.this.dstColumns.forward(
//											this.val$a[i][j], this.val$scale);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDST_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < FloatDST_3D.this.columns; ++j) {
//									for (int k = 0; k < FloatDST_3D.this.rows; ++k)
//										arrayOfFloat[k] = this.val$a[i][k][j];
//									FloatDST_3D.this.dstRows.forward(
//											arrayOfFloat, this.val$scale);
//									for (k = 0; k < FloatDST_3D.this.rows; ++k)
//										this.val$a[i][k][j] = arrayOfFloat[k];
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
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDST_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								for (int j = 0; j < FloatDST_3D.this.columns; ++j) {
//									for (int k = 0; k < FloatDST_3D.this.slices; ++k)
//										arrayOfFloat[k] = this.val$a[k][i][j];
//									FloatDST_3D.this.dstSlices.forward(
//											arrayOfFloat, this.val$scale);
//									for (k = 0; k < FloatDST_3D.this.slices; ++k)
//										this.val$a[k][i][j] = arrayOfFloat[k];
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int j = 0; j < this.slices; ++j)
//					for (k = 0; k < this.rows; ++k)
//						this.dstColumns.forward(paramArrayOfFloat[j][k],
//								paramBoolean);
//				float[] arrayOfFloat = new float[this.rows];
//				for (k = 0; k < this.slices; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.rows; ++i1)
//							arrayOfFloat[i1] = paramArrayOfFloat[k][i1][l];
//						this.dstRows.forward(arrayOfFloat, paramBoolean);
//						for (i1 = 0; i1 < this.rows; ++i1)
//							paramArrayOfFloat[k][i1][l] = arrayOfFloat[i1];
//					}
//				arrayOfFloat = new float[this.slices];
//				for (k = 0; k < this.rows; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.slices; ++i1)
//							arrayOfFloat[i1] = paramArrayOfFloat[i1][k][l];
//						this.dstSlices.forward(arrayOfFloat, paramBoolean);
//						for (i1 = 0; i1 < this.slices; ++i1)
//							paramArrayOfFloat[i1][k][l] = arrayOfFloat[i1];
//					}
//			}
//		}
//	}
//
//	public void inverse(float[] paramArrayOfFloat, boolean paramBoolean) {
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
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt3da_subth(1, paramArrayOfFloat, paramBoolean);
//				ddxt3db_subth(1, paramArrayOfFloat, paramBoolean);
//			} else {
//				ddxt3da_sub(1, paramArrayOfFloat, paramBoolean);
//				ddxt3db_sub(1, paramArrayOfFloat, paramBoolean);
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
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * FloatDST_3D.this.sliceStride;
//								for (int k = 0; k < FloatDST_3D.this.rows; ++k)
//									FloatDST_3D.this.dstColumns.inverse(
//											this.val$a,
//											j + k * FloatDST_3D.this.rowStride,
//											this.val$scale);
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDST_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * FloatDST_3D.this.sliceStride;
//								for (int k = 0; k < FloatDST_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < FloatDST_3D.this.rows; ++l) {
//										i1 = j + l * FloatDST_3D.this.rowStride
//												+ k;
//										arrayOfFloat[l] = this.val$a[i1];
//									}
//									FloatDST_3D.this.dstRows.inverse(
//											arrayOfFloat, this.val$scale);
//									for (l = 0; l < FloatDST_3D.this.rows; ++l) {
//										i1 = j + l * FloatDST_3D.this.rowStride
//												+ k;
//										this.val$a[i1] = arrayOfFloat[l];
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
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDST_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//								int j = i * FloatDST_3D.this.rowStride;
//								for (int k = 0; k < FloatDST_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < FloatDST_3D.this.slices; ++l) {
//										i1 = l * FloatDST_3D.this.sliceStride
//												+ j + k;
//										arrayOfFloat[l] = this.val$a[i1];
//									}
//									FloatDST_3D.this.dstSlices.inverse(
//											arrayOfFloat, this.val$scale);
//									for (l = 0; l < FloatDST_3D.this.slices; ++l) {
//										i1 = l * FloatDST_3D.this.sliceStride
//												+ j + k;
//										this.val$a[i1] = arrayOfFloat[l];
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
//						this.dstColumns.inverse(paramArrayOfFloat, k + l
//								* this.rowStride, paramBoolean);
//				}
//				float[] arrayOfFloat = new float[this.rows];
//				int i3;
//				for (k = 0; k < this.slices; ++k) {
//					l = k * this.sliceStride;
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							i3 = l + i2 * this.rowStride + i1;
//							arrayOfFloat[i2] = paramArrayOfFloat[i3];
//						}
//						this.dstRows.inverse(arrayOfFloat, paramBoolean);
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							i3 = l + i2 * this.rowStride + i1;
//							paramArrayOfFloat[i3] = arrayOfFloat[i2];
//						}
//					}
//				}
//				arrayOfFloat = new float[this.slices];
//				for (k = 0; k < this.rows; ++k) {
//					l = k * this.rowStride;
//					for (i1 = 0; i1 < this.columns; ++i1) {
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							arrayOfFloat[i2] = paramArrayOfFloat[i3];
//						}
//						this.dstSlices.inverse(arrayOfFloat, paramBoolean);
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							paramArrayOfFloat[i3] = arrayOfFloat[i2];
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public void inverse(float[][][] paramArrayOfFloat, boolean paramBoolean) {
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
//				this.t = new float[this.nt];
//				this.oldNthreads = i;
//			}
//			if ((i > 1) && (this.useThreads)) {
//				ddxt3da_subth(1, paramArrayOfFloat, paramBoolean);
//				ddxt3db_subth(1, paramArrayOfFloat, paramBoolean);
//			} else {
//				ddxt3da_sub(1, paramArrayOfFloat, paramBoolean);
//				ddxt3db_sub(1, paramArrayOfFloat, paramBoolean);
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
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < FloatDST_3D.this.rows; ++j)
//									FloatDST_3D.this.dstColumns.inverse(
//											this.val$a[i][j], this.val$scale);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDST_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < FloatDST_3D.this.columns; ++j) {
//									for (int k = 0; k < FloatDST_3D.this.rows; ++k)
//										arrayOfFloat[k] = this.val$a[i][k][j];
//									FloatDST_3D.this.dstRows.inverse(
//											arrayOfFloat, this.val$scale);
//									for (k = 0; k < FloatDST_3D.this.rows; ++k)
//										this.val$a[i][k][j] = arrayOfFloat[k];
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
//							i2, paramArrayOfFloat, paramBoolean) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDST_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								for (int j = 0; j < FloatDST_3D.this.columns; ++j) {
//									for (int k = 0; k < FloatDST_3D.this.slices; ++k)
//										arrayOfFloat[k] = this.val$a[k][i][j];
//									FloatDST_3D.this.dstSlices.inverse(
//											arrayOfFloat, this.val$scale);
//									for (k = 0; k < FloatDST_3D.this.slices; ++k)
//										this.val$a[k][i][j] = arrayOfFloat[k];
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int j = 0; j < this.slices; ++j)
//					for (k = 0; k < this.rows; ++k)
//						this.dstColumns.inverse(paramArrayOfFloat[j][k],
//								paramBoolean);
//				float[] arrayOfFloat = new float[this.rows];
//				for (k = 0; k < this.slices; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.rows; ++i1)
//							arrayOfFloat[i1] = paramArrayOfFloat[k][i1][l];
//						this.dstRows.inverse(arrayOfFloat, paramBoolean);
//						for (i1 = 0; i1 < this.rows; ++i1)
//							paramArrayOfFloat[k][i1][l] = arrayOfFloat[i1];
//					}
//				arrayOfFloat = new float[this.slices];
//				for (k = 0; k < this.rows; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.slices; ++i1)
//							arrayOfFloat[i1] = paramArrayOfFloat[i1][k][l];
//						this.dstSlices.inverse(arrayOfFloat, paramBoolean);
//						for (i1 = 0; i1 < this.slices; ++i1)
//							paramArrayOfFloat[i1][k][l] = arrayOfFloat[i1];
//					}
//			}
//		}
//	}
//
//	private void ddxt3da_sub(int paramInt, float[] paramArrayOfFloat,
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
//					this.dstColumns.forward(paramArrayOfFloat, i + i1
//							* this.rowStride, paramBoolean);
//				if (this.columns > 2) {
//					for (i1 = 0; i1 < this.columns; i1 += 4) {
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							j = i + i2 * this.rowStride + i1;
//							k = this.rows + i2;
//							this.t[i2] = paramArrayOfFloat[j];
//							this.t[k] = paramArrayOfFloat[(j + 1)];
//							this.t[(k + this.rows)] = paramArrayOfFloat[(j + 2)];
//							this.t[(k + 2 * this.rows)] = paramArrayOfFloat[(j + 3)];
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
//							paramArrayOfFloat[j] = this.t[i2];
//							paramArrayOfFloat[(j + 1)] = this.t[k];
//							paramArrayOfFloat[(j + 2)] = this.t[(k + this.rows)];
//							paramArrayOfFloat[(j + 3)] = this.t[(k + 2 * this.rows)];
//						}
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (i1 = 0; i1 < this.rows; ++i1) {
//						j = i + i1 * this.rowStride;
//						this.t[i1] = paramArrayOfFloat[j];
//						this.t[(this.rows + i1)] = paramArrayOfFloat[(j + 1)];
//					}
//					this.dstRows.forward(this.t, 0, paramBoolean);
//					this.dstRows.forward(this.t, this.rows, paramBoolean);
//					for (i1 = 0; i1 < this.rows; ++i1) {
//						j = i + i1 * this.rowStride;
//						paramArrayOfFloat[j] = this.t[i1];
//						paramArrayOfFloat[(j + 1)] = this.t[(this.rows + i1)];
//					}
//				}
//			}
//		else
//			for (l = 0; l < this.slices; ++l) {
//				i = l * this.sliceStride;
//				for (i1 = 0; i1 < this.rows; ++i1)
//					this.dstColumns.inverse(paramArrayOfFloat, i + i1
//							* this.rowStride, paramBoolean);
//				if (this.columns > 2) {
//					for (i1 = 0; i1 < this.columns; i1 += 4) {
//						for (i2 = 0; i2 < this.rows; ++i2) {
//							j = i + i2 * this.rowStride + i1;
//							k = this.rows + i2;
//							this.t[i2] = paramArrayOfFloat[j];
//							this.t[k] = paramArrayOfFloat[(j + 1)];
//							this.t[(k + this.rows)] = paramArrayOfFloat[(j + 2)];
//							this.t[(k + 2 * this.rows)] = paramArrayOfFloat[(j + 3)];
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
//							paramArrayOfFloat[j] = this.t[i2];
//							paramArrayOfFloat[(j + 1)] = this.t[k];
//							paramArrayOfFloat[(j + 2)] = this.t[(k + this.rows)];
//							paramArrayOfFloat[(j + 3)] = this.t[(k + 2 * this.rows)];
//						}
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (i1 = 0; i1 < this.rows; ++i1) {
//						j = i + i1 * this.rowStride;
//						this.t[i1] = paramArrayOfFloat[j];
//						this.t[(this.rows + i1)] = paramArrayOfFloat[(j + 1)];
//					}
//					this.dstRows.inverse(this.t, 0, paramBoolean);
//					this.dstRows.inverse(this.t, this.rows, paramBoolean);
//					for (i1 = 0; i1 < this.rows; ++i1) {
//						j = i + i1 * this.rowStride;
//						paramArrayOfFloat[j] = this.t[i1];
//						paramArrayOfFloat[(j + 1)] = this.t[(this.rows + i1)];
//					}
//				}
//			}
//	}
//
//	private void ddxt3da_sub(int paramInt, float[][][] paramArrayOfFloat,
//			boolean paramBoolean) {
//		int j;
//		int k;
//		int l;
//		int i;
//		if (paramInt == -1)
//			for (j = 0; j < this.slices; ++j) {
//				for (k = 0; k < this.rows; ++k)
//					this.dstColumns.forward(paramArrayOfFloat[j][k],
//							paramBoolean);
//				if (this.columns > 2) {
//					for (k = 0; k < this.columns; k += 4) {
//						for (l = 0; l < this.rows; ++l) {
//							i = this.rows + l;
//							this.t[l] = paramArrayOfFloat[j][l][k];
//							this.t[i] = paramArrayOfFloat[j][l][(k + 1)];
//							this.t[(i + this.rows)] = paramArrayOfFloat[j][l][(k + 2)];
//							this.t[(i + 2 * this.rows)] = paramArrayOfFloat[j][l][(k + 3)];
//						}
//						this.dstRows.forward(this.t, 0, paramBoolean);
//						this.dstRows.forward(this.t, this.rows, paramBoolean);
//						this.dstRows.forward(this.t, 2 * this.rows,
//								paramBoolean);
//						this.dstRows.forward(this.t, 3 * this.rows,
//								paramBoolean);
//						for (l = 0; l < this.rows; ++l) {
//							i = this.rows + l;
//							paramArrayOfFloat[j][l][k] = this.t[l];
//							paramArrayOfFloat[j][l][(k + 1)] = this.t[i];
//							paramArrayOfFloat[j][l][(k + 2)] = this.t[(i + this.rows)];
//							paramArrayOfFloat[j][l][(k + 3)] = this.t[(i + 2 * this.rows)];
//						}
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (k = 0; k < this.rows; ++k) {
//						this.t[k] = paramArrayOfFloat[j][k][0];
//						this.t[(this.rows + k)] = paramArrayOfFloat[j][k][1];
//					}
//					this.dstRows.forward(this.t, 0, paramBoolean);
//					this.dstRows.forward(this.t, this.rows, paramBoolean);
//					for (k = 0; k < this.rows; ++k) {
//						paramArrayOfFloat[j][k][0] = this.t[k];
//						paramArrayOfFloat[j][k][1] = this.t[(this.rows + k)];
//					}
//				}
//			}
//		else
//			for (j = 0; j < this.slices; ++j) {
//				for (k = 0; k < this.rows; ++k)
//					this.dstColumns.inverse(paramArrayOfFloat[j][k],
//							paramBoolean);
//				if (this.columns > 2) {
//					for (k = 0; k < this.columns; k += 4) {
//						for (l = 0; l < this.rows; ++l) {
//							i = this.rows + l;
//							this.t[l] = paramArrayOfFloat[j][l][k];
//							this.t[i] = paramArrayOfFloat[j][l][(k + 1)];
//							this.t[(i + this.rows)] = paramArrayOfFloat[j][l][(k + 2)];
//							this.t[(i + 2 * this.rows)] = paramArrayOfFloat[j][l][(k + 3)];
//						}
//						this.dstRows.inverse(this.t, 0, paramBoolean);
//						this.dstRows.inverse(this.t, this.rows, paramBoolean);
//						this.dstRows.inverse(this.t, 2 * this.rows,
//								paramBoolean);
//						this.dstRows.inverse(this.t, 3 * this.rows,
//								paramBoolean);
//						for (l = 0; l < this.rows; ++l) {
//							i = this.rows + l;
//							paramArrayOfFloat[j][l][k] = this.t[l];
//							paramArrayOfFloat[j][l][(k + 1)] = this.t[i];
//							paramArrayOfFloat[j][l][(k + 2)] = this.t[(i + this.rows)];
//							paramArrayOfFloat[j][l][(k + 3)] = this.t[(i + 2 * this.rows)];
//						}
//					}
//				} else {
//					if (this.columns != 2)
//						continue;
//					for (k = 0; k < this.rows; ++k) {
//						this.t[k] = paramArrayOfFloat[j][k][0];
//						this.t[(this.rows + k)] = paramArrayOfFloat[j][k][1];
//					}
//					this.dstRows.inverse(this.t, 0, paramBoolean);
//					this.dstRows.inverse(this.t, this.rows, paramBoolean);
//					for (k = 0; k < this.rows; ++k) {
//						paramArrayOfFloat[j][k][0] = this.t[k];
//						paramArrayOfFloat[j][k][1] = this.t[(this.rows + k)];
//					}
//				}
//			}
//	}
//
//	private void ddxt3db_sub(int paramInt, float[] paramArrayOfFloat,
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
//							this.t[i2] = paramArrayOfFloat[j];
//							this.t[k] = paramArrayOfFloat[(j + 1)];
//							this.t[(k + this.slices)] = paramArrayOfFloat[(j + 2)];
//							this.t[(k + 2 * this.slices)] = paramArrayOfFloat[(j + 3)];
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
//							paramArrayOfFloat[j] = this.t[i2];
//							paramArrayOfFloat[(j + 1)] = this.t[k];
//							paramArrayOfFloat[(j + 2)] = this.t[(k + this.slices)];
//							paramArrayOfFloat[(j + 3)] = this.t[(k + 2 * this.slices)];
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
//						this.t[i1] = paramArrayOfFloat[j];
//						this.t[(this.slices + i1)] = paramArrayOfFloat[(j + 1)];
//					}
//					this.dstSlices.forward(this.t, 0, paramBoolean);
//					this.dstSlices.forward(this.t, this.slices, paramBoolean);
//					for (i1 = 0; i1 < this.slices; ++i1) {
//						j = i1 * this.sliceStride + i;
//						paramArrayOfFloat[j] = this.t[i1];
//						paramArrayOfFloat[(j + 1)] = this.t[(this.slices + i1)];
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
//						this.t[i2] = paramArrayOfFloat[j];
//						this.t[k] = paramArrayOfFloat[(j + 1)];
//						this.t[(k + this.slices)] = paramArrayOfFloat[(j + 2)];
//						this.t[(k + 2 * this.slices)] = paramArrayOfFloat[(j + 3)];
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
//						paramArrayOfFloat[j] = this.t[i2];
//						paramArrayOfFloat[(j + 1)] = this.t[k];
//						paramArrayOfFloat[(j + 2)] = this.t[(k + this.slices)];
//						paramArrayOfFloat[(j + 3)] = this.t[(k + 2 * this.slices)];
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
//					this.t[i1] = paramArrayOfFloat[j];
//					this.t[(this.slices + i1)] = paramArrayOfFloat[(j + 1)];
//				}
//				this.dstSlices.inverse(this.t, 0, paramBoolean);
//				this.dstSlices.inverse(this.t, this.slices, paramBoolean);
//				for (i1 = 0; i1 < this.slices; ++i1) {
//					j = i1 * this.sliceStride + i;
//					paramArrayOfFloat[j] = this.t[i1];
//					paramArrayOfFloat[(j + 1)] = this.t[(this.slices + i1)];
//				}
//			}
//		}
//	}
//
//	private void ddxt3db_sub(int paramInt, float[][][] paramArrayOfFloat,
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
//							this.t[l] = paramArrayOfFloat[l][j][k];
//							this.t[i] = paramArrayOfFloat[l][j][(k + 1)];
//							this.t[(i + this.slices)] = paramArrayOfFloat[l][j][(k + 2)];
//							this.t[(i + 2 * this.slices)] = paramArrayOfFloat[l][j][(k + 3)];
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
//							paramArrayOfFloat[l][j][k] = this.t[l];
//							paramArrayOfFloat[l][j][(k + 1)] = this.t[i];
//							paramArrayOfFloat[l][j][(k + 2)] = this.t[(i + this.slices)];
//							paramArrayOfFloat[l][j][(k + 3)] = this.t[(i + 2 * this.slices)];
//						}
//					}
//			} else {
//				if (this.columns != 2)
//					return;
//				for (j = 0; j < this.rows; ++j) {
//					for (k = 0; k < this.slices; ++k) {
//						this.t[k] = paramArrayOfFloat[k][j][0];
//						this.t[(this.slices + k)] = paramArrayOfFloat[k][j][1];
//					}
//					this.dstSlices.forward(this.t, 0, paramBoolean);
//					this.dstSlices.forward(this.t, this.slices, paramBoolean);
//					for (k = 0; k < this.slices; ++k) {
//						paramArrayOfFloat[k][j][0] = this.t[k];
//						paramArrayOfFloat[k][j][1] = this.t[(this.slices + k)];
//					}
//				}
//			}
//		} else if (this.columns > 2) {
//			for (j = 0; j < this.rows; ++j)
//				for (k = 0; k < this.columns; k += 4) {
//					for (l = 0; l < this.slices; ++l) {
//						i = this.slices + l;
//						this.t[l] = paramArrayOfFloat[l][j][k];
//						this.t[i] = paramArrayOfFloat[l][j][(k + 1)];
//						this.t[(i + this.slices)] = paramArrayOfFloat[l][j][(k + 2)];
//						this.t[(i + 2 * this.slices)] = paramArrayOfFloat[l][j][(k + 3)];
//					}
//					this.dstSlices.inverse(this.t, 0, paramBoolean);
//					this.dstSlices.inverse(this.t, this.slices, paramBoolean);
//					this.dstSlices.inverse(this.t, 2 * this.slices,
//							paramBoolean);
//					this.dstSlices.inverse(this.t, 3 * this.slices,
//							paramBoolean);
//					for (l = 0; l < this.slices; ++l) {
//						i = this.slices + l;
//						paramArrayOfFloat[l][j][k] = this.t[l];
//						paramArrayOfFloat[l][j][(k + 1)] = this.t[i];
//						paramArrayOfFloat[l][j][(k + 2)] = this.t[(i + this.slices)];
//						paramArrayOfFloat[l][j][(k + 3)] = this.t[(i + 2 * this.slices)];
//					}
//				}
//		} else {
//			if (this.columns != 2)
//				return;
//			for (j = 0; j < this.rows; ++j) {
//				for (k = 0; k < this.slices; ++k) {
//					this.t[k] = paramArrayOfFloat[k][j][0];
//					this.t[(this.slices + k)] = paramArrayOfFloat[k][j][1];
//				}
//				this.dstSlices.inverse(this.t, 0, paramBoolean);
//				this.dstSlices.inverse(this.t, this.slices, paramBoolean);
//				for (k = 0; k < this.slices; ++k) {
//					paramArrayOfFloat[k][j][0] = this.t[k];
//					paramArrayOfFloat[k][j][1] = this.t[(this.slices + k)];
//				}
//			}
//		}
//	}
//
//	private void ddxt3da_subth(int paramInt, float[] paramArrayOfFloat,
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
//					l, i, paramArrayOfFloat, paramBoolean, i1) {
//				public void run() {
//					int l;
//					int i;
//					int i1;
//					int i2;
//					int j;
//					int k;
//					if (this.val$isgn == -1) {
//						l = this.val$n0;
//						while (l < FloatDST_3D.this.slices) {
//							i = l * FloatDST_3D.this.sliceStride;
//							for (i1 = 0; i1 < FloatDST_3D.this.rows; ++i1)
//								FloatDST_3D.this.dstColumns.forward(this.val$a,
//										i + i1 * FloatDST_3D.this.rowStride,
//										this.val$scale);
//							if (FloatDST_3D.this.columns > 2) {
//								for (i1 = 0; i1 < FloatDST_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < FloatDST_3D.this.rows; ++i2) {
//										j = i + i2 * FloatDST_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ FloatDST_3D.this.rows + i2;
//										FloatDST_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										FloatDST_3D.this.t[k] = this.val$a[(j + 1)];
//										FloatDST_3D.this.t[(k + FloatDST_3D.this.rows)] = this.val$a[(j + 2)];
//										FloatDST_3D.this.t[(k + 2 * FloatDST_3D.this.rows)] = this.val$a[(j + 3)];
//									}
//									FloatDST_3D.this.dstRows.forward(
//											FloatDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatDST_3D.this.dstRows.forward(
//											FloatDST_3D.this.t, this.val$startt
//													+ FloatDST_3D.this.rows,
//											this.val$scale);
//									FloatDST_3D.this.dstRows.forward(
//											FloatDST_3D.this.t,
//											this.val$startt + 2
//													* FloatDST_3D.this.rows,
//											this.val$scale);
//									FloatDST_3D.this.dstRows.forward(
//											FloatDST_3D.this.t,
//											this.val$startt + 3
//													* FloatDST_3D.this.rows,
//											this.val$scale);
//									for (i2 = 0; i2 < FloatDST_3D.this.rows; ++i2) {
//										j = i + i2 * FloatDST_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ FloatDST_3D.this.rows + i2;
//										this.val$a[j] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[k];
//										this.val$a[(j + 2)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(k + FloatDST_3D
//												.access$100(FloatDST_3D.this))];
//										this.val$a[(j + 3)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(k + 2 * FloatDST_3D
//												.access$100(FloatDST_3D.this))];
//									}
//								}
//							} else if (FloatDST_3D.this.columns == 2) {
//								for (i1 = 0; i1 < FloatDST_3D.this.rows; ++i1) {
//									j = i + i1 * FloatDST_3D.this.rowStride;
//									FloatDST_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									FloatDST_3D.this.t[(this.val$startt
//											+ FloatDST_3D.this.rows + i1)] = this.val$a[(j + 1)];
//								}
//								FloatDST_3D.this.dstRows.forward(
//										FloatDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDST_3D.this.dstRows.forward(
//										FloatDST_3D.this.t, this.val$startt
//												+ FloatDST_3D.this.rows,
//										this.val$scale);
//								for (i1 = 0; i1 < FloatDST_3D.this.rows; ++i1) {
//									j = i + i1 * FloatDST_3D.this.rowStride;
//									this.val$a[j] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt
//											+ FloatDST_3D
//													.access$100(FloatDST_3D.this) + i1)];
//								}
//							}
//							l += this.val$nthreads;
//						}
//					} else {
//						l = this.val$n0;
//						while (l < FloatDST_3D.this.slices) {
//							i = l * FloatDST_3D.this.sliceStride;
//							for (i1 = 0; i1 < FloatDST_3D.this.rows; ++i1)
//								FloatDST_3D.this.dstColumns.inverse(this.val$a,
//										i + i1 * FloatDST_3D.this.rowStride,
//										this.val$scale);
//							if (FloatDST_3D.this.columns > 2) {
//								for (i1 = 0; i1 < FloatDST_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < FloatDST_3D.this.rows; ++i2) {
//										j = i + i2 * FloatDST_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ FloatDST_3D.this.rows + i2;
//										FloatDST_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										FloatDST_3D.this.t[k] = this.val$a[(j + 1)];
//										FloatDST_3D.this.t[(k + FloatDST_3D.this.rows)] = this.val$a[(j + 2)];
//										FloatDST_3D.this.t[(k + 2 * FloatDST_3D.this.rows)] = this.val$a[(j + 3)];
//									}
//									FloatDST_3D.this.dstRows.inverse(
//											FloatDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatDST_3D.this.dstRows.inverse(
//											FloatDST_3D.this.t, this.val$startt
//													+ FloatDST_3D.this.rows,
//											this.val$scale);
//									FloatDST_3D.this.dstRows.inverse(
//											FloatDST_3D.this.t,
//											this.val$startt + 2
//													* FloatDST_3D.this.rows,
//											this.val$scale);
//									FloatDST_3D.this.dstRows.inverse(
//											FloatDST_3D.this.t,
//											this.val$startt + 3
//													* FloatDST_3D.this.rows,
//											this.val$scale);
//									for (i2 = 0; i2 < FloatDST_3D.this.rows; ++i2) {
//										j = i + i2 * FloatDST_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ FloatDST_3D.this.rows + i2;
//										this.val$a[j] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[k];
//										this.val$a[(j + 2)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(k + FloatDST_3D
//												.access$100(FloatDST_3D.this))];
//										this.val$a[(j + 3)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(k + 2 * FloatDST_3D
//												.access$100(FloatDST_3D.this))];
//									}
//								}
//							} else if (FloatDST_3D.this.columns == 2) {
//								for (i1 = 0; i1 < FloatDST_3D.this.rows; ++i1) {
//									j = i + i1 * FloatDST_3D.this.rowStride;
//									FloatDST_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									FloatDST_3D.this.t[(this.val$startt
//											+ FloatDST_3D.this.rows + i1)] = this.val$a[(j + 1)];
//								}
//								FloatDST_3D.this.dstRows.inverse(
//										FloatDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDST_3D.this.dstRows.inverse(
//										FloatDST_3D.this.t, this.val$startt
//												+ FloatDST_3D.this.rows,
//										this.val$scale);
//								for (i1 = 0; i1 < FloatDST_3D.this.rows; ++i1) {
//									j = i + i1 * FloatDST_3D.this.rowStride;
//									this.val$a[j] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt
//											+ FloatDST_3D
//													.access$100(FloatDST_3D.this) + i1)];
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
//	private void ddxt3da_subth(int paramInt, float[][][] paramArrayOfFloat,
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
//					l, i, paramArrayOfFloat, paramBoolean, i1) {
//				public void run() {
//					int j;
//					int k;
//					int l;
//					int i;
//					if (this.val$isgn == -1) {
//						j = this.val$n0;
//						while (j < FloatDST_3D.this.slices) {
//							for (k = 0; k < FloatDST_3D.this.rows; ++k)
//								FloatDST_3D.this.dstColumns.forward(
//										this.val$a[j][k], this.val$scale);
//							if (FloatDST_3D.this.columns > 2) {
//								for (k = 0; k < FloatDST_3D.this.columns; k += 4) {
//									for (l = 0; l < FloatDST_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ FloatDST_3D.this.rows + l;
//										FloatDST_3D.this.t[(this.val$startt + l)] = this.val$a[j][l][k];
//										FloatDST_3D.this.t[i] = this.val$a[j][l][(k + 1)];
//										FloatDST_3D.this.t[(i + FloatDST_3D.this.rows)] = this.val$a[j][l][(k + 2)];
//										FloatDST_3D.this.t[(i + 2 * FloatDST_3D.this.rows)] = this.val$a[j][l][(k + 3)];
//									}
//									FloatDST_3D.this.dstRows.forward(
//											FloatDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatDST_3D.this.dstRows.forward(
//											FloatDST_3D.this.t, this.val$startt
//													+ FloatDST_3D.this.rows,
//											this.val$scale);
//									FloatDST_3D.this.dstRows.forward(
//											FloatDST_3D.this.t,
//											this.val$startt + 2
//													* FloatDST_3D.this.rows,
//											this.val$scale);
//									FloatDST_3D.this.dstRows.forward(
//											FloatDST_3D.this.t,
//											this.val$startt + 3
//													* FloatDST_3D.this.rows,
//											this.val$scale);
//									for (l = 0; l < FloatDST_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ FloatDST_3D.this.rows + l;
//										this.val$a[j][l][k] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(this.val$startt + l)];
//										this.val$a[j][l][(k + 1)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[i];
//										this.val$a[j][l][(k + 2)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(i + FloatDST_3D
//												.access$100(FloatDST_3D.this))];
//										this.val$a[j][l][(k + 3)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(i + 2 * FloatDST_3D
//												.access$100(FloatDST_3D.this))];
//									}
//								}
//							} else if (FloatDST_3D.this.columns == 2) {
//								for (k = 0; k < FloatDST_3D.this.rows; ++k) {
//									FloatDST_3D.this.t[(this.val$startt + k)] = this.val$a[j][k][0];
//									FloatDST_3D.this.t[(this.val$startt
//											+ FloatDST_3D.this.rows + k)] = this.val$a[j][k][1];
//								}
//								FloatDST_3D.this.dstRows.forward(
//										FloatDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDST_3D.this.dstRows.forward(
//										FloatDST_3D.this.t, this.val$startt
//												+ FloatDST_3D.this.rows,
//										this.val$scale);
//								for (k = 0; k < FloatDST_3D.this.rows; ++k) {
//									this.val$a[j][k][0] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt + k)];
//									this.val$a[j][k][1] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt
//											+ FloatDST_3D
//													.access$100(FloatDST_3D.this) + k)];
//								}
//							}
//							j += this.val$nthreads;
//						}
//					} else {
//						j = this.val$n0;
//						while (j < FloatDST_3D.this.slices) {
//							for (k = 0; k < FloatDST_3D.this.rows; ++k)
//								FloatDST_3D.this.dstColumns.inverse(
//										this.val$a[j][k], this.val$scale);
//							if (FloatDST_3D.this.columns > 2) {
//								for (k = 0; k < FloatDST_3D.this.columns; k += 4) {
//									for (l = 0; l < FloatDST_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ FloatDST_3D.this.rows + l;
//										FloatDST_3D.this.t[(this.val$startt + l)] = this.val$a[j][l][k];
//										FloatDST_3D.this.t[i] = this.val$a[j][l][(k + 1)];
//										FloatDST_3D.this.t[(i + FloatDST_3D.this.rows)] = this.val$a[j][l][(k + 2)];
//										FloatDST_3D.this.t[(i + 2 * FloatDST_3D.this.rows)] = this.val$a[j][l][(k + 3)];
//									}
//									FloatDST_3D.this.dstRows.inverse(
//											FloatDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatDST_3D.this.dstRows.inverse(
//											FloatDST_3D.this.t, this.val$startt
//													+ FloatDST_3D.this.rows,
//											this.val$scale);
//									FloatDST_3D.this.dstRows.inverse(
//											FloatDST_3D.this.t,
//											this.val$startt + 2
//													* FloatDST_3D.this.rows,
//											this.val$scale);
//									FloatDST_3D.this.dstRows.inverse(
//											FloatDST_3D.this.t,
//											this.val$startt + 3
//													* FloatDST_3D.this.rows,
//											this.val$scale);
//									for (l = 0; l < FloatDST_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ FloatDST_3D.this.rows + l;
//										this.val$a[j][l][k] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(this.val$startt + l)];
//										this.val$a[j][l][(k + 1)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[i];
//										this.val$a[j][l][(k + 2)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(i + FloatDST_3D
//												.access$100(FloatDST_3D.this))];
//										this.val$a[j][l][(k + 3)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(i + 2 * FloatDST_3D
//												.access$100(FloatDST_3D.this))];
//									}
//								}
//							} else if (FloatDST_3D.this.columns == 2) {
//								for (k = 0; k < FloatDST_3D.this.rows; ++k) {
//									FloatDST_3D.this.t[(this.val$startt + k)] = this.val$a[j][k][0];
//									FloatDST_3D.this.t[(this.val$startt
//											+ FloatDST_3D.this.rows + k)] = this.val$a[j][k][1];
//								}
//								FloatDST_3D.this.dstRows.inverse(
//										FloatDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDST_3D.this.dstRows.inverse(
//										FloatDST_3D.this.t, this.val$startt
//												+ FloatDST_3D.this.rows,
//										this.val$scale);
//								for (k = 0; k < FloatDST_3D.this.rows; ++k) {
//									this.val$a[j][k][0] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt + k)];
//									this.val$a[j][k][1] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt
//											+ FloatDST_3D
//													.access$100(FloatDST_3D.this) + k)];
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
//	private void ddxt3db_subth(int paramInt, float[] paramArrayOfFloat,
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
//					l, i, i1, paramArrayOfFloat, paramBoolean) {
//				public void run() {
//					int l;
//					int i;
//					int i1;
//					int i2;
//					int j;
//					int k;
//					if (this.val$isgn == -1) {
//						if (FloatDST_3D.this.columns > 2) {
//							l = this.val$n0;
//							while (l < FloatDST_3D.this.rows) {
//								i = l * FloatDST_3D.this.rowStride;
//								for (i1 = 0; i1 < FloatDST_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < FloatDST_3D.this.slices; ++i2) {
//										j = i2 * FloatDST_3D.this.sliceStride
//												+ i + i1;
//										k = this.val$startt
//												+ FloatDST_3D.this.slices + i2;
//										FloatDST_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										FloatDST_3D.this.t[k] = this.val$a[(j + 1)];
//										FloatDST_3D.this.t[(k + FloatDST_3D.this.slices)] = this.val$a[(j + 2)];
//										FloatDST_3D.this.t[(k + 2 * FloatDST_3D.this.slices)] = this.val$a[(j + 3)];
//									}
//									FloatDST_3D.this.dstSlices.forward(
//											FloatDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatDST_3D.this.dstSlices.forward(
//											FloatDST_3D.this.t, this.val$startt
//													+ FloatDST_3D.this.slices,
//											this.val$scale);
//									FloatDST_3D.this.dstSlices.forward(
//											FloatDST_3D.this.t, this.val$startt
//													+ 2
//													* FloatDST_3D.this.slices,
//											this.val$scale);
//									FloatDST_3D.this.dstSlices.forward(
//											FloatDST_3D.this.t, this.val$startt
//													+ 3
//													* FloatDST_3D.this.slices,
//											this.val$scale);
//									for (i2 = 0; i2 < FloatDST_3D.this.slices; ++i2) {
//										j = i2 * FloatDST_3D.this.sliceStride
//												+ i + i1;
//										k = this.val$startt
//												+ FloatDST_3D.this.slices + i2;
//										this.val$a[j] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[k];
//										this.val$a[(j + 2)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(k + FloatDST_3D
//												.access$600(FloatDST_3D.this))];
//										this.val$a[(j + 3)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(k + 2 * FloatDST_3D
//												.access$600(FloatDST_3D.this))];
//									}
//								}
//								l += this.val$nthreads;
//							}
//						} else {
//							if (FloatDST_3D.this.columns != 2)
//								return;
//							l = this.val$n0;
//							while (l < FloatDST_3D.this.rows) {
//								i = l * FloatDST_3D.this.rowStride;
//								for (i1 = 0; i1 < FloatDST_3D.this.slices; ++i1) {
//									j = i1 * FloatDST_3D.this.sliceStride + i;
//									FloatDST_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									FloatDST_3D.this.t[(this.val$startt
//											+ FloatDST_3D.this.slices + i1)] = this.val$a[(j + 1)];
//								}
//								FloatDST_3D.this.dstSlices.forward(
//										FloatDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDST_3D.this.dstSlices.forward(
//										FloatDST_3D.this.t, this.val$startt
//												+ FloatDST_3D.this.slices,
//										this.val$scale);
//								for (i1 = 0; i1 < FloatDST_3D.this.slices; ++i1) {
//									j = i1 * FloatDST_3D.this.sliceStride + i;
//									this.val$a[j] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt
//											+ FloatDST_3D
//													.access$600(FloatDST_3D.this) + i1)];
//								}
//								l += this.val$nthreads;
//							}
//						}
//					} else if (FloatDST_3D.this.columns > 2) {
//						l = this.val$n0;
//						while (l < FloatDST_3D.this.rows) {
//							i = l * FloatDST_3D.this.rowStride;
//							for (i1 = 0; i1 < FloatDST_3D.this.columns; i1 += 4) {
//								for (i2 = 0; i2 < FloatDST_3D.this.slices; ++i2) {
//									j = i2 * FloatDST_3D.this.sliceStride + i
//											+ i1;
//									k = this.val$startt
//											+ FloatDST_3D.this.slices + i2;
//									FloatDST_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//									FloatDST_3D.this.t[k] = this.val$a[(j + 1)];
//									FloatDST_3D.this.t[(k + FloatDST_3D.this.slices)] = this.val$a[(j + 2)];
//									FloatDST_3D.this.t[(k + 2 * FloatDST_3D.this.slices)] = this.val$a[(j + 3)];
//								}
//								FloatDST_3D.this.dstSlices.inverse(
//										FloatDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDST_3D.this.dstSlices.inverse(
//										FloatDST_3D.this.t, this.val$startt
//												+ FloatDST_3D.this.slices,
//										this.val$scale);
//								FloatDST_3D.this.dstSlices.inverse(
//										FloatDST_3D.this.t, this.val$startt + 2
//												* FloatDST_3D.this.slices,
//										this.val$scale);
//								FloatDST_3D.this.dstSlices.inverse(
//										FloatDST_3D.this.t, this.val$startt + 3
//												* FloatDST_3D.this.slices,
//										this.val$scale);
//								for (i2 = 0; i2 < FloatDST_3D.this.slices; ++i2) {
//									j = i2 * FloatDST_3D.this.sliceStride + i
//											+ i1;
//									k = this.val$startt
//											+ FloatDST_3D.this.slices + i2;
//									this.val$a[j] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt + i2)];
//									this.val$a[(j + 1)] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[k];
//									this.val$a[(j + 2)] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(k + FloatDST_3D
//											.access$600(FloatDST_3D.this))];
//									this.val$a[(j + 3)] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(k + 2 * FloatDST_3D
//											.access$600(FloatDST_3D.this))];
//								}
//							}
//							l += this.val$nthreads;
//						}
//					} else {
//						if (FloatDST_3D.this.columns != 2)
//							return;
//						l = this.val$n0;
//						while (l < FloatDST_3D.this.rows) {
//							i = l * FloatDST_3D.this.rowStride;
//							for (i1 = 0; i1 < FloatDST_3D.this.slices; ++i1) {
//								j = i1 * FloatDST_3D.this.sliceStride + i;
//								FloatDST_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//								FloatDST_3D.this.t[(this.val$startt
//										+ FloatDST_3D.this.slices + i1)] = this.val$a[(j + 1)];
//							}
//							FloatDST_3D.this.dstSlices.inverse(
//									FloatDST_3D.this.t, this.val$startt,
//									this.val$scale);
//							FloatDST_3D.this.dstSlices.inverse(
//									FloatDST_3D.this.t, this.val$startt
//											+ FloatDST_3D.this.slices,
//									this.val$scale);
//							for (i1 = 0; i1 < FloatDST_3D.this.slices; ++i1) {
//								j = i1 * FloatDST_3D.this.sliceStride + i;
//								this.val$a[j] = FloatDST_3D
//										.access$800(FloatDST_3D.this)[(this.val$startt + i1)];
//								this.val$a[(j + 1)] = FloatDST_3D
//										.access$800(FloatDST_3D.this)[(this.val$startt
//										+ FloatDST_3D
//												.access$600(FloatDST_3D.this) + i1)];
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
//	private void ddxt3db_subth(int paramInt, float[][][] paramArrayOfFloat,
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
//					l, i, i1, paramArrayOfFloat, paramBoolean) {
//				public void run() {
//					int j;
//					int k;
//					int l;
//					int i;
//					if (this.val$isgn == -1) {
//						if (FloatDST_3D.this.columns > 2) {
//							j = this.val$n0;
//							while (j < FloatDST_3D.this.rows) {
//								for (k = 0; k < FloatDST_3D.this.columns; k += 4) {
//									for (l = 0; l < FloatDST_3D.this.slices; ++l) {
//										i = this.val$startt
//												+ FloatDST_3D.this.slices + l;
//										FloatDST_3D.this.t[(this.val$startt + l)] = this.val$a[l][j][k];
//										FloatDST_3D.this.t[i] = this.val$a[l][j][(k + 1)];
//										FloatDST_3D.this.t[(i + FloatDST_3D.this.slices)] = this.val$a[l][j][(k + 2)];
//										FloatDST_3D.this.t[(i + 2 * FloatDST_3D.this.slices)] = this.val$a[l][j][(k + 3)];
//									}
//									FloatDST_3D.this.dstSlices.forward(
//											FloatDST_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatDST_3D.this.dstSlices.forward(
//											FloatDST_3D.this.t, this.val$startt
//													+ FloatDST_3D.this.slices,
//											this.val$scale);
//									FloatDST_3D.this.dstSlices.forward(
//											FloatDST_3D.this.t, this.val$startt
//													+ 2
//													* FloatDST_3D.this.slices,
//											this.val$scale);
//									FloatDST_3D.this.dstSlices.forward(
//											FloatDST_3D.this.t, this.val$startt
//													+ 3
//													* FloatDST_3D.this.slices,
//											this.val$scale);
//									for (l = 0; l < FloatDST_3D.this.slices; ++l) {
//										i = this.val$startt
//												+ FloatDST_3D.this.slices + l;
//										this.val$a[l][j][k] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(this.val$startt + l)];
//										this.val$a[l][j][(k + 1)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[i];
//										this.val$a[l][j][(k + 2)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(i + FloatDST_3D
//												.access$600(FloatDST_3D.this))];
//										this.val$a[l][j][(k + 3)] = FloatDST_3D
//												.access$800(FloatDST_3D.this)[(i + 2 * FloatDST_3D
//												.access$600(FloatDST_3D.this))];
//									}
//								}
//								j += this.val$nthreads;
//							}
//						} else {
//							if (FloatDST_3D.this.columns != 2)
//								return;
//							j = this.val$n0;
//							while (j < FloatDST_3D.this.rows) {
//								for (k = 0; k < FloatDST_3D.this.slices; ++k) {
//									FloatDST_3D.this.t[(this.val$startt + k)] = this.val$a[k][j][0];
//									FloatDST_3D.this.t[(this.val$startt
//											+ FloatDST_3D.this.slices + k)] = this.val$a[k][j][1];
//								}
//								FloatDST_3D.this.dstSlices.forward(
//										FloatDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDST_3D.this.dstSlices.forward(
//										FloatDST_3D.this.t, this.val$startt
//												+ FloatDST_3D.this.slices,
//										this.val$scale);
//								for (k = 0; k < FloatDST_3D.this.slices; ++k) {
//									this.val$a[k][j][0] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt + k)];
//									this.val$a[k][j][1] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt
//											+ FloatDST_3D
//													.access$600(FloatDST_3D.this) + k)];
//								}
//								j += this.val$nthreads;
//							}
//						}
//					} else if (FloatDST_3D.this.columns > 2) {
//						j = this.val$n0;
//						while (j < FloatDST_3D.this.rows) {
//							for (k = 0; k < FloatDST_3D.this.columns; k += 4) {
//								for (l = 0; l < FloatDST_3D.this.slices; ++l) {
//									i = this.val$startt
//											+ FloatDST_3D.this.slices + l;
//									FloatDST_3D.this.t[(this.val$startt + l)] = this.val$a[l][j][k];
//									FloatDST_3D.this.t[i] = this.val$a[l][j][(k + 1)];
//									FloatDST_3D.this.t[(i + FloatDST_3D.this.slices)] = this.val$a[l][j][(k + 2)];
//									FloatDST_3D.this.t[(i + 2 * FloatDST_3D.this.slices)] = this.val$a[l][j][(k + 3)];
//								}
//								FloatDST_3D.this.dstSlices.inverse(
//										FloatDST_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDST_3D.this.dstSlices.inverse(
//										FloatDST_3D.this.t, this.val$startt
//												+ FloatDST_3D.this.slices,
//										this.val$scale);
//								FloatDST_3D.this.dstSlices.inverse(
//										FloatDST_3D.this.t, this.val$startt + 2
//												* FloatDST_3D.this.slices,
//										this.val$scale);
//								FloatDST_3D.this.dstSlices.inverse(
//										FloatDST_3D.this.t, this.val$startt + 3
//												* FloatDST_3D.this.slices,
//										this.val$scale);
//								for (l = 0; l < FloatDST_3D.this.slices; ++l) {
//									i = this.val$startt
//											+ FloatDST_3D.this.slices + l;
//									this.val$a[l][j][k] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(this.val$startt + l)];
//									this.val$a[l][j][(k + 1)] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[i];
//									this.val$a[l][j][(k + 2)] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(i + FloatDST_3D
//											.access$600(FloatDST_3D.this))];
//									this.val$a[l][j][(k + 3)] = FloatDST_3D
//											.access$800(FloatDST_3D.this)[(i + 2 * FloatDST_3D
//											.access$600(FloatDST_3D.this))];
//								}
//							}
//							j += this.val$nthreads;
//						}
//					} else {
//						if (FloatDST_3D.this.columns != 2)
//							return;
//						j = this.val$n0;
//						while (j < FloatDST_3D.this.rows) {
//							for (k = 0; k < FloatDST_3D.this.slices; ++k) {
//								FloatDST_3D.this.t[(this.val$startt + k)] = this.val$a[k][j][0];
//								FloatDST_3D.this.t[(this.val$startt
//										+ FloatDST_3D.this.slices + k)] = this.val$a[k][j][1];
//							}
//							FloatDST_3D.this.dstSlices.inverse(
//									FloatDST_3D.this.t, this.val$startt,
//									this.val$scale);
//							FloatDST_3D.this.dstSlices.inverse(
//									FloatDST_3D.this.t, this.val$startt
//											+ FloatDST_3D.this.slices,
//									this.val$scale);
//							for (k = 0; k < FloatDST_3D.this.slices; ++k) {
//								this.val$a[k][j][0] = FloatDST_3D
//										.access$800(FloatDST_3D.this)[(this.val$startt + k)];
//								this.val$a[k][j][1] = FloatDST_3D
//										.access$800(FloatDST_3D.this)[(this.val$startt
//										+ FloatDST_3D
//												.access$600(FloatDST_3D.this) + k)];
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