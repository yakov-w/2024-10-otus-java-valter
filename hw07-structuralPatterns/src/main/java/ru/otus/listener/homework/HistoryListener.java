package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class HistoryListener implements Listener, HistoryReader {

    private static final Map<Long, Message.Snapshot> history = new TreeMap<>(Comparator.reverseOrder());

    @Override
    public void onUpdated(Message msg) {
        history.put(msg.getId(), msg.save());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        // чую что-то я не делаю
        return Optional.of(history.get(id).restore());
    }
}
