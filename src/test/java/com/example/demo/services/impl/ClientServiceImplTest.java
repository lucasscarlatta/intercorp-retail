package com.example.demo.services.impl;

import com.example.demo.exception.NoContentException;
import com.example.demo.models.Client;
import com.example.demo.models.Statistic;
import com.example.demo.repositories.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Client service test")
@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientServiceImpl service;

    @DisplayName("Should pass when try to create a client with valid years test")
    @ParameterizedTest(name = "For example, year {0} is not supported.")
    @ValueSource(ints = { 10, 1 })
    void createValidYears(int year) {
        Client client = createClient(year);

        when(repository.save(client)).thenReturn(client);

        Client result = service.create(client);

        assertThat(result.getAge()).isEqualTo(client.getAge());
        verify(repository, times(1)).save(client);
    }

    @Test
    void statisticErrorWhenClientsIsEmpty() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(NoContentException.class, () ->
                service.statistic());

        assertThat("Client list is empty").isEqualTo(exception.getMessage());
    }

    @Test
    void getStatisticWhenClientsIsNotEmpty() {
        List<Client> clients = new ArrayList<>();
        Client client = createClient(10);
        clients.add(client);
        client = createClient(20);
        clients.add(client);
        client = createClient(30);
        clients.add(client);

        when(repository.findAll()).thenReturn(clients);

        Statistic result = service.statistic();

        assertThat(result.getAverageAge()).isEqualTo(20);
        assertThat(result.getStandardDeviation()).isEqualTo(8.16);
    }

    private Client createClient(int year) {
        Client client = new Client();
        client.setName("Lucas");
        client.setLastName("Scarlatta");
        client.setBirthday(LocalDate.now().minusYears(year));

        return client;
    }
}