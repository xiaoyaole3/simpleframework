package demo.reflect;

import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
public class ReflectTarget {
    ReflectTarget(String str) {
        System.out.println("str");
    }

    public ReflectTarget() {
        System.out.println("none");
    }

    public ReflectTarget(char name) {
        System.out.println("none");
    }

    public ReflectTarget(String name, int index) {
        System.out.println("name index");
    }

    protected ReflectTarget(boolean n) {
        System.out.println("protected");
    }

    private ReflectTarget(int index) {
        System.out.println("private");
    }

    // Field
    public String name;
    protected int index;
    char type;
    private String targetInfo;

    // Method
    public void show1(String s) {
        System.out.println("public s" + s);
    }

    protected void show2() {
        System.out.println("show2 ");
    }

    void show3() {
        System.out.println("show3 ");
    }

    private void show4(int index) {
        System.out.println("show4 " + index);
    }



    public static void main(String[] args) throws Exception {
//        getConstructor();
//        getFields();
        getMethods();

//       getClass();

    }

    private static void getConstructor() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class clazz = Class.forName("demo.reflect.ReflectTarget");
        // 1. 获取所有的公有构造方法
        Constructor[] conArray = clazz.getConstructors();
        for (Constructor constructor : conArray) {
            System.out.println(constructor);
        }
        // 2. 获取所有构造方法
        Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        // 3. 获取单个带参数的构造方法
        Constructor constructor = clazz.getConstructor(String.class, int.class);
        System.out.println(constructor);
        // 3. 获取单个私有的构造方法
        Constructor privateCon = clazz.getDeclaredConstructor(int.class);
        System.out.println(privateCon);

        // 调用私有构造方法创建实例
        privateCon.setAccessible(true);
        ReflectTarget target = (ReflectTarget) privateCon.newInstance(1);
    }

    private static void getFields() throws Exception {
        Class<?> aClass = Class.forName("demo.reflect.ReflectTarget");

        // 1. 获取所有公有字段
        Field[] fields = aClass.getFields();
        // 2. 获取所有字段
        Field[] declaredFields = aClass.getDeclaredFields();
        // 3. 获取单个
        Field name = aClass.getField("name");
        ReflectTarget newInstance = (ReflectTarget) aClass.getConstructor().newInstance();
        name.set(newInstance, "reflect result");
        System.out.println(newInstance);
        // 4. 获取单个私有
        Field targetInfo = aClass.getDeclaredField("targetInfo");
        targetInfo.setAccessible(true);
        targetInfo.set(newInstance, "123123123");
        System.out.println(newInstance);
    }

    private static void getMethods() throws Exception {
        Class<?> aClass = Class.forName("demo.reflect.ReflectTarget");
        Method[] methods = aClass.getMethods();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        Method show1 = aClass.getMethod("show1", String.class);
        ReflectTarget newInstance = (ReflectTarget) aClass.getConstructor().newInstance();
        show1.invoke(newInstance, "method 1");
        Method show4 = aClass.getDeclaredMethod("show4", int.class);
        show4.setAccessible(true);
        show4.invoke(newInstance, 5);
    }

//    private static void getClass() throws ClassNotFoundException {
//        // 3 种获取对象的方式
////        ReflectTarget target1 = new ReflectTarget();
////        Class targetClass1 = target1.getClass();
////
////        Class<ReflectTarget> targetClass2 = ReflectTarget.class;
////        Class<?> targetClass3 = Class.forName("demo.reflect.ReflectTarget");
////
////        System.out.println(targetClass1 == targetClass2);
////        System.out.println(targetClass2 == targetClass3);
//    }
}
