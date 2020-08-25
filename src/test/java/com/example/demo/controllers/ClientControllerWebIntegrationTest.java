package com.example.demo.controllers;

import com.example.demo.models.Client;
import com.example.demo.models.Statistic;
import com.example.demo.services.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ClientsController.class)
public class ClientControllerWebIntegrationTest {

    private static final String CLIENTS = "/clients";
    private static final String KPID = "/kpid";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientServiceImpl service;

    @Test
    public void createClient() throws Exception {
        String name = "Lucas";
        String lastName = "Scarlatta";
        LocalDate date = LocalDate.of(1992, 10, 16);
        String stringDate = date.toString();

        Client client = new Client();
        client.setName(name);
        client.setLastName(lastName);
        client.setBirthday(date);

        given(service.create(any(Client.class))).willReturn(client);

        String content = String.format("{\"name\":\"%s\",\"lastName\":\"%s\",\"birthday\":\"%s\"}",
                name, lastName, stringDate);

        performPostClient(content)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(lastName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(client.getAge()))
                .andReturn();

        verify(service, times(1)).create(any(Client.class));
    }

    @Test
    public void createClientEmptyName() throws Exception {
        String name = "";
        String lastName = "Scarlatta";
        LocalDate date = LocalDate.of(1992, 10, 16);
        String stringDate = date.toString();

        Client client = new Client();
        client.setName(name);
        client.setLastName(lastName);
        client.setBirthday(date);

        given(service.create(any(Client.class))).willReturn(client);

        String content = String.format("{\"name\":\"%s\",\"lastName\":\"%s\",\"birthday\":\"%s\"}",
                name, lastName, stringDate);

        performPostClient(content)
                .andExpect(status().isPreconditionFailed())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("name: must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value(CLIENTS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation Error"))
                .andReturn();

        verify(service, never()).create(any(Client.class));
    }

    @Test
    public void createClientNullLastName() throws Exception {
        String name = "Lucas";
        LocalDate date = LocalDate.of(1992, 10, 16);
        String stringDate = date.toString();

        Client client = new Client();
        client.setName(name);
        client.setBirthday(date);

        given(service.create(any(Client.class))).willReturn(client);

        String content = String.format("{\"name\":\"%s\",\"birthday\":\"%s\"}",
                name, stringDate);

        performPostClient(content)
                .andExpect(status().isPreconditionFailed())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("lastName: must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value(CLIENTS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation Error"))
                .andReturn();

        verify(service, never()).create(any(Client.class));
    }

    @Test
    public void createClientBirthdayAfterToday() throws Exception {
        String name = "Lucas";
        String lastName = "Scarlatta";
        LocalDate date = LocalDate.now().plusYears(1);
        String stringDate = date.toString();

        Client client = new Client();
        client.setName(name);
        client.setBirthday(date);

        given(service.create(any(Client.class))).willReturn(client);

        String content = String.format("{\"name\":\"%s\",\"lastName\":\"%s\",\"birthday\":\"%s\"}",
                name, lastName, stringDate);

        performPostClient(content)
                .andExpect(status().isPreconditionFailed())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("birthday: must be a past date"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value(CLIENTS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation Error"))
                .andReturn();

        verify(service, never()).create(any(Client.class));
    }

    @Test
    public void getStatistics() throws Exception {
        Statistic statistic = new Statistic();
        statistic.setAverageAge(10.0);
        statistic.setStandardDeviation(10.0);

        given(service.statistic()).willReturn(statistic);

        performGetClients().andExpect(status().isOk());

        verify(service, times(1)).statistic();
    }

    private ResultActions performPostClient(String content) throws Exception {
        return mockMvc.perform(post(CLIENTS)
                .content(content)
                .contentType(APPLICATION_JSON));
    }

    private ResultActions performGetClients() throws Exception {
        return mockMvc.perform(get(CLIENTS + KPID)
                .contentType(APPLICATION_JSON));
    }
}
