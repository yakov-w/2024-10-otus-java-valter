package ru.otus.services.processors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    BlockingQueue<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        dataBuffer = new LinkedBlockingQueue<>(bufferSize);
    }

    @Override
    public void process(SensorData data) {
        dataBuffer.offer(data);
        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    // Если я все правильно понимаю,на этом методе могут заблокироваться хоть все потоки
    // dataProcessThreadPool из SensorDataProcessingFlowImpl.
    // И соответственно, первый запишет "полный" набор данных, а остальные скорей всего по несколько документов.
    // Смотреть на размер очереди не вариант т.к. метод может быть вызван в onProcessingEnd при "завершении" приложения,
    // а значит нужно обработать все что есть в очереди.
    // Тут вот какой вопрос: Как правильно разрешать такие ситуации?
    public synchronized void flush() {
        List<SensorData> bufferedData = new ArrayList<>();

        while (!dataBuffer.isEmpty()) {
            bufferedData.add(dataBuffer.poll());
        }

        if (bufferedData.isEmpty()) {
            return;
        }

        bufferedData.sort(Comparator.comparing(SensorData::getMeasurementTime));

        try {
            writer.writeBufferedData(bufferedData);
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
