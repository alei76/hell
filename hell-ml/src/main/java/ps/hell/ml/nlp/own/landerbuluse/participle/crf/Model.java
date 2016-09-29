package ps.hell.ml.nlp.own.landerbuluse.participle.crf;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class Model {

	public static enum MODEL_TYPE {
		CRF, EMM
	};

	protected Template template = null;

	protected double[][] status = null;

	protected Map<String, Feature> myGrad;

	protected SmartForest<double[][]> smartForest = null;

	public int allFeatureCount = 0;

	private List<Element> leftList = null;

	private List<Element> rightList = null;

	public int end1;

	public int end2;

	/**
	 * 根据模板文件解析特征
	 * 
	 * @param left
	 * @param right
	 * @throws IOException
	 */
	public void makeSide(int left, int right) throws IOException {
		// TODO Auto-generated method stub

		leftList = new ArrayList<Element>(Math.abs(left));
		for (int i = left; i < 0; i++) {
			leftList.add(new Element((char) ('B' + i)));
		}

		rightList = new ArrayList<Element>(right);
		for (int i = 1; i < right + 1; i++) {
			rightList.add(new Element((char) ('B' + i)));
		}
	}

	/**
	 * 讲模型写入
	 * 
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void writeModel(String path) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		System.out.println("compute ok now to save model!");
		// 写模型
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(path))));

		// 配置模板
		oos.writeObject(template);
		// 特征转移率
		oos.writeObject(status);
		// 总共的特征数
		oos.writeInt(myGrad.size());
		double[] ds = null;
		for (Entry<String, Feature> entry : myGrad.entrySet()) {
			oos.writeUTF(entry.getKey());
			for (int i = 0; i < template.ft.length; i++) {
				ds = entry.getValue().w[i];
				for (int j = 0; j < ds.length; j++) {
					oos.writeByte(j);
					oos.writeFloat((float) ds[j]);
				}
				oos.writeByte(-1);
			}
		}

		oos.flush();
		oos.close();

	}

	/**
	 * 模型读取
	 * 
	 * @param modelPath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Model loadModel(String modelPath) throws Exception {
		return loadModel(new FileInputStream(modelPath));

	}

	public static Model loadModel(InputStream modelStream) throws Exception {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(modelStream)));

			Model model = new Model() {

				@Override
				public void writeModel(String path) throws FileNotFoundException, IOException {
					// TODO Auto-generated method stub
					throw new RuntimeException("you can not to calculate ,this model only use by cut ");
				}

			};

			model.template = (Template) ois.readObject();

			model.makeSide(model.template.left, model.template.right);

			int tagNum = model.template.tagNum;

			int featureNum = model.template.ft.length;

			model.smartForest = new SmartForest<double[][]>(0.8);

			model.status = (double[][]) ois.readObject();

			// 总共的特征数
			double[][] w = null;
			String key = null;
			int b = 0;
			int featureCount = ois.readInt();
			for (int i = 0; i < featureCount; i++) {
				key = ois.readUTF();
				w = new double[featureNum][0];
				for (int j = 0; j < featureNum; j++) {
					while ((b = ois.readByte()) != -1) {
						if (w[j].length == 0) {
							w[j] = new double[tagNum];
						}
						w[j][b] = ois.readFloat();
					}
				}
				model.smartForest.add(key, w);
			}

			return model;
		} finally {
			if (ois != null) {
				ois.close();
			}
		}
	}

	/**
	 * @param featureIndex 对应的feather 对应的 标记位
	 * @param chars 对应的1 or 2 ngram对应的字符
	 * @return
	 */
	public double[] getFeature(int featureIndex, char... chars) {
		// TODO Auto-generated method stub
		SmartForest<double[][]> sf = smartForest;
		sf = sf.getBranch(chars);
		if (sf == null || sf.getParam() == null) {
			return null;
		}
		return sf.getParam()[featureIndex];
	}

	/**
	 * tag转移率
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public double tagRate(int s1, int s2) {
		// TODO Auto-generated method stub
		return status[s1][s2];
	}

	
//	public void writeToFile(String filePath) throws Exception
//	{
////		NlpAnalysis.parse("说过，社交软件也是打着沟通的平台，让无数寂寞男女有了肉体与精神的寄托。")
//		Model model=Model.loadModel(DicReader.getInputStream("crf/crf.model"));
//		// 配置模板
//		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath, true),"utf-8");
//		BufferedWriter	writer = new BufferedWriter(write);
//		//Template template =tempLate;
//		LinkedList<String> list=new LinkedList<String>();
//		list.add(model.template.toString2());
////		// 特征转移率
////		oos.writeObject(status);
//		StringBuffer sb=new StringBuffer();
//		sb.append("<status>\n");
//		for(double[] dostat:model.status)
//		{
//			for(double zpS:dostat)
//			{
//				sb.append(zpS).append("\t");
//			}
//			sb.append("\n");
//		}
//		sb.append("</status>\n");
//		sb.append("<smartForest>\n");
//		ObjectInputStream ois = null;
//		try {
//			ois = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(DicReader.getInputStream("crf/crf.model"))));
//
//			Model model2 = new Model() {
//
//				@Override
//				public void writeModel(String path) throws FileNotFoundException, IOException {
//					// TODO Auto-generated method stub
//					throw new RuntimeException("you can not to calculate ,this model only use by cut ");
//				}
//
//			};
//
//			model2.template = (Template) ois.readObject();
//
//			model2.makeSide(model2.template.left, model2.template.right);
//
//			int tagNum = model2.template.tagNum;
//
//			int featureNum = model2.template.ft.length;
//
//			model2.smartForest = new SmartForest<double[][]>(0.8);
//
//			model2.status = (double[][]) ois.readObject();
//
//			// 总共的特征数
//			double[][] w = null;
//			String key = null;
//			int b = 0;
//			int featureCount = ois.readInt();
//			for (int i = 0; i < featureCount; i++) {
//				key = ois.readUTF();
//				w = new double[featureNum][0];
//				for (int j = 0; j < featureNum; j++) {
//					while ((b = ois.readByte()) != -1) {
//						if (w[j].length == 0) {
//							w[j] = new double[tagNum];
//						}
//						w[j][b] = ois.readFloat();
//					}
//				}
//				model2.smartForest.add(key, w);
//				sb.append(key).append("\n");
//				for(double[] dostat:w)
//				{
//					for(double zpS:dostat)
//					{
//						sb.append(zpS).append("\t");
//					}
//					sb.append("@");
//				}
//				sb.append("\n");
//			}
//		} finally {
//			if (ois != null) {
//				ois.close();
//			}
//		}
//		sb.append("</smartForest>\n");
//		// 总共的特征数
//		//sb.append("<featherSize>\n").append(model.myGrad.size()).append("</feathterSize>\n");
////		sb.append("<mGrad>\n");
////		double[] ds = null;
////		list.add(sb.toString());
////		for (Entry<String, Feature> entry : model.myGrad.entrySet()) {
////			sb=new StringBuffer();
////			sb.append(entry.getKey()+"@");
////			for(int i=0;i<model.template.ft.length;i++)
////			{
////				ds = entry.getValue().w[i];
////				for (int j = 0; j < ds.length; j++) {
////					if(j!=0)
////					{
////						sb.append("@");
////					}
////					sb.append(j).append(":").append(ds[j]);
////				}
////				sb.append("\t");
////			}
////			sb.append("\n");
////			list.add(sb.toString());
////		}
////		sb=new StringBuffer();
////		sb.append("</mGrad>\n");
//		list.add(sb.toString());
//		while(list.size()>0)
//		{
//			writer.write(list.pollFirst());
//		}
//		writer.flush();
//		writer.close();
//		write.close();
//	}
	public static void main(String[] args) throws Exception {
//		Model model=new CRFModel();
//		model.writeToFile(System.getProperty("user.dir")+"/resources/crf/crf_temp.model");
	}
}