package com.xxl.job.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxl.job.core.biz.model.ReturnT;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Gson工具类，提供了一系列静态方法用于简化Gson库的操作，
 * 包括对象与JSON字符串的相互转换，以及JSON到特定集合类型的解析。
 *
 * @author xuxueli 2020-04-11 20:56:31
 */
public class GsonTool {

    private static Gson gson = null;

    static {
        // 初始化Gson实例，设置日期格式
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    /**
     * 将给定的对象转换为JSON格式的字符串。
     *
     * @param src 要转换的对象。
     *
     * @return 对象的JSON字符串表示。
     */
    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    /**
     * 将JSON字符串转换为指定类型的对象。
     *
     * @param <T>      目标对象的类型。
     * @param json     JSON格式的字符串。
     * @param classOfT 要转换成的目标类的Class对象。
     *
     * @return 解析后的对象实例。
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * 将JSON字符串转换为具有泛型参数的指定类型对象。
     *
     * @param <T>         目标对象的类型。
     * @param json        JSON格式的字符串。
     * @param classOfT    泛型类的Class对象。
     * @param argClassOfT 泛型参数的Class对象。
     *
     * @return 解析后的对象实例。
     */
    public static <T, ArgT> T fromJson(String json, Class<T> classOfT, Class<ArgT> argClassOfT) {
        Type type = new ParameterizedType4T(classOfT, new Class[]{argClassOfT});
        return gson.fromJson(json, type);
    }

    /**
     * 辅助类，用于构建泛型参数类型信息。
     */
    public static class ParameterizedType4T implements ParameterizedType {
        private final Class<?> raw;
        private final Type[] args;

        public ParameterizedType4T(Class<?> raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    /**
     * 将JSON字符串转换为指定类型的List集合。
     *
     * @param <T>      集合元素的类型。
     * @param json     JSON格式的字符串。
     * @param classOfT List中元素的Class对象。
     *
     * @return 解析后的List实例。
     */
    public static <T> List<T> fromJsonList(String json, Class<T> classOfT) {
        ParameterizedType4T type = new ParameterizedType4T(List.class, new Type[]{classOfT});
        return gson.fromJson(json, type);
    }

    /**
     * 将JSON字符串转换为指定键值类型的Map集合。
     *
     * @param <K>      Map的键类型。
     * @param <V>      Map的值类型。
     * @param json     JSON格式的字符串。
     * @param classOfK Map键的Class对象。
     * @param classOfV Map值的Class对象。
     *
     * @return 解析后的Map实例。
     */
    public static <K, V> Map<K, V> fromJsonMap(String json, Class<K> classOfK, Class<V> classOfV) {
        Type mapType = new ParameterizedType4T(Map.class, new Type[]{classOfK, classOfV});
        return gson.fromJson(json, mapType);
    }

    /**
     * 反序列化JSON字符串为ReturnT<T>类型实例。
     *
     * @param json     待反序列化的JSON字符串
     * @param classOfT 实际泛型参数的类型
     * @param <T>      泛型参数类型
     *
     * @return 返回类型为ReturnT<T>的实例
     */
    public static <T> ReturnT<T> fromJsonReturnT(String json, Class<T> classOfT) {
        ParameterizedType4T type = new ParameterizedType4T(ReturnT.class, new Type[]{classOfT});
        return gson.fromJson(json, type);
    }
}
