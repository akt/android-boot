package com.ak.demo;

public class Test {

    public static void crashInJava() {
        String nullStr = "1";
        if (nullStr.equals("1")) {
            nullStr = null;
        }
        nullStr.equals("");
    }

}
