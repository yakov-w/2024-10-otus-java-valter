package ru.otus.exception;

public class ComponentNotFound extends Exception {
    public ComponentNotFound() {
        super("В контексте нет такого компонента!");
    }
}
