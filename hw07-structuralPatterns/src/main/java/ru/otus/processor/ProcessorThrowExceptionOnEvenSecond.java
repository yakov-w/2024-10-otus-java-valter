package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalTime;

public class ProcessorThrowExceptionOnEvenSecond implements Processor {
    @Override
    public Message process(Message message) {
        return process(message, LocalTime.now().getSecond());
    }

    public Message process(Message message, int seconds) {
        if (seconds % 2 == 0) {
            throw new RuntimeException("Even!!!");
        }
        return message;
    }
}
