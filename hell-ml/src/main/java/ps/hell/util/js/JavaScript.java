package ps.hell.util.js;

import java.io.File;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 通过javascript 引擎调用解析器方法
 * 相关语法使用了javascript脚本的语法 使用数学函数需要加入Math.sin等
 * @author Administrator
 *
 */
public class JavaScript {

	ScriptEngineManager manager = new ScriptEngineManager();
	ScriptEngine engine = manager.getEngineByName("javascript");

	/**
	 * 获取值
	 * @param mathScript
	 * @return
	 */
	public double getValue(String mathScript) throws Exception{
		return (Double) engine.eval(mathScript);
	}

	public void test2() {
		ScriptEngineManager manager = new ScriptEngineManager();

		ScriptEngine engine = manager.getEngineByName("javascript");
		try {
			String str = "2&1";
			Double d = (Double) engine.eval(str);
			Integer i = d.intValue();
			System.out.println(i);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}

	}

	/*
	 * 在java中调用js，jdk1.6中有加载js引擎类，然后由它来调用js方法。 并通过JDK平台给script的方法中的形参赋值
	 */
	public void test(String name) {
		ScriptEngineManager sem = new ScriptEngineManager();
		/*
		 * sem.getEngineByExtension(String extension)参数为js
		 * sem.getEngineByMimeType(String mimeType) 参数为application/javascript
		 * 或者text/javascript sem.getEngineByName(String
		 * shortName)参数为js或javascript或JavaScript
		 */
		ScriptEngine se = sem.getEngineByName("js");
		try {
			String script = "function say(){ return 'hello," + name + "';}";
			se.eval(script);
			Invocable inv2 = (Invocable) se;
			String res = (String) inv2.invokeFunction("say", name);
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * 演示如何将java中对象作为js中全局变量，同时演示将file类赋给脚本语言，并获得其属性。
	 */
	public void testScriptVariables() {
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByName("js");
		File file = new File("c://1.txt");
		engine.put("f", file);
		try {
			engine.eval("println('path:'+f.getPath())");// 无法使用alert方法
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 演示如何在java中如何通过线程来启动一个js方法
	 */
	public void testScriptInterface() throws ScriptException {
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByName("js");
		String script = "var obj=new Object();obj.run=function(){println('test thread')}";
		engine.eval(script);
		Object obj = engine.get("obj");// 获取js中对象
		Invocable inv = (Invocable) engine;
		Runnable r = inv.getInterface(obj, Runnable.class);
		Thread t = new Thread(r);
		t.start();

	}

	public static void main(String[] args) throws Exception {
		JavaScript test = new JavaScript();
		test.testScriptInterface();
		test.testScriptVariables();
		test.test2();
		test.test("asdlfjasdlf ");
		System.out.println(test.getValue("3+5*Math.sin(4)*(4*3)"));
	}
}
