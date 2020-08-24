package com.example.demo.services.impl;

import com.example.demo.models.Client;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
