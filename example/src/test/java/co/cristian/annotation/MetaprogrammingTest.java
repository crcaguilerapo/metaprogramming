package co.cristian.annotation;

import co.cristian.Annotation1;
import co.cristian.Annotation2;
import co.cristian.Operation;
import co.cristian.PersonDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetaprogrammingTest {
    @BeforeAll
    public void setUp() {

    }

    @Test
    public void test1() {
        var person = new PersonDto("Cristian", "Aguilera");
        var isPresent = person.getClass().isAnnotationPresent(Annotation1.class);
        if (isPresent) {
            System.out.println("add logic");
        }
    }

    @Test
    public void test2() throws IllegalAccessException {
        var person = new PersonDto("Cristian", "Aguilera");
        var annotation = person.getClass().getAnnotation(Annotation1.class);
        if (annotation.type().equals("map")) {
            Field[] fields = person.getClass().getDeclaredFields();

            Map dict = new HashMap<String, Object>();

            for (Field field : fields) {
                field.setAccessible(true);
                Object name = field.getName();
                Object value = field.get(person);
                dict.put(name, value);
            }
            System.out.println(dict);
        }
    }

    @Test
    public void test3() throws InvocationTargetException, IllegalAccessException {
        Method[] methods = Operation.class.getMethods();
        String data = "";

        for (Method method : methods) {
            if (method.isAnnotationPresent(Annotation2.class)) {
                System.out.println("Method: " + method.getName());
                data = (String) method.invoke(null, data);
            }
        }

        System.out.println(data);
    }

    @Test
    public void test4() {
        final Map myMap = new HashMap<String, Integer>();

        //Dynamic Proxy
        Map proxyInstance = (Map) Proxy.newProxyInstance(
                null,
                //Interfaces
                new Class[] { Map.class },
                //Handler
                (proxy, method, args) -> {
                    System.out.println("LOG: before execute " + method.getName());
                    Object result = method.invoke(myMap, args);
                    System.out.println("LOG: after execute " + method.getName());
                    return result;
                }
        );

        proxyInstance.put(1, "hello world");
        proxyInstance.get(1);
        System.out.println(proxyInstance.size());
    }

    @Test
    public void test5() {

        //Dynamic Proxy
        Map proxyInstance = (Map) Proxy.newProxyInstance(
                null,
                //Interfaces
                new Class[] { Map.class },
                //Handler
                new InvocationHandler() {
                    Map myMap = new HashMap<String, Integer>();

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        long start = System.nanoTime();
                        Object result = method.invoke(myMap, args);
                        long elapsed = System.nanoTime() - start;

                        System.out.println(
                                "Executing %s finished in %d ns"
                                        .formatted(method.getName(), elapsed)
                        );

                        return result;
                    }
                }
        );

        proxyInstance.put(1, "hello world");
        proxyInstance.put(2, "hello world");
        proxyInstance.get(1);
        proxyInstance.get(2);
        System.out.println(proxyInstance.size());
    }

    @Test
    public void test6() {

        //Dynamic Proxy
        Map proxyInstance = (Map) Proxy.newProxyInstance(
                null,
                //Interfaces
                new Class[] { Map.class },
                //Handler
                new InvocationHandler() {
                    Map myMap = new HashMap<String, Integer>();
                    Integer count = 0;

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object result = method.invoke(myMap, args);
                        if (method.getName().equals("get")) {
                            count++;
                            System.out.println(count);
                        }
                        return result;
                    }
                }
        );

        proxyInstance.put(1, "hello world");
        proxyInstance.put(2, "hello world");
        proxyInstance.get(1);
        proxyInstance.get(2);
        proxyInstance.get(2);
    }

}
