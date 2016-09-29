package ps.hell.util.office;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import ps.hell.util.db.MysqlConnection;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtil {

	/**
	 * excel 实体
	 */
	WritableWorkbook book = null;
	/**
	 * sheet 表
	 */
	WritableSheet sheet = null;

	public void createExecl(String file, String str) {
		try {
			File fileF = new File(file);
			if (fileF.exists()) {
				if (str.equals("delete")) {
					fileF.delete();
				} else {
					System.out.println("文件已经存在");
					System.exit(1);
				}
			}
			// 打开文件
			book = Workbook.createWorkbook(fileF);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
			// 以及单元格内容为test
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param sheetName
	 *            sheet 名字
	 * @param index
	 *            第几个 从0 开始
	 */
	public void addSheet(String sheetName, int index) {
		sheet = book.createSheet(sheetName, index);
	}

	/**
	 * 添加标题
	 * 
	 * @param x
	 * @param y
	 * @param name
	 */
	public void addTitle(int x, int y, String name) {
		Label label = new Label(y, x, name);

		// 将定义好的单元格添加到工作表中
		try {
			sheet.addCell(label);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 写入数值
	 * 
	 * @param x
	 * @param y
	 * @param value
	 */
	public void writeNumber(int x, int y, float value) {
		jxl.write.Number number = new jxl.write.Number(y, x, value);
		try {
			sheet.addCell(number);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 刷新
	 */
	public void flush() {
		// 写入数据并关闭文件
		try {
			book.write();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 关闭
	 */
	public void close() {
		flush();
		try {
			book.close();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class ExcelNode {
		/**
		 * 当前值
		 */
		public int thisValue = 2;
		/**
		 * 标题
		 */
		public String[] title = null;
		/**
		 * 值
		 */
		public ArrayList<Float[]> valueF = new ArrayList<Float[]>();
		/**
		 * 值
		 */
		public ArrayList<String[]> valueS = new ArrayList<String[]>();

		public ExcelNode() {
		}

		public void addF(Float[] f) {

			this.valueF.add(f);
		}

		public void addS(String[] s) {
			this.valueS.add(s);
		}
	}

	/**
	 * 
	 * @param filePath
	 *            文件地址
	 * @param sheetIndex
	 *            sheetindex
	 * @param hasTitle
	 *            是否有标题
	 * @param 读取类型
	 * @return
	 */
	public static ExcelNode readExecl(String filePath, int sheetIndex,
			boolean hasTitle, Object obj) {
		ExcelNode node = new ExcelUtil().new ExcelNode();
		try {
			Workbook book = Workbook.getWorkbook(new File(filePath));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(sheetIndex);
			int index = -1;
			if (hasTitle) {
				LinkedList<String> title = new LinkedList<String>();
				for (int i = 0; i < 1000; i++) {
					// 得到第一列第一行的单元格
					Cell cell1 = null;
					try {
						cell1 = sheet.getCell(i, 0);
					} catch (Exception e) {
						break;
					}
					String result = cell1.getContents();
					if (result == null || result.equals("")) {
						break;
					} else {
						title.add(result);
					}
					index++;
				}
				node.title = title.toArray(new String[title.size()]);
			}
			int indexL = 0;
			if (index >= 0) {
				indexL++;
			}
			for (int i = indexL; i < Integer.MAX_VALUE; i++) {
				if (obj instanceof String) {
					LinkedList<String> val = new LinkedList<String>();
					int valn = -1;
					for (int j = 0; j <= index; j++) {
						// 得到第一列第一行的单元格
						Cell cell1 = null;
						try {
							cell1 = sheet.getCell(j, i);
						} catch (Exception e) {
							valn++;
							val.add("");
							continue;
						}
						String result = cell1.getContents();
						if (result == null || result.equals("")) {
							valn++;
						}
						val.add(result);
					}
					if (valn == index) {
						break;
					}
					node.addS((val.toArray(new String[val.size()])));
				} else if (obj instanceof Float) {
					LinkedList<Float> val = new LinkedList<Float>();
					int valn = -1;
					for (int j = 0; j <= index; j++) {
						// 得到第一列第一行的单元格
						Cell cell1 = null;
						try {
							cell1 = sheet.getCell(j, i);
						} catch (Exception e) {
							valn++;
							val.add(0f);
							continue;
						}
						String result = cell1.getContents();
						if (result == null || result.equals("")) {
							valn++;
							val.add(0f);
							continue;
						}
						val.add(Float.parseFloat(result));
					}
					if (valn == index) {
						break;
					}
					// Float[] temp=new Float[val.size()];
					// int zn=0;
					// for(Float f:val)
					// {
					// temp[zn]=f;
					// zn++;
					// }
					node.addF((val.toArray(new Float[val.size()])));
				}

			}
			// 得到第一列第一行的单元格
			book.close();
			return node;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	// public static void main(String[] args) {
	// ExcelUtil excelUtil = new ExcelUtil();
	// excelUtil.createExecl("f:\\test.xls", "delete");
	// excelUtil.addSheet("test", 0);
	// excelUtil.addTitle(0, 0, "znnmm");
	// excelUtil.addTitle(0, 1, "znnmm2");
	// excelUtil.writeNumber(1, 0, 3);
	// excelUtil.writeNumber(1, 1, 4);
	// excelUtil.writeNumber(2, 0, 5);
	// excelUtil.flush();
	// excelUtil.close();
	//
	// ExcelNode node = ExcelUtil.readExecl("f:\\test.xls", 0, true,
	// new Float(3f));
	// System.out.println(node.title);
	// System.out.println("titile:" + Arrays.toString(node.title));
	// for (Float[] fs : node.valueF) {
	// System.out.println(Arrays.toString(fs));
	// }
	//
	// }

	public static String standerString(String str) {
		if(str==null||str.equals(""))
		{
			return "null";
		}
		char[] chars = str.toCharArray();
		boolean fl = false;
		for (int i = 0; i < chars.length; i++) {
			boolean flag = Character.isDigit(chars[i]);
			if (flag) {

			} else {
				fl = true;
				break;
			}
		}
		if (fl) {
			return "\"" + str.replaceAll("\"", "\\\\\"") + "\"";
		} else {
			return str;
		}

	}

	public static String standerStringDot(String str) {
		if(str==null||str.equals(""))
		{
			return "null,";
		}
		char[] chars = str.toCharArray();
		boolean fl = false;
		for (int i = 0; i < chars.length; i++) {
			boolean flag = Character.isDigit(chars[i]);
			if (flag) {
			} else {
				fl = true;
				break;
			}
		}
		if (fl) {
			return "\"" + str.replaceAll("\"", "\\\\\"") + "\",";
		} else {
			return str + ",";
		}
	}
	/**
	 * 获取excel数据集
	 * @param filePath
	 * @return
	 */
	public ArrayList<String[]> getData(String filePath){
		ExcelNode node = ExcelUtil.readExecl("f:\\update.xls", 1, true,
				new String());
		return node.valueS;
	}

	public static void main(String[] args) {
		ExcelNode node = ExcelUtil.readExecl("f:\\update.xls", 1, true,
				new String());
		System.out.println(node.title);
		System.out.println("titile:" + Arrays.toString(node.title));
		MysqlConnection mysql = new MysqlConnection("192.168.1.4", 3306,
				"fcMysql", "root", "zjroot");
		LinkedList<String> input = new LinkedList<String>();
		HashSet<String> name=new HashSet<String>();
		for (String[] fs : node.valueS) {
			if(name.contains(fs[0]))
			{
				continue;
			}else{
				name.add(fs[0]);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("insert into BrandProperityInfo(brandName,brandTendency,brandMTendency,rank,"
					+ "companyName,companyPropertity,companySummary,score,lable,scall,brandValue,popularity,assetsType,updateDate) values(");
			sb.append(standerStringDot(fs[0])).append(standerStringDot(fs[1]))
					.append(standerStringDot(fs[2]))
					.append(standerStringDot(fs[3]))
					.append(standerStringDot(fs[4]))
					.append(standerStringDot(fs[5]))
					.append(standerStringDot(fs[6]))
					.append(standerStringDot(fs[7]))
					.append(standerStringDot(fs[8]))
					.append(standerStringDot(fs[9]))
					.append(standerStringDot(fs[10]))
					.append(standerStringDot(fs[11]))
					.append(standerStringDot(fs[12])).append("now())");
			mysql.sqlUpdate(sb.toString());
		}
	}
}
