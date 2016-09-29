package ps.hell.math.base.persons.fft.zx;

public class print {
	public void print(double[] x, int y) {
		int size = x.length;
		for (int i = 1; i <= size; ++i) {
			System.out.print(x[(i - 1)] + " ");
			if (i % y == 0) {
				System.out.println();
			}
		}
		System.out.println();
	}

	public void print(int[] x, int y) {
		int size = x.length;
		for (int i = 1; i <= size; ++i) {
			System.out.print(x[(i - 1)] + " ");
			if (i % y == 0) {
				System.out.println();
			}
		}
		System.out.println();
	}

	public void print(double[] x) {
		print(x, 1);
	}

	public void print(boolean x) {
		System.out.println(x);
	}

	public void print(boolean[] x, int y) {
		int size = x.length;
		for (int i = 1; i <= size; ++i) {
			System.out.print(x[(i - 1)] + " ");
			if (i % y == 0) {
				System.out.println();
			}
		}
		System.out.println();
	}

	public void print(boolean[] x) {
		print(x, 1);
	}

	public void print(boolean[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		for (int i = 1; i <= size1; ++i) {
			for (int j = 1; j <= size2; ++j) {
				System.out.print(x[(i - 1)][(j - 1)] + " ");
				if (j % size2 == 0) {
					System.out.println();
				}
			}
		}
		System.out.println();
	}

	public void print(int[] x) {
		print(x, 1);
	}

	public void print(double[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		for (int i = 1; i <= size1; ++i) {
			for (int j = 1; j <= size2; ++j) {
				System.out.print(x[(i - 1)][(j - 1)] + " ");
				if (j % size2 == 0) {
					System.out.println();
				}
			}
		}
		System.out.println();
	}

	public void print(int[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		for (int i = 1; i <= size1; ++i) {
			for (int j = 1; j <= size2; ++j) {
				System.out.print(x[(i - 1)][(j - 1)] + " ");
				if (j % size2 == 0) {
					System.out.println();
				}
			}
		}
		System.out.println();
	}

	public void print(double x) {
		System.out.println(x);
	}

	public void print(int x) {
		System.out.println(x);
	}

	public void print(complex x) {
		System.out.print("[" + x.real + "," + x.imag + "]" + "\n");
	}

	public void print(complex[] x, int y) {
		int size = x.length;
		for (int i = 1; i <= size; ++i) {
			System.out.print("[" + x[(i - 1)].real + "," + x[(i - 1)].imag
					+ "]" + " ");
			if (i % y == 0) {
				System.out.println();
			}
		}
		System.out.println();
	}

	public void print(complex[] x) {
		print(x, 1);
	}

	public void print(complex[][] x) {
		int size1 = x.length;
		int size2 = x[0].length;
		for (int i = 1; i <= size1; ++i) {
			for (int j = 1; j <= size2; ++j) {
				System.out.print("[" + x[(i - 1)][(j - 1)].real + ","
						+ x[(i - 1)][(j - 1)].imag + "]" + " ");
				if (j % size2 == 0) {
					System.out.print("\n");
				}
			}
		}
		System.out.println();
	}
}