package ps.hell.math.base.persons.fft.transform.fft;//package ps.landerbuluse.ml.math.fft.transform.fft;
//
//import edu.emory.mathcs.utils.ConcurrencyUtils;
//import java.util.concurrent.Future;
//
//public class FloatFFT_1D {
//	private int n;
//	private int nBluestein;
//	private int[] ip;
//	private float[] w;
//	private int nw;
//	private int nc;
//	private float[] wtable;
//	private float[] wtable_r;
//	private float[] bk1;
//	private float[] bk2;
//	private Plans plan;
//	private static final int[] factors = { 4, 2, 3, 5 };
//	private static final float PI = 3.141593F;
//	private static final float TWO_PI = 6.283186F;
//
//	public strictfp FloatFFT_1D(int paramInt) {
//		if (paramInt < 1)
//			throw new IllegalArgumentException("n must be greater than 0");
//		this.n = paramInt;
//		int i;
//		if (!(ConcurrencyUtils.isPowerOf2(paramInt))) {
//			if (getReminder(paramInt, factors) >= 211) {
//				this.plan = Plans.BLUESTEIN;
//				this.nBluestein = ConcurrencyUtils.nextPow2(paramInt * 2 - 1);
//				this.bk1 = new float[2 * this.nBluestein];
//				this.bk2 = new float[2 * this.nBluestein];
//				this.ip = new int[2 + (int) Math.ceil(2 + (1 << (int) (Math
//						.log(this.nBluestein + 0.5D) / Math.log(2.0D)) / 2))];
//				this.w = new float[this.nBluestein];
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
//				this.wtable = new float[4 * paramInt + 15];
//				this.wtable_r = new float[2 * paramInt + 15];
//				cffti();
//				rffti();
//			}
//		} else {
//			this.plan = Plans.SPLIT_RADIX;
//			this.ip = new int[2 + (int) Math.ceil(2 + (1 << (int) (Math
//					.log(paramInt + 0.5D) / Math.log(2.0D)) / 2))];
//			this.w = new float[paramInt];
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
//	public strictfp void complexForward(float[] paramArrayOfFloat) {
//		complexForward(paramArrayOfFloat, 0);
//	}
//
//	public strictfp void complexForward(float[] paramArrayOfFloat, int paramInt)
//  {
//    if (this.n == 1)
//      return;
//    switch (18.$SwitchMap$edu$emory$mathcs$jtransforms$fft$FloatFFT_1D$Plans[this.plan.ordinal()])
//    {
//    case 1:
//      cftbsub(2 * this.n, paramArrayOfFloat, paramInt, this.ip, this.nw, this.w);
//      break;
//    case 2:
//      cfftf(paramArrayOfFloat, paramInt, -1);
//      break;
//    case 3:
//      bluestein_complex(paramArrayOfFloat, paramInt, -1);
//    }
//  }
//
//	public strictfp void complexInverse(float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		complexInverse(paramArrayOfFloat, 0, paramBoolean);
//	}
//
//	public strictfp void complexInverse(float[] paramArrayOfFloat, int paramInt, boolean paramBoolean)
//  {
//    if (this.n == 1)
//      return;
//    switch (18.$SwitchMap$edu$emory$mathcs$jtransforms$fft$FloatFFT_1D$Plans[this.plan.ordinal()])
//    {
//    case 1:
//      cftfsub(2 * this.n, paramArrayOfFloat, paramInt, this.ip, this.nw, this.w);
//      break;
//    case 2:
//      cfftf(paramArrayOfFloat, paramInt, 1);
//      break;
//    case 3:
//      bluestein_complex(paramArrayOfFloat, paramInt, 1);
//    }
//    if (!(paramBoolean))
//      return;
//    scale(this.n, paramArrayOfFloat, paramInt, true);
//  }
//
//	public strictfp void realForward(float[] paramArrayOfFloat) {
//		realForward(paramArrayOfFloat, 0);
//	}
//
//	public strictfp void realForward(float[] paramArrayOfFloat, int paramInt)
//  {
//    if (this.n == 1)
//      return;
//    switch (18.$SwitchMap$edu$emory$mathcs$jtransforms$fft$FloatFFT_1D$Plans[this.plan.ordinal()])
//    {
//    case 1:
//      if (this.n > 4)
//      {
//        cftfsub(this.n, paramArrayOfFloat, paramInt, this.ip, this.nw, this.w);
//        rftfsub(this.n, paramArrayOfFloat, paramInt, this.nc, this.w, this.nw);
//      }
//      else if (this.n == 4)
//      {
//        cftx020(paramArrayOfFloat, paramInt);
//      }
//      float f1 = paramArrayOfFloat[paramInt] - paramArrayOfFloat[(paramInt + 1)];
//      paramArrayOfFloat[paramInt] += paramArrayOfFloat[(paramInt + 1)];
//      paramArrayOfFloat[(paramInt + 1)] = f1;
//      break;
//    case 2:
//      rfftf(paramArrayOfFloat, paramInt);
//      for (int i = this.n - 1; i >= 2; --i)
//      {
//        int j = paramInt + i;
//        float f2 = paramArrayOfFloat[j];
//        paramArrayOfFloat[j] = paramArrayOfFloat[(j - 1)];
//        paramArrayOfFloat[(j - 1)] = f2;
//      }
//      break;
//    case 3:
//      bluestein_real_forward(paramArrayOfFloat, paramInt);
//    }
//  }
//
//	public strictfp void realForwardFull(float[] paramArrayOfFloat) {
//		realForwardFull(paramArrayOfFloat, 0);
//	}
//
//	public strictfp void realForwardFull(float[] paramArrayOfFloat, int paramInt)
//  {
//    int i = 2 * this.n;
//    int l;
//    int i4;
//    int i3;
//    int k;
//    switch (18.$SwitchMap$edu$emory$mathcs$jtransforms$fft$FloatFFT_1D$Plans[this.plan.ordinal()])
//    {
//    case 1:
//      realForward(paramArrayOfFloat, paramInt);
//      int j = ConcurrencyUtils.getNumberOfThreads();
//      if ((j > 1) && (this.n / 2 > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()))
//      {
//        Future[] arrayOfFuture = new Future[j];
//        l = this.n / 2 / j;
//        for (int i2 = 0; i2 < j; ++i2)
//        {
//          i4 = i2 * l;
//          int i5 = (i2 == j - 1) ? this.n / 2 : i4 + l;
//          arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i4, i5, paramInt, i, paramArrayOfFloat)
//          {
//            public strictfp void run()
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
//          paramArrayOfFloat[l] = paramArrayOfFloat[(paramInt + k)];
//          paramArrayOfFloat[(l + 1)] = (-paramArrayOfFloat[(paramInt + k + 1)]);
//        }
//      }
//      paramArrayOfFloat[(paramInt + this.n)] = (-paramArrayOfFloat[(paramInt + 1)]);
//      paramArrayOfFloat[(paramInt + 1)] = 0.0F;
//      break;
//    case 2:
//      rfftf(paramArrayOfFloat, paramInt);
//      if (this.n % 2 == 0)
//        k = this.n / 2;
//      else
//        k = (this.n + 1) / 2;
//      for (l = 1; l < k; ++l)
//      {
//        i3 = paramInt + i - (2 * l);
//        i4 = paramInt + 2 * l;
//        paramArrayOfFloat[(i3 + 1)] = (-paramArrayOfFloat[i4]);
//        paramArrayOfFloat[i3] = paramArrayOfFloat[(i4 - 1)];
//      }
//      for (int i1 = 1; i1 < this.n; ++i1)
//      {
//        i3 = paramInt + this.n - i1;
//        float f = paramArrayOfFloat[(i3 + 1)];
//        paramArrayOfFloat[(i3 + 1)] = paramArrayOfFloat[i3];
//        paramArrayOfFloat[i3] = f;
//      }
//      paramArrayOfFloat[(paramInt + 1)] = 0.0F;
//      break;
//    case 3:
//      bluestein_real_full(paramArrayOfFloat, paramInt, -1);
//    }
//  }
//
//	public strictfp void realInverse(float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		realInverse(paramArrayOfFloat, 0, paramBoolean);
//	}
//
//	public strictfp void realInverse(float[] paramArrayOfFloat, int paramInt, boolean paramBoolean)
//  {
//    if (this.n == 1)
//      return;
//    switch (18.$SwitchMap$edu$emory$mathcs$jtransforms$fft$FloatFFT_1D$Plans[this.plan.ordinal()])
//    {
//    case 1:
//      paramArrayOfFloat[(paramInt + 1)] = (float)(0.5D * (paramArrayOfFloat[paramInt] - paramArrayOfFloat[(paramInt + 1)]));
//      paramArrayOfFloat[paramInt] -= paramArrayOfFloat[(paramInt + 1)];
//      if (this.n > 4)
//      {
//        rftfsub(this.n, paramArrayOfFloat, paramInt, this.nc, this.w, this.nw);
//        cftbsub(this.n, paramArrayOfFloat, paramInt, this.ip, this.nw, this.w);
//      }
//      else if (this.n == 4)
//      {
//        cftxc020(paramArrayOfFloat, paramInt);
//      }
//      if (!(paramBoolean))
//        return;
//      scale(this.n / 2, paramArrayOfFloat, paramInt, false);
//      break;
//    case 2:
//      for (int i = 2; i < this.n; ++i)
//      {
//        int j = paramInt + i;
//        float f = paramArrayOfFloat[(j - 1)];
//        paramArrayOfFloat[(j - 1)] = paramArrayOfFloat[j];
//        paramArrayOfFloat[j] = f;
//      }
//      rfftb(paramArrayOfFloat, paramInt);
//      if (!(paramBoolean))
//        return;
//      scale(this.n, paramArrayOfFloat, paramInt, false);
//      break;
//    case 3:
//      bluestein_real_inverse(paramArrayOfFloat, paramInt);
//      if (!(paramBoolean))
//        return;
//      scale(this.n, paramArrayOfFloat, paramInt, false);
//    }
//  }
//
//	public strictfp void realInverseFull(float[] paramArrayOfFloat,
//			boolean paramBoolean) {
//		realInverseFull(paramArrayOfFloat, 0, paramBoolean);
//	}
//
//	public strictfp void realInverseFull(float[] paramArrayOfFloat, int paramInt, boolean paramBoolean)
//  {
//    int i = 2 * this.n;
//    int l;
//    int i4;
//    int i3;
//    int k;
//    switch (18.$SwitchMap$edu$emory$mathcs$jtransforms$fft$FloatFFT_1D$Plans[this.plan.ordinal()])
//    {
//    case 1:
//      realInverse2(paramArrayOfFloat, paramInt, paramBoolean);
//      int j = ConcurrencyUtils.getNumberOfThreads();
//      if ((j > 1) && (this.n / 2 > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()))
//      {
//        Future[] arrayOfFuture = new Future[j];
//        l = this.n / 2 / j;
//        for (int i2 = 0; i2 < j; ++i2)
//        {
//          i4 = i2 * l;
//          int i5 = (i2 == j - 1) ? this.n / 2 : i4 + l;
//          arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i4, i5, paramInt, i, paramArrayOfFloat)
//          {
//            public strictfp void run()
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
//          paramArrayOfFloat[l] = paramArrayOfFloat[(paramInt + k)];
//          paramArrayOfFloat[(l + 1)] = (-paramArrayOfFloat[(paramInt + k + 1)]);
//        }
//      }
//      paramArrayOfFloat[(paramInt + this.n)] = (-paramArrayOfFloat[(paramInt + 1)]);
//      paramArrayOfFloat[(paramInt + 1)] = 0.0F;
//      break;
//    case 2:
//      rfftf(paramArrayOfFloat, paramInt);
//      if (paramBoolean)
//        scale(this.n, paramArrayOfFloat, paramInt, false);
//      if (this.n % 2 == 0)
//        k = this.n / 2;
//      else
//        k = (this.n + 1) / 2;
//      for (l = 1; l < k; ++l)
//      {
//        i3 = paramInt + 2 * l;
//        i4 = paramInt + i - (2 * l);
//        paramArrayOfFloat[i3] = (-paramArrayOfFloat[i3]);
//        paramArrayOfFloat[(i4 + 1)] = (-paramArrayOfFloat[i3]);
//        paramArrayOfFloat[i4] = paramArrayOfFloat[(i3 - 1)];
//      }
//      for (int i1 = 1; i1 < this.n; ++i1)
//      {
//        i3 = paramInt + this.n - i1;
//        float f = paramArrayOfFloat[(i3 + 1)];
//        paramArrayOfFloat[(i3 + 1)] = paramArrayOfFloat[i3];
//        paramArrayOfFloat[i3] = f;
//      }
//      paramArrayOfFloat[(paramInt + 1)] = 0.0F;
//      break;
//    case 3:
//      bluestein_real_full(paramArrayOfFloat, paramInt, 1);
//      if (!(paramBoolean))
//        return;
//      scale(this.n, paramArrayOfFloat, paramInt, true);
//    }
//  }
//
//	protected strictfp void realInverse2(float[] paramArrayOfFloat, int paramInt, boolean paramBoolean)
//  {
//    if (this.n == 1)
//      return;
//    switch (18.$SwitchMap$edu$emory$mathcs$jtransforms$fft$FloatFFT_1D$Plans[this.plan.ordinal()])
//    {
//    case 1:
//      if (this.n > 4)
//      {
//        cftfsub(this.n, paramArrayOfFloat, paramInt, this.ip, this.nw, this.w);
//        rftbsub(this.n, paramArrayOfFloat, paramInt, this.nc, this.w, this.nw);
//      }
//      else if (this.n == 4)
//      {
//        cftbsub(this.n, paramArrayOfFloat, paramInt, this.ip, this.nw, this.w);
//      }
//      float f1 = paramArrayOfFloat[paramInt] - paramArrayOfFloat[(paramInt + 1)];
//      paramArrayOfFloat[paramInt] += paramArrayOfFloat[(paramInt + 1)];
//      paramArrayOfFloat[(paramInt + 1)] = f1;
//      if (!(paramBoolean))
//        return;
//      scale(this.n, paramArrayOfFloat, paramInt, false);
//      break;
//    case 2:
//      rfftf(paramArrayOfFloat, paramInt);
//      int j;
//      for (int i = this.n - 1; i >= 2; --i)
//      {
//        j = paramInt + i;
//        float f2 = paramArrayOfFloat[j];
//        paramArrayOfFloat[j] = paramArrayOfFloat[(j - 1)];
//        paramArrayOfFloat[(j - 1)] = f2;
//      }
//      if (paramBoolean)
//        scale(this.n, paramArrayOfFloat, paramInt, false);
//      int l;
//      if (this.n % 2 == 0)
//      {
//        i = this.n / 2;
//        for (j = 1; j < i; ++j)
//        {
//          l = paramInt + 2 * j + 1;
//          paramArrayOfFloat[l] = (-paramArrayOfFloat[l]);
//        }
//        return;
//      }
//      i = (this.n - 1) / 2;
//      for (int k = 0; k < i; ++k)
//      {
//        l = paramInt + 2 * k + 1;
//        paramArrayOfFloat[l] = (-paramArrayOfFloat[l]);
//      }
//      break;
//    case 3:
//      bluestein_real_inverse2(paramArrayOfFloat, paramInt);
//      if (!(paramBoolean))
//        return;
//      scale(this.n, paramArrayOfFloat, paramInt, false);
//    }
//  }
//
//	private static strictfp int getReminder(int paramInt, int[] paramArrayOfInt) {
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
//	strictfp void cffti(int paramInt1, int paramInt2) {
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
//			this.wtable[(paramInt2 + 2 + j)] = 2.0F;
//		} while (i12 != 1);
//		this.wtable[(paramInt2 + j)] = paramInt1;
//		this.wtable[(paramInt2 + 1 + j)] = i10;
//		float f1 = 6.283186F / paramInt1;
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
//				this.wtable[(paramInt2 + i1 - 1 + i)] = 1.0F;
//				this.wtable[(paramInt2 + i1 + i)] = 0.0F;
//				i8 += i5;
//				float f3 = 0.0F;
//				float f2 = i8 * f1;
//				for (int i9 = 4; i9 <= k; i9 += 2) {
//					i1 += 2;
//					f3 += 1.0F;
//					float f4 = f3 * f2;
//					i17 = i1 + i;
//					this.wtable[(paramInt2 + i17 - 1)] = (float) Math.cos(f4);
//					this.wtable[(paramInt2 + i17)] = (float) Math.sin(f4);
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
//	strictfp void cffti() {
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
//			this.wtable[(2 + j)] = 2.0F;
//		} while (i12 != 1);
//		this.wtable[j] = this.n;
//		this.wtable[(1 + j)] = i10;
//		float f1 = 6.283186F / this.n;
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
//				this.wtable[(i1 - 1 + i)] = 1.0F;
//				this.wtable[(i1 + i)] = 0.0F;
//				i8 += i5;
//				float f3 = 0.0F;
//				float f2 = i8 * f1;
//				for (int i9 = 4; i9 <= k; i9 += 2) {
//					i1 += 2;
//					f3 += 1.0F;
//					float f4 = f3 * f2;
//					i17 = i1 + i;
//					this.wtable[(i17 - 1)] = (float) Math.cos(f4);
//					this.wtable[i17] = (float) Math.sin(f4);
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
//	strictfp void rffti() {
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
//			this.wtable_r[(2 + i)] = 2.0F;
//		} while (i9 != 1);
//		this.wtable_r[i] = this.n;
//		this.wtable_r[(1 + i)] = i7;
//		float f1 = 6.283186F / this.n;
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
//				float f2 = i5 * f1;
//				float f3 = 0.0F;
//				for (int i6 = 3; i6 <= i13; i6 += 2) {
//					k += 2;
//					f3 += 1.0F;
//					float f4 = f3 * f2;
//					i16 = k + this.n;
//					this.wtable_r[(i16 - 2)] = (float) Math.cos(f4);
//					this.wtable_r[(i16 - 1)] = (float) Math.sin(f4);
//				}
//				i10 += i13;
//			}
//			i2 = i3;
//		}
//	}
//
//	private strictfp void bluesteini() {
//		int i = 0;
//		float f2 = 3.141593F / this.n;
//		this.bk1[0] = 1.0F;
//		this.bk1[1] = 0.0F;
//		for (int j = 1; j < this.n; ++j) {
//			i += 2 * j - 1;
//			if (i >= 2 * this.n)
//				i -= 2 * this.n;
//			float f1 = f2 * i;
//			this.bk1[(2 * j)] = (float) Math.cos(f1);
//			this.bk1[(2 * j + 1)] = (float) Math.sin(f1);
//		}
//		float f3 = (float) (1.0D / this.nBluestein);
//		this.bk2[0] = (this.bk1[0] * f3);
//		this.bk2[1] = (this.bk1[1] * f3);
//		for (int k = 2; k < 2 * this.n; k += 2) {
//			this.bk2[k] = (this.bk1[k] * f3);
//			this.bk2[(k + 1)] = (this.bk1[(k + 1)] * f3);
//			this.bk2[(2 * this.nBluestein - k)] = this.bk2[k];
//			this.bk2[(2 * this.nBluestein - k + 1)] = this.bk2[(k + 1)];
//		}
//		cftbsub(2 * this.nBluestein, this.bk2, 0, this.ip, this.nw, this.w);
//	}
//
//	private strictfp void makewt(int paramInt) {
//		this.ip[0] = paramInt;
//		this.ip[1] = 1;
//		if (paramInt <= 2)
//			return;
//		int j = paramInt >> 1;
//		float f1 = (float) (0.7853981633974483D / j);
//		float f7 = f1 * 2.0F;
//		float f2 = (float) Math.cos(f1 * j);
//		this.w[0] = 1.0F;
//		this.w[1] = f2;
//		int i;
//		if (j == 4) {
//			this.w[2] = (float) Math.cos(f7);
//			this.w[3] = (float) Math.sin(f7);
//		} else if (j > 4) {
//			makeipt(paramInt);
//			this.w[2] = (float) (0.5D / Math.cos(f7));
//			this.w[3] = (float) (0.5D / Math.cos(f1 * 6.0F));
//			for (i = 4; i < j; i += 4) {
//				float f8 = f1 * i;
//				float f9 = 3.0F * f8;
//				this.w[i] = (float) Math.cos(f8);
//				this.w[(i + 1)] = (float) Math.sin(f8);
//				this.w[(i + 2)] = (float) Math.cos(f9);
//				this.w[(i + 3)] = (float) (-Math.sin(f9));
//			}
//		}
//		int l;
//		for (int k = 0; j > 2; k = l) {
//			l = k + j;
//			j >>= 1;
//			this.w[l] = 1.0F;
//			this.w[(l + 1)] = f2;
//			float f3;
//			float f4;
//			if (j == 4) {
//				f3 = this.w[(k + 4)];
//				f4 = this.w[(k + 5)];
//				this.w[(l + 2)] = f3;
//				this.w[(l + 3)] = f4;
//			} else {
//				if (j <= 4)
//					continue;
//				f3 = this.w[(k + 4)];
//				float f5 = this.w[(k + 6)];
//				this.w[(l + 2)] = (float) (0.5D / f3);
//				this.w[(l + 3)] = (float) (0.5D / f5);
//				for (i = 4; i < j; i += 4) {
//					int i1 = k + 2 * i;
//					int i2 = l + i;
//					f3 = this.w[i1];
//					f4 = this.w[(i1 + 1)];
//					f5 = this.w[(i1 + 2)];
//					float f6 = this.w[(i1 + 3)];
//					this.w[i2] = f3;
//					this.w[(i2 + 1)] = f4;
//					this.w[(i2 + 2)] = f5;
//					this.w[(i2 + 3)] = f6;
//				}
//			}
//		}
//	}
//
//	private strictfp void makeipt(int paramInt) {
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
//	private strictfp void makect(int paramInt1, float[] paramArrayOfFloat,
//			int paramInt2) {
//		this.ip[1] = paramInt1;
//		if (paramInt1 <= 1)
//			return;
//		int j = paramInt1 >> 1;
//		float f1 = (float) (0.7853981633974483D / j);
//		paramArrayOfFloat[paramInt2] = (float) Math.cos(f1 * j);
//		paramArrayOfFloat[(paramInt2 + j)] = (float) (0.5D * paramArrayOfFloat[paramInt2]);
//		for (int i = 1; i < j; ++i) {
//			float f2 = f1 * i;
//			paramArrayOfFloat[(paramInt2 + i)] = (float) (0.5D * Math.cos(f2));
//			paramArrayOfFloat[(paramInt2 + paramInt1 - i)] = (float) (0.5D * Math
//					.sin(f2));
//		}
//	}
//
//	private strictfp void bluestein_complex(float[] paramArrayOfFloat,
//			int paramInt1, int paramInt2) {
//		float[] arrayOfFloat = new float[2 * this.nBluestein];
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
//						paramInt2, i3, i5, paramInt1, arrayOfFloat,
//						paramArrayOfFloat) {
//					public strictfp void run() {
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
//										* FloatFFT_1D
//												.access$000(FloatFFT_1D.this)[j] - (this.val$a[i1] * FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[k]));
//								this.val$ak[k] = (this.val$a[l]
//										* FloatFFT_1D
//												.access$000(FloatFFT_1D.this)[k] + this.val$a[i1]
//										* FloatFFT_1D
//												.access$000(FloatFFT_1D.this)[j]);
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								l = this.val$offa + j;
//								i1 = this.val$offa + k;
//								this.val$ak[j] = (this.val$a[l]
//										* FloatFFT_1D
//												.access$000(FloatFFT_1D.this)[j] + this.val$a[i1]
//										* FloatFFT_1D
//												.access$000(FloatFFT_1D.this)[k]);
//								this.val$ak[k] = (-this.val$a[l]
//										* FloatFFT_1D
//												.access$000(FloatFFT_1D.this)[k] + this.val$a[i1]
//										* FloatFFT_1D
//												.access$000(FloatFFT_1D.this)[j]);
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftbsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			k = this.nBluestein / i;
//			for (int i1 = 0; i1 < i; ++i1) {
//				i3 = i1 * k;
//				i5 = (i1 == i - 1) ? this.nBluestein : i3 + k;
//				arrayOfFuture[i1] = ConcurrencyUtils.submit(new Runnable(
//						paramInt2, i3, i5, arrayOfFloat) {
//					public strictfp void run() {
//						int i;
//						int j;
//						int k;
//						float f;
//						if (this.val$isign > 0)
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								f = -this.val$ak[j] * FloatFFT_1D.this.bk2[k]
//										+ this.val$ak[k]
//										* FloatFFT_1D.this.bk2[j];
//								this.val$ak[j] = (this.val$ak[j]
//										* FloatFFT_1D
//												.access$100(FloatFFT_1D.this)[j] + this.val$ak[k]
//										* FloatFFT_1D
//												.access$100(FloatFFT_1D.this)[k]);
//								this.val$ak[k] = f;
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								f = this.val$ak[j] * FloatFFT_1D.this.bk2[k]
//										+ this.val$ak[k]
//										* FloatFFT_1D.this.bk2[j];
//								this.val$ak[j] = (this.val$ak[j]
//										* FloatFFT_1D
//												.access$100(FloatFFT_1D.this)[j] - (this.val$ak[k] * FloatFFT_1D
//										.access$100(FloatFFT_1D.this)[k]));
//								this.val$ak[k] = f;
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftfsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			k = this.n / i;
//			for (i2 = 0; i2 < i; ++i2) {
//				i3 = i2 * k;
//				i5 = (i2 == i - 1) ? this.n : i3 + k;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(
//						paramInt2, i3, i5, paramInt1, paramArrayOfFloat,
//						arrayOfFloat) {
//					public strictfp void run() {
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
//								this.val$a[l] = (FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[j]
//										* this.val$ak[j] - (FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[k] * this.val$ak[k]));
//								this.val$a[i1] = (FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[k]
//										* this.val$ak[j] + FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[j]
//										* this.val$ak[k]);
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								l = this.val$offa + j;
//								i1 = this.val$offa + k;
//								this.val$a[l] = (FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[j]
//										* this.val$ak[j] + FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[k]
//										* this.val$ak[k]);
//								this.val$a[i1] = (-FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[k]
//										* this.val$ak[j] + FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[j]
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
//					arrayOfFloat[k] = (paramArrayOfFloat[i3] * this.bk1[k] - (paramArrayOfFloat[i5] * this.bk1[i2]));
//					arrayOfFloat[i2] = (paramArrayOfFloat[i3] * this.bk1[i2] + paramArrayOfFloat[i5]
//							* this.bk1[k]);
//				}
//			else
//				for (j = 0; j < this.n; ++j) {
//					k = 2 * j;
//					i2 = k + 1;
//					i3 = paramInt1 + k;
//					i5 = paramInt1 + i2;
//					arrayOfFloat[k] = (paramArrayOfFloat[i3] * this.bk1[k] + paramArrayOfFloat[i5]
//							* this.bk1[i2]);
//					arrayOfFloat[i2] = (-paramArrayOfFloat[i3] * this.bk1[i2] + paramArrayOfFloat[i5]
//							* this.bk1[k]);
//				}
//			cftbsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			float f;
//			if (paramInt2 > 0)
//				for (j = 0; j < this.nBluestein; ++j) {
//					k = 2 * j;
//					i2 = k + 1;
//					f = -arrayOfFloat[k] * this.bk2[i2] + arrayOfFloat[i2]
//							* this.bk2[k];
//					arrayOfFloat[k] = (arrayOfFloat[k] * this.bk2[k] + arrayOfFloat[i2]
//							* this.bk2[i2]);
//					arrayOfFloat[i2] = f;
//				}
//			else
//				for (j = 0; j < this.nBluestein; ++j) {
//					k = 2 * j;
//					i2 = k + 1;
//					f = arrayOfFloat[k] * this.bk2[i2] + arrayOfFloat[i2]
//							* this.bk2[k];
//					arrayOfFloat[k] = (arrayOfFloat[k] * this.bk2[k] - (arrayOfFloat[i2] * this.bk2[i2]));
//					arrayOfFloat[i2] = f;
//				}
//			cftfsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			int i4;
//			if (paramInt2 > 0)
//				for (j = 0; j < this.n; ++j) {
//					k = 2 * j;
//					i2 = k + 1;
//					i4 = paramInt1 + k;
//					i5 = paramInt1 + i2;
//					paramArrayOfFloat[i4] = (this.bk1[k] * arrayOfFloat[k] - (this.bk1[i2] * arrayOfFloat[i2]));
//					paramArrayOfFloat[i5] = (this.bk1[i2] * arrayOfFloat[k] + this.bk1[k]
//							* arrayOfFloat[i2]);
//				}
//			else
//				for (j = 0; j < this.n; ++j) {
//					k = 2 * j;
//					i2 = k + 1;
//					i4 = paramInt1 + k;
//					i5 = paramInt1 + i2;
//					paramArrayOfFloat[i4] = (this.bk1[k] * arrayOfFloat[k] + this.bk1[i2]
//							* arrayOfFloat[i2]);
//					paramArrayOfFloat[i5] = (-this.bk1[i2] * arrayOfFloat[k] + this.bk1[k]
//							* arrayOfFloat[i2]);
//				}
//		}
//	}
//
//	private strictfp void bluestein_real_full(float[] paramArrayOfFloat,
//			int paramInt1, int paramInt2) {
//		float[] arrayOfFloat = new float[2 * this.nBluestein];
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
//						paramInt2, i5, i6, paramInt1, arrayOfFloat,
//						paramArrayOfFloat) {
//					public strictfp void run() {
//						int i;
//						int j;
//						int k;
//						int l;
//						if (this.val$isign > 0)
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								l = this.val$offa + i;
//								this.val$ak[j] = (this.val$a[l] * FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[j]);
//								this.val$ak[k] = (this.val$a[l] * FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[k]);
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								l = this.val$offa + i;
//								this.val$ak[j] = (this.val$a[l] * FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[j]);
//								this.val$ak[k] = (-this.val$a[l] * FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[k]);
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftbsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			i1 = this.nBluestein / i;
//			for (int i3 = 0; i3 < i; ++i3) {
//				i5 = i3 * i1;
//				i6 = (i3 == i - 1) ? this.nBluestein : i5 + i1;
//				arrayOfFuture[i3] = ConcurrencyUtils.submit(new Runnable(
//						paramInt2, i5, i6, arrayOfFloat) {
//					public strictfp void run() {
//						int i;
//						int j;
//						int k;
//						float f;
//						if (this.val$isign > 0)
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								f = -this.val$ak[j] * FloatFFT_1D.this.bk2[k]
//										+ this.val$ak[k]
//										* FloatFFT_1D.this.bk2[j];
//								this.val$ak[j] = (this.val$ak[j]
//										* FloatFFT_1D
//												.access$100(FloatFFT_1D.this)[j] + this.val$ak[k]
//										* FloatFFT_1D
//												.access$100(FloatFFT_1D.this)[k]);
//								this.val$ak[k] = f;
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								f = this.val$ak[j] * FloatFFT_1D.this.bk2[k]
//										+ this.val$ak[k]
//										* FloatFFT_1D.this.bk2[j];
//								this.val$ak[j] = (this.val$ak[j]
//										* FloatFFT_1D
//												.access$100(FloatFFT_1D.this)[j] - (this.val$ak[k] * FloatFFT_1D
//										.access$100(FloatFFT_1D.this)[k]));
//								this.val$ak[k] = f;
//							}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftfsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			i1 = this.n / i;
//			for (i4 = 0; i4 < i; ++i4) {
//				i5 = i4 * i1;
//				i6 = (i4 == i - 1) ? this.n : i5 + i1;
//				arrayOfFuture[i4] = ConcurrencyUtils.submit(new Runnable(
//						paramInt2, i5, i6, paramArrayOfFloat, paramInt1,
//						arrayOfFloat) {
//					public strictfp void run() {
//						int i;
//						int j;
//						int k;
//						if (this.val$isign > 0)
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								this.val$a[(this.val$offa + j)] = (FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[j]
//										* this.val$ak[j] - (FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[k] * this.val$ak[k]));
//								this.val$a[(this.val$offa + k)] = (FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[k]
//										* this.val$ak[j] + FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[j]
//										* this.val$ak[k]);
//							}
//						else
//							for (i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//								j = 2 * i;
//								k = j + 1;
//								this.val$a[(this.val$offa + j)] = (FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[j]
//										* this.val$ak[j] + FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[k]
//										* this.val$ak[k]);
//								this.val$a[(this.val$offa + k)] = (-FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[k]
//										* this.val$ak[j] + FloatFFT_1D
//										.access$000(FloatFFT_1D.this)[j]
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
//					arrayOfFloat[i1] = (paramArrayOfFloat[i5] * this.bk1[i1]);
//					arrayOfFloat[i4] = (paramArrayOfFloat[i5] * this.bk1[i4]);
//				}
//			else
//				for (int k = 0; k < this.n; ++k) {
//					i1 = 2 * k;
//					i4 = i1 + 1;
//					i5 = paramInt1 + k;
//					arrayOfFloat[i1] = (paramArrayOfFloat[i5] * this.bk1[i1]);
//					arrayOfFloat[i4] = (-paramArrayOfFloat[i5] * this.bk1[i4]);
//				}
//			cftbsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			int l;
//			float f;
//			if (paramInt2 > 0)
//				for (l = 0; l < this.nBluestein; ++l) {
//					i1 = 2 * l;
//					i4 = i1 + 1;
//					f = -arrayOfFloat[i1] * this.bk2[i4] + arrayOfFloat[i4]
//							* this.bk2[i1];
//					arrayOfFloat[i1] = (arrayOfFloat[i1] * this.bk2[i1] + arrayOfFloat[i4]
//							* this.bk2[i4]);
//					arrayOfFloat[i4] = f;
//				}
//			else
//				for (l = 0; l < this.nBluestein; ++l) {
//					i1 = 2 * l;
//					i4 = i1 + 1;
//					f = arrayOfFloat[i1] * this.bk2[i4] + arrayOfFloat[i4]
//							* this.bk2[i1];
//					arrayOfFloat[i1] = (arrayOfFloat[i1] * this.bk2[i1] - (arrayOfFloat[i4] * this.bk2[i4]));
//					arrayOfFloat[i4] = f;
//				}
//			cftfsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			if (paramInt2 > 0)
//				for (l = 0; l < this.n; ++l) {
//					i1 = 2 * l;
//					i4 = i1 + 1;
//					paramArrayOfFloat[(paramInt1 + i1)] = (this.bk1[i1]
//							* arrayOfFloat[i1] - (this.bk1[i4] * arrayOfFloat[i4]));
//					paramArrayOfFloat[(paramInt1 + i4)] = (this.bk1[i4]
//							* arrayOfFloat[i1] + this.bk1[i1]
//							* arrayOfFloat[i4]);
//				}
//			else
//				for (l = 0; l < this.n; ++l) {
//					i1 = 2 * l;
//					i4 = i1 + 1;
//					paramArrayOfFloat[(paramInt1 + i1)] = (this.bk1[i1]
//							* arrayOfFloat[i1] + this.bk1[i4]
//							* arrayOfFloat[i4]);
//					paramArrayOfFloat[(paramInt1 + i4)] = (-this.bk1[i4]
//							* arrayOfFloat[i1] + this.bk1[i1]
//							* arrayOfFloat[i4]);
//				}
//		}
//	}
//
//	private strictfp void bluestein_real_forward(float[] paramArrayOfFloat,
//			int paramInt) {
//		float[] arrayOfFloat = new float[2 * this.nBluestein];
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
//						i4, paramInt, arrayOfFloat, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							int l = this.val$offa + i;
//							this.val$ak[j] = (this.val$a[l] * FloatFFT_1D
//									.access$000(FloatFFT_1D.this)[j]);
//							this.val$ak[k] = (-this.val$a[l] * FloatFFT_1D
//									.access$000(FloatFFT_1D.this)[k]);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftbsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			l = this.nBluestein / i;
//			for (i2 = 0; i2 < i; ++i2) {
//				i3 = i2 * l;
//				i4 = (i2 == i - 1) ? this.nBluestein : i3 + l;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, arrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							float f = this.val$ak[j] * FloatFFT_1D.this.bk2[k]
//									+ this.val$ak[k] * FloatFFT_1D.this.bk2[j];
//							this.val$ak[j] = (this.val$ak[j]
//									* FloatFFT_1D.access$100(FloatFFT_1D.this)[j] - (this.val$ak[k] * FloatFFT_1D
//									.access$100(FloatFFT_1D.this)[k]));
//							this.val$ak[k] = f;
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
//				arrayOfFloat[l] = (paramArrayOfFloat[i3] * this.bk1[l]);
//				arrayOfFloat[i2] = (-paramArrayOfFloat[i3] * this.bk1[i2]);
//			}
//			cftbsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			for (k = 0; k < this.nBluestein; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				float f = arrayOfFloat[l] * this.bk2[i2] + arrayOfFloat[i2]
//						* this.bk2[l];
//				arrayOfFloat[l] = (arrayOfFloat[l] * this.bk2[l] - (arrayOfFloat[i2] * this.bk2[i2]));
//				arrayOfFloat[i2] = f;
//			}
//		}
//		cftfsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw, this.w);
//		if (this.n % 2 == 0) {
//			paramArrayOfFloat[paramInt] = (this.bk1[0] * arrayOfFloat[0] + this.bk1[1]
//					* arrayOfFloat[1]);
//			paramArrayOfFloat[(paramInt + 1)] = (this.bk1[this.n]
//					* arrayOfFloat[this.n] + this.bk1[(this.n + 1)]
//					* arrayOfFloat[(this.n + 1)]);
//			for (k = 1; k < this.n / 2; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				paramArrayOfFloat[(paramInt + l)] = (this.bk1[l]
//						* arrayOfFloat[l] + this.bk1[i2] * arrayOfFloat[i2]);
//				paramArrayOfFloat[(paramInt + i2)] = (-this.bk1[i2]
//						* arrayOfFloat[l] + this.bk1[l] * arrayOfFloat[i2]);
//			}
//		} else {
//			paramArrayOfFloat[paramInt] = (this.bk1[0] * arrayOfFloat[0] + this.bk1[1]
//					* arrayOfFloat[1]);
//			paramArrayOfFloat[(paramInt + 1)] = (-this.bk1[this.n]
//					* arrayOfFloat[(this.n - 1)] + this.bk1[(this.n - 1)]
//					* arrayOfFloat[this.n]);
//			for (k = 1; k < (this.n - 1) / 2; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				paramArrayOfFloat[(paramInt + l)] = (this.bk1[l]
//						* arrayOfFloat[l] + this.bk1[i2] * arrayOfFloat[i2]);
//				paramArrayOfFloat[(paramInt + i2)] = (-this.bk1[i2]
//						* arrayOfFloat[l] + this.bk1[l] * arrayOfFloat[i2]);
//			}
//			paramArrayOfFloat[(paramInt + this.n - 1)] = (this.bk1[(this.n - 1)]
//					* arrayOfFloat[(this.n - 1)] + this.bk1[this.n]
//					* arrayOfFloat[this.n]);
//		}
//	}
//
//	private strictfp void bluestein_real_inverse(float[] paramArrayOfFloat,
//			int paramInt) {
//		float[] arrayOfFloat = new float[2 * this.nBluestein];
//		int j;
//		int l;
//		int i1;
//		int i3;
//		if (this.n % 2 == 0) {
//			arrayOfFloat[0] = (paramArrayOfFloat[paramInt] * this.bk1[0]);
//			arrayOfFloat[1] = (paramArrayOfFloat[paramInt] * this.bk1[1]);
//			for (i = 1; i < this.n / 2; ++i) {
//				j = 2 * i;
//				l = j + 1;
//				i1 = paramInt + j;
//				i3 = paramInt + l;
//				arrayOfFloat[j] = (paramArrayOfFloat[i1] * this.bk1[j] - (paramArrayOfFloat[i3] * this.bk1[l]));
//				arrayOfFloat[l] = (paramArrayOfFloat[i1] * this.bk1[l] + paramArrayOfFloat[i3]
//						* this.bk1[j]);
//			}
//			arrayOfFloat[this.n] = (paramArrayOfFloat[(paramInt + 1)] * this.bk1[this.n]);
//			arrayOfFloat[(this.n + 1)] = (paramArrayOfFloat[(paramInt + 1)] * this.bk1[(this.n + 1)]);
//			for (i = this.n / 2 + 1; i < this.n; ++i) {
//				j = 2 * i;
//				l = j + 1;
//				i1 = paramInt + 2 * this.n - j;
//				i3 = i1 + 1;
//				arrayOfFloat[j] = (paramArrayOfFloat[i1] * this.bk1[j] + paramArrayOfFloat[i3]
//						* this.bk1[l]);
//				arrayOfFloat[l] = (paramArrayOfFloat[i1] * this.bk1[l] - (paramArrayOfFloat[i3] * this.bk1[j]));
//			}
//		} else {
//			arrayOfFloat[0] = (paramArrayOfFloat[paramInt] * this.bk1[0]);
//			arrayOfFloat[1] = (paramArrayOfFloat[paramInt] * this.bk1[1]);
//			for (i = 1; i < (this.n - 1) / 2; ++i) {
//				j = 2 * i;
//				l = j + 1;
//				i1 = paramInt + j;
//				i3 = paramInt + l;
//				arrayOfFloat[j] = (paramArrayOfFloat[i1] * this.bk1[j] - (paramArrayOfFloat[i3] * this.bk1[l]));
//				arrayOfFloat[l] = (paramArrayOfFloat[i1] * this.bk1[l] + paramArrayOfFloat[i3]
//						* this.bk1[j]);
//			}
//			arrayOfFloat[(this.n - 1)] = (paramArrayOfFloat[(paramInt + this.n - 1)]
//					* this.bk1[(this.n - 1)] - (paramArrayOfFloat[(paramInt + 1)] * this.bk1[this.n]));
//			arrayOfFloat[this.n] = (paramArrayOfFloat[(paramInt + this.n - 1)]
//					* this.bk1[this.n] + paramArrayOfFloat[(paramInt + 1)]
//					* this.bk1[(this.n - 1)]);
//			arrayOfFloat[(this.n + 1)] = (paramArrayOfFloat[(paramInt + this.n - 1)]
//					* this.bk1[(this.n + 1)] + paramArrayOfFloat[(paramInt + 1)]
//					* this.bk1[(this.n + 2)]);
//			arrayOfFloat[(this.n + 2)] = (paramArrayOfFloat[(paramInt + this.n - 1)]
//					* this.bk1[(this.n + 2)] - (paramArrayOfFloat[(paramInt + 1)] * this.bk1[(this.n + 1)]));
//			for (i = (this.n - 1) / 2 + 2; i < this.n; ++i) {
//				j = 2 * i;
//				l = j + 1;
//				i1 = paramInt + 2 * this.n - j;
//				i3 = i1 + 1;
//				arrayOfFloat[j] = (paramArrayOfFloat[i1] * this.bk1[j] + paramArrayOfFloat[i3]
//						* this.bk1[l]);
//				arrayOfFloat[l] = (paramArrayOfFloat[i1] * this.bk1[l] - (paramArrayOfFloat[i3] * this.bk1[j]));
//			}
//		}
//		cftbsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw, this.w);
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
//						i4, arrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							float f = -this.val$ak[j] * FloatFFT_1D.this.bk2[k]
//									+ this.val$ak[k] * FloatFFT_1D.this.bk2[j];
//							this.val$ak[j] = (this.val$ak[j]
//									* FloatFFT_1D.access$100(FloatFFT_1D.this)[j] + this.val$ak[k]
//									* FloatFFT_1D.access$100(FloatFFT_1D.this)[k]);
//							this.val$ak[k] = f;
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftfsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			l = this.n / i;
//			for (i2 = 0; i2 < i; ++i2) {
//				i3 = i2 * l;
//				i4 = (i2 == i - 1) ? this.n : i3 + l;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, paramArrayOfFloat, paramInt, arrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							this.val$a[(this.val$offa + i)] = (FloatFFT_1D
//									.access$000(FloatFFT_1D.this)[j]
//									* this.val$ak[j] - (FloatFFT_1D
//									.access$000(FloatFFT_1D.this)[k] * this.val$ak[k]));
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (int k = 0; k < this.nBluestein; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				float f = -arrayOfFloat[l] * this.bk2[i2] + arrayOfFloat[i2]
//						* this.bk2[l];
//				arrayOfFloat[l] = (arrayOfFloat[l] * this.bk2[l] + arrayOfFloat[i2]
//						* this.bk2[i2]);
//				arrayOfFloat[i2] = f;
//			}
//			cftfsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			for (k = 0; k < this.n; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				paramArrayOfFloat[(paramInt + k)] = (this.bk1[l]
//						* arrayOfFloat[l] - (this.bk1[i2] * arrayOfFloat[i2]));
//			}
//		}
//	}
//
//	private strictfp void bluestein_real_inverse2(float[] paramArrayOfFloat,
//			int paramInt) {
//		float[] arrayOfFloat = new float[2 * this.nBluestein];
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
//						i4, paramInt, arrayOfFloat, paramArrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							int l = this.val$offa + i;
//							this.val$ak[j] = (this.val$a[l] * FloatFFT_1D
//									.access$000(FloatFFT_1D.this)[j]);
//							this.val$ak[k] = (this.val$a[l] * FloatFFT_1D
//									.access$000(FloatFFT_1D.this)[k]);
//						}
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//			cftbsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			l = this.nBluestein / i;
//			for (i2 = 0; i2 < i; ++i2) {
//				i3 = i2 * l;
//				i4 = (i2 == i - 1) ? this.nBluestein : i3 + l;
//				arrayOfFuture[i2] = ConcurrencyUtils.submit(new Runnable(i3,
//						i4, arrayOfFloat) {
//					public strictfp void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i) {
//							int j = 2 * i;
//							int k = j + 1;
//							float f = -this.val$ak[j] * FloatFFT_1D.this.bk2[k]
//									+ this.val$ak[k] * FloatFFT_1D.this.bk2[j];
//							this.val$ak[j] = (this.val$ak[j]
//									* FloatFFT_1D.access$100(FloatFFT_1D.this)[j] + this.val$ak[k]
//									* FloatFFT_1D.access$100(FloatFFT_1D.this)[k]);
//							this.val$ak[k] = f;
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
//				arrayOfFloat[l] = (paramArrayOfFloat[i3] * this.bk1[l]);
//				arrayOfFloat[i2] = (paramArrayOfFloat[i3] * this.bk1[i2]);
//			}
//			cftbsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw,
//					this.w);
//			for (k = 0; k < this.nBluestein; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				float f = -arrayOfFloat[l] * this.bk2[i2] + arrayOfFloat[i2]
//						* this.bk2[l];
//				arrayOfFloat[l] = (arrayOfFloat[l] * this.bk2[l] + arrayOfFloat[i2]
//						* this.bk2[i2]);
//				arrayOfFloat[i2] = f;
//			}
//		}
//		cftfsub(2 * this.nBluestein, arrayOfFloat, 0, this.ip, this.nw, this.w);
//		if (this.n % 2 == 0) {
//			paramArrayOfFloat[paramInt] = (this.bk1[0] * arrayOfFloat[0] - (this.bk1[1] * arrayOfFloat[1]));
//			paramArrayOfFloat[(paramInt + 1)] = (this.bk1[this.n]
//					* arrayOfFloat[this.n] - (this.bk1[(this.n + 1)] * arrayOfFloat[(this.n + 1)]));
//			for (k = 1; k < this.n / 2; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				paramArrayOfFloat[(paramInt + l)] = (this.bk1[l]
//						* arrayOfFloat[l] - (this.bk1[i2] * arrayOfFloat[i2]));
//				paramArrayOfFloat[(paramInt + i2)] = (this.bk1[i2]
//						* arrayOfFloat[l] + this.bk1[l] * arrayOfFloat[i2]);
//			}
//		} else {
//			paramArrayOfFloat[paramInt] = (this.bk1[0] * arrayOfFloat[0] - (this.bk1[1] * arrayOfFloat[1]));
//			paramArrayOfFloat[(paramInt + 1)] = (this.bk1[this.n]
//					* arrayOfFloat[(this.n - 1)] + this.bk1[(this.n - 1)]
//					* arrayOfFloat[this.n]);
//			for (k = 1; k < (this.n - 1) / 2; ++k) {
//				l = 2 * k;
//				i2 = l + 1;
//				paramArrayOfFloat[(paramInt + l)] = (this.bk1[l]
//						* arrayOfFloat[l] - (this.bk1[i2] * arrayOfFloat[i2]));
//				paramArrayOfFloat[(paramInt + i2)] = (this.bk1[i2]
//						* arrayOfFloat[l] + this.bk1[l] * arrayOfFloat[i2]);
//			}
//			paramArrayOfFloat[(paramInt + this.n - 1)] = (this.bk1[(this.n - 1)]
//					* arrayOfFloat[(this.n - 1)] - (this.bk1[this.n] * arrayOfFloat[this.n]));
//		}
//	}
//
//	strictfp void rfftf(float[] paramArrayOfFloat, int paramInt) {
//		if (this.n == 1)
//			return;
//		float[] arrayOfFloat = new float[this.n];
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
//					radf2(i4, i, paramArrayOfFloat, paramInt, arrayOfFloat, 0,
//							i3);
//				else
//					radf2(i4, i, arrayOfFloat, 0, paramArrayOfFloat, paramInt,
//							i3);
//				break;
//			case 3:
//				if (k == 0)
//					radf3(i4, i, paramArrayOfFloat, paramInt, arrayOfFloat, 0,
//							i3);
//				else
//					radf3(i4, i, arrayOfFloat, 0, paramArrayOfFloat, paramInt,
//							i3);
//				break;
//			case 4:
//				if (k == 0)
//					radf4(i4, i, paramArrayOfFloat, paramInt, arrayOfFloat, 0,
//							i3);
//				else
//					radf4(i4, i, arrayOfFloat, 0, paramArrayOfFloat, paramInt,
//							i3);
//				break;
//			case 5:
//				if (k == 0)
//					radf5(i4, i, paramArrayOfFloat, paramInt, arrayOfFloat, 0,
//							i3);
//				else
//					radf5(i4, i, arrayOfFloat, 0, paramArrayOfFloat, paramInt,
//							i3);
//				break;
//			default:
//				if (i4 == 1)
//					k = 1 - k;
//				if (k == 0) {
//					radfg(i4, i2, i, i5, paramArrayOfFloat, paramInt,
//							arrayOfFloat, 0, i3);
//					k = 1;
//				} else {
//					radfg(i4, i2, i, i5, arrayOfFloat, 0, paramArrayOfFloat,
//							paramInt, i3);
//					k = 0;
//				}
//			}
//			j = i;
//		}
//		if (k == 1)
//			return;
//		System.arraycopy(arrayOfFloat, 0, paramArrayOfFloat, paramInt, this.n);
//	}
//
//	strictfp void rfftb(float[] paramArrayOfFloat, int paramInt) {
//		if (this.n == 1)
//			return;
//		float[] arrayOfFloat = new float[this.n];
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
//					radb2(i3, i, paramArrayOfFloat, paramInt, arrayOfFloat, 0,
//							i2);
//				else
//					radb2(i3, i, arrayOfFloat, 0, paramArrayOfFloat, paramInt,
//							i2);
//				k = 1 - k;
//				break;
//			case 3:
//				if (k == 0)
//					radb3(i3, i, paramArrayOfFloat, paramInt, arrayOfFloat, 0,
//							i2);
//				else
//					radb3(i3, i, arrayOfFloat, 0, paramArrayOfFloat, paramInt,
//							i2);
//				k = 1 - k;
//				break;
//			case 4:
//				if (k == 0)
//					radb4(i3, i, paramArrayOfFloat, paramInt, arrayOfFloat, 0,
//							i2);
//				else
//					radb4(i3, i, arrayOfFloat, 0, paramArrayOfFloat, paramInt,
//							i2);
//				k = 1 - k;
//				break;
//			case 5:
//				if (k == 0)
//					radb5(i3, i, paramArrayOfFloat, paramInt, arrayOfFloat, 0,
//							i2);
//				else
//					radb5(i3, i, arrayOfFloat, 0, paramArrayOfFloat, paramInt,
//							i2);
//				k = 1 - k;
//				break;
//			default:
//				if (k == 0)
//					radbg(i3, i1, i, i4, paramArrayOfFloat, paramInt,
//							arrayOfFloat, 0, i2);
//				else
//					radbg(i3, i1, i, i4, arrayOfFloat, 0, paramArrayOfFloat,
//							paramInt, i2);
//				if (i3 == 1)
//					k = 1 - k;
//			}
//			i = j;
//			i2 += (i1 - 1) * i3;
//		}
//		if (k == 0)
//			return;
//		System.arraycopy(arrayOfFloat, 0, paramArrayOfFloat, paramInt, this.n);
//	}
//
//	strictfp void radf2(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5) {
//		int i4 = paramInt5;
//		int k = paramInt2 * paramInt1;
//		int l = 2 * paramInt1;
//		int i8;
//		int i9;
//		int i10;
//		int i11;
//		float f6;
//		for (int i5 = 0; i5 < paramInt2; ++i5) {
//			i8 = paramInt4 + i5 * l;
//			i9 = i8 + l - 1;
//			i10 = paramInt3 + i5 * paramInt1;
//			i11 = i10 + k;
//			float f5 = paramArrayOfFloat1[i10];
//			f6 = paramArrayOfFloat1[i11];
//			paramArrayOfFloat2[i8] = (f5 + f6);
//			paramArrayOfFloat2[i9] = (f5 - f6);
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
//					f6 = paramArrayOfFloat1[(i11 - 1)];
//					float f7 = paramArrayOfFloat1[i11];
//					float f8 = paramArrayOfFloat1[(i12 - 1)];
//					float f9 = paramArrayOfFloat1[i12];
//					float f3 = this.wtable_r[(i8 - 1)];
//					float f4 = this.wtable_r[i8];
//					float f2 = f3 * f8 + f4 * f9;
//					float f1 = f3 * f9 - (f4 * f8);
//					paramArrayOfFloat2[i9] = (f7 + f1);
//					paramArrayOfFloat2[(i9 - 1)] = (f6 + f2);
//					paramArrayOfFloat2[i10] = (f1 - f7);
//					paramArrayOfFloat2[(i10 - 1)] = (f6 - f2);
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
//			paramArrayOfFloat2[i8] = (-paramArrayOfFloat1[(i9 + k)]);
//			paramArrayOfFloat2[(i8 - 1)] = paramArrayOfFloat1[i9];
//		}
//	}
//
//	strictfp void radb2(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5) {
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
//			float f5 = paramArrayOfFloat1[i8];
//			float f6 = paramArrayOfFloat1[i9];
//			paramArrayOfFloat2[i7] = (f5 + f6);
//			paramArrayOfFloat2[(i7 + l)] = (f5 - f6);
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
//					float f3 = this.wtable_r[(i8 - 1)];
//					float f4 = this.wtable_r[i8];
//					int i12 = i10 + i5;
//					int i13 = i11 + i6;
//					int i14 = i9 + i4;
//					int i15 = i9 + i7;
//					float f2 = paramArrayOfFloat1[(i12 - 1)]
//							- paramArrayOfFloat1[(i13 - 1)];
//					float f1 = paramArrayOfFloat1[i12]
//							+ paramArrayOfFloat1[i13];
//					float f7 = paramArrayOfFloat1[i12];
//					float f8 = paramArrayOfFloat1[(i12 - 1)];
//					float f9 = paramArrayOfFloat1[i13];
//					float f10 = paramArrayOfFloat1[(i13 - 1)];
//					paramArrayOfFloat2[(i14 - 1)] = (f8 + f10);
//					paramArrayOfFloat2[i14] = (f7 - f9);
//					paramArrayOfFloat2[(i15 - 1)] = (f3 * f2 - (f4 * f1));
//					paramArrayOfFloat2[i15] = (f3 * f1 + f4 * f2);
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
//			paramArrayOfFloat2[i6] = (2.0F * paramArrayOfFloat1[(i7 - 1)]);
//			paramArrayOfFloat2[(i6 + l)] = (-2.0F * paramArrayOfFloat1[i7]);
//		}
//	}
//
//	strictfp void radf3(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5) {
//		int k = paramInt5;
//		int l = k + paramInt1;
//		int i1 = paramInt2 * paramInt1;
//		int i4;
//		int i5;
//		int i6;
//		int i7;
//		int i8;
//		int i9;
//		float f4;
//		for (int i2 = 0; i2 < paramInt2; ++i2) {
//			i4 = i2 * paramInt1;
//			i5 = 2 * i1;
//			i6 = (3 * i2 + 1) * paramInt1;
//			i7 = paramInt3 + i4;
//			i8 = i7 + i1;
//			i9 = i7 + i5;
//			float f15 = paramArrayOfFloat1[i7];
//			float f16 = paramArrayOfFloat1[i8];
//			float f17 = paramArrayOfFloat1[i9];
//			f4 = f16 + f17;
//			paramArrayOfFloat2[(paramInt4 + 3 * i4)] = (f15 + f4);
//			paramArrayOfFloat2[(paramInt4 + i6 + paramInt1)] = (0.8660254F * (f17 - f16));
//			paramArrayOfFloat2[(paramInt4 + paramInt1 - 1 + i6)] = (f15 + -0.5F
//					* f4);
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
//				float f11 = this.wtable_r[(i10 - 1)];
//				float f13 = this.wtable_r[i10];
//				float f12 = this.wtable_r[(i11 - 1)];
//				float f14 = this.wtable_r[i11];
//				int i12 = paramInt3 + i;
//				int i13 = paramInt4 + i;
//				int i14 = paramInt4 + j;
//				int i15 = i12 + i4;
//				int i16 = i12 + i6;
//				int i17 = i12 + i7;
//				float f18 = paramArrayOfFloat1[(i15 - 1)];
//				float f19 = paramArrayOfFloat1[i15];
//				float f20 = paramArrayOfFloat1[(i16 - 1)];
//				float f21 = paramArrayOfFloat1[i16];
//				float f22 = paramArrayOfFloat1[(i17 - 1)];
//				float f23 = paramArrayOfFloat1[i17];
//				float f5 = f11 * f20 + f13 * f21;
//				float f2 = f11 * f21 - (f13 * f20);
//				float f6 = f12 * f22 + f14 * f23;
//				float f3 = f12 * f23 - (f14 * f22);
//				f4 = f5 + f6;
//				float f1 = f2 + f3;
//				float f9 = f18 + -0.5F * f4;
//				float f7 = f19 + -0.5F * f1;
//				float f10 = 0.8660254F * (f2 - f3);
//				float f8 = 0.8660254F * (f6 - f5);
//				int i18 = i13 + i5;
//				int i19 = i14 + i8;
//				int i20 = i13 + i9;
//				paramArrayOfFloat2[(i18 - 1)] = (f18 + f4);
//				paramArrayOfFloat2[i18] = (f19 + f1);
//				paramArrayOfFloat2[(i19 - 1)] = (f9 - f10);
//				paramArrayOfFloat2[i19] = (f8 - f7);
//				paramArrayOfFloat2[(i20 - 1)] = (f9 + f10);
//				paramArrayOfFloat2[i20] = (f7 + f8);
//			}
//		}
//	}
//
//	strictfp void radb3(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5) {
//		int k = paramInt5;
//		int l = k + paramInt1;
//		int i4;
//		int i5;
//		float f10;
//		float f5;
//		float f2;
//		for (int i1 = 0; i1 < paramInt2; ++i1) {
//			int i2 = i1 * paramInt1;
//			i4 = paramInt3 + 3 * i2;
//			i5 = i4 + 2 * paramInt1;
//			float f15 = paramArrayOfFloat1[i4];
//			f10 = 2.0F * paramArrayOfFloat1[(i5 - 1)];
//			f5 = f15 + -0.5F * f10;
//			f2 = 1.732051F * paramArrayOfFloat1[i5];
//			paramArrayOfFloat2[(paramInt4 + i2)] = (f15 + f10);
//			paramArrayOfFloat2[(paramInt4 + (i1 + paramInt2) * paramInt1)] = (f5 - f2);
//			paramArrayOfFloat2[(paramInt4 + (i1 + 2 * paramInt2) * paramInt1)] = (f5 + f2);
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
//				float f16 = paramArrayOfFloat1[(i13 - 1)];
//				float f17 = paramArrayOfFloat1[i13];
//				float f18 = paramArrayOfFloat1[(i14 - 1)];
//				float f19 = paramArrayOfFloat1[i14];
//				float f20 = paramArrayOfFloat1[(i15 - 1)];
//				float f21 = paramArrayOfFloat1[i15];
//				f10 = f18 + f20;
//				f5 = f16 + -0.5F * f10;
//				float f9 = f19 - f21;
//				float f1 = f17 + -0.5F * f9;
//				float f6 = 0.8660254F * (f18 - f20);
//				f2 = 0.8660254F * (f19 + f21);
//				float f7 = f5 - f2;
//				float f8 = f5 + f2;
//				float f3 = f1 + f6;
//				float f4 = f1 - f6;
//				int i16 = i - 1 + k;
//				int i17 = i - 1 + l;
//				float f11 = this.wtable_r[(i16 - 1)];
//				float f13 = this.wtable_r[i16];
//				float f12 = this.wtable_r[(i17 - 1)];
//				float f14 = this.wtable_r[i17];
//				int i18 = i12 + i4;
//				int i19 = i12 + i8;
//				int i20 = i12 + i9;
//				paramArrayOfFloat2[(i18 - 1)] = (f16 + f10);
//				paramArrayOfFloat2[i18] = (f17 + f9);
//				paramArrayOfFloat2[(i19 - 1)] = (f11 * f7 - (f13 * f3));
//				paramArrayOfFloat2[i19] = (f11 * f3 + f13 * f7);
//				paramArrayOfFloat2[(i20 - 1)] = (f12 * f8 - (f14 * f4));
//				paramArrayOfFloat2[i20] = (f12 * f4 + f14 * f8);
//			}
//		}
//	}
//
//	strictfp void radf4(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5) {
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
//		float f11;
//		float f12;
//		int i16;
//		int i17;
//		for (int i3 = 0; i3 < paramInt2; ++i3) {
//			i6 = i3 * paramInt1;
//			i7 = 4 * i6;
//			i8 = i6 + i2;
//			i9 = i8 + i2;
//			i10 = i9 + i2;
//			i11 = i7 + paramInt1;
//			float f21 = paramArrayOfFloat1[(paramInt3 + i6)];
//			float f22 = paramArrayOfFloat1[(paramInt3 + i8)];
//			float f23 = paramArrayOfFloat1[(paramInt3 + i9)];
//			float f24 = paramArrayOfFloat1[(paramInt3 + i10)];
//			f11 = f22 + f24;
//			f12 = f21 + f23;
//			i16 = paramInt4 + i7;
//			i17 = paramInt4 + i11 + paramInt1;
//			paramArrayOfFloat2[i16] = (f11 + f12);
//			paramArrayOfFloat2[(i17 - 1 + paramInt1 + paramInt1)] = (f12 - f11);
//			paramArrayOfFloat2[(i17 - 1)] = (f21 - f23);
//			paramArrayOfFloat2[i17] = (f24 - f22);
//		}
//		if (paramInt1 < 2)
//			return;
//		int i12;
//		int i13;
//		int i14;
//		int i15;
//		float f7;
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
//					i16 = i - 1 + i1;
//					float f15 = this.wtable_r[(i14 - 1)];
//					float f16 = this.wtable_r[i14];
//					float f17 = this.wtable_r[(i15 - 1)];
//					float f18 = this.wtable_r[i15];
//					float f19 = this.wtable_r[(i16 - 1)];
//					float f20 = this.wtable_r[i16];
//					i17 = paramInt3 + i;
//					int i18 = paramInt4 + i;
//					int i19 = paramInt4 + j;
//					int i20 = i17 + i6;
//					int i21 = i17 + i7;
//					int i22 = i17 + i8;
//					int i23 = i17 + i9;
//					float f29 = paramArrayOfFloat1[(i20 - 1)];
//					float f30 = paramArrayOfFloat1[i20];
//					float f31 = paramArrayOfFloat1[(i21 - 1)];
//					float f32 = paramArrayOfFloat1[i21];
//					float f33 = paramArrayOfFloat1[(i22 - 1)];
//					float f34 = paramArrayOfFloat1[i22];
//					float f35 = paramArrayOfFloat1[(i23 - 1)];
//					float f36 = paramArrayOfFloat1[i23];
//					float f4 = f15 * f31 + f16 * f32;
//					float f1 = f15 * f32 - (f16 * f31);
//					float f5 = f17 * f33 + f18 * f34;
//					float f2 = f17 * f34 - (f18 * f33);
//					float f6 = f19 * f35 + f20 * f36;
//					float f3 = f19 * f36 - (f20 * f35);
//					f11 = f4 + f6;
//					float f14 = f6 - f4;
//					f7 = f1 + f3;
//					float f10 = f1 - f3;
//					float f8 = f30 + f2;
//					float f9 = f30 - f2;
//					f12 = f29 + f5;
//					float f13 = f29 - f5;
//					int i24 = i18 + i10;
//					int i25 = i19 + i11;
//					int i26 = i18 + i12;
//					int i27 = i19 + i13;
//					paramArrayOfFloat2[(i24 - 1)] = (f11 + f12);
//					paramArrayOfFloat2[(i27 - 1)] = (f12 - f11);
//					paramArrayOfFloat2[i24] = (f7 + f8);
//					paramArrayOfFloat2[i27] = (f7 - f8);
//					paramArrayOfFloat2[(i26 - 1)] = (f10 + f13);
//					paramArrayOfFloat2[(i25 - 1)] = (f13 - f10);
//					paramArrayOfFloat2[i26] = (f14 + f9);
//					paramArrayOfFloat2[i25] = (f14 - f9);
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
//			float f25 = paramArrayOfFloat1[(i14 - 1 + i6)];
//			float f26 = paramArrayOfFloat1[(i14 - 1 + i8)];
//			float f27 = paramArrayOfFloat1[(i14 - 1 + i9)];
//			float f28 = paramArrayOfFloat1[(i14 - 1 + i10)];
//			f7 = -0.7071068F * (f26 + f28);
//			f11 = 0.7071068F * (f26 - f28);
//			paramArrayOfFloat2[(i15 - 1 + i7)] = (f11 + f25);
//			paramArrayOfFloat2[(i15 - 1 + i12)] = (f25 - f11);
//			paramArrayOfFloat2[(paramInt4 + i11)] = (f7 - f27);
//			paramArrayOfFloat2[(paramInt4 + i13)] = (f7 + f27);
//		}
//	}
//
//	strictfp void radb4(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5) {
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
//		float f11;
//		float f12;
//		float f13;
//		float f14;
//		for (int i3 = 0; i3 < paramInt2; ++i3) {
//			i6 = i3 * paramInt1;
//			i7 = 4 * i6;
//			i8 = i6 + i2;
//			i9 = i8 + i2;
//			i10 = i9 + i2;
//			i11 = i7 + paramInt1;
//			i12 = i11 + paramInt1;
//			i13 = i12 + paramInt1;
//			float f21 = paramArrayOfFloat1[(paramInt3 + i7)];
//			float f22 = paramArrayOfFloat1[(paramInt3 + i12)];
//			float f23 = paramArrayOfFloat1[(paramInt3 + paramInt1 - 1 + i13)];
//			float f25 = paramArrayOfFloat1[(paramInt3 + paramInt1 - 1 + i11)];
//			f11 = f21 - f23;
//			f12 = f21 + f23;
//			f13 = f25 + f25;
//			f14 = f22 + f22;
//			paramArrayOfFloat2[(paramInt4 + i6)] = (f12 + f13);
//			paramArrayOfFloat2[(paramInt4 + i8)] = (f11 - f14);
//			paramArrayOfFloat2[(paramInt4 + i9)] = (f12 - f13);
//			paramArrayOfFloat2[(paramInt4 + i10)] = (f11 + f14);
//		}
//		if (paramInt1 < 2)
//			return;
//		int i14;
//		int i15;
//		float f7;
//		float f8;
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
//					float f15 = this.wtable_r[(i14 - 1)];
//					float f16 = this.wtable_r[i14];
//					float f17 = this.wtable_r[(i15 - 1)];
//					float f18 = this.wtable_r[i15];
//					float f19 = this.wtable_r[(i16 - 1)];
//					float f20 = this.wtable_r[i16];
//					int i17 = paramInt3 + i;
//					int i18 = paramInt3 + j;
//					int i19 = paramInt4 + i;
//					int i20 = i17 + i10;
//					int i21 = i18 + i11;
//					int i22 = i17 + i12;
//					int i23 = i18 + i13;
//					float f29 = paramArrayOfFloat1[(i20 - 1)];
//					float f30 = paramArrayOfFloat1[i20];
//					float f31 = paramArrayOfFloat1[(i21 - 1)];
//					float f32 = paramArrayOfFloat1[i21];
//					float f33 = paramArrayOfFloat1[(i22 - 1)];
//					float f34 = paramArrayOfFloat1[i22];
//					float f35 = paramArrayOfFloat1[(i23 - 1)];
//					float f36 = paramArrayOfFloat1[i23];
//					f7 = f30 + f36;
//					f8 = f30 - f36;
//					float f9 = f34 - f32;
//					f14 = f34 + f32;
//					f11 = f29 - f35;
//					f12 = f29 + f35;
//					float f10 = f33 - f31;
//					f13 = f33 + f31;
//					float f5 = f12 - f13;
//					float f2 = f8 - f9;
//					float f4 = f11 - f14;
//					float f6 = f11 + f14;
//					float f1 = f7 + f10;
//					float f3 = f7 - f10;
//					int i24 = i19 + i6;
//					int i25 = i19 + i7;
//					int i26 = i19 + i8;
//					int i27 = i19 + i9;
//					paramArrayOfFloat2[(i24 - 1)] = (f12 + f13);
//					paramArrayOfFloat2[i24] = (f8 + f9);
//					paramArrayOfFloat2[(i25 - 1)] = (f15 * f4 - (f16 * f1));
//					paramArrayOfFloat2[i25] = (f15 * f1 + f16 * f4);
//					paramArrayOfFloat2[(i26 - 1)] = (f17 * f5 - (f18 * f2));
//					paramArrayOfFloat2[i26] = (f17 * f2 + f18 * f5);
//					paramArrayOfFloat2[(i27 - 1)] = (f19 * f6 - (f20 * f3));
//					paramArrayOfFloat2[i27] = (f19 * f3 + f20 * f6);
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
//			float f24 = paramArrayOfFloat1[(i14 - 1 + i7)];
//			float f26 = paramArrayOfFloat1[(i14 - 1 + i12)];
//			float f27 = paramArrayOfFloat1[(paramInt3 + i11)];
//			float f28 = paramArrayOfFloat1[(paramInt3 + i13)];
//			f7 = f27 + f28;
//			f8 = f28 - f27;
//			f11 = f24 - f26;
//			f12 = f24 + f26;
//			paramArrayOfFloat2[(i15 - 1 + i6)] = (f12 + f12);
//			paramArrayOfFloat2[(i15 - 1 + i8)] = (1.414214F * (f11 - f7));
//			paramArrayOfFloat2[(i15 - 1 + i9)] = (f8 + f8);
//			paramArrayOfFloat2[(i15 - 1 + i10)] = (-1.414214F * (f11 + f7));
//		}
//	}
//
//	strictfp void radf5(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5) {
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
//		float f9;
//		float f4;
//		float f10;
//		float f3;
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
//			float f33 = paramArrayOfFloat1[(paramInt3 + i6)];
//			float f34 = paramArrayOfFloat1[(paramInt3 + i12)];
//			float f35 = paramArrayOfFloat1[(paramInt3 + i13)];
//			float f36 = paramArrayOfFloat1[(paramInt3 + i14)];
//			float f37 = paramArrayOfFloat1[(paramInt3 + i15)];
//			f9 = f37 + f34;
//			f4 = f37 - f34;
//			f10 = f36 + f35;
//			f3 = f36 - f35;
//			paramArrayOfFloat2[(paramInt4 + i7)] = (f33 + f9 + f10);
//			paramArrayOfFloat2[(i16 + i8)] = (f33 + 0.309017F * f9 + -0.809017F
//					* f10);
//			paramArrayOfFloat2[(paramInt4 + i9)] = (0.9510565F * f4 + 0.5877852F * f3);
//			paramArrayOfFloat2[(i16 + i10)] = (f33 + -0.809017F * f9 + 0.309017F * f10);
//			paramArrayOfFloat2[(paramInt4 + i11)] = (0.5877852F * f4 - (0.9510565F * f3));
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
//				float f25 = this.wtable_r[(i16 - 1)];
//				float f26 = this.wtable_r[i16];
//				float f27 = this.wtable_r[(i17 - 1)];
//				float f28 = this.wtable_r[i17];
//				float f29 = this.wtable_r[(i18 - 1)];
//				float f30 = this.wtable_r[i18];
//				float f31 = this.wtable_r[(i19 - 1)];
//				float f32 = this.wtable_r[i19];
//				int j = paramInt1 - i;
//				int i20 = paramInt3 + i;
//				int i21 = paramInt4 + i;
//				int i22 = paramInt4 + j;
//				int i23 = i20 + i6;
//				int i24 = i20 + i12;
//				int i25 = i20 + i13;
//				int i26 = i20 + i14;
//				int i27 = i20 + i15;
//				float f38 = paramArrayOfFloat1[(i23 - 1)];
//				float f39 = paramArrayOfFloat1[i23];
//				float f40 = paramArrayOfFloat1[(i24 - 1)];
//				float f41 = paramArrayOfFloat1[i24];
//				float f42 = paramArrayOfFloat1[(i25 - 1)];
//				float f43 = paramArrayOfFloat1[i25];
//				float f44 = paramArrayOfFloat1[(i26 - 1)];
//				float f45 = paramArrayOfFloat1[i26];
//				float f46 = paramArrayOfFloat1[(i27 - 1)];
//				float f47 = paramArrayOfFloat1[i27];
//				float f11 = f25 * f40 + f26 * f41;
//				float f2 = f25 * f41 - (f26 * f40);
//				float f12 = f27 * f42 + f28 * f43;
//				float f5 = f27 * f43 - (f28 * f42);
//				float f13 = f29 * f44 + f30 * f45;
//				float f6 = f29 * f45 - (f30 * f44);
//				float f14 = f31 * f46 + f32 * f47;
//				float f7 = f31 * f47 - (f32 * f46);
//				f9 = f11 + f14;
//				f4 = f14 - f11;
//				float f15 = f2 - f7;
//				float f1 = f2 + f7;
//				f10 = f12 + f13;
//				f3 = f13 - f12;
//				float f16 = f5 - f6;
//				float f8 = f5 + f6;
//				float f21 = f38 + 0.309017F * f9 + -0.809017F * f10;
//				float f17 = f39 + 0.309017F * f1 + -0.809017F * f8;
//				float f22 = f38 + -0.809017F * f9 + 0.309017F * f10;
//				float f18 = f39 + -0.809017F * f1 + 0.309017F * f8;
//				float f24 = 0.9510565F * f15 + 0.5877852F * f16;
//				float f19 = 0.9510565F * f4 + 0.5877852F * f3;
//				float f23 = 0.5877852F * f15 - (0.9510565F * f16);
//				float f20 = 0.5877852F * f4 - (0.9510565F * f3);
//				int i28 = i21 + i7;
//				int i29 = i22 + i8;
//				int i30 = i21 + i9;
//				int i31 = i22 + i10;
//				int i32 = i21 + i11;
//				paramArrayOfFloat2[(i28 - 1)] = (f38 + f9 + f10);
//				paramArrayOfFloat2[i28] = (f39 + f1 + f8);
//				paramArrayOfFloat2[(i30 - 1)] = (f21 + f24);
//				paramArrayOfFloat2[(i29 - 1)] = (f21 - f24);
//				paramArrayOfFloat2[i30] = (f17 + f19);
//				paramArrayOfFloat2[i29] = (f19 - f17);
//				paramArrayOfFloat2[(i32 - 1)] = (f22 + f23);
//				paramArrayOfFloat2[(i31 - 1)] = (f22 - f23);
//				paramArrayOfFloat2[i32] = (f18 + f20);
//				paramArrayOfFloat2[i31] = (f20 - f18);
//			}
//		}
//	}
//
//	strictfp void radb5(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5) {
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
//		float f16;
//		float f15;
//		float f21;
//		float f22;
//		float f9;
//		float f10;
//		float f4;
//		float f3;
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
//			float f33 = paramArrayOfFloat1[(paramInt3 + i7)];
//			f16 = 2.0F * paramArrayOfFloat1[(paramInt3 + i9)];
//			f15 = 2.0F * paramArrayOfFloat1[(paramInt3 + i11)];
//			f21 = 2.0F * paramArrayOfFloat1[(i16 + i8)];
//			f22 = 2.0F * paramArrayOfFloat1[(i16 + i10)];
//			f9 = f33 + 0.309017F * f21 + -0.809017F * f22;
//			f10 = f33 + -0.809017F * f21 + 0.309017F * f22;
//			f4 = 0.9510565F * f16 + 0.5877852F * f15;
//			f3 = 0.5877852F * f16 - (0.9510565F * f15);
//			paramArrayOfFloat2[(paramInt4 + i6)] = (f33 + f21 + f22);
//			paramArrayOfFloat2[(paramInt4 + i12)] = (f9 - f4);
//			paramArrayOfFloat2[(paramInt4 + i13)] = (f10 - f3);
//			paramArrayOfFloat2[(paramInt4 + i14)] = (f10 + f3);
//			paramArrayOfFloat2[(paramInt4 + i15)] = (f9 + f4);
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
//				float f25 = this.wtable_r[(i16 - 1)];
//				float f26 = this.wtable_r[i16];
//				float f27 = this.wtable_r[(i17 - 1)];
//				float f28 = this.wtable_r[i17];
//				float f29 = this.wtable_r[(i18 - 1)];
//				float f30 = this.wtable_r[i18];
//				float f31 = this.wtable_r[(i19 - 1)];
//				float f32 = this.wtable_r[i19];
//				int i20 = paramInt3 + i;
//				int i21 = paramInt3 + j;
//				int i22 = paramInt4 + i;
//				int i23 = i20 + i7;
//				int i24 = i21 + i8;
//				int i25 = i20 + i9;
//				int i26 = i21 + i10;
//				int i27 = i20 + i11;
//				float f34 = paramArrayOfFloat1[(i23 - 1)];
//				float f35 = paramArrayOfFloat1[i23];
//				float f36 = paramArrayOfFloat1[(i24 - 1)];
//				float f37 = paramArrayOfFloat1[i24];
//				float f38 = paramArrayOfFloat1[(i25 - 1)];
//				float f39 = paramArrayOfFloat1[i25];
//				float f40 = paramArrayOfFloat1[(i26 - 1)];
//				float f41 = paramArrayOfFloat1[i26];
//				float f42 = paramArrayOfFloat1[(i27 - 1)];
//				float f43 = paramArrayOfFloat1[i27];
//				f16 = f39 + f37;
//				float f13 = f39 - f37;
//				f15 = f43 + f41;
//				float f14 = f43 - f41;
//				float f24 = f38 - f36;
//				f21 = f38 + f36;
//				float f23 = f42 - f40;
//				f22 = f42 + f40;
//				f9 = f34 + 0.309017F * f21 + -0.809017F * f22;
//				float f1 = f35 + 0.309017F * f13 + -0.809017F * f14;
//				f10 = f34 + -0.809017F * f21 + 0.309017F * f22;
//				float f2 = f35 + -0.809017F * f13 + 0.309017F * f14;
//				float f11 = 0.9510565F * f24 + 0.5877852F * f23;
//				f4 = 0.9510565F * f16 + 0.5877852F * f15;
//				float f12 = 0.5877852F * f24 - (0.9510565F * f23);
//				f3 = 0.5877852F * f16 - (0.9510565F * f15);
//				float f17 = f10 - f3;
//				float f18 = f10 + f3;
//				float f5 = f2 + f12;
//				float f6 = f2 - f12;
//				float f19 = f9 + f4;
//				float f20 = f9 - f4;
//				float f7 = f1 - f11;
//				float f8 = f1 + f11;
//				int i28 = i22 + i6;
//				int i29 = i22 + i12;
//				int i30 = i22 + i13;
//				int i31 = i22 + i14;
//				int i32 = i22 + i15;
//				paramArrayOfFloat2[(i28 - 1)] = (f34 + f21 + f22);
//				paramArrayOfFloat2[i28] = (f35 + f13 + f14);
//				paramArrayOfFloat2[(i29 - 1)] = (f25 * f20 - (f26 * f8));
//				paramArrayOfFloat2[i29] = (f25 * f8 + f26 * f20);
//				paramArrayOfFloat2[(i30 - 1)] = (f27 * f17 - (f28 * f5));
//				paramArrayOfFloat2[i30] = (f27 * f5 + f28 * f17);
//				paramArrayOfFloat2[(i31 - 1)] = (f29 * f18 - (f30 * f6));
//				paramArrayOfFloat2[i31] = (f29 * f6 + f30 * f18);
//				paramArrayOfFloat2[(i32 - 1)] = (f31 * f19 - (f32 * f7));
//				paramArrayOfFloat2[i32] = (f31 * f7 + f32 * f19);
//			}
//		}
//	}
//
//	strictfp void radfg(int paramInt1, int paramInt2, int paramInt3,
//			int paramInt4, float[] paramArrayOfFloat1, int paramInt5,
//			float[] paramArrayOfFloat2, int paramInt6, int paramInt7) {
//		int i5 = paramInt7;
//		float f8 = 6.283186F / paramInt2;
//		float f7 = (float) Math.cos(f8);
//		float f9 = (float) Math.sin(f8);
//		int j = (paramInt2 + 1) / 2;
//		int i4 = (paramInt1 - 1) / 2;
//		int i29;
//		int i33;
//		int i40;
//		int i19;
//		int i1;
//		int i49;
//		int i51;
//		int i52;
//		int i36;
//		if (paramInt1 != 1) {
//			for (int i6 = 0; i6 < paramInt4; ++i6)
//				paramArrayOfFloat2[(paramInt6 + i6)] = paramArrayOfFloat1[(paramInt5 + i6)];
//			int i24;
//			for (int i7 = 1; i7 < paramInt2; ++i7) {
//				i13 = i7 * paramInt3 * paramInt1;
//				for (int i17 = 0; i17 < paramInt3; ++i17) {
//					i24 = i17 * paramInt1 + i13;
//					paramArrayOfFloat2[(paramInt6 + i24)] = paramArrayOfFloat1[(paramInt5 + i24)];
//				}
//			}
//			int i3;
//			int i;
//			float f12;
//			float f13;
//			int i34;
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
//						f12 = this.wtable_r[(i24 - 1)];
//						f13 = this.wtable_r[i24];
//						for (i34 = 0; i34 < paramInt3; ++i34) {
//							i40 = i34 * paramInt1 + i13;
//							int i42 = i33 + i40;
//							int i45 = i29 + i40;
//							float f18 = paramArrayOfFloat1[(i45 - 1)];
//							float f19 = paramArrayOfFloat1[i45];
//							paramArrayOfFloat2[(i42 - 1)] = (f12 * f18 + f13
//									* f19);
//							paramArrayOfFloat2[i42] = (f12 * f19 - (f13 * f18));
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
//							f12 = this.wtable_r[(i33 - 1)];
//							f13 = this.wtable_r[i33];
//							i34 = paramInt6 + i29 + i24;
//							i40 = paramInt5 + i29 + i24;
//							float f14 = paramArrayOfFloat1[(i40 - 1)];
//							float f16 = paramArrayOfFloat1[i40];
//							paramArrayOfFloat2[(i34 - 1)] = (f12 * f14 + f13
//									* f16);
//							paramArrayOfFloat2[i34] = (f12 * f16 - (f13 * f14));
//						}
//					}
//				}
//			}
//			int i43;
//			int i46;
//			float f20;
//			float f21;
//			float f22;
//			float f23;
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
//							i46 = i40 + i29;
//							i49 = i40 + i33;
//							i51 = i43 + i29;
//							i52 = i43 + i33;
//							f20 = paramArrayOfFloat2[(i51 - 1)];
//							f21 = paramArrayOfFloat2[i51];
//							f22 = paramArrayOfFloat2[(i52 - 1)];
//							f23 = paramArrayOfFloat2[i52];
//							paramArrayOfFloat1[(i46 - 1)] = (f20 + f22);
//							paramArrayOfFloat1[i46] = (f21 + f23);
//							paramArrayOfFloat1[(i49 - 1)] = (f21 - f23);
//							paramArrayOfFloat1[i49] = (f22 - f20);
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
//							i46 = i29 + i40;
//							i49 = i29 + i43;
//							i51 = i33 + i40;
//							i52 = i33 + i43;
//							f20 = paramArrayOfFloat2[(i51 - 1)];
//							f21 = paramArrayOfFloat2[i51];
//							f22 = paramArrayOfFloat2[(i52 - 1)];
//							f23 = paramArrayOfFloat2[i52];
//							paramArrayOfFloat1[(i46 - 1)] = (f20 + f22);
//							paramArrayOfFloat1[i46] = (f21 + f23);
//							paramArrayOfFloat1[(i49 - 1)] = (f21 - f23);
//							paramArrayOfFloat1[i49] = (f22 - f20);
//						}
//					}
//				}
//		} else {
//			System.arraycopy(paramArrayOfFloat2, paramInt6, paramArrayOfFloat1,
//					paramInt5, paramInt4);
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
//				float f15 = paramArrayOfFloat2[i36];
//				float f17 = paramArrayOfFloat2[i40];
//				paramArrayOfFloat1[(paramInt5 + i29)] = (f15 + f17);
//				paramArrayOfFloat1[(paramInt5 + i33)] = (f17 - f15);
//			}
//		}
//		float f4 = 1.0F;
//		float f2 = 0.0F;
//		i12 = (paramInt2 - 1) * paramInt4;
//		int i41;
//		int i44;
//		int i47;
//		for (int i13 = 1; i13 < j; ++i13) {
//			int i2 = paramInt2 - i13;
//			float f10 = f7 * f4 - (f9 * f2);
//			f2 = f7 * f2 + f9 * f4;
//			f4 = f10;
//			i19 = i13 * paramInt4;
//			i27 = i2 * paramInt4;
//			for (int i30 = 0; i30 < paramInt4; ++i30) {
//				i33 = paramInt6 + i30;
//				i36 = paramInt5 + i30;
//				paramArrayOfFloat2[(i33 + i19)] = (paramArrayOfFloat1[i36] + f4
//						* paramArrayOfFloat1[(i36 + paramInt4)]);
//				paramArrayOfFloat2[(i33 + i27)] = (f2 * paramArrayOfFloat1[(i36 + i12)]);
//			}
//			float f1 = f4;
//			float f6 = f2;
//			float f5 = f4;
//			float f3 = f2;
//			for (int i31 = 2; i31 < j; ++i31) {
//				i1 = paramInt2 - i31;
//				float f11 = f1 * f5 - (f6 * f3);
//				f3 = f1 * f3 + f6 * f5;
//				f5 = f11;
//				i33 = i31 * paramInt4;
//				i36 = i1 * paramInt4;
//				for (i41 = 0; i41 < paramInt4; ++i41) {
//					i44 = paramInt6 + i41;
//					i47 = paramInt5 + i41;
//					paramArrayOfFloat2[(i44 + i19)] += f5
//							* paramArrayOfFloat1[(i47 + i33)];
//					paramArrayOfFloat2[(i44 + i27)] += f3
//							* paramArrayOfFloat1[(i47 + i36)];
//				}
//			}
//		}
//		int i28;
//		for (int i14 = 1; i14 < j; ++i14) {
//			i19 = i14 * paramInt4;
//			for (i28 = 0; i28 < paramInt4; ++i28)
//				paramArrayOfFloat2[(paramInt6 + i28)] += paramArrayOfFloat1[(paramInt5
//						+ i28 + i19)];
//		}
//		int i32;
//		if (paramInt1 >= paramInt3)
//			for (int i15 = 0; i15 < paramInt3; ++i15) {
//				i19 = i15 * paramInt1;
//				i28 = i19 * paramInt2;
//				for (i32 = 0; i32 < paramInt1; ++i32)
//					paramArrayOfFloat1[(paramInt5 + i32 + i28)] = paramArrayOfFloat2[(paramInt6
//							+ i32 + i19)];
//			}
//		else
//			for (i16 = 0; i16 < paramInt1; ++i16)
//				for (int i20 = 0; i20 < paramInt3; ++i20) {
//					i28 = i20 * paramInt1;
//					paramArrayOfFloat1[(paramInt5 + i16 + i28 * paramInt2)] = paramArrayOfFloat2[(paramInt6
//							+ i16 + i28)];
//				}
//		int i16 = paramInt2 * paramInt1;
//		int k;
//		for (int i21 = 1; i21 < j; ++i21) {
//			i1 = paramInt2 - i21;
//			k = 2 * i21;
//			i28 = i21 * paramInt3 * paramInt1;
//			i32 = i1 * paramInt3 * paramInt1;
//			i33 = k * paramInt1;
//			for (int i37 = 0; i37 < paramInt3; ++i37) {
//				i41 = i37 * paramInt1;
//				i44 = i41 + i28;
//				i47 = i41 + i32;
//				i49 = i37 * i16;
//				paramArrayOfFloat1[(paramInt5 + paramInt1 - 1 + i33 - paramInt1 + i49)] = paramArrayOfFloat2[(paramInt6 + i44)];
//				paramArrayOfFloat1[(paramInt5 + i33 + i49)] = paramArrayOfFloat2[(paramInt6 + i47)];
//			}
//		}
//		if (paramInt1 == 1)
//			return;
//		int i48;
//		int l;
//		int i53;
//		int i54;
//		int i55;
//		int i56;
//		float f24;
//		float f25;
//		float f26;
//		float f27;
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
//					for (i48 = 2; i48 < paramInt1; i48 += 2) {
//						l = paramInt1 - i48;
//						i49 = paramInt5 + i48;
//						i51 = paramInt5 + l;
//						i52 = paramInt6 + i48;
//						i53 = i49 + i33 + i41;
//						i54 = i51 + i33 - paramInt1 + i41;
//						i55 = i52 + i44 + i28;
//						i56 = i52 + i44 + i32;
//						f24 = paramArrayOfFloat2[(i55 - 1)];
//						f25 = paramArrayOfFloat2[i55];
//						f26 = paramArrayOfFloat2[(i56 - 1)];
//						f27 = paramArrayOfFloat2[i56];
//						paramArrayOfFloat1[(i53 - 1)] = (f24 + f26);
//						paramArrayOfFloat1[(i54 - 1)] = (f24 - f26);
//						paramArrayOfFloat1[i53] = (f25 + f27);
//						paramArrayOfFloat1[i54] = (f27 - f25);
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
//					i48 = paramInt6 + i39;
//					for (int i50 = 0; i50 < paramInt3; ++i50) {
//						i51 = i50 * i16;
//						i52 = i50 * paramInt1;
//						i53 = i41 + i33 + i51;
//						i54 = i44 + i33 - paramInt1 + i51;
//						i55 = i48 + i52 + i28;
//						i56 = i48 + i52 + i32;
//						f24 = paramArrayOfFloat2[(i55 - 1)];
//						f25 = paramArrayOfFloat2[i55];
//						f26 = paramArrayOfFloat2[(i56 - 1)];
//						f27 = paramArrayOfFloat2[i56];
//						paramArrayOfFloat1[(i53 - 1)] = (f24 + f26);
//						paramArrayOfFloat1[(i54 - 1)] = (f24 - f26);
//						paramArrayOfFloat1[i53] = (f25 + f27);
//						paramArrayOfFloat1[i54] = (f27 - f25);
//					}
//				}
//			}
//	}
//
//	strictfp void radbg(int paramInt1, int paramInt2, int paramInt3,
//			int paramInt4, float[] paramArrayOfFloat1, int paramInt5,
//			float[] paramArrayOfFloat2, int paramInt6, int paramInt7) {
//		int i5 = paramInt7;
//		float f10 = 6.283186F / paramInt2;
//		float f9 = (float) Math.cos(f10);
//		float f11 = (float) Math.sin(f10);
//		int i4 = (paramInt1 - 1) / 2;
//		int j = (paramInt2 + 1) / 2;
//		int i6 = paramInt2 * paramInt1;
//		int i21;
//		if (paramInt1 >= paramInt3)
//			for (int i7 = 0; i7 < paramInt3; ++i7) {
//				i9 = i7 * paramInt1;
//				i12 = i7 * i6;
//				for (int i20 = 0; i20 < paramInt1; ++i20)
//					paramArrayOfFloat2[(paramInt6 + i20 + i9)] = paramArrayOfFloat1[(paramInt5
//							+ i20 + i12)];
//			}
//		else
//			for (i8 = 0; i8 < paramInt1; ++i8) {
//				i9 = paramInt6 + i8;
//				i12 = paramInt5 + i8;
//				for (i21 = 0; i21 < paramInt3; ++i21)
//					paramArrayOfFloat2[(i9 + i21 * paramInt1)] = paramArrayOfFloat1[(i12 + i21
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
//				float f14 = paramArrayOfFloat1[i39];
//				float f16 = paramArrayOfFloat1[i42];
//				paramArrayOfFloat2[(paramInt6 + i35 + i12)] = (f14 + f14);
//				paramArrayOfFloat2[(paramInt6 + i35 + i21)] = (f16 + f16);
//			}
//		}
//		int i44;
//		int i48;
//		int i49;
//		float f23;
//		int i43;
//		if (paramInt1 != 1) {
//			int l;
//			int i46;
//			int i50;
//			int i51;
//			int i52;
//			float f24;
//			float f25;
//			float f26;
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
//							i48 = paramInt5 + i42;
//							i49 = i44 + i35;
//							i50 = i44 + i37;
//							i51 = i48 + i39;
//							i52 = i46 + i39 - paramInt1;
//							f23 = paramArrayOfFloat1[(i51 - 1)];
//							f24 = paramArrayOfFloat1[i51];
//							f25 = paramArrayOfFloat1[(i52 - 1)];
//							f26 = paramArrayOfFloat1[i52];
//							paramArrayOfFloat2[(i49 - 1)] = (f23 + f25);
//							paramArrayOfFloat2[(i50 - 1)] = (f23 - f25);
//							paramArrayOfFloat2[i49] = (f24 - f26);
//							paramArrayOfFloat2[i50] = (f24 + f26);
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
//							i48 = i43 * paramInt2 * paramInt1 + i22;
//							i49 = i35 + i44;
//							i50 = i35 + i46;
//							i51 = i39 + i48;
//							i52 = i37 + i48 - paramInt1;
//							f23 = paramArrayOfFloat1[(i51 - 1)];
//							f24 = paramArrayOfFloat1[i51];
//							f25 = paramArrayOfFloat1[(i52 - 1)];
//							f26 = paramArrayOfFloat1[i52];
//							paramArrayOfFloat2[(i49 - 1)] = (f23 + f25);
//							paramArrayOfFloat2[(i50 - 1)] = (f23 - f25);
//							paramArrayOfFloat2[i49] = (f24 - f26);
//							paramArrayOfFloat2[i50] = (f24 + f26);
//						}
//					}
//				}
//		}
//		float f4 = 1.0F;
//		float f2 = 0.0F;
//		int i11 = (paramInt2 - 1) * paramInt4;
//		int i31;
//		for (int i12 = 1; i12 < j; ++i12) {
//			int i2 = paramInt2 - i12;
//			float f12 = f9 * f4 - (f11 * f2);
//			f2 = f9 * f2 + f11 * f4;
//			f4 = f12;
//			i21 = i12 * paramInt4;
//			i22 = i2 * paramInt4;
//			for (int i30 = 0; i30 < paramInt4; ++i30) {
//				i35 = paramInt5 + i30;
//				i37 = paramInt6 + i30;
//				paramArrayOfFloat1[(i35 + i21)] = (paramArrayOfFloat2[i37] + f4
//						* paramArrayOfFloat2[(i37 + paramInt4)]);
//				paramArrayOfFloat1[(i35 + i22)] = (f2 * paramArrayOfFloat2[(i37 + i11)]);
//			}
//			float f1 = f4;
//			float f6 = f2;
//			float f5 = f4;
//			float f3 = f2;
//			for (i31 = 2; i31 < j; ++i31) {
//				i1 = paramInt2 - i31;
//				float f13 = f1 * f5 - (f6 * f3);
//				f3 = f1 * f3 + f6 * f5;
//				f5 = f13;
//				i35 = i31 * paramInt4;
//				i37 = i1 * paramInt4;
//				for (i39 = 0; i39 < paramInt4; ++i39) {
//					i43 = paramInt5 + i39;
//					i44 = paramInt6 + i39;
//					paramArrayOfFloat1[(i43 + i21)] += f5
//							* paramArrayOfFloat2[(i44 + i35)];
//					paramArrayOfFloat1[(i43 + i22)] += f3
//							* paramArrayOfFloat2[(i44 + i37)];
//				}
//			}
//		}
//		int i23;
//		for (int i13 = 1; i13 < j; ++i13) {
//			i21 = i13 * paramInt4;
//			for (i23 = 0; i23 < paramInt4; ++i23) {
//				i31 = paramInt6 + i23;
//				paramArrayOfFloat2[i31] += paramArrayOfFloat2[(i31 + i21)];
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
//				float f15 = paramArrayOfFloat1[i39];
//				float f17 = paramArrayOfFloat1[i43];
//				paramArrayOfFloat2[(i37 + i21)] = (f15 - f17);
//				paramArrayOfFloat2[(i37 + i23)] = (f15 + f17);
//			}
//		}
//		if (paramInt1 == 1)
//			return;
//		int i38;
//		int i45;
//		int i47;
//		float f20;
//		float f21;
//		float f22;
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
//						i47 = i39 + i35 + i23;
//						i48 = i43 + i35 + i21;
//						i49 = i43 + i35 + i23;
//						f20 = paramArrayOfFloat1[(i48 - 1)];
//						f21 = paramArrayOfFloat1[i48];
//						f22 = paramArrayOfFloat1[(i49 - 1)];
//						f23 = paramArrayOfFloat1[i49];
//						paramArrayOfFloat2[(i45 - 1)] = (f20 - f23);
//						paramArrayOfFloat2[(i47 - 1)] = (f20 + f23);
//						paramArrayOfFloat2[i45] = (f21 + f22);
//						paramArrayOfFloat2[i47] = (f21 - f22);
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
//						i47 = i35 + i43 + i23;
//						i48 = i38 + i43 + i21;
//						i49 = i38 + i43 + i23;
//						f20 = paramArrayOfFloat1[(i48 - 1)];
//						f21 = paramArrayOfFloat1[i48];
//						f22 = paramArrayOfFloat1[(i49 - 1)];
//						f23 = paramArrayOfFloat1[i49];
//						paramArrayOfFloat2[(i45 - 1)] = (f20 - f23);
//						paramArrayOfFloat2[(i47 - 1)] = (f20 + f23);
//						paramArrayOfFloat2[i45] = (f21 + f22);
//						paramArrayOfFloat2[i47] = (f21 - f22);
//					}
//				}
//			}
//		System.arraycopy(paramArrayOfFloat2, paramInt6, paramArrayOfFloat1,
//				paramInt5, paramInt4);
//		for (int i17 = 1; i17 < paramInt2; ++i17) {
//			i21 = i17 * paramInt3 * paramInt1;
//			for (int i24 = 0; i24 < paramInt3; ++i24) {
//				i34 = i24 * paramInt1 + i21;
//				paramArrayOfFloat1[(paramInt5 + i34)] = paramArrayOfFloat2[(paramInt6 + i34)];
//			}
//		}
//		int i3;
//		int i;
//		float f7;
//		float f8;
//		int i41;
//		float f18;
//		float f19;
//		if (i4 <= paramInt3) {
//			i3 = -paramInt1;
//			for (int i18 = 1; i18 < paramInt2; ++i18) {
//				i3 += paramInt1;
//				i = i3 - 1;
//				i21 = i18 * paramInt3 * paramInt1;
//				for (int i25 = 2; i25 < paramInt1; i25 += 2) {
//					i34 = (i += 2) + i5;
//					f7 = this.wtable_r[(i34 - 1)];
//					f8 = this.wtable_r[i34];
//					i35 = paramInt5 + i25;
//					i38 = paramInt6 + i25;
//					for (i41 = 0; i41 < paramInt3; ++i41) {
//						i43 = i41 * paramInt1 + i21;
//						i45 = i35 + i43;
//						i47 = i38 + i43;
//						f18 = paramArrayOfFloat2[(i47 - 1)];
//						f19 = paramArrayOfFloat2[i47];
//						paramArrayOfFloat1[(i45 - 1)] = (f7 * f18 - (f8 * f19));
//						paramArrayOfFloat1[i45] = (f7 * f19 + f8 * f18);
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
//						f7 = this.wtable_r[(i38 - 1)];
//						f8 = this.wtable_r[i38];
//						i41 = paramInt5 + i36;
//						i43 = paramInt6 + i36;
//						i45 = i41 + i34;
//						i47 = i43 + i34;
//						f18 = paramArrayOfFloat2[(i47 - 1)];
//						f19 = paramArrayOfFloat2[i47];
//						paramArrayOfFloat1[(i45 - 1)] = (f7 * f18 - (f8 * f19));
//						paramArrayOfFloat1[i45] = (f7 * f19 + f8 * f18);
//					}
//				}
//			}
//		}
//	}
//
//	strictfp void cfftf(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
//		int[] arrayOfInt = new int[1];
//		int i6 = 2 * this.n;
//		float[] arrayOfFloat = new float[i6];
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
//					passf4(i, j, paramArrayOfFloat, paramInt1, arrayOfFloat, 0,
//							i3, paramInt2);
//				else
//					passf4(i, j, arrayOfFloat, 0, paramArrayOfFloat, paramInt1,
//							i3, paramInt2);
//				l = 1 - l;
//				break;
//			case 2:
//				if (l == 0)
//					passf2(i, j, paramArrayOfFloat, paramInt1, arrayOfFloat, 0,
//							i3, paramInt2);
//				else
//					passf2(i, j, arrayOfFloat, 0, paramArrayOfFloat, paramInt1,
//							i3, paramInt2);
//				l = 1 - l;
//				break;
//			case 3:
//				if (l == 0)
//					passf3(i, j, paramArrayOfFloat, paramInt1, arrayOfFloat, 0,
//							i3, paramInt2);
//				else
//					passf3(i, j, arrayOfFloat, 0, paramArrayOfFloat, paramInt1,
//							i3, paramInt2);
//				l = 1 - l;
//				break;
//			case 5:
//				if (l == 0)
//					passf5(i, j, paramArrayOfFloat, paramInt1, arrayOfFloat, 0,
//							i3, paramInt2);
//				else
//					passf5(i, j, arrayOfFloat, 0, paramArrayOfFloat, paramInt1,
//							i3, paramInt2);
//				l = 1 - l;
//				break;
//			default:
//				if (l == 0)
//					passfg(arrayOfInt, i, i2, j, i5, paramArrayOfFloat,
//							paramInt1, arrayOfFloat, 0, i3, paramInt2);
//				else
//					passfg(arrayOfInt, i, i2, j, i5, arrayOfFloat, 0,
//							paramArrayOfFloat, paramInt1, i3, paramInt2);
//				if (arrayOfInt[0] != 0)
//					l = 1 - l;
//			}
//			j = k;
//			i3 += (i2 - 1) * i;
//		}
//		if (l == 0)
//			return;
//		System.arraycopy(arrayOfFloat, 0, paramArrayOfFloat, paramInt1, i6);
//	}
//
//	strictfp void passf2(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5,
//			int paramInt6) {
//		int i = paramInt5;
//		int j = paramInt1 * paramInt2;
//		int i3;
//		int i4;
//		float f4;
//		float f5;
//		float f6;
//		int i7;
//		if (paramInt1 <= 2)
//			for (int k = 0; k < paramInt2; ++k) {
//				int i1 = k * paramInt1;
//				i3 = paramInt3 + 2 * i1;
//				i4 = i3 + paramInt1;
//				float f3 = paramArrayOfFloat1[i3];
//				f4 = paramArrayOfFloat1[(i3 + 1)];
//				f5 = paramArrayOfFloat1[i4];
//				f6 = paramArrayOfFloat1[(i4 + 1)];
//				int i6 = paramInt4 + i1;
//				i7 = i6 + j;
//				paramArrayOfFloat2[i6] = (f3 + f5);
//				paramArrayOfFloat2[(i6 + 1)] = (f4 + f6);
//				paramArrayOfFloat2[i7] = (f3 - f5);
//				paramArrayOfFloat2[(i7 + 1)] = (f4 - f6);
//			}
//		else
//			for (int l = 0; l < paramInt2; ++l)
//				for (int i2 = 0; i2 < paramInt1 - 1; i2 += 2) {
//					i3 = l * paramInt1;
//					i4 = paramInt3 + i2 + 2 * i3;
//					int i5 = i4 + paramInt1;
//					f4 = paramArrayOfFloat1[i4];
//					f5 = paramArrayOfFloat1[(i4 + 1)];
//					f6 = paramArrayOfFloat1[i5];
//					float f7 = paramArrayOfFloat1[(i5 + 1)];
//					i7 = i2 + i;
//					float f8 = this.wtable[i7];
//					float f9 = paramInt6 * this.wtable[(i7 + 1)];
//					float f2 = f4 - f6;
//					float f1 = f5 - f7;
//					int i8 = paramInt4 + i2 + i3;
//					int i9 = i8 + j;
//					paramArrayOfFloat2[i8] = (f4 + f6);
//					paramArrayOfFloat2[(i8 + 1)] = (f5 + f7);
//					paramArrayOfFloat2[i9] = (f8 * f2 - (f9 * f1));
//					paramArrayOfFloat2[(i9 + 1)] = (f8 * f1 + f9 * f2);
//				}
//	}
//
//	strictfp void passf3(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5,
//			int paramInt6) {
//		int i = paramInt5;
//		int j = i + paramInt1;
//		int k = paramInt2 * paramInt1;
//		int i2;
//		int i3;
//		int i4;
//		float f14;
//		float f15;
//		float f16;
//		float f10;
//		float f5;
//		float f9;
//		float f1;
//		float f6;
//		float f2;
//		if (paramInt1 == 2)
//			for (int l = 1; l <= paramInt2; ++l) {
//				i2 = paramInt3 + (3 * l - 2) * paramInt1;
//				i3 = i2 + paramInt1;
//				i4 = i2 - paramInt1;
//				float f11 = paramArrayOfFloat1[i2];
//				float f12 = paramArrayOfFloat1[(i2 + 1)];
//				float f13 = paramArrayOfFloat1[i3];
//				f14 = paramArrayOfFloat1[(i3 + 1)];
//				f15 = paramArrayOfFloat1[i4];
//				f16 = paramArrayOfFloat1[(i4 + 1)];
//				f10 = f11 + f13;
//				f5 = f15 + -0.5F * f10;
//				f9 = f12 + f14;
//				f1 = f16 + -0.5F * f9;
//				f6 = paramInt6 * 0.8660254F * (f11 - f13);
//				f2 = paramInt6 * 0.8660254F * (f12 - f14);
//				int i8 = paramInt4 + (l - 1) * paramInt1;
//				int i9 = i8 + k;
//				int i10 = i9 + k;
//				paramArrayOfFloat2[i8] = (paramArrayOfFloat1[i4] + f10);
//				paramArrayOfFloat2[(i8 + 1)] = (f16 + f9);
//				paramArrayOfFloat2[i9] = (f5 - f2);
//				paramArrayOfFloat2[(i9 + 1)] = (f1 + f6);
//				paramArrayOfFloat2[i10] = (f5 + f2);
//				paramArrayOfFloat2[(i10 + 1)] = (f1 - f6);
//			}
//		else
//			for (int i1 = 1; i1 <= paramInt2; ++i1) {
//				i2 = paramInt3 + (3 * i1 - 2) * paramInt1;
//				i3 = paramInt4 + (i1 - 1) * paramInt1;
//				for (i4 = 0; i4 < paramInt1 - 1; i4 += 2) {
//					int i5 = i4 + i2;
//					int i6 = i5 + paramInt1;
//					int i7 = i5 - paramInt1;
//					f14 = paramArrayOfFloat1[i5];
//					f15 = paramArrayOfFloat1[(i5 + 1)];
//					f16 = paramArrayOfFloat1[i6];
//					float f17 = paramArrayOfFloat1[(i6 + 1)];
//					float f18 = paramArrayOfFloat1[i7];
//					float f19 = paramArrayOfFloat1[(i7 + 1)];
//					f10 = f14 + f16;
//					f5 = f18 + -0.5F * f10;
//					f9 = f15 + f17;
//					f1 = f19 + -0.5F * f9;
//					f6 = paramInt6 * 0.8660254F * (f14 - f16);
//					f2 = paramInt6 * 0.8660254F * (f15 - f17);
//					float f7 = f5 - f2;
//					float f8 = f5 + f2;
//					float f3 = f1 + f6;
//					float f4 = f1 - f6;
//					int i11 = i4 + i;
//					int i12 = i4 + j;
//					float f20 = this.wtable[i11];
//					float f21 = paramInt6 * this.wtable[(i11 + 1)];
//					float f22 = this.wtable[i12];
//					float f23 = paramInt6 * this.wtable[(i12 + 1)];
//					int i13 = i4 + i3;
//					int i14 = i13 + k;
//					int i15 = i14 + k;
//					paramArrayOfFloat2[i13] = (f18 + f10);
//					paramArrayOfFloat2[(i13 + 1)] = (f19 + f9);
//					paramArrayOfFloat2[i14] = (f20 * f7 - (f21 * f3));
//					paramArrayOfFloat2[(i14 + 1)] = (f20 * f3 + f21 * f7);
//					paramArrayOfFloat2[i15] = (f22 * f8 - (f23 * f4));
//					paramArrayOfFloat2[(i15 + 1)] = (f22 * f4 + f23 * f8);
//				}
//			}
//	}
//
//	strictfp void passf4(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5,
//			int paramInt6) {
//		int i = paramInt5;
//		int j = i + paramInt1;
//		int k = j + paramInt1;
//		int l = paramInt2 * paramInt1;
//		int i3;
//		int i4;
//		int i7;
//		int i8;
//		float f17;
//		float f18;
//		float f19;
//		float f20;
//		float f21;
//		float f22;
//		float f7;
//		float f8;
//		float f14;
//		float f9;
//		float f11;
//		float f12;
//		float f10;
//		float f13;
//		int i13;
//		int i14;
//		if (paramInt1 == 2)
//			for (int i1 = 0; i1 < paramInt2; ++i1) {
//				i3 = i1 * paramInt1;
//				i4 = paramInt3 + 4 * i3 + 1;
//				int i5 = i4 + paramInt1;
//				i7 = i5 + paramInt1;
//				i8 = i7 + paramInt1;
//				float f15 = paramArrayOfFloat1[(i4 - 1)];
//				float f16 = paramArrayOfFloat1[i4];
//				f17 = paramArrayOfFloat1[(i5 - 1)];
//				f18 = paramArrayOfFloat1[i5];
//				f19 = paramArrayOfFloat1[(i7 - 1)];
//				f20 = paramArrayOfFloat1[i7];
//				f21 = paramArrayOfFloat1[(i8 - 1)];
//				f22 = paramArrayOfFloat1[i8];
//				f7 = f16 - f20;
//				f8 = f16 + f20;
//				f14 = f22 - f18;
//				f9 = f18 + f22;
//				f11 = f15 - f19;
//				f12 = f15 + f19;
//				f10 = f17 - f21;
//				f13 = f17 + f21;
//				int i11 = paramInt4 + i3;
//				int i12 = i11 + l;
//				i13 = i12 + l;
//				i14 = i13 + l;
//				paramArrayOfFloat2[i11] = (f12 + f13);
//				paramArrayOfFloat2[(i11 + 1)] = (f8 + f9);
//				paramArrayOfFloat2[i12] = (f11 + paramInt6 * f14);
//				paramArrayOfFloat2[(i12 + 1)] = (f7 + paramInt6 * f10);
//				paramArrayOfFloat2[i13] = (f12 - f13);
//				paramArrayOfFloat2[(i13 + 1)] = (f8 - f9);
//				paramArrayOfFloat2[i14] = (f11 - (paramInt6 * f14));
//				paramArrayOfFloat2[(i14 + 1)] = (f7 - (paramInt6 * f10));
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
//					f17 = paramArrayOfFloat1[(i7 - 1)];
//					f18 = paramArrayOfFloat1[i7];
//					f19 = paramArrayOfFloat1[(i8 - 1)];
//					f20 = paramArrayOfFloat1[i8];
//					f21 = paramArrayOfFloat1[(i9 - 1)];
//					f22 = paramArrayOfFloat1[i9];
//					float f23 = paramArrayOfFloat1[(i10 - 1)];
//					float f24 = paramArrayOfFloat1[i10];
//					f7 = f18 - f22;
//					f8 = f18 + f22;
//					f9 = f20 + f24;
//					f14 = f24 - f20;
//					f11 = f17 - f21;
//					f12 = f17 + f21;
//					f10 = f19 - f23;
//					f13 = f19 + f23;
//					float f5 = f12 - f13;
//					float f2 = f8 - f9;
//					float f4 = f11 + paramInt6 * f14;
//					float f6 = f11 - (paramInt6 * f14);
//					float f1 = f7 + paramInt6 * f10;
//					float f3 = f7 - (paramInt6 * f10);
//					i13 = i6 + i;
//					i14 = i6 + j;
//					int i15 = i6 + k;
//					float f25 = this.wtable[i13];
//					float f26 = paramInt6 * this.wtable[(i13 + 1)];
//					float f27 = this.wtable[i14];
//					float f28 = paramInt6 * this.wtable[(i14 + 1)];
//					float f29 = this.wtable[i15];
//					float f30 = paramInt6 * this.wtable[(i15 + 1)];
//					int i16 = paramInt4 + i6 + i3;
//					int i17 = i16 + l;
//					int i18 = i17 + l;
//					int i19 = i18 + l;
//					paramArrayOfFloat2[i16] = (f12 + f13);
//					paramArrayOfFloat2[(i16 + 1)] = (f8 + f9);
//					paramArrayOfFloat2[i17] = (f25 * f4 - (f26 * f1));
//					paramArrayOfFloat2[(i17 + 1)] = (f25 * f1 + f26 * f4);
//					paramArrayOfFloat2[i18] = (f27 * f5 - (f28 * f2));
//					paramArrayOfFloat2[(i18 + 1)] = (f27 * f2 + f28 * f5);
//					paramArrayOfFloat2[i19] = (f29 * f6 - (f30 * f3));
//					paramArrayOfFloat2[(i19 + 1)] = (f29 * f3 + f30 * f6);
//				}
//			}
//	}
//
//	strictfp void passf5(int paramInt1, int paramInt2,
//			float[] paramArrayOfFloat1, int paramInt3,
//			float[] paramArrayOfFloat2, int paramInt4, int paramInt5,
//			int paramInt6) {
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
//		float f28;
//		float f29;
//		float f30;
//		float f31;
//		float f32;
//		float f33;
//		float f34;
//		float f16;
//		float f13;
//		float f15;
//		float f14;
//		float f24;
//		float f21;
//		float f23;
//		float f22;
//		float f9;
//		float f1;
//		float f10;
//		float f2;
//		float f11;
//		float f4;
//		float f12;
//		float f3;
//		int i15;
//		int i16;
//		if (paramInt1 == 2)
//			for (int i2 = 1; i2 <= paramInt2; ++i2) {
//				i4 = paramInt3 + (5 * i2 - 4) * paramInt1 + 1;
//				i5 = i4 + paramInt1;
//				i6 = i4 - paramInt1;
//				i7 = i5 + paramInt1;
//				i8 = i7 + paramInt1;
//				float f25 = paramArrayOfFloat1[(i4 - 1)];
//				float f26 = paramArrayOfFloat1[i4];
//				float f27 = paramArrayOfFloat1[(i5 - 1)];
//				f28 = paramArrayOfFloat1[i5];
//				f29 = paramArrayOfFloat1[(i6 - 1)];
//				f30 = paramArrayOfFloat1[i6];
//				f31 = paramArrayOfFloat1[(i7 - 1)];
//				f32 = paramArrayOfFloat1[i7];
//				f33 = paramArrayOfFloat1[(i8 - 1)];
//				f34 = paramArrayOfFloat1[i8];
//				f16 = f26 - f34;
//				f13 = f26 + f34;
//				f15 = f28 - f32;
//				f14 = f28 + f32;
//				f24 = f25 - f33;
//				f21 = f25 + f33;
//				f23 = f27 - f31;
//				f22 = f27 + f31;
//				f9 = f29 + 0.309017F * f21 + -0.809017F * f22;
//				f1 = f30 + 0.309017F * f13 + -0.809017F * f14;
//				f10 = f29 + -0.809017F * f21 + 0.309017F * f22;
//				f2 = f30 + -0.809017F * f13 + 0.309017F * f14;
//				f11 = paramInt6 * (0.9510565F * f24 + 0.5877852F * f23);
//				f4 = paramInt6 * (0.9510565F * f16 + 0.5877852F * f15);
//				f12 = paramInt6 * (0.5877852F * f24 - (0.9510565F * f23));
//				f3 = paramInt6 * (0.5877852F * f16 - (0.9510565F * f15));
//				int i12 = paramInt4 + (i2 - 1) * paramInt1;
//				int i13 = i12 + i1;
//				int i14 = i13 + i1;
//				i15 = i14 + i1;
//				i16 = i15 + i1;
//				paramArrayOfFloat2[i12] = (f29 + f21 + f22);
//				paramArrayOfFloat2[(i12 + 1)] = (f30 + f13 + f14);
//				paramArrayOfFloat2[i13] = (f9 - f4);
//				paramArrayOfFloat2[(i13 + 1)] = (f1 + f11);
//				paramArrayOfFloat2[i14] = (f10 - f3);
//				paramArrayOfFloat2[(i14 + 1)] = (f2 + f12);
//				paramArrayOfFloat2[i15] = (f10 + f3);
//				paramArrayOfFloat2[(i15 + 1)] = (f2 - f12);
//				paramArrayOfFloat2[i16] = (f9 + f4);
//				paramArrayOfFloat2[(i16 + 1)] = (f1 - f11);
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
//					f28 = paramArrayOfFloat1[(i7 - 1)];
//					f29 = paramArrayOfFloat1[i7];
//					f30 = paramArrayOfFloat1[(i8 - 1)];
//					f31 = paramArrayOfFloat1[i8];
//					f32 = paramArrayOfFloat1[(i9 - 1)];
//					f33 = paramArrayOfFloat1[i9];
//					f34 = paramArrayOfFloat1[(i10 - 1)];
//					float f35 = paramArrayOfFloat1[i10];
//					float f36 = paramArrayOfFloat1[(i11 - 1)];
//					float f37 = paramArrayOfFloat1[i11];
//					f16 = f29 - f37;
//					f13 = f29 + f37;
//					f15 = f31 - f35;
//					f14 = f31 + f35;
//					f24 = f28 - f36;
//					f21 = f28 + f36;
//					f23 = f30 - f34;
//					f22 = f30 + f34;
//					f9 = f32 + 0.309017F * f21 + -0.809017F * f22;
//					f1 = f33 + 0.309017F * f13 + -0.809017F * f14;
//					f10 = f32 + -0.809017F * f21 + 0.309017F * f22;
//					f2 = f33 + -0.809017F * f13 + 0.309017F * f14;
//					f11 = paramInt6 * (0.9510565F * f24 + 0.5877852F * f23);
//					f4 = paramInt6 * (0.9510565F * f16 + 0.5877852F * f15);
//					f12 = paramInt6 * (0.5877852F * f24 - (0.9510565F * f23));
//					f3 = paramInt6 * (0.5877852F * f16 - (0.9510565F * f15));
//					float f17 = f10 - f3;
//					float f18 = f10 + f3;
//					float f5 = f2 + f12;
//					float f6 = f2 - f12;
//					float f19 = f9 + f4;
//					float f20 = f9 - f4;
//					float f7 = f1 - f11;
//					float f8 = f1 + f11;
//					i15 = i6 + i;
//					i16 = i6 + j;
//					int i17 = i6 + k;
//					int i18 = i6 + l;
//					float f38 = this.wtable[i15];
//					float f39 = paramInt6 * this.wtable[(i15 + 1)];
//					float f40 = this.wtable[i16];
//					float f41 = paramInt6 * this.wtable[(i16 + 1)];
//					float f42 = this.wtable[i17];
//					float f43 = paramInt6 * this.wtable[(i17 + 1)];
//					float f44 = this.wtable[i18];
//					float f45 = paramInt6 * this.wtable[(i18 + 1)];
//					int i19 = i6 + i5;
//					int i20 = i19 + i1;
//					int i21 = i20 + i1;
//					int i22 = i21 + i1;
//					int i23 = i22 + i1;
//					paramArrayOfFloat2[i19] = (f32 + f21 + f22);
//					paramArrayOfFloat2[(i19 + 1)] = (f33 + f13 + f14);
//					paramArrayOfFloat2[i20] = (f38 * f20 - (f39 * f8));
//					paramArrayOfFloat2[(i20 + 1)] = (f38 * f8 + f39 * f20);
//					paramArrayOfFloat2[i21] = (f40 * f17 - (f41 * f5));
//					paramArrayOfFloat2[(i21 + 1)] = (f40 * f5 + f41 * f17);
//					paramArrayOfFloat2[i22] = (f42 * f18 - (f43 * f6));
//					paramArrayOfFloat2[(i22 + 1)] = (f42 * f6 + f43 * f18);
//					paramArrayOfFloat2[i23] = (f44 * f19 - (f45 * f7));
//					paramArrayOfFloat2[(i23 + 1)] = (f44 * f7 + f45 * f19);
//				}
//			}
//	}
//
//	strictfp void passfg(int[] paramArrayOfInt, int paramInt1, int paramInt2,
//			int paramInt3, int paramInt4, float[] paramArrayOfFloat1,
//			int paramInt5, float[] paramArrayOfFloat2, int paramInt6,
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
//		float f9;
//		int i22;
//		int i40;
//		float f11;
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
//						float f6 = paramArrayOfFloat1[(paramInt5 + i35 + i13 + i34)];
//						f9 = paramArrayOfFloat1[(paramInt5 + i35 + i17 + i34)];
//						paramArrayOfFloat2[(i38 + i31)] = (f6 + f9);
//						paramArrayOfFloat2[(i38 + i32)] = (f6 - f9);
//					}
//				}
//			}
//			for (int i10 = 0; i10 < paramInt3; ++i10) {
//				i13 = i10 * paramInt1;
//				i17 = i13 * paramInt2;
//				for (i22 = 0; i22 < paramInt1; ++i22)
//					paramArrayOfFloat2[(paramInt6 + i22 + i13)] = paramArrayOfFloat1[(paramInt5
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
//						f9 = paramArrayOfFloat1[(i40 + i22 + i35)];
//						f11 = paramArrayOfFloat1[(i40 + i24 + i35)];
//						paramArrayOfFloat2[(i38 + i34 + i13)] = (f9 + f11);
//						paramArrayOfFloat2[(i38 + i34 + i17)] = (f9 - f11);
//					}
//			}
//			for (i12 = 0; i12 < paramInt1; ++i12)
//				for (i14 = 0; i14 < paramInt3; ++i14) {
//					i17 = i14 * paramInt1;
//					paramArrayOfFloat2[(paramInt6 + i12 + i17)] = paramArrayOfFloat1[(paramInt5
//							+ i12 + i17 * paramInt2)];
//				}
//		}
//		int i5 = 2 - paramInt1;
//		int i6 = 0;
//		int i12 = (paramInt2 - 1) * paramInt4;
//		float f1;
//		float f2;
//		int i26;
//		int i36;
//		for (int i1 = 1; i1 < l; ++i1) {
//			int i3 = paramInt2 - i1;
//			i5 += paramInt1;
//			i14 = i1 * paramInt4;
//			i17 = i3 * paramInt4;
//			i22 = i5 + i8;
//			f1 = this.wtable[(i22 - 2)];
//			f2 = paramInt8 * this.wtable[(i22 - 1)];
//			for (int i25 = 0; i25 < paramInt4; ++i25) {
//				i31 = paramInt5 + i25;
//				i32 = paramInt6 + i25;
//				paramArrayOfFloat1[(i31 + i14)] = (paramArrayOfFloat2[i32] + f1
//						* paramArrayOfFloat2[(i32 + paramInt4)]);
//				paramArrayOfFloat1[(i31 + i17)] = (f2 * paramArrayOfFloat2[(i32 + i12)]);
//			}
//			int j = i5;
//			i6 += paramInt1;
//			for (i26 = 2; i26 < l; ++i26) {
//				i2 = paramInt2 - i26;
//				j += i6;
//				if (j > i7)
//					j -= i7;
//				i31 = j + i8;
//				float f4 = this.wtable[(i31 - 2)];
//				float f3 = paramInt8 * this.wtable[(i31 - 1)];
//				i32 = i26 * paramInt4;
//				i34 = i2 * paramInt4;
//				for (i36 = 0; i36 < paramInt4; ++i36) {
//					i38 = paramInt5 + i36;
//					i40 = paramInt6 + i36;
//					paramArrayOfFloat1[(i38 + i14)] += f4
//							* paramArrayOfFloat2[(i40 + i32)];
//					paramArrayOfFloat1[(i38 + i17)] += f3
//							* paramArrayOfFloat2[(i40 + i34)];
//				}
//			}
//		}
//		int i23;
//		for (int i15 = 1; i15 < l; ++i15) {
//			i17 = i15 * paramInt4;
//			for (i23 = 0; i23 < paramInt4; ++i23) {
//				i26 = paramInt6 + i23;
//				paramArrayOfFloat2[i26] += paramArrayOfFloat2[(i26 + i17)];
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
//				float f5 = paramArrayOfFloat1[(i34 - 1)];
//				float f7 = paramArrayOfFloat1[i34];
//				f9 = paramArrayOfFloat1[(i36 - 1)];
//				f11 = paramArrayOfFloat1[i36];
//				int i43 = i31 + i17;
//				int i44 = i31 + i23;
//				paramArrayOfFloat2[(i43 - 1)] = (f5 - f11);
//				paramArrayOfFloat2[(i44 - 1)] = (f5 + f11);
//				paramArrayOfFloat2[i43] = (f7 + f9);
//				paramArrayOfFloat2[i44] = (f7 - f9);
//			}
//		}
//		paramArrayOfInt[0] = 1;
//		if (paramInt1 == 2)
//			return;
//		paramArrayOfInt[0] = 0;
//		System.arraycopy(paramArrayOfFloat2, paramInt6, paramArrayOfFloat1,
//				paramInt5, paramInt4);
//		i16 = paramInt3 * paramInt1;
//		for (int i18 = 1; i18 < paramInt2; ++i18) {
//			i23 = i18 * i16;
//			for (int i28 = 0; i28 < paramInt3; ++i28) {
//				i31 = i28 * paramInt1;
//				i32 = paramInt6 + i31 + i23;
//				i34 = paramInt5 + i31 + i23;
//				paramArrayOfFloat1[i34] = paramArrayOfFloat2[i32];
//				paramArrayOfFloat1[(i34 + 1)] = paramArrayOfFloat2[(i32 + 1)];
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
//					f1 = this.wtable[(i31 - 1)];
//					f2 = paramInt8 * this.wtable[i31];
//					i32 = paramInt5 + i29;
//					i34 = paramInt6 + i29;
//					for (i37 = 0; i37 < paramInt3; ++i37) {
//						i39 = i37 * paramInt1 + i23;
//						int i41 = i32 + i39;
//						int i42 = i34 + i39;
//						f11 = paramArrayOfFloat2[(i42 - 1)];
//						float f12 = paramArrayOfFloat2[i42];
//						paramArrayOfFloat1[(i41 - 1)] = (f1 * f11 - (f2 * f12));
//						paramArrayOfFloat1[i41] = (f1 * f12 + f2 * f11);
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
//						f1 = this.wtable[(i34 - 1)];
//						f2 = paramInt8 * this.wtable[i34];
//						i37 = paramInt5 + i33 + i31;
//						i39 = paramInt6 + i33 + i31;
//						float f8 = paramArrayOfFloat2[(i39 - 1)];
//						float f10 = paramArrayOfFloat2[i39];
//						paramArrayOfFloat1[(i37 - 1)] = (f1 * f8 - (f2 * f10));
//						paramArrayOfFloat1[i37] = (f1 * f10 + f2 * f8);
//					}
//				}
//			}
//		}
//	}
//
//	private strictfp void cftfsub(int paramInt1, float[] paramArrayOfFloat1,
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
//			cftxb020(paramArrayOfFloat1, paramInt2);
//		}
//	}
//
//	private strictfp void cftbsub(int paramInt1, float[] paramArrayOfFloat1,
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
//			cftxb020(paramArrayOfFloat1, paramInt2);
//		}
//	}
//
//	private strictfp void bitrv2(int paramInt1, int[] paramArrayOfInt,
//			float[] paramArrayOfFloat, int paramInt2) {
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
//		float f1;
//		float f2;
//		float f3;
//		float f4;
//		if (k == 8)
//			for (i6 = 0; i6 < l; ++i6) {
//				i3 = 4 * i6;
//				for (i7 = 0; i7 < i6; ++i7) {
//					i = 4 * i7 + 2 * paramArrayOfInt[(l + i6)];
//					j = i3 + 2 * paramArrayOfInt[(l + i7)];
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i1;
//					j += 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += 2;
//					j += i1;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i1;
//					j -= 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//				}
//				j = i3 + 2 * paramArrayOfInt[(l + i6)];
//				i = j + 2;
//				j += i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				i += i2;
//				j += 2 * i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				i += i2;
//				j -= i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				i -= 2;
//				j -= i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				i += i1 + 2;
//				j += i1 + 2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				i -= i1 - i2;
//				j += 2 * i2 - 2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//			}
//		else
//			for (i6 = 0; i6 < l; ++i6) {
//				i3 = 4 * i6;
//				for (i7 = 0; i7 < i6; ++i7) {
//					i = 4 * i7 + paramArrayOfInt[(l + i6)];
//					j = i3 + paramArrayOfInt[(l + i7)];
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i1;
//					j += 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += 2;
//					j += i1;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i1;
//					j -= 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//				}
//				j = i3 + paramArrayOfInt[(l + i6)];
//				i = j + 2;
//				j += i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				i += i2;
//				j += i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//			}
//	}
//
//	private strictfp void bitrv2conj(int paramInt1, int[] paramArrayOfInt,
//			float[] paramArrayOfFloat, int paramInt2) {
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
//		float f1;
//		float f2;
//		float f3;
//		float f4;
//		if (k == 8)
//			for (i6 = 0; i6 < l; ++i6) {
//				i3 = 4 * i6;
//				for (i7 = 0; i7 < i6; ++i7) {
//					i = 4 * i7 + 2 * paramArrayOfInt[(l + i6)];
//					j = i3 + 2 * paramArrayOfInt[(l + i7)];
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i1;
//					j += 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += 2;
//					j += i1;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i1;
//					j -= 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= 2 * i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//				}
//				j = i3 + 2 * paramArrayOfInt[(l + i6)];
//				i = j + 2;
//				j += i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				paramArrayOfFloat[(i4 - 1)] = (-paramArrayOfFloat[(i4 - 1)]);
//				f1 = paramArrayOfFloat[i4];
//				f2 = -paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = -paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				paramArrayOfFloat[(i5 + 3)] = (-paramArrayOfFloat[(i5 + 3)]);
//				i += i2;
//				j += 2 * i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = -paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = -paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				i += i2;
//				j -= i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = -paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = -paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				i -= 2;
//				j -= i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = -paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = -paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				i += i1 + 2;
//				j += i1 + 2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				f1 = paramArrayOfFloat[i4];
//				f2 = -paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = -paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				i -= i1 - i2;
//				j += 2 * i2 - 2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				paramArrayOfFloat[(i4 - 1)] = (-paramArrayOfFloat[(i4 - 1)]);
//				f1 = paramArrayOfFloat[i4];
//				f2 = -paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = -paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				paramArrayOfFloat[(i5 + 3)] = (-paramArrayOfFloat[(i5 + 3)]);
//			}
//		else
//			for (i6 = 0; i6 < l; ++i6) {
//				i3 = 4 * i6;
//				for (i7 = 0; i7 < i6; ++i7) {
//					i = 4 * i7 + paramArrayOfInt[(l + i6)];
//					j = i3 + paramArrayOfInt[(l + i7)];
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i1;
//					j += 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += 2;
//					j += i1;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i += i2;
//					j += i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i1;
//					j -= 2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//					i -= i2;
//					j -= i2;
//					i4 = paramInt2 + i;
//					i5 = paramInt2 + j;
//					f1 = paramArrayOfFloat[i4];
//					f2 = -paramArrayOfFloat[(i4 + 1)];
//					f3 = paramArrayOfFloat[i5];
//					f4 = -paramArrayOfFloat[(i5 + 1)];
//					paramArrayOfFloat[i4] = f3;
//					paramArrayOfFloat[(i4 + 1)] = f4;
//					paramArrayOfFloat[i5] = f1;
//					paramArrayOfFloat[(i5 + 1)] = f2;
//				}
//				j = i3 + paramArrayOfInt[(l + i6)];
//				i = j + 2;
//				j += i1;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				paramArrayOfFloat[(i4 - 1)] = (-paramArrayOfFloat[(i4 - 1)]);
//				f1 = paramArrayOfFloat[i4];
//				f2 = -paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = -paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				paramArrayOfFloat[(i5 + 3)] = (-paramArrayOfFloat[(i5 + 3)]);
//				i += i2;
//				j += i2;
//				i4 = paramInt2 + i;
//				i5 = paramInt2 + j;
//				paramArrayOfFloat[(i4 - 1)] = (-paramArrayOfFloat[(i4 - 1)]);
//				f1 = paramArrayOfFloat[i4];
//				f2 = -paramArrayOfFloat[(i4 + 1)];
//				f3 = paramArrayOfFloat[i5];
//				f4 = -paramArrayOfFloat[(i5 + 1)];
//				paramArrayOfFloat[i4] = f3;
//				paramArrayOfFloat[(i4 + 1)] = f4;
//				paramArrayOfFloat[i5] = f1;
//				paramArrayOfFloat[(i5 + 1)] = f2;
//				paramArrayOfFloat[(i5 + 3)] = (-paramArrayOfFloat[(i5 + 3)]);
//			}
//	}
//
//	private strictfp void bitrv216(float[] paramArrayOfFloat, int paramInt) {
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
//	private strictfp void bitrv216neg(float[] paramArrayOfFloat, int paramInt) {
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
//	private strictfp void bitrv208(float[] paramArrayOfFloat, int paramInt) {
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
//	private strictfp void bitrv208neg(float[] paramArrayOfFloat, int paramInt) {
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
//	private strictfp void cftf1st(int paramInt1, float[] paramArrayOfFloat1,
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
//	private strictfp void cftb1st(int paramInt1, float[] paramArrayOfFloat1,
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
//	private strictfp void cftrec4_th(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, int paramInt3, float[] paramArrayOfFloat2) {
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
//						i3, i2, paramInt1, paramArrayOfFloat1,
//						paramArrayOfFloat2, paramInt3) {
//					public strictfp void run() {
//						int i1 = this.val$firstIdx + this.val$mf;
//						int l = this.val$n;
//						while (l > 512) {
//							l >>= 2;
//							FloatFFT_1D.this.cftmdl1(l, this.val$a, i1 - l,
//									this.val$w, this.val$nw - (l >> 1));
//						}
//						FloatFFT_1D.this.cftleaf(l, 1, this.val$a, i1 - l,
//								this.val$nw, this.val$w);
//						int k = 0;
//						int i2 = this.val$firstIdx - l;
//						int j = this.val$mf - l;
//						while (j > 0) {
//							int i = FloatFFT_1D.this.cfttree(l, j, ++k,
//									this.val$a, this.val$firstIdx, this.val$nw,
//									this.val$w);
//							FloatFFT_1D.this.cftleaf(l, i, this.val$a, i2 + j,
//									this.val$nw, this.val$w);
//							j -= l;
//						}
//					}
//				});
//			else
//				arrayOfFuture[(i1++)] = ConcurrencyUtils.submit(new Runnable(
//						i3, i2, paramInt1, paramArrayOfFloat1,
//						paramArrayOfFloat2, paramInt3) {
//					public strictfp void run() {
//						int i1 = this.val$firstIdx + this.val$mf;
//						int k = 1;
//						int l = this.val$n;
//						while (l > 512) {
//							l >>= 2;
//							k <<= 2;
//							FloatFFT_1D.this.cftmdl2(l, this.val$a, i1 - l,
//									this.val$w, this.val$nw - l);
//						}
//						FloatFFT_1D.this.cftleaf(l, 0, this.val$a, i1 - l,
//								this.val$nw, this.val$w);
//						k >>= 1;
//						int i2 = this.val$firstIdx - l;
//						int j = this.val$mf - l;
//						while (j > 0) {
//							int i = FloatFFT_1D.this.cfttree(l, j, ++k,
//									this.val$a, this.val$firstIdx, this.val$nw,
//									this.val$w);
//							FloatFFT_1D.this.cftleaf(l, i, this.val$a, i2 + j,
//									this.val$nw, this.val$w);
//							j -= l;
//						}
//					}
//				});
//		}
//		ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//	}
//
//	private strictfp void cftrec4(int paramInt1, float[] paramArrayOfFloat1,
//			int paramInt2, int paramInt3, float[] paramArrayOfFloat2) {
//		int l = paramInt1;
//		int i1 = paramInt2 + paramInt1;
//		while (l > 512) {
//			l >>= 2;
//			cftmdl1(l, paramArrayOfFloat1, i1 - l, paramArrayOfFloat2,
//					paramInt3 - (l >> 1));
//		}
//		cftleaf(l, 1, paramArrayOfFloat1, i1 - l, paramInt3, paramArrayOfFloat2);
//		int k = 0;
//		int i2 = paramInt2 - l;
//		int j = paramInt1 - l;
//		while (j > 0) {
//			int i = cfttree(l, j, ++k, paramArrayOfFloat1, paramInt2,
//					paramInt3, paramArrayOfFloat2);
//			cftleaf(l, i, paramArrayOfFloat1, i2 + j, paramInt3,
//					paramArrayOfFloat2);
//			j -= l;
//		}
//	}
//
//	private strictfp int cfttree(int paramInt1, int paramInt2, int paramInt3,
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
//	private strictfp void cftleaf(int paramInt1, int paramInt2,
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
//	private strictfp void cftmdl1(int paramInt1, float[] paramArrayOfFloat1,
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
//	private strictfp void cftmdl2(int paramInt1, float[] paramArrayOfFloat1,
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
//	private strictfp void cftfx41(int paramInt1, float[] paramArrayOfFloat1,
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
//	private strictfp void cftf161(float[] paramArrayOfFloat1, int paramInt1,
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
//	private strictfp void cftf162(float[] paramArrayOfFloat1, int paramInt1,
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
//	private strictfp void cftf081(float[] paramArrayOfFloat1, int paramInt1,
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
//	private strictfp void cftf082(float[] paramArrayOfFloat1, int paramInt1,
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
//	private strictfp void cftf040(float[] paramArrayOfFloat, int paramInt) {
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
//	private strictfp void cftb040(float[] paramArrayOfFloat, int paramInt) {
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
//	private strictfp void cftx020(float[] paramArrayOfFloat, int paramInt) {
//		float f1 = paramArrayOfFloat[paramInt]
//				- paramArrayOfFloat[(paramInt + 2)];
//		float f2 = -paramArrayOfFloat[(paramInt + 1)]
//				+ paramArrayOfFloat[(paramInt + 3)];
//		paramArrayOfFloat[paramInt] += paramArrayOfFloat[(paramInt + 2)];
//		paramArrayOfFloat[(paramInt + 1)] += paramArrayOfFloat[(paramInt + 3)];
//		paramArrayOfFloat[(paramInt + 2)] = f1;
//		paramArrayOfFloat[(paramInt + 3)] = f2;
//	}
//
//	private strictfp void cftxb020(float[] paramArrayOfFloat, int paramInt) {
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
//	private strictfp void cftxc020(float[] paramArrayOfFloat, int paramInt) {
//		float f1 = paramArrayOfFloat[paramInt]
//				- paramArrayOfFloat[(paramInt + 2)];
//		float f2 = paramArrayOfFloat[(paramInt + 1)]
//				+ paramArrayOfFloat[(paramInt + 3)];
//		paramArrayOfFloat[paramInt] += paramArrayOfFloat[(paramInt + 2)];
//		paramArrayOfFloat[(paramInt + 1)] -= paramArrayOfFloat[(paramInt + 3)];
//		paramArrayOfFloat[(paramInt + 2)] = f1;
//		paramArrayOfFloat[(paramInt + 3)] = f2;
//	}
//
//	private strictfp void rftfsub(int paramInt1, float[] paramArrayOfFloat1,
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
//			paramArrayOfFloat1[(i1 + 1)] = (f6 - paramArrayOfFloat1[(i1 + 1)]);
//			paramArrayOfFloat1[i2] += f5;
//			paramArrayOfFloat1[(i2 + 1)] = (f6 - paramArrayOfFloat1[(i2 + 1)]);
//		}
//		paramArrayOfFloat1[(paramInt2 + l + 1)] = (-paramArrayOfFloat1[(paramInt2
//				+ l + 1)]);
//	}
//
//	private strictfp void rftbsub(int paramInt1, float[] paramArrayOfFloat1,
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
//	private strictfp void scale(float paramFloat, float[] paramArrayOfFloat,
//			int paramInt, boolean paramBoolean) {
//		float f = (float) (1.0D / paramFloat);
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
//						paramArrayOfFloat, f) {
//					public strictfp void run() {
//						for (int i = this.val$firstIdx; i < this.val$lastIdx; ++i)
//							this.val$a[i] *= this.val$norm;
//					}
//				});
//			}
//			ConcurrencyUtils.waitForCompletion(arrayOfFuture);
//		} else {
//			for (k = paramInt; k < paramInt + i; ++k)
//				paramArrayOfFloat[k] *= f;
//		}
//	}
//
//	private static enum Plans {
//		SPLIT_RADIX, MIXED_RADIX, BLUESTEIN;
//
//		public static strictfp Plans[] values() {
//			return ((Plans[]) $VALUES.clone());
//		}
//
//		public static strictfp Plans valueOf(String paramString) {
//			return ((Plans) Enum.valueOf(Plans.class, paramString));
//		}
//	}
//}