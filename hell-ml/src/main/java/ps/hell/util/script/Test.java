package ps.hell.util.script;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
public static void main(String[] args) {
	String scriptFile="asdflasjdfljwlejf${asldfj.asdf}qwerwe";
	Pattern pattern = Pattern.compile("(\\$\\{[^\\}]*?\\})");
	Matcher matcher = pattern.matcher(scriptFile);
	int start = 0;
	while (matcher.find()) {
		String name = matcher.group(1).replaceAll("[$\\{\\}\\s]", "");
		int start2 = matcher.start();
		start = matcher.end();
		System.out.println(name);
	}
}
}
