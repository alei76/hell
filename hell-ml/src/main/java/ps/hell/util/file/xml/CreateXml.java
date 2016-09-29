package ps.hell.util.file.xml;

import java.io.FileOutputStream;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 使用dom4j处理数据
 * @author Administrator
 *
 */
public class CreateXml{

public static void CreateXMl1(){
	 //创建document对象
	 Document document = DocumentHelper.createDocument();
	 //定义根节点Element
	 document.addProcessingInstruction("xml-stylesheet", "type='text/xsl href='students.xsl'");
	 Element rootGen = document.addElement("root");
	 //定义根节点ROOT的子节点们
	 Element nameGen = rootGen.addElement("Name");
	 nameGen.addAttribute("name", "我是中文");
	 Element ageGen = rootGen.addElement("Age");
	 Element addrGen = rootGen.addElement("Address");
	 Writer writer = null;
	 OutputFormat format = null;
	 XMLWriter xmlwriter = null;
	 //将定义好的内容写入xml文件中
	 try {
	 //使用这个writer也可以，只不过遇到中文会乱码哦
	// writer = new FileWriter("d:/test.xml");
	 //进行格式化
	 format = OutputFormat.createPrettyPrint();
	 //设定编码
	 format.setEncoding("UTF-8");
	 xmlwriter = new XMLWriter(new FileOutputStream("./test.xml"), format);
	 xmlwriter.write(document);
	 xmlwriter.flush();
	 xmlwriter.close();
	 System.out.println("-----------Xmlfile successfully created-------------");
	 } catch (Exception e) {
	 e.printStackTrace();
	 System.out.println("-----------Exception occured during of create xmlfile -------");
	 }
	 }
public static void main(String[] args) {
	CreateXml.CreateXMl1();
}
}