package ps.hell.ml.forest.model.libsvm;

import java.io.*;
import java.util.*;

class SvmTrain {
	public SvmParameter param; // set by parse_command_line
	public SvmProblem prob; // set by read_problem
	public SvmModel model;
	public String input_file_name; // set by parse_command_line
	public String model_file_name; // set by parse_command_line
	public String error_msg;
	public int cross_validation;
	public int nr_fold;
	/**
	 * 是否打印预测
	 */
	public boolean isPrint = true;

	SvmPrintInterface print_func = null; // default printing to stdout

	/**
	 * 输入
	 */
	public Vector<SvmNode[]> data = null;
	/**
	 * 输入
	 */
	public Vector<Double> dataTag = null;

	/**
	 * y的输出
	 */
	HashMap<Integer, String> mapString = null;
	HashMap<String, Integer> mapStringRel = null;
	/**
	 * x的输入string
	 */
	HashMap<Integer, HashMap<Integer, String>> indexMapString = null;
	HashMap<Integer, HashMap<String, Integer>> indexMapStringRel = null;
	/**
	 * s对应字符串
	 */
	String[] xClass = null;

	public static SvmPrintInterface svm_print_null = new SvmPrintInterface() {
		@Override
		public void print(String s) {
		}
	};

	public SvmTrain() {
		param = new SvmParameter();
		// default values
		param.svm_type = SvmParameter.C_SVC;
		param.kernel_type = SvmParameter.RBF;
		param.degree = 3;
		param.gamma = 0; // 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		// c值越大越容易过拟合
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		cross_validation = 0;
	}

	private static void exit_with_help() {
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

	private void do_cross_validation() {
		int i;
		int total_correct = 0;
		double total_error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
		double[] target = new double[prob.l];

		Svm.svm_cross_validation(prob, param, nr_fold, target);
		if (param.svm_type == SvmParameter.EPSILON_SVR
				|| param.svm_type == SvmParameter.NU_SVR) {
			for (i = 0; i < prob.l; i++) {
				double y = prob.y[i];
				double v = target[i];
				total_error += (v - y) * (v - y);
				sumv += v;
				sumy += y;
				sumvv += v * v;
				sumyy += y * y;
				sumvy += v * y;
			}
			System.out.print("Cross Validation Mean squared error = "
					+ total_error / prob.l + "\n");
			System.out
					.print("Cross Validation Squared correlation coefficient = "
							+ ((prob.l * sumvy - sumv * sumy) * (prob.l * sumvy - sumv
									* sumy))
							/ ((prob.l * sumvv - sumv * sumv) * (prob.l * sumyy - sumy
									* sumy)) + "\n");
		} else {
			for (i = 0; i < prob.l; i++)
				if (target[i] == prob.y[i])
					++total_correct;
			System.out.print("Cross Validation Accuracy = " + 100.0
					* total_correct / prob.l + "%\n");
		}
	}

	private void run(String argv[]) throws IOException {
		parse_command_line(argv);
		read_problem();
		error_msg = Svm.svm_check_parameter(prob, param);

		if (error_msg != null) {
			System.err.print("ERROR: " + error_msg + "\n");
			System.exit(1);
		}

		if (cross_validation != 0) {
			do_cross_validation();
		} else {
			model = Svm.svm_train(prob, param);
			Svm.svm_save_model(model_file_name, model);
		}
	}

	/**
	 * 
	 * @param argv
	 * @throws IOException
	 */
	public static void main(String argv[]) throws IOException {
		SvmTrain t = new SvmTrain();
		t.run(argv);
	}

	/**
	 * 输入参数 输入参数
	 * 
	 * @param argv
	 *            输入参数
	 * @param x
	 *            输入值
	 * @param y
	 *            y
	 * @param inputFile
	 *            输入文件
	 */
	public void train(String[] argv, double[][] x, double[] y, String inputFile) {
		// determine filenames
		if (x == null) {
			input_file_name = inputFile;
		} else {
			this.data = new Vector<SvmNode[]>();
			this.dataTag = new Vector<Double>();
			for (int i = 0; i < y.length; i++) {
				SvmNode[] svmNode = new SvmNode[x[i].length];
				for (int j = 0; j < x[i].length; j++) {
					SvmNode node = new SvmNode();
					node.index = j + 1;
					node.value = x[i][j];
					svmNode[j] = node;
				}
				this.data.add(svmNode);
				this.dataTag.add(y[i]);
			}
		}
		train(argv);
	}

	/**
	 * 执行程序
	 * 
	 * @param args
	 *            输入参数 可以为空 如果需要设置相关svm参数可以直接使用 model中的 param
	 * @param x
	 *            输入的x
	 * @param y
	 *            输入的y index从1开始
	 * @param 如果x为空则输入为文件
	 *            文件格式为 预测值 index1:val1 index2:val2
	 */
	public void train(String[] argv, double[][] x, String[] y, String inputFile) {
		// determine filenames
		this.mapString = new HashMap<Integer, String>();
		this.mapStringRel = new HashMap<String, Integer>();
		int index = 0;
		if (x == null) {
			input_file_name = inputFile;
		} else {
			this.data = new Vector<SvmNode[]>();
			this.dataTag = new Vector<Double>();
			for (int i = 0; i < y.length; i++) {
				SvmNode[] svmNode = new SvmNode[x[i].length];
				for (int j = 0; j < x[i].length; j++) {
					SvmNode node = new SvmNode();
					node.index = j + 1;
					node.value = x[i][j];
					svmNode[j] = node;
				}
				this.data.add(svmNode);
				Integer indexMapValue = mapStringRel.get(y[i]);
				if (indexMapValue != null) {
					this.dataTag.add(indexMapValue * 1D);
				} else {
					index++;
					mapString.put(index, y[i]);
					mapStringRel.put(y[i], index);
					this.dataTag.add(index * 1D);
				}

			}
		}
		this.train(argv);
	}

	/**
	 * 执行程序
	 * 
	 * @param args
	 *            输入参数 可以为空 如果需要设置相关svm参数可以直接使用 model中的 param
	 * @param x
	 *            输入的x
	 * @param y
	 *            输入的y index从1开始
	 * @param 如果x为空则输入为文件
	 *            文件格式为 预测值 index1:val1 index2:val2
	 */
	public void train(String[] argv, String[][] x, String[] xClass, String[] y,
			String inputFile) {
		// determine filenames
		this.mapString = new HashMap<Integer, String>();
		this.mapStringRel = new HashMap<String, Integer>();
		this.indexMapString = new HashMap<Integer, HashMap<Integer, String>>();
		this.indexMapStringRel = new HashMap<Integer, HashMap<String, Integer>>();
		this.xClass = xClass;
		int index = 0;
		int[] indexList = new int[x[0].length];
		if (x == null) {
			input_file_name = inputFile;
		} else {
			this.data = new Vector<SvmNode[]>();
			this.dataTag = new Vector<Double>();
			for (int i = 0; i < y.length; i++) {
				SvmNode[] svmNode = new SvmNode[x[i].length];
				for (int j = 0; j < x[i].length; j++) {
					SvmNode node = new SvmNode();
					node.index = j + 1;
					if (xClass[j] == null || !xClass[j].equals("s")) {
						node.value = Double.parseDouble(x[i][j]);
					} else {
						HashMap<String, Integer> map = indexMapStringRel
								.get(j + 1);
						HashMap<Integer, String> mapRel = indexMapString
								.get(j + 1);
						if (map == null) {
							map = new HashMap<String, Integer>();
							indexMapStringRel.put(j + 1, map);
							mapRel = new HashMap<Integer, String>();
							indexMapString.put(j + 1, mapRel);
							indexList[j]++;
							map.put(x[i][j], indexList[j]);
							mapRel.put(indexList[j], x[i][j]);
							node.value = indexList[j];
						} else {
							Integer val = map.get(x[i][j]);
							if (val == null) {
								indexList[j]++;
								map.put(x[i][j], indexList[j]);
								mapRel.put(indexList[j], x[i][j]);
								node.value = indexList[j];
							} else {
								node.value = val;
							}
						}
					}
					svmNode[j] = node;
				}
				this.data.add(svmNode);
				Integer indexMapValue = mapStringRel.get(y[i]);
				if (indexMapValue != null) {
					this.dataTag.add(indexMapValue * 1D);
				} else {
					index++;
					mapString.put(index, y[i]);
					mapStringRel.put(y[i], index);
					this.dataTag.add(index * 1D);
				}

			}
		}
		this.train(argv);
	}

	/**
	 * 加载参数
	 * 
	 * @param argv
	 */
	public void train(String[] argv) {
		// SvmTrain t = new SvmTrain();
		int i = 0;

		// parse options
		if (argv != null) {
			for (i = 0; i < argv.length; i++) {
				if (argv[i].charAt(0) != '-')
					break;
				if (++i >= argv.length)
					exit_with_help();
				switch (argv[i - 1].charAt(1)) {
				case 's':
					param.svm_type = atoi(argv[i]);
					break;
				case 't':
					param.kernel_type = atoi(argv[i]);
					break;
				case 'd':
					param.degree = atoi(argv[i]);
					break;
				case 'g':
					param.gamma = atof(argv[i]);
					break;
				case 'r':
					param.coef0 = atof(argv[i]);
					break;
				case 'n':
					param.nu = atof(argv[i]);
					break;
				case 'm':
					param.cache_size = atof(argv[i]);
					break;
				case 'c':
					param.C = atof(argv[i]);
					break;
				case 'e':
					param.eps = atof(argv[i]);
					break;
				case 'p':
					param.p = atof(argv[i]);
					break;
				case 'h':
					param.shrinking = atoi(argv[i]);
					break;
				case 'b':
					param.probability = atoi(argv[i]);
					break;
				case 'q':
					print_func = svm_print_null;
					i--;
					break;
				case 'v':
					cross_validation = 1;
					nr_fold = atoi(argv[i]);
					if (nr_fold < 2) {
						System.err
								.print("n-fold cross validation: n must >= 2\n");
						exit_with_help();
					}
					break;
				case 'w':
					++param.nr_weight;
					{
						int[] old = param.weight_label;
						param.weight_label = new int[param.nr_weight];
						System.arraycopy(old, 0, param.weight_label, 0,
								param.nr_weight - 1);
					}

					{
						double[] old = param.weight;
						param.weight = new double[param.nr_weight];
						System.arraycopy(old, 0, param.weight, 0,
								param.nr_weight - 1);
					}

					param.weight_label[param.nr_weight - 1] = atoi(argv[i - 1]
							.substring(2));
					param.weight[param.nr_weight - 1] = atof(argv[i]);
					break;
				default:
					System.err.print("Unknown option: " + argv[i - 1] + "\n");
					exit_with_help();
				}
			}
		}

		Svm.svm_set_print_string_function(print_func);

		if (argv != null) {
			if (i >= argv.length)
				exit_with_help();
		}
		try {
			read_problem();
		} catch (IOException e) {
			e.printStackTrace();
		}
		error_msg = Svm.svm_check_parameter(prob, param);
		if (error_msg != null) {
			System.err.print("ERROR: " + error_msg + "\n");
			System.exit(1);
		}

		if (cross_validation != 0) {
			do_cross_validation();
		} else {
			// 训练
			model = Svm.svm_train(prob, param);
			model.mapString = this.mapString;
			model.mapStringRel = this.mapStringRel;
			model.indexMapString = this.indexMapString;
			model.indexMapStringRel = this.indexMapStringRel;
			model.xClass = this.xClass;
			if (isPrint) {
				int correct = 0;
				int all = prob.y.length;
				double error = 0;
				for (int j = 0; j < prob.x.length; j++) {
					double predictVal = predict(prob.x[j]);
					error += (predictVal - prob.y[j])
							* (predictVal - prob.y[j]);
					if ((int) (predictVal) == (int) (prob.y[j])) {
						correct++;
					}
				}
				Svm.info("error_all:" + error + "\n");
				Svm.info("correct rate:" + correct * 1f / all + "\t summary:"
						+ correct + "/" + all + "\n");
			}
		}
	}

	/**
	 * 具体值
	 * 
	 * @param x
	 * @return
	 */
	public double predict(SvmNode[] x) {
		double v;
		double[] prob_estimates = null;
		int svm_type = Svm.svm_get_svm_type(model);
		int nr_class = Svm.svm_get_nr_class(model);
		if (model.param.probability == 1) {
			if (model.param.svm_type == SvmParameter.EPSILON_SVR
					|| model.param.svm_type == SvmParameter.NU_SVR) {
				SvmPredict
						.info("Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="
								+ Svm.svm_get_svr_probability(model) + "\n");
			} else {
				int[] labels = new int[nr_class];
				Svm.svm_get_labels(model, labels);
				prob_estimates = new double[nr_class];
			}
		}
		if (model.param.probability == 1
				&& (model.param.svm_type == SvmParameter.C_SVC || model.param.svm_type == SvmParameter.NU_SVC)) {
			v = Svm.svm_predict_probability(model, x, prob_estimates);
			return v;
		} else {
			v = Svm.svm_predict(model, x);
			return v;
		}
	}

	/**
	 * 写入文件
	 * 
	 * @param filePath
	 *            写入文件目录
	 */
	public void writeToFile(String filePath) {
		model_file_name = filePath;
		try {
			Svm.svm_save_model(model_file_name, model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static double atof(String s) {
		double d = Double.valueOf(s).doubleValue();
		if (Double.isNaN(d) || Double.isInfinite(d)) {
			System.err.print("NaN or Infinity in input\n");
			System.exit(1);
		}
		return (d);
	}

	private static int atoi(String s) {
		return Integer.parseInt(s);
	}

	private void parse_command_line(String argv[]) {
		int i;
		// parse options
		for (i = 0; i < argv.length; i++) {
			if (argv[i].charAt(0) != '-')
				break;
			if (++i >= argv.length)
				exit_with_help();
			switch (argv[i - 1].charAt(1)) {
			case 's':
				param.svm_type = atoi(argv[i]);
				break;
			case 't':
				param.kernel_type = atoi(argv[i]);
				break;
			case 'd':
				param.degree = atoi(argv[i]);
				break;
			case 'g':
				param.gamma = atof(argv[i]);
				break;
			case 'r':
				param.coef0 = atof(argv[i]);
				break;
			case 'n':
				param.nu = atof(argv[i]);
				break;
			case 'm':
				param.cache_size = atof(argv[i]);
				break;
			case 'c':
				param.C = atof(argv[i]);
				break;
			case 'e':
				param.eps = atof(argv[i]);
				break;
			case 'p':
				param.p = atof(argv[i]);
				break;
			case 'h':
				param.shrinking = atoi(argv[i]);
				break;
			case 'b':
				param.probability = atoi(argv[i]);
				break;
			case 'q':
				print_func = svm_print_null;
				i--;
				break;
			case 'v':
				cross_validation = 1;
				nr_fold = atoi(argv[i]);
				if (nr_fold < 2) {
					System.err.print("n-fold cross validation: n must >= 2\n");
					exit_with_help();
				}
				break;
			case 'w':
				++param.nr_weight;
				{
					int[] old = param.weight_label;
					param.weight_label = new int[param.nr_weight];
					System.arraycopy(old, 0, param.weight_label, 0,
							param.nr_weight - 1);
				}

				{
					double[] old = param.weight;
					param.weight = new double[param.nr_weight];
					System.arraycopy(old, 0, param.weight, 0,
							param.nr_weight - 1);
				}

				param.weight_label[param.nr_weight - 1] = atoi(argv[i - 1]
						.substring(2));
				param.weight[param.nr_weight - 1] = atof(argv[i]);
				break;
			default:
				System.err.print("Unknown option: " + argv[i - 1] + "\n");
				exit_with_help();
			}
		}

		Svm.svm_set_print_string_function(print_func);

		// determine filenames

		if (i >= argv.length)
			exit_with_help();

		input_file_name = argv[i];

		if (i < argv.length - 1)
			model_file_name = argv[i + 1];
		else {
			int p = argv[i].lastIndexOf('/');
			++p; // whew...
			model_file_name = argv[i].substring(p) + ".model";
		}
	}

	// read in a problem (in svmlight format)

	private void read_problem() throws IOException {

		Vector<Double> vy = new Vector<Double>();
		Vector<SvmNode[]> vx = new Vector<SvmNode[]>();
		int max_index = 0;
		if (input_file_name != null) {
			BufferedReader fp = new BufferedReader(new FileReader(
					input_file_name));
			while (true) {
				String line = fp.readLine();
				if (line == null)
					break;
				StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
				vy.addElement(atof(st.nextToken()));
				int m = st.countTokens() / 2;
				SvmNode[] x = new SvmNode[m];
				for (int j = 0; j < m; j++) {
					x[j] = new SvmNode();
					x[j].index = atoi(st.nextToken());
					x[j].value = atof(st.nextToken());
				}
				if (m > 0)
					max_index = Math.max(max_index, x[m - 1].index);
				vx.addElement(x);
			}
			fp.close();
		} else if (data == null) {
			Svm.info("请输入文件获取数据");
			System.exit(1);
		} else {
			vx = data;
			vy = dataTag;
			if (data.size() > 0) {
				max_index = data.elementAt(0).length + 1;
			}
			for (int i = 0; i < data.size(); i++) {
				System.out.println(Arrays.toString(data.elementAt(i)) + "\t"
						+ dataTag.elementAt(i).toString());
			}
		}
		prob = new SvmProblem();
		prob.l = vy.size();
		prob.x = new SvmNode[prob.l][];
		for (int i = 0; i < prob.l; i++)
			prob.x[i] = vx.elementAt(i);
		prob.y = new double[prob.l];
		for (int i = 0; i < prob.l; i++)
			prob.y[i] = vy.elementAt(i);

		if (param.gamma == 0 && max_index > 0)
			param.gamma = 1.0 / max_index;

		if (param.kernel_type == SvmParameter.PRECOMPUTED)
			for (int i = 0; i < prob.l; i++) {
				if (prob.x[i][0].index != 0) {
					System.err
							.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
					System.exit(1);
				}
				if ((int) prob.x[i][0].value <= 0
						|| (int) prob.x[i][0].value > max_index) {
					System.err
							.print("Wrong input format: sample_serial_number out of range\n");
					System.exit(1);
				}
			}
	}
}
