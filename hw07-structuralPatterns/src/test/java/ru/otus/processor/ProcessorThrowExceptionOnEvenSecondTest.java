package ru.otus.processor;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

class ProcessorThrowExceptionOnEvenSecondTest {

    static Message message;

    @BeforeAll
    static void init() {
        message = new Message.Builder(1).build();
    }

    @Test
    @DisplayName("Тест, что процессор выбрасывает исключение!")
    void process() {
        Assertions.assertThrows(Exception.class, () -> new ProcessorThrowExceptionOnEvenSecond(() -> LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC)).process(message));
    }

    @Test
    @DisplayName("Тест, что процессор не выбрасывает исключение!")
    void processNonEven() {
        Assertions.assertEquals(message, new ProcessorThrowExceptionOnEvenSecond(() -> LocalDateTime.ofEpochSecond(1L, 0, ZoneOffset.UTC)).process(message));
    }
}
