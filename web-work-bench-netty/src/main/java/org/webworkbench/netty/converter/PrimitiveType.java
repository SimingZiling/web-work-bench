package org.webworkbench.netty.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * 原始类型
 */
public class PrimitiveType {

    /**
     * 私有构造函数，避免用户实例化
     */
    private PrimitiveType(){}

    /**
     * 基本类型
     */
    private static final Class<?>[] PRI_TYPE = {
            String.class,
            boolean.class,
            byte.class,
            short.class,
            int.class,
            long.class,
            float.class,
            double.class,
            char.class,
            Boolean.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Character.class,
            BigInteger.class,
            BigDecimal.class
    };

    /**
     * 基本数组类型
     */
    private static final Class<?>[] PRI_ARRAY_TYPE = {
            String[].class,
            boolean[].class,
            byte[].class,
            short[].class,
            int[].class,
            long[].class,
            float[].class,
            double[].class,
            char[].class,
            Boolean[].class,
            Byte[].class,
            Short[].class,
            Integer[].class,
            Long[].class,
            Float[].class,
            Double[].class,
            Character[].class,
            BigInteger[].class,
            BigDecimal[].class
    };

    /**
     * 基本类型默认值
     */
    private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>(9);
    static {
        primitiveDefaults.put(boolean.class, false);
        primitiveDefaults.put(byte.class, (byte)0);
        primitiveDefaults.put(short.class, (short)0);
        primitiveDefaults.put(char.class, (char)0);
        primitiveDefaults.put(int.class, 0);
        primitiveDefaults.put(long.class, 0L);
        primitiveDefaults.put(float.class, 0.0f);
        primitiveDefaults.put(double.class, 0.0);
    }

    /**
     * 判断是否为基本类型
     * @param cls 需要进行判断的Class对象
     * @return 是否为基本类型
     */
    public static boolean isPriType(Class<?> cls) {
        for (Class<?> priType : PRI_TYPE) {
            if (cls == priType){
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否为基本类型数组
     * @param cls 需要进行判断的Class对象
     * @return 是否为基本类型数组
     */
    public static boolean isPriArrayType(Class<?> cls) {
        for (Class<?> priType : PRI_ARRAY_TYPE) {
            if (cls == priType){
                return true;
            }
        }
        return false;
    }

    /**
     * 获得基本类型的默认值
     * @param type 基本类型的Class
     * @return 基本类型的默认值
     */
    public static Object getPriDefaultValue(Class<?> type) {
        return primitiveDefaults.get(type);
    }

}
