package ps.hell.ml.forest.model.libsvm;

import java.io.*;
import java.util.*;

class SvmPredict {

	private SvmModel model = null;
	private BufferedReader input = null;
	private DataOutputStream output = null;

	private int predict_probability = 0;

	private static SvmPrintInterface svm_print_null = new SvmPrintInterface() {
		@Override
		public void print(String s) {
		}
	};

	private static SvmPrintInterface svm_print_stdout = new SvmPrintInterface() {
		@Override
		public void print(String s) {
			System.out.print(s);
		}
	};

	private static SvmPrintInterface svm_print_string = svm_print_stdout;

	static void info(String s) {
		svm_print_string.print(s);
	}

	private static double atof(String s) {
		return Double.valueOf(s).doubleValue();
	}

	private static int atoi(String s) {
		return Integer.parseInt(s);
	}

	private static void predict(BufferedReader input, DataOutputStream output,
			SvmModel model, int predict_probability) throws IOException {
		int correct = 0;
		int total = 0;
		double error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;

		int svm_type = Svm.svm_get_svm_type(model);
		int nr_class = Svm.svm_get_nr_class(model);
		double[] prob_estimates = null;

		if (predict_probability == 1) {
			if (svm_type == SvmParameter.EPSILON_SVR
					|| svm_type == SvmParameter.NU_SVR) {
				SvmPredict
						.info("Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="
								+ Svm.svm_get_svr_probability(model) + "\n");
			} else {
				int[] labels = new int[nr_class];
				Svm.svm_get_labels(model, labels);
				prob_estimates = new double[nr_class];
				output.writeBytes("labels");
				for (int j = 0; j < nr_class; j++)
					output.writeBytes(" " + labels[j]);
				output.writeBytes("\n");
			}
		}
		while (true) {
			String line = input.readLine();
			if (line == null)
				break;

			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

			double target = atof(st.nextToken());
			int m = st.countTokens() / 2;
			SvmNode[] x = new SvmNode[m];
			for (int j = 0; j < m; j++) {
				x[j] = new SvmNode();
				x[j].index = atoi(st.nextToken());
				x[j].value = atof(st.nextToken());
			}

			double v;
			if (predict_probability == 1
					&& (svm_type == SvmParameter.C_SVC || svm_type == SvmParameter.NU_SVC)) {
				v = Svm.svm_predict_probability(model, x, prob_estimates);
				output.writeBytes(v + " ");
				for (int j = 0; j < nr_class; j++)
					output.writeBytes(prob_estimates[j] + " ");
				output.writeBytes("\n");
			} else {
				v = Svm.svm_predict(model, x);
				output.writeBytes(v + "\n");
			}

			if (v == target)
				++correct;
			error += (v - target) * (v - target);
			sumv += v;
			sumy += target;
			sumvv += v * v;
			sumyy += target * target;
			sumvy += v * target;
			++total;
		}
		if (svm_type == SvmParameter.EPSILON_SVR
				|| svm_type == SvmParameter.NU_SVR) {
			SvmPredict.info("Mean squared error = " + error / total
					+ " (regression)\n");
			SvmPredict.info("Squared correlation coefficient = "
					+ ((total * sumvy - sumv * sumy) * (total * sumvy - sumv
							* sumy))
					/ ((total * sumvv - sumv * sumv) * (total * sumyy - sumy
							* sumy)) + " (regression)\n");
		} else
			SvmPredict.info("Accuracy = " + (double) correct / total * 100
					+ "% (" + correct + "/" + total + ") (classification)\n");
	}

	public void predict() {
		try {
			predict(input, output, model, predict_probability);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 预测一个值
	 * 
	 * @param inputData
	 * @return
	 */
	public double predict(double[] inputData) {
		return predict(inputData, output, model, predict_probability);
	}
	/**
	 * 预测一个值
	 * 
	 * @param inputData
	 * @return
	 */
	public double[] predict(double[][] inputData) {
		double[] result=new double[inputData.length];
		for(int i=0;i<inputData.length;i++)
		{
			result[i]=predict(inputData[i], output, model, predict_probability);
		}
		return result;
	}
	/**
	 * 预测内容
	 * @param inputData
	 * @return
	 */
	public String predictString(double[] inputData){
		Integer val=(int)predict(inputData, output, model, predict_probability);
		return model.mapString.get(val);
	}

	/**
	 * 预测一个值
	 * 
	 * @param inputData
	 * @return
	 */
	public String[] predictString(double[][] inputData) {
		String[] result=new String[inputData.length];
		for(int i=0;i<inputData.length;i++)
		{
			result[i]=model.mapString.get((int)predict(inputData[i], output, model, predict_probability));
		}
		return result;
	}
	
	
	/**
	 * 预测一个值
	 * 
	 * @param inputData
	 * @return
	 */
	public String[] predictString(String[][] inputData) {
		double[][] val=new double[inputData.length][];
		for(int i=0;i<inputData.length;i++)
		{
			val[i]=new double[inputData[i].length];
			for(int j=0;j<inputData[i].length;j++)
			{
				if(model.xClass==null||model.xClass[j]==null||!model.xClass[j].equals("s"))
				{
					val[i][j]=Double.parseDouble(inputData[i][j]);
				}else{
					val[i][j]=model.indexMapStringRel.get(j+1).get(inputData[i][j]);
				}
			}
		}
		String[] result=new String[inputData.length];
		for(int i=0;i<inputData.length;i++)
		{
			result[i]=model.mapString.get((int)predict(val[i], output, model, predict_probability));
		}
		return result;
	}
	/**
	 * 获取其中一个值
	 * 
	 * @param inputData
	 * @param output
	 * @param model
	 * @param predict_probability
	 * @return
	 * @throws IOException
	 */
	private static double predict(double[] inputData, DataOutputStream output,
			SvmModel model, int predict_probability) {

		SvmNode[] x = new SvmNode[inputData.length];
		for (int j = 0; j < inputData.length; j++) {
			x[j] = new SvmNode();
			x[j].index = j+1;
			x[j].value = inputData[j];
		}
		double v = 0D;
		int svm_type = Svm.svm_get_svm_type(model);
		int nr_class = Svm.svm_get_nr_class(model);
		double[] prob_estimates = null;

		if (predict_probability == 1) {
			if (svm_type == SvmParameter.EPSILON_SVR
					|| svm_type == SvmParameter.NU_SVR) {
				SvmPredict
						.info("Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="
								+ Svm.svm_get_svr_probability(model) + "\n");
			} else {
				int[] labels = new int[nr_class];
				Svm.svm_get_labels(model, labels);
				prob_estimates = new double[nr_class];
//				output.writeBytes("labels");
//				for (int j = 0; j < nr_class; j++)
//					output.writeBytes(" " + labels[j]);
//				output.writeBytes("\n");
			}
		}
		if (predict_probability == 1
				&& (svm_type == SvmParameter.C_SVC || svm_type == SvmParameter.NU_SVC)) {
			v = Svm.svm_predict_probability(model, x, prob_estimates);
			try {
				output.writeBytes(v + " ");
				for (int j = 0; j < nr_class; j++)
					output.writeBytes(prob_estimates[j] + " ");
				output.writeBytes("\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			v = Svm.svm_predict(model, x);
			if (output != null) {
				try {
					output.writeBytes(v + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return v;
	}

	public static void exit_with_help() {
		System.out
				.print("Usage: svm_train [options] training_set_file [model_file]\n"
						+ "options:\n"
						+ "-s svm_type : set type of SVM (default 0)\n"
						+ "	0 -- C-SVC		(multi-class classification)\n"
						+ "	1 -- nu-SVC		(multi-class classification)\n"
						+ "	2 -- one-class SVM\n"
						+ "	3 -- epsilon-SVR	(regression)\n"
						+ "	4 -- nu-SVR		(regression)\n"
						+ "-t kernel_type : set type of kernel function (default 2)\n"
						+ "	0 -- linear: u'*v\n"
						+ "	1 -- polynomial: (gamma*u'*v + coef0)^degree\n"
						+ "	2 -- radial basis function: exp(-gamma*|u-v|^2)\n"
						+ "	3 -- sigmoid: tanh(gamma*u'*v + coef0)\n"
						+ "	4 -- precomputed kernel (kernel values in training_set_file)\n"
						+ "-d degree : set degree in kernel function (default 3)\n"
						+ "-g gamma : set gamma in kernel function (default 1/num_features)\n"
						+ "-r coef0 : set coef0 in kernel function (default 0)\n"
						+ "-c cost : set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)\n"
						+ "-n nu : set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)\n"
						+ "-p epsilon : set the epsilon in loss function of epsilon-SVR (default 0.1)\n"
						+ "-m cachesize : set cache memory size in MB (default 100)\n"
						+ "-e epsilon : set tolerance of termination criterion (default 0.001)\n"
						+ "-h shrinking : whether to use the shrinking heuristics, 0 or 1 (default 1)\n"
						+ "-b probability_estimates : whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)\n"
						+ "-wi weight : set the parameter C of class i to weight*C, for C-SVC (default 1)\n"
						+ "-v n : n-fold cross validation mode\n"
						+ "-q : quiet mode (no outputs)\n");
		System.exit(1);
	}

	public static void main(String argv[]) throws IOException {
		int i, predict_probability = 0;
		svm_print_string = svm_print_stdout;

		// parse options
		for (i = 0; i < argv.length; i++) {
			if (argv[i].charAt(0) != '-')
				break;
			++i;
			switch (argv[i - 1].charAt(1)) {
			case 'b':
				predict_probability = atoi(argv[i]);
				break;
			case 'q':
				svm_print_string = svm_print_null;
				i--;
				break;
			default:
				System.err.print("Unknown option: " + argv[i - 1] + "\n");
				exit_with_help();
			}
		}
		if (i >= argv.length - 2)
			exit_with_help();
		try {
			BufferedReader input = new BufferedReader(new FileReader(argv[i]));
			DataOutputStream output = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(argv[i + 2])));
			SvmModel model = Svm.svm_load_model(argv[i + 1]);
			if (model == null) {
				System.err.print("can't open model file " + argv[i + 1] + "\n");
				System.exit(1);
			}
			if (predict_probability == 1) {
				if (Svm.svm_check_probability_model(model) == 0) {
					System.err
							.print("Model does not support probabiliy estimates\n");
					System.exit(1);
				}
			} else {
				if (Svm.svm_check_probability_model(model) != 0) {
					SvmPredict
							.info("Model supports probability estimates, but disabled in prediction.\n");
				}
			}
			predict(input, output, model, predict_probability);
			input.close();
			output.close();
		} catch (FileNotFoundException e) {
			exit_with_help();
		} catch (ArrayIndexOutOfBoundsException e) {
			exit_with_help();
		}
	}

	/**
	 * 测试 argv 第一个参数为模型的输入 第二个参数为模型的结果输出
	 * 
	 * @param argv
	 *            0 输入文件 1输入模型 2 输出文件
	 */
	public void test(String[] argv, SvmTrain trainModel) {
		int i = 0, predict_probability = 0;
		svm_print_string = svm_print_stdout;
		// parse options
		if (argv == null) {
			argv = new String[3];
		} else if (argv.length <= 1) {
			String temp = argv[0];
			argv = new String[3];
			argv[0] = temp;
		}
		try {
			if (trainModel == null) {
				try {
					if(argv[i]!=null)
					{
						input = new BufferedReader(new FileReader(argv[i]));
					}
					model = Svm.svm_load_model(argv[i + 1]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (model == null) {
					System.err.print("can't open model file " + argv[i + 1]
							+ "\n");
					System.exit(1);
				}
			} else {
				model = trainModel.model;
			}
			if (argv.length > 2 && argv[2] != null) {
				try {
					output = new DataOutputStream(new BufferedOutputStream(
							new FileOutputStream(argv[i + 2])));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (predict_probability == 1) {
				if (Svm.svm_check_probability_model(model) == 0) {
					System.err
							.print("Model does not support probabiliy estimates\n");
					System.exit(1);
				}
			} else {
				if (Svm.svm_check_probability_model(model) != 0) {
					SvmPredict
							.info("Model supports probability estimates, but disabled in prediction.\n");
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			exit_with_help();
		}
	}
}
