package com.example.demo.services.impl;

import com.example.demo.models.Client;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository repository;

    @Override
    public Client create(Client client) {
        return repository.save(client);
    }
}
