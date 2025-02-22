package ru.otus.crm.service;

import org.hibernate.CacheMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate, MyCache cache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            session.setCacheMode(CacheMode.IGNORE);
            var clientCloned = client.clone();
            Client savedClient;
            if (client.getId() == null) {
                savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
            } else {
                savedClient = clientDataTemplate.update(session, clientCloned);
                log.info("updated client: {}", savedClient);
            }
            cache.put(savedClient.getId().toString(), savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        if (cache != null) {
            Client client = cache.get(Long.toString(id));
            if (client != null) {
                return Optional.of(client);
            }
        }

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // TODO если нет то надо положить
        return transactionManager.doInReadOnlyTransaction(session -> {
            session.setCacheMode(CacheMode.IGNORE);
            var clientOptional = clientDataTemplate.findById(session, id);
            cache.put(clientOptional.get().getId().toString(), clientOptional.get());
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
