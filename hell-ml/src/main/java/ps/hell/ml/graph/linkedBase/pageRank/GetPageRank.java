package ps.hell.ml.graph.linkedBase.pageRank;

public class GetPageRank {
	private static float alpha = 0.85f;

	public static void main(String[] args) {
		float[][] S = { { 0, 0, 0, 0 }, { 0.3333333f, 0, 0, 1 },
				{ 0.3333333f, 0.5f, 0, 0 }, { 0.3333333f, 0.5f, 1, 0 } };
		float[][] U = { { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 },
				{ 1, 1, 1, 1 } };

		int n = S.length;
		float[][] f1 = multiGeneMatrix(alpha, S);// aS
		 printContentOfMatrix(f1);
		// System.out.println();
		float[][] f2 = multiGeneMatrix((1 - alpha) / S[1].length, U);// (1-a)/n*U
		 printContentOfMatrix(f2);
		// System.out.println();
		float[][] G = addMatrix(f1, f2);// aS+(1-a)/n*U
		printContentOfMatrix(G);

		// float[] pr_cur={100f,80f,5000f,0.9f};//result：194.2838 1933.7593
		// 1071.1786 1981.6804
		// float[] pr_cur={645f,129f,9f,6f}; //result：29.587515 294.49255
		// 163.12997 301.79047
		// float[] pr_cur={869f,986f,97f,97f};//result：76.83754 764.7849
		// 423.64175 783.7373
		float[] pr_cur = { 1f, 1f, 1f, 1f };// result：0.15000004 1.492991
											// 0.82702124 1.5299894

		for (int i = 0; i < 1000; i++) {
			float[] pr_next = multiMatrixVector(G, pr_cur);
			System.out.println(pr_next[0] + " " + pr_next[1] + " " + pr_next[2]
					+ " " + pr_next[3]);
			pr_cur = pr_next;
		}
	}

	// 矩阵与向量相乘
	public static float[] multiMatrixVector(float[][] m, float[] v) {
		float[] rv = new float[v.length];
		for (int vl = 0; vl < v.length; vl++) {

			for (int row = 0; row < m.length; row++) {
				float one = 0;
				for (int col = 0; col < m[1].length; col++) {
					one += m[row][col] * v[col];
				}
				rv[row] = one;
			}
		}
		return rv;
	}

	// 两矩阵相加
	public static float[][] addMatrix(float[][] f1, float[][] f2) {
		float[][] result = new float[f1.length][f1[1].length];

		for (int row = 0; row < f1.length; row++) {
			for (int col = 0; col < f1[1].length; col++) {
				result[row][col] = f1[row][col] + f2[row][col];
			}
		}
		return result;
	}

	// 矩阵乘因子
	public static float[][] multiGeneMatrix(float f, float[][] fm) {
		float[][] result = new float[fm.length][fm[1].length];

		for (int row = 0; row < fm.length; row++) {
			for (int col = 0; col < fm[1].length; col++) {
				result[row][col] = fm[row][col] * f;
			}
		}
		return result;
	}

	// 打印矩阵内容
	public static void printContentOfMatrix(float[][] f) {
		System.out.println("println");
		for (int row = 0; row < f.length; row++) {
			for (int col = 0; col < f[1].length; col++) {
				System.out.print(f[row][col] + " ");
			}
			System.out.println();
		}
		System.out.println("end");
	}
}