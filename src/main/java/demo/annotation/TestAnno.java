package demo.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@CourseInfoAnnotation(courseName = "courseName", courseTag = "courseTag", courseProfile = "courseProfile", courseIndex = 120)
public class TestAnno {

    @PersonInfoAnnotation(name = "zhangsan", language = {"java", "c++"})
    private String author;

    @CourseInfoAnnotation(courseName = "courseNameMethod", courseTag = "courseTagMethod", courseProfile = "courseProfileMethod", courseIndex = 120)
    public void getCourseInfo() {

    }

    /**
     * -Djdk.proxy.ProxyGenerator.saveGeneratedFiles=true
     * -XX:+TraceClassLoading // 跟踪类加载
     */
    public static void main(String[] args) throws Exception{
//        TestAnno testAnno = new TestAnno();
        Class<?> aClass = Class.forName("demo.annotation.TestAnno");
//        classAnno(aClass);

        fieldsAnno(aClass);

//        methodsAnno(aClass);
    }

    private static void methodsAnno(Class<?> aClass) {
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            // 判断成员变量是否有对应的注解
            if (declaredMethod.isAnnotationPresent(CourseInfoAnnotation.class)) {
                CourseInfoAnnotation annotation = declaredMethod.getAnnotation(CourseInfoAnnotation.class);
                System.out.println(annotation);
            }
        }
    }

    private static void fieldsAnno(Class<?> aClass) {
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            // 判断成员变量是否有对应的注解
            if (declaredField.isAnnotationPresent(PersonInfoAnnotation.class)) {
                PersonInfoAnnotation annotation = declaredField.getAnnotation(PersonInfoAnnotation.class);
                System.out.println(annotation);
            }
        }
    }

    private static Class<?> classAnno(Class<?> aClass) throws ClassNotFoundException {
        Annotation[] annotations = aClass.getAnnotations();
        for (Annotation annotation : annotations) {
            CourseInfoAnnotation annotation1 = (CourseInfoAnnotation) annotation;
            System.out.println(annotation1);
        }
        return aClass;
    }
}
