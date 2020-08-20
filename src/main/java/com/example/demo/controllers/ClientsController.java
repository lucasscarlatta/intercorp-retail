package com.example.demo.controllers;

import com.example.demo.models.Client;
import com.example.demo.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientsController {

    @Autowired
    private ClientService service;

    @RequestMapping(method = POST, value = "")
    public ResponseEntity<Client> create(@Valid @RequestBody Client client) {
        return new ResponseEntity<Client>(service.create(client), CREATED);
    }
}
