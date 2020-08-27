package functional;

import com.example.demo.DemoApplication;
import com.example.demo.exception.error.ApiError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DemoApplication.class)
@ActiveProfiles("test")
@DisplayName("Error handler controller functional test")
public class ErrorHandlerControllerFunctionalTest {

    private final String baseURL;

    private static final String ERROR = "/error";
    private static final String OTHER_URL = "/url";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    public ErrorHandlerControllerFunctionalTest() throws UnknownHostException {
        String host = InetAddress.getLocalHost().getHostAddress();
        baseURL = "http://" + host + ":";
    }

    @DisplayName("Should return 404 status code when call error path")
    @Test
    public void shouldReturn404CodeWhenCallErrorPath() {
        ResponseEntity<ApiError> response = this.restTemplate
                .getForEntity(baseURL + port + ERROR, ApiError.class);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getCode()).isEqualTo(NOT_FOUND.value());
        assertThat(body.getStatus()).isEqualTo(NOT_FOUND);
        assertThat(body.getMessage()).isEqualTo("Path not found");
        assertThat(body.getPath()).isEqualTo(ERROR);
    }

    @DisplayName("Should return 404 status code when call not exist path")
    @Test
    public void shouldReturn404CodeWhenCallNonExistPath() {
        ResponseEntity<ApiError> response = this.restTemplate
                .getForEntity(baseURL + port + OTHER_URL, ApiError.class);
        assertThat(response.getStatusCode()).isEqualByComparingTo(NOT_FOUND);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getCode()).isEqualTo(NOT_FOUND.value());
        assertThat(body.getStatus()).isEqualTo(NOT_FOUND);
        assertThat(body.getMessage()).isEqualTo("Path not found");
        assertThat(body.getPath()).isEqualTo(ERROR);
    }
}
