package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorThrowExceptionOnEvenSecond implements Processor {

    private final DateTimeProvider dateTime;

    public ProcessorThrowExceptionOnEvenSecond(DateTimeProvider dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public Message process(Message message) {
        int second = dateTime.getDate().getSecond();
        if (second % 2 == 0) {
            throw new RuntimeException("Even!!!");
        }
        return message;
    }
}
