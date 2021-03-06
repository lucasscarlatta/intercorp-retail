package com.example.demo.controllers;

import com.example.demo.exception.error.ApiError;
import com.example.demo.models.Client;
import com.example.demo.models.Statistic;
import com.example.demo.services.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Clients")
public class ClientsController {

    @Autowired
    private ClientService service;

    @ApiOperation(value = "Create client")
    @ApiResponse(responseCode = "201", description = "New client created")
    @ApiResponse(responseCode = "422", description = "Body validation errors", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    @RequestMapping(method = POST, value = "")
    @ResponseStatus(value = CREATED)
    public ResponseEntity<Client> create(@Valid @RequestBody Client client) {
        return new ResponseEntity<>(service.create(client), CREATED);
    }

    @ApiOperation(value = "Get clients")
    @ApiResponse(responseCode = "200", description = "Clients information")
    @ApiResponse(responseCode = "204", description = "No clients found")
    @RequestMapping(method = GET)
    public ResponseEntity<List<Client>> clients() {
        return new ResponseEntity<>(service.clients(), OK);
    }

    @ApiOperation(value = "Get statistic from clients")
    @ApiResponse(responseCode = "200", description = "Clients statistics")
    @ApiResponse(responseCode = "204", description = "No clients found")
    @RequestMapping(method = GET, value = "/kpid")
    public ResponseEntity<Statistic> statistic() {
        return new ResponseEntity<>(service.statistic(), OK);
    }
}
