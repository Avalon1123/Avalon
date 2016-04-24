package com.librariy.json;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/***
 * 异步对象的转换
 * 
 * @author just
 */
public class JsonObject extends LinkedHashMap<String, Object>{
    private static final long serialVersionUID = 1000L;
    public static boolean encoding = false;
    public long BaselineTime = System.currentTimeMillis();

    /**
     * JSONObject.NULL is equivalent to the value that JavaScript calls null,
     * whilst Java's null is equivalent to the value that JavaScript calls
     * undefined.
     */
    public static final class Null {

        /**
         * Make a Null object.
         */
        private Null() {
        }

        /**
         * There is only intended to be a single instance of the NULL object, so
         * the clone method returns itself.
         * 
         * @return NULL.
         */
        protected final Object clone() {
            return this;
        }

        /**
         * A Null object is equal to the null value and to itself.
         * 
         * @param object
         *            An object to test for nullness.
         * @return true if the object parameter is the JSONObject.NULL object or
         *         null.
         */
        public boolean equals(Object object) {
            return object == null || object == this;
        }

        /**
         * Get the "null" string value.
         * 
         * @return The string "null".
         */
        public String toString() {
            return "null";
        }
    }

    /**
     * The hash map where the JSONObject's properties are kept.
     */
    // private Map myHashMap;

    /**
     * It is sometimes more convenient and less ambiguous to have a NULL object
     * than to use Java's null value. JSONObject.NULL.equals(null) returns true.
     * JSONObject.NULL.toString() returns "null".
     */
    public static final Object NULL = new Null();

    /**
     * Construct an empty JSONObject.
     */
    public JsonObject() {
        Collections.synchronizedMap(this);
    }

    public JsonObject(Map bean) {
        super(bean);
        Collections.synchronizedMap(this);
    }

    /**
     * Construct a JSONObject from a JSONTokener.
     * 
     * @throws ParseException
     *             if there is a syntax error in the source string.
     * @param x
     *            A JSONTokener object containing the source string.
     */
    public JsonObject(JsonTokener x) {
        this();
        try {
            char c;
            String key;
            if (x.next() == '%') {
                x.unescape();
            }
            x.back();
            if (x.nextClean() != '{') {
                throw x.syntaxError("A JSONObject must begin with '{'");
            }
            while (true) {
                c = x.nextClean();
                switch (c) {
                    case 0:
                        throw x.syntaxError("A JSONObject must end with '}'");
                    case '}':
                        return;
                    default:
                        x.back();
                        key = x.nextValue().toString();
                }
                if (x.nextClean() != ':') {
                    throw x.syntaxError("Expected a ':' after a key");
                }
                this.put(key, x.nextValue());
                switch (x.nextClean()) {
                    case ',':
                        if (x.nextClean() == '}') {
                            return;
                        }
                        x.back();
                        break;
                    case '}':
                        return;
                    default:
                        throw x.syntaxError("Expected a ',' or '}'");
                }
            }
        } catch (ParseException e) {
            System.err.println("[JsonObject#Parse faild]:    " + e.getMessage() + "");
        }
    }

    /**
     * Construct a JSONObject from a string.
     * 
     * @exception ParseException
     *                The string must be properly formatted.
     * @param string
     *            A string beginning with '{' and ending with '}'.
     */
    public JsonObject(String string) {
        this(new JsonTokener(string));
    }

    /**
     * Accumulate values under a key. It is similar to the put method except
     * that if there is already an object stored under the key then a JSONArray
     * is stored under the key to hold all of the accumulated values. If there
     * is already a JSONArray, then the new value is appended to it. In
     * contrast, the put method replaces the previous value.
     * 
     * @throws NullPointerException
     *             if the key is null
     * @param key
     *            A key string.
     * @param value
     *            An object to be accumulated under the key.
     * @return this.
     */
    public JsonObject accumulate(String key, Object value) throws NullPointerException {
        JsonArray a;
        Object o = get(key);
        if (o == null) {
            put(key, value);
        } else if (o instanceof JsonArray) {
            a = (JsonArray) o;
            a.add(value);
        } else {
            a = new JsonArray();
            a.add(o);
            a.add(value);
            put(key, a);
        }
        return this;
    }

    public Object put(Enum key, Object value) {
        if (key == null)
            return null;
        return this.put(key.name(), value);
    }

    public Object put(String key, Object value) {
        if (key == null)
            return null;
        return super.put(key, value);
    }

    /**
     * Get the boolean value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return The boolean value.
     */
    public boolean optBoolean(Enum mkey, boolean fallback) {
        return this.optBoolean(mkey.name(), fallback);
    }

    public boolean optBoolean(String key, boolean fallback) {
        try {
            Object o = get(key);
            if (JsonObject.NULL.equals(o))
                return fallback;
            if (o instanceof Boolean) {
                return (Boolean) o;
            }
            return Boolean.parseBoolean(o.toString());
        } catch (Exception e) {
            System.out.println(JsonObject.class + "Info：optBoolean(" + key + ")失败，返回默认值！");
            return fallback;
        }
    }

    /**
     * Get the int value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return The int value.
     */
    public int optInt(Enum mkey, int fallback) {
        return this.optInt(mkey.name(), fallback);
    }

    public int optInt(String key, int fallback) {
        try {
            Object o = get(key);
            if (JsonObject.NULL.equals(o))
                return fallback;
            if (o instanceof Number) {
                return ((Number) o).intValue();
            }
            return Integer.parseInt(o.toString());
        } catch (Exception e) {
            System.out.println(JsonObject.class + "Info：optInt(" + key + ")失败，返回默认值！");
            return fallback;
        }
    }

    /**
     * Get the long value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return The long value.
     */
    public long optLong(Enum mkey, long fallback) {
        return this.optLong(mkey.name(), fallback);
    }

    public long optLong(String key, long fallback) {
        try {
            Object o = get(key);
            if (JsonObject.NULL.equals(o))
                return fallback;
            if (o instanceof Number) {
                return ((Number) o).longValue();
            }
            return Long.parseLong(o.toString());
        } catch (Exception e) {
            System.out.println(JsonObject.class + "Info：optLong(" + key + ")失败，返回默认值！");
            return fallback;
        }
    }

    /**
     * Get the double value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return The double value.
     */
    public double optDouble(Enum mkey, double fallback) {
        return this.optDouble(mkey.name(), fallback);
    }

    public double optDouble(String key, double fallback) {
        try {
            Object o = get(key);
            if (JsonObject.NULL.equals(o))
                return fallback;
            if (o instanceof Number) {
                return ((Number) o).doubleValue();
            }
            return Double.parseDouble(o.toString());
        } catch (Exception e) {
            System.out.println(JsonObject.class + "Info：optBoolean(" + key + ")失败，返回默认值！");
            return fallback;
        }
    }

    /**
     * Get the String value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return The String value.
     */
    public String optString(Enum mkey, String fallback) {
        return this.optString(mkey.name(), fallback);
    }

    public String optString(String key, String fallback) {
        Object o = get(key);
        if (JsonObject.NULL.equals(o))
            return fallback;
        return o.toString();
    }

    /**
     * Get the String[] value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return The String[] value.
     */
    public String[] optSplitArray(Enum mkey, String splitString) {
        return this.optSplitArray(mkey.name(), splitString);
    }

    public String[] optSplitArray(String key, String splitString) {
        String ret = optString(key, "");
        return (ret == null || ret.equals("")) ? new String[0] : ret.split(splitString);
    }

    /**
     * Get the JSONObject value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return A JSONObject which is the value.
     */
    public JsonObject optJsonObject(Enum mkey, JsonObject fallback) {
        return this.optJsonObject(mkey.name(), fallback);
    }

    public JsonObject optJsonObject(String key, JsonObject fallback) {
        Object o = get(key);
        if (JsonObject.NULL.equals(o))
            return fallback;
        if (o instanceof JsonObject) {
            return (JsonObject) o;
        } else {
            return fallback;
        }
    }

    /**
     * Get the JSONArray value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return A JSONArray which is the value.
     */
    public JsonArray optJsonArray(Enum mkey, JsonArray fallback) {
        return this.optJsonArray(mkey.name(), fallback);
    }

    public JsonArray optJsonArray(String key, JsonArray fallback) {
        Object o = get(key);
        if (JsonObject.NULL.equals(o)) {
            return fallback;
        } else if (o instanceof JsonArray) {
            return (JsonArray) o;
        } else {
            JsonArray o1 = new JsonArray();
            o1.add(o);
            return o1;
        }
    }

    /**
     * Determine if the JSONObject contains a specific key.
     * 
     * @param key
     *            A key string.
     * @return true if the key exists in the JSONObject.
     */
    public boolean has(String key) {
        return this.containsKey(key);
    }

    /**
     * Determine if the value associated with the key is null or if there is no
     * value.
     * 
     * @param key
     *            A key string.
     * @return true if there is no value associated with the key or if the value
     *         is the JSONObject.NULL object.
     */
    public boolean isNull(String key) {
        if (super.containsKey(key))
            return true;
        else
            return JsonObject.NULL.equals(super.get(key));
    }

    /**
     * Produce a JSONArray containing the names of the elements of this
     * JSONObject.
     * 
     * @return A JSONArray containing the key strings, or null if the JSONObject
     *         is empty.
     */
    public JsonArray names() {
        JsonArray ja = new JsonArray();
        Iterator keys = this.keySet().iterator();
        while (keys.hasNext()) {
            ja.add(keys.next());
        }
        if (ja.size() == 0) {
            return null;
        }
        return ja;
    }

    /**
     * Produce a string from a number.
     * 
     * @exception ArithmeticException
     *                JSON can only serialize finite numbers.
     * @param n
     *            A Number
     * @return A String.
     */
    public static String numberToString(Number n) throws ArithmeticException {
        if ((n instanceof Float && (((Float) n).isInfinite() || ((Float) n).isNaN())) || (n instanceof Double && (((Double) n).isInfinite() || ((Double) n).isNaN()))) {
            throw new ArithmeticException("JSON can only serialize finite numbers.");
        }
        // Shave off trailing zeros and decimal point, if possible.
        String s = n.toString().toLowerCase();
        if (s.indexOf('e') < 0 && s.indexOf('.') > 0) {
            while (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    /**
     * Produce a string in double quotes with backslash sequences in all the
     * right places.
     * 
     * @param string
     *            A String
     * @return A String correctly formatted for insertion in a JSON message.
     */
    public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }

        char c;
        int i;
        int len = string.length();
        StringBuffer sb = new StringBuffer(len + 4);
        String t;

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                case '/':
                    if (JsonObject.encoding)
                        sb.append('\\');
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (!JsonObject.encoding) {
                        sb.append(c);
                    } else if (c < ' ' || c >= 128) {
                        t = "000" + Integer.toHexString(c);
                        sb.append("\\u" + t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
        return sb.toString();
    }

    /**
     * Produce a JSONArray containing the values of the members of this
     * JSONObject.
     * 
     * @param names
     *            A JSONArray containing a list of key strings. This determines
     *            the sequence of the values in the result.
     * @return A JSONArray of values.
     */
    public JsonArray toJsonArray(JsonArray names) {
        if (names == null || names.size() == 0) {
            return null;
        }
        JsonArray ja = new JsonArray();
        for (int i = 0; i < names.size(); i += 1) {
            ja.add(this.get(names.getString(i)));
        }
        return ja;
    }

    /**
     * Make an JSON external form string of this JSONObject. For compactness, no
     * unnecessary whitespace is added.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     * 
     * @return a printable, displayable, portable, transmittable representation
     *         of the object, beginning with '{' and ending with '}'.
     */
    public String toString() {
        Iterator keys = super.keySet().iterator();
        Object o = null;
        String s;
        StringBuffer sb = new StringBuffer();

        sb.append('{');
        while (keys.hasNext()) {
            if (o != null) {
                sb.append(',');
            }
            s = keys.next().toString();
            if(s==null) continue;
            o = this.get(s);            
            sb.append(quote(s));
            sb.append(':');
            if(JsonObject.NULL.equals(o)){
                sb.append(JsonObject.NULL);
            }else if(o instanceof JsonObject){
                sb.append(o.toString());
            }else if(o instanceof JsonArray){
                sb.append(o.toString());
            }else if (o instanceof Boolean) {
                sb.append(o);
            } else if (o instanceof String) {
                sb.append(quote((String) o));
            } else if (o instanceof Number) {
                sb.append(numberToString((Number) o));
            } else if (Map.class.isAssignableFrom(o.getClass())) {
                sb.append(new JsonObject((Map) o).toString());
            } else if (Collection.class.isAssignableFrom(o.getClass())) {
                sb.append(new JsonArray((Collection) o).toString());
            } else {
                sb.append(quote(o.toString()));
            }
        }
        sb.append('}');
        return sb.toString();
    }

    /**
     * Make a prettyprinted JSON external form string of this JSONObject.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     * 
     * @param indentFactor
     *            The number of spaces to add to each level of indentation.
     * @return a printable, displayable, portable, transmittable representation
     *         of the object, beginning with '{' and ending with '}'.
     */
    public String toString(int indentFactor) {
        return toString(indentFactor, 0);
    }

    /**
     * Make a prettyprinted JSON string of this JSONObject.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     * 
     * @param indentFactor
     *            The number of spaces to add to each level of indentation.
     * @param indent
     *            The indentation of the top level.
     * @return a printable, displayable, transmittable representation of the
     *         object, beginning with '{' and ending with '}'.
     */
    String toString(int indentFactor, int indent) {
        int i;
        Iterator keys = super.keySet().iterator();
        String pad = "";
        StringBuffer sb = new StringBuffer();
        indent += indentFactor;
        for (i = 0; i < indent; i += 1) {
            pad += ' ';
        }
        sb.append("{\n");
        while (keys.hasNext()) {
            String s = keys.next().toString();
            Object o = this.get(s);
            if (o != null) {
                if (sb.length() > 2) {
                    sb.append(",\n");
                }
                sb.append(pad);
                sb.append(quote(s));
                sb.append(": ");
                if (o instanceof Boolean) {
                    sb.append(o);
                } else if (o instanceof String) {
                    sb.append(quote((String) o));
                } else if (o instanceof Number) {
                    sb.append(numberToString((Number) o));
                } else if (o instanceof JsonObject) {
                    sb.append(((JsonObject) o).toString(indentFactor, indent));
                } else if (o instanceof JsonArray) {
                    sb.append(((JsonArray) o).toString(indentFactor, indent));
                } else {
                    sb.append(quote(o.toString()));
                }
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
