package ru.otus;

import java.util.Objects;

public class HelloOtus {
    public static void main(String[] args) {
        String a = null;
        String b = "hello";

        System.out.println(Objects.equals(a, b));
    }
}
