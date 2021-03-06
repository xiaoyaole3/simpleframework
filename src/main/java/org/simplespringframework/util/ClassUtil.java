package org.simplespringframework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ClassUtil {

    public static final String FILE_PROTOCOL = "file";

    /**
     * 获取包下类的集合
     * @param packageName 包名
     * @return 类集合
     */
    public static Set<Class<?>> extractPackageClass(String packageName) {
        /*
         * 1. 获取到类的加载器
         * 	> 目的：
         * 	> 获取项目发布的实际路径
         */
        ClassLoader classLoader = getClassLoader();
        // 2. 通过类加载器获取到加载的资源信息
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url == null) {
            log.warn("Unable to retrieve anything from package : " + packageName);
            return null;
        }
        // 3. 依据不同的资源类型，采用不同的方式获取资源的集合
        Set<Class<?>> classSet = null;
        // 过滤出文件类型的资源
        if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
            classSet = new HashSet<>();
            File packageDirectory = new File(url.getPath());
            extractClassFile(classSet, packageDirectory, packageName);
        }

        // TODO : 此处可以加入针对其他类型资源的处理
        return classSet;
    }

    /**
     * 递归获取目标package里面的所有class文件（包括子package里的class文件）
     * @param emptyClassSet 壮哉目标类的集合
     * @param fileSource 文件或者目录
     * @param packageName 包名
     * @return 类集合
     */
    private static void extractClassFile(Set<Class<?>> emptyClassSet, File fileSource, String packageName) {
        if (!fileSource.isDirectory()) {
            return;
        }

        // 如果是一个文件夹，则调用其listFiles获取文件夹下的文件或文件夹
        File[] files = fileSource.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    // 获取文件的绝对值路径
                    String absolutePath = file.getAbsolutePath();
                    if (absolutePath.endsWith(".class")) {
                        // 若是class文件，则直接加载
                        addToClassSet(absolutePath);
                    }
                }
                return false;
            }

            /**
             * 根据class文件的绝对值路径，获取并生成class对象，并放入classSet中
             * @param absolutePath 绝对值路径
             */
            private void addToClassSet(String absolutePath) {
                // 如 /Users/...../target/classes/com/wander/entity/dto/MainPageInfoDTO.class
                // 需要弄成com.wander.entity.dto.MainPageInfoDTO

                // 1. 从class文件的绝对值陆经理提取出包含了package的类名
                absolutePath = absolutePath.replace(File.separator, ".");
                String className = absolutePath.substring(absolutePath.indexOf(packageName));
                className = className.substring(0, className.lastIndexOf("."));
                // 2. 通过反射机制获取对饮的Class对象并加入到classSet里
                Class<?> targetClass = loadClass(className);
                emptyClassSet.add(targetClass);

            }
        });

        if (files != null) {
            for (File file : files) {
                extractClassFile(emptyClassSet, file, packageName);
            }
        }
    }

    /**
     * 获取class对象
     * @param className class全名= package + 类名
     * @return Class
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error:", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取classLoader
     * @return 当前的ClassLoader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 实例化class，这里为了简单，暂时不支持参数的构造函数
     * @param clazz Class
     * @param <T> class的类型
     * @param accessible 是否支持创建出私有class对象的实例
     * @return 类的实例化
     */
    public static <T> T newInstance(Class<?> clazz, boolean accessible) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(accessible);
            return ((T) constructor.newInstance());
        } catch (Exception e) {
            log.error("New instance error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置类的属性值
     * @param field 成员变量
     * @param target 类实例
     * @param value 成员变量的值
     * @param isAccessible 是否允许设置私有属性
     */
    public static void setField(Field field, Object target, Object value, boolean isAccessible) {
        field.setAccessible(isAccessible);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            log.error("SetField failed.", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        extractPackageClass("com.wander.entity");
    }
}
