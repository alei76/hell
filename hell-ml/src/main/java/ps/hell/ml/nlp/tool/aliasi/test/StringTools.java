package ps.hell.ml.nlp.tool.aliasi.test;

import ps.hell.ml.nlp.tool.aliasi.spell.EditDistance;

import java.io.IOException;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * A class containing a bunch of string utilities - <br> 
 * a. filterChars: Remove extraneous characters from a string and return a 
 * "clean" string. <br> 
 * b. getSuffix: Given a file name return its extension. <br> 
 * c. fillin: pad or truncate a string to a fixed number of characters. <br> 
 * d. removeAmpersandStrings: remove strings that start with ampersand <br> 
 * e. shaDigest: Compute the 40 byte digest signature of a string <br> 
 */  
public class StringTools {  
  public static final Locale LOCALE = new Locale("en");  
  // * -- String limit for StringTools  
  private static int STRING_TOOLS_LIMIT = 1000000;  
  // *-- pre-compiled RE patterns  
  private static Pattern extPattern = Pattern.compile("^.*[.](.*?){1}quot;");  
  private static Pattern spacesPattern = Pattern.compile("\\s+");  
  private static Pattern removeAmpersandPattern = Pattern.compile("&[^;]*?;");  
  
  /** 
   * Removes non-printable spaces and replaces with a single space 
   *  
   * @param in 
   *          String with mixed characters 
   * @return String with collapsed spaces and printable characters 
   */  
  public static String filterChars(String in) {  
    return (filterChars(in, "", ' ', true));  
  }  
  
  public static String filterChars(String in, boolean newLine) {  
    return (filterChars(in, "", ' ', newLine));  
  }  
  
  public static String filterChars(String in, String badChars) {  
    return (filterChars(in, badChars, ' ', true));  
  }  
  
  public static String filterChars(String in, char replaceChar) {  
    return (filterChars(in, "", replaceChar, true));  
  }  
  
  public static String filterChars(String in, String badChars,  
      char replaceChar, boolean newLine) {  
    if (in == null)  
      return "";  
    int inLen = in.length();  
    if (inLen > STRING_TOOLS_LIMIT)  
      return in;  
    try {  
      // **-- replace non-recognizable characters with spaces  
      StringBuffer out = new StringBuffer();  
      int badLen = badChars.length();  
      for (int i = 0; i < inLen; i++) {  
        char ch = in.charAt(i);  
        if ((badLen != 0) && removeChar(ch, badChars)) {  
          ch = replaceChar;  
        } else if (!Character.isDefined(ch) && !Character.isSpaceChar(ch)) {  
          ch = replaceChar;  
        }  
        out.append(ch);  
      }  
  
      // *-- replace new lines with space  
      Matcher matcher = null;  
      in = out.toString();  
  
      // *-- replace consecutive spaces with single space and remove  
      // leading/trailing spaces  
      in = in.trim();  
      matcher = spacesPattern.matcher(in);  
      in = matcher.replaceAll(" ");  
    } catch (OutOfMemoryError e) {  
      return in;  
    }  
  
    return in;  
  }  
  
  // *-- remove any chars found in the badChars string  
  private static boolean removeChar(char ch, String badChars) {  
    if (badChars.length() == 0)  
      return false;  
    for (int i = 0; i < badChars.length(); i++) {  
      if (ch == badChars.charAt(i))  
        return true;  
    }  
    return false;  
  }  
  
  /** 
   * Return the extension of a file, if possible. 
   *  
   * @param filename 
   * @return string 
   */  
  public static String getSuffix(String filename) {  
    if (filename.length() > STRING_TOOLS_LIMIT)  
      return ("");  
    Matcher matcher = extPattern.matcher(filename);  
    if (!matcher.matches())  
      return "";  
    return (matcher.group(1).toLowerCase(LOCALE));  
  }  
  
  public static String fillin(String in, int len) {  
    return fillin(in, len, true, ' ', 3);  
  }  
  
  public static String fillin(String in, int len, char fillinChar) {  
    return fillin(in, len, true, fillinChar, 3);  
  }  
  
  public static String fillin(String in, int len, boolean right) {  
    return fillin(in, len, right, ' ', 3);  
  }  
  
  public static String fillin(String in, int len, boolean right, char fillinChar) {  
    return fillin(in, len, right, fillinChar, 3);  
  }  
  
  /** 
   * Return a string concatenated or padded to the specified length 
   *  
   * @param in 
   *          string to be truncated or padded 
   * @param len 
   *          int length for string 
   * @param right 
   *          boolean fillin from the left or right 
   * @param fillinChar 
   *          char to pad the string 
   * @param numFills 
   *          int number of characters to pad 
   * @return String of specified length 
   */  
  public static String fillin(String in, int len, boolean right,  
      char fillinChar, int numFills) {  
    // *-- return if string is of required length  
    int slen = in.length();  
    if ((slen == len) || (slen > STRING_TOOLS_LIMIT))  
      return (in);  
  
    // *-- build the fillin string  
    StringBuffer fillinStb = new StringBuffer();  
    for (int i = 0; i < numFills; i++)  
      fillinStb.append(fillinChar);  
    String fillinString = fillinStb.toString();  
  
    // *-- truncate and pad string if length exceeds required length  
    if (slen > len) {  
      if (right)  
        return (in.substring(0, len - numFills) + fillinString);  
      else  
        return (fillinString + in.substring(slen - len + numFills, slen));  
    }  
  
    // *-- pad string if length is less than required length DatabaseEntry  
    // dbe = dbt.getNextKey(); String dbkey = new String (dbe.getData());  
    StringBuffer sb = new StringBuffer();  
    if (right)  
      sb.append(in);  
    sb.append(fillinString);  
    if (!right)  
      sb.append(in);  
    return (sb.toString());  
  }  
  
  /** 
   * Remove ampersand strings such as \  
   *  
   * @param in 
   *          Text string extracted from Web pages 
   * @return String Text string without ampersand strings 
   */  
  public static String removeAmpersandStrings(String in) {  
    if (in.length() > STRING_TOOLS_LIMIT)  
      return (in);  
    Matcher matcher = removeAmpersandPattern.matcher(in);  
    return (matcher.replaceAll(""));  
  }  
  
  /** 
   * Escape back slashes 
   *  
   * @param in 
   *          Text to be escaped 
   * @return String Escaped test 
   */  
  public static String escapeText(String in) {  
    StringBuffer sb = new StringBuffer();  
    for (int i = 0; i < in.length(); i++) {  
      char ch = in.charAt(i);  
      if (ch == '\\')  
        sb.append("\\\\");  
      else  
        sb.append(ch);  
    }  
    return (sb.toString());  
  }  
  
  /** 
   * Get the SHA signature of a string 
   *  
   * @param in 
   *          String 
   * @return String SHA signature of in 
   */  
  public static String shaDigest(String in) {  
    StringBuffer out = new StringBuffer();  
    if ((in == null) || (in.length() == 0))  
      return ("");  
    try {  
      // *-- create a message digest instance and compute the hash  
      // byte array  
      MessageDigest md = MessageDigest.getInstance("SHA-1");  
      md.reset();  
      md.update(in.getBytes());  
      byte[] hash = md.digest();  
  
      // *--- Convert the hash byte array to hexadecimal format, pad  
      // hex chars with leading zeroes  
      // *--- to get a signature of consistent length (40) for all  
      // strings.  
      for (int i = 0; i < hash.length; i++) {  
        out.append(fillin(Integer.toString(0xFF & hash[i], 16), 2, false, '0',  
            1));  
      }  
    } catch (OutOfMemoryError e) {  
      return ("<-------------OUT_OF_MEMORY------------>");  
    } catch (NoSuchAlgorithmException e) {  
      return ("<------SHA digest algorithm not found--->");  
    }  
  
    return (out.toString());  
  }  
  
  /** 
   * Return the string with the first letter upper cased 
   *  
   * @param in 
   * @return String 
   */  
  public static String firstLetterUC(String in) {  
    if ((in == null) || (in.length() == 0))  
      return ("");  
    String out = in.toLowerCase(LOCALE);  
    String part1 = out.substring(0, 1);  
    String part2 = out.substring(1, in.length());  
    return (part1.toUpperCase(LOCALE) + part2.toLowerCase(LOCALE));  
  }  
  
  /** 
   * Return a pattern that can be used to collapse consecutive patterns of the 
   * same type 
   *  
   * @param entityTypes 
   *          A list of entity types 
   * @return Regex pattern for the entity types 
   */  
  public static Pattern getCollapsePattern(String[] entityTypes) {  
    Pattern collapsePattern = null;  
    StringBuffer collapseStr = new StringBuffer();  
    for (int i = 0; i < entityTypes.length; i++) {  
      collapseStr.append("(<\\/");  
      collapseStr.append(entityTypes[i]);  
      collapseStr.append(">\\s+");  
      collapseStr.append("<");  
      collapseStr.append(entityTypes[i]);  
      collapseStr.append(">)|");  
    }  
    collapsePattern = Pattern.compile(collapseStr.toString().substring(0,  
        collapseStr.length() - 1));  
    return (collapsePattern);  
  }  
  
  /** 
   * return a double that indicates the degree of similarity between two strings 
   * Use the Jaccard similarity, i.e. the ratio of A intersection B to A union B 
   *  
   * @param first 
   *          string 
   * @param second 
   *          string 
   * @return double degreee of similarity 
   */  
  public static double stringSimilarity(String first, String second) {  
    if ((first == null) || (second == null))  
      return (0.0);  
    String[] a = first.split("\\s+");  
    String[] b = second.split("\\s+");  
  
    // *-- compute a union b  
    HashSet<String> aUnionb = new HashSet<String>();  
    HashSet<String> aTokens = new HashSet<String>();  
    HashSet<String> bTokens = new HashSet<String>();  
    for (int i = 0; i < a.length; i++) {  
      aUnionb.add(a[i]);  
      aTokens.add(a[i]);  
    }  
    for (int i = 0; i < b.length; i++) {  
      aUnionb.add(b[i]);  
      bTokens.add(b[i]);  
    }  
    int sizeAunionB = aUnionb.size();  
  
    // *-- compute a intersect b  
    Iterator <String> iter = aUnionb.iterator();  
    int sizeAinterB = 0;  
    while (iter != null && iter.hasNext()) {  
      String token = (String) iter.next();  
      if (aTokens.contains(token) && bTokens.contains(token))  
        sizeAinterB++;  
    }  
    return ((sizeAunionB > 0) ? (sizeAinterB + 0.0) / sizeAunionB : 0.0);  
  }  
  
  /** 
   * Return the edit distance between the two strings 
   *  
   * @param s1 
   * @param s2 
   * @return double 
   */  
  public static double editDistance(String s1, String s2) {  
    if ((s1.length() == 0) || (s2.length() == 0))  
      return (0.0);  
    return EditDistance.editDistance(s1.subSequence(0, s1.length()), s2  
        .subSequence(0, s2.length()), false);  
  }  
  
  /** 
   * Return a string with the contents from the passed reader 
   *  
   * @param r Reader 
   * @return String 
   */  
  public static String readerToString(Reader r) {  
    int charValue;  
    StringBuffer sb = new StringBuffer(1024);  
    try {  
      while ((charValue = r.read()) != -1)  
        sb.append((char) charValue);  
    } catch (IOException ie) {  
      sb.setLength(0);  
    }  
    return (sb.toString());  
  }  
  
  /** 
   * Clean up a sentence by consecutive non-alphanumeric chars with a single 
   * non-alphanumeric char 
   *  
   * @param in Array of chars 
   * @return String 
   */  
  public static String cleanString(char[] in) {  
    int len = in.length;  
    boolean prevOK = true;  
    for (int i = 0; i < len; i++) {  
      if (Character.isLetterOrDigit(in[i]) || Character.isWhitespace(in[i]))  
        prevOK = true;  
      else {  
        if (!prevOK)  
          in[i] = ' ';  
        prevOK = false;  
      }  
    }  
    return (new String(in));  
  }  
  
  /** 
   * Return a clean file name 
   *  
   * @param filename 
   * @return String 
   */  
  public static String parseFile(String filename) {  
    return (filterChars(filename, "\\/_:."));  
  }  
} 