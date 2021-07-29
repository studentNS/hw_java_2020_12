package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientCacheImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientCacheImpl.class);

    DBServiceClient dbServiceClient;
    private final HwCache<String, Client> cache;

    public DbServiceClientCacheImpl(DBServiceClient dbServiceClient, HwCache<String, Client> cache) {
        this.dbServiceClient = dbServiceClient;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        var dbClient = dbServiceClient.saveClient(client);
        cache.put(dbClient.getId().toString(), dbClient);
        return dbClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var client  = Optional.ofNullable(cache.get(getCacheKey(id)));
        if (!client.isPresent()){
            client = dbServiceClient.getClient(id);
            client.ifPresent(value -> cache.put(value.getId().toString(), value));
        }
        return client;
    }

    private String getCacheKey(long id) {
        return String.valueOf(id);
    }

    @Override
    public List<Client> findAll() {
        var clients = dbServiceClient.findAll();
        clients.forEach(client -> cache.put(client.getId().toString(), client));
        return clients;
    }
}
