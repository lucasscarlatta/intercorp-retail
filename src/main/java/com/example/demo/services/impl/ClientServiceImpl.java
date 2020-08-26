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

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Random;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    private static final Integer FIRST_MONTH = 1;
    private static final Integer LAST_MONTH = 12;
    private static final Integer FIRST_DAY = 1;
    private static final Integer LAST_DAY = 31;

    private static final Integer LIFE_EXPECTANCY = 75;
    public static final Integer YOUNG = LIFE_EXPECTANCY - 8;
    public static final Integer OLD = LIFE_EXPECTANCY + 8;
    public static final Integer EVEN_OLD = OLD + 3;

    @Autowired
    private ClientRepository repository;

    @Override
    public Client create(Client client) {
        LOGGER.info("Creating client.");
        generateDateOfDead(client);
        return repository.save(client);
    }

    @Override
    public List<Client> clients() {
        return getClients();
    }

    @Override
    public Statistic statistic() {
        List<Client> clients = getClients();

        return getStatistic(clients);
    }

    private void generateDateOfDead(Client client) {
        Integer age = client.getAge();
        Random r = new Random();
        LocalDate today = LocalDate.now();
        int result;
        if (age <= YOUNG) {
            result = r.nextInt(OLD - YOUNG) + YOUNG;
        } else if (age >= OLD) {
            result = r.nextInt(age + 3 - age) + age;
        } else {
            result = r.nextInt(EVEN_OLD - age) + age;
        }
        int month = r.nextInt(LAST_MONTH - FIRST_MONTH) + FIRST_MONTH;
        int day = r.nextInt(LAST_DAY - FIRST_DAY) + FIRST_DAY;
        int year = result - age > 0 ? result - age : 1;
        client.setDateOfDeath(today.plusYears(year).withMonth(month).plusDays(day));

        LOGGER.info("Date of dead: {}. Age of dead: {}", client.getDateOfDeath(),
                Period.between(client.getBirthday(), client.getDateOfDeath()).getYears());
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
