package ps.hell.util.sql.parse2.select2;

import java.util.ArrayList;

import ps.hell.util.sql.parse2.ArrayTableNode;
import ps.hell.util.sql.parse2.enum2.FunctionEnum;


public class ColumnSelectFunction {

	/**
	 * 函数
	 */
	private String function = null;
	/**
	 * 对应使用的列
	 */
	private String functionAlais = null;
	/**
	 * 使用的列
	 */
	public ArrayList<ColumnSelect> usedList = null;

	private String source;

	public ColumnSelectFunction(ArrayTableNode tables, String input) {
		source = input;
		System.out.println("source:" + source);
		parse(tables, input);
	}

	public void parse(ArrayTableNode tables, String input) {
		int index = input.lastIndexOf(" as ");
		int index2 = input.lastIndexOf(")");
		if (index > index2) {
			this.functionAlais = input.substring(index + 4);
			input=input.substring(0,index);
		}
		char[] chars = input.toCharArray();
		StringBuffer sb = new StringBuffer();
		// 第一个为基本括号
		// 第二个为函数括号
		int funcIndex = -1;
		FunctionEnum[] funcs = FunctionEnum.values();
		StringBuffer col = new StringBuffer();
		// 获取第一个函数或者括号
		boolean isCol = true;
		boolean isStart = false;
		int isCase = 0;
		char cha =' ';
		int lcount=0;
		boolean isDot=false;
		for (int i = 0; i < chars.length; i++) {
			cha= chars[i];
			if (!isStart && cha == ' ') {
				continue;
			}
//			System.out.println("str:" + sb.toString() + "\t:col:"
//					+ col.toString());
			isStart = true;
			boolean flag = false;
			String temp2 = input.substring(i);
			if (temp2.startsWith("case when ")) {
				isCase++;
				i += 9;
				flag=true;
				if (flag) {
					isCol = false;
					if (col.length() > 0) {
						funcIndex++;
						if (this.usedList == null) {
							this.usedList = new ArrayList<ColumnSelect>();
						}
						this.usedList.add(new ColumnSelect(tables, col.toString()));
						col = new StringBuffer();
						sb.append("{func=").append(funcIndex).append("}");
					}
				}
				sb.append("case when ");
				continue;
			} else if (temp2.startsWith(" else ") && isCase > 0) {
				i += 5;
				flag=true;
				if (flag) {
					isCol = false;
					if (col.length() > 0) {
						funcIndex++;
						if (this.usedList == null) {
							this.usedList = new ArrayList<ColumnSelect>();
						}
						this.usedList.add(new ColumnSelect(tables, col.toString()));
						col = new StringBuffer();
						sb.append("{func=").append(funcIndex).append("}");
					}
				}
				sb.append(" else ");
				continue;
			} else if (temp2.startsWith(" then ") && isCase > 0) {
				i += 5;
				flag=true;
				if (flag) {
					isCol = false;
					if (col.length() > 0) {
						funcIndex++;
						if (this.usedList == null) {
							this.usedList = new ArrayList<ColumnSelect>();
						}
						this.usedList.add(new ColumnSelect(tables, col.toString()));
						col = new StringBuffer();
						sb.append("{func=").append(funcIndex).append("}");
					}
				}
				sb.append(" then ");
				continue;
			} else if (temp2.startsWith(" when ") && isCase > 0) {
				i += 5;
				flag=true;
				if (flag) {
					isCol = false;
					if (col.length() > 0) {
						funcIndex++;
						if (this.usedList == null) {
							this.usedList = new ArrayList<ColumnSelect>();
						}
						this.usedList.add(new ColumnSelect(tables, col.toString()));
						col = new StringBuffer();
						sb.append("{func=").append(funcIndex).append("}");
					} 
				}
				sb.append(" when ");
				continue;
			} else if (temp2.startsWith(" end") && isCase > 0) {
				i += 3;
				isCase--;
				flag=true;
				if (flag) {
					isCol = false;
					if (col.length() > 0) {
						funcIndex++;
						if (this.usedList == null) {
							this.usedList = new ArrayList<ColumnSelect>();
						}
						this.usedList.add(new ColumnSelect(tables, col.toString()));
						col = new StringBuffer();
						sb.append("{func=").append(funcIndex).append("}");
					}
				}
				sb.append(" end ");
				continue;
			}
			//测试'' 及,号
			if (cha == '\\') {
				lcount++;
				sb.append(cha);
				continue;
			} else {
				lcount = 0;
			}
			if (cha == '\'') {
				if (lcount % 2 ==0) {
					// 如果为单个
					if (isDot) {
						isDot = false;
					} else {
						isDot = true;
					}
				}
				sb.append(cha);
				continue;
			} else if (isDot) {
				sb.append(cha);
				continue;
			}
			if (!flag) {
				for (FunctionEnum enu : funcs) {
					if (cha == enu.getKey()) {
						// 则为非标准值
						flag = true;
						break;
					}
				}
				if (flag && cha == '(' && col.length() > 0) {
					sb.append(col.toString()).append("(");
					col = new StringBuffer();
					continue;
				}
			}
			System.out.println("flag:"+flag);
			if (flag) {
				isCol = false;
				if (col.length() > 0) {
					funcIndex++;
					if (this.usedList == null) {
						this.usedList = new ArrayList<ColumnSelect>();
					}
					this.usedList.add(new ColumnSelect(tables, col.toString()));
					col = new StringBuffer();
					sb.append("{func=").append(funcIndex).append("}");
					sb.append(cha);
				} else {
					sb.append(cha);
				}
				continue;
			}
			isCol = true;
			col.append(cha);
		}
		if(col.length()>0){
			funcIndex++;
			if (this.usedList == null) {
				this.usedList = new ArrayList<ColumnSelect>();
			}
			this.usedList.add(new ColumnSelect(tables, col.toString()));
			col = new StringBuffer();
			sb.append("{func=").append(funcIndex).append("}");
			System.out.println("an:"+sb.toString());
		}
		this.function = sb.toString();
		System.out.println("function:"+this.function);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		String sz = function;
		for (int i = 0; i < usedList.size(); i++) {
			sz = sz.replaceAll("\\{func=" + i + "\\}", usedList.get(i)
					.toString());
		}
		sb.append(sz);
		if (this.functionAlais != null) {
			sb.append(" as ").append(this.functionAlais);
		}
		return sb.toString();
	}

	public String toTransIdString() {
		StringBuffer sb = new StringBuffer();
		String sz = function;
		for (int i = 0; i < usedList.size(); i++) {
			sz = sz.replaceAll("\\{func=" + i + "\\}", usedList.get(i)
					.toTransIdString());
		}
		sb.append(sz);
		if (this.functionAlais != null) {
			sb.append(" as ").append(this.functionAlais);
		}
		return sb.toString();
	}

	public String toFuncIdString() {
		StringBuffer sb = new StringBuffer();
		sb.append(function);
		if (this.functionAlais != null) {
			sb.append(" as ").append(this.functionAlais);
		}
		return sb.toString();
	}

	public String toTransString() {
		StringBuffer sb = new StringBuffer();
		String sz = function;
		for (int i = 0; i < usedList.size(); i++) {
			sz = sz.replaceAll("\\{func=" + i + "\\}", usedList.get(i)
					.toTransString());
		}
		sb.append(sz);
		if (this.functionAlais != null) {
			sb.append(" as ").append(this.functionAlais);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String input = "test(a.user_id) as e1";
		input = "test(a+b- (case when a=3 then 2 else a end))/te2(a)";
		input ="concat('fs\\(user_id\\)dasd%',test(user_id)/case when user_id ='abc' then 'a' else user_id end,',')";
		ColumnSelectFunction select = new ColumnSelectFunction(
				new ArrayTableNode(1), input);
		System.out.println("toString:"+select);

		System.out.println("toFunc:"+select.toFuncIdString());
		
		System.out.println("toString:"+select.toTransIdString());

	}
}
