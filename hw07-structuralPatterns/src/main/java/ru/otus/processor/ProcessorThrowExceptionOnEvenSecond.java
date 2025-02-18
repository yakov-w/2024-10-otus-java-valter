package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class ProcessorThrowExceptionOnEvenSecond implements Processor {

    private final LocalDateTime dateTime;

    public ProcessorThrowExceptionOnEvenSecond(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public Message process(Message message) {
        if (dateTime.getSecond() % 2 == 0) {
            throw new RuntimeException("Even!!!");
        }
        return message;
    }
}
