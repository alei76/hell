package ps.hell.util.sql.parse2;

import java.util.ArrayList;

import ps.hell.util.sql.parse2.enum2.LinkedEnum;
import ps.hell.util.sql.parse2.select2.LinkedRelationSelect;
import ps.hell.util.sql.parse2.select2.WhereSelect;


public class WhereNode {

	/**
	 * 涉及的子类
	 */
	public ArrayList<WhereNode> nodes = new ArrayList<WhereNode>();

	public WhereSelect node = null;
	/**
	 * 之前的关系
	 */
	public LinkedRelationSelect before = null;

	/**
	 * 深度 从 1开始
	 */
	public int deep = 1;

	public int fromDeep = 1;

	private ArrayTableNode tables;

	private ArrayList<TableNode> tables_list;

	/**
	 * 输入
	 * 
	 * @param input
	 * @param deep
	 * @param fromDeep
	 */
	public WhereNode(ArrayTableNode talbes, String input, int deep, int fromDeep) {
		this.fromDeep = fromDeep;
		this.tables = talbes;
		this.deep = deep;
		init(input);
	}

	/**
	 * 输入
	 * 
	 * @param input
	 * @param deep
	 * @param fromDeep
	 */
	public WhereNode(ArrayList<TableNode> talbes, String input, int deep,
			int fromDeep) {
		this.fromDeep = fromDeep;
		this.tables_list = talbes;
		this.deep = deep;
		init(input);
	}

	/**
	 * @param tables
	 *            涉及的table
	 * @param linked
	 *            之前的条件
	 * @param input
	 *            输入数据
	 * @param deep
	 *            深度
	 * @param fromDeep
	 *            来自的深度
	 */
	public WhereNode(ArrayTableNode tables, String linked, String input,
			int deep, int fromDeep) {
		this.tables = tables;
		this.fromDeep = fromDeep;
		if (linked != null) {
			this.before = new LinkedRelationSelect(linked);
		}
		this.deep = deep;
		init(input);
	}

	/**
	 * @param tables
	 *            涉及的table
	 * @param linked
	 *            之前的条件
	 * @param input
	 *            输入数据
	 * @param deep
	 *            深度
	 * @param fromDeep
	 *            来自的深度
	 */
	public WhereNode(ArrayList<TableNode> tables, String linked, String input,
			int deep, int fromDeep) {
		this.tables_list = tables;
		this.fromDeep = fromDeep;
		if (linked != null) {
			this.before = new LinkedRelationSelect(linked);
		}
		this.deep = deep;
		init(input);
	}

	/**
	 * 初始化函数
	 * 
	 * @param input
	 */
	public void init(String input) {
		System.out.println("where:" + input);
		getParse(input);
	}

	public void getParse(String input) {
		// 获取标准分隔符
		// Pattern pattern = Pattern.compile("(\\(\\))");
		// Matcher matcher = pattern.matcher(input);
		// 需要清洗掉 input左右两侧多余的括号
		input = clearnK(input);
		char[] chars = input.toCharArray();
		LinkedEnum[] linkedValues = LinkedEnum.values();
		// 括号数量()
		int leftBracketCount = 0;
		int funcBracketCount = 0;
		StringBuffer sb = new StringBuffer();
		String conditon = null;
		char chaBefore = '1';
		boolean isIn = false;
		// 是否是'
		boolean isDot = false;
		int lcount = 0;
		for (int i = 0, len = chars.length; i < len; i++) {
			if (i > 0) {
				chaBefore = chars[i - 1];
			}
			char cha = chars[i];
			if (cha == '\\') {
				lcount++;
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
			} else if ((chaBefore != ' ' && chaBefore != '(') && cha == '('
					&& !isIn) {
				funcBracketCount++;
//				if (funcBracketCount > 0) {
					sb.append(cha);
//				}
				continue;
			} else if (cha == '(' && !isIn) {
				leftBracketCount++;
				if (leftBracketCount != 1) {
					sb.append(cha);
				}
				continue;
			} else if (cha == ')' && !isIn) {
				if (funcBracketCount > 0) {
					funcBracketCount--;
//					if (leftBracketCount >= 0) {
						sb.append(cha);
//					}
					continue;
				} else if (leftBracketCount != 1) {
					sb.append(cha);
				}
				leftBracketCount--;
			} else if (cha == ' ') {
				if (leftBracketCount > 0 || funcBracketCount > 0) {
					sb.append(cha);
					continue;
				}
				if (i + 5 > input.length()) {
				} else {

					if (input.substring(i, i + 4).equals(" in ")) {
						// System.out.println("推送:"+sb.toString());
						sb.append(" in ");
						isIn = true;
						i += 3;
						continue;
					}
					// 判断接下来的是否为 and 或者 or
					boolean flag2 = false;
					for (LinkedEnum val : linkedValues) {
						int length = val.getKey().length() + 2;
						if (input.subSequence(i, i + length).equals(
								" " + val.getKey() + " ")) {
							if (this.tables != null) {
								nodes.add(new WhereNode(this.tables, conditon,
										sb.toString(), deep + 1, deep));
							} else {
								nodes.add(new WhereNode(this.tables_list,
										conditon, sb.toString(), deep + 1, deep));
							}
							isIn = false;
							sb = new StringBuffer();
							conditon = val.getKey();
							i += length - 2;
							flag2 = true;
							break;
						}
					}
					if (flag2) {
						continue;
					}
				}
				sb.append(cha);
			} else {
				sb.append(cha);
			}
		}
		if (conditon == null) {
			if (tables != null) {
				node = new WhereSelect(tables, sb.toString());
			} else {
				node = new WhereSelect(tables_list, sb.toString());
			}
		} else {
			if (tables != null) {
				nodes.add(new WhereNode(this.tables, conditon, sb.toString(),
						deep + 1, deep));
				// System.err.println("迭代错误:"+sb.toString());
			} else {
				nodes.add(new WhereNode(this.tables_list, conditon, sb
						.toString(), deep + 1, deep));
			}
		}
	}

	/**
	 * 剔除过多的括号
	 * 
	 * @return
	 */
	public String clearnK(String input) {
		// int clearn=0;
		// int clearnK=0;
		//
		// int clearn2=0;
		// int clearnK2=0;
		// boolean fl=false;
		// for(int i=0,len=chars.length;i<len;i++){
		// if(chars[i]=='('){
		// clearn++;
		// clearnK++;
		// }else if(chars[i]==' '){
		// clearn++;
		// }else if(chars[i]==')'){
		// fl=true;
		// }else if(fl){
		// clearnK--;
		// break;
		// }
		// }
		// for(int i=chars.length-1;i>=0;i--){
		// if(chars[i]==')'){
		// clearn2++;
		// clearnK2++;
		// if(clearnK2==clearnK){
		// break;
		// }
		// }else if(chars[i]==' '){
		// clearn2++;
		// }else{
		// break;
		// }
		// }
		// System.out.println("source:"+input);
		// input=input.substring(clearn,input.length()-clearn2);
		// System.out.println("transs:"+input);
		return input;
	}

	public WhereNode() {

	}

	public String toTransIdString() {
		StringBuffer sb = new StringBuffer();
		if (nodes.size() == 0) {
			if (before != null) {
				sb.append(before.toString()).append(" ");
			}
			sb.append(node.toTransIdString()).append(" ");
		} else {
			if (before != null) {
				sb.append(before.toString()).append(" ");
			}
			if (this.deep != 1) {
				sb.append("( ");
			}
			for (WhereNode node : nodes) {
				sb.append(node.toTransIdString());
			}
			if (this.deep != 1) {
				sb.append(")");
			}
		}
		return sb.toString();
	}

	public String toTransString() {
		StringBuffer sb = new StringBuffer();
		if (nodes.size() == 0) {
			if (before != null) {
				sb.append(before.toString()).append(" ");
			}
			sb.append(node.toTransString()).append(" ");
		} else {
			if (before != null) {
				sb.append(before.toString()).append(" ");
			}
			if (this.deep != 1) {
				sb.append("( ");
			}
			for (WhereNode node : nodes) {
				sb.append(node.toTransString());
			}
			if (this.deep != 1) {
				sb.append(")");
			}
		}
		return sb.toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (nodes.size() == 0) {
			if (before != null) {
				sb.append(before.toString()).append(" ");
			}
			sb.append(node.toString()).append(" ");
		} else {
			if (before != null) {
				sb.append(before.toString()).append(" ");
			}
			if (nodes.size() > 1 && deep != 1) {
				sb.append("( ");
			}
			for (WhereNode node : nodes) {
				sb.append(node.toString());
			}
			if (nodes.size() > 1 && deep != 1) {
				sb.append(")");
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String input = "a=b and c=3 and ((e.b=c.b3 and 2=1) or e.c is not null or (c.da > d.wer and floor(e.asd,2) != 33))";
		input = "(c2.x=1) and (a.b=c.d and 2=1) or c.1=d.2";
		input = "(b.user!=0 and b.user=1) or b.user!=2 ";
		input = "a.id=b.id and b.status in ('a','b')";
		WhereNode node = new WhereNode(new ArrayList<TableNode>(), null, input,
				1, 1);
		System.out.println(node.toString());

	}

}
