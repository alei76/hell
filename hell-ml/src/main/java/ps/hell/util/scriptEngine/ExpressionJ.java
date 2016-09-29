package ps.hell.util.scriptEngine;

import org.da.expressionj.expr.parser.EquationParser;
import org.da.expressionj.expr.parser.ParseException;
import org.da.expressionj.model.Equation;
import org.da.expressionj.model.Expression;
import org.da.expressionj.util.ExpressionCombiner;

public class ExpressionJ {
public static void main(String[] args) throws ParseException {
	   Equation condition = EquationParser.parse("sin(a)");
	   
       Equation condition2 = EquationParser.parse("b + c");
       Expression expr = condition2.getExpression();

       ExpressionCombiner combiner = new ExpressionCombiner();
       Expression exprResult = combiner.replace(condition, "a", expr);

       exprResult.getVariable("b").setValue(1);
       exprResult.getVariable("c").setValue(2);

       System.out.println(condition.eval());
}
}
