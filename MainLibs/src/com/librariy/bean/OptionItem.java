package com.librariy.bean;
import com.librariy.json.JsonObject;

/**
 * 列表框通用选项对象
 * 
 * @author luchyu
 */
public class OptionItem extends JsonObject implements CharSequence {
    private static final long serialVersionUID = 1L;
    public static enum FieldKey{LABLE,VALUE}
    public OptionItem(String mLable) {
        this(mLable, null);
    }
    public OptionItem(String mLable,Object mValue) {
        if(mLable!=null){
            super.put(FieldKey.LABLE, mLable);
        }
        if(mValue==null){
           return;
        }
        if(mValue instanceof JsonObject){
            super.putAll((JsonObject)mValue);
         }else{
             super.put(FieldKey.VALUE, mValue);
         }
    }
    @Override
    public String toString() {
        return super.optString(FieldKey.LABLE, "");
    }
    @Override
    public int length() {
        return toString().length();
    }
    @Override
    public char charAt(int index) {
        return toString().charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }
    @Override 
    public synchronized int hashCode() {        
        return 0;
    }
    @Override
    public boolean equals(Object o) {
        try {
            if(o==this) return true;
            if(o==null||this==null) return false;
            if(!(o instanceof JsonObject)){
                return false;
            }
            JsonObject o1=(JsonObject)o;
            String l1=super.optString(FieldKey.LABLE, null);
            String v1=super.optString(FieldKey.VALUE, null);
            String l2=o1.optString(FieldKey.LABLE, null);
            String v2=o1.optString(FieldKey.VALUE, null);
            if(v1==null||v2==null){
                return (l1==l2)||(l1!=null&&l1.equals(l2));
            }
            if(l1==null||l2==null){
                return (v1==v2)||(v1!=null&&v1.equals(v2));
            }
            if(l1!=null&&v1!=null){
                return l1.equals(l2)&&v1.equals(v2);
            }
            return false;
        } catch (Exception e) {
            return super.equals(o);
        }
    }
    public static OptionItem create(String mLable,String json) {
        return new OptionItem(mLable,new JsonObject(json));
    }
    public static void main(String[] args) {
//        HashSet<OptionItem> l=new HashSet<OptionItem>();
//        l.add(new OptionItem("新房",2));
//        l.add(new OptionItem("二手房","4"));
//        l.add(new OptionItem("商铺",16));
//        OptionItem o=new OptionItem("二手房","4");
//        System.out.println(l.contains(new OptionItem("二手房","4")));
//        System.out.println(l.contains(new OptionItem("二手房",null)));
//        System.out.println(l.contains(new OptionItem(null,"4")));
//        System.out.println(l.contains(new OptionItem("二手房","")));
//        System.out.println(l.contains(new OptionItem("","4")));
//
//        System.out.println(o.equals(new OptionItem("二手房","4")));
//        System.out.println(o.equals(new OptionItem("二手房",null)));
//        System.out.println(o.equals(new OptionItem(null,"4")));
//        System.out.println(o.equals(new OptionItem("二手房","")));
//        System.out.println(o.equals(new OptionItem("","4")));
        
    }
}