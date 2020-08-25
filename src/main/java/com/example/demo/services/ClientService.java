package com.example.demo.services;

import com.example.demo.models.Client;
import com.example.demo.models.Statistic;

public interface ClientService {

    Client create(Client client);

    Statistic statistic();
}
