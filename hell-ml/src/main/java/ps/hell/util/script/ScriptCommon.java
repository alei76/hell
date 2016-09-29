package ps.hell.util.script;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ps.hell.util.JsonUtil;

/**
 * 存储脚本的基本结构
 * 
 * @author Administrator
 *
 */
public class ScriptCommon {
	/**
	 * 日志
	 */
	public static Logger logger = LoggerFactory.getLogger(ScriptCommon.class);
	/**
	 * 所使用的文档
	 */
	public ArrayList<ArrayList<String>> useFeature = new ArrayList<ArrayList<String>>();

	public ArrayList<String> defaultFeature = new ArrayList<String>();
	/**
	 * 权重系数
	 */
	public ArrayList<Double> coefficientFeature=new ArrayList<Double>();
	/**
	 * script 切分
	 */
	public ArrayList<String> scriptSlit = new ArrayList<String>();
	/**
	 * 使用数组的索引值
	 */
	public ArrayList<Integer> indexFeature = new ArrayList<Integer>();

	/**
	 * 将脚本文件转换成 脚本的存储结构
	 * 
	 * @param fileName
	 *            使用的文件名
	 * @param scriptFile
	 *            使用的数据值
	 */
	public ScriptCommon(String fileName, String scriptFile) {

		Pattern pattern = Pattern.compile("(\\$\\{[^\\}]*?\\})");
		Matcher matcher = pattern.matcher(scriptFile);
		int start = 0;
		while (matcher.find()) {
			String name = matcher.group(1).replaceAll("[\\$\\{\\}]", "").trim();
			System.out.println("name:" + name);
			int start2 = matcher.start();

			change(name);
			scriptSlit.add(scriptFile.substring(start, start2));
			start = matcher.end();
		}
		scriptSlit.add(scriptFile.substring(start));
	}

	/**
	 * 将使用的字段转化为反射方法集
	 * @param name
	 *            使用的数据值
	 */
	public void change(String name) {
		int index = name.indexOf(" default=");
		String nameTemp = null;
		if (index < 0) {
			nameTemp = name;
			defaultFeature.add(null);
		} else {
			nameTemp = name.substring(0, index).trim();
			defaultFeature.add(name.substring(index + 9).split(" ")[0].trim());
		}

		int indexV = name.indexOf(" index=");
		if (indexV < 0) {
			indexFeature.add(null);
		} else {
			if (indexV < index) {
				nameTemp = name.substring(0, indexV).trim();
			} else if (index < 0) {
				nameTemp = name.substring(0, indexV).trim();
			}
			indexFeature.add(Integer
					.parseInt(name.substring(indexV + 7).split(" ")[0].trim()));
		}
		index=Math.min(index, indexV);
		indexV = name.indexOf(" coefficient=");
		if (indexV < 0) {
			coefficientFeature.add(null);
		} else {
			if (indexV < index) {
				nameTemp = name.substring(0, indexV).trim();
			} else if (index < 0) {
				nameTemp = name.substring(0, indexV).trim();
			}
			coefficientFeature.add(Double
					.parseDouble(name.substring(indexV + 13).split(" ")[0].trim()));
		}
		String[] nameList = nameTemp.split("\\.");
		ArrayList<String> list = new ArrayList<String>();
		for (String str : nameList) {
			list.add("get" + Character.toUpperCase(str.charAt(0))
					+ str.substring(1));
		}
		useFeature.add(list);
	}

	public Object getObject(Object fatherObject, String methodName)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		System.out.println(fatherObject.toString());
		Class classType = fatherObject.getClass();
		Method getMethod = classType.getMethod(methodName, new Class[] {});
		// 调用原对象的getXXX()方法
		return getMethod.invoke(fatherObject, new Object[] {});
	}

	/**
	 * 获取实际数据值
	 * 
	 * @param inputObject
	 * @param index
	 * @return
	 */
	public Object getValue(Object inputObject, int index) {
		ArrayList<String> list = useFeature.get(index);
		Object obj = inputObject;
		for (int i = 0; i < list.size(); i++) {
			try {
				obj = getObject(obj, list.get(i));
				if (obj == null) {
					return defaultFeature.get(index);
				}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj;
	}

	/**
	 * 获取实际额脚本
	 * 
	 * @param obj
	 * @return
	 */
	private String getScript(Object obj) {
		// 获取每一个变量的值
		// 获取每一个变量的值
				ArrayList<Object> list = new ArrayList<Object>();
				for (int i = 0; i < useFeature.size(); i++) {
					Object indexValue = getValue(obj, i);
					// 替换每一个值
					list.add(indexValue);
				}
				StringBuffer sb = new StringBuffer();
				sb.append(scriptSlit.get(0));
				for (int i = 0; i < list.size(); i++) {
					//logger.info("数据:" + JsonUtil.getJsonStr(list.get(i)));
					// logger.info(list.get(i).getClass().getName());
					Object listOne = list.get(i);
					if (listOne == null) {
						sb.append(defaultFeature.get(i)).append(scriptSlit.get(i + 1));
					} else if (listOne instanceof String) {
						sb.append("\"").append(listOne).append("\"")
								.append(scriptSlit.get(i + 1));
					} else if (listOne instanceof List) {
						if (indexFeature.get(i) == null) {
							sb.append(JsonUtil.getJsonStr(listOne)).append(scriptSlit.get(i + 1));
						} else if (((List) listOne).size() > indexFeature.get(i)) {
							Object val = ((List) listOne).get(indexFeature.get(i));
							if (val instanceof String) {
								sb.append("\"").append(val).append("\"")
										.append(scriptSlit.get(i + 1));
							} else {
								sb.append(JsonUtil.getJsonStr(list.get(i))).append(
										scriptSlit.get(i + 1));
							}
						} else {
							sb.append(defaultFeature.get(i)).append(scriptSlit.get(i + 1));
						}
					} else if (listOne instanceof Set) {
						if (indexFeature.get(i) == null) {
							sb.append(JsonUtil.getJsonStr(listOne)).append(scriptSlit.get(i + 1));
						} else if (((Set) listOne).size() > indexFeature.get(i)) {
							Object val = ((List) listOne).get(indexFeature.get(i));
							if (val instanceof String) {
								sb.append("\"").append(val).append("\"")
										.append(scriptSlit.get(i + 1));
							} else {
								sb.append(JsonUtil.getJsonStr(list.get(i))).append(
										scriptSlit.get(i + 1));
							}
						} else {
							sb.append(defaultFeature.get(i)).append(scriptSlit.get(i + 1));
						}
					}else if (listOne.getClass().isArray()) {
						if (indexFeature.get(i) == null) {
							
							if(listOne instanceof double[]){
								double[] temp=(double[])listOne;
								if(temp.length>0){
									sb.append(JsonUtil.getJsonStr(listOne)).append(scriptSlit.get(i + 1));
								}else{
									sb.append(defaultFeature.get(i)).append(scriptSlit.get(i+1));
								}
							}else if(listOne instanceof int[]){
								int[] temp=(int[])listOne;
								if(temp.length>0){
									sb.append(JsonUtil.getJsonStr(listOne)).append(scriptSlit.get(i + 1));
								}else{
									sb.append(defaultFeature.get(i)).append(scriptSlit.get(i+1));
								}
							}else if(listOne instanceof float[]){
								float[] temp=(float[])listOne;
								if(temp.length>0){
									sb.append(JsonUtil.getJsonStr(listOne)).append(scriptSlit.get(i + 1));
								}else{
									sb.append(defaultFeature.get(i)).append(scriptSlit.get(i+1));
								}
							}else if(listOne instanceof long[]){
								long[] temp=(long[])listOne;
								if(temp.length>0){
									sb.append(JsonUtil.getJsonStr(listOne)).append(scriptSlit.get(i + 1));
								}else{
									sb.append(defaultFeature.get(i)).append(scriptSlit.get(i+1));
								}
							}else if(listOne instanceof String[]){
								String[] temp=(String[])listOne;
								if(temp.length>0){
									sb.append(JsonUtil.getJsonStr(listOne)).append(scriptSlit.get(i + 1));
								}else{
									sb.append(defaultFeature.get(i)).append(scriptSlit.get(i+1));
								}
							}
						} else if (listOne instanceof double[]) {
							double[] ll = (double[]) listOne;
							if (ll.length > indexFeature.get(i)) {
								sb.append(ll[indexFeature.get(i)]).append(
										scriptSlit.get(i + 1));
							} else {
								sb.append(ll[indexFeature.get(i)]).append(
										scriptSlit.get(i + 1));
							}
						} else if (listOne instanceof long[]) {
							long[] ll = (long[]) listOne;
							if (ll.length > indexFeature.get(i)) {
								sb.append(ll[indexFeature.get(i)]).append(
										scriptSlit.get(i + 1));
							} else {
								sb.append(ll[indexFeature.get(i)]).append(
										scriptSlit.get(i + 1));
							}
						} else if (listOne instanceof int[]) {
							int[] ll = (int[]) listOne;
							if (ll.length > indexFeature.get(i)) {
								sb.append(ll[indexFeature.get(i)]).append(
										scriptSlit.get(i + 1));
							} else {
								sb.append(ll[indexFeature.get(i)]).append(
										scriptSlit.get(i + 1));
							}
						} else if (listOne instanceof float[]) {
							float[] ll = (float[]) listOne;
							if (ll.length > indexFeature.get(i)) {
								sb.append(ll[indexFeature.get(i)]).append(
										scriptSlit.get(i + 1));
							} else {
								sb.append(ll[indexFeature.get(i)]).append(
										scriptSlit.get(i + 1));
							}
						} else if (((String[]) listOne).length > indexFeature.get(i)) {
							Object val = ((Object[]) list.get(i))[indexFeature.get(i)];
							if (val instanceof String) {
								sb.append("\"").append(val).append("\"")
										.append(scriptSlit.get(i + 1));
							} else {
								sb.append(val).append(scriptSlit.get(i + 1));
							}
						} else {
							sb.append(defaultFeature.get(i)).append(scriptSlit.get(i + 1));
						}
					} else {
						Double coef=coefficientFeature.get(i);
						if(coef==null){
							sb.append(listOne).append(scriptSlit.get(i + 1));
						}else if(listOne instanceof Long){
							sb.append((long)((long)listOne*coef)).append(scriptSlit.get(i+1));
						}else if(listOne instanceof Double){
							sb.append((double)((double)listOne*coef)).append(scriptSlit.get(i+1));
						}else if(listOne instanceof Integer){
							System.out.println("listOne:"+(int)listOne+"\t"+coef);
							sb.append((int)((int)listOne*coef)).append(scriptSlit.get(i+1));
						}else if(listOne instanceof Float){
							sb.append((float)((float)listOne*coef)).append(scriptSlit.get(i+1));
						}else{
							sb.append(listOne).append(scriptSlit.get(i + 1));
						}
					}
				}
				return sb.toString();
	}

	public String get() {
		return scriptSlit.get(0);
	}

	public String get(Object obj) {
		return getScript(obj);
	}

	public String get(int bid) {
		return scriptSlit.get(0) + bid + scriptSlit.get(1);
	}
}