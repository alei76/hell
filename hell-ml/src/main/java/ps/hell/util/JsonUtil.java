package ps.hell.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * json (C)
 */
public class JsonUtil {
	private static Logger log = LoggerFactory.getLogger(JsonUtil.class);
	public static ObjectMapper mapper = new ObjectMapper();
	public static XmlMapper xml = new XmlMapper();
	static {
		// mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING,true);
	}

	/**
	 * 
	 * 
	 * @param jsonObjStr
	 *            e.g. {'name':'get','dateAttr':'2009-11-12'}
	 * @param clazz
	 *            Person.class
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getDtoFromJsonObjStr(String jsonObjStr, Class clazz) {
		try {
			return mapper.readValue(jsonObjStr, clazz);
		} catch (Exception e) {
			log.error("json:" + jsonObjStr + ",clazz:" + clazz + ",exception:"
					+ e.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * 
	 * @param collectionClass
	 *            e.g. {'data':[{'name':'get'},{'name':'set'}]}
	 * @param elementClasses
	 *            e.g. MyBean.class
	 * @return Object
	 */
	// @SuppressWarnings("rawtypes")
	// public static Object getDtoFromJsonObjStr(String jsonObjStr, Class clazz,
	// Map classMap) {
	// try{
	// return mapper.readVa(jsonObjStr, clazz,classMap);
	// }catch(Exception e){
	// log.error("json:"+jsonObjStr+",clazz:"+clazz+",exception:"+e.getMessage());
	// return null;
	// }
	//
	// }

	public static JavaType getCollectionType(Class<?> collectionClass,
			Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass,
				elementClasses);
	}

	/**
	 * 
	 * 
	 * @param jsonArrStr
	 *            e.g. ['get',1,true,null]
	 * @return Object[]
	 */
	public static <T> T[] getArrFromJsonArrStr(String jsonArrStr, Class clazz) {
		try {
			JavaType javaType = getCollectionType(ArrayList.class, clazz);
			return mapper.readValue(jsonArrStr, javaType);
		} catch (Exception e) {
			log.error("json:" + jsonArrStr + ",exception:" + e.getMessage());
			return null;
		}

	}

	@SuppressWarnings("rawtypes")
	public static <T> T getDtoArrFromJsonArrListStr(String jsonArrStr,
			Class clazz) {
		try {
			JavaType javaType = getCollectionType(ArrayList.class, clazz);
			return mapper.readValue(jsonArrStr, javaType);
		} catch (Exception e) {
			log.error("json:" + jsonArrStr + ",clazz:" + clazz + ",exception:"
					+ e.getMessage());
			return null;
		}

	}

	/**
	 *
	 * 
	 * @param jsonObjStr
	 *            e.g. {'name':'get','int':1,'double',1.1,'null':null}
	 * @return Map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getMapFromJsonObjStr(String jsonObjStr) {

		try {
			return mapper.readValue(jsonObjStr, Map.class);
		} catch (Exception e) {
			log.error("json:" + jsonObjStr + ",exception:" + e.getMessage());
			return null;
		}
	}

	/**
	 *
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 *             String
	 */
	public static String getJsonStr(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * http://json-lib
	 * .sourceforge.net/apidocs/net/sf/json/xml/XMLSerializer.html ����ʵ����ο���
	 * http://json-lib.sourceforge.net/xref-test/net/sf/json/xml/
	 * TestXMLSerializer_writes.html
	 * http://json-lib.sourceforge.net/xref-test/net
	 * /sf/json/xml/TestXMLSerializer_writes.html
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 *             String
	 */
	public static String getXMLFromObj(Object obj) {
		StringWriter sw = new StringWriter();
		try {
			xml.writeValue(sw, obj);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	}

	/**
	 * 解析字符串
	 * 
	 * @param str
	 * @return
	 */
	public static ObjectNode parse(String str) {
		try {
			return (ObjectNode)mapper.readTree(str);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 对象转换为 node
	 * @param obj
	 * @return ObjectNode ArrayNode
	 */
	public static Object parse(Object obj){
		return mapper.valueToTree(obj);
	}

	/**
	 * 解析字符串
	 *
	 * @return
	 */
	public static ObjectNode init() {
		try {
			return mapper.createObjectNode();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析字符串
	 *
	 * @return
	 */
	public static ArrayNode initArray() {
		try {
			return mapper.createArrayNode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	   /**
     * 格式化
     * @param jsonStr
     * @return
     * @author   lizhgb
     * @Date   2015-10-14 下午1:17:35
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
 
        return sb.toString();
    }
 
    /**
     * 添加space
     * @param sb
     * @param indent
     * @author   lizhgb
     * @Date   2015-10-14 上午10:38:04
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

	public static void main(String[] args) {
		ObjectNode node=JsonUtil.init();
		node.put("nodekey1", 1);
		ArrayNode node2=JsonUtil.initArray();
		node2.add("a");
		node2.add(3);
		node.replace("test",node2);
		System.out.println(JsonUtil.getJsonStr(node));
		
		//System.out.println(JsonUtil.getJsonStr(new test2()));
		//System.out.println(JsonUtil.getJsonStr(JsonUtil.getDtoFromJsonObjStr(JsonUtil.getJsonStr(new test2()),test2.class)));
		
		
		try  
	    {  
	      String str = "{\"data\":{\"birth_day\":7,\"birth_month\":6},\"errcode\":0,\"msg\":\"ok\",\"ret\":0}";  
	  
	      ObjectMapper mapper = new ObjectMapper();  
	      JsonNode root = mapper.readTree(str);  
	  
	      JsonNode data = root.path("data");  
	  
	      JsonNode birth_day = data.path("birth_day");  
	      System.out.println(birth_day.asInt());  
	        
	      JsonNode birth_month = data.path("birth_month");  
	      System.out.println(birth_month.asInt());  
	  
	      JsonNode msg = root.path("msg");  
	      System.out.println(msg.textValue());
	      ((ObjectNode)root).set("tes2",(ArrayNode)JsonUtil.parse(new String[]{"a","b","c"}));
	      System.out.println(root.toString());
	    }  
	    catch (IOException e)  
	    {  
	    }  
	}

}
