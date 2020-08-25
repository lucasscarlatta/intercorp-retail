package com.example.demo.services.impl;

import com.example.demo.exception.NoContentException;
import com.example.demo.models.Client;
import com.example.demo.models.Statistic;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.services.ClientService;
import com.example.demo.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository repository;

    @Override
    public Client create(Client client) {
        LOGGER.info("Creating client.");
        return repository.save(client);
    }

    @Override
    public Statistic statistic() {
        List<Client> clients = getClients();

        return getStatistic(clients);
    }

    private List<Client> getClients() {
        List<Client> clients = repository.findAll();

        if (clients.isEmpty()) {
            throw new NoContentException("Client list is empty");
        }

        return clients;
    }

    private Statistic getStatistic(List<Client> clients) {
        int population = clients.size();
        LOGGER.info("Population: {}", population);

        double avg = clients.stream().mapToDouble(Client::getAge).average().orElse(0);
        LOGGER.info("Average: {}", avg);

        double deviation = clients.stream().mapToDouble(c -> Math.pow(c.getAge() - avg, 2)).sum();
        LOGGER.info("Deviation: {}", deviation);

        double variance = deviation / population;
        LOGGER.info("Variance: {}", variance);

        double standardDeviation = Math.sqrt(variance);
        LOGGER.info("Standard deviation: {}", standardDeviation);

        return createStatistic(avg, standardDeviation);
    }

    private Statistic createStatistic(double avg, double standardDeviation) {
        Statistic statistic = new Statistic();
        statistic.setAverageAge(Util.decimalTwoDigits(avg));
        statistic.setStandardDeviation(Util.decimalTwoDigits(standardDeviation));

        return statistic;
    }
}
