package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.AddressDataSet;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.PhoneDataSet;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.ArrayList;

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
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        var clientFirstData = new Client("dbServiceFirst");
        clientFirstData.setAddress(new AddressDataSet("First street"));
        var phones1 = new ArrayList<PhoneDataSet>();
        phones1.add(new PhoneDataSet("Phone 1",clientFirstData));
        phones1.add(new PhoneDataSet("Phone 2",clientFirstData));
        clientFirstData.setPhone(phones1);
        dbServiceClient.saveClient(clientFirstData);

        var clientSecondData = new Client("dbServiceSecond");
        clientSecondData.setAddress(new AddressDataSet("Second street"));
        var phones2 = new ArrayList<PhoneDataSet>();
        phones2.add(new PhoneDataSet("Phone 3",clientSecondData));
        phones2.add(new PhoneDataSet("Phone 4",clientSecondData));
        clientSecondData.setPhone(phones2);
        dbServiceClient.saveClient(clientSecondData);

        var clientSecondSelected = dbServiceClient.getClient(clientSecondData.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondData.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
///
        /*dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated", clientSecondSelected.getAddress(),
                clientSecondSelected.getPhone()));

        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));*/
        clientSecondSelected.setName("New name");
        clientSecondSelected.setAddress(new AddressDataSet("New address"));
        dbServiceClient.saveClient(clientSecondSelected);
        log.info("clientUpdated:{}", clientSecondSelected);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}
