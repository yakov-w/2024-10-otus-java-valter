package ru.otus.processor;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

class ProcessorThrowExceptionOnEvenSecondTest {

    @Test
    @DisplayName("Тест, что процессор выбрасывает исключение!")
    void process() {
        Assertions.assertThrows(Exception.class, () -> new ProcessorThrowExceptionOnEvenSecond().process(new Message.Builder(1).build(), 2));
    }

    @Test
    @DisplayName("Тест, что процессор не выбрасывает исключение!")
    void processNonEven() {
        Message message = new Message.Builder(1).build();
        Assertions.assertEquals(message, new ProcessorThrowExceptionOnEvenSecond().process(message,1 ));
    }
}