package com.example.demo.services;

import com.example.demo.models.Client;
import com.example.demo.models.Statistic;

import java.util.List;

public interface ClientService {

    Client create(Client client);

    List<Client> clients();

    Statistic statistic();
}
