package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.AddressDataSet;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.PhoneDataSet;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientCacheImpl;
import ru.otus.crm.service.DbServiceClientImpl;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, AddressDataSet.class, PhoneDataSet.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///

        HwCache<String, Client> cache = new MyCache<>();

        log.info("Without cache");
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        time(dbServiceClient);

        log.info("With cache");
        var cacheDbServiceClient = new DbServiceClientCacheImpl(dbServiceClient, cache);
        time(cacheDbServiceClient);

    }

    private static void time(DBServiceClient dbServiceClient){
        long startTime = System.currentTimeMillis();
        dbServiceClient.findAll().forEach(tempClient ->{
            var client = dbServiceClient.getClient(tempClient.getId());
        });
        long estimatedTime = System.currentTimeMillis() - startTime;
        log.info("Time elapsed: {}ms", estimatedTime);
    }

}
