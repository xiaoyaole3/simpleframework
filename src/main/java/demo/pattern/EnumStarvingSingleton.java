package demo.pattern;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EnumStarvingSingleton {
    private EnumStarvingSingleton() {}

    public static EnumStarvingSingleton getInstance() {
        return ContainerHolder.HOLDER.instance;
    }

    private enum ContainerHolder {
        HOLDER;

        private EnumStarvingSingleton instance;
        ContainerHolder() {
            instance = new EnumStarvingSingleton();
        }
    }

    public static void main(String[] args) throws Exception{
        System.out.println(EnumStarvingSingleton.getInstance());
        Class<EnumStarvingSingleton> clazz = EnumStarvingSingleton.class;
        Constructor<EnumStarvingSingleton> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        EnumStarvingSingleton enumStarvingSingleton = constructor.newInstance();
        System.out.println(enumStarvingSingleton);

        System.out.println(EnumStarvingSingleton.getInstance() == enumStarvingSingleton);


        enumSingleton();
    }

    // 枚举的单例模式能够抵挡反射以及序列化的进攻，满足容器需求
    // 序列化可以参考ObjectInputStream的readObject源码
    private static void enumSingleton() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        /*
         * Exception in thread "main" java.lang.NoSuchMethodException: demo.pattern.EnumStarvingSingleton$ContainerHolder.<init>()
         * 	at java.base/java.lang.Class.getConstructor0(Class.java:3350)
         * 	at java.base/java.lang.Class.getDeclaredConstructor(Class.java:2554)
         * 	at demo.pattern.EnumStarvingSingleton.main(EnumStarvingSingleton.java:33)
         */
        Class<ContainerHolder> clazzContain = ContainerHolder.class;
//        Constructor<ContainerHolder> constructor1 = clazzContain.getDeclaredConstructor();

        /*
         * Exception in thread "main" java.lang.IllegalArgumentException: Cannot reflectively create enum objects
         * 	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:484)
         * 	at demo.pattern.EnumStarvingSingleton.enumSingleton(EnumStarvingSingleton.java:49)
         * 	at demo.pattern.EnumStarvingSingleton.main(EnumStarvingSingleton.java:33)
         */
        Constructor<ContainerHolder> constructor1 = clazzContain.getDeclaredConstructor(String.class, int.class);
        constructor1.setAccessible(true);

        System.out.println(EnumStarvingSingleton.getInstance());
        System.out.println(constructor1.newInstance());
    }
}
