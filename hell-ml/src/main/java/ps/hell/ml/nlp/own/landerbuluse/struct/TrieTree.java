package ps.hell.ml.nlp.own.landerbuluse.struct;

import java.util.HashMap;

public class TrieTree<T> {

	public HashMap<Character,TrieTree<T>> source=new HashMap<Character,TrieTree<T>>();
	public T param=null;
	public TrieTree(int deep)
	{
	}
	public TrieTree()
	{
		
	}
	/**
	 * 添加节点
	 * @param str 对应的字符串
	 * @param param 添加参数
	 */
	public void add(String str,T param)
	{
		char[] strList=str.toCharArray();
		TrieTree<T> trie=this;
		TrieTree<T> trieTemp=null;
		int index=0;
		for(char cha:strList)
		{
			index++;
			trieTemp=trie.source.get(cha);
			if(trieTemp==null)
			{
				trieTemp=new TrieTree();
				trie.source.put(cha,trieTemp);
			}
			if(index==strList.length)
			{
				trie.source.get(cha).param=param;
			}else{
				trie=trie.source.get(cha);
			}
		}
	}
	/**
	 * 打印
	 * @param trie
	 */
	public void print(TrieTree trie)
	{
		for(int i=0;i<trie.source.size();i++)
		{
			if(trie.source.get(i)!=null)
			{
				System.out.print((char)i);
				print((TrieTree)trie.source.get(i));
				System.out.println();
			}
		}
	}
	/**
	 * 硬性匹配是否存在
	 * @param str
	 * @return
	 */
	public T getFlx(String str)
	{
		TrieTree<T> trie=this;
		for(char cha:str.toCharArray())
		{
			trie=trie.source.get(cha);
			if(trie==null)
			{
				return null;
			}
		}
		return trie.param;
	}
	
	/**
	 * 软性性匹配是否存在
	 * @param str
	 * @return
	 */
	public TrieTree get(String str)
	{
		TrieTree<T> trie=this;
		for(char cha:str.toCharArray())
		{
			trie=trie.source.get(cha);
			if(trie==null)
			{
				return null;
			}
		}
		return trie;
	}
	
	public static void main(String[] args) {
		TrieTree trie=new TrieTree(1);
//		for(int i=1;i<65535;i++)
//		{
//			trie.add(Character.toString((char)i)+Character.toString((char)i)+Character.toString((char)i));
//		}
		trie.add("asdfas","defin");
		trie.add("bse","n");
		trie.add("ase","adj");
		trie.print(trie);
		System.out.println(trie.get("asdfas"));
		System.out.println(trie.get("abs"));
		System.out.println(trie.get("bse"));
	}
}
