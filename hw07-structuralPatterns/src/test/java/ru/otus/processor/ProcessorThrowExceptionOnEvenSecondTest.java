package ru.otus.processor;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

class ProcessorThrowExceptionOnEvenSecondTest {

    @Test
    @DisplayName("Тест, что процессор выбрасывает исключение!")
    void process() {
        long future = System.currentTimeMillis() + 2000;
        Assertions.assertThrows(Exception.class, () -> {
            while (System.currentTimeMillis() < future) {
                new ProcessorThrowExceptionOnEvenSecond().process(new Message.Builder(1).build());
            }
        });
    }
}