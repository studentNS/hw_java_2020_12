package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.AddressDataSet;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.PhoneDataSet;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;


import java.io.IOException;
import java.util.*;


public class ClientsServlet extends HttpServlet {

    private static final String PAGE_TEMPLATE = "clients.html";
    private static final String USER_KEY = "clients";

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        var clients = dbServiceClient.findAll();
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, Map.of(USER_KEY, clients)));
/*
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, Collections.emptyMap()));*/

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {

        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String phones = req.getParameter("phones");

        Client client = new Client();
        client.setName(name);
        client.setAddress(new AddressDataSet(address));
        var phonesArr = new ArrayList<PhoneDataSet>();
        phonesArr.add(new PhoneDataSet(phones, client));
        client.setPhone(phonesArr);

        dbServiceClient.saveClient(client);

        response.sendRedirect("/clients");
    }

}
