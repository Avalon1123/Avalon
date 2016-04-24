package com.librariy.json;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A JSONArray is an ordered sequence of values. Its external form is a string
 * wrapped in square brackets with commas between the values. The internal form
 * is an object having get() and opt() methods for accessing the values by
 * index, and put() methods for adding or replacing values. The values can be
 * any of these types: Boolean, JSONArray, JSONObject, Number, String, or the
 * JSONObject.NULL object.
 * <p>
 * The constructor can convert a JSON external form string into an internal form
 * Java object. The toString() method creates an external form string.
 * <p>
 * A get() method returns a value if one can be found, and throws an exception
 * if one cannot be found. An opt() method returns a default value instead of
 * throwing an exception, and so is useful for obtaining optional values.
 * <p>
 * The generic get() and opt() methods return an object which you can cast or
 * query for type. There are also typed get() and opt() methods that do typing
 * checking and type coersion for you.
 * <p>
 * The texts produced by the toString() methods are very strict. The
 * constructors are more forgiving in the texts they will accept.
 * <ul>
 * <li>An extra comma may appear just before the closing bracket.</li>
 * <li>Strings may be quoted with single quotes.</li>
 * <li>Strings do not need to be quoted at all if they do not contain leading or
 * trailing spaces, and if they do not contain any of these characters: { } [ ]
 * / \ : ,</li>
 * <li>Numbers may have the 0- (octal) or 0x- (hex) prefix.</li>
 * </ul>
 * <p>
 * Public Domain 2002 JSON.org
 * 
 * @author JSON.org
 * @version 0.1
 */
public class JsonArray extends ArrayList {

    /**
     * The getArrayList where the JSONArray's properties are kept.
     */
    // private ArrayList myArrayList;

    /**
     * Construct an empty JSONArray.
     */
    public JsonArray() {
        // myArrayList = new ArrayList();
    }

    /**
     * Construct a JSONArray from a JSONTokener.
     * 
     * @exception ParseException
     *                A JSONArray must start with '['
     * @exception ParseException
     *                Expected a ',' or ']'
     * @param x
     *            A JSONTokener
     */
    public JsonArray(JsonTokener x){
        this();
        try {
            if (x.nextClean() != '[') {
                throw x.syntaxError("A JSONArray must start with '['");
            }
            if (x.nextClean() == ']') {
                return;
            }
            x.back();
            while (true) {
                this.add(x.nextValue());
                switch (x.nextClean()) {
                    case ',':
                        if (x.nextClean() == ']') {
                            return;
                        }
                        x.back();
                        break;
                    case ']':
                        return;
                    default:
                        throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        } catch (Exception e) {
            System.err.println("[JsonArray#Parse faild]:    "+e.getMessage()+"");
        }
    }

    /**
     * Construct a JSONArray from a source string.
     * 
     * @exception ParseException
     *                The string must conform to JSON syntax.
     * @param string
     *            A string that begins with '[' and ends with ']'.
     */
    public JsonArray(String string){
        this(new JsonTokener(string));
    }

    /**
     * Construct a JSONArray from a Collection.
     * 
     * @param collection
     *            A Collection.
     */
    public JsonArray(Collection collection) {
        super(collection);
        // myArrayList = new ArrayList(collection);
    }
    public Object opt(int index) {
        if(index<0||index>=super.size()) return null;
        return super.get(index);
    }
    /**
     * Get the boolean value associated with an index. The string values "true"
     * and "false" are converted to boolean.
     *
     * @param index
     *            subscript
     * @return The truth.
     */
    public boolean optBoolean(int index, boolean fallback) {
        try {
            Object o = opt(index);
            if (JsonObject.NULL.equals(o))
                return fallback;
            if (o instanceof Boolean) {
                return (Boolean) o;
            }
            return Boolean.parseBoolean(o.toString());
        } catch (Exception e) {
            System.out.println(JsonObject.class + "Info：optBoolean(" + index + ")失败，返回默认值！");
            return fallback;
        }
    }

    /**
     * Get the double value associated with an index.
     * @param index
     *            subscript
     * @return The value.
     */
    public double optDouble(int index, double fallback) {
        try {
            Object o = opt(index);
            if (JsonObject.NULL.equals(o))
                return fallback;
            if (o instanceof Number) {
                return ((Number) o).doubleValue();
            }
            return Double.parseDouble(o.toString());
        } catch (Exception e) {
            System.out.println(JsonObject.class + "Info：optDouble(" + index + ")失败，返回默认值！");
            return fallback;
        }
    }

    /**
     * Get the int value associated with an index.
     * @param index
     *            subscript
     * @return The value.
     */
    public int optInt(int index, int fallback){
        try {
            Object o = opt(index);
            if (JsonObject.NULL.equals(o))
                return fallback;
            if (o instanceof Number) {
                return ((Number) o).intValue();
            }
            return Integer.parseInt(o.toString());
        } catch (Exception e) {
            System.out.println(JsonObject.class + "Info：optInt(" + index + ")失败，返回默认值！");
            return fallback;
        }
    }
    /**
     * Get the int value associated with an index.
     * @param index
     *            subscript
     * @return The value.
     */
    public long optLong(int index, long fallback) {
        try {
            Object o = opt(index);
            if (JsonObject.NULL.equals(o))
                return fallback;
            if (o instanceof Number) {
                return ((Number) o).longValue();
            }
            return Long.parseLong(o.toString());
        } catch (Exception e) {
            System.out.println(JsonObject.class + "Info：optLong(" + index + ")失败，返回默认值！");
            return fallback;
        }
    }
    /**
     * Get the int value associated with an index.
     * @param index
     *            subscript
     * @return The value.
     */
    public String optString(int index, String fallback) {
        Object o = opt(index);
        if (JsonObject.NULL.equals(o))
            return fallback;
        return o.toString();
    }
    /**
     * Get the JSONArray associated with an index.
     * 
     * @exception NoSuchElementException
     *                if the index is not found or if the value is not a
     *                JSONArray
     * @param index
     *            subscript
     * @return A JSONArray value.
     */
    public JsonArray optJsonArray(int index,JsonArray fallback){
        Object o = opt(index);
        if (JsonObject.NULL.equals(o))
            return fallback;
        if (o instanceof JsonArray) {
            return (JsonArray) o;
        }else{
            return fallback;
        }
    }
    public JsonArray optFiledValues(Enum mkey) {
        return optFiledValues(mkey.name());
    }
    public JsonArray optFiledValues(String filedName) {
        JsonArray filedValues = new JsonArray();
        for (int i = 0; i < this.size(); i++) {
            Object value = this.get(i);
            if (value == null || value.equals(""))
                continue;
            if (Map.class.isAssignableFrom(value.getClass())) {
                Map valueMap = ((Map) value);
                value = valueMap.get(filedName);
            } else {
                try {
                    Field f = value.getClass().getField(filedName);
                    f.setAccessible(true);
                    value = f.get(value);
                } catch (Exception e) {
                    value=null;
                    new Exception("数组元素不存在名为[" + filedName + "]的字段,请查证！").printStackTrace();
                    break;
                }
            }
            if (value == null || value.equals(""))
                continue;
            filedValues.add(value);
        }
        return filedValues;
    }

    /**
     * Get the JSONObject associated with an index.
     * 
     * @exception NoSuchElementException
     *                if the index is not found or if the value is not a
     *                JSONObject
     * @param index
     *            subscript
     * @return A JSONObject value.
     */
    public JsonObject optJsonObject(int index,JsonObject fallback){
        Object o = opt(index);
        if (JsonObject.NULL.equals(o))
            return fallback;
        if (o instanceof JsonObject) {
            return (JsonObject) o;
        }else{
            return fallback;
        }
    }  
    public JsonObject getJsonObject(int index) throws NoSuchElementException {
        Object o = get(index);
        if (o instanceof JsonObject) {
            return (JsonObject) o;
        }
        throw new NoSuchElementException("JSONArray[" + index + "] is not a JSONObject.");
    }

    /**
     * Get the string associated with an index.
     * 
     * @exception NoSuchElementException
     * @param index
     *            subscript
     * @return A string value.
     */
    public String getString(int index) throws NoSuchElementException {
        return get(index).toString();
    }

    /**
     * Determine if the value is null.
     * 
     * @param index
     *            subscript
     * @return true if the value at the index is null, or if there is no value.
     */
    public boolean isNull(int index) {
        if (index >= size())
            return true;
        else
            return get(index) == null;
    }

    /**
     * Make a string from the contents of this JSONArray. The separator string
     * is inserted between each element. Warning: This method assumes that the
     * data structure is acyclical.
     * 
     * @param separator
     *            A string that will be inserted between the elements.
     * @return a string.
     */
    public String join(String separator) {
        int i;
        Object o;
        StringBuffer sb = new StringBuffer();
        for (i = 0; i < this.size(); i += 1) {
            if (i > 0) {
                sb.append(separator);
            }
            o = this.get(i);
            if(JsonObject.NULL.equals(o)){
                sb.append(JsonObject.NULL);
            }else if(o instanceof JsonObject){
                sb.append(o.toString());
            }else if(o instanceof JsonArray){
                sb.append(o.toString());
            }else if (o instanceof Boolean) {
                sb.append(o);
            } else if (o instanceof String) {
                sb.append(JsonObject.quote((String) o));
            } else if (o instanceof Number) {
                sb.append(JsonObject.numberToString((Number) o));
            } else if (Map.class.isAssignableFrom(o.getClass())) {
                sb.append(new JsonObject((Map) o).toString());
            } else if (Collection.class.isAssignableFrom(o.getClass())) {
                sb.append(new JsonArray((Collection) o).toString());
            } else {
                sb.append(JsonObject.quote(o.toString()));
            }
        }
        return sb.toString();
    }

    /**
     * Produce a JSONObject by combining a JSONArray of names with the values of
     * this JSONArray.
     * 
     * @param names
     *            A JSONArray containing a list of key strings. These will be
     *            paired with the values.
     * @return A JSONObject, or null if there are no names or if this JSONArray
     *         has no values.
     */
    public JsonObject toJsonObject(JsonArray names) {
        if (names == null || names.size() == 0 || this.size() == 0) {
            return null;
        }
        JsonObject jo = new JsonObject();
        for (int i = 0; i < names.size(); i += 1) {
            jo.put(names.getString(i), this.get(i));
        }
        return jo;
    }

    /**
     * Make an JSON external form string of this JSONArray. For compactness, no
     * unnecessary whitespace is added. Warning: This method assumes that the
     * data structure is acyclical.
     *
     * @return a printable, displayable, transmittable representation of the
     *         array.
     */
    public String toString() {
        return '[' + join(",") + ']';
    }
    /**
     * Make a prettyprinted JSON string of this JSONArray. Warning: This method
     * assumes that the data structure is non-cyclical.
     * 
     * @param indentFactor
     *            The number of spaces to add to each level of indentation.
     * @return a printable, displayable, transmittable representation of the
     *         object, beginning with '[' and ending with ']'.
     */
    public String toString(int indentFactor) {
        return toString(indentFactor, 0);
    }

    /**
     * Make a prettyprinted string of this JSONArray. Warning: This method
     * assumes that the data structure is non-cyclical.
     * 
     * @param indentFactor
     *            The number of spaces to add to each level of indentation.
     * @param indent
     *            The indention of the top level.
     * @return a printable, displayable, transmittable representation of the
     *         array.
     */
    String toString(int indentFactor, int indent) {
        int i;
        Object o;
        String pad = "";
        StringBuffer sb = new StringBuffer();
        indent += indentFactor;
        for (i = 0; i < indent; i += 1) {
            pad += ' ';
        }
        sb.append("[\n");
        for (i = 0; i < this.size(); i += 1) {
            if (i > 0) {
                sb.append(",\n");
            }
            sb.append(pad);
            o = this.get(i);
            if (o == null) {
                sb.append("null");
            } else if (o instanceof String) {
                sb.append(JsonObject.quote((String) o));
            } else if (o instanceof Number) {
                sb.append(JsonObject.numberToString((Number) o));
            } else if (o instanceof JsonObject) {
                sb.append(((JsonObject) o).toString(indentFactor, indent));
            } else if (o instanceof JsonArray) {
                sb.append(((JsonArray) o).toString(indentFactor, indent));
            } else {
                sb.append(o.toString());
            }
        }
        sb.append(']');
        return sb.toString();
    }
}
