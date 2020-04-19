package com.example.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.parseObject;

public class JsonUtils {

    private static Gson mGson = new Gson();
    /**
     * http 请求数据返回 json 中中文字符为 unicode 编码转汉字转码
     *
     * @param theString
     * @return 转化后的结果.
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    /**
     * 格式化json字符串
     *
     * @param jsonStr 需要格式化的json串
     * @return 格式化后的json串
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
            //遇到{ [换行，且下一行缩进
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                //遇到} ]换行，当前行缩进
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                //遇到,换行
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
     * 将json字符串转化成实体对象
     */
    public static <T> T stringToObject(String json, Class<T> classOfT) {
        return mGson.fromJson(json, classOfT);
    }

    /**
     * 将对象准换为json字符串 或者 把list 转化成json
     */
    public static <T> String objectToString(T object) {
        return mGson.toJson(object);
    }

    /**
     * 把json 字符串转化成list
     */
    public static <T> List<T> stringToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

    /**
     * json解析为泛型对象
     */
    public static <T> T stringToObjectByType(String str, Type type) {
        try {
            return mGson.fromJson(str, type);
        } catch (JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static String encodeObjectToJson(Object object) {
        return URLEncoder.encode(objectToJson(object));
    }

    public static <T> T decodeObjectFromJson(String str, Class<T> clazz) {
        return jsonToObject(URLDecoder.decode(str), clazz);
    }

    /**
     * 对象转化为json
     *
     * @param object 对象
     * @return Json字符串
     */
    public static String objectToJson(Object object) {
        if (object == null) {
            return "";
        }
        try {
            return JSON.toJSONString(object);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 对象转化为json
     *
     * @param object        对象
     * @param excludeFields 排除的字段
     * @return Json字符串
     */
    public static String objectToJson(Object object, String... excludeFields) {
        if (object == null) {
            return "";
        }
        try {
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            if (excludeFields != null) {
                for (String field : excludeFields) {
                    filter.getExcludes().add(field);
                }
            }
            return JSON.toJSONString(object, filter);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * json转化为对象
     *
     * @param json  字符串
     * @param clazz 需要转化的对象类型
     * @return 对象
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return parseObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转化为对象
     *
     * @param json  字符串
     * @param clazz 需要转化的对象类型
     * @return 对象
     */
    public static <T> List jsonToArray(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return parseArray(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * json to map
     *
     * @param object jsonObject
     * @return map
     */
    public static HashMap<String, Object> objToHashMap(JSONObject object) {
        if (object == null) return null;
        String jsonData = object.toString();
        HashMap<String, Object> objectHashMap = null;
        try {
            if (!TextUtils.isEmpty(jsonData)) {
                objectHashMap = parseObject(jsonData, new TypeReference<HashMap<String, Object>>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectHashMap;
    }

    /**
     * json to list
     *
     * @return list
     */
    public static ArrayList jsonArrayToArrayList(org.json.JSONArray resources) {
        if (resources == null) {
            return null;
        }
        String jsonData = resources.toString();
        ArrayList arrayList = null;
        try {
            if (!TextUtils.isEmpty(jsonData)) {
                arrayList = parseObject(jsonData, new TypeReference<ArrayList>() {
                });

            }
        } catch (Exception e) {
        }
        return arrayList;
    }

    /**
     *json转化为Map  fastjson 使用方式
     *//*
	public static HashMap jsonToMapForForGson(String jsonData){
		if (TextUtils.isEmpty(jsonData)) {
			return null;
		}
		HashMap map = null;
		try {
			Type listType = new TypeToken<HashMap>() {
			}.getType();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			map = gson.fromJson(jsonData, listType);
		} catch (Exception e) {
		}
		return map;
	}*/

}
