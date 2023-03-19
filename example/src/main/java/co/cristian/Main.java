package co.cristian;


import java.util.HashMap;
import java.util.Map;


public class Main {

    static void hello() {
         Map myMap = new HashMap();
         myMap.put("message", "hello");
    }

    static void hello1() {
        Map myMap = new HashMap();
        myMap.put("message", "saludo");
    }
    public static void main(String[] args) {
        hello();
        hello1();
    }
}