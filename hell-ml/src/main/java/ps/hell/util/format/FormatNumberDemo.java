package ps.hell.util.format;
import java.text.DecimalFormat;

public class FormatNumberDemo {

        public static void main(String[] args) {
                DecimalFormat df = new DecimalFormat();
                double data = 1234.56789;
                System.out.println("格式化之前的数字: " + data);
                String style = "0.0";//定义要显示的数字的格式
                df.applyPattern(style);// 将格式应用于格式化器
                System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
                style = "00000.000 kg";//在格式后添加诸如单位等字符
                df.applyPattern(style);
                System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
                // 模式中的"#"表示如果该位存在字符，则显示字符，如果不存在，则不显示。
                style = "##000.000 kg";
                df.applyPattern(style);
                System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
                // 模式中的"-"表示输出为负数，要放在最前面
                style = "-000.000";
                df.applyPattern(style);
                System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
                // 模式中的","在数字中添加逗号，方便读数字
                style = "-0,000.0#";
                df.applyPattern(style);
                System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
                // 模式中的"E"表示输出为指数，"E"之前的字符串是底数的格式，
                // "E"之后的是字符串是指数的格式
                style = "0.00E000";
                df.applyPattern(style);
                System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
                // 模式中的"%"表示乘以100并显示为百分数，要放在最后。
                style = "0.00%";
                df.applyPattern(style);
                System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
                // 模式中的"\u2030"表示乘以1000并显示为千分数，要放在最后。
                style = "0.00\u2030";
                //在构造函数中设置数字格式
                DecimalFormat df1 = new DecimalFormat(style);
                //df.applyPattern(style);
                System.out.println("采用style: " + style + "格式化之后: " + df1.format(data));
        }
}
