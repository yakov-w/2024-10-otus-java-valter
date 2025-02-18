package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.processor.ProcessorSwitcherField11AndField12;
import ru.otus.processor.ProcessorThrowExceptionOnEvenSecond;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var processors = List.of(new ProcessorSwitcherField11AndField12(), new ProcessorThrowExceptionOnEvenSecond(LocalDateTime.now()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {
        });
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(historyListener);

    }
}
