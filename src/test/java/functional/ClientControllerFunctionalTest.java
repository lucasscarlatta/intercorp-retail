package functional;

import com.example.demo.DemoApplication;
import com.example.demo.exception.error.ApiError;
import com.example.demo.models.Client;
import com.example.demo.models.Statistic;
import com.example.demo.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DemoApplication.class)
@ActiveProfiles("test")
public class ClientControllerFunctionalTest {

    private final String baseURL;

    private static final String CLIENTS = "/clients";
    private static final String KPID = "/kpid";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClientRepository clientRepository;

    public ClientControllerFunctionalTest() throws UnknownHostException {
        String host = InetAddress.getLocalHost().getHostAddress();
        baseURL = "http://" + host + ":";
    }

    @BeforeEach
    public void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    public void shouldReturn201CodeWhenCreateClient() {
        String name = "Lucas";
        String lastName = "Scarlatta";
        int age = 21;
        Client client = createClient(name, lastName, age);

        ResponseEntity<Client> response = this.restTemplate
                .postForEntity(baseURL + port + CLIENTS, client, Client.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(name);
        assertThat(response.getBody().getLastName()).isEqualTo(lastName);
        assertThat(response.getBody().getAge()).isEqualTo(age);
    }

    @Test
    public void shouldReturn412CodeWhenClientNameAndLastNameAreEmpty() {
        String name = "";
        String lastName = "";
        int age = 21;
        Client client = createClient(name, lastName, age);

        ResponseEntity<ApiError> response = this.restTemplate
                .postForEntity(baseURL + port + CLIENTS, client, ApiError.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(PRECONDITION_FAILED);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getCode()).isEqualTo(PRECONDITION_FAILED.value());
        assertThat(body.getStatus()).isEqualTo(PRECONDITION_FAILED);
        assertThat(body.getMessage()).isEqualTo("Validation Error");
        assertThat(body.getPath()).isEqualTo(CLIENTS);
        assertThat(body.getErrors().stream().filter(e -> e.equals("name: must not be blank")).count()).isEqualTo(1);
        assertThat(body.getErrors().stream().filter(e -> e.equals("lastName: must not be blank")).count()).isEqualTo(1);
    }

    @Test
    public void shouldReturn412CodeWhenClientNameAndLastNameAreNull() {
        int age = 21;
        Client client = createClient(null, null, age);

        ResponseEntity<ApiError> response = this.restTemplate
                .postForEntity(baseURL + port + CLIENTS, client, ApiError.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(PRECONDITION_FAILED);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getCode()).isEqualTo(PRECONDITION_FAILED.value());
        assertThat(body.getStatus()).isEqualTo(PRECONDITION_FAILED);
        assertThat(body.getMessage()).isEqualTo("Validation Error");
        assertThat(body.getPath()).isEqualTo(CLIENTS);
        assertThat(body.getErrors().stream().filter(e -> e.equals("name: must not be blank")).count()).isEqualTo(1);
        assertThat(body.getErrors().stream().filter(e -> e.equals("lastName: must not be blank")).count()).isEqualTo(1);
    }

    @Test
    public void shouldReturn412CodeWhenClientBirthdayIsInTheFuture() {
        String name = "Lucas";
        String lastName = "Scarlatta";
        int age = -21;
        Client client = createClient(name, lastName, age);

        ResponseEntity<ApiError> response = this.restTemplate
                .postForEntity(baseURL + port + CLIENTS, client, ApiError.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(PRECONDITION_FAILED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(PRECONDITION_FAILED.value());
        assertThat(response.getBody().getStatus()).isEqualTo(PRECONDITION_FAILED);
        assertThat(response.getBody().getMessage()).isEqualTo("Validation Error");
        assertThat(response.getBody().getErrors().get(0)).isEqualTo("birthday: must be a past date");
    }

    @Test
    public void shouldReturn204CodeWhenClientsListIsEmpty() {
        ResponseEntity<Statistic> response = this.restTemplate
                .getForEntity(baseURL + port + CLIENTS + KPID, Statistic.class);
        assertThat(response.getStatusCode()).isEqualByComparingTo(NO_CONTENT);
        assertThat(response.getBody()).isNull();
   }

    @Test
    public void shouldReturn200CodeWhenClientsListExist() {
        List<Client> clients = new ArrayList<>(Arrays.asList(
                createClient("Lucas", "Scarlatta", 21),
                createClient("Carla", "Lujan", 9),
                createClient("Juan", "Perez", 33)));

        clientRepository.saveAll(clients);

        ResponseEntity<Statistic> response = this.restTemplate
                .getForEntity(baseURL + port + CLIENTS + KPID, Statistic.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualByComparingTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAverageAge()).isEqualTo(21);
        assertThat(response.getBody().getStandardDeviation()).isEqualTo(9.8);
    }

    private Client createClient(String name, String lastName, int age) {
        Client client = new Client();
        client.setName(name);
        client.setLastName(lastName);
        client.setBirthday(LocalDate.now().minusDays(1).minusYears(age));

        return client;
    }
}
