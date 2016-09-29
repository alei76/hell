package ps.hell.math.base.persons.fft.transform.fft;//package ps.landerbuluse.ml.math.fft.transform.fft;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class DoubleFFT_1D {
//	private int n;
//	private int nBluestein;
//	private int[] ip;
//	private double[] w;
//	private int nw;
//	private int nc;
//	private double[] wtable;
//	private double[] wtable_r;
//	private double[] bk1;
//	private double[] bk2;
//	private Plans plan;
//	private static final int[] factors = { 4, 2, 3, 5 };
//	private static final double PI = 3.141592653589793D;
//	private static final double TWO_PI = 6.283185307179586D;
//
//	public DoubleFFT_1D(int paramInt) {
//		if (paramInt < 1)
//			throw new IllegalArgumentException("n must be greater than 0");
//		this.n = paramInt;
//		int i;
//		if (!(ConcurrencyUtils.isPowerOf2(paramInt))) {
//			if (getReminder(paramInt, factors) >= 211) {
//				this.plan = Plans.BLUESTEIN;
//				this.nBluestein = ConcurrencyUtils.nextPow2(paramInt * 2 - 1);
//				this.bk1 = new double[2 * this.nBluestein];
//				this.bk2 = new double[2 * this.nBluestein];
//				this.ip = new int[2 + (int) Math.ceil(2 + (1 << (int) (Math
//						.log(this.nBluestein + 0.5D) / Math.log(2.0D)) / 2))];
//				this.w = new double[this.nBluestein];
//				i = 2 * this.nBluestein;
//				this.nw = this.ip[0];
//				if (i > this.nw << 2) {
//					this.nw = (i >> 2);
//					makewt(this.nw);
//				}
//				this.nc = this.ip[1];
//				if (this.nBluestein > this.nc << 2) {
//					this.nc = (this.nBluestein >> 2);
//					makect(this.nc, this.w, this.nw);
//				}
//				bluesteini();
//			} else {
//				this.plan = Plans.MIXED_RADIX;
//				this.wtable = new double[4 * paramInt + 15];
//				this.wtable_r = new double[2 * paramInt + 15];
//				cffti();
//				rffti();
//			}
//		} else {
//			this.plan = Plans.SPLIT_RADIX;
//			this.ip = new int[2 + (int) Math.ceil(2 + (1 << (int) (Math
//					.log(paramInt + 0.5D) / Math.log(2.0D)) / 2))];
//			this.w = new double[paramInt];
//			i = 2 * paramInt;
//			this.nw = this.ip[0];
//			if (i > this.nw << 2) {
//				this.nw = (i >> 2);
//				makewt(this.nw);
//			}
//			this.nc = this.ip[1];
//			if (paramInt <= this.nc << 2)
//				return;
//			this.nc = (paramInt >> 2);
//			makect(this.nc, this.w, this.nw);
//		}
//	}
//
//	public void complexForward(double[] paramArrayOfDouble) {
//		complexForward(paramArrayOfDouble, 0);
//	}
//
//	public void complexForward(double[] paramArrayOfDouble, int paramInt)
//  {
//    if (this.n == 1)
//      return;
//    switch (this.plan.ordinal())
//    {
//    case 1:
//      cftbsub(2 * this.n, paramArrayOfDouble, paramInt, this.ip, this.nw, this.w);
//      break;
//    case 2:
//      cfftf(paramArrayOfDouble, paramInt, -1);
//      break;
//    case 3:
//      bluestein_complex(paramArrayOfDouble, paramInt, -1);
//    }
//  }
//
//	public void complexInverse(double[] paramArrayOfDouble, boolean paramBoolean) {
//		complexInverse(paramArrayOfDouble, 0, paramBoolean);
//	}
//
//	public void complexInverse(double[] paramArrayOfDouble, int paramInt, boolean paramBoolean)
//  {
//    if (this.n == 1)
//      return;
//    switch (this.plan.ordinal())
//    {
//    case 1:
//      cftfsub(2 * this.n, paramArrayOfDouble, paramInt, this.ip, this.nw, this.w);
//      break;
//    case 2:
//      cfftf(paramArrayOfDouble, paramInt, 1);
//      break;
//    case 3:
//      bluestein_complex(paramArrayOfDouble, paramInt, 1);
//    }
//    if (!(paramBoolean))
//      return;
//    scale(this.n, paramArrayOfDouble, paramInt, true);
//  }
//
//	public void realForward(double[] paramArrayOfDouble) {
//		realForward(paramArrayOfDouble, 0);
//	}
//
//	public void realForward(double[] paramArrayOfDouble, int paramInt)
//  {
//    if (this.n == 1)
//      return;
//    switch (this.plan.ordinal())
//    {
//    case 1:
//      if (this.n > 4)
//      {
//        cftfsub(this.n, paramArrayOfDouble, paramInt, this.ip, this.nw, this.w);
//        rftfsub(this.n, paramArrayOfDouble, paramInt, this.nc, this.w, this.nw);
//      }
//      else if (this.n == 4)
//      {
//        cftx020(paramArrayOfDouble, paramInt);
//      }
//      double d1 = paramArrayOfDouble[paramInt] - paramArrayOfDouble[(paramInt + 1)];
//      paramArrayOfDouble[paramInt] += paramArrayOfDouble[(paramInt + 1)];
//      paramArrayOfDouble[(paramInt + 1)] = d1;
//      break;
//    case 2:
//      rfftf(paramArrayOfDouble, paramInt);
//      for (int i = this.n - 1; i >= 2; --i)
//      {
//        int j = paramInt + i;
//        double d2 = paramArrayOfDouble[j];
//        paramArrayOfDouble[j] = paramArrayOfDouble[(j - 1)];
//        paramArrayOfDouble[(j - 1)] = d2;
//      }
//      break;
//    case 3:
//      bluestein_real_forward(paramArrayOfDouble, paramInt);
//    }
//  }
//
//	public void realForwardFull(double[] paramArrayOfDouble) {
//		realForwardFull(paramArrayOfDouble, 0);
//	}
//
//	public void realForwardFull(double[] paramArrayOfDouble, int paramInt)
//  {
//    int i = 2 * this.n;
//    int l;
//    int i4;
//    int i3;
//    int k;
//    switch (this.plan.ordinal())
//    {
//    case 1:
//      realForward(paramArrayOfDouble, paramInt);
//      int j = ConcurrencyUtils.getNumberOfThreads();
//      if ((j > 1) && (this.n / 2 > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()))
//      {
//        Future[] arrayOfFuture = new Future[j];
//        l = this.n / 2 / j;
//        for (int i2 = 0; i2 < j; ++i2)
//        {
//          i4 = i2 * l;
//          int i5 = (i2 == j - 1) ? this.n / 2 : i4 + l;
//          arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i4, i5, paramInt, i, paramArrayOfDouble)
//          {
//            public void run()
//            {
//              for (int k = this.val$firstIdx; k < this.val$lastIdx; ++k)
//              {
//                int i = 2 * k;
//                int j = this.val$offa + (this.val$twon - i) % this.val$twon;
//                this.val$a[j] = this.val$a[(this.val$offa + i)];
//                this.val$a[(j + 1)] = (-this.val$a[(this.val$offa + i + 1)]);
//              }
//            }
//          });
//        }
//        ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//      }
//      else
//      {
//        for (i3 = 0; i3 < this.n / 2; ++i3)
//        {
//          k = 2 * i3;
//          l = paramInt + (i - k) % i;
//          paramArrayOfDouble[l] = paramArrayOfDouble[(paramInt + k)];
//          paramArrayOfDouble[(l + 1)] = (-paramArrayOfDouble[(paramInt + k + 1)]);
//        }
//      }
//      paramArrayOfDouble[(paramInt + this.n)] = (-paramArrayOfDouble[(paramInt + 1)]);
//      paramArrayOfDouble[(paramInt + 1)] = 0.0D;
//      break;
//    case 2:
//      rfftf(paramArrayOfDouble, paramInt);
//      if (this.n % 2 == 0)
//        k = this.n / 2;
//      else
//        k = (this.n + 1) / 2;
//      for (l = 1; l < k; ++l)
//      {
//        i3 = paramInt + i - (2 * l);
//        i4 = paramInt + 2 * l;
//        paramArrayOfDouble[(i3 + 1)] = (-paramArrayOfDouble[i4]);
//        paramArrayOfDouble[i3] = paramArrayOfDouble[(i4 - 1)];
//      }
//      for (int i1 = 1; i1 < this.n; ++i1)
//      {
//        i3 = paramInt + this.n - i1;
//        double d = paramArrayOfDouble[(i3 + 1)];
//        paramArrayOfDouble[(i3 + 1)] = paramArrayOfDouble[i3];
//        paramArrayOfDouble[i3] = d;
//      }
//      paramArrayOfDouble[(paramInt + 1)] = 0.0D;
//      break;
//    case 3:
//      bluestein_real_full(paramArrayOfDouble, paramInt, -1);
//    }
//  }
//
//	public void realInverse(double[] paramArrayOfDouble, boolean paramBoolean) {
//		realInverse(paramArrayOfDouble, 0, paramBoolean);
//	}
//
//	public void realInverse(double[] paramArrayOfDouble, int paramInt, boolean paramBoolean)
//  {
//    if (this.n == 1)
//      return;
//    switch (18.$SwitchMap$edu$emory$mathcs$jtransforms$fft$DoubleFFT_1D$Plans[this.plan.ordinal()])
//    {
//    case 1:
//      paramArrayOfDouble[(paramInt + 1)] = (0.5D * (paramArrayOfDouble[paramInt] - paramArrayOfDouble[(paramInt + 1)]));
//      paramArrayOfDouble[paramInt] -= paramArrayOfDouble[(paramInt + 1)];
//      if (this.n > 4)
//      {
//        rftfsub(this.n, paramArrayOfDouble, paramInt, this.nc, this.w, this.nw);
//        cftbsub(this.n, paramArrayOfDouble, paramInt, this.ip, this.nw, this.w);
//      }
//      else if (this.n == 4)
//      {
//        cftxc020(paramArrayOfDouble, paramInt);
//      }
//      if (!(paramBoolean))
//        return;
//      scale(this.n / 2, paramArrayOfDouble, paramInt, false);
//      break;
//    case 2:
//      for (int i = 2; i < this.n; ++i)
//      {
//        int j = paramInt + i;
//        double d = paramArrayOfDouble[(j - 1)];
//        paramArrayOfDouble[(j - 1)] = paramArrayOfDouble[j];
//        paramArrayOfDouble[j] = d;
//      }
//      rfftb(paramArrayOfDouble, paramInt);
//      if (!(paramBoolean))
//        return;
//      scale(this.n, paramArrayOfDouble, paramInt, false);
//      break;
//    case 3:
//      bluestein_real_inverse(paramArrayOfDouble, paramInt);
//      if (!(paramBoolean))
//        return;
//      scale(this.n, paramArrayOfDouble, paramInt, false);
//    }
//  }
//
//	public void realInverseFull(double[] paramArrayOfDouble,
//			boolean paramBoolean) {
//		realInverseFull(paramArrayOfDouble, 0, paramBoolean);
//	}
//
//	public void realInverseFull(double[] paramArrayOfDouble, int paramInt, boolean paramBoolean)
//  {
//    int i = 2 * this.n;
//    int l;
//    int i4;
//    int i3;
//    int k;
//    switch (18.$SwitchMap$edu$emory$mathcs$jtransforms$fft$DoubleFFT_1D$Plans[this.plan.ordinal()])
//    {
//    case 1:
//      realInverse2(paramArrayOfDouble, paramInt, paramBoolean);
//      int j = ConcurrencyUtils.getNumberOfThreads();
//      if ((j > 1) && (this.n / 2 > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()))
//      {
//        Future[] arrayOfFuture = new Future[j];
//        l = this.n / 2 / j;
//        for (int i2 = 0; i2 < j; ++i2)
//        {
//          i4 = i2 * l;
//          int i5 = (i2 == j - 1) ? this.n / 2 : i4 + l;
//          arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i4, i5, paramInt, i, paramArrayOfDouble)
//          {
//            public void run()
//            {
//              for (int k = this.val$firstIdx; k < this.val$lastIdx; ++k)
//              {
//                int i = 2 * k;
//                int j = this.val$offa + (this.val$twon - i) % this.val$twon;
//                this.val$a[j] = this.val$a[(this.val$offa + i)];
//                this.val$a[(j + 1)] = (-this.val$a[(this.val$offa + i + 1)]);
//              }
//            }
//          });
//        }
//        ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//      }
//      else
//      {
//        for (i3 = 0; i3 < this.n / 2; ++i3)
//        {
//          k = 2 * i3;
//          l = paramInt + (i - k) % i;
//          paramArrayOfDouble[l] = paramArrayOfDouble[(paramInt + k)];
//          paramArrayOfDouble[(l + 1)] = (-paramArrayOfDouble[(paramInt + k + 1)]);
//        }
//      }
//      paramArrayOfDouble[(paramInt + this.n)] = (-paramArrayOfDouble[(paramInt + 1)]);
//      paramArrayOfDouble[(paramInt + 1)] = 0.0D;
//      break;
//    case 2:
//      rfftf(paramArrayOfDouble, paramInt);
//      if (paramBoolean)
//        scale(this.n, paramArrayOfDouble, paramInt, false);
//      if (this.n % 2 == 0)
//        k = this.n / 2;
//      else
//        k = (this.n + 1) / 2;
//      for (l = 1; l < k; ++l)
//      {
//        i3 = paramInt + 2 * l;
//        i4 = paramInt + i - (2 * l);
//        paramArrayOfDouble[i3] = (-paramArrayOfDouble[i3]);
//        paramArrayOfDouble[(i4 + 1)] = (-paramArrayOfDouble[i3]);
//        paramArrayOfDouble[i4] = paramArrayOfDouble[(i3 - 1)];
//      }
//      for (int i1 = 1; i1 < this.n; ++i1)
//      {
//        i3 = paramInt + this.n - i1;
//        double d = paramArrayOfDouble[(i3 + 1)];
//        paramArrayOfDouble[(i3 + 1)] = paramArrayOfDouble[i3];
//        paramArrayOfDouble[i3] = d;
//      }
//      paramArrayOfDouble[(paramInt + 1)] = 0.0D;
//      break;
//    case 3:
//      bluestein_real_full(paramArrayOfDouble, paramInt, 1);
//      if (!(paramBoolean))
//        return;
//      scale(this.n, paramArrayOfDouble, paramInt, true);
//    }
//  }
//
//	protected void realInverse2(double[] paramArrayOfDouble, int paramInt, boolean paramBoolean)
//  {
//    if (this.n == 1)
//      return;
//    switch (18.$SwitchMap$edu$emory$mathcs$jtransforms$fft$DoubleFFT_1D$Plans[this.plan.ordinal()])
//    {
//    case 1:
//      if (this.n > 4)
//      {
//        cftfsub(this.n, paramArrayOfDouble, paramInt, this.ip, this.nw, this.w);
//        rftbsub(this.n, paramArrayOfDouble, paramInt, this.nc, this.w, this.nw);
//      }
//      else if (this.n == 4)
//      {
//        cftbsub(this.n, paramArrayOfDouble, paramInt, this.ip, this.nw, this.w);
//      }
//      double d1 = paramArrayOfDouble[paramInt] - paramArrayOfDouble[(paramInt + 1)];
//      paramArrayOfDouble[paramInt] += paramArrayOfDouble[(paramInt + 1)];
//      paramArrayOfDouble[(paramInt + 1)] = d1;
//      if (!(paramBoolean))
//        return;
//      scale(this.n, paramArrayOfDouble, paramInt, false);
//      break;
//    case 2:
//      rfftf(paramArrayOfDouble, paramInt);
//      int j;
//      for (int i = this.n - 1; i >= 2; --i)
//      {
//        j = paramInt + i;
//        double d2 = paramArrayOfDouble[j];
//        paramArrayOfDouble[j] = paramArrayOfDouble[(j - 1)];
//        paramArrayOfDouble[(j - 1)] = d2;
//      }
//      if (paramBoolean)
//        scale(this.n, paramArrayOfDouble, paramInt, false);
//      int l;
//      if (this.n % 2 == 0)
//      {
//        i = this.n / 2;
//        for (j = 1; j < i; ++j)
//        {
//          l = paramInt + 2 * j + 1;
//          paramArrayOfDouble[l] = (-paramArrayOfDouble[l]);
//        }
//        return;
//      }
//      i = (this.n - 1) / 2;
//      for (int k = 0; k < i; ++k)
//      {
//        l = paramInt + 2 * k + 1;
//        paramArrayOfDouble[l] = (-paramArrayOfDouble[l]);
//      }
//      break;
//    case 3:
//      bluestein_real_inverse2(paramArrayOfDouble, paramInt);
//      if (!(paramBoolean))
//        return;
//      scale(this.n, paramArrayOfDouble, paramInt, false);
//    }
//  }
//
//	private static int getReminder(int paramInt, int[] paramArrayOfInt) {
//		int i = paramInt;
//		if (paramInt <= 0)
//			throw new IllegalArgumentException("n must be positive integer");
//		for (int j = 0; (j < paramArrayOfInt.length) && (i != 1); ++j) {
//			int k = paramArrayOfInt[j];
//			while (i % k == 0)
//				i /= k;
//		}
//		return i;
//	}
//
//	void cffti(int paramInt1, int paramInt2) {
//		if (paramInt1 == 1)
//			return;
//		int i = 2 * paramInt1;
//		int j = 4 * paramInt1;
//		int l = 0;
//		int i12 = paramInt1;
//		int i10 = 0;
//		int i2 = 0;
//		if (++i2 <= 4)
//			l = factors[(i2 - 1)];
//		int i17;
//		do {
//			int i13 = i12 / (l += 2);
//			int i14 = i12 - (l * i13);
//			if (i14 == 0)
//				;
//			this.wtable[(paramInt2 + ++i10 + 1 + j)] = l;
//			i12 = i13;
//			if ((l != 2) || (i10 == 1))
//				continue;
//			for (i1 = 2; i1 <= i10; ++i1) {
//				int i7 = i10 - i1 + 2;
//				i17 = i7 + j;
//				this.wtable[(paramInt2 + i17 + 1)] = this.wtable[(paramInt2 + i17)];
//			}
//			this.wtable[(paramInt2 + 2 + j)] = 2.0D;
//		} while (i12 != 1);
//		this.wtable[(paramInt2 + j)] = paramInt1;
//		this.wtable[(paramInt2 + 1 + j)] = i10;
//		double d1 = 6.283185307179586D / paramInt1;
//		int i1 = 1;
//		int i5 = 1;
//		for (int i4 = 1; i4 <= i10; ++i4) {
//			int i11 = (int) this.wtable[(paramInt2 + i4 + 1 + j)];
//			int i8 = 0;
//			int i6 = i5 * i11;
//			int i15 = paramInt1 / i6;
//			int k = i15 + i15 + 2;
//			int i16 = i11 - 1;
//			for (i2 = 1; i2 <= i16; ++i2) {
//				int i3 = i1;
//				this.wtable[(paramInt2 + i1 - 1 + i)] = 1.0D;
//				this.wtable[(paramInt2 + i1 + i)] = 0.0D;
//				i8 += i5;
//				double d3 = 0.0D;
//				double d2 = i8 * d1;
//				for (int i9 = 4; i9 <= k; i9 += 2) {
//					i1 += 2;
//					d3 += 1.0D;
//					double d4 = d3 * d2;
//					i17 = i1 + i;
//					this.wtable[(paramInt2 + i17 - 1)] = Math.cos(d4);
//					this.wtable[(paramInt2 + i17)] = Math.sin(d4);
//				}
//				if (i11 <= 5)
//					continue;
//				i17 = i3 + i;
//				int i18 = i1 + i;
//				this.wtable[(paramInt2 + i17 - 1)] = this.wtable[(paramInt2
//						+ i18 - 1)];
//				this.wtable[(paramInt2 + i17)] = this.wtable[(paramInt2 + i18)];
//			}
//			i5 = i6;
//		}
//	}
//
//	void cffti() {
//		if (this.n == 1)
//			return;
//		int i = 2 * this.n;
//		int j = 4 * this.n;
//		int l = 0;
//		int i12 = this.n;
//		int i10 = 0;
//		int i2 = 0;
//		if (++i2 <= 4)
//			l = factors[(i2 - 1)];
//		int i17;
//		do {
//			int i13 = i12 / (l += 2);
//			int i14 = i12 - (l * i13);
//			if (i14 == 0)
//				;
//			this.wtable[(++i10 + 1 + j)] = l;
//			i12 = i13;
//			if ((l != 2) || (i10 == 1))
//				continue;
//			for (i1 = 2; i1 <= i10; ++i1) {
//				int i7 = i10 - i1 + 2;
//				i17 = i7 + j;
//				this.wtable[(i17 + 1)] = this.wtable[i17];
//			}
//			this.wtable[(2 + j)] = 2.0D;
//		} while (i12 != 1);
//		this.wtable[j] = this.n;
//		this.wtable[(1 + j)] = i10;
//		double d1 = 6.283185307179586D / this.n;
//		int i1 = 1;
//		int i5 = 1;
//		for (int i4 = 1; i4 <= i10; ++i4) {
//			int i11 = (int) this.wtable[(i4 + 1 + j)];
//			int i8 = 0;
//			int i6 = i5 * i11;
//			int i15 = this.n / i6;
//			int k = i15 + i15 + 2;
//			int i16 = i11 - 1;
//			for (i2 = 1; i2 <= i16; ++i2) {
//				int i3 = i1;
//				this.wtable[(i1 - 1 + i)] = 1.0D;
//				this.wtable[(i1 + i)] = 0.0D;
//				i8 += i5;
//				double d3 = 0.0D;
//				double d2 = i8 * d1;
//				for (int i9 = 4; i9 <= k; i9 += 2) {
//					i1 += 2;
//					d3 += 1.0D;
//					double d4 = d3 * d2;
//					i17 = i1 + i;
//					this.wtable[(i17 - 1)] = Math.cos(d4);
//					this.wtable[i17] = Math.sin(d4);
//				}
//				if (i11 <= 5)
//					continue;
//				i17 = i3 + i;
//				int i18 = i1 + i;
//				this.wtable[(i17 - 1)] = this.wtable[(i18 - 1)];
//				this.wtable[i17] = this.wtable[i18];
//			}
//			i5 = i6;
//		}
//	}
//
//	void rffti() {
//		if (this.n == 1)
//			return;
//		int i = 2 * this.n;
//		int j = 0;
//		int i9 = this.n;
//		int i7 = 0;
//		int l = 0;
//		if (++l <= 4)
//			j = factors[(l - 1)];
//		int k;
//		int i16;
//		do {
//			int i11 = i9 / (j += 2);
//			int i12 = i9 - (j * i11);
//			if (i12 == 0)
//				;
//			this.wtable_r[(++i7 + 1 + i)] = j;
//			i9 = i11;
//			if ((j != 2) || (i7 == 1))
//				continue;
//			for (k = 2; k <= i7; ++k) {
//				int i4 = i7 - k + 2;
//				i16 = i4 + i;
//				this.wtable_r[(i16 + 1)] = this.wtable_r[i16];
//			}
//			this.wtable_r[(2 + i)] = 2.0D;
//		} while (i9 != 1);
//		this.wtable_r[i] = this.n;
//		this.wtable_r[(1 + i)] = i7;
//		double d1 = 6.283185307179586D / this.n;
//		int i10 = 0;
//		int i15 = i7 - 1;
//		int i2 = 1;
//		if (i15 == 0)
//			return;
//		for (int i1 = 1; i1 <= i15; ++i1) {
//			int i8 = (int) this.wtable_r[(i1 + 1 + i)];
//			int i5 = 0;
//			int i3 = i2 * i8;
//			int i13 = this.n / i3;
//			int i14 = i8 - 1;
//			for (l = 1; l <= i14; ++l) {
//				i5 += i2;
//				k = i10;
//				double d2 = i5 * d1;
//				double d3 = 0.0D;
//				for (int i6 = 3; i6 <= i13; i6 += 2) {
//					k += 2;
//					d3 += 1.0D;
//					double d4 = d3 * d2;
//					i16 = k + this.n;
//					this.wtable_r[(i16 - 2)] = Math.cos(d4);
//					this.wtable_r[(i16 - 1)] = Math.sin(d4);
//				}
//				i10 += i13;
//			}
//			i2 = i3;
//		}
//	}
//
//	private void bluesteini() {
//		int i = 0;
//		double d2 = 3.141592653589793D / this.n;
//		this.bk1[0] = 1.0D;
//		this.bk1[1] = 0.0D;
//		for (int j = 1; j < this.n; ++j) {
//			i += 2 * j - 1;
//			if (i >= 2 * this.n)
//				i -= 2 * this.n;
//			double d1 = d2 * i;
//			this.bk1[(2 * j)] = Math.cos(d1);
//			this.bk1[(2 * j + 1)] = Math.sin(d1);
//		}
//		double d3 = 1.0D / this.nBluestein;
//		this.bk2[0] = (this.bk1[0] * d3);
//		this.bk2[1] = (this.bk1[1] * d3);
//		for (int k = 2; k < 2 * this.n; k += 2) {
//			this.bk2[k] = (this.bk1[k] * d3);
//			this.bk2[(k + 1)] = (this.bk1[(k + 1)] * d3);
//			this.bk2[(2 * this.nBluestein - k)] = this.bk2[k];
//			this.bk2[(2 * this.nBluestein - k + 1)] = this.bk2[(k + 1)];
//		}
//		cftbsub(2 * this.nBluestein, this.bk2, 0, this.ip, this.nw, this.w);
//	}
//
//	private void makewt(int paramInt) {
//		this.ip[0] = paramInt;
//		this.ip[1] = 1;
//		if (paramInt <= 2)
//			return;
//		int j = paramInt >> 1;
//		double d1 = 0.7853981633974483D / j;
//		double d7 = d1 * 2.0D;
//		double d2 = Math.cos(d1 * j);
//		this.w[0] = 1.0D;
//		this.w[1] = d2;
//		int i;
//		if (j == 4) {
//			this.w[2] = Math.cos(d7);
//			this.w[3] = Math.sin(d7);
//		} else if (j > 4) {
//			makeipt(paramInt);
//			this.w[2] = (0.5D / Math.cos(d7));
//			this.w[3] = (0.5D / Math.cos(d1 * 6.0D));
//			for (i = 4; i < j; i += 4) {
//				double d8 = d1 * i;
//				double d9 = 3.0D * d8;
//				this.w[i] = Math.cos(d8);
//				this.w[(i + 1)] = Math.sin(d8);
//				this.w[(i + 2)] = Math.cos(d9);
//				this.w[(i + 3)] = (-Math.sin(d9));
//			}
//		}
//		int l;
//		for (int k = 0; j > 2; k = l) {
//			l = k + j;
//			j >>= 1;
//			this.w[l] = 1.0D;
//			this.w[(l + 1)] = d2;
//			double d3;
//			double d4;
//			if (j == 4) {
//				d3 = this.w[(k + 4)];
//				d4 = this.w[(k + 5)];
//				this.w[(l + 2)] = d3;
//				this.w[(l + 3)] = d4;
//			} else {
//				if (j <= 4)
//					continue;
//				d3 = this.w[(k + 4)];
//				double d5 = this.w[(k + 6)];
//				this.w[(l + 2)] = (0.5D / d3);
//				this.w[(l + 3)] = (0.5D / d5);
//				for (i = 4; i < j; i += 4) {
//					int i1 = k + 2 * i;
//					int i2 = l + i;
//					d3 = this.w[i1];
//					d4 = this.w[(i1 + 1)];
//					d5 = this.w[(i1 + 2)];
//					double d6 = this.w[(i1 + 3)];
//					this.w[i2] = d3;
//					this.w[(i2 + 1)] = d4;
//					this.w[(i2 + 2)] = d5;
//					this.w[(i2 + 3)] = d6;
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
//	private void makect(int paramInt1, double[] paramArrayOfDouble,
//			int paramInt2) {
//		this.ip[1] = paramInt1;
//		if (paramInt1 <= 1)
//			return;
//		int j = paramInt1 >> 1;
//		double d1 = 0.7853981633974483D / j;
//		paramArrayOfDouble[paramInt2] = Math.cos(d1 * j);
//		paramArrayOfDouble[(paramInt2 + j)] = (0.5D * paramArrayOfDouble[paramInt2]);
//		for (int i = 1; i < j; ++i) {
//			double d2 = d1 * i;
//			paramArrayOfDouble[(paramInt2 + i)] = (0.5D * Math.cos(d2));
//			paramArrayOfDouble[(paramInt2 + paramInt1 - i)] = (0.5D * Math
//					.sin(d2));
//		}
//	}
//
//	private void bluestein_complex(double[] paramArrayOfDouble, int paramInt1,
//			int paramInt2) {
//		double[] arrayOfDouble = new double[2 * this.nBluestein];
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int k;
//		int i3;
//		int i5;
//		int i2;
//		if ((i > 1)
//				&& (this.n > ConcurrencyUtils
//						.getThreadsBeginN_1D_FFT_2Threads())) {
//			i = 2;
//			if ((i >= 4)
//					&& (this.n > ConcurrencyUtils
//							.getThreadsBeginN_1D_FFT_4Threads()))
//				i = 4;
//			Future[] arrayOfFuture = new Future[i];
//			k = this.n / i;
//			for (int l = 0; l < i; ++l) {
//				i3 = l * k;
//				i5 = (l == i - 1) ? this.n : i3 + k;
//				arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(
//						paramInt2, i3, i5, paramInt1, arrayOfDouble,
//						paramArrayOfDouble) {
//					public void run() {
//						int i;
//						int j;
//						int k;
//						int l;
//						int i1;
//						if (this.val$isign > 0)
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								l = this.val$offa + j;
//								i1 = this.val$offa + k;
//								this.val$ak[j] = (this.val$a[l]
//										* DoubleFFT_1D
//												.access$000(DoubleFFT_1D.this)[j] - (this.val$a[i1] * DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[k]));
//								this.val$ak[k] = (this.val$a[l]
//										* DoubleFFT_1D
//												.access$000(DoubleFFT_1D.this)[k] + this.val$a[i1]
//										* DoubleFFT_1D
//												.access$000(DoubleFFT_1D.this)[j]);
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								l = this.val$offa + j;
//								i1 = this.val$offa + k;
//								this.val$ak[j] = (this.val$a[l]
//										* DoubleFFT_1D
//												.access$000(DoubleFFT_1D.this)[j] + this.val$a[i1]
//										* DoubleFFT_1D
//												.access$000(DoubleFFT_1D.this)[k]);
//								this.val$ak[k] = (-this.val$a[l]
//										* DoubleFFT_1D
//												.access$000(DoubleFFT_1D.this)[k] + this.val$a[i1]
//										* DoubleFFT_1D
//												.access$000(DoubleFFT_1D.this)[j]);
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftbsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			k = this.nBluestein / i;
//			for (int i1 = 0; i1 < i; ++i1) {
//				i3 = i1 * k;
//				i5 = (i1 == i - 1) ? this.nBluestein : i3 + k;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//						paramInt2, i3, i5, arrayOfDouble) {
//					public void run() {
//						int i;
//						int j;
//						int k;
//						double d;
//						if (this.val$isign > 0)
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								d = -this.val$ak[j] * DoubleFFT_1D.this.bk2[k]
//										+ this.val$ak[k]
//										* DoubleFFT_1D.this.bk2[j];
//								this.val$ak[j] = (this.val$ak[j]
//										* DoubleFFT_1D
//												.access$100(DoubleFFT_1D.this)[j] + this.val$ak[k]
//										* DoubleFFT_1D
//												.access$100(DoubleFFT_1D.this)[k]);
//								this.val$ak[k] = d;
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								d = this.val$ak[j] * DoubleFFT_1D.this.bk2[k]
//										+ this.val$ak[k]
//										* DoubleFFT_1D.this.bk2[j];
//								this.val$ak[j] = (this.val$ak[j]
//										* DoubleFFT_1D
//												.access$100(DoubleFFT_1D.this)[j] - (this.val$ak[k] * DoubleFFT_1D
//										.access$100(DoubleFFT_1D.this)[k]));
//								this.val$ak[k] = d;
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftfsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			k = this.n / i;
//			for (i2 = 0; i2 < i; ++i2) {
//				i3 = i2 * k;
//				i5 = (i2 == i - 1) ? this.n : i3 + k;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(
//						paramInt2, i3, i5, paramInt1, paramArrayOfDouble,
//						arrayOfDouble) {
//					public void run() {
//						int i;
//						int j;
//						int k;
//						int l;
//						int i1;
//						if (this.val$isign > 0)
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								l = this.val$offa + j;
//								i1 = this.val$offa + k;
//								this.val$a[l] = (DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[j]
//										* this.val$ak[j] - (DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[k] * this.val$ak[k]));
//								this.val$a[i1] = (DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[k]
//										* this.val$ak[j] + DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[j]
//										* this.val$ak[k]);
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								l = this.val$offa + j;
//								i1 = this.val$offa + k;
//								this.val$a[l] = (DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[j]
//										* this.val$ak[j] + DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[k]
//										* this.val$ak[k]);
//								this.val$a[i1] = (-DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[k]
//										* this.val$ak[j] + DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[j]
//										* this.val$ak[k]);
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			int j;
//			if (paramInt2 > 0)
//				for (j = 0; j < this.n; ++j) {
//					k = 2 * j;
//					i2 = k + 1;
//					i3 = paramInt1 + k;
//					i5 = paramInt1 + i2;
//					arrayOfDouble[k] = (paramArrayOfDouble[i3] * this.bk1[k] - (paramArrayOfDouble[i5] * this.bk1[i2]));
//					arrayOfDouble[i2] = (paramArrayOfDouble[i3] * this.bk1[i2] + paramArrayOfDouble[i5]
//							* this.bk1[k]);
//				}
//			else
//				for (j = 0; j < this.n; ++j) {
//					k = 2 * j;
//					i2 = k + 1;
//					i3 = paramInt1 + k;
//					i5 = paramInt1 + i2;
//					arrayOfDouble[k] = (paramArrayOfDouble[i3] * this.bk1[k] + paramArrayOfDouble[i5]
//							* this.bk1[i2]);
//					arrayOfDouble[i2] = (-paramArrayOfDouble[i3] * this.bk1[i2] + paramArrayOfDouble[i5]
//							* this.bk1[k]);
//				}
//			cftbsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			double d;
//			if (paramInt2 > 0)
//				for (j = 0; j < this.nBluestein; ++j) {
//					k = 2 * j;
//					i2 = k + 1;
//					d = -arrayOfDouble[k] * this.bk2[i2] + arrayOfDouble[i2]
//							* this.bk2[k];
//					arrayOfDouble[k] = (arrayOfDouble[k] * this.bk2[k] + arrayOfDouble[i2]
//							* this.bk2[i2]);
//					arrayOfDouble[i2] = d;
//				}
//			else
//				for (j = 0; j < this.nBluestein; ++j) {
//					k = 2 * j;
//					i2 = k + 1;
//					d = arrayOfDouble[k] * this.bk2[i2] + arrayOfDouble[i2]
//							* this.bk2[k];
//					arrayOfDouble[k] = (arrayOfDouble[k] * this.bk2[k] - (arrayOfDouble[i2] * this.bk2[i2]));
//					arrayOfDouble[i2] = d;
//				}
//			cftfsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			int i4;
//			if (paramInt2 > 0)
//				for (j = 0; j < this.n; ++j) {
//					k = 2 * j;
//					i2 = k + 1;
//					i4 = paramInt1 + k;
//					i5 = paramInt1 + i2;
//					paramArrayOfDouble[i4] = (this.bk1[k] * arrayOfDouble[k] - (this.bk1[i2] * arrayOfDouble[i2]));
//					paramArrayOfDouble[i5] = (this.bk1[i2] * arrayOfDouble[k] + this.bk1[k]
//							* arrayOfDouble[i2]);
//				}
//			else
//				for (j = 0; j < this.n; ++j) {
//					k = 2 * j;
//					i2 = k + 1;
//					i4 = paramInt1 + k;
//					i5 = paramInt1 + i2;
//					paramArrayOfDouble[i4] = (this.bk1[k] * arrayOfDouble[k] + this.bk1[i2]
//							* arrayOfDouble[i2]);
//					paramArrayOfDouble[i5] = (-this.bk1[i2] * arrayOfDouble[k] + this.bk1[k]
//							* arrayOfDouble[i2]);
//				}
//		}
//	}
//
//	private void bluestein_real_full(double[] paramArrayOfDouble,
//			int paramInt1, int paramInt2) {
//		double[] arrayOfDouble = new double[2 * this.nBluestein];
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int i1;
//		int i5;
//		int i4;
//		if ((i > 1)
//				&& (this.n > ConcurrencyUtils
//						.getThreadsBeginN_1D_FFT_2Threads())) {
//			i = 2;
//			if ((i >= 4)
//					&& (this.n > ConcurrencyUtils
//							.getThreadsBeginN_1D_FFT_4Threads()))
//				i = 4;
//			Future[] arrayOfFuture = new Future[i];
//			i1 = this.n / i;
//			int i6;
//			for (int i2 = 0; i2 < i; ++i2) {
//				i5 = i2 * i1;
//				i6 = (i2 == i - 1) ? this.n : i5 + i1;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(
//						paramInt2, i5, i6, paramInt1, arrayOfDouble,
//						paramArrayOfDouble) {
//					public void run() {
//						int i;
//						int j;
//						int k;
//						int l;
//						if (this.val$isign > 0)
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								l = this.val$offa + i;
//								this.val$ak[j] = (this.val$a[l] * DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[j]);
//								this.val$ak[k] = (this.val$a[l] * DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[k]);
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								l = this.val$offa + i;
//								this.val$ak[j] = (this.val$a[l] * DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[j]);
//								this.val$ak[k] = (-this.val$a[l] * DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[k]);
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftbsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			i1 = this.nBluestein / i;
//			for (int i3 = 0; i3 < i; ++i3) {
//				i5 = i3 * i1;
//				i6 = (i3 == i - 1) ? this.nBluestein : i5 + i1;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(
//						paramInt2, i5, i6, arrayOfDouble) {
//					public void run() {
//						int i;
//						int j;
//						int k;
//						double d;
//						if (this.val$isign > 0)
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								d = -this.val$ak[j] * DoubleFFT_1D.this.bk2[k]
//										+ this.val$ak[k]
//										* DoubleFFT_1D.this.bk2[j];
//								this.val$ak[j] = (this.val$ak[j]
//										* DoubleFFT_1D
//												.access$100(DoubleFFT_1D.this)[j] + this.val$ak[k]
//										* DoubleFFT_1D
//												.access$100(DoubleFFT_1D.this)[k]);
//								this.val$ak[k] = d;
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								d = this.val$ak[j] * DoubleFFT_1D.this.bk2[k]
//										+ this.val$ak[k]
//										* DoubleFFT_1D.this.bk2[j];
//								this.val$ak[j] = (this.val$ak[j]
//										* DoubleFFT_1D
//												.access$100(DoubleFFT_1D.this)[j] - (this.val$ak[k] * DoubleFFT_1D
//										.access$100(DoubleFFT_1D.this)[k]));
//								this.val$ak[k] = d;
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftfsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			i1 = this.n / i;
//			for (i4 = 0; i4 < i; ++i4) {
//				i5 = i4 * i1;
//				i6 = (i4 == i - 1) ? this.n : i5 + i1;
//				arrayOfFuture[i4] = ConcurrencyUtils.submit(new Runnable(
//						paramInt2, i5, i6, paramArrayOfDouble, paramInt1,
//						arrayOfDouble) {
//					public void run() {
//						int i;
//						int j;
//						int k;
//						if (this.val$isign > 0)
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								this.val$a[(this.val$offa + j)] = (DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[j]
//										* this.val$ak[j] - (DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[k] * this.val$ak[k]));
//								this.val$a[(this.val$offa + k)] = (DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[k]
//										* this.val$ak[j] + DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[j]
//										* this.val$ak[k]);
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								this.val$a[(this.val$offa + j)] = (DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[j]
//										* this.val$ak[j] + DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[k]
//										* this.val$ak[k]);
//								this.val$a[(this.val$offa + k)] = (-DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[k]
//										* this.val$ak[j] + DoubleFFT_1D
//										.access$000(DoubleFFT_1D.this)[j]
//										* this.val$ak[k]);
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			if (paramInt2 > 0)
//				for (int j = 0; j < this.n; ++j) {
//					i1 = 2 * j;
//					i4 = i1 + 1;
//					i5 = paramInt1 + j;
//					arrayOfDouble[i1] = (paramArrayOfDouble[i5] * this.bk1[i1]);
//					arrayOfDouble[i4] = (paramArrayOfDouble[i5] * this.bk1[i4]);
//				}
//			else
//				for (int k = 0; k < this.n; ++k) {
//					i1 = 2 * k;
//					i4 = i1 + 1;
//					i5 = paramInt1 + k;
//					arrayOfDouble[i1] = (paramArrayOfDouble[i5] * this.bk1[i1]);
//					arrayOfDouble[i4] = (-paramArrayOfDouble[i5] * this.bk1[i4]);
//				}
//			cftbsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			int l;
//			double d;
//			if (paramInt2 > 0)
//				for (l = 0; l < this.nBluestein; ++l) {
//					i1 = 2 * l;
//					i4 = i1 + 1;
//					d = -arrayOfDouble[i1] * this.bk2[i4] + arrayOfDouble[i4]
//							* this.bk2[i1];
//					arrayOfDouble[i1] = (arrayOfDouble[i1] * this.bk2[i1] + arrayOfDouble[i4]
//							* this.bk2[i4]);
//					arrayOfDouble[i4] = d;
//				}
//			else
//				for (l = 0; l < this.nBluestein; ++l) {
//					i1 = 2 * l;
//					i4 = i1 + 1;
//					d = arrayOfDouble[i1] * this.bk2[i4] + arrayOfDouble[i4]
//							* this.bk2[i1];
//					arrayOfDouble[i1] = (arrayOfDouble[i1] * this.bk2[i1] - (arrayOfDouble[i4] * this.bk2[i4]));
//					arrayOfDouble[i4] = d;
//				}
//			cftfsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			if (paramInt2 > 0)
//				for (l = 0; l < this.n; ++l) {
//					i1 = 2 * l;
//					i4 = i1 + 1;
//					paramArrayOfDouble[(paramInt1 + i1)] = (this.bk1[i1]
//							* arrayOfDouble[i1] - (this.bk1[i4] * arrayOfDouble[i4]));
//					paramArrayOfDouble[(paramInt1 + i4)] = (this.bk1[i4]
//							* arrayOfDouble[i1] + this.bk1[i1]
//							* arrayOfDouble[i4]);
//				}
//			else
//				for (l = 0; l < this.n; ++l) {
//					i1 = 2 * l;
//					i4 = i1 + 1;
//					paramArrayOfDouble[(paramInt1 + i1)] = (this.bk1[i1]
//							* arrayOfDouble[i1] + this.bk1[i4]
//							* arrayOfDouble[i4]);
//					paramArrayOfDouble[(paramInt1 + i4)] = (-this.bk1[i4]
//							* arrayOfDouble[i1] + this.bk1[i1]
//							* arrayOfDouble[i4]);
//				}
//		}
//	}
//
//	private void bluestein_real_forward(double[] paramArrayOfDouble,
//			int paramInt) {
//		double[] arrayOfDouble = new double[2 * this.nBluestein];
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int l;
//		int i3;
//		int i2;
//		int k;
//		if ((i > 1)
//				&& (this.n > ConcurrencyUtils
//						.getThreadsBeginN_1D_FFT_2Threads())) {
//			i = 2;
//			if ((i >= 4)
//					&& (this.n > ConcurrencyUtils
//							.getThreadsBeginN_1D_FFT_4Threads()))
//				i = 4;
//			Future[] arrayOfFuture = new Future[i];
//			l = this.n / i;
//			int i4;
//			for (int i1 = 0; i1 < i; ++i1) {
//				i3 = i1 * l;
//				i4 = (i1 == i - 1) ? this.n : i3 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, paramInt, arrayOfDouble, paramArrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							int l = this.val$offa + i;
//							this.val$ak[j] = (this.val$a[l] * DoubleFFT_1D
//									.access$000(DoubleFFT_1D.this)[j]);
//							this.val$ak[k] = (-this.val$a[l] * DoubleFFT_1D
//									.access$000(DoubleFFT_1D.this)[k]);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftbsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			l = this.nBluestein / i;
//			for (i2 = 0; i2 < i; ++i2) {
//				i3 = i2 * l;
//				i4 = (i2 == i - 1) ? this.nBluestein : i3 + l;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, arrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							double d = this.val$ak[j]
//									* DoubleFFT_1D.this.bk2[k] + this.val$ak[k]
//									* DoubleFFT_1D.this.bk2[j];
//							this.val$ak[j] = (this.val$ak[j]
//									* DoubleFFT_1D
//											.access$100(DoubleFFT_1D.this)[j] - (this.val$ak[k] * DoubleFFT_1D
//									.access$100(DoubleFFT_1D.this)[k]));
//							this.val$ak[k] = d;
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int j = 0; j < this.n; ++j) {
//				l = 2 * j;
//				i2 = l + 1;
//				i3 = paramInt + j;
//				arrayOfDouble[l] = (paramArrayOfDouble[i3] * this.bk1[l]);
//				arrayOfDouble[i2] = (-paramArrayOfDouble[i3] * this.bk1[i2]);
//			}
//			cftbsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			for (k = 0; k < this.nBluestein; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				double d = arrayOfDouble[l] * this.bk2[i2] + arrayOfDouble[i2]
//						* this.bk2[l];
//				arrayOfDouble[l] = (arrayOfDouble[l] * this.bk2[l] - (arrayOfDouble[i2] * this.bk2[i2]));
//				arrayOfDouble[i2] = d;
//			}
//		}
//		cftfsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw, this.w);
//		if (this.n % 2 == 0) {
//			paramArrayOfDouble[paramInt] = (this.bk1[0] * arrayOfDouble[0] + this.bk1[1]
//					* arrayOfDouble[1]);
//			paramArrayOfDouble[(paramInt + 1)] = (this.bk1[this.n]
//					* arrayOfDouble[this.n] + this.bk1[(this.n + 1)]
//					* arrayOfDouble[(this.n + 1)]);
//			for (k = 1; k < this.n / 2; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				paramArrayOfDouble[(paramInt + l)] = (this.bk1[l]
//						* arrayOfDouble[l] + this.bk1[i2] * arrayOfDouble[i2]);
//				paramArrayOfDouble[(paramInt + i2)] = (-this.bk1[i2]
//						* arrayOfDouble[l] + this.bk1[l] * arrayOfDouble[i2]);
//			}
//		} else {
//			paramArrayOfDouble[paramInt] = (this.bk1[0] * arrayOfDouble[0] + this.bk1[1]
//					* arrayOfDouble[1]);
//			paramArrayOfDouble[(paramInt + 1)] = (-this.bk1[this.n]
//					* arrayOfDouble[(this.n - 1)] + this.bk1[(this.n - 1)]
//					* arrayOfDouble[this.n]);
//			for (k = 1; k < (this.n - 1) / 2; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				paramArrayOfDouble[(paramInt + l)] = (this.bk1[l]
//						* arrayOfDouble[l] + this.bk1[i2] * arrayOfDouble[i2]);
//				paramArrayOfDouble[(paramInt + i2)] = (-this.bk1[i2]
//						* arrayOfDouble[l] + this.bk1[l] * arrayOfDouble[i2]);
//			}
//			paramArrayOfDouble[(paramInt + this.n - 1)] = (this.bk1[(this.n - 1)]
//					* arrayOfDouble[(this.n - 1)] + this.bk1[this.n]
//					* arrayOfDouble[this.n]);
//		}
//	}
//
//	private void bluestein_real_inverse(double[] paramArrayOfDouble,
//			int paramInt) {
//		double[] arrayOfDouble = new double[2 * this.nBluestein];
//		int j;
//		int l;
//		int i1;
//		int i3;
//		if (this.n % 2 == 0) {
//			arrayOfDouble[0] = (paramArrayOfDouble[paramInt] * this.bk1[0]);
//			arrayOfDouble[1] = (paramArrayOfDouble[paramInt] * this.bk1[1]);
//			for (i = 1; i < this.n / 2; ++i) {
//				j = 2 * i;
//				l = j + 1;
//				i1 = paramInt + j;
//				i3 = paramInt + l;
//				arrayOfDouble[j] = (paramArrayOfDouble[i1] * this.bk1[j] - (paramArrayOfDouble[i3] * this.bk1[l]));
//				arrayOfDouble[l] = (paramArrayOfDouble[i1] * this.bk1[l] + paramArrayOfDouble[i3]
//						* this.bk1[j]);
//			}
//			arrayOfDouble[this.n] = (paramArrayOfDouble[(paramInt + 1)] * this.bk1[this.n]);
//			arrayOfDouble[(this.n + 1)] = (paramArrayOfDouble[(paramInt + 1)] * this.bk1[(this.n + 1)]);
//			for (i = this.n / 2 + 1; i < this.n; ++i) {
//				j = 2 * i;
//				l = j + 1;
//				i1 = paramInt + 2 * this.n - j;
//				i3 = i1 + 1;
//				arrayOfDouble[j] = (paramArrayOfDouble[i1] * this.bk1[j] + paramArrayOfDouble[i3]
//						* this.bk1[l]);
//				arrayOfDouble[l] = (paramArrayOfDouble[i1] * this.bk1[l] - (paramArrayOfDouble[i3] * this.bk1[j]));
//			}
//		} else {
//			arrayOfDouble[0] = (paramArrayOfDouble[paramInt] * this.bk1[0]);
//			arrayOfDouble[1] = (paramArrayOfDouble[paramInt] * this.bk1[1]);
//			for (i = 1; i < (this.n - 1) / 2; ++i) {
//				j = 2 * i;
//				l = j + 1;
//				i1 = paramInt + j;
//				i3 = paramInt + l;
//				arrayOfDouble[j] = (paramArrayOfDouble[i1] * this.bk1[j] - (paramArrayOfDouble[i3] * this.bk1[l]));
//				arrayOfDouble[l] = (paramArrayOfDouble[i1] * this.bk1[l] + paramArrayOfDouble[i3]
//						* this.bk1[j]);
//			}
//			arrayOfDouble[(this.n - 1)] = (paramArrayOfDouble[(paramInt
//					+ this.n - 1)]
//					* this.bk1[(this.n - 1)] - (paramArrayOfDouble[(paramInt + 1)] * this.bk1[this.n]));
//			arrayOfDouble[this.n] = (paramArrayOfDouble[(paramInt + this.n - 1)]
//					* this.bk1[this.n] + paramArrayOfDouble[(paramInt + 1)]
//					* this.bk1[(this.n - 1)]);
//			arrayOfDouble[(this.n + 1)] = (paramArrayOfDouble[(paramInt
//					+ this.n - 1)]
//					* this.bk1[(this.n + 1)] + paramArrayOfDouble[(paramInt + 1)]
//					* this.bk1[(this.n + 2)]);
//			arrayOfDouble[(this.n + 2)] = (paramArrayOfDouble[(paramInt
//					+ this.n - 1)]
//					* this.bk1[(this.n + 2)] - (paramArrayOfDouble[(paramInt + 1)] * this.bk1[(this.n + 1)]));
//			for (i = (this.n - 1) / 2 + 2; i < this.n; ++i) {
//				j = 2 * i;
//				l = j + 1;
//				i1 = paramInt + 2 * this.n - j;
//				i3 = i1 + 1;
//				arrayOfDouble[j] = (paramArrayOfDouble[i1] * this.bk1[j] + paramArrayOfDouble[i3]
//						* this.bk1[l]);
//				arrayOfDouble[l] = (paramArrayOfDouble[i1] * this.bk1[l] - (paramArrayOfDouble[i3] * this.bk1[j]));
//			}
//		}
//		cftbsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw, this.w);
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int i2;
//		if ((i > 1)
//				&& (this.n > ConcurrencyUtils
//						.getThreadsBeginN_1D_FFT_2Threads())) {
//			i = 2;
//			if ((i >= 4)
//					&& (this.n > ConcurrencyUtils
//							.getThreadsBeginN_1D_FFT_4Threads()))
//				i = 4;
//			Future[] arrayOfFuture = new Future[i];
//			l = this.nBluestein / i;
//			int i4;
//			for (i1 = 0; i1 < i; ++i1) {
//				i3 = i1 * l;
//				i4 = (i1 == i - 1) ? this.nBluestein : i3 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, arrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							double d = -this.val$ak[j]
//									* DoubleFFT_1D.this.bk2[k] + this.val$ak[k]
//									* DoubleFFT_1D.this.bk2[j];
//							this.val$ak[j] = (this.val$ak[j]
//									* DoubleFFT_1D
//											.access$100(DoubleFFT_1D.this)[j] + this.val$ak[k]
//									* DoubleFFT_1D
//											.access$100(DoubleFFT_1D.this)[k]);
//							this.val$ak[k] = d;
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftfsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			l = this.n / i;
//			for (i2 = 0; i2 < i; ++i2) {
//				i3 = i2 * l;
//				i4 = (i2 == i - 1) ? this.n : i3 + l;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, paramArrayOfDouble, paramInt, arrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							this.val$a[(this.val$offa + i)] = (DoubleFFT_1D
//									.access$000(DoubleFFT_1D.this)[j]
//									* this.val$ak[j] - (DoubleFFT_1D
//									.access$000(DoubleFFT_1D.this)[k] * this.val$ak[k]));
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int k = 0; k < this.nBluestein; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				double d = -arrayOfDouble[l] * this.bk2[i2] + arrayOfDouble[i2]
//						* this.bk2[l];
//				arrayOfDouble[l] = (arrayOfDouble[l] * this.bk2[l] + arrayOfDouble[i2]
//						* this.bk2[i2]);
//				arrayOfDouble[i2] = d;
//			}
//			cftfsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			for (k = 0; k < this.n; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				paramArrayOfDouble[(paramInt + k)] = (this.bk1[l]
//						* arrayOfDouble[l] - (this.bk1[i2] * arrayOfDouble[i2]));
//			}
//		}
//	}
//
//	private void bluestein_real_inverse2(double[] paramArrayOfDouble,
//			int paramInt) {
//		double[] arrayOfDouble = new double[2 * this.nBluestein];
//		int i = ConcurrencyUtils.getNumberOfThreads();
//		int l;
//		int i3;
//		int i2;
//		int k;
//		if ((i > 1)
//				&& (this.n > ConcurrencyUtils
//						.getThreadsBeginN_1D_FFT_2Threads())) {
//			i = 2;
//			if ((i >= 4)
//					&& (this.n > ConcurrencyUtils
//							.getThreadsBeginN_1D_FFT_4Threads()))
//				i = 4;
//			Future[] arrayOfFuture = new Future[i];
//			l = this.n / i;
//			int i4;
//			for (int i1 = 0; i1 < i; ++i1) {
//				i3 = i1 * l;
//				i4 = (i1 == i - 1) ? this.n : i3 + l;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, paramInt, arrayOfDouble, paramArrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							int l = this.val$offa + i;
//							this.val$ak[j] = (this.val$a[l] * DoubleFFT_1D
//									.access$000(DoubleFFT_1D.this)[j]);
//							this.val$ak[k] = (this.val$a[l] * DoubleFFT_1D
//									.access$000(DoubleFFT_1D.this)[k]);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftbsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			l = this.nBluestein / i;
//			for (i2 = 0; i2 < i; ++i2) {
//				i3 = i2 * l;
//				i4 = (i2 == i - 1) ? this.nBluestein : i3 + l;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, arrayOfDouble) {
//					public void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							double d = -this.val$ak[j]
//									* DoubleFFT_1D.this.bk2[k] + this.val$ak[k]
//									* DoubleFFT_1D.this.bk2[j];
//							this.val$ak[j] = (this.val$ak[j]
//									* DoubleFFT_1D
//											.access$100(DoubleFFT_1D.this)[j] + this.val$ak[k]
//									* DoubleFFT_1D
//											.access$100(DoubleFFT_1D.this)[k]);
//							this.val$ak[k] = d;
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int j = 0; j < this.n; ++j) {
//				l = 2 * j;
//				i2 = l + 1;
//				i3 = paramInt + j;
//				arrayOfDouble[l] = (paramArrayOfDouble[i3] * this.bk1[l]);
//				arrayOfDouble[i2] = (paramArrayOfDouble[i3] * this.bk1[i2]);
//			}
//			cftbsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw,
//					this.w);
//			for (k = 0; k < this.nBluestein; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				double d = -arrayOfDouble[l] * this.bk2[i2] + arrayOfDouble[i2]
//						* this.bk2[l];
//				arrayOfDouble[l] = (arrayOfDouble[l] * this.bk2[l] + arrayOfDouble[i2]
//						* this.bk2[i2]);
//				arrayOfDouble[i2] = d;
//			}
//		}
//		cftfsub(2 * this.nBluestein, arrayOfDouble, 0, this.ip, this.nw, this.w);
//		if (this.n % 2 == 0) {
//			paramArrayOfDouble[paramInt] = (this.bk1[0] * arrayOfDouble[0] - (this.bk1[1] * arrayOfDouble[1]));
//			paramArrayOfDouble[(paramInt + 1)] = (this.bk1[this.n]
//					* arrayOfDouble[this.n] - (this.bk1[(this.n + 1)] * arrayOfDouble[(this.n + 1)]));
//			for (k = 1; k < this.n / 2; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				paramArrayOfDouble[(paramInt + l)] = (this.bk1[l]
//						* arrayOfDouble[l] - (this.bk1[i2] * arrayOfDouble[i2]));
//				paramArrayOfDouble[(paramInt + i2)] = (this.bk1[i2]
//						* arrayOfDouble[l] + this.bk1[l] * arrayOfDouble[i2]);
//			}
//		} else {
//			paramArrayOfDouble[paramInt] = (this.bk1[0] * arrayOfDouble[0] - (this.bk1[1] * arrayOfDouble[1]));
//			paramArrayOfDouble[(paramInt + 1)] = (this.bk1[this.n]
//					* arrayOfDouble[(this.n - 1)] + this.bk1[(this.n - 1)]
//					* arrayOfDouble[this.n]);
//			for (k = 1; k < (this.n - 1) / 2; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				paramArrayOfDouble[(paramInt + l)] = (this.bk1[l]
//						* arrayOfDouble[l] - (this.bk1[i2] * arrayOfDouble[i2]));
//				paramArrayOfDouble[(paramInt + i2)] = (this.bk1[i2]
//						* arrayOfDouble[l] + this.bk1[l] * arrayOfDouble[i2]);
//			}
//			paramArrayOfDouble[(paramInt + this.n - 1)] = (this.bk1[(this.n - 1)]
//					* arrayOfDouble[(this.n - 1)] - (this.bk1[this.n] * arrayOfDouble[this.n]));
//		}
//	}
//
//	void rfftf(double[] paramArrayOfDouble, int paramInt) {
//		if (this.n == 1)
//			return;
//		double[] arrayOfDouble = new double[this.n];
//		int i6 = 2 * this.n;
//		int i1 = (int) this.wtable_r[(1 + i6)];
//		int k = 1;
//		int j = this.n;
//		int i3 = i6 - 1;
//		for (int i7 = 1; i7 <= i1; ++i7) {
//			int l = i1 - i7;
//			int i2 = (int) this.wtable_r[(l + 2 + i6)];
//			int i = j / i2;
//			int i4 = this.n / j;
//			int i5 = i4 * i;
//			i3 -= (i2 - 1) * i4;
//			k = 1 - k;
//			switch (i2) {
//			case 2:
//				if (k == 0)
//					radf2(i4, i, paramArrayOfDouble, paramInt, arrayOfDouble,
//							0, i3);
//				else
//					radf2(i4, i, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt, i3);
//				break;
//			case 3:
//				if (k == 0)
//					radf3(i4, i, paramArrayOfDouble, paramInt, arrayOfDouble,
//							0, i3);
//				else
//					radf3(i4, i, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt, i3);
//				break;
//			case 4:
//				if (k == 0)
//					radf4(i4, i, paramArrayOfDouble, paramInt, arrayOfDouble,
//							0, i3);
//				else
//					radf4(i4, i, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt, i3);
//				break;
//			case 5:
//				if (k == 0)
//					radf5(i4, i, paramArrayOfDouble, paramInt, arrayOfDouble,
//							0, i3);
//				else
//					radf5(i4, i, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt, i3);
//				break;
//			default:
//				if (i4 == 1)
//					k = 1 - k;
//				if (k == 0) {
//					radfg(i4, i2, i, i5, paramArrayOfDouble, paramInt,
//							arrayOfDouble, 0, i3);
//					k = 1;
//				} else {
//					radfg(i4, i2, i, i5, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt, i3);
//					k = 0;
//				}
//			}
//			j = i;
//		}
//		if (k == 1)
//			return;
//		System.arraycopy(arrayOfDouble, 0, paramArrayOfDouble, paramInt, this.n);
//	}
//
//	void rfftb(double[] paramArrayOfDouble, int paramInt) {
//		if (this.n == 1)
//			return;
//		double[] arrayOfDouble = new double[this.n];
//		int i5 = 2 * this.n;
//		int l = (int) this.wtable_r[(1 + i5)];
//		int k = 0;
//		int i = 1;
//		int i2 = this.n;
//		for (int i6 = 1; i6 <= l; ++i6) {
//			int i1 = (int) this.wtable_r[(i6 + 1 + i5)];
//			int j = i1 * i;
//			int i3 = this.n / j;
//			int i4 = i3 * i;
//			switch (i1) {
//			case 2:
//				if (k == 0)
//					radb2(i3, i, paramArrayOfDouble, paramInt, arrayOfDouble,
//							0, i2);
//				else
//					radb2(i3, i, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt, i2);
//				k = 1 - k;
//				break;
//			case 3:
//				if (k == 0)
//					radb3(i3, i, paramArrayOfDouble, paramInt, arrayOfDouble,
//							0, i2);
//				else
//					radb3(i3, i, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt, i2);
//				k = 1 - k;
//				break;
//			case 4:
//				if (k == 0)
//					radb4(i3, i, paramArrayOfDouble, paramInt, arrayOfDouble,
//							0, i2);
//				else
//					radb4(i3, i, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt, i2);
//				k = 1 - k;
//				break;
//			case 5:
//				if (k == 0)
//					radb5(i3, i, paramArrayOfDouble, paramInt, arrayOfDouble,
//							0, i2);
//				else
//					radb5(i3, i, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt, i2);
//				k = 1 - k;
//				break;
//			default:
//				if (k == 0)
//					radbg(i3, i1, i, i4, paramArrayOfDouble, paramInt,
//							arrayOfDouble, 0, i2);
//				else
//					radbg(i3, i1, i, i4, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt, i2);
//				if (i3 == 1)
//					k = 1 - k;
//			}
//			i = j;
//			i2 += (i1 - 1) * i3;
//		}
//		if (k == 0)
//			return;
//		System.arraycopy(arrayOfDouble, 0, paramArrayOfDouble, paramInt, this.n);
//	}
//
//	void radf2(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5) {
//		int i4 = paramInt5;
//		int k = paramInt2 * paramInt1;
//		int l = 2 * paramInt1;
//		int i8;
//		int i9;
//		int i10;
//		int i11;
//		for (int i5 = 0; i5 < paramInt2; ++i5) {
//			i8 = paramInt4 + i5 * l;
//			i9 = i8 + l - 1;
//			i10 = paramInt3 + i5 * paramInt1;
//			i11 = i10 + k;
//			double d5 = paramArrayOfDouble1[i10];
//			double d7 = paramArrayOfDouble1[i11];
//			paramArrayOfDouble2[i8] = (d5 + d7);
//			paramArrayOfDouble2[i9] = (d5 - d7);
//		}
//		if (paramInt1 < 2)
//			return;
//		if (paramInt1 != 2) {
//			for (int i6 = 0; i6 < paramInt2; ++i6) {
//				l = i6 * paramInt1;
//				i1 = 2 * l;
//				int i2 = i1 + paramInt1;
//				int i3 = l + k;
//				for (int i = 2; i < paramInt1; i += 2) {
//					int j = paramInt1 - i;
//					i8 = i - 1 + i4;
//					i9 = paramInt4 + i + i1;
//					i10 = paramInt4 + j + i2;
//					i11 = paramInt3 + i + l;
//					int i12 = paramInt3 + i + i3;
//					double d6 = paramArrayOfDouble1[(i11 - 1)];
//					double d8 = paramArrayOfDouble1[i11];
//					double d9 = paramArrayOfDouble1[(i12 - 1)];
//					double d10 = paramArrayOfDouble1[i12];
//					double d3 = this.wtable_r[(i8 - 1)];
//					double d4 = this.wtable_r[i8];
//					double d2 = d3 * d9 + d4 * d10;
//					double d1 = d3 * d10 - (d4 * d9);
//					paramArrayOfDouble2[i9] = (d8 + d1);
//					paramArrayOfDouble2[(i9 - 1)] = (d6 + d2);
//					paramArrayOfDouble2[i10] = (d1 - d8);
//					paramArrayOfDouble2[(i10 - 1)] = (d6 - d2);
//				}
//			}
//			if (paramInt1 % 2 == 1)
//				return;
//		}
//		int i1 = 2 * l;
//		for (int i7 = 0; i7 < paramInt2; ++i7) {
//			l = i7 * paramInt1;
//			i8 = paramInt4 + i1 + paramInt1;
//			i9 = paramInt3 + paramInt1 - 1 + l;
//			paramArrayOfDouble2[i8] = (-paramArrayOfDouble1[(i9 + k)]);
//			paramArrayOfDouble2[(i8 - 1)] = paramArrayOfDouble1[i9];
//		}
//	}
//
//	void radb2(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5) {
//		int k = paramInt5;
//		int l = paramInt2 * paramInt1;
//		int i4;
//		int i5;
//		int i6;
//		int i7;
//		int i8;
//		int i9;
//		for (int i1 = 0; i1 < paramInt2; ++i1) {
//			i4 = i1 * paramInt1;
//			i5 = 2 * i4;
//			i6 = i5 + paramInt1;
//			i7 = paramInt4 + i4;
//			i8 = paramInt3 + i5;
//			i9 = paramInt3 + paramInt1 - 1 + i6;
//			double d5 = paramArrayOfDouble1[i8];
//			double d6 = paramArrayOfDouble1[i9];
//			paramArrayOfDouble2[i7] = (d5 + d6);
//			paramArrayOfDouble2[(i7 + l)] = (d5 - d6);
//		}
//		if (paramInt1 < 2)
//			return;
//		if (paramInt1 != 2) {
//			for (int i2 = 0; i2 < paramInt2; ++i2) {
//				i4 = i2 * paramInt1;
//				i5 = 2 * i4;
//				i6 = i5 + paramInt1;
//				i7 = i4 + l;
//				for (int i = 2; i < paramInt1; i += 2) {
//					int j = paramInt1 - i;
//					i8 = i - 1 + k;
//					i9 = paramInt4 + i;
//					int i10 = paramInt3 + i;
//					int i11 = paramInt3 + j;
//					double d3 = this.wtable_r[(i8 - 1)];
//					double d4 = this.wtable_r[i8];
//					int i12 = i10 + i5;
//					int i13 = i11 + i6;
//					int i14 = i9 + i4;
//					int i15 = i9 + i7;
//					double d2 = paramArrayOfDouble1[(i12 - 1)]
//							- paramArrayOfDouble1[(i13 - 1)];
//					double d1 = paramArrayOfDouble1[i12]
//							+ paramArrayOfDouble1[i13];
//					double d7 = paramArrayOfDouble1[i12];
//					double d8 = paramArrayOfDouble1[(i12 - 1)];
//					double d9 = paramArrayOfDouble1[i13];
//					double d10 = paramArrayOfDouble1[(i13 - 1)];
//					paramArrayOfDouble2[(i14 - 1)] = (d8 + d10);
//					paramArrayOfDouble2[i14] = (d7 - d9);
//					paramArrayOfDouble2[(i15 - 1)] = (d3 * d2 - (d4 * d1));
//					paramArrayOfDouble2[i15] = (d3 * d1 + d4 * d2);
//				}
//			}
//			if (paramInt1 % 2 == 1)
//				return;
//		}
//		for (int i3 = 0; i3 < paramInt2; ++i3) {
//			i4 = i3 * paramInt1;
//			i5 = 2 * i4;
//			i6 = paramInt4 + paramInt1 - 1 + i4;
//			i7 = paramInt3 + i5 + paramInt1;
//			paramArrayOfDouble2[i6] = (2.0D * paramArrayOfDouble1[(i7 - 1)]);
//			paramArrayOfDouble2[(i6 + l)] = (-2.0D * paramArrayOfDouble1[i7]);
//		}
//	}
//
//	void radf3(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5) {
//		int k = paramInt5;
//		int l = k + paramInt1;
//		int i1 = paramInt2 * paramInt1;
//		int i4;
//		int i5;
//		int i6;
//		int i7;
//		int i8;
//		int i9;
//		double d4;
//		for (int i2 = 0; i2 < paramInt2; ++i2) {
//			i4 = i2 * paramInt1;
//			i5 = 2 * i1;
//			i6 = (3 * i2 + 1) * paramInt1;
//			i7 = paramInt3 + i4;
//			i8 = i7 + i1;
//			i9 = i7 + i5;
//			double d15 = paramArrayOfDouble1[i7];
//			double d16 = paramArrayOfDouble1[i8];
//			double d17 = paramArrayOfDouble1[i9];
//			d4 = d16 + d17;
//			paramArrayOfDouble2[(paramInt4 + 3 * i4)] = (d15 + d4);
//			paramArrayOfDouble2[(paramInt4 + i6 + paramInt1)] = (0.8660254037844387D * (d17 - d16));
//			paramArrayOfDouble2[(paramInt4 + paramInt1 - 1 + i6)] = (d15 + -0.5D
//					* d4);
//		}
//		if (paramInt1 == 1)
//			return;
//		for (int i3 = 0; i3 < paramInt2; ++i3) {
//			i4 = i3 * paramInt1;
//			i5 = 3 * i4;
//			i6 = i4 + i1;
//			i7 = i6 + i1;
//			i8 = i5 + paramInt1;
//			i9 = i8 + paramInt1;
//			for (int i = 2; i < paramInt1; i += 2) {
//				int j = paramInt1 - i;
//				int i10 = i - 1 + k;
//				int i11 = i - 1 + l;
//				double d11 = this.wtable_r[(i10 - 1)];
//				double d13 = this.wtable_r[i10];
//				double d12 = this.wtable_r[(i11 - 1)];
//				double d14 = this.wtable_r[i11];
//				int i12 = paramInt3 + i;
//				int i13 = paramInt4 + i;
//				int i14 = paramInt4 + j;
//				int i15 = i12 + i4;
//				int i16 = i12 + i6;
//				int i17 = i12 + i7;
//				double d18 = paramArrayOfDouble1[(i15 - 1)];
//				double d19 = paramArrayOfDouble1[i15];
//				double d20 = paramArrayOfDouble1[(i16 - 1)];
//				double d21 = paramArrayOfDouble1[i16];
//				double d22 = paramArrayOfDouble1[(i17 - 1)];
//				double d23 = paramArrayOfDouble1[i17];
//				double d5 = d11 * d20 + d13 * d21;
//				double d2 = d11 * d21 - (d13 * d20);
//				double d6 = d12 * d22 + d14 * d23;
//				double d3 = d12 * d23 - (d14 * d22);
//				d4 = d5 + d6;
//				double d1 = d2 + d3;
//				double d9 = d18 + -0.5D * d4;
//				double d7 = d19 + -0.5D * d1;
//				double d10 = 0.8660254037844387D * (d2 - d3);
//				double d8 = 0.8660254037844387D * (d6 - d5);
//				int i18 = i13 + i5;
//				int i19 = i14 + i8;
//				int i20 = i13 + i9;
//				paramArrayOfDouble2[(i18 - 1)] = (d18 + d4);
//				paramArrayOfDouble2[i18] = (d19 + d1);
//				paramArrayOfDouble2[(i19 - 1)] = (d9 - d10);
//				paramArrayOfDouble2[i19] = (d8 - d7);
//				paramArrayOfDouble2[(i20 - 1)] = (d9 + d10);
//				paramArrayOfDouble2[i20] = (d7 + d8);
//			}
//		}
//	}
//
//	void radb3(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5) {
//		int k = paramInt5;
//		int l = k + paramInt1;
//		int i4;
//		int i5;
//		double d10;
//		double d5;
//		double d2;
//		for (int i1 = 0; i1 < paramInt2; ++i1) {
//			int i2 = i1 * paramInt1;
//			i4 = paramInt3 + 3 * i2;
//			i5 = i4 + 2 * paramInt1;
//			double d15 = paramArrayOfDouble1[i4];
//			d10 = 2.0D * paramArrayOfDouble1[(i5 - 1)];
//			d5 = d15 + -0.5D * d10;
//			d2 = 1.732050807568877D * paramArrayOfDouble1[i5];
//			paramArrayOfDouble2[(paramInt4 + i2)] = (d15 + d10);
//			paramArrayOfDouble2[(paramInt4 + (i1 + paramInt2) * paramInt1)] = (d5 - d2);
//			paramArrayOfDouble2[(paramInt4 + (i1 + 2 * paramInt2) * paramInt1)] = (d5 + d2);
//		}
//		if (paramInt1 == 1)
//			return;
//		i1 = paramInt2 * paramInt1;
//		for (int i3 = 0; i3 < paramInt2; ++i3) {
//			i4 = i3 * paramInt1;
//			i5 = 3 * i4;
//			int i6 = i5 + paramInt1;
//			int i7 = i6 + paramInt1;
//			int i8 = i4 + i1;
//			int i9 = i8 + i1;
//			for (int i = 2; i < paramInt1; i += 2) {
//				int j = paramInt1 - i;
//				int i10 = paramInt3 + i;
//				int i11 = paramInt3 + j;
//				int i12 = paramInt4 + i;
//				int i13 = i10 + i5;
//				int i14 = i10 + i7;
//				int i15 = i11 + i6;
//				double d16 = paramArrayOfDouble1[(i13 - 1)];
//				double d17 = paramArrayOfDouble1[i13];
//				double d18 = paramArrayOfDouble1[(i14 - 1)];
//				double d19 = paramArrayOfDouble1[i14];
//				double d20 = paramArrayOfDouble1[(i15 - 1)];
//				double d21 = paramArrayOfDouble1[i15];
//				d10 = d18 + d20;
//				d5 = d16 + -0.5D * d10;
//				double d9 = d19 - d21;
//				double d1 = d17 + -0.5D * d9;
//				double d6 = 0.8660254037844387D * (d18 - d20);
//				d2 = 0.8660254037844387D * (d19 + d21);
//				double d7 = d5 - d2;
//				double d8 = d5 + d2;
//				double d3 = d1 + d6;
//				double d4 = d1 - d6;
//				int i16 = i - 1 + k;
//				int i17 = i - 1 + l;
//				double d11 = this.wtable_r[(i16 - 1)];
//				double d13 = this.wtable_r[i16];
//				double d12 = this.wtable_r[(i17 - 1)];
//				double d14 = this.wtable_r[i17];
//				int i18 = i12 + i4;
//				int i19 = i12 + i8;
//				int i20 = i12 + i9;
//				paramArrayOfDouble2[(i18 - 1)] = (d16 + d10);
//				paramArrayOfDouble2[i18] = (d17 + d9);
//				paramArrayOfDouble2[(i19 - 1)] = (d11 * d7 - (d13 * d3));
//				paramArrayOfDouble2[i19] = (d11 * d3 + d13 * d7);
//				paramArrayOfDouble2[(i20 - 1)] = (d12 * d8 - (d14 * d4));
//				paramArrayOfDouble2[i20] = (d12 * d4 + d14 * d8);
//			}
//		}
//	}
//
//	void radf4(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5) {
//		int k = paramInt5;
//		int l = paramInt5 + paramInt1;
//		int i1 = l + paramInt1;
//		int i2 = paramInt2 * paramInt1;
//		int i6;
//		int i7;
//		int i8;
//		int i9;
//		int i10;
//		int i11;
//		double d11;
//		double d12;
//		int i20;
//		int i21;
//		for (int i3 = 0; i3 < paramInt2; ++i3) {
//			i6 = i3 * paramInt1;
//			i7 = 4 * i6;
//			i8 = i6 + i2;
//			i9 = i8 + i2;
//			i10 = i9 + i2;
//			i11 = i7 + paramInt1;
//			double d21 = paramArrayOfDouble1[(paramInt3 + i6)];
//			double d22 = paramArrayOfDouble1[(paramInt3 + i8)];
//			double d23 = paramArrayOfDouble1[(paramInt3 + i9)];
//			double d25 = paramArrayOfDouble1[(paramInt3 + i10)];
//			d11 = d22 + d25;
//			d12 = d21 + d23;
//			i20 = paramInt4 + i7;
//			i21 = paramInt4 + i11 + paramInt1;
//			paramArrayOfDouble2[i20] = (d11 + d12);
//			paramArrayOfDouble2[(i21 - 1 + paramInt1 + paramInt1)] = (d12 - d11);
//			paramArrayOfDouble2[(i21 - 1)] = (d21 - d23);
//			paramArrayOfDouble2[i21] = (d25 - d22);
//		}
//		if (paramInt1 < 2)
//			return;
//		int i12;
//		int i13;
//		int i14;
//		int i15;
//		double d7;
//		if (paramInt1 != 2) {
//			for (int i4 = 0; i4 < paramInt2; ++i4) {
//				i6 = i4 * paramInt1;
//				i7 = i6 + i2;
//				i8 = i7 + i2;
//				i9 = i8 + i2;
//				i10 = 4 * i6;
//				i11 = i10 + paramInt1;
//				i12 = i11 + paramInt1;
//				i13 = i12 + paramInt1;
//				for (int i = 2; i < paramInt1; i += 2) {
//					int j = paramInt1 - i;
//					i14 = i - 1 + k;
//					i15 = i - 1 + l;
//					int i16 = i - 1 + i1;
//					double d15 = this.wtable_r[(i14 - 1)];
//					double d16 = this.wtable_r[i14];
//					double d17 = this.wtable_r[(i15 - 1)];
//					double d18 = this.wtable_r[i15];
//					double d19 = this.wtable_r[(i16 - 1)];
//					double d20 = this.wtable_r[i16];
//					int i17 = paramInt3 + i;
//					int i18 = paramInt4 + i;
//					int i19 = paramInt4 + j;
//					i20 = i17 + i6;
//					i21 = i17 + i7;
//					int i22 = i17 + i8;
//					int i23 = i17 + i9;
//					double d29 = paramArrayOfDouble1[(i20 - 1)];
//					double d30 = paramArrayOfDouble1[i20];
//					double d31 = paramArrayOfDouble1[(i21 - 1)];
//					double d32 = paramArrayOfDouble1[i21];
//					double d33 = paramArrayOfDouble1[(i22 - 1)];
//					double d34 = paramArrayOfDouble1[i22];
//					double d35 = paramArrayOfDouble1[(i23 - 1)];
//					double d36 = paramArrayOfDouble1[i23];
//					double d4 = d15 * d31 + d16 * d32;
//					double d1 = d15 * d32 - (d16 * d31);
//					double d5 = d17 * d33 + d18 * d34;
//					double d2 = d17 * d34 - (d18 * d33);
//					double d6 = d19 * d35 + d20 * d36;
//					double d3 = d19 * d36 - (d20 * d35);
//					d11 = d4 + d6;
//					double d14 = d6 - d4;
//					d7 = d1 + d3;
//					double d10 = d1 - d3;
//					double d8 = d30 + d2;
//					double d9 = d30 - d2;
//					d12 = d29 + d5;
//					double d13 = d29 - d5;
//					int i24 = i18 + i10;
//					int i25 = i19 + i11;
//					int i26 = i18 + i12;
//					int i27 = i19 + i13;
//					paramArrayOfDouble2[(i24 - 1)] = (d11 + d12);
//					paramArrayOfDouble2[(i27 - 1)] = (d12 - d11);
//					paramArrayOfDouble2[i24] = (d7 + d8);
//					paramArrayOfDouble2[i27] = (d7 - d8);
//					paramArrayOfDouble2[(i26 - 1)] = (d10 + d13);
//					paramArrayOfDouble2[(i25 - 1)] = (d13 - d10);
//					paramArrayOfDouble2[i26] = (d14 + d9);
//					paramArrayOfDouble2[i25] = (d14 - d9);
//				}
//			}
//			if (paramInt1 % 2 == 1)
//				return;
//		}
//		for (int i5 = 0; i5 < paramInt2; ++i5) {
//			i6 = i5 * paramInt1;
//			i7 = 4 * i6;
//			i8 = i6 + i2;
//			i9 = i8 + i2;
//			i10 = i9 + i2;
//			i11 = i7 + paramInt1;
//			i12 = i11 + paramInt1;
//			i13 = i12 + paramInt1;
//			i14 = paramInt3 + paramInt1;
//			i15 = paramInt4 + paramInt1;
//			double d24 = paramArrayOfDouble1[(i14 - 1 + i6)];
//			double d26 = paramArrayOfDouble1[(i14 - 1 + i8)];
//			double d27 = paramArrayOfDouble1[(i14 - 1 + i9)];
//			double d28 = paramArrayOfDouble1[(i14 - 1 + i10)];
//			d7 = -0.7071067811865476D * (d26 + d28);
//			d11 = 0.7071067811865476D * (d26 - d28);
//			paramArrayOfDouble2[(i15 - 1 + i7)] = (d11 + d24);
//			paramArrayOfDouble2[(i15 - 1 + i12)] = (d24 - d11);
//			paramArrayOfDouble2[(paramInt4 + i11)] = (d7 - d27);
//			paramArrayOfDouble2[(paramInt4 + i13)] = (d7 + d27);
//		}
//	}
//
//	void radb4(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5) {
//		int k = paramInt5;
//		int l = k + paramInt1;
//		int i1 = l + paramInt1;
//		int i2 = paramInt2 * paramInt1;
//		int i6;
//		int i7;
//		int i8;
//		int i9;
//		int i10;
//		int i11;
//		int i12;
//		int i13;
//		double d11;
//		double d12;
//		double d13;
//		double d14;
//		for (int i3 = 0; i3 < paramInt2; ++i3) {
//			i6 = i3 * paramInt1;
//			i7 = 4 * i6;
//			i8 = i6 + i2;
//			i9 = i8 + i2;
//			i10 = i9 + i2;
//			i11 = i7 + paramInt1;
//			i12 = i11 + paramInt1;
//			i13 = i12 + paramInt1;
//			double d21 = paramArrayOfDouble1[(paramInt3 + i7)];
//			double d22 = paramArrayOfDouble1[(paramInt3 + i12)];
//			double d24 = paramArrayOfDouble1[(paramInt3 + paramInt1 - 1 + i13)];
//			double d26 = paramArrayOfDouble1[(paramInt3 + paramInt1 - 1 + i11)];
//			d11 = d21 - d24;
//			d12 = d21 + d24;
//			d13 = d26 + d26;
//			d14 = d22 + d22;
//			paramArrayOfDouble2[(paramInt4 + i6)] = (d12 + d13);
//			paramArrayOfDouble2[(paramInt4 + i8)] = (d11 - d14);
//			paramArrayOfDouble2[(paramInt4 + i9)] = (d12 - d13);
//			paramArrayOfDouble2[(paramInt4 + i10)] = (d11 + d14);
//		}
//		if (paramInt1 < 2)
//			return;
//		int i14;
//		int i15;
//		double d7;
//		double d8;
//		if (paramInt1 != 2) {
//			for (int i4 = 0; i4 < paramInt2; ++i4) {
//				i6 = i4 * paramInt1;
//				i7 = i6 + i2;
//				i8 = i7 + i2;
//				i9 = i8 + i2;
//				i10 = 4 * i6;
//				i11 = i10 + paramInt1;
//				i12 = i11 + paramInt1;
//				i13 = i12 + paramInt1;
//				for (int i = 2; i < paramInt1; i += 2) {
//					int j = paramInt1 - i;
//					i14 = i - 1 + k;
//					i15 = i - 1 + l;
//					int i16 = i - 1 + i1;
//					double d15 = this.wtable_r[(i14 - 1)];
//					double d16 = this.wtable_r[i14];
//					double d17 = this.wtable_r[(i15 - 1)];
//					double d18 = this.wtable_r[i15];
//					double d19 = this.wtable_r[(i16 - 1)];
//					double d20 = this.wtable_r[i16];
//					int i17 = paramInt3 + i;
//					int i18 = paramInt3 + j;
//					int i19 = paramInt4 + i;
//					int i20 = i17 + i10;
//					int i21 = i18 + i11;
//					int i22 = i17 + i12;
//					int i23 = i18 + i13;
//					double d29 = paramArrayOfDouble1[(i20 - 1)];
//					double d30 = paramArrayOfDouble1[i20];
//					double d31 = paramArrayOfDouble1[(i21 - 1)];
//					double d32 = paramArrayOfDouble1[i21];
//					double d33 = paramArrayOfDouble1[(i22 - 1)];
//					double d34 = paramArrayOfDouble1[i22];
//					double d35 = paramArrayOfDouble1[(i23 - 1)];
//					double d36 = paramArrayOfDouble1[i23];
//					d7 = d30 + d36;
//					d8 = d30 - d36;
//					double d9 = d34 - d32;
//					d14 = d34 + d32;
//					d11 = d29 - d35;
//					d12 = d29 + d35;
//					double d10 = d33 - d31;
//					d13 = d33 + d31;
//					double d5 = d12 - d13;
//					double d2 = d8 - d9;
//					double d4 = d11 - d14;
//					double d6 = d11 + d14;
//					double d1 = d7 + d10;
//					double d3 = d7 - d10;
//					int i24 = i19 + i6;
//					int i25 = i19 + i7;
//					int i26 = i19 + i8;
//					int i27 = i19 + i9;
//					paramArrayOfDouble2[(i24 - 1)] = (d12 + d13);
//					paramArrayOfDouble2[i24] = (d8 + d9);
//					paramArrayOfDouble2[(i25 - 1)] = (d15 * d4 - (d16 * d1));
//					paramArrayOfDouble2[i25] = (d15 * d1 + d16 * d4);
//					paramArrayOfDouble2[(i26 - 1)] = (d17 * d5 - (d18 * d2));
//					paramArrayOfDouble2[i26] = (d17 * d2 + d18 * d5);
//					paramArrayOfDouble2[(i27 - 1)] = (d19 * d6 - (d20 * d3));
//					paramArrayOfDouble2[i27] = (d19 * d3 + d20 * d6);
//				}
//			}
//			if (paramInt1 % 2 == 1)
//				return;
//		}
//		for (int i5 = 0; i5 < paramInt2; ++i5) {
//			i6 = i5 * paramInt1;
//			i7 = 4 * i6;
//			i8 = i6 + i2;
//			i9 = i8 + i2;
//			i10 = i9 + i2;
//			i11 = i7 + paramInt1;
//			i12 = i11 + paramInt1;
//			i13 = i12 + paramInt1;
//			i14 = paramInt3 + paramInt1;
//			i15 = paramInt4 + paramInt1;
//			double d23 = paramArrayOfDouble1[(i14 - 1 + i7)];
//			double d25 = paramArrayOfDouble1[(i14 - 1 + i12)];
//			double d27 = paramArrayOfDouble1[(paramInt3 + i11)];
//			double d28 = paramArrayOfDouble1[(paramInt3 + i13)];
//			d7 = d27 + d28;
//			d8 = d28 - d27;
//			d11 = d23 - d25;
//			d12 = d23 + d25;
//			paramArrayOfDouble2[(i15 - 1 + i6)] = (d12 + d12);
//			paramArrayOfDouble2[(i15 - 1 + i8)] = (1.414213562373095D * (d11 - d7));
//			paramArrayOfDouble2[(i15 - 1 + i9)] = (d8 + d8);
//			paramArrayOfDouble2[(i15 - 1 + i10)] = (-1.414213562373095D * (d11 + d7));
//		}
//	}
//
//	void radf5(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5) {
//		int k = paramInt5;
//		int l = k + paramInt1;
//		int i1 = l + paramInt1;
//		int i2 = i1 + paramInt1;
//		int i3 = paramInt2 * paramInt1;
//		int i6;
//		int i7;
//		int i8;
//		int i9;
//		int i10;
//		int i11;
//		int i12;
//		int i13;
//		int i14;
//		int i15;
//		int i16;
//		double d9;
//		double d4;
//		double d10;
//		double d3;
//		for (int i4 = 0; i4 < paramInt2; ++i4) {
//			i6 = i4 * paramInt1;
//			i7 = 5 * i6;
//			i8 = i7 + paramInt1;
//			i9 = i8 + paramInt1;
//			i10 = i9 + paramInt1;
//			i11 = i10 + paramInt1;
//			i12 = i6 + i3;
//			i13 = i12 + i3;
//			i14 = i13 + i3;
//			i15 = i14 + i3;
//			i16 = paramInt4 + paramInt1 - 1;
//			double d33 = paramArrayOfDouble1[(paramInt3 + i6)];
//			double d34 = paramArrayOfDouble1[(paramInt3 + i12)];
//			double d35 = paramArrayOfDouble1[(paramInt3 + i13)];
//			double d36 = paramArrayOfDouble1[(paramInt3 + i14)];
//			double d37 = paramArrayOfDouble1[(paramInt3 + i15)];
//			d9 = d37 + d34;
//			d4 = d37 - d34;
//			d10 = d36 + d35;
//			d3 = d36 - d35;
//			paramArrayOfDouble2[(paramInt4 + i7)] = (d33 + d9 + d10);
//			paramArrayOfDouble2[(i16 + i8)] = (d33 + 0.3090169943749475D * d9 + -0.8090169943749473D
//					* d10);
//			paramArrayOfDouble2[(paramInt4 + i9)] = (0.9510565162951535D * d4 + 0.5877852522924733D * d3);
//			paramArrayOfDouble2[(i16 + i10)] = (d33 + -0.8090169943749473D * d9 + 0.3090169943749475D * d10);
//			paramArrayOfDouble2[(paramInt4 + i11)] = (0.5877852522924733D * d4 - (0.9510565162951535D * d3));
//		}
//		if (paramInt1 == 1)
//			return;
//		for (int i5 = 0; i5 < paramInt2; ++i5) {
//			i6 = i5 * paramInt1;
//			i7 = 5 * i6;
//			i8 = i7 + paramInt1;
//			i9 = i8 + paramInt1;
//			i10 = i9 + paramInt1;
//			i11 = i10 + paramInt1;
//			i12 = i6 + i3;
//			i13 = i12 + i3;
//			i14 = i13 + i3;
//			i15 = i14 + i3;
//			for (int i = 2; i < paramInt1; i += 2) {
//				i16 = i - 1 + k;
//				int i17 = i - 1 + l;
//				int i18 = i - 1 + i1;
//				int i19 = i - 1 + i2;
//				double d25 = this.wtable_r[(i16 - 1)];
//				double d26 = this.wtable_r[i16];
//				double d27 = this.wtable_r[(i17 - 1)];
//				double d28 = this.wtable_r[i17];
//				double d29 = this.wtable_r[(i18 - 1)];
//				double d30 = this.wtable_r[i18];
//				double d31 = this.wtable_r[(i19 - 1)];
//				double d32 = this.wtable_r[i19];
//				int j = paramInt1 - i;
//				int i20 = paramInt3 + i;
//				int i21 = paramInt4 + i;
//				int i22 = paramInt4 + j;
//				int i23 = i20 + i6;
//				int i24 = i20 + i12;
//				int i25 = i20 + i13;
//				int i26 = i20 + i14;
//				int i27 = i20 + i15;
//				double d38 = paramArrayOfDouble1[(i23 - 1)];
//				double d39 = paramArrayOfDouble1[i23];
//				double d40 = paramArrayOfDouble1[(i24 - 1)];
//				double d41 = paramArrayOfDouble1[i24];
//				double d42 = paramArrayOfDouble1[(i25 - 1)];
//				double d43 = paramArrayOfDouble1[i25];
//				double d44 = paramArrayOfDouble1[(i26 - 1)];
//				double d45 = paramArrayOfDouble1[i26];
//				double d46 = paramArrayOfDouble1[(i27 - 1)];
//				double d47 = paramArrayOfDouble1[i27];
//				double d11 = d25 * d40 + d26 * d41;
//				double d2 = d25 * d41 - (d26 * d40);
//				double d12 = d27 * d42 + d28 * d43;
//				double d5 = d27 * d43 - (d28 * d42);
//				double d13 = d29 * d44 + d30 * d45;
//				double d6 = d29 * d45 - (d30 * d44);
//				double d14 = d31 * d46 + d32 * d47;
//				double d7 = d31 * d47 - (d32 * d46);
//				d9 = d11 + d14;
//				d4 = d14 - d11;
//				double d15 = d2 - d7;
//				double d1 = d2 + d7;
//				d10 = d12 + d13;
//				d3 = d13 - d12;
//				double d16 = d5 - d6;
//				double d8 = d5 + d6;
//				double d21 = d38 + 0.3090169943749475D * d9
//						+ -0.8090169943749473D * d10;
//				double d17 = d39 + 0.3090169943749475D * d1
//						+ -0.8090169943749473D * d8;
//				double d22 = d38 + -0.8090169943749473D * d9
//						+ 0.3090169943749475D * d10;
//				double d18 = d39 + -0.8090169943749473D * d1
//						+ 0.3090169943749475D * d8;
//				double d24 = 0.9510565162951535D * d15 + 0.5877852522924733D
//						* d16;
//				double d19 = 0.9510565162951535D * d4 + 0.5877852522924733D
//						* d3;
//				double d23 = 0.5877852522924733D * d15
//						- (0.9510565162951535D * d16);
//				double d20 = 0.5877852522924733D * d4
//						- (0.9510565162951535D * d3);
//				int i28 = i21 + i7;
//				int i29 = i22 + i8;
//				int i30 = i21 + i9;
//				int i31 = i22 + i10;
//				int i32 = i21 + i11;
//				paramArrayOfDouble2[(i28 - 1)] = (d38 + d9 + d10);
//				paramArrayOfDouble2[i28] = (d39 + d1 + d8);
//				paramArrayOfDouble2[(i30 - 1)] = (d21 + d24);
//				paramArrayOfDouble2[(i29 - 1)] = (d21 - d24);
//				paramArrayOfDouble2[i30] = (d17 + d19);
//				paramArrayOfDouble2[i29] = (d19 - d17);
//				paramArrayOfDouble2[(i32 - 1)] = (d22 + d23);
//				paramArrayOfDouble2[(i31 - 1)] = (d22 - d23);
//				paramArrayOfDouble2[i32] = (d18 + d20);
//				paramArrayOfDouble2[i31] = (d20 - d18);
//			}
//		}
//	}
//
//	void radb5(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5) {
//		int k = paramInt5;
//		int l = k + paramInt1;
//		int i1 = l + paramInt1;
//		int i2 = i1 + paramInt1;
//		int i3 = paramInt2 * paramInt1;
//		int i6;
//		int i7;
//		int i8;
//		int i9;
//		int i10;
//		int i11;
//		int i12;
//		int i13;
//		int i14;
//		int i15;
//		int i16;
//		double d16;
//		double d15;
//		double d21;
//		double d22;
//		double d9;
//		double d10;
//		double d4;
//		double d3;
//		for (int i4 = 0; i4 < paramInt2; ++i4) {
//			i6 = i4 * paramInt1;
//			i7 = 5 * i6;
//			i8 = i7 + paramInt1;
//			i9 = i8 + paramInt1;
//			i10 = i9 + paramInt1;
//			i11 = i10 + paramInt1;
//			i12 = i6 + i3;
//			i13 = i12 + i3;
//			i14 = i13 + i3;
//			i15 = i14 + i3;
//			i16 = paramInt3 + paramInt1 - 1;
//			double d33 = paramArrayOfDouble1[(paramInt3 + i7)];
//			d16 = 2.0D * paramArrayOfDouble1[(paramInt3 + i9)];
//			d15 = 2.0D * paramArrayOfDouble1[(paramInt3 + i11)];
//			d21 = 2.0D * paramArrayOfDouble1[(i16 + i8)];
//			d22 = 2.0D * paramArrayOfDouble1[(i16 + i10)];
//			d9 = d33 + 0.3090169943749475D * d21 + -0.8090169943749473D * d22;
//			d10 = d33 + -0.8090169943749473D * d21 + 0.3090169943749475D * d22;
//			d4 = 0.9510565162951535D * d16 + 0.5877852522924733D * d15;
//			d3 = 0.5877852522924733D * d16 - (0.9510565162951535D * d15);
//			paramArrayOfDouble2[(paramInt4 + i6)] = (d33 + d21 + d22);
//			paramArrayOfDouble2[(paramInt4 + i12)] = (d9 - d4);
//			paramArrayOfDouble2[(paramInt4 + i13)] = (d10 - d3);
//			paramArrayOfDouble2[(paramInt4 + i14)] = (d10 + d3);
//			paramArrayOfDouble2[(paramInt4 + i15)] = (d9 + d4);
//		}
//		if (paramInt1 == 1)
//			return;
//		for (int i5 = 0; i5 < paramInt2; ++i5) {
//			i6 = i5 * paramInt1;
//			i7 = 5 * i6;
//			i8 = i7 + paramInt1;
//			i9 = i8 + paramInt1;
//			i10 = i9 + paramInt1;
//			i11 = i10 + paramInt1;
//			i12 = i6 + i3;
//			i13 = i12 + i3;
//			i14 = i13 + i3;
//			i15 = i14 + i3;
//			for (int i = 2; i < paramInt1; i += 2) {
//				int j = paramInt1 - i;
//				i16 = i - 1 + k;
//				int i17 = i - 1 + l;
//				int i18 = i - 1 + i1;
//				int i19 = i - 1 + i2;
//				double d25 = this.wtable_r[(i16 - 1)];
//				double d26 = this.wtable_r[i16];
//				double d27 = this.wtable_r[(i17 - 1)];
//				double d28 = this.wtable_r[i17];
//				double d29 = this.wtable_r[(i18 - 1)];
//				double d30 = this.wtable_r[i18];
//				double d31 = this.wtable_r[(i19 - 1)];
//				double d32 = this.wtable_r[i19];
//				int i20 = paramInt3 + i;
//				int i21 = paramInt3 + j;
//				int i22 = paramInt4 + i;
//				int i23 = i20 + i7;
//				int i24 = i21 + i8;
//				int i25 = i20 + i9;
//				int i26 = i21 + i10;
//				int i27 = i20 + i11;
//				double d34 = paramArrayOfDouble1[(i23 - 1)];
//				double d35 = paramArrayOfDouble1[i23];
//				double d36 = paramArrayOfDouble1[(i24 - 1)];
//				double d37 = paramArrayOfDouble1[i24];
//				double d38 = paramArrayOfDouble1[(i25 - 1)];
//				double d39 = paramArrayOfDouble1[i25];
//				double d40 = paramArrayOfDouble1[(i26 - 1)];
//				double d41 = paramArrayOfDouble1[i26];
//				double d42 = paramArrayOfDouble1[(i27 - 1)];
//				double d43 = paramArrayOfDouble1[i27];
//				d16 = d39 + d37;
//				double d13 = d39 - d37;
//				d15 = d43 + d41;
//				double d14 = d43 - d41;
//				double d24 = d38 - d36;
//				d21 = d38 + d36;
//				double d23 = d42 - d40;
//				d22 = d42 + d40;
//				d9 = d34 + 0.3090169943749475D * d21 + -0.8090169943749473D
//						* d22;
//				double d1 = d35 + 0.3090169943749475D * d13
//						+ -0.8090169943749473D * d14;
//				d10 = d34 + -0.8090169943749473D * d21 + 0.3090169943749475D
//						* d22;
//				double d2 = d35 + -0.8090169943749473D * d13
//						+ 0.3090169943749475D * d14;
//				double d11 = 0.9510565162951535D * d24 + 0.5877852522924733D
//						* d23;
//				d4 = 0.9510565162951535D * d16 + 0.5877852522924733D * d15;
//				double d12 = 0.5877852522924733D * d24
//						- (0.9510565162951535D * d23);
//				d3 = 0.5877852522924733D * d16 - (0.9510565162951535D * d15);
//				double d17 = d10 - d3;
//				double d18 = d10 + d3;
//				double d5 = d2 + d12;
//				double d6 = d2 - d12;
//				double d19 = d9 + d4;
//				double d20 = d9 - d4;
//				double d7 = d1 - d11;
//				double d8 = d1 + d11;
//				int i28 = i22 + i6;
//				int i29 = i22 + i12;
//				int i30 = i22 + i13;
//				int i31 = i22 + i14;
//				int i32 = i22 + i15;
//				paramArrayOfDouble2[(i28 - 1)] = (d34 + d21 + d22);
//				paramArrayOfDouble2[i28] = (d35 + d13 + d14);
//				paramArrayOfDouble2[(i29 - 1)] = (d25 * d20 - (d26 * d8));
//				paramArrayOfDouble2[i29] = (d25 * d8 + d26 * d20);
//				paramArrayOfDouble2[(i30 - 1)] = (d27 * d17 - (d28 * d5));
//				paramArrayOfDouble2[i30] = (d27 * d5 + d28 * d17);
//				paramArrayOfDouble2[(i31 - 1)] = (d29 * d18 - (d30 * d6));
//				paramArrayOfDouble2[i31] = (d29 * d6 + d30 * d18);
//				paramArrayOfDouble2[(i32 - 1)] = (d31 * d19 - (d32 * d7));
//				paramArrayOfDouble2[i32] = (d31 * d7 + d32 * d19);
//			}
//		}
//	}
//
//	void radfg(int paramInt1, int paramInt2, int paramInt3, int paramInt4,
//			double[] paramArrayOfDouble1, int paramInt5,
//			double[] paramArrayOfDouble2, int paramInt6, int paramInt7) {
//		int i5 = paramInt7;
//		double d8 = 6.283185307179586D / paramInt2;
//		double d7 = Math.cos(d8);
//		double d9 = Math.sin(d8);
//		int j = (paramInt2 + 1) / 2;
//		int i4 = (paramInt1 - 1) / 2;
//		int i29;
//		int i33;
//		int i40;
//		int i45;
//		int i19;
//		int i1;
//		int i50;
//		int i51;
//		double d21;
//		double d22;
//		int i36;
//		if (paramInt1 != 1) {
//			for (int i6 = 0; i6 < paramInt4; ++i6)
//				paramArrayOfDouble2[(paramInt6 + i6)] = paramArrayOfDouble1[(paramInt5 + i6)];
//			int i24;
//			for (int i7 = 1; i7 < paramInt2; ++i7) {
//				i13 = i7 * paramInt3 * paramInt1;
//				for (int i17 = 0; i17 < paramInt3; ++i17) {
//					i24 = i17 * paramInt1 + i13;
//					paramArrayOfDouble2[(paramInt6 + i24)] = paramArrayOfDouble1[(paramInt5 + i24)];
//				}
//			}
//			int i3;
//			int i;
//			double d12;
//			double d13;
//			int i34;
//			double d16;
//			if (i4 <= paramInt3) {
//				i3 = -paramInt1;
//				for (int i8 = 1; i8 < paramInt2; ++i8) {
//					i3 += paramInt1;
//					i = i3 - 1;
//					i13 = i8 * paramInt3 * paramInt1;
//					for (int i18 = 2; i18 < paramInt1; i18 += 2) {
//						i24 = (i += 2) + i5;
//						i29 = paramInt5 + i18;
//						i33 = paramInt6 + i18;
//						d12 = this.wtable_r[(i24 - 1)];
//						d13 = this.wtable_r[i24];
//						for (i34 = 0; i34 < paramInt3; ++i34) {
//							i40 = i34 * paramInt1 + i13;
//							int i42 = i33 + i40;
//							i45 = i29 + i40;
//							d16 = paramArrayOfDouble1[(i45 - 1)];
//							double d18 = paramArrayOfDouble1[i45];
//							paramArrayOfDouble2[(i42 - 1)] = (d12 * d16 + d13
//									* d18);
//							paramArrayOfDouble2[i42] = (d12 * d18 - (d13 * d16));
//						}
//					}
//				}
//			} else {
//				i3 = -paramInt1;
//				for (int i9 = 1; i9 < paramInt2; ++i9) {
//					i3 += paramInt1;
//					i13 = i9 * paramInt3 * paramInt1;
//					for (i19 = 0; i19 < paramInt3; ++i19) {
//						i = i3 - 1;
//						i24 = i19 * paramInt1 + i13;
//						for (i29 = 2; i29 < paramInt1; i29 += 2) {
//							i33 = (i += 2) + i5;
//							d12 = this.wtable_r[(i33 - 1)];
//							d13 = this.wtable_r[i33];
//							i34 = paramInt6 + i29 + i24;
//							i40 = paramInt5 + i29 + i24;
//							double d14 = paramArrayOfDouble1[(i40 - 1)];
//							d16 = paramArrayOfDouble1[i40];
//							paramArrayOfDouble2[(i34 - 1)] = (d12 * d14 + d13
//									* d16);
//							paramArrayOfDouble2[i34] = (d12 * d16 - (d13 * d14));
//						}
//					}
//				}
//			}
//			int i43;
//			int i47;
//			double d19;
//			double d20;
//			if (i4 >= paramInt3)
//				for (int i10 = 1; i10 < j; ++i10) {
//					i1 = paramInt2 - i10;
//					i13 = i10 * paramInt3 * paramInt1;
//					i19 = i1 * paramInt3 * paramInt1;
//					for (int i25 = 0; i25 < paramInt3; ++i25) {
//						i29 = i25 * paramInt1 + i13;
//						i33 = i25 * paramInt1 + i19;
//						for (int i35 = 2; i35 < paramInt1; i35 += 2) {
//							i40 = paramInt5 + i35;
//							i43 = paramInt6 + i35;
//							i45 = i40 + i29;
//							i47 = i40 + i33;
//							i50 = i43 + i29;
//							i51 = i43 + i33;
//							d19 = paramArrayOfDouble2[(i50 - 1)];
//							d20 = paramArrayOfDouble2[i50];
//							d21 = paramArrayOfDouble2[(i51 - 1)];
//							d22 = paramArrayOfDouble2[i51];
//							paramArrayOfDouble1[(i45 - 1)] = (d19 + d21);
//							paramArrayOfDouble1[i45] = (d20 + d22);
//							paramArrayOfDouble1[(i47 - 1)] = (d20 - d22);
//							paramArrayOfDouble1[i47] = (d21 - d19);
//						}
//					}
//				}
//			else
//				for (int i11 = 1; i11 < j; ++i11) {
//					i1 = paramInt2 - i11;
//					i13 = i11 * paramInt3 * paramInt1;
//					i19 = i1 * paramInt3 * paramInt1;
//					for (int i26 = 2; i26 < paramInt1; i26 += 2) {
//						i29 = paramInt5 + i26;
//						i33 = paramInt6 + i26;
//						for (i36 = 0; i36 < paramInt3; ++i36) {
//							i40 = i36 * paramInt1 + i13;
//							i43 = i36 * paramInt1 + i19;
//							i45 = i29 + i40;
//							i47 = i29 + i43;
//							i50 = i33 + i40;
//							i51 = i33 + i43;
//							d19 = paramArrayOfDouble2[(i50 - 1)];
//							d20 = paramArrayOfDouble2[i50];
//							d21 = paramArrayOfDouble2[(i51 - 1)];
//							d22 = paramArrayOfDouble2[i51];
//							paramArrayOfDouble1[(i45 - 1)] = (d19 + d21);
//							paramArrayOfDouble1[i45] = (d20 + d22);
//							paramArrayOfDouble1[(i47 - 1)] = (d20 - d22);
//							paramArrayOfDouble1[i47] = (d21 - d19);
//						}
//					}
//				}
//		} else {
//			System.arraycopy(paramArrayOfDouble2, paramInt6,
//					paramArrayOfDouble1, paramInt5, paramInt4);
//		}
//		int i27;
//		for (int i12 = 1; i12 < j; ++i12) {
//			i1 = paramInt2 - i12;
//			i13 = i12 * paramInt3 * paramInt1;
//			i19 = i1 * paramInt3 * paramInt1;
//			for (i27 = 0; i27 < paramInt3; ++i27) {
//				i29 = i27 * paramInt1 + i13;
//				i33 = i27 * paramInt1 + i19;
//				i36 = paramInt6 + i29;
//				i40 = paramInt6 + i33;
//				double d15 = paramArrayOfDouble2[i36];
//				double d17 = paramArrayOfDouble2[i40];
//				paramArrayOfDouble1[(paramInt5 + i29)] = (d15 + d17);
//				paramArrayOfDouble1[(paramInt5 + i33)] = (d17 - d15);
//			}
//		}
//		double d4 = 1.0D;
//		double d2 = 0.0D;
//		i12 = (paramInt2 - 1) * paramInt4;
//		int i41;
//		int i44;
//		for (int i13 = 1; i13 < j; ++i13) {
//			int i2 = paramInt2 - i13;
//			double d10 = d7 * d4 - (d9 * d2);
//			d2 = d7 * d2 + d9 * d4;
//			d4 = d10;
//			i19 = i13 * paramInt4;
//			i27 = i2 * paramInt4;
//			for (int i30 = 0; i30 < paramInt4; ++i30) {
//				i33 = paramInt6 + i30;
//				i36 = paramInt5 + i30;
//				paramArrayOfDouble2[(i33 + i19)] = (paramArrayOfDouble1[i36] + d4
//						* paramArrayOfDouble1[(i36 + paramInt4)]);
//				paramArrayOfDouble2[(i33 + i27)] = (d2 * paramArrayOfDouble1[(i36 + i12)]);
//			}
//			double d1 = d4;
//			double d6 = d2;
//			double d5 = d4;
//			double d3 = d2;
//			for (int i31 = 2; i31 < j; ++i31) {
//				i1 = paramInt2 - i31;
//				double d11 = d1 * d5 - (d6 * d3);
//				d3 = d1 * d3 + d6 * d5;
//				d5 = d11;
//				i33 = i31 * paramInt4;
//				i36 = i1 * paramInt4;
//				for (i41 = 0; i41 < paramInt4; ++i41) {
//					i44 = paramInt6 + i41;
//					i45 = paramInt5 + i41;
//					paramArrayOfDouble2[(i44 + i19)] += d5
//							* paramArrayOfDouble1[(i45 + i33)];
//					paramArrayOfDouble2[(i44 + i27)] += d3
//							* paramArrayOfDouble1[(i45 + i36)];
//				}
//			}
//		}
//		int i28;
//		for (int i14 = 1; i14 < j; ++i14) {
//			i19 = i14 * paramInt4;
//			for (i28 = 0; i28 < paramInt4; ++i28)
//				paramArrayOfDouble2[(paramInt6 + i28)] += paramArrayOfDouble1[(paramInt5
//						+ i28 + i19)];
//		}
//		int i32;
//		if (paramInt1 >= paramInt3)
//			for (int i15 = 0; i15 < paramInt3; ++i15) {
//				i19 = i15 * paramInt1;
//				i28 = i19 * paramInt2;
//				for (i32 = 0; i32 < paramInt1; ++i32)
//					paramArrayOfDouble1[(paramInt5 + i32 + i28)] = paramArrayOfDouble2[(paramInt6
//							+ i32 + i19)];
//			}
//		else
//			for (i16 = 0; i16 < paramInt1; ++i16)
//				for (int i20 = 0; i20 < paramInt3; ++i20) {
//					i28 = i20 * paramInt1;
//					paramArrayOfDouble1[(paramInt5 + i16 + i28 * paramInt2)] = paramArrayOfDouble2[(paramInt6
//							+ i16 + i28)];
//				}
//		int i16 = paramInt2 * paramInt1;
//		int k;
//		int i48;
//		for (int i21 = 1; i21 < j; ++i21) {
//			i1 = paramInt2 - i21;
//			k = 2 * i21;
//			i28 = i21 * paramInt3 * paramInt1;
//			i32 = i1 * paramInt3 * paramInt1;
//			i33 = k * paramInt1;
//			for (int i37 = 0; i37 < paramInt3; ++i37) {
//				i41 = i37 * paramInt1;
//				i44 = i41 + i28;
//				i45 = i41 + i32;
//				i48 = i37 * i16;
//				paramArrayOfDouble1[(paramInt5 + paramInt1 - 1 + i33
//						- paramInt1 + i48)] = paramArrayOfDouble2[(paramInt6 + i44)];
//				paramArrayOfDouble1[(paramInt5 + i33 + i48)] = paramArrayOfDouble2[(paramInt6 + i45)];
//			}
//		}
//		if (paramInt1 == 1)
//			return;
//		int i46;
//		int l;
//		int i52;
//		int i53;
//		int i54;
//		int i55;
//		double d23;
//		double d24;
//		if (i4 >= paramInt3)
//			for (int i22 = 1; i22 < j; ++i22) {
//				i1 = paramInt2 - i22;
//				k = 2 * i22;
//				i28 = i22 * paramInt3 * paramInt1;
//				i32 = i1 * paramInt3 * paramInt1;
//				i33 = k * paramInt1;
//				for (int i38 = 0; i38 < paramInt3; ++i38) {
//					i41 = i38 * i16;
//					i44 = i38 * paramInt1;
//					for (i46 = 2; i46 < paramInt1; i46 += 2) {
//						l = paramInt1 - i46;
//						i48 = paramInt5 + i46;
//						i50 = paramInt5 + l;
//						i51 = paramInt6 + i46;
//						i52 = i48 + i33 + i41;
//						i53 = i50 + i33 - paramInt1 + i41;
//						i54 = i51 + i44 + i28;
//						i55 = i51 + i44 + i32;
//						d21 = paramArrayOfDouble2[(i54 - 1)];
//						d22 = paramArrayOfDouble2[i54];
//						d23 = paramArrayOfDouble2[(i55 - 1)];
//						d24 = paramArrayOfDouble2[i55];
//						paramArrayOfDouble1[(i52 - 1)] = (d21 + d23);
//						paramArrayOfDouble1[(i53 - 1)] = (d21 - d23);
//						paramArrayOfDouble1[i52] = (d22 + d24);
//						paramArrayOfDouble1[i53] = (d24 - d22);
//					}
//				}
//			}
//		else
//			for (int i23 = 1; i23 < j; ++i23) {
//				i1 = paramInt2 - i23;
//				k = 2 * i23;
//				i28 = i23 * paramInt3 * paramInt1;
//				i32 = i1 * paramInt3 * paramInt1;
//				i33 = k * paramInt1;
//				for (int i39 = 2; i39 < paramInt1; i39 += 2) {
//					l = paramInt1 - i39;
//					i41 = paramInt5 + i39;
//					i44 = paramInt5 + l;
//					i46 = paramInt6 + i39;
//					for (int i49 = 0; i49 < paramInt3; ++i49) {
//						i50 = i49 * i16;
//						i51 = i49 * paramInt1;
//						i52 = i41 + i33 + i50;
//						i53 = i44 + i33 - paramInt1 + i50;
//						i54 = i46 + i51 + i28;
//						i55 = i46 + i51 + i32;
//						d21 = paramArrayOfDouble2[(i54 - 1)];
//						d22 = paramArrayOfDouble2[i54];
//						d23 = paramArrayOfDouble2[(i55 - 1)];
//						d24 = paramArrayOfDouble2[i55];
//						paramArrayOfDouble1[(i52 - 1)] = (d21 + d23);
//						paramArrayOfDouble1[(i53 - 1)] = (d21 - d23);
//						paramArrayOfDouble1[i52] = (d22 + d24);
//						paramArrayOfDouble1[i53] = (d24 - d22);
//					}
//				}
//			}
//	}
//
//	void radbg(int paramInt1, int paramInt2, int paramInt3, int paramInt4,
//			double[] paramArrayOfDouble1, int paramInt5,
//			double[] paramArrayOfDouble2, int paramInt6, int paramInt7) {
//		int i5 = paramInt7;
//		double d10 = 6.283185307179586D / paramInt2;
//		double d9 = Math.cos(d10);
//		double d11 = Math.sin(d10);
//		int i4 = (paramInt1 - 1) / 2;
//		int j = (paramInt2 + 1) / 2;
//		int i6 = paramInt2 * paramInt1;
//		int i21;
//		if (paramInt1 >= paramInt3)
//			for (int i7 = 0; i7 < paramInt3; ++i7) {
//				i9 = i7 * paramInt1;
//				i12 = i7 * i6;
//				for (int i20 = 0; i20 < paramInt1; ++i20)
//					paramArrayOfDouble2[(paramInt6 + i20 + i9)] = paramArrayOfDouble1[(paramInt5
//							+ i20 + i12)];
//			}
//		else
//			for (i8 = 0; i8 < paramInt1; ++i8) {
//				i9 = paramInt6 + i8;
//				i12 = paramInt5 + i8;
//				for (i21 = 0; i21 < paramInt3; ++i21)
//					paramArrayOfDouble2[(i9 + i21 * paramInt1)] = paramArrayOfDouble1[(i12 + i21
//							* i6)];
//			}
//		int i8 = paramInt5 + paramInt1 - 1;
//		int i1;
//		int i22;
//		int i35;
//		int i37;
//		int i39;
//		int i42;
//		for (int i9 = 1; i9 < j; ++i9) {
//			i1 = paramInt2 - i9;
//			int k = 2 * i9;
//			i12 = i9 * paramInt3 * paramInt1;
//			i21 = i1 * paramInt3 * paramInt1;
//			i22 = k * paramInt1;
//			for (int i27 = 0; i27 < paramInt3; ++i27) {
//				i35 = i27 * paramInt1;
//				i37 = i35 * paramInt2;
//				i39 = i8 + i22 + i37 - paramInt1;
//				i42 = paramInt5 + i22 + i37;
//				double d14 = paramArrayOfDouble1[i39];
//				double d16 = paramArrayOfDouble1[i42];
//				paramArrayOfDouble2[(paramInt6 + i35 + i12)] = (d14 + d14);
//				paramArrayOfDouble2[(paramInt6 + i35 + i21)] = (d16 + d16);
//			}
//		}
//		int i44;
//		int i46;
//		int i49;
//		int i43;
//		if (paramInt1 != 1) {
//			int l;
//			int i47;
//			int i50;
//			int i51;
//			int i52;
//			double d21;
//			double d23;
//			double d25;
//			double d26;
//			if (i4 >= paramInt3)
//				for (int i10 = 1; i10 < j; ++i10) {
//					i1 = paramInt2 - i10;
//					i12 = i10 * paramInt3 * paramInt1;
//					i21 = i1 * paramInt3 * paramInt1;
//					i22 = 2 * i10 * paramInt1;
//					for (int i28 = 0; i28 < paramInt3; ++i28) {
//						i35 = i28 * paramInt1 + i12;
//						i37 = i28 * paramInt1 + i21;
//						i39 = i28 * paramInt2 * paramInt1 + i22;
//						for (i42 = 2; i42 < paramInt1; i42 += 2) {
//							l = paramInt1 - i42;
//							i44 = paramInt6 + i42;
//							i46 = paramInt5 + l;
//							i47 = paramInt5 + i42;
//							i49 = i44 + i35;
//							i50 = i44 + i37;
//							i51 = i47 + i39;
//							i52 = i46 + i39 - paramInt1;
//							d21 = paramArrayOfDouble1[(i51 - 1)];
//							d23 = paramArrayOfDouble1[i51];
//							d25 = paramArrayOfDouble1[(i52 - 1)];
//							d26 = paramArrayOfDouble1[i52];
//							paramArrayOfDouble2[(i49 - 1)] = (d21 + d25);
//							paramArrayOfDouble2[(i50 - 1)] = (d21 - d25);
//							paramArrayOfDouble2[i49] = (d23 - d26);
//							paramArrayOfDouble2[i50] = (d23 + d26);
//						}
//					}
//				}
//			else
//				for (i11 = 1; i11 < j; ++i11) {
//					i1 = paramInt2 - i11;
//					i12 = i11 * paramInt3 * paramInt1;
//					i21 = i1 * paramInt3 * paramInt1;
//					i22 = 2 * i11 * paramInt1;
//					for (int i29 = 2; i29 < paramInt1; i29 += 2) {
//						l = paramInt1 - i29;
//						i35 = paramInt6 + i29;
//						i37 = paramInt5 + l;
//						i39 = paramInt5 + i29;
//						for (i43 = 0; i43 < paramInt3; ++i43) {
//							i44 = i43 * paramInt1 + i12;
//							i46 = i43 * paramInt1 + i21;
//							i47 = i43 * paramInt2 * paramInt1 + i22;
//							i49 = i35 + i44;
//							i50 = i35 + i46;
//							i51 = i39 + i47;
//							i52 = i37 + i47 - paramInt1;
//							d21 = paramArrayOfDouble1[(i51 - 1)];
//							d23 = paramArrayOfDouble1[i51];
//							d25 = paramArrayOfDouble1[(i52 - 1)];
//							d26 = paramArrayOfDouble1[i52];
//							paramArrayOfDouble2[(i49 - 1)] = (d21 + d25);
//							paramArrayOfDouble2[(i50 - 1)] = (d21 - d25);
//							paramArrayOfDouble2[i49] = (d23 - d26);
//							paramArrayOfDouble2[i50] = (d23 + d26);
//						}
//					}
//				}
//		}
//		double d4 = 1.0D;
//		double d2 = 0.0D;
//		int i11 = (paramInt2 - 1) * paramInt4;
//		int i31;
//		for (int i12 = 1; i12 < j; ++i12) {
//			int i2 = paramInt2 - i12;
//			double d12 = d9 * d4 - (d11 * d2);
//			d2 = d9 * d2 + d11 * d4;
//			d4 = d12;
//			i21 = i12 * paramInt4;
//			i22 = i2 * paramInt4;
//			for (int i30 = 0; i30 < paramInt4; ++i30) {
//				i35 = paramInt5 + i30;
//				i37 = paramInt6 + i30;
//				paramArrayOfDouble1[(i35 + i21)] = (paramArrayOfDouble2[i37] + d4
//						* paramArrayOfDouble2[(i37 + paramInt4)]);
//				paramArrayOfDouble1[(i35 + i22)] = (d2 * paramArrayOfDouble2[(i37 + i11)]);
//			}
//			double d1 = d4;
//			double d6 = d2;
//			double d5 = d4;
//			double d3 = d2;
//			for (i31 = 2; i31 < j; ++i31) {
//				i1 = paramInt2 - i31;
//				double d13 = d1 * d5 - (d6 * d3);
//				d3 = d1 * d3 + d6 * d5;
//				d5 = d13;
//				i35 = i31 * paramInt4;
//				i37 = i1 * paramInt4;
//				for (i39 = 0; i39 < paramInt4; ++i39) {
//					i43 = paramInt5 + i39;
//					i44 = paramInt6 + i39;
//					paramArrayOfDouble1[(i43 + i21)] += d5
//							* paramArrayOfDouble2[(i44 + i35)];
//					paramArrayOfDouble1[(i43 + i22)] += d3
//							* paramArrayOfDouble2[(i44 + i37)];
//				}
//			}
//		}
//		int i23;
//		for (int i13 = 1; i13 < j; ++i13) {
//			i21 = i13 * paramInt4;
//			for (i23 = 0; i23 < paramInt4; ++i23) {
//				i31 = paramInt6 + i23;
//				paramArrayOfDouble2[i31] += paramArrayOfDouble2[(i31 + i21)];
//			}
//		}
//		for (int i14 = 1; i14 < j; ++i14) {
//			i1 = paramInt2 - i14;
//			i21 = i14 * paramInt3 * paramInt1;
//			i23 = i1 * paramInt3 * paramInt1;
//			for (int i32 = 0; i32 < paramInt3; ++i32) {
//				i35 = i32 * paramInt1;
//				i37 = paramInt6 + i35;
//				i39 = paramInt5 + i35 + i21;
//				i43 = paramInt5 + i35 + i23;
//				double d15 = paramArrayOfDouble1[i39];
//				double d17 = paramArrayOfDouble1[i43];
//				paramArrayOfDouble2[(i37 + i21)] = (d15 - d17);
//				paramArrayOfDouble2[(i37 + i23)] = (d15 + d17);
//			}
//		}
//		if (paramInt1 == 1)
//			return;
//		int i38;
//		int i45;
//		int i48;
//		double d19;
//		double d20;
//		double d22;
//		double d24;
//		int i34;
//		if (i4 >= paramInt3)
//			for (int i15 = 1; i15 < j; ++i15) {
//				i1 = paramInt2 - i15;
//				i21 = i15 * paramInt3 * paramInt1;
//				i23 = i1 * paramInt3 * paramInt1;
//				for (int i33 = 0; i33 < paramInt3; ++i33) {
//					i35 = i33 * paramInt1;
//					for (i38 = 2; i38 < paramInt1; i38 += 2) {
//						i39 = paramInt6 + i38;
//						i43 = paramInt5 + i38;
//						i45 = i39 + i35 + i21;
//						i46 = i39 + i35 + i23;
//						i48 = i43 + i35 + i21;
//						i49 = i43 + i35 + i23;
//						d19 = paramArrayOfDouble1[(i48 - 1)];
//						d20 = paramArrayOfDouble1[i48];
//						d22 = paramArrayOfDouble1[(i49 - 1)];
//						d24 = paramArrayOfDouble1[i49];
//						paramArrayOfDouble2[(i45 - 1)] = (d19 - d24);
//						paramArrayOfDouble2[(i46 - 1)] = (d19 + d24);
//						paramArrayOfDouble2[i45] = (d20 + d22);
//						paramArrayOfDouble2[i46] = (d20 - d22);
//					}
//				}
//			}
//		else
//			for (int i16 = 1; i16 < j; ++i16) {
//				i1 = paramInt2 - i16;
//				i21 = i16 * paramInt3 * paramInt1;
//				i23 = i1 * paramInt3 * paramInt1;
//				for (i34 = 2; i34 < paramInt1; i34 += 2) {
//					i35 = paramInt6 + i34;
//					i38 = paramInt5 + i34;
//					for (int i40 = 0; i40 < paramInt3; ++i40) {
//						i43 = i40 * paramInt1;
//						i45 = i35 + i43 + i21;
//						i46 = i35 + i43 + i23;
//						i48 = i38 + i43 + i21;
//						i49 = i38 + i43 + i23;
//						d19 = paramArrayOfDouble1[(i48 - 1)];
//						d20 = paramArrayOfDouble1[i48];
//						d22 = paramArrayOfDouble1[(i49 - 1)];
//						d24 = paramArrayOfDouble1[i49];
//						paramArrayOfDouble2[(i45 - 1)] = (d19 - d24);
//						paramArrayOfDouble2[(i46 - 1)] = (d19 + d24);
//						paramArrayOfDouble2[i45] = (d20 + d22);
//						paramArrayOfDouble2[i46] = (d20 - d22);
//					}
//				}
//			}
//		System.arraycopy(paramArrayOfDouble2, paramInt6, paramArrayOfDouble1,
//				paramInt5, paramInt4);
//		for (int i17 = 1; i17 < paramInt2; ++i17) {
//			i21 = i17 * paramInt3 * paramInt1;
//			for (int i24 = 0; i24 < paramInt3; ++i24) {
//				i34 = i24 * paramInt1 + i21;
//				paramArrayOfDouble1[(paramInt5 + i34)] = paramArrayOfDouble2[(paramInt6 + i34)];
//			}
//		}
//		int i3;
//		int i;
//		double d7;
//		double d8;
//		int i41;
//		double d18;
//		if (i4 <= paramInt3) {
//			i3 = -paramInt1;
//			for (int i18 = 1; i18 < paramInt2; ++i18) {
//				i3 += paramInt1;
//				i = i3 - 1;
//				i21 = i18 * paramInt3 * paramInt1;
//				for (int i25 = 2; i25 < paramInt1; i25 += 2) {
//					i34 = (i += 2) + i5;
//					d7 = this.wtable_r[(i34 - 1)];
//					d8 = this.wtable_r[i34];
//					i35 = paramInt5 + i25;
//					i38 = paramInt6 + i25;
//					for (i41 = 0; i41 < paramInt3; ++i41) {
//						i43 = i41 * paramInt1 + i21;
//						i45 = i35 + i43;
//						i46 = i38 + i43;
//						d18 = paramArrayOfDouble2[(i46 - 1)];
//						d19 = paramArrayOfDouble2[i46];
//						paramArrayOfDouble1[(i45 - 1)] = (d7 * d18 - (d8 * d19));
//						paramArrayOfDouble1[i45] = (d7 * d19 + d8 * d18);
//					}
//				}
//			}
//		} else {
//			i3 = -paramInt1;
//			for (int i19 = 1; i19 < paramInt2; ++i19) {
//				i3 += paramInt1;
//				i21 = i19 * paramInt3 * paramInt1;
//				for (int i26 = 0; i26 < paramInt3; ++i26) {
//					i = i3 - 1;
//					i34 = i26 * paramInt1 + i21;
//					for (int i36 = 2; i36 < paramInt1; i36 += 2) {
//						i38 = (i += 2) + i5;
//						d7 = this.wtable_r[(i38 - 1)];
//						d8 = this.wtable_r[i38];
//						i41 = paramInt5 + i36;
//						i43 = paramInt6 + i36;
//						i45 = i41 + i34;
//						i46 = i43 + i34;
//						d18 = paramArrayOfDouble2[(i46 - 1)];
//						d19 = paramArrayOfDouble2[i46];
//						paramArrayOfDouble1[(i45 - 1)] = (d7 * d18 - (d8 * d19));
//						paramArrayOfDouble1[i45] = (d7 * d19 + d8 * d18);
//					}
//				}
//			}
//		}
//	}
//
//	void cfftf(double[] paramArrayOfDouble, int paramInt1, int paramInt2) {
//		int[] arrayOfInt = new int[1];
//		int i6 = 2 * this.n;
//		double[] arrayOfDouble = new double[i6];
//		int i7 = i6;
//		int i8 = 4 * this.n;
//		arrayOfInt[0] = 0;
//		int i1 = (int) this.wtable[(1 + i8)];
//		int l = 0;
//		int j = 1;
//		int i3 = i7;
//		for (int i9 = 2; i9 <= i1 + 1; ++i9) {
//			int i2 = (int) this.wtable[(i9 + i8)];
//			int k = i2 * j;
//			int i4 = this.n / k;
//			int i = i4 + i4;
//			int i5 = i * j;
//			switch (i2) {
//			case 4:
//				if (l == 0)
//					passf4(i, j, paramArrayOfDouble, paramInt1, arrayOfDouble,
//							0, i3, paramInt2);
//				else
//					passf4(i, j, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt1, i3, paramInt2);
//				l = 1 - l;
//				break;
//			case 2:
//				if (l == 0)
//					passf2(i, j, paramArrayOfDouble, paramInt1, arrayOfDouble,
//							0, i3, paramInt2);
//				else
//					passf2(i, j, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt1, i3, paramInt2);
//				l = 1 - l;
//				break;
//			case 3:
//				if (l == 0)
//					passf3(i, j, paramArrayOfDouble, paramInt1, arrayOfDouble,
//							0, i3, paramInt2);
//				else
//					passf3(i, j, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt1, i3, paramInt2);
//				l = 1 - l;
//				break;
//			case 5:
//				if (l == 0)
//					passf5(i, j, paramArrayOfDouble, paramInt1, arrayOfDouble,
//							0, i3, paramInt2);
//				else
//					passf5(i, j, arrayOfDouble, 0, paramArrayOfDouble,
//							paramInt1, i3, paramInt2);
//				l = 1 - l;
//				break;
//			default:
//				if (l == 0)
//					passfg(arrayOfInt, i, i2, j, i5, paramArrayOfDouble,
//							paramInt1, arrayOfDouble, 0, i3, paramInt2);
//				else
//					passfg(arrayOfInt, i, i2, j, i5, arrayOfDouble, 0,
//							paramArrayOfDouble, paramInt1, i3, paramInt2);
//				if (arrayOfInt[0] != 0)
//					l = 1 - l;
//			}
//			j = k;
//			i3 += (i2 - 1) * i;
//		}
//		if (l == 0)
//			return;
//		System.arraycopy(arrayOfDouble, 0, paramArrayOfDouble, paramInt1, i6);
//	}
//
//	void passf2(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5, int paramInt6) {
//		int i = paramInt5;
//		int j = paramInt1 * paramInt2;
//		int i3;
//		int i4;
//		int i7;
//		if (paramInt1 <= 2)
//			for (int k = 0; k < paramInt2; ++k) {
//				int i1 = k * paramInt1;
//				i3 = paramInt3 + 2 * i1;
//				i4 = i3 + paramInt1;
//				double d3 = paramArrayOfDouble1[i3];
//				double d5 = paramArrayOfDouble1[(i3 + 1)];
//				double d7 = paramArrayOfDouble1[i4];
//				double d9 = paramArrayOfDouble1[(i4 + 1)];
//				int i6 = paramInt4 + i1;
//				i7 = i6 + j;
//				paramArrayOfDouble2[i6] = (d3 + d7);
//				paramArrayOfDouble2[(i6 + 1)] = (d5 + d9);
//				paramArrayOfDouble2[i7] = (d3 - d7);
//				paramArrayOfDouble2[(i7 + 1)] = (d5 - d9);
//			}
//		else
//			for (int l = 0; l < paramInt2; ++l)
//				for (int i2 = 0; i2 < paramInt1 - 1; i2 += 2) {
//					i3 = l * paramInt1;
//					i4 = paramInt3 + i2 + 2 * i3;
//					int i5 = i4 + paramInt1;
//					double d4 = paramArrayOfDouble1[i4];
//					double d6 = paramArrayOfDouble1[(i4 + 1)];
//					double d8 = paramArrayOfDouble1[i5];
//					double d10 = paramArrayOfDouble1[(i5 + 1)];
//					i7 = i2 + i;
//					double d11 = this.wtable[i7];
//					double d12 = paramInt6 * this.wtable[(i7 + 1)];
//					double d2 = d4 - d8;
//					double d1 = d6 - d10;
//					int i8 = paramInt4 + i2 + i3;
//					int i9 = i8 + j;
//					paramArrayOfDouble2[i8] = (d4 + d8);
//					paramArrayOfDouble2[(i8 + 1)] = (d6 + d10);
//					paramArrayOfDouble2[i9] = (d11 * d2 - (d12 * d1));
//					paramArrayOfDouble2[(i9 + 1)] = (d11 * d1 + d12 * d2);
//				}
//	}
//
//	void passf3(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5, int paramInt6) {
//		int i = paramInt5;
//		int j = i + paramInt1;
//		int k = paramInt2 * paramInt1;
//		int i2;
//		int i3;
//		int i4;
//		double d10;
//		double d5;
//		double d9;
//		double d1;
//		double d6;
//		double d2;
//		if (paramInt1 == 2)
//			for (int l = 1; l <= paramInt2; ++l) {
//				i2 = paramInt3 + (3 * l - 2) * paramInt1;
//				i3 = i2 + paramInt1;
//				i4 = i2 - paramInt1;
//				double d11 = paramArrayOfDouble1[i2];
//				double d12 = paramArrayOfDouble1[(i2 + 1)];
//				double d14 = paramArrayOfDouble1[i3];
//				double d16 = paramArrayOfDouble1[(i3 + 1)];
//				double d18 = paramArrayOfDouble1[i4];
//				double d20 = paramArrayOfDouble1[(i4 + 1)];
//				d10 = d11 + d14;
//				d5 = d18 + -0.5D * d10;
//				d9 = d12 + d16;
//				d1 = d20 + -0.5D * d9;
//				d6 = paramInt6 * 0.8660254037844387D * (d11 - d14);
//				d2 = paramInt6 * 0.8660254037844387D * (d12 - d16);
//				int i8 = paramInt4 + (l - 1) * paramInt1;
//				int i9 = i8 + k;
//				int i10 = i9 + k;
//				paramArrayOfDouble2[i8] = (paramArrayOfDouble1[i4] + d10);
//				paramArrayOfDouble2[(i8 + 1)] = (d20 + d9);
//				paramArrayOfDouble2[i9] = (d5 - d2);
//				paramArrayOfDouble2[(i9 + 1)] = (d1 + d6);
//				paramArrayOfDouble2[i10] = (d5 + d2);
//				paramArrayOfDouble2[(i10 + 1)] = (d1 - d6);
//			}
//		else
//			for (int i1 = 1; i1 <= paramInt2; ++i1) {
//				i2 = paramInt3 + (3 * i1 - 2) * paramInt1;
//				i3 = paramInt4 + (i1 - 1) * paramInt1;
//				for (i4 = 0; i4 < paramInt1 - 1; i4 += 2) {
//					int i5 = i4 + i2;
//					int i6 = i5 + paramInt1;
//					int i7 = i5 - paramInt1;
//					double d13 = paramArrayOfDouble1[i5];
//					double d15 = paramArrayOfDouble1[(i5 + 1)];
//					double d17 = paramArrayOfDouble1[i6];
//					double d19 = paramArrayOfDouble1[(i6 + 1)];
//					double d21 = paramArrayOfDouble1[i7];
//					double d22 = paramArrayOfDouble1[(i7 + 1)];
//					d10 = d13 + d17;
//					d5 = d21 + -0.5D * d10;
//					d9 = d15 + d19;
//					d1 = d22 + -0.5D * d9;
//					d6 = paramInt6 * 0.8660254037844387D * (d13 - d17);
//					d2 = paramInt6 * 0.8660254037844387D * (d15 - d19);
//					double d7 = d5 - d2;
//					double d8 = d5 + d2;
//					double d3 = d1 + d6;
//					double d4 = d1 - d6;
//					int i11 = i4 + i;
//					int i12 = i4 + j;
//					double d23 = this.wtable[i11];
//					double d24 = paramInt6 * this.wtable[(i11 + 1)];
//					double d25 = this.wtable[i12];
//					double d26 = paramInt6 * this.wtable[(i12 + 1)];
//					int i13 = i4 + i3;
//					int i14 = i13 + k;
//					int i15 = i14 + k;
//					paramArrayOfDouble2[i13] = (d21 + d10);
//					paramArrayOfDouble2[(i13 + 1)] = (d22 + d9);
//					paramArrayOfDouble2[i14] = (d23 * d7 - (d24 * d3));
//					paramArrayOfDouble2[(i14 + 1)] = (d23 * d3 + d24 * d7);
//					paramArrayOfDouble2[i15] = (d25 * d8 - (d26 * d4));
//					paramArrayOfDouble2[(i15 + 1)] = (d25 * d4 + d26 * d8);
//				}
//			}
//	}
//
//	void passf4(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5, int paramInt6) {
//		int i = paramInt5;
//		int j = i + paramInt1;
//		int k = j + paramInt1;
//		int l = paramInt2 * paramInt1;
//		int i3;
//		int i4;
//		int i7;
//		int i8;
//		double d16;
//		double d17;
//		double d18;
//		double d19;
//		double d20;
//		double d21;
//		double d22;
//		double d7;
//		double d8;
//		double d14;
//		double d9;
//		double d11;
//		double d12;
//		double d10;
//		double d13;
//		int i13;
//		int i14;
//		if (paramInt1 == 2)
//			for (int i1 = 0; i1 < paramInt2; ++i1) {
//				i3 = i1 * paramInt1;
//				i4 = paramInt3 + 4 * i3 + 1;
//				int i5 = i4 + paramInt1;
//				i7 = i5 + paramInt1;
//				i8 = i7 + paramInt1;
//				double d15 = paramArrayOfDouble1[(i4 - 1)];
//				d16 = paramArrayOfDouble1[i4];
//				d17 = paramArrayOfDouble1[(i5 - 1)];
//				d18 = paramArrayOfDouble1[i5];
//				d19 = paramArrayOfDouble1[(i7 - 1)];
//				d20 = paramArrayOfDouble1[i7];
//				d21 = paramArrayOfDouble1[(i8 - 1)];
//				d22 = paramArrayOfDouble1[i8];
//				d7 = d16 - d20;
//				d8 = d16 + d20;
//				d14 = d22 - d18;
//				d9 = d18 + d22;
//				d11 = d15 - d19;
//				d12 = d15 + d19;
//				d10 = d17 - d21;
//				d13 = d17 + d21;
//				int i11 = paramInt4 + i3;
//				int i12 = i11 + l;
//				i13 = i12 + l;
//				i14 = i13 + l;
//				paramArrayOfDouble2[i11] = (d12 + d13);
//				paramArrayOfDouble2[(i11 + 1)] = (d8 + d9);
//				paramArrayOfDouble2[i12] = (d11 + paramInt6 * d14);
//				paramArrayOfDouble2[(i12 + 1)] = (d7 + paramInt6 * d10);
//				paramArrayOfDouble2[i13] = (d12 - d13);
//				paramArrayOfDouble2[(i13 + 1)] = (d8 - d9);
//				paramArrayOfDouble2[i14] = (d11 - (paramInt6 * d14));
//				paramArrayOfDouble2[(i14 + 1)] = (d7 - (paramInt6 * d10));
//			}
//		else
//			for (int i2 = 0; i2 < paramInt2; ++i2) {
//				i3 = i2 * paramInt1;
//				i4 = paramInt3 + 1 + 4 * i3;
//				for (int i6 = 0; i6 < paramInt1 - 1; i6 += 2) {
//					i7 = i6 + i4;
//					i8 = i7 + paramInt1;
//					int i9 = i8 + paramInt1;
//					int i10 = i9 + paramInt1;
//					d16 = paramArrayOfDouble1[(i7 - 1)];
//					d17 = paramArrayOfDouble1[i7];
//					d18 = paramArrayOfDouble1[(i8 - 1)];
//					d19 = paramArrayOfDouble1[i8];
//					d20 = paramArrayOfDouble1[(i9 - 1)];
//					d21 = paramArrayOfDouble1[i9];
//					d22 = paramArrayOfDouble1[(i10 - 1)];
//					double d23 = paramArrayOfDouble1[i10];
//					d7 = d17 - d21;
//					d8 = d17 + d21;
//					d9 = d19 + d23;
//					d14 = d23 - d19;
//					d11 = d16 - d20;
//					d12 = d16 + d20;
//					d10 = d18 - d22;
//					d13 = d18 + d22;
//					double d5 = d12 - d13;
//					double d2 = d8 - d9;
//					double d4 = d11 + paramInt6 * d14;
//					double d6 = d11 - (paramInt6 * d14);
//					double d1 = d7 + paramInt6 * d10;
//					double d3 = d7 - (paramInt6 * d10);
//					i13 = i6 + i;
//					i14 = i6 + j;
//					int i15 = i6 + k;
//					double d24 = this.wtable[i13];
//					double d25 = paramInt6 * this.wtable[(i13 + 1)];
//					double d26 = this.wtable[i14];
//					double d27 = paramInt6 * this.wtable[(i14 + 1)];
//					double d28 = this.wtable[i15];
//					double d29 = paramInt6 * this.wtable[(i15 + 1)];
//					int i16 = paramInt4 + i6 + i3;
//					int i17 = i16 + l;
//					int i18 = i17 + l;
//					int i19 = i18 + l;
//					paramArrayOfDouble2[i16] = (d12 + d13);
//					paramArrayOfDouble2[(i16 + 1)] = (d8 + d9);
//					paramArrayOfDouble2[i17] = (d24 * d4 - (d25 * d1));
//					paramArrayOfDouble2[(i17 + 1)] = (d24 * d1 + d25 * d4);
//					paramArrayOfDouble2[i18] = (d26 * d5 - (d27 * d2));
//					paramArrayOfDouble2[(i18 + 1)] = (d26 * d2 + d27 * d5);
//					paramArrayOfDouble2[i19] = (d28 * d6 - (d29 * d3));
//					paramArrayOfDouble2[(i19 + 1)] = (d28 * d3 + d29 * d6);
//				}
//			}
//	}
//
//	void passf5(int paramInt1, int paramInt2, double[] paramArrayOfDouble1,
//			int paramInt3, double[] paramArrayOfDouble2, int paramInt4,
//			int paramInt5, int paramInt6) {
//		int i = paramInt5;
//		int j = i + paramInt1;
//		int k = j + paramInt1;
//		int l = k + paramInt1;
//		int i1 = paramInt2 * paramInt1;
//		int i4;
//		int i5;
//		int i6;
//		int i7;
//		int i8;
//		double d16;
//		double d13;
//		double d15;
//		double d14;
//		double d24;
//		double d21;
//		double d23;
//		double d22;
//		double d9;
//		double d1;
//		double d10;
//		double d2;
//		double d11;
//		double d4;
//		double d12;
//		double d3;
//		int i15;
//		int i16;
//		if (paramInt1 == 2)
//			for (int i2 = 1; i2 <= paramInt2; ++i2) {
//				i4 = paramInt3 + (5 * i2 - 4) * paramInt1 + 1;
//				i5 = i4 + paramInt1;
//				i6 = i4 - paramInt1;
//				i7 = i5 + paramInt1;
//				i8 = i7 + paramInt1;
//				double d25 = paramArrayOfDouble1[(i4 - 1)];
//				double d26 = paramArrayOfDouble1[i4];
//				double d28 = paramArrayOfDouble1[(i5 - 1)];
//				double d30 = paramArrayOfDouble1[i5];
//				double d32 = paramArrayOfDouble1[(i6 - 1)];
//				double d34 = paramArrayOfDouble1[i6];
//				double d36 = paramArrayOfDouble1[(i7 - 1)];
//				double d38 = paramArrayOfDouble1[i7];
//				double d40 = paramArrayOfDouble1[(i8 - 1)];
//				double d42 = paramArrayOfDouble1[i8];
//				d16 = d26 - d42;
//				d13 = d26 + d42;
//				d15 = d30 - d38;
//				d14 = d30 + d38;
//				d24 = d25 - d40;
//				d21 = d25 + d40;
//				d23 = d28 - d36;
//				d22 = d28 + d36;
//				d9 = d32 + 0.3090169943749475D * d21 + -0.8090169943749473D
//						* d22;
//				d1 = d34 + 0.3090169943749475D * d13 + -0.8090169943749473D
//						* d14;
//				d10 = d32 + -0.8090169943749473D * d21 + 0.3090169943749475D
//						* d22;
//				d2 = d34 + -0.8090169943749473D * d13 + 0.3090169943749475D
//						* d14;
//				d11 = paramInt6
//						* (0.9510565162951535D * d24 + 0.5877852522924733D * d23);
//				d4 = paramInt6
//						* (0.9510565162951535D * d16 + 0.5877852522924733D * d15);
//				d12 = paramInt6
//						* (0.5877852522924733D * d24 - (0.9510565162951535D * d23));
//				d3 = paramInt6
//						* (0.5877852522924733D * d16 - (0.9510565162951535D * d15));
//				int i12 = paramInt4 + (i2 - 1) * paramInt1;
//				int i13 = i12 + i1;
//				int i14 = i13 + i1;
//				i15 = i14 + i1;
//				i16 = i15 + i1;
//				paramArrayOfDouble2[i12] = (d32 + d21 + d22);
//				paramArrayOfDouble2[(i12 + 1)] = (d34 + d13 + d14);
//				paramArrayOfDouble2[i13] = (d9 - d4);
//				paramArrayOfDouble2[(i13 + 1)] = (d1 + d11);
//				paramArrayOfDouble2[i14] = (d10 - d3);
//				paramArrayOfDouble2[(i14 + 1)] = (d2 + d12);
//				paramArrayOfDouble2[i15] = (d10 + d3);
//				paramArrayOfDouble2[(i15 + 1)] = (d2 - d12);
//				paramArrayOfDouble2[i16] = (d9 + d4);
//				paramArrayOfDouble2[(i16 + 1)] = (d1 - d11);
//			}
//		else
//			for (int i3 = 1; i3 <= paramInt2; ++i3) {
//				i4 = paramInt3 + 1 + (i3 * 5 - 4) * paramInt1;
//				i5 = paramInt4 + (i3 - 1) * paramInt1;
//				for (i6 = 0; i6 < paramInt1 - 1; i6 += 2) {
//					i7 = i6 + i4;
//					i8 = i7 + paramInt1;
//					int i9 = i7 - paramInt1;
//					int i10 = i8 + paramInt1;
//					int i11 = i10 + paramInt1;
//					double d27 = paramArrayOfDouble1[(i7 - 1)];
//					double d29 = paramArrayOfDouble1[i7];
//					double d31 = paramArrayOfDouble1[(i8 - 1)];
//					double d33 = paramArrayOfDouble1[i8];
//					double d35 = paramArrayOfDouble1[(i9 - 1)];
//					double d37 = paramArrayOfDouble1[i9];
//					double d39 = paramArrayOfDouble1[(i10 - 1)];
//					double d41 = paramArrayOfDouble1[i10];
//					double d43 = paramArrayOfDouble1[(i11 - 1)];
//					double d44 = paramArrayOfDouble1[i11];
//					d16 = d29 - d44;
//					d13 = d29 + d44;
//					d15 = d33 - d41;
//					d14 = d33 + d41;
//					d24 = d27 - d43;
//					d21 = d27 + d43;
//					d23 = d31 - d39;
//					d22 = d31 + d39;
//					d9 = d35 + 0.3090169943749475D * d21 + -0.8090169943749473D
//							* d22;
//					d1 = d37 + 0.3090169943749475D * d13 + -0.8090169943749473D
//							* d14;
//					d10 = d35 + -0.8090169943749473D * d21
//							+ 0.3090169943749475D * d22;
//					d2 = d37 + -0.8090169943749473D * d13 + 0.3090169943749475D
//							* d14;
//					d11 = paramInt6
//							* (0.9510565162951535D * d24 + 0.5877852522924733D * d23);
//					d4 = paramInt6
//							* (0.9510565162951535D * d16 + 0.5877852522924733D * d15);
//					d12 = paramInt6
//							* (0.5877852522924733D * d24 - (0.9510565162951535D * d23));
//					d3 = paramInt6
//							* (0.5877852522924733D * d16 - (0.9510565162951535D * d15));
//					double d17 = d10 - d3;
//					double d18 = d10 + d3;
//					double d5 = d2 + d12;
//					double d6 = d2 - d12;
//					double d19 = d9 + d4;
//					double d20 = d9 - d4;
//					double d7 = d1 - d11;
//					double d8 = d1 + d11;
//					i15 = i6 + i;
//					i16 = i6 + j;
//					int i17 = i6 + k;
//					int i18 = i6 + l;
//					double d45 = this.wtable[i15];
//					double d46 = paramInt6 * this.wtable[(i15 + 1)];
//					double d47 = this.wtable[i16];
//					double d48 = paramInt6 * this.wtable[(i16 + 1)];
//					double d49 = this.wtable[i17];
//					double d50 = paramInt6 * this.wtable[(i17 + 1)];
//					double d51 = this.wtable[i18];
//					double d52 = paramInt6 * this.wtable[(i18 + 1)];
//					int i19 = i6 + i5;
//					int i20 = i19 + i1;
//					int i21 = i20 + i1;
//					int i22 = i21 + i1;
//					int i23 = i22 + i1;
//					paramArrayOfDouble2[i19] = (d35 + d21 + d22);
//					paramArrayOfDouble2[(i19 + 1)] = (d37 + d13 + d14);
//					paramArrayOfDouble2[i20] = (d45 * d20 - (d46 * d8));
//					paramArrayOfDouble2[(i20 + 1)] = (d45 * d8 + d46 * d20);
//					paramArrayOfDouble2[i21] = (d47 * d17 - (d48 * d5));
//					paramArrayOfDouble2[(i21 + 1)] = (d47 * d5 + d48 * d17);
//					paramArrayOfDouble2[i22] = (d49 * d18 - (d50 * d6));
//					paramArrayOfDouble2[(i22 + 1)] = (d49 * d6 + d50 * d18);
//					paramArrayOfDouble2[i23] = (d51 * d19 - (d52 * d7));
//					paramArrayOfDouble2[(i23 + 1)] = (d51 * d7 + d52 * d19);
//				}
//			}
//	}
//
//	void passfg(int[] paramArrayOfInt, int paramInt1, int paramInt2,
//			int paramInt3, int paramInt4, double[] paramArrayOfDouble1,
//			int paramInt5, double[] paramArrayOfDouble2, int paramInt6,
//			int paramInt7, int paramInt8) {
//		int i8 = paramInt7;
//		int k = paramInt1 / 2;
//		int l = (paramInt2 + 1) / 2;
//		int i7 = paramInt2 * paramInt1;
//		int i2;
//		int i13;
//		int i17;
//		int i24;
//		int i31;
//		int i32;
//		int i34;
//		int i35;
//		int i38;
//		double d9;
//		int i22;
//		int i40;
//		double d8;
//		double d10;
//		int i14;
//		if (paramInt1 >= paramInt3) {
//			for (int i9 = 1; i9 < l; ++i9) {
//				i2 = paramInt2 - i9;
//				i13 = i9 * paramInt1;
//				i17 = i2 * paramInt1;
//				for (int i21 = 0; i21 < paramInt3; ++i21) {
//					i24 = i21 * paramInt1;
//					i31 = i24 + i13 * paramInt3;
//					i32 = i24 + i17 * paramInt3;
//					i34 = i24 * paramInt2;
//					for (i35 = 0; i35 < paramInt1; ++i35) {
//						i38 = paramInt6 + i35;
//						double d6 = paramArrayOfDouble1[(paramInt5 + i35 + i13 + i34)];
//						d9 = paramArrayOfDouble1[(paramInt5 + i35 + i17 + i34)];
//						paramArrayOfDouble2[(i38 + i31)] = (d6 + d9);
//						paramArrayOfDouble2[(i38 + i32)] = (d6 - d9);
//					}
//				}
//			}
//			for (int i10 = 0; i10 < paramInt3; ++i10) {
//				i13 = i10 * paramInt1;
//				i17 = i13 * paramInt2;
//				for (i22 = 0; i22 < paramInt1; ++i22)
//					paramArrayOfDouble2[(paramInt6 + i22 + i13)] = paramArrayOfDouble1[(paramInt5
//							+ i22 + i17)];
//			}
//		} else {
//			for (int i11 = 1; i11 < l; ++i11) {
//				i2 = paramInt2 - i11;
//				i13 = i11 * paramInt3 * paramInt1;
//				i17 = i2 * paramInt3 * paramInt1;
//				i22 = i11 * paramInt1;
//				i24 = i2 * paramInt1;
//				for (i31 = 0; i31 < paramInt1; ++i31)
//					for (i32 = 0; i32 < paramInt3; ++i32) {
//						i34 = i32 * paramInt1;
//						i35 = i34 * paramInt2;
//						i38 = paramInt6 + i31;
//						i40 = paramInt5 + i31;
//						d8 = paramArrayOfDouble1[(i40 + i22 + i35)];
//						d10 = paramArrayOfDouble1[(i40 + i24 + i35)];
//						paramArrayOfDouble2[(i38 + i34 + i13)] = (d8 + d10);
//						paramArrayOfDouble2[(i38 + i34 + i17)] = (d8 - d10);
//					}
//			}
//			for (i12 = 0; i12 < paramInt1; ++i12)
//				for (i14 = 0; i14 < paramInt3; ++i14) {
//					i17 = i14 * paramInt1;
//					paramArrayOfDouble2[(paramInt6 + i12 + i17)] = paramArrayOfDouble1[(paramInt5
//							+ i12 + i17 * paramInt2)];
//				}
//		}
//		int i5 = 2 - paramInt1;
//		int i6 = 0;
//		int i12 = (paramInt2 - 1) * paramInt4;
//		double d1;
//		double d2;
//		int i26;
//		int i36;
//		for (int i1 = 1; i1 < l; ++i1) {
//			int i3 = paramInt2 - i1;
//			i5 += paramInt1;
//			i14 = i1 * paramInt4;
//			i17 = i3 * paramInt4;
//			i22 = i5 + i8;
//			d1 = this.wtable[(i22 - 2)];
//			d2 = paramInt8 * this.wtable[(i22 - 1)];
//			for (int i25 = 0; i25 < paramInt4; ++i25) {
//				i31 = paramInt5 + i25;
//				i32 = paramInt6 + i25;
//				paramArrayOfDouble1[(i31 + i14)] = (paramArrayOfDouble2[i32] + d1
//						* paramArrayOfDouble2[(i32 + paramInt4)]);
//				paramArrayOfDouble1[(i31 + i17)] = (d2 * paramArrayOfDouble2[(i32 + i12)]);
//			}
//			int j = i5;
//			i6 += paramInt1;
//			for (i26 = 2; i26 < l; ++i26) {
//				i2 = paramInt2 - i26;
//				j += i6;
//				if (j > i7)
//					j -= i7;
//				i31 = j + i8;
//				double d4 = this.wtable[(i31 - 2)];
//				double d3 = paramInt8 * this.wtable[(i31 - 1)];
//				i32 = i26 * paramInt4;
//				i34 = i2 * paramInt4;
//				for (i36 = 0; i36 < paramInt4; ++i36) {
//					i38 = paramInt5 + i36;
//					i40 = paramInt6 + i36;
//					paramArrayOfDouble1[(i38 + i14)] += d4
//							* paramArrayOfDouble2[(i40 + i32)];
//					paramArrayOfDouble1[(i38 + i17)] += d3
//							* paramArrayOfDouble2[(i40 + i34)];
//				}
//			}
//		}
//		int i23;
//		for (int i15 = 1; i15 < l; ++i15) {
//			i17 = i15 * paramInt4;
//			for (i23 = 0; i23 < paramInt4; ++i23) {
//				i26 = paramInt6 + i23;
//				paramArrayOfDouble2[i26] += paramArrayOfDouble2[(i26 + i17)];
//			}
//		}
//		for (int i16 = 1; i16 < l; ++i16) {
//			i2 = paramInt2 - i16;
//			i17 = i16 * paramInt4;
//			i23 = i2 * paramInt4;
//			for (int i27 = 1; i27 < paramInt4; i27 += 2) {
//				i31 = paramInt6 + i27;
//				i32 = paramInt5 + i27;
//				i34 = i32 + i17;
//				i36 = i32 + i23;
//				double d5 = paramArrayOfDouble1[(i34 - 1)];
//				d8 = paramArrayOfDouble1[i34];
//				d10 = paramArrayOfDouble1[(i36 - 1)];
//				double d12 = paramArrayOfDouble1[i36];
//				int i42 = i31 + i17;
//				int i43 = i31 + i23;
//				paramArrayOfDouble2[(i42 - 1)] = (d5 - d12);
//				paramArrayOfDouble2[(i43 - 1)] = (d5 + d12);
//				paramArrayOfDouble2[i42] = (d8 + d10);
//				paramArrayOfDouble2[i43] = (d8 - d10);
//			}
//		}
//		paramArrayOfInt[0] = 1;
//		if (paramInt1 == 2)
//			return;
//		paramArrayOfInt[0] = 0;
//		System.arraycopy(paramArrayOfDouble2, paramInt6, paramArrayOfDouble1,
//				paramInt5, paramInt4);
//		i16 = paramInt3 * paramInt1;
//		for (int i18 = 1; i18 < paramInt2; ++i18) {
//			i23 = i18 * i16;
//			for (int i28 = 0; i28 < paramInt3; ++i28) {
//				i31 = i28 * paramInt1;
//				i32 = paramInt6 + i31 + i23;
//				i34 = paramInt5 + i31 + i23;
//				paramArrayOfDouble1[i34] = paramArrayOfDouble2[i32];
//				paramArrayOfDouble1[(i34 + 1)] = paramArrayOfDouble2[(i32 + 1)];
//			}
//		}
//		int i;
//		int i37;
//		int i39;
//		if (k <= paramInt3) {
//			i = 0;
//			for (int i19 = 1; i19 < paramInt2; ++i19) {
//				i += 2;
//				i23 = i19 * paramInt3 * paramInt1;
//				for (int i29 = 3; i29 < paramInt1; i29 += 2) {
//					i31 = (i += 2) + i8 - 1;
//					d1 = this.wtable[(i31 - 1)];
//					d2 = paramInt8 * this.wtable[i31];
//					i32 = paramInt5 + i29;
//					i34 = paramInt6 + i29;
//					for (i37 = 0; i37 < paramInt3; ++i37) {
//						i39 = i37 * paramInt1 + i23;
//						i40 = i32 + i39;
//						int i41 = i34 + i39;
//						d9 = paramArrayOfDouble2[(i41 - 1)];
//						double d11 = paramArrayOfDouble2[i41];
//						paramArrayOfDouble1[(i40 - 1)] = (d1 * d9 - (d2 * d11));
//						paramArrayOfDouble1[i40] = (d1 * d11 + d2 * d9);
//					}
//				}
//			}
//		} else {
//			int i4 = 2 - paramInt1;
//			for (int i20 = 1; i20 < paramInt2; ++i20) {
//				i4 += paramInt1;
//				i23 = i20 * paramInt3 * paramInt1;
//				for (int i30 = 0; i30 < paramInt3; ++i30) {
//					i = i4;
//					i31 = i30 * paramInt1 + i23;
//					for (int i33 = 3; i33 < paramInt1; i33 += 2) {
//						i34 = (i += 2) - 1 + i8;
//						d1 = this.wtable[(i34 - 1)];
//						d2 = paramInt8 * this.wtable[i34];
//						i37 = paramInt5 + i33 + i31;
//						i39 = paramInt6 + i33 + i31;
//						double d7 = paramArrayOfDouble2[(i39 - 1)];
//						d9 = paramArrayOfDouble2[i39];
//						paramArrayOfDouble1[(i37 - 1)] = (d1 * d7 - (d2 * d9));
//						paramArrayOfDouble1[i37] = (d1 * d9 + d2 * d7);
//					}
//				}
//			}
//		}
//	}
//
//	private void cftfsub(int paramInt1, double[] paramArrayOfDouble1,
//			int paramInt2, int[] paramArrayOfInt, int paramInt3,
//			double[] paramArrayOfDouble2) {
//		if (paramInt1 > 8) {
//			if (paramInt1 > 32) {
//				cftf1st(paramInt1, paramArrayOfDouble1, paramInt2,
//						paramArrayOfDouble2, paramInt3 - (paramInt1 >> 2));
//				if ((ConcurrencyUtils.getNumberOfThreads() > 1)
//						&& (paramInt1 > ConcurrencyUtils
//								.getThreadsBeginN_1D_FFT_2Threads()))
//					cftrec4_th(paramInt1, paramArrayOfDouble1, paramInt2,
//							paramInt3, paramArrayOfDouble2);
//				else if (paramInt1 > 512)
//					cftrec4(paramInt1, paramArrayOfDouble1, paramInt2,
//							paramInt3, paramArrayOfDouble2);
//				else if (paramInt1 > 128)
//					cftleaf(paramInt1, 1, paramArrayOfDouble1, paramInt2,
//							paramInt3, paramArrayOfDouble2);
//				else
//					cftfx41(paramInt1, paramArrayOfDouble1, paramInt2,
//							paramInt3, paramArrayOfDouble2);
//				bitrv2(paramInt1, paramArrayOfInt, paramArrayOfDouble1,
//						paramInt2);
//			} else if (paramInt1 == 32) {
//				cftf161(paramArrayOfDouble1, paramInt2, paramArrayOfDouble2,
//						paramInt3 - 8);
//				bitrv216(paramArrayOfDouble1, paramInt2);
//			} else {
//				cftf081(paramArrayOfDouble1, paramInt2, paramArrayOfDouble2, 0);
//				bitrv208(paramArrayOfDouble1, paramInt2);
//			}
//		} else if (paramInt1 == 8) {
//			cftf040(paramArrayOfDouble1, paramInt2);
//		} else {
//			if (paramInt1 != 4)
//				return;
//			cftxb020(paramArrayOfDouble1, paramInt2);
//		}
//	}
//
//	private void cftbsub(int paramInt1, double[] paramArrayOfDouble1,
//			int paramInt2, int[] paramArrayOfInt, int paramInt3,
//			double[] paramArrayOfDouble2) {
//		if (paramInt1 > 8) {
//			if (paramInt1 > 32) {
//				cftb1st(paramInt1, paramArrayOfDouble1, paramInt2,
//						paramArrayOfDouble2, paramInt3 - (paramInt1 >> 2));
//				if ((ConcurrencyUtils.getNumberOfThreads() > 1)
//						&& (paramInt1 > ConcurrencyUtils
//								.getThreadsBeginN_1D_FFT_2Threads()))
//					cftrec4_th(paramInt1, paramArrayOfDouble1, paramInt2,
//							paramInt3, paramArrayOfDouble2);
//				else if (paramInt1 > 512)
//					cftrec4(paramInt1, paramArrayOfDouble1, paramInt2,
//							paramInt3, paramArrayOfDouble2);
//				else if (paramInt1 > 128)
//					cftleaf(paramInt1, 1, paramArrayOfDouble1, paramInt2,
//							paramInt3, paramArrayOfDouble2);
//				else
//					cftfx41(paramInt1, paramArrayOfDouble1, paramInt2,
//							paramInt3, paramArrayOfDouble2);
//				bitrv2conj(paramInt1, paramArrayOfInt, paramArrayOfDouble1,
//						paramInt2);
//			} else if (paramInt1 == 32) {
//				cftf161(paramArrayOfDouble1, paramInt2, paramArrayOfDouble2,
//						paramInt3 - 8);
//				bitrv216neg(paramArrayOfDouble1, paramInt2);
//			} else {
//				cftf081(paramArrayOfDouble1, paramInt2, paramArrayOfDouble2, 0);
//				bitrv208neg(paramArrayOfDouble1, paramInt2);
//			}
//		} else if (paramInt1 == 8) {
//			cftb040(paramArrayOfDouble1, paramInt2);
//		} else {
//			if (paramInt1 != 4)
//				return;
//			cftxb020(paramArrayOfDouble1, paramInt2);
//		}
//	}
//
//	private void bitrv2(int paramInt1, int[] paramArrayOfInt,
//			double[] paramArrayOfDouble, int paramInt2) {
//		int l = 1;
//		int k = paramInt1 >> 2;
//		while (k > 8) {
//			l <<= 1;
//			k >>= 2;
//		}
//		int i1 = paramInt1 >> 1;
//		int i2 = 4 * l;
//		int i6;
//		int i3;
//		int i7;
//		int i;
//		int j;
//		int i4;
//		int i5;
//		double d1;
//		double d2;
//		double d3;
//		double d4;
//		if (k == 8)
//			for (i6 = 0; i6 < l; ++i6) {
//				i3 = 4 * i6;
//				for (i7 = 0; i7 < i6; ++i7) {
//					i = 4 * i7 + 2 * paramArrayOfInt[(l + i6)];
//					j = i3 + 2 * paramArrayOfInt[(l + i7)];
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i1;
//					j += 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += 2;
//					j += i1;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i1;
//					j -= 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//				}
//				j = i3 + 2 * paramArrayOfInt[(l + i6)];
//				i = j + 2;
//				j += i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				i += i2;
//				j += 2 * i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				i += i2;
//				j -= i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				i -= 2;
//				j -= i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				i += i1 + 2;
//				j += i1 + 2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				i -= i1 - i2;
//				j += 2 * i2 - 2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//			}
//		else
//			for (i6 = 0; i6 < l; ++i6) {
//				i3 = 4 * i6;
//				for (i7 = 0; i7 < i6; ++i7) {
//					i = 4 * i7 + paramArrayOfInt[(l + i6)];
//					j = i3 + paramArrayOfInt[(l + i7)];
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i1;
//					j += 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += 2;
//					j += i1;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i1;
//					j -= 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//				}
//				j = i3 + paramArrayOfInt[(l + i6)];
//				i = j + 2;
//				j += i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				i += i2;
//				j += i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//			}
//	}
//
//	private void bitrv2conj(int paramInt1, int[] paramArrayOfInt,
//			double[] paramArrayOfDouble, int paramInt2) {
//		int l = 1;
//		int k = paramInt1 >> 2;
//		while (k > 8) {
//			l <<= 1;
//			k >>= 2;
//		}
//		int i1 = paramInt1 >> 1;
//		int i2 = 4 * l;
//		int i6;
//		int i3;
//		int i7;
//		int i;
//		int j;
//		int i4;
//		int i5;
//		double d1;
//		double d2;
//		double d3;
//		double d4;
//		if (k == 8)
//			for (i6 = 0; i6 < l; ++i6) {
//				i3 = 4 * i6;
//				for (i7 = 0; i7 < i6; ++i7) {
//					i = 4 * i7 + 2 * paramArrayOfInt[(l + i6)];
//					j = i3 + 2 * paramArrayOfInt[(l + i7)];
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i1;
//					j += 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += 2;
//					j += i1;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i1;
//					j -= 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//				}
//				j = i3 + 2 * paramArrayOfInt[(l + i6)];
//				i = j + 2;
//				j += i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				paramArrayOfDouble[(i4 - 1)] = (-paramArrayOfDouble[(i4 - 1)]);
//				d1 = paramArrayOfDouble[i4];
//				d2 = -paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = -paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				paramArrayOfDouble[(i5 + 3)] = (-paramArrayOfDouble[(i5 + 3)]);
//				i += i2;
//				j += 2 * i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = -paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = -paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				i += i2;
//				j -= i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = -paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = -paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				i -= 2;
//				j -= i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = -paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = -paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				i += i1 + 2;
//				j += i1 + 2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				d1 = paramArrayOfDouble[i4];
//				d2 = -paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = -paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				i -= i1 - i2;
//				j += 2 * i2 - 2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				paramArrayOfDouble[(i4 - 1)] = (-paramArrayOfDouble[(i4 - 1)]);
//				d1 = paramArrayOfDouble[i4];
//				d2 = -paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = -paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				paramArrayOfDouble[(i5 + 3)] = (-paramArrayOfDouble[(i5 + 3)]);
//			}
//		else
//			for (i6 = 0; i6 < l; ++i6) {
//				i3 = 4 * i6;
//				for (i7 = 0; i7 < i6; ++i7) {
//					i = 4 * i7 + paramArrayOfInt[(l + i6)];
//					j = i3 + paramArrayOfInt[(l + i7)];
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i1;
//					j += 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += 2;
//					j += i1;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i += i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i1;
//					j -= 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//					i -= i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					d1 = paramArrayOfDouble[i4];
//					d2 = -paramArrayOfDouble[(i4 + 1)];
//					d3 = paramArrayOfDouble[i5];
//					d4 = -paramArrayOfDouble[(i5 + 1)];
//					paramArrayOfDouble[i4] = d3;
//					paramArrayOfDouble[(i4 + 1)] = d4;
//					paramArrayOfDouble[i5] = d1;
//					paramArrayOfDouble[(i5 + 1)] = d2;
//				}
//				j = i3 + paramArrayOfInt[(l + i6)];
//				i = j + 2;
//				j += i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				paramArrayOfDouble[(i4 - 1)] = (-paramArrayOfDouble[(i4 - 1)]);
//				d1 = paramArrayOfDouble[i4];
//				d2 = -paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = -paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				paramArrayOfDouble[(i5 + 3)] = (-paramArrayOfDouble[(i5 + 3)]);
//				i += i2;
//				j += i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				paramArrayOfDouble[(i4 - 1)] = (-paramArrayOfDouble[(i4 - 1)]);
//				d1 = paramArrayOfDouble[i4];
//				d2 = -paramArrayOfDouble[(i4 + 1)];
//				d3 = paramArrayOfDouble[i5];
//				d4 = -paramArrayOfDouble[(i5 + 1)];
//				paramArrayOfDouble[i4] = d3;
//				paramArrayOfDouble[(i4 + 1)] = d4;
//				paramArrayOfDouble[i5] = d1;
//				paramArrayOfDouble[(i5 + 1)] = d2;
//				paramArrayOfDouble[(i5 + 3)] = (-paramArrayOfDouble[(i5 + 3)]);
//			}
//	}
//
//	private void bitrv216(double[] paramArrayOfDouble, int paramInt) {
//		double d1 = paramArrayOfDouble[(paramInt + 2)];
//		double d2 = paramArrayOfDouble[(paramInt + 3)];
//		double d3 = paramArrayOfDouble[(paramInt + 4)];
//		double d4 = paramArrayOfDouble[(paramInt + 5)];
//		double d5 = paramArrayOfDouble[(paramInt + 6)];
//		double d6 = paramArrayOfDouble[(paramInt + 7)];
//		double d7 = paramArrayOfDouble[(paramInt + 8)];
//		double d8 = paramArrayOfDouble[(paramInt + 9)];
//		double d9 = paramArrayOfDouble[(paramInt + 10)];
//		double d10 = paramArrayOfDouble[(paramInt + 11)];
//		double d11 = paramArrayOfDouble[(paramInt + 14)];
//		double d12 = paramArrayOfDouble[(paramInt + 15)];
//		double d13 = paramArrayOfDouble[(paramInt + 16)];
//		double d14 = paramArrayOfDouble[(paramInt + 17)];
//		double d15 = paramArrayOfDouble[(paramInt + 20)];
//		double d16 = paramArrayOfDouble[(paramInt + 21)];
//		double d17 = paramArrayOfDouble[(paramInt + 22)];
//		double d18 = paramArrayOfDouble[(paramInt + 23)];
//		double d19 = paramArrayOfDouble[(paramInt + 24)];
//		double d20 = paramArrayOfDouble[(paramInt + 25)];
//		double d21 = paramArrayOfDouble[(paramInt + 26)];
//		double d22 = paramArrayOfDouble[(paramInt + 27)];
//		double d23 = paramArrayOfDouble[(paramInt + 28)];
//		double d24 = paramArrayOfDouble[(paramInt + 29)];
//		paramArrayOfDouble[(paramInt + 2)] = d13;
//		paramArrayOfDouble[(paramInt + 3)] = d14;
//		paramArrayOfDouble[(paramInt + 4)] = d7;
//		paramArrayOfDouble[(paramInt + 5)] = d8;
//		paramArrayOfDouble[(paramInt + 6)] = d19;
//		paramArrayOfDouble[(paramInt + 7)] = d20;
//		paramArrayOfDouble[(paramInt + 8)] = d3;
//		paramArrayOfDouble[(paramInt + 9)] = d4;
//		paramArrayOfDouble[(paramInt + 10)] = d15;
//		paramArrayOfDouble[(paramInt + 11)] = d16;
//		paramArrayOfDouble[(paramInt + 14)] = d23;
//		paramArrayOfDouble[(paramInt + 15)] = d24;
//		paramArrayOfDouble[(paramInt + 16)] = d1;
//		paramArrayOfDouble[(paramInt + 17)] = d2;
//		paramArrayOfDouble[(paramInt + 20)] = d9;
//		paramArrayOfDouble[(paramInt + 21)] = d10;
//		paramArrayOfDouble[(paramInt + 22)] = d21;
//		paramArrayOfDouble[(paramInt + 23)] = d22;
//		paramArrayOfDouble[(paramInt + 24)] = d5;
//		paramArrayOfDouble[(paramInt + 25)] = d6;
//		paramArrayOfDouble[(paramInt + 26)] = d17;
//		paramArrayOfDouble[(paramInt + 27)] = d18;
//		paramArrayOfDouble[(paramInt + 28)] = d11;
//		paramArrayOfDouble[(paramInt + 29)] = d12;
//	}
//
//	private void bitrv216neg(double[] paramArrayOfDouble, int paramInt) {
//		double d1 = paramArrayOfDouble[(paramInt + 2)];
//		double d2 = paramArrayOfDouble[(paramInt + 3)];
//		double d3 = paramArrayOfDouble[(paramInt + 4)];
//		double d4 = paramArrayOfDouble[(paramInt + 5)];
//		double d5 = paramArrayOfDouble[(paramInt + 6)];
//		double d6 = paramArrayOfDouble[(paramInt + 7)];
//		double d7 = paramArrayOfDouble[(paramInt + 8)];
//		double d8 = paramArrayOfDouble[(paramInt + 9)];
//		double d9 = paramArrayOfDouble[(paramInt + 10)];
//		double d10 = paramArrayOfDouble[(paramInt + 11)];
//		double d11 = paramArrayOfDouble[(paramInt + 12)];
//		double d12 = paramArrayOfDouble[(paramInt + 13)];
//		double d13 = paramArrayOfDouble[(paramInt + 14)];
//		double d14 = paramArrayOfDouble[(paramInt + 15)];
//		double d15 = paramArrayOfDouble[(paramInt + 16)];
//		double d16 = paramArrayOfDouble[(paramInt + 17)];
//		double d17 = paramArrayOfDouble[(paramInt + 18)];
//		double d18 = paramArrayOfDouble[(paramInt + 19)];
//		double d19 = paramArrayOfDouble[(paramInt + 20)];
//		double d20 = paramArrayOfDouble[(paramInt + 21)];
//		double d21 = paramArrayOfDouble[(paramInt + 22)];
//		double d22 = paramArrayOfDouble[(paramInt + 23)];
//		double d23 = paramArrayOfDouble[(paramInt + 24)];
//		double d24 = paramArrayOfDouble[(paramInt + 25)];
//		double d25 = paramArrayOfDouble[(paramInt + 26)];
//		double d26 = paramArrayOfDouble[(paramInt + 27)];
//		double d27 = paramArrayOfDouble[(paramInt + 28)];
//		double d28 = paramArrayOfDouble[(paramInt + 29)];
//		double d29 = paramArrayOfDouble[(paramInt + 30)];
//		double d30 = paramArrayOfDouble[(paramInt + 31)];
//		paramArrayOfDouble[(paramInt + 2)] = d29;
//		paramArrayOfDouble[(paramInt + 3)] = d30;
//		paramArrayOfDouble[(paramInt + 4)] = d13;
//		paramArrayOfDouble[(paramInt + 5)] = d14;
//		paramArrayOfDouble[(paramInt + 6)] = d21;
//		paramArrayOfDouble[(paramInt + 7)] = d22;
//		paramArrayOfDouble[(paramInt + 8)] = d5;
//		paramArrayOfDouble[(paramInt + 9)] = d6;
//		paramArrayOfDouble[(paramInt + 10)] = d25;
//		paramArrayOfDouble[(paramInt + 11)] = d26;
//		paramArrayOfDouble[(paramInt + 12)] = d9;
//		paramArrayOfDouble[(paramInt + 13)] = d10;
//		paramArrayOfDouble[(paramInt + 14)] = d17;
//		paramArrayOfDouble[(paramInt + 15)] = d18;
//		paramArrayOfDouble[(paramInt + 16)] = d1;
//		paramArrayOfDouble[(paramInt + 17)] = d2;
//		paramArrayOfDouble[(paramInt + 18)] = d27;
//		paramArrayOfDouble[(paramInt + 19)] = d28;
//		paramArrayOfDouble[(paramInt + 20)] = d11;
//		paramArrayOfDouble[(paramInt + 21)] = d12;
//		paramArrayOfDouble[(paramInt + 22)] = d19;
//		paramArrayOfDouble[(paramInt + 23)] = d20;
//		paramArrayOfDouble[(paramInt + 24)] = d3;
//		paramArrayOfDouble[(paramInt + 25)] = d4;
//		paramArrayOfDouble[(paramInt + 26)] = d23;
//		paramArrayOfDouble[(paramInt + 27)] = d24;
//		paramArrayOfDouble[(paramInt + 28)] = d7;
//		paramArrayOfDouble[(paramInt + 29)] = d8;
//		paramArrayOfDouble[(paramInt + 30)] = d15;
//		paramArrayOfDouble[(paramInt + 31)] = d16;
//	}
//
//	private void bitrv208(double[] paramArrayOfDouble, int paramInt) {
//		double d1 = paramArrayOfDouble[(paramInt + 2)];
//		double d2 = paramArrayOfDouble[(paramInt + 3)];
//		double d3 = paramArrayOfDouble[(paramInt + 6)];
//		double d4 = paramArrayOfDouble[(paramInt + 7)];
//		double d5 = paramArrayOfDouble[(paramInt + 8)];
//		double d6 = paramArrayOfDouble[(paramInt + 9)];
//		double d7 = paramArrayOfDouble[(paramInt + 12)];
//		double d8 = paramArrayOfDouble[(paramInt + 13)];
//		paramArrayOfDouble[(paramInt + 2)] = d5;
//		paramArrayOfDouble[(paramInt + 3)] = d6;
//		paramArrayOfDouble[(paramInt + 6)] = d7;
//		paramArrayOfDouble[(paramInt + 7)] = d8;
//		paramArrayOfDouble[(paramInt + 8)] = d1;
//		paramArrayOfDouble[(paramInt + 9)] = d2;
//		paramArrayOfDouble[(paramInt + 12)] = d3;
//		paramArrayOfDouble[(paramInt + 13)] = d4;
//	}
//
//	private void bitrv208neg(double[] paramArrayOfDouble, int paramInt) {
//		double d1 = paramArrayOfDouble[(paramInt + 2)];
//		double d2 = paramArrayOfDouble[(paramInt + 3)];
//		double d3 = paramArrayOfDouble[(paramInt + 4)];
//		double d4 = paramArrayOfDouble[(paramInt + 5)];
//		double d5 = paramArrayOfDouble[(paramInt + 6)];
//		double d6 = paramArrayOfDouble[(paramInt + 7)];
//		double d7 = paramArrayOfDouble[(paramInt + 8)];
//		double d8 = paramArrayOfDouble[(paramInt + 9)];
//		double d9 = paramArrayOfDouble[(paramInt + 10)];
//		double d10 = paramArrayOfDouble[(paramInt + 11)];
//		double d11 = paramArrayOfDouble[(paramInt + 12)];
//		double d12 = paramArrayOfDouble[(paramInt + 13)];
//		double d13 = paramArrayOfDouble[(paramInt + 14)];
//		double d14 = paramArrayOfDouble[(paramInt + 15)];
//		paramArrayOfDouble[(paramInt + 2)] = d13;
//		paramArrayOfDouble[(paramInt + 3)] = d14;
//		paramArrayOfDouble[(paramInt + 4)] = d5;
//		paramArrayOfDouble[(paramInt + 5)] = d6;
//		paramArrayOfDouble[(paramInt + 6)] = d9;
//		paramArrayOfDouble[(paramInt + 7)] = d10;
//		paramArrayOfDouble[(paramInt + 8)] = d1;
//		paramArrayOfDouble[(paramInt + 9)] = d2;
//		paramArrayOfDouble[(paramInt + 10)] = d11;
//		paramArrayOfDouble[(paramInt + 11)] = d12;
//		paramArrayOfDouble[(paramInt + 12)] = d3;
//		paramArrayOfDouble[(paramInt + 13)] = d4;
//		paramArrayOfDouble[(paramInt + 14)] = d7;
//		paramArrayOfDouble[(paramInt + 15)] = d8;
//	}
//
//	private void cftf1st(int paramInt1, double[] paramArrayOfDouble1,
//			int paramInt2, double[] paramArrayOfDouble2, int paramInt3) {
//		int i3 = paramInt1 >> 3;
//		int i2 = 2 * i3;
//		int j = i2;
//		int k = j + i2;
//		int l = k + i2;
//		int i5 = paramInt2 + j;
//		int i6 = paramInt2 + k;
//		int i7 = paramInt2 + l;
//		double d12 = paramArrayOfDouble1[paramInt2] + paramArrayOfDouble1[i6];
//		double d13 = paramArrayOfDouble1[(paramInt2 + 1)]
//				+ paramArrayOfDouble1[(i6 + 1)];
//		double d14 = paramArrayOfDouble1[paramInt2] - paramArrayOfDouble1[i6];
//		double d15 = paramArrayOfDouble1[(paramInt2 + 1)]
//				- paramArrayOfDouble1[(i6 + 1)];
//		double d16 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//		double d17 = paramArrayOfDouble1[(i5 + 1)]
//				+ paramArrayOfDouble1[(i7 + 1)];
//		double d18 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//		double d19 = paramArrayOfDouble1[(i5 + 1)]
//				- paramArrayOfDouble1[(i7 + 1)];
//		paramArrayOfDouble1[paramInt2] = (d12 + d16);
//		paramArrayOfDouble1[(paramInt2 + 1)] = (d13 + d17);
//		paramArrayOfDouble1[i5] = (d12 - d16);
//		paramArrayOfDouble1[(i5 + 1)] = (d13 - d17);
//		paramArrayOfDouble1[i6] = (d14 - d19);
//		paramArrayOfDouble1[(i6 + 1)] = (d15 + d18);
//		paramArrayOfDouble1[i7] = (d14 + d19);
//		paramArrayOfDouble1[(i7 + 1)] = (d15 - d18);
//		double d1 = paramArrayOfDouble2[(paramInt3 + 1)];
//		double d2 = paramArrayOfDouble2[(paramInt3 + 2)];
//		double d3 = paramArrayOfDouble2[(paramInt3 + 3)];
//		double d8 = 1.0D;
//		double d9 = 0.0D;
//		double d10 = 1.0D;
//		double d11 = 0.0D;
//		int i1 = 0;
//		for (int i10 = 2; i10 < i3 - 2; i10 += 4) {
//			int i8 = paramInt3 + (i1 += 4);
//			d4 = d2 * (d8 + paramArrayOfDouble2[i8]);
//			d5 = d2 * (d9 + paramArrayOfDouble2[(i8 + 1)]);
//			d6 = d3 * (d10 + paramArrayOfDouble2[(i8 + 2)]);
//			d7 = d3 * (d11 + paramArrayOfDouble2[(i8 + 3)]);
//			d8 = paramArrayOfDouble2[i8];
//			d9 = paramArrayOfDouble2[(i8 + 1)];
//			d10 = paramArrayOfDouble2[(i8 + 2)];
//			d11 = paramArrayOfDouble2[(i8 + 3)];
//			j = i10 + i2;
//			k = j + i2;
//			l = k + i2;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			int i9 = paramInt2 + i10;
//			d12 = paramArrayOfDouble1[i9] + paramArrayOfDouble1[i6];
//			d13 = paramArrayOfDouble1[(i9 + 1)] + paramArrayOfDouble1[(i6 + 1)];
//			d14 = paramArrayOfDouble1[i9] - paramArrayOfDouble1[i6];
//			d15 = paramArrayOfDouble1[(i9 + 1)] - paramArrayOfDouble1[(i6 + 1)];
//			double d20 = paramArrayOfDouble1[(i9 + 2)]
//					+ paramArrayOfDouble1[(i6 + 2)];
//			double d21 = paramArrayOfDouble1[(i9 + 3)]
//					+ paramArrayOfDouble1[(i6 + 3)];
//			double d22 = paramArrayOfDouble1[(i9 + 2)]
//					- paramArrayOfDouble1[(i6 + 2)];
//			double d23 = paramArrayOfDouble1[(i9 + 3)]
//					- paramArrayOfDouble1[(i6 + 3)];
//			d16 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//			d17 = paramArrayOfDouble1[(i5 + 1)] + paramArrayOfDouble1[(i7 + 1)];
//			d18 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//			d19 = paramArrayOfDouble1[(i5 + 1)] - paramArrayOfDouble1[(i7 + 1)];
//			double d24 = paramArrayOfDouble1[(i5 + 2)]
//					+ paramArrayOfDouble1[(i7 + 2)];
//			double d25 = paramArrayOfDouble1[(i5 + 3)]
//					+ paramArrayOfDouble1[(i7 + 3)];
//			double d26 = paramArrayOfDouble1[(i5 + 2)]
//					- paramArrayOfDouble1[(i7 + 2)];
//			double d27 = paramArrayOfDouble1[(i5 + 3)]
//					- paramArrayOfDouble1[(i7 + 3)];
//			paramArrayOfDouble1[i9] = (d12 + d16);
//			paramArrayOfDouble1[(i9 + 1)] = (d13 + d17);
//			paramArrayOfDouble1[(i9 + 2)] = (d20 + d24);
//			paramArrayOfDouble1[(i9 + 3)] = (d21 + d25);
//			paramArrayOfDouble1[i5] = (d12 - d16);
//			paramArrayOfDouble1[(i5 + 1)] = (d13 - d17);
//			paramArrayOfDouble1[(i5 + 2)] = (d20 - d24);
//			paramArrayOfDouble1[(i5 + 3)] = (d21 - d25);
//			d12 = d14 - d19;
//			d13 = d15 + d18;
//			paramArrayOfDouble1[i6] = (d4 * d12 - (d5 * d13));
//			paramArrayOfDouble1[(i6 + 1)] = (d4 * d13 + d5 * d12);
//			d12 = d22 - d27;
//			d13 = d23 + d26;
//			paramArrayOfDouble1[(i6 + 2)] = (d8 * d12 - (d9 * d13));
//			paramArrayOfDouble1[(i6 + 3)] = (d8 * d13 + d9 * d12);
//			d12 = d14 + d19;
//			d13 = d15 - d18;
//			paramArrayOfDouble1[i7] = (d6 * d12 + d7 * d13);
//			paramArrayOfDouble1[(i7 + 1)] = (d6 * d13 - (d7 * d12));
//			d12 = d22 + d27;
//			d13 = d23 - d26;
//			paramArrayOfDouble1[(i7 + 2)] = (d10 * d12 + d11 * d13);
//			paramArrayOfDouble1[(i7 + 3)] = (d10 * d13 - (d11 * d12));
//			i = i2 - i10;
//			j = i + i2;
//			k = j + i2;
//			l = k + i2;
//			i4 = paramInt2 + i;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			d12 = paramArrayOfDouble1[i4] + paramArrayOfDouble1[i6];
//			d13 = paramArrayOfDouble1[(i4 + 1)] + paramArrayOfDouble1[(i6 + 1)];
//			d14 = paramArrayOfDouble1[i4] - paramArrayOfDouble1[i6];
//			d15 = paramArrayOfDouble1[(i4 + 1)] - paramArrayOfDouble1[(i6 + 1)];
//			d20 = paramArrayOfDouble1[(i4 - 2)] + paramArrayOfDouble1[(i6 - 2)];
//			d21 = paramArrayOfDouble1[(i4 - 1)] + paramArrayOfDouble1[(i6 - 1)];
//			d22 = paramArrayOfDouble1[(i4 - 2)] - paramArrayOfDouble1[(i6 - 2)];
//			d23 = paramArrayOfDouble1[(i4 - 1)] - paramArrayOfDouble1[(i6 - 1)];
//			d16 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//			d17 = paramArrayOfDouble1[(i5 + 1)] + paramArrayOfDouble1[(i7 + 1)];
//			d18 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//			d19 = paramArrayOfDouble1[(i5 + 1)] - paramArrayOfDouble1[(i7 + 1)];
//			d24 = paramArrayOfDouble1[(i5 - 2)] + paramArrayOfDouble1[(i7 - 2)];
//			d25 = paramArrayOfDouble1[(i5 - 1)] + paramArrayOfDouble1[(i7 - 1)];
//			d26 = paramArrayOfDouble1[(i5 - 2)] - paramArrayOfDouble1[(i7 - 2)];
//			d27 = paramArrayOfDouble1[(i5 - 1)] - paramArrayOfDouble1[(i7 - 1)];
//			paramArrayOfDouble1[i4] = (d12 + d16);
//			paramArrayOfDouble1[(i4 + 1)] = (d13 + d17);
//			paramArrayOfDouble1[(i4 - 2)] = (d20 + d24);
//			paramArrayOfDouble1[(i4 - 1)] = (d21 + d25);
//			paramArrayOfDouble1[i5] = (d12 - d16);
//			paramArrayOfDouble1[(i5 + 1)] = (d13 - d17);
//			paramArrayOfDouble1[(i5 - 2)] = (d20 - d24);
//			paramArrayOfDouble1[(i5 - 1)] = (d21 - d25);
//			d12 = d14 - d19;
//			d13 = d15 + d18;
//			paramArrayOfDouble1[i6] = (d5 * d12 - (d4 * d13));
//			paramArrayOfDouble1[(i6 + 1)] = (d5 * d13 + d4 * d12);
//			d12 = d22 - d27;
//			d13 = d23 + d26;
//			paramArrayOfDouble1[(i6 - 2)] = (d9 * d12 - (d8 * d13));
//			paramArrayOfDouble1[(i6 - 1)] = (d9 * d13 + d8 * d12);
//			d12 = d14 + d19;
//			d13 = d15 - d18;
//			paramArrayOfDouble1[i7] = (d7 * d12 + d6 * d13);
//			paramArrayOfDouble1[(i7 + 1)] = (d7 * d13 - (d6 * d12));
//			d12 = d22 + d27;
//			d13 = d23 - d26;
//			paramArrayOfDouble1[(paramInt2 + l - 2)] = (d11 * d12 + d10 * d13);
//			paramArrayOfDouble1[(paramInt2 + l - 1)] = (d11 * d13 - (d10 * d12));
//		}
//		double d4 = d2 * (d8 + d1);
//		double d5 = d2 * (d9 + d1);
//		double d6 = d3 * (d10 - d1);
//		double d7 = d3 * (d11 - d1);
//		int i = i3;
//		j = i + i2;
//		k = j + i2;
//		l = k + i2;
//		int i4 = paramInt2 + i;
//		i5 = paramInt2 + j;
//		i6 = paramInt2 + k;
//		i7 = paramInt2 + l;
//		d12 = paramArrayOfDouble1[(i4 - 2)] + paramArrayOfDouble1[(i6 - 2)];
//		d13 = paramArrayOfDouble1[(i4 - 1)] + paramArrayOfDouble1[(i6 - 1)];
//		d14 = paramArrayOfDouble1[(i4 - 2)] - paramArrayOfDouble1[(i6 - 2)];
//		d15 = paramArrayOfDouble1[(i4 - 1)] - paramArrayOfDouble1[(i6 - 1)];
//		d16 = paramArrayOfDouble1[(i5 - 2)] + paramArrayOfDouble1[(i7 - 2)];
//		d17 = paramArrayOfDouble1[(i5 - 1)] + paramArrayOfDouble1[(i7 - 1)];
//		d18 = paramArrayOfDouble1[(i5 - 2)] - paramArrayOfDouble1[(i7 - 2)];
//		d19 = paramArrayOfDouble1[(i5 - 1)] - paramArrayOfDouble1[(i7 - 1)];
//		paramArrayOfDouble1[(i4 - 2)] = (d12 + d16);
//		paramArrayOfDouble1[(i4 - 1)] = (d13 + d17);
//		paramArrayOfDouble1[(i5 - 2)] = (d12 - d16);
//		paramArrayOfDouble1[(i5 - 1)] = (d13 - d17);
//		d12 = d14 - d19;
//		d13 = d15 + d18;
//		paramArrayOfDouble1[(i6 - 2)] = (d4 * d12 - (d5 * d13));
//		paramArrayOfDouble1[(i6 - 1)] = (d4 * d13 + d5 * d12);
//		d12 = d14 + d19;
//		d13 = d15 - d18;
//		paramArrayOfDouble1[(i7 - 2)] = (d6 * d12 + d7 * d13);
//		paramArrayOfDouble1[(i7 - 1)] = (d6 * d13 - (d7 * d12));
//		d12 = paramArrayOfDouble1[i4] + paramArrayOfDouble1[i6];
//		d13 = paramArrayOfDouble1[(i4 + 1)] + paramArrayOfDouble1[(i6 + 1)];
//		d14 = paramArrayOfDouble1[i4] - paramArrayOfDouble1[i6];
//		d15 = paramArrayOfDouble1[(i4 + 1)] - paramArrayOfDouble1[(i6 + 1)];
//		d16 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//		d17 = paramArrayOfDouble1[(i5 + 1)] + paramArrayOfDouble1[(i7 + 1)];
//		d18 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//		d19 = paramArrayOfDouble1[(i5 + 1)] - paramArrayOfDouble1[(i7 + 1)];
//		paramArrayOfDouble1[i4] = (d12 + d16);
//		paramArrayOfDouble1[(i4 + 1)] = (d13 + d17);
//		paramArrayOfDouble1[i5] = (d12 - d16);
//		paramArrayOfDouble1[(i5 + 1)] = (d13 - d17);
//		d12 = d14 - d19;
//		d13 = d15 + d18;
//		paramArrayOfDouble1[i6] = (d1 * (d12 - d13));
//		paramArrayOfDouble1[(i6 + 1)] = (d1 * (d13 + d12));
//		d12 = d14 + d19;
//		d13 = d15 - d18;
//		paramArrayOfDouble1[i7] = (-d1 * (d12 + d13));
//		paramArrayOfDouble1[(i7 + 1)] = (-d1 * (d13 - d12));
//		d12 = paramArrayOfDouble1[(i4 + 2)] + paramArrayOfDouble1[(i6 + 2)];
//		d13 = paramArrayOfDouble1[(i4 + 3)] + paramArrayOfDouble1[(i6 + 3)];
//		d14 = paramArrayOfDouble1[(i4 + 2)] - paramArrayOfDouble1[(i6 + 2)];
//		d15 = paramArrayOfDouble1[(i4 + 3)] - paramArrayOfDouble1[(i6 + 3)];
//		d16 = paramArrayOfDouble1[(i5 + 2)] + paramArrayOfDouble1[(i7 + 2)];
//		d17 = paramArrayOfDouble1[(i5 + 3)] + paramArrayOfDouble1[(i7 + 3)];
//		d18 = paramArrayOfDouble1[(i5 + 2)] - paramArrayOfDouble1[(i7 + 2)];
//		d19 = paramArrayOfDouble1[(i5 + 3)] - paramArrayOfDouble1[(i7 + 3)];
//		paramArrayOfDouble1[(i4 + 2)] = (d12 + d16);
//		paramArrayOfDouble1[(i4 + 3)] = (d13 + d17);
//		paramArrayOfDouble1[(i5 + 2)] = (d12 - d16);
//		paramArrayOfDouble1[(i5 + 3)] = (d13 - d17);
//		d12 = d14 - d19;
//		d13 = d15 + d18;
//		paramArrayOfDouble1[(i6 + 2)] = (d5 * d12 - (d4 * d13));
//		paramArrayOfDouble1[(i6 + 3)] = (d5 * d13 + d4 * d12);
//		d12 = d14 + d19;
//		d13 = d15 - d18;
//		paramArrayOfDouble1[(i7 + 2)] = (d7 * d12 + d6 * d13);
//		paramArrayOfDouble1[(i7 + 3)] = (d7 * d13 - (d6 * d12));
//	}
//
//	private void cftb1st(int paramInt1, double[] paramArrayOfDouble1,
//			int paramInt2, double[] paramArrayOfDouble2, int paramInt3) {
//		int i3 = paramInt1 >> 3;
//		int i2 = 2 * i3;
//		int j = i2;
//		int k = j + i2;
//		int l = k + i2;
//		int i5 = paramInt2 + j;
//		int i6 = paramInt2 + k;
//		int i7 = paramInt2 + l;
//		double d12 = paramArrayOfDouble1[paramInt2] + paramArrayOfDouble1[i6];
//		double d13 = -paramArrayOfDouble1[(paramInt2 + 1)]
//				- paramArrayOfDouble1[(i6 + 1)];
//		double d14 = paramArrayOfDouble1[paramInt2] - paramArrayOfDouble1[i6];
//		double d15 = -paramArrayOfDouble1[(paramInt2 + 1)]
//				+ paramArrayOfDouble1[(i6 + 1)];
//		double d16 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//		double d17 = paramArrayOfDouble1[(i5 + 1)]
//				+ paramArrayOfDouble1[(i7 + 1)];
//		double d18 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//		double d19 = paramArrayOfDouble1[(i5 + 1)]
//				- paramArrayOfDouble1[(i7 + 1)];
//		paramArrayOfDouble1[paramInt2] = (d12 + d16);
//		paramArrayOfDouble1[(paramInt2 + 1)] = (d13 - d17);
//		paramArrayOfDouble1[i5] = (d12 - d16);
//		paramArrayOfDouble1[(i5 + 1)] = (d13 + d17);
//		paramArrayOfDouble1[i6] = (d14 + d19);
//		paramArrayOfDouble1[(i6 + 1)] = (d15 + d18);
//		paramArrayOfDouble1[i7] = (d14 - d19);
//		paramArrayOfDouble1[(i7 + 1)] = (d15 - d18);
//		double d1 = paramArrayOfDouble2[(paramInt3 + 1)];
//		double d2 = paramArrayOfDouble2[(paramInt3 + 2)];
//		double d3 = paramArrayOfDouble2[(paramInt3 + 3)];
//		double d8 = 1.0D;
//		double d9 = 0.0D;
//		double d10 = 1.0D;
//		double d11 = 0.0D;
//		int i1 = 0;
//		for (int i10 = 2; i10 < i3 - 2; i10 += 4) {
//			int i8 = paramInt3 + (i1 += 4);
//			d4 = d2 * (d8 + paramArrayOfDouble2[i8]);
//			d5 = d2 * (d9 + paramArrayOfDouble2[(i8 + 1)]);
//			d6 = d3 * (d10 + paramArrayOfDouble2[(i8 + 2)]);
//			d7 = d3 * (d11 + paramArrayOfDouble2[(i8 + 3)]);
//			d8 = paramArrayOfDouble2[i8];
//			d9 = paramArrayOfDouble2[(i8 + 1)];
//			d10 = paramArrayOfDouble2[(i8 + 2)];
//			d11 = paramArrayOfDouble2[(i8 + 3)];
//			j = i10 + i2;
//			k = j + i2;
//			l = k + i2;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			int i9 = paramInt2 + i10;
//			d12 = paramArrayOfDouble1[i9] + paramArrayOfDouble1[i6];
//			d13 = -paramArrayOfDouble1[(i9 + 1)]
//					- paramArrayOfDouble1[(i6 + 1)];
//			d14 = paramArrayOfDouble1[i9]
//					- paramArrayOfDouble1[(paramInt2 + k)];
//			d15 = -paramArrayOfDouble1[(i9 + 1)]
//					+ paramArrayOfDouble1[(i6 + 1)];
//			double d20 = paramArrayOfDouble1[(i9 + 2)]
//					+ paramArrayOfDouble1[(i6 + 2)];
//			double d21 = -paramArrayOfDouble1[(i9 + 3)]
//					- paramArrayOfDouble1[(i6 + 3)];
//			double d22 = paramArrayOfDouble1[(i9 + 2)]
//					- paramArrayOfDouble1[(i6 + 2)];
//			double d23 = -paramArrayOfDouble1[(i9 + 3)]
//					+ paramArrayOfDouble1[(i6 + 3)];
//			d16 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//			d17 = paramArrayOfDouble1[(i5 + 1)] + paramArrayOfDouble1[(i7 + 1)];
//			d18 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//			d19 = paramArrayOfDouble1[(i5 + 1)] - paramArrayOfDouble1[(i7 + 1)];
//			double d24 = paramArrayOfDouble1[(i5 + 2)]
//					+ paramArrayOfDouble1[(i7 + 2)];
//			double d25 = paramArrayOfDouble1[(i5 + 3)]
//					+ paramArrayOfDouble1[(i7 + 3)];
//			double d26 = paramArrayOfDouble1[(i5 + 2)]
//					- paramArrayOfDouble1[(i7 + 2)];
//			double d27 = paramArrayOfDouble1[(i5 + 3)]
//					- paramArrayOfDouble1[(i7 + 3)];
//			paramArrayOfDouble1[i9] = (d12 + d16);
//			paramArrayOfDouble1[(i9 + 1)] = (d13 - d17);
//			paramArrayOfDouble1[(i9 + 2)] = (d20 + d24);
//			paramArrayOfDouble1[(i9 + 3)] = (d21 - d25);
//			paramArrayOfDouble1[i5] = (d12 - d16);
//			paramArrayOfDouble1[(i5 + 1)] = (d13 + d17);
//			paramArrayOfDouble1[(i5 + 2)] = (d20 - d24);
//			paramArrayOfDouble1[(i5 + 3)] = (d21 + d25);
//			d12 = d14 + d19;
//			d13 = d15 + d18;
//			paramArrayOfDouble1[i6] = (d4 * d12 - (d5 * d13));
//			paramArrayOfDouble1[(i6 + 1)] = (d4 * d13 + d5 * d12);
//			d12 = d22 + d27;
//			d13 = d23 + d26;
//			paramArrayOfDouble1[(i6 + 2)] = (d8 * d12 - (d9 * d13));
//			paramArrayOfDouble1[(i6 + 3)] = (d8 * d13 + d9 * d12);
//			d12 = d14 - d19;
//			d13 = d15 - d18;
//			paramArrayOfDouble1[i7] = (d6 * d12 + d7 * d13);
//			paramArrayOfDouble1[(i7 + 1)] = (d6 * d13 - (d7 * d12));
//			d12 = d22 - d27;
//			d13 = d23 - d26;
//			paramArrayOfDouble1[(i7 + 2)] = (d10 * d12 + d11 * d13);
//			paramArrayOfDouble1[(i7 + 3)] = (d10 * d13 - (d11 * d12));
//			i = i2 - i10;
//			j = i + i2;
//			k = j + i2;
//			l = k + i2;
//			i4 = paramInt2 + i;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			d12 = paramArrayOfDouble1[i4] + paramArrayOfDouble1[i6];
//			d13 = -paramArrayOfDouble1[(i4 + 1)]
//					- paramArrayOfDouble1[(i6 + 1)];
//			d14 = paramArrayOfDouble1[i4] - paramArrayOfDouble1[i6];
//			d15 = -paramArrayOfDouble1[(i4 + 1)]
//					+ paramArrayOfDouble1[(i6 + 1)];
//			d20 = paramArrayOfDouble1[(i4 - 2)] + paramArrayOfDouble1[(i6 - 2)];
//			d21 = -paramArrayOfDouble1[(i4 - 1)]
//					- paramArrayOfDouble1[(i6 - 1)];
//			d22 = paramArrayOfDouble1[(i4 - 2)] - paramArrayOfDouble1[(i6 - 2)];
//			d23 = -paramArrayOfDouble1[(i4 - 1)]
//					+ paramArrayOfDouble1[(i6 - 1)];
//			d16 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//			d17 = paramArrayOfDouble1[(i5 + 1)] + paramArrayOfDouble1[(i7 + 1)];
//			d18 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//			d19 = paramArrayOfDouble1[(i5 + 1)] - paramArrayOfDouble1[(i7 + 1)];
//			d24 = paramArrayOfDouble1[(i5 - 2)] + paramArrayOfDouble1[(i7 - 2)];
//			d25 = paramArrayOfDouble1[(i5 - 1)] + paramArrayOfDouble1[(i7 - 1)];
//			d26 = paramArrayOfDouble1[(i5 - 2)] - paramArrayOfDouble1[(i7 - 2)];
//			d27 = paramArrayOfDouble1[(i5 - 1)] - paramArrayOfDouble1[(i7 - 1)];
//			paramArrayOfDouble1[i4] = (d12 + d16);
//			paramArrayOfDouble1[(i4 + 1)] = (d13 - d17);
//			paramArrayOfDouble1[(i4 - 2)] = (d20 + d24);
//			paramArrayOfDouble1[(i4 - 1)] = (d21 - d25);
//			paramArrayOfDouble1[i5] = (d12 - d16);
//			paramArrayOfDouble1[(i5 + 1)] = (d13 + d17);
//			paramArrayOfDouble1[(i5 - 2)] = (d20 - d24);
//			paramArrayOfDouble1[(i5 - 1)] = (d21 + d25);
//			d12 = d14 + d19;
//			d13 = d15 + d18;
//			paramArrayOfDouble1[i6] = (d5 * d12 - (d4 * d13));
//			paramArrayOfDouble1[(i6 + 1)] = (d5 * d13 + d4 * d12);
//			d12 = d22 + d27;
//			d13 = d23 + d26;
//			paramArrayOfDouble1[(i6 - 2)] = (d9 * d12 - (d8 * d13));
//			paramArrayOfDouble1[(i6 - 1)] = (d9 * d13 + d8 * d12);
//			d12 = d14 - d19;
//			d13 = d15 - d18;
//			paramArrayOfDouble1[i7] = (d7 * d12 + d6 * d13);
//			paramArrayOfDouble1[(i7 + 1)] = (d7 * d13 - (d6 * d12));
//			d12 = d22 - d27;
//			d13 = d23 - d26;
//			paramArrayOfDouble1[(i7 - 2)] = (d11 * d12 + d10 * d13);
//			paramArrayOfDouble1[(i7 - 1)] = (d11 * d13 - (d10 * d12));
//		}
//		double d4 = d2 * (d8 + d1);
//		double d5 = d2 * (d9 + d1);
//		double d6 = d3 * (d10 - d1);
//		double d7 = d3 * (d11 - d1);
//		int i = i3;
//		j = i + i2;
//		k = j + i2;
//		l = k + i2;
//		int i4 = paramInt2 + i;
//		i5 = paramInt2 + j;
//		i6 = paramInt2 + k;
//		i7 = paramInt2 + l;
//		d12 = paramArrayOfDouble1[(i4 - 2)] + paramArrayOfDouble1[(i6 - 2)];
//		d13 = -paramArrayOfDouble1[(i4 - 1)] - paramArrayOfDouble1[(i6 - 1)];
//		d14 = paramArrayOfDouble1[(i4 - 2)] - paramArrayOfDouble1[(i6 - 2)];
//		d15 = -paramArrayOfDouble1[(i4 - 1)] + paramArrayOfDouble1[(i6 - 1)];
//		d16 = paramArrayOfDouble1[(i5 - 2)] + paramArrayOfDouble1[(i7 - 2)];
//		d17 = paramArrayOfDouble1[(i5 - 1)] + paramArrayOfDouble1[(i7 - 1)];
//		d18 = paramArrayOfDouble1[(i5 - 2)] - paramArrayOfDouble1[(i7 - 2)];
//		d19 = paramArrayOfDouble1[(i5 - 1)] - paramArrayOfDouble1[(i7 - 1)];
//		paramArrayOfDouble1[(i4 - 2)] = (d12 + d16);
//		paramArrayOfDouble1[(i4 - 1)] = (d13 - d17);
//		paramArrayOfDouble1[(i5 - 2)] = (d12 - d16);
//		paramArrayOfDouble1[(i5 - 1)] = (d13 + d17);
//		d12 = d14 + d19;
//		d13 = d15 + d18;
//		paramArrayOfDouble1[(i6 - 2)] = (d4 * d12 - (d5 * d13));
//		paramArrayOfDouble1[(i6 - 1)] = (d4 * d13 + d5 * d12);
//		d12 = d14 - d19;
//		d13 = d15 - d18;
//		paramArrayOfDouble1[(i7 - 2)] = (d6 * d12 + d7 * d13);
//		paramArrayOfDouble1[(i7 - 1)] = (d6 * d13 - (d7 * d12));
//		d12 = paramArrayOfDouble1[i4] + paramArrayOfDouble1[i6];
//		d13 = -paramArrayOfDouble1[(i4 + 1)] - paramArrayOfDouble1[(i6 + 1)];
//		d14 = paramArrayOfDouble1[i4] - paramArrayOfDouble1[i6];
//		d15 = -paramArrayOfDouble1[(i4 + 1)] + paramArrayOfDouble1[(i6 + 1)];
//		d16 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//		d17 = paramArrayOfDouble1[(i5 + 1)] + paramArrayOfDouble1[(i7 + 1)];
//		d18 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//		d19 = paramArrayOfDouble1[(i5 + 1)] - paramArrayOfDouble1[(i7 + 1)];
//		paramArrayOfDouble1[i4] = (d12 + d16);
//		paramArrayOfDouble1[(i4 + 1)] = (d13 - d17);
//		paramArrayOfDouble1[i5] = (d12 - d16);
//		paramArrayOfDouble1[(i5 + 1)] = (d13 + d17);
//		d12 = d14 + d19;
//		d13 = d15 + d18;
//		paramArrayOfDouble1[i6] = (d1 * (d12 - d13));
//		paramArrayOfDouble1[(i6 + 1)] = (d1 * (d13 + d12));
//		d12 = d14 - d19;
//		d13 = d15 - d18;
//		paramArrayOfDouble1[i7] = (-d1 * (d12 + d13));
//		paramArrayOfDouble1[(i7 + 1)] = (-d1 * (d13 - d12));
//		d12 = paramArrayOfDouble1[(i4 + 2)] + paramArrayOfDouble1[(i6 + 2)];
//		d13 = -paramArrayOfDouble1[(i4 + 3)] - paramArrayOfDouble1[(i6 + 3)];
//		d14 = paramArrayOfDouble1[(i4 + 2)] - paramArrayOfDouble1[(i6 + 2)];
//		d15 = -paramArrayOfDouble1[(i4 + 3)] + paramArrayOfDouble1[(i6 + 3)];
//		d16 = paramArrayOfDouble1[(i5 + 2)] + paramArrayOfDouble1[(i7 + 2)];
//		d17 = paramArrayOfDouble1[(i5 + 3)] + paramArrayOfDouble1[(i7 + 3)];
//		d18 = paramArrayOfDouble1[(i5 + 2)] - paramArrayOfDouble1[(i7 + 2)];
//		d19 = paramArrayOfDouble1[(i5 + 3)] - paramArrayOfDouble1[(i7 + 3)];
//		paramArrayOfDouble1[(i4 + 2)] = (d12 + d16);
//		paramArrayOfDouble1[(i4 + 3)] = (d13 - d17);
//		paramArrayOfDouble1[(i5 + 2)] = (d12 - d16);
//		paramArrayOfDouble1[(i5 + 3)] = (d13 + d17);
//		d12 = d14 + d19;
//		d13 = d15 + d18;
//		paramArrayOfDouble1[(i6 + 2)] = (d5 * d12 - (d4 * d13));
//		paramArrayOfDouble1[(i6 + 3)] = (d5 * d13 + d4 * d12);
//		d12 = d14 - d19;
//		d13 = d15 - d18;
//		paramArrayOfDouble1[(i7 + 2)] = (d7 * d12 + d6 * d13);
//		paramArrayOfDouble1[(i7 + 3)] = (d7 * d13 - (d6 * d12));
//	}
//
//	private void cftrec4_th(int paramInt1, double[] paramArrayOfDouble1,
//			int paramInt2, int paramInt3, double[] paramArrayOfDouble2) {
//		int i1 = 0;
//		int l = 2;
//		int j = 0;
//		int k = paramInt1 >> 1;
//		if (paramInt1 > ConcurrencyUtils.getThreadsBeginN_1D_FFT_4Threads()) {
//			l = 4;
//			j = 1;
//			k >>= 1;
//		}
//		Future[] arrayOfFuture = new Future[l];
//		int i2 = k;
//		for (int i = 0; i < l; ++i) {
//			int i3 = paramInt2 + i * k;
//			if (i != j)
//				arrayOfFuture[(i1++)] = ConcurrencyUtils.submit(new Runnable(
//						i3, i2, paramInt1, paramArrayOfDouble1,
//						paramArrayOfDouble2, paramInt3) {
//					public void run() {
//						int i1 = this.val$firstIdx + this.val$mf;
//						int l = this.val$n;
//						while (l > 512) {
//							l >>= 2;
//							DoubleFFT_1D.this.cftmdl1(l, this.val$a, i1 - l,
//									this.val$w, this.val$nw - (l >> 1));
//						}
//						DoubleFFT_1D.this.cftleaf(l, 1, this.val$a, i1 - l,
//								this.val$nw, this.val$w);
//						int k = 0;
//						int i2 = this.val$firstIdx - l;
//						int j = this.val$mf - l;
//						while (j > 0) {
//							int i = DoubleFFT_1D.this.cfttree(l, j, ++k,
//									this.val$a, this.val$firstIdx, this.val$nw,
//									this.val$w);
//							DoubleFFT_1D.this.cftleaf(l, i, this.val$a, i2 + j,
//									this.val$nw, this.val$w);
//							j -= l;
//						}
//					}
//				});
//			else
//				arrayOfFuture[(i1++)] = ConcurrencyUtils.submit(new Runnable(
//						i3, i2, paramInt1, paramArrayOfDouble1,
//						paramArrayOfDouble2, paramInt3) {
//					public void run() {
//						int i1 = this.val$firstIdx + this.val$mf;
//						int k = 1;
//						int l = this.val$n;
//						while (l > 512) {
//							l >>= 2;
//							k <<= 2;
//							DoubleFFT_1D.this.cftmdl2(l, this.val$a, i1 - l,
//									this.val$w, this.val$nw - l);
//						}
//						DoubleFFT_1D.this.cftleaf(l, 0, this.val$a, i1 - l,
//								this.val$nw, this.val$w);
//						k >>= 1;
//						int i2 = this.val$firstIdx - l;
//						int j = this.val$mf - l;
//						while (j > 0) {
//							int i = DoubleFFT_1D.this.cfttree(l, j, ++k,
//									this.val$a, this.val$firstIdx, this.val$nw,
//									this.val$w);
//							DoubleFFT_1D.this.cftleaf(l, i, this.val$a, i2 + j,
//									this.val$nw, this.val$w);
//							j -= l;
//						}
//					}
//				});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private void cftrec4(int paramInt1, double[] paramArrayOfDouble1,
//			int paramInt2, int paramInt3, double[] paramArrayOfDouble2) {
//		int l = paramInt1;
//		int i1 = paramInt2 + paramInt1;
//		while (l > 512) {
//			l >>= 2;
//			cftmdl1(l, paramArrayOfDouble1, i1 - l, paramArrayOfDouble2,
//					paramInt3 - (l >> 1));
//		}
//		cftleaf(l, 1, paramArrayOfDouble1, i1 - l, paramInt3,
//				paramArrayOfDouble2);
//		int k = 0;
//		int i2 = paramInt2 - l;
//		int j = paramInt1 - l;
//		while (j > 0) {
//			int i = cfttree(l, j, ++k, paramArrayOfDouble1, paramInt2,
//					paramInt3, paramArrayOfDouble2);
//			cftleaf(l, i, paramArrayOfDouble1, i2 + j, paramInt3,
//					paramArrayOfDouble2);
//			j -= l;
//		}
//	}
//
//	private int cfttree(int paramInt1, int paramInt2, int paramInt3,
//			double[] paramArrayOfDouble1, int paramInt4, int paramInt5,
//			double[] paramArrayOfDouble2) {
//		int l = paramInt4 - paramInt1;
//		int j;
//		if ((paramInt3 & 0x3) != 0) {
//			j = paramInt3 & 0x1;
//			if (j != 0)
//				cftmdl1(paramInt1, paramArrayOfDouble1, l + paramInt2,
//						paramArrayOfDouble2, paramInt5 - (paramInt1 >> 1));
//			else
//				cftmdl2(paramInt1, paramArrayOfDouble1, l + paramInt2,
//						paramArrayOfDouble2, paramInt5 - paramInt1);
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
//					cftmdl1(k, paramArrayOfDouble1, i1 - k,
//							paramArrayOfDouble2, paramInt5 - (k >> 1));
//					k >>= 2;
//				}
//			while (k > 128) {
//				cftmdl2(k, paramArrayOfDouble1, i1 - k, paramArrayOfDouble2,
//						paramInt5 - k);
//				k >>= 2;
//			}
//		}
//		label185: return j;
//	}
//
//	private void cftleaf(int paramInt1, int paramInt2,
//			double[] paramArrayOfDouble1, int paramInt3, int paramInt4,
//			double[] paramArrayOfDouble2) {
//		if (paramInt1 == 512) {
//			cftmdl1(128, paramArrayOfDouble1, paramInt3, paramArrayOfDouble2,
//					paramInt4 - 64);
//			cftf161(paramArrayOfDouble1, paramInt3, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf162(paramArrayOfDouble1, paramInt3 + 32, paramArrayOfDouble2,
//					paramInt4 - 32);
//			cftf161(paramArrayOfDouble1, paramInt3 + 64, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf161(paramArrayOfDouble1, paramInt3 + 96, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftmdl2(128, paramArrayOfDouble1, paramInt3 + 128,
//					paramArrayOfDouble2, paramInt4 - 128);
//			cftf161(paramArrayOfDouble1, paramInt3 + 128, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf162(paramArrayOfDouble1, paramInt3 + 160, paramArrayOfDouble2,
//					paramInt4 - 32);
//			cftf161(paramArrayOfDouble1, paramInt3 + 192, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf162(paramArrayOfDouble1, paramInt3 + 224, paramArrayOfDouble2,
//					paramInt4 - 32);
//			cftmdl1(128, paramArrayOfDouble1, paramInt3 + 256,
//					paramArrayOfDouble2, paramInt4 - 64);
//			cftf161(paramArrayOfDouble1, paramInt3 + 256, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf162(paramArrayOfDouble1, paramInt3 + 288, paramArrayOfDouble2,
//					paramInt4 - 32);
//			cftf161(paramArrayOfDouble1, paramInt3 + 320, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf161(paramArrayOfDouble1, paramInt3 + 352, paramArrayOfDouble2,
//					paramInt4 - 8);
//			if (paramInt2 != 0) {
//				cftmdl1(128, paramArrayOfDouble1, paramInt3 + 384,
//						paramArrayOfDouble2, paramInt4 - 64);
//				cftf161(paramArrayOfDouble1, paramInt3 + 480,
//						paramArrayOfDouble2, paramInt4 - 8);
//			} else {
//				cftmdl2(128, paramArrayOfDouble1, paramInt3 + 384,
//						paramArrayOfDouble2, paramInt4 - 128);
//				cftf162(paramArrayOfDouble1, paramInt3 + 480,
//						paramArrayOfDouble2, paramInt4 - 32);
//			}
//			cftf161(paramArrayOfDouble1, paramInt3 + 384, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf162(paramArrayOfDouble1, paramInt3 + 416, paramArrayOfDouble2,
//					paramInt4 - 32);
//			cftf161(paramArrayOfDouble1, paramInt3 + 448, paramArrayOfDouble2,
//					paramInt4 - 8);
//		} else {
//			cftmdl1(64, paramArrayOfDouble1, paramInt3, paramArrayOfDouble2,
//					paramInt4 - 32);
//			cftf081(paramArrayOfDouble1, paramInt3, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf082(paramArrayOfDouble1, paramInt3 + 16, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfDouble1, paramInt3 + 32, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfDouble1, paramInt3 + 48, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftmdl2(64, paramArrayOfDouble1, paramInt3 + 64,
//					paramArrayOfDouble2, paramInt4 - 64);
//			cftf081(paramArrayOfDouble1, paramInt3 + 64, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf082(paramArrayOfDouble1, paramInt3 + 80, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfDouble1, paramInt3 + 96, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf082(paramArrayOfDouble1, paramInt3 + 112, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftmdl1(64, paramArrayOfDouble1, paramInt3 + 128,
//					paramArrayOfDouble2, paramInt4 - 32);
//			cftf081(paramArrayOfDouble1, paramInt3 + 128, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf082(paramArrayOfDouble1, paramInt3 + 144, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfDouble1, paramInt3 + 160, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfDouble1, paramInt3 + 176, paramArrayOfDouble2,
//					paramInt4 - 8);
//			if (paramInt2 != 0) {
//				cftmdl1(64, paramArrayOfDouble1, paramInt3 + 192,
//						paramArrayOfDouble2, paramInt4 - 32);
//				cftf081(paramArrayOfDouble1, paramInt3 + 240,
//						paramArrayOfDouble2, paramInt4 - 8);
//			} else {
//				cftmdl2(64, paramArrayOfDouble1, paramInt3 + 192,
//						paramArrayOfDouble2, paramInt4 - 64);
//				cftf082(paramArrayOfDouble1, paramInt3 + 240,
//						paramArrayOfDouble2, paramInt4 - 8);
//			}
//			cftf081(paramArrayOfDouble1, paramInt3 + 192, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf082(paramArrayOfDouble1, paramInt3 + 208, paramArrayOfDouble2,
//					paramInt4 - 8);
//			cftf081(paramArrayOfDouble1, paramInt3 + 224, paramArrayOfDouble2,
//					paramInt4 - 8);
//		}
//	}
//
//	private void cftmdl1(int paramInt1, double[] paramArrayOfDouble1,
//			int paramInt2, double[] paramArrayOfDouble2, int paramInt3) {
//		int i3 = paramInt1 >> 3;
//		int i2 = 2 * i3;
//		int j = i2;
//		int k = j + i2;
//		int l = k + i2;
//		int i5 = paramInt2 + j;
//		int i6 = paramInt2 + k;
//		int i7 = paramInt2 + l;
//		double d6 = paramArrayOfDouble1[paramInt2] + paramArrayOfDouble1[i6];
//		double d7 = paramArrayOfDouble1[(paramInt2 + 1)]
//				+ paramArrayOfDouble1[(i6 + 1)];
//		double d8 = paramArrayOfDouble1[paramInt2] - paramArrayOfDouble1[i6];
//		double d9 = paramArrayOfDouble1[(paramInt2 + 1)]
//				- paramArrayOfDouble1[(i6 + 1)];
//		double d10 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//		double d11 = paramArrayOfDouble1[(i5 + 1)]
//				+ paramArrayOfDouble1[(i7 + 1)];
//		double d12 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//		double d13 = paramArrayOfDouble1[(i5 + 1)]
//				- paramArrayOfDouble1[(i7 + 1)];
//		paramArrayOfDouble1[paramInt2] = (d6 + d10);
//		paramArrayOfDouble1[(paramInt2 + 1)] = (d7 + d11);
//		paramArrayOfDouble1[i5] = (d6 - d10);
//		paramArrayOfDouble1[(i5 + 1)] = (d7 - d11);
//		paramArrayOfDouble1[i6] = (d8 - d13);
//		paramArrayOfDouble1[(i6 + 1)] = (d9 + d12);
//		paramArrayOfDouble1[i7] = (d8 + d13);
//		paramArrayOfDouble1[(i7 + 1)] = (d9 - d12);
//		double d1 = paramArrayOfDouble2[(paramInt3 + 1)];
//		int i1 = 0;
//		for (int i10 = 2; i10 < i3; i10 += 2) {
//			int i8 = paramInt3 + (i1 += 4);
//			double d2 = paramArrayOfDouble2[i8];
//			double d3 = paramArrayOfDouble2[(i8 + 1)];
//			double d4 = paramArrayOfDouble2[(i8 + 2)];
//			double d5 = paramArrayOfDouble2[(i8 + 3)];
//			j = i10 + i2;
//			k = j + i2;
//			l = k + i2;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			int i9 = paramInt2 + i10;
//			d6 = paramArrayOfDouble1[i9] + paramArrayOfDouble1[i6];
//			d7 = paramArrayOfDouble1[(i9 + 1)] + paramArrayOfDouble1[(i6 + 1)];
//			d8 = paramArrayOfDouble1[i9] - paramArrayOfDouble1[i6];
//			d9 = paramArrayOfDouble1[(i9 + 1)] - paramArrayOfDouble1[(i6 + 1)];
//			d10 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//			d11 = paramArrayOfDouble1[(i5 + 1)] + paramArrayOfDouble1[(i7 + 1)];
//			d12 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//			d13 = paramArrayOfDouble1[(i5 + 1)] - paramArrayOfDouble1[(i7 + 1)];
//			paramArrayOfDouble1[i9] = (d6 + d10);
//			paramArrayOfDouble1[(i9 + 1)] = (d7 + d11);
//			paramArrayOfDouble1[i5] = (d6 - d10);
//			paramArrayOfDouble1[(i5 + 1)] = (d7 - d11);
//			d6 = d8 - d13;
//			d7 = d9 + d12;
//			paramArrayOfDouble1[i6] = (d2 * d6 - (d3 * d7));
//			paramArrayOfDouble1[(i6 + 1)] = (d2 * d7 + d3 * d6);
//			d6 = d8 + d13;
//			d7 = d9 - d12;
//			paramArrayOfDouble1[i7] = (d4 * d6 + d5 * d7);
//			paramArrayOfDouble1[(i7 + 1)] = (d4 * d7 - (d5 * d6));
//			i = i2 - i10;
//			j = i + i2;
//			k = j + i2;
//			l = k + i2;
//			i4 = paramInt2 + i;
//			i5 = paramInt2 + j;
//			i6 = paramInt2 + k;
//			i7 = paramInt2 + l;
//			d6 = paramArrayOfDouble1[i4] + paramArrayOfDouble1[i6];
//			d7 = paramArrayOfDouble1[(i4 + 1)] + paramArrayOfDouble1[(i6 + 1)];
//			d8 = paramArrayOfDouble1[i4] - paramArrayOfDouble1[i6];
//			d9 = paramArrayOfDouble1[(i4 + 1)] - paramArrayOfDouble1[(i6 + 1)];
//			d10 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//			d11 = paramArrayOfDouble1[(i5 + 1)] + paramArrayOfDouble1[(i7 + 1)];
//			d12 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//			d13 = paramArrayOfDouble1[(i5 + 1)] - paramArrayOfDouble1[(i7 + 1)];
//			paramArrayOfDouble1[i4] = (d6 + d10);
//			paramArrayOfDouble1[(i4 + 1)] = (d7 + d11);
//			paramArrayOfDouble1[i5] = (d6 - d10);
//			paramArrayOfDouble1[(i5 + 1)] = (d7 - d11);
//			d6 = d8 - d13;
//			d7 = d9 + d12;
//			paramArrayOfDouble1[i6] = (d3 * d6 - (d2 * d7));
//			paramArrayOfDouble1[(i6 + 1)] = (d3 * d7 + d2 * d6);
//			d6 = d8 + d13;
//			d7 = d9 - d12;
//			paramArrayOfDouble1[i7] = (d5 * d6 + d4 * d7);
//			paramArrayOfDouble1[(i7 + 1)] = (d5 * d7 - (d4 * d6));
//		}
//		int i = i3;
//		j = i + i2;
//		k = j + i2;
//		l = k + i2;
//		int i4 = paramInt2 + i;
//		i5 = paramInt2 + j;
//		i6 = paramInt2 + k;
//		i7 = paramInt2 + l;
//		d6 = paramArrayOfDouble1[i4] + paramArrayOfDouble1[i6];
//		d7 = paramArrayOfDouble1[(i4 + 1)] + paramArrayOfDouble1[(i6 + 1)];
//		d8 = paramArrayOfDouble1[i4] - paramArrayOfDouble1[i6];
//		d9 = paramArrayOfDouble1[(i4 + 1)] - paramArrayOfDouble1[(i6 + 1)];
//		d10 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[i7];
//		d11 = paramArrayOfDouble1[(i5 + 1)] + paramArrayOfDouble1[(i7 + 1)];
//		d12 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[i7];
//		d13 = paramArrayOfDouble1[(i5 + 1)] - paramArrayOfDouble1[(i7 + 1)];
//		paramArrayOfDouble1[i4] = (d6 + d10);
//		paramArrayOfDouble1[(i4 + 1)] = (d7 + d11);
//		paramArrayOfDouble1[i5] = (d6 - d10);
//		paramArrayOfDouble1[(i5 + 1)] = (d7 - d11);
//		d6 = d8 - d13;
//		d7 = d9 + d12;
//		paramArrayOfDouble1[i6] = (d1 * (d6 - d7));
//		paramArrayOfDouble1[(i6 + 1)] = (d1 * (d7 + d6));
//		d6 = d8 + d13;
//		d7 = d9 - d12;
//		paramArrayOfDouble1[i7] = (-d1 * (d6 + d7));
//		paramArrayOfDouble1[(i7 + 1)] = (-d1 * (d7 - d6));
//	}
//
//	private void cftmdl2(int paramInt1, double[] paramArrayOfDouble1,
//			int paramInt2, double[] paramArrayOfDouble2, int paramInt3) {
//		int i4 = paramInt1 >> 3;
//		int i3 = 2 * i4;
//		double d1 = paramArrayOfDouble2[(paramInt3 + 1)];
//		int j = i3;
//		int k = j + i3;
//		int l = k + i3;
//		int i6 = paramInt2 + j;
//		int i7 = paramInt2 + k;
//		int i8 = paramInt2 + l;
//		double d10 = paramArrayOfDouble1[paramInt2]
//				- paramArrayOfDouble1[(i7 + 1)];
//		double d11 = paramArrayOfDouble1[(paramInt2 + 1)]
//				+ paramArrayOfDouble1[i7];
//		double d12 = paramArrayOfDouble1[paramInt2]
//				+ paramArrayOfDouble1[(i7 + 1)];
//		double d13 = paramArrayOfDouble1[(paramInt2 + 1)]
//				- paramArrayOfDouble1[i7];
//		double d14 = paramArrayOfDouble1[i6] - paramArrayOfDouble1[(i8 + 1)];
//		double d15 = paramArrayOfDouble1[(i6 + 1)] + paramArrayOfDouble1[i8];
//		double d16 = paramArrayOfDouble1[i6] + paramArrayOfDouble1[(i8 + 1)];
//		double d17 = paramArrayOfDouble1[(i6 + 1)] - paramArrayOfDouble1[i8];
//		double d18 = d1 * (d14 - d15);
//		double d19 = d1 * (d15 + d14);
//		paramArrayOfDouble1[paramInt2] = (d10 + d18);
//		paramArrayOfDouble1[(paramInt2 + 1)] = (d11 + d19);
//		paramArrayOfDouble1[i6] = (d10 - d18);
//		paramArrayOfDouble1[(i6 + 1)] = (d11 - d19);
//		d18 = d1 * (d16 - d17);
//		d19 = d1 * (d17 + d16);
//		paramArrayOfDouble1[i7] = (d12 - d19);
//		paramArrayOfDouble1[(i7 + 1)] = (d13 + d18);
//		paramArrayOfDouble1[i8] = (d12 + d19);
//		paramArrayOfDouble1[(i8 + 1)] = (d13 - d18);
//		int i1 = 0;
//		int i2 = 2 * i3;
//		for (int i12 = 2; i12 < i4; i12 += 2) {
//			int i9 = paramInt3 + (i1 += 4);
//			d2 = paramArrayOfDouble2[i9];
//			d3 = paramArrayOfDouble2[(i9 + 1)];
//			double d4 = paramArrayOfDouble2[(i9 + 2)];
//			double d5 = paramArrayOfDouble2[(i9 + 3)];
//			int i10 = paramInt3 + (i2 -= 4);
//			double d7 = paramArrayOfDouble2[i10];
//			double d6 = paramArrayOfDouble2[(i10 + 1)];
//			double d9 = paramArrayOfDouble2[(i10 + 2)];
//			double d8 = paramArrayOfDouble2[(i10 + 3)];
//			j = i12 + i3;
//			k = j + i3;
//			l = k + i3;
//			i6 = paramInt2 + j;
//			i7 = paramInt2 + k;
//			i8 = paramInt2 + l;
//			int i11 = paramInt2 + i12;
//			d10 = paramArrayOfDouble1[i11] - paramArrayOfDouble1[(i7 + 1)];
//			d11 = paramArrayOfDouble1[(i11 + 1)] + paramArrayOfDouble1[i7];
//			d12 = paramArrayOfDouble1[i11] + paramArrayOfDouble1[(i7 + 1)];
//			d13 = paramArrayOfDouble1[(i11 + 1)] - paramArrayOfDouble1[i7];
//			d14 = paramArrayOfDouble1[i6] - paramArrayOfDouble1[(i8 + 1)];
//			d15 = paramArrayOfDouble1[(i6 + 1)] + paramArrayOfDouble1[i8];
//			d16 = paramArrayOfDouble1[i6] + paramArrayOfDouble1[(i8 + 1)];
//			d17 = paramArrayOfDouble1[(i6 + 1)] - paramArrayOfDouble1[i8];
//			d18 = d2 * d10 - (d3 * d11);
//			d19 = d2 * d11 + d3 * d10;
//			d20 = d6 * d14 - (d7 * d15);
//			d21 = d6 * d15 + d7 * d14;
//			paramArrayOfDouble1[i11] = (d18 + d20);
//			paramArrayOfDouble1[(i11 + 1)] = (d19 + d21);
//			paramArrayOfDouble1[i6] = (d18 - d20);
//			paramArrayOfDouble1[(i6 + 1)] = (d19 - d21);
//			d18 = d4 * d12 + d5 * d13;
//			d19 = d4 * d13 - (d5 * d12);
//			d20 = d8 * d16 + d9 * d17;
//			d21 = d8 * d17 - (d9 * d16);
//			paramArrayOfDouble1[i7] = (d18 + d20);
//			paramArrayOfDouble1[(i7 + 1)] = (d19 + d21);
//			paramArrayOfDouble1[i8] = (d18 - d20);
//			paramArrayOfDouble1[(i8 + 1)] = (d19 - d21);
//			i = i3 - i12;
//			j = i + i3;
//			k = j + i3;
//			l = k + i3;
//			i5 = paramInt2 + i;
//			i6 = paramInt2 + j;
//			i7 = paramInt2 + k;
//			i8 = paramInt2 + l;
//			d10 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[(i7 + 1)];
//			d11 = paramArrayOfDouble1[(i5 + 1)] + paramArrayOfDouble1[i7];
//			d12 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[(i7 + 1)];
//			d13 = paramArrayOfDouble1[(i5 + 1)] - paramArrayOfDouble1[i7];
//			d14 = paramArrayOfDouble1[i6] - paramArrayOfDouble1[(i8 + 1)];
//			d15 = paramArrayOfDouble1[(i6 + 1)] + paramArrayOfDouble1[i8];
//			d16 = paramArrayOfDouble1[i6] + paramArrayOfDouble1[(i8 + 1)];
//			d17 = paramArrayOfDouble1[(i6 + 1)] - paramArrayOfDouble1[i8];
//			d18 = d7 * d10 - (d6 * d11);
//			d19 = d7 * d11 + d6 * d10;
//			d20 = d3 * d14 - (d2 * d15);
//			d21 = d3 * d15 + d2 * d14;
//			paramArrayOfDouble1[i5] = (d18 + d20);
//			paramArrayOfDouble1[(i5 + 1)] = (d19 + d21);
//			paramArrayOfDouble1[i6] = (d18 - d20);
//			paramArrayOfDouble1[(i6 + 1)] = (d19 - d21);
//			d18 = d9 * d12 + d8 * d13;
//			d19 = d9 * d13 - (d8 * d12);
//			d20 = d5 * d16 + d4 * d17;
//			d21 = d5 * d17 - (d4 * d16);
//			paramArrayOfDouble1[i7] = (d18 + d20);
//			paramArrayOfDouble1[(i7 + 1)] = (d19 + d21);
//			paramArrayOfDouble1[i8] = (d18 - d20);
//			paramArrayOfDouble1[(i8 + 1)] = (d19 - d21);
//		}
//		double d2 = paramArrayOfDouble2[(paramInt3 + i3)];
//		double d3 = paramArrayOfDouble2[(paramInt3 + i3 + 1)];
//		int i = i4;
//		j = i + i3;
//		k = j + i3;
//		l = k + i3;
//		int i5 = paramInt2 + i;
//		i6 = paramInt2 + j;
//		i7 = paramInt2 + k;
//		i8 = paramInt2 + l;
//		d10 = paramArrayOfDouble1[i5] - paramArrayOfDouble1[(i7 + 1)];
//		d11 = paramArrayOfDouble1[(i5 + 1)] + paramArrayOfDouble1[i7];
//		d12 = paramArrayOfDouble1[i5] + paramArrayOfDouble1[(i7 + 1)];
//		d13 = paramArrayOfDouble1[(i5 + 1)] - paramArrayOfDouble1[i7];
//		d14 = paramArrayOfDouble1[i6] - paramArrayOfDouble1[(i8 + 1)];
//		d15 = paramArrayOfDouble1[(i6 + 1)] + paramArrayOfDouble1[i8];
//		d16 = paramArrayOfDouble1[i6] + paramArrayOfDouble1[(i8 + 1)];
//		d17 = paramArrayOfDouble1[(i6 + 1)] - paramArrayOfDouble1[i8];
//		d18 = d2 * d10 - (d3 * d11);
//		d19 = d2 * d11 + d3 * d10;
//		double d20 = d3 * d14 - (d2 * d15);
//		double d21 = d3 * d15 + d2 * d14;
//		paramArrayOfDouble1[i5] = (d18 + d20);
//		paramArrayOfDouble1[(i5 + 1)] = (d19 + d21);
//		paramArrayOfDouble1[i6] = (d18 - d20);
//		paramArrayOfDouble1[(i6 + 1)] = (d19 - d21);
//		d18 = d3 * d12 - (d2 * d13);
//		d19 = d3 * d13 + d2 * d12;
//		d20 = d2 * d16 - (d3 * d17);
//		d21 = d2 * d17 + d3 * d16;
//		paramArrayOfDouble1[i7] = (d18 - d20);
//		paramArrayOfDouble1[(i7 + 1)] = (d19 - d21);
//		paramArrayOfDouble1[i8] = (d18 + d20);
//		paramArrayOfDouble1[(i8 + 1)] = (d19 + d21);
//	}
//
//	private void cftfx41(int paramInt1, double[] paramArrayOfDouble1,
//			int paramInt2, int paramInt3, double[] paramArrayOfDouble2) {
//		if (paramInt1 == 128) {
//			cftf161(paramArrayOfDouble1, paramInt2, paramArrayOfDouble2,
//					paramInt3 - 8);
//			cftf162(paramArrayOfDouble1, paramInt2 + 32, paramArrayOfDouble2,
//					paramInt3 - 32);
//			cftf161(paramArrayOfDouble1, paramInt2 + 64, paramArrayOfDouble2,
//					paramInt3 - 8);
//			cftf161(paramArrayOfDouble1, paramInt2 + 96, paramArrayOfDouble2,
//					paramInt3 - 8);
//		} else {
//			cftf081(paramArrayOfDouble1, paramInt2, paramArrayOfDouble2,
//					paramInt3 - 8);
//			cftf082(paramArrayOfDouble1, paramInt2 + 16, paramArrayOfDouble2,
//					paramInt3 - 8);
//			cftf081(paramArrayOfDouble1, paramInt2 + 32, paramArrayOfDouble2,
//					paramInt3 - 8);
//			cftf081(paramArrayOfDouble1, paramInt2 + 48, paramArrayOfDouble2,
//					paramInt3 - 8);
//		}
//	}
//
//	private void cftf161(double[] paramArrayOfDouble1, int paramInt1,
//			double[] paramArrayOfDouble2, int paramInt2) {
//		double d1 = paramArrayOfDouble2[(paramInt2 + 1)];
//		double d2 = paramArrayOfDouble2[(paramInt2 + 2)];
//		double d3 = paramArrayOfDouble2[(paramInt2 + 3)];
//		double d4 = paramArrayOfDouble1[paramInt1]
//				+ paramArrayOfDouble1[(paramInt1 + 16)];
//		double d5 = paramArrayOfDouble1[(paramInt1 + 1)]
//				+ paramArrayOfDouble1[(paramInt1 + 17)];
//		double d6 = paramArrayOfDouble1[paramInt1]
//				- paramArrayOfDouble1[(paramInt1 + 16)];
//		double d7 = paramArrayOfDouble1[(paramInt1 + 1)]
//				- paramArrayOfDouble1[(paramInt1 + 17)];
//		double d8 = paramArrayOfDouble1[(paramInt1 + 8)]
//				+ paramArrayOfDouble1[(paramInt1 + 24)];
//		double d9 = paramArrayOfDouble1[(paramInt1 + 9)]
//				+ paramArrayOfDouble1[(paramInt1 + 25)];
//		double d10 = paramArrayOfDouble1[(paramInt1 + 8)]
//				- paramArrayOfDouble1[(paramInt1 + 24)];
//		double d11 = paramArrayOfDouble1[(paramInt1 + 9)]
//				- paramArrayOfDouble1[(paramInt1 + 25)];
//		double d12 = d4 + d8;
//		double d13 = d5 + d9;
//		double d20 = d4 - d8;
//		double d21 = d5 - d9;
//		double d28 = d6 - d11;
//		double d29 = d7 + d10;
//		double d36 = d6 + d11;
//		double d37 = d7 - d10;
//		d4 = paramArrayOfDouble1[(paramInt1 + 2)]
//				+ paramArrayOfDouble1[(paramInt1 + 18)];
//		d5 = paramArrayOfDouble1[(paramInt1 + 3)]
//				+ paramArrayOfDouble1[(paramInt1 + 19)];
//		d6 = paramArrayOfDouble1[(paramInt1 + 2)]
//				- paramArrayOfDouble1[(paramInt1 + 18)];
//		d7 = paramArrayOfDouble1[(paramInt1 + 3)]
//				- paramArrayOfDouble1[(paramInt1 + 19)];
//		d8 = paramArrayOfDouble1[(paramInt1 + 10)]
//				+ paramArrayOfDouble1[(paramInt1 + 26)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 11)]
//				+ paramArrayOfDouble1[(paramInt1 + 27)];
//		d10 = paramArrayOfDouble1[(paramInt1 + 10)]
//				- paramArrayOfDouble1[(paramInt1 + 26)];
//		d11 = paramArrayOfDouble1[(paramInt1 + 11)]
//				- paramArrayOfDouble1[(paramInt1 + 27)];
//		double d14 = d4 + d8;
//		double d15 = d5 + d9;
//		double d22 = d4 - d8;
//		double d23 = d5 - d9;
//		d4 = d6 - d11;
//		d5 = d7 + d10;
//		double d30 = d2 * d4 - (d3 * d5);
//		double d31 = d2 * d5 + d3 * d4;
//		d4 = d6 + d11;
//		d5 = d7 - d10;
//		double d38 = d3 * d4 - (d2 * d5);
//		double d39 = d3 * d5 + d2 * d4;
//		d4 = paramArrayOfDouble1[(paramInt1 + 4)]
//				+ paramArrayOfDouble1[(paramInt1 + 20)];
//		d5 = paramArrayOfDouble1[(paramInt1 + 5)]
//				+ paramArrayOfDouble1[(paramInt1 + 21)];
//		d6 = paramArrayOfDouble1[(paramInt1 + 4)]
//				- paramArrayOfDouble1[(paramInt1 + 20)];
//		d7 = paramArrayOfDouble1[(paramInt1 + 5)]
//				- paramArrayOfDouble1[(paramInt1 + 21)];
//		d8 = paramArrayOfDouble1[(paramInt1 + 12)]
//				+ paramArrayOfDouble1[(paramInt1 + 28)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 13)]
//				+ paramArrayOfDouble1[(paramInt1 + 29)];
//		d10 = paramArrayOfDouble1[(paramInt1 + 12)]
//				- paramArrayOfDouble1[(paramInt1 + 28)];
//		d11 = paramArrayOfDouble1[(paramInt1 + 13)]
//				- paramArrayOfDouble1[(paramInt1 + 29)];
//		double d16 = d4 + d8;
//		double d17 = d5 + d9;
//		double d24 = d4 - d8;
//		double d25 = d5 - d9;
//		d4 = d6 - d11;
//		d5 = d7 + d10;
//		double d32 = d1 * (d4 - d5);
//		double d33 = d1 * (d5 + d4);
//		d4 = d6 + d11;
//		d5 = d7 - d10;
//		double d40 = d1 * (d4 + d5);
//		double d41 = d1 * (d5 - d4);
//		d4 = paramArrayOfDouble1[(paramInt1 + 6)]
//				+ paramArrayOfDouble1[(paramInt1 + 22)];
//		d5 = paramArrayOfDouble1[(paramInt1 + 7)]
//				+ paramArrayOfDouble1[(paramInt1 + 23)];
//		d6 = paramArrayOfDouble1[(paramInt1 + 6)]
//				- paramArrayOfDouble1[(paramInt1 + 22)];
//		d7 = paramArrayOfDouble1[(paramInt1 + 7)]
//				- paramArrayOfDouble1[(paramInt1 + 23)];
//		d8 = paramArrayOfDouble1[(paramInt1 + 14)]
//				+ paramArrayOfDouble1[(paramInt1 + 30)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 15)]
//				+ paramArrayOfDouble1[(paramInt1 + 31)];
//		d10 = paramArrayOfDouble1[(paramInt1 + 14)]
//				- paramArrayOfDouble1[(paramInt1 + 30)];
//		d11 = paramArrayOfDouble1[(paramInt1 + 15)]
//				- paramArrayOfDouble1[(paramInt1 + 31)];
//		double d18 = d4 + d8;
//		double d19 = d5 + d9;
//		double d26 = d4 - d8;
//		double d27 = d5 - d9;
//		d4 = d6 - d11;
//		d5 = d7 + d10;
//		double d34 = d3 * d4 - (d2 * d5);
//		double d35 = d3 * d5 + d2 * d4;
//		d4 = d6 + d11;
//		d5 = d7 - d10;
//		double d42 = d2 * d4 - (d3 * d5);
//		double d43 = d2 * d5 + d3 * d4;
//		d4 = d36 - d40;
//		d5 = d37 - d41;
//		d6 = d36 + d40;
//		d7 = d37 + d41;
//		d8 = d38 - d42;
//		d9 = d39 - d43;
//		d10 = d38 + d42;
//		d11 = d39 + d43;
//		paramArrayOfDouble1[(paramInt1 + 24)] = (d4 + d8);
//		paramArrayOfDouble1[(paramInt1 + 25)] = (d5 + d9);
//		paramArrayOfDouble1[(paramInt1 + 26)] = (d4 - d8);
//		paramArrayOfDouble1[(paramInt1 + 27)] = (d5 - d9);
//		paramArrayOfDouble1[(paramInt1 + 28)] = (d6 - d11);
//		paramArrayOfDouble1[(paramInt1 + 29)] = (d7 + d10);
//		paramArrayOfDouble1[(paramInt1 + 30)] = (d6 + d11);
//		paramArrayOfDouble1[(paramInt1 + 31)] = (d7 - d10);
//		d4 = d28 + d32;
//		d5 = d29 + d33;
//		d6 = d28 - d32;
//		d7 = d29 - d33;
//		d8 = d30 + d34;
//		d9 = d31 + d35;
//		d10 = d30 - d34;
//		d11 = d31 - d35;
//		paramArrayOfDouble1[(paramInt1 + 16)] = (d4 + d8);
//		paramArrayOfDouble1[(paramInt1 + 17)] = (d5 + d9);
//		paramArrayOfDouble1[(paramInt1 + 18)] = (d4 - d8);
//		paramArrayOfDouble1[(paramInt1 + 19)] = (d5 - d9);
//		paramArrayOfDouble1[(paramInt1 + 20)] = (d6 - d11);
//		paramArrayOfDouble1[(paramInt1 + 21)] = (d7 + d10);
//		paramArrayOfDouble1[(paramInt1 + 22)] = (d6 + d11);
//		paramArrayOfDouble1[(paramInt1 + 23)] = (d7 - d10);
//		d4 = d22 - d27;
//		d5 = d23 + d26;
//		d8 = d1 * (d4 - d5);
//		d9 = d1 * (d5 + d4);
//		d4 = d22 + d27;
//		d5 = d23 - d26;
//		d10 = d1 * (d4 - d5);
//		d11 = d1 * (d5 + d4);
//		d4 = d20 - d25;
//		d5 = d21 + d24;
//		d6 = d20 + d25;
//		d7 = d21 - d24;
//		paramArrayOfDouble1[(paramInt1 + 8)] = (d4 + d8);
//		paramArrayOfDouble1[(paramInt1 + 9)] = (d5 + d9);
//		paramArrayOfDouble1[(paramInt1 + 10)] = (d4 - d8);
//		paramArrayOfDouble1[(paramInt1 + 11)] = (d5 - d9);
//		paramArrayOfDouble1[(paramInt1 + 12)] = (d6 - d11);
//		paramArrayOfDouble1[(paramInt1 + 13)] = (d7 + d10);
//		paramArrayOfDouble1[(paramInt1 + 14)] = (d6 + d11);
//		paramArrayOfDouble1[(paramInt1 + 15)] = (d7 - d10);
//		d4 = d12 + d16;
//		d5 = d13 + d17;
//		d6 = d12 - d16;
//		d7 = d13 - d17;
//		d8 = d14 + d18;
//		d9 = d15 + d19;
//		d10 = d14 - d18;
//		d11 = d15 - d19;
//		paramArrayOfDouble1[paramInt1] = (d4 + d8);
//		paramArrayOfDouble1[(paramInt1 + 1)] = (d5 + d9);
//		paramArrayOfDouble1[(paramInt1 + 2)] = (d4 - d8);
//		paramArrayOfDouble1[(paramInt1 + 3)] = (d5 - d9);
//		paramArrayOfDouble1[(paramInt1 + 4)] = (d6 - d11);
//		paramArrayOfDouble1[(paramInt1 + 5)] = (d7 + d10);
//		paramArrayOfDouble1[(paramInt1 + 6)] = (d6 + d11);
//		paramArrayOfDouble1[(paramInt1 + 7)] = (d7 - d10);
//	}
//
//	private void cftf162(double[] paramArrayOfDouble1, int paramInt1,
//			double[] paramArrayOfDouble2, int paramInt2) {
//		double d1 = paramArrayOfDouble2[(paramInt2 + 1)];
//		double d2 = paramArrayOfDouble2[(paramInt2 + 4)];
//		double d3 = paramArrayOfDouble2[(paramInt2 + 5)];
//		double d6 = paramArrayOfDouble2[(paramInt2 + 6)];
//		double d7 = -paramArrayOfDouble2[(paramInt2 + 7)];
//		double d4 = paramArrayOfDouble2[(paramInt2 + 8)];
//		double d5 = paramArrayOfDouble2[(paramInt2 + 9)];
//		double d10 = paramArrayOfDouble1[paramInt1]
//				- paramArrayOfDouble1[(paramInt1 + 17)];
//		double d11 = paramArrayOfDouble1[(paramInt1 + 1)]
//				+ paramArrayOfDouble1[(paramInt1 + 16)];
//		double d8 = paramArrayOfDouble1[(paramInt1 + 8)]
//				- paramArrayOfDouble1[(paramInt1 + 25)];
//		double d9 = paramArrayOfDouble1[(paramInt1 + 9)]
//				+ paramArrayOfDouble1[(paramInt1 + 24)];
//		double d12 = d1 * (d8 - d9);
//		double d13 = d1 * (d9 + d8);
//		double d14 = d10 + d12;
//		double d15 = d11 + d13;
//		double d22 = d10 - d12;
//		double d23 = d11 - d13;
//		d10 = paramArrayOfDouble1[paramInt1]
//				+ paramArrayOfDouble1[(paramInt1 + 17)];
//		d11 = paramArrayOfDouble1[(paramInt1 + 1)]
//				- paramArrayOfDouble1[(paramInt1 + 16)];
//		d8 = paramArrayOfDouble1[(paramInt1 + 8)]
//				+ paramArrayOfDouble1[(paramInt1 + 25)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 9)]
//				- paramArrayOfDouble1[(paramInt1 + 24)];
//		d12 = d1 * (d8 - d9);
//		d13 = d1 * (d9 + d8);
//		double d30 = d10 - d13;
//		double d31 = d11 + d12;
//		double d38 = d10 + d13;
//		double d39 = d11 - d12;
//		d8 = paramArrayOfDouble1[(paramInt1 + 2)]
//				- paramArrayOfDouble1[(paramInt1 + 19)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 3)]
//				+ paramArrayOfDouble1[(paramInt1 + 18)];
//		d10 = d2 * d8 - (d3 * d9);
//		d11 = d2 * d9 + d3 * d8;
//		d8 = paramArrayOfDouble1[(paramInt1 + 10)]
//				- paramArrayOfDouble1[(paramInt1 + 27)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 11)]
//				+ paramArrayOfDouble1[(paramInt1 + 26)];
//		d12 = d7 * d8 - (d6 * d9);
//		d13 = d7 * d9 + d6 * d8;
//		double d16 = d10 + d12;
//		double d17 = d11 + d13;
//		double d24 = d10 - d12;
//		double d25 = d11 - d13;
//		d8 = paramArrayOfDouble1[(paramInt1 + 2)]
//				+ paramArrayOfDouble1[(paramInt1 + 19)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 3)]
//				- paramArrayOfDouble1[(paramInt1 + 18)];
//		d10 = d6 * d8 - (d7 * d9);
//		d11 = d6 * d9 + d7 * d8;
//		d8 = paramArrayOfDouble1[(paramInt1 + 10)]
//				+ paramArrayOfDouble1[(paramInt1 + 27)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 11)]
//				- paramArrayOfDouble1[(paramInt1 + 26)];
//		d12 = d2 * d8 + d3 * d9;
//		d13 = d2 * d9 - (d3 * d8);
//		double d32 = d10 - d12;
//		double d33 = d11 - d13;
//		double d40 = d10 + d12;
//		double d41 = d11 + d13;
//		d8 = paramArrayOfDouble1[(paramInt1 + 4)]
//				- paramArrayOfDouble1[(paramInt1 + 21)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 5)]
//				+ paramArrayOfDouble1[(paramInt1 + 20)];
//		d10 = d4 * d8 - (d5 * d9);
//		d11 = d4 * d9 + d5 * d8;
//		d8 = paramArrayOfDouble1[(paramInt1 + 12)]
//				- paramArrayOfDouble1[(paramInt1 + 29)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 13)]
//				+ paramArrayOfDouble1[(paramInt1 + 28)];
//		d12 = d5 * d8 - (d4 * d9);
//		d13 = d5 * d9 + d4 * d8;
//		double d18 = d10 + d12;
//		double d19 = d11 + d13;
//		double d26 = d10 - d12;
//		double d27 = d11 - d13;
//		d8 = paramArrayOfDouble1[(paramInt1 + 4)]
//				+ paramArrayOfDouble1[(paramInt1 + 21)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 5)]
//				- paramArrayOfDouble1[(paramInt1 + 20)];
//		d10 = d5 * d8 - (d4 * d9);
//		d11 = d5 * d9 + d4 * d8;
//		d8 = paramArrayOfDouble1[(paramInt1 + 12)]
//				+ paramArrayOfDouble1[(paramInt1 + 29)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 13)]
//				- paramArrayOfDouble1[(paramInt1 + 28)];
//		d12 = d4 * d8 - (d5 * d9);
//		d13 = d4 * d9 + d5 * d8;
//		double d34 = d10 - d12;
//		double d35 = d11 - d13;
//		double d42 = d10 + d12;
//		double d43 = d11 + d13;
//		d8 = paramArrayOfDouble1[(paramInt1 + 6)]
//				- paramArrayOfDouble1[(paramInt1 + 23)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 7)]
//				+ paramArrayOfDouble1[(paramInt1 + 22)];
//		d10 = d6 * d8 - (d7 * d9);
//		d11 = d6 * d9 + d7 * d8;
//		d8 = paramArrayOfDouble1[(paramInt1 + 14)]
//				- paramArrayOfDouble1[(paramInt1 + 31)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 15)]
//				+ paramArrayOfDouble1[(paramInt1 + 30)];
//		d12 = d3 * d8 - (d2 * d9);
//		d13 = d3 * d9 + d2 * d8;
//		double d20 = d10 + d12;
//		double d21 = d11 + d13;
//		double d28 = d10 - d12;
//		double d29 = d11 - d13;
//		d8 = paramArrayOfDouble1[(paramInt1 + 6)]
//				+ paramArrayOfDouble1[(paramInt1 + 23)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 7)]
//				- paramArrayOfDouble1[(paramInt1 + 22)];
//		d10 = d3 * d8 + d2 * d9;
//		d11 = d3 * d9 - (d2 * d8);
//		d8 = paramArrayOfDouble1[(paramInt1 + 14)]
//				+ paramArrayOfDouble1[(paramInt1 + 31)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 15)]
//				- paramArrayOfDouble1[(paramInt1 + 30)];
//		d12 = d7 * d8 - (d6 * d9);
//		d13 = d7 * d9 + d6 * d8;
//		double d36 = d10 + d12;
//		double d37 = d11 + d13;
//		double d44 = d10 - d12;
//		double d45 = d11 - d13;
//		d10 = d14 + d18;
//		d11 = d15 + d19;
//		d12 = d16 + d20;
//		d13 = d17 + d21;
//		paramArrayOfDouble1[paramInt1] = (d10 + d12);
//		paramArrayOfDouble1[(paramInt1 + 1)] = (d11 + d13);
//		paramArrayOfDouble1[(paramInt1 + 2)] = (d10 - d12);
//		paramArrayOfDouble1[(paramInt1 + 3)] = (d11 - d13);
//		d10 = d14 - d18;
//		d11 = d15 - d19;
//		d12 = d16 - d20;
//		d13 = d17 - d21;
//		paramArrayOfDouble1[(paramInt1 + 4)] = (d10 - d13);
//		paramArrayOfDouble1[(paramInt1 + 5)] = (d11 + d12);
//		paramArrayOfDouble1[(paramInt1 + 6)] = (d10 + d13);
//		paramArrayOfDouble1[(paramInt1 + 7)] = (d11 - d12);
//		d10 = d22 - d27;
//		d11 = d23 + d26;
//		d8 = d24 - d29;
//		d9 = d25 + d28;
//		d12 = d1 * (d8 - d9);
//		d13 = d1 * (d9 + d8);
//		paramArrayOfDouble1[(paramInt1 + 8)] = (d10 + d12);
//		paramArrayOfDouble1[(paramInt1 + 9)] = (d11 + d13);
//		paramArrayOfDouble1[(paramInt1 + 10)] = (d10 - d12);
//		paramArrayOfDouble1[(paramInt1 + 11)] = (d11 - d13);
//		d10 = d22 + d27;
//		d11 = d23 - d26;
//		d8 = d24 + d29;
//		d9 = d25 - d28;
//		d12 = d1 * (d8 - d9);
//		d13 = d1 * (d9 + d8);
//		paramArrayOfDouble1[(paramInt1 + 12)] = (d10 - d13);
//		paramArrayOfDouble1[(paramInt1 + 13)] = (d11 + d12);
//		paramArrayOfDouble1[(paramInt1 + 14)] = (d10 + d13);
//		paramArrayOfDouble1[(paramInt1 + 15)] = (d11 - d12);
//		d10 = d30 + d34;
//		d11 = d31 + d35;
//		d12 = d32 - d36;
//		d13 = d33 - d37;
//		paramArrayOfDouble1[(paramInt1 + 16)] = (d10 + d12);
//		paramArrayOfDouble1[(paramInt1 + 17)] = (d11 + d13);
//		paramArrayOfDouble1[(paramInt1 + 18)] = (d10 - d12);
//		paramArrayOfDouble1[(paramInt1 + 19)] = (d11 - d13);
//		d10 = d30 - d34;
//		d11 = d31 - d35;
//		d12 = d32 + d36;
//		d13 = d33 + d37;
//		paramArrayOfDouble1[(paramInt1 + 20)] = (d10 - d13);
//		paramArrayOfDouble1[(paramInt1 + 21)] = (d11 + d12);
//		paramArrayOfDouble1[(paramInt1 + 22)] = (d10 + d13);
//		paramArrayOfDouble1[(paramInt1 + 23)] = (d11 - d12);
//		d10 = d38 - d43;
//		d11 = d39 + d42;
//		d8 = d40 + d45;
//		d9 = d41 - d44;
//		d12 = d1 * (d8 - d9);
//		d13 = d1 * (d9 + d8);
//		paramArrayOfDouble1[(paramInt1 + 24)] = (d10 + d12);
//		paramArrayOfDouble1[(paramInt1 + 25)] = (d11 + d13);
//		paramArrayOfDouble1[(paramInt1 + 26)] = (d10 - d12);
//		paramArrayOfDouble1[(paramInt1 + 27)] = (d11 - d13);
//		d10 = d38 + d43;
//		d11 = d39 - d42;
//		d8 = d40 - d45;
//		d9 = d41 + d44;
//		d12 = d1 * (d8 - d9);
//		d13 = d1 * (d9 + d8);
//		paramArrayOfDouble1[(paramInt1 + 28)] = (d10 - d13);
//		paramArrayOfDouble1[(paramInt1 + 29)] = (d11 + d12);
//		paramArrayOfDouble1[(paramInt1 + 30)] = (d10 + d13);
//		paramArrayOfDouble1[(paramInt1 + 31)] = (d11 - d12);
//	}
//
//	private void cftf081(double[] paramArrayOfDouble1, int paramInt1,
//			double[] paramArrayOfDouble2, int paramInt2) {
//		double d1 = paramArrayOfDouble2[(paramInt2 + 1)];
//		double d2 = paramArrayOfDouble1[paramInt1]
//				+ paramArrayOfDouble1[(paramInt1 + 8)];
//		double d3 = paramArrayOfDouble1[(paramInt1 + 1)]
//				+ paramArrayOfDouble1[(paramInt1 + 9)];
//		double d4 = paramArrayOfDouble1[paramInt1]
//				- paramArrayOfDouble1[(paramInt1 + 8)];
//		double d5 = paramArrayOfDouble1[(paramInt1 + 1)]
//				- paramArrayOfDouble1[(paramInt1 + 9)];
//		double d6 = paramArrayOfDouble1[(paramInt1 + 4)]
//				+ paramArrayOfDouble1[(paramInt1 + 12)];
//		double d7 = paramArrayOfDouble1[(paramInt1 + 5)]
//				+ paramArrayOfDouble1[(paramInt1 + 13)];
//		double d8 = paramArrayOfDouble1[(paramInt1 + 4)]
//				- paramArrayOfDouble1[(paramInt1 + 12)];
//		double d9 = paramArrayOfDouble1[(paramInt1 + 5)]
//				- paramArrayOfDouble1[(paramInt1 + 13)];
//		double d10 = d2 + d6;
//		double d11 = d3 + d7;
//		double d14 = d2 - d6;
//		double d15 = d3 - d7;
//		double d12 = d4 - d9;
//		double d13 = d5 + d8;
//		double d16 = d4 + d9;
//		double d17 = d5 - d8;
//		d2 = paramArrayOfDouble1[(paramInt1 + 2)]
//				+ paramArrayOfDouble1[(paramInt1 + 10)];
//		d3 = paramArrayOfDouble1[(paramInt1 + 3)]
//				+ paramArrayOfDouble1[(paramInt1 + 11)];
//		d4 = paramArrayOfDouble1[(paramInt1 + 2)]
//				- paramArrayOfDouble1[(paramInt1 + 10)];
//		d5 = paramArrayOfDouble1[(paramInt1 + 3)]
//				- paramArrayOfDouble1[(paramInt1 + 11)];
//		d6 = paramArrayOfDouble1[(paramInt1 + 6)]
//				+ paramArrayOfDouble1[(paramInt1 + 14)];
//		d7 = paramArrayOfDouble1[(paramInt1 + 7)]
//				+ paramArrayOfDouble1[(paramInt1 + 15)];
//		d8 = paramArrayOfDouble1[(paramInt1 + 6)]
//				- paramArrayOfDouble1[(paramInt1 + 14)];
//		d9 = paramArrayOfDouble1[(paramInt1 + 7)]
//				- paramArrayOfDouble1[(paramInt1 + 15)];
//		double d18 = d2 + d6;
//		double d19 = d3 + d7;
//		double d22 = d2 - d6;
//		double d23 = d3 - d7;
//		d2 = d4 - d9;
//		d3 = d5 + d8;
//		d6 = d4 + d9;
//		d7 = d5 - d8;
//		double d20 = d1 * (d2 - d3);
//		double d21 = d1 * (d2 + d3);
//		double d24 = d1 * (d6 - d7);
//		double d25 = d1 * (d6 + d7);
//		paramArrayOfDouble1[(paramInt1 + 8)] = (d12 + d20);
//		paramArrayOfDouble1[(paramInt1 + 9)] = (d13 + d21);
//		paramArrayOfDouble1[(paramInt1 + 10)] = (d12 - d20);
//		paramArrayOfDouble1[(paramInt1 + 11)] = (d13 - d21);
//		paramArrayOfDouble1[(paramInt1 + 12)] = (d16 - d25);
//		paramArrayOfDouble1[(paramInt1 + 13)] = (d17 + d24);
//		paramArrayOfDouble1[(paramInt1 + 14)] = (d16 + d25);
//		paramArrayOfDouble1[(paramInt1 + 15)] = (d17 - d24);
//		paramArrayOfDouble1[paramInt1] = (d10 + d18);
//		paramArrayOfDouble1[(paramInt1 + 1)] = (d11 + d19);
//		paramArrayOfDouble1[(paramInt1 + 2)] = (d10 - d18);
//		paramArrayOfDouble1[(paramInt1 + 3)] = (d11 - d19);
//		paramArrayOfDouble1[(paramInt1 + 4)] = (d14 - d23);
//		paramArrayOfDouble1[(paramInt1 + 5)] = (d15 + d22);
//		paramArrayOfDouble1[(paramInt1 + 6)] = (d14 + d23);
//		paramArrayOfDouble1[(paramInt1 + 7)] = (d15 - d22);
//	}
//
//	private void cftf082(double[] paramArrayOfDouble1, int paramInt1,
//			double[] paramArrayOfDouble2, int paramInt2) {
//		double d1 = paramArrayOfDouble2[(paramInt2 + 1)];
//		double d2 = paramArrayOfDouble2[(paramInt2 + 2)];
//		double d3 = paramArrayOfDouble2[(paramInt2 + 3)];
//		double d8 = paramArrayOfDouble1[paramInt1]
//				- paramArrayOfDouble1[(paramInt1 + 9)];
//		double d9 = paramArrayOfDouble1[(paramInt1 + 1)]
//				+ paramArrayOfDouble1[(paramInt1 + 8)];
//		double d10 = paramArrayOfDouble1[paramInt1]
//				+ paramArrayOfDouble1[(paramInt1 + 9)];
//		double d11 = paramArrayOfDouble1[(paramInt1 + 1)]
//				- paramArrayOfDouble1[(paramInt1 + 8)];
//		double d4 = paramArrayOfDouble1[(paramInt1 + 4)]
//				- paramArrayOfDouble1[(paramInt1 + 13)];
//		double d5 = paramArrayOfDouble1[(paramInt1 + 5)]
//				+ paramArrayOfDouble1[(paramInt1 + 12)];
//		double d12 = d1 * (d4 - d5);
//		double d13 = d1 * (d5 + d4);
//		d4 = paramArrayOfDouble1[(paramInt1 + 4)]
//				+ paramArrayOfDouble1[(paramInt1 + 13)];
//		d5 = paramArrayOfDouble1[(paramInt1 + 5)]
//				- paramArrayOfDouble1[(paramInt1 + 12)];
//		double d14 = d1 * (d4 - d5);
//		double d15 = d1 * (d5 + d4);
//		d4 = paramArrayOfDouble1[(paramInt1 + 2)]
//				- paramArrayOfDouble1[(paramInt1 + 11)];
//		d5 = paramArrayOfDouble1[(paramInt1 + 3)]
//				+ paramArrayOfDouble1[(paramInt1 + 10)];
//		double d16 = d2 * d4 - (d3 * d5);
//		double d17 = d2 * d5 + d3 * d4;
//		d4 = paramArrayOfDouble1[(paramInt1 + 2)]
//				+ paramArrayOfDouble1[(paramInt1 + 11)];
//		d5 = paramArrayOfDouble1[(paramInt1 + 3)]
//				- paramArrayOfDouble1[(paramInt1 + 10)];
//		double d18 = d3 * d4 - (d2 * d5);
//		double d19 = d3 * d5 + d2 * d4;
//		d4 = paramArrayOfDouble1[(paramInt1 + 6)]
//				- paramArrayOfDouble1[(paramInt1 + 15)];
//		d5 = paramArrayOfDouble1[(paramInt1 + 7)]
//				+ paramArrayOfDouble1[(paramInt1 + 14)];
//		double d20 = d3 * d4 - (d2 * d5);
//		double d21 = d3 * d5 + d2 * d4;
//		d4 = paramArrayOfDouble1[(paramInt1 + 6)]
//				+ paramArrayOfDouble1[(paramInt1 + 15)];
//		d5 = paramArrayOfDouble1[(paramInt1 + 7)]
//				- paramArrayOfDouble1[(paramInt1 + 14)];
//		double d22 = d2 * d4 - (d3 * d5);
//		double d23 = d2 * d5 + d3 * d4;
//		d4 = d8 + d12;
//		d5 = d9 + d13;
//		double d6 = d16 + d20;
//		double d7 = d17 + d21;
//		paramArrayOfDouble1[paramInt1] = (d4 + d6);
//		paramArrayOfDouble1[(paramInt1 + 1)] = (d5 + d7);
//		paramArrayOfDouble1[(paramInt1 + 2)] = (d4 - d6);
//		paramArrayOfDouble1[(paramInt1 + 3)] = (d5 - d7);
//		d4 = d8 - d12;
//		d5 = d9 - d13;
//		d6 = d16 - d20;
//		d7 = d17 - d21;
//		paramArrayOfDouble1[(paramInt1 + 4)] = (d4 - d7);
//		paramArrayOfDouble1[(paramInt1 + 5)] = (d5 + d6);
//		paramArrayOfDouble1[(paramInt1 + 6)] = (d4 + d7);
//		paramArrayOfDouble1[(paramInt1 + 7)] = (d5 - d6);
//		d4 = d10 - d15;
//		d5 = d11 + d14;
//		d6 = d18 - d22;
//		d7 = d19 - d23;
//		paramArrayOfDouble1[(paramInt1 + 8)] = (d4 + d6);
//		paramArrayOfDouble1[(paramInt1 + 9)] = (d5 + d7);
//		paramArrayOfDouble1[(paramInt1 + 10)] = (d4 - d6);
//		paramArrayOfDouble1[(paramInt1 + 11)] = (d5 - d7);
//		d4 = d10 + d15;
//		d5 = d11 - d14;
//		d6 = d18 + d22;
//		d7 = d19 + d23;
//		paramArrayOfDouble1[(paramInt1 + 12)] = (d4 - d7);
//		paramArrayOfDouble1[(paramInt1 + 13)] = (d5 + d6);
//		paramArrayOfDouble1[(paramInt1 + 14)] = (d4 + d7);
//		paramArrayOfDouble1[(paramInt1 + 15)] = (d5 - d6);
//	}
//
//	private void cftf040(double[] paramArrayOfDouble, int paramInt) {
//		double d1 = paramArrayOfDouble[paramInt]
//				+ paramArrayOfDouble[(paramInt + 4)];
//		double d2 = paramArrayOfDouble[(paramInt + 1)]
//				+ paramArrayOfDouble[(paramInt + 5)];
//		double d3 = paramArrayOfDouble[paramInt]
//				- paramArrayOfDouble[(paramInt + 4)];
//		double d4 = paramArrayOfDouble[(paramInt + 1)]
//				- paramArrayOfDouble[(paramInt + 5)];
//		double d5 = paramArrayOfDouble[(paramInt + 2)]
//				+ paramArrayOfDouble[(paramInt + 6)];
//		double d6 = paramArrayOfDouble[(paramInt + 3)]
//				+ paramArrayOfDouble[(paramInt + 7)];
//		double d7 = paramArrayOfDouble[(paramInt + 2)]
//				- paramArrayOfDouble[(paramInt + 6)];
//		double d8 = paramArrayOfDouble[(paramInt + 3)]
//				- paramArrayOfDouble[(paramInt + 7)];
//		paramArrayOfDouble[paramInt] = (d1 + d5);
//		paramArrayOfDouble[(paramInt + 1)] = (d2 + d6);
//		paramArrayOfDouble[(paramInt + 2)] = (d3 - d8);
//		paramArrayOfDouble[(paramInt + 3)] = (d4 + d7);
//		paramArrayOfDouble[(paramInt + 4)] = (d1 - d5);
//		paramArrayOfDouble[(paramInt + 5)] = (d2 - d6);
//		paramArrayOfDouble[(paramInt + 6)] = (d3 + d8);
//		paramArrayOfDouble[(paramInt + 7)] = (d4 - d7);
//	}
//
//	private void cftb040(double[] paramArrayOfDouble, int paramInt) {
//		double d1 = paramArrayOfDouble[paramInt]
//				+ paramArrayOfDouble[(paramInt + 4)];
//		double d2 = paramArrayOfDouble[(paramInt + 1)]
//				+ paramArrayOfDouble[(paramInt + 5)];
//		double d3 = paramArrayOfDouble[paramInt]
//				- paramArrayOfDouble[(paramInt + 4)];
//		double d4 = paramArrayOfDouble[(paramInt + 1)]
//				- paramArrayOfDouble[(paramInt + 5)];
//		double d5 = paramArrayOfDouble[(paramInt + 2)]
//				+ paramArrayOfDouble[(paramInt + 6)];
//		double d6 = paramArrayOfDouble[(paramInt + 3)]
//				+ paramArrayOfDouble[(paramInt + 7)];
//		double d7 = paramArrayOfDouble[(paramInt + 2)]
//				- paramArrayOfDouble[(paramInt + 6)];
//		double d8 = paramArrayOfDouble[(paramInt + 3)]
//				- paramArrayOfDouble[(paramInt + 7)];
//		paramArrayOfDouble[paramInt] = (d1 + d5);
//		paramArrayOfDouble[(paramInt + 1)] = (d2 + d6);
//		paramArrayOfDouble[(paramInt + 2)] = (d3 + d8);
//		paramArrayOfDouble[(paramInt + 3)] = (d4 - d7);
//		paramArrayOfDouble[(paramInt + 4)] = (d1 - d5);
//		paramArrayOfDouble[(paramInt + 5)] = (d2 - d6);
//		paramArrayOfDouble[(paramInt + 6)] = (d3 - d8);
//		paramArrayOfDouble[(paramInt + 7)] = (d4 + d7);
//	}
//
//	private void cftx020(double[] paramArrayOfDouble, int paramInt) {
//		double d1 = paramArrayOfDouble[paramInt]
//				- paramArrayOfDouble[(paramInt + 2)];
//		double d2 = -paramArrayOfDouble[(paramInt + 1)]
//				+ paramArrayOfDouble[(paramInt + 3)];
//		paramArrayOfDouble[paramInt] += paramArrayOfDouble[(paramInt + 2)];
//		paramArrayOfDouble[(paramInt + 1)] += paramArrayOfDouble[(paramInt + 3)];
//		paramArrayOfDouble[(paramInt + 2)] = d1;
//		paramArrayOfDouble[(paramInt + 3)] = d2;
//	}
//
//	private void cftxb020(double[] paramArrayOfDouble, int paramInt) {
//		double d1 = paramArrayOfDouble[paramInt]
//				- paramArrayOfDouble[(paramInt + 2)];
//		double d2 = paramArrayOfDouble[(paramInt + 1)]
//				- paramArrayOfDouble[(paramInt + 3)];
//		paramArrayOfDouble[paramInt] += paramArrayOfDouble[(paramInt + 2)];
//		paramArrayOfDouble[(paramInt + 1)] += paramArrayOfDouble[(paramInt + 3)];
//		paramArrayOfDouble[(paramInt + 2)] = d1;
//		paramArrayOfDouble[(paramInt + 3)] = d2;
//	}
//
//	private void cftxc020(double[] paramArrayOfDouble, int paramInt) {
//		double d1 = paramArrayOfDouble[paramInt]
//				- paramArrayOfDouble[(paramInt + 2)];
//		double d2 = paramArrayOfDouble[(paramInt + 1)]
//				+ paramArrayOfDouble[(paramInt + 3)];
//		paramArrayOfDouble[paramInt] += paramArrayOfDouble[(paramInt + 2)];
//		paramArrayOfDouble[(paramInt + 1)] -= paramArrayOfDouble[(paramInt + 3)];
//		paramArrayOfDouble[(paramInt + 2)] = d1;
//		paramArrayOfDouble[(paramInt + 3)] = d2;
//	}
//
//	private void rftfsub(int paramInt1, double[] paramArrayOfDouble1,
//			int paramInt2, int paramInt3, double[] paramArrayOfDouble2,
//			int paramInt4) {
//		int l = paramInt1 >> 1;
//		int k = 2 * paramInt3 / l;
//		int j = 0;
//		for (int i3 = 2; i3 < l; i3 += 2) {
//			int i = paramInt1 - i3;
//			j += k;
//			double d1 = 0.5D - paramArrayOfDouble2[(paramInt4 + paramInt3 - j)];
//			double d2 = paramArrayOfDouble2[(paramInt4 + j)];
//			int i1 = paramInt2 + i3;
//			int i2 = paramInt2 + i;
//			double d3 = paramArrayOfDouble1[i1] - paramArrayOfDouble1[i2];
//			double d4 = paramArrayOfDouble1[(i1 + 1)]
//					+ paramArrayOfDouble1[(i2 + 1)];
//			double d5 = d1 * d3 - (d2 * d4);
//			double d6 = d1 * d4 + d2 * d3;
//			paramArrayOfDouble1[i1] -= d5;
//			paramArrayOfDouble1[(i1 + 1)] = (d6 - paramArrayOfDouble1[(i1 + 1)]);
//			paramArrayOfDouble1[i2] += d5;
//			paramArrayOfDouble1[(i2 + 1)] = (d6 - paramArrayOfDouble1[(i2 + 1)]);
//		}
//		paramArrayOfDouble1[(paramInt2 + l + 1)] = (-paramArrayOfDouble1[(paramInt2
//				+ l + 1)]);
//	}
//
//	private void rftbsub(int paramInt1, double[] paramArrayOfDouble1,
//			int paramInt2, int paramInt3, double[] paramArrayOfDouble2,
//			int paramInt4) {
//		int l = paramInt1 >> 1;
//		int k = 2 * paramInt3 / l;
//		int j = 0;
//		for (int i3 = 2; i3 < l; i3 += 2) {
//			int i = paramInt1 - i3;
//			j += k;
//			double d1 = 0.5D - paramArrayOfDouble2[(paramInt4 + paramInt3 - j)];
//			double d2 = paramArrayOfDouble2[(paramInt4 + j)];
//			int i1 = paramInt2 + i3;
//			int i2 = paramInt2 + i;
//			double d3 = paramArrayOfDouble1[i1] - paramArrayOfDouble1[i2];
//			double d4 = paramArrayOfDouble1[(i1 + 1)]
//					+ paramArrayOfDouble1[(i2 + 1)];
//			double d5 = d1 * d3 - (d2 * d4);
//			double d6 = d1 * d4 + d2 * d3;
//			paramArrayOfDouble1[i1] -= d5;
//			paramArrayOfDouble1[(i1 + 1)] -= d6;
//			paramArrayOfDouble1[i2] += d5;
//			paramArrayOfDouble1[(i2 + 1)] -= d6;
//		}
//	}
//
//	private void scale(double paramDouble, double[] paramArrayOfDouble,
//			int paramInt, boolean paramBoolean) {
//		double d = 1.0D / paramDouble;
//		int i;
//		if (paramBoolean)
//			i = 2 * this.n;
//		else
//			i = this.n;
//		int j = ConcurrencyUtils.getNumberOfThreads();
//		int k;
//		if ((j > 1)
//				&& (i >= ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads())) {
//			k = i / j;
//			Future[] arrayOfFuture = new Future[j];
//			for (int l = 0; l < j; ++l) {
//				int i1 = paramInt + l * k;
//				int i2 = (l == j - 1) ? paramInt + i : i1 + k;
//				arrayOfFuture[l] = ConcurrencyUtils.submit(new Runnable(i1, i2,
//						paramArrayOfDouble, d) {
//					public void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i)
//							this.val$a[i] *= this.val$norm;
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (k = paramInt; k < paramInt + i; ++k)
//				paramArrayOfDouble[k] *= d;
//		}
//	}
//
//	private static enum Plans {
//		SPLIT_RADIX, MIXED_RADIX, BLUESTEIN;
//	}
//}