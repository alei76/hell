package ps.hell.ml.nlp.own.landerbuluse.participle.ikAnalyzer;

import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.Variable;

import java.util.ArrayList;
import java.util.List;

public class IKExpressionTest {
	public static void main(String[] args) {
	 if(args.length == 0){  
	        args = new String[2];  
	        args[0] = "IK Expression";
	        args[1] ="5";
	    }  
	    //定义表达式  
	    String expression = "\"Hello World \" + 用户名 +\":\" + (3 + x) + (3 == 4) ";  
	    //给表达式中的变量 "用户名" 付上下文的值  
	    List<Variable> variables = new ArrayList<Variable>();  
	    variables.add(Variable.createVariable("用户名", args[0]));
	    variables.add(Variable.createVariable("x", Integer.parseInt(args[1])));
	    //执行表达式  
	    Object result = ExpressionEvaluator.evaluate(expression, variables);  
	    System.out.println("Result = " + result);   
	}
}
