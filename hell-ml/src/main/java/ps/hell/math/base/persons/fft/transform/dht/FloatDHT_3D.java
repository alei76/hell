package ps.hell.math.base.persons.fft.transform.dht;//package ps.landerbuluse.ml.math.fft.transform.dht;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class FloatDHT_3D {
//	private int slices;
//	private int rows;
//	private int columns;
//	private int sliceStride;
//	private int rowStride;
//	private float[] t;
//	private FloatDHT_1D dhtSlices;
//	private FloatDHT_1D dhtRows;
//	private FloatDHT_1D dhtColumns;
//	private int oldNthreads;
//	private int nt;
//	private boolean isPowerOfTwo = false;
//	private boolean useThreads = false;
//
//	public FloatDHT_3D(int paramInt1, int paramInt2, int paramInt3) {
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
//		this.dhtSlices = new FloatDHT_1D(paramInt1);
//		if (paramInt1 == paramInt2)
//			this.dhtRows = this.dhtSlices;
//		else
//			this.dhtRows = new FloatDHT_1D(paramInt2);
//		if (paramInt1 == paramInt3)
//			this.dhtColumns = this.dhtSlices;
//		else if (paramInt2 == paramInt3)
//			this.dhtColumns = this.dhtRows;
//		else
//			this.dhtColumns = new FloatDHT_1D(paramInt3);
//	}
//
//	public void forward(float[] paramArrayOfFloat) {
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
//				ddxt3da_subth(-1, paramArrayOfFloat, true);
//				ddxt3db_subth(-1, paramArrayOfFloat, true);
//			} else {
//				ddxt3da_sub(-1, paramArrayOfFloat, true);
//				ddxt3db_sub(-1, paramArrayOfFloat, true);
//			}
//			yTransform(paramArrayOfFloat);
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
//							i2, paramArrayOfFloat) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * FloatDHT_3D.this.sliceStride;
//								for (int k = 0; k < FloatDHT_3D.this.rows; ++k)
//									FloatDHT_3D.this.dhtColumns.forward(
//											this.val$a,
//											j + k * FloatDHT_3D.this.rowStride);
//							}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfFloat) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDHT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * FloatDHT_3D.this.sliceStride;
//								for (int k = 0; k < FloatDHT_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < FloatDHT_3D.this.rows; ++l) {
//										i1 = j + l * FloatDHT_3D.this.rowStride
//												+ k;
//										arrayOfFloat[l] = this.val$a[i1];
//									}
//									FloatDHT_3D.this.dhtRows
//											.forward(arrayOfFloat);
//									for (l = 0; l < FloatDHT_3D.this.rows; ++l) {
//										i1 = j + l * FloatDHT_3D.this.rowStride
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
//					if (l == i - 1)
//						i2 = this.rows;
//					else
//						i2 = i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfFloat) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDHT_3D.this.slices];
//							for (int i = this.val$startRow; i < this.val$stopRow; ++i) {
//								int j = i * FloatDHT_3D.this.rowStride;
//								for (int k = 0; k < FloatDHT_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < FloatDHT_3D.this.slices; ++l) {
//										i1 = l * FloatDHT_3D.this.sliceStride
//												+ j + k;
//										arrayOfFloat[l] = this.val$a[i1];
//									}
//									FloatDHT_3D.this.dhtSlices
//											.forward(arrayOfFloat);
//									for (l = 0; l < FloatDHT_3D.this.slices; ++l) {
//										i1 = l * FloatDHT_3D.this.sliceStride
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
//						this.dhtColumns.forward(paramArrayOfFloat, k + l
//								* this.rowStride);
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
//						this.dhtRows.forward(arrayOfFloat);
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
//						this.dhtSlices.forward(arrayOfFloat);
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							paramArrayOfFloat[i3] = arrayOfFloat[i2];
//						}
//					}
//				}
//			}
//			yTransform(paramArrayOfFloat);
//		}
//	}
//
//	public void forward(float[][][] paramArrayOfFloat) {
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
//				ddxt3da_subth(-1, paramArrayOfFloat, true);
//				ddxt3db_subth(-1, paramArrayOfFloat, true);
//			} else {
//				ddxt3da_sub(-1, paramArrayOfFloat, true);
//				ddxt3db_sub(-1, paramArrayOfFloat, true);
//			}
//			yTransform(paramArrayOfFloat);
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
//							i2, paramArrayOfFloat) {
//						public void run() {
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < FloatDHT_3D.this.rows; ++j)
//									FloatDHT_3D.this.dhtColumns
//											.forward(this.val$a[i][j]);
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//				for (l = 0; l < i; ++l) {
//					i1 = l * k;
//					i2 = (l == i - 1) ? this.slices : i1 + k;
//					arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1,
//							i2, paramArrayOfFloat) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDHT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < FloatDHT_3D.this.columns; ++j) {
//									for (int k = 0; k < FloatDHT_3D.this.rows; ++k)
//										arrayOfFloat[k] = this.val$a[i][k][j];
//									FloatDHT_3D.this.dhtRows
//											.forward(arrayOfFloat);
//									for (k = 0; k < FloatDHT_3D.this.rows; ++k)
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
//							i2, paramArrayOfFloat) {
//						public void run() {
//							float[] arrayOfFloat = new float[FloatDHT_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								for (int j = 0; j < FloatDHT_3D.this.columns; ++j) {
//									for (int k = 0; k < FloatDHT_3D.this.slices; ++k)
//										arrayOfFloat[k] = this.val$a[k][i][j];
//									FloatDHT_3D.this.dhtSlices
//											.forward(arrayOfFloat);
//									for (k = 0; k < FloatDHT_3D.this.slices; ++k)
//										this.val$a[k][i][j] = arrayOfFloat[k];
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int j = 0; j < this.slices; ++j)
//					for (k = 0; k < this.rows; ++k)
//						this.dhtColumns.forward(paramArrayOfFloat[j][k]);
//				float[] arrayOfFloat = new float[this.rows];
//				for (k = 0; k < this.slices; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.rows; ++i1)
//							arrayOfFloat[i1] = paramArrayOfFloat[k][i1][l];
//						this.dhtRows.forward(arrayOfFloat);
//						for (i1 = 0; i1 < this.rows; ++i1)
//							paramArrayOfFloat[k][i1][l] = arrayOfFloat[i1];
//					}
//				arrayOfFloat = new float[this.slices];
//				for (k = 0; k < this.rows; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.slices; ++i1)
//							arrayOfFloat[i1] = paramArrayOfFloat[i1][k][l];
//						this.dhtSlices.forward(arrayOfFloat);
//						for (i1 = 0; i1 < this.slices; ++i1)
//							paramArrayOfFloat[i1][k][l] = arrayOfFloat[i1];
//					}
//			}
//			yTransform(paramArrayOfFloat);
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
//			yTransform(paramArrayOfFloat);
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
//								int j = i * FloatDHT_3D.this.sliceStride;
//								for (int k = 0; k < FloatDHT_3D.this.rows; ++k)
//									FloatDHT_3D.this.dhtColumns.inverse(
//											this.val$a,
//											j + k * FloatDHT_3D.this.rowStride,
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
//							float[] arrayOfFloat = new float[FloatDHT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i) {
//								int j = i * FloatDHT_3D.this.sliceStride;
//								for (int k = 0; k < FloatDHT_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < FloatDHT_3D.this.rows; ++l) {
//										i1 = j + l * FloatDHT_3D.this.rowStride
//												+ k;
//										arrayOfFloat[l] = this.val$a[i1];
//									}
//									FloatDHT_3D.this.dhtRows.inverse(
//											arrayOfFloat, this.val$scale);
//									for (l = 0; l < FloatDHT_3D.this.rows; ++l) {
//										i1 = j + l * FloatDHT_3D.this.rowStride
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
//							float[] arrayOfFloat = new float[FloatDHT_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i) {
//								int j = i * FloatDHT_3D.this.rowStride;
//								for (int k = 0; k < FloatDHT_3D.this.columns; ++k) {
//									int i1;
//									for (int l = 0; l < FloatDHT_3D.this.slices; ++l) {
//										i1 = l * FloatDHT_3D.this.sliceStride
//												+ j + k;
//										arrayOfFloat[l] = this.val$a[i1];
//									}
//									FloatDHT_3D.this.dhtSlices.inverse(
//											arrayOfFloat, this.val$scale);
//									for (l = 0; l < FloatDHT_3D.this.slices; ++l) {
//										i1 = l * FloatDHT_3D.this.sliceStride
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
//						this.dhtColumns.inverse(paramArrayOfFloat, k + l
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
//						this.dhtRows.inverse(arrayOfFloat, paramBoolean);
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
//						this.dhtSlices.inverse(arrayOfFloat, paramBoolean);
//						for (i2 = 0; i2 < this.slices; ++i2) {
//							i3 = i2 * this.sliceStride + l + i1;
//							paramArrayOfFloat[i3] = arrayOfFloat[i2];
//						}
//					}
//				}
//			}
//			yTransform(paramArrayOfFloat);
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
//			yTransform(paramArrayOfFloat);
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
//								for (int j = 0; j < FloatDHT_3D.this.rows; ++j)
//									FloatDHT_3D.this.dhtColumns.inverse(
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
//							float[] arrayOfFloat = new float[FloatDHT_3D.this.rows];
//							for (int i = this.val$firstSlice; i < this.val$lastSlice; ++i)
//								for (int j = 0; j < FloatDHT_3D.this.columns; ++j) {
//									for (int k = 0; k < FloatDHT_3D.this.rows; ++k)
//										arrayOfFloat[k] = this.val$a[i][k][j];
//									FloatDHT_3D.this.dhtRows.inverse(
//											arrayOfFloat, this.val$scale);
//									for (k = 0; k < FloatDHT_3D.this.rows; ++k)
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
//							float[] arrayOfFloat = new float[FloatDHT_3D.this.slices];
//							for (int i = this.val$firstRow; i < this.val$lastRow; ++i)
//								for (int j = 0; j < FloatDHT_3D.this.columns; ++j) {
//									for (int k = 0; k < FloatDHT_3D.this.slices; ++k)
//										arrayOfFloat[k] = this.val$a[k][i][j];
//									FloatDHT_3D.this.dhtSlices.inverse(
//											arrayOfFloat, this.val$scale);
//									for (k = 0; k < FloatDHT_3D.this.slices; ++k)
//										this.val$a[k][i][j] = arrayOfFloat[k];
//								}
//						}
//					});
//				}
//				ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			} else {
//				for (int j = 0; j < this.slices; ++j)
//					for (k = 0; k < this.rows; ++k)
//						this.dhtColumns.inverse(paramArrayOfFloat[j][k],
//								paramBoolean);
//				float[] arrayOfFloat = new float[this.rows];
//				for (k = 0; k < this.slices; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.rows; ++i1)
//							arrayOfFloat[i1] = paramArrayOfFloat[k][i1][l];
//						this.dhtRows.inverse(arrayOfFloat, paramBoolean);
//						for (i1 = 0; i1 < this.rows; ++i1)
//							paramArrayOfFloat[k][i1][l] = arrayOfFloat[i1];
//					}
//				arrayOfFloat = new float[this.slices];
//				for (k = 0; k < this.rows; ++k)
//					for (l = 0; l < this.columns; ++l) {
//						for (i1 = 0; i1 < this.slices; ++i1)
//							arrayOfFloat[i1] = paramArrayOfFloat[i1][k][l];
//						this.dhtSlices.inverse(arrayOfFloat, paramBoolean);
//						for (i1 = 0; i1 < this.slices; ++i1)
//							paramArrayOfFloat[i1][k][l] = arrayOfFloat[i1];
//					}
//			}
//			yTransform(paramArrayOfFloat);
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
//					this.dhtColumns.forward(paramArrayOfFloat, i + i1
//							* this.rowStride);
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
//						this.dhtRows.forward(this.t, 0);
//						this.dhtRows.forward(this.t, this.rows);
//						this.dhtRows.forward(this.t, 2 * this.rows);
//						this.dhtRows.forward(this.t, 3 * this.rows);
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
//					this.dhtRows.forward(this.t, 0);
//					this.dhtRows.forward(this.t, this.rows);
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
//					this.dhtColumns.inverse(paramArrayOfFloat, i + i1
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
//						this.dhtRows.inverse(this.t, 0, paramBoolean);
//						this.dhtRows.inverse(this.t, this.rows, paramBoolean);
//						this.dhtRows.inverse(this.t, 2 * this.rows,
//								paramBoolean);
//						this.dhtRows.inverse(this.t, 3 * this.rows,
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
//					this.dhtRows.inverse(this.t, 0, paramBoolean);
//					this.dhtRows.inverse(this.t, this.rows, paramBoolean);
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
//					this.dhtColumns.forward(paramArrayOfFloat[j][k]);
//				if (this.columns > 2) {
//					for (k = 0; k < this.columns; k += 4) {
//						for (l = 0; l < this.rows; ++l) {
//							i = this.rows + l;
//							this.t[l] = paramArrayOfFloat[j][l][k];
//							this.t[i] = paramArrayOfFloat[j][l][(k + 1)];
//							this.t[(i + this.rows)] = paramArrayOfFloat[j][l][(k + 2)];
//							this.t[(i + 2 * this.rows)] = paramArrayOfFloat[j][l][(k + 3)];
//						}
//						this.dhtRows.forward(this.t, 0);
//						this.dhtRows.forward(this.t, this.rows);
//						this.dhtRows.forward(this.t, 2 * this.rows);
//						this.dhtRows.forward(this.t, 3 * this.rows);
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
//					this.dhtRows.forward(this.t, 0);
//					this.dhtRows.forward(this.t, this.rows);
//					for (k = 0; k < this.rows; ++k) {
//						paramArrayOfFloat[j][k][0] = this.t[k];
//						paramArrayOfFloat[j][k][1] = this.t[(this.rows + k)];
//					}
//				}
//			}
//		else
//			for (j = 0; j < this.slices; ++j) {
//				for (k = 0; k < this.rows; ++k)
//					this.dhtColumns.inverse(paramArrayOfFloat[j][k],
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
//						this.dhtRows.inverse(this.t, 0, paramBoolean);
//						this.dhtRows.inverse(this.t, this.rows, paramBoolean);
//						this.dhtRows.inverse(this.t, 2 * this.rows,
//								paramBoolean);
//						this.dhtRows.inverse(this.t, 3 * this.rows,
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
//					this.dhtRows.inverse(this.t, 0, paramBoolean);
//					this.dhtRows.inverse(this.t, this.rows, paramBoolean);
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
//						this.dhtSlices.forward(this.t, 0);
//						this.dhtSlices.forward(this.t, this.slices);
//						this.dhtSlices.forward(this.t, 2 * this.slices);
//						this.dhtSlices.forward(this.t, 3 * this.slices);
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
//					this.dhtSlices.forward(this.t, 0);
//					this.dhtSlices.forward(this.t, this.slices);
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
//					this.dhtSlices.inverse(this.t, 0, paramBoolean);
//					this.dhtSlices.inverse(this.t, this.slices, paramBoolean);
//					this.dhtSlices.inverse(this.t, 2 * this.slices,
//							paramBoolean);
//					this.dhtSlices.inverse(this.t, 3 * this.slices,
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
//				this.dhtSlices.inverse(this.t, 0, paramBoolean);
//				this.dhtSlices.inverse(this.t, this.slices, paramBoolean);
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
//						this.dhtSlices.forward(this.t, 0);
//						this.dhtSlices.forward(this.t, this.slices);
//						this.dhtSlices.forward(this.t, 2 * this.slices);
//						this.dhtSlices.forward(this.t, 3 * this.slices);
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
//					this.dhtSlices.forward(this.t, 0);
//					this.dhtSlices.forward(this.t, this.slices);
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
//					this.dhtSlices.inverse(this.t, 0, paramBoolean);
//					this.dhtSlices.inverse(this.t, this.slices, paramBoolean);
//					this.dhtSlices.inverse(this.t, 2 * this.slices,
//							paramBoolean);
//					this.dhtSlices.inverse(this.t, 3 * this.slices,
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
//				this.dhtSlices.inverse(this.t, 0, paramBoolean);
//				this.dhtSlices.inverse(this.t, this.slices, paramBoolean);
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
//					l, i, paramArrayOfFloat, i1, paramBoolean) {
//				public void run() {
//					int l;
//					int i;
//					int i1;
//					int i2;
//					int j;
//					int k;
//					if (this.val$isgn == -1) {
//						l = this.val$n0;
//						while (l < FloatDHT_3D.this.slices) {
//							i = l * FloatDHT_3D.this.sliceStride;
//							for (i1 = 0; i1 < FloatDHT_3D.this.rows; ++i1)
//								FloatDHT_3D.this.dhtColumns.forward(this.val$a,
//										i + i1 * FloatDHT_3D.this.rowStride);
//							if (FloatDHT_3D.this.columns > 2) {
//								for (i1 = 0; i1 < FloatDHT_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < FloatDHT_3D.this.rows; ++i2) {
//										j = i + i2 * FloatDHT_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ FloatDHT_3D.this.rows + i2;
//										FloatDHT_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										FloatDHT_3D.this.t[k] = this.val$a[(j + 1)];
//										FloatDHT_3D.this.t[(k + FloatDHT_3D.this.rows)] = this.val$a[(j + 2)];
//										FloatDHT_3D.this.t[(k + 2 * FloatDHT_3D.this.rows)] = this.val$a[(j + 3)];
//									}
//									FloatDHT_3D.this.dhtRows
//											.forward(FloatDHT_3D.this.t,
//													this.val$startt);
//									FloatDHT_3D.this.dhtRows.forward(
//											FloatDHT_3D.this.t, this.val$startt
//													+ FloatDHT_3D.this.rows);
//									FloatDHT_3D.this.dhtRows
//											.forward(
//													FloatDHT_3D.this.t,
//													this.val$startt
//															+ 2
//															* FloatDHT_3D.this.rows);
//									FloatDHT_3D.this.dhtRows
//											.forward(
//													FloatDHT_3D.this.t,
//													this.val$startt
//															+ 3
//															* FloatDHT_3D.this.rows);
//									for (i2 = 0; i2 < FloatDHT_3D.this.rows; ++i2) {
//										j = i + i2 * FloatDHT_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ FloatDHT_3D.this.rows + i2;
//										this.val$a[j] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[k];
//										this.val$a[(j + 2)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(k + FloatDHT_3D
//												.access$100(FloatDHT_3D.this))];
//										this.val$a[(j + 3)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(k + 2 * FloatDHT_3D
//												.access$100(FloatDHT_3D.this))];
//									}
//								}
//							} else if (FloatDHT_3D.this.columns == 2) {
//								for (i1 = 0; i1 < FloatDHT_3D.this.rows; ++i1) {
//									j = i + i1 * FloatDHT_3D.this.rowStride;
//									FloatDHT_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									FloatDHT_3D.this.t[(this.val$startt
//											+ FloatDHT_3D.this.rows + i1)] = this.val$a[(j + 1)];
//								}
//								FloatDHT_3D.this.dhtRows.forward(
//										FloatDHT_3D.this.t, this.val$startt);
//								FloatDHT_3D.this.dhtRows.forward(
//										FloatDHT_3D.this.t, this.val$startt
//												+ FloatDHT_3D.this.rows);
//								for (i1 = 0; i1 < FloatDHT_3D.this.rows; ++i1) {
//									j = i + i1 * FloatDHT_3D.this.rowStride;
//									this.val$a[j] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt
//											+ FloatDHT_3D
//													.access$100(FloatDHT_3D.this) + i1)];
//								}
//							}
//							l += this.val$nthreads;
//						}
//					} else {
//						l = this.val$n0;
//						while (l < FloatDHT_3D.this.slices) {
//							i = l * FloatDHT_3D.this.sliceStride;
//							for (i1 = 0; i1 < FloatDHT_3D.this.rows; ++i1)
//								FloatDHT_3D.this.dhtColumns.inverse(this.val$a,
//										i + i1 * FloatDHT_3D.this.rowStride,
//										this.val$scale);
//							if (FloatDHT_3D.this.columns > 2) {
//								for (i1 = 0; i1 < FloatDHT_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < FloatDHT_3D.this.rows; ++i2) {
//										j = i + i2 * FloatDHT_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ FloatDHT_3D.this.rows + i2;
//										FloatDHT_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										FloatDHT_3D.this.t[k] = this.val$a[(j + 1)];
//										FloatDHT_3D.this.t[(k + FloatDHT_3D.this.rows)] = this.val$a[(j + 2)];
//										FloatDHT_3D.this.t[(k + 2 * FloatDHT_3D.this.rows)] = this.val$a[(j + 3)];
//									}
//									FloatDHT_3D.this.dhtRows.inverse(
//											FloatDHT_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatDHT_3D.this.dhtRows.inverse(
//											FloatDHT_3D.this.t, this.val$startt
//													+ FloatDHT_3D.this.rows,
//											this.val$scale);
//									FloatDHT_3D.this.dhtRows.inverse(
//											FloatDHT_3D.this.t,
//											this.val$startt + 2
//													* FloatDHT_3D.this.rows,
//											this.val$scale);
//									FloatDHT_3D.this.dhtRows.inverse(
//											FloatDHT_3D.this.t,
//											this.val$startt + 3
//													* FloatDHT_3D.this.rows,
//											this.val$scale);
//									for (i2 = 0; i2 < FloatDHT_3D.this.rows; ++i2) {
//										j = i + i2 * FloatDHT_3D.this.rowStride
//												+ i1;
//										k = this.val$startt
//												+ FloatDHT_3D.this.rows + i2;
//										this.val$a[j] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[k];
//										this.val$a[(j + 2)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(k + FloatDHT_3D
//												.access$100(FloatDHT_3D.this))];
//										this.val$a[(j + 3)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(k + 2 * FloatDHT_3D
//												.access$100(FloatDHT_3D.this))];
//									}
//								}
//							} else if (FloatDHT_3D.this.columns == 2) {
//								for (i1 = 0; i1 < FloatDHT_3D.this.rows; ++i1) {
//									j = i + i1 * FloatDHT_3D.this.rowStride;
//									FloatDHT_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									FloatDHT_3D.this.t[(this.val$startt
//											+ FloatDHT_3D.this.rows + i1)] = this.val$a[(j + 1)];
//								}
//								FloatDHT_3D.this.dhtRows.inverse(
//										FloatDHT_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDHT_3D.this.dhtRows.inverse(
//										FloatDHT_3D.this.t, this.val$startt
//												+ FloatDHT_3D.this.rows,
//										this.val$scale);
//								for (i1 = 0; i1 < FloatDHT_3D.this.rows; ++i1) {
//									j = i + i1 * FloatDHT_3D.this.rowStride;
//									this.val$a[j] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt
//											+ FloatDHT_3D
//													.access$100(FloatDHT_3D.this) + i1)];
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
//					l, i, paramArrayOfFloat, i1, paramBoolean) {
//				public void run() {
//					int j;
//					int k;
//					int l;
//					int i;
//					if (this.val$isgn == -1) {
//						j = this.val$n0;
//						while (j < FloatDHT_3D.this.slices) {
//							for (k = 0; k < FloatDHT_3D.this.rows; ++k)
//								FloatDHT_3D.this.dhtColumns
//										.forward(this.val$a[j][k]);
//							if (FloatDHT_3D.this.columns > 2) {
//								for (k = 0; k < FloatDHT_3D.this.columns; k += 4) {
//									for (l = 0; l < FloatDHT_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ FloatDHT_3D.this.rows + l;
//										FloatDHT_3D.this.t[(this.val$startt + l)] = this.val$a[j][l][k];
//										FloatDHT_3D.this.t[i] = this.val$a[j][l][(k + 1)];
//										FloatDHT_3D.this.t[(i + FloatDHT_3D.this.rows)] = this.val$a[j][l][(k + 2)];
//										FloatDHT_3D.this.t[(i + 2 * FloatDHT_3D.this.rows)] = this.val$a[j][l][(k + 3)];
//									}
//									FloatDHT_3D.this.dhtRows
//											.forward(FloatDHT_3D.this.t,
//													this.val$startt);
//									FloatDHT_3D.this.dhtRows.forward(
//											FloatDHT_3D.this.t, this.val$startt
//													+ FloatDHT_3D.this.rows);
//									FloatDHT_3D.this.dhtRows
//											.forward(
//													FloatDHT_3D.this.t,
//													this.val$startt
//															+ 2
//															* FloatDHT_3D.this.rows);
//									FloatDHT_3D.this.dhtRows
//											.forward(
//													FloatDHT_3D.this.t,
//													this.val$startt
//															+ 3
//															* FloatDHT_3D.this.rows);
//									for (l = 0; l < FloatDHT_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ FloatDHT_3D.this.rows + l;
//										this.val$a[j][l][k] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(this.val$startt + l)];
//										this.val$a[j][l][(k + 1)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[i];
//										this.val$a[j][l][(k + 2)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(i + FloatDHT_3D
//												.access$100(FloatDHT_3D.this))];
//										this.val$a[j][l][(k + 3)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(i + 2 * FloatDHT_3D
//												.access$100(FloatDHT_3D.this))];
//									}
//								}
//							} else if (FloatDHT_3D.this.columns == 2) {
//								for (k = 0; k < FloatDHT_3D.this.rows; ++k) {
//									FloatDHT_3D.this.t[(this.val$startt + k)] = this.val$a[j][k][0];
//									FloatDHT_3D.this.t[(this.val$startt
//											+ FloatDHT_3D.this.rows + k)] = this.val$a[j][k][1];
//								}
//								FloatDHT_3D.this.dhtRows.forward(
//										FloatDHT_3D.this.t, this.val$startt);
//								FloatDHT_3D.this.dhtRows.forward(
//										FloatDHT_3D.this.t, this.val$startt
//												+ FloatDHT_3D.this.rows);
//								for (k = 0; k < FloatDHT_3D.this.rows; ++k) {
//									this.val$a[j][k][0] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt + k)];
//									this.val$a[j][k][1] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt
//											+ FloatDHT_3D
//													.access$100(FloatDHT_3D.this) + k)];
//								}
//							}
//							j += this.val$nthreads;
//						}
//					} else {
//						j = this.val$n0;
//						while (j < FloatDHT_3D.this.slices) {
//							for (k = 0; k < FloatDHT_3D.this.rows; ++k)
//								FloatDHT_3D.this.dhtColumns.inverse(
//										this.val$a[j][k], this.val$scale);
//							if (FloatDHT_3D.this.columns > 2) {
//								for (k = 0; k < FloatDHT_3D.this.columns; k += 4) {
//									for (l = 0; l < FloatDHT_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ FloatDHT_3D.this.rows + l;
//										FloatDHT_3D.this.t[(this.val$startt + l)] = this.val$a[j][l][k];
//										FloatDHT_3D.this.t[i] = this.val$a[j][l][(k + 1)];
//										FloatDHT_3D.this.t[(i + FloatDHT_3D.this.rows)] = this.val$a[j][l][(k + 2)];
//										FloatDHT_3D.this.t[(i + 2 * FloatDHT_3D.this.rows)] = this.val$a[j][l][(k + 3)];
//									}
//									FloatDHT_3D.this.dhtRows.inverse(
//											FloatDHT_3D.this.t,
//											this.val$startt, this.val$scale);
//									FloatDHT_3D.this.dhtRows.inverse(
//											FloatDHT_3D.this.t, this.val$startt
//													+ FloatDHT_3D.this.rows,
//											this.val$scale);
//									FloatDHT_3D.this.dhtRows.inverse(
//											FloatDHT_3D.this.t,
//											this.val$startt + 2
//													* FloatDHT_3D.this.rows,
//											this.val$scale);
//									FloatDHT_3D.this.dhtRows.inverse(
//											FloatDHT_3D.this.t,
//											this.val$startt + 3
//													* FloatDHT_3D.this.rows,
//											this.val$scale);
//									for (l = 0; l < FloatDHT_3D.this.rows; ++l) {
//										i = this.val$startt
//												+ FloatDHT_3D.this.rows + l;
//										this.val$a[j][l][k] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(this.val$startt + l)];
//										this.val$a[j][l][(k + 1)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[i];
//										this.val$a[j][l][(k + 2)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(i + FloatDHT_3D
//												.access$100(FloatDHT_3D.this))];
//										this.val$a[j][l][(k + 3)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(i + 2 * FloatDHT_3D
//												.access$100(FloatDHT_3D.this))];
//									}
//								}
//							} else if (FloatDHT_3D.this.columns == 2) {
//								for (k = 0; k < FloatDHT_3D.this.rows; ++k) {
//									FloatDHT_3D.this.t[(this.val$startt + k)] = this.val$a[j][k][0];
//									FloatDHT_3D.this.t[(this.val$startt
//											+ FloatDHT_3D.this.rows + k)] = this.val$a[j][k][1];
//								}
//								FloatDHT_3D.this.dhtRows.inverse(
//										FloatDHT_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDHT_3D.this.dhtRows.inverse(
//										FloatDHT_3D.this.t, this.val$startt
//												+ FloatDHT_3D.this.rows,
//										this.val$scale);
//								for (k = 0; k < FloatDHT_3D.this.rows; ++k) {
//									this.val$a[j][k][0] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt + k)];
//									this.val$a[j][k][1] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt
//											+ FloatDHT_3D
//													.access$100(FloatDHT_3D.this) + k)];
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
//						if (FloatDHT_3D.this.columns > 2) {
//							l = this.val$n0;
//							while (l < FloatDHT_3D.this.rows) {
//								i = l * FloatDHT_3D.this.rowStride;
//								for (i1 = 0; i1 < FloatDHT_3D.this.columns; i1 += 4) {
//									for (i2 = 0; i2 < FloatDHT_3D.this.slices; ++i2) {
//										j = i2 * FloatDHT_3D.this.sliceStride
//												+ i + i1;
//										k = this.val$startt
//												+ FloatDHT_3D.this.slices + i2;
//										FloatDHT_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//										FloatDHT_3D.this.t[k] = this.val$a[(j + 1)];
//										FloatDHT_3D.this.t[(k + FloatDHT_3D.this.slices)] = this.val$a[(j + 2)];
//										FloatDHT_3D.this.t[(k + 2 * FloatDHT_3D.this.slices)] = this.val$a[(j + 3)];
//									}
//									FloatDHT_3D.this.dhtSlices
//											.forward(FloatDHT_3D.this.t,
//													this.val$startt);
//									FloatDHT_3D.this.dhtSlices.forward(
//											FloatDHT_3D.this.t, this.val$startt
//													+ FloatDHT_3D.this.slices);
//									FloatDHT_3D.this.dhtSlices.forward(
//											FloatDHT_3D.this.t, this.val$startt
//													+ 2
//													* FloatDHT_3D.this.slices);
//									FloatDHT_3D.this.dhtSlices.forward(
//											FloatDHT_3D.this.t, this.val$startt
//													+ 3
//													* FloatDHT_3D.this.slices);
//									for (i2 = 0; i2 < FloatDHT_3D.this.slices; ++i2) {
//										j = i2 * FloatDHT_3D.this.sliceStride
//												+ i + i1;
//										k = this.val$startt
//												+ FloatDHT_3D.this.slices + i2;
//										this.val$a[j] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(this.val$startt + i2)];
//										this.val$a[(j + 1)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[k];
//										this.val$a[(j + 2)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(k + FloatDHT_3D
//												.access$600(FloatDHT_3D.this))];
//										this.val$a[(j + 3)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(k + 2 * FloatDHT_3D
//												.access$600(FloatDHT_3D.this))];
//									}
//								}
//								l += this.val$nthreads;
//							}
//						} else {
//							if (FloatDHT_3D.this.columns != 2)
//								return;
//							l = this.val$n0;
//							while (l < FloatDHT_3D.this.rows) {
//								i = l * FloatDHT_3D.this.rowStride;
//								for (i1 = 0; i1 < FloatDHT_3D.this.slices; ++i1) {
//									j = i1 * FloatDHT_3D.this.sliceStride + i;
//									FloatDHT_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//									FloatDHT_3D.this.t[(this.val$startt
//											+ FloatDHT_3D.this.slices + i1)] = this.val$a[(j + 1)];
//								}
//								FloatDHT_3D.this.dhtSlices.forward(
//										FloatDHT_3D.this.t, this.val$startt);
//								FloatDHT_3D.this.dhtSlices.forward(
//										FloatDHT_3D.this.t, this.val$startt
//												+ FloatDHT_3D.this.slices);
//								for (i1 = 0; i1 < FloatDHT_3D.this.slices; ++i1) {
//									j = i1 * FloatDHT_3D.this.sliceStride + i;
//									this.val$a[j] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt + i1)];
//									this.val$a[(j + 1)] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt
//											+ FloatDHT_3D
//													.access$600(FloatDHT_3D.this) + i1)];
//								}
//								l += this.val$nthreads;
//							}
//						}
//					} else if (FloatDHT_3D.this.columns > 2) {
//						l = this.val$n0;
//						while (l < FloatDHT_3D.this.rows) {
//							i = l * FloatDHT_3D.this.rowStride;
//							for (i1 = 0; i1 < FloatDHT_3D.this.columns; i1 += 4) {
//								for (i2 = 0; i2 < FloatDHT_3D.this.slices; ++i2) {
//									j = i2 * FloatDHT_3D.this.sliceStride + i
//											+ i1;
//									k = this.val$startt
//											+ FloatDHT_3D.this.slices + i2;
//									FloatDHT_3D.this.t[(this.val$startt + i2)] = this.val$a[j];
//									FloatDHT_3D.this.t[k] = this.val$a[(j + 1)];
//									FloatDHT_3D.this.t[(k + FloatDHT_3D.this.slices)] = this.val$a[(j + 2)];
//									FloatDHT_3D.this.t[(k + 2 * FloatDHT_3D.this.slices)] = this.val$a[(j + 3)];
//								}
//								FloatDHT_3D.this.dhtSlices.inverse(
//										FloatDHT_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDHT_3D.this.dhtSlices.inverse(
//										FloatDHT_3D.this.t, this.val$startt
//												+ FloatDHT_3D.this.slices,
//										this.val$scale);
//								FloatDHT_3D.this.dhtSlices.inverse(
//										FloatDHT_3D.this.t, this.val$startt + 2
//												* FloatDHT_3D.this.slices,
//										this.val$scale);
//								FloatDHT_3D.this.dhtSlices.inverse(
//										FloatDHT_3D.this.t, this.val$startt + 3
//												* FloatDHT_3D.this.slices,
//										this.val$scale);
//								for (i2 = 0; i2 < FloatDHT_3D.this.slices; ++i2) {
//									j = i2 * FloatDHT_3D.this.sliceStride + i
//											+ i1;
//									k = this.val$startt
//											+ FloatDHT_3D.this.slices + i2;
//									this.val$a[j] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt + i2)];
//									this.val$a[(j + 1)] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[k];
//									this.val$a[(j + 2)] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(k + FloatDHT_3D
//											.access$600(FloatDHT_3D.this))];
//									this.val$a[(j + 3)] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(k + 2 * FloatDHT_3D
//											.access$600(FloatDHT_3D.this))];
//								}
//							}
//							l += this.val$nthreads;
//						}
//					} else {
//						if (FloatDHT_3D.this.columns != 2)
//							return;
//						l = this.val$n0;
//						while (l < FloatDHT_3D.this.rows) {
//							i = l * FloatDHT_3D.this.rowStride;
//							for (i1 = 0; i1 < FloatDHT_3D.this.slices; ++i1) {
//								j = i1 * FloatDHT_3D.this.sliceStride + i;
//								FloatDHT_3D.this.t[(this.val$startt + i1)] = this.val$a[j];
//								FloatDHT_3D.this.t[(this.val$startt
//										+ FloatDHT_3D.this.slices + i1)] = this.val$a[(j + 1)];
//							}
//							FloatDHT_3D.this.dhtSlices.inverse(
//									FloatDHT_3D.this.t, this.val$startt,
//									this.val$scale);
//							FloatDHT_3D.this.dhtSlices.inverse(
//									FloatDHT_3D.this.t, this.val$startt
//											+ FloatDHT_3D.this.slices,
//									this.val$scale);
//							for (i1 = 0; i1 < FloatDHT_3D.this.slices; ++i1) {
//								j = i1 * FloatDHT_3D.this.sliceStride + i;
//								this.val$a[j] = FloatDHT_3D
//										.access$800(FloatDHT_3D.this)[(this.val$startt + i1)];
//								this.val$a[(j + 1)] = FloatDHT_3D
//										.access$800(FloatDHT_3D.this)[(this.val$startt
//										+ FloatDHT_3D
//												.access$600(FloatDHT_3D.this) + i1)];
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
//						if (FloatDHT_3D.this.columns > 2) {
//							j = this.val$n0;
//							while (j < FloatDHT_3D.this.rows) {
//								for (k = 0; k < FloatDHT_3D.this.columns; k += 4) {
//									for (l = 0; l < FloatDHT_3D.this.slices; ++l) {
//										i = this.val$startt
//												+ FloatDHT_3D.this.slices + l;
//										FloatDHT_3D.this.t[(this.val$startt + l)] = this.val$a[l][j][k];
//										FloatDHT_3D.this.t[i] = this.val$a[l][j][(k + 1)];
//										FloatDHT_3D.this.t[(i + FloatDHT_3D.this.slices)] = this.val$a[l][j][(k + 2)];
//										FloatDHT_3D.this.t[(i + 2 * FloatDHT_3D.this.slices)] = this.val$a[l][j][(k + 3)];
//									}
//									FloatDHT_3D.this.dhtSlices
//											.forward(FloatDHT_3D.this.t,
//													this.val$startt);
//									FloatDHT_3D.this.dhtSlices.forward(
//											FloatDHT_3D.this.t, this.val$startt
//													+ FloatDHT_3D.this.slices);
//									FloatDHT_3D.this.dhtSlices.forward(
//											FloatDHT_3D.this.t, this.val$startt
//													+ 2
//													* FloatDHT_3D.this.slices);
//									FloatDHT_3D.this.dhtSlices.forward(
//											FloatDHT_3D.this.t, this.val$startt
//													+ 3
//													* FloatDHT_3D.this.slices);
//									for (l = 0; l < FloatDHT_3D.this.slices; ++l) {
//										i = this.val$startt
//												+ FloatDHT_3D.this.slices + l;
//										this.val$a[l][j][k] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(this.val$startt + l)];
//										this.val$a[l][j][(k + 1)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[i];
//										this.val$a[l][j][(k + 2)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(i + FloatDHT_3D
//												.access$600(FloatDHT_3D.this))];
//										this.val$a[l][j][(k + 3)] = FloatDHT_3D
//												.access$800(FloatDHT_3D.this)[(i + 2 * FloatDHT_3D
//												.access$600(FloatDHT_3D.this))];
//									}
//								}
//								j += this.val$nthreads;
//							}
//						} else {
//							if (FloatDHT_3D.this.columns != 2)
//								return;
//							j = this.val$n0;
//							while (j < FloatDHT_3D.this.rows) {
//								for (k = 0; k < FloatDHT_3D.this.slices; ++k) {
//									FloatDHT_3D.this.t[(this.val$startt + k)] = this.val$a[k][j][0];
//									FloatDHT_3D.this.t[(this.val$startt
//											+ FloatDHT_3D.this.slices + k)] = this.val$a[k][j][1];
//								}
//								FloatDHT_3D.this.dhtSlices.forward(
//										FloatDHT_3D.this.t, this.val$startt);
//								FloatDHT_3D.this.dhtSlices.forward(
//										FloatDHT_3D.this.t, this.val$startt
//												+ FloatDHT_3D.this.slices);
//								for (k = 0; k < FloatDHT_3D.this.slices; ++k) {
//									this.val$a[k][j][0] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt + k)];
//									this.val$a[k][j][1] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt
//											+ FloatDHT_3D
//													.access$600(FloatDHT_3D.this) + k)];
//								}
//								j += this.val$nthreads;
//							}
//						}
//					} else if (FloatDHT_3D.this.columns > 2) {
//						j = this.val$n0;
//						while (j < FloatDHT_3D.this.rows) {
//							for (k = 0; k < FloatDHT_3D.this.columns; k += 4) {
//								for (l = 0; l < FloatDHT_3D.this.slices; ++l) {
//									i = this.val$startt
//											+ FloatDHT_3D.this.slices + l;
//									FloatDHT_3D.this.t[(this.val$startt + l)] = this.val$a[l][j][k];
//									FloatDHT_3D.this.t[i] = this.val$a[l][j][(k + 1)];
//									FloatDHT_3D.this.t[(i + FloatDHT_3D.this.slices)] = this.val$a[l][j][(k + 2)];
//									FloatDHT_3D.this.t[(i + 2 * FloatDHT_3D.this.slices)] = this.val$a[l][j][(k + 3)];
//								}
//								FloatDHT_3D.this.dhtSlices.inverse(
//										FloatDHT_3D.this.t, this.val$startt,
//										this.val$scale);
//								FloatDHT_3D.this.dhtSlices.inverse(
//										FloatDHT_3D.this.t, this.val$startt
//												+ FloatDHT_3D.this.slices,
//										this.val$scale);
//								FloatDHT_3D.this.dhtSlices.inverse(
//										FloatDHT_3D.this.t, this.val$startt + 2
//												* FloatDHT_3D.this.slices,
//										this.val$scale);
//								FloatDHT_3D.this.dhtSlices.inverse(
//										FloatDHT_3D.this.t, this.val$startt + 3
//												* FloatDHT_3D.this.slices,
//										this.val$scale);
//								for (l = 0; l < FloatDHT_3D.this.slices; ++l) {
//									i = this.val$startt
//											+ FloatDHT_3D.this.slices + l;
//									this.val$a[l][j][k] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(this.val$startt + l)];
//									this.val$a[l][j][(k + 1)] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[i];
//									this.val$a[l][j][(k + 2)] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(i + FloatDHT_3D
//											.access$600(FloatDHT_3D.this))];
//									this.val$a[l][j][(k + 3)] = FloatDHT_3D
//											.access$800(FloatDHT_3D.this)[(i + 2 * FloatDHT_3D
//											.access$600(FloatDHT_3D.this))];
//								}
//							}
//							j += this.val$nthreads;
//						}
//					} else {
//						if (FloatDHT_3D.this.columns != 2)
//							return;
//						j = this.val$n0;
//						while (j < FloatDHT_3D.this.rows) {
//							for (k = 0; k < FloatDHT_3D.this.slices; ++k) {
//								FloatDHT_3D.this.t[(this.val$startt + k)] = this.val$a[k][j][0];
//								FloatDHT_3D.this.t[(this.val$startt
//										+ FloatDHT_3D.this.slices + k)] = this.val$a[k][j][1];
//							}
//							FloatDHT_3D.this.dhtSlices.inverse(
//									FloatDHT_3D.this.t, this.val$startt,
//									this.val$scale);
//							FloatDHT_3D.this.dhtSlices.inverse(
//									FloatDHT_3D.this.t, this.val$startt
//											+ FloatDHT_3D.this.slices,
//									this.val$scale);
//							for (k = 0; k < FloatDHT_3D.this.slices; ++k) {
//								this.val$a[k][j][0] = FloatDHT_3D
//										.access$800(FloatDHT_3D.this)[(this.val$startt + k)];
//								this.val$a[k][j][1] = FloatDHT_3D
//										.access$800(FloatDHT_3D.this)[(this.val$startt
//										+ FloatDHT_3D
//												.access$600(FloatDHT_3D.this) + k)];
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
//	private void yTransform(float[] paramArrayOfFloat) {
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
//					float f1 = paramArrayOfFloat[l];
//					float f2 = paramArrayOfFloat[i1];
//					float f3 = paramArrayOfFloat[i2];
//					float f4 = paramArrayOfFloat[i3];
//					float f5 = paramArrayOfFloat[i4];
//					float f6 = paramArrayOfFloat[i5];
//					float f7 = paramArrayOfFloat[i6];
//					float f8 = paramArrayOfFloat[i7];
//					paramArrayOfFloat[i6] = ((f1 + f2 + f3 - f4) / 2.0F);
//					paramArrayOfFloat[i2] = ((f5 + f6 + f7 - f8) / 2.0F);
//					paramArrayOfFloat[l] = ((f7 + f8 + f5 - f6) / 2.0F);
//					paramArrayOfFloat[i4] = ((f3 + f4 + f1 - f2) / 2.0F);
//					paramArrayOfFloat[i1] = ((f8 + f7 + f6 - f5) / 2.0F);
//					paramArrayOfFloat[i5] = ((f4 + f3 + f2 - f1) / 2.0F);
//					paramArrayOfFloat[i7] = ((f2 + f1 + f4 - f3) / 2.0F);
//					paramArrayOfFloat[i3] = ((f6 + f5 + f8 - f7) / 2.0F);
//				}
//			}
//		}
//	}
//
//	private void yTransform(float[][][] paramArrayOfFloat) {
//		for (int l = 0; l <= this.slices / 2; ++l) {
//			int k = (this.slices - l) % this.slices;
//			for (int i1 = 0; i1 <= this.rows / 2; ++i1) {
//				int j = (this.rows - i1) % this.rows;
//				for (int i2 = 0; i2 <= this.columns / 2; ++i2) {
//					int i = (this.columns - i2) % this.columns;
//					float f1 = paramArrayOfFloat[l][j][i2];
//					float f2 = paramArrayOfFloat[l][i1][i];
//					float f3 = paramArrayOfFloat[k][i1][i2];
//					float f4 = paramArrayOfFloat[k][j][i];
//					float f5 = paramArrayOfFloat[k][j][i2];
//					float f6 = paramArrayOfFloat[k][i1][i];
//					float f7 = paramArrayOfFloat[l][i1][i2];
//					float f8 = paramArrayOfFloat[l][j][i];
//					paramArrayOfFloat[l][i1][i2] = ((f1 + f2 + f3 - f4) / 2.0F);
//					paramArrayOfFloat[k][i1][i2] = ((f5 + f6 + f7 - f8) / 2.0F);
//					paramArrayOfFloat[l][j][i2] = ((f7 + f8 + f5 - f6) / 2.0F);
//					paramArrayOfFloat[k][j][i2] = ((f3 + f4 + f1 - f2) / 2.0F);
//					paramArrayOfFloat[l][i1][i] = ((f8 + f7 + f6 - f5) / 2.0F);
//					paramArrayOfFloat[k][i1][i] = ((f4 + f3 + f2 - f1) / 2.0F);
//					paramArrayOfFloat[l][j][i] = ((f2 + f1 + f4 - f3) / 2.0F);
//					paramArrayOfFloat[k][j][i] = ((f6 + f5 + f8 - f7) / 2.0F);
//				}
//			}
//		}
//	}
//}