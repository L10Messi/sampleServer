package zgbzhyjy.sampsystem.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串扩展类
 * 
 */
public class StringUtil {

	/**
	 * 格式化字符串，使用方法举例：formatStr("sss{0}fff{1}eee", "孙", "宇")
	 * 
	 * @param s
	 *            需要格式化的字符串
	 * @param str
	 *            传入参数
	 * @return 格式化后的字符串
	 * 
	 */
	public static String formatStr(String s, String... str) {
		for (int i = 0; i < str.length; i++) {
			s = s.replace("{" + i + "}", str[i]);
		}
		return s;
	}

	public static String cutLastLetter(String str) {
		if (!isNotBlank(str)) {
			return "";
		} else {
			return str.substring(0, str.length() - 1);
		}
	}

	public static String cutFirstAndLastLetter(String str) {
		if (!isNotBlank(str) || str.length() == 1) {
			return "";
		} else {
			return str.substring(1, str.length() - 1);
		}
	}
	
	/**
	 *判断一个字符串是否为空
	 *@param string
	 *@return boolean
	 * */
	public static boolean isNotBlank(String str) {
		return str != null && !"".equals(str);
	}

	public static String formatCamel(String s, Boolean firstUpper) {
		String[] elements = s.split("_");
		for (int i = 0; i < elements.length; i++) {
			elements[i] = elements[i].toLowerCase();
			if (i != 0 || firstUpper) {
				String elem = elements[i];
				char first = elem.charAt(0);
				elements[i] = "" + (char) (first - 32) + elem.substring(1);
			}
		}
		StringBuilder builder = new StringBuilder();
		for (String item : elements) {
			builder.append(item);
		}
		return builder.toString();
	}

	/**
	 * 
	 * */
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * 将字符串转换为Integer
	 * @param String
	 * @return int
	 * */
	public static int toInt(String str) {
		if (!isNotBlank(str)) {
			return 0;
		} else {
			return Integer.parseInt(str);
		}
	}

	public static int toInt(String str, int defaultValue) {
		if (!isNotBlank(str)) {
			return defaultValue;
		} else {
			return Integer.parseInt(str);
		}
	}

	public static String list2String(List<String> list) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			buf.append(list.get(i));
		}
		return buf.toString();
	}

	/**
	 * 重写replaceAll方法，只替换全部匹配的子串
	 * @param src
	 *            整个源串
	 * @param regex
	 *            要替换的内容
	 * @param replacement
	 *            替换成什么
	 * @return
	 */
	public static String replaceAll(String src, String regex, String replacement) {
		// 生成 Pattern对象并且编译一个简单的正则表达式
		Pattern p = Pattern.compile("\\b(" + regex + ")\\b");
		// 用 Pattern 类的 matcher() 方法生成一个 Matcher 对象
		Matcher m = p.matcher(src);
		StringBuffer sb = new StringBuffer();
		// 使用 find() 方法查找第一个匹配的对象
		boolean result = m.find();
		// 使用循环将句子里所有的待替换词 找出并替换再将内容加到 sb 里
		while (result) {
			m.appendReplacement(sb, replacement);
			// 继续查找下一个匹配对象
			result = m.find();
		}
		// 最后调用 appendTail() 方法将最后一次匹配后的剩余字符串加到 sb 里；
		m.appendTail(sb);
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(replaceAll("XWBLDKYE,DKYE,1DKYE", "DKYE", "jdk"));
	}
}
