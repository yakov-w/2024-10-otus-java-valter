package ru.otus.processor;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.Instant;
import java.time.ZoneOffset;

class ProcessorThrowExceptionOnEvenSecondTest {

    @Test
    @DisplayName("Тест, что процессор выбрасывает исключение!")
    void process() {
        Assertions.assertThrows(Exception.class, () -> new ProcessorThrowExceptionOnEvenSecond(Instant.ofEpochSecond(2).atZone(ZoneOffset.UTC).toLocalDateTime()).process(new Message.Builder(1).build()));
    }

    @Test
    @DisplayName("Тест, что процессор не выбрасывает исключение!")
    void processNonEven() {
        Message message = new Message.Builder(1).build();
        Assertions.assertEquals(message, new ProcessorThrowExceptionOnEvenSecond(Instant.ofEpochSecond(1).atZone(ZoneOffset.UTC).toLocalDateTime()).process(message));
    }
}