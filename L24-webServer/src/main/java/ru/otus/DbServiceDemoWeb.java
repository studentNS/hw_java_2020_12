package ru.otus;

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
import ru.otus.dao.InMemoryUserDao;
import ru.otus.dao.UserDao;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/

public class DbServiceDemoWeb {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemoWeb.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
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

        UserDao userDao = new InMemoryUserDao();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, userDao, gson, templateProcessor, dbServiceClient);

        usersWebServer.start();
        usersWebServer.join();
    }
}
